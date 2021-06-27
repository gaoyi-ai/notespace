---
title: Sentinel node
categories:
- DSA
- Algorithm
tags:
- Sentinel node
date: 2021/6/26
---



# Sentinel node

当要删除的一个或多个节点位于链表的头部时，事情会变得复杂。

![Sentinel node-1](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/Sentinel%20node-1.png)

可以通过哨兵节点去解决它，哨兵节点广泛应用于树和链表中，如伪头、伪尾、标记等，它们是纯功能的，通常不保存任何数据，其主要目的是使链表标准化，如使链表永不为空、永不无头、简化插入和删除。

![Sentinel node-2](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/Sentinel%20node-2.png)

 在这里哨兵节点将被用于伪头。