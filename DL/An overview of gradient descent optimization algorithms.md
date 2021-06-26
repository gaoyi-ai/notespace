---
title: An overview of gradient descent optimization algorithms
categories:
- DL
- Optimization
tags:
- gradient descent
date: 2021/6/25
---



> [OPTIMIZATION](https://ruder.io/tag/optimization/index.html)

# An overview of gradient descent optimization algorithms

Gradient descent is the preferred way to optimize neural networks and many other machine learning algorithms but is often used as a black box. This post explores how many of the most popular gradient-based optimization algorithms such as Momentum, Adagrad, and Adam actually work.
虽然梯度下降优化算法越来越受欢迎，但通常作为黑盒优化器使用，因此很难对其优点和缺点的进行实际的解释。本文旨在让读者对不同的算法有直观的认识，以帮助读者使用这些算法。在本综述中，我们介绍梯度下降的不同变形形式，总结这些算法面临的挑战，介绍最常用的优化算法，回顾并行和分布式架构，以及调研用于优化梯度下降的其他的策略。

![An overview of gradient descent optimization algorithms](https://ruder.io/content/images/size/w2000/2016/09/loss_function_image_tumblr.png)

This post explores how many of the most popular gradient-based optimization algorithms actually work.

Table of contents:

> - Gradient descent variants
>     - [Batch gradient descent](https://ruder.io/optimizing-gradient-descent/index.html#batchgradientdescent)
>     - [Stochastic gradient descent](https://ruder.io/optimizing-gradient-descent/index.html#stochasticgradientdescent)
>     - [Mini-batch gradient descent](https://ruder.io/optimizing-gradient-descent/index.html#minibatchgradientdescent)
> - [Challenges](https://ruder.io/optimizing-gradient-descent/index.html#challenges)
> - Gradient descent optimization algorithms
>     - [Momentum](https://ruder.io/optimizing-gradient-descent/index.html#momentum)
>     - [Nesterov accelerated gradient](https://ruder.io/optimizing-gradient-descent/index.html#nesterovacceleratedgradient)
>     - [Adagrad](https://ruder.io/optimizing-gradient-descent/index.html#adagrad)
>     - [Adadelta](https://ruder.io/optimizing-gradient-descent/index.html#adadelta)
>     - [RMSprop](https://ruder.io/optimizing-gradient-descent/index.html#rmsprop)
>     - [Adam](https://ruder.io/optimizing-gradient-descent/index.html#adam)
>     - [AdaMax](https://ruder.io/optimizing-gradient-descent/index.html#adamax)
>     - [Nadam](https://ruder.io/optimizing-gradient-descent/index.html#nadam)
>     - [AMSGrad](https://ruder.io/optimizing-gradient-descent/index.html#amsgrad)
>     - [Other recent optimizers](https://ruder.io/optimizing-gradient-descent/index.html#otherrecentoptimizers)
>     - [Visualization of algorithms](https://ruder.io/optimizing-gradient-descent/index.html#visualizationofalgorithms)
>     - [Which optimizer to use?](https://ruder.io/optimizing-gradient-descent/index.html#whichoptimizertouse)
> - Parallelizing and distributing SGD
>     - [Hogwild!](https://ruder.io/optimizing-gradient-descent/index.html#hogwild)
>     - [Downpour SGD](https://ruder.io/optimizing-gradient-descent/index.html#downpoursgd)
>     - [Delay-tolerant Algorithms for SGD](https://ruder.io/optimizing-gradient-descent/index.html#delaytolerantalgorithmsforsgd)
>     - [TensorFlow](https://ruder.io/optimizing-gradient-descent/index.html#tensorflow)
>     - [Elastic Averaging SGD](https://ruder.io/optimizing-gradient-descent/index.html#elasticaveragingsgd)
> - Additional strategies for optimizing SGD
>     - [Shuffling and Curriculum Learning](https://ruder.io/optimizing-gradient-descent/index.html#shufflingandcurriculumlearning)
>     - [Batch normalization](https://ruder.io/optimizing-gradient-descent/index.html#batchnormalization)
>     - [Early Stopping](https://ruder.io/optimizing-gradient-descent/index.html#earlystopping)
>     - [Gradient noise](https://ruder.io/optimizing-gradient-descent/index.html#gradientnoise)
> - [Conclusion](https://ruder.io/optimizing-gradient-descent/index.html#conclusion)
> - [References](https://ruder.io/optimizing-gradient-descent/index.html#references)

Gradient descent is one of the most popular algorithms to perform optimization and by far the most common way to optimize neural networks. At the same time, every state-of-the-art Deep Learning library contains implementations of various algorithms to optimize gradient descent (e.g. [lasagne's](https://lasagne.readthedocs.org/en/latest/modules/updates.html), [caffe's](http://caffe.berkeleyvision.org/tutorial/solver.html), and [keras'](http://keras.io/optimizers/) documentation). These algorithms, however, are often used as black-box optimizers, as practical explanations of their strengths and weaknesses are hard to come by.
梯度下降法是最著名的优化算法之一，也是迄今优化神经网络时最常用的方法。同时，在每一个最新的深度学习库中都包含了各种优化的梯度下降法的实现（例如：参见 [lasagne](http://lasagne.readthedocs.org/en/latest/modules/updates.html)，[caffe](http://caffe.berkeleyvision.org/tutorial/solver.html) 和 [keras](http://keras.io/optimizers/) 的文档）。然而，这些算法通常是作为黑盒优化器使用，因此，很难对其优点和缺点的进行实际的解释。

This blog post aims at providing you with intuitions towards the behaviour of different algorithms for optimizing gradient descent that will help you put them to use. We are first going to look at the different variants of gradient descent. We will then briefly summarize challenges during training. Subsequently, we will introduce the most common optimization algorithms by showing their motivation to resolve these challenges and how this leads to the derivation of their update rules. We will also take a short look at algorithms and architectures to optimize gradient descent in a parallel and distributed setting. Finally, we will consider additional strategies that are helpful for optimizing gradient descent.

Gradient descent is a way to minimize an objective function $J(\theta)$ parameterized by a model's parameters $\theta \in \mathbb{R}^d$ by updating the parameters in the opposite direction of the gradient of the objective function $\nabla_\theta J(\theta)$ w.r.t. to the parameters. The learning rate $\eta$ determines the size of the steps we take to reach a (local) minimum. In other words, we follow the direction of the slope of the surface created by the objective function downhill until we reach a valley. If you are unfamiliar with gradient descent, you can find a good introduction on optimizing neural networks [here](https://cs231n.github.io/optimization-1/).
梯度下降法是最小化目标函数 $J(\theta)$的一种方法，其中，$\theta \in \mathbb{R}^d$为模型参数，梯度下降法利用目标函数关于参数的梯度$\nabla_\theta J(\theta)$的反方向更新参数。学习率 $\eta$ 决定达到最小值或者局部最小值过程中所采用的步长的大小。即，我们沿着目标函数的斜面下降的方向，直到到达谷底。如果你对梯度下降法不熟悉，你可以从 [cs231n/optimization-1/](http://cs231n.github.io/optimization-1/) 找到介绍神经网络优化的材料。

# Gradient descent variants

There are three variants of gradient descent, which differ in how much data we use to compute the gradient of the objective function. Depending on the amount of data, we make a trade-off between the accuracy of the parameter update and the time it takes to perform an update.
梯度下降法有 3 中变形形式，它们之间的区别为我们在计算目标函数的梯度时使用到多少数据。根据数据量的不同，我们在参数更新的精度和更新过程中所需要的时间两个方面做出权衡。

## Batch gradient descent

Vanilla gradient descent, aka batch gradient descent, computes the gradient of the cost function w.r.t. to the parameters $\theta$ for the entire training dataset:
Vanilla 梯度下降法，又称为批梯度下降法（batch gradient descent），在整个训练数据集上计算损失函数关于参数$\theta$的梯度：

$\theta = \theta - \eta \cdot \nabla_\theta J( \theta).$

As we need to calculate the gradients for the whole dataset to perform just *one* update, batch gradient descent can be very slow and is intractable for datasets that don't fit in memory. Batch gradient descent also doesn't allow us to update our model *online*, i.e. with new examples on-the-fly.
因为在执行每次更新时，我们需要在整个数据集上计算所有的梯度，所以批梯度下降法的速度会很慢，同时，批梯度下降法无法处理超出内存容量限制的数据集。批梯度下降法同样也不能在线更新模型，即在运行的过程中，不能增加新的样本。

In code, batch gradient descent looks something like this:

```python
for i in range(nb_epochs):
	params_grad = evaluate_gradient(loss_function, data, params)
	params = params - learning_rate * params_grad
```

For a pre-defined number of epochs, we first compute the gradient vector `params_grad` of the loss function for the whole dataset w.r.t. our parameter vector `params`. Note that state-of-the-art deep learning libraries provide automatic differentiation that efficiently computes the gradient w.r.t. some parameters. If you derive the gradients yourself, then gradient checking is a good idea. (See [here](https://cs231n.github.io/neural-networks-3/) for some great tips on how to check gradients properly.)
对于给定的迭代次数，首先，我们利用全部数据集计算损失函数关于参数向量`params`的梯度向量`params_grad`。注意，最新的深度学习库中提供了自动求导的功能，可以有效地计算关于参数梯度。如果你自己求梯度，那么，梯度检查是一个不错的主意（关于如何正确检查梯度的一些技巧可以参见 [cs231n/neural-networks-3/](http://cs231n.github.io/neural-networks-3/)）。

We then update our parameters in the opposite direction of the gradients with the learning rate determining how big of an update we perform. Batch gradient descent is guaranteed to converge to the global minimum for convex error surfaces and to a local minimum for non-convex surfaces.
然后，我们利用梯度的方向和学习率更新参数，学习率决定我们将以多大的步长更新参数。对于凸误差函数，批梯度下降法能够保证收敛到全局最小值，对于非凸函数，则收敛到一个局部最小值。

## Stochastic gradient descent

Stochastic gradient descent (SGD) in contrast performs a parameter update for *each* training example $x^{(i)}$ and label $y^{(i)}$:
相反，随机梯度下降法（stochastic gradient descent, SGD）根据每一条训练样本 $x^{(i)}$和标签 $y^{(i)}$更新参数：

$\theta = \theta - \eta \cdot \nabla_\theta J( \theta; x^{(i)}; y^{(i)}).$

Batch gradient descent performs redundant computations for large datasets, as it recomputes gradients for similar examples before each parameter update. SGD does away with this redundancy by performing one update at a time. It is therefore usually much faster and can also be used to learn online.
对于大数据集，因为批梯度下降法在每一个参数更新之前，会对相似的样本计算梯度，所以在计算过程中会有冗余。而 SGD 在每一次更新中只执行一次，从而消除了冗余。因而，通常 SGD 的运行速度更快，同时，可以用于在线学习。SGD 以高方差频繁地更新，导致目标函数出现如图 1 所示的剧烈波动。

SGD performs frequent updates with a high variance that cause the objective function to fluctuate heavily as in Image 1.

![img](https://ruder.io/content/images/2016/09/sgd_fluctuation.png)

_Image 1: SGD fluctuation (Source: [Wikipedia](https://upload.wikimedia.org/wikipedia/commons/f/f3/Stogra.png))_

While batch gradient descent converges to the minimum of the basin the parameters are placed in, SGD's fluctuation, on the one hand, enables it to jump to new and potentially better local minima. On the other hand, this ultimately complicates convergence to the exact minimum, as SGD will keep overshooting. However, it has been shown that when we slowly decrease the learning rate, SGD shows the same convergence behaviour as batch gradient descent, almost certainly converging to a local or the global minimum for non-convex and convex optimization respectively.
Its code fragment simply adds a loop over the training examples and evaluates the gradient w.r.t. each example. Note that we shuffle the training data at every epoch as explained in [this section](https://ruder.io/optimizing-gradient-descent/index.html#shufflingandcurriculumlearning).
与批梯度下降法的收敛会使得损失函数陷入局部最小相比，由于 SGD 的波动性，一方面，波动性使得 SGD 可以跳到新的和潜在更好的局部最优。另一方面，这使得最终收敛到特定最小值的过程变得复杂，因为 SGD 会一直持续波动。然而，已经证明当我们缓慢减小学习率，SGD 与批梯度下降法具有相同的收敛行为，对于非凸优化和凸优化，可以分别收敛到局部最小值和全局最小值。与批梯度下降的代码相比，SGD 的代码片段仅仅是在对训练样本的遍历和利用每一条样本计算梯度的过程中增加一层循环。注意，如 6.1 节中的解释，在每一次循环中，我们打乱训练样本。

```python
for i in range(nb_epochs):
	np.random.shuffle(data)
	for example in data:
		params_grad = evaluate_gradient(loss_function, example, params)
		params = params - learning_rate * params_grad
```

## Mini-batch gradient descent

Mini-batch gradient descent finally takes the best of both worlds and performs an update for every mini-batch of n training examples:
小批量梯度下降法最终结合了上述两种方法的优点，在每次更新时使用 $n$个小批量训练样本：

$\theta = \theta - \eta \cdot \nabla_\theta J( \theta; x^{(i:i+n)}; y^{(i:i+n)}).$

This way, it **a)** reduces the variance of the parameter updates, which can lead to more stable convergence; and **b)** can make use of highly optimized matrix optimizations common to state-of-the-art deep learning libraries that make computing the gradient w.r.t. a mini-batch very efficient. Common mini-batch sizes range between 50 and 256, but can vary for different applications. Mini-batch gradient descent is typically the algorithm of choice when training a neural network and the term SGD usually is employed also when mini-batches are used. Note: In modifications of SGD in the rest of this post, we leave out the parameters $x^{(i:i+n)}$; $y^{(i:i+n)}$ for simplicity.
这种方法，a) 减少参数更新的方差，这样可以得到更加稳定的收敛结果；b) 可以利用最新的深度学习库中高度优化的矩阵优化方法，高效地求解每个小批量数据的梯度。通常，小批量数据的大小在 50 到 256 之间，也可以根据不同的应用有所变化。当训练神经网络模型时，小批量梯度下降法是典型的选择算法，当使用小批量梯度下降法时，也将其称为 SGD。注意：在下文的改进的 SGD 中，为了简单，我们省略了参数 $x^{(i:i+n)}$ $y^{(i:i+n)}$


In code, instead of iterating over examples, we now iterate over mini-batches of size 50:

```python
for i in range(nb_epochs):
	np.random.shuffle(data)
	for batch in get_batches(data, batch_size=50):
		params_grad = evaluate_gradient(loss_function, batch, params)
		params = params - learning_rate * params_grad
```

# Challenges

Vanilla mini-batch gradient descent, however, does not guarantee good convergence, but offers a few challenges that need to be addressed:
虽然 Vanilla 小批量梯度下降法并不能保证较好的收敛性，但是需要强调的是，这也给我们留下了如下的一些挑战：

- Choosing a proper learning rate can be difficult. A learning rate that is too small leads to painfully slow convergence, while a learning rate that is too large can hinder convergence and cause the loss function to fluctuate around the minimum or even to diverge.
    选择一个合适的学习率可能是困难的。学习率太小会导致收敛的速度很慢，学习率太大会妨碍收敛，导致损失函数在最小值附近波动甚至偏离最小值。
- Learning rate schedules [[1\]](https://ruder.io/optimizing-gradient-descent/index.html#fn1) try to adjust the learning rate during training by e.g. annealing, i.e. reducing the learning rate according to a pre-defined schedule or when the change in objective between epochs falls below a threshold. These schedules and thresholds, however, have to be defined in advance and are thus unable to adapt to a dataset's characteristics [[2\]](https://ruder.io/optimizing-gradient-descent/index.html#fn2).
    学习率调整试图在训练的过程中通过例如退火的方法调整学习率，即根据预定义的策略或者当相邻两代之间的下降值小于某个阈值时减小学习率。然而，策略和阈值需要预先设定好，因此无法适应数据集的特点。
- Additionally, the same learning rate applies to all parameter updates. If our data is sparse and our features have very different frequencies, we might not want to update all of them to the same extent, but perform a larger update for rarely occurring features.
    此外，对所有的参数更新使用同样的学习率。如果数据是稀疏的，同时，特征的频率差异很大时，我们也许不想以同样的学习率更新所有的参数，对于出现次数较少的特征，我们对其执行更大的学习率。
- Another key challenge of minimizing highly non-convex error functions common for neural networks is avoiding getting trapped in their numerous suboptimal local minima. Dauphin et al. [[3\]](https://ruder.io/optimizing-gradient-descent/index.html#fn3) argue that the difficulty arises in fact not from local minima but from saddle points, i.e. points where one dimension slopes up and another slopes down. These saddle points are usually surrounded by a plateau of the same error, which makes it notoriously hard for SGD to escape, as the gradient is close to zero in all dimensions.
    高度非凸误差函数普遍出现在神经网络中，在优化这类函数时，另一个关键的挑战是使函数避免陷入无数次优的局部最小值。Dauphin 等人指出出现这种困难实际上并不是来自局部最小值，而是来自鞍点，即那些在一个维度上是递增的，而在另一个维度上是递减的。这些鞍点通常被具有相同误差的点包围，因为在任意维度上的梯度都近似为 0，所以 SGD 很难从这些鞍点中逃开。

# Gradient descent optimization algorithms

In the following, we will outline some algorithms that are widely used by the deep learning community to deal with the aforementioned challenges. We will not discuss algorithms that are infeasible to compute in practice for high-dimensional data sets, e.g. second-order methods such as [Newton's method](https://en.wikipedia.org/wiki/Newton's_method_in_optimization).
下面，我们将列举一些算法，这些算法被深度学习社区广泛用来处理前面提到的挑战。我们不会讨论在实际中不适合在高维数据集中计算的算法，例如诸如[牛顿法](https://en.wikipedia.org/wiki/Newton%27s_method_in_optimization)的二阶方法。

## Momentum

SGD has trouble navigating ravines, i.e. areas where the surface curves much more steeply in one dimension than in another [[4\]](https://ruder.io/optimizing-gradient-descent/index.html#fn4), which are common around local optima. In these scenarios, SGD oscillates across the slopes of the ravine while only making hesitant progress along the bottom towards the local optimum as in Image 2.
SGD 很难通过陡谷，即在一个维度上的表面弯曲程度远大于其他维度的区域，这种情况通常出现在局部最优点附近。在这种情况下，SGD 摇摆地通过陡谷的斜坡，同时，沿着底部到局部最优点的路径上只是缓慢地前进，这个过程如图 2 所示。

<table>
  <tbody><tr>
    <td style="padding:1px">
      <figure>
      <img src="https://ruder.io/content/images/2015/12/without_momentum.gif" style="width: 90%" title="SGD without momentum">
<figcaption>Image 2: SGD without momentum</figcaption>
</figure>
    </td>
    <td style="padding:1px">
      <figure>
      <img src="https://ruder.io/content/images/2015/12/with_momentum.gif" style="width: 90%;" title="">
<figcaption>Image 3: SGD with momentum</figcaption>
</figure>
    </td>
  </tr>
</tbody></table>
Momentum [[5\]](https://ruder.io/optimizing-gradient-descent/index.html#fn5) is a method that helps accelerate SGD in the relevant direction and dampens oscillations as can be seen in Image 3. It does this by adding a fraction $\gamma$ of the update vector of the past time step to the current update vector:
如图 3 所示，动量法 是一种帮助 SGD 在相关方向上加速并抑制摇摆的一种方法。动量法将历史步长的更新向量的一个分量$\gamma$增加到当前的更新向量中（部分实现中交换了公式中的符号）
$$
\begin{align} \begin{split} v_t &= \gamma v_{t-1} + \eta \nabla_\theta J( \theta) \\ \theta &= \theta - v_t \end{split} \end{align}
$$


Note: Some implementations exchange the signs in the equations. The momentum term $\gamma $is usually set to 0.9 or a similar value.

Essentially, when using momentum, we push a ball down a hill. The ball accumulates momentum as it rolls downhill, becoming faster and faster on the way (until it reaches its terminal velocity if there is air resistance, i.e. $\gamma$ < 1). The same thing happens to our parameter updates: The momentum term increases for dimensions whose gradients point in the same directions and reduces updates for dimensions whose gradients change directions. As a result, we gain faster convergence and reduced oscillation.
从本质上说，动量法，就像我们从山上推下一个球，球在滚下来的过程中累积动量，变得越来越快（直到达到终极速度，如果有空气阻力的存在，则$\gamma \lt 1$）。同样的事情也发生在参数的更新过程中：对于在梯度点处具有相同的方向的维度，其动量项增大，对于在梯度点处改变方向的维度，其动量项减小。因此，我们可以得到更快的收敛速度，同时可以减少摇摆。

# Nesterov accelerated gradient

However, a ball that rolls down a hill, blindly following the slope, is highly unsatisfactory. We'd like to have a smarter ball, a ball that has a notion of where it is going so that it knows to slow down before the hill slopes up again.
然而，球从山上滚下的时候，盲目地沿着斜率方向，往往并不能令人满意。我们希望有一个智能的球，这个球能够知道它将要去哪，以至于在重新遇到斜率上升时能够知道减速。

Nesterov accelerated gradient (NAG) [[6\]](https://ruder.io/optimizing-gradient-descent/index.html#fn6) is a way to give our momentum term this kind of prescience. We know that we will use our momentum term $\gamma v_{t-1}$ to move the parameters $\theta$. Computing $\theta - \gamma v_{t-1}$ thus gives us an approximation of the next position of the parameters (the gradient is missing for the full update), a rough idea where our parameters are going to be. We can now effectively look ahead by calculating the gradient not w.r.t. to our current parameters \theta but w.r.t. the approximate future position of our parameters:
Nesterov 加速梯度下降法（Nesterov accelerated gradient，NAG） 是一种能够给动量项这样的预知能力的方法。我们知道，我们利用动量项$\gamma v_{t-1}$来更新参数$\theta$。通过计算$\theta - \gamma v_{t-1}$ 能够告诉我们参数未来位置的一个近似值（梯度并不是完全更新），这也就是告诉我们参数大致将变为多少。通过计算关于参数未来的近似位置的梯度，而不是关于当前的参数$\theta$的梯度，我们可以高效的求解 ：
$$
\begin{align} \begin{split} v_t &= \gamma v_{t-1} + \eta \nabla_\theta J( \theta - \gamma v_{t-1} ) \\ \theta &= \theta - v_t \end{split} \end{align}
$$
Again, we set the momentum term $\gamma$ to a value of around 0.9. While Momentum first computes the current gradient (small blue vector in Image 4) and then takes a big jump in the direction of the updated accumulated gradient (big blue vector), NAG first makes a big jump in the direction of the previous accumulated gradient (brown vector), measures the gradient and then makes a correction (red vector), which results in the complete NAG update (green vector). This anticipatory update prevents us from going too fast and results in increased responsiveness, which has significantly increased the performance of RNNs on a number of tasks [[7\]](https://ruder.io/optimizing-gradient-descent/index.html#fn7).
同时，我们设置动量项$\gamma$大约为 0.9。动量法首先计算当前的梯度值（图 4 中的小的蓝色向量），然后在更新的累积梯度（大的蓝色向量）方向上前进一大步，Nesterov 加速梯度下降法 NAG 首先在先前累积梯度（棕色的向量）方向上前进一大步，计算梯度值，然后做一个修正（绿色的向量）。这个具有预见性的更新防止我们前进得太快，同时增强了算法的响应能力，这一点在很多的任务中对于 RNN 的性能提升有着重要的意义。

![img](https://ruder.io/content/images/2016/09/nesterov_update_vector.png)Image 4: Nesterov update (Source: [G. Hinton's lecture 6c](http://www.cs.toronto.edu/~tijmen/csc321/slides/lecture_slides_lec6.pdf))

Refer to [here](https://cs231n.github.io/neural-networks-3/) for another explanation about the intuitions behind NAG, while Ilya Sutskever gives a more detailed overview in his PhD thesis [[8\]](https://ruder.io/optimizing-gradient-descent/index.html#fn8).


Now that we are able to adapt our updates to the slope of our error function and speed up SGD in turn, we would also like to adapt our updates to each individual parameter to perform larger or smaller updates depending on their importance.
既然我们能够使得我们的更新适应误差函数的斜率以相应地加速 SGD，我们同样也想要使得我们的更新能够适应每一个单独参数，以根据每个参数的重要性决定大的或者小的更新。

## Adagrad

Adagrad [[9\]](https://ruder.io/optimizing-gradient-descent/index.html#fn9) is an algorithm for gradient-based optimization that does just this: It adapts the learning rate to the parameters, performing smaller updates(i.e. low learning rates) for parameters associated with frequently occurring features, and larger updates (i.e. high learning rates) for parameters associated with infrequent features. For this reason, it is well-suited for dealing with sparse data. Dean et al. [[10\]](https://ruder.io/optimizing-gradient-descent/index.html#fn10) have found that Adagrad greatly improved the robustness of SGD and used it for training large-scale neural nets at Google, which -- among other things -- learned to [recognize cats in Youtube videos](https://www.wired.com/2012/06/google-x-neural-network/). Moreover, Pennington et al. [[11\]](https://ruder.io/optimizing-gradient-descent/index.html#fn11) used Adagrad to train GloVe word embeddings, as infrequent words require much larger updates than frequent ones.
Adagrad 是这样的一种基于梯度的优化算法：让学习率适应参数，对于出现次数较少的特征，我们对其采用更大的学习率，对于出现次数较多的特征，我们对其采用较小的学习率。因此，Adagrad 非常适合处理稀疏数据。Dean 等人  发现 Adagrad 能够极大提高了 SGD 的鲁棒性并将其应用于 Google 的大规模神经网络的训练，其中包含了 [recognize cats in Youtube videos](https://www.wired.com/2012/06/google-x-neural-network/) 中的猫的识别。此外，Pennington 等人 利用 Adagrad 训练 Glove 词向量，因为低频词比高频词需要更大的步长。

Previously, we performed an update for all parameters $\theta$ at once as every parameter $\theta_i $used the same learning rate $\eta$. As Adagrad uses a different learning rate for every parameter $\theta_i$ at every time step t, we first show Adagrad's per-parameter update, which we then vectorize. For brevity, we use $g_{t}$ to denote the gradient at time step t. $g_{t, i}$ is then the partial derivative of the objective function w.r.t. to the parameter $\theta_i$ at time step t:
前面，我们每次更新所有的参数$\theta$时，每一个参数$θi$都使用的是相同的学习率$\eta$。由于 Adagrad 在 t时刻对每一个参数$\theta_i $使用了不同的学习率，我们首先介绍 Adagrad 对每一个参数的更新，然后我们对其向量化。为了简洁，令 $g_{t, i}$为在 t 时刻目标函数关于参数$\theta_i$的梯度：

$g_{t, i} = \nabla_\theta J( \theta_{t, i} ).$

The SGD update for every parameter $\theta_i$ at each time step t then becomes:

$\theta_{t+1, i} = \theta_{t, i} - \eta \cdot g_{t, i}.$

In its update rule, Adagrad modifies the general learning rate $\eta$ at each time step t for every parameter $\theta_i$ based on the past gradients that have been computed for $\theta_i$:

$\theta_{t+1, i} = \theta_{t, i} - \dfrac{\eta}{\sqrt{G_{t, ii} + \epsilon}} \cdot g_{t, i}.$

$G_{t} \in \mathbb{R}^{d \times d}$ here is a diagonal matrix where each diagonal element $i, i$ is the sum of the squares of the gradients w.r.t. $\theta_i$ up to time step t [[12\]](https://ruder.io/optimizing-gradient-descent/index.html#fn12), while $\epsilon$ is a smoothing term that avoids division by zero (usually on the order of 1e-8). Interestingly, without the square root operation, the algorithm performs much worse.
其中，$G_{t} \in \mathbb{R}^{d \times d}$是一个对角矩阵，对角线上的元素 $i,i$是直到 $t$时刻为止，所有关于$\theta_i$的梯度的平方和（Duchi 等人 将该矩阵作为包含所有先前梯度的外积的完整矩阵的替代，因为即使是对于中等数量的参数 $d$，矩阵的均方根的计算都是不切实际的。），$\epsilon$是平滑项，用于防止除数为 0（通常大约设置为 $1e−8$）。比较有意思的是，如果没有平方根的操作，算法的效果会变得很差。

As $G_{t}$ contains the sum of the squares of the past gradients w.r.t. to all parameters $\theta$ along its diagonal, we can now vectorize our implementation by performing a matrix-vector product $\odot $between $G_{t}$ and $g_{t}$:
由于 $G_{t}$的对角线上包含了关于所有参数$θ$的历史梯度的平方和，现在，我们可以通过 $G_{t}$和 $g_t$之间的元素向量乘法$\odot$向量化上述的操作：

$\theta_{t+1} = \theta_{t} - \dfrac{\eta}{\sqrt{G_{t} + \epsilon}} \odot g_{t}.$

One of Adagrad's main benefits is that it eliminates the need to manually tune the learning rate. Most implementations use a default value of 0.01 and leave it at that.

Adagrad's main weakness is its accumulation of the squared gradients in the denominator: Since every added term is positive, the accumulated sum keeps growing during training. This in turn causes the learning rate to shrink and eventually become infinitesimally small, at which point the algorithm is no longer able to acquire additional knowledge. The following algorithms aim to resolve this flaw.
Adagrad 的一个主要缺点是它在分母中累加梯度的平方：由于没增加一个正项，在整个训练过程中，累加的和会持续增长。这会导致学习率变小以至于最终变得无限小，在学习率无限小时，Adagrad 算法将无法取得额外的信息。接下来的算法旨在解决这个不足。

## Adadelta

Adadelta [[13\]](https://ruder.io/optimizing-gradient-descent/index.html#fn13) is an extension of Adagrad that seeks to reduce its aggressive, monotonically decreasing learning rate. Instead of accumulating all past squared gradients, Adadelta restricts the window of accumulated past gradients to some fixed size w.
Adadelta 是 Adagrad 的一种扩展算法，以处理 Adagrad 学习速率单调递减的问题。不是计算所有的梯度平方，Adadelta 将计算计算历史梯度的窗口大小限制为一个固定值 w。

Instead of inefficiently storing w previous squared gradients, the sum of gradients is recursively defined as a decaying average of all past squared gradients. The running average $E[g^2]_t$ at time step t then depends (as a fraction $\gamma$ similarly to the Momentum term) only on the previous average and the current gradient:
在 Adadelta 中，无需存储先前的 w个平方梯度，而是将梯度的平方递归地表示成所有历史梯度平方的均值。在 $t$时刻的均值 $E[g^2]_t$只取决于先前的均值和当前的梯度（分量$\gamma$类似于动量项）：

$E[g^2]_t = \gamma E[g^2]_{t-1} + (1 - \gamma) g^2_t.$

We set $\gamma$ to a similar value as the momentum term, around 0.9. For clarity, we now rewrite our vanilla SGD update in terms of the parameter update vector $\Delta \theta_t$:

$$
\begin{align} \begin{split} \Delta \theta_t &= - \eta \cdot g_{t, i} \\ \theta_{t+1} &= \theta_t + \Delta \theta_t \end{split} \end{align}
$$
The parameter update vector of Adagrad that we derived previously thus takes the form:

$\Delta \theta_t = - \dfrac{\eta}{\sqrt{G_{t} + \epsilon}} \odot g_{t}.$

We now simply replace the diagonal matrix $G_{t}$ with the decaying average over past squared gradients $E[g^2]_t$:

$\Delta \theta_t = - \dfrac{\eta}{\sqrt{E[g^2]_t + \epsilon}} g_{t}.$

As the denominator is just the root mean squared (RMS) error criterion of the gradient, we can replace it with the criterion short-hand:

$\Delta \theta_t = - \dfrac{\eta}{RMS[g]_{t}} g_t.$

The authors note that the units in this update (as well as in SGD, Momentum, or Adagrad) do not match, i.e. the update should have the same hypothetical units as the parameter. To realize this, they first define another exponentially decaying average, this time not of squared gradients but of squared parameter updates:
作者指出上述更新公式中的每个部分（与 SGD，动量法或者 Adagrad）并不一致，即更新规则中必须与参数具有相同的假设单位。为了实现这个要求，作者首次定义了另一个指数衰减均值，这次不是梯度平方，而是参数的平方的更新：

$E[\Delta \theta^2]_t = \gamma E[\Delta \theta^2]_{t-1} + (1 - \gamma) \Delta \theta^2_t.$

The root mean squared error of parameter updates is thus:

$RMS[\Delta \theta]_{t} = \sqrt{E[\Delta \theta^2]_t + \epsilon}.$

Since $RMS[\Delta \theta]_{t}$ is unknown, we approximate it with the RMS of parameter updates until the previous time step. Replacing the learning rate \eta in the previous update rule with $RMS[\Delta \theta]_{t-1}$ finally yields the Adadelta update rule:

$$
\begin{align} \begin{split} \Delta \theta_t &= - \dfrac{RMS[\Delta \theta]_{t-1}}{RMS[g]_{t}} g_{t} \\ \theta_{t+1} &= \theta_t + \Delta \theta_t \end{split} \end{align}
$$
With Adadelta, we do not even need to set a default learning rate, as it has been eliminated from the update rule.

## RMSprop

RMSprop is an unpublished, adaptive learning rate method proposed by Geoff Hinton in [Lecture 6e of his Coursera Class](http://www.cs.toronto.edu/~tijmen/csc321/slides/lecture_slides_lec6.pdf).

RMSprop and Adadelta have both been developed independently around the same time stemming from the need to resolve Adagrad's radically diminishing learning rates. RMSprop in fact is identical to the first update vector of Adadelta that we derived above:

$$
\begin{align} \begin{split} E[g^2]_t &= 0.9 E[g^2]_{t-1} + 0.1 g^2_t \\ \theta_{t+1} &= \theta_{t} - \dfrac{\eta}{\sqrt{E[g^2]_t + \epsilon}} g_{t} \end{split} \end{align}
$$


RMSprop as well divides the learning rate by an exponentially decaying average of squared gradients. Hinton suggests $\gamma$ to be set to 0.9, while a good default value for the learning rate $\eta$ is 0.001.

## Adam

Adaptive Moment Estimation (Adam) [[14\]](https://ruder.io/optimizing-gradient-descent/index.html#fn14) is another method that computes adaptive learning rates for each parameter. In addition to storing an exponentially decaying average of past squared gradients $v_t$ like Adadelta and RMSprop, Adam also keeps an exponentially decaying average of past gradients $m_t$, similar to momentum. Whereas momentum can be seen as a ball running down a slope, Adam behaves like a heavy ball with friction, which thus prefers flat minima in the error surface [[15\]](https://ruder.io/optimizing-gradient-descent/index.html#fn15). We compute the decaying averages of past and past squared gradients $m_t$ and $v_t$ respectively as follows:

$$
\begin{align} \begin{split} m_t &= \beta_1 m_{t-1} + (1 - \beta_1) g_t \\ v_t &= \beta_2 v_{t-1} + (1 - \beta_2) g_t^2 \end{split} \end{align}
$$

$m_t$ and $v_t$ are estimates of the first moment (the mean) and the second moment (the uncentered variance) of the gradients respectively, hence the name of the method. As $m_t$ and $v_t$ are initialized as vectors of 0's, the authors of Adam observe that they are biased towards zero, especially during the initial time steps, and especially when the decay rates are small (i.e. $\beta_1$ and $\beta_2$ are close to 1).

They counteract these biases by computing bias-corrected first and second moment estimates:

$$
\begin{align} \begin{split} \hat{m}_t &= \dfrac{m_t}{1 - \beta^t_1} \\ \hat{v}_t &= \dfrac{v_t}{1 - \beta^t_2} \end{split} \end{align}
$$
They then use these to update the parameters just as we have seen in Adadelta and RMSprop, which yields the Adam update rule:

$\theta_{t+1} = \theta_{t} - \dfrac{\eta}{\sqrt{\hat{v}_t} + \epsilon} \hat{m}_t.$

The authors propose default values of 0.9 for $\beta_1$, 0.999 for $\beta_2$, and $10^{-8}$ for $\epsilon$. They show empirically that Adam works well in practice and compares favorably to other adaptive learning-method algorithms.

## AdaMax

The $v_t$ factor in the Adam update rule scales the gradient inversely proportionally to the $\ell_2$ norm of the past gradients (via the $v_{t-1}$ term) and current gradient $|g_t|^2$:

$v_t = \beta_2 v_{t-1} + (1 - \beta_2) |g_t|^2$

We can generalize this update to the $\ell_p$ norm. Note that Kingma and Ba also parameterize $\beta_2 $as $\beta^p_2$:

$v_t = \beta_2^p v_{t-1} + (1 - \beta_2^p) |g_t|^p$

Norms for large p values generally become numerically unstable, which is why $\ell_1$ and $\ell_2$ norms are most common in practice. However, $\ell_\infty$ also generally exhibits stable behavior. For this reason, the authors propose AdaMax (Kingma and Ba, 2015) and show that $v_t$ with $\ell_\infty$ converges to the following more stable value. To avoid confusion with Adam, we use u_t to denote the infinity norm-constrained $v_t$:

$$
\begin{align} \begin{split} u_t &= \beta_2^\infty v_{t-1} + (1 - \beta_2^\infty) |g_t|^\infty\\ & = \max(\beta_2 \cdot v_{t-1}, |g_t|) \end{split} \end{align}
$$
We can now plug this into the Adam update equation by replacing $\sqrt{\hat{v}_t} + \epsilon$ with $u_t$ to obtain the AdaMax update rule:

$$
\theta_{t+1} = \theta_{t} - \dfrac{\eta}{u_t} \hat{m}_t
$$
Note that as $u_t$ relies on the $\max$ operation, it is not as suggestible to bias towards zero as $m_t$ and $v_t$ in Adam, which is why we do not need to compute a bias correction for $u_t$. Good default values are again $\eta$ = 0.002, $\beta_1$ = 0.9, and $\beta_2$ = 0.999.

## Nadam

As we have seen before, Adam can be viewed as a combination of RMSprop and momentum: RMSprop contributes the exponentially decaying average of past squared gradients $v_t$, while momentum accounts for the exponentially decaying average of past gradients $m_t$. We have also seen that Nesterov accelerated gradient (NAG) is superior to vanilla momentum.

Nadam (Nesterov-accelerated Adaptive Moment Estimation) [[16\]](https://ruder.io/optimizing-gradient-descent/index.html#fn16) thus combines Adam and NAG. In order to incorporate NAG into Adam, we need to modify its momentum term $m_t$.

First, let us recall the momentum update rule using our current notation :

$$
\begin{align} \begin{split} g_t &= \nabla_{\theta_t}J(\theta_t)\\ m_t &= \gamma m_{t-1} + \eta g_t\\ \theta_{t+1} &= \theta_t - m_t \end{split} \end{align}
$$
where J is our objective function, $\gamma$ is the momentum decay term, and $\eta$ is our step size. Expanding the third equation above yields:

$$
\theta_{t+1} = \theta_t - ( \gamma m_{t-1} + \eta g_t)
$$
This demonstrates again that momentum involves taking a step in the direction of the previous momentum vector and a step in the direction of the current gradient.

>NAG then allows us to perform a more accurate step in the gradient direction by updating the parameters with the momentum step *before* computing the gradient. We thus only need to modify the gradient g_t to arrive at NAG:
>
>\begin{align} \begin{split} g_t &= \nabla_{\theta_t}J(\theta_t - \gamma m_{t-1})\\ m_t &= \gamma m_{t-1} + \eta g_t\\ \theta_{t+1} &= \theta_t - m_t \end{split} \end{align}
>
>Dozat proposes to modify NAG the following way: Rather than applying the momentum step twice -- one time for updating the gradient g_t and a second time for updating the parameters \theta_{t+1} -- we now apply the look-ahead momentum vector directly to update the current parameters:
>
>\begin{align} \begin{split} g_t &= \nabla_{\theta_t}J(\theta_t)\\ m_t &= \gamma m_{t-1} + \eta g_t\\ \theta_{t+1} &= \theta_t - (\gamma m_t + \eta g_t) \end{split} \end{align}
>
>Notice that rather than utilizing the previous momentum vector m_{t-1} as in the equation of the expanded momentum update rule above, we now use the current momentum vector m_t to look ahead. In order to add Nesterov momentum to Adam, we can thus similarly replace the previous momentum vector with the current momentum vector. First, recall that the Adam update rule is the following (note that we do not need to modify \hat{v}_t):
>
>\begin{align} \begin{split} m_t &= \beta_1 m_{t-1} + (1 - \beta_1) g_t\\ \hat{m}_t & = \frac{m_t}{1 - \beta^t_1}\\ \theta_{t+1} &= \theta_{t} - \frac{\eta}{\sqrt{\hat{v}_t} + \epsilon} \hat{m}_t \end{split} \end{align}
>  
>Expanding the second equation with the definitions of \hat{m}_t and m_t in turn gives us:
>    
>\theta_{t+1} = \theta_{t} - \dfrac{\eta}{\sqrt{\hat{v}_t} + \epsilon} (\dfrac{\beta_1 m_{t-1}}{1 - \beta^t_1} + \dfrac{(1 - \beta_1) g_t}{1 - \beta^t_1})
>
>Note that \dfrac{\beta_1 m_{t-1}}{1 - \beta^t_1} is just the bias-corrected estimate of the momentum vector of the previous time step. We can thus replace it with \hat{m}_{t-1}:
>
>\theta_{t+1} = \theta_{t} - \dfrac{\eta}{\sqrt{\hat{v}_t} + \epsilon} (\beta_1 \hat{m}_{t-1} + \dfrac{(1 - \beta_1) g_t}{1 - \beta^t_1})
>
>Note that for simplicity, we ignore that the denominator is 1 - \beta^t_1 and not 1 - \beta^{t-1}_1 as we will replace the denominator in the next step anyway. This equation again looks very similar to our expanded momentum update rule above. We can now add Nesterov momentum just as we did previously by simply replacing this bias-corrected estimate of the momentum vector of the previous time step \hat{m}_{t-1} with the bias-corrected estimate of the current momentum vector \hat{m}_t, which gives us the Nadam update rule:
>
>\theta_{t+1} = \theta_{t} - \dfrac{\eta}{\sqrt{\hat{v}_t} + \epsilon} (\beta_1 \hat{m}_t + \dfrac{(1 - \beta_1) g_t}{1 - \beta^t_1})
>
>## AMSGrad
>
>As adaptive learning rate methods have become the norm in training neural networks, practitioners noticed that in some cases, e.g. for object recognition [[17\]](https://ruder.io/optimizing-gradient-descent/index.html#fn17) or machine translation [[18\]](https://ruder.io/optimizing-gradient-descent/index.html#fn18) they fail to converge to an optimal solution and are outperformed by SGD with momentum.
>
>Reddi et al. (2018) [[19\]](https://ruder.io/optimizing-gradient-descent/index.html#fn19) formalize this issue and pinpoint the exponential moving average of past squared gradients as a reason for the poor generalization behaviour of adaptive learning rate methods. Recall that the introduction of the exponential average was well-motivated: It should prevent the learning rates to become infinitesimally small as training progresses, the key flaw of the Adagrad algorithm. However, this short-term memory of the gradients becomes an obstacle in other scenarios.
>
>In settings where Adam converges to a suboptimal solution, it has been observed that some minibatches provide large and informative gradients, but as these minibatches only occur rarely, exponential averaging diminishes their influence, which leads to poor convergence. The authors provide an example for a simple convex optimization problem where the same behaviour can be observed for Adam.
>
>To fix this behaviour, the authors propose a new algorithm, AMSGrad that uses the maximum of past squared gradients v_t rather than the exponential average to update the parameters. v_t is defined the same as in Adam above:
>
>v_t = \beta_2 v_{t-1} + (1 - \beta_2) g_t^2
>
>Instead of using v_t (or its bias-corrected version \hat{v}_t) directly, we now employ the previous v_{t-1} if it is larger than the current one:
>
>\hat{v}_t = \text{max}(\hat{v}_{t-1}, v_t)
>
>This way, AMSGrad results in a non-increasing step size, which avoids the problems suffered by Adam. For simplicity, the authors also remove the debiasing step that we have seen in Adam. The full AMSGrad update without bias-corrected estimates can be seen below:
>
>\begin{align} \begin{split} m_t &= \beta_1 m_{t-1} + (1 - \beta_1) g_t \\ v_t &= \beta_2 v_{t-1} + (1 - \beta_2) g_t^2\\ \hat{v}_t &= \text{max}(\hat{v}_{t-1}, v_t) \\ \theta_{t+1} &= \theta_{t} - \dfrac{\eta}{\sqrt{\hat{v}_t} + \epsilon} m_t \end{split} \end{align}
>
>The authors observe improved performance compared to Adam on small datasets and on CIFAR-10. [Other experiments](https://fdlm.github.io/post/amsgrad/), however, show similar or worse performance than Adam. It remains to be seen whether AMSGrad is able to consistently outperform Adam in practice. For more information about recent advances in Deep Learning optimization, refer to [this blog post](https://ruder.io/deep-learning-optimization-2017/).

## Other recent optimizers

A number of other optimizers have been proposed after AMSGrad. These include AdamW [[20\]](https://ruder.io/optimizing-gradient-descent/index.html#fn20), which fixes weight decay in Adam; QHAdam [[21\]](https://ruder.io/optimizing-gradient-descent/index.html#fn21), which averages a standard SGD step with a momentum SGD step; and AggMo [[22\]](https://ruder.io/optimizing-gradient-descent/index.html#fn22), which combines multiple momentum terms $\gamma$; and others. For an overview of recent gradient descent algorithms, have a look at [this blog post](https://johnchenresearch.github.io/demon/).

## Visualization of algorithms

The following two animations (Image credit: [Alec Radford](https://twitter.com/alecrad)) provide some intuitions towards the optimization behaviour of most of the presented optimization methods. Also have a look [here](https://cs231n.github.io/neural-networks-3/) for a description of the same images by Karpathy and another concise overview of the algorithms discussed.

In Image 5, we see their behaviour on the contours of a loss surface ([the Beale function](https://www.sfu.ca/~ssurjano/beale.html)) over time. Note that Adagrad, Adadelta, and RMSprop almost immediately head off in the right direction and converge similarly fast, while Momentum and NAG are led off-track, evoking the image of a ball rolling down the hill. NAG, however, is quickly able to correct its course due to its increased responsiveness by looking ahead and heads to the minimum.
在图 5 中，我们看到不同算法在损失曲面的等高线上走的不同路线。所有的算法都是从同一个点出发并选择不同路径到达最优点。注意：Adagrad，Adadelta 和 RMSprop 能够立即转移到正确的移动方向上并以类似的速度收敛，而动量法和 NAG 会导致偏离，想像一下球从山上滚下的画面。然而，NAG 能够在偏离之后快速修正其路线，因为 NAG 通过对最优点的预见增强其响应能力。

Image 6 shows the behaviour of the algorithms at a saddle point, i.e. a point where one dimension has a positive slope, while the other dimension has a negative slope, which pose a difficulty for SGD as we mentioned before. Notice here that SGD, Momentum, and NAG find it difficulty to break symmetry, although the two latter eventually manage to escape the saddle point, while Adagrad, RMSprop, and Adadelta quickly head down the negative slope.
图 5 中展示了不同算法在鞍点出的行为，鞍点即为一个点在一个维度上的斜率为正，而在其他维度上的斜率为负，正如我们前面提及的，鞍点对 SGD 的训练造成很大困难。这里注意，SGD，动量法和 NAG 在鞍点处很难打破对称性，尽管后面两个算法最终设法逃离了鞍点。而 Adagrad，RMSprop 和 Adadelta 能够快速想着梯度为负的方向移动，其中 Adadelta 走在最前面。

| ![img](https://ruder.io/content/images/2016/09/contours_evaluation_optimizers.gif)Image 5: SGD optimization on loss surface contours | ![SGD with momentum](https://ruder.io/content/images/2016/09/saddle_point_evaluation_optimizers.gif)Image 6: SGD optimization on saddle point |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
|                                                              |                                                              |

As we can see, the adaptive learning-rate methods, i.e. Adagrad, Adadelta, RMSprop, and Adam are most suitable and provide the best convergence for these scenarios.

Note: If you are interested in visualizing these or other optimization algorithms, refer to [this useful tutorial](http://louistiao.me/notes/visualizing-and-animating-optimization-algorithms-with-matplotlib/).

## Which optimizer to use?

So, which optimizer should you now use? If your input data is sparse, then you likely achieve the best results using one of the adaptive learning-rate methods. An additional benefit is that you won't need to tune the learning rate but likely achieve the best results with the default value.
那么，我们应该选择使用哪种优化算法呢？如果输入数据是稀疏的，选择任一自适应学习率算法可能会得到最好的结果。选用这类算法的另一个好处是无需调整学习率，选用默认值就可能达到最好的结果。

In summary, RMSprop is an extension of Adagrad that deals with its radically diminishing learning rates. It is identical to Adadelta, except that Adadelta uses the RMS of parameter updates in the numinator update rule. Adam, finally, adds bias-correction and momentum to RMSprop. Insofar, RMSprop, Adadelta, and Adam are very similar algorithms that do well in similar circumstances. Kingma et al. [[14:1\]](https://ruder.io/optimizing-gradient-descent/index.html#fn14) show that its bias-correction helps Adam slightly outperform RMSprop towards the end of optimization as gradients become sparser. Insofar, Adam might be the best overall choice.
总的来说，RMSprop 是 Adagrad 的扩展形式，用于处理在 Adagrad 中急速递减的学习率。RMSprop 与 Adadelta 相同，所不同的是 Adadelta 在更新规则中使用参数的均方根进行更新。最后，Adam 是将偏差校正和动量加入到 RMSprop 中。在这样的情况下，RMSprop、Adadelta 和 Adam 是很相似的算法并且在相似的环境中性能都不错。Kingma 等人 [9] 指出在优化后期由于梯度变得越来越稀疏，偏差校正能够帮助 Adam 微弱地胜过 RMSprop。综合看来，Adam 可能是最佳的选择。

Interestingly, many recent papers use vanilla SGD without momentum and a simple learning rate annealing schedule. As has been shown, SGD usually achieves to find a minimum, but it might take significantly longer than with some of the optimizers, is much more reliant on a robust initialization and annealing schedule, and may get stuck in saddle points rather than local minima. Consequently, if you care about fast convergence and train a deep or complex neural network, you should choose one of the adaptive learning rate methods.
有趣的是，最近许多论文中采用不带动量的 SGD 和一种简单的学习率的退火策略。已表明，通常 SGD 能够找到最小值点，但是比其他优化的 SGD 花费更多的时间，与其他算法相比，SGD 更加依赖鲁棒的初始化和退火策略，同时，SGD 可能会陷入鞍点，而不是局部极小值点。因此，如果你关心的是快速收敛和训练一个深层的或者复杂的神经网络，你应该选择一个自适应学习率的方法。

# Parallelizing and distributing SGD

Given the ubiquity of large-scale data solutions and the availability of low-commodity clusters, distributing SGD to speed it up further is an obvious choice.
SGD by itself is inherently sequential: Step-by-step, we progress further towards the minimum. Running it provides good convergence but can be slow particularly on large datasets. In contrast, running SGD asynchronously is faster, but suboptimal communication between workers can lead to poor convergence. Additionally, we can also parallelize SGD on one machine without the need for a large computing cluster. The following are algorithms and architectures that have been proposed to optimize parallelized and distributed SGD.
当存在大量的大规模数据和廉价的集群时，利用分布式 SGD 来加速是一个显然的选择。SGD 本身有固有的顺序：一步一步，我们进一步进展到最小。SGD 提供了良好的收敛性，但 SGD 的运行缓慢，特别是对于大型数据集。相反，SGD 异步运行速度更快，但客户端之间非最理想的通信会导致差的收敛。此外，我们也可以在一台机器上并行 SGD，这样就无需大的计算集群。以下是已经提出的优化的并行和分布式的 SGD 的算法和框架。

## Hogwild!

Niu et al. [[23\]](https://ruder.io/optimizing-gradient-descent/index.html#fn23) introduce an update scheme called Hogwild! that allows performing SGD updates in parallel on CPUs. Processors are allowed to access shared memory without locking the parameters. This only works if the input data is sparse, as each update will only modify a fraction of all parameters. They show that in this case, the update scheme achieves almost an optimal rate of convergence, as it is unlikely that processors will overwrite useful information.
Niu 等人提出称为 Hogwild! 的更新机制，Hogwild! 允许在多个 CPU 上并行执行 SGD 更新。在无需对参数加锁的情况下，处理器可以访问共享的内存。这种方法只适用于稀疏的输入数据，因为每一次更新只会修改一部分参数。在这种情况下，该更新策略几乎可以达到一个最优的收敛速率，因为 CPU 之间不可能重写有用的信息。

## Downpour SGD

Downpour SGD is an asynchronous variant of SGD that was used by Dean et al. [[10:1\]](https://ruder.io/optimizing-gradient-descent/index.html#fn10) in their DistBelief framework (predecessor to TensorFlow) at Google. It runs multiple replicas of a model in parallel on subsets of the training data. These models send their updates to a parameter server, which is split across many machines. Each machine is responsible for storing and updating a fraction of the model's parameters. However, as replicas don't communicate with each other e.g. by sharing weights or updates, their parameters are continuously at risk of diverging, hindering convergence.
Downpour SGD 是 SGD 的一种异步的变形形式，在 Google，Dean 等人 在他们的 DistBelief 框架（TensorFlow 的前身）中使用了该方法。Downpour SGD 在训练集的子集上并行运行多个模型的副本。这些模型将各自的更新发送给一个参数服务器，参数服务器跨越了多台机器。每一台机器负责存储和更新模型的一部分参数。然而，因为副本之间是彼此不互相通信的，即通过共享权重或者更新，因此可能会导致参数发散而不利于收敛。

## Delay-tolerant Algorithms for SGD

McMahan and Streeter [[24\]](https://ruder.io/optimizing-gradient-descent/index.html#fn24) extend AdaGrad to the parallel setting by developing delay-tolerant algorithms that not only adapt to past gradients, but also to the update delays. This has been shown to work well in practice.
通过容忍延迟算法的开发，McMahan 和 Streeter将 AdaGraad 扩展成并行的模式，该方法不仅适应于历史梯度，同时适应于更新延迟。该方法已经在实践中被证实是有效的。

## TensorFlow

[TensorFlow](https://www.tensorflow.org/) [[25\]](https://ruder.io/optimizing-gradient-descent/index.html#fn25) is Google's recently open-sourced framework for the implementation and deployment of large-scale machine learning models. It is based on their experience with DistBelief and is already used internally to perform computations on a large range of mobile devices as well as on large-scale distributed systems. For distributed execution, a computation graph is split into a subgraph for every device and communication takes place using Send/Receive node pairs. However, the open source version of TensorFlow currently does not support distributed functionality (see [here](https://github.com/tensorflow/tensorflow/issues/23)).
Update 13.04.16: A distributed version of TensorFlow has [been released](https://googleresearch.blogspot.ie/2016/04/announcing-tensorflow-08-now-with.html).

## Elastic Averaging SGD

Zhang et al. [[26\]](https://ruder.io/optimizing-gradient-descent/index.html#fn26) propose Elastic Averaging SGD (EASGD), which links the parameters of the workers of asynchronous SGD with an elastic force, i.e. a center variable stored by the parameter server. This allows the local variables to fluctuate further from the center variable, which in theory allows for more exploration of the parameter space. They show empirically that this increased capacity for exploration leads to improved performance by finding new local optima.
Zhang 等人 提出的弹性平均 SGD（Elastic Averaging SGD，EASGD）连接了异步 SGD 的参数客户端和一个弹性力，即参数服务器存储的一个中心变量。EASGD 使得局部变量能够从中心变量震荡得更远，这在理论上使得在参数空间中能够得到更多的探索。经验表明这种增强的探索能力通过发现新的局部最优点，能够提高整体的性能。

# Additional strategies for optimizing SGD

Finally, we introduce additional strategies that can be used alongside any of the previously mentioned algorithms to further improve the performance of SGD. For a great overview of some other common tricks, refer to [[27\]](https://ruder.io/optimizing-gradient-descent/index.html#fn27).

## Shuffling and Curriculum Learning

Generally, we want to avoid providing the training examples in a meaningful order to our model as this may bias the optimization algorithm. Consequently, it is often a good idea to shuffle the training data after every epoch.

On the other hand, for some cases where we aim to solve progressively harder problems, supplying the training examples in a meaningful order may actually lead to improved performance and better convergence. The method for establishing this meaningful order is called Curriculum Learning [[28\]](https://ruder.io/optimizing-gradient-descent/index.html#fn28).

Zaremba and Sutskever [[29\]](https://ruder.io/optimizing-gradient-descent/index.html#fn29) were only able to train LSTMs to evaluate simple programs using Curriculum Learning and show that a combined or mixed strategy is better than the naive one, which sorts examples by increasing difficulty.

## Batch normalization

To facilitate learning, we typically normalize the initial values of our parameters by initializing them with zero mean and unit variance. As training progresses and we update parameters to different extents, we lose this normalization, which slows down training and amplifies changes as the network becomes deeper.

Batch normalization [[30\]](https://ruder.io/optimizing-gradient-descent/index.html#fn30) reestablishes these normalizations for every mini-batch and changes are back-propagated through the operation as well. By making normalization part of the model architecture, we are able to use higher learning rates and pay less attention to the initialization parameters. Batch normalization additionally acts as a regularizer, reducing (and sometimes even eliminating) the need for Dropout.

为了便于学习，我们通常通过用零均值和单位方差初始化参数的初始值来规范化参数的初始值。 随着训练的进行和我们在不同程度上更新参数，我们失去了这种归一化，这会减慢训练并随着网络变得更深而放大变化。

批量标准化 [[30\]](https://ruder.io/optimizing-gradient-descent/index.html#fn30) 为每个小批量重新建立这些标准化，并且更改也通过操作反向传播。 通过将归一化作为模型架构的一部分，我们能够使用更高的学习率并且更少关注初始化参数。 批量归一化还充当正则化器，减少（有时甚至消除）对 Dropout 的需求。

## Early stopping

According to Geoff Hinton: "*Early stopping (is) beautiful free lunch*" ([NIPS 2015 Tutorial slides](http://www.iro.umontreal.ca/~bengioy/talks/DL-Tutorial-NIPS2015.pdf), slide 63). You should thus always monitor error on a validation set during training and stop (with some patience) if your validation error does not improve enough.

## Gradient noise

Neelakantan et al. [[31\]](https://ruder.io/optimizing-gradient-descent/index.html#fn31) add noise that follows a Gaussian distribution N(0, \sigma^2_t) to each gradient update:

$g_{t, i} = g_{t, i} + N(0, \sigma^2_t).$

They anneal the variance according to the following schedule:

$\sigma^2_t = \dfrac{\eta}{(1 + t)^\gamma}.$

They show that adding this noise makes networks more robust to poor initialization and helps training particularly deep and complex networks. They suspect that the added noise gives the model more chances to escape and find new local minima, which are more frequent for deeper models.

# Conclusion

In this blog post, we have initially looked at the three variants of gradient descent, among which mini-batch gradient descent is the most popular. We have then investigated algorithms that are most commonly used for optimizing SGD: Momentum, Nesterov accelerated gradient, Adagrad, Adadelta, RMSprop, Adam, as well as different algorithms to optimize asynchronous SGD. Finally, we've considered other strategies to improve SGD such as shuffling and curriculum learning, batch normalization, and early stopping.

I hope that this blog post was able to provide you with some intuitions towards the motivation and the behaviour of the different optimization algorithms. Are there any obvious algorithms to improve SGD that I've missed? What tricks are you using yourself to facilitate training with SGD? **Let me know in the comments below.**

# Acknowledgements

Thanks to [Denny Britz](https://twitter.com/dennybritz) and [Cesar Salgado](https://twitter.com/cesarsvs) for reading drafts of this post and providing suggestions.

# Printable version and citation

This blog post is also available as an [article on arXiv](https://arxiv.org/abs/1609.04747), in case you want to refer to it later.

In case you found it helpful, consider citing the corresponding arXiv article as:
*Sebastian Ruder (2016). An overview of gradient descent optimisation algorithms. arXiv preprint arXiv:1609.04747.*

# Translations

This blog post has been translated into the following languages:

- [Japanese](http://postd.cc/optimizing-gradient-descent/)
- [Chinese](http://blog.csdn.net/google19890102/article/details/69942970)
- [Korean](https://brunch.co.kr/@chris-song/50)

Image credit for cover photo: [Karpathy's beautiful loss functions tumblr](http://lossfunctions.tumblr.com/)

---

1. H. Robinds and S. Monro, “A stochastic approximation method,” Annals of Mathematical Statistics, vol. 22, pp. 400–407, 1951. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref1)
2. Darken, C., Chang, J., & Moody, J. (1992). Learning rate schedules for faster stochastic gradient search. Neural Networks for Signal Processing II Proceedings of the 1992 IEEE Workshop, (September), 1–11. [http://doi.org/10.1109/NNSP.1992.253713](https://doi.org/10.1109/NNSP.1992.253713) [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref2)
3. Dauphin, Y., Pascanu, R., Gulcehre, C., Cho, K., Ganguli, S., & Bengio, Y. (2014). Identifying and attacking the saddle point problem in high-dimensional non-convex optimization. arXiv, 1–14. Retrieved from [http://arxiv.org/abs/1406.2572](https://arxiv.org/abs/1406.2572) [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref3)
4. Sutton, R. S. (1986). Two problems with backpropagation and other steepest-descent learning procedures for networks. Proc. 8th Annual Conf. Cognitive Science Society. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref4)
5. Qian, N. (1999). On the momentum term in gradient descent learning algorithms. Neural Networks : The Official Journal of the International Neural Network Society, 12(1), 145–151. [http://doi.org/10.1016/S0893-6080(98)00116-6](https://doi.org/10.1016/S0893-6080(98)00116-6) [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref5)
6. Nesterov, Y. (1983). A method for unconstrained convex minimization problem with the rate of convergence o(1/k2). Doklady ANSSSR (translated as Soviet.Math.Docl.), vol. 269, pp. 543– 547. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref6)
7. Bengio, Y., Boulanger-Lewandowski, N., & Pascanu, R. (2012). Advances in Optimizing Recurrent Networks. Retrieved from [http://arxiv.org/abs/1212.0901](https://arxiv.org/abs/1212.0901) [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref7)
8. Sutskever, I. (2013). Training Recurrent neural Networks. PhD Thesis. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref8)
9. Duchi, J., Hazan, E., & Singer, Y. (2011). Adaptive Subgradient Methods for Online Learning and Stochastic Optimization. Journal of Machine Learning Research, 12, 2121–2159. Retrieved from http://jmlr.org/papers/v12/duchi11a.html [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref9)
10. Dean, J., Corrado, G. S., Monga, R., Chen, K., Devin, M., Le, Q. V, … Ng, A. Y. (2012). Large Scale Distributed Deep Networks. NIPS 2012: Neural Information Processing Systems, 1–11. http://papers.nips.cc/paper/4687-large-scale-distributed-deep-networks.pdf [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref10) [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref10:1)
11. Pennington, J., Socher, R., & Manning, C. D. (2014). Glove: Global Vectors for Word Representation. Proceedings of the 2014 Conference on Empirical Methods in Natural Language Processing, 1532–1543. [http://doi.org/10.3115/v1/D14-1162](https://doi.org/10.3115/v1/D14-1162) [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref11)
12. Duchi et al. [3] give this matrix as an alternative to the *full* matrix containing the outer products of all previous gradients, as the computation of the matrix square root is infeasible even for a moderate number of parameters d. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref12)
13. Zeiler, M. D. (2012). ADADELTA: An Adaptive Learning Rate Method. Retrieved from [http://arxiv.org/abs/1212.5701](https://arxiv.org/abs/1212.5701) [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref13)
14. Kingma, D. P., & Ba, J. L. (2015). Adam: a Method for Stochastic Optimization. International Conference on Learning Representations, 1–13. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref14) [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref14:1)
15. Heusel, M., Ramsauer, H., Unterthiner, T., Nessler, B., & Hochreiter, S. (2017). GANs Trained by a Two Time-Scale Update Rule Converge to a Local Nash Equilibrium. In Advances in Neural Information Processing Systems 30 (NIPS 2017). [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref15)
16. Dozat, T. (2016). Incorporating Nesterov Momentum into Adam. ICLR Workshop, (1), 2013–2016. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref16)
17. Huang, G., Liu, Z., Weinberger, K. Q., & van der Maaten, L. (2017). Densely Connected Convolutional Networks. In Proceedings of CVPR 2017. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref17)
18. Johnson, M., Schuster, M., Le, Q. V, Krikun, M., Wu, Y., Chen, Z., … Dean, J. (2016). Google’s Multilingual Neural Machine Translation System: Enabling Zero-Shot Translation. arXiv Preprint arXiv:1611.0455. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref18)
19. Reddi, Sashank J., Kale, Satyen, & Kumar, Sanjiv. On the Convergence of Adam and Beyond. Proceedings of ICLR 2018. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref19)
20. Loshchilov, I., & Hutter, F. (2019). Decoupled Weight Decay Regularization. In Proceedings of ICLR 2019. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref20)
21. Ma, J., & Yarats, D. (2019). Quasi-hyperbolic momentum and Adam for deep learning. In Proceedings of ICLR 2019. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref21)
22. Lucas, J., Sun, S., Zemel, R., & Grosse, R. (2019). Aggregated Momentum: Stability Through Passive Damping. In Proceedings of ICLR 2019. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref22)
23. Niu, F., Recht, B., Christopher, R., & Wright, S. J. (2011). Hogwild! : A Lock-Free Approach to Parallelizing Stochastic Gradient Descent, 1–22. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref23)
24. Mcmahan, H. B., & Streeter, M. (2014). Delay-Tolerant Algorithms for Asynchronous Distributed Online Learning. Advances in Neural Information Processing Systems (Proceedings of NIPS), 1–9. Retrieved from http://papers.nips.cc/paper/5242-delay-tolerant-algorithms-for-asynchronous-distributed-online-learning.pdf [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref24)
25. Abadi, M., Agarwal, A., Barham, P., Brevdo, E., Chen, Z., Citro, C., … Zheng, X. (2015). TensorFlow : Large-Scale Machine Learning on Heterogeneous Distributed Systems. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref25)
26. Zhang, S., Choromanska, A., & LeCun, Y. (2015). Deep learning with Elastic Averaging SGD. Neural Information Processing Systems Conference (NIPS 2015), 1–24. Retrieved from [http://arxiv.org/abs/1412.6651](https://arxiv.org/abs/1412.6651) [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref26)
27. LeCun, Y., Bottou, L., Orr, G. B., & Müller, K. R. (1998). Efficient BackProp. Neural Networks: Tricks of the Trade, 1524, 9–50. [http://doi.org/10.1007/3-540-49430-8_2](https://doi.org/10.1007/3-540-49430-8_2) [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref27)
28. Bengio, Y., Louradour, J., Collobert, R., & Weston, J. (2009). Curriculum learning. Proceedings of the 26th Annual International Conference on Machine Learning, 41–48. [http://doi.org/10.1145/1553374.1553380](https://doi.org/10.1145/1553374.1553380) [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref28)
29. Zaremba, W., & Sutskever, I. (2014). Learning to Execute, 1–25. Retrieved from [http://arxiv.org/abs/1410.4615](https://arxiv.org/abs/1410.4615) [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref29)
30. Ioffe, S., & Szegedy, C. (2015). Batch Normalization : Accelerating Deep Network Training by Reducing Internal Covariate Shift. arXiv Preprint arXiv:1502.03167v3. [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref30)
31. Neelakantan, A., Vilnis, L., Le, Q. V., Sutskever, I., Kaiser, L., Kurach, K., & Martens, J. (2015). Adding Gradient Noise Improves Learning for Very Deep Networks, 1–11. Retrieved from [http://arxiv.org/abs/1511.06807](https://arxiv.org/abs/1511.06807) [↩︎](https://ruder.io/optimizing-gradient-descent/index.html#fnref31)