---
title: State Estimation
categories:
- Probabilistic Robotics
- State Estimation
tags:
- state estimation
date: 2021/6/3 20:00:09
updated: 2021/6/3 12:00:09
---

# State Estimation

## State & Observation

State : x = 随机变量

Observation : z 中存在noise

在状态估计中，我们关心的不是State的值是多少，而是状态的分布$P(x)$

对机器人的状态估计，就是已知输入数据 u 和观测数据 z 的条件下，求计算状态 x 的条件概率分布 $P(x|z,u)$

当没有运动数据时，只考虑观测方程带来的数据时，就相当于估计 $P(x|z)$的条件概率。 

估计状态变量的条件分布，利用贝叶斯法则有$P(x|z)=\frac{P(z|x)p(x)}{p(z)}$ 因为分母部分（归一化）与待估计的状态无关因而可以忽略，$P(x|z) \rightarrow P(z|x)P(x)$那么贝叶斯法则的左侧称为后验概率，右侧的 $P(z|x)$称为似然，另一部分 $P(x)$称为先验，直接求后验分布是十分困难的，但是求状态的最优估计，使得在该状态下后验概率最大化是可行的

## 选取先验分布

均值分布；高斯分布，方差应取较大

