---
title: SVD
categories:
- Math
- Linear Algebra
- SVD
tags:
- linear algebra
- SVD
date: 2021/3/25 08:00:00
updated: 2021/3/25 21:00:00
---



1. ### 回顾特征值和特征向量

首先回顾下特征值和特征向量的定义如下：

$$
Ax=\lambda x
$$
其中 $A$ 是一个 $n \times n$ 矩阵， $x$ 是一个 $n$ 维向量，则 $\lambda$ 是矩阵 $A$ 的一个特征值，而 $x$ 是矩阵 $A$ 的特征值 $\lambda$ 所对应的特征向量。

求出特征值和特征向量有什么好处呢？ 就是我们可以将矩阵 A 特征分解。如果我们求出了矩阵 A 的 n 个特征值 $\lambda_{1}\leq \lambda_{2}\leq...\leq \lambda_{n}$ ，以及这 $n$ 个特征值所对应的特征向量  $w_{1},w_{2},...,w_{n}$，

那么矩阵 A 就可以用下式的特征分解表示：
$$
A = W \Sigma W^{-1}
$$
其中 W 是这 n 个特征向量所张成的 n×n 维矩阵，而Σ为这 n 个特征值为主对角线的 n×n 维矩阵。

一般我们会把 W 的这 n 个特征向量标准化，即满足 $\left|\left|w_{i}\right|\right|_{2}=1$ ，或者  $w_{i}^{T}w_{i}=1$，此时 W 的

n 个特征向量为标准正交基，满足 $W^{T}W=I$ ，即 $W^{T}=W^{-1}$ ，也就是说 W 为酉矩阵。

这样我们的特征分解表达式可以写成
$$
A = W \Sigma W^{T}
$$
注意到要进行特征分解，矩阵 A 必须为方阵。

那么如果 A 不是方阵，即行和列不相同时，我们还可以对矩阵进行分解吗？答案是可以，此时我们的 SVD 登场了。

#### 1.2 特征分解

如果我们求出了矩阵 $A$ 的 $n$ 个特征值$λ_1≤λ_2≤\cdots≤λ_n$, 以及这 $n$ 个特征值所对应的特征向量 $\{w_1,w_2,\cdots,w_n\}$, 如果这 $n$ 个特征向量线性无关，那么矩阵 $A$ 就可以用下式的特征分解表示： 

$\Sigma W^{-1}$ 其中 $W=(w_1,w_2,\cdots,w_n)$ 而$\Sigma$为： $\Sigma=\begin{pmatrix}\lambda_1&\\ &\lambda_2\\ &&\cdots\\ &&&\lambda_n \end{pmatrix}$一般我们会把 $W$ 的这 $n$ 个特征向量标准化，即满足$||w_i||_2=1$，或者说 $w_i^Tw_i =1$, 此时 $W$ 的 $n$ 个特征向量为标准正交基，且满足 $W^TW=I$, 即 $W^T=W^{-1}$, 此时我们称 $W$ 为酉矩阵。 

故，此时我们的特征分解表达式可以写成 $A=W \Sigma W^T$但是有一个局限，进行特征分解的矩阵必须是方阵，但是我们拿到的数据往往都不是方阵的形式，所以对于 $n\times m$ 的矩阵，如何进行特征分解？对于这个问题，SVD 算法应运而生。

#### 1.3 特征分解的几何意义

首先，要明确的是，一个矩阵其实就是一个线性变换，因为一个矩阵乘以一个向量后得到的向量，其实就相当于将这个向量进行了线性变换。比如说下面的一个矩阵： $M=\begin{bmatrix} 3&0\\ 0&1\\ \end{bmatrix}$

它其实对应的线性变换是下面的形式：

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/5c1133fe000111e705700265.jpg" style="zoom:80%;" /> 

因为这个矩阵 $M$ 乘以一个向量 $(x,y)$的结果是：$\begin{bmatrix} 3&0\\ 0&1 \end{bmatrix}\begin{bmatrix}x\\y\end{bmatrix}=\begin{bmatrix}3x\\y\end{bmatrix}$

上面的矩阵是对称的，所以这个变换是一个对 $x$，$y$ 轴的方向一个拉伸变换（每一个对角线上的元素将会对一个维度进行拉伸变换，当值 > 1 时，是拉长，当值 < 1 时时缩短），当矩阵不是对称的时候，假如说矩阵是下面的样子： $M=\begin{bmatrix} 1&1 \\ 0&1 \end{bmatrix}$
它所描述的变换是下面的样子： 

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/5c11b50d00016e1c10080506.png" style="zoom: 50%;" /> 

这其实是在平面上对一个轴进行的拉伸变换（如蓝色的箭头所示），在图中，蓝色的箭头是一个最主要的变化方向（变化方向可能有不止一个）。如果我们想要描述好一个变换，那我们就描述好这个变换主要的变化方向就好了。反过头来看看之前特征值分解的式子，分解得到的Σ矩阵是一个对角阵，里面的特征值是由大到小排列的，这些特征值所对应的特征向量就是描述这个矩阵变化方向（从主要的变化到次要的变化排列）。

当矩阵是高维的情况下，那么这个矩阵就是高维空间下的一个线性变换，这个线性变化可能没法通过图片来表示，但是可以想象，这个变换也同样有很多的变换方向，我们通过特征值分解得到的前 N 个特征向量，那么就对应了这个矩阵最主要的 N 个变化方向。我们利用这前 N 个变化方向，就可以近似这个矩阵（变换）。也就是之前说的：提取这个矩阵最重要的特征。总结一下，特征值分解可以得到特征值与特征向量，特征值表示的是这个特征到底有多重要，而特征向量表示这个特征是什么，可以将每一个特征向量理解为一个线性的子空间，我们可以利用这些线性的子空间干很多的事情。不过，特征值分解也有很多的局限，比如说变换的矩阵必须是方阵。

2. ### SVD 的定义

SVD 也是对矩阵进行分解，但是和特征分解不同，SVD 并不要求要分解的矩阵为方阵。假设我们的矩阵 A 是一个 m×n 的矩阵，那么我们定义矩阵 A 的 SVD 为：
$$
A = U \Sigma V^T
$$
其中 $U$ 是一个 $m \times m$ 的矩阵， $\Sigma$ 是一个 $m \times n$ 的矩阵，除了主对角线上的元素以外全为 0，主对角线上的每个元素都称为奇异值， $V$ 是一个 $n \times n$ 的矩阵。 $U$ 和 $V$ 都是酉矩阵，即满足

$U^{T}U=I,V^{T}V=I$ 。下图可以很形象的看出上面 SVD 的定义：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-5ee98f8f3426b845bc1c5038ecd29593_r.jpg)

那么我们如何求出 SVD 分解后的 U,Σ,V 这三个矩阵呢？

#### V

如果我们将 A 的转置和 A 做矩阵乘法，那么会得到 n×n 的一个方阵 $A^TA$ 。既然 $A^TA$ 是方阵，那么我们就可以进行特征分解，得到的特征值和特征向量满足下式：
$$
(A^TA)v_i = \lambda_i v_i
$$
这样我们就可以得到矩阵 $A^TA$ 的 n 个特征值和对应的 n 个特征向量 v 了。将 $A^TA$ 的所有特征向量张成一个 n×n 的矩阵 V，就是我们 SVD 公式里面的 V 矩阵了。一般我们将 V 中的每个特征向量叫做 A 的右奇异向量。

#### U

如果我们将 A 和 A 的转置做矩阵乘法，那么会得到 m×m 的一个方阵 $AA^T$ 。既然 $AA^T$ 是方阵，那么我们就可以进行特征分解，得到的特征值和特征向量满足下式：
$$
(AA^T)u_i = \lambda_i u_i
$$
这样我们就可以得到矩阵 $AA^T$ 的 m 个特征值和对应的 m 个特征向量 u 了。将 $AA^T$ 的所有特征向量张成一个 m×m 的矩阵 U，就是我们 SVD 公式里面的 U 矩阵了。一般我们将 U 中的每个特征向量叫做 A 的左奇异向量。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210326172838253.png" alt="image-20210326172838253" style="zoom: 67%;" />

u向量是有m 个元素的。这是因为v向量是有n 个元素的，而A 是mxn 的矩阵。所以A乘以v是一个含有m 个元素的向量，所以u是含有m 个元素的向量。但是现在只有r个u，r个u 虽然是A的列空间的一组基，但不是整个m维空间的一组基，因为u的个数比m 小。即，A的列空间是m维空间的一个子空间。那么在这种情况下，怎么生成一个方阵。

只需要使用关于正交性的gram schmidt 法则。现在相当于是对于整个m维空间，已经知道有r个标准正交向量，就可以使用gram schmidt法则，不断的去求与这r个向量也互相垂直的一个标准向量得到了第r+1个向量，然后再求与这r+1个向量也垂直的标准向量得到r+2个向量，以此类推，直到得到整个m维空间的一组标准正交基，构造mxm 的一个标准正交矩阵U。

#### $\Sigma$

由于Σ除了对角线上是奇异值其他位置都是 0，那我们只需要求出每个奇异值σ就可以了。

我们注意到:
$$
A=U \Sigma V^{T} \Rightarrow A V=U \Sigma V^{T} V \Rightarrow A V=U \Sigma \Rightarrow A v_{i}=\sigma_{i} u_{i} \Rightarrow \sigma_{i}=A v_{i} / u_{i}
$$

这样我们可以求出我们的每个奇异值，进而求出奇异值矩阵Σ。

上面还有一个问题没有讲，就是我们说 $A^TA$ 的特征向量组成的就是我们 SVD 中的 V 矩阵，而

$AA^T$ 的特征向量组成的就是我们 SVD 中的 U 矩阵，这有什么根据吗？这个其实很容易证明，我们以 V 矩阵的证明为例。
$$
A=U \Sigma V^{T} \Rightarrow A^{T}=V \Sigma U^{T} \Rightarrow A^{T} A=V \Sigma U^{T} U \Sigma V^{T}=V \Sigma^{2} V^{T}
$$
上式证明使用了 $U^{U}=I,\Sigma^{T}=\Sigma$ 。可以看出 $A^TA$ 的特征向量组成的的确就是我们 SVD 中的 V 矩阵。类似的方法可以得到 $AA^T$ 的特征向量组成的就是我们 SVD 中的 U 矩阵。

进一步我们还可以看出我们的特征值矩阵等于奇异值矩阵的平方，也就是说特征值和奇异值满足如下关系：
$$
A_{i}=\sqrt{\lambda_{i}}
$$
这样也就是说，我们可以不用 $\sigma_{i}=\frac{Av_{i}}{u_{i}}$ 来计算奇异值，也可以通过求出 $A^TA$ 的特征值取平方根来求奇异值。

3. ### SVD 计算举例

这里我们用一个简单的例子来说明矩阵是如何进行奇异值分解的。我们的矩阵 A 定义为：
$$
\mathbf{A}=\left(\begin{array}{cc}
0 & 1 \\
1 & 1 \\
1 & 0
\end{array}\right)
$$
首先求出 $A^TA$ 和 $AA^T$
$$
\mathbf{A}^{\mathrm{T}} \mathbf{A}=\left(\begin{array}{ccc}
0 & 1 & 1 \\
1 & 1 & 0
\end{array}\right)\left(\begin{array}{cc}
0 & 1 \\
1 & 1 \\
1 & 0
\end{array}\right)=\left(\begin{array}{cc}
2 & 1 \\
1 & 2
\end{array}\right) \\
\mathbf{A A}^{\mathrm{T}}=\left(\begin{array}{cc}
0 & 1 \\
1 & 1 \\
1 & 0
\end{array}\right)\left(\begin{array}{ccc}
0 & 1 & 1 \\
1 & 1 & 0
\end{array}\right)=\left(\begin{array}{ccc}
1 & 1 & 0 \\
1 & 2 & 1 \\
0 & 1 & 1
\end{array}\right)
$$
进而求出 $A^TA$ 的特征值和特征向量：
$$
\lambda_{1}=3 ; u_{1}=\left(\begin{array}{c} 
1 / \sqrt{2} \\
1 / \sqrt{2}
\end{array}\right) ; \lambda_{2}=1 ; u_{2}=\left(\begin{array}{c}
-1 / \sqrt{2} \\
1 / \sqrt{2}
\end{array}\right)
$$
接着求出 $AA^T$ 的特征值和特征向量：
$$
\lambda_{1}=3 ; u_{1}=\left(\begin{array}{c} 
1 / \sqrt{6} \\
2 / \sqrt{6} \\
1 / \sqrt{6}
\end{array}\right) ; \lambda_{2}=1 ; u_{2}=\left(\begin{array}{c}
1 / \sqrt{2} \\
0 \\
-1 / \sqrt{2}
\end{array}\right) ; \lambda_{3}=0 ; u_{3}=\left(\begin{array}{c}
1 / \sqrt{3} \\
-1 / \sqrt{3} \\
1 / \sqrt{3}
\end{array}\right)
$$
利用 $Av_{i}=\sigma_{i}u_{i},i=1,2$ 求奇异值：
$$
\left(\begin{array}{cc}
0 & 1 \\
1 & 1 \\
1 & 0
\end{array}\right)\left(\begin{array}{c}
1 / \sqrt{2} \\
1 / \sqrt{2}
\end{array}\right)=\sigma_{1}\left(\begin{array}{c}
1 / \sqrt{6} \\
2 / \sqrt{6} \\
1 / \sqrt{6}
\end{array}\right) \Rightarrow \sigma_{1}=\sqrt{3} \\
\left(\begin{array}{cc}
0 & 1 \\
1 & 1 \\
1 & 0
\end{array}\right)\left(\begin{array}{c}
-1 / \sqrt{2} \\
1 / \sqrt{2}
\end{array}\right)=\sigma_{2}\left(\begin{array}{c}
1 / \sqrt{2} \\
0 \\
-1 / \sqrt{2}
\end{array}\right) \Rightarrow \sigma_{2}=1
$$
也可以用 $\sigma_{i}=\sqrt{\lambda_{i}}$ 直接求出奇异值为  $\sqrt{3}$和 1.

最终得到 A 的奇异值分解为：
$$
A=U \Sigma V^{T}=\left(\begin{array}{ccc}
1 / \sqrt{6} & 1 / \sqrt{2} & 1 / \sqrt{3} \\
2 / \sqrt{6} & 0 & -1 / \sqrt{3} \\
1 / \sqrt{6} & -1 / \sqrt{2} & 1 / \sqrt{3}
\end{array}\right)\left(\begin{array}{cc}
\sqrt{3} & 0 \\
0 & 1 \\
0 & 0
\end{array}\right)\left(\begin{array}{cc}
1 / \sqrt{2} & 1 / \sqrt{2} \\
-1 / \sqrt{2} & 1 / \sqrt{2}
\end{array}\right)
$$

4. ### SVD 的一些性质 

对于奇异值, 它跟我们特征分解中的特征值类似，在奇异值矩阵中也是按照从大到小排列，而且奇异值的减少特别的快，在很多情况下，前 10% 甚至 1% 的奇异值的和就占了全部的奇异值之和的 99% 以上的比例。

也就是说，我们也可以用最大的 k 个的奇异值和对应的左右奇异向量来近似描述矩阵。

也就是说：
$$
A_{m \times n}=U_{m \times m} \Sigma_{m \times n} V_{n \times n}^{T} \approx U_{m \times k} \Sigma_{k \times k} V_{k \times n}^{T}
$$
其中 k 要比 n 小很多，也就是一个大的矩阵 A 可以用三个小的矩阵 $U_{m\times k},\sum_{}^{}{_{k\times k}},V_{k\times n}^{T}$ 来表示。如下图所示，现在我们的矩阵 A 只需要灰色的部分的三个小矩阵就可以近似描述了。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-4437f7678e8479bbc37fd965839259d2_r.jpg)

由于这个重要的性质，SVD 可以用于 PCA 降维，来做数据压缩和去噪。也可以用于推荐算法，将用户和喜好对应的矩阵做特征分解，进而得到隐含的用户需求来做推荐。同时也可以用于 NLP 中的算法，比如潜在语义索引（LSI）。

下面我们就对 SVD 用于 PCA 降维做一个介绍。

5. ### SVD 用于 PCA

PCA 降维，需要找到样本协方差矩阵 $X^TX$ 的最大的 d 个特征向量，然后用这最大的 d 个特征向量张成的矩阵来做低维投影降维。可以看出，在这个过程中需要先求出协方差矩阵 $X^TX$ ，当样本数多样本特征数也多的时候，这个计算量是很大的。

注意到我们的 SVD 也可以得到协方差矩阵 $X^TX$ 最大的 d 个特征向量张成的矩阵，但是 SVD 有个好处，有一些 SVD 的实现算法可以不求先求出协方差矩阵 $X^TX$ ，也能求出我们的右奇异矩阵 V。也就是说，我们的 PCA 算法可以不用做特征分解，而是做 SVD 来完成。这个方法在样本量很大的时候很有效。实际上，scikit-learn 的 PCA 算法的背后真正的实现就是用的 SVD，而不是暴力特征分解。

另一方面，注意到 PCA 仅仅使用了我们 SVD 的右奇异矩阵，没有使用左奇异矩阵，那么左奇异矩阵有什么用呢？

假设我们的样本是 m×n 的矩阵 X，如果我们通过 SVD 找到了矩阵 $XX^T$ 最大的 d 个特征向量张成的 m×d 维矩阵 U，则我们如果进行如下处理：
$$
X_{d \times n}^{\prime}=U_{d \times m}^{T} X_{m \times n}
$$
可以得到一个 d×n 的矩阵 X‘, 这个矩阵和我们原来的 m×n 维样本矩阵 X 相比，行数从 m 减到了 k，可见对行数进行了压缩。

**左奇异矩阵可以用于行数的压缩。**

**右奇异矩阵可以用于列数即特征维度的压缩，也就是我们的 PCA 降维。**

SVD是对数据进行有效特征整理的过程。首先，对于一个m×n矩阵A，我们可以理解为其有m个数据，n个特征，（想象成一个n个特征组成的坐标系中的m个点），然而一般情况下，这n个特征并不是正交的，也就是说这n个特征并不能归纳这个数据集的特征。**SVD的作用就相当于是一个坐标系变换的过程**，从一个不标准的n维坐标系，转换为一个标准的k维坐标系，并且使这个数据集中的点，到这个新坐标系的欧式距离为最小值（也就是这些点在这个新坐标系中的投影方差最大化），其实就是一个最小二乘的过程。进一步，如何使数据在新坐标系中的投影最大化呢，那么我们就需要让这个新坐标系中的基尽可能的不相关，我们可以用协方差来衡量这种相关性。A^T·A中计算的便是n×n的协方差矩阵，每一个值代表着原来的n个特征之间的相关性。当对这个协方差矩阵进行特征分解之后，我们可以得到奇异值和右奇异矩阵，而这个右奇异矩阵则是一个新的坐标系，奇异值则对应这个新坐标系中每个基对于整体数据的影响大小，我们这时便可以提取奇异值最大的k个基，作为新的坐标，这便是PCA的原理。

**6. SVD 小结** 
--------------

SVD 作为一个很基本的算法，在很多机器学习算法中都有它的身影，特别是在现在的大数据时代，由于 SVD 可以实现并行化，因此更是大展身手。

SVD 的缺点是**分解出的矩阵解释性往往不强**

> Reference [zhuanlan.zhihu.com](https://zhuanlan.zhihu.com/p/29846048)