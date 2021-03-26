---
title: SVD 几何意义
categories:
- Math
- Linear Algebra
- SVD
tags:
- linear algebra
- svd
date: 2021/3/25 08:00:00
updated: 2021/3/25 21:00:00
---

# 奇异值分解 (The singular value decomposition)

该部分是从几何层面上去理解二维的 SVD：对于任意的 2 x 2 矩阵，通过 SVD 可以将一个相互垂直的网格 (orthogonal grid) 变换到另外一个相互垂直的网格。

可以通过向量的方式来描述这个事实: 首先，选择两个相互正交的单位向量 v1 和 v2, 向量 $Mv1$ 和 $Mv2$ 正交。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/160736fh0w66wp0lnh16a6.jpg)

u1 和 u2 分别表示 $Mv1$ 和 $Mv2$ 的单位向量，$σ1 u1 =  Mv1$ 和 $σ2 * u2 =  Mv2$。σ1 和 σ2 分别表示这不同方向向量上的模，也称作为矩阵 M 的奇异值。

# 应用实例

## 实例一

$$
\mathbf{A}=\left(\begin{array}{cc}
1 & 1 \\
2 & 2 \\
\end{array}\right)
$$

经过这个矩阵变换后的效果如下图所示

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/1613544t1an5m6n5uagmcn.jpg)

在这个例子中，第二个奇异值为 0，因此经过变换后只有一个方向上有表达。

$M = u1σ1\ v1^T$

换句话说，如果某些奇异值非常小的话，其相对应的几项就可以不同出现在矩阵 M 的分解式中。因此，我们可以看到矩阵 M 的秩的大小等于非零奇异值的个数。

原来的x在v这个坐标系下相应的坐标是k1,k2,kn。那么经过A转换之后，x就变成了在u坐标系下相应的每一个维度的坐标拉伸奇异值倍。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210326170803493.png" alt="image-20210326170803493" style="zoom:67%;" />

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210326170912386.png" alt="image-20210326170912386" style="zoom: 80%;" />

## 实例二

我们来看一个奇异值分解在数据表达上的应用。假设我们有如下的一张 15 x 25 的图像数据。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/svd.O.gif)

如图所示，该图像主要由下面三部分构成。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/1615308zvpiyyjjddv8em8.jpg)

将图像表示成 15 x 25 的矩阵，如下图所示

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/noise.gif" style="zoom: 67%;" />

如果对矩阵 M 进行奇异值分解以后，得到奇异值分别是

σ1 = 14.72，σ2 = 5.22，σ3 = 3.31

矩阵 M 就可以表示成

$M=u1σ1 v1^T + u2σ2 v2^T + u3σ3 v3^T$

vi 具有 15 个元素，ui 具有 25 个元素，σi 对应不同的奇异值。如上图所示，我们就可以用 123 个元素来表示具有 375 个元素的图像数据了。

## 实例三 减噪 (noise reduction)

前面的例子的奇异值都不为零，或者都还算比较大，下面我们来探索一下拥有零或者非常小的奇异值的情况。通常来讲，大的奇异值对应的部分会包含更多的信息。比如，我们有一张扫描的，带有噪声的图像，如下图所示

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/noise.gif)

我们采用跟实例二相同的处理方式处理该扫描图像。得到图像矩阵的奇异值：

σ1 = 14.15 
σ2 = 4.67 
σ3 = 3.00 
σ4 = 0.21 
σ5 = 0.19 
... 
σ15 = 0.05

很明显，前面三个奇异值远远比后面的奇异值要大，这样矩阵 M 的分解方式就可以如下：

$M \approx u1σ1 v1^T + u2σ2 v2^T + u3σ3 v3^T$

经过奇异值分解后，我们得到了一张降噪后的图像。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/random.data.gif)

## 实例四 数据分析 (data analysis)

我们搜集的数据中总是存在噪声：无论采用的设备多精密，方法有多好，总是会存在一些误差的。如果你们还记得上文提到的，大的奇异值对应了矩阵中的主要信息的话，运用 SVD 进行数据分析，提取其中的主要部分的话，还是相当合理的。

作为例子，假如我们搜集的数据如下所示：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/random.data.gif)

我们将数据用矩阵的形式表示：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/161851gmlgg667vmvvh6gl.jpg)

经过奇异值分解后，得到

σ1 = 6.04，σ2 = 0.22

由于第一个奇异值远比第二个要大，数据中有包含一些噪声，第二个奇异值在原始矩阵分解相对应的部分可以忽略。经过 SVD 分解后，保留了主要样本点如图所示

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/random.data.svd.gif)

就保留主要样本数据来看，该过程跟 PCA(principal component analysis) 技术有一些联系，PCA 也使用了 SVD 去检测数据间依赖和冗余信息.

> [blog.chinaunix.net](http://blog.chinaunix.net/uid-20761674-id-4040274.html)