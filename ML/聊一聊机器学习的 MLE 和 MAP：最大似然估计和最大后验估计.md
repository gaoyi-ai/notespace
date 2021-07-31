---
title: 聊一聊机器学习的 MLE 和 MAP 最大似然估计和最大后验估计
categories:
- ML
- MLE
tags:
- MAP
- MLE
date: 2021/7/29
---



> [zhuanlan.zhihu.com](https://zhuanlan.zhihu.com/p/32480810)

**TLDR (or the take away)**
---------------------------

*   频率学派 - Frequentist - Maximum Likelihood Estimation (MLE，最大似然估计)
*   贝叶斯学派 - Bayesian - Maximum A Posteriori (MAP，最大后验估计)

**概述**
------

有时候和别人聊天，对方会说自己有很多机器学习经验，深入一聊发现，对方竟然对 MLE 和 MAP 一知半解，至少在我看来，这位同学的机器学习基础并不扎实。难道在这个深度学习盛行的年代，不少同学都只注重调参数？

现代机器学习的终极问题都会转化为解目标函数的优化问题，MLE 和 MAP 是生成这个函数的很基本的思想，因此我们对二者的认知是非常重要的。这次就和大家认真聊一聊 MLE 和 MAP 这两种 estimator。

**两大学派的争论**
-----------

抽象一点来讲，频率学派和贝叶斯学派对世界的认知有本质不同：频率学派认为世界是确定的，有一个本体，这个本体的真值是不变的，我们的目标就是要找到这个真值或真值所在的范围；而贝叶斯学派认为世界是不确定的，人们对世界先有一个预判，而后通过观测数据对这个预判做调整，我们的目标是要找到最优的描述这个世界的概率分布。

在对事物建模时，用 ![](https://www.zhihu.com/equation?tex=%5Ctheta+) 表示模型的参数，**请注意，解决问题的本质就是求 ![](https://www.zhihu.com/equation?tex=%5Ctheta) 。**那么：

**(1) 频率学派：**存在唯一真值 ![](https://www.zhihu.com/equation?tex=%5Ctheta+) 。举一个简单直观的例子 -- 抛硬币，我们用 ![](https://www.zhihu.com/equation?tex=P%28head%29) 来表示硬币的 bias。抛一枚硬币 100 次，有 20 次正面朝上，要估计抛硬币正面朝上的 bias ![](https://www.zhihu.com/equation?tex=P%28head%29%3D%5Ctheta) 。在频率学派来看，![](https://www.zhihu.com/equation?tex=%5Ctheta) = 20 / 100 = 0.2，很直观。当数据量趋于无穷时，这种方法能给出精准的估计；然而缺乏数据时则可能产生严重的偏差。例如，对于一枚均匀硬币，即 ![](https://www.zhihu.com/equation?tex=%5Ctheta) = 0.5，抛掷 5 次，出现 5 次正面 (这种情况出现的概率是 1/2^5=3.125%)，频率学派会直接估计这枚硬币 ![](https://www.zhihu.com/equation?tex=%5Ctheta) = 1，出现严重错误。

**(2) 贝叶斯学派：** ![](https://www.zhihu.com/equation?tex=%5Ctheta) 是一个随机变量，符合一定的概率分布。在贝叶斯学派里有两大输入和一大输出，输入是先验 (prior) 和似然 (likelihood)，输出是后验 (posterior)。_先验_，即 ![](https://www.zhihu.com/equation?tex=P%28%5Ctheta%29) ，指的是在没有观测到任何数据时对 ![](https://www.zhihu.com/equation?tex=%5Ctheta) 的预先判断，例如给我一个硬币，一种可行的先验是认为这个硬币有很大的概率是均匀的，有较小的概率是是不均匀的；_似然_，即 ![](https://www.zhihu.com/equation?tex=P%28X%7C%5Ctheta%29) ，是假设 ![](https://www.zhihu.com/equation?tex=%5Ctheta) 已知后我们观察到的数据应该是什么样子的；_后验_，即 ![](https://www.zhihu.com/equation?tex=P%28%5Ctheta%7CX%29) ，是最终的参数分布。贝叶斯估计的基础是贝叶斯公式，如下：

![](https://www.zhihu.com/equation?tex=P%28%5Ctheta%7CX%29%3D%5Cfrac%7BP%28X%7C%5Ctheta%29+%5Ctimes+P%28%5Ctheta%29%7D%7BP%28X%29%7D)

同样是抛硬币的例子，对一枚均匀硬币抛 5 次得到 5 次正面，如果先验认为大概率下这个硬币是均匀的 (例如最大值取在 0.5 处的 Beta 分布)，那么 ![](https://www.zhihu.com/equation?tex=P%28head%29) ，即 ![](https://www.zhihu.com/equation?tex=P%28%5Ctheta%7CX%29) ，是一个 distribution，最大值会介于 0.5~1 之间，而不是武断的 ![](https://www.zhihu.com/equation?tex=%5Ctheta) = 1。

这里有两点值得注意的地方：

*   随着数据量的增加，参数分布会越来越向数据靠拢，先验的影响力会越来越小
*   如果先验是 uniform distribution，则贝叶斯方法等价于频率方法。因为直观上来讲，先验是 uniform distribution 本质上表示对事物没有任何预判

**MLE - 最大似然估计**
----------------

Maximum Likelihood Estimation, MLE 是频率学派常用的估计方法！

假设数据 ![](https://www.zhihu.com/equation?tex=x_1%2C+x_2%2C+...%2C+x_n+) 是 i.i.d. 的一组抽样，![](https://www.zhihu.com/equation?tex=X+%3D+%28x_1%2C+x_2%2C+...%2C+x_n%29) 。其中 i.i.d. 表示 Independent and identical distribution，独立同分布。那么 MLE 对 ![](https://www.zhihu.com/equation?tex=%5Ctheta) 的估计方法可以如下推导：

![](https://www.zhihu.com/equation?tex=%5Cbegin%7Balign%2A%7D+%5Chat%7B%5Ctheta%7D_%5Ctext%7BMLE%7D+%26%3D+%5Carg+%5Cmax+P%28X%3B+%5Ctheta%29+%5C%5C+%26%3D+%5Carg+%5Cmax+P%28x_1%3B+%5Ctheta%29+P%28x_2%3B+%5Ctheta%29+%5Ccdot%5Ccdot%5Ccdot%5Ccdot+P%28x_n%3B%5Ctheta%29+%5C%5C+%26+%3D+%5Carg+%5Cmax%5Clog+%5Cprod_%7Bi%3D1%7D%5E%7Bn%7D+P%28x_i%3B+%5Ctheta%29+%5C%5C+%26%3D+%5Carg+%5Cmax+%5Csum_%7Bi%3D1%7D%5E%7Bn%7D+%5Clog+P%28x_i%3B+%5Ctheta%29+%5C%5C+%26%3D+%5Carg+%5Cmin+-+%5Csum_%7Bi%3D1%7D%5E%7Bn%7D+%5Clog+P%28x_i%3B+%5Ctheta%29+%5Cend%7Balign%2A%7D)

最后这一行所优化的函数被称为 Negative Log Likelihood (NLL)，这个概念和上面的推导是非常重要的！

我们经常在不经意间使用 MLE，例如

*   上文中关于频率学派求硬币概率的例子，其方法其实本质是由优化 NLL 得出。本文末尾附录中给出了具体的原因 :-)
*   给定一些数据，求对应的高斯分布时，我们经常会算这些数据点的均值和方差然后带入到高斯分布的公式，其理论依据是优化 NLL
*   深度学习做分类任务时所用的 cross entropy loss，其本质也是 MLE

**MAP - 最大后验估计**
----------------

Maximum A Posteriori, MAP 是贝叶斯学派常用的估计方法！

同样的，假设数据 ![](https://www.zhihu.com/equation?tex=x_1%2C+x_2%2C+...%2C+x_n+) 是 i.i.d. 的一组抽样，![](https://www.zhihu.com/equation?tex=X+%3D+%28x_1%2C+x_2%2C+...%2C+x_n%29) 。那么 MAP 对 ![](https://www.zhihu.com/equation?tex=%5Ctheta) 的估计方法可以如下推导：

![](https://www.zhihu.com/equation?tex=%5Cbegin%7Balign%2A%7D+%5Chat%7B%5Ctheta%7D_%5Ctext%7BMAP%7D+%26%3D+%5Carg+%5Cmax+P%28%5Ctheta+%7C+X%29+%5C%5C+%26%3D+%5Carg+%5Cmin+-%5Clog+P%28%5Ctheta+%7C+X%29+%5C%5C+%26+%3D+%5Carg+%5Cmin+-%5Clog+P%28X%7C%5Ctheta%29+-+%5Clog+P%28%5Ctheta%29+%2B+%5Clog+P%28X%29+%5C%5C+%26%3D+%5Carg+%5Cmin+-%5Clog+P%28X%7C%5Ctheta+%29+-+%5Clog+P%28%5Ctheta%29+%5Cend%7Balign%2A%7D)

其中，第二行到第三行使用了贝叶斯定理，第三行到第四行![](https://www.zhihu.com/equation?tex=P%28X%29) 可以丢掉因为与 ![](https://www.zhihu.com/equation?tex=%5Ctheta) 无关。**注意 ![](https://www.zhihu.com/equation?tex=-%5Clog+P%28X%7C%5Ctheta+%29) 其实就是 NLL，所以 MLE 和 MAP 在优化时的不同就是在于先验项 ![](https://www.zhihu.com/equation?tex=-+%5Clog+P%28%5Ctheta%29) 。**好的，那现在我们来研究一下这个先验项，假定先验是一个高斯分布，即

![](https://www.zhihu.com/equation?tex=P%28%5Ctheta%29+%3D+%5Ctext%7Bconstant%7D+%5Ctimes+e%5E%7B-%5Cfrac%7B%5Ctheta%5E2%7D%7B2%5Csigma%5E2%7D%7D)

那么， ![](https://www.zhihu.com/equation?tex=-%5Clog+P%28%5Ctheta%29+%3D+%5Ctext%7Bconstant%7D+%2B+%5Cfrac%7B%5Ctheta%5E2%7D%7B2%5Csigma%5E2%7D) 。至此，**一件神奇的事情发生了 -- 在 MAP 中使用一个高斯分布的先验等价于在 MLE 中采用 L2 的 regularizaton！**

再稍微补充几点：

*   我们不少同学大学里学习概率论时，最主要的还是频率学派的思想，其实贝叶斯学派思想也非常流行，而且实战性很强
*   CMU 的很多老师都喜欢用贝叶斯思想解决问题；我本科时的导师朱军老师也在做[贝叶斯深度学习](https://arxiv.org/abs/1709.05870)的工作，有兴趣可以关注一下。

**后记**
------

有的同学说：“了解这些没用，现在大家都不用了。” 这种想法是不对的，因为这是大家常年在用的知识，是推导优化函数的核心，而优化函数又是机器学习 (包含深度学习) 的核心之一。这位同学有这样的看法，说明对机器学习的本质并没有足够的认识，而让我吃惊的是，竟然有不少其他同学为这种看法点赞。内心感到有点儿悲凉，也引发了我写这篇文章的动力，希望能帮到一些朋友 :-)

**参考资料**
--------

[1] [Bayesian Method Lecture](http://www.utdallas.edu/~nrr150130/cs7301/2016fa/lects/Lecture_14_Bayes.pdf), UT Dallas.

[2] [MLE, MAP, Bayes classification Lecture](http://www.cs.cmu.edu/~aarti/Class/10701_Spring14/slides/MLE_MAP_Part1.pdf), CMU.

**附录**
------

**为什么说频率学派求硬币概率的算法本质是在优化 NLL？**

因为抛硬币可以表示为参数为 ![](https://www.zhihu.com/equation?tex=%5Ctheta+) 的 Bernoulli 分布，即

![](https://www.zhihu.com/equation?tex=P%28x_i%3B+%5Ctheta%29+%3D%5Cleft%5C%7B+%5Cbegin%7Barray%7D%7Bll%7D+%5Ctheta+%26+x_i+%3D+1+%5C%5C+1+-+%5Ctheta+%26+x_i+%3D+0+%5C%5C+%5Cend%7Barray%7D+%5Cright.+%5C+%3D+%5Ctheta%5E%7Bx_i%7D+%281-+%5Ctheta%29%5E%7B1-x_i%7D)

其中 ![](https://www.zhihu.com/equation?tex=x_i) = 1 表示第 ![](https://www.zhihu.com/equation?tex=i) 次抛出正面。那么，

![](https://www.zhihu.com/equation?tex=%5Ctext%7BNLL%7D+%3D+-%5Csum_%7Bi%3D1%7D%5En+%5Clog+P%28x_i%3B+%5Ctheta%29+%3D+-%5Csum_%7Bi%3D1%7D%5En+%5Clog+%5Ctheta%5E%7Bx_i%7D+%281-+%5Ctheta%29%5E%7B1-x_i%7D)

求导数并使其等于零，得到

![](https://www.zhihu.com/equation?tex=%5Ctext%7BNLL%7D%27+%3D+-%5Csum_%7Bi%3D1%7D%5En%5CBig%28%5Cfrac%7Bx_i%7D%7B%5Ctheta%7D+%2B+%281-x_i%29%5Cfrac%7B-1%7D%7B1-%5Ctheta%7D%5CBig%29+%3D+0)

即 ![](https://www.zhihu.com/equation?tex=%5Chat%7B%5Ctheta%7D+%3D+%5Cfrac%7B%5Csum_%7Bi%3D1%7D%5En+x_i%7D%7Bn%7D) ，也就是出现正面的次数除以总共的抛掷次数。