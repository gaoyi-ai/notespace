---
title: gradient descent
categories:
- Optimization
tags:
- gradient descent
date: 2021/3/7 10:00:00
updated: 2021/3/7 16:00:00
---

# gradient descent

## 关于步长的讨论(一元函数的情况)

梯度下降方法中$d_k = -g_k$,此时如果要求解精确的步长$\alpha_k$,问题可以描述为:
$$
\alpha^{*}=\underset{\alpha}{\arg \min } f\left(\boldsymbol{x}_{k}+\alpha \boldsymbol{d}_{k}\right)
$$

要求解上式,只要以α为变量求解方程

$$
g\left(\boldsymbol{x}_{k}+\alpha \boldsymbol{d}_{k}\right)=0
$$
但是函数$f(x)$有可能非常复杂,以至于我们无法求解方程

但是我们可以利用$f(x)$的二阶 Taylor展开式来进行近似求解
$$
f\left(x_{k}+\alpha d_{k}\right)=f_{k}+\alpha g_{k} d_{k}+\frac{1}{2} \alpha^{2} G_{k} d_{k}^{2}+o\left(\left\|d_{k}\right\|^{2}\right)
$$
我们在求解的时候忽略$o\left(\left\|d_{k}\right\|^{2}\right)$项，其中g为一阶导，G为二阶导。

我们对关于α函数求导数
$$
f\left(x_{k}+\alpha d_{k}\right) \approx f_{k}+\alpha g_{k} d_{k}+\frac{1}{2} \alpha^{2} G_{k} d_{k}^{2} \\
\frac{d f\left(x_{k}+\alpha d_{k}\right)}{d \alpha}=g_{k} d_{k}+\alpha G_{k} d_{k}^{2} \equiv 0
$$
得到精确的步长值:$\alpha=-\frac{g_{k} d_{k}}{G_{k} d_{k}^{2}}$

## 多元函数的一阶导数和二阶导数

多元函数的一阶导数$g(x)$和二阶导数$G(x)$
$$
g(\boldsymbol{x})=\left[\begin{array}{c}
\frac{\partial f(\boldsymbol{x})}{\partial x_{1}}\\
\frac{\partial f(\boldsymbol{x})}{\partial x_{2}} \\
{\vdots} \\
\frac{\partial f(\boldsymbol{x})}{\partial x_{n}}
\end{array}\right]\in \mathbb{R}^{n},
$$

$$
G(x)=\left[\begin{array}{cccc}
\frac{\partial^{2} f(x)}{\partial x_{1}^{2}} & \frac{\partial^{2} f(x)}{\partial x_{1} \partial x_{2}} & \cdots & \frac{\partial^{2} f(x)}{\partial x_{1} \partial x_{n}} \\
\frac{\partial^{2} f(x)}{\partial x_{2} \partial x_{1}} & \frac{\partial^{2} f(x)}{\partial x_{2}^{2}} & \cdots & \frac{\partial^{2} f(x)}{\partial x_{2} \partial x_{n}} \\
\vdots & \vdots & \ddots & \vdots \\
\frac{\partial^{2} f(x)}{\partial x_{n} \partial x_{1}} & \frac{\partial^{2} f(x)}{\partial x_{n} \partial x_{2}} & \cdots & \frac{\partial^{2} f(x)}{\partial x_{n}^{2}}
\end{array}\right] \in \mathbb{R}^{n \times n}
$$

例：有标量函数$f(\boldsymbol{x})=x^2_1+50x^2_2$,其一阶导数和二阶导数分别为
$$
g(\boldsymbol{x})=\left[\begin{array}{c}
2 x_{1} \\
100 x_{2}
\end{array}\right], \quad G(\boldsymbol{x})=\left[\begin{array}{cc}
2 & 0 \\
0 & 100
\end{array}\right]
$$
注意该函数可以写为向量化的表示
$$
f(x) = \frac{1}{2}\boldsymbol{x^T}\boldsymbol{G}\boldsymbol{x},G=\left[\begin{array}{cc}
2 & 0 \\
0 & 100
\end{array}\right]
$$

## 固定步长的梯度下降法的分析(多元函数的情况)

步长的取值对算法是否收敛极为重要,选取的不好会导致收敛速度慢,甚至发散。

如果能够让迭代步尽量向最优解的目标前进,而不是进行大幅度的震荡,会对迭代的结果更有好处。

在我们的例子中相邻的两次迭代,迭代方向的x轴的方向没有变化,y轴的方向不断发生变化

### Momentum方法

可以组合相邻两步迭代的迭代方向来增強x方向的变化、降低y方向的变化。
出于这种考虑,出现了 Momentum方法。

Momentum方法的迭代过程为

- $\boldsymbol{x}_{k+1}=\boldsymbol{x}_{k}+\boldsymbol{d}_{k}^{\text {Momentum }}$

- 其中 $\boldsymbol{d}_{k}^{\text {Momentum }}=\alpha_{k} \boldsymbol{d}_{k}+$ discount $\times \boldsymbol{d}_{k-1}^{\text {Momentum }},$ 其中 $\alpha_{k}$ 是固定的步长, discount $\in(0,1)$ 。
- $\boldsymbol{d}_{0}^{\text {Momentum }}=\mathbf{0}$

### Nesterov Momentum方法

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210308151847776.png" alt="image-20210308151847776" style="zoom:67%;" />

- 首先按照上一步迭代的方向更新一步(蓝色线)。
- 然后计算这个位置的梯度方向(红色线)。
- 用蓝色线和红色线计算本次迭代的方向(绿色线)

## 精确求解多元函数梯度下降的步长
梯度下降法在每步迭代时要分别确定迭代方向$d_k$和步长$a_k$

迭代方向为$d_k=-g_k$,如果要求能够沿迭代方向下降最快的步长$\alpha$,可以看做一个子问题
$$
\alpha_k = \arg \min_\alpha f(x_k+\alpha d_k)
$$
上面的优化目标函数是一个一元函数,最直接的思路就是令其导数为0,然后求解一元方程。

然而,目标函数的导数可能会非常复杂,从而导致求解α困难。

因此,通常利用目标函数在$x_k$处的二阶 Taylor展式来近似
$$
\begin{aligned}
&f\left(\boldsymbol{x}_{k}+\alpha \boldsymbol{d}_{k}\right) \approx f_{k}+\alpha \boldsymbol{g}_{k}^{T} \boldsymbol{d}_{k}+\frac{1}{2} \alpha^{2} \boldsymbol{d}_{k}^{T} \boldsymbol{G}_{k} \boldsymbol{d}_{k}\\
&\text { 因为在梯度下降法中 } \boldsymbol{d}_{k}=-\boldsymbol{g}_{k}, \text { 因此上式可以写成： }\\
&f\left(\boldsymbol{x}_{k}+\alpha \boldsymbol{d}_{k}\right) \approx f_{k}-\alpha \boldsymbol{g}_{k}^{T} \boldsymbol{g}_{k}+\frac{1}{2} \alpha^{2} \boldsymbol{g}_{k}^{T} \boldsymbol{G}_{k} \boldsymbol{g}_{k}\\
&\frac{d f\left(\boldsymbol{x}_{k}+\alpha \boldsymbol{d}_{k}\right)}{d \alpha} \approx-\boldsymbol{g}_{k}^{T} \boldsymbol{g}_{k}+\alpha \boldsymbol{g}_{k}^{T} \boldsymbol{G}_{k} \boldsymbol{g}_{k}=0
\end{aligned}
$$

$$
\alpha = \frac{\boldsymbol{g}_{k}^{T} \boldsymbol{g}_{k}} 
{\boldsymbol{g}_{k}^{T} \boldsymbol{G}_{k} \boldsymbol{g}_{k}}
$$

