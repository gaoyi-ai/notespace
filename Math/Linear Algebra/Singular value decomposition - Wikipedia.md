---
title: Geometric meaning in SVD
categories:
- Math
- Linear Algebra
- SVD
tags:
- linear algebra
- svd
date: 2021/3/27 08:00:00
updated: 2021/3/27 21:00:00
---

> [Singular_value_decomposition - wikipedia](https://en.wikipedia.org/wiki/Singular_value_decomposition)

In [linear algebra](/wiki/Linear_algebra "Linear algebra"), the **singular value decomposition** (**SVD**) is a [factorization](/wiki/Matrix_decomposition "Matrix decomposition") of a [real](/wiki/Real_number "Real number") or [complex](/wiki/Complex_number "Complex number") [matrix](/wiki/Matrix_(mathematics) "Matrix (mathematics)") that generalizes the [eigendecomposition](/wiki/Eigendecomposition "Eigendecomposition") of a square [normal matrix](/wiki/Normal_matrix "Normal matrix") to any $m \times n$ matrix via an extension of the [polar decomposition](/wiki/Polar_decomposition#Matrix_polar_decomposition "Polar decomposition").

在线性代数中，奇异值分解（SVD）是实数或复数矩阵的因式分解，它通过极坐标的扩展将平方正规矩阵的特征分解推广为任何$m \times n$矩阵分解。

Specifically, the singular value decomposition of an $m \times n$ complex matrix **M** is a factorization of the form $U \Sigma V^*$, where **U** is an $m \times m$ complex [unitary matrix](/wiki/Unitary_matrix "Unitary matrix"), $\Sigma$ is an $m \times n$ [rectangular diagonal matrix](/wiki/Rectangular_diagonal_matrix "Rectangular diagonal matrix") with non-negative real numbers on the diagonal, and **V** is an $n \times n$ complex unitary matrix. If **M** is real, **U** and  $V^T=V^*$are real [orthogonal](/wiki/Orthogonal_matrix "Orthogonal matrix") matrices.

The diagonal entries $\sigma_i=\Sigma_{ii} $  of $\Sigma$ are known as the **[singular values](/wiki/Singular_value "Singular value")** of **M**. The number of non-zero singular values is equal to the [rank](/wiki/Rank_of_a_matrix "Rank of a matrix") of **M**. The columns of **U** and the columns of **V** are called the **left-singular vectors** and **right-singular vectors** of **M**, respectively.

SVD不是唯一的。 始终可以选择分解，以使奇异值$ \Sigma_{ii}$降序排列。 在这种情况下，$\mathbf{\Sigma}$（但不总是U和V）由M唯一确定。

该术语有时指紧凑的SVD，类似的分解$\mathbf {M} =\mathbf {U\Sigma V^{*}}$，其中$\mathbf {\Sigma}$是大小为$r \times r$的对角线矩阵，其中$r \leq \min \{m,n \}$是M的秩，并且只有非零的奇异值。 

## Intuitive interpretations

### 旋转，坐标缩放和反射

在特殊情况下，当M为m×m实方矩阵时，矩阵U和V⁎也可以选择为m×m实矩阵。在这种情况下，“unitary”与“正交”相同。然后，将酉矩阵和对角矩阵（在此总结为A）解释为空间Rm的线性变换x↦Ax，矩阵U和V⁎表示空间的旋转或反射，而$\mathbf {\Sigma}$ represents the [scaling](/wiki/Scaling_matrix "Scaling matrix") of each coordinate $x_i$ by the factor $σ_i$。因此，SVD分解将Rm的任何可逆线性变换分解为三个几何变换的组合：旋转或反射（V⁎），然后是逐坐标缩放（$\mathbf {\Sigma}$），然后再进行一次旋转或反射（U）。

尤其是，如果**M**具有正的行列式，则**U** 和 **V ** 可以选择既是反射，又是旋转。 如果行列式为负，则其中之一必须是反射。 如果行列式为零，则每个行可以独立选择为任一类型。

If the matrix **M** is real but not square, namely _m_×_n_ with _m_ ≠ _n_, it can be interpreted as a linear transformation from **R**_n_ to **R**_m_. Then **U** and **V**⁎ can be chosen to be rotations of **R**_m_ and **R**_n_, respectively; and $\Sigma$, besides scaling the first  $\min \{m,n\}$coordinates, also extends the vector with zeros, i.e. removes trailing coordinates, so as to turn **R**_n_ into **R**_m_.

### 奇异值作为椭圆或椭球的半轴

如图所示，奇异值可以解释为2D中椭圆的半轴的大小。 这个概念可以推广到n维欧几里德空间，其中任何_n_×_n_ 方矩阵的奇异值被视为 n维椭球的半轴的大小。 同样，任何_m_×_n_矩阵的奇异值都可以看作是m维空间中n维椭球的半轴大小，例如 （倾斜）3D空间中的2D平面。 奇异值编码半轴的大小，而奇异矢量编码方向。 

### The columns of _U_ and _V_ are 正交基

由于U和V⁎是unitary矩阵，因此它们的列构成了一组正交向量，这些向量可被视为基础向量。 矩阵M将基本向量$V_i$映射到扩展的单位向量$σ_iU_i$。 根据酉矩阵的定义，它们的共轭转置$U^*$和V同样如此，只是由于拉伸丢失而对奇异值进行了几何解释。 简而言之，U，U⁎，V和V⁎的列是正交基。 当$\mathbf{M}$是normal矩阵时，U和V都等于用来对角化$\mathbf {M}$的酉矩阵。 但是，当$\mathbf{M}$ not normal但仍可对角化时，其特征分解和奇异值分解就很明显。

### Geometric meaning

![img](https://gitee.com/gaoyi-ai/image-bed/raw/8b20bc52f4f8e0224e142dbe6aaa9921b411d730/images/220px-Singular-Value-Decomposition.svg.png)

Illustration of the singular value decomposition **UΣV**⁎ of a real 2×2 matrix **M**.

- **Top:** The action of **M**, indicated by its effect on the unit disc *D* and the two canonical unit vectors *e*1 and *e*2.
- **Left:** The action of **V**⁎, a rotation, on *D*, *e*1, and *e*2.
- **Bottom:** The action of **Σ**, a scaling by the singular values *σ*1 horizontally and *σ*2 vertically.
- **Right:** The action of **U**, another rotation.

---

![img](https://gitee.com/gaoyi-ai/image-bed/raw/c208ae91ce1132fdcc98c4b0acf7271125d13016/images/280px-Singular_value_decomposition.gif)

Animated illustration of the SVD of a 2D, real [shearing matrix](https://en.wikipedia.org/wiki/Shear_mapping) **M**. First, we see the [unit disc](https://en.wikipedia.org/wiki/Unit_disc) in blue together with the two [canonical unit vectors](https://en.wikipedia.org/wiki/Standard_basis). We then see the actions of **M**, which distorts the disk to an [ellipse](https://en.wikipedia.org/wiki/Ellipse). The SVD decomposes **M** into three simple transformations: an initial [rotation](https://en.wikipedia.org/wiki/Rotation_matrix) $V^*$, a [scaling](https://en.wikipedia.org/wiki/Scaling_matrix) $\mathbf {\Sigma }$ along the coordinate axes, and a final rotation **U**. The lengths $σ_1$ and $σ_2$ of the [semi-axes](https://en.wikipedia.org/wiki/Ellipse#Elements_of_an_ellipse) of the ellipse are the [singular values](https://en.wikipedia.org/wiki/Singular_value) of **M**, namely $Σ_{1,1}$ and $Σ_{2,2}$.

---

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/220px-Singular_value_decomposition_visualisation.svg.png)

Visualisation of the matrix multiplications in singular value decomposition

---

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/220px-Reduced_Singular_Value_Decompositions.svg.png)

简化的SVD变体的可视化。 从上到下：1：完整SVD，2：细SVD（删除不与V *行对应的U列），3：紧凑型SVD（删除U和V *中消失的奇异值以及对应的列/行），4 ：截断的SVD（仅保留最大的t奇异值以及U和V *中的相应列/行）