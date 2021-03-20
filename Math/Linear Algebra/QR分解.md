---
title: Gram-Schmidt正交化 & QR分解
categories:
- Math
- Linear Algebra
tags:
- QR
- Gram-Schmidt
date: 2021/3/20 08:00:00
updated: 2021/3/20 21:00:00
---



Gram-Schmidt 正交化
================

在提到矩阵的 QR 分解前，必须要提到 Gram–Schmidt 方法，理论上 QR 分解是由 Gram–Schmidt 正交化推出来的。那么 Gram–Schmidt 正交化究竟是什么。

在三维空间存在直角坐标系，其中任意一点都可以由 (x,y,z) 坐标唯一确定，在这个坐标系中，X、Y、Z 三轴都是相互正交 (垂直) 的。那么推广到 n 维欧式空间，就是 n 个线性无关的基向量组成的一组基，n 维欧式空间中任意一位置，都可以由这组基线性表示。

那么就引出来另一个问题，怎么得到一组两两相互正交的正交基呢? 这一过程就是 Gram–Schmidt 正交化。下面简

单推理一下 Gram–Schmidt 正交化方法的得出过程。重点是正交投影这种思想。

## 一维投影

![image-20210320175308168](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210320175703395.png)

## 高维投影

![image-20210320175703395](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210320175703395.png)

推广到第 j 个正交向量可得：
$$
\begin{array}{c}
\beta_{1}=\alpha_{1} \\
\beta_{j}=\alpha_{j}-\sum_{k=1}^{j-1} \frac{\left(\alpha_{j}, \beta_{k}\right)}{\left(\beta_{k}, \beta_{k}\right)} \beta_{k}
\end{array}
$$
以上就是求正交基的 Gram–Schmidt 正交化方法。

QR 分解
=====

条件：A的各个列向量线性无关

Ax=b =>  (QR)x=b
				$Q^{-1}QRx=Q^{-1}b$
				$Rx=Q^Tb$

![image-20210320173101885](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210320175703395.png)
$$
\begin{aligned}
\bar{p}_{1}=& \bar{a}_{1}=\left\|\bar{p}_{1}\right\| \bar{q}_{1}=r_{11} \bar{q}_{1} \\
\bar{p}_{2}=& \vec{a}_{2}-\frac{\vec{a}_{2} \cdot \bar{p}_{1}}{\left\|\bar{p}_{1}\right\|^{2}} \cdot \bar{p}_{1}=\left\|\bar{p}_{2}\right\| \bar{q}_{2} \\
\vec{a}_{2} &=\frac{\vec{a}_{2} \cdot \bar{p}_{1}}{\left\|\bar{p}_{1}\right\|^{2}} \cdot \bar{p}_{1}+\left\|\bar{p}_{2}\right\| \bar{q}_{2} \\
\bar{a}_{2} &=\frac{\vec{a}_{2} \cdot \bar{p}_{1}}{\| \bar{p}_{1}||^{2}} \cdot|| \bar{p}_{1}\left\|\bar{q}_{1}+\right\| \bar{p}_{2} \| \bar{q}_{2}=r_{21} \vec{q}_{1}+r_{22} \bar{q}_{2}
\end{aligned}
$$

$$
\begin{aligned}
\bar{p}_{3}=\bar{a}_{3}-& \frac{\vec{a}_{3} \cdot \bar{p}_{1}}{\left\|\bar{p}_{1}\right\|^{2}} \cdot \bar{p}_{1}-\frac{\bar{a}_{3} \cdot \bar{p}_{2}}{\left\|\bar{p}_{2}\right\|^{2}} \cdot \bar{p}_{2}=\left\|\bar{p}_{3}\right\| \bar{q}_{3} \\
\bar{a}_{3}=& \frac{\bar{a}_{3} \cdot \bar{p}_{1}}{\left\|\bar{p}_{1}\right\|^{2}} \cdot \bar{p}_{1}+\frac{\bar{a}_{3} \cdot \bar{p}_{2}}{\left\|\bar{p}_{2}\right\|^{2}} \cdot \bar{p}_{2}+\left\|\bar{p}_{3}\right\| \bar{q}_{3} \\
=& \frac{\vec{a}_{3} \cdot \bar{p}_{1}}{\left\|\bar{p}_{1}\right\|^{2}} \cdot\left\|\bar{p}_{1}\right\| \vec{q}_{1}+\frac{\vec{a}_{3} \cdot \bar{p}_{2}}{\left\|\bar{p}_{2}\right\|^{2}} \cdot\left\|\bar{p}_{2}\right\| \vec{q}_{2}+\left\|\bar{p}_{3}\right\| \bar{q}_{3}
\end{aligned}
$$

$$
\begin{array}{l}
\vec{a}_{1}=r_{11} \vec{q}_{1} \\
\vec{a}_{2}=r_{21} \vec{q}_{1}+r_{22} \vec{q}_{2} \\
\vec{a}_{3}=r_{31} \vec{q}_{1}+r_{32} \vec{q}_{2}+r_{33} \vec{q}_{3} \\
\vec{a}_{4}=r_{41} \vec{q}_{1}+r_{42} \vec{q}_{2}+r_{43} \vec{q}_{3}+r_{44} \vec{q}_{4}
\end{array}
$$

$$
\begin{aligned}
A &=\left(\begin{array}{cccc}
\vec{a}_{1} & \vec{a}_{2} & \ldots & \vec{a}_{n}
\end{array}\right) \\
&=\left(\begin{array}{cccc}
r_{11}\vec{q}_{1} & r_{21}\vec{q}_{1}+r_{22}\vec{q}_{2} & \ldots & r_{n 1}\vec{q}_{1}+r_{n 2} \vec{q}_{2}+\ldots+r_{n n} \vec{q}_{n}
\end{array}\right) \\
&=\left(\begin{array}{cccc}
r_{11} \vec{q}_{1} & r_{21} \bar{q}_{1} & \ldots & r_{n 1} \bar{q}_{1}
\end{array}\right) \\
&+\left(\begin{array}{cccc}
0 & r_{22} \bar{q}_{2} & \ldots & r_{n 2} \bar{q}_{2}
\end{array}\right) \\
&+\left(\begin{array}{cccc}
0 & 0 & \ldots & r_{nn} \bar{q}_{nn}
\end{array}\right) \\
\end{aligned}
$$

n个矩阵相加
$$
=\left(\begin{array}{cccc}
\bar{q}_{1} & \bar{q}_{2} & \ldots & \bar{q}_{n}
\end{array}\right) \cdot\left(\begin{array}{cccc}
r_{11} & r_{21} & \ldots & r_{n 1} \\
0 & r_{22} & \ldots & r_{n 2} \\
\ldots & \ldots & \ldots & \ldots \\
0 & 0 & \ldots & r_{n n}
\end{array}\right)
$$
由此得到 QR 分解定义：

对于 n 阶方阵 A，若存在正交矩阵 Q 和上三角矩阵 R，使得 A = QR，则该式称为矩阵 A 的完全 QR 分解或正交三角分解。(对于可逆矩阵 A 存在完全 QR 分解)。

从上面可以看出矩阵 QR 分解是由 Gram–Schmidt 正交化推理出来的一种方阵分解形式，矩阵 QR 分解的计算方法也是以 Gram–Schmidt 正交化为核心。通过 Gram–Schmidt 正交化求出正交矩阵 Q，再通过$R=Q^TA$得到矩阵 R。

这里对于 Gram–Schmidt 正交化求正交矩阵 Q 提出一种改进的计算方法 (改进的地方是每产生一个单位正交向量后，就用后续的向量减去它，消去其中包含这个正交向量的部分)：

$(1)令\beta_{1}=\alpha_{1}$
(2) 对 $j=1,2, \ldots, n ;$
	(1)计算 $r_{11}=\left\|\beta_{j}\right\|$ 以及 $\varepsilon_{j}=\beta_{j} / r_{i j} ;$
	(2)计算 $r_{j, j+1}, r_{j, j+2}, \ldots, r_{j, n}$ 以及 $\beta_{l}=\alpha_{l}-\sum_{k=1}^{l-1} r_{k j} \varepsilon_{k}(l=j+1, j+2, \ldots, n)$

其实对于非方阵的 mxn(m≥n) 阶矩阵 A 也可能存在 QR 分解的，A = QR。这时 Q 为 mxn 阶的半正交矩阵，R 为 n*n 阶上三角矩阵。这时的 QR 分解不是完整的 (方阵)，因此称为约化 QR 分解 (对于列满秩矩阵 A 必存在约化 QR 分解)。

同时也可以通过扩充矩阵 A 为方阵或者对矩阵 R 补零，可以得到完全 QR 分解。
