---
title: What Are Degrees of Freedom in Statistics?
categories:
- Math
- Statistic
tags:
- freedom
- T Test
date: 2021/3/17 10:00:00
updated: 2021/3/17 16:00:00
---



# What Are Degrees of Freedom in Statistics?

自由度很难解释。 它们出现在统计数据的许多不同背景中，有些情况既复杂又复杂。 在数学上，它们在技术上被定义为随机向量的域的维数。

但是我们不会涉及到这一点。 因为通常不需要自由度即可进行统计分析，除非您是研究统计学家或学习统计学理论的人。

但是，询问的人想知道。 因此，对于喜欢冒险和好奇的人，以下是一些示例，这些示例提供了其在统计学中的含义的基本要点。

## THE FREEDOM TO VARY

First, forget about statistics. Imagine you’re a fun-loving person who loves to wear hats. You couldn't care less what a degree of freedom is. You believe that variety is the spice of life.

Unfortunately, you have constraints. You have only 7 hats. Yet you want to wear a different hat every day of the week.

On the first day, you can wear any of the 7 hats. On the second day, you can choose from the 6 remaining hats, on day 3 you can choose from 5 hats, and so on.

When day 6 rolls around, you still have a choice between 2 hats that you haven’t worn yet that week. But after you choose your hat for day 6, you have no choice for the hat that you wear on Day 7. You *must* wear the one remaining hat. You had 7-1 = 6 days of “hat” freedom—in which the hat you wore could vary!

这是统计自由度背后的想法。 自由度通常被广义地定义为在估计统计参数时可以自由改变的数据中“观测”（信息的数量）。

## DEGREES OF FREEDOM: 1-SAMPLE T检验

现在想象一下你没有戴帽子。您正在进行数据分析。

您有一个包含10个值的数据集。如果您不进行任何估算，则每个值都可以取任意数字，对不对？每个值都是完全可以自由更改的。

但是，假设您想使用1个样本的t检验来用10个值的样本来检验总体平均值。您现在有了一个约束-均值的估计。那个约束到底是什么？根据平均值的定义，必须满足以下关系：数据中所有值的总和必须等于n x mean，其中n是数据集中值的数量。

因此，如果数据集具有10个值，则10个值的总和必须等于平均值x10。如果10个值的平均值是3.5（您可以选择任何数字），则此约束要求10个值的总和必须等于10 x 3.5 = 35

在这种约束下，数据集中的第一个值可以自由变化。无论值是多少，所有10个数字的总和仍然有可能为35。第二个值也可以自由变化，因为无论您选择什么值，都仍然允许所有值的总和是35。

In fact, the first 9 values could be anything, including these two examples:

34, -8.3, -37, -92, -1, 0, 1, -22, 99
0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9

But to have all 10 values sum to 35, and have a mean of 3.5, the 10th value *cannot* vary. It must be a specific number:

34, -8.3, -37, -92, -1, 0, 1, -22, 99 -----> 10TH value *must* be 61.3
0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9 ----> 10TH value *must* be 30.5

Therefore, you have 10 - 1 = 9 degrees of freedom. It doesn’t matter what sample size you use, or what mean value you use—the last value in the sample is not free to vary. You end up with *n* - 1 degrees of freedom, where *n* is the sample size.

另一种说法是，自由度的数量等于“观测”的数量减去观测之间所需的关系的数量（例如，参数估计的数量）。 对于1样本t检验，花费了一个自由度来估计平均值，而其余的n-1个自由度则用来估计变异性。

The degrees for freedom then define the specific[ t-distribution that’s used to calculate the p-values and t-values for the t-test](https://blog.minitab.com/blog/statistics-and-quality-data-analysis/what-are-t-values-and-p-values-in-statistics).

![t dist](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/t_distribution_df.jpg)

请注意，对于较小的样本量（n），它对应于较小的自由度（对于1样本t检验，n-1），t分布的尾巴更胖。 这是因为t分布是专门设计用于在分析小样本（例如酿造行业）时提供更保守的测试结果的。 随着样本大小（n）的增加，自由度的数量也会增加，并且t分布接近正态分布。

## **DEGREES OF FREEDOM: CHI-SQUARE TEST OF INDEPENDENCE**

Let's look at another context. A chi-square test of independence is used to determine whether two categorical variables are dependent. For this test, the degrees of freedom are the number of cells in the two-way table of the categorical variables that can vary, given the constraints of the row and column marginal totals.So each "observation" in this case is a frequency in a cell.

Consider the simplest example: a 2 x 2 table, with two categories and two levels for each category:

<table border="1" cellpadding="0" cellspacing="0" height="197" width="340">
<tbody>
<tr>
<td style="width: 160px;">
<p style="text-align: center;">&nbsp;</p>
</td>
<td style="width: 319px;" colspan="2">
<p style="text-align: center;"><strong>Category A</strong></p>
</td>
<td style="width: 160px;">
<p style="text-align: center;"><strong>Total</strong></p>
</td>
</tr>
<tr>
<td style="width: 160px;" rowspan="2">
<p style="text-align: center;"><strong>Category B</strong></p>
</td>
<td style="width: 160px;">
<p style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span style="color: #ff8c00;"><strong>?</strong></span></p>
</td>
<td style="width: 160px;">
<p style="text-align: center;">&nbsp;&nbsp;</p>
</td>
<td style="width: 160px;">
<p style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 6</p>
</td>
</tr>
<tr>
<td style="width: 160px;">
<p style="text-align: center;">&nbsp;</p>
</td>
<td style="width: 160px;">
<p style="text-align: center;">&nbsp;</p>
</td>
<td style="width: 160px;">
<p style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 15</p>
</td>
</tr>
<tr>
<td style="width: 160px; height: 5px;">
<p style="text-align: center;"><strong>Total</strong></p>
</td>
<td style="width: 160px; height: 5px;">
<p style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp; 10</p>
</td>
<td style="width: 160px; height: 5px;">
<p style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 11</p>
</td>
<td style="width: 160px; height: 5px;">
<p style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 21</p>
</td>
</tr>
</tbody>
</table>

It doesn't matter what values you use for the row and column marginal totals. Once those values are set, there's only one cell value that can vary (here, shown with the question mark—but it could be any one of the four cells). Once you enter a number for one cell, the numbers for all the other cells are predetermined by the row and column totals. They're not free to vary. So the chi-square test for independence has only 1 degree of freedom for a 2 x 2 table.

Similarly, a 3 x 2 table has 2 degrees of freedom, because only two of the cells can vary for a given set of marginal totals.

<table border="1" cellpadding="0" cellspacing="0" height="197" width="437">
<tbody>
<tr>
<td style="width: 136px;">
<p>&nbsp;</p>
</td>
<td style="width: 375px;" colspan="3">
<p style="text-align: center;"><strong>Category A</strong></p>
</td>
<td style="width: 128px;">
<p style="text-align: center;"><strong>&nbsp;Total</strong></p>
</td>
</tr>
<tr>
<td style="width: 136px;" rowspan="2">
<p style="text-align: center;"><strong>Category B</strong></p>
</td>
<td style="width: 136px;">
<p style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span style="color: #ff8c00;"><strong>?</strong></span></p>
</td>
<td style="width: 122px;">
<p style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span style="color: #ff8c00;"><strong>?</strong></span></p>
</td>
<td style="width: 117px;">
<p style="text-align: center;">&nbsp;</p>
</td>
<td style="width: 128px;">
<p style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 15</p>
</td>
</tr>
<tr>
<td style="width: 136px;">
<p style="text-align: center;">&nbsp;</p>
</td>
<td style="width: 122px;">
<p style="text-align: center;">&nbsp;</p>
</td>
<td style="width: 117px;">
<p style="text-align: center;">&nbsp;</p>
</td>
<td style="width: 128px;">
<p style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 15</p>
</td>
</tr>
<tr>
<td style="width: 136px;">
<p style="text-align: center;"><strong>Total</strong></p>
</td>
<td style="width: 136px;">
<p style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 10</p>
</td>
<td style="width: 122px;">
<p style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 11</p>
</td>
<td style="width: 117px;">
<p style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp; 9</p>
</td>
<td style="width: 128px;">
<p style="text-align: center;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 30</p>
</td>
</tr>
</tbody>
</table>

If you experimented with different sized tables, eventually you’d find a general pattern. For a table with *r* rows and *c* columns, the number of cells that can vary is (*r*-1)(*c*-1). And that’s the formula for the degrees for freedom for the chi-square test of independence!

The degrees of freedom then define the chi-square distribution used to evaluate independence for the test.

![chi square](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/chi_square_dfs.jpg)

卡方分布呈正偏。 随着自由度的增加，它接近法线曲线。

## **DEGREES OF FREEDOM: REGRESSION**

Degrees of freedom is more involved in the context of regression. 

回想一下，自由度通常等于观察值（或信息片段）减去估计的参数数量。 当执行回归时，将为模型中的每个术语估计一个参数，并且每个参数都会消耗一个自由度。 因此，在多元回归模型中包含过多的项会降低可用于估计参数变异性的自由度。In fact, if the amount of data isn't sufficient for the number of terms in your model, there may not even be enough degrees of freedom (DF) for the error term and no p-value or F-values can be calculated at all. You'll get output something like this:

![regression output](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/regression_output_dof.jpg)

如果发生这种情况，您要么需要收集更多数据（以增加自由度），要么从模型中删除项（以减少所需的自由度数）。 因此，尽管存在于随机向量域的整个世界中，自由度的确对您的数据分析产生了实际的，切实的影响。

