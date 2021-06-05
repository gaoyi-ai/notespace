---
title: Bayes Filters
categories:
- State Estimation
- Probabilistic Robotics
tags:
- bayes filters
date: 2021/6/3 20:00:09
updated: 2021/6/3 12:00:09
---

# Probabilistic Robotics

Explicit representation of **uncertainty** using the calculus of probability theory

- Perception = state estimation
- Action  = utility optimization

## Bayes Rule with Background Knowledge

$$
P(x \mid y, z)=\frac{P(y \mid x, z) P(x \mid z)}{P(y \mid z)}
$$

## Recursive Bayesian Updating

how can we estimate $P(x| z_1...z_n )$
$$
P\left(x \mid z_{1}, \ldots, z_{n}\right)=\frac{P\left(z_{n} \mid x, z_{1}, \ldots, z_{n-1}\right) P\left(x \mid z_{1}, \ldots, z_{n-1}\right)}{P\left(z_{n} \mid z_{1}, \ldots, z_{n-1}\right)}
\\
\text{说明:} z_1,...,z_{n-1}\text{都作为Background Knowledge}
$$
**Markov assumption**: $z_n$ is independent of $z_1,...,z_{n-1}$ if we know $x$.
$$
\begin{aligned}
P\left(x \mid z_{1}, \cdots, z_{n}\right) &=\frac{P\left(z_{n} \mid x\right) P\left(x \mid z_{1}, \ldots, z_{n-1}\right)}{P\left(z_{n} \mid z_{1}, \ldots, z_{n-1}\right)} \\
&=\eta P\left(z_{n} \mid x\right) P\left(x \mid z_{1}, \cdots, z_{n-1}\right) \\
&=\eta_{1 \ldots n} \prod_{i=1 . . . n} P\left(z_{i} \mid x\right) P(x)
\end{aligned}
$$

## A Typical Pitfall

- Two possible locations $x_1$ and $x_2$
- $P(x_1)=0.99$
- $P(z|x_2)=0.09,P(z|x_1)=0.07$ 

虽然在一开始对 $x_1$ 处的概率为0.99，但是随着不断地观测，对 $x_2$ 处的信心增加。

![image-20210604205206037](images/Probabilistic%20Robotics/image-20210604205206037.png)

