---
title: Marginal Distribution
categories:
- Math
- Statistic
tags:
- Marginal Distribution
date: 2021/6/4 10:00:00
updated: 2021/6/4 16:00:00
---

> [Marginal distribution - Wikipedia](https://en.wikipedia.org/wiki/Marginal_distribution)

# Marginal distribution

In [probability theory](https://en.wikipedia.org/wiki/Probability_theory) and [statistics](https://en.wikipedia.org/wiki/Statistics), the **marginal distribution** of a [subset](https://en.wikipedia.org/wiki/Subset) of a [collection](https://en.wikipedia.org/wiki/Indexed_family) of [random variables](https://en.wikipedia.org/wiki/Random_variable) is the [probability distribution](https://en.wikipedia.org/wiki/Probability_distribution) of the variables contained in the subset. It gives the probabilities of various values of the variables in the subset without reference to the values of the other variables. This contrasts with a [conditional distribution](https://en.wikipedia.org/wiki/Conditional_distribution), which gives the probabilities contingent upon the values of the other variables.

在概率论和统计学中，一个随机变量集合的一个子集的边际分布是该子集中包含的变量的概率分布。它给出了子集中变量的各种值的概率，而不参考其他变量的值。这与条件分布相反，条件分布给出的概率取决于其他变量的值。

**Marginal variables** are those variables in the subset of variables being retained. These concepts are "marginal" because they can be found by summing values in a table along rows or columns, and writing the sum in the margins of the table.[[1\]](https://en.wikipedia.org/wiki/Marginal_distribution#cite_note-1) The distribution of the marginal variables (the marginal distribution) is obtained by **marginalizing** – that is, focusing on the sums in the margin – over the distribution of the variables being discarded, and the discarded variables are said to have been **marginalized out**.

边际变量是被保留变量的子集中的变量。这些概念是“边际的”，因为它们可以通过对表中的行或列的值求和，并将求和写在表的边距中来找到边际变量的分配(边际分配)是通过使边际- -即集中于边际的总和- -超过被抛弃的变量的分配而得到的，而被抛弃的变量据说已被边缘化。

The context here is that the theoretical studies being undertaken, or the [data analysis](https://en.wikipedia.org/wiki/Data_analysis) being done, involves a wider set of random variables but that attention is being limited to a reduced number of those variables. In many applications, an analysis may start with a given collection of random variables, then first extend the set by defining new ones (such as the sum of the original random variables) and finally reduce the number by placing interest in the marginal distribution of a subset (such as the sum). Several different analyses may be done, each treating a different subset of variables as the marginal variables.

这里的背景是，正在进行的理论研究，或正在进行的数据分析，涉及到更广泛的随机变量，但注意力被限制在这些变量的数量减少。在许多应用程序中,一个分析可能从一个给定的随机变量的集合,然后第一个扩展设置通过定义新的(如原始随机变量之和),最终减少通过将感兴趣的边际分布(如之和)的一个子集。可以进行几种不同的分析，每种分析都将不同的变量子集作为边际变量。

## Definition

### Marginal probability mass function

Given a known [joint distribution](https://en.wikipedia.org/wiki/Joint_distribution) of two **discrete** [random variables](https://en.wikipedia.org/wiki/Random_variable), say, *X* and *Y*, the marginal distribution of either variable – *X* for example — is the [probability distribution](https://en.wikipedia.org/wiki/Probability_distribution) of *X* when the values of *Y* are not taken into consideration. This can be calculated by summing the [joint probability](https://en.wikipedia.org/wiki/Joint_probability) distribution over all values of *Y*. Naturally, the converse is also true: the marginal distribution can be obtained for *Y* by summing over the separate values of *X*.

${\displaystyle p_{X}(x_{i})=\sum _{j}p(x_{i},y_{j})}$, and ${\displaystyle \ p_{Y}(y_{j})=\sum _{i}p(x_{i},y_{j})}$

<table class="wikitable" style="text-align: center; width:350px; margin: 1em auto 1em auto;">
<tbody><tr>
<th style="background:linear-gradient(to top right,#eaecf0 49.5%,#aaa 49.5%,#aaa 50.5%,#eaecf0 50.5%);line-height:1;"><div style="margin-left:2em;text-align:right;"><i>X</i></div><div style="margin-right:2em;text-align:left;"><i>Y</i></div></th>
<th><i>x</i><sub>1</sub></th>
<th><i>x</i><sub>2</sub></th>
<th><i>x</i><sub>3</sub></th>
<th><i>x</i><sub>4</sub></th>
<th><i>p<sub>Y</sub></i>(<i>y</i>) ↓
</th></tr>
<tr>
<th><i>y</i><sub>1</sub>
</th>
<td>4/32</td>
<td>2/32</td>
<td>1/32</td>
<td>1/32
</td>
<th>&nbsp;&nbsp;8/32
</th></tr>
<tr>
<th><i>y</i><sub>2</sub>
</th>
<td>3/32</td>
<td>6/32</td>
<td>3/32</td>
<td>3/32
</td>
<th>15/32
</th></tr>
<tr>
<th><i>y</i><sub>3</sub>
</th>
<td>9/32</td>
<td>0</td>
<td>0</td>
<td>0
</td>
<th>&nbsp;&nbsp;9/32
</th></tr>
<tr>
<th><i>p<sub>X</sub></i>(<i>x</i>) →
</th>
<th>16/32</th>
<th>8/32</th>
<th>4/32</th>
<th>4/32
</th>
<th>32/32
</th></tr>
<tr>
<td colspan="6"><b>Table. 1</b> Joint and marginal distributions of a pair of discrete random variables, <i>X</i> and <i>Y</i>, dependent, thus having nonzero <a href="/wiki/Mutual_information" title="Mutual information">mutual information</a> <i>I</i>(<i>X</i>; <i>Y</i>). The values of the joint distribution are in the 3×4 rectangle; the values of the marginal distributions are along the right and bottom margins.
</td></tr></tbody></table>

A **marginal probability** can always be written as an [expected value](https://en.wikipedia.org/wiki/Expected_value):
$$
{\displaystyle p_{X}(x)=\int _{y}p_{X\mid Y}(x\mid y)\,p_{Y}(y)\,\mathrm {d} y=\operatorname {E} _{Y}[p_{X\mid Y}(x\mid y)]\;.}
$$
Intuitively, the marginal probability of *X* is computed by examining the conditional probability of *X* given a particular value of *Y*, and then averaging this conditional probability over the distribution of all values of *Y*.

This follows from the definition of [expected value](https://en.wikipedia.org/wiki/Expected_value) (after applying the [law of the unconscious statistician](https://en.wikipedia.org/wiki/Law_of_the_unconscious_statistician))
$$
{\displaystyle \operatorname {E} _{Y}[f(Y)]=\int _{y}f(y)p_{Y}(y)\,\mathrm {d} y.}
$$
Therefore, marginalization provides the rule for the transformation of the probability distribution of a random variable *Y* and another random variable *X* = *g*(*Y*):
$$
{\displaystyle p_{X}(x)=\int _{y}p_{X\mid Y}(x\mid y)\,p_{Y}(y)\,\mathrm {d} y=\int _{y}\delta {\big (}x-g(y){\big )}\,p_{Y}(y)\,\mathrm {d} y.}
$$


### Marginal probability density function

Given two **continuous** [random variables](https://en.wikipedia.org/wiki/Random_variable) *X* and *Y* whose [joint distribution](https://en.wikipedia.org/wiki/Joint_distribution) is known, then the marginal [probability density function](https://en.wikipedia.org/wiki/Probability_density_function) can be obtained by integrating the [joint probability](https://en.wikipedia.org/wiki/Joint_probability) distribution, $f$, over *Y,* and vice versa. That is
$$
{\displaystyle f_{X}(x)=\int _{c}^{d}f(x,y)dy,} \text{ and } {\displaystyle f_{Y}(y)=\int _{a}^{b}f(x,y)dx}
$$
where $x\in [a,b]$, and ${\displaystyle y\in [c,d]}$.

## Marginal distribution vs. conditional distribution

### Definition

The **marginal probability** is the probability of a single event occurring, independent of other events. A **[conditional probability](https://en.wikipedia.org/wiki/Conditional_probability_distribution)**, on the other hand, is the probability that an event occurs given that another specific event *has already* occurred. This means that the calculation for one variable is dependent on another variable.[[2\]](https://en.wikipedia.org/wiki/Marginal_distribution#cite_note-2)

The conditional distribution of a variable given another variable is the joint distribution of both variables divided by the marginal distribution of the other variable.[[3\]](https://en.wikipedia.org/wiki/Marginal_distribution#cite_note-3) That is,

${\displaystyle p_{Y|X}(y|x)=P(Y=y|X=x)={\frac {P(X=x,Y=y)}{P_{X}(x)}}}$ for **discrete [random variables](https://en.wikipedia.org/wiki/Random_variable)**,

${\displaystyle f_{Y|X}(y|x)={\frac {f_{X,Y}(x,y)}{f_{X}(x)}}}$ for **continuous random variables**.

### Example

Suppose there is data from classroom of 200 students on the amount of time studied (*X*) and the percent correct (*Y*).[[4\]](https://en.wikipedia.org/wiki/Marginal_distribution#cite_note-4) Assuming that *X* and *Y* are discrete random variables, the joint distribution of *X* and *Y* can be described by listing all the possible values of *p(xi,yj)*, as shown in Table.3.

<table class="wikitable" style="text-align: center; width:560px; margin: 1em auto 1em auto;">
<tbody><tr>
<th style="background:linear-gradient(to top right,#eaecf0 49.5%,#aaa 49.5%,#aaa 50.5%,#eaecf0 50.5%);line-height:1;"><div style="margin-left:2em;text-align:right;"><i>X</i></div><div style="margin-right:2em;text-align:left;"><i>Y</i></div>
</th>
<th colspan="6">Time studied (minutes)
</th></tr>
<tr>
<th rowspan="7">% correct
</th>
<th scope="col">
</th>
<th scope="col"><i>x<sub>1</sub></i> (0-20)
</th>
<th scope="col"><i>x<sub>2</sub></i> (21-40)
</th>
<th><i>x<sub>3</sub></i> (41-60)
</th>
<th><i>x<sub>4</sub></i>(&gt;60)
</th>
<th><i>p<sub>Y</sub>(y)</i> ↓
</th></tr>
<tr>
<th><i>y<sub>1</sub></i> (0-20)
</th>
<td>2/200
</td>
<td>0
</td>
<td>0
</td>
<td>8/200
</td>
<th>10/200
</th></tr>
<tr>
<th><i>y<sub>2</sub></i> (21-40)
</th>
<td>10/200
</td>
<td>2/200
</td>
<td>8/200
</td>
<td>0
</td>
<th>20/200
</th></tr>
<tr>
<th><i>y<sub>3</sub></i> (41-59)
</th>
<td>2/200
</td>
<td>4/200
</td>
<td>32/200
</td>
<td>32/200
</td>
<th>70/200
</th></tr>
<tr>
<th><i>y<sub>4</sub></i> (60-79)
</th>
<td>0
</td>
<td>20/200
</td>
<td>30/200
</td>
<td>10/200
</td>
<th>60/200
</th></tr>
<tr>
<th><i>y<sub>5</sub></i> (80-100)
</th>
<td align="center">0
</td>
<td align="center">4/200
</td>
<td align="center">16/200
</td>
<td align="center">20/200
</td>
<th>40/200
</th></tr>
<tr>
<th><i>p<sub>X</sub>(x)</i> →
</th>
<th align="center">14/200
</th>
<th align="center">30/200
</th>
<th align="center">86/200
</th>
<th align="center">70/200
</th>
<th align="center">1
</th></tr>
<tr>
<td colspan="7"><b>Table.3</b> <a href="/wiki/Two-way_table" class="mw-redirect" title="Two-way table">Two-way table</a> of dataset of the relationship in a classroom of 200 students between the amount of time studied and the percent correct
</td></tr></tbody></table>

The **marginal distribution** can be used to determine how many students that scored 20 or below: 
${\displaystyle p_{Y}(y_{1})=P_{Y}(Y=y_{1})=\sum _{i=1}^{4}P(x_{i},y_{1})={\frac {2}{200}}+{\frac {8}{200}}={\frac {10}{200}}}$, meaning 10 students or 5%.

The **[conditional distribution](https://en.wikipedia.org/wiki/Conditional_distribution)** can be used to determine the probability that a student that studied 60 minutes or more obtains a scored of 20 or below: ${\displaystyle p_{Y|X}(y_{1}|x_{4})=P(Y=y_{1}|X=x_{4})={\frac {P(X=x_{4},Y=y_{1})}{P(X=x_{4})}}={\frac {8/200}{70/200}}={\frac {8}{70}}={\frac {4}{35}}}$, meaning there is about a 11% probability of scoring 20 after having studied for at least 60 minutes.

## Real-world example

Suppose that the probability that a pedestrian will be hit by a car, while crossing the road at a pedestrian crossing, without paying attention to the traffic light, is to be computed. Let H be a [discrete random variable](https://en.wikipedia.org/wiki/Discrete_random_variable) taking one value from {Hit, Not Hit}. Let L (for traffic light) be a discrete random variable taking one value from {Red, Yellow, Green}.

Realistically, H will be dependent on L. That is, P(H = Hit) will take different values depending on whether L is red, yellow or green (and likewise for P(H = Not Hit)). A person is, for example, far more likely to be hit by a car when trying to cross while the lights for perpendicular traffic are green than if they are red. In other words, for any given possible pair of values for H and L, one must consider the [joint probability distribution](https://en.wikipedia.org/wiki/Joint_probability_distribution) of H and L to find the probability of that pair of events occurring together if the pedestrian ignores the state of the light.

However, in trying to calculate the **marginal probability** P(H = Hit), what is being sought is the probability that H = Hit in the situation in which the particular value of L is unknown and in which the pedestrian ignores the state of the light. In general, a pedestrian can be hit if the lights are red OR if the lights are yellow OR if the lights are green. So, the answer for the marginal probability can be found by summing P(H | L) for all possible values of L, with each value of L weighted by its probability of occurring.

Here is a table showing the conditional probabilities of being hit, depending on the state of the lights. (Note that the columns in this table must add up to 1 because the probability of being hit or not hit is 1 regardless of the state of the light.)

|  H \ L  | Red  | Yellow | Green |
| :-----: | :--: | :----: | :---: |
| Not Hit | 0.99 |  0.9   |  0.2  |
|   Hit   | 0.01 |  0.1   |  0.8  |

To find the joint probability distribution, more data is required. For example, suppose P(L = red) = 0.2, P(L = yellow) = 0.1, and P(L = green) = 0.7. Multiplying each column in the conditional distribution by the probability of that column occurring results in the joint probability distribution of H and L, given in the central 2×3 block of entries. (Note that the cells in this 2×3 block add up to 1).

|  H \ L  |  Red  | Yellow | Green | Marginal probability P(*H*) |
| :-----: | :---: | :----: | :---: | :-------------------------: |
| Not Hit | 0.198 |  0.09  | 0.14  |            0.428            |
|   Hit   | 0.002 |  0.01  | 0.56  |            0.572            |
|  Total  |  0.2  |  0.1   |  0.7  |              1              |

The marginal probability P(H = Hit) is the sum 0.572 along the H = Hit row of this joint distribution table, as this is the probability of being hit when the lights are red OR yellow OR green. Similarly, the marginal probability that P(H = Not Hit) is the sum along the H = Not Hit row.

## Multivariate distributions

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/300px-MultivariateNormal.png)

Many samples from a bivariate normal distribution. The marginal distributions are shown in red and blue. The marginal distribution of X is also approximated by creating a histogram of the X coordinates without consideration of the Y coordinates.

For [multivariate distributions](https://en.wikipedia.org/wiki/Multivariate_distribution), formulae similar to those above apply with the symbols *X* and/or *Y* being interpreted as vectors. In particular, each summation or integration would be over all variables except those contained in *X*.[[5\]](https://en.wikipedia.org/wiki/Marginal_distribution#cite_note-:1-5)

That means, If *X1,X2*,...,Xn are **discrete [random variables](https://en.wikipedia.org/wiki/Random_variable)**, then the marginal [probability mass function](https://en.wikipedia.org/wiki/Probability_mass_function) should be ${\displaystyle p_{X_{i}}(k)=\sum p(x_{1},x_{2},...,x_{i-1},k,x_{i+1},...x_{n})}$ 

if *X1,X2*,...Xn are **continuous random variables**, then the marginal [probability density function](https://en.wikipedia.org/wiki/Probability_density_function) should be ${\displaystyle f_{X_{i}}(x_{i})=\int _{-\infty }^{\infty }\int _{-\infty }^{\infty }\int _{-\infty }^{\infty }...\int _{-\infty }^{\infty }f(x_{1},x_{2},...,x_{n})dx_{1}dx_{2}...dx_{i-1}dx_{i+1}...dx_{n}}$