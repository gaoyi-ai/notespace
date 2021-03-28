---
title: Covariance Matrix
categories:
- Math
- Linear Algebra
- Covariance Matrix
tags:
- covariance matrix
date: 2021/3/27 08:00:00
updated: 2021/3/27 21:00:00
---



> [fouryears.eu](http://fouryears.eu/2016/11/23/what-is-the-covariance-matrix/)

> [Basic linear algebra](http://fouryears.eu/wp-content/uploads/matrixalgebra-en.pdf), introductory statistics and some familiarity with core machine learning concepts (such as PCA and linear models) are the prerequisites of this post. Otherwise it will probably make no sense. An abridged version of this text is also posted on [Quora](https://www.quora.com/Principal-Component-Analysis-What-is-the-intuitive-meaning-of-a-covariance-matrix/answer/Konstantin-Tretyakov).

大多数关于统计的教科书在其第一章中都覆盖了[协方差](https://en.wikipedia.org/wiki/Covariance)。 它被定义为两个随机变量之间的一种有用的“依赖关系度量”：

    ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-1831261b0c8298b6a3d12ddea0042089_l3.svg)

教科书通常会提供一些直觉，说明为什么要按原样定义它，证明一些特性，例如双线性，为多个变量定义_covariance matrix_![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-0d7914803f609097de8b5c6d60216895_l3.svg), and stop there. Later on the covariance matrix would pop up here and there in seeminly random ways. In one place you would have to take its inverse, in another - compute the eigenvectors, or multiply a vector by it, or do something else for no apparent reason apart from "that's the solution we came up with by solving an optimization task".

In reality, though, there are some very good and quite intuitive reasons for why the covariance matrix appears in various techniques in one or another way. This post aims to show that, illustrating some curious corners of linear algebra in the process.

### Meet the Normal Distribution

真正了解协方差矩阵的最佳方法是完全忘记教科书的定义，而从另一个角度出发。 即，根据多元高斯分布的定义：

We say that the vector ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-b5ed505b868cfcd7031913bd2248342c_l3.svg) has a _normal_ (or _Gaussian_) distribution with mean ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e97a786bb8173187895d3dce0bb4d8db_l3.svg) and covariance ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-a4f0532b7aad3b58dde760742a81639d_l3.svg) if:

    ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-c72dc0af31078a6b5c06ca187d4dd3e6_l3.svg)

To simplify the math a bit, we will limit ourselves to the centered distribution (i.e. ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-7b09d67e9d37a5316064a42690e3edb4_l3.svg)) and refrain from writing out the normalizing constant ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-69e3b5e5223a17f77aba2fb25adb4ee2_l3.svg). Now, the definition of the (centered) multivariate Gaussian looks as follows:

    ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-a5145d00c2add0f3651f234371825cbc_l3.svg)

简单得多，不是吗？ 最后，让我们将协方差矩阵定义为高斯分布的参数。 而已。 您很快就会看到它将引领我们前进的地方。

### Transforming the Symmetric Gaussian

考虑对称的高斯分布，即$\bf \Sigma = \bf I$（单位矩阵）的分布。 让我们从中抽取一个样本，它当然是对称的圆形点云：

![](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-f84e1485d8b3863069a06af8991f1421.png)

We know from above that the likelihood of each point in this sample is

(1)   ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-8a2ea394bef3046bc7e53d585738a238_l3.svg)

现在让我们对这些点应用线性变换${\bf A}$，即让${\bf y} = {\bf Ax}$。 假设出于本示例的原因，${\bf A}$将垂直轴缩放0.5，然后将所有内容旋转30度。 我们将获得以下新的点${\bf y}$的云：

![](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-9a3d76f461ec7fe32efbaa1e6bf78c15.png)

What is the distribution of ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-33bf7ea297ac99bffeb0e6886ffa44dc_l3.svg)? Just substitute ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-8287ead1bafba2c5886b001802a3af97_l3.svg) into (1), to get:

(2)   ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-f4d80ecf3e5cddf8057d879ab95d9bf7_l3.svg)

更一般而言，如果我们有任何数据，那么当我们计算其协方差为$\bf \Sigma$时，可以说，如果我们的数据是高斯函数，则可以使用一些变换$\bf A$从对称云中获得它， 我们只是估算了对应于此变换的矩阵$\bf AA^T$

注意，我们不知道实际的$\bf A$，从数学上讲这是完全公平的。 对称高斯变换可以有许多不同的变换，从而导致相同的分布形状。 例如，如果$\bf A$只是某个角度的旋转，则该转换完全不会影响分布的形状。 相应地，对于所有旋转矩阵$\bf AA^T = \bf I$。 当我们看到单位协方差矩阵时，我们真的不知道它是“原始对称”分布还是“旋转对称分布”。 而且我们不应该真正在乎-这两个是相同的。

There is a theorem in linear algebra, which says that any symmetric matrix ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-a4f0532b7aad3b58dde760742a81639d_l3.svg) can be represented as:

(3)   ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-77a7920779e813aa9d2a69434934a91c_l3.svg)

where ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-3b0c42a7b19f512935654deadfb373bf_l3.svg) is orthogonal (i.e. a rotation) and ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-1a357dbbed2ae038cde4b98d0f68ad7d_l3.svg) is diagonal (i.e. a coordinate-wise scaling). If we rewrite it slightly, we will get:

(4)   ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-2e20d88c4a3f98b68c62c23a2538d7a6_l3.svg)

where ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-a9f5741944da2d32dca9fffde8afd6a4_l3.svg).简而言之，这意味着任何协方差矩阵$\bf \Sigma$可能是使用坐标缩放${\bf D}^{1/2}$然后旋转$\bf V$转换数据的结果。 就像在上面带有$\bf x$和$\bf y$的示例中一样。

### Principal Component Analysis

鉴于上述直觉，PCA已经成为一种非常明显的技术。 假设我们得到了一些数据。 让我们假设（或“假装”）它来自正态分布，并让我们提出以下问题：

1.  这些从对称云中产生了我们的数据的旋转$\bf V$和缩放比例${\bf D}^{1/2}$可能是什么？
2.  在应用此转换之前，原始的“对称云”坐标为$\bf x$是什么。
3.  $\bf D$对原始坐标进行了最大比例缩放，从而对现在的数据传播做出了最大贡献。 我们可以只留下那些，把其余的扔掉吗？

如果仅根据（3）将$\bf \Sigma$分解为$\bf V$和$\bf D$，则所有这些问题都可以直接回答。 但是（3）正是$\bf \Sigma$的特征值分解。 我将让您思考一下，您将看到这种观察如何使您获得有关PCA以及更多内容的所有信息。

### The Metric Tensor

Bear me for just a bit more. One way to summarize the observations above is to say that we can (and should) regard ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-5db126f1a8deef39d9d21c191d48061a_l3.svg) as a [metric tensor](https://en.wikipedia.org/wiki/Metric_tensor). A metric tensor is just a fancy formal name for a matrix, which summarizes the _deformation of space_. However, rather than claiming that it in some sense determines a particular transformation ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-752d5cdc8c14f1c2b1f569ac8fb96ba4_l3.svg) (which it does not, as we saw), we shall say that it affects the way we compute _angles and distances_ in our transformed space.

Namely, let us redefine, for any two vectors ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-c8da0c7eecfcb321d7222c9192ef0a6a_l3.svg) and ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e2dc32fffa8cb040682116eb8aab24e6_l3.svg), their [inner product](https://en.wikipedia.org/wiki/Inner_product_space) as:

(5)   ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-ccde20d05d63e8aeddefc2f4e97c1bab_l3.svg)

To stay consistent we will also need to redefine the _norm_ of any vector as

(6)   ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-f724b1da4af1628a260d50940cbee9d0_l3.svg)

and the _distance_ between any two vectors as

(7)   ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-99ad5ced1b604342ad5cd31d96b9c77a_l3.svg)

Those definitions now describe a kind of a “skewed world” of points. For example, a unit circle (a set of points with “skewed distance” 1 to the center) in this world might look as follows:

![](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-cf417484691bffa9846debf02f38469a.png)And here is an example of two vectors, which are considered “orthogonal”, a.k.a. “perpendicular” in this strange world:

![](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-51b454623cc9a71555c081cacecc9f9d.png)

Although it may look weird at first, note that the new inner product we defined is actually just the dot product of the “untransformed” originals of the vectors:

(8)   ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-de617b8c6664fb235e0fbc97b63fb9a3_l3.svg)

The following illustration might shed light on what is actually happening in this ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-14fb1e14301ad034b94e3db3ff52c0c9_l3.svg)-“skewed” world. Somehow “deep down inside”, the ellipse thinks of itself as a circle and the two vectors behave as if they were (2,2) and (-2,2).

![](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-7ab9b429b375292f8b376bf5ba7e5f09.png)

Getting back to our example with the transformed points, we could now say that the point-cloud ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-db9e2b16ef5ae5a44424d800d16cae03_l3.svg) is actually a perfectly round and symmetric cloud “deep down inside”, it just happens to live in a _skewed space_. The deformation of this space is described by the tensor ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-5db126f1a8deef39d9d21c191d48061a_l3.svg) (which is, as we know, equal to ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-7101e833456af07f1d5cfbcb4f5456b4_l3.svg). The PCA now becomes a method for analyzing the _deformation of space_, how cool is that.

### The Dual Space

We are not done yet. There’s one interesting property of “skewed” spaces worth knowing about. Namely, the elements of their [dual space](https://en.wikipedia.org/wiki/Dual_space) have a particular form. No worries, I’ll explain in a second.

Let us forget the whole skewed space story for a moment, and get back to the usual inner product ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-b3a458836e9e83e09f4a930f38a9e9c0_l3.svg). Think of this inner product as a function ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-2ca74aeb629abd611b41a6ada8085bd1_l3.svg), which takes a vector ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-c8da0c7eecfcb321d7222c9192ef0a6a_l3.svg) and maps it to a real number, the dot product of ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-c8da0c7eecfcb321d7222c9192ef0a6a_l3.svg) and ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e2dc32fffa8cb040682116eb8aab24e6_l3.svg). Regard the ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e2dc32fffa8cb040682116eb8aab24e6_l3.svg) here as the _parameter (“weight vector”)_ of the function. If you have done any machine learning at all, you have certainly come across such _linear functionals_ over and over, sometimes in disguise. Now, the set of _all possible linear functionals_ ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-27aa50933bf1dff244750db0dcfb70ad_l3.svg) is known as the _dual space_ to your “data space”_._

Note that each linear functional is determined uniquely by the parameter vector ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e2dc32fffa8cb040682116eb8aab24e6_l3.svg), which has the same dimensionality as ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-c8da0c7eecfcb321d7222c9192ef0a6a_l3.svg), so apparently the dual space is in some sense equivalent to your data space - just the interpretation is different. An element ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-c8da0c7eecfcb321d7222c9192ef0a6a_l3.svg) of your “data space” denotes, well, a data point. An element ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e2dc32fffa8cb040682116eb8aab24e6_l3.svg) of the dual space denotes a function ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-27aa50933bf1dff244750db0dcfb70ad_l3.svg), which _projects_ your data points on the direction ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e2dc32fffa8cb040682116eb8aab24e6_l3.svg) (recall that if ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e2dc32fffa8cb040682116eb8aab24e6_l3.svg) is unit-length, ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-b3a458836e9e83e09f4a930f38a9e9c0_l3.svg) is exactly the length of the perpendicular projection of ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-c8da0c7eecfcb321d7222c9192ef0a6a_l3.svg) upon the direction ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e2dc32fffa8cb040682116eb8aab24e6_l3.svg)). So, in some sense, if ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-c8da0c7eecfcb321d7222c9192ef0a6a_l3.svg)-s are “vectors”, ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e2dc32fffa8cb040682116eb8aab24e6_l3.svg)-s are “directions, perpendicular to these vectors”. Another way to understand the difference is to note that if, say, the elements of your data points numerically correspond to amounts in kilograms, the elements of ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e2dc32fffa8cb040682116eb8aab24e6_l3.svg) would have to correspond to “units per kilogram”. Still with me?

Let us now get back to the skewed space. If ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-c8da0c7eecfcb321d7222c9192ef0a6a_l3.svg) are elements of a skewed Euclidean space with the metric tensor ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-5db126f1a8deef39d9d21c191d48061a_l3.svg), is a function ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-cf07d7e3ac17a9232ae42c70402c307d_l3.svg) an element of a dual space? Yes, it is, because, after all, it is a linear functional. However, the _parameterization_ of this function is inconvenient, because, due to the skewed tensor, we cannot interpret it as projecting vectors upon ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e2dc32fffa8cb040682116eb8aab24e6_l3.svg) nor can we say that ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e2dc32fffa8cb040682116eb8aab24e6_l3.svg) is an “orthogonal direction” (to a separating hyperplane of a classifier, for example). Because, remember, in the skewed space it is not true that orthogonal vectors satisfy ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-4321c1c527e84021f9d5bb5818bb29ee_l3.svg). Instead, they satisfy ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-a324b1f5a0a2a636626ee7eeaa87ee7f_l3.svg). Things would therefore look much better if we parameterized our dual space differently. Namely, by considering linear functionals of the form ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e019c5f49bcb71471a0d95c204796078_l3.svg). The new parameters ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-b91a3db1f8648153a1ab443fe9a0e633_l3.svg) could now indeed be interpreted as an “orthogonal direction” and things overall would make more sense.

However when we work with actual machine learning models, we still prefer to have our functions in the simple form of a dot product, i.e. ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-27aa50933bf1dff244750db0dcfb70ad_l3.svg), without any ugly ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-cf446132f1ef4a26ff6a940c4cef9665_l3.svg)-s inside. What happens if we turn a “skewed space” linear functional from its natural representation into a simple inner product?

(9)   ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-84091d56adccf60763a2162ec33ba4bf_l3.svg)

where ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-cf98e63701642cb88178e7036123f067_l3.svg). (Note that we can lose the transpose because ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-a4f0532b7aad3b58dde760742a81639d_l3.svg) is symmetric).

What it means, in simple terms, is that when you fit linear models in a skewed space, your resulting weight vectors will always be of the form ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-698fe1405685f3ddbfbc5d833643c8ec_l3.svg). Or, in other words, ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-5db126f1a8deef39d9d21c191d48061a_l3.svg) is a _transformation, which maps from “skewed perpendiculars” to “true perpendiculars”_. Let me show you what this means visually.

Consider again the two “orthogonal” vectors from the skewed world example above:

![](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-205e3852e95a4101a0e0f1ffd6e74e52.png)

Let us interpret the blue vector as an element of the _dual space_. That is, it is the ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-b91a3db1f8648153a1ab443fe9a0e633_l3.svg) vector in a linear functional ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-984db7ecc7211c629b2fb8ea53c5e70d_l3.svg). The red vector is an element of the “data space”, which would be mapped to 0 by this functional (because the two vectors are “orthogonal”, remember).

For example, if the blue vector was meant to be a linear classifier, it would have its separating line along the red vector, just like that:

![](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-be356379841323f5d4b954bdc75d3bf2.png)

If we now wanted to use this classifier, we could, of course, work in the “skewed space” and use the expression ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-984db7ecc7211c629b2fb8ea53c5e70d_l3.svg) to evaluate the functional. However, why don’t we find the actual _normal_ ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e2dc32fffa8cb040682116eb8aab24e6_l3.svg) to that red separating line so that we wouldn’t need to do an extra matrix multiplication every time we use the function?

It is not too hard to see that ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-9f389bcb829561e531dba0894718f8ad_l3.svg) will give us that normal. Here it is, the black arrow:

![](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-e7f3dd471ccf0462b164c9c4cb0058cd.png)

Therefore, next time, whenever you see expressions like ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-b12b8e0e9f20f61a7949b2c45f9d8da2_l3.svg) or ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-4305cd2f22172e24f4cdd714fef32678_l3.svg), remember that those are simply _inner products and (squared) distances_ in a skewed space, while ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-698fe1405685f3ddbfbc5d833643c8ec_l3.svg) is a _conversion from a skewed normal to a true normal._ Also remember that the “skew” was estimated by pretending that the data were normally-distributed.

Once you see it, the role of the covariance matrix in some methods like the [Fisher’s discriminant](https://en.wikipedia.org/wiki/Linear_discriminant_analysis) or [Canonical correlation analysis](https://en.wikipedia.org/wiki/Canonical_correlation) might become much more obvious.

### The Dual Space Metric Tensor

“But wait”, you should say here. “You have been talking about expressions like ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-b12b8e0e9f20f61a7949b2c45f9d8da2_l3.svg) all the time, while things like ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-bc258fd8956fe2bbec6c4bfa334a1d4a_l3.svg) are also quite common in practice. What about those?”

Hopefully you know enough now to suspect that ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-bc258fd8956fe2bbec6c4bfa334a1d4a_l3.svg) is again an inner product or a squared norm in some deformed space, just not the “internal data metric space”, that we considered so far. Which space is it? It turns out it is the “internal _dual_ metric space”. That is, whilst the expression ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-b12b8e0e9f20f61a7949b2c45f9d8da2_l3.svg) denoted the “new inner product” between the _points_, the expression ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-bc258fd8956fe2bbec6c4bfa334a1d4a_l3.svg) denotes the “new inner product” between the _parameter vectors_. Let us see why it is so.

Consider an example again. Suppose that our space transformation ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-752d5cdc8c14f1c2b1f569ac8fb96ba4_l3.svg) scaled all points by 2 along the ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-ede05c264bba0eda080918aaa09c4658_l3.svg) axis. The point (1,0) became (2,0), the point (3, 1) became (6, 1), etc. Think of it as changing the units of measurement - before we measured the ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-ede05c264bba0eda080918aaa09c4658_l3.svg) axis in kilograms, and now we measure it in pounds. Consequently, the norm of the point (2,0) according to the new metric, ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-a9954f80fd759b165991a0eae16abcd2_l3.svg) will be 1, because 2 pounds is still just 1 kilogram “deep down inside”.

What should happen to the _parameter ("direction")_ vectors due to this transformation? Can we say that the parameter vector (1,0) also got scaled to (2,0) and that the norm of the parameter vector (2,0) is now therefore also 1? No! Recall that if our initial data denoted kilograms, our dual vectors must have denoted “units per kilogram”. After the transformation they will be denoting “units per pound”, correspondingly. To stay consistent we must therefore convert the parameter vector (”1 unit per kilogram”, 0) to its equivalent (“0.5 units per pound”,0). Consequently, the norm of the parameter vector (0.5,0) in the new metric will be 1 and, by the same logic, the norm of the dual vector (2,0) in the new metric must be 4. You see, the “importance of a parameter/direction” gets scaled inversely to the “importance of data” along that parameter or direction.

More formally, if ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-5a97bb4b0aeb55dc88b030aaae266923_l3.svg), then

(10)   ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-8a310fd0bed82bf1395296a8221fc26c_l3.svg)

This means, that the transformation ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-752d5cdc8c14f1c2b1f569ac8fb96ba4_l3.svg) of the data points implies the transformation ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-05985bb893e1685c9002823e92a6d45c_l3.svg) of the dual vectors. The metric tensor for the dual space must thus be:

(11)   ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-30e0bda6d93e7e5c5d06f3ee12f89c77_l3.svg)

Remember the illustration of the “unit circle” in the ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-49eb04e7bf7286f1e7dfc2a092ea1245_l3.svg) metric? This is how the unit circle looks in the corresponding ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-cf446132f1ef4a26ff6a940c4cef9665_l3.svg) metric. It is rotated by the same angle, but it is stretched in the direction where it was squished before.

![](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-a0401addb808e2ea6190dd391c3a861e.png)

Intuitively, the norm (“importance”) of the dual vectors along the directions in which the data was stretched by ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-752d5cdc8c14f1c2b1f569ac8fb96ba4_l3.svg) becomes proportionally larger (note that the “unit circle” is, on the contrary, “squished” along those directions).

But the “stretch” of the space deformation in any direction can be measured by the variance of the data. It is therefore not a coincidence that ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-b95204d37b5177cec5bdfe32603bb1f5_l3.svg) is exactly the variance of the data along the direction ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-e2dc32fffa8cb040682116eb8aab24e6_l3.svg) (assuming ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-b0848205df65076c807c3033b6c8e386_l3.svg)).

### The Covariance Estimate

Once we start viewing the covariance matrix as a transformation-driven metric tensor, many things become clearer, but one thing becomes extremely puzzling: _why is the inverse covariance of the data a good estimate for that metric tensor_? After all, it is not obvious that ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-3ebc173188c01f214214b32fa3d76b6c_l3.svg) (where ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-6ddcccfbf544d8852883d3601b27b596_l3.svg) is the data matrix) must be related to the ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-cf446132f1ef4a26ff6a940c4cef9665_l3.svg) in the distribution equation ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-7fbc0a157b2f12963d1523303517fe35_l3.svg).

Here is one possible way to see the connection. Firstly, let us take it for granted that if ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-6ddcccfbf544d8852883d3601b27b596_l3.svg) is sampled from a symmetric Gaussian, then ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-3ebc173188c01f214214b32fa3d76b6c_l3.svg) is, on average, a unit matrix. This has nothing to do with transformations, but just a consequence of pairwise independence of variables in the symmetric Gaussian.

Now, consider the transformed data, ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-87b097d4ff2b70b9d52268a6267956df_l3.svg) (vectors in the data matrix are row-wise, hence the multiplication on the right with a transpose). What is the covariance estimate of ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-8ec2cedf898a9494c955eeb5f25dccfc_l3.svg)?

(12)   ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-7fe9f5f8b90f6c7b79a42c9aaf5aa003_l3.svg)

the familiar tensor.

This is a place where one could see that a covariance matrix may make sense outside the context of a Gaussian distribution, after all. Indeed, if you assume that your data was generated from _any_ distribution ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-650eb7688af6737ac325425b5c9a5982_l3.svg) with uncorrelated variables of unit variance and then transformed using some matrix ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-752d5cdc8c14f1c2b1f569ac8fb96ba4_l3.svg), the expression ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-3ebc173188c01f214214b32fa3d76b6c_l3.svg) will still be an estimate of ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-17c575347071986bc36a88c758534118_l3.svg), the metric tensor for the corresponding (dual) space deformation.

However, note that out of _all_ possible initial distributions ![](http://fouryears.eu/wp-content/ql-cache/quicklatex.com-650eb7688af6737ac325425b5c9a5982_l3.svg), the normal distribution is exactly the one with the [maximum entropy](https://en.wikipedia.org/wiki/Maximum_entropy_probability_distribution#Specified_variance:_the_normal_distribution), i.e. the “most generic”. Thus, if you base your analysis on the mean and the covariance matrix (which is what you do with PCA, for example), you could just as well assume your data to be normally distributed. In fact, a good rule of thumb is to remember, that whenever you even _mention_ the word "covariance matrix", you are implicitly fitting a Gaussian distribution to your data.