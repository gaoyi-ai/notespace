---
title: SGD with Problem-specific
categories:
- Optimization
tags:
- SGD
date: 2021/3/7 10:00:00
updated: 2021/3/12 16:00:00
---

随机梯度下降是一种为[机器学习](https://deepai.org/machine-learning-glossary-and-terms/machine-learning)算法寻找最佳参数配置的方法。它迭代地对机器学习网络配置进行小幅调整，以降低网络的误差。 

误差函数很少像典型的抛物线那样简单。大多数情况下，它们有很多山丘和山谷，就像这里的函数图。在这张图中，如果[真梯度下降](https://deepai.org/machine-learning-glossary-and-terms/true-gradient-descent)从图的左侧开始，它就会停在左边的山谷处，因为无论你从这个点往哪个方向走，都必须往上走。这个点被称为局部最小点。然而，在图形中还存在着另一个更低的点。整个图形中的最低点就是全局最低点，这也是随机梯度下降法试图找到的。 

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/stochastic-gradient-descent-2840469.jpg)  

随机梯度下降法试图在每个训练点之后通过调整网络的配置来寻找全局最小值。这种方法不是减少整个数据集的误差，或者找到梯度，而是仅仅通过近似于随机选择的批次（可能小到单个训练样本）的梯度来减少误差。  在实际操作中，随机选择是通过随机洗牌数据集，并以循序渐进的方式对各批次进行工作来实现的。

[启发式](https://deepai.org/machine-learning-glossary-and-terms/heuristics)，如果网络弄错了一个训练样本，它将更新配置，以利于将来弄对它。然而，配置更新可能会以弄错其他问题为代价，从而增加网络的整体误差。因此，并不是每次训练迭代都可能通过随机梯度下降算法改善网络。

另一方面，随机梯度下降可以调整网络参数，使模型脱离局部最小值，走向全局最小值。回过头来看上图中的凹函数，在处理完一个训练例子后，算法可能会选择在图上向右移动，以摆脱我们所处的局部最小值。尽管这样做会增加网络的误差，但它可以让网络移过山头。这将允许进一步的训练，使梯度下降向全局最小值移动。 

随机梯度下降的一个好处是，它所需要的计算量比真正的梯度下降要少得多（因此计算速度更快），同时通常仍会收敛到一个最小值（虽然不一定是全局最小值）。

> [deepai.org](https://deepai.org/machine-learning-glossary-and-terms/stochastic-gradient-descent)

