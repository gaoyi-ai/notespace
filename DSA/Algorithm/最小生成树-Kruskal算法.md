---
title: Kruskal
categories:
- DSA
- Algorithm
- Graph
tags:
- MST
date: 2019/8/1 20:00:14
updated: 2020/12/10 12:00:14
---



![Kruskal](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20191108130835450.jpg)

### Kruskal算法
**更为直接地贪心，每次从图中找：
没有收录的 不会构成回路的 权重最小的边**

```python
class Edge:
    def __init__(self, weight=None, v1=None, v2=None):
        self.weight = weight
        self.v1 = v1
        self.v2 = v2
    def __lt__(self, other): # 比较类的对象比其他小的
        return self.weight < other.weight
class MSTNode:
    def __init__(self,v1=None,v2=None):
        self.v1 = v1
        self.v2 = v2
graph = {
    1:{2:2,4:1,3:4},
    2:{5:10,4:3,1:2},
    3:{1:4,6:5,4:2},
    4:{3:2,6:8,7:4,5:7,2:3,1:1},
    5:{7:6,2:10,4:7},
    6:{7:1,4:8,3:5},
    7:{6:1,4:4,5:6},
}
INFINITY = float('inf')
# 转化为二维矩阵表示图，两点不邻接用无穷大表示
graph = [[graph[v1].get(v2,INFINITY) for v2 in graph] for v1 in graph]
```

```python
class SetADT:
    def __init__(self):
        self.set = list()  # 默认集合初始元素为-1 
    def find(self, target):
        if self.set[target] < 0:  # 找到集合的根
            return target
        else:
            # 先找到根，把根变成x的父结点，再返回根
            self.set[target] = self.find(self.set[target]) # 路径压缩
            return self.set[target]
    def union(self, par1=None, par2=None):
        if self.set[par1] < self.set[par2]:  # 按秩归并
            self.set[par1] += self.set[par2]
            self.set[par2] = par1
        else:
            self.set[par2] += self.set[par1]
            self.set[par1] = par2
```


```python
from heapq import *
def InitEdge(graph):
    EdgeSet = list()
    for row in range(len(graph)):
        for col in range(row+1,len(graph[row])): # 避免重复录入无向图的边，只收单向边，取上三角矩阵，不包括对角线
            if graph[row][col] < INFINITY:
                EdgeSet.append(Edge(graph[row][col],row,col))
    heapify(EdgeSet) # 生成最小堆
    return EdgeSet
def CheckCircle(VertexSet,v1,v2): # 检查连接V1和V2的边是否在现有的最小生成树子集中构成回路
    root1 = VertexSet.find(v1) # 得到V1所属的连通集名称
    root2 = VertexSet.find(v2) # 得到V2所属的连通集名称
    if root1 == root2: # 若V1和V2已经连通，则该边不能要
        return False
    else: # 否则该边可以被收集，同时将V1和V2并入同一连通集
        VertexSet.union(root1,root2)
        return True
def Kruskal(graph):
    VertexSet = SetADT()
    VertexSet.set = [-1 for i in range(len(graph))] # 初始化顶点集合
    EdgeSet = InitEdge(graph)
    MST = list()
    TotalWeight = 0
    EdgeCount = 0
    while EdgeCount < len(graph)-1: # 一共要找V-1条边
        next_edge = heappop(EdgeSet) # 从边集中得到最小边
        if next_edge is None:
            break
        if CheckCircle(VertexSet,next_edge.v1,next_edge.v2):
            # 如果该边的加入不构成回路，即两端结点不属于同一连通集
            MST.append(MSTNode(next_edge.v1+1,next_edge.v2+1)) # 将该边插入MST，由于集合定义从下标为0开始，所以这里要＋1
            TotalWeight += next_edge.weight
            EdgeCount += 1
    if EdgeCount < len(graph)-1: # 当MST的边数＜V-1时，说明图不是连通的
        TotalWeight = None
    return TotalWeight
```

各邻接点:
6 - 7
1 - 4
3 - 4
1 - 2
4 - 7
5 - 7
MST : 集合根结点为4,树规模为7
[3, 3, 3, -7, 3, 3, 3]
树的权重: 16

