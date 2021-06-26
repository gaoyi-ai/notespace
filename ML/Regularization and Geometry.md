---
title: Regularization and Geometry
categories:
- ML
- Norm
tags:
- regularization
date: 2021/6/25
---



# Regularization and Geometry

# **I. Bias-Variance Tradeoff**

When we perform statistical modeling, the goal is not to select a model that fits all the training data points and obtain the smallest error on the  training data. The objective is to give the model the ability to  generalize well on new and unseen data.

![img](https://miro.medium.com/max/558/1*EaLwMYXiXdB8iFQQigZPzw.png)

Bias and variance contributing to generalization error [1]

As more and more parameters are added to a model, the complexity of the model increases, i.e., it can fit more noise in training data  and leads to overfitting. This means that we are increasing the variance and decreasing the bias of the model. From the figure above, if we keep raising the model’s complexity, the generalization error will  eventually pass the optimal spot and continue to increase.

To conclude, if our model complexity exceeds the optimal point, we risk  falling into the overfitting zone; while if the complexity falls short  of the optimal point, we are in the underfitting zone.

Regularization is a technique that helps prevent the statistical model from falling  into the overfitting zone. It does so by hindering the complexity of the model.

# II. Regularization

**Linear Regression**

This is the equation for a simple linear regression.
$$
\hat{Y}=\beta_{0}+\beta_{1} X
$$
Linear Regression

Our goal is to find the set of beta to minimize the residual sum of squares (RSS).
$$
R S S=\sum_{i=1}^{n}\left(y_{i}-\left(\beta_{0}+\beta_{1} x_{i}\right)\right)^{2}
$$
Residual Sum of Squares

**L2 Regularization**

A regression model that utilizes L2 regularization is also called Ridge Regression. In terms of Linear Regression, instead of just minimizing the RSS. We add another piece to the game by having a constraint for the betas.
$$
\beta_{0}^{2}+\beta_{1}^{2}<S
$$
L1 Regularization

“s” can be understood as a budget for the betas. If you want to make one of the first beta big, the second beta has to be small and vice versa.  This becomes a competition and as we have more betas, the unimportant  ones will be forced to be small.

![img](https://miro.medium.com/max/989/1*WXCYBidYnW4D8VI1A7FIzA.jpeg)

Due to the square term, the RSS equation actually gives us the geometry of  an ellipse. The black point is the set of betas using the normal  equation, i.e., without regularization. The red circle contains all the  sets of betas that we can "afford." The red point is the final betas for Ridge Regression, which is closest to the black point while in our  budget. The red point will always be on the boundary of the constraint.  The only time that you attain a red point inside the constraint is when  the solution for the normal equation, i.e., the solution for Linear  Regression without regularization, already satisfies the constraint.

**L1 Regularization**

A regression model that utilizes L1 regularization is also called Lasso (*Least Absolute Shrinkage and Selection Operator*) Regression. The goal of Lasso is similar to Ridge with the exception that the constraint becomes:
$$
\mid \beta_0 \mid + \mid \beta_1 \mid \ \leq s
$$
L2 Regularization

Lasso also provides us with a beautiful geometry that comes with unique properties.

![img](https://miro.medium.com/max/989/1*WHp1RrtEUhMh-aMsd0wajQ.jpeg)

The set of betas that we can “afford” with L2 regularization lies within a  diamond. The red point is at the corner of the diamond, which sets one  of the betas to 0.

What if the red point is on the edge of the diamond instead, i.e., the  ellipse touches the diamond on the edge? Hence, neither of the betas  will be 0. However, the diamond in 2-D space is a special case where  neither of the betas becomes 0 if the red point is on the edge.

Let us consider the case where we have 3 betas. The geometry for the constraint becomes:

![img](https://miro.medium.com/max/2061/1*jdVlHC6plWGdJd6iN8Q4YA.jpeg)

Lasso with 3 betas

The figure above consists of 6 vertices and 8 edges. If the red point lies  on an edge, one beta will be set to 0. If it lies on a vertices, two  betas will be set to 0. As the dimension increases, the number of  vertices and edges increases as well, making it more likely that the  ellipse will be in contact with the diamond on one of those places. That being said, Lasso tends to work better in higher dimension.

**Differences between L1 and L2 Regularization**

Ridge regression is an extension for linear regression that enforces the  betas coefficients to be small, reducing the impact of irrelevant  features. This way the statistical model will not fit all the noise in  the training data and fall into the overfitting zone.

Lasso regression bring some unique properties to the table because of its  beautiful geometry. Some of the betas will be set to 0, giving us a  sparse output. We can also use Lasso for feature selection. While  feature selection techniques such as [Best Subset](https://www.statisticssolutions.com/best-subsets-regression/), [Forward Stepwise](https://gerardnico.com/data_mining/stepwise_regression) or [Backward Stepwise](https://gerardnico.com/data_mining/stepwise_regression) may be time inefficient, Lasso will converge faster to the final solution.

# **III. Conclusion**

A statistical model that with high complexity may be prone to  overfitting. In this article, I introduced two regularization techniques to discourage the model from fitting all the noise in the training  data. Moreover, I explained their properties and differences with the  help of geometry.

That is the end of my article! Have a wonderful day folks :)

## **Images:**

[1] Daniel Saunders, [The Bias-Variance Tradeoff](https://djsaunde.wordpress.com/2017/07/17/the-bias-variance-tradeoff/) (2017)