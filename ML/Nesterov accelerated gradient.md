---
title: Nesterov accelerated gradient
categories:
- Optimization
tags:
- momentum
- nesterov
date: 2021/3/10 20:00:17
updated: 2021/3/12 12:00:17
---



上一篇文章讲解了犹如小球自动滚动下山的动量法（momentum）这篇文章将介绍一种更加 “聪明” 的滚动下山的方式。动量法每下降一步都是由前面下降方向的一个累积和当前点的梯度方向组合而成。于是一位大神（Nesterov）就开始思考，既然每一步都要将两个梯度方向（历史梯度、当前梯度）做一个合并再下降，那为什么不先按照历史梯度往前走那么一小步，按照前面一小步位置的 “超前梯度” 来做梯度合并呢？如此一来，小球就可以先不管三七二十一先往前走一步，在靠前一点的位置看到梯度，然后按照那个位置再来修正这一步的梯度方向。如此一来，有了超前的眼光，小球就会更加”聪明“, 这种方法被命名为 Nesterov accelerated gradient 简称 NAG。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/17649-36485-1.jpg)

![17649-36485-1](images/Nesterov%20accelerated%20gradient/17649-36485-1.jpg)

↑这是 momentum 下降法示意图

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/17649-36486-2.jpg)

![17649-36486-2](images/Nesterov%20accelerated%20gradient/17649-36486-2.jpg)

↑这是 NAG 下降法示意图

看上面一张图仔细想一下就可以明白，Nesterov 动量法和经典动量法的差别就在 B 点和 C 点梯度的不同。

**公式推导**

上图直观的解释了 NAG 的全部内容。下面就把我推导的过程写出来。我推导公式的过程完全符合上面 NAG 的示意图，可以对比参考。

记 v~t~ 为第 t 次迭代梯度的累积  

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/17649-36487-3.jpg)

参数更新公式 $\theta_{new} = \theta - v_i$

公式里的$−γv_{t−1}$ 就是图中 B 到 C 的那一段向量， $θ−γv_{t−1}$ 就是 C 点的坐标（参数）

γ 代表衰减率，η 代表学习率。

**实验**

实验选择了学习率 η=0.01, 衰减率 γ=0.9

![](images/Nesterov%20accelerated%20gradient/17649-36489-4.png)

↑ 这是 Nesterov 方法

![](images/Nesterov%20accelerated%20gradient/17649-36490-5.png)

↑ 这是动量法（momentum)

没有对比就没有伤害，NAG 方法收敛速度明显加快。波动也小了很多。实际上 NAG 方法用到了二阶信息，所以才会有这么好的结果。

实验源码下载 [https://github.com/tsycnh/mlbasic/blob/master/p5%20Nesterov%20momentum.py](https://github.com/tsycnh/mlbasic/blob/master/p5%20Nesterov%20momentum.py "https://github.com/tsycnh/mlbasic/blob/master/p5%20Nesterov%20momentum.py")
