---
title: Float Distribution
categories:
- Organization
tags:
- float distribution
date: 2021/3/22 20:00:17
updated: 2021/3/22 12:00:17
---



# 计算机中的浮点数在数轴上分布均匀吗？

不均匀。越靠近原点越密集，越远离原点越稀疏。

---

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/ee202540b7cbdb71882da0e0d74aca9b_r.jpg" alt="img" style="zoom: 67%;" />

最近正好在对比浮点数据和整型数据分别适合处理什么类型的计算，这是16bit = s(1bit).e(6 bits).f(9 bits)格式在范围内的分布，纵轴使用的是log坐标，绝对值越小的数表示的越精确。