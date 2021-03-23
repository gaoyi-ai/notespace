---
title: freedom
categories:
- Math
- Statistic
tags:
- residuals
- freedom
date: 2021/3/21 10:00:00
updated: 2021/3/21 16:00:00
---

# Degrees of freedom (statistics)

In [statistics](http://en.wikipedia.org/wiki/Statistics), the number of **degrees of freedom** is the **number of values in the final calculation of a [statistic](http://en.wikipedia.org/wiki/Statistic) that are free to vary.**

[统计参数](https://en.wikipedia.org/wiki/Statistical_parameter)的估计可以基于不同数量的信息或数据。一个参数的估计所依据的独立信息的数量称为自由度。In general, the degrees of freedom of an estimate of a parameter are equal to the number of independent [scores](https://en.wikipedia.org/wiki/Realization_(probability)) that go into the estimate minus the number of parameters used as intermediate steps in the estimation of the parameter itself.

关于样本方差，里面提到：
i.e., the sample variance has N-1 degrees of freedom, **since it is computed from N random scores minus the only 1 parameter estimated as intermediate step, which is the sample mean**.

简单说， n 个样本，如果在某种条件下，**样本均值是先定的 (fixed)**，那么只剩 n-1 个样本的值是可以变化的。

下面这个例子也许可以说明：

假设你现在手头有 3 个样本，$X_1,X_2,X_3$。因为样本具有随机性，所以它们取值不定。但是假设出于某种原因，我们需要让样本均值固定，比如说，  $\bar{X}=5$， 那么这时真正取值自由，” 有随机性 “ 的样本只有 2 个。 试想，如果 $\bar{X}=5$, 那么每选取一组 $X_1,X_2$ 的取值， $X_3$ 将不得不等于$15-X_1-X_2.$ 对于第三个样本来说，这种 “不得不” 就可以理解为被剥夺了一个自由度。所以就这个例子而言，3 个样本最终 "自由" 的只有其中的 2 个。不失一般性， n 个样本， 留出一个自由度给固定的均值，剩下的自由度即为 n-1 。

突兀的举上面这个例子干什么？事实上，计算样本方差时，样本均值就需要给定。计算样本均值也就是维基百科里提到的'intermediate step'。如果你去观察计算样本方差的一系列表达式，比如往往最常会被介绍的方差的无偏估计 （样本方差） $\frac{1}{n-1}\sum^n_{i=1}\left(X_i-\bar{X}\right)^2$ ，你会发现样本均值这一项都包含在内。考虑到方差是衡量数据偏差程度的统计量，计算一下样本均值作为中间步骤的中间量，也不失其合理性。于是，为计算样本方差，样本里原有的 n 个自由度，有一个自由度被分配给计算样本均值，剩下自由度即为 n-1。

## Of residuals

A common way to think of degrees of freedom is as the number of independent pieces of information available to estimate another piece of information. More concretely, the number of degrees of freedom is the number of independent observations in a sample of data that are available to estimate a parameter of the population from which that sample is drawn. For example, if we have two observations, when calculating the mean we have two independent observations; however, when calculating the variance, we have only one independent observation, since the two observations are equally distant from the sample mean.

In fitting statistical models to data, the vectors of residuals are constrained to lie in a space of smaller dimension than the number of components in the vector. That smaller dimension is the number of *degrees of freedom for error*, also called *residual degrees of freedom*.

- Example

Perhaps the simplest example is this. Suppose$X_{1},\dots ,X_{n}$are [random variables](https://en.wikipedia.org/wiki/Random_variable) each with [expected value](https://en.wikipedia.org/wiki/Expected_value) *μ*, and let ${\overline {X}}_{n}={X_{1}+\cdots +X_{n} \over n}$ be the "sample mean." Then the quantities $X_{i}-{\overline {X}}_{n}\,$ are residuals that may be considered [estimates](https://en.wikipedia.org/wiki/Estimation_theory) of the [errors](https://en.wikipedia.org/wiki/Errors_and_residuals_in_statistics) $X_i − μ$. The sum of the residuals (unlike the sum of the errors) is necessarily 0. If one knows the values of any *n* − 1 of the residuals, one can thus find the last one. That means they are constrained to lie in a space of dimension *n* − 1. One says that there are *n* − 1 degrees of freedom for errors.

An example which is only slightly less simple is that of [least squares](https://en.wikipedia.org/wiki/Least_squares) estimation of *a* and *b* in the model $Y_{i}=a+bx_{i}+e_{i}{\text{ for }}i=1,\dots ,n$ 

where $x_i$ is given, but $e_i$ and hence $Y_i$ are random. Let $\widehat {a}$ and $\widehat {b}$ be the least-squares estimates of *a* and *b*. Then the residuals ${\displaystyle {\widehat {e}}_{i}=y_{i}-({\widehat {a}}+{\widehat {b}}x_{i})\,}$ are constrained to lie within the space defined by the two equations
$$
{\displaystyle {\widehat {e}}_{1}+\cdots +{\widehat {e}}_{n}=0,\,} \\
{\displaystyle x_{1}{\widehat {e}}_{1}+\cdots +x_{n}{\widehat {e}}_{n}=0.\,}
$$
One says that there are *n* − 2 degrees of freedom for error.

Notationally, the capital letter *Y* is used in specifying the model, while lower-case *y* in the definition of the residuals; that is because the former are hypothesized random variables and the latter are actual data.

We can generalise this to multiple regression involving *p* parameters and covariates (e.g. *p* − 1 predictors and one mean (=intercept in the regression)), in which case the cost in *degrees of freedom of the fit* is *p*, leaving *n - p* degrees of freedom for errors

---

假设现在有一个总体｛1,2,3,4,5,6,7,8,9｝，其均值为 5，我们从这个总体中抽取了一个样本｛3，6，4，7，9｝根据这个样本的均值来估计总体的均值。但样本的均值为 5.8，明显高于实际的总体的均值。要想我们抽出的样本达到理想的效果，我们应当是抽取了 9，就应当抽取 1，抽取了 2，就抽取了 8。但在我们前面抽取的样本中抽了一个 9，却没有 1，我们可以重新抽取剩下的个体，让它们中的一个个体值为 1，这样我们就有 4 次机会修正样本与总体不符的问题，这个时候，我们的自由度就是 4。

## Why Do Critical Values Decrease While DF Increase?

Let’s take a look at the t-score formula in a hypothesis test:
$$
t = \frac{\bar{x}-\mu_0}{s / \sqrt{n}}
$$
当n增加时，t分数上升。 这是由于分母的平方根：随着分母的平方根变大，分数s /√n变小，t分数（另一个分数的结果）变大。 由于自由度在上面被定义为n-1，因此您会认为[t-critical值](https://www.statisticshowto.com/probability-and-statistics/find-critical-values/t-critical-value/)也应该变大，但它们不会变大：它们变小。 这似乎违反直觉。

但是，请**考虑一下t检验的实际用途**。 之所以使用t检验，是因为您不知道总体的标准偏差，因此也不知道图形的形状。 它可能有短的[胖尾巴](https://www.statisticshowto.com/fat-tail-distribution/)。 它可能有长长的瘦尾巴。 你只是不知道。 自由度会影响t分布中图的形状； 随着df变大，分布尾部的面积变小。 当df接近无穷大时，t分布将看起来像正态分布。 发生这种情况时，您可以确定自己的标准偏差（在正态分布上为1）。

假设您是从四个人中重复取样的权重，这些人是从标准偏差未知的总体中得出的。 您测量它们的权重，计算样本对之间的平均差，然后一遍又一遍地重复该过程。 极小的样本数量4将导致t分布带有肥尾。 粗尾巴告诉您，您的样本中更有可能具有极高的价值。 您以5％的alpha水平测试假设，从而**切断了分布的最后5％** 。下图显示了5％截止的t分布。 得出的临界值为2.6。（**注意**：这里我以假设的t分布为例-CV不准确）。

![sample size and t dist shape](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/sample-size-and-t-dist-shape.png)

现在来看正态分布。 对于正态分布，获得极值的机会较小。 5％Alpha水平在CV为2时会降低。

Back to the original question “Why Do Critical Values Decrease While DF Increases?” Here’s the short answer:

自由度与样本大小（n-1）有关。 如果df增加，则也表明样本量正在增加；如果df增加，则表明样本量正在增加。 t分布图的尾巴更细，将临界值推向均值。

---

## Degrees of Freedom in Regression Analysis

[regression](https://statisticsbyjim.com/glossary/regression-analysis/)中的自由度稍微复杂一些，我会保持简单。 在回归模型中，每个项都是使用一个自由度的估计参数。 在下面的回归输出中，您可以看到每个术语如何需要DF。 有28个观察值，两个[independent variables](https://statisticsbyjim.com/glossary/predictor-variables/)总共使用两个自由度。 输出显示“错误”中剩余的26个自由度。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/df_regr.png)

错误的自由度是可用于估计[系数](https://statisticsbyjim.com/glossary/regression-coefficient/)的独立信息。 对于回归中的精确[系数估计和强大的假设检验](https://statisticsbyjim.com/regression/interpret-coefficients-p-values-regression/), you must have many error degrees of freedom, which equates to having many observations for each model term.

当您向模型中添加项时，误差的自由度会降低。 您可用于估计系数的信息较少。 这种情况降低了估计的精度和测试的能力。 当剩余自由度太少时，您将无法相信回归结果。 如果您使用所有自由度，则该过程将无法计算p值。