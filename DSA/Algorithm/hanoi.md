---
title: Hanoi
categories:
- DSA
- Algorithm
tags:
- recursion
date: 2020/12/14 20:00:14
updated: 2020/12/14 22:00:14
---

# hanoi

## Solution

```python
def hanoi_move(n ,source, dest, intermediate):
	if n>=1:
		hanoi_move(n-1,source,intermediate,dest)
		print('move %s to %s'%(source,dest))
		hanoi_move(n-1,intermediate,dest,source)

hanoi_move(3,'A','B','C')
"""
move A to B
move A to C
move B to C
move A to B
move C to A
move C to B
move A to B
"""
```

```
n(a,c,b) -> # n块，从a挪到c，需要借助b
			n-1(a,b,c) # 先把上面的n-1块，从a挪到b，需要借助c
			a->c # 之后挪动最下面的块
			n-1(b,c,a) # 在把上面的n-1块，从b挪到c，借助a

当n=1时，解决方案是确定的，a->c
```

## 递归

**为什么不能用循环**

1. 不知道循环次数
2. 每次挪动时，需要求出从哪挪到哪的通项公式

**递归解决什么问题**

1. 规模大->规模小
2. 最终有确定解