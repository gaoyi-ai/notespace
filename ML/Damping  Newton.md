---
title: 阻尼牛顿法
categories:
- Optimization
tags:
- Damping Newton
date: 2021/3/17 10:00:00
updated: 2021/3/17 16:00:00
---



# 阻尼牛顿法

从上面的推导中看出, 牛顿方向 $-H^{-1} g$ 能使得更新后函数处于极值点, 但是它不一定是极小点, 也就是说 牛顿方向可能是下降方向，也可能是上升方向，以至于当初始点远离极小点时，牛顿法有可能不收敘。因此提 出 阻尼牛顿法, 在牛顿法的基础上, 每次迭代除了计算更新方向 (牛顿方向)，还要对最优步长做一维搜索。

算法步骤
(1) 给定给初始点 $x^{(0)},$ 允许误差 $\epsilon$
(2) 计算点 $x^{(t)}$ 处梯度 $g_{t}$ 和Hessian矩阵 $H,$ 若 $\left|g_{t}\right|<\epsilon$ 则停止迭代
(3) 计算点 $x^{(t)}$ 处的牛顿方向作为搜索方向:
$$
d^{(t)}=-H_{t}^{-1} g_{t}
$$
(4) 从点 $x^{(t)}$ 出发，沿着牛顿方向 $d^{(t)}$ 做一维搜索, 获得最优步长:
$$
\lambda_{t}=\arg \min _{\lambda} f\left(x^{(t)}+\lambda \cdot d^{(t)}\right)
$$
(5) 更新参数
$$
x^{(t+1)}=x^{(t)}+\lambda_{t} \cdot d^{(t)}
$$