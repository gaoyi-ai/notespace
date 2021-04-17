---
title: EXISTS 到底做了什么
categories:
- DB 
tags:
- EXISTS
date: 2021/4/15 20:00:14
updated: 2021/4/15 12:00:14
---



# EXISTS 到底做了什么

> [zhuanlan.zhihu.com](https://zhuanlan.zhihu.com/p/20005249)

先从 SQL 中最基础的 WHERE 子句开始,比如下面这条 SQL 语句：

```sql
SELECT Sno, Sname
FROM Student
WHERE Sdept = 'IS'
```

很显然，在执行这条 SQL 语句的时候，DBMS 会扫描 Student 表中的每一条记录，然后把符合 Sdept = 'IS' 这个条件的所有记录筛选出来，并放到结果集里面去。也就是说 WHERE 关键字的作用就是判断后面的逻辑表达式的值是否为 True。如果为 True，则将当前这条记录（经过 SELECT 关键字处理后）放到结果集里面去，如果逻辑表达式的值为 False 则不放。

接下来看使用了 EXISTS 关键字的一条 SQL 语句：

```sql
SELECT Sname
FROM Student
WHERE EXISTS
(SELECT *
FROM SC
WHERE Sno=Student.Sno
AND Cno = '1');
```

这条 SQL 语句的作用，就是查找所有选修了 1 号课程的课程的学生，并显示他们的姓名。

我们先不管 EXISTS 关键字在其中起了什么作用，而是先来看子查询中的 WHERE 关键字后的表达式 Sno = Student.Sno AND Cno = '1'。

其中的 Sno = Student.Sno 是怎么一回事？

这就涉及到 SQL 中的不相关子查询与相关子查询了。

我们常见的带子查询的 SQL 语句是这样的：

```sql
SELECT Sno
FROM SC
WHERE Cno IN
(SELECT Cno
FROM Course
WHERE Cname = '数据结构');
```

首先通过子查询得到课名为 “数据结构” 的课程的课号，然后遍历 SC （选课）表中的每一条选课记录，若当前这条记录的课号为 “数据结构” 这门课的课号，则将这条记录的 Sno 列的值放到结果集里面去。最终我们可以得到所有选修了 ” 数据结构 “ 这门课的学生的学号。

这种类型的查询是先执行子查询，得到一个集合（或值），然后将这个集合（或值）作为一个常量带入到父查询的 WHERE 子句中去。如果单纯地执行子查询，也是可以成功的。

这种类型的查询，叫做 ” 不相关子查询 “。

大多数情况下，不相关子查询已经够用了，但是如果有这样的一个查询要求：

查询每个学生超过他/她选修的所有课程的平均成绩的课程的课程号

子查询可以这么写：

```sql
SELECT AVG(Grade)
FROM SC
WHERE Sno = ?
```

那么问题来了， ？ 处应该写什么值？

关键问题就是，？ 处这个常量，并不是一个确定的值，而应该是不断地将 Student 表中的每一条记录中的 Sno 列的值代入此处，然后求出该 Sno 对应的平均成绩。我们需要的是输入一系列的值，然后得到一系列对应的输出。

这个时候，我们就要用到另一种嵌套查询，叫做 “相关子查询”。“相关子查询” 的意思就是，子查询中需要用到父查询中的值。

对于这个查询要求，我们可以使用以下 SQL 语句：

```sql
SELECT Cno
FROM SC x
WHERE Grade >=
(SELECT AVG(Grade)
FROM SC y
WHERE y.Sno = x.Sno)
```

其工作原理就是，扫描父查询中数据来源（如 SC 表）中的每一条记录，然后将当前这条记录中的，在子查询中会用到的值代入到子查询中去，然后执行子查询并得到结果（可以看成是返回值），然后再将这个结果代入到父查询的条件中，判断父查询的条件表达式的值是否为 True，若为 True，则将当前 SC 表中的这条记录（经过 SELECT 处理）后放到结果集中去。若为 False 则不放。

在这个例子中，父查询先从 SC 表中取出第一条记录，然后将当前这条记录的 Sno 列的值（如 95001）代入到子查询中，求出学号为 95001 的学生选修的所有课程的平均分（如 80 分）。然后将这个 80 作为 Grade >= 后面的值代入，若 SC 表中的第一条记录的 Grade 列的值为 90，那么 Grade >= 80 这个条件表达式的值为 True，则将当前这条记录中的 Cno 列的值（如 1）放入结果集中去。以此类推，遍历 SC 表中的所有记录，即可得到每个超过学生超过他 / 她所有课程平均分的课程的课号了。

判断是否是 “相关子查询” 也很简单，只要子查询不能脱离父查询单独执行，那么就是 “相关子查询”。

知道了 “相关子查询” 的概念之后，我们就可以回来了解 EXISTS 关键字的作用了。它的作用，就是判断子查询得到的结果集是否是一个空集，如果不是，则返回 True，如果是，则返回 False。EXISTS 本身就是 “存在” 的意思，用我们可以理解的话来说，就是如果在当前的表中存在符合条件的这样一条记录，那么返回 True，否则返回 False。

为了方便，我们再次放出这条 SQL 语句：

![](https://pic1.zhimg.com/98d8cdfd7c10e0087c9cf5f3af7b4b34_r.jpg)

```sql
SELECT Sname
FROM Student
WHERE EXISTS
(SELECT *
FROM SC
WHERE Sno=Student.Sno
AND Cno = '1');
```

在这个查询中，首先会取出 Student 表中的第一条记录，得到其 Sno 列（因为在子查询中用到了）的值（如 95001），然后将该值代入到子查询中。若能找到这样的一条记录，那么说明学号为 95001 的学生选修了 1 号课程。因为能找到这样的一条记录，所以子查询的结果不为空集，那么 EXISTS 会返回 True，从而使 Student 表中的第一条记录中的 Sname 列的值被放入结果集中去。以此类推，遍历 Student 表中的所有记录后，就能得到所有选修了 1 号课程的学生的姓名。

与 EXISTS 关键字相对的是 NOT EXISTS，作用与 EXISTS 正相反，当子查询的结果为空集时，返回 True，反之返回 False。也就是所谓的 ” 若不存在 “。

对于下面的查询要求，只能通过 NOT EXISTS 关键字来实现，因为 SQL 中并未直接提供关系代数中的除法功能。

查询选修了全部课程的学生的姓名。

可以通过以下步骤的思路来实现：

STEP1：先取 Student 表中的第一个元组，得到其 Sno 列的值。  
STEP2：再取 Course 表中的第一个元组，得到其 Cno 列的值。  
STEP3：根据 Sno 与 Cno 的值，遍历 SC 表中的所有记录（也就是选课记录）。若对于某个 Sno 和 Cno 的值来说，在 SC 表中找不到相应的记录，则说明该 Sno 对应的学生没有选修该 Cno 对应的课程。  
STEP4：对于某个学生来说，若在遍历 Course 表中所有记录（也就是所有课程）后，仍找不到任何一门他 / 她没有选修的课程，就说明此学生选修了全部的课程。  
STEP5：将此学生放入结果元组集合中。  
STEP6：回到 STEP1，取 Student 中的下一个元组。  
STEP7：将所有结果元组集合显示。

根据以上思路，可以写出 SQL 语句：

```sql
SELECT Sname
FROM Student
WHERE NOT EXISTS
(SELECT *
FROM Course
WHERE NOT EXISTS
(SELECT *
FROM SC
WHERE Sno = Student.Sno
AND Cno = Course.Cno);
```

其中第一个 NOT EXISTS 对应 STEP4，第二个 NOT EXISTS 对应 STEP3。

同理，对于类似的查询要求

查询被所有学生选修的课程的课名。

可以使用 SQL 语句：

```
SELECT Cname
FROM Course
WHERE NOT EXISTS
(SELECT *
FROM Student
WHERE NOT EXISTS
(SELECT *
FROM SC
WHERE Sno = Student.Sno
AND Cno = Course.Cno);
```

对于查询要求

查询选修了95001号学生选修的全部课程的学生的学号

可以使用 SQL 语句：

```sql
SELECT DISTINCT Sno
FROM SC SCX
WHERE NOT EXISTS
(SELECT *
FROM SC SCY
WHERE SCY.Sno = '95001' AND
NOT EXISTS
(SELECT *
FROM SC SCZ
WHERE SCZ.Sno=SCX.Sno
AND SCZ.Cno=SCY.Cno);
```



> [blog.csdn.net](https://blog.csdn.net/weixin_43901882/article/details/89467490)

# SQL 中 EXISTS 的理解使用

关联子查询
-----

> *   在讲述 EXISTS 用法之前，先讲述一下关联子查询：  
>     关联子查询：是指在内查询中需要借助于外查询，而外查询离不开内查询的执行。

举个栗子：  
在 Oracle 中自带的 EMP 表中，**查询工资大于同职位平均工资的员工信息**

*   EMP 表：

![](https://img-blog.csdnimg.cn/2019042310474622.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MzkwMTg4Mg==,size_16,color_FFFFFF,t_70)

*   Oracle 语句：

```sql
select * from emp e
where sal >(select avg(sal) from emp where job = e.job);
```

1.  首先**执行外查询 select * from emp e**，然后取出第一行数据，将数据中的 JOB 传递给内查询
2.  内查询 (select avg(sal) from emp where job = e.job) **根据外查询传递的 JOB 来查询平均工资**，此时相当于 select avg(sal) from emp where job = ‘CLERK’;
3.  外查询取出的第一行数据**比较 sal 是否大于内查询查出的平均工资**，若大于，则保留改行作结果显示，反之则不保留
4.  **依次逐行查询、比较、是否保留**；类似 Java 的 for 循环一样

执行结果：  
![](https://img-blog.csdnimg.cn/20190423105747958.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MzkwMTg4Mg==,size_16,color_FFFFFF,t_70)

EXISTS 理解使用
-----------

> EXISTS (subquery)  
> 如果子查询包含任何行，则 EXISTS 运算符返回 true。 否则它返回 false。

用 exists 代替 in 是 SQL 性能优化的一个手段，使用 exists 能提高查询性能

举个栗子：  
Oracle 自带的 EMP、DEPT 表查询，**部门编号小于 30 的所有员工信息**

*   Oracle 语句：

```sql
select * from emp where deptno in (select deptno from dept where deptno <30);

select * from emp e where exists (
	select * from dept d where  deptno <30 and d.deptno = e.deptno ；
);
```

1.  首先**执行外查询 select * from emp e**，然后取出第一行数据，将数据中的部门编号传给内查询
2.  **内查询执行** select * from dept d where deptno <30 and d.deptno = e.deptno ；**看是否查询到结果**，查询到，则返回 true，否则返回 false；比如传来的是 30，则不满足 deptno <30 and d.deptno = 30，返回 false
3.  内查询**返回 true，则该行数据保留，作为结果显示**；反之，返回 false，则不作结果显示
4.  **逐行查询**，**看内查询是否查到数据**，是否保留作结果显示

> 如果上述内查询为： select * from dept d where deptno <30  
> 因为该语句都会查询到有结果，每一行都返回 true，所以会查询到的是全部员工信息

查询结果：

![](https://img-blog.csdnimg.cn/20190423125859829.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MzkwMTg4Mg==,size_16,color_FFFFFF,t_70)

> not exists：子查询查询到有结果则返回 false，没有查询到结果返回 true  
> 如下述语句，查询到的是 deptno = 30 的员工信息（dept 表只有 10，20，30）

```sql
select * from emp e where not exists (
	select * from dept d where  deptno <30 and d.deptno = e.deptno ；
);
```

注意：**exists 子查询查找到有结果则返回 true**，即使子查询查到的内容为 null，也返回 true