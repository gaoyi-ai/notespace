```python
def CountingSort(seq):
    """
    进行一次累加操作之后，
    对于存在数个相等的元素a[x]
    其中最后一个a[x]记作a[x]_last，即在 a[x] 中处于最右边位置
    则在排序后，有 (cnt[i] - 1) 即为 a[x]_last 前面存在的元素个数。
    保证排序结果的稳定
    """
    n = len(seq)
    max_num = max(seq)
    min_num = min(seq)
    res = [0 for ele in seq]
    cnt = [0 for ele in range(min_num,max_num+1)] # 找到计数数组的最小长度
    for i in range(n):
        cnt[seq[i]-min_num] += 1
    for i in range(1,max_num+1-min_num):
        cnt[i] += cnt[i-1] # 将数组的计数结果合并累加起来，从而形成连续的位置索引
    for i in range(n-1,-1,-1):
        item = seq[i] - min_num # 找到正确位置(因为cnt可以理解成下标从seq的最小数开始的)
        pos = cnt[item]-1 # cnt[item]-1直接记录当前它是第几个(从0开始)
        res[pos] = item + min_num # 恢复原数
        cnt[item] -= 1 # (解决相同数的问题)
    return res
```
CountingSort( [5,8,3,8,10,7,9] )
res： [3, 5, 7, 8, 8, 9, 10]
   
