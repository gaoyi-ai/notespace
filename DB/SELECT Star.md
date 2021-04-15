---
title: "SELECT * "效率低的原因及场景
categories:
- DB
- Optimization 
tags:
- select
date: 2021/4/14 20:00:14
updated: 2021/4/14 12:00:14
---



深入了解一下"SELECT * "效率低的原因及场景。

## 一、效率低的原因

先看一下最新《阿里java开发手册（泰山版）》中 MySQL 部分描述：

> 4 - 1. 【强制】在表查询中，一律不要使用 * 作为查询的字段列表，需要哪些字段必须明确写明。

说明：

- 增加查询分析器解析成本。
- 增减字段容易与 resultMap 配置不一致。
- 无用字段增加网络 消耗，尤其是 text 类型的字段。

开发手册中比较概括的提到了几点原因，让我们深入一些看看：

### 1. 不需要的列会增加数据传输时间和网络开销

1. 用“SELECT * ”数据库需要解析更多的对象、字段、权限、属性等相关内容，在 SQL 语句复杂，硬解析较多的情况下，会对数据库造成沉重的负担。
2. 增大网络开销；* 有时会误带上如log、IconMD5之类的无用且大文本字段，数据传输size会几何增涨。如果DB和应用程序不在同一台机器，这种开销非常明显
3. 即使 mysql 服务器和客户端是在同一台机器上，使用的协议还是 tcp，通信也是需要额外的时间。

### 2. 对于无用的大字段，如 varchar、blob、text，会增加 io 操作

准确来说，长度超过 728 字节的时候，会先把超出的数据序列化到另外一个地方，因此读取这条记录会增加一次 io 操作。（MySQL InnoDB）

### 3. 失去MySQL优化器“覆盖索引”策略优化的可能性

SELECT * 杜绝了覆盖索引的可能性，而基于MySQL优化器的“覆盖索引”策略又是速度极快，效率极高，业界极为推荐的查询优化方式。

例如，有一个表为t(a,b,c,d,e,f)，其中，a为主键，b列有索引。

那么，在磁盘上有两棵 B+ 树，即聚集索引和辅助索引（包括单列索引、联合索引），分别保存(a,b,c,d,e,f)和(a,b)，如果查询条件中where条件可以通过b列的索引过滤掉一部分记录，查询就会先走辅助索引，如果用户只需要a列和b列的数据，直接通过辅助索引就可以知道用户查询的数据。

如果用户使用`select *`，获取了不需要的数据，则首先通过辅助索引过滤数据，然后再通过聚集索引获取所有的列，这就多了一次b+树查询，速度必然会慢很多。

![图片](https://mmbiz.qpic.cn/mmbiz_png/a7wPU9Eqe9sMBBA3zT893lnCo0ibZ0OhK2yuG9iaSajaM9quCePEJdJ2OhoYYTvalWySgFN3SibuITTWZaiagiaURRw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

_图片取自博《我去，为什么最左前缀原则失效了？》_

由于辅助索引的数据比聚集索引少很多，很多情况下，通过辅助索引进行覆盖索引（通过索引就能获取用户需要的所有列），都不需要读磁盘，直接从内存取，而聚集索引很可能数据在磁盘（外存）中（取决于buffer pool的大小和命中率），这种情况下，一个是内存读，一个是磁盘读，速度差异就很显著了，几乎是数量级的差异。



## 二、索引知识延申

上面提到了辅助索引，在MySQL中辅助索引包括单列索引、联合索引（多列联合），单列索引就不再赘述了，这里提一下联合索引的作用

### 联合索引 (a,b,c)

联合索引 (a,b,c) 实际建立了 (a)、(a,b)、(a,b,c) 三个索引

我们可以将组合索引想成书的一级目录、二级目录、三级目录，如index(a,b,c)，相当于a是一级目录，b是一级目录下的二级目录，c是二级目录下的三级目录。要使用某一目录，必须先使用其上级目录，一级目录除外。

如下：

![图片](https://mmbiz.qpic.cn/mmbiz_png/a7wPU9Eqe9sMBBA3zT893lnCo0ibZ0OhKOxbBMUKX1XkAASic92YrDMc4pEOdgzgic6ATpvKmprib4ZvPibN7sZQE7w/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

### 联合索引的优势

#### 1） 减少开销

建一个联合索引 (a,b,c) ，实际相当于建了 (a)、(a,b)、(a,b,c) 三个索引。每多一个索引，都会增加写操作的开销和磁盘空间的开销。对于大量数据的表，使用联合索引会大大的减少开销！

#### 2）覆盖索引

对联合索引 (a,b,c)，如果有如下 sql 的，

```
SELECT a,b,c from table where a='xx' and b = 'xx';
```

那么 MySQL 可以直接通过遍历索引取得数据，而无需回表，这减少了很多的随机 io 操作。减少 io 操作，特别是随机 io 其实是 DBA 主要的优化策略。所以，在真正的实际应用中，覆盖索引是主要的提升性能的优化手段之一。

#### 3）效率高

索引列多，通过联合索引筛选出的数据越少。比如有 1000W 条数据的表，有如下SQL:

```
select col1,col2,col3 from table where col1=1 and col2=2 and col3=3;
```

假设：假设每个条件可以筛选出 10% 的数据。

- A. 如果只有单列索引，那么通过该索引能筛选出 1000W10%=100w 条数据，然后再回表从 100w 条数据中找到符合 col2=2 and col3= 3 的数据，然后再排序，再分页，以此类推（递归）；
- B. 如果是（col1,col2,col3）联合索引，通过三列索引筛选出 1000w10% 10% *10%=1w，效率提升可想而知！

### 索引是建的越多越好吗

答案自然是否定的

- 数据量小的表不需要建立索引，建立会增加额外的索引开销
- 不经常引用的列不要建立索引，因为不常用，即使建立了索引也没有多大意义
- 经常频繁更新的列不要建立索引，因为肯定会影响插入或更新的效率
- 数据重复且分布平均的字段，因此他建立索引就没有太大的效果（例如性别字段，只有男女，不适合建立索引）
- 数据变更需要维护索引，意味着索引越多维护成本越高。
- 更多的索引也需要更多的存储空间

---

# [Why is SELECT * considered harmful?](https://stackoverflow.com/questions/3639861/why-is-select-considered-harmful)

There are really three major reasons:

- **Inefficiency in moving data to the consumer.** When you SELECT *, you're often retrieving more columns from the database than your application really needs to function. This causes more data to move from the database server to the client, slowing access and increasing load on your machines, as well as taking more time to travel across the network. This is especially true when someone adds new columns to underlying tables that didn't exist and weren't needed when the original consumers coded their data access.
- **Indexing issues.** Consider a scenario where you want to tune a query to a high level of performance. If you were to use \*, and it returned more columns than you actually needed, the server would often have to perform more expensive methods to retrieve your data than it otherwise might. For example, you wouldn't be able to create an index which simply covered the columns in your SELECT list, and even if you did (including all columns [*shudder*]), the next guy who came around and added a column to the underlying table would cause the optimizer to ignore your optimized covering index, and you'd likely find that the performance of your query would drop substantially for no readily apparent reason.
- **Binding Problems.** When you SELECT \*, it's possible to retrieve two columns of the same name from two different tables. This can often crash your data consumer. Imagine a query that joins two tables, both of which contain a column called "ID". How would a consumer know which was which? SELECT \* can also confuse views (at least in some versions SQL Server) when underlying table structures change -- [the view is not rebuilt, and the data which comes back can be nonsense](http://www.mssqltips.com/tip.asp?tip=1427). And the worst part of it is that you can take care to name your columns whatever you want, but the next guy who comes along might have no way of knowing that he has to worry about adding a column which will collide with your already-developed names.

But it's not all bad for SELECT *. I use it liberally for these use cases:

- **Ad-hoc queries.** When trying to debug something, especially off a narrow table I might not be familiar with, SELECT * is often my best friend. It helps me just see what's going on without having to do a boatload of research as to what the underlying column names are. This gets to be a bigger "plus" the longer the column names get.

- **When \* means "a row".** In the following use cases, SELECT * is just fine, and rumors that it's a performance killer are just urban legends which may have had some validity many years ago, but don't now:

    ```sql
    SELECT COUNT(*) FROM table;
    ```

    in this case, * means "count the rows". If you were to use a column name instead of * , *it would count the rows where that column's value was not null*. COUNT(*), to me, really drives home the concept that you're counting *rows*, and you avoid strange edge-cases caused by NULLs being eliminated from your aggregates.

    Same goes with this type of query:

    ```sql
    SELECT a.ID FROM TableA a
    WHERE EXISTS (
        SELECT *
        FROM TableB b
        WHERE b.ID = a.B_ID);
    ```

    in any database worth its salt, * just means "a row". It doesn't matter what you put in the subquery. Some people use b's ID in the SELECT list, or they'll use the number 1, but IMO those conventions are pretty much nonsensical. What you mean is "count the row", and that's what * signifies. Most query optimizers out there are smart enough to know this. (Though to be honest, I only *know* this to be true with SQL Server and Oracle.)