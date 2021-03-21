---
title: CNN
categories:
- DL
- CNN
tags:
- CNN
date: 2021/3/21 20:00:16
updated: 2021/3/21 12:00:16
---



拿到一张图片，要对它进行识别，最简单的栗子是，这张图是什么？

比如，我现在要训练一个最简单的 CNN，用来识别一张图片里的字母是 X 还是 O。

![](https://pic4.zhimg.com/v2-ce4e2ad2cb9919d7e49a66fcd9f7a267_b.png)

我们人眼一看，很简单嘛，明显就是 X 啊，但是计算机不知道，它不明白什么是 X。所以我们给这张图片加一个标签，也就是俗称的 Label，Label=X，就告诉了计算机这张图代表的是 X。它就记住了 X 的长相。

但是并不是所有的 X 都长这样呀。比如说...

![](https://pic4.zhimg.com/v2-547828bbcc202a030dfc98fcb3cfe5af_r.jpg)

这四个都是 X，但它们和之前那张 X 明显不一样，计算机没见过它们，又都不认识了。

（这里可以扯出机器学习中听起来很高冷的名词 “ **_欠拟合_** ”）

不认识了怎么办，当然是回忆看看是不是见过差不多的呀。这时候 CNN 要做的，就是如何提取内容为 X 的图片的特征。

我们都知道，图片在计算机内部以像素值的方式被存储，也就是说两张 X 在计算机看来，其实是这样子的。

![](https://pic3.zhimg.com/v2-8bc4063deeaf11853fe1f9531b71e50a_b.png)![](https://pic1.zhimg.com/v2-e216abf6d8042827b1e6de85732f0060_b.png)

其中 1 代表白色，-1 代表黑色。

如果按照每像素逐个比较肯定是不科学的，结果不对而且效率低下，因此提出其他匹配方法。

我们称之为 patch 匹配。

观察这两张 X 图，可以发现尽管像素值无法一一对应，但也存在着某些共同点。

![](https://pic3.zhimg.com/v2-df0a51f10a46347704274ee6ddbb614a_r.jpg)

如上图所示，两张图中三个同色区域的结构完全一致！

因此，我们就考虑，要将这两张图联系起来，无法进行全体像素对应，但是否能进行局部地匹配？

答案当然是肯定的。

相当于如果我要在一张照片中进行人脸定位，但是 CNN 不知道什么是人脸，我就告诉它：人脸上有三个特征，眼睛鼻子嘴巴是什么样，再告诉它这三个长啥样，只要 CNN 去搜索整张图，找到了这三个特征在的地方就定位到了人脸。

同理，从标准的 X 图中我们提取出三个**特征**（**feature**）

![](https://pic4.zhimg.com/v2-2230b625dcbac671af2646ff9d39a7d7_b.png)![](https://pic3.zhimg.com/v2-2674d52504845d3fc062fa36a607fff2_b.png)![](https://pic3.zhimg.com/v2-292a36d6fedd82498d406846e6a8fba2_b.png)![](https://pic3.zhimg.com/v2-d71bef1581b18cdb5e13e5a472d7220e_b.png)

我们发现只要用这三个 feature 便可定位到 X 的某个局部。

![](https://pic2.zhimg.com/v2-eaa1665a93ae616abad84971ac09f60d_r.jpg)

feature 在 CNN 中也被成为卷积核（filter），一般是 3X3，或者 5X5 的大小。

**_【2】卷积运算_**

![](https://pic3.zhimg.com/v2-2da1946c4f7da1d2f48eeb89e8f1b71a_b.png)![](https://pic2.zhimg.com/v2-d433bbdd6486bcca448829d8afc6dc79_b.png)

卷积神经网络在本质和原理上还是和卷积运算有一定的联系的

好了，下面继续讲怎么计算。四个字：对应相乘。

看下图。

取 feature 里的（1，1）元素值，再取图像上蓝色框内的（1，1）元素值，二者相乘等于 1。把这个结果 1 填入新的图中。

![](https://pic1.zhimg.com/v2-e96f9de5db221fc8a0e3ddd5e8ba0514_b.png)![](https://pic4.zhimg.com/v2-a9af9a7d6d5208720df375d1b79634a3_r.jpg)

同理再继续计算其他 8 个坐标处的值

![](https://pic2.zhimg.com/v2-dd84ec4256cfbae35f4b3fd1c0bd1e5d_b.png)

9 个都计算完了就会变成这样。

![](https://pic3.zhimg.com/v2-725dd797cb015fa424524a4d366c214a_b.png)

接下来的工作是对右图九个值求平均，得到一个均值，将均值填入一张新的图中。

这张新的图我们称之为 **feature map** （**特征图**）

![](https://pic3.zhimg.com/v2-68a6efb63fbbb0b83ebded0d6268d0fe_r.jpg)

可能有小盆友要举手问了，为什么蓝色框要放在图中这个位置呢？

这只是个栗子嘛。 这个蓝色框我们称之为 “窗口”，窗口的特性呢，就是要会滑动。

其实最开始，它应该在起始位置。

![](https://pic4.zhimg.com/v2-6b3596592e0122e3c5dfd66255a9c773_b.png)

进行卷积对应相乘运算并求得均值后，滑动窗便开始向右边滑动。根据步长的不同选择滑动幅度。

比如，若 步长 stride=1，就往右平移一个像素。

![](https://pic3.zhimg.com/v2-c9d84fa9f5fc1b826d44e2d95aaec7de_b.png)

若 步长 stride=2，就往右平移两个像素。

![](https://pic2.zhimg.com/v2-09338fc4abadd746badea88f74c379a1_b.png)

就这么移动到最右边后，返回左边，开始第二排。同样，若步长 stride=1，向下平移一个像素；stride=2 则向下平移 2 个像素。

![](https://pic4.zhimg.com/v2-26021c366f377a8ae7b941ea0d8f8c97_b.png)

好了, 经过一系列卷积对应相乘，求均值运算后，我们终于把一张完整的 feature map 填满了。

![](https://pic4.zhimg.com/v2-683c8d63e22eef01a271a08016006d17_r.jpg)

feature map 是每一个 feature 从原始图像中提取出来的 “特征”。其中的值，越接近为 **1** 表示对应位置和 feature 的**匹配越完整**，越是接近 - 1，表示对应位置和 feature 的反面匹配越完整，而值接近 **0** 的表示对应位置没有任何匹配或者说**没有什么关联**。

一个 feature 作用于图片产生一张 feature map，对这张 X 图来说，我们用的是 3 个 feature，因此最终产生 3 个 feature map。

![](https://pic1.zhimg.com/v2-fb26f8eeaea46279cf9e94695289b44c_r.jpg)

**_【3】非线性激活层_**

卷积层对原图运算多个卷积产生一组线性激活响应，而非线性激活层是对之前的结果进行一个非线性的激活响应。

在神经网络中用到最多的非线性激活函数是 Relu 函数，它的公式定义如下：

f(x)=max(0,x)

即，保留大于等于 0 的值，其余所有小于 0 的数值直接改写为 0。

为什么要这么做呢？上面说到，卷积后产生的特征图中的值，越靠近 1 表示与该特征越关联，越靠近 - 1 表示越不关联，而我们进行特征提取时，为了使得数据更少，操作更方便，就直接舍弃掉那些不相关联的数据。

如下图所示：>=0 的值不变

![](https://pic3.zhimg.com/v2-f1f4029c3ff8d2bbb138fcdc7af090fe_r.jpg)

而 < 0 的值一律改写为 0

![](https://pic4.zhimg.com/v2-b9508442abbab0395810df7f288ceda7_r.jpg)

得到非线性激活函数作用后 的结果：

![](https://pic4.zhimg.com/v2-f9af9fde70d5d3b7db5562956c6cc213_r.jpg)

【4】pooling 池化层

卷积操作后，我们得到了一张张有着不同值的 feature map，尽管数据量比原图少了很多，但还是过于庞大（比较深度学习动不动就几十万张训练图片），因此接下来的池化操作就可以发挥作用了，它最大的目标就是减少数据量。

池化分为两种，Max Pooling 最大池化、Average Pooling 平均池化。顾名思义，最大池化就是取最大值，平均池化就是取平均值。

拿最大池化举例：选择池化尺寸为 2x2，因为选定一个 2x2 的窗口，在其内选出最大值更新进新的 feature map。

![](https://pic4.zhimg.com/v2-bdc0421a13e06122b6d13fb84cdf5e9f_r.jpg)

同样向右依据步长滑动窗口。

![](https://pic3.zhimg.com/v2-bc7736e0a815c8db07a855c35234b76e_r.jpg)![](https://pic4.zhimg.com/v2-e58c86e2a784a341afe648607ab2f1a7_r.jpg)

最终得到池化后的 feature map。可明显发现数据量减少了很多。

因为最大池化保留了每一个小块内的最大值，所以它相当于保留了这一块最佳匹配结果（因为值越接近 1 表示匹配越好）。这也就意味着它不会具体关注窗口内到底是哪一个地方匹配了，而只关注是不是有某个地方匹配上了。这也就能够看出，CNN 能够发现图像中是否具有某种特征，而不用在意到底在哪里具有这种特征。这也就能够帮助解决之前提到的计算机逐一像素匹配的死板做法。

到这里就介绍了 CNN 的基本配置 --- 卷积层、Relu 层、池化层。

在常见的几种 CNN 中，这三层都是可以堆叠使用的，将前一层的输入作为后一层的输出。比如：

![](https://pic3.zhimg.com/v2-141ed65cf0003c9550a8d57fe7c6afb2_r.jpg)

也可以自行添加更多的层以实现更为复杂的神经网络。
