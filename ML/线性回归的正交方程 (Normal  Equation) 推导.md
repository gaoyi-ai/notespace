---
title: 线性回归的正交方程(Normal Equation)推导
categories:
- ML
- Linear Regression
tags:
- Normal Equation
date: 2019/8/1 20:00:14
updated: 2020/12/10 12:00:14
---



> [zhuanlan.zhihu.com](https://zhuanlan.zhihu.com/p/268561676)

使用**梯度下降**等迭代的方法逼近模型参数在机器学习实践中已司空见惯，但是在特征数量不是很多 (![](https://www.zhihu.com/equation?tex=n) <1000) 的**线性回归**的情况下，使用**正交方程** (Normal Equation)直接得到参数的解析解 (Analysis Solution) 更为便捷，还不用纠结如何选择**学习率** (Learning Rate)。

Normal 这里的意思是**正交**，也就是**垂直**，源于希腊语中的_norma，_是木匠和石匠使用的确定直角的正方形工具_，_类似的用法还有**法向量** (Normal Vector)，将 Normal Equation 译为 “正规方程” 是不合理的。

正交方程这个术语是由高斯（Gauss）在 1822 年首先提出的，但和正交方程紧密相关的最小二乘法是勒让德（Legendre）在 1805 年发表的 [[1]](#ref_1)，细节请移步[人类发现的第 1 颗小行星、高斯与最小二乘法](https://zhuanlan.zhihu.com/p/270862634)。

线性回归的目标是估计出参数 ![](https://www.zhihu.com/equation?tex=%5Ctheta) ，尽量使 ![](https://www.zhihu.com/equation?tex=X%5Ctheta+%5Capprox+y) , 特征数据![](https://www.zhihu.com/equation?tex=X) 被组织成维度为 ![](https://www.zhihu.com/equation?tex=m%5Ctimes+%28n%2B1%29) 的设计矩阵（Design Matrix），第 1 列全部为 1 对应截距，其中 ![](https://www.zhihu.com/equation?tex=m) 是数据样本的数量， ![](https://www.zhihu.com/equation?tex=n) 是特征（Feature）的数量， ![](https://www.zhihu.com/equation?tex=y+) 是 ![](https://www.zhihu.com/equation?tex=m%5Ctimes+1) （向量）的标签 (Label) 数据。

![](https://www.zhihu.com/equation?tex=%5Cbegin%7Bbmatrix%7D++1%26x_%7B11%7D%26%5Ccdots%26x_%7B1n%7D%5C%5C++1%26x_%7B21%7D%26%5Ccdots%26x_%7B2n%7D%5C%5C++%5Cvdots%26%5Cvdots+%26%5Cvdots%26%5Cvdots%5C%5C++1%26x_%7Bm1%7D%26%5Ccdots%26x_%7Bmn%7D++%5Cend%7Bbmatrix%7D) ![](https://www.zhihu.com/equation?tex=%5Ccdot) ![](https://www.zhihu.com/equation?tex=%5Cbegin%7Bbmatrix%7D++%5Ctheta_%7B0%7D%5C%5C++%5Ctheta_%7B1%7D%5C%5C++%5Cvdots%5C%5C++%5Ctheta_%7Bn%7D++%5Cend%7Bbmatrix%7D) = ![](https://www.zhihu.com/equation?tex=%5Cbegin%7Bbmatrix%7D++%5Ctilde%7By_0%7D%5C%5C++%5C%5C%5Ctilde%7By_1%7D%5C%5C++%5Cvdots%5C%5C++%5C%5C%5Ctilde%7By_m%7D++%5Cend%7Bbmatrix%7D)

Design Matrix ![](https://www.zhihu.com/equation?tex=X%3A+m+%5Ctimes+%28n%2B1%29) ![](https://www.zhihu.com/equation?tex=%5Ctheta%3A%28n%2B1%29+%5Ctimes+1++) ![](https://www.zhihu.com/equation?tex=%5Ctilde%7By%7D%3A+m+%5Ctimes+1)

我们以 ![](https://www.zhihu.com/equation?tex=J%28%5Ctheta%29%3D%5Cfrac%7B1%7D%7B2m%7D%5CSigma_%7Bi%3D1%7D%5Em%28%5Ctilde%7By_i%7D-y_i%29%5E%7B2%7D) 来表示模型的成本（Cost），预测值 ![](https://www.zhihu.com/equation?tex=%5Ctilde%7By%7D) 和实际值 ![](https://www.zhihu.com/equation?tex=y) 的差的平方的均值，即**均方误差** (Mean Square Error，MSE)，其中 ![](https://www.zhihu.com/equation?tex=%5Ctilde%7By%7D) = ![](https://www.zhihu.com/equation?tex=X%5Ctheta) 。选择 MSE 不仅由于数学上的方便，更深层次的理由是：实际值偏离预测值的误差可以视为**噪声**，噪声一般符合**正态分布**，![](https://www.zhihu.com/equation?tex=%28%5Ctilde%7By_i%7D-y_i%29%5E%7B2%7D) 与正态分布的概率密度函数（PDF，Probability Density Function）的指数部分形式相同。更进一步，高斯 - 马尔可夫定律中指出：MSE 是最优线性无偏估计量 (BLUE)，也不要求误差符合正态分布，只要求 3 个条件：不相关，同方差，期望 0。

言归正传，要求成本函数的最小值，通过令 ![](https://www.zhihu.com/equation?tex=%5Cfrac%7B%5Cpartial%7D%7B%5Cpartial%5Ctheta%7DJ%28%5Ctheta%29%3D0) , 求出 ![](https://www.zhihu.com/equation?tex=%5Ctheta) 。

![](https://www.zhihu.com/equation?tex=J%28%5Ctheta%29+) = ![](https://www.zhihu.com/equation?tex=%5Cfrac%7B1%7D%7B2m%7D%5CSigma_%7Bi%3D1%7D%5Em%28%5Ctilde%7By_i%7D-y_i%29%5E%7B2%7D)

= ![](https://www.zhihu.com/equation?tex=%5Cfrac%7B1%7D%7B2m%7D%5CSigma_%7Bi%3D1%7D%5Em%28X_i%5Ctheta-y_i%29%5E%7B2%7D)

= ![](https://www.zhihu.com/equation?tex=%5Cfrac%7B1%7D%7B2m%7D%5C)![](https://www.zhihu.com/equation?tex=%5Cbegin%7BVmatrix%7D++x_%7B1%7D%5Ccdot%5Ctheta-y_%7B1%7D%5C%5C++x_%7B2%7D%5Ccdot%5Ctheta-y_%7B2%7D%5C%5C++%5Ccdots%5C%5C++x_%7Bm%7D%5Ccdot%5Ctheta-y_%7Bm%7D+%5Cend%7BVmatrix%7D_%7B2%7D%5E%7B2%7D)

= ![](https://www.zhihu.com/equation?tex=%5Cfrac%7B1%7D%7B2m%7D%5Cbegin%7BVmatrix%7D++X%5Ctheta-y+%5Cend%7BVmatrix%7D_%7B2%7D%5E%7B2%7D)

= ![](https://www.zhihu.com/equation?tex=%5Cfrac%7B1%7D%7B2m%7D%28X%5Ctheta-y%29%5E%7BT%7D%28X%5Ctheta-y%29)

= ![](https://www.zhihu.com/equation?tex=%5Cfrac%7B1%7D%7B2m%7D%28%28X%5Ctheta%29%5E%7BT%7D-y%5E%7BT%7D%29%28X%5Ctheta-y%29)

= ![](https://www.zhihu.com/equation?tex=%5Cfrac%7B1%7D%7B2m%7D%28%28X%5Ctheta%29%5E%7BT%7DX%5Ctheta-%28X%5Ctheta%29%5E%7BT%7Dy-y%5E%7BT%7DX%5Ctheta-y%5E%7BT%7Dy%29)

= ![](https://www.zhihu.com/equation?tex=%5Cfrac%7B1%7D%7B2m%7D%28%5Ctheta%5E%7BT%7DX%5E%7BT%7DX%5Ctheta-2%5Ctheta%5E%7BT%7DX%5E%7BT%7Dy++%2B++y%5E%7BT%7Dy%29)

*   ![](https://www.zhihu.com/equation?tex=%28X%5Ctheta%29%5E%7BT%7D) 是 ![](https://www.zhihu.com/equation?tex=1%5Ctimes+m) 向量， ![](https://www.zhihu.com/equation?tex=y) 是 ![](https://www.zhihu.com/equation?tex=m%5Ctimes+1) 向量, 两者乘积 ![](https://www.zhihu.com/equation?tex=%28X%5Ctheta%29%5E%7BT%7Dy) 是个标量 (![](https://www.zhihu.com/equation?tex=1%5Ctimes1))， ![](https://www.zhihu.com/equation?tex=y%5E%7BT%7DX%5Ctheta) 的情况类似，也是一个标量，且相等，因此两者可直接相加；
*   ![](https://www.zhihu.com/equation?tex=y%5E%7BT%7Dy) 不含 ![](https://www.zhihu.com/equation?tex=%5Ctheta) ，对 ![](https://www.zhihu.com/equation?tex=%5Ctheta) 求导为 0

相关的矩阵求导我在[机器学习常用的矩阵求导](https://zhuanlan.zhihu.com/p/268576866)中进一步介绍。

![](https://www.zhihu.com/equation?tex=%5Cfrac%7B%5Cpartial%7D%7B%5Cpartial%5Ctheta%7DJ%28%5Ctheta%29)

= ![](https://www.zhihu.com/equation?tex=%5Cfrac%7B1%7D%7B2m%7D%282X%5E%7BT%7DX%5Ctheta-2X%5E%7BT%7Dy+%2B0%29)

= ![](https://www.zhihu.com/equation?tex=%5Cfrac%7B1%7D%7Bm%7D%28X%5E%7BT%7DX%5Ctheta-X%5E%7BT%7Dy+%29)

Let ![](https://www.zhihu.com/equation?tex=%5Cfrac%7B1%7D%7Bm%7D%28X%5E%7BT%7DX%5Ctheta-X%5E%7BT%7Dy+%29%3D+0) then ![](https://www.zhihu.com/equation?tex=X%5E%7BT%7DX%5Ctheta-X%5E%7BT%7Dy+%3D+0)

![](https://www.zhihu.com/equation?tex=X%5E%7BT%7DX%5Ctheta+%3D+X%5E%7BT%7Dy+)

![](https://www.zhihu.com/equation?tex=%5Ctheta%3D%28X%5E%7BT%7DX%29%5E%7B-1%7DX%5E%7BT%7Dy+)

Q.E.D.

从线性代数的向量投影的角度，只需 1 步即可直接得出结论，细节请移步 [Normal Equation 的向量投影解法与几何和直觉解释](https://zhuanlan.zhihu.com/p/269232332)。

参考
--

1.  [^](#ref_1_0)Miller, J. (ed) "Earliest known uses of some of the words of mathematics, N" in Earliest known uses of some of the words of mathematics [http://jeff560.tripod.com/n.html](http://jeff560.tripod.com/n.html)