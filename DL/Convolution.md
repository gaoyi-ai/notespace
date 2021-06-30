---
title: Convolution
categories:
- DL
- CNN
tags:
- convolution
date: 2021/3/22 20:00:17
updated: 2021/3/22 12:00:17
---

# Convolution (computer science)

在[计算机科学](https://en.wikipedia.org/wiki/Computer_science)中，特别是在[形式语言](https://en.wikipedia.org/wiki/Formal_language)中，**卷积**(有时称为 以zip 开头)是一种映射[序列](https://en.wikipedia.org/wiki/Sequence)的[tuple](https://en.wikipedia.org/wiki/Tuple)的函数。 )转换成[tuple](https://en.wikipedia.org/wiki/Tuple)的[sequence](https://en.wikipedia.org/wiki/Sequence)。 该名称zip源自[zipper](https://en.wikipedia.org/wiki/Zipper)的作用，它交错了两个以前不相交的序列。 反向功能是unzip，它执行反卷积。

## Example

Given the three words *cat*, *fish* and *be* where |*cat*| is 3, |*fish*| is 4 and |*be*| is 2. Let $\ell$denote the length of the longest word which is *fish*; $ \ell =4$. The convolution of *cat*, *fish*, *be* is then 4 tuples of elements:

${\displaystyle (c,f,b)(a,i,e)(t,s,\#)(\#,h,\#)}$

where *#* is a symbol not in the original alphabet. In [Haskell](https://en.wikipedia.org/wiki/Haskell_(programming_language)) this truncates to shortest sequence ${\underline {\ell }}$, where${\underline {\ell }}=2$:

```haskell
zip3 "cat" "fish" "be"
-- [('c','f','b'),('a','i','e')]
```

---

> [从“卷积”、到“图像卷积操作”、再到“卷积神经网络”，“卷积”意义的3次改变_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1VV411478E)

