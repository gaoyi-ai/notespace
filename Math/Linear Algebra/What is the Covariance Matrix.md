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

Most textbooks on statistics cover covariance right in their first chapters. It is defined as a useful "measure of dependency" between two random variables:
大多数关于统计的教科书在其第一章中都覆盖了[协方差](https://en.wikipedia.org/wiki/Covariance)。 它被定义为两个随机变量之间的一种有用的“依赖关系度量”：
$$
\operatorname{cov}(X, Y)=E[(X-E[X])(Y-E[Y])]
$$
The textbook would usually provide some intuition on why it is defined as it is, prove a couple of properties, such as bilinearity, define the covariance matrix for multiple variables as $\boldsymbol{\Sigma}_{i, j}=\operatorname{cov}\left(X_{i}, X_{j}\right)$, and stop there. Later on the covariance matrix would pop up here and there in seeminly random ways. In one place you would have to take its inverse, in another compute the eigenvectors, or multiply a vector by it, or do something else for no apparent reason apart from "that's the solution we came up with by solving an optimization task".

In reality, though, there are some very good and quite intuitive reasons for why the covariance matrix appears in various techniques in one or another way. This post aims to show that, illustrating some curious corners of linear algebra in the process.

### Meet the Normal Distribution

The best way to truly understand the covariance matrix is to forget the textbook definitions completely and depart from a different point instead. Namely, from the the definition of the multivariate Gaussian distribution:

We say that the vector $\mathbf{X}$ has a normal (or Gaussian) distribution with mean $\mu$ and covariance $\boldsymbol{\Sigma}$ if:
$$
\operatorname{Pr}(\mathbf{x})=|2 \pi \mathbf{\Sigma}|^{-1 / 2} \exp \left(-\frac{1}{2}(\mathbf{x}-\mu)^{T} \mathbf{\Sigma}^{-1}(\mathbf{x}-\mu)\right)
$$
To simplify the math a bit, we will limit ourselves to the centered distribution (i.e. $\mu=\mathbf{0}$ ) and refrain from writing out the normalizing constant $|2 \pi \boldsymbol{\Sigma}|^{-1 / 2}$. Now, the definition of the (centered) multivariate Gaussian looks as follows:
$$
\operatorname{Pr}(\mathbf{x}) \propto \exp \left(-0.5 \mathbf{x}^{T} \mathbf{\Sigma}^{-1} \mathbf{x}\right)
$$
Much simpler, isn't it? Finally, let us define the covariance matrix as nothing else but the parameter of the Gaussian distribution. That's it. You will see where it will lead us in a moment.

### Transforming the Symmetric Gaussian

Consider a symmetric Gaussian distribution, i.e. the one with $\boldsymbol{\Sigma}=\mathbf{I}$ (the identity matrix). Let us take a sample from it, which will of course be a symmetric, round cloud of points:
考虑对称的高斯分布，即$\bf \Sigma = \bf I$（单位矩阵）的分布。 让我们从中抽取一个样本，它当然是对称的圆形点云：

![](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-f84e1485d8b3863069a06af8991f1421.png)

We know from above that the likelihood of each point in this sample is
$$
P(\mathbf{x}) \propto \exp \left(-0.5 \mathbf{x}^{T} \mathbf{x}\right)
$$
Now let us apply a linear transformation $\mathbf{A}$ to the points, i.e. let $\mathbf{y}=\mathbf{A} \mathbf{x}$. Suppose that, for the sake of this example, $\mathbf{A}$ scales the vertical axis by $0.5$ and then rotates everything by 30 degrees. We will get the following new cloud of points $\mathbf{y}$ :
现在让我们对这些点应用线性变换${\bf A}$，即让${\bf y} = {\bf Ax}$。 假设出于本示例的原因，${\bf A}$将垂直轴缩放0.5，然后将所有内容旋转30度。 我们将获得以下新的点${\bf y}$的云：

![](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-9a3d76f461ec7fe32efbaa1e6bf78c15.png)

What is the distribution of $\mathbf{y} ?$ Just substitute $\mathbf{x}=\mathbf{A}^{-1} \mathbf{y}$ into (1), to get:
$$
\begin{aligned}
P(\mathbf{y}) & \propto \exp \left(-0.5\left(\mathbf{A}^{-1} \mathbf{y}\right)^{T}\left(\mathbf{A}^{-1} \mathbf{y}\right)\right) \\
&=\exp \left(-0.5 \mathbf{y}^{T}\left(\mathbf{A} \mathbf{A}^{T}\right)^{-1} \mathbf{y}\right)
\end{aligned}
$$
This is exactly the Gaussian distribution with covariance $\mathbf{\Sigma}=\mathbf{A} \mathbf{A}^{T}$. The logic works both ways: if we have a Gaussian distribution with covariance $\bar{\Sigma}$, we can regard it as a distribution Which was obtained by transforming the symmetric Gaussian by some $\mathbf{A}$, and we are given $\mathbf{A} \mathbf{A}^{T}$

More generally, if we have any data, then, when we compute its covariance to be $\mathbf{\Sigma}$, we can say that if our data were Gaussian, then it could have been obtained from a symmetric cloud using some transformation $\mathbf{A}$, and we just estimated the matrix $\mathbf{A} \mathbf{A}^{T}$, corresponding to this transformation.
更一般而言，如果我们有任何数据，那么当我们计算其协方差为$\bf \Sigma$时，可以说，如果我们的数据是高斯函数，则可以使用一些变换$\bf A$从对称云中获得它， 我们只是估算了对应于此变换的矩阵$\bf AA^T$

Note that we do not know the actual $\mathbf{A}$, and it is mathematically totally fair. There can be many different transformations of the symmetric Gaussian which result in the same distribution shape. For example, if $\mathbf{A}$ is just a rotation by some angle, the transformation does not affect the shape of the distribution at all. Correspondingly, $\mathbf{A} \mathbf{A}^{T}=\mathbf{I}$ for all rotation matrices. When we see a unit covariance matrix we really do not know, whether it is the "originally symmetric" distribution, or a "rotated symmetric distribution". And we should not really care - those two are identical.
注意，我们不知道实际的$\bf A$，从数学上讲这是完全公平的。 对称高斯变换可以有许多不同的变换，从而导致相同的分布形状。 例如，如果$\bf A$只是某个角度的旋转，则该转换完全不会影响分布的形状。 相应地，对于所有旋转矩阵$\bf AA^T = \bf I$。 当我们看到单位协方差矩阵时，我们真的不知道它是“原始对称”分布还是“旋转对称分布”。 而且我们不应该真正在乎-这两个是相同的。

There is a theorem in linear algebra, which says that any symmetric matrix $\mathbf{\Sigma}$ can be represented as:
$$
\boldsymbol{\Sigma}=\mathbf{V D V}^{T}
$$
where $\mathbf{V}$ is orthogonal (i.e. a rotation) and $\mathbf{D}$ is diagonal (i.e. a coordinate-wise scaling). If we rewrite it slightly, we will get:
$$
\boldsymbol{\Sigma}=\left(\mathbf{V} \mathbf{D}^{1 / 2}\right)\left(\mathbf{V} \mathbf{D}^{1 / 2}\right)^{T}=\mathbf{A} \mathbf{A}^{T}
$$
where $\mathbf{A}=\mathbf{V} \mathbf{D}^{1 / 2}$. This, in simple words, means that any covariance matrix $\mathbf{\Sigma}$ could have been the result of transforming the data using a coordinate-wise scaling $\mathbf{D}^{1 / 2}$ followed by $a$ rotation $\mathbf{V}$. Just like in our example with $\mathbf{X}$ and $\mathbf{y}$ above.

### Principal Component Analysis

Given the above intuition, PCA already becomes a very obvious technique. Suppose we are given some data. Let us assume (or "pretend") it came from a normal distribution, and let us ask the following questions:

1. What could have been the rotation $\mathbf{V}$ and scaling $\mathbf{D}^{1 / 2}$, which produced our data from a symmetric cloud?

2. What were the original, "symmetric-cloud" coordinates $\mathbf{X}$ before this transformation was applied.

3. Which original coordinates were scaled the most by $\mathbf{D}$ and thus contribute most to the spread of the data now. Can we only leave those and throw the rest out?

All of those questions can be answered in a straightforward manner if we just decompose $\mathbf{\Sigma}$ into $\mathbf{V}$ and $\mathbf{D}$ according to (3). But (3) is exactly the eigenvalue decomposition of $\mathbf{\Sigma}$. I'll leave you to think for just a bit and you'll see how this observation lets you derive everything there is about PCA and more.

### The Metric Tensor

Bear me for just a bit more. One way to summarize the observations above is to say that we can (and should) regard $\Sigma^{-1}$ as a [metric tensor](https://en.wikipedia.org/wiki/Metric_tensor). A metric tensor is just a fancy formal name for a matrix, which summarizes the _deformation of space_. However, rather than claiming that it in some sense determines a particular transformation $A$ (which it does not, as we saw), we shall say that it affects the way we compute _angles and distances_ in our transformed space.

Namely, let us redefine, for any two vectors $\mathbf{V}$ and $\mathbf{w}$, their inner product as:
$$
\langle\mathbf{v}, \mathbf{w}\rangle_{\Sigma^{-1}}=\mathbf{v}^{T} \mathbf{\Sigma}^{-1} \mathbf{w}
$$
To stay consistent we will also need to redefine the norm of any vector as
$$
|\mathbf{v}|_{\Sigma^{-1}}=\sqrt{\mathbf{v}^{T} \mathbf{\Sigma}^{-1} \mathbf{v}}
$$
and the distance between any two vectors as
$$
|\mathbf{v}-\mathbf{w}|_{\Sigma^{-1}}=\sqrt{(\mathbf{v}-\mathbf{w})^{T} \mathbf{\Sigma}^{-1}(\mathbf{v}-\mathbf{w})}
$$
Those definitions now describe a kind of a "skewed world" of points. For example, a unit circle (a set of points with "skewed distance" 1 to the center) in this world might look as follows:

![](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-cf417484691bffa9846debf02f38469a.png)

Although it may look weird at first, note that the new inner product we defined is actually just the dot product of the "untransformed" originals of the vectors:
$$
\mathbf{v}^{T} \mathbf{\Sigma}^{-1} \mathbf{w}=\mathbf{v}^{T}\left(\mathbf{A} \mathbf{A}^{T}\right)^{-1} \mathbf{w}=\left(\mathbf{A}^{-1} \mathbf{v}\right)^{T}\left(\mathbf{A}^{-1} \mathbf{w}\right)
$$
The following illustration might shed light on what is actually happening in this $\Sigma$ -"skewed" world. 

![](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-51b454623cc9a71555c081cacecc9f9d.png)

Somehow "deep down inside", the ellipse thinks of itself as a circle and the two vectors behave as if they were $(2,2)$ and $(-2,2)$.

![](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-7ab9b429b375292f8b376bf5ba7e5f09.png)

Getting back to our example with the transformed points, we could now say that the point-cloud $\mathbf{y}$ is actually a perfectly round and symmetric cloud "deep down inside", it just happens to live in a skewed space. The deformation of this space is described by the tensor $\mathbf{\Sigma}^{-1}$ (which is, as we know, equal to $\left(\mathbf{A} \mathbf{A}^{T}\right)^{-1}$. The PCA now becomes a method for analyzing the deformation of space, how cool is that.

### The Dual Space

We are not done yet. There's one interesting property of "skewed" spaces worth knowing about. Namely, the elements of their dual space have a particular form. No worries, I'll explain in a second.

Let us forget the whole skewed space story for a moment, and get back to the usual inner product $\mathbf{w}^{T} \mathbf{v}$. Think of this inner product as a function $f_{\mathbf{w}}(\mathbf{v})$, which takes a vector $\mathbf{v}$ and maps it to a real number, the dot product of $\mathbf{V}$ and $\mathbf{W}$. Regard the $\mathbf{W}$ here as the parameter ("weight vector") of the function. If you have done any machine learning at all, you have certainly come across such linear functionals over and over, sometimes in disguise. Now, the set of all possible linear functionals $f_{\mathbf{w}}$ is known as the dual space to your "data space".

Note that each linear functional is determined uniquely by the parameter vector $\mathbf{W}$, which has the same dimensionality as $\mathbf{V}$, so apparently the dual space is in some sense equivalent to your data space - just the interpretation is different. An element $\mathbf{V}$ of your "data space" denotes, well, a data point. An element $\mathbf{W}$ of the dual space denotes a function $f_{\mathbf{w}}$, which projects your data points on the direction $\mathbf{W}$ (recall that if $\mathbf{w}$ is unit-length, $\mathbf{w}^{T} \mathbf{v}$ is exactly the length of the perpendicular projection of $\mathbf{V}$ upon the direction $\mathbf{W}) .$ So, in some sense, if $\mathbf{V}-\mathrm{s}$ are "vectors", $\mathbf{w}$
-s are "directions, perpendicular to these vectors". Another way to understand the difference is to note that if, say, the elements of your data points numerically correspond to amounts in kilograms, the elements of $\mathbf{w}$ would have to correspond to "units per kilogram". Still with me?

Let us now get back to the skewed space. If $\mathbf{v}$ are elements of a skewed Euclidean space with the metric tensor $\boldsymbol{\Sigma}^{-1}$, is a function $f_{\mathbf{w}}(\mathbf{v})=\mathbf{w}^{T} \mathbf{v}$ an element of a dual space? Yes, it is, because, after all, it is a linear functional. However, the parameterization of this function is inconvenient, because, due to the skewed tensor, we cannot interpret it as projecting vectors upon $\mathbf{W}$ nor can we say that $\mathbf{w}$ is an "orthogonal direction" (to a separating hyperplane of a classifier, for example). Because, remember, in the skewed space it is not true that orthogonal vectors satisfy $\mathbf{w}^{T} \mathbf{v}=0$. Instead, they satisfy $\mathbf{w}^{T} \mathbf{\Sigma}^{-1} \mathbf{v}=0$. Things would therefore look much better if we parameterized our dual space differently. Namely, by considering linear functionals of the form $f_{\mathbf{z}}^{\Sigma^{-1}}(\mathbf{v})=\mathbf{z}^{T} \mathbf{\Sigma}^{-1} \mathbf{v}$. The new parameters $\mathbf{z}$ could now indeed be interpreted as an "orthogonal direction" and things overall would make more sense.

However when we work with actual machine learning models, we still prefer to have our functions in the simple form of a dot product, i.e. $f_{\mathrm{w}}$, without any ugly $\Sigma$ -s inside. What happens if we turn a "skewed space" linear functional from its natural representation into a simple inner product?
$$
f_{\mathbf{z}}^{\Sigma^{-1}}(\mathbf{v})=\mathbf{z}^{T} \mathbf{\Sigma}^{-1} \mathbf{v}=\left(\boldsymbol{\Sigma}^{-1} \mathbf{z}\right)^{T} \mathbf{v}=f_{\mathbf{w}}(\mathbf{v})
$$
where $\mathbf{w}=\mathbf{\Sigma}^{-1} \mathbf{Z}$. (Note that we can lose the transpose because $\boldsymbol{\Sigma}$ is symmetric).
What it means, in simple terms, is that when you fit linear models in a skewed space, your resulting weight vectors will always be of the form $\mathbf{\Sigma}^{-1} \mathbf{z}$. Or, in other words, $\mathbf{\Sigma}^{-1}$ is a transformation, which maps from "skewed perpendiculars" to "true perpendiculars". Let me show you what this means visually.

Consider again the two "orthogonal" vectors from the skewed world example above:

![](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-205e3852e95a4101a0e0f1ffd6e74e52.png)

Let us interpret the blue vector as an element of the dual space. That is, it is the $\mathbf{Z}$ vector in a linear functional $\mathbf{z}^{T} \mathbf{\Sigma}^{-1} \mathbf{v}$. The red vector is an element of the "data space", which would be mapped to 0 by this functional (because the two vectors are "orthogonal", remember).

For example, if the blue vector was meant to be a linear classifier, it would have its separating line along the red vector, just like that:

![img](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-be356379841323f5d4b954bdc75d3bf2.png)

If we now wanted to use this classifier, we could, of course, work in the "skewed space" and use the expression $\mathbf{z}^{T} \mathbf{\Sigma}^{-1} \mathbf{v}$ to evaluate the functional. However, why don't we find the actual normal $\mathbf{W}$ to that red separating line so that we wouldn't need to do an extra matrix multiplication every time we use the function?

It is not too hard to see that $\mathbf{w}=\mathbf{\Sigma}^{-1} \mathbf{Z}$ will give us that normal. Here it is, the black arrow:

![img](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-e7f3dd471ccf0462b164c9c4cb0058cd.png)

Therefore, next time, whenever you see expressions like $\mathbf{w}^{T} \mathbf{\Sigma}^{-1} \mathbf{v}$ on $(\mathbf{v}-\mathbf{w})^{T} \mathbf{\Sigma}^{-1}(\mathbf{v}-\mathbf{w})$, remember that those are simply inner products and (squared) distances in a skewed space, while $\mathbf{\Sigma}^{-1} \mathbf{Z}$ is a conversion from a skewed normal to a true normal. Also remember that the "skew" was estimated by pretending that the data were normally-distributed.

Once you see it, the role of the covariance matrix in some methods like the Fisher's discriminant or Canonical correlation analysis might become much more obvious.

### The Dual Space Metric Tensor

"But wait", you should say here. "You have been talking about expressions like $\mathbf{w}^{T} \mathbf{\Sigma}^{-1} \mathbf{v}$ all the time, while things like $\mathbf{w}^{T} \mathbf{\Sigma} \mathbf{v}$ are also quite common in practice. What about those?"
Hopefully you know enough now to suspect that $\mathbf{w}^{T} \mathbf{\Sigma} \mathbf{v}$ is again an inner product or a squared norm in some deformed space, just not the "internal data metric space", that we considered so far. Which space is it? It turns out it is the "internal dual metric space". That is, whilst the expression $\mathbf{w}^{T} \mathbf{\Sigma}^{-1} \mathbf{v}$ denoted the "new inner product" between the points, the expression $\mathbf{w}^{T} \mathbf{\Sigma} \mathbf{v}$ denotes the "new inner product" between the parameter vectors. Let us see why it is so.

Consider an example again. Suppose that our space transformation A scaled all points by 2 along the $x$ axis. The point $(1,0)$ became $(2,0)$, the point $(3,1)$ became $(6,1)$, etc. Think of it as changing the units of measurement - before we measured the $x$ axis in kilograms, and now we measure it in pounds. Consequently, the norm of the point $(2,0)$ according to the new metric, $\left.(2,0)\right|_{\Sigma}-1$ will be 1, because 2 pounds is still just 1 kilogram "deep down inside".

What should happen to the parameter ("direction") vectors due to this transformation? Can we say that the parameter vector $(1,0)$ also got scaled to $(2,0)$ and that the norm of the parameter vector $(2,0)$ is now therefore also $1 ?$ No! Recall that if our initial data denoted kilograms, our dual vectors must have denoted "units per kilogram". After the transformation they will be denoting "units per pound", correspondingly. To stay consistent we must therefore convert the parameter vector ("1 unit per kilogram", 0) to its equivalent ("0.5 units per pound",0). Consequently, the norm of the parameter vector $(0.5,0)$ in the new metric will be 1 and, by the same logic, the norm of the dual vector $(2,0)$ in the new metric must be 4 . You see, the "importance of a parameter/direction" gets scaled inversely to the "importance of data" along that parameter or direction.

More formally, if $\mathbf{x}^{\prime}=\mathbf{A} \mathbf{x}$, then
$$
\begin{aligned}
f_{\mathbf{w}}(\mathbf{x}) &=\mathbf{w}^{T} \mathbf{x}=\mathbf{w}^{T} \mathbf{A}^{-1} \mathbf{x}^{\prime} \\
&=\left(\left(\mathbf{A}^{-1}\right)^{T} \mathbf{w}\right)^{T} \mathbf{x}^{\prime}=f_{\left(\mathbf{A}^{-1}\right)^{T} \mathbf{w}}\left(\mathbf{x}^{\prime}\right)
\end{aligned}
$$
This means, that the transformation $\mathbf{A}$ of the data points implies the transformation $\mathbf{B}:=\left(\mathbf{A}^{-1}\right)^{T}$ of the dual vectors. The metric tensor for the dual space must thus be:
$$
\left(\mathbf{B B}^{T}\right)^{-1}=\left(\left(\mathbf{A}^{-1}\right)^{T} \mathbf{A}^{-1}\right)^{-1}=\mathbf{A} \mathbf{A}^{T}=\mathbf{\Sigma}
$$
Remember the illustration of the "unit circle" in the $\mathbf{\Sigma}^{-1}$ metric? This is how the unit circle looks in the corresponding $\mathbf{\Sigma}$ metric. It is rotated by the same angle, but it is stretched in the direction where it was squished before.

![img](http://fouryears.eu/wp-content/uploads/2016/11/main-qimg-a0401addb808e2ea6190dd391c3a861e.png)

Intuitively, the norm ("importance") of the dual vectors along the directions in which the data was stretched by $\mathbf{A}$ becomes proportionally larger (note that the "unit circle" is, on the contrary, "squished" along those directions).

But the "stretch" of the space deformation in any direction can be measured by the variance of the data. It is therefore not a coincidence that $\mathbf{w}^{T} \boldsymbol{\Sigma} \mathbf{w}$ is exactly the variance of the data along the direction $\mathbf{W}$ (assuming $|\mathbf{w}|=1$ ).

### The Covariance Estimate

Once we start viewing the covariance matrix as a transformation-driven metric tensor, many things become clearer, but one thing becomes extremely puzzling: why is the inverse covariance of the data a good estimate for that metric tensor? After all, it is not obvious that $\mathbf{X}^{T} \mathbf{X} / n$ (where $\mathbf{X}$ is the data matrix) must be related to the $\boldsymbol{\Sigma}$ in the distribution equation $\exp \left(-0.5 \mathbf{x}^{T} \mathbf{\Sigma}^{-1} \mathbf{x}\right)$
Here is one possible way to see the connection. Firstly, let us take it for granted that if $\mathbf{X}$ is sampled from a symmetric Gaussian, then $\mathbf{X}^{T} \mathbf{X} / n$ is, on average, a unit matrix. This has nothing to do with transformations, but just a consequence of pairwise independence of variables in the symmetric Gaussian.

Now, consider the transformed data, $\mathbf{Y}=\mathbf{X} \mathbf{A}^{T}$ (vectors in the data matrix are row-wise, hence the multiplication on the right with a transpose). What is the covariance estimate of $\mathbf{Y}$ ?
$$
\mathbf{Y}^{T} \mathbf{Y} / n=\left(\mathbf{X} \mathbf{A}^{T}\right)^{T} \mathbf{X} \mathbf{A}^{T} / n=\mathbf{A}\left(\mathbf{X}^{T} \mathbf{X}\right) \mathbf{A}^{T} / n \approx \mathbf{A} \mathbf{A}^{T}
$$
the familiar tensor.

This is a place where one could see that a covariance matrix may make sense outside the context of a Gaussian distribution, after all. Indeed, if you assume that your data was generated from any distribution $P$ with uncorrelated variables of unit variance and then transformed using some matrix $\mathbf{A}$, the expression $\mathbf{X}^{T} \mathbf{X} / n$ will still be an estimate of $\mathbf{A} \mathbf{A}^{T}$, the metric tensor for the corresponding (dual) space deformation.

However, note that out of all possible initial distributions $P$, the normal distribution is exactly the one with the maximum entropy, i.e. the "most generic". Thus, if you base your analysis on the mean and the covariance matrix (which is what you do with PCA, for example), you could just as well assume your data to be normally distributed. In fact, a good rule of thumb is to remember, that whenever you even mention the word "covariance matrix", you are implicitly fitting a Gaussian distribution to your data.