---
title: Graph Coloring
categories:
- DSA
- DS
- Graph
tags:
- Coloring
date: 2019/8/1 20:00:14
updated: 2020/12/10 12:00:14
---



着色问题解决的第一步也是最重要的一步就是把结点按度数从大到小排好序

其目的就是可以找到邻接点最多的点，即邻接点使用不同颜色，从而着色更少的点，得到最小着色数

```python
graph = {
    1:[2,3,4,7],
    2:[1,3,4,5],
    3:[1,2,5,6,7],
    4:[1,2,5,7],
    5:[2,3,4,6,7,8],
    6:[3,5,8],
    7:[1,3,4,5,8],
    8:[5,6,7],
}
length = len(graph)
# 生成二维矩阵图
graph = [[1 if col+1 in graph[row+1] else 0 for col in range(length)]for row in range(length)]
deg = {}
for row in range(len(graph)):
    deg[row+1] = graph[row].count(1)
# 把结点按度数降序排好
nodes = sorted(deg,key=deg.get,reverse=True)

painted = set()
cnt = 0 # 记录着色数
res = list()
for v in nodes:
    if v not in painted: # 每次找未着色的点
        tmp = list() # 保存相同颜色的结点
        for i in range(length):
            if graph[v-1][i] == 0 and i+1 not in painted: # 如果两结点不邻接且其邻接点没有被着色
                painted.add(i+1)
                tmp.append(i+1)
        res.append(tmp)
        cnt += 1
        if len(painted) == length: # 当着色的结点数＝总结点数，结束
            break
print('着色情况:',res) # 着色情况: [[1, 5], [3, 4, 8], [2, 6, 7]]
print('着色数:',cnt) # 着色数: 3
```

