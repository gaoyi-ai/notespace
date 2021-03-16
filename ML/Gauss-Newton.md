---
title: Gauss–Newton
categories:
- Optimization
tags:
- Gauss–Newton
date: 2021/3/15 20:00:17
updated: 2021/3/15 12:00:17
---

# Gauss–Newton algorithm

高斯-牛顿算法是用来解决非线性最小二乘问题的。它是牛顿法的一种修改，用于寻找函数的最小值。与牛顿法不同的是，Gauss-Newton算法只能用于最小化函数值的平方和，但它的优点是不需要计算具有挑战性的二次导数。

例如，在非线性回归中就会出现非线性最小二乘问题，即寻求模型中的参数，使模型与现有观测值有良好的一致性。

高斯牛顿法是对牛顿法的一种改进，它用雅克比矩阵的乘积近似代替牛顿法中的二阶Hessian 矩阵，从而省略了求二阶Hessian 矩阵的计算。

高斯—牛顿迭代法的基本思想是使用泰勒级数展开式去近似地代替非线性回归模型，然后通过多次迭代，多次修正回归系数，使回归系数不断逼近非线性回归模型的最佳回归系数，最后使原模型的残差平方和达到最小。

对于一个非线性最小二乘问题：
$$
x^{*} = arg \, \min\limits_{x} \frac{1}{2} || f(x) ||^2
$$
高斯牛顿的思想是把$f(x)$利用泰勒展开，取一阶线性项近似。
$$
f(x + \Delta x) = f(x) + f^\prime (x) \Delta x = f(x) +J(x) \Delta x
$$
带入到上式，得：
$$
\frac{1}{2} || f(x + \Delta x) ||^2 = \frac{1}{2}(f(x)^Tf(x) + 2f(x)^TJ(x) \Delta x + \Delta x^TJ(x)^TJ(x) \Delta x)
$$
对上式求导，令导数为0：
$$
J(x)^TJ(x) \Delta x = -J(x)^Tf(x)
$$
令$H = J^TJ \quad B = -J^Tf$得：
$$
H \Delta x = B
$$
求解，便可以获得调整增量 $Δx$ 。这要求 $H$可逆（正定），但实际情况并不一定满足这个条件，因此可能发散，另外步长$Δx$可能太大，也会导致发散。

如果已知观测z的协方差的矩阵$Σ$，应该对指标函数按方差Σ加权，方差大的观测分量权重小，对结果的影响小.
$$
x^{*} = arg \, \min\limits_{x} || f(x) ||_{\Sigma}^2 = arg \, \min\limits_{x} e^{T} \Sigma^{-1} e
$$
迭代公式为：
$$
J(x)^T \Sigma^{-1} J(x) \Delta x = -J(x)^T \Sigma^{-1} f(x)
$$
设信息矩阵$Σ−1$，由Cholesky分解，$\Sigma^{-1} = A^{T}A$, 得：
$$
\underbrace{ J(x)^T A^{T}}_{\tilde{J}^{T}} \underbrace{A J(x) }_{\tilde{J}} \underbrace{\Delta x}_{\delta x} = -\underbrace{J(x)^T A^{T}}_{\tilde{J}^{T}} \underbrace{A f(x)}_{\tilde{f(x)}}
$$
因此加权最小二乘可以转换为非加权问题。

## Example

有时候为了拟合数据，比如根据重投影误差求相机位姿(R,T为方程系数)，常常将求解模型转化为非线性最小二乘问题。高斯牛顿法正是用于解决非线性最小二乘问题，达到数据拟合、参数估计和函数估计的目的。

假设我们研究如下形式的非线性最小二乘问题：
$$
\min _{x \in R^{n}} f(x)=\frac{1}{2} r(x)^{T} r(x)=\frac{1}{2} \sum_{i=1}^{m}\left[r_{i}(x)\right]^{2}, \quad m \geq n
$$
r(x)为某个问题的残差residual,是x的非线性函数。比如对于重投影误差而言，假设有相机位姿$T_{i=1,\dots,m}$和3D特征点$y_{j=1,\dots,n}$,相机T的图像中看到y所在位置为z~i,j~,而根据相机模型将y~j~投影在T~i~中的预测位置为$\hat{z}(T_i,y_j)$,

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20160609001439758)

这两个位置间残差（重投影误差）：$r_{i,j}=z_{i,j} - \hat{z}(T_i,y_j)$

如果有大量观测点(多维)，我们可以通过选择合理的T使得残差的平方和最小求得两个相机之间的位姿。

若用牛顿法求解，则牛顿迭代公式为：

$x^{(k+1)}=x^{(k)}-H^{-1} \nabla f$

其中， $\mathrm{H}$ 为函数 $\mathrm{f}(\mathrm{x})$ 的Hessian矩阵， $\nabla f$ 为 $\mathrm{f}(\mathrm{x})$ 的梯度，它们的数学表达式:

$\nabla f=2 \sum_{i=1}^{m} r_{i} \frac{\delta r_{i}}{\delta x_{j}}$

$H_{j k}=2 \sum_{i=1}^{m}\left(\frac{\delta r_{i}}{\delta x_{j}} \frac{\delta r_{i}}{\delta x_{k}}+r_{i} \frac{\partial^{2} r_{i}}{\partial x_{j} \partial x_{k}}\right)$

高斯牛顿法通过舍弃Hessian矩阵的二阶偏导数实现，也就是:$H_{j k} \approx 2 \sum_{i=1}^{m} J_{i j} J_{i k},$ 其中雅可比矩阵 $\mathrm{J}_{\mathrm{r}}$ 中的元素 $J_{i j}=\frac{\delta r_{i}}{\delta x_{j}}$
那么梯度和Hessian矩阵可以写成如下简化形式:$\nabla f=2 J_{r}^{T} r, \quad H \approx 2 J_{r}^{T} J_{r}$
相应的迭代公式为:$x^{(k+1)}=x^{(k)}+\Delta, \quad \Delta=-\left(J_{r}^{T} J_{r}\right)^{-1} J_{r}^{T} r$

迭代项上。经典高斯牛顿算法迭代步长λ为1.

那回过头来，高斯牛顿法里为啥要舍弃Hessian矩阵的二阶偏导数呢？主要问题是因为牛顿法中Hessian矩阵中的二阶信息项通常难以计算或者花费的工作量很大，而利用整个H的割线近似也不可取，因为在计算梯度$\nabla f$ 时已经得到J(x)，这样H中的一阶信息项$J^T J$几乎是现成的。鉴于此，为了简化计算，获得有效算法，我们可用一阶导数信息逼近二阶信息项。 注意这么干的前提是，残差r接近于零或者接近线性函数从而$\nabla^2 r$接近与零时，二阶信息项才可以忽略。通常称为“小残量问题”，否则高斯牛顿法不收敛。