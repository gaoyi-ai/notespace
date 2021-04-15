---
title: Reasons why SELECT is bad for SQL performance
categories:
- DB
- Optimization
tags:
- select
date: 2021/4/14 20:00:14
updated: 2021/4/14 12:00:14
---



> [tanelpoder.com](https://tanelpoder.com/posts/reasons-why-select-star-is-bad-for-sql-performance/)

Here’s a list of reasons why `SELECT *` is bad for SQL performance, assuming that your application doesn’t actually need all the columns. When I write production code, I explicitly specify the columns of interest in the select-list (projection), not only for performance reasons, but also for application reliability reasons. For example, will your application’s data processing code suddenly break when a new column has been added or the column order has changed in a table?

I’ll focus only on the SQL performance aspects in this article. I’m using examples based on Oracle, but most of this reasoning applies to other modern relational databases too.

#### Index

1.  [Increased network traffic](#increased-network-traffic)
2.  [Increased CPU usage on client side](#increased-cpu-usage-on-client-side)
3.  [Some query plan optimizations not possible](#some-query-plan-optimizations-not-possible)
4.  [Server-side memory usage](#server-side-memory-usage)
5.  [Increased CPU usage on server side](#increased-cpu-usage-on-server-side)
6.  [Hard parsing/optimization takes more time](#hard-parsingoptimization-takes-more-time)
7.  [Cached cursors take more memory in shared pool](#cached-cursors-take-more-memory-in-shared-pool)
8.  [LOB Fetching](#lob-fetching)
9.  [Summary](#summary)

#### Increased network traffic

This is the most obvious effect - if you’re returning 800 columns instead of 8 columns from every row, you could end up sending 100x more bytes over the network for every query execution (your mileage may vary depending on the individual column lengths, of course). More network bytes means more network packets sent and depending on your RDBMS implementation, also more app-DB network roundtrips.

Oracle can stream result data _of a single fetch call_ back to client in multiple consecutive SQL*Net packets sent out in a burst, without needing the client application to acknowledge every preceding packet first. The throughput of such bursts depends on TCP send buffer size and of course the network link bandwidth and latency. Read more about the [SQL*Net more data to client](https://tanelpoder.com/2008/02/10/sqlnet-message-to-client-vs-sqlnet-more-data-to-client/) wait event.

```
SQL> SET AUTOT TRACE STAT
SQL> SELECT * FROM soe_small.customers;

1699260 rows selected.

Elapsed: 00:01:35.82

Statistics
----------------------------------------------------------
          0  recursive calls
          0  db block gets
      45201  consistent gets
          0  physical reads
          0  redo size
  169926130  bytes sent via SQL*Net to client
     187267  bytes received via SQL*Net from client
      16994  SQL*Net roundtrips to/from client
          0  sorts (memory)
          0  sorts (disk)
    1699260  rows processed


```

It took around 1 min 35 seconds and 169 MB of data was sent from the database back to the client (roughly 100 bytes per row, on average). Interestingly, the rough row length estimate from data dictionary stats shows that an average row size ought to be 119 bytes (116 plus 3 bytes for the row header, lock byte & column count):

```
SQL> SELECT COUNT(*),SUM(avg_col_len) FROM dba_tab_columns 
     WHERE owner = 'SOE_SMALL' AND table_name = 'CUSTOMERS';

  COUNT(*) SUM(AVG_COL_LEN)
---------- ----------------
        16              116


```

The above table has only 16 columns, now let’s just select 3 columns that my application needs:

```
SQL> SELECT customer_id, credit_limit, customer_since FROM soe_small.customers;

1699260 rows selected.

Elapsed: 00:00:43.20

Statistics
----------------------------------------------------------
          0  recursive calls
          0  db block gets
      45201  consistent gets
          0  physical reads
          0  redo size
   31883155  bytes sent via SQL*Net to client
     187307  bytes received via SQL*Net from client
      16994  SQL*Net roundtrips to/from client
          0  sorts (memory)
          0  sorts (disk)
    1699260  rows processed


```

So, selecting only 3 columns out of 16 has given me over 2x better query response time (1m 35sec vs 43 sec). The sqlplus _Elapsed_ metric includes the time it took to execute the query on the DB server _and_ to fetch all its records from to the client side, so the network latency, throughput and TCP send buffer configuration will affect it.

Oracle can deduplicate repetitive field values within a result set of every fetch call, so if you need to fetch a lot of rows & columns and save network bandwidth (say, copying data from New York to Singapore over a database link), you could maximize this “compression” by ordering the query resultset by the most repetitive (least distinct values) columns that are also wide.

```
SQL> SELECT * FROM soe_small.customers 
     ORDER BY customer_class,nls_territory,nls_language,cust_first_name;

1699260 rows selected.

Elapsed: 00:01:09.23

Statistics
----------------------------------------------------------
          0  recursive calls
          0  db block gets
      28478  consistent gets
          0  physical reads
          0  redo size
   65960489  bytes sent via SQL*Net to client
     187334  bytes received via SQL*Net from client
      16994  SQL*Net roundtrips to/from client
          1  sorts (memory)
          0  sorts (disk)
    1699260  rows processed


```

The test above is a `SELECT *` again, sorted by a few VARCHAR2 columns that were 10-40 bytes (max) size, with lots of repetitive values. Only about 65 MB were sent by the server after its SQL*Net protocol-level deduplication. Note that the **SQL*Net roundtrips to/from client** value is the same for all test runs above, this is because my fetch `arraysize` has been set to 100 in my application. The arraysize controls how many fetch calls you end up sending over the network for data retrieval, every fetch after the 1st one requests arraysize-ful of rows to be returned regardless of how wide they are:

*   1699260 rows / arraysize 100 will need 16993 fetches + 1 initial single-row fetch = 16994 SQL*Net roundtrips

So the SQL*net roundtrips metric depends on the number of DB calls (number of fetches) sent over the network and the _bytes sent per roundtrip_ derived metric depends on both the number of rows a fetch asks for in a single DB call and also the width of these rows. The reality is slightly more complicated and depends on app client library’s behavior, but I’ll skip this part for brevity.

Note that you could increase the arraysize further (from 100 to 1000 for example) and not only will you be doing less SQL*Net roundtrips (1700 instead of 16994), but the amount of bytes transferred will slightly shrink too, potentially due to better compression and slightly lower SQL*Net packet overhead. When transferring data over Oracle database links, you won’t need to increase arraysize in your client session as Oracle uses the maximum possible arraysize (~32767) for dblinks automatically.

#### Increased CPU usage on client side

The more rows you process on the client side - and the more columns (and wider columns) you have, the more CPU time it will take to process them. In my case, the _application think time_ is about extracting, formatting the records and writing them to an output file.

I logged in to the Linux database server directly and am running sqlplus over a local pipe, to rule out any network/TCP overhead. The two scripts I’m running, are:

`selectstar.sql`: Select all 16 columns:

```
SET ARRAYSIZE 100 TERMOUT OFF
SPOOL customers.txt
SELECT * FROM soe_small.customers;
SPOOL OFF
EXIT


```

`selectsome.sql`: Select 3 columns:

```
SET ARRAYSIZE 100 TERMOUT OFF
SPOOL customers.txt
SELECT customer_id, credit_limit, customer_since FROM soe_small.customers;
SPOOL OFF
EXIT


```

So, let’s run `selectstar` locally:

```
$ time sqlplus -s system/oracle @selectstar

real   1m21.056s
user   1m3.053s
sys    0m15.736s


```

When adding user+sys CPU together, we get around 1m 19 seconds of CPU time, out of 1m 21s of total wall-clock elapsed time, meaning that sqlplus spent very little time sleeping, waiting for more results to arrive from the pipe. So my “application” spent 99% of its runtime in _application think time_ on the client side, burning CPU when processing the retrieved data.

I confirmed this with my [pSnapper](https://tanelpoder.com/psnapper/) tool:

```
$ sudo psn -G syscall,wchan -p sqlplus

Linux Process Snapper v0.18 by Tanel Poder [https://0x.tools]
Sampling /proc/syscall, stat, wchan for 5 seconds... finished.


=== Active Threads ===========================================================

 samples | avg_threads | comm      | state            | syscall   | wchan     
------------------------------------------------------------------------------
      95 |        0.95 | (sqlplus) | Running (ON CPU) | [running] | 0         
       2 |        0.02 | (sqlplus) | Running (ON CPU) | [running] | pipe_wait 
       2 |        0.02 | (sqlplus) | Running (ON CPU) | read      | 0         


```

Since practically all the time is spent on the client application side, there’s not much “tuning” that I can do on the database, adding indexes or increasing various database buffers won’t help as the database time is only 1% of my total runtime.

But with application code changes, by fetching only the columns I need, I can drastically reduce the client processing / application think time:

```
$ time sqlplus -s system/oracle @selectsome

real   0m4.047s
user   0m2.752s
sys    0m0.349s


```

Only 4 seconds total runtime, with about 3.1 seconds of it spent on CPU. Better performance, lower CPU usage!

Of course your mileage may vary, depending on what kind of application you’re running and which DB client libraries you’re using. Nevertheless, when your table has 500+ columns (like many data warehouse tables tend to be like), the difference between a `SELECT *` and `SELECT 10 columns...` can be massive.

By the way, starting from Oracle 12.2, you can use [sqlplus -fast option](https://blogs.oracle.com/opal/sqlplus-12201-adds-new-performance-features) to make sqlplus enable some performance options (arraysize, large output pagesize, etc):

```
$ time sqlplus -fast -s system/oracle @selectstar

real	0m16.046s
user	0m11.851s
sys	0m1.718s


```

The select _star_ script now runs in only 16 seconds instead of 1 min 21 sec.

And with printing the output directly to CSV, sqlplus can avoid some (column-aligned) formatting codepath, using even less CPU:

```
$ time sqlplus -m "csv on" -fast -s system/oracle @selectstar

real	0m12.048s
user	0m10.144s
sys	0m0.447s


```

_The fast CSV unloader written by Oracle has finally arrived!_

#### Some query plan optimizations not possible

Oracle’s optimizer can transform your query structure into something different, but logically equivalent, if it thinks it’s good for performance. Some transformations open up additional optimization opportunities (more efficient data access paths), some even allow you to skip executing a part of your query.

For example, if there happens to be an index that covers all required columns by the SQL, Oracle can do an index-only scan through the “skinny index” instead of the entire “fat” wide table. This _index fast full scan_ is not using index tree-walking, but more like a full table scan done through all the index blocks in their storage order (ignoring root & branch blocks).

Here’s an example of the `select *` vs `select col1, col2` where col1,col2 happen to be in an index:

```
SQL> @xi f2czqvfz3pj5w 0

SELECT * FROM soe_small.customers

---------------------------------------------------------------------------
| Id | Operation         | Name      | Starts | A-Rows | A-Time   | Reads |
---------------------------------------------------------------------------
|  0 | SELECT STATEMENT  |           |      1 |   1699K| 00:00.57 | 28475 |
|  1 |  TABLE ACCESS FULL| CUSTOMERS |      1 |   1699K| 00:00.57 | 28475 |
---------------------------------------------------------------------------


```

The above `select *` had to scan the table to get all its columns. Total runtime 0.57 seconds and 28475 blocks read. Now let’s just select a couple of columns that happen to be covered by a single multi-column index:

```
SQL> @xi 9gwxhcvwngh96 0

SELECT customer_id, dob FROM soe_small.customers

---------------------------------------------------------------------------------------
| Id  | Operation            | Name              | Starts | A-Rows | A-Time   | Reads |
---------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT     |                   |      1 |   1699K| 00:00.21 |  5915 |
|   1 |  INDEX FAST FULL SCAN| IDX_CUSTOMER_DOB2 |      1 |   1699K| 00:00.21 |  5915 |
---------------------------------------------------------------------------------------


```

The above query switched from `table access full` to `index fast full scan` and as a result had to read only 5915 index blocks and ran in 0.21 seconds instead of 0.57 seconds.

Another, more sophisticated example is Oracle’s _join elimination_ transformation. It can help with large complex queries accessing views that use multiple joins under the hood, but I’ll show a microscopic test case here. The U (users) and O (objects) test tables have a foreign key constraint defined between them - o.owner points to u.username and the FK constraint enforces that for every object record in O table, there must be a corresponding user record in U table.

So let’s run a two-table join in SQL:

```
SELECT o.owner FROM u, o WHERE u.username = o.owner

--------------------------------------------------------------
| Id  | Operation         | Name | Starts | A-Rows | Buffers |
--------------------------------------------------------------
|   0 | SELECT STATEMENT  |      |      1 |  61477 |    1346 |
|   1 |  TABLE ACCESS FULL| O    |      1 |  61477 |    1346 |
--------------------------------------------------------------


```

Wait, what? Only one table is actually accessed according to the execution plan above? This is Oracle’s [Join Elimination](https://oracle-base.com/articles/misc/join-elimination) transformation in action. This query can be satisfied by accessing just the child table from the parent-child relationship as we want records from O that have a corresponding record in U - and the foreign key constraint guarantees that to be true!

It gets better - in the previous query we selected columns only from the child table O, let’s also add `U.username` into the select list:

```
SELECT o.owner,u.username FROM u, o WHERE u.username = o.owner

Plan hash value: 3411128970

--------------------------------------------------------------
| Id  | Operation         | Name | Starts | A-Rows | Buffers |
--------------------------------------------------------------
|   0 | SELECT STATEMENT  |      |      1 |  61477 |    1346 |
|   1 |  TABLE ACCESS FULL| O    |      1 |  61477 |    1346 |
--------------------------------------------------------------


```

We _still_ don’t have to go to the table U despite selecting a column from it - it’s because this column is guaranteed to be exactly the same as `o.owner` thanks to the `WHERE u.username = o.owner` join condition. Oracle is smart enough to avoid doing the join as it knows it’s a logically valid shortcut.

But now let’s select an additional _non-join column_ from the table U, I’m not even using SELECT * that would have the same effect:

```
SELECT o.owner,u.username,u.created FROM u, o WHERE u.username = o.owner

--------------------------------------------------------------------------
| Id  | Operation          | Name | Starts | A-Rows | Buffers | Used-Mem |
--------------------------------------------------------------------------
|   0 | SELECT STATEMENT   |      |      1 |  61477 |    1350 |          |
|*  1 |  HASH JOIN         |      |      1 |  61477 |    1350 | 1557K (0)|
|   2 |   TABLE ACCESS FULL| U    |      1 |     51 |       3 |          |
|   3 |   TABLE ACCESS FULL| O    |      1 |  61477 |    1346 |          |
--------------------------------------------------------------------------

   1 - access("U"."USERNAME"="O"."OWNER")


```

Now we see both tables accessed and joined as there are no valid shortcuts (optimizations) to take.

You might say that this seems like a quite exotic optimization with little value in real life (how often do you _not_ need columns from the parent table and the parent table is indexed by its primary key anyway). In practice, with complex execution plans (tens of tables joined, with multiple subqueries, views, etc) it can be quite beneficial. Additionally, if the transformation phase can eliminate some tables from the join, it will be easier for the “physical optimizer” to figure out a good join order for the remaining tables.

#### Server-side memory usage

If you look into the hash join plan above, there’s a column called `Used-Mem`. Buffering row sources like sort buffers for `order by` or hashtables for hash joins, `distinct` and `group by` all need a memory scratch area (SQL cursor workarea) to operate. The more rows you process at once, the more memory you generally need. But also, the more _columns_ you buffer with each row, the more memory you’ll need!

The simplest example is just an ORDER BY:

```
SELECT * FROM soe_small.customers ORDER BY customer_since

Plan hash value: 2792773903

----------------------------------------------------------------------------------
| Id  | Operation          | Name      | Starts | A-Rows |   A-Time   | Used-Mem |
----------------------------------------------------------------------------------
|   0 | SELECT STATEMENT   |           |      1 |   1699K|00:00:02.31 |          |
|   1 |  SORT ORDER BY     |           |      1 |   1699K|00:00:02.31 |  232M (0)|
|   2 |   TABLE ACCESS FULL| CUSTOMERS |      1 |   1699K|00:00:00.24 |          |
----------------------------------------------------------------------------------


```

232 MB of memory was used for the sort above. The `(0)` indicates a zero-pass operation, we didn’t have to spill any temporary results to disk, the whole sort fit in memory.

And now select just 2 columns (and order by 3rd):

```
SELECT customer_id,dob FROM soe_small.customers ORDER BY customer_since

Plan hash value: 2792773903

----------------------------------------------------------------------------------
| Id  | Operation          | Name      | Starts | A-Rows |   A-Time   | Used-Mem |
----------------------------------------------------------------------------------
|   0 | SELECT STATEMENT   |           |      1 |   1699K|00:00:00.59 |          |
|   1 |  SORT ORDER BY     |           |      1 |   1699K|00:00:00.59 |   67M (0)|
|   2 |   TABLE ACCESS FULL| CUSTOMERS |      1 |   1699K|00:00:00.13 |          |
----------------------------------------------------------------------------------


```

Memory usage dropped from 232 MB to 67 MB. The query still had to scan through the entire Customers table and processed 1699k rows as before, but it ran 4x faster as it did’t spend so much CPU time on the sorting phase. Narrower records not only use less memory in the buffers, but also are also CPU cache-friendly and require moving less bytes around ([RAM access is slow](https://tanelpoder.com/2015/08/09/ram-is-the-new-disk-and-how-to-measure-its-performance-part-1/)).

> Wide resultsets also increase memory usage (both on server and the client) due to sending/receiving arrays of records through the database network & client libraries (not even talking about TCP send/receive buffers here). When you retrieve 1000 records per fetch and each 1000-column record is 5 kB in size on average, we are talking about at least 5 MB of memory _per connection_ on the database side and at least 5 MB of memory per _open cursor_ on the application side. In practice the usage will be larger as the data structures for processing & packing (and holding) the results have some overhead. Nowadays with memory being relatively cheap, this is not that much of a problem, but I recall one large Oracle system from 15 years ago where the customer had to reduce arraysize as otherwise they ran out of server memory with their 80 000 database connections :-) Also if your application has some sort of a cursor leak (statement handle leak), the associated memory with arrays of unconsumed cursor result sets may build up.

#### Increased CPU usage on server side

Naturally, if the higher level shortcuts and optimizations described above do not kick in, you will end up doing more work. More work means more CPU usage (and possibly disk I/O, etc).

Leaving the structural SQL plan shape optimization aside, when extracting all 500 fields from records in data blocks instead of just 20 - and passing them through the execution plan tree, you’ll use more CPU cycles for doing that. With columnar storage layouts, you potentially end up doing more I/O too. For example, with Oracle’s traditional record-oriented (OLTP) block format, navigating to 500th column of a table, requires to jump through all previous 499 columns' header bytes (run-length encoding) to find where the last column starts. Now, if you actually _need_ to retrieve all 500 fields from all rows, `SELECT *` will be efficient for the _task at hand_, but if your applications only use a handful of columns from the resultset, you’d be unnecessarily burning lots of extra CPU time on the (expensive) database server.

If your database engine happens to perform datatype conversion (from its internal number, date format to what the client expects) on the server side - and character set conversion of strings (if any), you’ll be burning more CPU on your DB server too. For example, Oracle can leave this work to client side - a “decimal” NUMBER or DATE datatype within Oracle client libraries has the same representation as in the database storage, but needs to be converted into something that’s native for the application on client side. This price has to be paid somewhere, if the client & server speak different datatype-languages.

So if you’re selecting “only” 1M rows from the database into some analytics app, but you select all 500 columns of the table, you’ll end up with _half a billion_ datatype/character set conversion operations just for this one query and you’ll quickly realize that these operations aren’t cheap.

#### Hard parsing/optimization takes more time

There’s more! I created a wide table (1000 columns) using my [create_wide_table.sql](https://github.com/tanelpoder/tpt-oracle/blob/master/demos/create_wide_table.sql) script. It has 100 rows in it and histograms on each column. I am running a very simple, single table select query against it (the “testNNN” in comments is for forcing a new hard parse each time I run the query). In the first two tests, I’m running the select statement right after recreating the table & gathering stats (no other queries have been executed at this table):

```
SQL> SET AUTOTRACE TRACE STAT

SQL> SELECT * FROM widetable /* test100 */;

100 rows selected.

Statistics
----------------------------------------------------------
       2004  recursive calls
       5267  db block gets
       2458  consistent gets
          9  physical reads
    1110236  redo size
     361858  bytes sent via SQL*Net to client
        363  bytes received via SQL*Net from client
          2  SQL*Net roundtrips to/from client
          0  sorts (memory)
          0  sorts (disk)
        100  rows processed


```

2004 recursive calls for `SELECT *` (for data dictionary access, can be verified using SQL*Trace). I recreated the table again and ran just a two column select next:

```
SQL> SELECT id,col1 FROM widetable /* test101 */;

100 rows selected.

Statistics
----------------------------------------------------------
          5  recursive calls
         10  db block gets
         51  consistent gets
          0  physical reads
       2056  redo size
       1510  bytes sent via SQL*Net to client
        369  bytes received via SQL*Net from client
          2  SQL*Net roundtrips to/from client
          0  sorts (memory)
          0  sorts (disk)
        100  rows processed


```

Only 5 recursive calls for the hard parse. See, asking Oracle to do more work (“please check, evaluate & extract 1000 columns instead of 2”) has performance consequences. Ok, this may not be a too big deal assuming that your shared pool is big enough to keep all the column (and their stats/histograms) info in dictionary cache, you wouldn’t have all these recursive SQLs with a nice warm cache. Let’s see how much _time_ the hard parse phase takes when everything’s nicely cached in dictionary cache. I’m using my [Session Snapper](https://tanelpoder.com/snapper/) in a separate Oracle session to report metrics from the hard parsing tests in another session (1136):

```
SQL> SELECT * FROM widetable /* test1 */;

SQL> @snapper stats,gather=t 5 1 1136
Sampling SID 1136 with interval 5 seconds, taking 1 snapshots...

-- Session Snapper v4.30 - by Tanel Poder ( https://tanelpoder.com/snapper )

-----------------------------------------------------------------------------
    SID, USERNAME  , TYPE, STATISTIC                          ,         DELTA
-----------------------------------------------------------------------------
   1136, SYSTEM    , TIME, hard parse elapsed time            ,         78158
   1136, SYSTEM    , TIME, parse time elapsed                 ,         80912
   1136, SYSTEM    , TIME, PL/SQL execution elapsed time      ,           127
   1136, SYSTEM    , TIME, DB CPU                             ,         89580
   1136, SYSTEM    , TIME, sql execute elapsed time           ,          5659
   1136, SYSTEM    , TIME, DB time                            ,         89616

--  End of Stats snap 1, end=2020-11-24 19:31:49, seconds=5


```

The hard parse/optimization/compilation phase took 78 milliseconds (all CPU time) for this very simple query that was selecting all 1000 columns, even with all the table metadata and column stats & histograms already cached. Oracle had to do analysis & typechecking for all 1000 columns. Now let’s run another query on the same table, selecting only 2 columns:

```
SQL> SELECT id,col1 FROM widetable /* test2 */;

-----------------------------------------------------------------------------
    SID, USERNAME  , TYPE, STATISTIC                          ,         DELTA
-----------------------------------------------------------------------------
   1136, SYSTEM    , TIME, hard parse elapsed time            ,          1162
   1136, SYSTEM    , TIME, parse time elapsed                 ,          1513
   1136, SYSTEM    , TIME, PL/SQL execution elapsed time      ,           110
   1136, SYSTEM    , TIME, DB CPU                             ,          2281
   1136, SYSTEM    , TIME, sql execute elapsed time           ,           376
   1136, SYSTEM    , TIME, DB time                            ,          2128


```

The hard parse took just ~1 millisecond! The SQL is structurally identical, on the same exact table, with just less columns selected.

Out of curiosity, what happens when we drop the histograms on all columns and do a `SELECT *` again:

```
SQL> EXEC DBMS_STATS.GATHER_TABLE_STATS(user,'WIDETABLE',method_opt=>'FOR ALL COLUMNS SIZE 1');

PL/SQL procedure successfully completed.


SQL> SELECT * FROM widetable /* test3 */;

-----------------------------------------------------------------------------
    SID, USERNAME  , TYPE, STATISTIC                          ,         DELTA
-----------------------------------------------------------------------------
   1136, SYSTEM    , TIME, hard parse elapsed time            ,         30018
   1136, SYSTEM    , TIME, parse time elapsed                 ,         30547
   1136, SYSTEM    , TIME, PL/SQL execution elapsed time      ,           202
   1136, SYSTEM    , TIME, DB CPU                             ,         37899
   1136, SYSTEM    , TIME, sql execute elapsed time           ,          5770
   1136, SYSTEM    , TIME, DB time                            ,         37807


```

Now, hard parsing takes 30 milliseconds for the 1000 column query, apparently it enumerates/maps histograms for all columns involved in the query, including the columns that are just projected (and not used in any filters or joins, where histograms are actually used for plan optimization).

And there’s more!

Oracle caches compiled cursors in shared pool memory. Oracle is smart and includes only the required metadata (various opcodes, datatypes, rules) into a compiled cursor. Thus, a cached cursor using 1000 columns is going to be much bigger than a cursor using just 2 columns:

```
SQL> SELECT sharable_mem, sql_id, child_number, sql_text FROM v$sql 
     WHERE sql_text LIKE 'SELECT % FROM widetable';

SHARABLE_MEM SQL_ID        CHILD_NUMBER SQL_TEXT
------------ ------------- ------------ -------------------------------------
       19470 b98yvssnnk13p            0 SELECT id,col1 FROM widetable
      886600 c4d3jr3fjfa3t            0 SELECT * FROM widetable


```

The 2-column cursor takes 19 kB and the 1000-column one takes 886 kB of memory in shared pool!

> Since around 10g, Oracle splits most large library cache object allocations into standardized extent sizes (4 kB) to reduce the effect of shared pool fragmentation.

Let’s take a look _inside_ these cursors with my [sqlmem.sql](https://github.com/tanelpoder/tpt-oracle/blob/master/sqlmem.sql) script (`v$sql_shared_memory`):

```
SQL> @sqlmem c4d3jr3fjfa3t
Show shared pool memory usage of SQL statement with SQL_ID c4d3jr3fjfa3t

CHILD_NUMBER SHARABLE_MEM PERSISTENT_MEM RUNTIME_MEM
------------ ------------ -------------- -----------
           0       886600         324792      219488


TOTAL_SIZE   AVG_SIZE     CHUNKS ALLOC_CL CHUNK_TYPE STRUCTURE            FUNCTION             CHUNK_COM            HEAP_ADDR
---------- ---------- ---------- -------- ---------- -------------------- -------------------- -------------------- ----------------
    272000        272       1000 freeabl           0 kccdef               qkxrMem              kccdef: qkxrMem      000000019FF49290
    128000        128       1000 freeabl           0 opn                  qkexrInitO           opn: qkexrInitO      000000019FF49290
    112568         56       2002 freeabl           0                      qosdInitExprCtx      qosdInitExprCtx      000000019FF49290
     96456         96       1000 freeabl           0                      qosdUpdateExprM      qosdUpdateExprM      000000019FF49290
     57320         57       1000 freeabl           0 idndef*[]            qkex                 idndef*[]: qkex      000000019FF49290
     48304         48       1000 freeabl           0 qeSel                qkxrXfor             qeSel: qkxrXfor      000000019FF49290
     40808         40       1005 freeabl           0 idndef               qcuAll               idndef : qcuAll      000000019FF49290
     40024      40024          1 freeabl           0 kafco                qkacol               kafco : qkacol       000000019FF49290
     37272        591         63 freeabl           0                      237.kggec            237.kggec            000000019FF49290
     16080       8040          2 freeabl           0 qeeRwo               qeeCrea              qeeRwo: qeeCrea      000000019FF49290
      8032       8032          1 freeabl           0 kggac                kggacCre             kggac: kggacCre      000000019FF49290
      8024       8024          1 freeabl           0 kksoff               opitca               kksoff : opitca      000000019FF49290
      3392         64         53 freeabl           0 kksol                kksnsg               kksol : kksnsg       000000019FF49290
      2880       2880          1 free              0                      free memory          free memory          000000019FF49290
      1152        576          2 freeabl           0                      16751.kgght          16751.kgght          000000019FF49290
      1040       1040          1 freeabl           0 ctxdef               kksLoadC             ctxdef:kksLoadC      000000019FF49290
       640        320          2 freeabl           0                      615.kggec            615.kggec            000000019FF49290
       624        624          1 recr           4095                      237.kggec            237.kggec            000000019FF49290
       472        472          1 freeabl           0 qertbs               qertbIAl             qertbs:qertbIAl      000000019FF49290
...

53 rows selected.


```

The 1000-column `SELECT *` cursor has plenty of internal allocations (allocated inside the _cursor heaps_) where the count of internal chunks is 1000 or close to a multiple of 1000, so one (or two) for each column in the compiled cursor. These structures are needed for executing the plan (like what Oracle kernel’s C function needs to be called, when the field #3 needs to be passed up the execution plan tree). For example if column #77 happens to be a DATE and it’s later compared to a TIMESTAMP column #88 in a separate step of the plan, there would need to be an additional _opcode_ somewhere that instructs Oracle to execute an additional datatype conversion function for one of the columns at that plan step. An execution plan is a tree of such dynamically allocated structures and opcodes within them. Apparently, even a simple select from a single table without any further complexity, requires plenty of such internal allocations to be in place.

Let’s look inside the 2-column cursor memory:

```
SQL> @sqlmem b98yvssnnk13p
Show shared pool memory usage of SQL statement with SQL_ID b98yvssnnk13p

CHILD_NUMBER SHARABLE_MEM PERSISTENT_MEM RUNTIME_MEM
------------ ------------ -------------- -----------
           0        19470           7072        5560


TOTAL_SIZE   AVG_SIZE     CHUNKS ALLOC_CL CHUNK_TYPE STRUCTURE            FUNCTION             CHUNK_COM            HEAP_ADDR
---------- ---------- ---------- -------- ---------- -------------------- -------------------- -------------------- ----------------
      1640       1640          1 free              0                      free memory          free memory          00000001AF2B75D0
      1152        576          2 freeabl           0                      16751.kgght          16751.kgght          00000001AF2B75D0
      1040       1040          1 freeabl           0 ctxdef               kksLoadC             ctxdef:kksLoadC      00000001AF2B75D0
       640        320          2 freeabl           0                      615.kggec            615.kggec            00000001AF2B75D0
       624        624          1 recr           4095                      237.kggec            237.kggec            00000001AF2B75D0
       544        272          2 freeabl           0 kccdef               qkxrMem              kccdef: qkxrMem      00000001AF2B75D0
       472        472          1 freeabl           0 qertbs               qertbIAl             qertbs:qertbIAl      00000001AF2B75D0
       456        456          1 freeabl           0 opixpop              kctdef               opixpop:kctdef       00000001AF2B75D0
       456        456          1 freeabl           0 kctdef               qcdlgo               kctdef : qcdlgo      00000001AF2B75D0
       328         54          6 freeabl           0                      qosdInitExprCtx      qosdInitExprCtx      00000001AF2B75D0
       312        312          1 freeabl           0 pqctx                kkfdParal            pqctx:kkfdParal      00000001AF2B75D0
       296        296          1 freeabl           0                      unmdef in opipr      unmdef in opipr      00000001AF2B75D0
       256        128          2 freeabl           0 opn                  qkexrInitO           opn: qkexrInitO      00000001AF2B75D0
       256         42          6 freeabl           0 idndef               qcuAll               idndef : qcuAll      00000001AF2B75D0
       208         41          5 freeabl           0                      kggsmInitCompac      kggsmInitCompac      00000001AF2B75D0
       192         96          2 freeabl           0                      qosdUpdateExprM      qosdUpdateExprM      00000001AF2B75D0
       184        184          1 freeabl           0                      237.kggec            237.kggec            00000001AF2B75D0
...


```

Indeed we don’t see thousands of internal allocation chunks anymore (only 2 `kccdef`s for example, compared to previous 1000).

#### LOB fetching

Ok, let’s try to finish this post with a bit mellower theme :-)

When you select LOB columns from a table, your performance will drop quite a lot due to extra network roundtrips done fetching LOB items for each returned row _individually_. Yes, you read that right, you can set your arraysize to `1000`, but if you are selecting a LOB column from the result set, then for each arrayful (of 1000) rows, you will have to do 1000 _extra_ network roundtrips for fetching individual LOB values.

I’ll create a table with 2 LOB columns in addition to “normal” columns:

```
SQL> CREATE TABLE tl (id INT, a VARCHAR2(100), b CLOB, c CLOB);

Table created.

SQL> INSERT INTO tl SELECT rownum, dummy, dummy, dummy FROM dual CONNECT BY LEVEL <= 1000;

1000 rows created.

SQL> COMMIT;

Commit complete.


```

Let’s only select the 2 normal columns first:

```
SQL> SET AUTOT TRACE STAT
SQL> SET TIMING ON

SQL> SELECT id, a FROM tl;

1000 rows selected.

Elapsed: 00:00:00.04

Statistics
----------------------------------------------------------
          0  recursive calls
          0  db block gets
         28  consistent gets
          0  physical reads
          0  redo size
      10149  bytes sent via SQL*Net to client
        441  bytes received via SQL*Net from client
         11  SQL*Net roundtrips to/from client
          0  sorts (memory)
          0  sorts (disk)
       1000  rows processed


```

Fetching 2 normal columns was very fast (0.04 seconds) and took only 11 SQL*Net roundtrips (with arraysize 100).

Now let’s add one LOB column:

```
SQL> SELECT id, a, b FROM tl;

1000 rows selected.

Elapsed: 00:00:05.50

Statistics
----------------------------------------------------------
         10  recursive calls
          5  db block gets
       2027  consistent gets
          0  physical reads
       1052  redo size
     421070  bytes sent via SQL*Net to client
     252345  bytes received via SQL*Net from client
       2002  SQL*Net roundtrips to/from client
          0  sorts (memory)
          0  sorts (disk)
       1000  rows processed


```

It took 5.5 seconds and 2002 SQL*Net roundtrips due to the “breaking” nature of LOB retrieval. By default, any row with a non-NULL LOB column is sent back immediately (just one row in the fetched array) and instead of the LOB column value, a _LOB locator_ is sent back, causing the client to issue a separate [LOBREAD](https://tanelpoder.com/2011/03/20/lobread-sql-trace-entry-in-oracle-11-2/) database call just to fetch the single LOB column value. And this gets worse when you’re selecting multiple LOB columns:

```
SQL> SELECT id, a, b, c FROM tl;

1000 rows selected.

Elapsed: 00:00:09.28

Statistics
----------------------------------------------------------
          6  recursive calls
          5  db block gets
       3026  consistent gets
          0  physical reads
        996  redo size
     740122  bytes sent via SQL*Net to client
     493348  bytes received via SQL*Net from client
       3002  SQL*Net roundtrips to/from client
          0  sorts (memory)
          0  sorts (disk)
       1000  rows processed


```

Now it takes over 9 seconds instead of previous 5.5 with just a single LOB column. We have ~3000 roundtrips, one for each row (because LOB item retrieval breaks the array fetching) and one two LOB item fetch roundtrips for each row.

Starting from Oracle 12.2 (I think), there’s a parameter LOBPREFETCH in sqlplus that allows _“bundling”_ some amount of LOB data right into the _row fetch_ roundtrip. Oracle client libraries should allow bigger LOB prefetch values, but the limit in sqlplus is 32kB:

```
SQL> SET LOBPREFETCH 32767
SQL> 
SQL> SELECT id, a, b, c FROM tl;

1000 rows selected.

Elapsed: 00:00:04.80

Statistics
----------------------------------------------------------
          0  recursive calls
          0  db block gets
       1005  consistent gets
          0  physical reads
          0  redo size
     366157  bytes sent via SQL*Net to client
      11756  bytes received via SQL*Net from client
       1002  SQL*Net roundtrips to/from client
          0  sorts (memory)
          0  sorts (disk)
       1000  rows processed


```

Now we are down to ~1000 roundtrips again, because my LOB values were small, both of them were bundled within each row’s fetch result. But Oracle still ended up fetching just one row at a time, despite my arraysize = 100 value.

So, with LOB columns added thanks to a casual `SELECT *`, your 40 millisecond query may end up taking over 9 seconds. And you won’t see much activity at the database at all, as most of the response time is spent in the SQL*Net roundtrips between the client and server. No index will make this faster, more CPUs won’t make this faster - fixing your application code will make this faster. This leads to the question of what if _want_ to pull in millions of LOB values into my app, but I’ll leave this to a separate blog entry!

#### One more thing

Note that you can use `SELECT *` in places like view definitions (or inline views) without a problem as long you do restrict the query to the columns you want somewhere in your SQL.

For example, this query would not cause a problem despite seeing a `SELECT *` somewhere within it:

```
SELECT
    id, a 
FROM (
    SELECT * FROM tl
)


```

Oracle is smart enough to propagate the projection from top level SELECT into the inline view and only get the two required columns from it.

Or, this would also be fine:

```
SELECT * FROM (
    SELECT id, a FROM tl
)


```

The goal is not to avoid a `*` in your _SQL text_, but to select only the columns that you actually need.

#### Summary

When I look at a performance problem (something is taking too much time), I think about how to _**do it less**_. The other option is to add more hardware (and there are no guarantees that it will help). One way to “do it less” is to make sure that you ask _exactly what you want_ from your database, no more, no less. Selecting only the columns you actually need is one part of that approach.

Thanks for reading, I sense more blog entries coming! :-)

*   [Twitter discussion](https://twitter.com/TanelPoder/status/1331441487863754755)
*   [HackerNews discussion](https://news.ycombinator.com/item?id=25812320)

Also, you can still sign up for my [Advanced Oracle SQL Tuning training](https://tanelpoder.com/seminar) starting on 30. Nov!