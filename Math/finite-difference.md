---
title: 有限差分法
categories:
- Math
- Calculus
tags:
- Derivative
- Taylor's theorem
date: 2021/3/4 10:00:00
updated: 2021/3/4 16:00:00
---

# 有限差分法

在[数学](https://zh.wikipedia.org/wiki/数学)中，**有限差分法**（**finite-difference methods**，简称**FDM**），是一种[微分方程](https://zh.wikipedia.org/wiki/微分方程)[数值方法](https://zh.wikipedia.org/wiki/数值方法)，是通过有限[差分](https://zh.wikipedia.org/wiki/差分)来[近似](https://zh.wikipedia.org/wiki/近似)[导数](https://zh.wikipedia.org/wiki/导数)，从而寻求微分方程的近似解。

## 由泰勒展开式的推导

首先假设要近似函数的各级导数都有良好的性质，依照[泰勒定理](https://zh.wikipedia.org/wiki/泰勒定理)，可以形成以下的[泰勒展开式](https://zh.wikipedia.org/wiki/泰勒展開式)：
$$
f(x_{0}+h)=f(x_{0})+{\frac  {f'(x_{0})}{1!}}h+{\frac  {f^{{(2)}}(x_{0})}{2!}}h^{2}+\cdots +{\frac  {f^{{(n)}}(x_{0})}{n!}}h^{n}+R_{n}(x),
$$
其中*n*!表示是*n*的[阶乘](https://zh.wikipedia.org/wiki/階乘)，*R**n*(*x*)为余数，表示泰勒多项式和原函数之间的差。可以推导函数*f*一阶导数的近似值：
$$
f(x_{0}+h)=f(x_{0})+f'(x_{0})h+R_{1}(x),
$$
设定x0=a，可得：
$$
f(a+h)=f(a)+f'(a)h+R_{1}(x),
$$
除以*h*可得：
$$
{f(a+h) \over h}={f(a) \over h}+f'(a)+{R_{1}(x) \over h}
$$
求解f'(a)：
$$
f'(a)={f(a+h)-f(a) \over h}-{R_{1}(x) \over h}
$$
假设$R_1(x)$相当小，因此可以将"f"的一阶导数近似为：
$$
f'(a)\approx {f(a+h)-f(a) \over h}.
$$


## 准确度及误差

近似解的误差定义为近似解及解析解之间的差值。有限差分法的两个误差来源分别是[舍入误差](https://zh.wikipedia.org/wiki/捨入誤差)及[截尾误差](https://zh.wikipedia.org/w/index.php?title=截尾誤差&action=edit&redlink=1)（或称为离散化误差），前者是因为电脑计算小数时四舍五入造成的误差，后者则是用有限阶级数表示导数引起的误差。

有限差分法是以在格点上函数的值为准

在运用有限差分法求解一问题（或是说找到问题的近似解）时，第一步需要将问题的定义域离散化。一般会将问题的定义域用均匀的网格分割（可参考下图）。因此有限差分法会制造一组导数的离散数值近似值。

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/220px-Finite_Differences.svg.png)