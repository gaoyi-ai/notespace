---
title: Git - 内部原理
categories:
- SE
- Git
tags:
- Git
date: 2021/6/26
---



> [www.lzane.com](https://www.lzane.com/tech/git-internal/)

![](https://www.lzane.com/tech/git-internal/git-update-file.gif)

> 这是一个系列的文章，计划包括三篇：
> 
> *   这才是真正的 Git——Git 内部原理
> *   [这才是真正的 Git——分支合并](https://www.lzane.com/tech/git-merge/)
> *   [这才是真正的 Git——Git 实用技巧](https://www.lzane.com/tech/git-tips/)

TL;DR
-----

本文以一个具体例子结合动图介绍了 Git 的内部原理，包括 Git 是什么储存我们的代码和变更历史的、更改一个文件时，Git 内部是怎么变化的、Git 这样实现的有什么好处等等。

通过例子解释清楚上面这张动图，让大家了解 Git 的内部原理。如果你已经能够看懂这张图了，下面的内容可能对你来说会比较基础。

> 本文是 2019/11/24 在深圳腾讯大厦 2 楼多功能厅举办的 FCC 前端分享会（freeCodeConf 2019 深圳站）上分享的文字版。
> 
> 视频：[https://www.bilibili.com/video/av77252063](https://www.bilibili.com/video/av77252063)
> 
> PPT：[https://www.lzane.com/slide/git-under-the-hood](https://www.lzane.com/slide/git-under-the-hood)

前言
--

近几年技术发展十分迅猛，让部分同学养成了一种学习知识停留在表面，只会调用一些指令的习惯。我们时常有一种 “我会用这个技术、这个框架” 的错觉，等到真正遇到问题，才发现事情没有那么简单。

后来我开始沉下心，回归一开始接触编程的时候，那时候学习一个知识都会深入一点去思考背后的原理。但这并不是说掌握并会使用高级 Api 不重要，他们也非常重要，并且是日常工作中大部分时间都在使用的，快速掌握它们意味着高效学习，可以快速的应用在开发生产上。

只是有时候知道一些底层的东西，可以更好的帮你理清思路，知道你真正在操作什么，不会迷失在 Git 大量的指令和参数上面。

Git 是怎么储存信息的
------------

这里会用一个简单的例子让大家直观感受一下 git 是怎么储存信息的。

首先我们先创建两个文件

```
$ git init
$ echo '111' > a.txt
$ echo '222' > b.txt
$ git add *.txt
```

Git 会将整个数据库储存在`.git/`目录下，如果你此时去查看`.git/objects`目录，你会发现仓库里面多了两个 object。

```
$ tree .git/objects
.git/objects
├── 58
│   └── c9bdf9d017fcd178dc8c073cbfcbb7ff240d6c
├── c2
│   └── 00906efd24ec5e783bee7f23b5d7c941b0c12c
├── info
└── pack
```

好奇的我们来看一下里面存的是什么东西

```
$ cat .git/objects/58/c9bdf9d017fcd178dc8c073cbfcbb7ff240d6c
xKOR0a044K%
```

怎么是一串乱码？这是因为 Git 将信息压缩成二进制文件。但是不用担心，因为 Git 也提供了一个能够帮助你探索它的 api `git cat-file [-t] [-p]`， `-t`可以查看 object 的类型，`-p`可以查看 object 储存的具体内容。

```
$ git cat-file -t 58c9
blob
$ git cat-file -p 58c9
111
```

可以发现这个 object 是一个 blob 类型的节点，他的内容是 111，也就是说这个 object 储存着 a.txt 文件的内容。

这里我们遇到第一种 Git object，blob 类型，它只储存的是一个文件的内容，不包括文件名等其他信息。然后将这些信息经过 SHA1 哈希算法得到对应的哈希值 58c9bdf9d017fcd178dc8c073cbfcbb7ff240d6c，作为这个 object 在 Git 仓库中的唯一身份证。

也就是说，我们此时的 Git 仓库是这样子的：

![](https://www.lzane.com/tech/git-internal/p1s1.png)

我们继续探索，我们创建一个 commit。

```
$ git commit -am '[+] init'
$ tree .git/objects
.git/objects
├── 0c
│   └── 96bfc59d0f02317d002ebbf8318f46c7e47ab2
├── 4c
│   └── aaa1a9ae0b274fba9e3675f9ef071616e5b209
...
```

我们会发现当我们 commit 完成之后，Git 仓库里面多出来两个 object。同样使用`cat-file`命令，我们看看它们分别是什么类型以及具体的内容是什么。

```
$ git cat-file -t 4caaa1
tree
$ git cat-file -p 4caaa1
100644 blob 58c9bdf9d017fcd178dc8c0... 	a.txt
100644 blob c200906efd24ec5e783bee7...	b.txt
```

这里我们遇到了第二种 Git object 类型——tree，它将当前的目录结构打了一个快照。从它储存的内容来看可以发现它储存了一个目录结构（类似于文件夹），以及每一个文件（或者子文件夹）的权限、类型、对应的身份证（SHA1 值）、以及文件名。

此时的 Git 仓库是这样的：

![](https://www.lzane.com/tech/git-internal/p1s2.png)

```
$ git cat-file -t 0c96bf
commit
$ git cat-file -p 0c96bf
tree 4caaa1a9ae0b274fba9e3675f9ef071616e5b209
author lzane 李泽帆  1573302343 +0800
committer lzane 李泽帆  1573302343 +0800
[+] init
```

接着我们发现了第三种 Git object 类型——commit，它储存的是一个提交的信息，包括对应目录结构的快照 tree 的哈希值，上一个提交的哈希值（这里由于是第一个提交，所以没有父节点。在一个 merge 提交中还会出现多个父节点），提交的作者以及提交的具体时间，最后是该提交的信息。

此时我们去看 Git 仓库是这样的：

![](https://www.lzane.com/tech/git-internal/p1s3.png)

到这里我们就知道 Git 是怎么储存一个提交的信息的了，那有同学就会问，我们平常接触的分支信息储存在哪里呢？

```
$ cat .git/HEAD
ref: refs/heads/master

$ cat .git/refs/heads/master
0c96bfc59d0f02317d002ebbf8318f46c7e47ab2
```

在 Git 仓库里面，HEAD、分支、普通的 Tag 可以简单的理解成是一个指针，指向对应 commit 的 SHA1 值。

![](https://www.lzane.com/tech/git-internal/p1s4.png)

其实还有第四种 Git object，类型是 tag，在添加含附注的 tag（`git tag -a`）的时候会新建，这里不详细介绍，有兴趣的朋友按照上文中的方法可以深入探究。

至此我们知道了 Git 是什么储存一个文件的内容、目录结构、commit 信息和分支的。**其本质上是一个 key-value 的数据库加上默克尔树形成的有向无环图（DAG）**。这里可以蹭一下区块链的热度，区块链的数据结构也使用了默克尔树。

Git 的三个分区
---------

接下来我们来看一下 Git 的三个分区（工作目录、Index 索引区域、Git 仓库），以及 Git 变更记录是怎么形成的。了解这三个分区和 Git 链的内部原理之后可以对 Git 的众多指令有一个 “可视化” 的理解，不会再经常搞混。

接着上面的例子，目前的仓库状态如下：

![](https://www.lzane.com/tech/git-internal/3area.png)

这里有三个区域，他们所储存的信息分别是：

*   工作目录 （ working directory ）：操作系统上的文件，所有代码开发编辑都在这上面完成。
*   索引（ index or staging area ）：可以理解为一个暂存区域，这里面的代码会在下一次 commit 被提交到 Git 仓库。
*   Git 仓库（ git repository ）：由 Git object 记录着每一次提交的快照，以及链式结构记录的提交变更历史。

我们来看一下更新一个文件的内容这个过程会发生什么事。

![](https://www.lzane.com/tech/git-internal/p2s1.gif)

运行`echo "333" > a.txt`将 a.txt 的内容从 111 修改成 333，此时如上图可以看到，此时索引区域和 git 仓库没有任何变化。

![](https://www.lzane.com/tech/git-internal/p2s2.gif)

运行`git add a.txt`将 a.txt 加入到索引区域，此时如上图所示，git 在仓库里面新建了一个 blob object，储存了新的文件内容。并且更新了索引将 a.txt 指向了新建的 blob object。

![](https://www.lzane.com/tech/git-internal/p2s3.gif)

运行`git commit -m 'update'`提交这次修改。如上图所示

1.  Git 首先根据当前的索引生产一个 tree object，充当新提交的一个快照。
2.  创建一个新的 commit object，将这次 commit 的信息储存起来，并且 parent 指向上一个 commit，组成一条链记录变更历史。
3.  将 master 分支的指针移到新的 commit 结点。

至此我们知道了 Git 的三个分区分别是什么以及他们的作用，以及历史链是怎么被建立起来的。**基本上 Git 的大部分指令就是在操作这三个分区以及这条链。**可以尝试的思考一下 git 的各种命令，试一下你能不能够在上图将它们 **“可视化”** 出来，这个很重要，建议尝试一下。

如果不能很好的将日常使用的指令 “可视化” 出来，推荐阅读 [图解 Git](https://marklodato.github.io/visual-git-guide/index-zh-cn.html)

一些有趣的问题
-------

有兴趣的同学可以继续阅读，这部分不是文章的主要内容

### 问题 1：为什么要把文件的权限和文件名储存在 tree object 里面而不是 blob object 呢？

想象一下修改一个文件的命名。

如果将文件名保存在 blob 里面，那么 Git 只能多复制一份原始内容形成一个新的 blob object。而 Git 的实现方法只需要创建一个新的 tree object 将对应的文件名更改成新的即可，原本的 blob object 可以复用，节约了空间。

### 问题 2：每次 commit，Git 储存的是全新的文件快照还是储存文件的变更部分？

由上面的例子我们可以看到，Git 储存的是全新的文件快照，而不是文件的变更记录。也就是说，就算你只是在文件中添加一行，Git 也会新建一个全新的 blob object。那这样子是不是很浪费空间呢?

这其实是 Git 在空间和时间上的一个取舍，思考一下你要 checkout 一个 commit，或对比两个 commit 之间的差异。如果 Git 储存的是问卷的变更部分，那么为了拿到一个 commit 的内容，Git 都只能从第一个 commit 开始，然后一直计算变更，直到目标 commit，这会花费很长时间。而相反，Git 采用的储存全新文件快照的方法能使这个操作变得很快，直接从快照里面拿取内容就行了。

当然，在涉及网络传输或者 Git 仓库真的体积很大的时候，Git 会有垃圾回收机制 gc，不仅会清除无用的 object，还会把已有的相似 object 打包压缩。

### 问题 3：Git 怎么保证历史记录不可篡改？

通过 SHA1 哈希算法和哈系树来保证。假设你偷偷修改了历史变更记录上一个文件的内容，那么这个问卷的 blob object 的 SHA1 哈希值就变了，与之相关的 tree object 的 SHA1 也需要改变，commit 的 SHA1 也要变，这个 commit 之后的所有 commit SHA1 值也要跟着改变。又由于 Git 是分布式系统，即所有人都有一份完整历史的 Git 仓库，所以所有人都能很轻松的发现存在问题。

希望大家读完有所收获。感兴趣的同学可以阅读同系列的其他文章

*   [这才是真正的 Git——分支合并](https://www.lzane.com/tech/git-merge/)
*   [这才是真正的 Git——Git 实用技巧](https://www.lzane.com/tech/git-tips/)

参考
--

*   [Scott Chacon, Ben Straub - Pro Git-Apress (2014)](https://git-scm.com/book/en/v1) 免费，有兴趣继续深入的同学推荐阅读这本书
*   [Jon Loeliger, Matthew McCullough - Version Control with Git, 2nd Edition - O’Reilly Media (2012)](https://www.amazon.com/Version-Control-Git-collaborative-development/dp/1449316387/ref=sr_1_1?keywords=Version+Control+with+Git&qid=1573794832&sr=8-1) 作为上面那本书的补充
