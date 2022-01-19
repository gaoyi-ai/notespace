---
title: 关于卷积 互相关与自相关的讨论
categories:
- DL
- Convolutions
tags:
- convolutions
- neural network
date: 2022/1/18
---



- convolution is equivalent to simple multiplication in frequency domain. But, signal must be transform from spatial (time) to frequency domain first.
- Cross-correlation is to determine similarity; greater output if 2 signals are similar (compare to inner product of 2 vectors).
    On the contrary, convolution is to determine how the input signal reacts for a kernel (impulse response), in other words, finding the output for any impulse response.
    Why is it flipped in convolution? It is simply a visual representation to help the understating of how convoluton computed. When you examine all terms of convolution calculation, it looks like multipying the input and the flipped kernel one by one.

# [The difference between convolution and cross-correlation from a signal-analysis point of view](https://dsp.stackexchange.com/questions/27451/the-difference-between-convolution-and-cross-correlation-from-a-signal-analysis)

In signal processing, two problems are common:

- What is the output of this filter when its input is x(t)x(t)? The answer is given by x(t)∗h(t)x(t)∗h(t), where h(t)h(t) is a signal called the "impulse response" of the filter, and ∗∗ is the convolution operation.
- Given a noisy signal y(t)y(t), is the signal x(t)x(t) somehow present in y(t)y(t)? In other words, is y(t)y(t) of the form x(t)+n(t)x(t)+n(t), where n(t)n(t) is noise? The answer can be found by the correlation of y(t)y(t) and x(t)x(t). If the correlation is large for a given time delay ττ, then we may be confident in saying that the answer is yes.

Note that when the signals involved are symmetric, convolution and cross-correlation become the same operation; this case is also very common in some areas of DSP.

what I like about the impulse response explanation is you really get an intuition why convolution is "reversed". In discrete terms, the current output is the current input x impulse response at time 0 + residual output from previous inputs impulse responses (input a n-1 * impulse 1 + input n-2 * impulse 2 and so on).

## Cross-Correlation, Autocorrelation, and Convolution[¶](http://www.ceri.memphis.edu/people/egdaub/datanotes/_build/html/sac5.html#cross-correlation-autocorrelation-and-convolution)

Common techniques in signal processing frequently make use of cross-correlation, autocorrelation, and convolution operations. These three operations are all related, and involve integrating the product of two functions with a varying time lag. Given two functions $f(t)$ and $g(t)$, then the cross correlation ${(f\star g)(\tau)}$ of the two functions is defined to be
$$
(f\star g)(\tau) = \int_{-\infty}^{\infty}f(t)g(t+\tau)dt = \int_{-\infty}^{\infty}f(t+\tau)g(t)dt
$$
This can be thought of as a moving inner product between the two signals, where ττ indicates the time lag between the two signals. If the cross correlation is large at a given time lag, that means that the two signals are similar when they are offset by that value of the time lag – this is true if the cross correlation is positive or negative, with negative indicating the signs are reversed. If the cross correlation is small at a given time lag, then the signals are different at that given offset.

The autocorrelation is simply the cross-correlation of a signal with itself. The autocorrelation function thus has its maximum at a time lag of zero, and is symmetric about τ=0. The convolution f∗g is also similar, but one of the functions is reversed in time. This is illustrated in the figure below, along with a visual representation of how each function takes the values that it does.

![_images/sac5_conv_corr_auto.png](http://www.ceri.memphis.edu/people/egdaub/datanotes/_build/html/_images/sac5_conv_corr_auto.png)

Examples of convolution, cross correlation, and autocorrelation for basic functions. The cross correlation measures the similarity between two signals, while the convolution is essentially a time reversed cross correlation. Autocorrelation measures the similarity of a signal with itself, so it always peaks at zero offset and is symmetric.