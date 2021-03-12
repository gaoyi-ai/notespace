---
title: Lossless join decomposition
categories:
- DB
- NF
tags:
- wiki
date: 2021/3/10 20:00:17
updated: 2021/3/12 12:00:17
---



在数据库设计中，无损连接分解是将一个关系R分解为关系R1，R2，这样两个较小关系的自然连接就会产生回原始关系。这对于从数据库中安全地去除冗余，同时保留原始数据来说是非常重要的。

### Check 1: Verify join explicitly

Projecting on R1 and R2, and joining them back, results in the relation you started with.