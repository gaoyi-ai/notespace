---
title: Normalization & Denormalization
categories:
- DB
- NF
tags:
- wiki
date: 2021/3/10 20:00:17
updated: 2021/3/12 12:00:17
---

# Database normalization

## Objectives

A basic objective of the first normal form defined by Codd in 1970 was to permit data to be queried and manipulated using a "universal data sub-language" grounded in [first-order logic](/wiki/First-order_logic "First-order logic").[[1]](#cite_note-1) ([SQL](/wiki/SQL "SQL") is an example of such a data sub-language, albeit one that Codd regarded as seriously flawed.[[2]](#cite_note-2))

The objectives of normalization beyond 1NF (first normal form) were stated as follows by Codd:

1.  使关系集合摆脱不需要的插入、更新和删除依赖关系。
2.  减少因引入新的数据类型而对关系集合进行重组的需要，从而增加应用程序的寿命。
3.  为了让关系模型对用户更有参考价值。
2.   使关系集合对查询统计保持中立，因为随着时间的推移，这些统计数据有可能发生变化。

— E.F. Codd, "Further Normalization of the Data Base Relational Model"[[3]](#cite_note-3)

When an attempt is made to modify (update, insert into, or delete from) a relation, the following undesirable side-effects may arise in relations that have not been sufficiently normalized:

*   **更新异常。**相同的信息可以在多行中表达；因此，对关系的更新可能导致逻辑上的不一致。例如，"Employees' Skills"关系中的每条记录可能包含雇员ID、雇员地址和技能；因此，特定雇员的地址变更可能需要应用于多条记录（每条技能一条）。如果更新只是部分成功--雇员的地址在一些记录上更新了，但在另一些记录上没有更新--那么关系就会处于不一致的状态。具体来说，对于这个特定员工的地址是什么这个问题，关系提供了矛盾的答案。这种现象被称为更新异常。
*   **插入异常。**在某些情况下，某些事实根本无法记录。例如，"Faculty and Their Courses "关系中的每条记录可能包含教员ID、教员姓名、教员雇用日期和课程代码。因此，我们可以记录任何一个至少教授一门课程的教员的详细信息，但我们不能记录一个尚未被分配教授任何课程的新聘教员，除非将课程代码设置为[null](/wiki/Null_(SQL) "Null (SQL)")。这种现象称为插入异常。
*   **删除异常**在某些情况下，删除代表某些事实的数据，就必须删除代表完全不同事实的数据。前面例子中描述的 "Faculty and Their Courses"关系就存在这种异常情况，因为如果一个教员暂时不再被分配到任何课程，我们就必须删除该教员出现的最后一条记录，实际上也删除了该教员，除非我们将课程代码设置为空。这种现象称为删除异常。

### Minimize redesign when extending the database structure

完全规范化的数据库允许扩展其结构，以适应新的数据类型，而不会对现有结构进行过多的改变。因此，与数据库交互的应用程序受到的影响最小。

Normalized relations, and the relationship between one normalized relation and another, mirror real-world concepts and their interrelationships.

### Example

Querying and manipulating the data within a data structure that is not normalized, such as the following non-1NF representation of customers' credit card transactions, involves more complexity than is really necessary:

<table><tbody><tr><th>Customer</th><th>Cust. ID</th><th>Transactions</th></tr><tr><td>Abraham</td><td>1</td><td><table><tbody><tr><th>Tr. ID</th><th>Date</th><th>Amount</th></tr><tr><td>12890</td><td>14-Oct-2003</td><td>−87</td></tr><tr><td>12904</td><td>15-Oct-2003</td><td>−50</td></tr></tbody></table></td></tr><tr><td>Isaac</td><td>2</td><td><table><tbody><tr><th>Tr. ID</th><th>Date</th><th>Amount</th></tr><tr><td>12898</td><td>14-Oct-2003</td><td>−21</td></tr></tbody></table></td></tr><tr><td>Jacob</td><td>3</td><td><table><tbody><tr><th>Tr. ID</th><th>Date</th><th>Amount</th></tr><tr><td>12907</td><td>15-Oct-2003</td><td>−18</td></tr><tr><td>14920</td><td>20-Nov-2003</td><td>−70</td></tr><tr><td>15003</td><td>27-Nov-2003</td><td>−60</td></tr></tbody></table></td></tr></tbody></table>

每个客户都对应着一组 "重复的 "交易。因此，对任何与客户交易有关的查询进行自动评估，大致包括两个阶段。

1. 拆开一个或多个客户的交易组，允许检查组中的个别交易，以及... ...
2. 根据第一阶段的结果推导出查询结果。

例如，为了找出所有客户在2003年10月发生的所有交易的货币总和，系统必须知道，它必须首先解开每个客户的交易组，然后在交易日期落在2003年10月的地方，将所有交易的金额相加，从而得到。

Codd的一个重要见解是，结构复杂度是可以降低的。结构复杂度的降低给用户、应用程序和DBMS提供了更多的权力和灵活性来制定和评估查询。上述结构的一个更规范化的等价物可能是这样的。

<table><tbody><tr><th>Customer</th><th>Cust. ID</th></tr><tr><td>Abraham</td><td>1</td></tr><tr><td>Isaac</td><td>2</td></tr><tr><td>Jacob</td><td>3</td></tr></tbody></table>

<table><tbody><tr><th>Cust. ID</th><th>Tr. ID</th><th>Date</th><th>Amount</th></tr><tr><td>1</td><td>12890</td><td>14-Oct-2003</td><td>−87</td></tr><tr><td>1</td><td>12904</td><td>15-Oct-2003</td><td>−50</td></tr><tr><td>2</td><td>12898</td><td>14-Oct-2003</td><td>−21</td></tr><tr><td>3</td><td>12907</td><td>15-Oct-2003</td><td>−18</td></tr><tr><td>3</td><td>14920</td><td>20-Nov-2003</td><td>−70</td></tr><tr><td>3</td><td>15003</td><td>27-Nov-2003</td><td>−60</td></tr></tbody></table>

In the modified structure, the [primary key](/wiki/Primary_key "Primary key") is {Cust. ID} in the first relation, {Cust. ID, Tr. ID} in the second relation.

现在，每一行都代表了一个单独的信用卡交易，DBMS可以获得感兴趣的答案，只需找到日期落在10月的所有行，并将它们的Amounts相加即可。该数据结构将所有的值放在平等的位置上，将每个值直接暴露给DBMS，因此每个值都有可能直接参与查询；而在以前的情况下，有些值被嵌入到低级结构中，必须进行特殊处理。相应地，规范化的设计适合于通用的查询处理，而非规范化的设计则不适合。规范化版本还允许用户在一个地方更改客户名称，并防范在某些记录上客户名称拼写错误而产生的错误（见上文 "更新异常"）。

Normal forms
------------

Codd introduced the concept of normalization and what is now known as the [first normal form](/wiki/First_normal_form "First normal form") (1NF) in 1970.[[4]](#cite_note-Codd1970-4) Codd went on to define the [second normal form](/wiki/Second_normal_form "Second normal form") (2NF) and [third normal form](/wiki/Third_normal_form "Third normal form") (3NF) in 1971,[[5]](#cite_note-Codd,_E.F_1971-5) and Codd and [Raymond F. Boyce](/wiki/Raymond_F._Boyce "Raymond F. Boyce") defined the [Boyce-Codd normal form](/wiki/Boyce-Codd_normal_form "Boyce-Codd normal form") (BCNF) in 1974.[[6]](#cite_note-CoddBCNF-6)

Informally, a relational database relation is often described as "normalized" if it meets third normal form.[[7]](#cite_note-DateIntroDBSys-7) Most 3NF relations are free of insertion, update, and deletion anomalies.

The normal forms (from least normalized to most normalized) are:

*   UNF: [Unnormalized form](/wiki/Unnormalized_form "Unnormalized form")
*   1NF: [First normal form](/wiki/First_normal_form "First normal form")
*   2NF: [Second normal form](/wiki/Second_normal_form "Second normal form")
*   3NF: [Third normal form](/wiki/Third_normal_form "Third normal form")
*   EKNF: [Elementary key normal form](/wiki/Elementary_key_normal_form "Elementary key normal form")
*   BCNF: [Boyce–Codd normal form](/wiki/Boyce%E2%80%93Codd_normal_form "Boyce–Codd normal form")
*   4NF: [Fourth normal form](/wiki/Fourth_normal_form "Fourth normal form")
*   ETNF: [Essential tuple normal form](/w/index.php?title=Essential_tuple_normal_form&action=edit&redlink=1 "Essential tuple normal form (page does not exist)")
*   5NF: [Fifth normal form](/wiki/Fifth_normal_form "Fifth normal form")
*   DKNF: [Domain-key normal form](/wiki/Domain-key_normal_form "Domain-key normal form")
*   6NF: [Sixth normal form](/wiki/Sixth_normal_form "Sixth normal form")

Example of a step by step normalization
---------------------------------------

规范化是一种数据库设计技术，用来设计关系型数据库表，直至更高的规范化形式，[10]这个过程是渐进式的，除非满足了前几级的要求，否则无法实现更高层次的数据库规范化，[11]。

That means that, having data in [unnormalized form](/wiki/Unnormalized_form "Unnormalized form") (the least normalized) and aiming to achieve the highest level of normalization, the first step would be to ensure compliance to [first normal form](/wiki/First_normal_form "First normal form"), the second step would be to ensure [second normal form](/wiki/Second_normal_form "Second normal form") is satisfied, and so forth in order mentioned above, until the data conform to [sixth normal form](/wiki/Sixth_normal_form "Sixth normal form").

However, it is worth noting that normal forms beyond [4NF](/wiki/4NF "4NF") are mainly of academic interest, as the problems they exist to solve rarely appear in practice.[[12]](#cite_note-12)

### Initial data

Let a database table with the following structure:[[11]](#cite_note-:0-11)

<table><tbody><tr><th>Title</th><th>Author</th><th>Author Nationality</th><th>Format</th><th>Price</th><th>Subject</th><th>Pages</th><th>Thickness</th><th>Publisher</th><th>Publisher Country</th><th>Publication Type</th><th>Genre ID</th><th>Genre Name</th></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>Chad Russell</td><td>American</td><td>Hardcover</td><td>49.99</td><td>MySQL,<p>Database,</p><p>Design</p></td><td>520</td><td>Thick</td><td>Apress</td><td>USA</td><td>E-book</td><td>1</td><td>Tutorial</td></tr></tbody></table>

We assume in this example that each book has only one author.

### Satisfying 1NF

为了满足1NF，表的每一列的值必须是原子的。在初始表中，Subject包含一组主题值，这意味着它不符合要求。

实现1NF的一种方法是使用重复组Subject将重复的内容分成多列。

<table><tbody><tr><th><u>Title</u></th><th><u>Format</u></th><th>Author</th><th>Author Nationality</th><th>Price</th><th>Subject 1</th><th>Subject 2</th><th>Subject 3</th><th>Pages</th><th>Thickness</th><th>Publisher</th><th>Publisher country</th><th>Genre ID</th><th>Genre Name</th></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>Hardcover</td><td>Chad Russell</td><td>American</td><td>49.99</td><td>MySQL</td><td>Database</td><td>Design</td><td>520</td><td>Thick</td><td>Apress</td><td>USA</td><td>1</td><td>Tutorial</td></tr></tbody></table>

虽然现在表在形式上符合1NF（是原子的），但这种解决方案的问题是显而易见的--如果一本书有三个以上的主题，那么在不改变其结构的情况下，就无法将其添加到数据库中。

为了以更优雅的方式解决这个问题，有必要确定表中所代表的实体，并将它们分开到各自的表中。在这种情况下，将导致**书**、**科目**和**出版商**表：

<table><caption>Book</caption><tbody><tr><th><u>Title</u></th><th><u>Format</u></th><th>Author</th><th>Author Nationality</th><th>Price</th><th>Pages</th><th>Thickness</th><th>Genre ID</th><th>Genre Name</th><th><i>Publisher ID</i></th></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>Hardcover</td><td>Chad Russell</td><td>American</td><td>49.99</td><td>520</td><td>Thick</td><td>1</td><td>Tutorial</td><td><i>1</i></td></tr></tbody></table>

<table><tbody><tr><td><table><caption><b>Subject</b></caption><tbody><tr><th><b><u>Subject ID</u></b></th><th><b>Subject name</b></th></tr><tr><td>1</td><td>MySQL</td></tr><tr><td>2</td><td>Database</td></tr><tr><td>3</td><td>Design</td></tr></tbody></table></td><td><table><caption><b>Publisher</b></caption><tbody><tr><th><b><u>Publisher_ID</u></b></th><th><b>Name</b></th><th><b>Country</b></th></tr><tr><td>1</td><td>Apress</td><td>USA</td></tr></tbody></table></td></tr></tbody></table>

简单地将初始数据分离成多个表，会破坏数据之间的联系。也就是说，需要确定新引入的表之间的关系。注意到Book的表中的_Publisher ID_列是一个[外键](/wiki/Foreign_key "外键")实现了一本书和一个出版社之间的[多对一](/wiki/Many-to-one "多对一")关系。

一本书可以适合很多科目，同时一个科目也可能对应很多本书。这意味着还需要定义一个[多对多](/wiki/Many-to-many_(data_model) "多对多(数据模型)")关系，通过创建一个[链接表](/wiki/Link_Table "链接表")来实现：[[11]](#cite_note-:0-11)

<table><tbody><tr><td><table><caption><b>Title - Subject</b></caption><tbody><tr><th><u>Title</u></th><th><i><b>Subject ID</b></i></th></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>1</td></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>2</td></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>3</td></tr></tbody></table></td></tr></tbody></table>

Instead of one table in [unnormalized form](/wiki/Unnormalized_form "Unnormalized form"), there are now 4 tables conforming to the 1NF.

### Satisfying 2NF

The **Book** table has one [candidate key](/wiki/Candidate_key "Candidate key") (which is therefore the [primary key](/wiki/Primary_key "Primary key")), the [composite key](/wiki/Composite_key "Composite key") **{Title, Format}**.[[13]](#cite_note-13) Consider the following table fragment:

<table><caption>Book</caption><tbody><tr><th><u>Title</u></th><th><u>Format</u></th><th>Author</th><th>Author Nationality</th><th>Price</th><th>Pages</th><th>Thickness</th><th>Genre ID</th><th>Genre Name</th><th><i>Publisher ID</i></th></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>Hardcover</td><td>Chad Russell</td><td>American</td><td>49.99</td><td>520</td><td>Thick</td><td>1</td><td>Tutorial</td><td><i>1</i></td></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>E-book</td><td>Chad Russell</td><td>American</td><td>22.34</td><td>520</td><td>Thick</td><td>1</td><td>Tutorial</td><td><i>1</i></td></tr><tr><td>The Relational Model for Database Management: Version 2</td><td>E-book</td><td>E.F.Codd</td><td>British</td><td>13.88</td><td>538</td><td>Thick</td><td>2</td><td>Popular science</td><td><i>2</i></td></tr><tr><td>The Relational Model for Database Management: Version 2</td><td>Paperback</td><td>E.F.Codd</td><td>British</td><td>39.99</td><td>538</td><td>Thick</td><td>2</td><td>Popular science</td><td><i>2</i></td></tr></tbody></table>

所有不属于候选键的属性都依赖于_Title_，但只有_Price_还依赖于_Format_。为了符合[2NF](/wiki/Second_normal_form "Second normal form")，并消除重复性，每个非候选键的属性都必须依赖于整个候选键，而不仅仅是其中的一部分。

为了使这个表正常化，将**{Title}**作为一个（简单的）候选键（主键），使每个非候选键属性都依赖于整个候选键，并将_Price_删除到一个单独的表中，这样就可以保留它对_Format_的依赖性。

<table><tbody><tr><td><table><caption>Book</caption><tbody><tr><th><u>Title</u></th><th>Author</th><th>Author Nationality</th><th>Pages</th><th>Thickness</th><th>Genre ID</th><th>Genre Name</th><th><i>Publisher ID</i></th></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>Chad Russell</td><td>American</td><td>520</td><td>Thick</td><td>1</td><td>Tutorial</td><td><i>1</i></td></tr><tr><td>The Relational Model for Database Management: Version 2</td><td>E.F.Codd</td><td>British</td><td>538</td><td>Thick</td><td>2</td><td>Popular science</td><td><i>2</i></td></tr></tbody></table></td><td><table><caption>Format - Price</caption><tbody><tr><th><u>Title</u></th><th><u>Format</u></th><th>Price</th></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>Hardcover</td><td>49.99</td></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>E-book</td><td>22.34</td></tr><tr><td>The Relational Model for Database Management: Version 2</td><td>E-book</td><td>13.88</td></tr><tr><td>The Relational Model for Database Management: Version 2</td><td>Paperback</td><td>39.99</td></tr></tbody></table></td></tr></tbody></table>

Now, the **Book** table conforms to [2NF](/wiki/Second_normal_form "Second normal form").

### Satisfying 3NF

**书**表仍有一个转义功能依赖关系（{作者国籍}依赖于{作者}，而{作者}依赖于{标题}）。对于流派也存在类似的违反（{流派名称}依附于{流派ID}，而{流派ID}依附于{标题}）。因此，**书**表不在3NF中。为了使其在3NF中，让我们使用下面的表结构，从而消除转义功能依赖，将{作者国籍}和{流派名称}放在各自的表中。

<table><caption>Book<table><tbody><tr><th><u>Title</u></th><th>Author</th><th>Pages</th><th>Thickness</th><th>Genre ID</th><th><i>Publisher ID</i></th></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>Chad Russell</td><td>520</td><td>Thick</td><td>1</td><td><i>1</i></td></tr><tr><td>The Relational Model for Database Management: Version 2</td><td>E.F.Codd</td><td>538</td><td>Thick</td><td>2</td><td><i>2</i></td></tr></tbody></table></caption><tbody><tr><td></td></tr></tbody></table>

<table><tbody><tr><td><table><caption>Format - Price</caption><tbody><tr><th><u>Title</u></th><th><u>Format</u></th><th>Price</th></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>Hardcover</td><td>49.99</td></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>E-book</td><td>22.34</td></tr><tr><td>The Relational Model for Database Management: Version 2</td><td>E-book</td><td>13.88</td></tr><tr><td>The Relational Model for Database Management: Version 2</td><td>Paperback</td><td>39.99</td></tr></tbody></table></td></tr></tbody></table>

<table><caption>Author</caption><tbody><tr><th>Author</th><th>Author Nationality</th></tr><tr><td>Chad Russell</td><td>American</td></tr><tr><td>E.F.Codd</td><td>British</td></tr></tbody></table>

<table><caption>Genre</caption><tbody><tr><th>Genre ID</th><th>Genre Name</th></tr><tr><td>1</td><td>Tutorial</td></tr><tr><td>2</td><td>Popular science</td></tr></tbody></table>

### Satisfying EKNF

Main article: [Elementary key normal form](/wiki/Elementary_key_normal_form "Elementary key normal form")

The elementary key normal form (EKNF) falls strictly between 3NF and BCNF and is not much discussed in the literature. It is intended _“to capture the salient qualities of both 3NF and BCNF”_ while avoiding the problems of both (namely, that 3NF is “too forgiving” and BCNF is “prone to computational complexity”). Since it is rarely mentioned in literature, it is not included in this example.[[14]](#cite_note-14)

### Satisfying 4NF

假设数据库是由一家图书零售商的特许经营权所拥有，该特许经营权有几个加盟商，他们在不同的地点拥有商店。因此，零售商决定增加一个表，其中包含不同地点的图书供应情况的数据。

<table><caption align="top"><b>Franchisee - Book Location</b></caption><tbody><tr><th><u>Franchisee ID</u></th><th><u>Title</u></th><th><u>Location</u></th></tr><tr><td>1</td><td>Beginning MySQL Database Design and Optimization</td><td>California</td></tr><tr><td>1</td><td>Beginning MySQL Database Design and Optimization</td><td>Florida</td></tr><tr><td>1</td><td>Beginning MySQL Database Design and Optimization</td><td>Texas</td></tr><tr><td>1</td><td>The Relational Model for Database Management: Version 2</td><td>California</td></tr><tr><td>1</td><td>The Relational Model for Database Management: Version 2</td><td>Florida</td></tr><tr><td>1</td><td>The Relational Model for Database Management: Version 2</td><td>Texas</td></tr><tr><td>2</td><td>Beginning MySQL Database Design and Optimization</td><td>California</td></tr><tr><td>2</td><td>Beginning MySQL Database Design and Optimization</td><td>Florida</td></tr><tr><td>2</td><td>Beginning MySQL Database Design and Optimization</td><td>Texas</td></tr><tr><td>2</td><td>The Relational Model for Database Management: Version 2</td><td>California</td></tr><tr><td>2</td><td>The Relational Model for Database Management: Version 2</td><td>Florida</td></tr><tr><td>2</td><td>The Relational Model for Database Management: Version 2</td><td>Texas</td></tr><tr><td>3</td><td>Beginning MySQL Database Design and Optimization</td><td>Texas</td></tr></tbody></table>

由于这个表结构由[复合主键](/wiki/Compound_key "复合主键")组成，所以它不包含任何非键属性，而且它已经在[BCNF](/wiki/Boyce%E2%80%93Codd_normal_form "Boyce-Codd normal form")中了(因此也满足前面所有的[normal forms](/wiki/Database_normalization#Normal_forms "数据库规范化"))。然而，如果我们假设每个区域都提供了所有可用的书籍，我们可能会注意到，**Title**并未明确地绑定到某个**Location**，因此该表不满足[4NF](/wiki/Fourth_normal_form "第四种正常形式")。

That means that, to satisfy the [fourth normal form](/wiki/Fourth_normal_form "Fourth normal form"), this table needs to be decomposed as well:

<table><tbody><tr><td><table><caption align="top"><b>Franchisee - Book</b></caption><tbody><tr><th><u>Franchisee ID</u></th><th><u>Title</u></th></tr><tr><td>1</td><td>Beginning MySQL Database Design and Optimization</td></tr><tr><td>1</td><td>The Relational Model for Database Management: Version 2</td></tr><tr><td>2</td><td>Beginning MySQL Database Design and Optimization</td></tr><tr><td>2</td><td>The Relational Model for Database Management: Version 2</td></tr><tr><td>3</td><td>Beginning MySQL Database Design and Optimization</td></tr></tbody></table></td><td><table><caption align="top">Franchisee - Location</caption><tbody><tr><th><u>Franchisee ID</u></th><th><u>Location</u></th></tr><tr><td>1</td><td>California</td></tr><tr><td>1</td><td>Florida</td></tr><tr><td>1</td><td>Texas</td></tr><tr><td>2</td><td>California</td></tr><tr><td>2</td><td>Florida</td></tr><tr><td>2</td><td>Texas</td></tr><tr><td>3</td><td>Texas</td></tr></tbody></table></td></tr></tbody></table>

Now, every record is unambiguously identified by a [superkey](/wiki/Superkey "Superkey"), therefore [4NF](/wiki/4NF "4NF") is satisfied.[[15]](#cite_note-:3-15)

### Satisfying ETNF

*   假设特许经营商也可以向不同的供应商订购图书。让这种关系也受到以下约束。

    * 如果某家供应商供应某种书刊，
    * 并将**标题**提供给**特许经营商**。
    * 而**特许经营商**由**供应商提供，**
    * 然后，**供应商**将**标题**提供给**特许经营商**。

<table><caption>Supplier - Book - Franchisee</caption><tbody><tr><th><u>Supplier ID</u></th><th><u>Title</u></th><th><u>Franchisee ID</u></th></tr><tr><td>1</td><td>Beginning MySQL Database Design and Optimization</td><td>1</td></tr><tr><td>2</td><td>The Relational Model for Database Management: Version 2</td><td>2</td></tr><tr><td>3</td><td>Learning SQL</td><td>3</td></tr></tbody></table>

这个表是[4NF](/wiki/Fourth_normal_form "第四正态形式")，但供应商ID等于其投影的连接。**{{Supplier ID，Book}，{Book，Franchisee ID}，{Franchisee ID，Supplier ID}}.**该联接依赖的任何组件都不是[superkey](/wiki/Superkey "超级键")（唯一的[超级键](/wiki/Superkey "超级键")是整个标题），所以该表不满足ETNF，可以进一步分解：

<table><tbody><tr><td><table><caption>Supplier - Book</caption><tbody><tr><th><u>Supplier ID</u></th><th><u>Title</u></th></tr><tr><td>1</td><td>Beginning MySQL Database Design and Optimization</td></tr><tr><td>2</td><td>The Relational Model for Database Management: Version 2</td></tr><tr><td>3</td><td>Learning SQL</td></tr></tbody></table></td><td><table><caption>Book - Franchisee</caption><tbody><tr><th><u>Title</u></th><th><u>Franchisee ID</u></th></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>1</td></tr><tr><td>The Relational Model for Database Management: Version 2</td><td>2</td></tr><tr><td>Learning SQL</td><td>3</td></tr></tbody></table></td><td><table><caption>Franchisee - Supplier</caption><tbody><tr><th><u>Supplier ID</u></th><th><u>Franchisee ID</u></th></tr><tr><td>1</td><td>1</td></tr><tr><td>2</td><td>2</td></tr><tr><td>3</td><td>3</td></tr></tbody></table></td></tr></tbody></table>

The decomposition produces [ETNF](/w/index.php?title=Essential_tuple_normal_form&action=edit&redlink=1 "Essential tuple normal form (page does not exist)") compliance.

### Satisfying 5NF

To spot a table not satisfying the [5NF](/wiki/Fifth_normal_form "Fifth normal form"), it is usually necessary to examine the data thoroughly. Suppose the table from [4NF example](/wiki/Database_normalization#Satisfying_4NF "Database normalization") with a little modification in data and let's examine if it satisfies [5NF](/wiki/Fifth_normal_form "Fifth normal form"):

<table><caption align="top"><b>Franchisee - Book Location</b></caption><tbody><tr><th><u>Franchisee ID</u></th><th><u>Title</u></th><th><u>Location</u></th></tr><tr><td>1</td><td>Beginning MySQL Database Design and Optimization</td><td>California</td></tr><tr><td>1</td><td>Learning SQL</td><td>California</td></tr><tr><td>1</td><td>The Relational Model for Database Management: Version 2</td><td>Texas</td></tr><tr><td>2</td><td>The Relational Model for Database Management: Version 2</td><td>California</td></tr></tbody></table>

If we decompose this table, we lower redundancies and get the following two tables:

<table><tbody><tr><td><table><caption align="top"><b>Franchisee - Book</b></caption><tbody><tr><th><u>Franchisee ID</u></th><th><u>Title</u></th></tr><tr><td>1</td><td>Beginning MySQL Database Design and Optimization</td></tr><tr><td>1</td><td>Learning SQL</td></tr><tr><td>1</td><td>The Relational Model for Database Management: Version 2</td></tr><tr><td>2</td><td>The Relational Model for Database Management: Version 2</td></tr></tbody></table></td><td><table><caption align="top"><b>Franchisee - Location</b></caption><tbody><tr><th><u>Franchisee ID</u></th><th><u>Location</u></th></tr><tr><td>1</td><td>California</td></tr><tr><td>1</td><td>Texas</td></tr><tr><td>2</td><td>California</td></tr></tbody></table></td></tr></tbody></table>

What happens if we try to join these tables? The query would return the following data:

<table><caption align="top"><b>Franchisee - Book - Location JOINed</b></caption><tbody><tr><th><u>Franchisee ID</u></th><th><u>Title</u></th><th><u>Location</u></th></tr><tr><td>1</td><td>Beginning MySQL Database Design and Optimization</td><td>California</td></tr><tr><td>1</td><td>Learning SQL</td><td>California</td></tr><tr><td>1</td><td>The Relational Model for Database Management: Version 2</td><td>California</td></tr><tr><td>1</td><td>The Relational Model for Database Management: Version 2</td><td>Texas</td></tr><tr><td>1</td><td>Learning SQL</td><td>Texas</td></tr><tr><td>1</td><td>Beginning MySQL Database Design and Optimization</td><td>Texas</td></tr><tr><td>2</td><td>The Relational Model for Database Management: Version 2</td><td>California</td></tr></tbody></table>

Apparently, the JOIN returns three more rows than it should - let's try to add another table to clarify the relation. We end up with three separate tables:  

<table><tbody><tr><td><table><caption align="top"><b>Franchisee - Book</b></caption><tbody><tr><th><u>Franchisee ID</u></th><th><u>Title</u></th></tr><tr><td>1</td><td>Beginning MySQL Database Design and Optimization</td></tr><tr><td>1</td><td>Learning SQL</td></tr><tr><td>1</td><td>The Relational Model for Database Management: Version 2</td></tr><tr><td>2</td><td>The Relational Model for Database Management: Version 2</td></tr></tbody></table></td><td><table><caption align="top"><b>Franchisee - Location</b></caption><tbody><tr><th><u>Franchisee ID</u></th><th><u>Location</u></th></tr><tr><td>1</td><td>California</td></tr><tr><td>1</td><td>Texas</td></tr><tr><td>2</td><td>California</td></tr></tbody></table></td><td><table><caption align="top"><b>Location - Book</b></caption><tbody><tr><th><u>Location</u></th><th><u>Title</u></th></tr><tr><td>California</td><td>Beginning MySQL Database Design and Optimization</td></tr><tr><td>California</td><td>Learning SQL</td></tr><tr><td>California</td><td>The Relational Model for Database Management: Version 2</td></tr><tr><td>Texas</td><td>The Relational Model for Database Management: Version 2</td></tr></tbody></table></td></tr></tbody></table>

What will the JOIN return now? It actually is not possible to join these three tables. That means it wasn't possible to decompose the **Franchisee - Book Location** without data loss, therefore the table already satisfies [5NF](/wiki/5NF "5NF").[[15]](#cite_note-:3-15)

C.J. Date has argued that only a database in 5NF is truly "normalized".[[17]](#cite_note-17)

### Satisfying DKNF

Let's have a look at the **Book** table from previous examples and see if it satisfies the [Domain-key normal form](/wiki/Domain-key_normal_form "Domain-key normal form"):

<table><caption>Book</caption><tbody><tr><th><u>Title</u></th><th><b>Pages</b></th><th>Thickness</th><th><i>Genre ID</i></th><th><i>Publisher ID</i></th></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>520</td><td>Thick</td><td><i>1</i></td><td><i>1</i></td></tr><tr><td>The Relational Model for Database Management: Version 2</td><td>538</td><td>Thick</td><td><i>2</i></td><td><i>2</i></td></tr><tr><td>Learning SQL</td><td>338</td><td>Slim</td><td><i>1</i></td><td><i>3</i></td></tr><tr><td>SQL Cookbook</td><td>636</td><td>Thick</td><td><i>1</i></td><td><i>3</i></td></tr></tbody></table>

Logically, **Thickness** is determined by number of pages. That means it depends on **Pages** which is not a key. Let's set an example convention saying a book up to 350 pages is considered"slim"and a book over 350 pages is considered"thick".

This convention is technically a constraint but it is neither a domain constraint nor a key constraint; therefore we cannot rely on domain constraints and key constraints to keep the data integrity.

In other words - nothing prevents us from putting, for example, "Thick" for a book with only 50 pages - and this makes the table violate [DKNF](/wiki/Domain-key_normal_form "Domain-key normal form").

To solve this, we can create a table holding enumeration that defines the **Thickness** and remove that column from the original table:

<table><tbody><tr><td><table><caption>Thickness Enum</caption><tbody><tr><th><u>Thickness</u></th><th>Min pages</th><th>Max pages</th></tr><tr><td>Slim</td><td>1</td><td>350</td></tr><tr><td>Thick</td><td>351</td><td>999,999,999,999</td></tr></tbody></table></td><td><table><caption>Book - Pages - Genre - Publisher</caption><tbody><tr><th><u>Title</u></th><th>Pages</th><th><i>Genre ID</i></th><th><i>Publisher ID</i></th></tr><tr><td>Beginning MySQL Database Design and Optimization</td><td>520</td><td><i>1</i></td><td><i>1</i></td></tr><tr><td>The Relational Model for Database Management: Version 2</td><td>538</td><td><i>2</i></td><td><i>2</i></td></tr><tr><td>Learning SQL</td><td>338</td><td><i>1</i></td><td><i>3</i></td></tr><tr><td>SQL Cookbook</td><td>636</td><td><i>1</i></td><td><i>3</i></td></tr></tbody></table></td></tr></tbody></table>

That way, the domain integrity violation has been eliminated, and the table is in [DKNF](/wiki/Domain-key_normal_form "Domain-key normal form").

### Satisfying 6NF

A simple and intuitive definition of the [sixth normal form](/wiki/Sixth_normal_form "Sixth normal form") is that _"a table is in [6NF](/wiki/Sixth_normal_form "Sixth normal form") when **the row contains the Primary Key, and at most one other attribute"**_**.**[[18]](#cite_note-18)

That means, for example, the **Publisher** table designed while [creating the 1NF](#Satisfying_1NF)

<table><caption>Publisher</caption><tbody><tr><th><u>Publisher_ID</u></th><th>Name</th><th>Country</th></tr><tr><td>1</td><td>Apress</td><td>USA</td></tr></tbody></table>

needs to be further decomposed into two tables:

<table><tbody><tr><td><table><caption>Publisher</caption><tbody><tr><th><u>Publisher_ID</u></th><th>Name</th></tr><tr><td>1</td><td>Apress</td></tr></tbody></table></td><td><table><caption>Publisher country</caption><tbody><tr><th><u>Publisher_ID</u></th><th>Country</th></tr><tr><td>1</td><td>USA</td></tr></tbody></table></td></tr></tbody></table>

The obvious drawback of 6NF is the proliferation of tables required to represent the information on a single entity. If a table in 5NF has one primary key column and N attributes, representing the same information in 6NF will require N tables; multi-field updates to a single conceptual record will require updates to multiple tables; and inserts and deletes will similarly require operations across multiple tables. For this reason, in databases intended to serve [Online Transaction Processing](/wiki/OLTP "OLTP") needs, 6NF should not be used.

However, in [data warehouses](/wiki/Data_warehouses "Data warehouses"), which do not permit interactive updates and which are specialized for fast query on large data volumes, certain DBMSs use an internal 6NF representation - known as a [Columnar data store](/wiki/Column-oriented_DBMS "Column-oriented DBMS"). In situations where the number of unique values of a column is far less than the number of rows in the table, column-oriented storage allow significant savings in space through data compression. Columnar storage also allows fast execution of range queries (e.g., show all records where a particular column is between X and Y, or less than X.)

In all these cases, however, the database designer does not have to perform 6NF normalization manually by creating separate tables. Some DBMSs that are specialized for warehousing, such as [Sybase IQ](/wiki/Sybase_IQ "Sybase IQ"), use columnar storage by default, but the designer still sees only a single multi-column table. Other DBMSs, such as Microsoft SQL Server 2012 and later, let you specify a "columnstore index" for a particular table.[[19]](#cite_note-19)

See also
--------

*   [Denormalization](/wiki/Denormalization "Denormalization")
*   [Database refactoring](/wiki/Database_refactoring "Database refactoring")

*   [Lossless join decomposition](/wiki/Lossless_join_decomposition "Lossless join decomposition")

# Denormalization

去规范化是一种用于先前已规范化的数据库上的策略，以提高性能。在计算中，去规范化是指通过增加数据的冗余副本或对数据进行分组，以损失一些写性能为代价，试图提高数据库的读性能的过程。 [1][2]在需要进行非常多的读操作的关系型数据库软件中，去规范化通常是出于性能或可扩展性的考虑。规范化与非规范化形式的不同之处在于，去规范化的好处只有在一个原本已经规范化的数据模型上才能完全实现。

## 执行情况

一个规范化的设计通常会将不同但相关的信息 "存储 "在不同的逻辑表中（称为关系）。如果这些关系被物理存储为单独的磁盘文件，那么完成一个从几个关系中获取信息的数据库查询（连接操作）可能会很慢。如果连接了许多关系，可能会慢得令人望而却步。有两种策略来处理这个问题。

## DBMS支持

一种方法是保持逻辑设计的规范化，但允许数据库管理系统（DBMS）在磁盘上存储额外的冗余信息以优化查询响应。在这种情况下，DBMS软件有责任确保任何冗余副本保持一致。这种方法通常在SQL中以索引视图（Microsoft SQL Server）或实体化视图（Oracle、PostgreSQL）的形式实现。除其他因素外，视图可能以方便查询的格式来表示信息，索引确保针对视图的查询在物理上得到优化。

## DBA的实施

另一种方法是将逻辑数据设计非正常化。如果小心翼翼，这可以在查询响应方面实现类似的改进，但代价是--现在数据库设计者有责任确保去正常化后的数据库不会变得不一致。这是通过在数据库中创建称为约束的规则来实现的，这些规则规定了必须如何保持信息的冗余副本同步，这可能很容易使去正常化过程变得毫无意义。正是由于数据库设计的逻辑复杂性的增加，以及额外约束的复杂性的增加，使得这种方法变得危险。此外，约束引入了一种权衡，加快了读（SQL中的SELECT），同时减慢了写（INSERT、UPDATE和DELETE）。这意味着，在重度写入负载下的去规范化数据库可能会比功能上等效的规范化数据库提供更差的性能。