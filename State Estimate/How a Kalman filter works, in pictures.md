---
title: How a Kalman filter works, in pictures
categories:
- Probabilistic Robotics
- State Estimation
- Kalman Filter
tags:
- Kalman filter
date: 2021/6/6 20:00:09
updated: 2021/6/6 12:00:09
---



卡尔曼滤波解决的是如何从多个不确定数据中提取相对精确的数据。

1.  实践前提是这些数据满足高斯分布。
2.  理论前提是一个高斯分布乘以另一个高斯分布可以得到第三个高斯分布，第三个高斯分布即为提取到相对精确的数据范围。

以上理解来自博客 [How a Kalman filter works, in pictures](http://www.bzarg.com/p/how-a-kalman-filter-works-in-pictures/) ，写的很好，对原理解释的非常清楚，今天把它翻译为中文记录在下边，适合用来入门。

# How a Kalman filter works, in pictures

I have to tell you about the Kalman filter, because what it does is pretty damn amazing.
我必须要告诉你一些关于卡尔曼滤波的知识，因为它的作用非常惊人。

Surprisingly few software engineers and scientists seem to know about it, and that makes me sad because it is such a general and powerful tool for **combining information** in the presence of uncertainty. At times its ability to extract accurate information seems almost magical— and if it sounds like I’m talking this up too much, then take a look at [this previously posted video](https://www.bzarg.com/p/improving-imu-attitude-estimates-with-velocity-data) where I demonstrate a Kalman filter figuring out the *orientation* of a free-floating body by looking at its *velocity*. Totally neat!
奇怪的是很少有软件工程师或者科学家对它有所了解。这让我有点小失望，因为在一些含有不确定因素的场景里，如何去综合获取有效的信息，卡尔曼滤波是一个通用并且强有力的算法。有时候它提取精确信息的能力看上去就像是 “见证奇迹的时刻”。如果看到这里你认为我说的话里有夸大的水分，你可以看下我开发的效果视频。在这个 demo 里我通过检测角速度来获取一个自由物体的姿态，效果奇佳。

## 什么是卡尔曼滤波？

You can use a Kalman filter in any place where you have **uncertain information** about some dynamic system, and you can make an **educated guess** about what the system is going to do next. Even if messy reality comes along and interferes with the clean motion you guessed about, the Kalman filter will often do a very good job of figuring out what actually happened. And it can take advantage of correlations between crazy phenomena that you maybe wouldn’t have thought to exploit!
你可以在任何含有不确定因素的动态系统里使用卡尔曼滤波，而且你应该可以通过某种数学建模对系统下一步动向做一个**有根据的预测**。尽管系统总是会受到一些未知的干扰，但是卡尔曼滤波总是可以派上用场来提高系统预估的精确度，这样你就可以更加准确地知道到底发生了什么事情 (系统状态是如何转移的)。而且它可以有效利用多个粗糙数据之间的关系，而单独面对这些数据你可能都无从下手。

Kalman filters are ideal for systems which are **continuously changing**. They have the advantage that they are light on memory (they don’t need to keep any history other than the previous state), and they are very fast, making them well suited for real time problems and embedded systems.
卡尔曼滤波尤其适合动态系统。它对于内存要求极低（它仅需要保留系统上一个状态的数据，而不是一段跨度很长的历史数据）。并且它运算很快，这使得它非常适合解决实时问题和应用于嵌入式系统。

The math for implementing the Kalman filter appears pretty scary and opaque in most places you find on Google. That’s a bad state of affairs, because the Kalman filter is actually super simple and easy to understand if you look at it in the right way. Thus it makes a great article topic, and I will attempt to illuminate it with lots of clear, pretty pictures and colors. The prerequisites are simple; all you need is a basic understanding of probability and matrices.

I’ll start with a loose example of the kind of thing a Kalman filter can solve, but if you want to get right to the shiny pictures and math, feel free to [jump ahead](https://www.bzarg.com/p/how-a-kalman-filter-works-in-pictures/#mathybits).

## 利用卡尔曼滤波我们可以做什么？

Let’s make a toy example: You’ve built a little robot that can wander around in the woods, and the robot needs to know exactly where it is so that it can navigate.

![](https://pic4.zhimg.com/50/v2-3e2ab5a07a120d695c3ff1db7c80a4c4_hd.jpg)

We’ll say our robot has a state $\vec{x_k}$, which is just a position and a velocity:
$$
\vec{x_k} = (\vec{p}, \vec{v})
$$
Note that the state is just a list of numbers about the underlying configuration of your system; it could be anything. In our example it’s position and velocity, but it could be data about the amount of fluid in a tank, the temperature of a car engine, the position of a user’s finger on a touchpad, or any number of things you need to keep track of.
注意这个状态仅仅是系统所有状态中的一部分，你可以选取任何数据变量作为观测的状态。在我们这个例子中选取的是位置和速度，它也可以是水箱中的水位，汽车引擎的温度，一个用户的手指在平板上划过的位置，或者任何你想要跟踪的数据。

Our robot also has a GPS sensor, which is accurate to about 10 meters, which is good, but it needs to know its location more precisely than 10 meters. There are lots of gullies and cliffs in these woods, and if the robot is wrong by more than a few feet, it could fall off a cliff. So GPS by itself is not good enough.
我们的机器人同时拥有一个 GPS 传感器，精度在 10m。这已经很好了，但是对我们的机器人来说它需要以远高于 10m 的这个精度来定位自己的位置。在机器人所处的树林里有很多溪谷和断崖，如果机器人对位置误判了哪怕只是几步远的距离，它就有可能掉到坑里。所以仅靠 GPS 是不够的。

![Oh no.](https://www.bzarg.com/wp-content/uploads/2015/08/robot_ohnoes-300x283.png)

We might also know something about how the robot moves: It knows the commands sent to the wheel motors, and its knows that if it’s headed in one direction and nothing interferes, at the next instant it will likely be further along that same direction. But of course it doesn’t know everything about its motion: It might be buffeted by the wind, the wheels might slip a little bit, or roll over bumpy terrain; so the amount the wheels have turned might not exactly represent how far the robot has actually traveled, and the prediction won’t be perfect.
同时我们可以获取到一些机器人的运动的信息：驱动轮子的电机指令对我们也有用处。如果没有外界干扰，仅仅是朝一个方向前进，那么下一个时刻的位置只是比上一个时刻的位置在该方向上移动了一个固定距离。当然我们无法获取影响运动的所有信息：机器人可能会受到风力影响，轮子可能会打滑，或者碰到了一些特殊的路况；所以轮子转过的距离并不能完全表示机器人移动的距离，这就导致通过轮子转动预测机器人位置不会非常准确。

The GPS **sensor** tells us something about the state, but only indirectly, and with some uncertainty or inaccuracy. Our **prediction** tells us something about how the robot is moving, but only indirectly, and with some uncertainty or inaccuracy.
GPS 传感器告诉我们一些关于状态的信息，但只是间接的，并且带有一些不确定性或不准确。 我们的预测告诉我们一些关于机器人如何移动的信息，但只是间接的，并且有一些不确定性或不准确。


But if we use all the information available to us, can we get a better answer than **either estimate would give us by itself**? Of course the answer is yes, and that’s what a Kalman filter is for.

## 卡尔曼滤波如何看待你的问题

Let’s look at the landscape we’re trying to interpret. We’ll continue with a simple state having only position and velocity.
$$
\vec{x} = \begin{bmatrix} 
p\\ 
v 
\end{bmatrix}
$$
We don’t know what the *actual* position and velocity are; there are a whole range of possible combinations of position and velocity that might be true, but some of them are more likely than others:
我们不知道位置和速度的准确值；但是我们可以列出一个准确数值可能落在的区间。在这个范围里，一些数值组合的可能性要高于另一些组合的可能性。

<img src="https://pic4.zhimg.com/50/v2-f7931f76177ec93a23b8adfbdbf3c9e1_hd.jpg" style="zoom: 67%;" />

The Kalman filter assumes that both variables (postion and velocity, in our case) are random and *Gaussian distributed.* Each variable has a **mean** value $\mu$, which is the center of the random distribution (and its most likely state), and a **variance** $\sigma^2$, which is the uncertainty:

<img src="https://pic1.zhimg.com/50/v2-8eb051706a22925db4380de43274134d_hd.jpg" style="zoom:67%;" />

In the above picture, position and velocity are **uncorrelated**, which means that the state of one variable tells you nothing about what the other might be.
在上图中位置和速度是无关联的，即系统状态中的一个变量并不会告诉你关于另一个变量的任何信息。

The example below shows something more interesting: Position and velocity are **correlated**. The likelihood of observing a particular position depends on what velocity you have:
下图则展示了一些有趣的事情：在现实中，速度和位置是有关联的。如果已经确定位置的值，那么某些速度值存在的可能性更高。

![](https://pic4.zhimg.com/50/v2-15fa86573f39f47d64e519f013de36bc_hd.jpg?source=1940ef5c)

This kind of situation might arise if, for example, we are estimating a new position based on an old one. If our velocity was high, we probably moved farther, so our position will be more distant. If we’re moving slowly, we didn’t get as far.
假如我们已知上一个状态的位置值，现在要预测下一个状态的位置值。如果我们的速度值很高，我们移动的距离会远一点。相反，如果速度慢，机器人不会走的很远。

This kind of relationship is really important to keep track of, because it gives us **more information:** One measurement tells us something about what the others could be. And that’s the goal of the Kalman filter, we want to squeeze as much information from our uncertain measurements as we possibly can!
这种关系在跟踪系统状态时很重要，因为它给了我们更多的信息：一个测量值告诉我们另一个测量值可能是什么样子。这就是卡尔曼滤波的目的，我们要尽量从所有不确定信息中提取有价值的信息！

This correlation is captured by something called a [covariance matrix](https://en.wikipedia.org/wiki/Covariance_matrix). In short, each element of the matrix $\Sigma_{ij}$ is the degree of correlation between the *ith* state variable and the *jth* state variable. (You might be able to guess that the covariance matrix is [symmetric](https://en.wikipedia.org/wiki/Symmetric_matrix) 对称, which means that it doesn’t matter if you swap *i* and *j*). Covariance matrices are often labelled “$\mathbf{\Sigma}$”, so we call their elements “$\Sigma_{ij}$”.  $\Sigma_{ij}=Cov(x_i,x_j)=E[(x_i-\mu_i)(x_j-\mu_j)]$

<img src="https://pic4.zhimg.com/50/v2-bf3c5cbfd5c791a08121e6f3220b1c4e_hd.jpg" style="zoom:67%;" />

## 利用矩阵描述问题

We’re modeling our knowledge about the state as a Gaussian blob, so we need two pieces of information at time k: We’ll call our best estimate $\mathbf{\hat{x}_k}$ (the mean, elsewhere named $\mu$ ), and its covariance matrix $\mathbf{P_k}$.
$$
\begin{equation} \label{eq:statevars} 
\begin{aligned} 
\mathbf{\hat{x}}_k &= \begin{bmatrix} 
\text{position}\\ 
\text{velocity} 
\end{bmatrix}\\ 
\mathbf{P}_k &= 
\begin{bmatrix} 
\Sigma_{pp} & \Sigma_{pv} \\ 
\Sigma_{vp} & \Sigma_{vv} \\ 
\end{bmatrix} 
\end{aligned} 
\end{equation}
$$
(Of course we are using only position and velocity here, but it’s useful to remember that the state can contain any number of variables, and represent anything you want).

Next, we need some way to look at the <font style="color: #3366ff;">**current state**</font> (at time <font style="color: #3366ff;">**k-1**</font>) and **predict the <font style="color: #ff00ff;">next state</font>** at time <font style="color: #ff00ff;">**k**</font>. Remember, we don’t know which state is the “real” one, but our prediction function doesn’t care. It just works on *all of them*, and gives us a new distribution:
下一步，我们需要通过 k-1 时刻的状态来预测 k 时刻的状态。请注意，我们不知道状态的准确值，但是我们的预测函数并不在乎。它仅仅是对 k-1 时刻所有可能值的范围进行预测转移，然后得出一个 k 时刻新值的范围。

<img src="https://pic2.zhimg.com/50/v2-8170a6981244e9aa2f60d093e0d20a4e_hd.jpg" style="zoom:67%;" />

We can represent this prediction step with a matrix, $\mathbf{F_k}$:

<img src="https://pic1.zhimg.com/50/v2-5810ceee40fd2cf52478551e156ec773_hd.jpg" style="zoom:67%;" />

It takes *every point* in our original estimate and moves it to a new predicted location, which is where the system would move if that original estimate was the right one.
$\mathbf{F_k}$ 把 k-1 时刻所有可能的状态值转移到一个新的范围内，这个新的范围代表了系统新的状态值可能存在的范围，如果 k-1 时刻估计值的范围是准确的话。

Let’s apply this. How would we use a matrix to predict the position and velocity at the next moment in the future? We’ll use a really basic kinematic formula:
$$
\begin{split} 
\color{deeppink}{p_k} &= \color{royalblue}{p_{k-1}} + \Delta t &\color{royalblue}{v_{k-1}} \\ 
\color{deeppink}{v_k} &= &\color{royalblue}{v_{k-1}} 
\end{split}
$$
In other words:
$$
\begin{align} 
\color{deeppink}{\mathbf{\hat{x}}_k} &= \begin{bmatrix} 
1 & \Delta t \\ 
0 & 1 
\end{bmatrix} \color{royalblue}{\mathbf{\hat{x}}_{k-1}} \\ 
&= \mathbf{F}_k \color{royalblue}{\mathbf{\hat{x}}_{k-1}} \label{statevars} 
\end{align}
$$
We now have a **prediction matrix** which gives us our next state, but we still don’t know how to update the covariance matrix.

This is where we need another formula. If we multiply every point in a distribution by a matrix $\color{firebrick}{\mathbf{A}}$, then what happens to its covariance matrix $\Sigma$?
这里我们需要另一个公式。如果我们对每个点进行矩阵 A 转换，它的协方差矩阵Σ会发生什么变化呢？

Well, it’s easy. I’ll just give you the identity:
$$
\begin{equation} 
\begin{split} 
Cov(x) &= \Sigma\\ 
Cov(\color{firebrick}{\mathbf{A}}x) &= \color{firebrick}{\mathbf{A}} \Sigma \color{firebrick}{\mathbf{A}}^T 
\end{split} \label{covident} 
\end{equation}
$$
So combining $\eqref{covident}$ with equation $\eqref{statevars}$:
$$
\begin{equation} 
\begin{split} 
\color{deeppink}{\mathbf{\hat{x}}_k} &= \mathbf{F}_k \color{royalblue}{\mathbf{\hat{x}}_{k-1}} \\ 
\color{deeppink}{\mathbf{P}_k} &= \mathbf{F_k} \color{royalblue}{\mathbf{P}_{k-1}} \mathbf{F}_k^T 
\end{split} 
\end{equation}
$$

## External influence 外界作用力

We haven’t captured everything, though. There might be some changes that **aren’t related to the state** itself— the outside world could be affecting the system.
我们并没有考虑到所有影响因素。系统状态的改变并不只依靠上一个系统状态，外界作用力可能会影响系统状态的变化。

For example, if the state models the motion of a train, the train operator might push on the throttle, causing the train to accelerate. Similarly, in our robot example, the navigation software might issue a command to turn the wheels or stop. If we know this additional information about what’s going on in the world, we could stuff it into a vector called $\color{darkorange}{\vec{\mathbf{u}_k}}$, do something with it, and add it to our prediction as a correction.
例如，跟踪一列火车的运动状态，火车驾驶员可能踩了油门使火车提速。同样，在我们机器人例子中，导航软件可能发出一些指令启动或者制动轮子。如果我们知道这些额外的信息，我们可以通过一个向量 $\color{darkorange}{\vec{\mathbf{u}_k}}$ 来描述这些信息，把它添加到我们的预测方程里作为一个修正。

Let’s say we know the expected acceleration $\color{darkorange}{a}$ due to the throttle setting or control commands. From basic kinematics we get:

$$
\begin{split} 
\color{deeppink}{p_k} &= \color{royalblue}{p_{k-1}} + {\Delta t} &\color{royalblue}{v_{k-1}} + &\frac{1}{2} \color{darkorange}{a} {\Delta t}^2 \\ 
\color{deeppink}{v_k} &= &\color{royalblue}{v_{k-1}} + & \color{darkorange}{a} {\Delta t} 
\end{split}
$$
In matrix form:
$$
\begin{equation} 
\begin{split} 
\color{deeppink}{\mathbf{\hat{x}}_k} &= \mathbf{F}_k \color{royalblue}{\mathbf{\hat{x}}_{k-1}} + \begin{bmatrix} 
\frac{\Delta t^2}{2} \\ 
\Delta t 
\end{bmatrix} \color{darkorange}{a} \\ 
&= \mathbf{F}_k \color{royalblue}{\mathbf{\hat{x}}_{k-1}} + \mathbf{B}_k \color{darkorange}{\vec{\mathbf{u}_k}} 
\end{split} 
\end{equation}
$$
$\mathbf{B}_k$ is called the **control matrix** and $\color{darkorange}{\vec{\mathbf{u}_k}}$ the **control vector.** (For very simple systems with no external influence, you could omit these).

Let’s add one more detail. What happens if our prediction is not a 100% accurate model of what’s actually going on?

## External uncertainty 外界不确定性

Everything is fine if the state evolves based on its own properties. Everything is *still* fine if the state evolves based on external forces, so long as we know what those external forces are.
如果状态只会根据系统自身特性演变那将不会有任何问题。如果我们可以把所有外界作用力对系统的影响计算清楚那也不会有任何问题。

But what about forces that we *don’t* know about? If we’re tracking a quadcopter, for example, it could be buffeted around by wind. If we’re tracking a wheeled robot, the wheels could slip, or bumps on the ground could slow it down. We can’t keep track of these things, and if any of this happens, our prediction could be off because we didn’t account for those extra forces.
但是如果有些外力我们无法预测呢？假如我们在跟踪一个四轴飞行器，它会受到风力影响。如果我们在跟踪一个轮式机器人，轮子可能会打滑，或者地面上的突起会使它降速。我们无法跟踪这些因素，并且这些事情发生的时候上述的预测方程可能会失灵。

We can model the uncertainty associated with the “world” (i.e. things we aren’t keeping track of) by adding some new uncertainty after every prediction step:
我们可以把 “世界” 中的这些不确定性统一建模，在预测方程中增加一个不确定项。

<img src="https://pic1.zhimg.com/50/v2-3e5d0ea136563cb8b15d7e4af4af1bed_hd.jpg" style="zoom:67%;" />

Every state in our original estimate could have moved to a *range* of states. Because we like Gaussian blobs so much, we’ll say that each point in $\color{royalblue}{\mathbf{\hat{x}}_{k-1}}$ is moved to somewhere inside a Gaussian blob with covariance $\color{mediumaquamarine}{\mathbf{Q}_k}$. Another way to say this is that we are treating the untracked influences as **noise** with covariance $\color{mediumaquamarine}{\mathbf{Q}_k}$.

这样，原始状态中的每一个点可以都会预测转换到一个范围，而不是某个确定的点。可以这样描述： $\color{royalblue}{\mathbf{\hat{x}}_{k-1}}$ 中的每个点移动到一个符合方差 $\color{mediumaquamarine}{\mathbf{Q}_k}$ 的高斯分布里。另一种说法，我们把这些不确定因素描述为方差为 $\color{mediumaquamarine}{\mathbf{Q}_k}$ 的高斯噪声。

<img src="https://pic1.zhimg.com/50/v2-1736dbc6bac349f1fd86de3c9e2cc76f_hd.jpg" style="zoom:67%;" />

This produces a new Gaussian blob, with a different covariance (but the same mean):

<img src="https://pic2.zhimg.com/50/v2-63b04b1e1330df6d2248f9272618ba8b_hd.jpg" style="zoom:67%;" />

We get the expanded covariance by simply **adding** ${\color{mediumaquamarine}{\mathbf{Q}_k}}$, giving our complete expression for the **prediction step**:
$$
\begin{equation} 
\begin{split} 
\color{deeppink}{\mathbf{\hat{x}}_k} &= \mathbf{F}_k \color{royalblue}{\mathbf{\hat{x}}_{k-1}} + \mathbf{B}_k \color{darkorange}{\vec{\mathbf{u}_k}} \\ 
\color{deeppink}{\mathbf{P}_k} &= \mathbf{F_k} \color{royalblue}{\mathbf{P}_{k-1}} \mathbf{F}_k^T + \color{mediumaquamarine}{\mathbf{Q}_k} 
\end{split} 
\label{kalpredictfull} 
\end{equation}
$$
In other words, the <font style="color: #ff00ff;"><strong>new best estimate</strong></font> is a <strong>prediction</strong> made from<font style="color: #3366ff;"><strong> previous best estimate</strong></font>, plus a <strong>correction</strong> for <font style="color: #ff9900;"><strong>known external influences</strong></font>.
新的预测转换方程只是引入了已知的可以预测的外力影响因素。

And the <span style="color: #ff00ff;"><strong>new uncertainty</strong></span> is <strong>predicted</strong> from the <span style="color: #3366ff;"><strong>old uncertainty</strong></span>, with some <span style="color: #64cc98;"><strong>additional uncertainty from the environment</strong></span>.
新的不确定性可以通过老的不确定性计算得到，通过增加外界无法预测的、不确定的因素成分。

All right, so that’s easy enough. We have a fuzzy estimate of where our system might be, given by $\color{deeppink}{\mathbf{\hat{x}}_k}$ and $\color{deeppink}{\mathbf{P}_k}$. What happens when we get some data from our sensors?

## Refining the estimate with measurements 通过测量值精炼预测值

We might have several sensors which give us information about the state of our system. For the time being it doesn’t matter what they measure; perhaps one reads position and the other reads velocity. Each sensor tells us something **indirect** about the state— in other words, the sensors operate on a state and produce a set of **readings**.
我们可能还有一些传感器来测量系统的状态。目前我们不用太关心所测量的状态变量是什么。也许一个测量位置一个测量速度。每个传感器可以提供一些关于系统状态的数据信息，每个传感器检测一个系统变量并且产生一些读数。

<img src="https://pic1.zhimg.com/50/v2-b7e5edbd408b664a2ec07e95d0794227_hd.jpg" style="zoom:67%;" />

Notice that the units and scale of the reading might not be the same as the units and scale of the state we’re keeping track of. You might be able to guess where this is going: We’ll model the sensors with a matrix, $\mathbf{H}_k$.
注意传感器测量的范围和单位可能与我们跟踪系统变量所使用的范围和单位不一致。我们需要对传感器做下建模：通过矩阵 $\mathbf{H}_k$

<img src="https://pic2.zhimg.com/50/v2-e014690db550baa5baaf62d1d6c21e55_hd.jpg" style="zoom:67%;" />

We can figure out the distribution of sensor readings we’d expect to see in the usual way:
$$
\begin{equation} 
\begin{aligned} 
\vec{\mu}_{\text{expected}} &= \mathbf{H}_k \color{deeppink}{\mathbf{\hat{x}}_k} \\ 
\mathbf{\Sigma}_{\text{expected}} &= \mathbf{H}_k \color{deeppink}{\mathbf{P}_k} \mathbf{H}_k^T 
\end{aligned} 
\end{equation}
$$
One thing that Kalman filters are great for is dealing with *sensor noise*. In other words, our sensors are at least somewhat unreliable, and every state in our original estimate might result in a *range* of sensor readings.
卡尔曼滤波也可以处理传感器噪声。换句话说，我们的传感器有自己的精度范围，对于一个真实的位置和速度，传感器的读数受到高斯噪声影响会使读数在某个范围内波动。

<img src="https://pic1.zhimg.com/50/v2-aaa22652895229c41402d46df09359b9_hd.jpg" style="zoom:67%;" />

From each reading we observe, we might guess that our system was in a particular state. But because there is uncertainty, **some states are more likely than others** to have have produced the reading we saw:
我们观测到的每个数据，可以认为其对应某个真实的状态。但是因为存在不确定性，某些状态的可能性比另外一些可能性更高。

<img src="https://pic1.zhimg.com/50/v2-8f86b40e297d206f439c2c3653357c77_hd.jpg" style="zoom:67%;" />

We’ll call the **covariance** of this uncertainty (i.e. of the sensor noise) $\color{mediumaquamarine}{\mathbf{R}_k}$. The distribution has a **mean** equal to the reading we observed, which we’ll call $\color{yellowgreen}{\vec{\mathbf{z}_k}}$.

So now we have two Gaussian blobs: One surrounding the mean of our transformed prediction, and one surrounding the actual sensor reading we got.

<img src="https://pic1.zhimg.com/50/v2-954bea3147c72502022920b95819621b_hd.jpg" style="zoom:67%;" />

We must try to reconcile our guess about the readings we’d see based on the <strong>predicted state</strong> (<span style="color: #ff00ff;"><strong>pink</strong></span>) with a <em>different</em> guess based on our <strong>sensor readings</strong> (<span style="color: #99cc00;"><strong>green</strong></span>) that we actually observed.
我们必须尝试去把两者的数据预测值（粉色）与观测值（绿色）融合起来。

So what’s our new most likely state? For any possible reading $(z_1,z_2)$, we have two associated probabilities: <font style="color: #99cc00;">(1)</font> The probability that our sensor reading $\color{yellowgreen}{\vec{\mathbf{z}_k}}$ is a (mis-)measurement of $(z_1,z_2)$, and <font style="color: #ff00ff">(2)</font> the probability that our previous estimate thinks $(z_1,z_2)$ is the reading we should see.
所以我们得到的新的数据会长什么样子呢？对于任何状态 $(z_1,z_2)$，我们有两个可能性：（1）传感器读数更接近系统真实状态（2)预测值更接近系统真实状态。

If we have two probabilities and we want to know the chance that *both* are true, we just multiply them together. So, we take the two Gaussian blobs and multiply them:
如果我们有两个相互独立的获取系统状态的方式，并且我们想知道两者都准确的概率值，我们只需要将两者相乘。所以我们将两个高斯斑相乘。

<img src="https://pic2.zhimg.com/50/v2-5872b13eb9fc87370a8decd02cdf0012_hd.jpg" style="zoom:67%;" />

What we’re left with is the **overlap**, the region where *both* blobs are bright/likely. And it’s a lot more precise than either of our previous estimates. The mean of this distribution is the configuration for which **both estimates are most likely**, and is therefore the **best guess** of the true configuration given all the information we have.
相乘之后得到的即为重叠部分，这个区域同时属于两个高斯斑。并且比单独任何一个区域都要精确。这个区域的平均值取决于我们更取信于哪个数据来源，这样我们也通过我们手中的数据得到了一个最好的估计值。

Hmm. This looks like another Gaussian blob.

<img src="https://pic2.zhimg.com/50/v2-3c5dcbb045192ae881e94e732d29eadc_hd.jpg" style="zoom:67%;" />

As it turns out, when you multiply two Gaussian blobs with separate means and covariance matrices, you get a *new* Gaussian blob with its **own** mean and covariance matrix! Maybe you can see where this is going: There’s got to be a formula to get those new parameters from the old ones!
已经被证明，当你对两个均值方差都不相同的侧高斯斑进行相乘，你可以得到一个新的高斯斑。你可以自行推导：新高斯分布的均值和方差均可以通过老的均值方差求得。

## Combining Gaussians

Let’s find that formula. It’s easiest to look at this first in **one dimension**. A 1D Gaussian bell curve with variance $\sigma^2$ and mean $\mu$ is defined as:
$$
\begin{equation} \label{gaussformula} 
\mathcal{N}(x, \mu,\sigma) = \frac{1}{ \sigma \sqrt{ 2\pi } } e^{ -\frac{ (x – \mu)^2 }{ 2\sigma^2 } } 
\end{equation}
$$
We want to know what happens when you multiply two Gaussian curves together. The blue curve below represents the (unnormalized) intersection of the two Gaussian populations:
我们想知道两个高斯分布相乘会发生什么。蓝色曲线代表了两个高斯分布的交集部分。

<img src="https://pic4.zhimg.com/50/v2-855027a453eb445e25ba3b8d9b11695c_hd.jpg" style="zoom:67%;" />
$$
\begin{equation} \label{gaussequiv} 
\mathcal{N}(x, \color{fuchsia}{\mu_0}, \color{deeppink}{\sigma_0}) \cdot \mathcal{N}(x, \color{yellowgreen}{\mu_1}, \color{mediumaquamarine}{\sigma_1}) \stackrel{?}{=} \mathcal{N}(x, \color{royalblue}{\mu’}, \color{mediumblue}{\sigma’}) 
\end{equation}
$$
You can substitute equation $\eqref{gaussformula}$ into equation $\eqref{gaussequiv}$ and do some algebra (being careful to renormalize, so that the total probability is 1) to obtain:

$$
\begin{equation} \label{fusionformula} 
\begin{aligned} 
\color{royalblue}{\mu’} &= \mu_0 + \frac{\sigma_0^2 (\mu_1 – \mu_0)} {\sigma_0^2 + \sigma_1^2}\\ 
\color{mediumblue}{\sigma’}^2 &= \sigma_0^2 – \frac{\sigma_0^4} {\sigma_0^2 + \sigma_1^2} 
\end{aligned} 
\end{equation}
$$
We can simplify by factoring out a little piece and calling it $\color{purple}{\mathbf{k}}$:

$$
\begin{equation} \label{gainformula} 
\color{purple}{\mathbf{k}} = \frac{\sigma_0^2}{\sigma_0^2 + \sigma_1^2} 
\end{equation}
$$

$$
\begin{equation} 
\begin{split} 
\color{royalblue}{\mu’} &= \mu_0 + &\color{purple}{\mathbf{k}} (\mu_1 – \mu_0)\\ 
\color{mediumblue}{\sigma’}^2 &= \sigma_0^2 – &\color{purple}{\mathbf{k}} \sigma_0^2 
\end{split} \label{update} 
\end{equation}
$$

Take note of how you can take your previous estimate and **add something** to make a new estimate. And look at how simple that formula is!

But what about a matrix version? Well, let’s just re-write equations $\eqref{gainformula}$ and $\eqref{update}$ in matrix form. If $\Sigma$ is the covariance matrix of a Gaussian blob, and $\vec{\mu}$ its mean along each axis, then:

$$
\begin{equation} \label{matrixgain} 
\color{purple}{\mathbf{K}} = \Sigma_0 (\Sigma_0 + \Sigma_1)^{-1} 
\end{equation}
$$

$$
\begin{equation} 
\begin{split} 
\color{royalblue}{\vec{\mu}’} &= \vec{\mu_0} + &\color{purple}{\mathbf{K}} (\vec{\mu_1} – \vec{\mu_0})\\ 
\color{mediumblue}{\Sigma’} &= \Sigma_0 – &\color{purple}{\mathbf{K}} \Sigma_0 
\end{split} \label{matrixupdate} 
\end{equation}
$$

$\color{purple}{\mathbf{K}}$ is a matrix called the **Kalman gain**, and we’ll use it in just a moment.

Easy! We’re almost finished!

## Putting it all together

We have two distributions: The predicted measurement with $(\color{fuchsia}{\mu_0}, \color{deeppink}{\Sigma_0}) = (\color{fuchsia}{\mathbf{H}_k \mathbf{\hat{x}}_k}, \color{deeppink}{\mathbf{H}_k \mathbf{P}_k \mathbf{H}_k^T})$, and the observed measurement with $(\color{yellowgreen}{\mu_1}, \color{mediumaquamarine}{\Sigma_1}) = (\color{yellowgreen}{\vec{\mathbf{z}_k}}, \color{mediumaquamarine}{\mathbf{R}_k})$. We can just plug these into equation $\eqref{matrixupdate}$ to find their overlap:
$$
\begin{equation} 
\begin{aligned} 
\mathbf{H}_k \color{royalblue}{\mathbf{\hat{x}}_k’} &= \color{fuchsia}{\mathbf{H}_k \mathbf{\hat{x}}_k} & + & \color{purple}{\mathbf{K}} ( \color{yellowgreen}{\vec{\mathbf{z}_k}} – \color{fuchsia}{\mathbf{H}_k \mathbf{\hat{x}}_k} ) \\ 
\mathbf{H}_k \color{royalblue}{\mathbf{P}_k’} \mathbf{H}_k^T &= \color{deeppink}{\mathbf{H}_k \mathbf{P}_k \mathbf{H}_k^T} & – & \color{purple}{\mathbf{K}} \color{deeppink}{\mathbf{H}_k \mathbf{P}_k \mathbf{H}_k^T} 
\end{aligned} \label {kalunsimplified} 
\end{equation}
$$
And from $\eqref{matrixgain}$, the Kalman gain is:

$$
\begin{equation} \label{eq:kalgainunsimplified} 
\color{purple}{\mathbf{K}} = \color{deeppink}{\mathbf{H}_k \mathbf{P}_k \mathbf{H}_k^T} ( \color{deeppink}{\mathbf{H}_k \mathbf{P}_k \mathbf{H}_k^T} + \color{mediumaquamarine}{\mathbf{R}_k})^{-1} 
\end{equation}
$$
We can knock an $\mathbf{H}_k$ off the front of every term in $\eqref{kalunsimplified}$ and $\eqref{eq:kalgainunsimplified}$ (note that one is hiding inside $\color{purple}{\mathbf{K}}$ ), and an $\mathbf{H}_k^T$ off the end of all terms in the equation for $\color{royalblue}{\mathbf{P}_k’}$.
$$
\begin{equation} 
\begin{split} 
\color{royalblue}{\mathbf{\hat{x}}_k’} &= \color{fuchsia}{\mathbf{\hat{x}}_k} & + & \color{purple}{\mathbf{K}’} ( \color{yellowgreen}{\vec{\mathbf{z}_k}} – \color{fuchsia}{\mathbf{H}_k \mathbf{\hat{x}}_k} ) \\ 
\color{royalblue}{\mathbf{P}_k’} &= \color{deeppink}{\mathbf{P}_k} & – & \color{purple}{\mathbf{K}’} \color{deeppink}{\mathbf{H}_k \mathbf{P}_k} 
\end{split} 
\label{kalupdatefull} 
\end{equation}
$$

$$
\begin{equation} 
\color{purple}{\mathbf{K}’} = \color{deeppink}{\mathbf{P}_k \mathbf{H}_k^T} ( \color{deeppink}{\mathbf{H}_k \mathbf{P}_k \mathbf{H}_k^T} + \color{mediumaquamarine}{\mathbf{R}_k})^{-1} 
\label{kalgainfull} 
\end{equation}
$$

…giving us the complete equations for the **update step.**

And that’s it! $\color{royalblue}{\mathbf{\hat{x}}_k’}$ is our new best estimate, and we can go on and feed it (along with $\color{royalblue}{\mathbf{P}_k’}$ ) back into another round of **predict** or **update** as many times as we like.

<img src="https://pic2.zhimg.com/50/v2-77564014db4bb16a4cdb87334c77df92_hd.jpg" style="zoom:67%;" />

## Wrapping up

Of all the math above, all you need to implement are equations $\eqref{kalpredictfull}$, $\eqref{kalupdatefull}$, and $\eqref{kalgainfull}$. (Or if you forget those, you could re-derive everything from equations $\eqref{covident}$ and $\eqref{matrixupdate}$.)

This will allow you to model any linear system accurately. For nonlinear systems, we use the **extended Kalman filter**, which works by simply linearizing the predictions and measurements about their mean. (I may do a second write-up on the EKF in the future).

If I’ve done my job well, hopefully someone else out there will realize how cool these things are and come up with an unexpected new place to put them into action.

Some credit and referral should be given to [this fine document](http://www.cl.cam.ac.uk/~rmf25/papers/Understanding the Basis of the Kalman Filter.pdf), which uses a similar approach involving overlapping Gaussians. More in-depth derivations can be found there, for the curious.