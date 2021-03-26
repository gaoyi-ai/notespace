---
title: Linear Algebra
categories:
- Math
- Linear Algebra
tags:
- linear algebra
date: 2021/2/21 08:00:00
updated: 2021/2/22 21:00:00
---

# Linear Algebra

## The introduction

- 线性代数则在研究“一组数”，即向量
- 线性代数在研究“空间”
- 矩阵可以理解为对向量作用的“函数”

## dot product

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210222100709152.png" alt="image-20210222100709152" style="zoom:67%;" />

将u 这个向量投影到v 这个向量。得到的结果依然是将这两个向量的方向统一之后得到的这两个向量所对应的长度，相应的这个乘积。
在向量的点乘背后，其实蕴含着这样的一个含义。由于对于向量要处理方向性，那么不同方向的向量直接做乘法是没有意义的。所以将这两个向量统一到一个方向上。不管是将v 这个向量投影到u 这个向量上，还是将u 这个向量投影到v 这个向量上。都可以在投影之后，用投影得到的这个向量，它对应的长度乘以被投影的这个向量对应的长度，那么认为。这二者相乘是具有意义的，这个结果就是向量的点乘。

## 矩阵乘法

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210222213841347.png" alt="image-20210222213841347" style="zoom:67%;" />

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210319165243503.png" alt="image-20210319165243503" style="zoom:67%;" />

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210319165304448.png" alt="image-20210319165304448" style="zoom:67%;" />

用行视角来看的话，最终得到的结果，每一个结果就是一个元素，这个元素不需要再做任何运算，不需要进行加法运算，可以直接放到结果矩阵中的那个对应的位置。

如果把前面的矩阵看成一列一列的，后面的矩阵看成一行一行的。每次从前面取出一列，然后取出后面对应的那一行，进行乘法。这个乘法的结果是一个mx n 的矩阵，总共可以得到k 个这样的矩阵，把这k 个矩阵加在一起，得到的是结果矩阵。

## 矩阵表示空间

**一个矩阵乘以一个向量。可以把看作是把这个向量扔进由这个矩阵所定义的新的空间中，看这个向量所代表的点在这个新的空间中。所对应的那个位置。而对于这个新的空间，它的坐标轴是由这个矩阵的列向量所定义的。**

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210222225132236.png" alt="image-20210222225132236" style="zoom:67%;" />

[[1,0],[0,1]]这个矩阵按列来看，第一列[1,0]就代表的是一个轴的方向,是e1。第二列，就代表的是另外一个轴的方向是e2。

那么使用列视角来看待这个矩阵和这个向量相乘，其实就是让第一个轴和第一个元素相乘加上第二个轴和第二个元素相乘，最终得到的那个坐标。
换句话说，整个二维空间就是需要使用这两个轴来定义。这个矩阵就定义了这两个坐标轴。那么一旦表示出这两个坐标轴之后，就相当于也就表示出了整个空间。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210222225150121.png" alt="image-20210222225150121" style="zoom:50%;" />

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210222225218629.png" alt="image-20210222225218629" style="zoom:50%;" />

对于二维空间来说。只需要知道e1和e2，这就是矩阵表示了一个空间。那么这样的思想再去拓展。现在写任意一个矩阵，比如说是[[4,2],[1,3]]。是不是也表示一个空间呢？换句话说，可不可以把它一列一列的看,在这个矩阵中。第一列是[4,1]这样的一个向量，记作u。第二列是[2,3],记作v。是不是这个矩阵就代表了一个由u 和v 这两个向量所组成的空间呢？

是的，这是一个非常重要的结论。首先绘制的是标准的一个二维空间。在这个二维空间中绘制出来u,v。那么其实这两个向量本身也定义了一个空间，这个空间中的x 轴，就是这个u。 y 轴呢就是v。相应的在这个空间中，在x 轴方向上的每一个单位，其实就是u 这个向量的模，而在y 轴上的一个单位就是v 这个向量的模。

那么在这两个向量上所定义的空间中，比如说[2,2]这个点在哪里呢？

这个概念其实和一般所探讨的标准空间。也就是通常欧拉空间中是同样的，也就是对于[2,2]这个点，就要沿着第一个坐标轴由这个方向走两步。那在这里每一步其实就是u 的模。在v 这个方向上也要走两步。

那么对于这个矩阵和[2,2]相乘，其实就相当于是看[2,2]这个点坐标对应于在这个矩阵所表示的空间中，在哪个位置呢？跟之前的推导一样，现在所画出来这两个红色的向量其实就相当于是[2,2]这个坐标点在u v 这两个坐标轴上所对应的分量是多少。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210222230243410.png" alt="image-20210222230243410" style="zoom:67%;" />

之前图形变换，用一个矩阵乘以一个向量，理解为这个矩阵是一个向量的函数。其实也可以把这个矩阵看作是一个新的空间。

那么比如之前这个图形要沿着y 轴进行翻转的话，相应的变换函数是[[-1,0],[0,1]]。其实也可以把它看作是一个新的轴[-1,0]这样的一个轴和[0,1]这样的一个轴所定义的空间。所谓的这个翻转的变换，就是将原来的图像放到这个新的空间中，看它是怎么样的。那么这个新的空间的x y 就是这个样子。

## 线性方程组解的结构

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210223212953271.png" alt="image-20210223212953271" style="zoom:67%;" />

阶梯型矩阵

- 非零行的第一个元素（主元）为 1
- 主元所在列的其他元素均为 0

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210223213351777.png" alt="image-20210223213351777" style="zoom:67%;" />

## 求解矩阵的逆

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210319104935466.png" alt="image-20210319104935466" style="zoom:67%;" />

转为求解线性系统

唯一解：对应的逆矩阵
无解：没有逆矩阵
无数解：不会出现此情况

## 矩阵的 LU 分解

在数值分析和线性代数中，LU分解或因式化将一个矩阵分解为一个下三角矩阵和一个上三角矩阵的乘积。这个乘积有时也包括一个换元矩阵。LU分解可以看作是高斯消除的矩阵形式。计算机通常用LU分解来求解线性方程的方程组，在对矩阵进行反演或计算矩阵的行列式时，它也是一个关键步骤。

高斯消元法的过程，通过初等变换，把一个矩阵变成了上三角矩阵

$E_p ⋅...⋅E_3 ⋅E_2 ⋅E_1 ⋅ A = U$
$L = E_1^{−1} ⋅E_2^{−1} ⋅E_3^{−1} ⋅...⋅E_p^{−1}$

## 线性组合

矩阵和向量的乘法，可以看做是矩阵的列向量的一个线性组合
$$
\left(\begin{array}{ll}
1 & 2 \\
4 & 5
\end{array}\right) \cdot\left(\begin{array}{l}
x \\
y
\end{array}\right)=\left(\begin{array}{c}
x+2 y \\
4 x+5 y
\end{array}\right)=\left(\begin{array}{l}
1 \\
4
\end{array}\right) x+\left(\begin{array}{c}
2 \\
5
\end{array}\right) y \quad \text { 列视角 }
$$

### 线性相关的重要性质

$$
\vec{u}=\left(\begin{array}{c}
-3 \\
1
\end{array}\right), \vec{v}=\left(\begin{array}{c}
1 \\
-1 / 3
\end{array}\right)
$$
是否线性相关?
是否存在k1, k2不全为0, 满足: $k_{1} \cdot \vec{u}+k_{2} \cdot \vec{v}=k_{1} \cdot\left(\begin{array}{c}-3 \\ 1\end{array}\right)+k_{2} \cdot\left(\begin{array}{c}1 \\ -1 / 3\end{array}\right)=0$

转化为齐次线性方程组求解 $\left(\begin{array}{cc}-3 & 1 \\ 1 & -1 / 3\end{array}\right) \cdot\left(\begin{array}{l}k_{1} \\ k_{2}\end{array}\right)=0 \quad$ 是否只有唯一零解（线性无关）?

在二维平面中两个向量不共线，则这两个向量线性无关
在三维空间中，三个向量不共面，则这三个向量线性无关

如果V1,V2,V3,…，Vn线性相关→其中一个向量可以写成其他向量的线性组合
如果V1,V2,V3,…，Vn线性无关→没有一个向量可以写成其他向量的线性组合

## 生成空间的基

n个n维向量v1,v2,ν3,…,vn,若他们是这个n维空间的基 <=> 
1)v1,v2,v3,…,vn 生成整个n维空间
2)v1,v2,v3,…,vn 线性无关

## 广义向量空间

所有的多项式,构成一个向量空间。
$$
a_px^p+a_{p-1}x^{p-1}+…+a_1x+a_0
$$
加法:多项式加法;数量乘法:多项式乘以一个数

## 子空间

- V的任何子空间都一定包含O。（一个空间的子空间，这个子空间一定包含这个原来空间所对应的零向量。）

- 对于V的子空间S,如果u属于s,则-u一定属于S

对于三维空间来说
过原点的一个平面,是三维空间的一个子空间
过原点的一个直线,是三维空间的一个子空间
原点本身,是三维空间的一个子空间!

对于n维空间来说，过原点的一个m维空间(m<n),是n维空间的一个子空间

## 行空间

高斯消元法结果的每一行其实就是原来矩阵中各行的一个线性组合。

如果进行高斯消元法，一共有m 行的话，那么下面假设一共有x 行都为零行，这意味着什么？
这就意味着这x 行所对应的那些行向量，其实都可以表示成上面的那m-x 行这些行向量的线性组合。所以这x 个为零向量的向量都可以删去。

最终的结果那个零行就意味着这一行的向量可以表示成其他向量的线性组合。所以就可以把这些向量删去。那么剩下的这个非零行的个数，就是这p个n维向量相应的生成空间的基，也是这p个n 维向量所对应的这个生成空间的维度。

被向量$\mathrm{u}=(2,0 0);\mathrm{v}=(-1,0,0);\mathrm{w}=(0,0,1)$ 生成的空间,维度是多少   2
$$
\begin{aligned}

&\left(\begin{array}{c}
\vec{u} \\
\bar{v} \\
\bar{w}
\end{array}\right)=\left(\begin{array}{ccc}
2 & 0 & 0 \\
-1 & 0 & 0 \\
0 & 0 & 1
\end{array}\right)=\left(\begin{array}{ccc}
1 & 0 & 0 \\
-1 & 0 & 0 \\
0 & 0 & 1
\end{array}\right)=\left(\begin{array}{ccc}
1 & 0 & 0 \\
0 & 0 & 0 \\
0 & 0 & 1
\end{array}\right)=\left(\begin{array}{ccc}
1 & 0 & 0 \\
0 & 0 & 1 \\
0 & 0 & 0
\end{array}\right)
\end{aligned}
$$

## 行空间和列空间

对于一个m行n列的矩阵

| 行空间                                 | 列空间                                       |
| -------------------------------------- | -------------------------------------------- |
| 行空间是n维空间的子空间                | 列空间是m维空间的子空间                      |
| 行最简形式的非零行个数为矩阵的行秩     | 行最简形式的主元列数量为矩阵的列秩           |
| 行空间的维度，为矩阵的行秩             | 列空间的维度，为矩阵的列秩                   |
| 行最简形式的非零行，是行空间的一组基。 | 主元列的对应**原矩阵**的列，是列空间的一组基 |



## 正交化

### 高维投影

一组向量，如果两两正交，则成为正交向量组。
正交非零向量组一定线性无关。

如果已知一组基：$\vec{v}_{1},\vec{v}_{2},\vec{v}_{3},\cdots,\vec{v}_{n}$求一组正交基。

格拉姆-施密特过程 $$
$$
\quad \bar{p}_{1}=\vec{v}_{1} \\
\bar{p}_{2}=\vec{v}_{2}-\frac{\vec{v}_{2} \cdot \vec{p}_{1}}{\| \bar{p}_{1}||^{2}} \cdot \bar{p}_{1} \\
\bar{p}_{3}=\vec{v}_{3}-\frac{\vec{v}_{3} \cdot \vec{p}_{1}}{\| \bar{p}_{1}||^{2}} \cdot \bar{p}_{1}-\frac{\vec{v}_{3} \cdot \bar{p}_{2}}{\left\|\bar{p}_{2}\right\|^{2}} \cdot \bar{p}_{2}
$$

### 标准正交矩阵的重要性质：$Q^{-1}=Q^T$



## 利用投影与向量之间的偏差来计算最小二乘法，解决方程 $Ax=b$ 无解时候的最优问题

#### 1.1  直线上的投影

从 $\mathbb{R}^2$空间讲起，有向量 $a,b$ ，作 $b$ 在 $a$ 上的投影 $p$ ，如图：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-732a147dc3894d4c171cd0d97e461eb6_b.jpg)

从图中我们可以得到如下结论：

*   p 与 a 共线，有 p=ax
*   向量 e 可看做向量 b，p 之间的误差，e=b-p
*   向量 e 与 向量 a 正交 $e\bot a$ ，即 $a^Te=0$

由以上三个结论可以得到 $a^Te=a^T(b-p)=a^T(b-ax)=0$。求解得到：

$$
p=\frac{aa^Tb}{a^Ta}=\frac{aa^T}{a^Ta}b
$$
从上式可以看出，如果将 b 变为 2b 则 p 也会翻倍，如果将 a 变为 2a 则 p 不变。

同时，还可以看出投影向量是一个矩阵 作用于原向量得到，该矩阵称为投影矩阵。设投影矩阵为 $P=\frac{aa^T}{a^Ta}$ ，则：$p=Pb$

若 a 是 n 维列向量，则 P 是一个 $n\times n$ 矩阵。

这里 $aa^T$ 与 $a^Ta$ 是不同的，当 a 是列向量的时候，前者是一个矩阵，后者是一个具体的数字，上图中，矩阵 P 的秩为 1 ，即 $rank(P)=1$。

投影矩阵具有以下性质：

*   $P=P^T$ ，投影矩阵是一个对称矩阵。
*   如果对一个向量做两次投影，即 $PPb$ ，则其结果仍然与 $Pb$ 相同，也就是 $P^2=P$ 。

为什么我们需要投影？有些时候 $Ax=b$ 无解，我们只能求出最接近的那个解。

Ax 总是在 A 的列空间中，而 b 却不一定，这是问题所在，所以我们可以将 b 变为 A 的列空间中最接近的那个向量，即将无解的 $Ax=b$ 变为求有解的 $A\hat{x}=p$ （ p 是 b 在 A 的列空间中的投影， $\hat{x}$ 不再是那个不存在的 x，而是最接近的解）。

#### 1.2 平面上的投影

介绍完了向量之间的投影，现在来看一下将向量 b 投影在平面 A 上的情况。同样的，p 是向量 b 在平面 A 上的投影，e 是垂直于平面 A 的向量，即 b 在平面 A 法方向的分量。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-4c87d61f53a0d97adfa5dabb9c90abd0_r.jpg" style="zoom: 50%;" />

设平面 A 的一组基为 $a_1,a_2$ ，则投影向量 $p=\hat{x_1}a_1+\hat{x_2}a_2$ ，即 $p=A\hat{x}$ ，其中 $A=[a_1 \quad a_2]$ ， $\hat{x}=\begin{bmatrix}\hat{x_1} \quad \hat{x_2}\end{bmatrix}$ ，这里如果我们求出 $\hat{x}$ ，则该解就是无解方程组最近似的解。

现在问题的关键在于找 $e=b-A\hat{x}$ ，使它垂直于平面，由 $e \bot A$ 得到：

$$
\begin{cases}a_1^T(b-A\hat{x})=0\\
a_2^T(b-A\hat{x})=0\end{cases}
$$
将方程组写成矩阵形式：

$$
\begin{bmatrix}a_1^T\\a_2^T\end{bmatrix}(b-A\hat{x})=\begin{bmatrix}0\\0\end{bmatrix}
$$
即：

$$
A^T(b-A\hat{x})=0
$$
比较该方程与 $\mathbb {R}^2$ 中的投影方程，发现只是向量 a 变为矩阵 A 而已，本质上就是 $A^Te=0$ 。所以， 在 $A^T$ 的零空间中（ $e\in N(A^T)$ )，从前面几讲我们知道，左零空间 $\bot$ 列空间，则有 $e\bot C(A)$ ，与我们设想的一致。

再化简方程得：

$$
A^TAx=A^Tb
$$
比较在$\mathbb {R}^2$中的情形， $a^Ta$ 是一个数字而 $A^TA$ 是一个 n 阶方阵，而解出的 x 可以看做两个数字的比值。现在在 $\mathbb{R}^3$ 中，我们需要再次考虑：什么是 $\hat{x}$ ？投影是什么？投影矩阵又是什么？

*   第一个问题： $\hat{x}=(A^TA)^{-1}A^Tb$ ；
*   第二个问题： $p=A\hat{x}=\underline{A(A^TA)^{-1}A^T}b$ 。
*   第三个问题：易看出投影矩阵就是下划线部分 $P=A(A^TA)^{-1}A^T$ 。类比在 $\mathbb {R}^2$ 中 $P=\frac{aa^T}{a^Ta}$ ；

这里还需要注意一个问题， $P=A(A^TA)^{-1}A^T$ 是不能继续化简为 $P=AA^{-1}(A^T)^{-1}A^T=I$ 的，因为这里的 A 并不是一个可逆方阵，而矩阵 A 是两个基向量构成的矩阵，两个基向量线性无关，因此$A^TA$ 是可逆的。

也可以换一种思路，如果 A 是一个 n 阶可逆方阵，则 A 的列空间是整个 $\mathbb {R}^n$ 空间，于是 b 在  $\mathbb {R}^n$上的投影矩阵确实变为了 $I$ ，因为 b 已经在空间中了，其投影不再改变。

同样投影矩阵 P 具有两个形式性质：

*   $P=P^T$

证明：

$$
\left[A(A^TA)^{-1}A^T\right]^T=A\left[(A^TA)^{-1}\right]^TA^T
$$
而 $(A^TA)$ 是对称的，所以其逆也是对称的

有 $A((A^TA)^{-1})^TA^T=A(A^TA)^{-1}A^T$ ，得证。

*   $P^2=P$

证明：

$$
\left[A(A^TA)^{-1}A^T\right]\left[A(A^TA)^{-1}A^T\right]=A(A^TA)^{-1}\left[(A^TA)(A^TA)^{-1}\right]A^T=A(A^TA)^{-1}A^T
$$
，得证。

#### 最小二乘法初步

接下看看投影的经典应用案例：最小二乘法拟合直线（least squares fitting by a line）。最小二乘法与投影有什么关系呢？我们知道投影 e 可以看做 b 与 a 之间偏移量的大小，这就为使用最小二乘法拟合直线提供了方便。

例：找到距离图中三个点 $(1,1),(2,2),(3,2)$ 偏差最小的直线。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-b9ed420948b37b97844fd90a0075dacc_b.jpg)

*   假设最优直线方程 b=C+Dt，代入三个点列出方程：

$$
\begin{cases}C+D&=1\\C+2D&=2\\C+3D&=2\end{cases}
$$



*   写作矩阵形式：

$$
\begin{bmatrix}1&1\\1&2\\1&3\\\end{bmatrix}\begin{bmatrix}C\\D\\\end{bmatrix}=\begin{bmatrix}1\\2\\2\\\end{bmatrix}
$$

也就是我们的 $Ax=b$ ，方程组无解。  

*   采取投影的方法来拟合这条曲线。 $A^TA\hat{x}=A^Tb$ 有解，于是我们将原是两边同时乘以 $A^T$ 后得到的新方程组是有解的， $A^TA\hat{x}=A^Tb$ 也是最小二乘法的核心方程。

## 线性变换

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210325104310578.png" alt="image-20210325104310578" style="zoom:50%;" />

那么这种空间转换一个非常重要的应用就是压缩。比如在二维空间中=有五个数据点。那么对这五个数据点每个数据就必须要用两个数来表示，分别是在x 轴的位置和在y 轴的位置，如果是用红色的这组坐标轴，用红色的这组基来表示的话，这组数据就产生了一个特点。就是这些数据点在这个红色坐标系的y轴的位置所对应的那个值都特别的小。换句话说这些数据在这个红色的坐标系下，在y 轴这个方向上的。信息量是非常低的，主要的信息量呢都在这个红色的坐标轴的这个x 方向上。那么这就为进一步压缩数据提供了基础。

## 行列式

一个二维平面空间来说，可以使用两个二维向量就可以作为这个空间的一组基，就可以来描述这个空间。所谓的描述这个空间就是这个空间中的任何一个向量都可以被这两个二维向量线性表示出来。

那么行列式描述的就是n 个n 维向量所对应的这样的一个n 维体所对应的体积。行列式表示向量组在空间中形成的**有向**体积。当然在二维空间中就是面积。

![image-20210325111411550](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210325111411550.png)
$$
\begin{array}{l}
\operatorname{det}\left(\begin{array}{ll}
a & b \\
c & d
\end{array}\right) \\
=(a+c)(b+d)-2 b c-c d-a b \\
=a b+c b+a d+c d-2 b c-c d-a b \\
=b c+a d-2 b c \\
=a d-b c
\end{array}
$$
<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210325110736016.png" alt="image-20210325110736016" style="zoom:67%;" />

由于行列式的结果就是一个数字一个值，所以它的方向只有两个或正或负，这和向量不一样。向量在n 维空间中可以指向无数个方向，但是现在行列式的结果就是一个数值，也可以理解成是在一维空间中的，方向只有可能是正或者是负这两种情况。

那么具体在二维空间中，这个有向面积其实相对还是比较好理解的。可以简单的理解成，看这个四边形永远是从逆时针的方方向去看的，或者由于绘制这个二维平面的坐标轴，横轴是x 纵轴是y 所以习惯从x 轴向y 轴这样的一个逆时针的方向去看。如果给出的这两个向量，从x 轴向y 轴这个角度顺过去的话，先看到第一个向量，再看到第二个向量。那么对应的这个面积呢就是正的。如果顺着这个方向看过去，先看到了第二个向量，再看到了第一个向量，得到的这个面积呢就是一个负数。

或者可以想成，如果这个平行四边形它是一个纸片的话，对于这个纸片如果分正反面的话，那么这个第二种情况呢，相当于是把这个纸片在这个二维平面中翻过来放置了。所以需要在这个屏幕的另外一侧才能看到这个纸片的正面。那么在这个屏幕的这一侧呢，看到的是这个纸片的反面，所以它对应的这个面积呢，让它是一个负数。

![image-20210325111624281](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210325111624281.png)

简单说，在行列式中，向量排列的顺序是有意义的。交换两行，则行列式的值取反。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210325145024861.png" alt="image-20210325145024861" style="zoom: 67%;" />
$$
\operatorname{det}(A)=\operatorname{det}\left(A^{T}\right) \\
证明: \\
\begin{array}{l}
\operatorname{det}(A)=\operatorname{det}\left(P L U P^{\prime}\right)=\operatorname{det}(P) \cdot \operatorname{det}(L) \cdot \operatorname{det}(U) \cdot \operatorname{det}\left(P^{\prime}\right) \\
任意A可以分解成PLUP' \\
\operatorname{det}\left(A^{T}\right)=\operatorname{det}\left(\left(P L U P^{\prime}\right)^{T}\right)=\operatorname{det}\left(P^{\prime T} U^{T} L^{T} P^{T}\right) \\
=\operatorname{det}\left(P^{\prime T}\right) \operatorname{det}\left(U^{T}\right) \operatorname{det}\left(L^{T}\right) \operatorname{det}\left(P^{T}\right)
\end{array}
$$

## 特征值 特征向量

$\boldsymbol{A} \vec{u} = \lambda \vec{u}$，即仅改变向量模的大小，而没有改变方向。

$\lambda$称为A的特征值（eigenvalue)
$\vec{u}$称为A对应于$\lambda$的特征向量（eigenvector)

求解特征值和特征向量 $\boldsymbol{A} \vec{u} = \lambda \vec{u}, \boldsymbol{A} = \left(\begin{array}{ll}
4 & -2 \\
1 & 1
\end{array}\right)$ 
$$
\begin{aligned}
A \vec{u}=\lambda \vec{u} & & A \vec{u}-\lambda \vec{u}=0 & \\
& & A \vec{u}-\lambda I \vec{u}=0 & \\
& &(A-\lambda I) \vec{u}=0 & \quad \text { 希望该方程有非零解。 }
\end{aligned}
$$
即 $\operatorname{det}(A - \lambda \boldsymbol{I}) = 0$ , $\lambda = 2, \lambda = 3$，各自对应的特征向量有无数个。

$\boldsymbol{A} \vec{u} = \lambda \vec{u}$，当$\lambda = 0$，$\boldsymbol{A} \vec{u} = 0$，由于特征向量不为零向量，即线性系统不仅有零解，即A不可逆。

### 投影变换

投影变换是不是一个线性变换？如果是一个线性变换，它就一定对应了一个矩阵。怎么看一个变换是不是一个线性变换呢？线性变换的定义：
$T(u+v)=T(u)+T(v)$
$T(cu)=cT(u), c \in R$

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210325170902557.png" alt="image-20210325170902557" style="zoom:67%;" />

通过几何的方式得到一个投影变换所对应的特征值和特征向量，当用几何的视角来研究这个问题的时候，先看特征向量是比较方便的。所谓的特征向量就是经过这种变换之后得到的结果向量和原向量还是在一条直线上，它们之间只相差某一个常数倍，这个常数就是特征值。

不仅如此，在这个图中，其实还直接找到了这两个特征值所对应的特征空间。那么这个特征空间一个就是投影变换要投影到(2,1)这个向量所在的直线，这根直线是一个空间。这个空间中的所有向量都是这个投影变换对于$\lambda=1$的特征向量。另外一个特征空间则是和这根蓝色的直线相垂直的那个直线，它形成了一个特征空间，是投影变换对于$\lambda=0$这个特征值所对应的所有特征向量的集合。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210325170948027.png" alt="image-20210325170948027" style="zoom:67%;" />

对于矩阵$\left(\begin{array}{ll}
0 & 1 \\
1 & 0
\end{array}\right)$，它所对应的特征值和特征向量。

这两个特征空间分别就是$y=x$，这根直线形成了一个空间是一个特征空间。另外一个就是和这根直线垂直的直线，这根直线形成了第二个特征空间。

这个变换其实就是将所有的向量沿$y=x$ 直线做一次翻转.

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210325170106133.png" alt="image-20210325170106133" style="zoom:67%;" />

## 相似矩阵

如果矩阵A,B满足$A=P^{-1}BP$则称A和B相似。

类比相似三角形，相似三角形可以理解成是从不同的视角来观察相同的内容。

$A=P^{-1}BP$，即A变换是在P坐标系下观察的B变换，A和B这两个矩阵所表示的变换是同一个变换。只不过观察所在的坐标系是不同的。观察B这个变换就是在通常的标准坐标系下，而观察A的这个坐标系则是在P这个矩阵所定义的坐标系下，是在两个不同的坐标系下观察同一个变换得到的。结果是不同的得到的这两个结果就分别是矩阵A和矩阵B但是它们本质又是同一个变换。所以就称A和B是相似的。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210326092807264.png" alt="image-20210326092807264" style="zoom:67%;" />

上图即，一个P坐标系下的坐标，先转换到标准坐标系下，再给它进行B变换。进行完B变换之后得到的结果还在标准坐标系下，再转回P这个坐标系下再做成一个P的逆，最终的结果其实和在P这个坐标系下直接对这个坐标进行A变换得到的结果是一致的，这就说明了A和B这两个变换，它们本质是一个变换。只不过我们所观察的坐标系不同。

关于$A=P^{-1}BP$ 和 $B=PAP^{-1}$，理解的方式就是这个等号的左右两边同时乘以一个坐标，关键是在有几何解释的时候乘以哪个坐标系下的坐标有意义。如果P在最后的话，最后肯定要乘以一个在P坐标系下的坐标才有意义。而如果$P^{-1}$在最后，最后相应的要乘以一个在标准坐标系下的坐标才有意义。

那么现在A和B相似，它们本质是一个变换，什么相同呢？
答案是特征方程相同，A和B的特征方程相同，就意味着A和B的特征值是相同的。
证明：
$$
\begin{aligned}
\operatorname{det}(A-\lambda I) &=\operatorname{det}\left(P^{-1} B P-\lambda I\right)=\operatorname{det}\left(P^{-1} B P-\lambda P^{-1} P\right)=\operatorname{det}\left(P^{-1} B P-P^{-1} \lambda P\right) \\
&=\operatorname{det}\left(P^{-1}(B-\lambda I) P\right) \\
&=\operatorname{det}\left(P^{-1}\right) \operatorname{det}(B-\lambda I) \operatorname{det}(P)=\operatorname{det}(B-\lambda I) \operatorname{det}\left(P^{-1} P\right) \\
&=\operatorname{det}(B-\lambda I)
\end{aligned}
$$

## 矩阵对角化

如果A有n个线性无关的特征向量，则A和某个D相似。

$A=PDP^{-1}$，D变换是P坐标系下观察到的A

矩阵D为特征值组成的对角矩阵，P为特征向量组成的矩阵。

## 对称矩阵

对称矩阵的特征值一定是实数
对称矩阵的多重特征值，其对应的特征空间的维度一定等于重数
对称矩阵的几何重数等于代数重数 
对称矩阵一定有n个线性无关的特征向量
对称矩阵一定可以被对角化

对称矩阵的所有不同的特征值对应的特征向量互相垂直
证明：
$$
\begin{array}{l}
\left(\lambda_{1} \vec{v}_{1}\right) \cdot \vec{v}_{2}=\left(\lambda_{1} v_{1}\right)^{T} v_{2}=\left(A v_{1}\right)^{T} v_{2}=v_{1}^{T} A^{T} v_{2}=v_{1}^{T} A v_{2} \\
=v_{1}^{T} \lambda_{2} v_{2}=\lambda_{2} v_{1}^{T} v_{2}=\lambda_{2} \vec{v}_{1} \vec{v}_{2} \\
=\left(\lambda_{1}-\lambda_{2}\right)\left(\vec{v}_{1} \cdot \vec{v}_{2}\right)=0 \\
=\vec{v}_{1} \cdot \vec{v}_{2}=0
\end{array}
$$

A是对称矩阵 ⇿ A可以被正交对角化$A=QDQ^T$，Q为标准正交矩阵

## 奇异值

若A是一个mxn的矩阵，则$A^TA$是一个n*n的方阵，且对称

$A^{T}A$ 可以被正交对角化，拥有n个实数特征值 $\lambda_{1}, \lambda_{2}, \ldots, \lambda_{n}$ ; n个互相垂直的标准特征向量 $\vec{v}_{1}, \vec{v}_{2}, \ldots, \vec{v}_{n}$
$$
\left\|A \vec{v}_{i}\right\|^{2}=\lambda_{i} \\
证明: \\
\begin{aligned}
\left\|A \vec{v}_{i}\right\|^{2}=\left(A \vec{v}_{i}\right) \cdot\left(A \vec{v}_{i}\right) &=\left(A v_{i}\right)^{T} \cdot\left(A v_{i}\right)=v_{i}^{T} A^{T} A v_{i} \\
&=v_{i}^{T}\left(A^{T} A v_{i}\right)=v_{i}^{T}\left(\lambda_{i} v_{i}\right) \\
&=\lambda_{i} v_{i}^{T} v_{i}=\lambda_{i}|| v_{i} \|^{2}=\lambda_{i}
\end{aligned}
$$
即$A^{T} A$的特征值 $>=0$
奇异值 (Singular Value)$\sigma_{i}=\sqrt{\lambda_{i}}$，奇异值就是 $A \vec{v}_{i}$ 的长度

$\{ A \bar{v}_{i}\}$是A的列空间的一组正交基，$\lambda_{i} \neq 0$

如果A有r个不为零的奇异值： $\left\{A \vec{v}_{1}, A \vec{v}_{2}, \ldots, A \vec{v}_{r}\right\} \quad$ 是A的列空间的一组正交基

A的列空间的维度为r $; \operatorname{rank}(\mathrm{A})=\mathrm{r}$

$\left\{\frac{A \vec{v}_{1}}{\sigma_{1}}, \frac{A \vec{v}_{2}}{\sigma_{2}}, \ldots, \frac{A \vec{v}_{k}}{\sigma_{r}}\right\}$ 是A的列空间的一组标准正交基