---
title: Normalization
categories:
- DB
- NF
tags:
- NF
date: 2021/3/10 20:00:17
updated: 2021/3/12 12:00:17
---

# Description of normalization

规范化是组织数据库中数据的过程。这包括创建表，并根据规则建立这些表之间的关系，这些规则既是为了保护数据，也是为了通过消除冗余和不一致的依赖关系使数据库更加灵活。

冗余数据会浪费磁盘空间，并造成维护问题。如果必须改变存在于多个地方的数据，那么必须在所有地方以完全相同的方式改变数据。如果客户地址的更改只存储在Customers表中，而不是数据库中的其他地方，那么实施起来就容易多了。

什么是 "不一致的依赖"？虽然用户在Customers表中查找某个客户的地址是很直观的，但在那里查找拜访该客户的员工的工资可能没有意义。该员工的工资与该员工相关，或依赖于该员工，因此应该移到Employees表中。不一致的依赖关系会使数据难以访问，因为查找数据的路径可能缺失或中断。

数据库规范化有一些规则。每条规则称为 "规范形式"。如果遵守了第一条规则，则称数据库处于 "第一规范形式"。如果遵守前三条规则，则认为数据库处于 "第三规范形式"。虽然其他级别的规范化是可能的，但第三规范形式被认为是大多数应用所必需的最高级别。

与许多正式的规则和规范一样，现实世界的场景并不总是允许完美的遵守。一般来说，规范化需要额外的表格，一些客户认为这很麻烦。如果你决定违反规范化的前三条规则中的一条，请确保你的应用程序预计到任何可能发生的问题，如冗余数据和不一致的依赖关系。

The following descriptions include examples.

## First normal form

*   消除各个表格中重复的组。
*   为每一组相关数据建立一个单独的表。
*   用一个主键来标识每一组相关数据。

不要在一个表中使用多个字段来存储类似的数据。例如，为了跟踪可能来自两个可能来源的库存项目，一条库存记录可能包含供应商代码1和供应商代码2的字段。

当您添加第三个供应商时会发生什么？添加一个字段并不是解决问题的办法，它需要修改程序和表格，而且不能顺利地适应供应商的动态数量。相反，将所有的供应商信息放在一个单独的名为 "供应商 "的表中，然后用货号键将库存与供应商联系起来，或者用供应商代码键将供应商与库存联系起来。

Second normal form
-----------------------------------------

*   为适用于多个记录的值集创建单独的表。
* 用一个外键将这些表联系起来。

记录除了表的主键（必要时可使用复合键）外，不应依赖于其他任何东西。例如，考虑会计系统中客户的地址。客户表需要这个地址，但订单表、发货表、发票表、应收账款表和收款表也需要这个地址。与其将客户的地址作为一个单独的条目存储在这些表中，不如将其存储在一个地方，要么存储在客户表中，要么存储在一个单独的地址表中。

Third normal form
---------------------------------------

*   删除不依赖键的字段。

记录中不属于该记录的键的值不属于该表。一般来说，只要一组字段的内容可能适用于表中不止一条记录，就要考虑将这些字段放在一个单独的表中。

例如，在员工招聘表中，可能包括候选人的大学名称和地址。但你需要一个完整的大学列表来进行群发邮件。如果大学信息存储在 "候选人 "表中，就无法列出没有当前候选人的大学。创建一个单独的Universities表，并用大学代码键将其链接到Candidates表。

例外：坚持第三种规范形式，虽然理论上是可取的，但并不总是实用的。如果你有一个Customers表，并且你想消除所有可能的字段间依赖关系，你必须为城市、邮政编码、销售代表、客户类别以及任何其他可能在多个记录中重复的因素创建单独的表。理论上，规范化是值得追求的。然而，许多小表可能会降低性能或超过打开的文件和内存容量。

只对经常变化的数据应用第三种正常形式可能更可行。如果一些依赖的字段仍然存在，设计你的应用程序，要求用户在任何一个字段改变时验证所有相关字段。

Other normalization forms
-------------------------------------------------------

Fourth normal form, also called Boyce Codd Normal Form (BCNF), and fifth normal form do exist, but are rarely considered in practical design. Disregarding these rules may result in less than perfect database design, but should not affect functionality.

Normalizing an example table
-------------------------------------------------------------

These steps demonstrate the process of normalizing a fictitious student table.

1.  Unnormalized table:
    
    <table><caption>Table 1</caption><thead><tr><th>Student#</th><th>Advisor</th><th>Adv-Room</th><th>Class1</th><th>Class2</th><th>Class3</th></tr></thead><tbody><tr><td>1022</td><td>Jones</td><td>412</td><td>101-07</td><td>143-01</td><td>159-02</td></tr><tr><td>4123</td><td>Smith</td><td>216</td><td>201-01</td><td>211-02</td><td>214-01</td></tr></tbody></table>
    
2.  First normal form: No repeating groups
    
    Tables should have only two dimensions. Since one student has several classes, these classes should be listed in a separate table. Fields Class1, Class2, and Class3 in the above records are indications of design trouble.
    
    Spreadsheets often use the third dimension, but tables should not. Another way to look at this problem is with a one-to-many relationship, do not put the one side and the many side in the same table. Instead, create another table in first normal form by eliminating the repeating group (Class#), as shown below:
    
    <table><caption>Table 2</caption><thead><tr><th>Student#</th><th>Advisor</th><th>Adv-Room</th><th>Class#</th></tr></thead><tbody><tr><td>1022</td><td>Jones</td><td>412</td><td>101-07</td></tr><tr><td>1022</td><td>Jones</td><td>412</td><td>143-01</td></tr><tr><td>1022</td><td>Jones</td><td>412</td><td>159-02</td></tr><tr><td>4123</td><td>Smith</td><td>216</td><td>201-01</td></tr><tr><td>4123</td><td>Smith</td><td>216</td><td>211-02</td></tr><tr><td>4123</td><td>Smith</td><td>216</td><td>214-01</td></tr></tbody></table>
3. Second normal form: Eliminate redundant data

    Note the multiple Class# values for each Student# value in the above table. Class# is not functionally dependent on Student# (primary key), so this relationship is not in second normal form.

    The following two tables demonstrate second normal form:

    Students:

    <table><caption>Table 3</caption><thead><tr><th>Student#</th><th>Advisor</th><th>Adv-Room</th></tr></thead><tbody><tr><td>1022</td><td>Jones</td><td>412</td></tr><tr><td>4123</td><td>Smith</td><td>216</td></tr></tbody></table>

    Registration:

    <table><caption>Table 4</caption><thead><tr><th>Student#</th><th>Class#</th></tr></thead><tbody><tr><td>1022</td><td>101-07</td></tr><tr><td>1022</td><td>143-01</td></tr><tr><td>1022</td><td>159-02</td></tr><tr><td>4123</td><td>201-01</td></tr><tr><td>4123</td><td>211-02</td></tr><tr><td>4123</td><td>214-01</td></tr></tbody></table>

4. Third normal form: Eliminate data not dependent on key

    In the last example, Adv-Room (the advisor's office number) is functionally dependent on the Advisor attribute. The solution is to move that attribute from the Students table to the Faculty table, as shown below:

    Students:

    <table><caption>Table 5</caption><thead><tr><th>Student#</th><th>Advisor</th></tr></thead><tbody><tr><td>1022</td><td>Jones</td></tr><tr><td>4123</td><td>Smith</td></tr></tbody></table>

    Faculty:

    <table><caption>Table 6</caption><thead><tr><th>Name</th><th>Room</th><th>Dept</th></tr></thead><tbody><tr><td>Jones</td><td>412</td><td>42</td></tr><tr><td>Smith</td><td>216</td><td>42</td></tr></tbody></table>

> [Description of the database normalization - Microsoft](https://docs.microsoft.com/en-us/office/troubleshoot/access/database-normalization-description)

