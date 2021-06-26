---
title: Using Support Vector Machines (SVMs) for Regression
categories:
- ML
- SVR
tags:
- svr
date: 2021/6/25
---



> [towardsdatascience.com](https://towardsdatascience.com/an-introduction-to-support-vector-regression-svr-a3ebc1672c2)

Using Support Vector Machines (SVMs) for Regression
---------------------------------------------------

Support Vector Machines (SVMs) are well known in classification problems. The use of SVMs in regression is not as well documented, however. These types of models are known as Support Vector Regression (SVR).

In this article, I will walk through the usefulness of SVR compared to other regression models, do a deep-dive into the math behind the algorithm, and provide an example using the Boston Housing Price dataset.

Simple Linear Regression
------------------------

In most linear regression models, the objective is to minimize the sum of squared errors. Take Ordinary Least Squares (OLS) for example. The objective function for OLS with one predictor (feature) is as follows:

$$
M I N \sum_{i=1}^{n}\left(y_{i}-w_{i} x_{i}\right)^{2}
$$
where yᵢ is the target, wᵢ is the coefficient, and xᵢ is the predictor (feature).

![img](https://miro.medium.com/max/721/1*mR2v3pLqTPyyp9BOCQuvEQ.png)

OLS Prediction of Boston Housing Prices

Lasso, Ridge, and ElasticNet are all extensions of this simple equation, with an additional penalty parameter that aims to minimize complexity and/or reduce the number of features used in the final model. Regardless, the aim — as with many models — is to reduce the error of the test set.

However, what if we are only concerned about reducing error to a certain degree? What if we don’t care how large our errors are, as long as they fall within an acceptable range?

Take housing prices for example. What if we are okay with the prediction being within a certain dollar amount — say $5,000? We can then give our model some flexibility in finding the predicted values, as long as the error is within that range.

SVR FTW
-------

Enter Support Vector Regression. SVR gives us the flexibility to define how much error is acceptable in our model and will find an appropriate line (or hyperplane in higher dimensions) to fit the data.

In contrast to OLS, the objective function of SVR is to minimize the coefficients — more specifically, the _l_2-norm of the coefficient vector — not the squared error. The error term is instead handled in the constraints, where we set the absolute error less than or equal to a specified margin, called the maximum error, ϵ (epsilon). We can tune epsilon to gain the desired accuracy of our model. Our new objective function and constraints are as follows:

**Minimize**:
$$
M I N \frac{1}{2}|| w||^{2}
$$
**Constraints**:
$$
\left|y_{i}-w_{i} x_{i}\right| \leq \varepsilon
$$
**Illustrative Example:**

![img](https://miro.medium.com/max/2073/1*nrXHNqC_hqpyux7GUbtqAQ.png)

Illustrative Example of Simple SVR

Let’s try the simple SVR on our dataset. The plot below shows the results of a trained SVR model on the Boston Housing Prices data. The red line represents the line of best fit and the black lines represent the margin of error, ϵ, which we set to 5 ($5,000).

![img](https://miro.medium.com/max/758/1*bSZn9bK43MaA5vVDamRQ2A.png)

SVR Prediction of Boston Housing Prices with ϵ=5

You may quickly realize that this algorithm doesn’t work for all data points. The algorithm solved the objective function as best as possible but some of the points still fall outside the margins. As such, we need to account for the possibility of errors that are larger than ϵ. We can do this with slack variables.

Giving Ourselves some Slack (and another Hyperparameter)
--------------------------------------------------------

The concept of slack variables is simple: for any value that falls outside of ϵ, we can denote its deviation from the margin as ξ.

We know that these deviations have the potential to exist, but we would still like to minimize them as much as possible. Thus, we can add these deviations to the objective function.

**Minimize**:
$$
M I N \frac{1}{2}|| w||^{2}+C \sum_{i=1}^{n}\left|\xi_{i}\right|
$$
**Constraints**:
$$
\left|y_{i}-w_{i} x_{i}\right| \leq \varepsilon+\left|\xi_{i}\right|
$$
**Illustrative Example:**

![img](https://miro.medium.com/max/2073/1*BunsGiCZCPAHzp33L2lCBA.png)

Illustrative Example of SVR with Slack Variables

We now have an additional hyperparameter, _C_, that we can tune_._ As _C_ increases, our tolerance for points outside of ϵ also increases. As _C_ approaches 0, the tolerance approaches 0 and the equation collapses into the simplified (although sometimes infeasible) one.

Let’s set _C_=1.0 and retrain our model above. The results are plotted below:

![img](https://miro.medium.com/max/758/1*bi8rm_6KlJfoZdX5CNPM7w.png)

SVR Prediction of Boston Housing Prices with ϵ=5, C=1.0

Finding the Best Value of C
---------------------------

The above model seems to fit the data much better. We can go one step further and grid search over _C_ to obtain an even better solution. Let’s define a scoring metric, _% within Epsilon_. This metric measures how many of the total points within our test set fall within our margin of error. We can also monitor how the Mean Absolute Error (_MAE_) varies with _C_ as well.

Below is a plot of the grid search results, with values of _C_ on the x-axis and _% within Epsilon_ and _MAE_ on the left and right y-axes, respectively.

![Hyperparameter Tuning of SVR](https://miro.medium.com/max/943/1*JL3ErBjXHfFk1eF0oTLvDw.png)

GridSearch for C

As we can see, _MAE_ generally decreases as _C_ increases. However, we see a maximum occur in the _% within Epsilon_ metric. Since our original objective of this model was to maximize the prediction within our margin of error ($5,000), we want t find the value of _C_ that maximizes _% within Epsilon_. Thus, _C_=6.13.

Let’s build one last model with our final hyperparameters, ϵ=5, _C_=6.13.

![img](https://miro.medium.com/max/758/1*CgDvkD7OtXVhOScMpyMKsw.png)

SVR Prediction of Boston Housing Prices with ϵ=5, C=6.13

The plot above shows that this model has again improved upon previous ones, as expected.

Conclusion
----------

SVR is a powerful algorithm that allows us to choose how tolerant we are of errors, both through an acceptable error margin(ϵ) and through tuning our tolerance of falling outside that acceptable error rate. Hopefully, this tutorial has shown you the ins-and-outs of SVR and has left you confident enough to add it to your modeling arsenal.