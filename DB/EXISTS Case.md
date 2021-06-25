---
title: EXISTS, IN, NOT EXISTS, NOT IN 比较 
categories:
- DB 
tags:
- EXISTS
date: 2021/4/15
---

# EXISTS Case

# Business Case

```sql
CREATE TABLE appointment
(
    emp_id     integer        NOT NULL,
    jobtitle   varchar(128)   NOT NULL,
    salary     decimal(10, 2) NOT NULL,
    start_date date           NOT NULL,
    end_date   date           NULL
);
ALTER TABLE appointment
    ADD CONSTRAINT pkey_appointment PRIMARY KEY (emp_id, jobtitle, start_date);
ALTER TABLE appointment
    ADD CONSTRAINT chk_appointment_period CHECK (start_date <= end_date);
```

## 问题

Write an SQL query which returns for all current employees the start of their current period of continuous employment. That is, we are asking for the oldest date X such that the employee had one or more appointments on every day since X. E.g. if table appointment is populated as follows:

```sql
INSERT INTO appointment
VALUES (1, 'tutor', 40000, '2008-01-01', '2009-02-01'),
       (1, 'tutor', 42000, '2009-01-01', '2010-09-30'),
       (1, 'tutor', 45000, '2012-04-01', '2013-12-31'),
       (1, 'tutor', 46000, '2014-01-01', '2014-12-31'),
       (1, 'lecturer', 65000, '2014-06-01', NULL),
       (2, 'librarian', 35000, '2014-01-01', NULL),
       (2, 'tutor', 20000, '2014-01-01', NULL),
       (3, 'lecturer', 65000, '2014-06-01', '2015-01-01');
```

then the query should return

```
emp_id start_date
------------------
1      2012-04-01
2      2014-01-01
```

## 分析

- 统计的都是现在职人员的信息，所以查询都要 join current_employees

- 薪水都是年薪，年底发，一年一发，只发从年初到年底的。

- 职员可能会有多个职位，薪水叠加。某些职位可能只工作一段时间，只要没离职，这段时间的薪水也是年底发。

- 统计的是在职人员中，连续工作（从某个时间点一直到现在都在工作(职位不限)）的最早时间节点，通俗点说就是想看看现在这些人最早都是什么时间跟着公司打拼并且一直没闲着。

    注意：现在职人员不都是一直在职的，可能会有中间离职又回来入职的情况(或者可能是没离职但是中间没干活)，这种情况只算最后一次入职的时间点。

基本方案就是 对于每一个工作安排 a ，查询是否存在如下安排 b , 满足`b.start_date < a.start_date`并且 `b.end_date+1 >= a.start_date` 或者 `b.end_date is null` ，也就说，是否存在一个和当前 a 交叉、重叠或者正好衔接的工作安排 b 并且比 a 早，如果是这样，那么 `a.start_date` 就不能作为一个题目中要求的连续工作时间的起点，否则 a 就是一个合理的 start_date，把每个在职员工的所有这种工作片段都检索出来。

上述工作可以用 query a not exist ( subquery b) 来实现

但是这样可能会检索出多个片段，因为可能有前边提到的离职了又回来的情况(或者可能是没离职但是中间没干活)，显然这就违反了一直没停工作到现在的条件，所以，最后需要根据 `emp_id` 分组然后取最新的那个片段。

上述可以用 `max(start_date)`配合 `group by emp_id` 来实现。

Hint: First construct a subquery to compute appointments for current employees that do not overlap with (or are adjacent to) appointments (for the same employee) starting earlier then select for each employee the latest start-date of such appointments.

1. Recursive Query

```
WITH recursive latest_employees AS (
    SELECT DISTINCT emp_id, start_date
    FROM appointment
    WHERE end_date IS NULL
    union
    select a.emp_id, a.start_date
    from latest_employees e
             join appointment a
                  on e.emp_id = a.emp_id and (a.end_date is null or (a.end_date + 1) >= e.start_date)
)
select emp_id, min(start_date) as start_date
from latest_employees
group by emp_id;
```

2. **Model Answer**

```
with current_employees as (
    select distinct emp_id
    from appointment
    where end_date is null
),
     start_dates as (
         select a.emp_id,
                a.start_date
         from appointment a
                  join current_employees ce on a.emp_id = ce.emp_id
         where not exists(
                 select 1
                 from appointment earlier
                 where earlier.emp_id = a.emp_id
                   and earlier.start_date < a.start_date
                   and (earlier.end_date + 1 >= a.start_date or earlier.end_date is null)
             )
     )
select emp_id,
       max(start_date) as start_date
from start_dates
group by emp_id;
```

# Exists

> [www.cnblogs.com](https://www.cnblogs.com/xuanhai/p/5810918.html)

比如在 Northwind 数据库中有一个查询为 

```
SELECT c.CustomerId,CompanyName FROM Customers c  
WHERE EXISTS(  
SELECT OrderID FROM Orders o WHERE o.CustomerID=c.CustomerID) 
```

这里面的 EXISTS 是如何运作呢？子查询返回的是 OrderId 字段，可是外面的查询要找的是 CustomerID 和 CompanyName 字段，这两个字段肯定不在 OrderID 里面啊，这是如何匹配的呢？  

EXISTS 用于检查子查询是否至少会返回一行数据，该子查询实际上并不返回任何数据，而是返回值 True 或 False 

EXISTS 指定一个子查询，检测 行 的存在。 

- 语法： EXISTS subquery 
- 参数： subquery 是一个受限的 SELECT 语句 (不允许有 COMPUTE 子句和 INTO 关键字)。 
- 结果类型： Boolean 如果子查询包含行，则返回 TRUE ，否则返回 FLASE 。

<table><tbody><tr><td>例表 A：TableIn</td><td>例表 B：TableEx</td></tr><tr><td><img class="" src="https://images.cnblogs.com/cnblogs_com/netserver/tableA.jpg"></td><td><img class="" src="https://images.cnblogs.com/cnblogs_com/netserver/tableB.jpg"></td></tr></tbody></table>

## 在子查询中使用 NULL 仍然返回结果集 

`select * from TableIn where exists(select null) `
等同于： `select * from TableIn`
![](https://images.cnblogs.com/cnblogs_com/netserver/tableA.jpg) 

## 比较使用 EXISTS 和 IN 的查询

`select * from TableIn where exists(select BID from TableEx where BNAME=TableIn.ANAME) `
`select * from TableIn where ANAME in(select BNAME from TableEx) `
![](https://images.cnblogs.com/cnblogs_com/netserver/008.jpg)

## 比较使用 EXISTS 和 = ANY 的查询
`select * from TableIn where exists(select BID from TableEx where BNAME=TableIn.ANAME) `
`select * from TableIn where ANAME=ANY(select BNAME from TableEx) `
![](https://images.cnblogs.com/cnblogs_com/netserver/008.jpg) 

NOT EXISTS 的作用与 EXISTS 正好相反。如果子查询没有返回行，则满足了 NOT EXISTS 中的 WHERE 子句。 

## 结论

EXISTS(包括 NOT EXISTS) 子句的返回值是一个 BOOL 值。 EXISTS 内部有一个子查询语句 (SELECT ... FROM...)， 将其称为 EXIST 的内查询语句。其内查询语句返回一个结果集。 EXISTS 子句根据其内查询语句的结果集空或者非空，返回一个布尔值。 

一种通俗的可以理解为：将外查询表的每一行，代入内查询作为检验，如果内查询返回的结果取非空值，则 EXISTS 子句返回 TRUE，这一行行可作为外查询的结果行，否则不能作为结果。 

分析器会先看语句的第一个词，当它发现第一个词是 SELECT 关键字的时候，它会跳到 FROM 关键字，然后通过 FROM 关键字找到表名并把表装入内存。接着是找 WHERE 关键字，如果找不到则返回到 SELECT 找字段解析，如果找到 WHERE，则分析其中的条件，完成后再回到 SELECT 分析字段。最后形成一张我们要的虚表。 WHERE 关键字后面的是条件表达式。条件表达式计算完成后，会有一个返回值，即非 0 或 0，非 0 即为真 (true)，0 即为假 (false)。

分析器先找到关键字 SELECT，然后跳到 FROM 关键字将 STUDENT 表导入内存，并通过指针找到第一条记录，接着找到 WHERE 关键字计算它的条件表达式，如果为真那么把这条记录装到一个虚表当中，指针再指向下一条记录。如果为假那么指针直接指向下一条记录，而不进行其它操作。一直检索完整个表，并把检索出来的虚拟表返回给用户。EXISTS 是条件表达式的一部分，它也有一个返回值 (true 或 false)。 

在插入记录前，需要检查这条记录是否已经存在，只有当记录不存在时才执行插入操作，可以通过使用 EXISTS 条件句防止插入重复记录。 

```
INSERT INTO TableIn (ANAME,ASEX)   
SELECT top 1 '张三', '男' FROM TableIn  
WHERE not exists (select * from TableIn where TableIn.AID = 7) 
```

EXISTS 与 IN 的使用效率的问题，通常情况下采用 exists 要比 in 效率高，因为 IN 不走索引，但要看实际情况具体使用： 

**IN 适合于外表大而内表小的情况；EXISTS 适合于外表小而内表大的情况。**

## in、not in、exists 和 not exists 的区别：

### 先谈谈 in 和 exists 的区别： 

exists: 存在，后面一般都是子查询，当子查询返回行数时，exists 返回 true。 

`select * from class where exists (select 1 from stu where stu.cid=class.cid) `

当 in 和 exists 在查询效率上比较时，exists 查询的效率快于 in 的查询效率 

exists(...) 后面的子查询被称做相关子查询, 他是不返回列表的值的. 
只是返回一个 true或 false 的结果 (这也是为什么子查询里是 select 1的原因 也就是它只在乎括号里的数据能不能查找出来，是否存在这样的记录。 

其运行方式是先运行主查询一次 再去子查询里查询与其对应的结果 如果存在，返回 true 则输出, 反之返回 false 则不输出, 再根据主查询中的每一行去子查询里去查询.

执行顺序如下：  

1. 首先执行一次外部查询  
2. 对于外部查询中的每一行分别执行一次子查询，而且每次执行子查询时都会引用外部查询中当前行的值。
3. 使用子查询的结果来确定外部查询的结果集。 如果外部查询返回 100 行，SQL   就将执行 101 次查询，一次执行外部查询，然后为外部查询返回的每一行执行一次子查询。

### in：包含

查询和所有女生年龄相同的男生 
`select * from stu where sex='男' and age in(select age from stu where sex='女') `

in() 后面的子查询 是返回结果集的, 换句话说执行次序和 exists() 不一样. 子查询先产生结果集, 
然后主查询再去结果集里去找符合要求的字段列表去. 符合要求的输出, 反之则不输出.

### not in 和 not exists 的区别： 

not in 只有当子查询中，select 关键字后的字段有 not null 约束或者有这种暗示时用 not in, 另外如果主查询中表大，子查询中的表小但是记录多，则应当使用 not in, 

例如: 查询那些班级中没有学生的， 
`select * from class where cid not in(select distinct cid from stu) `
当表中 cid 存在 null 值，not in 不对空值进行处理 
解决: `select * from class where cid not in (select distinct cid from stu where cid is not null)`

not in 的执行顺序是：是在表中一条记录一条记录的查询 (查询每条记录）符合要求的就返回结果集，不符合的就继续查询下一条记录，直到把表中的记录查询完。也就是说为了证明找不到，所以只能查询全部记录才能证明。并没有用到索引。 

not exists：如果主查询表中记录少，子查询表中记录多，并有索引。 
例如: 查询那些班级中没有学生的， 
`select * from class2 where not exists (select * from stu1 where stu1.cid =class2.cid)`

not exists 的执行顺序是：在表中查询，是根据索引查询的，如果存在就返回 true，如果不存在就返回 false，不会每条记录都去查询。 

之所以要多用 not exists，而不用 not in，也就是 not exists 查询的效率远远高与 not in 查询的效率。

## 实例：

exists,not exists 的使用方法示例，需要的朋友可以参考下。

```sql
-- 学生表
create table student
(
 id number(8) primary key,
 name varchar2(10),deptment number(8)
)
```

```sql
-- 选课表
create table select_course
(
  ID         NUMBER(8) primary key,
  STUDENT_ID NUMBER(8) foreign key (COURSE_ID) references course(ID),
  COURSE_ID  NUMBER(8) foreign key (STUDENT_ID) references student(ID)
)
```

```sql
-- 课程表
create table COURSE
(
  ID     NUMBER(8) not null,
  C_NAME VARCHAR2(20),
  C_NO   VARCHAR2(10)
)
```

```
student表的数据：
        ID NAME            DEPTMENT_ID
---------- --------------- -----------
         1 echo                   1000
         2 spring                 2000
         3 smith                  1000
         4 liter                  2000
```

```
course表的数据：
        ID C_NAME               C_NO
---------- -------------------- --------
         1 数据库               data1
         2 数学                 month1
         3 英语                 english1
```

```
select_course表的数据：
        ID STUDENT_ID  COURSE_ID
---------- ---------- ----------
         1    1         1
         2    1         2
         3    1         3
         4    2         1
         5    2         2
         6    3         2
```

1.查询选修了所有课程的学生id、name:（即这一个学生没有一门课程他没有选的。）

分析：如果有一门课没有选，则此时(1) `select * from select_course sc where sc.student_id=ts.id and sc.course_id=c.id`存在null，

这说明(2)`select * from course c` 的查询结果中确实有记录不存在(1查询中)，查询结果返回没有选的课程，

此时select * from t_student ts 后的not exists 判断结果为false，不执行查询。

```
SQL> select * from t_student ts where not exists
	 (select * from course c where not exists
  		(select * from select_course sc where sc.student_id=ts.id and sc.course_id=c.id));       
```

```
        ID NAME            DEPTMENT_ID
---------- --------------- -----------
         1 echo                   1000
```

2.查询没有选择所有课程的学生，即没有全选的学生。（存在这样的一个学生，他至少有一门课没有选），

分析：只要有一个门没有选，即`select * from select_course sc where student_id=t_student.id and course_id =course.id` 有一条为空，即not exists null 为true,此时select * from course有查询结果（id为子查询中的course.id ），因此select id,name from t_student 将执行查询（id为子查询中t_student.id ）。

```
SQL> select id,name from t_student where exists
(select * from course where not exists 
(select * from select_course sc where student_id=t_student.id and course_id=course.id));
```

```
        ID NAME
---------- ---------------
         2 spring
         3 smith
         4 liter
```

3.查询一门课也没有选的学生。（不存这样的一个学生，他至少选修一门课程），

分析：如果他选修了一门select * from course结果集不为空，not exists 判断结果为false;

select id,name from t_student 不执行查询。

```
SQL> select id,name from t_student where not exists
(select * from course where exists
(select * from select_course sc where student_id=t_student.id and course_id=course.id));
```

```
        ID NAME
---------- ---------------
         4 liter
```

4.查询至少选修了一门课程的学生。

```
SQL> select id,name from t_student where exists
(select * from course where exists
(select * from select_course sc where student_id=t_student.id and course_id=course.id));
```

```
        ID NAME
---------- ---------------
         1 echo
         2 spring
         3 smith
```

