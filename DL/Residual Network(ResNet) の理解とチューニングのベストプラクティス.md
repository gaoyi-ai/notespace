---
title: 残差网络 (ResNet) 理解和调整最佳实践
categories:
- DL
- Modern
tags:
- ResNet
date: 2021/6/6 20:00:16
updated: 2021/6/6 12:00:16
---



> [Residual Network(ResNet)の理解とチューニングのベストプラクティス](https://deepage.net/deep_learning/2016/11/30/resnet.html)

*   [What is Residual Network（ResNet）](#residual-networkresnetとは)
*   [ResNet のアイデア](#resnetのアイデア)
*   [Shortcut Connection の導入](#shortcut-connectionの導入)
*   [Bottleneck アーキテクチャ](#bottleneckアーキテクチャ)
*   [ResNet の最適化ベストプラクティス](#resnetの最適化ベストプラクティス)
*   [Optimizer の選定](#optimizerの選定)
*   [Batch Normalization の位置](#batch-normalizationの位置)
*   [Post Activation vs Pre Activation](#post-activation-vs-pre-activation)
*   [Wide Residual Network](#wide-residual-network)
*   [参考](#参考)

本記事では、

*   Residual Network とは何か
*   Residual Network のチューニング方法
*   Residual Network の派生モデル

# What is Residual Network（ResNet）

[CNN](https://deepage.net/deep_learning/2016/11/07/convolutional_neural_network.html) において層を深くすることは重要な役割を果たす。層を重ねるごとに、より高度で複雑な特徴を抽出していると考えられているからだ。
加深层在CNN中起着重要作用。 这是因为人们认为每一层都提取了更复杂和更复杂的特征。

![](https://deepage.net/img/convolutional_neural_network/conv.jpg)

Convolution 層はフィルタを持ち、Pooling 層と組み合わせて何らかの特徴を検出する役割を持っている。低・中・高レベルの特徴を多層形式で自然に統合し、認識レベルを強化することができる。
Convolution层有过滤器，与Pooling层结合，起到检测一些特征的作用。 低、中、高层次特征可以以多层格式自然融合，以提升识别水平。

## ResNet 的想法

ResNet 以前も層を更に深くする試みはあったものの、思い通りに学習が進まなかった。
甚至在 ResNet 之前，就有尝试加深层，但学习没有按预期进行。

![](https://deepage.net/img/resnet/not_easy_deep.jpg)

単純に層を深くすると、性能が悪化することが報告されていた。
据报道，简单地加深层会降低性能。

では、ResNet はどのようにしてニューラルネットワークのモデルを深くすることを可能にしたのだろうか？
那么 ResNet 是如何让深度神经网络模型成为可能的呢？

ResNet のアイデアはシンプルで、「**ある層で求める最適な出力を学習するのではなく、層の入力を参照した残差関数を学習する**」 ことで最適化しやすくしている。
ResNet的思想很简单，通过“**学习参考层的输入的残差函数，而不是学习某一层的最优输出**”，很容易优化。

つまり、H(x) が学習して欲しい関数だとすると、入力との差分は F(x) := H(x) - x となり、H(x) := F(x) + x を学習するように再定義する。
换句话说，如果H(x)是你要学习的函数，与输入的差值将是F(x):=H(x)--x，这样H(x):=F(x) + x 将被学习。重新定义。

## Shortcut Connection

では、どのように実現したらいいのだろう？
那你怎么做呢？

ResNet では、**残差ブロック**と **Shortcut Connection** を導入することで実現している。一般的なネットワークが以下のような図だとしたら、
ResNet 通过引入 **残差块** 和 **Shortcut Connection** 实现了这一点。 如果一个典型的网络如下图所示，

![](https://deepage.net/img/resnet/plain_network.jpg)

残差ブロックはこうなる。
残差块看起来像这样。

![](https://deepage.net/img/resnet/residual_block.jpg)

残差ブロックでは、畳込み層と Skip Connection の組み合わせになっている。2 つの枝から構成されていて、それぞれの要素を足し合わせる。残差ブロックの一つは Convolution 層の組み合わせで、もう一つは Identity 関数となる。
残差块是卷积层和Skip Connection的组合。 它由两个分支组成，每个分支的元素相加。 残差块之一是卷积层的组合，另一个是恒等函数。

こうすれば、仮に追加の層で変換が不要でも weight を 0 にすれば良い。
这样，即使附加层不需要转换，权重也可以设置为 0。

残差ブロックを導入することで、結果的に層の深度の限界を押し上げることができ、精度向上を果たすことが出来た。
通过引入残差块，可以将层的深度限制推高，从而提高精度。

### Bottleneck アーキテクチャ 瓶颈架构

Residual Block には 2 つのアーキテクチャがある。

*   Plain アーキテクチャ
*   Bottleneck アーキテクチャ

で、こちらが Plain アーキテクチャとなっている。

![](https://deepage.net/img/resnet/plain_architecture.jpg)

3×3 の Convolution 層が 2 つある。そして、こちらが Bottleneck アーキテクチャで残差ブロックの中が少しだけ変わっている。

![](https://deepage.net/img/resnet/bottleneck_architecture.jpg)

この 2 つは同等の計算コストとなるが、Bottleneck アーキテクチャの方は Plain よりも 1 層多くなる。1×1 と 3×3 の Convolution 層で出力の Depth の次元を小さくしてから最後の 1×1 の Convolution 層で Depth の次元を復元することから **Bottleneck** という名前がついている。
两者具有相当的计算成本，但瓶颈架构比普通架构多一层。 之所以命名为**Bottleneck**，是因为1x1 和3x3 卷积层减少了输出的Depth 维度，然后最后一个1x1 卷积层恢复了Depth 维度。

## ResNet の最適化ベストプラクティス ResNet 优化最佳实践

それでは、実際の実装におけるパラメータのチューニングポイントを紹介する。Facebook の Torch のブログにある [Training and investigating Residual Nets](http://torch.ch/blog/2016/02/04/resnets.html) と、ResNet の派生モデルの提案

*   [Identity Mappings in Deep Residual Networks](https://arxiv.org/abs/1603.05027)
*   [Wide Residual Networks](https://arxiv.org/abs/1605.07146)

を参考にしている。

### Optimizer の選定

原論文には、SGD+Momentum を使うと書かれていたが Torch のブログでもこれが最良の結果となったようだ。

*   RMSprop
*   Adadelta
*   Adagrad
*   SGD+Momentum

で調査したところ、唯一 **SGD+Momentum だけが 0.7% を下回る**テストエラーとなったそう。

### Batch Normalization の位置

Batch Normalization を足し合わせる前に入れるか、足し合わせた後に入れるかを Torch のブログで検証している。
Torch 的博客研究了在添加之前还是之后添加 Batch Normalization。

Batch Normalization についてはこちら：

> [Batch Normalization：ニューラルネットワークの学習を加速させる汎用的で強力な手法](https://deepage.net/deep_learning/2016/10/26/batch_normalization.html)

![](https://deepage.net/img/resnet/bn_pos.jpg)

元は左の Batch Normalization を足し合わせる前に入れるタイプだが、右側の後に入れるタイプにするとどうなるだろうか？

この検証結果は、Batch Normalization を後に入れた場合、著しくテストデータにおける性能が落ちた。理由は、最後に Batch Normalization をすれば残差ブロック全てが正規化されて良いように見えるが、実際には Skip Connection の情報を Batch Normalization が大きく変更して情報の伝達を妨げてしまうからだそうだ。
这个验证结果在后来加入 Batch Normalization 时显着降低了测试数据的性能。 原因是看起来所有的残差块都可以通过最后的Batch Normalization进行归一化，但实际上，Batch Normalization显着改变了Skip Connection的信息，阻碍了信息的传递。

### Post Activation vs Pre Activation

次は、[Identity Mappings in Deep Residual Networks](https://arxiv.org/abs/1603.05027) においての検証で下図のように元の構成が左のようになっていた場合、Activation 関数と Batch Normalization の位置を前方に持ってくるとどうなるだろうか？

![](https://deepage.net/img/resnet/post_vs_pre.jpg)

この実験では、層の数が増えれば増えるほど顕著に Activation 関数と Batch Normalization を前に持ってきた後者のほうが良い結果となったそうだ。

画像認識の分類タスク ImageNet において、200 層の ResNet を使った実験ではエラーレート **1.1% の改善**をしている。

さらにメリットとして、Pre Activation の場合 Batch Normalization を前方に持ってくることで正則化の役割が強くなったという結果が報告されている。

### Wide Residual Network

最後は Wide Residual Network で、こちらは Batch Normalization や Activation 関数の順序ではなく、フィルタの数を増やしてネットワークを” **広く** “したらどうなるのか？

ということを検証したものだ。以下の表を見て欲しい。

![](https://deepage.net/img/resnet/wide_param.jpg)

N

が残差ブロックの数となっていて、k が” 広さ” 係数だ。[] の中のパラメータは、フィルタサイズ, フィルタの数で広いネットワークでは、フィルタの数が大幅に増えることになる。

こうすることで、広さの係数を 10 倍にすると 1000 層の” 薄い”ResNet と同等のパラメータ数を持つこととなり、さらに GPU による処理をフルに活かすことができるようになる。

そして実験結果では、” 薄い”ResNet よりも高い精度を **50 分の 1 の層数で、半分の時間の訓練時間**になったそうだ。

そしてさらに、Convolution 層の間に Dropout を入れることで更なる性能向上を果たすことができたということが報告されている。

Dropout についてはこちら：

> [Dropout：ディープラーニングの火付け役、単純な方法で過学習を防ぐ](https://deepage.net/deep_learning/2016/10/17/deeplearning_dropout.html)

以下の図のように、最後の Convolution 層の手前に Dropout を入れる。

![](https://deepage.net/img/resnet/wide_resnet.jpg)

広さの係数 10 で 28 層の Wide ResNet に Dropout 率 30~40% 程度適用すると高い精度となったそうだ。

参考
-------------------------

*   [Deep Residual Learning for Image Recognition](https://arxiv.org/abs/1512.03385)
*   [Training and investigating Residual Nets](http://torch.ch/blog/2016/02/04/resnets.html)
*   [Identity Mappings in Deep Residual Networks](https://arxiv.org/abs/1603.05027)
*   [Wide Residual Networks](https://arxiv.org/abs/1605.07146)