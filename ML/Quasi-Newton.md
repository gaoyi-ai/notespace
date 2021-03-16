---
title: Quasi-Newton
categories:
- Optimization
tags:
- Quasi-Newton
date: 2021/2/1 10:00:00
updated: 2021/2/2 16:00:00
---

# Quasi-Newton

Quasi-Newton方法的产生源于人们希望使用类似牛顿方法的速度，但又不需要每次都计算Hessian 矩阵。我们的想法是，如果牛顿迭代是$\theta_{n+1}=\theta_n-f^{\prime\prime}(\theta_n)^{-1}f^\prime(\theta_n)$是否有其他矩阵可以用来代替$f^{\prime\prime}(\theta_n)$或$f^{\prime\prime}(\theta_n)^{-1}$？也就是说，我们是否可以使用修改后的迭代，$\theta_{n+1}=\theta_n-B_n^{-1}f^\prime(\theta_n)$其中$B_n$计算更简单，但仍然允许算法快速收敛？

这是一个具有挑战性的问题，因为$f^{\prime\prime}(θn)$给我们提供了很多关于$θ_n$处$f$表面的信息，而丢掉这些信息的结果是，严重的信息损失。

Quasi-Newton的思想是找到问题的解$B_n$
$$
f^\prime(\theta_n)-f^\prime(\theta_{n-1})
=
B_n (\theta_n-\theta_{n-1}).
$$
上述方程有时被称为*secant方程*。首先请注意，这需要我们存储两个值，$θ_n$和$θ_{n-1}$。而且，在一维中，解是不重要的：我们可以简单地将左手边除以$θ_n-θ_{n-1}$。然而，在不止一个维度上，存在着无限多的解，我们需要一些方法来约束问题，以得出合理的答案。

一般来说，准牛顿方法的关键在于，虽然最初我们可能没有多少关于$f$的信息，但随着每次迭代，我们获得的信息就多一点。具体来说，我们通过$f′$的连续差值了解到更多关于Hessian矩阵的信息。因此，每一次迭代，我们都可以将这些新获得的信息纳入我们对Hessian矩阵的估计中。放在矩阵$B_n$上的约束条件是它必须是对称的，并且它必须接近$B_{n-1}$。这些约束条件可以通过增加秩一矩阵来更新$B_n$来满足。

如果我们让$y_n = f^\prime(\theta_n)-f^\prime(\theta_{n-1})$，$s_n = \theta_n-\theta_{n-1}$，那么secant方程为$y_n = B_ns_n$的一个更新程序
$$
B_{n} = B_{n-1} + \frac{y_ny_n^\prime}{y_n^\prime s_n}
- \frac{B_{n-1}s_ns_n^\prime B_{n-1}^\prime}{s_n^\prime B_{n-1}s_n}
$$
上述更新程序是由Broyden，Fletcher，Goldfarb和Shanno(BFGS)提出的。一个类似的方法，它解决了以下的secant方程，Hnyn=snHnyn=sn是由Davidon，Fletcher，和Powell（DFP）提出的。

请注意，在BFGS方法的情况下，我们实际上在牛顿更新中使用了$B^{-1}_n$。但是，并不需要直接求解$B_n$，然后直接反演。我们可以通过[Sherman-Morrison更新公式](https://en.wikipedia.org/wiki/Sherman-Morrison_formula-Morrison_formula)直接更新$B_{n-1}^{-1}$，生成$B_n^{-1}$。通过这个公式，我们可以利用之前的逆矩阵和一些矩阵乘法来生成新的逆矩阵。

|                  *Newton's Method*                   |            *Quasi-Newton Method*            |
| :--------------------------------------------------: | :-----------------------------------------: |
|              Computationally expensive               |            Computationally cheap            |
|                   Slow computation                   |            Fast(er) computation             |
|   Need to iteratively calculate second derivative    |        No need for second derivative        |
| Need to iteratively solve linear system of equations | No need to solve linear system of equations |
|                Less convergence steps                |           More convergence steps            |
|            More precise convergence path             |        Less precise convergence path        |

## Advantages:

虽然表中的最后两行似乎是准牛顿法的缺点，但较快的计算时间最终可以将其平衡。对于大型和复杂的问题，这种平衡是准牛顿法比完全牛顿法的好处，因为总的解决时间更快。

## Disadvantages:

由于Hessian计算的精度不够，导致步数的收敛速度较慢。正因为如此，准牛顿方法可能比使用完整的牛顿方法更慢（因此更差）。这种情况发生在简单的问题上，在这些问题上，实际计算Hessian逆的额外计算时间很低。在这种情况下，无论如何，全牛顿法是更好的。准牛顿方法的另一个潜在缺点是需要存储反Hessian近似。这可能需要大量的内存，因此在大型复杂系统的情况下可能是不利的。

