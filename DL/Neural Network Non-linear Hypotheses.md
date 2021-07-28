---
title: Neural Network
categories:
- ML
- Neural Network
tags:
- neural network
- non-linear solution
date: 2021/3/13 10:00:00
updated: 2021/3/13 16:00:00
---

# Neural Network

## Non-linear Hypotheses

我们之前学的，无论是线性回归还是逻辑回归都有这样一个缺点，即：当特征太多时，计算的负荷会非常大。

下面是一个例子：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/5316b24cd40908fb5cb1db5a055e4de5.png)

当我们使用$x_1$, $x_2$ 的多次项式进行预测时，我们可以应用的很好。
之前我们已经看到过，使用非线性的多项式项，能够帮助我们建立更好的分类模型。假设我们有非常多的特征，例如大于100个变量，我们希望用这100个特征来构建一个非线性的多项式模型，结果将是数量非常惊人的特征组合，即便我们只采用两两特征的组合$(x_1x_2+x_1x_3+x_1x_4+...+x_2x_3+x_2x_4+...+x_{99}x_{100})$，我们也会有接近5000个组合而成的特征。这对于一般的逻辑回归来说需要计算的特征太多了。

假设我们希望训练一个模型来识别视觉对象（例如识别一张图片上是否是一辆汽车），我们怎样才能这么做呢？一种方法是我们利用很多汽车的图片和很多非汽车的图片，然后利用这些图片上一个个像素的值（饱和度或亮度）来作为特征。

假如我们只选用灰度图片，每个像素则只有一个值（而非 **RGB**值），我们可以选取图片上的两个不同位置上的两个像素，然后训练一个逻辑回归算法利用这两个像素的值来判断图片上是否是汽车：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/3ac5e06e852ad3deef4cba782ebe425b.jpg)

假使我们采用的都是50x50像素的小图片，并且我们将所有的像素视为特征，则会有 2500个特征，如果我们要进一步将两两特征组合构成一个多项式模型，则会有约$2500^{2}/2$个（接近3百万个）特征。普通的逻辑回归模型，不能有效地处理这么多的特征，这时候我们需要神经网络。

## Model Presentation

神经网络模型建立在很多神经元之上，每一个神经元又是一个个学习模型。这些神经元（也叫激活单元，**activation unit**）采纳一些特征作为输出，并且根据本身的模型提供一个输出。下图是一个以逻辑回归模型作为自身学习模型的神经元示例，在神经网络中，参数又可被成为权重（**weight**）。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/c2233cd74605a9f8fe69fd59547d3853.jpg)

我们设计出了类似于神经元的神经网络，效果如下：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/fbb4ffb48b64468c384647d45f7b86b5.png)

其中$x_1$, $x_2$, $x_3$是输入单元（**input units**），我们将原始数据输入给它们。
$a_1$, $a_2$, $a_3$是中间单元，它们负责将数据进行处理，然后呈递到下一层。
最后是输出单元，它负责计算${h_\theta}\left( x \right)$。

神经网络模型是许多逻辑单元按照不同层级组织起来的网络，每一层的输出变量都是下一层的输入变量。下图为一个3层的神经网络，第一层成为输入层（**Input Layer**），最后一层称为输出层（**Output Layer**），中间一层成为隐藏层（**Hidden Layers**）。我们为每一层都增加一个偏差单位（**bias unit**）：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/8293711e1d23414d0a03f6878f5a2d91.jpg)

下面引入一些标记法来帮助描述模型：
$a_{i}^{\left( j \right)}$ 代表第$j$ 层的第 $i$ 个激活单元。$\theta^{\left( j \right)}$代表从第 $j$ 层映射到第$ j+1$ 层时的权重的矩阵，例如$\theta^{\left( 1 \right)}$代表从第一层映射到第二层的权重的矩阵。其尺寸为：以第 $j+1$层的激活单元数量为行数，以第 $j$ 层的激活单元数加一为列数的矩阵。例如：上图所示的神经网络中$\theta^{\left( 1 \right)}$的尺寸为 3*4。

对于上图所示的模型，激活单元和输出分别表达为：

$a_{1}^{(2)}=g(\Theta _{10}^{(1)}{x}_{0}+\Theta _{11}^{(1)}{x}_{1}+\Theta _{12}^{(1)}{x}_{2}+\Theta _{13}^{(1)}{x}_{3})$
$a_{2}^{(2)}=g(\Theta _{20}^{(1)}{x}_{0}+\Theta _{21}^{(1)}{x}_{1}+\Theta _{22}^{(1)}{x}_{2}+\Theta _{23}^{(1)}{x}_{3})$
$a_{3}^{(2)}=g(\Theta _{30}^{(1)}{x}_{0}+\Theta _{31}^{(1)}{x}_{1}+\Theta _{32}^{(1)}{x}_{2}+\Theta _{33}^{(1)}{x}_{3})$
$h_{\Theta }(x)=g(\Theta _{10}^{(2)}a_{0}^{(2)}+\Theta _{11}^{(2)}a_{1}^{(2)}+\Theta _{12}^{(2)}a_{2}^{(2)}+\Theta _{13}^{(2)}a_{3}^{(2)})$

上面进行的讨论中只是将特征矩阵中的一行（一个训练实例）喂给了神经网络，我们需要将整个训练集都喂给我们的神经网络算法来学习模型。

我们可以知道：每一个$a$都是由上一层所有的$x$和每一个$x$所对应的$\theta$决定的。

（我们把这样从左到右的算法称为前向传播算法( **FORWARD PROPAGATION** )）

把$x$, $\theta$, $a$ 分别用矩阵表示：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20171101224053.png)

我们可以得到$\theta \cdot X=a$ 。

( **FORWARD PROPAGATION** )
相对于使用循环来编码，利用向量化的方法会使得计算更为简便。以上面的神经网络为例，试着计算第二层的值：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/303ce7ad54d957fca9dbb6a992155111.png)
$$
g\left(\left[\begin{array}{llll}
\theta_{10}^{(1)} & \theta_{11}^{(1)} & \theta_{12}^{(1)} & \theta_{13}^{(1)} \\
\theta_{20}^{(1)} & \theta_{21}^{(1)} & \theta_{22}^{(1)} & \theta_{23}^{(1)} \\
\theta_{30}^{(1)} & \theta_{31}^{(1)} & \theta_{32}^{(1)} & \theta_{33}^{(1)}
\end{array}\right] \times\left[\begin{array}{c}
x_{0} \\
x_{1} \\
x_{2} \\
x_{3}
\end{array}\right]\right)=g\left(\left[\begin{array}{l}
\theta_{10}^{(1)} x_{0}+\theta_{11}^{(1)} x_{1}+\theta_{12}^{(1)} x_{2}+\theta_{13}^{(1)} x_{3} \\
\theta_{20}^{(1)} x_{0}+\theta_{21}^{(1)} x_{1}+\theta_{22}^{(1)} x_{2}+\theta_{23}^{(1)} x_{3} \\
\theta_{30}^{(1)} x_{0}+\theta_{31}^{(1)} x_{1}+\theta_{32}^{(1)} x_{2}+\theta_{33}^{(1)} x_{3}
\end{array}\right]\right)=\left[\begin{array}{c}
a_{1}^{(2)} \\
a_{2}^{(2)} \\
a_{3}^{(2)}
\end{array}\right]
$$
我们令 $z^{\left( 2 \right)}=\theta ^{\left( 1 \right)}x$，则 $a^{\left( 2 \right)}=g(z^{\left( 2 \right)})$ ，计算后添加 $a_{0}^{\left( 2 \right)}=1$。 计算输出的值为：
$$
g\left(\left[\begin{array}{llll}
\theta_{10}^{(2)} & \theta_{11}^{(2)} & \theta_{12}^{(2)} & \theta_{13}^{(2)}
\end{array}\right] \times\left(\begin{array}{c}
u_{0} \\
a_{1}^{(2)} \\
a_{2}^{(2)} \\
a_{3}^{(2)}
\end{array}\right]\right)=g\left(\theta_{10}^{(2)} a_{0}^{(2)}+\theta_{11}^{(2)} a_{1}^{(2)}+\theta_{12}^{(2)} a_{2}^{(2)}+\theta_{13}^{(2)} a_{3}^{(2)}\right)=h_{\theta}(x)
$$
我们令 $z^{\left( 3 \right)}=\theta^{\left( 2 \right)} a^{\left( 2 \right)}$，则 $h_\theta(x)=a^{\left( 3 \right)}=g(z^{\left( 3 \right)})$。
这只是针对训练集中一个训练实例所进行的计算。如果我们要对整个训练集进行计算，我们需要将训练集特征矩阵进行转置，使得同一个实例的特征都在同一列里。即：
$z^{\left( 2 \right)}=\Theta^{\left( 1 \right)}\times X^T $

 $a^{\left( 2 \right)}=g(z^{\left( 2 \right)})$


为了更好了了解**Neuron Networks**的工作原理，我们先把左半部分遮住：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/6167ad04e696c400cb9e1b7dc1e58d8a.png)

右半部分其实就是以$a_0, a_1, a_2, a_3$, 按照**Logistic Regression**的方式输出$h_\theta(x)$：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/10342b472803c339a9e3bc339188c5b8.png)

其实神经网络就像是**logistic regression**，只不过我们把**logistic regression**中的输入向量$\left[ x_1\sim {x_3} \right]$ 变成了中间层的$\left[ a_1^{(2)}\sim a_3^{(2)} \right]$, 即:  $h_\theta(x)=g\left( \Theta_0^{\left( 2 \right)}a_0^{\left( 2 \right)}+\Theta_1^{\left( 2 \right)}a_1^{\left( 2 \right)}+\Theta_{2}^{\left( 2 \right)}a_{2}^{\left( 2 \right)}+\Theta_{3}^{\left( 2 \right)}a_{3}^{\left( 2 \right)} \right)$ 
我们可以把$a_0, a_1, a_2, a_3$看成更为高级的特征值，也就是$x_0, x_1, x_2, x_3$的进化体，并且它们是由 $x$与$\theta$决定的，因为是梯度下降的，所以$a$是变化的，并且变得越来越厉害，所以这些更高级的特征值远比仅仅将 $x$次方厉害，也能更好的预测新数据。
这就是神经网络相比于逻辑回归和线性回归的优势。

### 特征和直观理解Ⅰ

从本质上讲，神经网络能够通过学习得出其自身的一系列特征。在普通的逻辑回归中，我们被限制为使用数据中的原始特征$x_1,x_2,...,x_n$，我们虽然可以使用一些二项式项来组合这些特征，但是我们仍然受到这些原始特征的限制。在神经网络中，原始特征只是输入层，在我们上面三层的神经网络例子中，第三层也就是输出层做出的预测利用的是第二层的特征，而非输入层中的原始特征，我们可以认为第二层中的特征是神经网络通过学习后自己得出的一系列用于预测输出变量的新特征。

神经网络中，单层神经元（无中间层）的计算可用来表示逻辑运算，比如逻辑与(**AND**)、逻辑或(**OR**)。

举例说明：逻辑与(**AND**)；下图中左半部分是神经网络的设计与**output**层表达式，右边上部分是**sigmod**函数，下半部分是真值表。

我们可以用这样的一个神经网络表示**AND** 函数：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/809187c1815e1ec67184699076de51f2.png)

其中$\theta_0 = -30, \theta_1 = 20, \theta_2 = 20$
我们的输出函数$h_\theta(x)$即为：$h_\Theta(x)=g\left( -30+20x_1+20x_2 \right)$

我们知道$g(x)$的图像是：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/6d652f125654d077480aadc578ae0164.png)



![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/f75115da9090701516aa1ff0295436dd.png)

所以我们有：$h_\Theta(x) \approx \text{x}_1 \text{AND} \, \text{x}_2$

所以我们的：$h_\Theta(x) $

这就是**AND**函数。

接下来再介绍一个**OR**函数：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/aa27671f7a3a16545a28f356a2fb98c0.png)

**OR**与**AND**整体一样，区别只在于的取值不同。

## 样本和直观理解Ⅱ

二元逻辑运算符（**BINARY LOGICAL OPERATORS**）当输入特征为布尔值（0或1）时，我们可以用一个单一的激活层可以作为二元逻辑运算符，为了表示不同的运算符，我们只需要选择不同的权重即可。

下图的神经元（三个权重分别为-30，20，20）可以被视为作用同于逻辑与（**AND**）：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/57480b04956f1dc54ecfc64d68a6b357.png)

下图的神经元（三个权重分别为-10，20，20）可以被视为作用等同于逻辑或（**OR**）：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/7527e61b1612dcf84dadbcf7a26a22fb.png)

下图的神经元（两个权重分别为 10，-20）可以被视为作用等同于逻辑非（**NOT**）：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/1fd3017dfa554642a5e1805d6d2b1fa6.png)

我们可以利用神经元来组合成更为复杂的神经网络以实现更复杂的运算。例如我们要实现**XNOR** 功能（输入的两个值必须一样，均为1或均为0），即 $\text{XNOR}=( \text{x}_1\, \text{AND}\, \text{x}_2 )\, \text{OR} \left( \left( \text{NOT}\, \text{x}_1 \right) \text{AND} \left( \text{NOT}\, \text{x}_2 \right) \right)$
首先构造一个能表达$\left( \text{NOT}\, \text{x}_1 \right) \text{AND} \left( \text{NOT}\, \text{x}_2 \right)$部分的神经元：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/4c44e69a12b48efdff2fe92a0a698768.png)

然后将表示 **AND** 的神经元和表示$\left( \text{NOT}\, \text{x}_1 \right) \text{AND} \left( \text{NOT}\, \text{x}_2 \right)$的神经元以及表示 OR 的神经元进行组合：

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/1035701-20170414202618236-244168000.jpg)

我们就得到了一个能实现 $\text{XNOR}$ 运算符功能的神经网络。

按这种方法我们可以逐渐构造出越来越复杂的函数，也能得到更加厉害的特征值。

这就是神经网络的厉害之处。

## 多类分类

当我们有不止两种分类时（也就是$y=1,2,3….$），比如以下这种情况，该怎么办？如果我们要训练一个神经网络算法来识别路人、汽车、摩托车和卡车，在输出层我们应该有4个值。例如，第一个值为1或0用于预测是否是行人，第二个值用于判断是否为汽车。

输入向量$x$有三个维度，两个中间层，输出层4个神经元分别用来表示4类，也就是每一个数据在输出层都会出现${\left[ a\text{ }b\text{ }c\text{ }d \right]}^T$，且$a,b,c,d$中仅有一个为1，表示当前类。下面是该神经网络的可能结构示例：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/f3236b14640fa053e62c73177b3474ed.jpg)

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/685180bf1774f7edd2b0856a8aae3498.png)

神经网络算法的输出结果为四种可能情形之一：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/5e1a39d165f272b7f145c68ef78a3e13.png)