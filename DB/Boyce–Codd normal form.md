---
title: BCNF
categories:
- DB
- NF
tags:
- BCNF
- wiki
date: 2021/3/10 20:00:17
updated: 2021/3/12 12:00:17
---

如果一个关系模式处于BCNF中，那么所有基于功能依赖的冗余都已经被删除，尽管其他类型的冗余可能仍然存在。如果且仅如果对于它的每一个依赖关系X → Y，至少以下条件之一成立，那么关系模式R就处于Boyce-Codd规范形式：[2] 。

*   _X_ → _Y_ is a trivial functional dependency (Y ⊆ X),
*   _X_ is a [superkey](/wiki/Superkey "Superkey") for schema _R_.

3NF table always meeting BCNF (Boyce–Codd normal form)
------------------------------------------------------

只有在极少数情况下，3NF表才不符合BCNF的要求。一个没有多个重叠候选键的3NF表可以保证在BCNF中，[3]根据其功能依赖性，一个有两个或多个重叠候选键的3NF表可能在BCNF中，也可能不在BCNF中。

An example of a 3NF table that does not meet BCNF is:

<table><caption>Today's court bookings</caption><tbody><tr><th>Court</th><th>Start time</th><th>End time</th><th>Rate type</th></tr><tr><td>1</td><td>09:30</td><td>10:30</td><td>SAVER</td></tr><tr><td>1</td><td>11:00</td><td>12:00</td><td>SAVER</td></tr><tr><td>1</td><td>14:00</td><td>15:30</td><td>STANDARD</td></tr><tr><td>2</td><td>10:00</td><td>11:30</td><td>PREMIUM-B</td></tr><tr><td>2</td><td>11:30</td><td>13:30</td><td>PREMIUM-B</td></tr><tr><td>2</td><td>15:00</td><td>16:30</td><td>PREMIUM-A</td></tr></tbody></table>

*   Each row in the table represents a court booking at a tennis club. That club has one hard court (Court 1) and one grass court (Court 2)
*   A booking is defined by its Court and the period for which the Court is reserved
*   Additionally, each booking has a Rate Type associated with it. There are four distinct rate types:
    *   SAVER, for Court 1 bookings made by members
    *   STANDARD, for Court 1 bookings made by non-members
    *   PREMIUM-A, for Court 2 bookings made by members
    *   PREMIUM-B, for Court 2 bookings made by non-members

The table's [superkeys](/wiki/Superkey "Superkey") are:

*   S1 = {Court, Start time}
*   S2 = {Court, End time}
*   S3 = {Rate type, Start time}
*   S4 = {Rate type, End time}
*   S5 = {Court, Start time, End time}
*   S6 = {Rate type, Start time, End time}
*   S7 = {Court, Rate type, Start time}
*   S8 = {Court, Rate type, End time}
*   ST = {Court, Rate type, Start time, End time}, the trivial superkey

请注意，尽管在上表中，开始时间和结束时间属性的值没有重复，但我们仍然不得不承认，在其他一些日子里，球场1和球场2的两个不同的预订可能在同一时间开始或在同一时间结束。这就是为什么{开始时间}和{结束时间}不能作为表的超级键的原因。

然而，只有S1、S2、S3和S4是候选键（即该关系的最小超键），因为如S1⊂S5，所以S5不能成为候选键。

回想一下，2NF禁止非主属性的部分功能依赖（即在任何候选键中都不出现的属性。见候选键）对候选键的依赖性，3NF禁止非主属性对候选键的转义功能依赖性。

在今天的法院预约表中，没有非主属性：即所有属性都属于某个候选键。因此，该表同时遵守2NF和3NF。

The table does not adhere to BCNF. This is because of the dependency Rate type → Court in which the determining attribute Rate type – on which Court depends – (1) is neither a candidate key nor a superset of a candidate key and (2) Court is no subset of Rate type.

Dependency Rate type → Court is respected, since a Rate type should only ever apply to a single Court.

The design can be amended so that it meets BCNF:

<table><tbody><tr><td><table><caption>Rate types</caption><tbody><tr><th>Rate type</th><th>Court</th><th>Member flag</th></tr><tr><td>SAVER</td><td>1</td><td>Yes</td></tr><tr><td>STANDARD</td><td>1</td><td>No</td></tr><tr><td>PREMIUM-A</td><td>2</td><td>Yes</td></tr><tr><td>PREMIUM-B</td><td>2</td><td>No</td></tr></tbody></table></td><td><table><caption>Today's bookings</caption><tbody><tr><th>Member flag</th><th>Court</th><th>Start time</th><th>End time</th></tr><tr><td>Yes</td><td>1</td><td>09:30</td><td>10:30</td></tr><tr><td>Yes</td><td>1</td><td>11:00</td><td>12:00</td></tr><tr><td>No</td><td>1</td><td>14:00</td><td>15:30</td></tr><tr><td>No</td><td>2</td><td>10:00</td><td>11:30</td></tr><tr><td>No</td><td>2</td><td>11:30</td><td>13:30</td></tr><tr><td>Yes</td><td>2</td><td>15:00</td><td>16:30</td></tr></tbody></table></td></tr></tbody></table>

The candidate keys for the Rate types table are {Rate type} and {Court, Member flag}; the candidate keys for the Today's bookings table are {Court, Start time} and {Court, End time}. Both tables are in BCNF. When {Rate type} is a key in the Rate types table, having one Rate type associated with two different Courts is impossible, so by using {Rate type} as a key in the Rate types table, the anomaly affecting the original table has been eliminated.

Achievability of BCNF
---------------------

In some cases, a non-BCNF table cannot be decomposed into tables that satisfy BCNF and preserve the dependencies that held in the original table. Beeri and Bernstein showed in 1979 that, for example, a set of functional dependencies {AB → C, C → B} cannot be represented by a BCNF schema.[[4]](#cite_note-Beeri-4)

Consider the following non-BCNF table whose functional dependencies follow the {AB → C, C → B} pattern:

<table><caption>Nearest shops</caption><tbody><tr><th>Person</th><th>Shop type</th><th>Nearest shop</th></tr><tr><td>Davidson</td><td>Optician</td><td>Eagle Eye</td></tr><tr><td>Davidson</td><td>Hairdresser</td><td>Snippets</td></tr><tr><td>Wright</td><td>Bookshop</td><td>Merlin Books</td></tr><tr><td>Fuller</td><td>Bakery</td><td>Doughy's</td></tr><tr><td>Fuller</td><td>Hairdresser</td><td>Sweeney Todd's</td></tr><tr><td>Fuller</td><td>Optician</td><td>Eagle Eye</td></tr></tbody></table>

For each Person / Shop type combination, the table tells us which shop of this type is geographically nearest to the person's home. We assume for simplicity that a single shop cannot be of more than one type.

The candidate keys of the table are:

*   {Person, Shop type},
*   {Person, Nearest shop}.

Because all three attributes are prime attributes (i.e. belong to candidate keys), the table is in 3NF. The table is not in BCNF, however, as the Shop type attribute is functionally dependent on a non-superkey: Nearest shop.

The violation of BCNF means that the table is subject to anomalies. For example, Eagle Eye might have its Shop type changed to "Optometrist" on its "Fuller" record while retaining the Shop type "Optician" on its "Davidson" record. This would imply contradictory answers to the question: "What is Eagle Eye's Shop Type?"Holding each shop's Shop type only once would seem preferable, as doing so would prevent such anomalies from occurring:

<table><tbody><tr><td><table><caption>Shop near person</caption><tbody><tr><th>Person</th><th>Shop</th></tr><tr><td>Davidson</td><td>Eagle Eye</td></tr><tr><td>Davidson</td><td>Snippets</td></tr><tr><td>Wright</td><td>Merlin Books</td></tr><tr><td>Fuller</td><td>Doughy's</td></tr><tr><td>Fuller</td><td>Sweeney Todd's</td></tr><tr><td>Fuller</td><td>Eagle Eye</td></tr></tbody></table></td><td><table><caption>Shop</caption><tbody><tr><th>Shop</th><th>Shop type</th></tr><tr><td>Eagle Eye</td><td>Optician</td></tr><tr><td>Snippets</td><td>Hairdresser</td></tr><tr><td>Merlin Books</td><td>Bookshop</td></tr><tr><td>Doughy's</td><td>Bakery</td></tr><tr><td>Sweeney Todd's</td><td>Hairdresser</td></tr></tbody></table></td></tr></tbody></table>

In this revised design, the "Shop near person" table has a candidate key of {Person, Shop}, and the "Shop" table has a candidate key of {Shop}. Unfortunately, although this design adheres to BCNF, it is unacceptable on different grounds: it allows us to record multiple shops of the same type against the same person. In other words, its candidate keys do not guarantee that the functional dependency {Person, Shop type} → {Shop} will be respected.

A design that eliminates all of these anomalies (but does not conform to BCNF) is possible. This design introduces a new normal form, known as [Elementary Key Normal Form](/wiki/Elementary_Key_Normal_Form "Elementary Key Normal Form").[[5]](#cite_note-Zaniolo-5) This design consists of the original "Nearest shops" table supplemented by the "Shop" table described above. The table structure generated by Bernstein's schema generation algorithm[[6]](#cite_note-Bernstein-6) is actually EKNF, although that enhancement to 3NF had not been recognized at the time the algorithm was designed:

<table><tbody><tr><td><table><caption>Nearest shops</caption><tbody><tr><th>Person</th><th>Shop type</th><th>Nearest shop</th></tr><tr><td>Davidson</td><td>Optician</td><td>Eagle Eye</td></tr><tr><td>Davidson</td><td>Hairdresser</td><td>Snippets</td></tr><tr><td>Wright</td><td>Bookshop</td><td>Merlin Books</td></tr><tr><td>Fuller</td><td>Bakery</td><td>Doughy's</td></tr><tr><td>Fuller</td><td>Hairdresser</td><td>Sweeney Todd's</td></tr><tr><td>Fuller</td><td>Optician</td><td>Eagle Eye</td></tr></tbody></table></td><td><table><caption>Shop</caption><tbody><tr><th>Shop</th><th>Shop type</th></tr><tr><td>Eagle Eye</td><td>Optician</td></tr><tr><td>Snippets</td><td>Hairdresser</td></tr><tr><td>Merlin Books</td><td>Bookshop</td></tr><tr><td>Doughy's</td><td>Bakery</td></tr><tr><td>Sweeney Todd's</td><td>Hairdresser</td></tr></tbody></table></td></tr></tbody></table>

If a [referential integrity constraint](/wiki/Referential_integrity "Referential integrity") is defined to the effect that {Shop type, Nearest shop} from the first table must refer to a {Shop type, Shop} from the second table, then the data anomalies described previously are prevented.

Intractability
--------------

It is [NP-complete](/wiki/NP-complete "NP-complete"), given a database schema in [third normal form](/wiki/Third_normal_form "Third normal form"), to determine whether it violates Boyce–Codd normal form.[[7]](#cite_note-7)
