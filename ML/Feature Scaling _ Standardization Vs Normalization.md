---
title: Feature Scaling
categories:
- ML
- Feature
tags:
- Feature Scaling
- Standardization
- Normalization
date: 2021/3/22 20:00:17
updated: 2021/3/22 12:00:17
---



> [www.analyticsvidhya.com](https://www.analyticsvidhya.com/blog/2020/04/feature-scaling-machine-learning-normalization-standardization/)

Introduction to Feature Scaling
-------------------------------

最近，我正在使用一个具有跨越不同程度的幅度，范围和单位的多个特征的数据集。 这是一个重大障碍，因为一些机器学习算法对这些功能高度敏感。

我敢肯定，你们中的大多数人在您的项目或学习过程中一定会遇到这个问题。 例如，一个要素完全以千克为单位，而另一个要素以克为单位，另一个则以升为单位，依此类推。 当这些功能在呈现的内容方面差异很大时，我们该如何使用这些功能？

> This is where I turned to the concept of feature scaling. It’s a crucial part of the data preprocessing stage but I’ve seen a lot of beginners overlook it (to the detriment of their machine learning model).

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/Feature-image-Normalization-vs.-Standardization.png" style="zoom:50%;" />

这是有关功能扩展的一个奇怪的事情–它（显着）改善了某些机器学习算法的性能，而对其他则完全不起作用。 怪癖背后的原因可能是什么？

Also, what’s the difference between normalization and standardization? These are two of the most commonly used feature scaling techniques in machine learning but a level of ambiguity exists in their understanding. When should you use which technique?

I will answer these questions and more in this article on feature scaling. We will also implement feature scaling in Python to give you a practice understanding of how it works for different machine learning algorithms.

Table of Contents
-----------------

1.  Why Should we Use Feature Scaling?
2.  What is Normalization?
3.  What is Standardization?
4.  The Big Question – Normalize or Standardize?
5.  Implementing Feature Scaling in Python
    *   Normalization using Sklearn
    *   Standardization using Sklearn
6.  Applying Feature Scaling to Machine Learning Algorithms
    *   K-Nearest Neighbours (KNN)
    *   Support Vector Regressor
    *   Decision Tree

Why Should we Use Feature Scaling?
----------------------------------

我们需要解决的第一个问题–为什么我们需要缩放数据集中的变量？ 一些机器学习算法对特征缩放很敏感，而另一些则几乎不变。 让我更详细地解释一下。

### Gradient Descent Based Algorithms

**Machine learning algorithms like [linear regression](https://www.analyticsvidhya.com/blog/2017/05/neural-network-from-scratch-in-python-and-r/?utm_source=blog&utm_medium=feature-scaling-machine-learning-normalization-standardization), [logistic regression](https://www.analyticsvidhya.com/blog/2017/05/neural-network-from-scratch-in-python-and-r/?utm_source=blog&utm_medium=feature-scaling-machine-learning-normalization-standardization), [neural network](https://www.analyticsvidhya.com/blog/2017/05/neural-network-from-scratch-in-python-and-r/?utm_source=blog&utm_medium=feature-scaling-machine-learning-normalization-standardization), etc. that use gradient descent as an optimization technique require data to be scaled.** Take a look at the formula for gradient descent below:
$$
\theta_{j}:=\theta_{j}-\alpha \frac{1}{m} \sum_{i=1}^{m}\left(h_{\theta}\left(x^{(i)}\right)-y^{(i)}\right) x_{j}^{(i)}
$$
公式中特征值X的存在将影响梯度下降的步长。 特征范围的差异将导致每个特征的步长不同。 为确保梯度下降平稳地向最小值移动，并确保所有特征的梯度下降步骤均以相同的速率更新，我们在将数据输入模型之前先对数据进行缩放。

> 具有相似比例的特征可以帮助梯度下降更快地收敛到最小值。

### Distance-Based Algorithms

Distance algorithms like [KNN](https://www.analyticsvidhya.com/blog/2018/03/introduction-k-neighbours-algorithm-clustering/?utm_source=blog&utm_medium=feature-scaling-machine-learning-normalization-standardization), [K-means](https://www.analyticsvidhya.com/blog/2019/08/comprehensive-guide-k-means-clustering/?utm_source=blog&utm_medium=feature-scaling-machine-learning-normalization-standardization), and [SVM](https://www.analyticsvidhya.com/blog/2017/09/understaing-support-vector-machine-example-code/?utm_source=blog&utm_medium=feature-scaling-machine-learning-normalization-standardization) are most affected by the range of features. This is because behind the scenes **they are using distances between data points to determine their similarity.**

For example, let’s say we have data containing high school CGPA scores of students (ranging from 0 to 5) and their future incomes (in thousands Rupees):

![](https://cdn.analyticsvidhya.com/wp-content/uploads/2020/03/knn_ex.png)

由于这两个特征的比例不同，因此可能会给幅值较大的特征赋予更高的权重。 这将影响机器学习算法的性能，显然，我们不希望我们的算法偏向一个功能。

> Therefore, we scale our data before employing a distance based algorithm so that all the features contribute equally to the result.

![](https://cdn.analyticsvidhya.com/wp-content/uploads/2020/03/knn_ex_scaled.png)

The effect of scaling is conspicuous when we compare the Euclidean distance between data points for students A and B, and between B and C, before and after scaling as shown below:

Scaling has brought both the features into the picture and the distances are now more comparable than they were before we applied scaling.

### Tree-Based Algorithms

[Tree-based algorithms](https://www.analyticsvidhya.com/blog/2016/04/tree-based-algorithms-complete-tutorial-scratch-in-python/?utm_source=blog&utm_medium=feature-scaling-machine-learning-normalization-standardization), on the other hand, are fairly insensitive to the scale of the features. 考虑一下，决策树仅基于单个功能拆分节点。 决策树在特征上分割节点，从而增加了节点的同质性。 功能上的此拆分不受其他功能影响。 

So, there is virtually no effect of the remaining features on the split. This is what makes them invariant to the scale of the features!

What is Normalization?
----------------------

**Normalization is a scaling technique in which values are shifted and rescaled so that they end up ranging between 0 and 1. It is also known as Min-Max scaling.**

Here, Xmax and Xmin are the maximum and the minimum values of the feature respectively.

*   When the value of X is the minimum value in the column, the numerator will be 0, and hence X’ is 0
*   On the other hand, when the value of X is the maximum value in the column, the numerator is equal to the denominator and thus the value of X’ is 1
*   If the value of X is between the minimum and the maximum value, then the value of X’ is between 0 and 1

What is Standardization?
------------------------

**Standardization is another scaling technique where the values are centered around the mean with a unit standard deviation. This means that the mean of the attribute becomes zero and the resultant distribution has a unit standard deviation.**

$\mu$ is the mean of the feature values and $\sigma$ is the standard deviation of the feature values. Note that in this case, the values are not restricted to a particular range.

Now, the big question in your mind must be when should we use normalization and when should we use standardization? Let’s find out!

The Big Question – Normalize or Standardize?
--------------------------------------------

Normalization vs. standardization is an eternal question among machine learning newcomers. Let me elaborate on the answer in this section.

*   Normalization is good to use when you know that the distribution of your data does not follow a Gaussian distribution. This can be useful in algorithms that do not assume any distribution of the data like K-Nearest Neighbors and Neural Networks.
*   Standardization, on the other hand, can be helpful in cases where the data follows a Gaussian distribution. However, this does not have to be necessarily true. Also, unlike normalization, standardization does not have a bounding range.  因此，即使您的数据中有异常值，它们也不会受到标准化的影响。 

However, at the end of the day, the choice of using normalization or standardization will depend on your problem and the machine learning algorithm you are using. There is no hard and fast rule to tell you when to normalize or standardize your data. **You can always start by fitting your model to raw, normalized and standardized data and compare the performance for best results.**

_It is a good practice to fit the scaler on the training data and then use it to transform the testing data. This would avoid any data leakage during the model testing process. Also, the scaling of target values is generally not required._

**Implementing Feature Scaling in Python**
------------------------------------------

Now comes the fun part – putting what we have learned into practice. I will be applying feature scaling to a few machine learning algorithms on the [Big Mart dataset](https://datahack.analyticsvidhya.com/contest/practice-problem-big-mart-sales-iii/?utm_source=blog&utm_medium=feature-scaling-machine-learning-normalization-standardization) I’ve taken the [DataHack](https://datahack.analyticsvidhya.com/contest/all/?utm_source=blog&utm_medium=feature-scaling-machine-learning-normalization-standardization) platform.

I will skip the preprocessing steps since they are out of the scope of this tutorial. But you can find them neatly explained in this [article](https://www.analyticsvidhya.com/blog/2016/02/bigmart-sales-solution-top-20/?utm_source=blog&utm_medium=feature-scaling-machine-learning-normalization-standardization). Those steps will enable you to reach the top 20 percentile on the hackathon leaderboard so that’s worth checking out!

So, let’s first split our data into training and testing sets:

Before moving to the feature scaling part, let’s glance at the details about our data using the **pd.describe()** method:

![](https://cdn.analyticsvidhya.com/wp-content/uploads/2020/03/NormVsStand_1.png)

We can see that there is a huge difference in the range of values present in our numerical features: **Item_Visibility**, **Item_Weight, Item_MRP,** and **Outlet_Establishment_Year**. Let’s try and fix that using feature scaling!

_Note: You will notice negative values in the Item_Visibility feature because I have taken log-transformation to deal with the skewness in the feature._

### Normalization using sklearn

To normalize your data, you need to import the _MinMaxScalar_ from the [sklearn](https://courses.analyticsvidhya.com/courses/get-started-with-scikit-learn-sklearn?utm_source=blog&utm_medium=feature-scaling-machine-learning-normalization-standardization) library and apply it to our dataset. So, let’s do that!

Let’s see how normalization has affected our dataset:

![](https://cdn.analyticsvidhya.com/wp-content/uploads/2020/03/NormVsStand_2.png)

All the features now have a minimum value of 0 and a maximum value of 1. Perfect!

Next, let’s try to standardize our data.

### Standardization using sklearn

To standardize your data, you need to import the _StandardScalar_ from the sklearn library and apply it to our dataset. Here’s how you can do it:

You would have noticed that I only applied standardization to my numerical columns and not the other [One-Hot Encoded](https://www.analyticsvidhya.com/blog/2020/03/one-hot-encoding-vs-label-encoding-using-scikit-learn/?utm_source=blog&utm_medium=feature-scaling-machine-learning-normalization-standardization) features. Standardizing the One-Hot encoded features would mean assigning a distribution to categorical features. You don’t want to do that!

But why did I not do the same while normalizing the data? Because One-Hot encoded features are already in the range between 0 to 1. So, normalization would not affect their value.

Right, let’s have a look at how standardization has transformed our data:

![](https://cdn.analyticsvidhya.com/wp-content/uploads/2020/03/NormVsStand_3.png)

The numerical features are now centered on the mean with a unit standard deviation. Awesome!

### Comparing unscaled, normalized and standardized data

It is always great to visualize your data to understand the distribution present. We can see the comparison between our unscaled and scaled data using boxplots.

_You can learn more about data visualization_ [here](https://www.analyticsvidhya.com/blog/tag/data-visualization/?utm_source=blog&utm_medium=feature-scaling-machine-learning-normalization-standardization)_._

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/NormVsStand_box_plots-1.png" style="zoom: 25%;" />

You can notice how scaling the features brings everything into perspective. The features are now more comparable and will have a similar effect on the learning models.

Applying Scaling to Machine Learning Algorithms
-----------------------------------------------

It’s now time to train some machine learning algorithms on our data to compare the effects of different scaling techniques on the performance of the algorithm. I want to see the effect of scaling on three algorithms in particular: K-Nearest Neighbours, Support Vector Regressor, and Decision Tree.

**K-Nearest Neighbours**

Like we saw before, KNN is a distance-based algorithm that is affected by the range of features. Let’s see how it performs on our data, before and after scaling:

![](https://cdn.analyticsvidhya.com/wp-content/uploads/2020/03/NormVsStand_knn.png)

You can see that scaling the features has brought down the RMSE score of our KNN model. Specifically, the normalized data performs a tad bit better than the standardized data.

_Note: I am measuring the RMSE here because this competition evaluates the RMSE._

**Support Vector Regressor**

[SVR](https://www.analyticsvidhya.com/blog/2020/03/support-vector-regression-tutorial-for-machine-learning/?utm_source=blog&utm_medium=feature-scaling-machine-learning-normalization-standardization) is another distance-based algorithm. So let’s check out whether it works better with normalization or standardization:

![](https://cdn.analyticsvidhya.com/wp-content/uploads/2020/03/NormVsStand_svr.png)

We can see that scaling the features does bring down the RMSE score. And the standardized data has performed better than the normalized data. Why do you think that’s the case?

The [sklearn documentation](https://scikit-learn.org/stable/modules/preprocessing.html#standardization-or-mean-removal-and-variance-scaling) states that SVM, with RBF kernel,  assumes that all the features are centered around zero and variance is of the same order. This is because a feature with a variance greater than that of others prevents the estimator from learning from all the features. Great!

**Decision Tree**

We already know that a Decision tree is invariant to feature scaling. But I wanted to show a practical example of how it performs on the data:

![](https://cdn.analyticsvidhya.com/wp-content/uploads/2020/03/NormVsStand_dt.png)

You can see that the RMSE score has not moved an inch on scaling the features. So rest assured when you are using tree-based algorithms on your data!

End Notes
---------

This tutorial covered the relevance of using feature scaling on your data and how normalization and standardization have varying effects on the working of machine learning algorithms

Keep in mind that there is no correct answer to when to use normalization over standardization and vice-versa. It all depends on your data and the algorithm you are using.

As a next step, I encourage you to try out feature scaling with other algorithms and figure out what works best – normalization or standardization? I recommend you use the [BigMart Sales data](https://datahack.analyticsvidhya.com/contest/practice-problem-big-mart-sales-iii/?utm_source=blog&utm_medium=feature-scaling-machine-learning-normalization-standardization) for that purpose to maintain the continuity with this article. And don’t forget to share your insights in the comments section below!

### Related Articles

[![](https://i0.wp.com/cdn.analyticsvidhya.com/wp-content/uploads/2020/12/72267Scaling.png?resize=350%2C200&ssl=1)](https://www.analyticsvidhya.com/blog/2020/12/feature-engineering-feature-improvements-scaling/ "Feature Engineering  (Feature Improvements - Scaling)")

#### [Feature Engineering (Feature Improvements - Scaling)](https://www.analyticsvidhya.com/blog/2020/12/feature-engineering-feature-improvements-scaling/ "Feature Engineering  (Feature Improvements - Scaling)")

Introduction Data Science Lifecycle revolves around using various analytical methods to produce insights and followed by applying Machine Learning Techniques, to do predictions from the collected data from various sources, through that we could achieve major and innovative objectives, challenges and value added solutions for certain business problem statements. The…

[![](https://i1.wp.com/cdn.analyticsvidhya.com/wp-content/uploads/2020/05/Importance-Of-Feature-Engineering-In-DataHack-Competitions.jpg?resize=350%2C200&ssl=1)](https://www.analyticsvidhya.com/blog/2020/10/7-feature-engineering-techniques-machine-learning/ "7 Feature Engineering Techniques in Machine Learning You Should Know")

#### [7 Feature Engineering Techniques in Machine Learning You Should Know](https://www.analyticsvidhya.com/blog/2020/10/7-feature-engineering-techniques-machine-learning/ "7 Feature Engineering Techniques in Machine Learning You Should Know")

This article was published as a part of the Data Science Blogathon. Overview Feature engineering techniques are a must know concept for machine learning professionals Here are 7 feature engineering techniques you can start using right away   Introduction Feature engineering is a topic every machine learning enthusiast has heard…

[![](https://i2.wp.com/cdn.analyticsvidhya.com/wp-content/uploads/2020/07/Feature-Transformation-and-Scaling-Techniques.jpg?resize=350%2C200&ssl=1)](https://www.analyticsvidhya.com/blog/2020/07/types-of-feature-transformation-and-scaling/ "Feature Transformation and Scaling Techniques to Boost Your Model Performance")

#### [Feature Transformation and Scaling Techniques to Boost Your Model Performance](https://www.analyticsvidhya.com/blog/2020/07/types-of-feature-transformation-and-scaling/ "Feature Transformation and Scaling Techniques to Boost Your Model Performance")

Overview Understand the requirement of feature transformation and scaling techniques Get to know different feature transformation and scaling techniques including- MinMax Scaler Standard Scaler Power Transformer Scaler Unit Vector Scaler/Normalizer   Introduction In my machine learning journey, more often than not, I have found that feature preprocessing is a more…