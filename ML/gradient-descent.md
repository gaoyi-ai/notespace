---
title: gradient descent
categories:
- Optimization
tags:
- gradient descent
- momentum
- nesterov
date: 2021/3/7 10:00:00
updated: 2021/3/11 16:00:00
---

# gradient descent

## 关于步长的讨论(一元函数的情况)

梯度下降方法中$d_k = -g_k$,此时如果要求解精确的步长$\alpha_k$,问题可以描述为:
$$
\alpha^{*}=\underset{\alpha}{\arg \min } f\left(\boldsymbol{x}_{k}+\alpha \boldsymbol{d}_{k}\right)
$$

要求解上式,只要以α为变量求解方程

$$
g\left(\boldsymbol{x}_{k}+\alpha \boldsymbol{d}_{k}\right)=0
$$
但是函数$f(x)$有可能非常复杂,以至于我们无法求解方程

但是我们可以利用$f(x)$的二阶 Taylor展开式来进行近似求解
$$
f\left(x_{k}+\alpha d_{k}\right)=f_{k}+\alpha g_{k} d_{k}+\frac{1}{2} \alpha^{2} G_{k} d_{k}^{2}+o\left(\left\|d_{k}\right\|^{2}\right)
$$
我们在求解的时候忽略$o\left(\left\|d_{k}\right\|^{2}\right)$项，其中g为一阶导，G为二阶导。

我们对关于α函数求导数
$$
f\left(x_{k}+\alpha d_{k}\right) \approx f_{k}+\alpha g_{k} d_{k}+\frac{1}{2} \alpha^{2} G_{k} d_{k}^{2} \\
\frac{d f\left(x_{k}+\alpha d_{k}\right)}{d \alpha}=g_{k} d_{k}+\alpha G_{k} d_{k}^{2} \equiv 0
$$
得到精确的步长值:$\alpha=-\frac{g_{k} d_{k}}{G_{k} d_{k}^{2}}$

## 多元函数的一阶导数和二阶导数

多元函数的一阶导数$g(x)$和二阶导数$G(x)$
$$
g(\boldsymbol{x})=\left[\begin{array}{c}
\frac{\partial f(\boldsymbol{x})}{\partial x_{1}}\\
\frac{\partial f(\boldsymbol{x})}{\partial x_{2}} \\
{\vdots} \\
\frac{\partial f(\boldsymbol{x})}{\partial x_{n}}
\end{array}\right]\in \mathbb{R}^{n},
$$

$$
G(x)=\left[\begin{array}{cccc}
\frac{\partial^{2} f(x)}{\partial x_{1}^{2}} & \frac{\partial^{2} f(x)}{\partial x_{1} \partial x_{2}} & \cdots & \frac{\partial^{2} f(x)}{\partial x_{1} \partial x_{n}} \\
\frac{\partial^{2} f(x)}{\partial x_{2} \partial x_{1}} & \frac{\partial^{2} f(x)}{\partial x_{2}^{2}} & \cdots & \frac{\partial^{2} f(x)}{\partial x_{2} \partial x_{n}} \\
\vdots & \vdots & \ddots & \vdots \\
\frac{\partial^{2} f(x)}{\partial x_{n} \partial x_{1}} & \frac{\partial^{2} f(x)}{\partial x_{n} \partial x_{2}} & \cdots & \frac{\partial^{2} f(x)}{\partial x_{n}^{2}}
\end{array}\right] \in \mathbb{R}^{n \times n}
$$

例：有标量函数$f(\boldsymbol{x})=x^2_1+50x^2_2$,其一阶导数和二阶导数分别为
$$
g(\boldsymbol{x})=\left[\begin{array}{c}
2 x_{1} \\
100 x_{2}
\end{array}\right], \quad G(\boldsymbol{x})=\left[\begin{array}{cc}
2 & 0 \\
0 & 100
\end{array}\right]
$$
注意该函数可以写为向量化的表示
$$
f(x) = \frac{1}{2}\boldsymbol{x^T}\boldsymbol{G}\boldsymbol{x},G=\left[\begin{array}{cc}
2 & 0 \\
0 & 100
\end{array}\right]
$$

## 精确求解多元函数梯度下降的步长

梯度下降法在每步迭代时要分别确定迭代方向$d_k$和步长$a_k$

迭代方向为$d_k=-g_k$,如果要求能够沿迭代方向下降最快的步长$\alpha$,可以看做一个子问题
$$
\alpha_k = \arg \min_\alpha f(x_k+\alpha d_k)
$$
上面的优化目标函数是一个一元函数,最直接的思路就是令其导数为0,然后求解一元方程。

然而,目标函数的导数可能会非常复杂,从而导致求解α困难。

因此,通常利用目标函数在$x_k$处的二阶 Taylor展式来近似
$$
\begin{aligned}
&f\left(\boldsymbol{x}_{k}+\alpha \boldsymbol{d}_{k}\right) \approx f_{k}+\alpha \boldsymbol{g}_{k}^{T} \boldsymbol{d}_{k}+\frac{1}{2} \alpha^{2} \boldsymbol{d}_{k}^{T} \boldsymbol{G}_{k} \boldsymbol{d}_{k}
\end{aligned}
$$
因为在梯度下降法中 $\boldsymbol{d}_{k}=-\boldsymbol{g}_{k}$, 因此上式可以写成： 
$$
f\left(\boldsymbol{x}_{k}+\alpha \boldsymbol{d}_{k}\right) \approx f_{k}-\alpha \boldsymbol{g}_{k}^{T} \boldsymbol{g}_{k}+\frac{1}{2} \alpha^{2} \boldsymbol{g}_{k}^{T} \boldsymbol{G}_{k} \boldsymbol{g}_{k}\\
\frac{d f\left(\boldsymbol{x}_{k}+\alpha \boldsymbol{d}_{k}\right)}{d \alpha} \approx-\boldsymbol{g}_{k}^{T} \boldsymbol{g}_{k}+\alpha \boldsymbol{g}_{k}^{T} \boldsymbol{G}_{k} \boldsymbol{g}_{k}=0
$$

$$
\alpha = \frac{\boldsymbol{g}_{k}^{T} \boldsymbol{g}_{k}} 
{\boldsymbol{g}_{k}^{T} \boldsymbol{G}_{k} \boldsymbol{g}_{k}}
$$

## 固定步长的梯度下降法的分析(多元函数的情况)

步长的取值对算法是否收敛极为重要,选取的不好会导致收敛速度慢,甚至发散。

如果能够让迭代步尽量向最优解的目标前进,而不是进行大幅度的震荡,会对迭代的结果更有好处。

在我们的例子中相邻的两次迭代,迭代方向的x轴的方向没有变化,y轴的方向不断发生变化

### Momentum方法

可以组合相邻两步迭代的迭代方向来增強x方向的变化、降低y方向的变化。
出于这种考虑,出现了 Momentum方法。

Momentum方法的迭代过程为

- $\boldsymbol{x}_{k+1}=\boldsymbol{x}_{k}+\boldsymbol{d}_{k}^{\text {Momentum }}$

- 其中 $\boldsymbol{d}_{k}^{\text {Momentum }}=\alpha_{k} \boldsymbol{d}_{k}+$ discount $\times \boldsymbol{d}_{k-1}^{\text {Momentum }},$ 其中 $\alpha_{k}$ 是固定的步长, discount $\in(0,1)$ 。
- $\boldsymbol{d}_{0}^{\text {Momentum }}=\mathbf{0}$

### Nesterov Momentum方法

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210308151847776.png" alt="image-20210308151847776" style="zoom:67%;" />

- 首先按照上一步迭代的方向更新一步(蓝色线)。
- 然后计算这个位置的梯度方向(红色线)。
- 用蓝色线和红色线计算本次迭代的方向(绿色线)

# Gradient descent with momentum

momentum 有冲量、动量的意思。那么我们在什么情况下使用动量呢？将动量引入梯度下降法能够使模型在 training_data 上得到更好的结果。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-526c3b77bd4d32b37b72b229819bcf58_r.jpg" style="zoom:67%;" />

我们选取损失函数，使用梯度下降算法来搜索使得损失函数最小的参数值。但是由于损失函数有时候会非常复杂，所以在很多时候我们求解到的并不是 global minima（全局最小值）。

在上图的三个位置它们并不是全局最小值，但是它们的梯度值为 0，梯度值为 0 在应用梯度下降法的时候其中的参数得不到更新操作，也就是会陷在这些不是全局最优解的地方，这显然不是我们想要的结果。

三个地方代表的三种伪最优解分别是：

1.  plateau(稳定的水平)；
2.  saddle point(鞍点)；
3.  local minima(局部最小点)；但是不用太担心 local minimal 的问题，其实在复杂的神经网络上没有太多的 local minimal，因为你要有一个 local minimal 都要是一个山谷的形状，我们假设出现谷底的概率是 p，那因为我们的 neural 有非常非常多的参数，所以假设有 1000 参数，假设你每一个参数都是山谷的谷底，那么就是 $p^{1000}$ 。 neural 越大，你的参数就越大，出现 local minimal 的概率就越低。所以 local minimal 在一个很大的 neural 里面并没有那么多 local minimal。那走走你觉的是 local minimal 卡住的时候，他八成是 global minimal 或者很接近 global minimal 的。

那有没有什么方法能够解决这种问题呢 (通过现实生活中得到灵感)？

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-2f12fe47b0a2dba815cdc657d0cfe9fa_r.jpg" style="zoom:67%;" />

上面是一种物理现象，我们可以想象把一个小球放在这种现实的轨道上，这个小球并不会在上面提到的三个伪最优解（梯度为 0）的地方停下来。因为在现实中事物都是有惯性的，也就是动量。如果我们想要解决上面的问题，很自然的就会想能不能也给我们的梯度下降法加上一个动量呢？让它在这些伪最优解的地方能够像现实生活中的小球那样冲出去。

接下来看一看普通的梯度下降法以及加了动量的梯度下降法的具体迭代路径：

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-893512da8d4a034354fa85f86233db62_b.jpg" style="zoom:50%;" />

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-893512da8d4a034354fa85f86233db62_b.jpg" style="zoom:50%;" />

通过两个路径曲线的对比，很直观的感觉使用动量的路径曲线：

1.  振荡的幅度变小了；
2.  而且到达一定地点的时间变短了；

接下来再感受一下动量梯度下降法的其它优点：

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-6b401625c74ab21baf946f2bf37b2fae_r.jpg" style="zoom:67%;" />

**当然不是说使用了动量就一定可以达到最优解。**下面说明一下这几个点 (红色代表梯度下降的方向，虚线绿色代表动量的方向，蓝色代表实际移动的方向)：

1.  对于第一个点来说。梯度下降的方向是往右的，但是由于我们设置的 $v_{0}=0$ ，所以初始时并没有动量的作用，所以此时实际移动的方向就是梯度下降的方向。
2.  对于第二个点来说。梯度下降的方向是向右的，但是此时球现在还有一个向右的动量，这个动量会使小球继续往右移动。
3.  对于第三个点来说。由于此时是 local minima，所以此时的梯度值为 0。如果对于普通的梯度下降来说，他就会卡在这个地方。但是我们还有向右的一个动量值，所以使用动量的话，实际是会向右边继续走。
4.  **对于第四个点来说。**此时我们的梯度下降的方向是向左的，我们可以假设如果此处的动量值 > 梯度的值。此时计算，在此处小球就会朝着动量的方向继续走，他甚至可以冲出山峰，跳出 local minima。

当然这只是动量的其中一个好处：可以很轻松的跳出伪最优解。下面还有一个好处，使用动量梯度下降法（gradient descent with momentum），其速度会比传统的梯度下降算法快的多。我们不论是使用批梯度下降法还是使用小批量梯度下降法，寻找最优解的道路通常都是曲折的，也就是下面这种情况。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-bd3f77b54ee1eb88fe9e8be4a0d3350b_r.jpg)

我们期望让寻找最优解的曲线能够不那么振荡、波动，希望让他能够更加的平滑，在水平方向的速度更快。那怎么去做呢?

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-b7c350e769446f907b1b04724da68ef8_180x120.jpg)

使用能够使得曲线不那么振荡技术，也就是指数移动加权平均。这就是我们动量技术的原理所在。下面正式的给出动量的公式：

$$
V_{dW}+=+\beta+V_{dW}+(1-\beta)dW \\
V_{db}+=+\beta+V_{db}+(1-\beta)db
$$
上面说过使用动量好处是可以很轻松的跳出伪最优解，其实对于在速度上的解释，就完全可以通过移动加权平均来进行解释。详细的可以看上面的指数移动平均。

现在，我们对动量法的速度变量做变形：

$\boldsymbol{v}_t \leftarrow \gamma \boldsymbol{v}_{t-1} + (1 - \gamma) \left(\frac{\eta_t}{1 - \gamma} \boldsymbol{g}_t\right).$

由指数加权移动平均的形式可得，速度变量$\boldsymbol{v}_t$实际上对序列$\{\eta_{t-i}\boldsymbol{g}_{t-i} /(1-\gamma):i=0,\ldots,1/(1-\gamma)-1\}$做了指数加权移动平均。换句话说，相比于小批量随机梯度下降，动量法在每个时间步的自变量更新量近似于将前者对应的最近$1/(1-\gamma)$个时间步的更新量做了指数加权移动平均后再除以$1-\gamma$。所以，在动量法中，自变量在各个方向上的移动幅度不仅取决于当前梯度，还取决于过去的各个梯度在各个方向上是否一致。在本节之前示例的优化问题中，所有梯度在水平方向上为正（向右），而在竖直方向上时正（向上）时负（向下）。这样，我们就可以使用较大的学习率，从而使自变量向最优解更快移动。

### 动量法 (Momentum)

普通的梯度下降法解决常规问题就足够了，如线性回归，但当问题变复杂，普通的梯度下降法就会面临很多局限。具体来说，对于普通的梯度下降法公式 $\theta\leftarrow\theta-\eta\nabla_{\theta}J(\theta)$ ， $\eta$ 表示学习率，含义是每一时间步上梯度调整的步长（step-size）。当接近最优值时梯度会比较小，由于学习率固定，普通的梯度下降法的收敛速度会变慢，有时甚至陷入局部最优。这时如果考虑历史梯度，将会引导参数朝着最优值更快收敛，这就是动量算法的基本思想。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-4af7c822795fff5bb541d94c217fd3c7_r.jpg" style="zoom:67%;" />

结合物理学上的动量思想，在梯度下降问题中引入动量项 m 和折扣因子 $\gamma$ ，公式变为 $m\leftarrow\gamma m+\eta\nabla_{\theta}J(\theta)$ ，$ \theta \leftarrow\theta-m$ 。其中， $\gamma$ 表示历史梯度的影响力， $\gamma$ 越大，历史梯度对现在的影响也越大。直观上来说，要是当前时刻的梯度与历史梯度方向趋近，这种趋势会在当前时刻加强，否则当前时刻的梯度方向减弱。若用 $G_{t}$ 表示第 t 轮迭代的动量， $g_t=\eta\nabla_{\theta}J(\theta)$ 表示第 t 轮迭代的更新量，当 $t\rightarrow\infty$ ， $G_\infty=\frac{g_0}{1-\gamma}$ ，该式的含义是如果梯度保持不变，最终的更新速度会是梯度项乘以学习率的 $\frac{1}{1-\gamma}$ 倍。举例来说， $\gamma=0.9$ 时，动量算法最终的更新速度是普通梯度下降法的 10 倍，意味着在穿越 “平原” 和“平缓山谷”从而摆脱局部最小值时，动量法更有优势。

考虑一个二元函数的例子， $z=x^2+50y^2$ ，利用动量法求最小值。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-703b5fa2da4b1fefac338e5b526f2cac_r.jpg)

$\eta=0.016,\gamma=0.7$ 时，在等高线图上的曲线如下：

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-46b737c92e2c8e95a7a35dca4e270b42_r.jpg" style="zoom:33%;" />

$\eta=0.016,\gamma=0.9$ 时，曲线如下：

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-a70c89568ba0bd25383a993e3ab6618f_r.jpg" style="zoom:33%;" />

观察比较（a）（b）时可知，当折扣率变大时，对历史梯度的记忆更多，下一步梯度方向会没这么容易改变过来（从图上直观理解，（b）扭转程度不如（a），即（b）的震荡更明显）。

### 牛顿动量（Nesterov）算法

观察上图（b）可发现，尽管算法最终找到了最优值，但在过程中由于 y 方向的梯度比 x 方向大很多，所以轨迹在 y 方向存在强烈的震荡抖动。于是 Yurii Nesterov 在 1983 年提出了牛顿动量法，改进在于不是在 $\theta$ 做梯度更新，而是前瞻一步，超前一个动量单位处： $\theta+\beta m$ 。则梯度修正公式变为： $m\leftarrow \gamma m+\eta\nabla_{\theta}J(\theta+\beta m)$ ， $\theta\leftarrow \theta-m$ 。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-192f31039957e2ac04d7b7e1b057383d_r.jpg" style="zoom:50%;" />

在上图中， $\nabla_1$ 表示普通动量法的梯度更新向量， $\nabla_2$ 表示 Nesterov 梯度更新向量。直观分析可知， $\nabla_1$ 会比 $\nabla_2$ 超前，即牛顿动量法更新会比动量法快。另一方面，当 $\gamma+m$ 项越过最优点（optimum）时， $\eta \nabla_1$ 指向另一侧梯度上升方向，而 $\eta \nabla_2$ 指向梯度下降方向，故牛顿动量法能减弱震荡。

同样以之前的二元函数做例子，使用 Nesterov 法参数设置 $\eta=0.012,\gamma=0.7$ 时效果如下：

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-3f57e8d5a47270fefe1af7f8337c0639_r.jpg" style="zoom:33%;" />

和动量方法的区别在于二者用到了不同点的梯度，动量方法采用的是上一步 $\theta_{t-1}$ 的梯度方向，而Nesterov则是从 $\theta_{t-1}$ 朝着 $v_{t-1}$ 往前一步。 一种解释是，反正要朝着  $v_{t-1}$方向走，不如先利用了这个信息，这个叫未卜先知。 

### 自然梯度法（Natural Gradient Descent）

当优化问题的两个坐标轴尺度差异较大时，动量法在更新过程中会出现震荡问题，Nesterov 算法给出了初步解决，但这两种方法有一个共性，就是都是从参数的角度去优化模型的，那有没有可能从模型本身角度来考虑呢？——这就是自然梯度法。在强化学习的 Natural Actor-Critic 算法和 TRPO 算法中，自然梯度法是强有力的优化工具。

自然梯度法用到 Fisher 信息矩阵（Fisher Information Matrix）对 [KL 散度](https://zhuanlan.zhihu.com/p/52147425)进行近似表示，首先介绍一下 Fisher 信息矩阵。 $E_{J(\theta)}$ 表示以 $J(\theta)$ 概率计算得到的期望，Fisher 信息矩阵定义为 $F_{\theta}=E_{J(\theta)}[\nabla_{\theta}logJ(\theta)\nabla_{\theta}logJ(\theta)^T])$。以 KL 散度表示两个模型之间的距离，有近似关系式 $KL(J(\theta)||J(\theta+\Delta\theta))\approx \frac{1}{2}\Delta\theta^TF_{\theta}\Delta\theta$ 。以参数视角看待梯度下降时，可以把每一轮的优化看作这样一个子优化问题：

$min_{\Delta\theta}J(\theta+\Delta\theta)\approx J(\theta)+\nabla_{\theta}J(\theta)\Delta\theta$

$s.t.|\Delta \theta|<\epsilon$

使用自然梯度法以模型距离视角看待问题时，条件限制项变为了 $s.t.KL(J(\theta)||J(\theta+\Delta\theta))<\epsilon$

以 Fisher 矩阵替代并采用拉格朗日乘子法解带约束的优化问题，原问题变为

$min_{\Delta\theta}J(\theta)+\nabla_{\theta}J(\theta)\Delta\theta+\lambda(\frac{1}{2}\Delta\theta^TF_{\theta}\Delta\theta-\epsilon)$

对 $\Delta{\theta}$ 求导，得 $\Delta \theta=-\frac{1}{\lambda}F_{\theta}^{-1}\nabla_{\theta}J(\theta)$ ，故优化方向可以看作 $F_{\theta}^{-1}\nabla_{\theta}J(\theta)$ 的反方向。

时隔一年，终于把代码传上来啦：

[https://github.com/zhengsizuo/Deep-Learning-Note/blob/master/basic%20theory/optimization_algorithm.py​github.com](https://github.com/zhengsizuo/Deep-Learning-Note/blob/master/basic%20theory/optimization_algorithm.py)

**_参考：_**《Hands-On Machine Learning with Scikit-Learn and TensorFlow》第 11 章