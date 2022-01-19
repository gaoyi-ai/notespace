---
title: Convolution Vs Correlation
categories:
- DL
- Convolutions
tags:
- convolutions
- neural network
date: 2022/1/18
---



# Convolution Vs Correlation

> [Convolution Vs Correlation. Convolutional Neural Networks which are… | by Divyanshu Mishra | Towards Data Science](https://towardsdatascience.com/convolution-vs-correlation-af868b6b4fb5)

![img](https://miro.medium.com/max/619/1*hOI0jW3CcS_yuxcmJIYjKw.gif)

Convolutional Neural Networks which are the backbones of most Computer Vision Applications like Self-Driving Cars, Facial Recognition Systems etc are a special kind of Neural Network architecture in which the basic matrix-multiplication operation is replaced by a convolution operation. They specialize in processing data that has a grid-like topology. Examples include time-series data and image data which can be thought of as a 2-D grid of pixels.

# HISTORY

The Convolutional Neural Networks was first introduced by Fukushima by the name [Neocognitron](https://www.rctn.org/bruno/public/papers/Fukushima1980.pdf) in 1980. It was inspired by the hierarchical model of the nervous system as proposed by Hubel and Weisel. But the model was not popular because of its complex unsupervised learning algorithm referred to as learning without a teacher. Yann LeCun in 1989 used backpropagation along with the concepts of Neocognitron to propose an architecture named [LeNet](http://yann.lecun.com/exdb/publis/pdf/lecun-89e.pdf) which was used for handwritten Zip Code Recognition by the U.S Postal Service. Yann LeCun further worked on this project and finally in 1998 released LeNet-5 — the first modern convnet that introduced some of the essential concepts we still use in CNN today. He also released the MNIST dataset of handwritten digits which is perhaps the most famous benchmark dataset in machine learning. In the 1990s the field of Computer Vision shifted its focus and a lot of researchers stopped trying to work on CNN architectures. There was a cold winter for the Neural Network research until 2012 when a group of researchers from the University of Toronto entered a CNN based model(AlexNet) in the famous ImageNet challenge and ended up winning it with an error rate of 16.4%. Since then the Convolutional Neural Networks keep progressing forward and CNN based architectures keep winning the ImageNet and in 2015 the Convolutional Neural Networks based architecture [ResNet](https://arxiv.org/abs/1505.00393) surpassed the human-level error rate of 5.1% with an error rate of 3.57%.

# THE MISNOMER:

The Convolutional operation widely used in CNN is a misnomer. The operation that is used is strictly speaking a correlation instead of convolution. Both the operators have a slight difference and we will go through each of them separately to understand the difference.

# Cross-Correlation:

Correlation is the process of moving a filter mask often referred to as kernel over the image and computing the sum of products at each location. Correlation is the function of displacement of the filter. In other words, the first value of the correlation corresponds to zero displacements of the filter, the second value corresponds to one unit of displacement, and so on.

![img](https://miro.medium.com/max/875/1*WncBBUvi7lpWl-T1d2SlfQ.png)

Figure 1.Cross-Correlation in 1-D

![img](https://miro.medium.com/max/875/1*EnH-qswA8Ip8YqrwDLCROw.png)

Figure 2.Cross-Correlation in 1-D

**Mathematical Formula :**

The mathematical formula for the cross-correlation operation in 1-D on Image I using a Filter F is given by Figure 3. It would be convenient to suppose that F has an odd number of elements, so we can suppose that as it shifts, its centre is right on top of an element of Image I. So we say that F has 2N+1 elements, and these are indexed from -N to N, so that the centre element of F is F(0).

![img](https://miro.medium.com/max/541/1*tNs6HjyNRfxzRaOkwZAd-Q.png)

Figure 3. The formula of Cross-Correlation in 1-D

Similarly, we can extend the notion to 2-D which is represented in Figure 4. The basic idea is the same, except the image and the filter are now 2D. We can suppose that our filter has an odd number of elements, so it is represented by a (2N+1)x(2N+1) matrix.

![img](https://miro.medium.com/max/864/1*yCL_MB2tLrLoNZ6fXx_SaQ.png)

Figure 4. The Formula of Cross-Correlation in 2-D.

The Correlation operation in 2D is very straightforward. We just take a filter of a given size and place it over a local region in the image having the same size as the filter. We continue this operation shifting the same filter through the entire image. This also helps us achieve two very popular properties :

1. **Translational Invariance:** Our vision system should be to sense, respond or detect the same object regardless of where it appears in the image.
2. **Locality:** Our vision system focuses on the local regions, without regard to what else is happening in other parts of the image.

The Cross-Correlation function has a limitation or characteristic property that when it is applied on a discrete unit impulse(a 2D matrix of all zeros and just single 1) yields a result that is a copy of the filter but rotated by an angle of 180 degrees.

![img](https://miro.medium.com/max/875/1*RhHoldDIzmca3ula71tkFg.png)

Figure 5. The complete correlation operation

> after flipping the kernel you should use the original definition of the (discrete) convolution. i.e. multiply (for kernel size of 3) `kernel[0][0]` with `patch[2][2]`, `kernel[0][1]` with `patch[2][1]`, `kernel[0][2]` with `patch[2][0]` and so on.
>
> **convolution is correlation with the *flipped* kernel**

# Convolution:

The convolution operation is very similar to the cross-correlation operation but has a slight difference. In Convolution operation, the kernel is first flipped by an angle of 180 degrees and is then applied to the image. The fundamental property of convolution is that convolving a kernel with a discrete unit impulse yields a copy of the kernel at the location of the impulse.

We saw in the cross-correlation section that a correlation operation yields a copy of the impulse but rotated by an angle of 180 degrees. Therefore, if we *pre-rotate* the filter and perform the same sliding sum of products operation, we should be able to obtain the desired result.

![img](https://miro.medium.com/max/875/1*DJIJX1Adlo_DzKo63IBYSg.png)

Figure 6. Applying the convolutional operation on Image b in Figure 5.

**Mathematical Formula:**

The convolution operation applied on Image I using a kernel F is given by the formula in 1-D. Convolution is just like correlation, except we flip over the filter before correlating.

![img](https://miro.medium.com/max/565/1*GuxN3-UpM829V1WzLp7Esw.png)

Figure 7. Convolution Operation in 1-D.

In the case of 2D convolution, we flip the filter both horizontally and vertically. This can be written as:

![img](https://miro.medium.com/max/875/1*7eAgyV6vgvXVezmtPAlI2Q.png)

Figure 8. Convolution Operation in 2-D.

The same properties of **Translational Invariance** and **Locality** are followed by Convolution operation as well.

![img](https://miro.medium.com/max/875/1*iHlLqY3wLObj764UoYqNPQ@2x.gif)

Figure 9. The Correlation Operation demonstrated what many would refer to as convolution.

# NOTE:

Though both the operations are different slightly yet it doesn’t matter if the kernel used is symmetric.

# Conclusion:

In this post, we briefly discussed the history and some properties of Convolutional Neural Networks. We discussed the misnomer that the convolutional operation often mentioned in the various text is actually a cross-correlation operation. The difference is very slight yet very useful and should be known by everyone entering, practising or experienced in the wide field of Computer Vision. I hope you liked the post and for any questions, queries or discussion, DM me on [Twitter](https://twitter.com/Perceptron97) or[ Linkedin](https://www.linkedin.com/in/divyanshu-mishra-ai/).

# References:

1. Deep Learning book by **Ian Goodfellow and Yoshua Bengio and Aaron Courville.**
2. Digital Image Processing by **Rafael C. Gonzalez.**
3. Dive into Deep Learning by **Aston Zhang, Zack C. Lipton, Mu Li and Alex J. Smola.**
4. Correlation and Convolution by **David Jacobs**.
5. Figure 9 taken from https://towardsdatascience.com/applied-deep-learning-part-4-convolutional-neural-networks-584bc134c1e2.
6. https://spatial-lang.org/conv
7. The meme is taken from [https://www.mihaileric.com/posts/convolutional-neural-networks/.](https://www.mihaileric.com/posts/convolutional-neural-networks/)