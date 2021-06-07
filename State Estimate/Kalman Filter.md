---
title: 细说Kalman滤波：The Kalman Filter
categories:
- Probabilistic Robotics
- State Estimation
- Kalman Filter
tags:
- Kalman filter
date: 2021/6/6 20:00:09
updated: 2021/6/6 12:00:09
---

> [细说Kalman滤波：The Kalman Filter](https://www.cnblogs.com/ycwang16/p/5999034.html)

# Kalman 滤波

##  Beyes 滤波

Bayes滤波分为两步：**1.状态预测**；和 **2.状态更新**

1. 状态预测，基于状态转移模型：


$$
\overline{b e l}\left(x_{t}\right)=\int p\left(x_{t} \mid u_{t}, x_{t-1}\right) \operatorname{bel}\left(x_{t-1}\right) d x_{t-1}
$$

2. 状态更新，基于新的观测

$$
\operatorname{bel}\left(x_{t}\right)=\eta p\left(z_{t} \mid x_{t}\right) \overline{b e l}\left(x_{t}\right)
$$

我们可以看到，我们的目的是计算$x_t$的后验概率，如果$\operatorname{bel}\left(x_{t}\right)$是任意分布，我们需要在$x_t$的所有可能取值点上，计算该取值的概率，这在计算上是难于实现的。这一计算问题可以有多种方法来近似，比如利用采样的方法，就是后面要讲的粒子滤波和无迹Kalman滤波。

这节要说的近似方法是，当假设$\operatorname{bel}\left(x_{t}\right)$服从Gauss分布，那么我们只需要分布的均值和方差就可以完全描述$\operatorname{bel}\left(x_{t}\right)$，而无需在$x_t$的每个可能取值点上进行概率计算。这也是用高斯分布来近似$\operatorname{bel}\left(x_{t}\right)$的好处，因为我们在每一个时刻，只需要计算均值μtμt和方差ΣtΣt这两个数值，就可以对$\operatorname{bel}\left(x_{t}\right)$完全描述，所以我们就可以推导出这两个数值的递推公式，从而在每个时刻由这两个数值的递推公式完全获得状态估计，这就是The Kalman Filter的基本思想。

#### 1.2 正态分布(Guassian Distribution)

然后我们再回顾一下正态分布的基础知识。正态分布是一种特殊的概率分布，分布的形态完全由二阶矩决定。

多元高斯分布的表达式为：

$$
\begin{aligned}
&p(\mathbf{x}) \sim \mathrm{N}(\mu, \mathbf{\Sigma}): \\
&p(\mathbf{x})=\frac{1}{\left(2 \pi \right)^{d / 2} \left| \mathbf{\Sigma}\right|^{1 / 2}} e^{-\frac{1}{2}(\mathbf{x}-\mu)^{t} \mathbf{\Sigma}^{-1}(\mathbf{x}-\mu)}
\end{aligned}
$$
同样，一阶矩为$\bf{\mu}$表示各元变量的期望值，二阶矩为方差矩阵$\bf{\Sigma}$表示各元变量的不确定程度。

#### 1.3 正态分布的特点

在线性变换下，一旦高斯，代代高斯。

首先，高斯变量线性变换后，仍为高斯分布，均值和方差如下：

$$
\left.\begin{array}{l}X \sim N(\mu, \Sigma) \\ Y=A X+B\end{array}\right\} \quad \Rightarrow \quad Y \sim N\left(A \mu+B, A \Sigma A^{T}\right)
$$
然后，两个高斯变量线性组合，仍为高斯分布，均值和方差如下：

$$
\left.\begin{array}{l}X_{1} \sim N\left(\mu_{1}, \sigma_{1}^{2}\right) \\ X_{2} \sim N\left(\mu_{2}, \sigma_{2}^{2}\right)\end{array}\right\} \Rightarrow p\left(X_{1}+X_{2}\right) \sim N\left(\mu_{1}+\mu_{2}, \sigma_{1}^{2}+\sigma_{2}^{2}+2 \rho \sigma_{1} \sigma_{2}\right)
$$
最后，两个相互独立的高斯变量的乘积，仍然为高斯分布，均值和方差如下：

$$
\left.\begin{array}{l}X_{1} \sim N\left(\mu_{1}, \sigma_{1}^{2}\right) \\ X_{2} \sim N\left(\mu_{2}, \sigma_{2}^{2}\right)\end{array}\right\} \Rightarrow p\left(X_{1}\right) \cdot p\left(X_{2}\right) \sim N\left(\frac{\sigma_{2}^{2}}{\sigma_{1}^{2}+\sigma_{2}^{2}} \mu_{1}+\frac{\sigma_{1}^{2}}{\sigma_{1}^{2}+\sigma_{2}^{2}} \mu_{2}, \quad \frac{\sigma_{1}^{2} \sigma_{2}^{2}}{\sigma_{1}^{2}+\sigma_{2}^{2}}\right)
$$
正因为高斯分布有这些特点，所以，在Bayes滤波公式中的随机变量的加法、乘法，可以用解析的公式计算均值和方差，这使得Bayes滤波的整个计算过程非常简便，即Kalman滤波器的迭代过程。

## Kalman 滤波

### 2.1 Kalman滤波的模型假设

Kalman滤波所解决的问题，是对一个动态变化的系统的状态跟踪的问题，基本的模型假设包括：1）系统的状态方程是线性的；2）观测方程是线性的；3）过程噪声符合零均值高斯分布；4）观测噪声符合零均值高斯分布；从而，一直在线性变化的空间中操作高斯分布，状态的概率密度符合高斯分布。

1. 状态方程
    ${x_t} = {A_t}{x_{t - 1}} + {B_t}{u_t} + {\varepsilon _t}$
2. 观测方程
    ${z_t} = {H_t}{x_t} + {\delta _t}$

其中过程噪声${\varepsilon _t}$假设符合零均值高斯分布；观测噪声${\delta _t}$假设符合零均值高斯分布。对于上述模型，我们可以用如下参数描述整个问题：

### 2.2 Kalman滤波器的模型

1. $x_t$，$n$维向量，表示$t$时刻观测状态的均值。
2. $P_t$，$n*n$方差矩阵，表示$t$时刻被观测的$n$个状态的方差。
3. $u_t$，$l$维向量，表示$t$时刻的输入
4. $z_t$，$m$维向量，表示$t$时刻的观测
5. ${A_t}$，$n*n$矩阵，表示状态从$t-1$到$t$在没有输入影响时转移方式
6. ${B_t}$，$n*n$矩阵，表示$u_t$如何影响$x_t$
7. ${H_t}$，$m*n$矩阵，表示状态$x_t$如何被转换为观测$z_t$
8. ${R_t}$，$n*n$矩阵，表示过程噪声${\varepsilon _t}$的方差矩阵
9. $Q_t$，$m*m$矩阵，表示观测噪声${\delta _t}$的方差矩阵

[![image](https://images2015.cnblogs.com/blog/895560/201610/895560-20161026111132437-1141272610.png)](http://images2015.cnblogs.com/blog/895560/201610/895560-20161026111131921-1214636033.png)

图1. 在没有观测情况下，系统状态的从$t-1$到$t$的转移方式

图1给出了在没有观测，仅有输入$u_t$时，状态变量的均值和方差从$t-1$到$t$的转移方式，可见均值和方差的计算，完全是基于高斯分布的线性变化的方法来算的。

[![image](https://images2015.cnblogs.com/blog/895560/201610/895560-20161026111134046-229058495.png)](http://images2015.cnblogs.com/blog/895560/201610/895560-20161026111133437-1478239154.png)

图2. Kalman 滤波解决在收到t时刻的输入$u_t$和观测$z_t$的情况下，更新状态$x_t$的问题

图2给出了Kalman滤波所解决的问题，即在获得t时刻的输入和观测的情况下，如何更新$x_t$的均值和方差的问题。当然$u_t$和$z_t$也并不是每一个时刻都需要同时获得，就像贝叶斯滤波一样，可以在获得$u_t$时就做一次状态预测，在获得$z_t$时做一次状态更新。

### 2.3 Kalman滤波算法

Kalman滤波整体算法如下：

Kalman Filter ($x_{t-1}, P_{t-1}, u_t, z_t$)

- Prediction

    - ${\overline x _t} = {A_t}{x_{t - 1}} + {B_t}{u_t}$
    - ${\overline P _t} = {A_t}{P_{t - 1}}A_t^T + {R_t}$
- Correction
    - ${K_t} = {\overline P _t}H_t^T{({H_t}{\overline P _t}H_t^T + {Q_t})^{ - 1}}$
    - ${x_t} = {\overline x _t} + {K_t}({z_t} - {H_t}{\overline x _t})$
    - ${P_t} = (I - {K_t}{H_t}){\overline P _t}$
- 第一行基于转移矩阵和控制输入，预测$t$时刻的状态
- 第二行是预测方差矩阵
- 第三行计算Kalman增益，$\boldsymbol{K}_t$
- 第四行基于观测的新息进行状态更新
- 第五行计算更新状态的方差矩阵。

可以看到算法的所有的精妙之处都在于第三行和第四行。我们可以这样来理解：

1.   $({H_t}{\overline P _t}H_t^T + {Q_t})$代表对状态进行观测时，观测的不确定程度，它与Kalman增益$\boldsymbol{K}_t$成反比，表示观测的可能噪声越大的时候，Kalman增益$\boldsymbol{K}_t$越小。
2. 再看第四行，${x_t}$的更新是在$\overline x_t$上加一个 $K_t$ 乘以 $({z_t} - {H_t}{\overline x _t})$。$({z_t} - {H_t}{\overline x _t})$代表的是预测的值与观测之间的差异，这个差异当预测和观测都比较接近于真实值时比较小。当观测不准，或者预测不准时都会比较大。而前面的乘子$\boldsymbol{K}_t$是在观测噪声大的时候比较小，所以整个${K_t}({z_t} - {H_t}{\overline x _t})$这个修正量，表示利用观测对预测结果的修正量。
	- 当观测噪声比较小，预测误差比较大时修正幅度比较大
	- 当观测噪声比较小预测误差比较小的时候，或者观测噪声比较大的时候，修正误差的幅度也比较小，从而起到了一种平滑的作用。

3. 利用较准确的观测修正预测误差，不准确的观测修正量也较小，所以在误差较大的时候能快速修正，而在误差较小时能逐渐收敛。

### 2.4 Kalman滤波算法的推导

这里我们用Bayes公式，给出Kalman Filter是如何导出的。

#### 1. 系统的初始状态是：

$bel({x_0}) = N\left( {\mu_0,P_0} \right)$

#### 2. 预测过程的推导

状态转移模型是线性函数

${x_t} = {A_t}{x_{t - 1}} + {B_t}{u_t} + {\varepsilon _t}$

所以，由$x_{t-1}$到$x_{t}$状态转移的条件概率为:

$p(x_t \mid u_t ,x_{t - 1}) = N\left( A_t x_{t - 1} + B_t u_t,R_t \right)$

回顾Bayes公式，计算预测状态的分布，需要考虑所有可能的$x_{t-1}$：

$\overline {bel} ({x_t}) = \int {p({x_t}|{u_t},{x_{t - 1}})} {\rm{ }}bel({x_{t - 1}})\;d{x_{t - 1}}$

这正是计算两个高斯分布的卷积的过程，参考文献[2]：

$$
\overline{b e l}\left(x_{t}\right)= \int p\left(x_{t} \mid u_{t}, x_{t-1}\right) bel\left(x_{t-1}\right) d x_{t-1} \\
\text { with} \quad p\left(x_{t} \mid u_{t}, x_{t-1}\right) \sim N\left(A_{t} \mu_{t-1}+B_{t} u_{t}, R_{t}\right),\\ b e l\left(x_{t-1}\right) \sim N\left(\mu_{t-1}, \Sigma_{t-1}\right) \\
$$


$$
\begin{aligned}
\overline{b e l}\left(x_{t}\right)=& \eta \int \exp \left\{-\frac{1}{2}\left(x_{t}-A_{t} x_{t-1}-B_{t} u_{t}\right)^{T} R_{t}^{-1}\left(x_{t}-A_{t} x_{t-1}-B_{t} u_{t}\right)\right\} \\
& \exp \left\{-\frac{1}{2}\left(x_{t-1}-\mu_{t-1}\right)^{T} \Sigma_{t-1}^{-1}\left(x_{t-1}-\mu_{t-1}\right)\right\} d x_{t-1}
\end{aligned}
$$


$$
\overline{b e l}\left(x_{t}\right)=\left\{\begin{array}{l}
\bar{\mu}_{t}=A_{t} \mu_{t-1}+B_{t} u_{t} \\
\bar{\Sigma}_{t}=A_{t} \Sigma_{t-1} A_{t}^{T}+R_{t}
\end{array}\right.
$$
所以Kalman滤波器的预测过程，正是基于两个高斯分布的卷积计算得到的解析表达式。

#### 3. 观测更新过程的推导

观测方程也是线性方程，并且噪声是高斯噪声

${z_t} = {H_t}{x_t} + {\delta _t}$

所以$p({z_t}|{x_t}) $的条件概率是高斯分布的线性变换计算：

$p(z_t \mid x_t) = N \left( H_t x_t,Q_t \right)$

再考虑贝叶斯公式的状态更新步骤

$bel(x_t) = \,\quad \eta \quad \,p(z_t \mid x_t)\overline {bel} ( x_t )$

这正是两个高斯分布的乘积的问题，参考文献[2]
$$
\operatorname{bel}\left(x_t\right)= \eta p\left(z_t \mid x_t\right)  \overline{bel}\left(x_t\right) \\
\text { with} \quad p\left(z_t \mid x_t\right) \sim N\left(z_t ; C_t x_t, Q_t\right) \\
\overline{bel}\left(x_t\right) \sim N\left(x_t ; \bar{\mu}_t, \bar{\Sigma}_t\right)
$$
$$
\operatorname{bel}\left(\boldsymbol{x}_{t}\right)=\eta \exp \left\{-J_{t}\right\}
$$
其中
$$
J_{t}=\frac{1}{2}\left(z_{t}-C_{t} x_{t}\right)^{\mathrm{T}} Q_{t}^{-1}\left(z_{t}-C_{t} x_{t}\right)+\frac{1}{2}\left(x_{t}-\bar{\mu}_{t}\right)^{\mathrm{T}} \bar{\Sigma}_{t}^{-1}\left(x_{t}-\bar{\mu}_{t}\right)
$$
所以，基于求高斯变量乘积的分布的方法，可以导出结果仍然是高斯分布，用它的二阶矩表示：

$$
\operatorname{bel}\left(x_{t}\right)=
\left\{\begin{array}{c}
\mu_{t}=\bar{\mu}_{t}+K_{t}\left(z_{t}-C_{t} \bar{\mu}_{t}\right) \\
\Sigma_{t}=\left(I-K_{t} C_{t}\right) \bar{\Sigma}_{t}
\end{array} 
\quad \text {with }   K_{t}=\bar{\Sigma}_{t} C_{t}^{T}\left(C_{t} \bar{\Sigma}_{t} C_{t}^{T}+Q_{t}\right)^{-1}\right.
$$
所以状态更新中的Kalman增益，均值和方差的更新公式，都是这样导出的。

### 2.5 Kalman滤波算法的举例

图3和图4通过一维高斯分布的例子，给出在预测和更新过程中状态变量的概率密度分布是如何变化的。

[![image](https://images2015.cnblogs.com/blog/895560/201610/895560-20161026182142890-1899490801.png)](http://images2015.cnblogs.com/blog/895560/201610/895560-20161026182142203-110549955.png)

图3. 预测过程的举例，蓝色曲线表示$x_{t-1}$的pdf，紫色曲线表示$\overline x_t$的pdf.

[![image](https://images2015.cnblogs.com/blog/895560/201610/895560-20161026182143890-1042794198.png)](http://images2015.cnblogs.com/blog/895560/201610/895560-20161026182143203-583912930.png)

图4. 更新过程举例，紫色为预测后的pdf, 黄色为更新后的pdf，青色为观测的结果

从这个例子中可以值得注意的是，在预测部分高斯分布的卷积一般会使状态估计的方差加大；在观测部分高斯分布的乘积一般会将估计的方差收窄。

### 2.6 Kalman滤波的代码实现

Kalman滤波算法可以非常方便的用矩阵计算方法实现，其迭代更新过程的Matlab实现的代码仅有如下几行：

[![image](https://images2015.cnblogs.com/blog/895560/201610/895560-20161026183622375-1228104867.png)](http://images2015.cnblogs.com/blog/895560/201610/895560-20161026183621296-575251680.png)

### 2.7 Kalman滤波的效果示例

通过实现一个简单的Kalman滤波器，我们可以直观的看一下Kalman滤波器的提高跟踪准确性的效果。

[![image](https://images2015.cnblogs.com/blog/895560/201610/895560-20161026183623671-914579783.png)](http://images2015.cnblogs.com/blog/895560/201610/895560-20161026183623109-305765872.png)

图5. Kalman滤波器的实验效果示例，其中红色实线是真值；蓝色点是观测；绿色线是滑动平均的结果；紫色曲线是Kalman滤波的结果。

[![image](https://images2015.cnblogs.com/blog/895560/201610/895560-20161026183625343-953377359.png)](http://images2015.cnblogs.com/blog/895560/201610/895560-20161026183624343-1011958315.png)

图6. 比较Kalman滤波的跟踪结果和滑动平均的跟踪结果

图6给出了直观的跟踪结果与真实值之间的最小二乘误差的比较，课件Kalman滤波算法相比滑动平均等，提供了更高的跟踪准确性。

### **2.8** Kalman滤波的算法特点

1. Kalman滤波计算快速，计算复杂度为$O(m^{2.376} + n^2)$，其中$m$是观测的维数；$n$是状态的个数。
2. 对于线性系统，零均值高斯噪声的系统，Kalman是理论上无偏的，最优滤波器。
3. Kalman滤波在实际使用中，要注意参数$R$和$Q$的调节，这两者实际上是相对的，表示更相信观测还是更相信预测。具体使用时，$R$可以根据过程噪声的幅度决定，然后$Q$可以相对$R$来给定。当更相信观测时，把$Q$调小，不相信观测时，把$Q$调大。
4. $Q$越大，表示越不相信观测，这是系统状态越容易收敛，对观测的变化响应越慢。$Q$越小，表示越相信观测，这时对观测的变化响应快，但是越不容易收敛。

## 参考文献

[1]. Sebastian Thrun, Wolfram Burgard, Dieter Fox, Probabilistic Robotics, 2002, The MIT Press.

[2]. P.A. Bromiley, Products and Convolutions of Gaussian Probability Density Functions, University of Manchester