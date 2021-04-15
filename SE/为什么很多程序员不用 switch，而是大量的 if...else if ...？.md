---
title: 为什么很多程序员不用 switch，而是大量的 if...else if ...？
categories:
- SE
tags:
- if else
- switch
date: 2021/4/14 20:00:14
updated: 2021/4/14 12:00:14
---



# 为什么很多程序员不用 switch，而是大量的 if...else if ...？

分支非常多的 if 和 else if 往往并不是一次写出来的，而是每次增加新功能时就开个新的分支。对于每一个增加分支的人来说，他们都不觉得自己有责任要重构整段代码，因为他们只想用最低的成本把自己要做的事情做完，于是代码质量变得越来越低。

一般来说，如果 if 和 else if 分支超过 3 个就可以考虑写成 switch。如果 switch 的分支超过 10 个就可以考虑写成 config，然后专门写一个函数根据 config 来做 mapping。如果需要进行的映射逻辑很复杂，但使用频率很高，可以考虑做一个专门的 rule engine 来处理这件事情，或者是一门 DSL。