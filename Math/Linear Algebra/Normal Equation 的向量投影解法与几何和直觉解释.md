---
title: Normal Equation 的向量投影解法与几何和直觉解释
categories:
- Math
- Linear Algebra
tags:
- Normal Equation
date: 2021/4/14 20:00:14
updated: 2021/4/14 12:00:14
---



> [zhuanlan.zhihu.com](https://zhuanlan.zhihu.com/p/269232332)

在[线性回归的正交方程 (Normal Equation) 推导](https://zhuanlan.zhihu.com/p/268561676)一文中提到使用向量投影的方法也可以推导出正交方程，该方法简单到只需 1 步，并能从向量投影的角度体现线性回归的本质。

**预备知识**：向量投影

![](https://pic4.zhimg.com/v2-0f106866f8a31c4fbdc2176852e04d1b_r.jpg)

平面 A 由基向量 ![](https://www.zhihu.com/equation?tex=a_%7B1%7D+%E3%80%81a_%7B2%7D) 所张成（Span），换言之，平面 A 是 ![](https://www.zhihu.com/equation?tex=%5Ba_%7B1%7D%2C+a_%7B2%7D%5D) 的列空间。

![](https://www.zhihu.com/equation?tex=b+) 是平面外的一点， ![](https://www.zhihu.com/equation?tex=p) 是 $b$在平面上的投影， ![](https://www.zhihu.com/equation?tex=p+%3D+A%5Ctilde%7Bx%7D) ，求 ![](https://www.zhihu.com/equation?tex=%5Ctilde%7Bx%7D)

![](https://www.zhihu.com/equation?tex=e)是 ![](https://www.zhihu.com/equation?tex=b)和 ![](https://www.zhihu.com/equation?tex=p)之间的距离 ：![](https://www.zhihu.com/equation?tex=e+%3D+b+-+p+%3D+b-+A%5Ctilde%7Bx%7D)

用人话来解释下： ![](https://www.zhihu.com/equation?tex=a_%7B1%7D+%E3%80%81a_%7B2%7D) 构建了一个平面，平面内的任何向量，均可由它俩通过线性组合构建出来，换言之，平面外的向量它俩就搞不定了，向量$b$**就没有落在** ![](https://www.zhihu.com/equation?tex=a_%7B1%7D+%E3%80%81a_%7B2%7D) **构建的平面内，不论** ![](https://www.zhihu.com/equation?tex=a_%7B1%7D+%E3%80%81a_%7B2%7D) **如何组合都不可能组合出** $b$**，**这个问题**无解。但是，可以组合出** $b$**在平面内的投影** ![](https://www.zhihu.com/equation?tex=p) **，这是平面内最接近** $b$的点， ![](https://www.zhihu.com/equation?tex=e) 是 $b$和 ![](https://www.zhihu.com/equation?tex=p) 之间的偏差。于是，问题就变成了**：如何找到组合** ![](https://www.zhihu.com/equation?tex=%5Ctilde%7Bx%7D) **，使**![](https://www.zhihu.com/equation?tex=A%5Ctilde%7Bx%7D+%3D+p) 。

整个线性代数都在解决一个问题： ![](https://www.zhihu.com/equation?tex=Ax+%3D+b) ，在在明显无解的情况下（方程数 > 变量数），退而求其次，解决一个可以解决的近似问题： ![](https://www.zhihu.com/equation?tex=A%5Ctilde%7Bx%7D+%3D+p) 。

![](https://www.zhihu.com/equation?tex=a_%7B1%7D+%E3%80%81a_%7B2%7D) 垂直于 ![](https://www.zhihu.com/equation?tex=e)

![](https://www.zhihu.com/equation?tex=a_%7B1%7D%5E%7BT%7D%5Ccdot%28b-A%5Ctilde%7Bx%7D%29%3D+0++)

![](https://www.zhihu.com/equation?tex=a_%7B2%7D%5E%7BT%7D%5Ccdot%28b-A%5Ctilde%7Bx%7D%29%3D+0)

![](https://www.zhihu.com/equation?tex=%5Cbegin%7Bbmatrix%7D+a_%7B1%7D%5E%7BT%7D+%5C%5C+a_%7B2%7D%5E%7BT%7D%5Cend%7Bbmatrix%7D%28b-A%5Ctilde%7Bx%7D%29%3D%5Cbegin%7Bbmatrix%7D+0+%5C%5C+0%5Cend%7Bbmatrix%7D)

![](https://www.zhihu.com/equation?tex=A%5E%7BT%7D%28b-A%5Ctilde%7Bx%7D%29%3D0)

![](https://www.zhihu.com/equation?tex=A%5E%7BT%7Db%3DA%5E%7BT%7DA%5Ctilde%7Bx%7D)

![](https://www.zhihu.com/equation?tex=%5Ctilde%7Bx%7D+%3D%28A%5E%7BT%7DA%29%5E%7B-1%7DA%5E%7BT%7Db)

言归正传，回到线性回归的问题：

线性回归要解决的问题是：找到合适的 $\theta$ ，使![](https://www.zhihu.com/equation?tex=X%5Ctheta+%3D+%7By%7D)

实际应用问，方程的数量（样本数）经常远大于变量 / 未知数的数量（特征数），例如以下数据情况：100 条数据 10 个字段，意味着 100 个方程 10 个未知数，显然，这样的方程组是无解的（over determined），所以我们不得不改变目标，**寻找一个最 “接近” 的近似解 ![](https://www.zhihu.com/equation?tex=%7B%5Ctheta%7D)** ，**使得** ![](https://www.zhihu.com/equation?tex=X%5Ctheta+%3D+%5Ctilde%7By%7D) **，** ![](https://www.zhihu.com/equation?tex=%5Ctilde%7By%7D) **是** ![](https://www.zhihu.com/equation?tex=y) **在** $X$ **的列空间的投影**。

将 $X$ 代入 $A$ ，将 ![](https://www.zhihu.com/equation?tex=y) 代入 $b$，将 $\theta$ 代入 ![](https://www.zhihu.com/equation?tex=%5Ctilde%7Bx%7D) ，于是就得到 Normal Equation:

![](https://www.zhihu.com/equation?tex=%5Ctheta%3D%28X%5E%7BT%7DX%29%5E%7B-1%7DX%5E%7BT%7Dy+)

翻译成线性回归的语境：**由于** ![](https://www.zhihu.com/equation?tex=y) **不在** ![](https://www.zhihu.com/equation?tex=x_%7B1%7D+%E3%80%81x_%7B2%7D) **所张成的空间内，不论** ![](https://www.zhihu.com/equation?tex=x_%7B1%7D+%E3%80%81x_%7B2%7D) **如何进行线性组合，都不可能组合出** ![](https://www.zhihu.com/equation?tex=y) **，但是，可以组合出** ![](https://www.zhihu.com/equation?tex=y) **在平面内的投影** ![](https://www.zhihu.com/equation?tex=%5Ctilde%7By%7D) **，线性回归的目标就是找到参数** $\theta$ **，使**![](https://www.zhihu.com/equation?tex=X%5Ctheta+%3D+%5Ctilde%7By%7D) 。

在理解了线性回归的投影本质后，使用向量投影公式，只需一步就可以得到 Normal Equation： ![](https://www.zhihu.com/equation?tex=%5Ctheta%3D%28X%5E%7BT%7DX%29%5E%7B-1%7DX%5E%7BT%7Dy+) 。

也可以表示为： ![](https://www.zhihu.com/equation?tex=%5Ctheta%3DX%5E%7B%2B%7Dy+) ，其中 ![](https://www.zhihu.com/equation?tex=X%5E%7B%2B%7D%3D%28X%5E%7BT%7DX%29%5E%7B-1%7DX%5E%7BT%7D) ，被成为伪**逆矩阵**。

最后从代数直觉的角度再来看一下 Normal Equation 和伪逆矩阵：

回顾我们的出发点 ![](https://www.zhihu.com/equation?tex=X%5Ctheta+%3D+%7By%7D) ，如果 $X$ 可逆，两边同时乘以 ![](https://www.zhihu.com/equation?tex=X%5E%7B-1%7D) ，显然 ![](https://www.zhihu.com/equation?tex=%5Ctheta+%3D+X%5E%7B-1%7Dy) 。

但我们在机器学习中，经常面对的是超定（Overdetermined）方程，方程数（数据点的数量，行，记录）大于未知数（即特征数，列，字段），因此 $X$ 不可逆。

![](https://www.zhihu.com/equation?tex=X%5E%7BT%7DX) 是对称矩阵，也称 Gram 矩阵，它是 ![](https://www.zhihu.com/equation?tex=n%5Ctimes+n) 的方阵，可逆的可能性较高，因此，我们很自然的希望在方程两边同时乘以 ![](https://www.zhihu.com/equation?tex=X%5E%7BT%7D) ，得到： ![](https://www.zhihu.com/equation?tex=X%5E%7BT%7DX%5Ctheta%3DX%5E%7BT%7Dy+) ，再两边同时乘以 ![](https://www.zhihu.com/equation?tex=%28X%5E%7BT%7DX%29%5E%7B-1%7D) 就能到 Normal Equation。这个推导并不严谨， ![](https://www.zhihu.com/equation?tex=X%5E%7BT%7DX) 可能不可逆，但可作为快速记忆公式的方法。