---
title: What is difference between memorization and dynamic programming?
categories:
- DSA
- Algorithm
- Dynamic Programming
tags:
- dynamic programming
date: 2021/6/25
---



# What is difference between memorization and dynamic programming?

**Memorization** is a term describing an optimization technique where you cache previously computed results, and return the cached result when the same computation is needed again.
Memorization 是一个术语，描述了一种优化技术，您可以缓存先前计算的结果，并在再次需要相同的计算时返回缓存的结果。

**Dynamic programming** is a technique for solving problems of recursive nature, iteratively and is applicable when the computations of the subproblems overlap.
动态规划是一种以迭代方式解决递归性质问题的技术，适用于子问题的计算重叠时。

Dynamic programming is *typically* implemented using tabulation, but can also be implemented using memorization. So as you can see, neither one is a "subset" of the other.

------

A reasonable follow-up question is: **What is the difference between tabulation (the typical dynamic programming technique) and memorization?**

When you solve a dynamic programming problem using tabulation you solve the problem "**bottom up**", i.e., by solving all related sub-problems first, typically by filling up an *n*-dimensional table. Based on the results in the table, the solution to the "top" / original problem is then computed.

If you use memorization to solve the problem you do it by maintaining a map of already solved sub problems. You do it "**top down**" in the sense that you solve the "top" problem first (which typically recurses down to solve the sub-problems).

A good slide from ~~[here](http://web2.cc.nctu.edu.tw/~claven/course/Algorithm/unit13.ppt)~~ (link is now dead, slide is still good though):

> - If all subproblems must be solved at least once, a bottom-up dynamic-programming algorithm usually outperforms a top-down memorized algorithm by a constant factor
>     - No overhead for recursion and less overhead for maintaining table
>     - There are some problems for which the regular pattern of table accesses in the dynamic-programming algorithm can be exploited to reduce the time or space requirements even further
> - If some subproblems in the subproblem space need not be solved at all, the memorized solution has the advantage of solving only those subproblems that are definitely required

**Additional resources:**

- Wikipedia: [Memorization](http://en.wikipedia.org/wiki/Memoization), [Dynamic Programming](http://en.wikipedia.org/wiki/Dynamic_programming)
- Related SO Q/A: [Memorization or Tabulation approach for Dynamic programming](https://stackoverflow.com/questions/12042356/memoization-or-tabulation-approach-for-dynamic-programming)

