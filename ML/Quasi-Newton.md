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

割线法：拟牛顿法的前身
---------------

要说拟牛顿法（Quasi-Newton Method）必然要先提到上一节说的牛顿法。如果我们不用一般的情况来看它，而直接考虑一元的情况，其实对应的就是下面这张图

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-fa62d72f5aa4f7a933b85a186895f674_r.jpg" style="zoom:67%;" />

注意这张图是某一个函数的**导函数的图像**。在这种情况下，我们可以看出，如果在导函数上某一点做切线，这条切线的斜率就是二次导函数，并且对应的就是下面这个式子

> $f''(x_k)=-\frac{f'(x_k)}{x_{k+1}-x_k}$

化简一下，就是$x_{k+1}=x_k-\frac{f'(x_k)}{f''(x_k)}$，这个就是牛顿法的一元形式。如果我们不使用导函数的切线，**而是割线**，那么这个时候，会有下面这张图

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-834da6b65b6aa9a4119a3c827c60ab3b_r.jpg" style="zoom:67%;" />

我们可以看出，这个时候考虑的是导函数之前两个点所形成的割线，那么这个时候会得到一个式子为

> $B_k(x_k-x_{k-1})=f'(x_k)-f'(x_{k-1})$

这里的$B_k$就是**这条割线的斜率**。所以割线法其实就是拟牛顿法的前身，因为如果我们设$s_k=x_{k+1}-x_k$，$y_k=\nabla f(x_{k+1})-\nabla f(x_k)$，式子就会变成

> $B_{k}s_{k-1}=y_{k-1}$

这就是拟牛顿法的本质。拟牛顿法可以好用，一个很重要的地方在于它**不需要精确计算二阶信息**。因为二阶信息本质上是一个矩阵，在大规模问题上计算一个矩阵的复杂度是难以接受的，所以拟牛顿法自然就有了它独特的优势。

但是我相信你也发现了，对于一般的n元的情况，$B_k$为一个**对称正定矩阵**的情况下，这个方程组只有n-1个，这是因为我们需要两个初始条件，就必然会使得方程组的条件减少一个。因此我们需要其他额外的条件，而这些额外的条件就促使了各种各样的拟牛顿法的诞生。

Quasi-Newton方法的产生源于人们希望使用类似牛顿方法的速度，但又不需要每次都计算Hessian 矩阵。我们的想法是，如果牛顿迭代是$\theta_{n+1}=\theta_n-f^{\prime\prime}(\theta_n)^{-1}f^\prime(\theta_n)$是否有其他矩阵可以用来代替$f^{\prime\prime}(\theta_n)$或$f^{\prime\prime}(\theta_n)^{-1}$？也就是说，我们是否可以使用修改后的迭代，$\theta_{n+1}=\theta_n-B_n^{-1}f^\prime(\theta_n)$其中$B_n$计算更简单，但仍然允许算法快速收敛？

> 拟Newton方法的思路
>
> • 假定当前迭代点为 $𝑥_{𝑘+1}$ ，若用已得到的 $x_k$ ， $x_{k+1}$ 以及一阶导数信息 $𝑔_𝑘$ 和 $𝑔_{𝑘+1}$ ，构造一个正定矩阵 $𝐵_{𝑘+1}$ 作为 $𝐺_{𝑘+1}$ 的近似，这样下降方向 $𝑑_{𝑘+1}$ 由方程组 $𝐵_{𝑘+1}𝑑 = −𝑔_{𝑘+1}$ 给出。
> • 或者用相同的信息构造一个矩阵 $𝐻_{𝑘+1}$ 作为 $𝐺_{𝑘+1} −1$ 的近似，这样下降方向 $𝑑_{𝑘+1}$ 可以直接由 $𝑑 = −𝐻_{𝑘+1}𝑔_{𝑘+1}$ 决定。
>
> 拟Newton条件
> • 𝑔(𝑥) 在点 $𝑥_{𝑘+1}$ 处做Taylor展开得： $𝑔(𝑥) =g(x_{k+1} + (x-x_{k+1}))= 𝑔(𝑥_{𝑘+1}) + 𝐺_{𝑘+1}(𝑥 − 𝑥_{𝑘+1})$
> • 令 𝑥 = $𝑥_𝑘$ ，有： $𝑔(𝑥_𝑘) = 𝑔(𝑥_{𝑘+1}) + 𝐺_{𝑘+1}( 𝑥_𝑘 − 𝑥_{𝑘+1}) $
> • 记 $𝑠_𝑘 = 𝑥_{𝑘+1} − 𝑥_𝑘$ 和 $𝑦_𝑘 = 𝑔_{𝑘+1} − 𝑔_𝑘$ ，则有： $𝐺_{𝑘+1}𝑠_𝑘 = 𝑦_𝑘$

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

