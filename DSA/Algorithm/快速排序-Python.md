---
title: QuickSort
categories:
- DSA
- Algorithm
- Sort
tags:
- Divide and conquer
date: 2019/8/1 20:00:14
updated: 2020/12/10 12:00:14
---



@[toc]

**快速排序由于递归十分占用时间，对大规模数据排序更好**

### 快排实现1
```python
def partition(seq,begin,end):
    pivot_index = begin 
    pivot = seq[pivot_index] # 假定主元为序列首个
    left = pivot_index+1
    right = end -1
    while True:
        if left > right: # 如果左指针越过右指针就结束交换
            break
        else:
            seq[left], seq[right] = seq[right], seq[left] # 让大的放大的一边，小的放小的一边
        while left<=right and seq[left] < pivot: 
            # 左指针不越过右指针且左指针所指内容都小于主元，就一直向后找
            left +=1
        while seq[right] > pivot:
            right -=1
    seq[pivot_index] ,seq[right] = seq[right] ,seq[pivot_index] 
    # 最后主元与中值交换，此时right符合越过left，左边都小，右边都大
    return right

def quicksort_inplace(seq,begin,end):
    if begin < end:
        pivot = partition(seq,begin,end) # 找主元
        quicksort_inplace(seq,begin,pivot) # 递归排序左边
        quicksort_inplace(seq,pivot+1,end) # 递归排序右边
```
单测：
```python
def test_quicksort_inplace():
    seq = list(range(10))
    import random
    random.shuffle(seq)
    print(seq) # [3, 1, 2, 9, 6, 0, 4, 8, 5, 7]
    quicksort_inplace(seq,0,len(seq))
    print(seq) # [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
```
**注意** 
**当正好有元素等于pivot时，		
1.停下交换，这样median3中不会进行交换，
主元会被放在一个中间的位置，这就相当于二分查找，为O nlogn
2.不会处理，直接忽略，那么low会一直找到high，而high不会动
就导致主元被放在某一端，为O n²**



### 快排实现2
```python
def partition(seq,bottom,top):
    pivot = seq[bottom] # 假设主元为所取区间的首个
    low = bottom
    for i in range(low+1,top+1): 
        # i相当于指针，从主元向后找小于假定主元的
        if seq[i] < pivot:
            low += 1
            seq[low], seq[i] = seq[i], seq[low] # seq的low指的都是比pivot小的
    seq[bottom], seq[low] = seq[low], seq[bottom] # 令主元与比主元小的后一个交换，把主元放到中间位置
    return low
def quicksort(seq,bottom,top):
    if bottom<top: # 如果区间seq的长度＞1
        split = partition(seq,bottom,top)
        quicksort(seq,bottom,split-1)
        quicksort(seq,split+1,top)
    else: # 否则，一个元素的seq一定有序
        return

seq = [12,30,21,8,6,9,7]
quicksort(seq,0,6)
print(a) # [6, 7, 8, 9, 12, 21, 30]
```
