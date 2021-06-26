---
title: Gradient & Divergence & Curl
categories:
- Math
- Calculus
tags:
- 梯度
- 散度
- 旋度
date: 2021/6/25
---



# Gradient

从一元函数入手，把一元函数看成是scalar field的一种：$\frac{\mathrm df}{\mathrm dx}$表示的是$f$变化的速率，而在讨论一元函数的时候，其变化的"方向"显然是确定的（因为只有一个变量发生了变化)

延伸到多元的scalar filed, $f=(x,y,z)$由于牵扯到了三个变量，在处理的时候对于不同的变量求"导"即求偏微分。得到三个微分部分：$\frac{\mathrm df}{\mathrm dx}$，$\frac{\mathrm df}{\mathrm dy}$，$\frac{\mathrm df}{\mathrm dz}$

那么多元的scalar field 和 一元的 scalar field 有什么不同呢？区别在于scalar field 的形状：

- 一元scalar field在坐标系中表现为一条曲线，某点的切线唯一确定
- 多元scalar field 在坐标系中表现为一个“曲面”，某点的切线有无数条

因而讨论scalar field的增加减少时，总要指明“方向”，这个方向一个 unit vector表示。所以$f$的导数被表示为 $\frac{\mathrm df}{\mathrm dx}=(\frac{\partial f}{\partial x},\frac{\partial f}{\partial y},\frac {\partial f}{\partial z}) \cdot \mathbf{u}$，被记$(\frac{\partial f}{\partial x},\frac{\partial f}{\partial y},\frac {\partial f}{\partial z})$为 $\nabla f$或 $\mathrm{grad}f$。

gradient $\nabla$也作为一个常用的微分算符在其他地方出现。

# Divergence & Curl

对于 scalar field f，gradient很够很好的解决f变化速率的问题，但对于vector field u，这一问题更加复杂。

![img](https://pic1.zhimg.com/80/0138d4e896d38db68f3c2a57519da86d_720w.jpg)
_暂时把这样的一个vector field 看成是实际的流体，这样比较好理解。_

在scalar field 中，我们关注的的某点变化的速率，那么这样一个速率在vector field u中，应该被理解成单位体积内流体体积的变化率：为了得到这样的一个速率，我们先取某点A，并取一个包含A的小平面$\delta S$，其法向量记成 $\mathbf n$,这样一来单位体积内的经过的流体就成为 $\frac{1}{\delta V}\int_{\delta S} \mathbf u \cdot \mathbf n \ \mathrm dS$ 

对V取极限，并且把这个式子记作散度divergence，$\mathrm{div} \ \mathbf u =\lim_{\delta V \rightarrow 0} \frac{1}{\delta V}\int_{\delta S} \mathbf u \cdot \mathbf n \ \mathrm dS$

经过一系列的化简之后，divergence可以被表达成 $\mathrm{div} \ \mathbf u = \frac{\partial f}{\partial x}+\frac{\partial f}{\partial y}+\frac {\partial f}{\partial z}$

思考一个点电荷激发的电场，任意选取一个单位体积，若是单位体积不包含该电荷，那么毫无疑问，有多少电场线进入就有多少电场线出，散度为0.但若选取的单位体积内包含了一个正点电荷，则电场线只出不进，因而散度不为零。所以散度经常用来判断是否存在场源。

但divergence就能够满足我们对场性质的需求了么？

思考一下涡流电场（电场线），这样的场和一般的点电荷电场是不一样的。传统的点电荷电场是保守场(conservative field),保守场最大的特点是做功只与始末位置相关，于是试着沿着某闭合曲线C做一个积分：$\oint_{\delta C} \mathbf u \cdot \mathbf{dr}$ ，其中S是闭合曲线围成的面，我们要S尽量小，再考虑到单位面积，我们做下面的极限 $\lim_{\delta S \rightarrow 0} \frac{1}{\delta S} \oint_{\delta C} \mathbf u \cdot \mathbf {dr}$ ，这样的式子被定义成旋度curl和该面S的单位法向量的乘积 $\mathbf n \cdot \mathrm{curl} \mathbf u =\lim_{\delta S \rightarrow 0} \frac{1}{\delta S} \oint_{\delta C} \mathbf u \cdot \mathbf {dr}$ ，经过一系列数学演算之后，curl u最终可以被表示成 $\mathrm{curl}\ \mathbf u=\nabla \times \mathbf u$

