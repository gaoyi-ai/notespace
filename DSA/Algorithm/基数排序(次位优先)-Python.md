---
title: 基数排序
categories:
- DSA
- Algorithm
- Sort
date: 2019/8/1 20:00:14
updated: 2020/12/10 12:00:14
---



 - **次位优先-Least Significant Digit**

**先比较每个数字的低位，排好之后，再按更高位的数排序，以此类推
注意：高位没有数字默认为0**

![LSD](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20191120133421415.jpg)

```python
def LSD(seq):
    max_digit = 1 # 初始默认最大位数为1位
    # 找到最大值，更新最大位数
    max_num = max(seq) 
    while max_num > 10**max_digit:
        max_digit += 1
    
    for i in range(max_digit):
    """从低位(个位)开始,按0-9排一遍，更新一遍seq"""
        bucket = {} # 使用字典构建桶
        for x in range(10): # 设置0-9十个空桶
            bucket.setdefault(x,[])
        for x in seq:
            radix = (x//(10**i)) % 10 # 得到每位的基数
            bucket[radix].append(x) # 放入对应桶中
            
        j = 0
        for k in range(10):
            if bucket[k]: # 若桶不为空
                for ele in bucket[k]: # 将该桶中每个元素放回到数组中
                    seq[j] = ele
                    j+=1
    return seq
```
LSD( [64,8,512,27,729,0,1,343,125] )
res :    [0, 1, 8, 27, 64, 125, 343, 512, 729]

