---
title: Perceptron
categories:
- ML
- Perceptron
tags:
- perceptron
- MLP
date: 2021/3/13 10:00:00
updated: 2021/3/13 16:00:00
---

# ﻿What is a Perceptron?

Perceptron是一种用于二进制[分类器](https://deepai.org/machine-learning-glossary-and-terms/classifier)的[监督学习](https://deepai.org/machine-learning-glossary-and-terms/supervised-learning)的算法。二进制分类器决定一个输入，通常由一系列[向量](https://deepai.org/machine-learning-glossary-and-terms/vector)表示，是否属于一个特定的类别。

简而言之，感知器是一个单层[神经网络](https://deepai.org/machine-learning-glossary-and-terms/neural-network)。它们由四个主要部分组成，包括输入值、权重和偏置、净和以及[激活函数](https://deepai.org/machine-learning-glossary-and-terms/activation-function)。

感知器是如何工作的？
---------------------------

这个过程首先是将所有输入值乘以它们的权重。然后，将所有这些乘法值相加，形成加权和。然后将加权和应用于激活函数，产生感知器的输出。激活函数扮演着不可或缺的角色，它确保输出在所需的值之间进行映射，如（0,1）或（-1,1）。需要注意的是，一个输入的权重表明了一个节点的强度。同样，一个输入的偏置值可以使激活函数曲线向上或向下移动。

### 感知器和机器学习

作为神经网络的一种简化形式，特别是单层神经网络，感知器在二元分类中发挥着重要作用。这意味着感知器是用来将数据分为两部分，因此是二进制的。有时，感知器也因此被称为线性二进制分类器。

# MLP

多层神经网络的出现源于实现XOR逻辑门的需要。早期的感知器研究者在XOR上遇到了一个问题。和电子XOR电路的问题一样：需要多个元件来实现XOR逻辑。对于电子器件，通常使用2个NOT门、2个AND门和一个OR门。对于神经网络，似乎需要多个感知器（好吧，从某种程度上说）。更准确地说，抽象的感知器活动需要以特定的序列连接在一起，并改变为一个单一的单元。于是，多层网络诞生了。

为什么要大费周章地做XOR网络呢？嗯，两个原因。(1)随着XOR门的出现，电路设计中的很多问题都得到了解决；(2)XOR网络为更有趣的神经网络和机器学习设计打开了大门。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/XORLogic1.gif)

Figure 1.  XOR logic circuit (Floyd, p. 241).

如果你熟悉逻辑符号，你可以直接看这个电路，与图2比较一下，看看两者的功能有何相同之处。  两个反相器（NOT门）做的事情和图2中的-2做的事情完全一样。  OR门所做的功能和图1中输出单元的0.5激活完全一样。  而在图2中你看到的所有地方都有一个+1，这些加在一起的功能和图1中两个AND门的功能是一样的。

While perceptrons are limited to variations of Boolean algebra functions including NOT, AND and OR, the XOR function is a hybrid involving multiple units (Floyd, p. 241).

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/MLPXOR1.gif)

> Figure 2.  XOR Gate.  注意最中间的单元是隐藏的，不受外界影响，只通过输入或输出单元连接。  隐蔽单元的阈值为1.5，保证只有当两个输入单元都开启时才会开启。  输出单元的值为0.5，保证只有当净正输入大于0.5时才会开启。  从隐藏单元到输出单元的权重为-2，保证了当两个输入单元都开启时，输出单元才会开启。

多层感知器（MLP）是一种被称为监督网络的神经网络，因为它需要一个期望的输出来学习。这种类型的网络的目标是创建一个模型，使用预先选择的数据正确地将输入映射到输出，以便当期望的输出是未知的时候，该模型可以用来产生输出。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/MLPLayers1.gif)

> Figure 3.  An MLP with two hidden layers.  当输入模式被输入到输入层时，它们在从输入层传递到第一隐藏层的同时，会被互联权重相乘。  在第一隐藏层内，它们被相加，然后由非线性激活函数处理。  每次数据被一层处理时，都会被互联权重乘以，然后被下一层求和并处理。  最后数据在输出层内进行最后一次处理，产生神经网络输出。

MLP使用一种称为反向传播的算法进行学习。  通过反向传播，输入数据在一个被称为 "训练 "的过程中反复呈现给神经网络。  每一次呈现，神经网络的输出都会与期望的输出进行比较，并计算出误差。  然后，该误差被反馈（反向传播）到神经网络，并用于调整权重，使误差随着每次迭代而减少，神经模型越来越接近产生预期的输出。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/MLPBackProp1.gif)

> Figure 4.  A neural network learning to model exclusive-or (XOR) data.  XOR数据被反复呈现给神经网络。每一次呈现，都会计算出网络输出和期望输出之间的误差，并反馈给神经网络。神经网络利用这个误差调整其权重，使误差减小。这一系列事件通常会重复进行，直到达到一个可接受的误差或直到网络不再出现学习。

> [mnemstudio.org](http://mnemstudio.org/ai/nn/multi-layer_nets.php)