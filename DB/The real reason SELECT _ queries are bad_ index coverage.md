---
title: The real reason SELECT * queries bad
categories:
- DB
- Optimization
tags:
- select
date: 2021/4/14 20:00:14
updated: 2021/4/14 12:00:14
---



> [weblogs.asp.net](https://weblogs.asp.net/jongalloway/the-real-reason-select-queries-are-bad-index-coverage)

Are SELECT * queries bad? Sure, everyone know that. But, why?

### It's returning too much data, right?

That's the common answer, but I don't think it's the right one. If you're working with a reasonably normalized database, the actual network traffic difference is pretty small.

Let's take a look at a sample. The following two queries select 326 rows from the TransactionHistoryArchive table in the AdventureWorks database (which has a total of 89K rows). The first uses a SELECT * query, the second selects a specific column:

```
SELECT * FROM Production.TransactionHistoryArchive 
WHERE ReferenceOrderID < 100

SELECT ReferenceOrderLineID FROM Production.TransactionHistoryArchive 
WHERE ReferenceOrderID < 100
```

In this case, the difference in network traffic is only 15K, roughly a 10% difference (180K vs. 165K). It's worth fixing, but not a huge difference.

### SELECT * makes the Table / Index Scan Monster come

Often, the bigger problem with SELECT * is the effect it will have on the execution plan. While SQL Server primarily uses indexes to look up your data, if the index contains all the columns you’re requesting it doesn’t even need to look in the table. That concept is known as _index coverage._ In the above example, the first query results in a Clustered Index Scan, whereas the second query uses a much more efficient Index Seek. In this case, the Index seek is **one hundred times** more efficient than the Clustered Index Scan.

![SelectStarQueryPlan](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/SelectStarQueryPlan.jpg)

Unless you've indexed every single column in a table (which is almost never a good idea), a SELECT * query can't take advantage of index coverage, and you're likely to get (extremely inefficient) scan operations.

If you just query the rows you'll actually be using, it's more likely they'll be covered by indexes. And I think that's the biggest performance advantage of ignoring SELECT * queries.

### The Stability Aspect

SELECT * queries are also bad from an application maintenance point of view as well, since it introduces another outside variable to your code. If a column is added to a table, the results returned to your application will change in structure. Well programmed applications should be referring to columns by name and shouldn't be affected, but well programmed applications should also minimize the ways in which they are vulnerable to external changes.
