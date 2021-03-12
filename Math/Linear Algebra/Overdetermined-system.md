---
title:  Overdetermined system
categories:
- Math
- Linear Algebra
tags:
- overdetermined system
date: 2021/3/7 08:00:00
updated: 2021/3/7 21:00:00
---

# Overdetermined system

在数学中，如果一个方程系统的方程数多于未知数，那么这个方程系统就被认为是超前的，[1][2][引文]
一个超前的系统在用随机系数构造时，几乎总是不一致的（它没有解）。然而，超前系统在某些情况下会有解，例如，如果一些方程在系统中出现了几次，或者一些方程是其他方程的线性组合。

该术语可以用约束计数的概念来描述。每个未知数都可以看作是一个可用的自由度。每一个引入系统的方程都可以被看作是限制一个自由度的约束条件。因此，当方程的数量和自由变量的数量相等时，就会出现临界情况。对于每一个给出一个自由度的变量，都存在一个相应的约束条件。当系统受到过度约束时，也就是方程数超过未知数时，就会出现过度确定的情况。与此相反，当系统受到约束不足时，即方程数少于未知数时，就会出现欠确定的情况。这种系统通常有无限的解。

## Systems of equations

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/220px-3_equations_-1.JPG)

_\#1 A system of three linearly independent equations, three lines, no solutions_

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/220px-3_equations_-2.JPG)

_\#2 A system of three linearly independent equations, three lines (two [parallel), no solutions_

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/220px-3_equations_-3.JPG)

_\#3 A system of three linearly independent equations, three lines (all parallel), no solutions_

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/220px-3_equations_-4.JPG)

_\#4 A system of three equations (one equation linearly dependent on the others), three lines (two coinciding), one solution_

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/220px-3_equations_-5.JPG)

_\#5 A system of three equations (one equation linearly dependent on the others), three lines, one solution_

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/220px-3_equations_-6.JPG)

_\#6 A system of three equations (two equations each linearly dependent on the third), three coinciding lines, an infinitude of solutions_

### An example in two dimensions

考虑3个方程和2个未知数(X和Y)组成的系统，由于3>2，所以它是超前的，它对应于图1。

$$
{\displaystyle {\begin{aligned}Y&=-2X-1\\Y&=3X-2\\Y&=X+1.\end{aligned}}}
$$

每一对线性方程都有一个解：第一、二方程（0.2，-1.4），第一、三方程（-2/3，1/3），第二、三方程（1.5，2.5）。但是，没有同时满足这三个方程的解。图2和图3显示了其他不一致的配置，因为没有一点在所有的线上。这种多样性的系统被认为是不一致的。

只有在图4，5，6中显示了超前系统有解的情况。只有当超前系统包含足够多的线性依赖方程，独立方程的数量不超过未知数时，才会出现这些例外情况。线性依赖是指一些方程可以通过线性组合其他方程得到。例如，Y＝X＋1和2Y＝2X＋2是线性依赖方程，因为第二个方程可以通过取第一个方程的两倍得到。

### Matrix form

任何线性方程组都可以写成矩阵方程。前面的方程组（在图1中）可以写成如下。
$$
{\displaystyle {\begin{bmatrix}2&1\\-3&1\\-1&1\\\end{bmatrix}}{\begin{bmatrix}X\\Y\\\end{bmatrix}}={\begin{bmatrix}-1\\-2\\1\\\end{bmatrix}}}
$$

注意系数矩阵的行（对应方程）比列（对应未知数）多，这意味着系统是超前确定的。该矩阵的秩为2，对应于系统中的因变量数[3]，如果且仅当系数矩阵与其增广矩阵具有相同的秩时，线性系统才是一致的。增广矩阵的秩为3，所以系统不一致。无效值(nullity)为0，这意味着空空间(null space)只包含零向量，因此没有基础。

在线性代数中，行空间、列空间和空空间的概念对于确定矩阵的性质非常重要。上面关于约束条件和自由度的非正式讨论直接与这些比较正式的概念有关。

## Homogeneous case

同质情况（所有常数项都为零）总是一致的（因为有一个微不足道的、全零的解）。根据线性依赖方程的数量，有两种情况：要么只有微不足道的解，要么有微不足道的解加上无限的其他解集。

考虑线性方程组。1≤i≤M的Li=0 变量X1, X2, ..., XN, 其中每个Li是Xis的加权和. 那么X1＝X2＝......＝XN＝0总是一个解。当M＜N时，系统是欠确定的，总有无穷多的进一步的解。事实上，解的空间维度总是至少为N-M。

当M≥N时，除了所有值都为0外，可能没有其他解，只有当方程系统有足够多的依赖关系（线性依赖方程），独立方程的数量最多为N - 1时，才会有无穷多的其他解。但当M≥N时，独立方程的数量可能高达N，在这种情况下，琐碎的解是唯一的解。

## Non-homogeneous case

在线性方程系统中，对于1≤i≤M的Li=ci，在变量X1，X2，...，XN中，方程有时是线性依赖的，事实上线性独立方程的数量不能超过N+1。对于一个有N个未知数和M个方程（M>N）的超前系统，我们有以下可能的情况。

M=N+1，所有M个方程都是线性独立的。这种情况下不会产生解。例如：x=1，x=2。
M>N，但只有K方程（K<M且K≤N+1）是线性独立的。这其中存在三种可能的子情况。
K = N+1. 这种情况下不会产生解。例：2x=2，x=1，x=2。
K=N.这种情况下，要么产生一个解，要么不产生解，后者发生在一个方程的系数向量可以被其他方程的系数向量的加权和所复制，但该加权和应用于其他方程的常数项不能复制一个方程的常数项。有解的例子：2x=2，x=1。无解的例子：2x＋2y＝2，x＋y＝1，x＋y＝3。
K < N.这种情况下，要么有无限多的解，要么无解，后一种情况与前一种子情况一样。有无限多解的例子。3x + 3y = 3, 2x + 2y = 2, x + y = 1. 无解的例子：3x+3y+3z 3x + 3y + 3z = 3, 2x + 2y + 2z = 2, x + y + z = 1, x + y + z = 4.
通过使用高斯消除法将系统系数的增强矩阵放在行梯队形式中，这些结果可能更容易理解。这个行梯队形式是与给定系统等价的方程系统的增强矩阵（它的解完全相同）。原系统中的独立方程数就是梯队形式中非零行数。如果且仅当梯队形式中最后一行非零行只有一个非零条目，且该条目在最后一列中（给出方程0=c，其中c为非零常数），则该系统是不一致的（无解）。否则，当梯队形式中的非零行数等于未知数时，正好有一个解，当非零行数小于变量数时，有无限多的解。

换一种说法，根据Rouché-Capelli定理，任何方程系统（无论是否超前），如果增强矩阵的秩大于系数矩阵的秩，则方程系统是不一致的。另一方面，如果这两个矩阵的秩相等，则系统至少有一个解。如果且仅当秩等于变量数时，解是唯一的。否则，一般解有k个自由参数，其中k是变量数和秩的差值；因此在这种情况下，有无穷多的解。

## Approximate solutions

普通最小二乘法可以用来寻找过定系统的近似解。对于系统$Ax=b$，由问题可得最小二乘法公式
$$
\min _{{x}}\Vert Ax-b\Vert
$$
其解可写成normal equations,
$$
x=(A^{\mathrm{T}} A)^{-1} A^{\mathrm  {T}} b
$$
其中T表示矩阵转置，但前提是$(A^TA)^{-1}$存在(也就是说，前提是A具有完整的列级)。通过这个公式，当没有精确解时，可以找到一个近似解，当有精确解时，可以给出一个精确解。但是，为了达到良好的数值精度，最好使用A的QR因子化来解决最小二乘问题[5]。