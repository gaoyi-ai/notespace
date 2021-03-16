---
title: Stochastic Descent Algorithm with Python and Numpy
categories:
- Optimization
tags:
- SGD
date: 2021/3/12 10:00:00
updated: 2021/3/12 16:00:00
---



[**随机梯度下降**](https://en.wikipedia.org/wiki/Stochastic_gradient_descent)是机器学习应用中经常使用的一种优化算法，用于寻找预测输出和实际输出之间对应的最佳拟合的模型参数。这是一种不精确但强大的技术。

随机梯度下降法在机器学习应用中被广泛使用。结合[反向传播](https://brilliant.org/wiki/backpropagation/)，它在[神经网络](https://realpython.com/python-keras-text-classification/#a-primer-on-deep-neural-networks)训练应用中占主导地位。

**In this tutorial, you’ll learn:**

*   How **gradient descent** and **stochastic gradient descent** algorithms work
*   How to apply gradient descent and stochastic gradient descent to **minimize the loss function** in machine learning
*   What the **learning rate** is, why it’s important, and how it impacts results
*   How to **write your own function** for stochastic gradient descent

Basic Gradient Descent Algorithm
--------------------------------------------------------------------------------------

[梯度下降算法](https://en.wikipedia.org/wiki/Gradient_descent)是[数学优化](https://en.wikipedia.org/wiki/Mathematical_optimization)的一种近似和迭代方法。你可以用它来接近任何[可微分函数](https://en.wikipedia.org/wiki/Differentiable_function)的最小值。

虽然梯度下降法有时会卡在[局部最小值](https://en.wikipedia.org/wiki/Local_optimum)或[鞍点](https://en.wikipedia.org/wiki/Saddle_point)，而不是找到全局最小值，但它在实践中被广泛使用。[数据科学](https://realpython.com/learning-paths/data-science-python-core-skills/)和[机器学习](https://realpython.com/learning-paths/machine-learning-python/)方法经常在内部应用它来优化模型参数。例如，神经网络用梯度下降法找到[权重和偏置](https://docs.paperspace.com/machine-learning/wiki/weights-and-biases)。

### Cost Function: The Goal of Optimization

**成本函数**，或[损失函数](https://en.wikipedia.org/wiki/Loss_function)，是指通过改变决策变量来最小化（或最大化）的函数。许多机器学习方法解决的是表面下的优化问题。它们倾向于通过调整模型参数（如[神经网络](https://en.wikipedia.org/wiki/Artificial_neural_network)的权重和偏置、[随机森林](https://en.wikipedia.org/wiki/Random_forest)或[梯度提升](https://en.wikipedia.org/wiki/Gradient_boosting)的决策规则等）来最小化实际输出和预测输出之间的差异。

在[回归问题](https://realpython.com/linear-regression-in-python/#regression)中，你通常有输入变量的向量𝐱=(𝑥₁，...，𝑥ᵣ)和实际输出𝑦。你想找到一个模型，将𝐱映射到预测响应𝑓(𝐱)，使𝑓(𝐱)尽可能地接近于𝑦。例如，您可能想预测一个人的工资等输出，给定的输入是这个人在公司的年数或教育水平。

Your goal is to minimize the difference between the prediction 𝑓(𝐱) and the actual data 𝑦. This difference is called the **residual**.

In this type of problem, you want to minimize the [sum of squared residuals (SSR)](https://en.wikipedia.org/wiki/Residual_sum_of_squares), where SSR = Σᵢ(𝑦ᵢ − 𝑓(𝐱ᵢ))² for all observations 𝑖 = 1, …, 𝑛, where 𝑛 is the total number of observations. Alternatively, you could use the [mean squared error](https://en.wikipedia.org/wiki/Mean_squared_error) (MSE = SSR / 𝑛) instead of SSR.

Both SSR and MSE use the square of the difference between the actual and predicted outputs. The lower the difference, the more accurate the prediction. A difference of zero indicates that the prediction is equal to the actual data.

SSR or MSE is minimized by adjusting the model parameters. For example, in [linear regression](https://realpython.com/linear-regression-in-python/), you want to find the function 𝑓(𝐱) = 𝑏₀ + 𝑏₁𝑥₁ + ⋯ + 𝑏ᵣ𝑥ᵣ, so you need to determine the weights 𝑏₀, 𝑏₁, …, 𝑏ᵣ that minimize SSR or MSE.

在一个[分类问题](https://realpython.com/logistic-regression-python/#classification)中，输出𝑦是[分类](https://en.wikipedia.org/wiki/Categorical_variable)，通常不是0就是1。例如，你可能会尝试预测一封邮件是否是垃圾邮件。在二进制输出的情况下，将[交叉熵函数](https://en.wikipedia.org/wiki/Cross_entropy)最小化是很方便的，它也取决于实际输出𝑦ᵢ和相应的预测𝑝(𝐱ᵢ)。

$$
H=-\sum_{i}\left(y_{i} \log \left(p\left(\mathbf{x}_{i}\right)\right)+\left(1-y_{i}\right) \log \left(1-p\left(\mathbf{x}_{i}\right)\right)\right)
$$
In [logistic regression](https://realpython.com/logistic-regression-python/), which is often used to solve classification problems, the functions 𝑝(𝐱) and 𝑓(𝐱) are defined as the following:
$$
\begin{array}{c}
p(\mathbf{x})=\frac{1}{1+\exp (-f(\mathbf{x}))} \\
f(\mathbf{x})=b_{0}+b_{1} x_{1}+\cdots+b_{r} x_{r}
\end{array}
$$
Again, you need to find the weights 𝑏₀, 𝑏₁, …, 𝑏ᵣ, but this time they should minimize the cross-entropy function.

### Gradient of a Function: Calculus Refresher

在微积分中，函数的[导数](https://www.mathsisfun.com/calculus/derivatives-introduction.html)向你展示了当你修改它的参数(或参数)时，一个值的变化有多大。导数对优化很重要，因为[零导数](http://sofia.nmsu.edu/~breakingaway/ebookofcalculus/MeaningOfDerivativesAndIntegrals/WhatDoesItMeanThatTheDerivativeOfAFunctionEquals0/WhatDoesItMeanThatTheDerivativeOfAFunctionEquals0.html)可能表示最小、最大或鞍点。

函数𝐶 的几个独立变量𝑣₁，...，𝑣ᵣ的[梯度](https://en.wikipedia.org/wiki/Gradient)用∇𝐶(𝑣₁，...，𝑣ᵣ)表示，并定义为𝐶 的[部分导数](https://en.wikipedia.org/wiki/Partial_derivative)对每个独立变量的向量函数：∇𝐶 = (∂𝐶/∂𝑣₁，...，∂𝐶/𝑣ᵣ)。符号∇称为[nabla](https://en.wikipedia.org/wiki/Nabla_symbol)。

函数𝐶在给定点的梯度的非零值定义了𝐶最快增加的方向和速率。当使用梯度下降时，你感兴趣的是成本函数中最快的_减少的方向。这个方向是由负梯度，-∇𝐶决定的。

### Intuition Behind Gradient Descent

为了理解梯度下降算法，想象一滴水从碗边滑下，或者一个球从山上滚下。水滴和球倾向于沿着下降最快的方向移动，直到它们到达底部。随着时间的推移，它们会获得动量并加速。

梯度下降背后的想法与此类似：你从任意选择的点或向量𝐯=（𝑣₁，...，𝑣ᵣ）的位置开始，并沿着成本函数的最快下降方向迭代移动。如前所述，这就是负梯度向量的方向，-∇𝐶。

一旦你有了一个随机的起始点𝐯 = (𝑣₁，...，𝑣ᵣ)，你就会**更新**它，或者将它移动到负梯度方向的新位置：𝐯 → 𝐯 - 𝜂∇𝐶，其中𝜂(读作 "e-tah")是一个小的正值，称为**学习率**。

学习率决定了更新或移动步骤的大小。这是一个非常重要的参数。如果𝜂太小，那么算法可能收敛得很慢。大的𝜂值也会导致收敛问题或使算法出现分歧。

### Implementation of Basic Gradient Descent

Now that you know how the basic gradient descent works, you can implement it in Python. You’ll use only plain Python and [NumPy](https://numpy.org/), which enables you to write [concise code](https://realpython.com/numpy-array-programming/) when working with arrays (or vectors) and gain a [performance boost](https://realpython.com/numpy-tensorflow-performance/).

This is a basic implementation of the algorithm that starts with an arbitrary point, `start`, iteratively moves it toward the minimum, and [returns](https://realpython.com/python-return-statement/) a point that is hopefully at or near the minimum:

```python
def gradient_descent(gradient, start, learn_rate, n_iter):
    vector = start
    for _ in range(n_iter):
        diff = -learn_rate * gradient(vector)
        vector += diff
    return vector
```

`gradient_descent()` takes four arguments:

1.  **`gradient`** is the [function](https://realpython.com/defining-your-own-python-function/) or any Python [callable object](https://docs.python.org/3/reference/datamodel.html#emulating-callable-objects) that takes a vector and returns the gradient of the function you’re trying to minimize.
2.  **`start`** is the point where the algorithm starts its search, given as a sequence ([tuple, list](https://realpython.com/python-lists-tuples/), [NumPy array](https://numpy.org/doc/stable/reference/generated/numpy.ndarray.html), and so on) or scalar (in the case of a one-dimensional problem).
3.  **`learn_rate`** is the learning rate that controls the magnitude of the vector update.
4.  **`n_iter`** is the number of iterations.

This function does exactly what’s described [above](#intuition-behind-gradient-descent): it takes a starting point (line 2), iteratively updates it according to the learning rate and the value of the gradient (lines 3 to 5), and finally returns the last position found.

Before you apply `gradient_descent()`, you can add another termination criterion:

```python
import numpy as np

def gradient_descent(
    gradient, start, learn_rate, n_iter=50, tolerance=1e-06
):
    vector = start
    for _ in range(n_iter):
        diff = -learn_rate * gradient(vector)
        if np.all(np.abs(diff) <= tolerance):
            break
        vector += diff
    return vector
```

You now have the additional parameter `tolerance` (line 4), which specifies the minimal allowed movement in each iteration. You’ve also defined the default values for `tolerance` and `n_iter`, so you don’t have to specify them each time you call `gradient_descent()`.

Lines 9 and 10 enable `gradient_descent()` to stop iterating and return the result before `n_iter` is reached if the vector update in the current iteration is less than or equal to `tolerance`. This often happens near the minimum, where gradients are usually very small. Unfortunately, it can also happen near a local minimum or a saddle point.

Line 9 uses the convenient NumPy functions [`numpy.all()`](https://numpy.org/doc/stable/reference/generated/numpy.all.html) and [`numpy.abs()`](https://numpy.org/doc/stable/reference/generated/numpy.absolute.html) to compare the absolute values of `diff` and `tolerance` in a single statement. That’s why you `import numpy` on line 1.

Now that you have the first version of `gradient_descent()`, it’s time to test your function. You’ll start with a small example and find the minimum of the function [𝐶 = 𝑣²](https://www.wolframalpha.com/input/?i=v**2).

This function has only one independent variable (𝑣), and its gradient is the derivative 2𝑣. It’s a differentiable [convex function](https://en.wikipedia.org/wiki/Convex_function), and the analytical way to find its minimum is straightforward. However, in practice, analytical differentiation can be difficult or even impossible and is often approximated with [numerical methods](https://en.wikipedia.org/wiki/Numerical_method).

You need only one statement to test your gradient descent implementation:

```
>>> gradient_descent(
...     gradient=lambda v: 2 * v, start=10.0, learn_rate=0.2
... )
2.210739197207331e-06
```

You use the [lambda function](https://realpython.com/python-lambda/) `lambda v: 2 * v` to provide the gradient of 𝑣². You start from the value `10.0` and set the learning rate to `0.2`. You get a result that’s very close to zero, which is the correct minimum.

The figure below shows the movement of the solution through the iterations:

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/gd-1.25c5ef2aed4e.png" style="zoom:67%;" />

You start from the rightmost green dot (𝑣 = 10) and move toward the minimum (𝑣 = 0). The updates are larger at first because the value of the gradient (and slope) is higher. As you approach the minimum, they become lower.

### Learning Rate Impact

The learning rate is a very important parameter of the algorithm. Different learning rate values can significantly affect the behavior of gradient descent. Consider the previous example, but with a learning rate of 0.8 instead of 0.2:

```
>>> gradient_descent(
...     gradient=lambda v: 2 * v, start=10.0, learn_rate=0.8
... )
-4.77519666596786e-07
```

You get another solution that’s very close to zero, but the internal behavior of the algorithm is different. This is what happens with the value of 𝑣 through the iterations:

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/gd-3.ff9f92989807.png" style="zoom:67%;" />

In this case, you again start with 𝑣 = 10, but because of the high learning rate, you get a large change in 𝑣 that passes to the other side of the optimum and becomes −6. It crosses zero a few more times before settling near it.

Small learning rates can result in very slow convergence. If the number of iterations is limited, then the algorithm may return before the minimum is found. Otherwise, the whole process might take an unacceptably large amount of time. To illustrate this, run `gradient_descent()` again, this time with a much smaller learning rate of 0.005:

```
>>> gradient_descent(
...     gradient=lambda v: 2 * v, start=10.0, learn_rate=0.005
... )
6.050060671375367
```

The result is now `6.05`, which is nowhere near the true minimum of zero. This is because the changes in the vector are very small due to the small learning rate:

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/gd-4.9a5c436570fd.png" style="zoom:67%;" />

The search process starts at 𝑣 = 10 as before, but it can’t reach zero in fifty iterations. However, with a hundred iterations, the error will be much smaller, and with a thousand iterations, you’ll be very close to zero:

```
>>> gradient_descent(
...     gradient=lambda v: 2 * v, start=10.0, learn_rate=0.005,
...     n_iter=100
... )
3.660323412732294
>>> gradient_descent(
...     gradient=lambda v: 2 * v, start=10.0, learn_rate=0.005,
...     n_iter=1000
... )
0.0004317124741065828
>>> gradient_descent(
...     gradient=lambda v: 2 * v, start=10.0, learn_rate=0.005,
...     n_iter=2000
... )
9.952518849647663e-05
```

Nonconvex functions might have local minima or saddle points where the algorithm can get trapped. In such situations, your choice of learning rate or starting point can make the difference between finding a local minimum and finding the global minimum.

Consider the function [𝑣⁴ - 5𝑣² - 3𝑣](https://www.wolframalpha.com/input/?i=v**4+-+5+*+v**2+-+3+*+v). It has a global minimum in 𝑣 ≈ 1.7 and a local minimum in 𝑣 ≈ −1.42. The gradient of this function is 4𝑣³ − 10𝑣 − 3. Let’s see how `gradient_descent()` works here:

```
>>> gradient_descent(
...     gradient=lambda v: 4 * v**3 - 10 * v - 3, start=0,
...     learn_rate=0.2
... )
-1.4207567437458342
```

You started at zero this time, and the algorithm ended near the local minimum. Here’s what happened under the hood:

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/gd-7.67e03e9337db.png" style="zoom:67%;" />

During the first two iterations, your vector was moving toward the global minimum, but then it crossed to the opposite side and stayed trapped in the local minimum. You can prevent this with a smaller learning rate:

```
>>> gradient_descent(
...     gradient=lambda v: 4 * v**3 - 10 * v - 3, start=0,
...     learn_rate=0.1
... )
1.285401330315467
```

When you decrease the learning rate from `0.2` to `0.1`, you get a solution very close to the global minimum. Remember that gradient descent is an approximate method. This time, you avoid the jump to the other side:

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/gd-8.f055cad0b634.png" style="zoom:67%;" />

A lower learning rate prevents the vector from making large jumps, and in this case, the vector remains closer to the global optimum.

Adjusting the learning rate is tricky. You can’t know the best value in advance. There are many techniques and heuristics that try to help with this. In addition, machine learning practitioners often tune the learning rate during model selection and evaluation.

Besides the learning rate, the starting point can affect the solution significantly, especially with nonconvex functions.

Application of the Gradient Descent Algorithm
----------------------------------------------------------------------------------------------------------------

In this section, you’ll see two short examples of using gradient descent. You’ll also learn that it can be used in real-life machine learning problems like linear regression. In the second case, you’ll need to modify the code of `gradient_descent()` because you need the data from the observations to calculate the gradient.

### Short Examples

First, you’ll apply `gradient_descent()` to another one-dimensional problem. Take the function [𝑣 − log(𝑣)](https://www.wolframalpha.com/input/?i=v+-+log%28v%29). The gradient of this function is 1 − 1/𝑣. With this information, you can find its minimum:

```
>>> gradient_descent(
...     gradient=lambda v: 1 - 1 / v, start=2.5, learn_rate=0.5
... )
1.0000011077232125
```

With the provided set of arguments, `gradient_descent()` correctly calculates that this function has the minimum in 𝑣 = 1. You can try it with other values for the learning rate and starting point.

You can also use `gradient_descent()` with functions of more than one variable. The application is the same, but you need to provide the gradient and starting points as vectors or arrays. For example, you can find the minimum of the function [𝑣₁² + 𝑣₂⁴](https://www.wolframalpha.com/input/?i=v_1**2+%2B+v_2**4) that has the gradient vector (2𝑣₁, 4𝑣₂³):

```
>>> gradient_descent(
...     gradient=lambda v: np.array([2 * v[0], 4 * v[1]**3]),
...     start=np.array([1.0, 1.0]), learn_rate=0.2, tolerance=1e-08
... )
array([8.08281277e-12, 9.75207120e-02])
```

In this case, your gradient function returns an array, and the start value is an array, so you get an array as the result. The resulting values are almost equal to zero, so you can say that `gradient_descent()` correctly found that the minimum of this function is at 𝑣₁ = 𝑣₂ = 0.

### Ordinary Least Squares

As you’ve already learned, linear regression and the [ordinary least squares method](https://en.wikipedia.org/wiki/Ordinary_least_squares) start with the observed values of the inputs 𝐱 = (𝑥₁, …, 𝑥ᵣ) and outputs 𝑦. They define a linear function 𝑓(𝐱) = 𝑏₀ + 𝑏₁𝑥₁ + ⋯ + 𝑏ᵣ𝑥ᵣ, which is as close as possible to 𝑦.

This is an optimization problem. It finds the values of weights 𝑏₀, 𝑏₁, …, 𝑏ᵣ that minimize the sum of squared residuals SSR = Σᵢ(𝑦ᵢ − 𝑓(𝐱ᵢ))² or the mean squared error MSE = SSR / 𝑛. Here, 𝑛 is the total number of observations and 𝑖 = 1, …, 𝑛.

You can also use the cost function 𝐶 = SSR / (2𝑛), which is mathematically more convenient than SSR or MSE.

The most basic form of linear regression is [simple linear regression](https://realpython.com/linear-regression-in-python/#simple-linear-regression). It has only one set of inputs 𝑥 and two weights: 𝑏₀ and 𝑏₁. The equation of the regression line is 𝑓(𝑥) = 𝑏₀ + 𝑏₁𝑥. Although the optimal values of 𝑏₀ and 𝑏₁ can be [calculated analytically](https://en.wikipedia.org/wiki/Ordinary_least_squares#Simple_linear_regression_model), you’ll use gradient descent to determine them.

First, you need calculus to find the gradient of the cost function 𝐶 = Σᵢ(𝑦ᵢ − 𝑏₀ − 𝑏₁𝑥ᵢ)² / (2𝑛). Since you have two decision variables, 𝑏₀ and 𝑏₁, the gradient ∇𝐶 is a vector with two components:

1.  ∂𝐶/∂𝑏₀ = (1/𝑛) Σᵢ(𝑏₀ + 𝑏₁𝑥ᵢ − 𝑦ᵢ) = mean(𝑏₀ + 𝑏₁𝑥ᵢ − 𝑦ᵢ)
2.  ∂𝐶/∂𝑏₁ = (1/𝑛) Σᵢ(𝑏₀ + 𝑏₁𝑥ᵢ − 𝑦ᵢ) 𝑥ᵢ = mean((𝑏₀ + 𝑏₁𝑥ᵢ − 𝑦ᵢ) 𝑥ᵢ)

You need the values of 𝑥 and 𝑦 to calculate the gradient of this cost function. Your gradient function will have as inputs not only 𝑏₀ and 𝑏₁ but also 𝑥 and 𝑦. This is how it might look:

```python
def ssr_gradient(x, y, b):
    res = b[0] + b[1] * x - y
    return res.mean(), (res * x).mean()  # .mean() is a method of np.ndarray
```

`ssr_gradient()` takes the arrays `x` and `y`, which contain the observation inputs and outputs, and the array `b` that holds the current values of the decision variables 𝑏₀ and 𝑏₁. This function first calculates the array of the residuals for each observation (`res`) and then returns the pair of values of ∂𝐶/∂𝑏₀ and ∂𝐶/∂𝑏₁.

In this example, you can use the convenient NumPy method [`ndarray.mean()`](https://numpy.org/doc/stable/reference/generated/numpy.ndarray.mean.html) since you pass NumPy arrays as the arguments.

`gradient_descent()` needs two small adjustments:

1.  Add `x` and `y` as the parameters of `gradient_descent()` on line 4.
2.  Provide `x` and `y` to the gradient function and make sure you convert your gradient tuple to a NumPy array on line 8.

Here’s how `gradient_descent()` looks after these changes:

```python
import numpy as np

def gradient_descent(
    gradient, x, y, start, learn_rate=0.1, n_iter=50, tolerance=1e-06
):
    vector = start
    for _ in range(n_iter):
        diff = -learn_rate * np.array(gradient(x, y, vector))
        if np.all(np.abs(diff) <= tolerance):
            break
        vector += diff
    return vector
```

`gradient_descent()` now accepts the observation inputs `x` and outputs `y` and can use them to calculate the gradient. Converting the output of `gradient(x, y, vector)` to a NumPy array enables elementwise multiplication of the gradient elements by the learning rate, which isn’t necessary in the case of a single-variable function.

Now apply your new version of `gradient_descent()` to find the regression line for some arbitrary values of `x` and `y`:

```
>>> x = np.array([5, 15, 25, 35, 45, 55])
>>> y = np.array([5, 20, 14, 32, 22, 38])

>>> gradient_descent(
...     ssr_gradient, x, y, start=[0.5, 0.5], learn_rate=0.0008,
...     n_iter=100_000
... )
array([5.62822349, 0.54012867])
```

The result is an array with two values that correspond to the decision variables: 𝑏₀ = 5.63 and 𝑏₁ = 0.54. The best regression line is 𝑓(𝑥) = 5.63 + 0.54𝑥. As in the previous examples, this result heavily depends on the learning rate. You might not get such a good result with too low or too high of a learning rate.

This example isn’t entirely random–it’s taken from the tutorial [Linear Regression in Python](https://realpython.com/linear-regression-in-python/#simple-linear-regression-with-scikit-learn). The good news is that you’ve obtained almost the same result as the [linear regressor from scikit-learn](https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LinearRegression.html). The data and regression results are visualized in the section [Simple Linear Regression](https://realpython.com/linear-regression-in-python/#simple-linear-regression).

### Improvement of the Code

You can make `gradient_descent()` more robust, comprehensive, and better-looking without modifying its core functionality:

```python
import numpy as np

def gradient_descent(
    gradient, x, y, start, learn_rate=0.1, n_iter=50, tolerance=1e-06,
    dtype="float64"
):
    # Checking if the gradient is callable
    if not callable(gradient):
        raise TypeError("'gradient' must be callable")

    # Setting up the data type for NumPy arrays
    dtype_ = np.dtype(dtype)

    # Converting x and y to NumPy arrays
    x, y = np.array(x, dtype=dtype_), np.array(y, dtype=dtype_)
    if x.shape[0] != y.shape[0]:
        raise ValueError("'x' and 'y' lengths do not match")

    # Initializing the values of the variables
    vector = np.array(start, dtype=dtype_)

    # Setting up and checking the learning rate
    learn_rate = np.array(learn_rate, dtype=dtype_)
    if np.any(learn_rate <= 0):
        raise ValueError("'learn_rate' must be greater than zero")

    # Setting up and checking the maximal number of iterations
    n_iter = int(n_iter)
    if n_iter <= 0:
        raise ValueError("'n_iter' must be greater than zero")

    # Setting up and checking the tolerance
    tolerance = np.array(tolerance, dtype=dtype_)
    if np.any(tolerance <= 0):
        raise ValueError("'tolerance' must be greater than zero")

    # Performing the gradient descent loop
    for _ in range(n_iter):
        # Recalculating the difference
        diff = -learn_rate * np.array(gradient(x, y, vector), dtype_)

        # Checking if the absolute difference is small enough
        if np.all(np.abs(diff) <= tolerance):
            break

        # Updating the values of the variables
        vector += diff

    return vector if vector.shape else vector.item()
```

`gradient_descent()` now accepts an additional `dtype` parameter that defines the data type of NumPy arrays inside the function. For more information about NumPy types, see the [official documentation on data types](https://numpy.org/doc/stable/user/basics.types.html).

In most applications, you won’t notice a difference between 32-bit and 64-bit floating-point numbers, but when you work with big datasets, this might significantly affect memory use and maybe even [processing speed](https://stackoverflow.com/questions/15340781/python-numpy-data-types-performance). For example, although NumPy uses 64-bit floats by default, [TensorFlow often uses 32-bit decimal numbers](https://www.tensorflow.org/guide/tensor).

In addition to considering data types, the code above introduces a few modifications related to type checking and ensuring the use of NumPy capabilities:

*   **Lines 8 and 9** check if `gradient` is a Python callable object and whether it can be used as a function. If not, then the function will raise a [`TypeError`](https://docs.python.org/3/library/exceptions.html#TypeError).
    
*   **Line 12** sets an instance of [`numpy.dtype`](https://numpy.org/doc/stable/reference/generated/numpy.dtype.html), which will be used as the data type for all arrays throughout the function.
    
*   **Line 15** takes the arguments `x` and `y` and produces NumPy arrays with the desired data type. The arguments `x` and `y` can be lists, tuples, arrays, or other sequences.
    
*   **Lines 16 and 17** compare the sizes of `x` and `y`. This is useful because you want to be sure that both arrays have the same number of observations. If they don’t, then the function will raise a [`ValueError`](https://docs.python.org/3/library/exceptions.html#ValueError).
    
*   **Line 20** converts the argument `start` to a NumPy array. This is an interesting trick: if `start` is a Python scalar, then it’ll be transformed into a corresponding NumPy object (an array with one item and zero dimensions). If you pass a sequence, then it’ll become a regular NumPy array with the same number of elements.
    
*   **Line 23** does the same thing with the learning rate. This can be very useful because it enables you to specify different learning rates for each decision variable by passing a list, tuple, or NumPy array to `gradient_descent()`.
    
*   **Lines 24 and 25** check if the learning rate value (or values for all variables) is greater than zero.
    
*   **Lines 28 to 35** similarly set `n_iter` and `tolerance` and check that they are greater than zero.
    
*   **Lines 38 to 47** are almost the same as before. The only difference is the type of the gradient array on line 40.
    
*   **Line 49** conveniently returns the resulting array if you have several decision variables or a Python scalar if you have a single variable.
    

Your `gradient_descent()` is now finished. Feel free to add some additional capabilities or polishing. The next step of this tutorial is to use what you’ve learned so far to implement the stochastic version of gradient descent.

Stochastic Gradient Descent Algorithms
--------------------------------------------------------------------------------------------------

**Stochastic gradient descent algorithms** are a modification of gradient descent. In stochastic gradient descent, you calculate the gradient using just a random small part of the observations instead of all of them. In some cases, this approach can reduce computation time.

**Online stochastic gradient descent** is a variant of stochastic gradient descent in which you estimate the gradient of the cost function for each observation and update the decision variables accordingly. This can help you find the global minimum, especially if the objective function is convex.

**Batch stochastic gradient descent** is somewhere between ordinary gradient descent and the online method. The gradients are calculated and the decision variables are updated iteratively with subsets of all observations, called **minibatches**. This variant is very popular for training neural networks.

You can imagine the online algorithm as a special kind of batch algorithm in which each minibatch has only one observation. Classical gradient descent is another special case in which there’s only one batch containing all observations.

### Minibatches in Stochastic Gradient Descent

As in the case of the ordinary gradient descent, stochastic gradient descent starts with an initial vector of decision variables and updates it through several iterations. The difference between the two is in what happens inside the iterations:

*   Stochastic gradient descent randomly divides the set of observations into minibatches.
*   For each minibatch, the gradient is computed and the vector is moved.
*   Once all minibatches are used, you say that the iteration, or **epoch**, is finished and start the next one.

This algorithm randomly selects observations for minibatches, so you need to simulate this random (or pseudorandom) behavior. You can do that with [random number generation](https://realpython.com/python-random/). Python has the built-in [`random`](https://docs.python.org/3/library/random.html) module, and NumPy has its own [random generator](https://numpy.org/doc/stable/reference/random/generator.html). The latter is more convenient when you work with arrays.

You’ll create a new function called `sgd()` that is very similar to `gradient_descent()` but uses randomly selected minibatches to move along the search space:

```python
import numpy as np

def sgd(
    gradient, x, y, start, learn_rate=0.1, batch_size=1, n_iter=50,
    tolerance=1e-06, dtype="float64", random_state=None
):
    # Checking if the gradient is callable
    if not callable(gradient):
        raise TypeError("'gradient' must be callable")

    # Setting up the data type for NumPy arrays
    dtype_ = np.dtype(dtype)

    # Converting x and y to NumPy arrays
    x, y = np.array(x, dtype=dtype_), np.array(y, dtype=dtype_)
    n_obs = x.shape[0]
    if n_obs != y.shape[0]:
        raise ValueError("'x' and 'y' lengths do not match")
    xy = np.c_[x.reshape(n_obs, -1), y.reshape(n_obs, 1)]

    # Initializing the random number generator
    seed = None if random_state is None else int(random_state)
    rng = np.random.default_rng(seed=seed)

    # Initializing the values of the variables
    vector = np.array(start, dtype=dtype_)

    # Setting up and checking the learning rate
    learn_rate = np.array(learn_rate, dtype=dtype_)
    if np.any(learn_rate <= 0):
        raise ValueError("'learn_rate' must be greater than zero")

    # Setting up and checking the size of minibatches
    batch_size = int(batch_size)
    if not 0 < batch_size <= n_obs:
        raise ValueError(
            "'batch_size' must be greater than zero and less than "
            "or equal to the number of observations"
        )

    # Setting up and checking the maximal number of iterations
    n_iter = int(n_iter)
    if n_iter <= 0:
        raise ValueError("'n_iter' must be greater than zero")

    # Setting up and checking the tolerance
    tolerance = np.array(tolerance, dtype=dtype_)
    if np.any(tolerance <= 0):
        raise ValueError("'tolerance' must be greater than zero")

    # Performing the gradient descent loop
    for _ in range(n_iter):
        # Shuffle x and y
        rng.shuffle(xy)

        # Performing minibatch moves
        for start in range(0, n_obs, batch_size):
            stop = start + batch_size
            x_batch, y_batch = xy[start:stop, :-1], xy[start:stop, -1:]

            # Recalculating the difference
            grad = np.array(gradient(x_batch, y_batch, vector), dtype_)
            diff = -learn_rate * grad

            # Checking if the absolute difference is small enough
            if np.all(np.abs(diff) <= tolerance):
                break

            # Updating the values of the variables
            vector += diff

    return vector if vector.shape else vector.item()
```

You have a new parameter here. With `batch_size`, you specify the number of observations in each minibatch. This is an essential parameter for stochastic gradient descent that can significantly affect performance. Lines 34 to 39 ensure that `batch_size` is a positive integer no larger than the total number of observations.

Another new parameter is `random_state`. It defines the seed of the random number generator on line 22. The seed is used on line 23 as an argument to [`default_rng()`](https://numpy.org/doc/stable/reference/random/generator.html#numpy.random.default_rng), which creates an instance of [`Generator`](https://numpy.org/doc/stable/reference/random/generator.html#numpy.random.Generator).

If you pass the argument [`None`](https://realpython.com/null-in-python/) for `random_state`, then the random number generator will return different numbers each time it’s instantiated. If you want each instance of the generator to behave exactly the same way, then you need to specify `seed`. The easiest way is to provide an arbitrary integer.

Line 16 deduces the number of observations with `x.shape[0]`. If `x` is a one-dimensional array, then this is its size. If `x` has two dimensions, then `.shape[0]` is the number of rows.

On line 19, you use [`.reshape()`](https://numpy.org/doc/stable/reference/generated/numpy.ndarray.reshape.html#numpy.ndarray.reshape) to make sure that both `x` and `y` become two-dimensional arrays with `n_obs` rows and that `y` has exactly one column. [`numpy.c_[]`](https://numpy.org/doc/stable/reference/generated/numpy.ndarray.reshape.html#numpy.ndarray.reshape) conveniently concatenates the columns of `x` and `y` into a single array, `xy`. This is one way to make data suitable for random selection.

Finally, on lines 52 to 70, you implement the [`for` loop](https://realpython.com/python-for-loop/) for the stochastic gradient descent. It differs from `gradient_descent()`. On line 54, you use the random number generator and its method [`.shuffle()`](https://numpy.org/doc/stable/reference/random/generated/numpy.random.Generator.shuffle.html#numpy.random.Generator.shuffle) to shuffle the observations. This is one of the ways to choose minibatches randomly.

The inner `for` loop is repeated for each minibatch. The main difference from the ordinary gradient descent is that, on line 62, the gradient is calculated for the observations from a minibatch (`x_batch` and `y_batch`) instead of for all observations (`x` and `y`).

On line 59, `x_batch` becomes a part of `xy` that contains the rows of the current minibatch (from `start` to `stop`) and the columns that correspond to `x`. `y_batch` holds the same rows from `xy` but only the last column (the outputs). For more information about how indices work in NumPy, see the [official documentation on indexing](https://numpy.org/doc/stable/reference/arrays.indexing.html).

Now you can test your implementation of stochastic gradient descent:

```
>>> sgd(
...     ssr_gradient, x, y, start=[0.5, 0.5], learn_rate=0.0008,
...     batch_size=3, n_iter=100_000, random_state=0
... )
array([5.63093736, 0.53982921])
```

The result is almost the same as you got with `gradient_descent()`. If you omit `random_state` or use `None`, then you’ll get somewhat different results each time you run `sgd()` because the random number generator will shuffle `xy` differently.

### Momentum in Stochastic Gradient Descent

As you’ve already seen, the learning rate can have a significant impact on the result of gradient descent. You can use several different strategies for adapting the learning rate during the algorithm execution. You can also apply **momentum** to your algorithm.

You can use momentum to correct the effect of the learning rate. The idea is to remember the previous update of the vector and apply it when calculating the next one. You don’t move the vector exactly in the direction of the negative gradient, but you also tend to keep the direction and magnitude from the previous move.

The parameter called the **decay rate** or **decay factor** defines how strong the contribution of the previous update is. To include the momentum and the decay rate, you can modify `sgd()` by adding the parameter `decay_rate` and use it to calculate the direction and magnitude of the vector update (`diff`):

```python
import numpy as np

def sgd(
    gradient, x, y, start, learn_rate=0.1, decay_rate=0.0, batch_size=1,
    n_iter=50, tolerance=1e-06, dtype="float64", random_state=None
):
    # Checking if the gradient is callable
    if not callable(gradient):
        raise TypeError("'gradient' must be callable")

    # Setting up the data type for NumPy arrays
    dtype_ = np.dtype(dtype)

    # Converting x and y to NumPy arrays
    x, y = np.array(x, dtype=dtype_), np.array(y, dtype=dtype_)
    n_obs = x.shape[0]
    if n_obs != y.shape[0]:
        raise ValueError("'x' and 'y' lengths do not match")
    xy = np.c_[x.reshape(n_obs, -1), y.reshape(n_obs, 1)]

    # Initializing the random number generator
    seed = None if random_state is None else int(random_state)
    rng = np.random.default_rng(seed=seed)

    # Initializing the values of the variables
    vector = np.array(start, dtype=dtype_)

    # Setting up and checking the learning rate
    learn_rate = np.array(learn_rate, dtype=dtype_)
    if np.any(learn_rate <= 0):
        raise ValueError("'learn_rate' must be greater than zero")

    # Setting up and checking the decay rate
    decay_rate = np.array(decay_rate, dtype=dtype_)
    if np.any(decay_rate < 0) or np.any(decay_rate > 1):
        raise ValueError("'decay_rate' must be between zero and one")

    # Setting up and checking the size of minibatches
    batch_size = int(batch_size)
    if not 0 < batch_size <= n_obs:
        raise ValueError(
            "'batch_size' must be greater than zero and less than "
            "or equal to the number of observations"
        )

    # Setting up and checking the maximal number of iterations
    n_iter = int(n_iter)
    if n_iter <= 0:
        raise ValueError("'n_iter' must be greater than zero")

    # Setting up and checking the tolerance
    tolerance = np.array(tolerance, dtype=dtype_)
    if np.any(tolerance <= 0):
        raise ValueError("'tolerance' must be greater than zero")

    # Setting the difference to zero for the first iteration
    diff = 0

    # Performing the gradient descent loop
    for _ in range(n_iter):
        # Shuffle x and y
        rng.shuffle(xy)

        # Performing minibatch moves
        for start in range(0, n_obs, batch_size):
            stop = start + batch_size
            x_batch, y_batch = xy[start:stop, :-1], xy[start:stop, -1:]

            # Recalculating the difference
            grad = np.array(gradient(x_batch, y_batch, vector), dtype_)
            diff = decay_rate * diff - learn_rate * grad

            # Checking if the absolute difference is small enough
            if np.all(np.abs(diff) <= tolerance):
                break

            # Updating the values of the variables
            vector += diff

    return vector if vector.shape else vector.item()
```

In this implementation, you add the `decay_rate` parameter on line 4, convert it to a NumPy array of the desired type on line 34, and check if it’s between zero and one on lines 35 and 36. On line 57, you initialize `diff` before the iterations start to ensure that it’s available in the first iteration.

The most important change happens on line 71. You recalculate `diff` with the learning rate and gradient but also add the product of the decay rate and the old value of `diff`. Now `diff` has two components:

1.  **`decay_rate * diff`** is the momentum, or impact of the previous move.
2.  **`-learn_rate * grad`** is the impact of the current gradient.

The decay and learning rates serve as the weights that define the contributions of the two.

### Random Start Values

As opposed to ordinary gradient descent, the starting point is often not so important for stochastic gradient descent. It may also be an unnecessary difficulty for a user, especially when you have many decision variables. To get an idea, just imagine if you needed to manually initialize the values for a neural network with thousands of biases and weights!

In practice, you can start with some small arbitrary values. You’ll use the random number generator to get them:

```python
import numpy as np

def sgd(
    gradient, x, y, n_vars=None, start=None, learn_rate=0.1,
    decay_rate=0.0, batch_size=1, n_iter=50, tolerance=1e-06,
    dtype="float64", random_state=None
):
    # Checking if the gradient is callable
    if not callable(gradient):
        raise TypeError("'gradient' must be callable")

    # Setting up the data type for NumPy arrays
    dtype_ = np.dtype(dtype)

    # Converting x and y to NumPy arrays
    x, y = np.array(x, dtype=dtype_), np.array(y, dtype=dtype_)
    n_obs = x.shape[0]
    if n_obs != y.shape[0]:
        raise ValueError("'x' and 'y' lengths do not match")
    xy = np.c_[x.reshape(n_obs, -1), y.reshape(n_obs, 1)]

    # Initializing the random number generator
    seed = None if random_state is None else int(random_state)
    rng = np.random.default_rng(seed=seed)

    # Initializing the values of the variables
    vector = (
        rng.normal(size=int(n_vars)).astype(dtype_)
        if start is None else
        np.array(start, dtype=dtype_)
    )

    # Setting up and checking the learning rate
    learn_rate = np.array(learn_rate, dtype=dtype_)
    if np.any(learn_rate <= 0):
        raise ValueError("'learn_rate' must be greater than zero")

    # Setting up and checking the decay rate
    decay_rate = np.array(decay_rate, dtype=dtype_)
    if np.any(decay_rate < 0) or np.any(decay_rate > 1):
        raise ValueError("'decay_rate' must be between zero and one")

    # Setting up and checking the size of minibatches
    batch_size = int(batch_size)
    if not 0 < batch_size <= n_obs:
        raise ValueError(
            "'batch_size' must be greater than zero and less than "
            "or equal to the number of observations"
        )

    # Setting up and checking the maximal number of iterations
    n_iter = int(n_iter)
    if n_iter <= 0:
        raise ValueError("'n_iter' must be greater than zero")

    # Setting up and checking the tolerance
    tolerance = np.array(tolerance, dtype=dtype_)
    if np.any(tolerance <= 0):
        raise ValueError("'tolerance' must be greater than zero")

    # Setting the difference to zero for the first iteration
    diff = 0

    # Performing the gradient descent loop
    for _ in range(n_iter):
        # Shuffle x and y
        rng.shuffle(xy)

        # Performing minibatch moves
        for start in range(0, n_obs, batch_size):
            stop = start + batch_size
            x_batch, y_batch = xy[start:stop, :-1], xy[start:stop, -1:]

            # Recalculating the difference
            grad = np.array(gradient(x_batch, y_batch, vector), dtype_)
            diff = decay_rate * diff - learn_rate * grad

            # Checking if the absolute difference is small enough
            if np.all(np.abs(diff) <= tolerance):
                break

            # Updating the values of the variables
            vector += diff

    return vector if vector.shape else vector.item()
```

You now have the new parameter `n_vars` that defines the number of decision variables in your problem. The parameter `start` is optional and has the default value `None`. Lines 27 to 31 initialize the starting values of the decision variables:

*   If you provide a `start` value other than `None`, then it’s used for the starting values.
*   If `start` is `None`, then your random number generator creates the starting values using the [standard normal distribution](https://en.wikipedia.org/wiki/Normal_distribution#Standard_normal_distribution) and the NumPy method [`.normal()`](https://numpy.org/doc/stable/reference/random/generated/numpy.random.Generator.normal.html).

Now give `sgd()` a try:

```
>>> sgd(
...     ssr_gradient, x, y, n_vars=2, learn_rate=0.0001,
...     decay_rate=0.8, batch_size=3, n_iter=100_000, random_state=0
... )
array([5.63014443, 0.53901017])
```

You get similar results again.

You’ve learned how to write the functions that implement gradient descent and stochastic gradient descent. The code above can be made more robust and polished. You can also find different implementations of these methods in well-known machine learning libraries.

Gradient Descent in Keras and TensorFlow
------------------------------------------------------------------------------------------------------

Stochastic gradient descent is widely used to train neural networks. The libraries for neural networks often have different variants of optimization algorithms based on stochastic gradient descent, such as:

*   Adam
*   Adagrad
*   Adadelta
*   RMSProp

These optimization libraries are usually called internally when neural network software is trained. However, you can use them independently as well:

```
>>> import tensorflow as tf

>>> # Create needed objects
>>> sgd = tf.keras.optimizers.SGD(learning_rate=0.1, momentum=0.9)
>>> var = tf.Variable(2.5)
>>> cost = lambda: 2 + var ** 2

>>> # Perform optimization
>>> for _ in range(100):
...     sgd.minimize(cost, var_list=[var])

>>> # Extract results
>>> var.numpy()
-0.007128528
>>> cost().numpy()
2.0000508
```

In this example, you first import `tensorflow` and then create the object needed for optimization:

*   **`sgd`** is an instance of the stochastic gradient descent optimizer with a learning rate of `0.1` and a momentum of `0.9`.
*   **`var`** is an instance of the decision variable with an initial value of `2.5`.
*   **`cost`** is the cost function, which is a square function in this case.

The main part of the code is a `for` loop that iteratively calls `.minimize()` and modifies `var` and `cost`. Once the loop is exhausted, you can get the values of the decision variable and the cost function with `.numpy()`.

You can find more information on these algorithms in the [Keras](https://keras.io/api/optimizers/) and [TensorFlow](https://www.tensorflow.org/api_docs/python/tf/keras/optimizers/) documentation. The article [An overview of gradient descent optimization algorithms](https://ruder.io/optimizing-gradient-descent/) offers a comprehensive list with explanations of gradient descent variants.

Conclusion
------------------------------------------

You now know what **gradient descent** and **stochastic gradient descent** algorithms are and how they work. They’re widely used in the applications of artificial neural networks and are implemented in popular libraries like Keras and TensorFlow.

**In this tutorial, you’ve learned:**

*   How to **write your own functions** for gradient descent and stochastic gradient descent
*   How to **apply your functions** to solve optimization problems
*   What the **key features and concepts** of gradient descent are, like learning rate or momentum, as well as its limitations

You’ve used gradient descent and stochastic gradient descent to find the minima of several functions and to fit the regression line in a linear regression problem. You’ve also seen how to apply the class `SGD` from TensorFlow that’s used to train neural networks.

If you have questions or comments, then please put them in the comment section below.

> [realpython.com/gradient-descent-algorithm-python](https://realpython.com/gradient-descent-algorithm-python/)