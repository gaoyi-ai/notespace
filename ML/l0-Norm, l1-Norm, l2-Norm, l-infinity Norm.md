---
title: Norm
categories:
- ML
- Norm
tags:
- norm
date: 2021/3/27 20:00:17
updated: 2021/3/27 12:00:17
---



I’m working on things related to norm a lot lately and it is time to talk about it. In this post we are going to discuss about a whole family of norm.

**What is a norm?**

从数学上讲，范数是向量空间或矩阵中所有向量的总大小或长度。 为简单起见，我们可以说范数越高，矩阵或向量的（值）越大。 规范可能有多种形式和名称，包括以下流行名称：_Euclidean distance_，_Mean-squared Error_等。

Most of the time you will see the norm appears in a equation like this:

$\left \|x \right\|$ where  $x$ can be a vector or a matrix.

For example, a Euclidean norm of a vector $a=\begin{bmatrix}3\\-2\\1\end{bmatrix}$  is $\left \|a\right\|_2=\sqrt{3^2+(-2)^2+1^2}=3.742$  which is the size of vector $a$

上面的示例显示了如何计算欧几里得范数，或正式称为$ l_2 $-范数。 除了我们在这里的解释之外，还有许多其他类型的规范，实际上对于每个_实数，_都有一个对应的规范（请注意强调的单词_real_ _number_，这意味着它不仅限于整数。）

Formally the $l_p$-norm of $x$ is defined as:

$\left\|x\right\|_p=\sqrt[p]{\sum_{i}\left|x_i\right|^p}$  where $p \in \mathbb{R}$

That’s it! A p-th-root of a summation of all elements to the p-th power is what we call a norm.

The interesting point is even though every $l_p$-norm is all look  very similar to each other, their mathematical properties are very different and thus their application are dramatically different too. Hereby we are going to look into some of these norms in details.

**l0-norm** 

The first norm we are going to discuss is a $l_0$-norm. By definition, $l_0$-norm of $x$ is

$$
\left\|x\right\|_0=\sqrt[0]{\sum_{i}x_i^0}
$$
严格来说，$ l_0 $ -norm实际上不是一个规范。 它是基数函数，其定义形式为$ l_p $ -norm，尽管许多人称其为norm。 使用它有点棘手，因为其中存在零次幂和零次方。 显然，任何$ x> 0 $都将变为1，但是零次幂，尤其是零次幂的定义问题使这里的事情变得混乱。 So in reality, most mathematicians and engineers use this definition of $l_0$-norm instead:

$$
\left\|x\right\|_0=\#(i|x_i\neq 0)
$$
that is a total number of non-zero elements in a vector.

因为它是许多非零元素，所以有许多应用程序使用$ l_0 $ -norm。 最近，由于_Compressive Sensing_方案的兴起，它变得更加受到关注，该方案试图找到欠定线性系统的最稀疏解决方案。 最稀疏的解决方案是指具有最少非零条目，即最低的$ l_0 $ -norm的解决方案。 通常将此问题视为$ l_0 $ -norm或$ l_0 $ -optimisation的优化问题。

**l0-optimisation**

Many application, including Compressive Sensing, try to minimise the $l_0$-norm of a vector corresponding to some constraints, hence called “$l_0$-minimisation”. A standard minimisation problem is formulated as:

![](https://s0.wp.com/latex.php?latex=min+%5Cleft+%5C%7C+x+%5Cright+%5C%7C_0&bg=ffffff&fg=888888&s=0&c=20201002) subject to ![](https://s0.wp.com/latex.php?latex=Ax+%3D+b&bg=ffffff&fg=888888&s=0&c=20201002)

但是，这样做并非易事。 由于缺乏$ l_0 $-范数的数学表示，因此计算机科学家将$ l_0 $ -minimization视为NP难题，只是说这太复杂了，几乎无法解决。

In many case, $l_0$-minimisation problem is relaxed to be higher-order norm problem such as $l_1$-minimisation and $l_2$-minimisation.

**l1-norm**

Following the definition of norm, $l_1$-norm of $x$ is defined as

![](https://s0.wp.com/latex.php?latex=%5Cleft+%5C%7C+x+%5Cright+%5C%7C_1+%3D+%5Csum_%7Bi%7D+%5Cleft+%7C+x_i+%5Cright+%7C&bg=ffffff&fg=888888&s=0&c=20201002)

This norm is quite common among the norm family. It has many name and many forms among various fields, namely _Manhattan norm_ is it’s nickname. If the $l_1$-norm is computed for a difference between two vectors or matrices, that is

![](https://s0.wp.com/latex.php?latex=SAD%28x_1%2Cx_2%29+%3D+%5Cleft+%5C%7C+x_1-x_2+%5Cright+%5C%7C_1+%3D+%5Csum+%5Cleft+%7C+x_%7B1_i%7D-x_%7B2_i%7D+%5Cright+%7C&bg=ffffff&fg=888888&s=0&c=20201002)

it is called _Sum of Absolute Difference (SAD)_ among computer vision scientists.

In more general case of signal difference measurement, it may be scaled to a unit vector by:

![](https://s0.wp.com/latex.php?latex=MAE%28x_1%2Cx_2%29+%3D+%5Cfrac%7B1%7D%7Bn%7D+%5Cleft+%5C%7C+x_1-x_2+%5Cright+%5C%7C_1+%3D+%5Cfrac+%7B1%7D+%7Bn%7D+%5Csum+%5Cleft+%7C+x_%7B1_i%7D+-+x_%7B2_i%7D+%5Cright+%7C&bg=ffffff&fg=888888&s=0&c=20201002) where $n$ is a size of $x$.

which is known as _Mean-Absolute Error (MAE)_.

**l2-norm**

The most popular of all norm is the $l_2$-norm. It is used in almost every field of engineering and science as a whole. Following the basic definition, $l_2$-norm is defined as

![](https://s0.wp.com/latex.php?latex=%5Cleft+%5C%7C+x+%5Cright+%5C%7C_2+%3D+%5Csqrt%7B%5Csum_%7Bi%7Dx_i%5E2%7D&bg=ffffff&fg=888888&s=0&c=20201002)

$l_2$-norm is well known as a _Euclidean_ norm, which is used as a standard quantity for measuring a vector difference. As in $l_1$-norm, if the Euclidean norm is computed for a vector difference, it is known as a _Euclidean distance_:

![](https://s0.wp.com/latex.php?latex=%5Cleft+%5C%7C+x_1-x_2+%5Cright+%5C%7C_2+%3D+%5Csqrt%7B%5Csum_i+%28x_%7B1_i%7D-x_%7B2_i%7D%29%5E2%7D+&bg=ffffff&fg=888888&s=0&c=20201002)

or in its squared form, known as a _Sum of Squared Difference (SSD)_ among Computer Vision scientists:

![](https://s0.wp.com/latex.php?latex=SSD%28x_1%2Cx_2%29+%3D+%5Cleft+%5C%7C+x_1-x_2+%5Cright+%5C%7C_2%5E2+%3D+%5Csum_i+%28x_%7B1_i%7D-x_%7B2_i%7D%29%5E2&bg=ffffff&fg=888888&s=0&c=20201002)

It’s most well known application in the signal processing field is the _Mean-Squared Error (MSE)_ measurement, which is used to compute a similarity, a quality, or a  correlation between two signals. MSE is

![](https://s0.wp.com/latex.php?latex=MSE%28x_1%2Cx_2%29+%3D+%5Cfrac%7B1%7D%7Bn%7D+%5Cleft+%5C%7C+x_1-x_2+%5Cright+%5C%7C_2%5E2+%3D+%5Cfrac%7B1%7D%7Bn%7D+%5Csum_i+%28x_%7B1_i%7D-x_%7B2_i%7D%29%5E2&bg=ffffff&fg=888888&s=0&c=20201002)

As previously discussed in $l_0$-optimisation section, because of many issues from both a computational view and a mathematical view, many $l_0$-optimisation problems relax themselves to become $l_1$– and $l_2$-optimisation instead. Because of this, we will now discuss about the optimisation of $l_2$.

**l2-optimisation**

As in $l_0$-optimisation case, the problem of minimising $l_2$-norm is formulated by

![](https://s0.wp.com/latex.php?latex=min+%5Cleft+%5C%7C+x+%5Cright+%5C%7C_2&bg=ffffff&fg=888888&s=0&c=20201002) subject to ![](https://s0.wp.com/latex.php?latex=Ax+%3D+b&bg=ffffff&fg=888888&s=0&c=20201002)

假设约束矩阵$ A $满秩，这个问题现在是一个有着无穷的解决方案underdertermined系统。 在这种情况下，目标是从这些无限多个解决方案中得出最佳解决方案，即具有最低的$ l_2 $-范数。 如果直接进行计算，这可能是一件非常繁琐的工作。 幸运的是，这是一个数学技巧，可以对我们的工作有所帮助。

By using a trick of Lagrange multipliers, we can then define a Lagrangian

![](https://s0.wp.com/latex.php?latex=%5Cmathfrak%7BL%7D%28%5Cboldsymbol%7Bx%7D%29+%3D+%5Cleft+%5C%7C+%5Cboldsymbol%7Bx%7D+%5Cright+%5C%7C_2%5E2%2B%5Clambda%5E%7BT%7D%28%5Cboldsymbol%7BAx%7D-%5Cboldsymbol%7Bb%7D%29&bg=ffffff&fg=888888&s=0&c=20201002)

where $\lambda$ is the introduced Lagrange multipliers. Take derivative of this equation equal to zero to find a optimal solution and get

![](https://s0.wp.com/latex.php?latex=%5Chat%7B%5Cboldsymbol%7Bx%7D%7D_%7Bopt%7D+%3D+-%5Cfrac%7B1%7D%7B2%7D+%5Cboldsymbol%7BA%7D%5E%7BT%7D+%5Clambda&bg=ffffff&fg=888888&s=0&c=20201002)

plug this solution into the constraint to get

![](https://s0.wp.com/latex.php?latex=%5Cboldsymbol%7BA%7D%5Chat%7B%5Cboldsymbol%7Bx%7D%7D_%7Bopt%7D+%3D+-%5Cfrac%7B1%7D%7B2%7D%5Cboldsymbol%7BAA%7D%5E%7BT%7D%5Clambda%3D%5Cboldsymbol%7Bb%7D&bg=ffffff&fg=888888&s=0&c=20201002)

![](https://s0.wp.com/latex.php?latex=%5Clambda%3D-2%28%5Cboldsymbol%7BAA%7D%5E%7BT%7D%29%5E%7B-1%7D%5Cboldsymbol%7Bb%7D&bg=ffffff&fg=888888&s=0&c=20201002)

and finally

![](https://s0.wp.com/latex.php?latex=%5Chat%7B%5Cboldsymbol%7Bx%7D%7D_%7Bopt%7D%3D%5Cboldsymbol%7BA%7D%5E%7BT%7D+%28%5Cboldsymbol%7BAA%7D%5E%7BT%7D%29%5E%7B-1%7D+%5Cboldsymbol%7Bb%7D%3D%5Cboldsymbol%7BA%7D%5E%7B%2B%7D+%5Cboldsymbol%7Bb%7D&bg=ffffff&fg=888888&s=0&c=20201002)

By using this equation, we can now instantly compute an optimal solution of the $l_2$-optimisation problem. This equation is well known as the _Moore-Penrose Pseudoinverse_ and the problem itself is usually known as _Least Square_ problem, Least Square regression, or Least Square optimisation.

However, even though the solution of Least Square method is easy to compute, it’s not necessary be the best solution. Because of the smooth nature of $l_2$-norm itself,  it is hard to find a single, best solution for the problem.

[![](https://rorasa.files.wordpress.com/2012/05/l2.png?w=288)](https://rorasa.files.wordpress.com/2012/05/l2.png)

In contrary, the $l_1$-optimisation can provide much better result than this solution.

**l1-optimisation**

As usual, the $l_1$-minimisation problem is formulated as

![](https://s0.wp.com/latex.php?latex=min+%5Cleft+%5C%7C+x+%5Cright+%5C%7C_1&bg=ffffff&fg=888888&s=0&c=20201002) subject to ![](https://s0.wp.com/latex.php?latex=Ax+%3D+b&bg=ffffff&fg=888888&s=0&c=20201002)

Because the nature of $l_1$-norm is not smooth as in the $l_2$-norm case, the solution of this problem is much better and more unique than the $l_2$-optimisation.

[![](https://rorasa.files.wordpress.com/2012/05/l1.png?w=288)](https://rorasa.files.wordpress.com/2012/05/l1.png)

However, even though the problem of $l_1$-minimisation has almost the same form as the $l_2$-minimisation, it’s much harder to solve. Because this problem doesn’t have a smooth function, the trick we used to solve $l_2$-problem is no longer valid.  The only way left to find its solution is to search for it directly. Searching for the solution means that we have to compute every single possible solution to find the best one from the pool of “infinitely many” possible solutions.

由于没有数学上简单的方法来找到该问题的解决方案，因此几十年来，$ l_1 $ -optimization的用处非常有限。 直到最近，具有高计算能力的计算机的发展使我们能够“扫视”所有解决方案。 通过使用许多有用的算法，即_Convex Optimisation_算法（例如线性编程或非线性编程等），现在可以找到针对该问题的最佳解决方案。 现在，许多依赖$ l_1 $优化的应用程序都可以使用，包括压缩感测。

There are many toolboxes  for $l_1$-optimisation available nowadays.  These toolboxes usually use different approaches and/or algorithms to solve the same question. The example of these toolboxes are [l1-magic](http://users.ece.gatech.edu/~justin/l1magic/), [SparseLab](http://sparselab.stanford.edu/), [ISAL1](http://imo.rz.tu-bs.de/mo/spear/),

Now that we have discussed many members of norm family, starting from $l_0$-norm, $l_1$-norm, and $l_2$-norm. It’s time to move on to the next one. As we discussed in the very beginning that there can be any l-whatever norm following the same basic definition of norm, it’s going to take a lot of time to talk about all of them. Fortunately, apart from $l_0$-, $l_1$– , and $l_2$-norm, the rest of them usually uncommon and therefore don’t have so many interesting things to look at. So we’re going to look at the extreme case of norm which is a $l_{\infty}$-norm (l-infinity norm).

**l-infinity norm**

As always, the definition for $l_{\infty}$-norm is

![](https://s0.wp.com/latex.php?latex=%5Cleft+%5C%7C+x+%5Cright+%5C%7C_%7B%5Cinfty%7D+%3D+%5Csqrt%5B%5Cinfty%5D%7B%5Csum_i+x_i%5E%7B%5Cinfty%7D%7D&bg=ffffff&fg=888888&s=0&c=20201002)

Now this definition looks tricky again, but actually it is quite strait forward. Consider the vector $\boldsymbol{x}$, let’s say if $x_j$ is the highest entry in the vector  $\boldsymbol{x}$, by the property of the infinity itself, we can say that

![](https://s0.wp.com/latex.php?latex=x_j%5E%7B%5Cinfty%7D%5Cgg+x_i%5E%7B%5Cinfty%7D&bg=ffffff&fg=888888&s=0&c=20201002) ![](https://s0.wp.com/latex.php?latex=%5Cforall+i+%5Cneq+j&bg=ffffff&fg=888888&s=0&c=20201002)

 then

![](https://s0.wp.com/latex.php?latex=%5Csum_i+x_i%5E%7B%5Cinfty%7D+%3D+x_j%5E%7B%5Cinfty%7D&bg=ffffff&fg=888888&s=0&c=20201002)

then

![](https://s0.wp.com/latex.php?latex=%5Cleft+%5C%7C+x+%5Cright+%5C%7C_%7B%5Cinfty%7D+%3D+%5Csqrt%5B%5Cinfty%5D%7B%5Csum_i+x_i%5E%7B%5Cinfty%7D%7D+%3D+%5Csqrt%5B%5Cinfty%5D%7Bx_j%5E%7B%5Cinfty%7D%7D+%3D+%5Cleft+%7C+x_j+%5Cright+%7C&bg=ffffff&fg=888888&s=0&c=20201002)

Now we can simply say that the $l_{\infty}$-norm is

![](https://s0.wp.com/latex.php?latex=%5Cleft+%5C%7C+x+%5Cright+%5C%7C_%7B%5Cinfty%7D+%3D+max%28%5Cleft+%7C+x_i+%5Cright+%7C%29&bg=ffffff&fg=888888&s=0&c=20201002)

that is the maximum entries’ magnitude of that vector. That surely demystified the meaning of $l_{\infty}$-norm

Now we have discussed the whole family of norm from $l_0$ to $l_{\infty}$, I hope that this discussion would help understanding the meaning of norm, its mathematical properties, and its real-world implication.

_**Reference and further reading:**_

[Mathematical Norm – wikipedia](http://en.wikipedia.org/wiki/Norm_(mathematics)) 

[Mathematical Norm – MathWorld](http://mathworld.wolfram.com/Norm.html)

Michael Elad – “Sparse and Redundant Representations : From Theory to Applications in Signal and Image Processing” , Springer, 2010.

 [Linear Programming – MathWorld](http://mathworld.wolfram.com/LinearProgramming.html)

[Compressive Sensing – Rice University](http://dsp.rice.edu/cs)

> [l0-norm-l1-norm-l2-norm-l-infinity-norm](https://rorasa.wordpress.com/2012/05/13/l0-norm-l1-norm-l2-norm-l-infinity-norm