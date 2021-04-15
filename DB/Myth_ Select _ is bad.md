---
title: Select * is bad
categories:
- DB
- Optimization
tags:
- select
date: 2021/4/14 20:00:14
updated: 2021/4/14 12:00:14
---



> [use-the-index-luke.com](https://use-the-index-luke.com/blog/2013-08/its-not-about-the-star-stupid)

This is one of the most persistent myths I’ve seen in the field. It’s there for decades. If a myth is alive that long there must be some truth behind it. So, what could be bad about `select *`? Let’s have a closer look.

We all know that selecting “*” is just a short-hand for selecting all columns. Believe it or not, this makes a big difference to many people. So, lets first rephrase the question using this “finding”:

Why is it bad to select all columns?

In fact, there are a few very good reasons it is bad to select all columns if you don’t need them. And they all boil down to performance. What is surprising, however, is that the performance impact can be huge.

###### Up to 100x slower when preventing an Index-Only Scan

Broadly speaking, the less columns you ask for, the less data must be loaded from disk when processing your query. However, this relationship is _non-linear_.

Quite often, selecting from a table involves two steps: (1) use an index to find the address where the selected rows are stored; (2) load the selected rows from the table. Now imagine that you are just selecting columns that are present in the index. Why should the database still perform the second step? In fact, most databases don’t. They can process your query just with the information stored in the index—hence [index-only scan](https://use-the-index-luke.com/sql/clustering/index-only-scan-covering-index).

But why should an index-only scan be 100 times faster? Simple: an ideal index stores the selected rows next to each other. It’s not uncommon that each index page holds about 100 rows—a ballpark figure; it depends on the size of the indexed columns. Nonetheless, it means that one IO operation might fetch 100 rows. The table data, on the other hand, is not organized like that ([exceptions](https://use-the-index-luke.com/sql/clustering/index-organized-clustered-index)). Here it is quite common that a page just contains one of the selected rows—along with many other rows that are of no interest for the particular query. So, the reason an Index-Only Scan can be 100 times faster is that an index access can easily deliver 100 rows per IO while the table access typically just fetches a few rows per IO.

If you select a single column that’s not in the index, the database cannot do an index-only scan. If you select all columns, … , well I guess you know the answer.

Further, some databases store large objects in a separate place (e.g., LOBs in Oracle). Accessing those causes an extra IO too.

###### Up to 5x slower when bloating server memory footprint

Although databases avoid storing the result in the server’s main memory—instead they deliver each row after loading and forget about it again—it is sometimes inevitable. Sorting, for example, needs to keep all rows—and all selected columns—in memory to do the job. Once again, the more columns you select, the more memory the database needs. In the worst case, the database might even need to do an external sort on disk.

However, most database are extremely well tuned for this kind of workload. Although I’ve seen a sorting speed-up of factor two quite often—just by removing a few unused columns—I cannot remember having got more than factor five. However, it’s not just sorting, [hash joins](https://use-the-index-luke.com/sql/join/hash-join-partial-objects) are rather sensitive to memory bloat too. Don’t know what that is? Please read [this article](https://use-the-index-luke.com/sql/join/hash-join-partial-objects).

These are just the two top issues from database perspective. Remember that the client needs to process the data too—which might put a considerable load on garbage collection.

Now that we have established a common understanding of why selecting everything is bad for performance, you may ask why it is listed as a myth? It’s because many people think the star is the bad thing. Further they believe they are not committing this crime because their ORM lists all columns by name anyway. In fact, the crime is to select all columns without thinking about it—and most [ORMs readily commit this crime](https://use-the-index-luke.com/blog/2013-04/the-two-top-performance-problems-caused-by-ORM-tools) on behalf of their users.

The reason `select *` actually is bad—hence the reason the myth is very resistant—is because the star is just used as an allegory for “selecting everything without thinking about it”. This is the bad thing. But if you need a more catch phrase to remember the truth behind this myth, take this:

It’s not about the star, stupid!

_If you like my way to explain things, you’ll love [SQL Performance Explained](https://sql-performance-explained.com/?utm_source=use-the-index-luke.com&utm_campaign=myth-select-star&utm_medium=web)._

#### Update 2013-11-03 - Is the star itself also bad?

Besides the performance issues mentioned above that are not caused by the star (asterisk) itself, the star itself might still cause other trouble. E.g. with software that expects the columns in a specific order when you add or drop a column. However, from my observation I’d say these issues are rather well understood in the field and usually easily identify (software stops working) fixed.

The focus of the article is on very subtle issues which are hardly understood, hard to find, and often even hard to fix (e.g. when using ORM tools). The main goal of this article is to stop people thinking about the star itself. Once people start to name the wanted columns explicitly to gain the performance benefit explained above, the issues caused by the star itself are also gone. Hence, I’ve felt no reason to add a discussion about these issues here—that’s just a distraction from the arguments that I wanted to explain with the article.