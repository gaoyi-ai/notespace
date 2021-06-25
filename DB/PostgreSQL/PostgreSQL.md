---
title: PostgreSQL
categories:
- DB
- PostgreSQL
tags:
- SQL
date: 2021/2/27
---

# PostgreSQL

在数据库术语里，PostgreSQL使用一种客户端/服务器的模型。一次PostgreSQL会话由下列相关的进程（程序）组成：

- 一个服务器进程，它管理数据库文件、接受来自客户端应用与数据库的联接并且代表客户端在数据库上执行操作。 该数据库服务器程序叫做postgres。 
- 那些需要执行数据库操作的用户的客户端（前端）应用。  客户端应用可能本身就是多种多样的：可以是一个面向文本的工具，  也可以是一个图形界面的应用，或者是一个通过访问数据库来显示网页的网页服务器，或者是一个特制的数据库管理工具。  一些客户端应用是和 PostgreSQL发布一起提供的，但绝大部分是用户开发的。

和典型的客户端/服务器应用（C/S应用）一样，这些客户端和服务器可以在不同的主机上。这时它们通过 TCP/IP 网络联接通讯。 你应该记住的是，在客户机上可以访问的文件未必能够在数据库服务器机器上访问（或者只能用不同的文件名进行访问）。

PostgreSQL服务器可以处理来自客户端的多个并发请求。  因此，它为每个连接启动（“forks”）一个新的进程。  从这个时候开始，客户端和新服务器进程就不再经过最初的postgres进程的干涉进行通讯。  因此，主服务器进程总是在运行并等待着客户端联接，  而客户端和相关联的服务器进程则是起起停停（当然，这些对用户是透明的。）

# DBMS

## Integrity Constraints

- Database schema must capture both structure and semantics

- Integrity constraints enforce business rules in databases

    - they are speciﬁed on the database schema

- Databases are restricted to those satisfying constraints
  
	- already seen: domain, key, and foreign key constraints

- Integrity constraints interact with one another

	- If some constraints hold, others hold automatically
        e.g. if a key holds, so do all of its superkeys
- Try to minimize cost to enforce constraints
	      e.g. don’t enforce non-minimal keys explicitly

对主键（primary key）、外键（foreign key）、候选键（Candidate key）、超键（super key）、references的总结

概念：

主键：用户选择元组标识的一个候选键，主键不允许为空

外键：来描述两个表的关系，外键可为空

超键：能唯一的标识元组的属性集

候选键：不含有多余属性的超键

实例：

假如有以下学生和教师两个表：

Student（student_no,student_name,student_age,student_sex,student_credit,teacher_no）

Teacher（teacher_no,teacher_name,teacher_salary）

超键：Student表中可根据学生编号（student_no），或身份证号（student_credit），或（学生编号，姓名）（student_no,student_name），或（学生编号，身份证号）（student_no,student_credit）等来唯一确定是哪一个学生，因此这些组合都可以作为此表的超键

候选键：候选键属于超键，且是最小的超键，即如果去掉超键组合中任意一个属性就不再是超键了。Student表中候选键为学生编号（student_no），身份证号（student_credit）

主键：主键是候选键中的一个，可人为决定，通常会选择编号来作为表的主键。现分别选取student_no，teacher_no作为Student表，Teacher表的主键

外键：teacher_no为两个表的公共关键字，且是Teacher表的主键，因此teacher_no是Student表的外键，用来描述Student表和Teacher表的关系

# SQL

## aggregate

```sql
SELECT city FROM weather WHERE temp_lo = max(temp_lo);     WRONG
```

过这个方法不能运转，因为聚集max不能被用于WHERE子句中（存在这个限制是因为WHERE子句决定哪些行可以被聚集计算包括；因此显然它必需在聚集函数之前被计算）。不过，我们通常都可以用其它方法实现我们的目的；这里我们就可以使用子查询：

```sql
SELECT city FROM weather WHERE temp_lo = (SELECT max(temp_lo) FROM weather);
```

这样做是 OK 的，因为子查询是一次独立的计算，它独立于外层的查询计算出自己的聚集。

## HAVING vs WHERE

WHERE和HAVING的基本区别如下：WHERE在分组和聚集计算之前选取输入行（因此，它控制哪些行进入聚集计算），而HAVING在分组和聚集之后选取分组行。因此，WHERE子句不能包含聚集函数；  因为试图用聚集函数判断哪些行应输入给聚集运算是没有意义的。相反，HAVING子句总是包含聚集函数（严格说来，你可以写不不使用聚集的HAVING子句，  但这样做很少有用。同样的条件用在WHERE阶段会更有效）。

## join

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210301112959656.png" alt="image-20210301112959656" style="zoom:50%;" />

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210301113010920.png" alt="image-20210301113010920" style="zoom:50%;" />

# WITH

## 前言

WITH 提供了一种方式来书写在一个大型查询中使用的辅助语句。这些语句通常被称为公共表表达式或 CTE，它们可以被看成是定义只在一个查询中存在的临时表。在 WITH 子句中的每一个辅助语句可以是一个 SELECT、INSERT、UPDATE 或 DELETE，并且 WITH 子句本身也可以被附加到一个主语句，主语句也可以是 SELECT、INSERT、UPDATE 或 DELETE。

## CTE or WITH

WITH 语句通常被称为通用表表达式（Common Table Expressions）或者 CTEs。

WITH 语句作为一个辅助语句依附于主语句，WITH 语句和主语句都可以是 SELECT，INSERT，UPDATE，DELETE 中的任何一种语句。

举个栗子

```sql
WITH regional_sales AS (
    SELECT region, SUM(amount) AS total_sales
    FROM orders
    GROUP BY region
), top_regions AS (
    SELECT region
    FROM regional_sales
    WHERE total_sales > (SELECT SUM(total_sales)/10 FROM
 regional_sales)
)
SELECT region,
       product,
       SUM(quantity) AS product_units,
       SUM(amount) AS product_sales
FROM orders
WHERE region IN (SELECT region FROM top_regions)
GROUP BY region, product;
```

它只显示在高销售区域每种产品的销售总额。WITH子句定义了两个辅助语句regional_sales和top_regions，其中regional_sales的输出用在top_regions中而top_regions的输出用在主SELECT查询。这个例子可以不用WITH来书写，但是我们必须要用两层嵌套的子SELECT。使用这种方法要更简单些。

当然不用这个也是可以的，不过 CTE 主要的还是做数据的过滤。什么意思呢，我们可以定义多层级的 CTE，然后一层层的查询过滤组装。最终筛选出我们需要的数据，当然你可能会问为什么不一次性拿出所有的数据呢，当然如果数据很大，我们通过多层次的数据过滤组装，在效率上也更好。

### 在 WITH 中使用数据修改语句

WITH 中可以不仅可以使用 SELECT 语句，同时还能使用 DELETE，UPDATE，INSERT 语句。因此，可以使用 WITH，在一条 SQL 语句中进行不同的操作，如下例所示。

```sql
WITH moved_rows AS (
  DELETE FROM products
  WHERE
    "date" >= '2010-10-01'
  AND "date" < '2010-11-01'
  RETURNING *
)
INSERT INTO products_log
SELECT * FROM moved_rows;
```

本例通过 WITH 中的 DELETE 语句从 products 表中删除了一个月的数据，并通过 RETURNING 子句将删除的数据集赋给 moved_rows 这一 CTE，最后在主语句中通过 INSERT 将删除的商品插入 products_log 中。

如果 WITH 里面使用的不是 SELECT 语句，并且没有通过 RETURNING 子句返回结果集，则主查询中不可以引用该 CTE，但主查询和 WITH 语句仍然可以继续执行。这种情况可以实现将多个不相关的语句放在一个 SQL 语句里，实现了在不显式使用事务的情况下保证 WITH 语句和主语句的事务性，如下例所示。

```sql
WITH d AS (
  DELETE FROM foo
),
u as (
  UPDATE foo SET a = 1
  WHERE b = 2
)
DELETE FROM bar;
```

The sub-statements in WITH 中的子语句被和每一个其他子语句以及主查询并发执行。因此在使用 WITH 中的数据修改语句时，指定更新的顺序实际是以不可预测的方式发生的。RETURNING 数据是在不同 WITH 子语句和主查询之间传达改变的唯一方法。

```sql
WITH t AS (
    UPDATE products SET price = price * 1.05
    RETURNING *
)
SELECT * FROM products;
```

外层 SELECT 可以返回在 UPDATE 动作之前的原始价格，而在

```sql
WITH t AS (
    UPDATE products SET price = price * 1.05
    RETURNING *
)
SELECT * FROM t;
```

外部 SELECT 将返回更新过的数据。

## WITH 使用注意事项

1、WITH 中的数据修改语句会被执行一次，并且肯定会完全执行，无论主语句是否读取或者是否读取所有其输出。而 WITH 中的 SELECT 语句则只输出主语句中所需要记录数。

2、WITH 中使用多个子句时，这些子句和主语句会并行执行，所以当存在多个修改子语句修改相同的记录时，它们的结果不可预测。

3、所有的子句所能 “看” 到的数据集是一样的，所以它们看不到其它语句对目标数据集的影响。这也缓解了多子句执行顺序的不可预测性造成的影响。

4、如果在一条 SQL 语句中，更新同一记录多次，只有其中一条会生效，并且很难预测哪一个会生效。

5、如果在一条 SQL 语句中，同时更新和删除某条记录，则只有更新会生效。

6、目前，任何一个被数据修改 CTE 的表，不允许使用条件规则，和 ALSO 规则以及 INSTEAD 规则。

## RECURSIVE

可选的 RECURSIVE 修饰符将 WITH 从单纯的句法便利变成了一种在标准 SQL 中不能完成的特性。通过使用 RECURSIVE，一个 WITH 查询可以引用它自己的输出。

比如下面的这个表：

```sql
create table document_directories
(
    id         bigserial                                          not null
        constraint document_directories_pk
            primary key,
    name       text                                               not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    parent_id  bigint                   default 0                 not null
);

comment on table document_directories is '文档目录';

comment on column document_directories.name is '名称';

comment on column document_directories.parent_id is '父级id';

INSERT INTO public.document_directories (id, name, created_at, updated_at, parent_id) VALUES (1, '中国', '2020-03-28 15:55:27.137439', '2020-03-28 15:55:27.137439', 0);
INSERT INTO public.document_directories (id, name, created_at, updated_at, parent_id) VALUES (2, '上海', '2020-03-28 15:55:40.894773', '2020-03-28 15:55:40.894773', 1);
INSERT INTO public.document_directories (id, name, created_at, updated_at, parent_id) VALUES (3, '北京', '2020-03-28 15:55:53.631493', '2020-03-28 15:55:53.631493', 1);
INSERT INTO public.document_directories (id, name, created_at, updated_at, parent_id) VALUES (4, '南京', '2020-03-28 15:56:05.496985', '2020-03-28 15:56:05.496985', 1);
INSERT INTO public.document_directories (id, name, created_at, updated_at, parent_id) VALUES (5, '浦东新区', '2020-03-28 15:56:24.824672', '2020-03-28 15:56:24.824672', 2);
INSERT INTO public.document_directories (id, name, created_at, updated_at, parent_id) VALUES (6, '徐汇区', '2020-03-28 15:56:39.664924', '2020-03-28 15:56:39.664924', 2);
INSERT INTO public.document_directories (id, name, created_at, updated_at, parent_id) VALUES (7, '漕宝路', '2020-03-28 15:57:14.320631', '2020-03-28 15:57:14.320631', 6);
```

这是一个无限级分类的列表，我们制造几条数据，来分析下 RECURSIVE 的使用。

```sql
WITH RECURSIVE res AS (
    SELECT id, name, parent_id
    FROM document_directories
    WHERE id = 5
    UNION
    SELECT dd.id,
           dd.name || ' > ' || d.name,
           dd.parent_id
    FROM res d
             INNER JOIN document_directories dd ON dd.id = d.parent_id
)
select *
from res

--当然这个sql也可以这样写
WITH RECURSIVE res(id, name, parent_id) AS (
    SELECT id, name, parent_id
    FROM document_directories
    WHERE id = 5
    UNION
    SELECT dd.id,
           dd.name || ' > ' || d.name,
           dd.parent_id
    FROM res d
             INNER JOIN document_directories dd ON dd.id = d.parent_id
)
select *
from res
```

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/1237626-20200329015425207-244915367.png)

### 递归查询的过程

这是 pgsql 操作文档中的描述:

> 1、计算非递归项。对 UNION（但不对 UNION ALL），抛弃重复行。把所有剩余的行包括在递归查询的结果中，并且也把它们放在一个临时的工作表中。

> 2、只要工作表不为空，重复下列步骤：

> a 计算递归项，用当前工作表的内容替换递归自引用。对 UNION（不是 UNION ALL），抛弃重复行以及那些与之前结果行重复的行。将剩下的所有行包括在递归查询的结果中，并且也把它们放在一个临时的中间表中。

> b 用中间表的内容替换工作表的内容，然后清空中间表。

### 拆解下执行的过程

其实执行就分成了两部分：

1、non-recursive term（非递归部分），即上例中的 union 前面部分

2、recursive term（递归部分），即上例中 union 后面部分

拆解下我们上面的 sql

#### 1、执行非递归部分

```sql
SELECT id, name, parent_id
    FROM document_directories
    WHERE id = 5
--结果集和working table为
5	浦东新区	2
```

#### 2、执行递归部分, 如果是 UNION, 要用当前查询的结果和上一个 working table 的结果进行去重，然后放到到临时表中。然后把 working table 的数据替换成临时表里面的数据。

```sql
SELECT dd.id,
           dd.name || ' > ' || d.name,
           dd.parent_id
    FROM res d
             INNER JOIN document_directories dd ON dd.id = d.parent_id
--结果集和working table为
2	上海 > 浦东新区	1
```

#### 3、同 2，直到数据表中没有数据。

```sql
SELECT dd.id,
           dd.name || ' > ' || d.name,
           dd.parent_id
    FROM res d
             INNER JOIN document_directories dd ON dd.id = d.parent_id
--结果集和working table为
1	中国 > 上海 > 浦东新区	0
```

#### 4、结束递归，将前几个步骤的结果集合并，即得到最终的 WITH RECURSIVE 的结果集

严格来讲，这个过程实现上是一个迭代的过程而非递归，不过 RECURSIVE 这个关键词是 SQL 标准委员会定立的，所以 PostgreSQL 也延用了 RECURSIVE 这一关键词。

### WITH RECURSIVE 中止

在使用递归查询时，确保查询的递归部分最终将不返回元组非常重要，否则查询将会无限循环。在某些时候，使用UNION替代UNION  ALL可以通过抛弃与之前输出行重复的行来达到这个目的。不过，经常有循环不涉及到完全重复的输出行：它可能只需要检查一个或几个域来看相同点之前是否达到过。处理这种情况的标准方法是计算一个已经访问过值的数组。例如，考虑下面这个使用link域搜索表graph的查询：

```sql
WITH RECURSIVE search_graph(id, link, data, depth) AS (
    SELECT g.id, g.link, g.data, 1
    FROM graph g
  UNION ALL
    SELECT g.id, g.link, g.data, sg.depth + 1
    FROM graph g, search_graph sg
    WHERE g.id = sg.link
)
SELECT * FROM search_graph;
```

如果link关系包含环，这个查询将会循环。因为我们要求一个“depth”输出，仅仅将UNION ALL 改为UNION 不会消除循环。反过来在我们顺着一个特定链接路径搜索时，我们需要识别我们是否再次到达了一个相同的行。我们可以项这个有循环倾向的查询增加两个列path和cycle：

```sql
WITH RECURSIVE search_graph(id, link, data, depth, path, cycle) AS
 (
    SELECT g.id, g.link, g.data, 1,
      ARRAY[g.id],
      false
    FROM graph g
  UNION ALL
    SELECT g.id, g.link, g.data, sg.depth + 1,
      path || g.id,
      g.id = ANY(path)
    FROM graph g, search_graph sg
    WHERE g.id = sg.link AND NOT cycle
)
SELECT * FROM search_graph;
```

除了阻止环，数组值对于它们自己的工作显示到达任何特定行的“path”也有用。

当你不确定查询是否可能循环时，一个测试查询的有用技巧是在父查询中放一个LIMIT。例
如，这个查询没有LIMIT时会永远循环：

```sql
WITH RECURSIVE t(n) AS (
    SELECT 1
  UNION ALL
    SELECT n+1 FROM t
)
SELECT n FROM t LIMIT 100;
```

这会起作用，因为PostgreSQL的实现只计算WITH查询中被父查询实际取到的行。不推荐在生产中使用这个技巧，因为其他系统可能以不同方式工作。同样，如果你让外层查询排序递归查询的结果或者把它们连接成某种其他表，这个技巧将不会起作用，因为在这些情况下外层查询通常将尝试取得WITH查询的所有输出。

WITH查询的一个有用的特性是在每一次父查询的执行中它们通常只被计算一次，即使它们被父查询或兄弟WITH查询引用了超过一次。因此，在多个地方需要的昂贵计算可以被放在一个WITH查询中来避免冗余工作。另一种可能的应用是阻止不希望的多个函数计算产生副作用。

然而，这一问题的另一方面是，优化器不能将父查询中的限制推到多个引用的WITH查询中，因为当WITH查询应该只影响一个查询时，这可能会影响所有的使用。multiple-referenced with查询将按照写入的方式进行计算，而不会抑制父查询随后可能丢弃的行。(但是，如上所述，如果对查询的引用只需要有限的行数，那么计算可能会提前停止。)

### CTE 优缺点

1、 可以使用递归 WITH RECURSIVE，从而实现其它方式无法实现或者不容易实现的查询  
2、 当不需要将查询结果被其它独立查询共享时，它比视图更灵活也更轻量  
3、 CTE 只会被计算一次，且可在主查询中多次使用  
4、 CTE 可极大提高代码可读性及可维护性  
5、 CTE 不支持将主查询中 where 后的限制条件 push down 到 CTE 中，而普通的子查询支持

### UNION 与 UNION ALL 的区别

- union all 是直接连接，取到得是所有值，记录可能有重复 
- union 是取唯一值，记录没有重复

1、UNION 的语法如下：

```sql
[SQL 语句 1]
      UNION
     [SQL 语句 2]
```

2、UNION ALL 的语法如下：

```sql
[SQL 语句 1]
      UNION ALL
     [SQL 语句 2]
```

UNION 和 UNION ALL 关键字都是将两个结果集合并为一个，但这两者从使用和效率上来说都有所不同。

1、对重复结果的处理：UNION 在进行表链接后会筛选掉重复的记录，Union All 不会去除重复记录。  
2、对排序的处理：Union 将会按照字段的顺序进行排序；UNION ALL 只是简单的将两个结果合并后就返回。

从效率上说，UNION ALL 要比 UNION 快很多，所以，如果可以确认合并的两个结果集中不包含重复数据且不需要排序时的话，那么就使用 UNION ALL。

### 总结

*   UNION 去重且排序

*   UNION ALL 不去重不排序 (效率高)

## 总结

recursive 是 pgsql 中提供的一种递归的机制，比如当我们查询一个完整的树形结构使用这个就很完美，但是我们应该避免发生递归的死循环，也就是数据的环状。当然他只是 cte 中的一个查询的属性，对于 cte 的使用，我们也不能忽略它需要注意的地方，使用多个子句时，这些子句和主语句会并行执行。我们是不能判断那个将会被执行的，在一条 SQL 语句中，更新同一记录多次，只有其中一条会生效，并且很难预测哪一个会生效。当然功能还是很强大的，WITH 语句和主语句都可以是 SELECT，INSERT，UPDATE，DELETE 中的任何一种语句，我们可以组装出我们需要的任何操作的场景。