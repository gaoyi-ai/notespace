---
title: Quasi-Newton
categories:
- Optimization
tags:
- Levenberg-Marquardt
date: 2021/3/17 10:00:00
updated: 2021/3/17 16:00:00
---

# Levenberg-Marquardt

## 1、前言

- 对于工程问题，一般描述为：**从一些测量值（观测量）x 中估计参数 p？即 x = f(p)，**其中，x 为测量值构成的向量，参数 p 为待求量，为了让模型能适应一般场景，这里 p 也为向量。
- 这是一个函数求解问题，可以使用 Guass-Newton 法进行求解，LM 算法是对 Newton 法的改进。
- 如果函数 f 为线性函数，那这个问题就变成了最小二乘问题 
- LM 法、Newton 法主要用于函数 f 为非线性函数的情况。

## 2、x = f(p) 问题的 Newton 法求解

当迭代到第 k 次的时候得到参数$p_k$，其中为$\varepsilon_k$残差：

$$
x-f({p_k})={\varepsilon_k}
$$
对 f(p) 进行一阶泰勒公式展开, J 为 Jacobian 矩阵，因为参数 p 是个向量，因此对 p 的求导即对 p 逐个元素求偏导：

$$
f({p_{k+1}})=f({p_k}+\Delta)\approx{f({p_k})+J\Delta}\quad(2)
$$
计算第 k+1 次的残差：

$$
x-f({p_{k+1}})=x-f({p_k})-J\Delta={\varepsilon_k}-J\Delta
$$
通过第 k 次到第 k+1 次的迭代，

可以发现已经把非线性问题$x-f({p_{k+1}})=0$转化为线性求解${\varepsilon_k}-J\Delta=0$，则最小二乘解为：

$$
{J^T}J\Delta={J^T}{\varepsilon_k}
$$

$$
\Delta={({J^T}J)^{-1}}{J^T}{\varepsilon_k}
$$

则 k+1 次的参数 p 为：

$$
{p_{k+1}}={p_k}+\Delta
$$

## 3、加权 Newton 迭代

在 Newton 法中，所有的因变量都是等量加权的，除此之外，可以使用一个加权的矩阵对因变量进行加权。

例如，当测量矢量 x 满足一个协方差矩阵为![](http://latex.codecogs.com/gif.latex?%7b%5cSigma_x%7d)的高斯分布，且希望最小化 Mahalanobis 距离$||x-f(p)|{|_\Sigma}$

当这个协方差矩阵可以是对角的，则表示 x 各坐标之间相互独立。

当协方差矩阵为正定对称矩阵时，正规变为：

$$
{J^T}{\Sigma^{-1}}J{\Delta_k}={J^T}{\Sigma^{-1}}{\varepsilon_k}
$$

$$
{\Delta_k}={({J^T}{\Sigma^{-1}}J)^{-1}}{J^T}{\Sigma^{-1}}{\varepsilon_k}
$$

马氏距离$d(x,y)=\sqrt{(x-y)^T{\Sigma^{-1}}(x-y)}$

通过协方差反向传播，一阶近似下的协方差可以这么计算：

$$
\Sigma={({J^T}\Sigma{_x^{-1}}J)^{-1}}
$$
如果不可逆，那这个取逆过程为广义逆。

## 4、Levenberg-Marquardt 迭代（LM 算法）

LM 算法是对 Newton 迭代的改进。

${J^T}J\Delta={J^T}{\varepsilon_k}$式的正规方程可以简化写成：$N\Delta={J^T}J\Delta={J^T}\varepsilon$

LM 算法将上式改为：$N'\Delta={J^T}\varepsilon$，其中$N_{ii}^{'}=(1+ \lambda) N_{ii}$，即 N 的对角线元素乘以$(1+\lambda)$，非对角线元素不变${N_{ij}^{'}}={N_{ij}}\quad(i{\ne}j)$

$\lambda$的设定策略为：在初始化时，$\lambda$通常设定为$10^{-3}$。

如果通过解增量正规方程得到的$\Delta$导致误差减小，那么接受该增量并在下一次迭代前将$\lambda$除以 10。

反之，如果$\Delta$值导致误差增加，那么将$\lambda$乘以 10 并重新解增量正规方程，继续这一过程直到求出的一个误差下降的$\Delta$为止。

对不同的$\lambda$重复地解增量正规方程直到求出一个可以接受的$\Delta$。

LM 算法的直观解释：当$\lambda$非常小时，该方法与 Newton 迭代本质相同。

当$\lambda$非常大时（本质上大于 1），此时${J^T}J$的非对角线元素相对于对角元素而言变得不重要，此时算法倾向于下降法。

LM 算法在 Newton 迭代和下降方法之间无缝地移动，Newton 法将使得算法在解的领域附近快速收敛，下降法使得算法在运行困难时保证代价函数是下降的。

