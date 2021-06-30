---
title: 5-applications-SVD
categories:
- Math
- Linear Algebra
- SVD
tags:
- linear algebra
- svd
date: 2021/3/25 08:00:00
updated: 2021/3/25 21:00:00
---



> [5-applications-singular-value-decomposition-svd-data-science](https://www.analyticsvidhya.com/blog/2019/08/5-applications-singular-value-decomposition-svd-data-science/)

Overview
--------

*   Singular Value Decomposition (SVD) is a common dimensionality reduction technique in data science
*   We will discuss 5 must-know applications of SVD here and understand their role in data science
*   We will also see three different ways of implementing SVD in Python

Introduction
------------

> “Another day has passed, and I still haven’t used _y = mx + b._“

Sounds familiar? I often hear my school and college acquaintances complain that the algebra equations they spent so much time on are essentially useless in the real world.

Well – I can assure you that’s simply not true. Especially if you want to carve out a career in data science.

Linear algebra bridges the gap between theory and practical implementation of concepts. A healthy understanding of linear algebra opens doors to [machine learning](https://courses.analyticsvidhya.com/courses/applied-machine-learning-beginner-to-professional/?utm_source=blog&utm_medium=5-applications-singular-value-decomposition-svd-data-science) algorithms we thought were impossible to understand. And one such use of linear algebra is in Singular Value Decomposition (SVD) for dimensionality reduction.

[![](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/Singular-Value-Decomposition.jpg)](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/Singular-Value-Decomposition.jpg)

You must have come across SVD a lot in data science. It’s everywhere, especially when we’re dealing with dimensionality reduction. But what is it? How does it work? And what are SVD’s applications?

I briefly mentioned SVD and its applications in my article on the [Applications of Linear Algebra in Data Science](https://www.analyticsvidhya.com/blog/2019/07/10-applications-linear-algebra-data-science/?utm_source=blog&utm_medium=5-applications-singular-value-decomposition-svd-data-science). In fact, SVD is the foundation of [Recommendation Systems](https://www.analyticsvidhya.com/blog/2018/06/comprehensive-guide-recommendation-engine-python/?utm_source=blog&utm_medium=5-applications-singular-value-decomposition-svd-data-science) that are at the heart of huge companies like Google, YouTube, Amazon, Facebook and many more.

We will look at five super useful applications of SVD in this article. But we won’t stop there – we will explore how we can use SVD in Python in three different ways as well.

_And if you’re looking for a one-stop-shop to learn all machine learning concepts, we have put together one of the [most comprehensive courses](http://courses.analyticsvidhya.com/courses/applied-machine-learning-beginner-to-professional/?utm_source=blog&utm_medium=5-applications-singular-value-decomposition-svd-data-science) available anywhere. Make sure you check it out (and yes, SVD is in there as part of the dimensionality reduction module)._

Table of Contents
-----------------

*   Applications of Singular Value Decomposition (SVD)
    *   Image Compression
    *   Image Recovery
    *   Eigenfaces
    *   Spectral Clustering
    *   Background Removal from Videos
*   What is Singular Value Decomposition?
    *   Rank of a Matrix
    *   Singular Value Decomposition
    *   Why is SVD used in Dimensionality Reduction?
*   3 Ways to Perform SVD in Python

Applications of Singular Value Decomposition (SVD)
--------------------------------------------------

We are going to follow a top-down approach here and discuss the applications first. I have explained the math behind SVD after the applications for those interested in how it works underneath.

You just need to know four things to understand the applications:

1.  SVD is the **decomposition** of a matrix A **into 3 matrices – U, S, and V**
2.  S is the diagonal matrix of singular values. Think of **singular values** as the importance values of different features in the matrix
3.  The **rank** of a matrix is a measure of the unique information stored in a matrix. Higher the rank, more the information
4.  **Eigenvectors** of a matrix are directions of maximum spread or variance of data

在大多数应用程序中，使用[Dimensionality Reduction](https://dimensionality-reduction-techniques-python/)的基本原理。 _您想在保留重要信息的同时将高阶矩阵简化为低阶矩阵_。

### SVD for Image Compression

How many times have we faced this issue? We love clicking images with our smartphone cameras and saving random photos off the web. And then one day – no space! Image compression helps deal with that headache.

It minimizes the size of an image in bytes to an acceptable level of quality. This means that you are able to store more images in the same disk space as compared to before.

Image compression takes advantage of the fact that only a few of the singular values obtained after SVD are large. You can trim the three matrices based on the first few singular values and obtain a compressed approximation of the original image. Some of the compressed images are nearly indistinguishable from the original by the human eye.

Here’s how you can code this in Python:

**Output:**

[![](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/image_compression.png)](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/image_compression.png)

If you ask me, even the last image (with n_components = 100) is quite impressive. I would not have guessed that it was compressed if I did not have the other images for comparison.

### SVD for Image Recovery

Ever clicked an image in low light? Or had an old image become corrupt? We assume that we cannot get that image back anymore. It’s surely lost to the past. Well – not anymore!

We’ll understand image recovery through the concept of matrix completion (and a cool Netflix example).

矩阵补全是在部分观察到的矩阵中填充缺失条目的过程。 Netflix问题就是一个常见的例子。

> 给定一个评分矩阵，其中每个条目表示客户对电影的评分，如果客户**i**已经观看了电影**j**，并且缺少其他条目，我们将预测剩余条目，以便为客户提供下一步观看内容的好建议。

帮助解决这个问题的基本事实是，大多数用户在观看的电影和对这些电影的评价上**都有自己的模式**。因此，评分矩阵几乎没有唯一的信息。这意味着低秩矩阵将能够为矩阵提供足够好的近似。

This is what we achieve with the help of SVD.

Where else do you see this property? Yes, in matrices of images! **Since an image is contiguous, the values of most pixels depend on the pixels around them.** So a low-rank matrix can be a good approximation of these images.

Here is a snapshot of the results:

[![](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/image_recovery.jpg.jpg)](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/image_recovery.jpg.jpg)

_Chen, Zihan. “Singular Value Decomposition and its Applications in Image Processing.”  ACM, 2018_

The entire formulation of the problem can be complex to comprehend and requires knowledge of other advanced concepts as well. You can read the paper that I referred to [here](https://sci-hub.tw/10.1145/3274250.3274261).

### SVD for Eigenfaces

The original paper [Eigenfaces for Recognition](http://www.face-rec.org/algorithms/pca/jcn.pdf) came out in 1991. Before this, most of the approaches for facial recognition dealt with identifying individual features such as the eyes or the nose and developing a face model by the position, size, and relationships among these features.

> Eigenface方法试图提取人脸图像中的相关信息，对其进行尽可能有效的编码，并将一个人脸编码与类似编码的模型数据库进行比较。

The encoding is obtained by expressing **each face as a linear combination of the selected eigenfaces in the new face space**.

[![](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/eigenfaces11.png)](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/eigenfaces11.png)

Let me break the approach down into five steps:

1.  Collect a training set of faces as the training set
2.  Find the most important features by finding the **directions of maximum variance** – the **eigenvectors or the eigenfaces**
3.  Choose top M eigenfaces corresponding to the highest eigenvalues. These eigenfaces now define a new _face space_
4.  Project all the data in this _face space_
5.  For a new face, project it into the new _face space_, find the closest face(s) in the space, and classify the face as **a** **known or an unknown face**

You can find these eigenfaces using both PCA and SVD. Here is the first of several eigenfaces I obtained after performing SVD on the [Labelled Faces in the Wild](http://vis-www.cs.umass.edu/lfw/) dataset:

[![](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/eigenfaces.jpg.jpg)](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/eigenfaces.jpg.jpg)

As we can see, only the images in the first few rows look like actual faces. Others look noisy and hence I discarded them. I preserved a total of 120 eigenfaces and transformed the data into the new _face space_. Then I used the _**[k-nearest neighbors](https://www.analyticsvidhya.com/blog/2018/03/introduction-k-neighbours-algorithm-clustering/?utm_source=blog&utm_medium=5-applications-singular-value-decomposition-svd-data-science) classifier**_ to predict the names based on the faces.

You can see the classification report below. Clearly, there is scope for improvement. You can try adjusting the number of eigenfaces to preserve and experiment with different classifiers:

[![](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/knnResult.jpg.jpg)](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/knnResult.jpg.jpg)

Have a look at some of the predictions and their true labels:

[![](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/knnPlot.png)](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/knnPlot.png)

You can find my attempt at Facial Recognition using Eigenfaces [here](https://github.com/KhyatiMahendru/EigenfacesWithSVD/blob/master/EigenfacesSVD.ipynb).

### SVD for Spectral Clustering

[Clustering](https://www.analyticsvidhya.com/blog/2016/11/an-introduction-to-clustering-and-different-methods-of-clustering/?utm_source=blog&utm_medium=5-applications-singular-value-decomposition-svd-data-science) is the task of grouping similar objects together. It is an unsupervised [machine learning](https://courses.analyticsvidhya.com/courses/applied-machine-learning-beginner-to-professional/?utm_source=blog&utm_medium=5-applications-singular-value-decomposition-svd-data-science) technique. For most of us, clustering is synonymous with K-Means Clustering – a simple but powerful algorithm. However, it is not always the most accurate.

Consider the below case:

[![](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/spectralClustering1.png)](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/spectralClustering1.png)

Clearly, there are 2 clusters in concentric circles. But KMeans with n_clusters = 2 gives the following clusters:

[![](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/spectralClustering2.png)](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/spectralClustering2.png)

K-Means is definitely not the appropriate algorithm to use here. Spectral clustering is a technique that combats this. It has roots in [Graph theory](https://www.analyticsvidhya.com/blog/2018/09/introduction-graph-theory-applications-python/?utm_source=blog&utm_medium=5-applications-singular-value-decomposition-svd-data-science). These are the basic steps:

*   Start with the **Affinity matrix (A)** or the **Adjacency matrix** of the data. This represents how similar one object is to another. In a graph, this would represent if an edge existed between the points or not
*   Find the **Degree matrix (D)** of each object. This is a diagonal matrix with entry **_(i,i)_** equal to the number of objects object _**i**_ is similar to
*   Find the **Laplacian (L)** of the Affinity Matrix: **L = A – D**
*   Find the highest **_k_** eigenvectors of the Laplacian Matrix depending on their eigenvalues
*   Run _**k-means**_ on these eigenvectors to cluster the objects into k classes

You can read about the complete algorithm and its math [here](https://www.cs.cmu.edu/~aarti/Class/10701/readings/Luxburg06_TR.pdf). The implementation of Spectral Clustering in scikit-learn is similar to KMeans:

You will obtain the below perfectly clustered data from the above code:

[![](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/spectralClustering3.png)](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/spectralClustering3.png)

### SVD for Removing Background from Videos

I have always been curious how all those TV commercials and programs manage to get a cool background behind the actors. While this can be done manually, why put in that much manual effort when you have machine learning?

Think of how you would distinguish the background of a video from its foreground. The **background of a video is essentially static – it does not see a lot of movement. All the movement is seen in the foreground.** This is the property that we exploit to separate the background from the foreground.

Here are the steps we can follow for implementing this approach:

*   Create matrix M from video – This is done by sampling image snapshots from the video at regular intervals, flattening these image matrices to arrays, and storing them as the columns of matrix M
*   We get the following plot for matrix M:

[![](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/matrixForVideo.png)](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/matrixForVideo.png)

What do you think these horizontal and wavy lines represent? Take a moment to think about this.

**The horizontal lines represent the pixel values that do not change throughout the video. So essentially, these represent the background in the video. The wavy lines show movement and represent the foreground.**

*   We can, therefore, think of M as being the sum of two matrices – one representing the background and other the foreground
*   The **background matrix** does not see a variation in pixels and is thus redundant i.e. it does not have a lot of unique information. So, it is a **low-rank** matrix
*   So, a low-rank approximation of M is the background matrix. We use SVD in this step
*   We can obtain the foreground matrix by simply subtracting the background matrix from the matrix M

Here is a frame of the video after removing the background:[![](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/bgremoval.jpg)](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/bgremoval.jpg)

Pretty impressive, right?

We have discussed five very useful applications of SVD so far. But how does the math behind SVD actually work? And how useful is it for us as data scientists? Let’s understand these points in the next section.

What is Singular Value Decomposition (SVD)?
-------------------------------------------

I have used the term **rank** a lot in this article. In fact, through all the literature on SVD and its applications, you will encounter the term “rank of a matrix” very frequently. So let us start by understanding what this is.

### Rank of a Matrix

The rank of a matrix is the maximum number of linearly independent row (or column) vectors in the matrix. A vector **r** is said to be linearly independent of vectors **r1** and **r2** if it cannot be expressed as a linear combination of **r1** and **r2**.
$$
r \neq ar_1 + br_2
$$

> 矩阵的秩可以看作是矩阵所代表的唯一信息量的代表。级别越高，信息越丰富。

### Singular Value Decomposition (SVD)

So where does SVD fit into the overall picture? SVD deals with decomposing a matrix into a product of 3 matrices as shown:
$$
A = USV^T
$$
If the dimensions of A are m x n:

*   U is an m x m matrix of **Left Singular Vectors**
*   S is an m x n rectangular diagonal matrix of **Singular Values** arranged in decreasing order
*   V is an n x n matrix of **Right Singular Vectors**

### Why is SVD used in Dimensionality Reduction?

You might be wondering why we should go through with this seemingly painstaking decomposition. The reason can be understood by an alternate representation of the decomposition. See the figure below:

[![](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/svd.jpg1.jpg)](https://cdn.analyticsvidhya.com/wp-content/uploads/2019/07/svd.jpg1.jpg)

The decomposition allows us to express our _**original matrix as a linear combination of low-rank matrices**_.

In a practical application, you will observe that only the first few, say k, singular values are large. The rest of the singular values approach zero. As a result, terms except the first few can be ignored without losing much of the information. See how the matrices are truncated in the figure below:

[![](https://cdn.analyticsvidhya.com/wp-content/uploads/2018/09/Screenshot_7.png)](https://cdn.analyticsvidhya.com/wp-content/uploads/2018/09/Screenshot_7.png)

To summarize:

*   Using SVD, we are able to represent our large matrix A by 3 smaller matrices U, S and V
*   This is helpful in large computations
*   We can obtain a **k-rank approximation** of A. To do this, select the first k singular values and truncate the 3 matrices accordingly
