---
title: Laplace Distribution
categories:
- Math
- Statistic
tags:
- Laplace Distribution
date: 2021/4/15 10:00:00
updated: 2021/4/15 16:00:00
---



> [rss.onlinelibrary.wiley.com](https://rss.onlinelibrary.wiley.com/doi/full/10.1111/j.1740-9713.2018.01185.x)

Abstract
--------

When studying the same object, different scientists and scientific instruments can produce widely divergent measurements. **Marco Geraci** and **Mario Cortina Borja** describe a probability distribution to model observations when heterogeneity and large errors are present

What is the Laplace distribution?
---------------------------------

The Laplace (or double exponential) distribution, like the normal, has a distinguished history in statistics. It has applications in image and speech recognition, ocean engineering, hydrology, and finance. Its main characteristic is the way it models the probability of deviations from a central value, also known as errors. For example, consider several astronomers in different locations on Earth trying to measure the distance to a celestial object. Despite having the same goal, measurements will be different because of errors, which, without loss of generality, we will assume follow a normal distribution. Now, suppose we repeat the experiment multiple times. Each astronomer may produce observations with different spread. This is called heterogeneity, and its source can be attributed to the set of characteristics of both the observers and their instruments. In this situation, the Laplace distribution can provide a better model to describe observations of this kind than the normal distribution with common variance. This is because each observer‐instrument would have its own variability.

拉普拉斯(或双指数)分布，像正态分布一样，在统计学上有着杰出的历史。它在图像和语音识别、海洋工程、水文学和金融等领域都有应用。它的主要特点是对偏离中心值(也称为误差)的概率进行建模。例如，考虑几个天文学家在地球上不同的位置试图测量一个天体的距离。尽管有相同的目标，测量结果将因为误差而不同，在不丧失一般性的情况下，我们将假定遵循正态分布。现在，假设我们重复这个实验多次。每个天文学家的观测范围可能不同。这就是所谓的异质性，它的来源可以归因于观察者和他们的仪器的一系列特征。在这种情况下，拉普拉斯分布比具有共同方差的正态分布提供了更好的模型来描述这种观测。这是因为每个观测仪器都有自己的可变性。

The Laplace distribution can be derived via a so‐called scale mixture of normals (see box). This means that each member of the population is assumed to produce an observation in a two‐stage process. First, a random process determines to what degree each individual is potentially different from the others (individual variability that we may not be able to observe). Second, another random process produces an actual observation given the individual's variability. In our example, a particular astronomer may be less trained than others, or a particular instrument may be less precise than others. This would result in higher variability among the observations, and, potentially, in large errors: often larger than those from a normal distribution, which assumes the same variance for the entire population.

拉普拉斯分布可以通过所谓的混合法线来推导(见框)。这意味着种群中的每个成员都被假定在一个两阶段的过程中产生一个观察结果。首先，随机过程决定了每个个体与其他个体的潜在不同程度(我们可能无法观察到的个体可变性)。第二，另一个随机过程产生了一个实际观察，考虑到个体的可变性。在我们的例子中，一个特定的天文学家可能比其他人训练得少，或者一个特定的仪器可能比其他的仪器不精确。这将导致观测结果的变异性更大，并可能产生较大的误差:通常比正态分布的误差更大，正态分布假设整个总体的方差相同。

What does it look like?
-----------------------

The plot in Figure [1](#sign1185-fig-0001)a depicts the Laplace probability density function centred at zero and, for comparison, the normal one. The former has a more pronounced peak around the central value, and much fatter tails, as detailed in Figures [1](#sign1185-fig-0001)b and [1](#sign1185-fig-0001)c. To explain what these fatter tails mean, consider that the cumulative probability of the standard normal at 3 is approximately 99.9%, which justifies the common assertion that events 3 standard deviations or more above (or below) the mean are unlikely. However, the corresponding cumulative probability of the Laplace with the same median and variance is about 99.3%. Therefore the occurrence of an “extreme” event in a Laplace population is more than five times as likely as in a normal population.

图[1](#sign1185-fig-0001)a中的图描述了以0为中心的拉普拉斯概率密度函数，为了比较，是正常的。前者在中心值附近有一个更明显的峰值，尾部更粗，具体见图[1](#sign1185- figo -0001)b和[1](#sign1185- figo -0001)c。为了解释这些胖尾的含义，考虑标准正态分布在3处的累积概率约为99.9%，这证明了高于(或低于)均值3个标准差以上的事件是不可能发生的。而具有相同中位数和方差的拉普拉斯对应的累积概率约为99.3%。因此，在拉普拉斯总体中发生“极端”事件的可能性是正常总体的五倍以上。

[![](https://rss.onlinelibrary.wiley.com/cms/asset/88edfd67-37e8-4f71-8a27-f94c13c27c99/sign1185-fig-0001-m.png)](https://rss.onlinelibrary.wiley.com/cms/asset/7fe82457-b904-43b1-bf46-49107bc8a3ad/sign1185-fig-0001-m.jpg)

(a) Laplace and normal densities; (b) and (c) show the considerably thicker lower and upper tails, respectively.

Like the normal, the Laplace distribution has a location (μ) and a scale (σ) parameter (see box), but in contrast to the normal, the maximum likelihood estimates (MLEs) of these parameters are different. Under the normal distribution, the MLE of the location parameter is the sample mean, while the MLE of the scale parameter is based on the squared residuals. Under the Laplace distribution, however, the MLE of the location parameter is the sample median, while an estimate of the scale parameter is obtained through the absolute value of the residuals.[1](#sign1185-bib-0001) The explanation of different MLEs follows from the form of the probability density, which is an exponential function of !_y_ – μ! for the Laplace (see box) but (_y_ – μ)[2](#sign1185-bib-0002) for the normal. As a result, the estimation of the location and scale parameters of the Laplace is robust in the presence of large errors. Still, the location parameter retains the same interpretation in both distributions since both normal and Laplace are symmetric and centred about μ.

像正态一样，拉普拉斯分布有一个位置(μ)和一个尺度(σ)参数(见框)，但与正态相反，这些参数的最大似然估计(MLEs)是不同的。在正态分布下，位置参数的MLE是样本均值，而尺度参数的MLE是基于残差的平方。根据拉普拉斯分布,然而,初速位置参数的样本值,而尺度参数的估计是通过残差绝对值。[1](# sign1185 -围嘴- 0001)的解释不同毫升遵循从概率密度的形式,这是一个指数函数的! _y_ -μ!为拉普拉斯(见框)，但(_y_ - μ)[2](#sign1185-bib-0002)为正常。结果表明，在存在较大误差的情况下，对拉普拉斯位置参数和尺度参数的估计是鲁棒的。尽管如此，位置参数在两种分布中保持相同的解释，因为法线和拉普拉斯都是对称的，并且以μ为中心。

Location, scale, and “a mixture of normals”
-------------------------------------------

We say that the random variable _Y_ follows a Laplace distribution if its probability density can be written as

![](https://rss.onlinelibrary.wiley.com/cms/asset/30fab65d-a446-4fa0-add2-6dc1fea4184c/sign1185-math-0001.png)

The location parameter μ corresponds to the mean, median and mode of the distribution, and the scale parameter σ to its standard deviation. There are several characterisations of the Laplace distribution by means of other probability models.[1](#sign1185-bib-0001) An important representation is the so‐called scale mixture of normal distributions, namely

![](https://rss.onlinelibrary.wiley.com/cms/asset/5f760730-fb34-4677-8301-eb61eef14748/sign1185-math-0002.png)

where _W_ is an exponential distribution with mean equal to 1 and _Z_ is a standard normal distribution (i.e., with mean equal to 0 and standard deviation equal to 1); also, _W_ and _Z_ are independent. One way of interpreting the equation above is as follows. Consider three instruments, each with a different degree of precision. This would correspond to a different value of _W_, for example, _W_1 = 1, _W_2 = 0.5, and _W_3 = 2. Then the distribution of the measurements from the first instrument would be normal with mean μ and variance equal to σ[2](#sign1185-bib-0002). Measurements from the second and third instruments, too, would be normal with the same mean. However, their variances would be, respectively, half and twice the variance of the first. The three instruments would therefore generate heterogeneous observations which, collectively, follow the Laplace distribution.

When should it be used?
-----------------------

The Laplace model is useful in all situations when heterogeneity in the population is suspected and the observations show large errors. Some examples in air and sea navigation are discussed by Hsu,[5](#sign1185-bib-0005) where it is crucial for modelling the distribution of position errors of aircraft or vessels obtained by pooling data from a complex navigation system. Also, an analogous situation in biomedical research arises when patients’ measures from a biological marker differ systematically according to unobserved (e.g., genetic) characteristics.

拉普拉斯模型适用于所有可能存在总体异质性且观测结果误差较大的情况。Hsu，[5](#sign1185-bib-0005)讨论了空中和海上导航中的一些例子，在这些例子中，对通过汇集复杂导航系统的数据而获得的飞机或船只的位置误差分布建模是至关重要的。同样，在生物医学研究中也会出现类似的情况，即根据未观察到的(如遗传)特征，患者从生物标记中获得的测量结果有系统地不同。

Keep in mind…
-------------

There are different models for symmetric, unimodal distributions and it is not always straightforward to know which model should be preferred. Specific goodness‐of‐fit tests have been developed to assess how well the Laplace model conforms to the data.

Since the estimator of the location parameter of the Laplace distribution is the sample median, this distribution can be used to fit median regression models, a robust alternative to classical mean regression which is based on minimising sums of squares. Indeed, the asymmetric Laplace distribution,[1](#sign1185-bib-0001) which is a generalisation of the double exponential, is often employed in the estimation of quantile regression, which generalises median regression to other quantiles.
