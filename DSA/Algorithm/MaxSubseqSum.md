---
title: 最大子列和
categories:
- DSA
- Algorithm
- LeetCode
tags:
- dp
- Divide and conquer
date: 2019/8/1 20:00:14
updated: 2020/12/10 12:00:14
---


 - 暴力遍历解法
 - 优化解法
 - 在线处理解法
 - 分治法

> 个人觉得解法1最先想到，再仔细观察是解法2
> 解法3不好想到，但理解起来不算太难；解法4是真的不好理解(本人对递归理解较弱)

```python
def maxsubseqsum(seq):
    n= len(seq)
    maxsum = 0
    for i in range(n):
        for j in range(i,n):
            thissum = 0
            for k in range(i,j+1):
                thissum += seq[k]
                if thissum > maxsum:
                    maxsum = thissum
    return maxsum
```

**这是最为直观的求解方式，但时间复杂度为O(n*3)**
**相当于每一次重复加上i，j的元素**


```python
def maxsubseqsum2(seq):
    n= len(seq)
    maxsum = 0
    for i in range(n):
        thissum = 0
        for j in range(i,n):
            thissum += seq[j]
            if thissum>maxsum:
                maxsum = thissum
    return maxsum
```

**每次要算一遍子列和，对与这一次的子列和就等于上一次的子列和再加一个**
**相比于上面的算法，从i到j，加和到thissum中，那么下一个子列和就是上一次的加上一个，少了一次循环**


```python
def maxsubseqsum3(seq):
    n= len(seq)
    thissum = maxsum = 0
    for i in range(n):
        thissum+=seq[i]
        if thissum >maxsum:
            maxsum = thissum
        elif thissum<0:
            thissum = 0
    return maxsum
```

**在线处理**
**当前的子列和如果是负的，那么就把它置0，因为一个负数只能让和减小，于是就从0开始加起，相当于丢掉那个数**
**在任何一个地方终止输入，都能对当前输出正确结果**


```python
def divideandconquer(subseq,left,right):
    if left == right : # 若subseq只有一个数，递归终止
        if subseq[left] > 0: # 这里当这个数大于0是，算上这个一个数的subseq的最大
            return subseq[left] # 如果是负数，则默认sum以0开始加和，则丢掉这个数
        else:
            return 0
    center =(left +right)//2

    maxleftsum = divideandconquer(subseq,left,center)
    maxrightsum = divideandconquer(subseq,center+1,right)

    maxleftbordersum = leftbordersum = 0
    for i in range(center,left-1,-1): # 从中线向左扫描
        leftbordersum += subseq[i]
        if leftbordersum>maxleftbordersum:
            maxleftbordersum = leftbordersum

    maxrightbordersum = rightbordersum = 0
    for i in range(center+1,right+1): # 从中线向右扫描
        rightbordersum += subseq[i]
        if rightbordersum > maxrightbordersum:
            maxrightbordersum = rightbordersum

    return max(maxleftsum,maxrightsum,maxleftbordersum+maxrightbordersum)
def maxsubseqsum4(seq):
    n = len(seq)
    return divideandconquer(seq,0,n-1)
```

**分而治之**
**把数组一分为二，找到左边的，再找到右边的，最后再找到跨越边界的**

递归结束判断还是有点懵,求大神指教
