---
title: Pytorch
categories:
- DL
- Pytorch
tags:
- neural network
date: 2021/3/29 20:00:16
updated: 2021/3/29 12:00:16
---

# Pytorch

## 计算图

在PyTorch的张量计算过程中，如果我们设置初始张量是可微的，则在计算过程中，每一个由原张量计算得出的新张量都是可微的，并且还会保存此前一步的函数关系，这也就是所谓的回溯机制。而根据这个回溯机制，我们就能非常清楚掌握张量的每一步计算，并据此绘制张量计算图。

借助回溯机制，我们就能将张量的复杂计算过程抽象为一张图（Graph),例如此前我们定义的x、y、z三个张量，三者的计算关系就可以由下图进行表示。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20210329090255259.png" alt="image-20210329090255259" style="zoom:50%;" />

### 计算图的定义

上图就是用于记录可微分张量计算关系的张量计算图，图由节点和有向边构成，其中节点表示张量，边表示函数计算关系，方向则表示实际运算方向，张量计算图本质是有向无环图。

### 节点类型

在张量计算图中，虽然每个节点都表示可微分张量，但节点和节点之间却略有不同。就像在前例中，y和z保存了函数计算关系，但x没有，而在实际计算关系中，我们不难发现z是所有计算的终点，因此，虽然x、y、z都是节点，但每个节点却并不一样。此处我们可以将节点分为三类，分别是：

a):叶节点，也就是初始输入的可微分张量，前例中x就是叶节点；
b):输出节点，也就是最后计算得出的张量，前例中z就是输出节点；
c):中间节点，在一张计算图中，除了叶节点和输出节点，其他都是中间节点，前例中y就是中间节点。

当然，在一张计算图中，可以有多个叶节点和中间节点，但大多数情况下，只有一个输出节点，若存在多个输出结果，我们也往往会将其保存在一个张量中。

## 2.反向传播运算注意事项

### 中间节点反向传播和输出节点反向传播区别

尽管中间节点也可进行反向传播，但很多时候由于存在复合函数关系，中间节点反向传播的计算结果和输出节点反向传播输出结果并不相同。

# 梯度下降

##  1.最小二乘法的局限与优化

在所有的优化算法中最小二乘法虽然高效并且结果精确，但也有不完美的地方，核心就在于最小二乘法的使用条件较为苛刻，要求特征张量的交叉乘积结果必须是满秩矩阵，才能进行求解，而在实际情况中，很多数据的特征张量并不能满足条件，此时就无法使用最小二乘法进行求解。

最小二乘法结果：
$$
\hat{w}^T=(X^TX)^{-1}X^Ty
$$
当最小二乘法失效的情况时，其实往往也就代表原目标函数没有最优解或最优解不唯一。针对这样的情况，有很多中解决方案，例如，我们可以在原矩阵方程中加入一个扰动项$\lambda I$,修改后表达式如下：
$$
\hat{w}^T=(X^TX + \lambda I)^{-1}X^Ty
$$
其中，$\lambda$是扰动项系数，$\bf I$是单元矩阵。由矩阵性质可知，加入单位矩阵后，$(X^TX+\lambda \bf I)$部分一定可逆，而后即可直接求解$\hat{w}^{T*}$,这也就是岭回归的一般做法。

当然，上式修改后求得的结果就不再是全域最小值，而是一个接近最小值的点。鉴于许多目标函数本身也并不存在最小值或者唯一最小值，在优化的过程中略有偏差也是可以接受的。当然，伴随着深度学习的逐渐深入，我们会发现，最小值并不唯存在才是目标函数的常态，基于此情况，很多根据等式形变得到的精确的求解析解的优化方法（如最小二乘）就无法适用，此时我们需要寻找一种更加通用的，能够高效、快速逼近目标函数优化目标的最优化方法。在机器学习领域，最通用的求解目标函数的最优化方法就是著名的梯度下降算法。

## 3.梯度下降的方向与步长

当然，梯度下降的基本思想好理解，但实现起来却并不容易（这也是大多数机器学习算法的常态）。在实际治着目标函数下降的过程中，我们核心需要解决两个问题，其一是往哪个方向走，其二是每一步走多远，以上述简单线性回日的目标函数为例，在三维空间中，目标函数上的每个点理论上都有无数个移动的方向，每次移动多远的物理距离也没有明显的约束，而这些就是梯度下降算法核心需要解决的问题，也就是所谓的方向和步长

首先，是关于方向的讨论。其实梯度下降是采用了一种局部最优推导全域最优的思路，我们首先是希望能够找到让目标函数变化最快的方向作为移动的方向，而这个方向，就是梯度。

# 在 MINST-FASHION上实现神经网络的学习流程

实现一个完整的训练流程。首先要梳理一下整个流程

1)设置超参数:步长$lr$,动量值$gamma$,迭代次数`epochs`,`batch_size`等信息,(如果需要)设置初始权重$w_0$
2)导入数据,将数据切分成 batches

3)定义神经网络架构

4)定义损失函数$L(w)$,如果需要的话,将损失函数调整成凸函数,以便求解最小值

5)定义所使用的优化算法

6)开始在`epochs`和`batch`上循环,执行优化算法
	6.1)调整数据结构,确定数据能够在神经网络、损失函数和优化算法中顺利运行
	6.2)完成向前传播,计算初始损失
	6.3)利用反向传播,在损失函数$L(w)$上对每一个求偏导数
	6.4)迭代当前权重
	6.5)清空本轮梯度
	6.6)完成模型进度与效果监控#loss、准确率 accuracy

7)输出结果

```python
#导入库
import torch
from torch import nn
from torch import optim
from torch.nn import functional as F
from torch.utils.data import DataLoader, TensorDataset
import torchvision
import torchvision, transforms as transform

#确定数据、确定超参数
lr = 0.15
gamma = 0.8
epochs = 5
bs = 128

mnist = torchvision.datasets.FashionMNIST(root ="\DL\.."
                                          ,download = False
                                          ,train = True
                                         ,transform = transforms.ToTensor())
# 放入进行送代的数据结构是什么样的?
batchdata = DataLoader(mnist
                       ,batch_size = bs
                       ,shuffle = True)
"""
for x,y in batchdata:
	print(x.shape)
	print(y.shape)
	break
"""
#torch.Size([128,1,28,28])
#torch.Size([128])

input_ = mnist.data[0].numel() #请问这个张量中总共有多少元素呢
output_= len(mnist.targets.unique())

#定义神经网络的架构
class Model(nn.Module):
	def init(self, in_features=10, out_features=2)
		super().__init__()
		self.linear1 = nn.Linear(in_features, 128, bias=False)
		self.output = nn.Linear(128, out_features, bias=False)
	
	def forward(self, x):
		x = x.view(-1,28*28)
		sigma1 = torch.relu(self.linear1(x))
		sigma2 = F.log_softmax(self.output(sigma1)
                               
"""
view(-1,)#需要对数据结构进行一个改变,-1作为占位符
"""

#定义损失函数、优化算法、梯度下降的流程
#定义一个训练函数
def fit(net, bacthdata, lr=0.01, epochs=5, gamma= 0):
	criterion = nn.NLLLoss()
	opt = optim.SGD(net.parameters(), lr=lr, momentum = gamma)
	correct=0#循环开始之前,预测正确的值为0
	samples = 0 #循环开始之前,模型一个样本都没有见过
	for epoch in range(epochs): #全数据被训练几次
		for batch_idx, (x,y) in enumerate(batchdata):
			y=y.view(x. shape[0])降维
			sigma=net.forward(x)#正向传播
			loss = criterion(sigma, y)
			loss.backward()
			opt.zero_grad()
			
			#求解准确率,全部判断正确的样本数量/已经看过的总样本量
			yhat= torch.max(sigma,1)[1]# torch.max函数结果中的索引为1的部分
			correct+= torch.sum(yhat == y)			
			samples += x.shape[0]
			#每训练一个batch的数据,模型见过的数据就会增加x.shape[0]
			if( batch_idx+1)%125==0 or batch_idx == len(batch_data)-1: #每N个batch就打印一次
				print("Epoch{}: [{}/{}({:.0f}%)], Loss:{:.6f}, Accuracy:{:.3f}".format(epoch+1
				,samples
				,epochs*len(batchdata.dataset)
				,100*samples/(epochs*len(batchdata.dataset))
				,loss.data.item()
				,float(100*correct*samples))
                     )
```

