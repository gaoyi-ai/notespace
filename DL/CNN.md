---
title: CNN
categories:
- DL
- CNN
tags:
- CNN
date: 2021/3/21 20:00:16
updated: 2021/3/21 12:00:16
---

Since the 1950s, the early days of artificial intelligence, computer scientists have been trying to build computers that can make sense of visual data. In the ensuing decades, the field, which has become known as computer vision, saw incremental advances. In 2012, computer vision took a quantum leap when a group of researchers from the University of Toronto developed an AI model that surpassed the best image recognition algorithms by a large margin.

The AI system, which became known as AlexNet (named after its main creator, Alex Krizhevsky), won the 2012 ImageNet computer vision contest with an amazing 85 percent accuracy. The runner-up scored a modest 74 percent on the test.

At the heart of the AlexNet was a convolutional neural network (CNN), a specialized type of [artificial neural network](https://bdtechtalks.com/2019/08/05/what-is-artificial-neural-network-ann/) that roughly mimics the human vision system. In recent years, CNNs have become pivotal to [many computer vision applications](https://bdtechtalks.com/2019/12/30/computer-vision-applications-deep-learning/). Here’s what you need to know about the history and workings of CNNs.

A brief history of convolutional neural networks
------------------------------------------------

Convolutional neural networks, also called ConvNets, were first introduced in the 1980s by Yann LeCun, a postdoctoral computer science researcher. LeCun had built on the work done by Kunihiko Fukushima, a Japanese scientist who, a few years earlier, had invented the neocognitron, a very basic image recognition neural network.

The early version of CNNs, called LeNet (after LeCun), could recognize handwritten digits. CNNs found a niche market in banking and postal services and banking, where they read zip codes on envelopes and digits on checks.

But despite their ingenuity, ConvNets remained on the sidelines of computer vision and artificial intelligence because they faced a serious problem: They could not scale. CNNs needed a lot of data and compute resources to work efficiently for large images. At the time, the technique was only applicable to images with low resolutions.

In 2012, AlexNet showed that perhaps the time had come to revisit [deep learning](https://bdtechtalks.com/2019/02/15/what-is-deep-learning-neural-networks/), the branch of AI that uses multi-layered neural networks. The availability of large sets of data, namely the ImageNet dataset with millions of labeled pictures, and vast compute resources enabled researchers to create complex CNNs that could perform computer vision tasks that were previously impossible.

How do CNNs work?
-----------------

Convolutional neural networks are composed of multiple layers of artificial neurons. Artificial neurons, a rough imitation of their biological counterparts, are mathematical functions that calculate the weighted sum of multiple inputs and outputs an activation value.

![](https://i1.wp.com/bdtechtalks.com/wp-content/uploads/2019/08/Artificial-Neuron.png?resize=696%2C331&is-pending-load=1#038;ssl=1)The structure of an artificial neuron, the basic component of artificial neural networks (source: Wikipedia)

The behavior of each neuron is defined by its weights. When fed with the pixel values, the artificial neurons of a CNN pick out various visual features.

When you input an image into a ConvNet, each of its layers generates several activation maps. Activation maps highlight the relevant features of the image. Each of the neurons takes a patch of pixels as input, multiplies their color values by its weights, sums them up, and runs them through the activation function.

The first (or bottom) layer of the CNN usually detects basic features such as horizontal, vertical, and diagonal edges. The output of the first layer is fed as input of the next layer, which extracts more complex features, such as corners and combinations of edges. As you move deeper into the convolutional neural network, the layers start detecting higher-level features such as objects, faces, and more.

![](https://i0.wp.com/bdtechtalks.com/wp-content/uploads/2019/06/neural-networks-layers-visualization.jpg?resize=696%2C955&is-pending-load=1#038;ssl=1)Each layer of the neural network will extract specific features from the input image.

The operation of multiplying pixel values by weights and summing them is called “convolution” (hence the name convolutional neural network). A CNN is usually composed of several convolution layers, but it also contains other components. The final layer of a CNN is a classification layer, which takes the output of the final convolution layer as input (remember, the higher convolution layers detect complex objects).

Based on the activation map of the final convolution layer, the classification layer outputs a set of confidence scores (values between 0 and 1) that specify how likely the image is to belong to a “class.” For instance, if you have a ConvNet that detects cats, dogs, and horses, the output of the final layer is the possibility that the input image contains any of those animals.

![](https://i1.wp.com/bdtechtalks.com/wp-content/uploads/2019/02/neural-networks-deep-learning-artificial-intelligence.png?resize=696%2C542&is-pending-load=1#038;ssl=1)The top layer of the CNN determines the class of the image based on features extracted by convolutional layers(source: [http://www.deeplearningbook.org](http://www.deeplearningbook.org/))

Training the convolutional neural network
-----------------------------------------

One of the great challenges of developing CNNs is adjusting the weights of the individual neurons to extract the right features from images. The process of adjusting these weights is called “training” the neural network.

In the beginning, the CNN starts off with random weights. During training, the developers provide the neural network with a large dataset of images annotated with their corresponding classes (cat, dog, horse, etc.). The ConvNet processes each image with its random values and then compares its output with the image’s correct label. If the network’s output does not match the label—which is likely the case at the beginning of the training process—it makes a small adjustment to the weights of its neurons so that the next time it sees the same image, its output will be a bit closer to the correct answer.

The corrections are made through a technique called backpropagation (or backprop). Essentially, backpropagation optimizes the tuning process and makes it easier for the network to decide which units to adjust instead of making random corrections.

Every run of the entire training dataset is called an “epoch.” The ConvNet goes through several epochs during training, adjusting its weights in small amounts. After each epoch, the neural network becomes a bit better at classifying the training images. As the CNN improves, the adjustments it makes to the weights become smaller and smaller. At some point, the network “converges,” which means it essentially becomes as good as it can.

After training the CNN, the developers use a test dataset to verify its accuracy. The test dataset is a set of labeled images that are were not part of the training process. Each image is run through the ConvNet, and the output is compared to the actual label of the image. Essentially, the test dataset evaluates how good the neural network has become at classifying images it has not seen before.

If a CNN scores good on its training data but scores bad on the test data, it is said to have been “overfitted.” This usually happens when there’s not enough variety in the training data or when the ConvNet goes through too many epochs on the training dataset.

The success of convolutional neural networks is largely due to the availability of huge image datasets developed in the past decade. ImageNet, the contest mentioned at the beginning of this article, got its title from a namesake dataset with more than 14 million labeled images. There are other more specialized datasets, such as the MNIST, a database of 70,000 images of handwritten digits.

You don’t, however, need to train every convolutional neural network on millions of images. In many cases, you can use a pretrained model, such as the AlexNet or Microsoft’s ResNet, and finetune it for another more specialized application. This process is called [transfer learning](https://bdtechtalks.com/2019/06/10/what-is-transfer-learning/), in which a trained neural network is retrained a smaller set of new examples.

The limits of convolutional neural networks
-------------------------------------------

Despite their power and complexity, convolutional neural networks are, in essence, pattern-recognition machines. They can leverage massive compute resources to ferret out tiny and inconspicuous visual patterns that might go unnoticed to the human eye. But when it comes to understanding the meaning of the contents of images, they perform poorly.

Consider the following image. A well-trained ConvNet will tell you that it’s the image of a soldier, a child and the American flag. But a person can give a long description of the scene, and talk about military service, tours in a foreign country, the feeling of longing for home, the joy of reuniting with the family, etc. Artificial neural networks have no notion of those concepts.

![](https://i2.wp.com/bdtechtalks.com/wp-content/uploads/2020/01/Soldier-reuniting-with-family.jpg?resize=394%2C591&is-pending-load=1#038;ssl=1)Image credit: Depositphotos

These limits become more evident in practical applications of convolutional neural networks. For instance, CNNs are now widely used to [moderate content on social media networks](https://bdtechtalks.com/2018/12/10/ai-deep-learning-adult-content-moderation/). But despite the vast repositories of images and videos they’re trained on, they still struggle to detect and block inappropriate content. In one case, Facebook’s content-moderation AI [banned the photo of a 30,000-year-old statue](https://www.businessinsider.com/facebook-bans-venus-of-willendorf-photos-over-nudity-policy-2018-3) as nudity.

Also, neural networks start to break as soon as they move a bit out of their context. Several studies have shown that CNNs trained on ImageNet and other popular datasets fail to detect objects when they see them under different lighting conditions and from new angles.

A recent [study by researchers at the MIT-IBM Watson AI Lab](https://bdtechtalks.com/2019/12/16/objectnet-dataset-ai-computer-vision/) highlights these shortcomings. It also introduces ObjectNet, a dataset that better represents the different nuances of how objects are seen in real life. CNNs don’t develop the mental models that humans have about different objects and their ability to imagine those objects in previously unseen contexts.

![](https://i0.wp.com/bdtechtalks.com/wp-content/uploads/2019/12/objectnet_controls_table.png?resize=696%2C706&is-pending-load=1#038;ssl=1)ImageNet vs reality: In ImageNet (left column) objects are neatly positioned, in ideal background and lighting conditions. In the real world, things are messier (source: objectnet.dev)

Another problem with convolutional neural networks is their inability to understand the relations between different objects. Consider the following image, which is known as a “Bongard problem,” named after its inventor, Russian computer scientist Mikhail Moiseevich Bongard. Bongard problems present you with two sets of images (six on the left and six on the right), and you must explain the key difference between the two sets. For instance, in the example below, images in the left set contains one object and images in the right set contain two objects.

It’s easy for humans to draw such conclusions from such small amounts of samples. If I show you these two sets and then provide you with a new image, you’ll be able to quickly decide whether it should go into the left or right set.

![](https://i2.wp.com/bdtechtalks.com/wp-content/uploads/2020/01/Bongard-problem-computer-vision.png?resize=696%2C454&is-pending-load=1#038;ssl=1)Bongard problems are easy for humans to solve, but hard for computer vision systems. (Source: [Harry Foundalis](https://www.foundalis.com/res/bps/bpidx.htm))

But there’s still no convolutional neural network that can solve Bongard problems with so few training examples. [In one study](https://arxiv.org/abs/1607.08366) conducted in 2016, AI researchers trained a CNN on 20,000 Bongard samples and tested it on 10,000 more. The CNN’s performance was much lower than that of average humans.

The peculiarities of ConvNets also make them vulnerable to [adversarial attacks](https://bdtechtalks.com/2018/12/27/deep-learning-adversarial-attacks-ai-malware/), perturbations in input data that go unnoticed to the human eye but affect the behavior of neural networks. Adversarial attacks have become a major source of concern as deep learning and especially CNNs have become an integral component of many critical applications [such as self-driving cars](https://bdtechtalks.com/2018/09/17/self-driving-cars-ai-computer-vision/).

![](https://i2.wp.com/bdtechtalks.com/wp-content/uploads/2019/02/ai-adversarial-example-panda-gibbon.png?resize=696%2C271&is-pending-load=1#038;ssl=1)Adversarial example: Adding an imperceptible layer of noise to this panda picture causes a convolutional neural network to mistake it for a gibbon.

Does this mean that CNNs are useless? Despite the limits of convolutional neural networks, however, there’s no denying that they have caused a revolution in artificial intelligence. Today, CNNs are used in many [computer vision applications](https://bdtechtalks.com/2019/12/30/computer-vision-applications-deep-learning/) such as facial recognition, image search and editing, augmented reality, and more. In some areas, such as medical image processing, well-trained ConvNets might even outperform human experts at detecting relevant patterns.

As advances in convolutional neural networks show, our achievements are remarkable and useful, but we are still very far from [replicating the key components of human intelligence](https://bdtechtalks.com/2019/07/22/general-ai-driverless-cars-impossible/).

## Different types of CNNs

**1D CNN**: With these, the CNN kernel moves in one direction. 1D CNNs are usually used on time-series data.

**2D CNN**: These kinds of CNN kernels move in two directions. You'll see these used with image labelling and processing.

**3D CNN**: This kind of CNN has a kernel that moves in three directions. With this type of CNN, researchers use them on 3D images like CT scans and MRIs.