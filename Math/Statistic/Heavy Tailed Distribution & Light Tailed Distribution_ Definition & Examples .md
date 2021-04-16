---
title: Heavy & Light Tailed Distribution
categories:
- Math
- Statistic
tags:
- Heavy Distribution
- Light Distribution
date: 2021/4/15 10:00:00
updated: 2021/4/15 16:00:00
---



> [www.statisticshowto.com](https://www.statisticshowto.com/heavy-tailed-distribution/)

What is a Heavy Tailed Distribution?
------------------------------------

[![](https://www.statisticshowto.com/wp-content/uploads/2016/05/heavy-tailed-300x178.png)](https://www.statisticshowto.com/wp-content/uploads/2016/05/heavy-tailed.png)

A heavy tailed distribution **has a tail that’s heavier than an [exponential distribution](https://www.statisticshowto.com/exponential-distribution/)** (Bryson, 1974). In other words, a distribution that is heavy tailed goes to zero slower than one with exponential tails; there will be more bulk [under the curve](https://www.calculushowto.com/problem-solving/area-under-the-curve-excel/) of the [PDF](https://www.statisticshowto.com/probability-density-function/). Heavy tailed distributions tend to have many [outliers](https://www.statisticshowto.com/statistics-basics/find-outliers/) with very high values. The heavier the tail, the larger the [probability](https://www.statisticshowto.com/probability-and-statistics/probability-main-index/) that you’ll get one or more disproportionate values in a [sample](https://www.statisticshowto.com/sample/).

重尾分布的尾部比指数分布更重。换句话说，有重尾的分布比有指数尾的分布更慢地趋近于零;在PDF的曲线下会有更多的体积。重尾分布往往有很多值很高的异常值。尾巴越重，在样本中得到一个或多个不成比例值的概率就越大。

Characteristics of the Heavy Tailed Distribution
------------------------------------------------

If you take a [random sample](https://www.statisticshowto.com/probability-and-statistics/statistics-definitions/simple-random-sample/) from the distribution, you’re likely to end up with a sample made up from mostly small values. But there will probably be one or more very large values that will throw your [statistics](https://www.statisticshowto.com/statistic/) off. For example, if you sample from the income of people in the United States, the bulk of your data will be relatively small—around \$50,000. However, one or two values in your sample could be ridiculously large (i.e. outliers). You could draw 99 items that are around ​\$50,000, then a “Bill Gates”—he earns several billion dollars per year.

如果你从分布中随机抽取一个样本，你很可能得到一个由小值组成的样本。但可能会有一个或多个非常大的值，会使你的统计数据混乱。例如，如果你从美国人的收入中取样，你的大部分数据相对较少——大约5万美元。然而，在你的样本中有一两个值可能非常大(即异常值)。你可以画出99件价值5万美元左右的物品，然后是一个“比尔·盖茨”，他每年能赚几十亿美元。

These large values tend to skew your sample statistics: the [mean](https://www.statisticshowto.com/mean/) would be very misleading (for the above example, it would be in the millions of dollars), the [sample variance](https://www.statisticshowto.com/probability-and-statistics/descriptive-statistics/sample-variance/) will probably be very large and the [sample mean](https://www.statisticshowto.com/probability-and-statistics/statistics-definitions/sample-mean/) usually underestimates the [population mean](https://www.statisticshowto.com/population-mean/). Another couple of quirks with heavy tails:

1.  The [Central Limit Theorem](https://www.statisticshowto.com/probability-and-statistics/normal-distributions/central-limit-theorem-definition-examples/) doesn’t work.
2.  Some [moments](https://www.calculushowto.com/moment-definition-examples/) don’t exist, so [order statistics](https://www.statisticshowto.com/order-statistics/) are used instead.

这些大的值往往会扭曲你的样本统计数据:平均值会非常具有误导性(在上面的例子中，它可能以百万美元为单位)，样本方差可能非常大，样本平均值通常会低估总体平均值。大尾巴的另几个怪癖:

1. 中心极限定理不成立。
2. 有些时刻并不存在，所以使用的是顺序统计数据。

Heavy Tailed Distributions in the Real World
--------------------------------------------

Many real world situations are heavy tailed, including:

*   The top 0.1% of the population in the USA owns as much as the bottom 90% ([Guardian](https://www.theguardian.com/business/2014/nov/13/us-wealth-inequality-top-01-worth-as-much-as-the-bottom-90)).
*   File sizes in computers tend to be small, with a few very large files thrown into the mix ([Columbia](http://dna-pubs.cs.columbia.edu/citation/paperfile/29/Liu_tail_01.pdf)).
*   Web page sizes and computer systems’ workloads tend to be heavy-tailed ([Stanford](http://web.stanford.edu/~balaji/papers/05systemswith.pdf)).
*   Insurance Payouts and Financial Returns follow a similar pattern ([Wolfram](https://reference.wolfram.com/language/guide/HeavyTailDistributions.html)).

Heavy Tailed Distribution Examples
----------------------------------

Many distributions are heavy tailed, including:

*   [Cauchy Distribution](https://www.statisticshowto.com/cauchy-distribution-2/)
*   [Fréchet Distribution](https://www.statisticshowto.com/frechet-distribution/)
*   [LogNormal Distribution](https://www.statisticshowto.com/lognormal-distribution/)
*   [Pareto Distribution](https://www.statisticshowto.com/pareto-distribution/)
*   [Student’s t Distribution](https://www.statisticshowto.com/probability-and-statistics/t-distribution/)
*   [Zipf Distribution](https://www.statisticshowto.com/zeta-distribution-zipf/)

[Cauchy distribution.](https://www.statisticshowto.com/cauchy-distribution-2/)

[![](https://www.statisticshowto.com/wp-content/uploads/2015/06/cauchy-distribution-300x240.png)](https://www.statisticshowto.com/wp-content/uploads/2015/06/cauchy-distribution.png)

_The Cauchy distribution. The purple curve is the standard Cauchy distribution. Image: Skbkekas | Wikimedia Commons._

The Cauchy is similar to a [normal distribution](https://www.statisticshowto.com/probability-and-statistics/normal-distributions/), except it has heavier tails and a taller peak. It is widely known for the fact that it’s [expected value](https://www.statisticshowto.com/probability-and-statistics/expected-value/) does not exist.

[Log normal distribution](https://www.statisticshowto.com/lognormal-distribution/).

[![](https://www.statisticshowto.com/wp-content/uploads/2015/08/PDF-log_normal_distributions.svg_.png)](https://www.statisticshowto.com/wp-content/uploads/2015/08/PDF-log_normal_distributions.svg_.png)

A few examples of lognormal density functions. Image: By Krishnavedala|Wikimedia Commons

A lognormal (log-normal or Galton) distribution is a probability distribution with a normally distributed [logarithm](https://calculushowto.com/integrals/integral-natural-log-logarithms/).

Heavy Tailed Subclasses
-----------------------

**[Fat tail distribution](https://www.statisticshowto.com/fat-tail-distribution/)**: A heavy tailed distribution with [infinite variance](https://www.statisticshowto.com/infinite-variance/). Note that some authors use the term “fat tail” and “heavy tail” interchangeably, especially in finance and trading.

**Regularly varying**: the tails’ behavior deviates from pure power laws (Mikosch, 1999).

**Subexponential**: A distribution where the largest value in a sample makes a very large contribution to the overall sum (Mikosch, 1999).

**[Long-tailed distribution](https://www.statisticshowto.com/long-tail-distribution/)**: A heavy-tailed distribution with a long tail. Some authors use the term long-tail as a synonym for “light-tailed” (see definition below), although the general consensus is that a long-tail is a subset of heavy-tail. To add to the confusion, others make a distinction between a thin tail (as compared to the exponential) and a tail with a long appearance— as in a very, very, very long tail (imagine 50 standard deviations from the mean, and you’ll begin to get the distinction). The key here is to r_ead the author’s intention very carefully is they are using the term “long tail”._

What is a Light Tailed Distribution?
------------------------------------

[![](https://www.statisticshowto.com/wp-content/uploads/2016/05/light-300x192.png)](https://www.statisticshowto.com/wp-content/uploads/2016/05/light.png)  
Probability distributions that have thinner tails than an exponential distribution are light-tailed distributions. They go to zero much faster than the exponential, and so have less mass in the tail. The [Gumbel distribution](https://www.statisticshowto.com/gumbel-distribution/) is an example of a light-tailed distribution. Most distributions that you’re introduced to in [elementary statistics](https://www.statisticshowto.com/what-is-elementary-statistics/) (e.g. the [normal distribution](https://www.statisticshowto.com/probability-and-statistics/normal-distributions/) & [t-distribution](https://www.statisticshowto.com/probability-and-statistics/t-distribution/)) are actually light-tailed; These don’t reflect “real world” data very well Nair et. al, 2013).

References
----------

Bryson, M. (1974). Heavy Tailed Distributions: Properties and Tests. Technometrics 16(1):61-68 (February 1974).  
Mikosch, T. (1999). Regular Variation, Subexponentiality, and Their Applications in Probability Theory.  
Retrieved December 10, 2017 from: https://www.eurandom.tue.nl/reports/1999/013-report.pdf  
Nair, J. et al., (2013). The fundamentals of heavy tails (PPT). Retrieved December 7, 2017 from: http://users.cms.caltech.edu/~adamw/papers/2013-SIGMETRICS-heavytails.pdf