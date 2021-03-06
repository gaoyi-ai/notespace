---
title: Prim
categories:
- DSA
- Algorithm
- Graph
tags:
- MST
date: 2019/8/1 20:00:14
updated: 2020/12/17 12:00:14
---



![Prim](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20191108124929234.jpg)

# MST - Minimum Spanning Tree

特点：首先是一棵树（没有回路）即，V个顶点一定有V-1条边

其次是生成树：
包含所有顶点，并且只能从现有边生成树的边，向生成树中任意加一条边都一定会构成回路
且 边的权重和最小

**贪心算法：**每一步都要最好的，什么是好？即权重最小的边

但是有约束：只能用图中的边，只能正好用V-1条边，不能构成回路

![image-20201217153642715](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217153642715.png)

适用于有权的连通图（无向）

## 切分定理
◼ 切分（Cut）：把图中的节点分为两部分，称为一个切分
◼ 下图有个切分 C = (S, T)，S = {A, B, D}，T = {C, E}

![image-20201217154238560](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217154238560.png)

◼ 横切边（Crossing Edge）：如果一个边的两个顶点，分别属于切分的两部分，这个边称为横切边
比如上图的边 BC、BE、DE 就是横切边
◼ 切分定理：给定任意切分，横切边中权值最小的边必然属于最小生成树

## Prim

◼ 假设 G = (V，E) 是有权的连通图（无向），A 是 G 中最小生成树的边集
算法从 S = {  u0 }（ u0 ∈ V），A = { } 开始，重复执行下述操作，直到 S = V 为止
✓ 找到切分 C = (S，V – S) 的最小横切边 ( u0 ， v0 ) 并入集合 A，同时将 v0 并入集合 S

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217154435990.png" alt="image-20201217154435990" style="zoom:67%;" />

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217154507737.png" alt="image-20201217154507737" style="zoom:67%;" />

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217154531094.png" alt="image-20201217154531094" style="zoom:67%;" />

### OOP Solution

```java
    private Set<EdgeInfo<V, E>> prim() {
        Iterator<Vertex<V, E>> it = vertices.values().iterator();
        if (!it.hasNext()) return null;
        Vertex<V, E> vertex = it.next();
        Set<EdgeInfo<V, E>> edgeInfos = new HashSet<>();
        // 每次加入的节点都处于同一个set下
        // 而kruskal并不是都在同一个set，所以需要并查集
        Set<Vertex<V, E>> addedVertices = new HashSet<>();
        addedVertices.add(vertex);
        // 利用minHeap拿到最小权重边
        MinHeap<Edge<V, E>> heap = new MinHeap<>(vertex.outEdges, edgeComparator);
        int verticesSize = vertices.size();
        while (!heap.isEmpty() && addedVertices.size() < verticesSize) {
            Edge<V, E> edge = heap.remove();
            // 去重 后加入的边的to一定存于addedVertices
            if (addedVertices.contains(edge.to)) continue;
            edgeInfos.add(edge.info());
            addedVertices.add(edge.to);
            heap.addAll(edge.to.outEdges);
        }
        return edgeInfos;
    }
```



### 链表实现

```python
graph = {
    1:{2:2,4:1,3:4},
    2:{5:10,4:3,1:3},
    3:{1:4,6:5,4:2},
    4:{3:2,6:8,7:4,5:7,2:3,1:1},
    5:{7:6,2:10,4:7},
    6:{7:1,4:8,3:5},
    7:{6:1,4:4,5:6},
}
INFINITY = float('inf')
```


```python
def FindMinDist(graph,dist):
    MinDist = INFINITY
    for node in graph:
        if dist[node] != 0 and dist[node] < MinDist: # 与Dijkstra算法类似，这里是把dist置为0来表示已收录在MST中
            # 若V未被收录，且dist[V]更小
            MinDist = dist[node]
            MinNode = node
    if MinDist < INFINITY:
        return MinNode
    else:
        return None

def Prim(graph): # 将最小生成树保存为邻接表存储的图MST，返回最小权重和
    dist = {}
    parent = {} # 类似集合，双亲表示法，找到树中结点的父结点
    for node in graph:
        dist[node] = graph[1].get(node,INFINITY) # 初始化树的根节点为1
        parent[node] = 1
    total_wight = 0
    node_count = 0
    mst = {1:0} # 根节点的父结点特殊化
    dist[1] = 0
    node_count +=1
    parent[1] = 0

    while True:
        min_node = FindMinDist(graph, dist)
        if min_node is None:
            break
        mst[min_node] = parent[min_node] # 更新当前权值最小的结点的父结点
        total_wight += dist[min_node]
        dist[min_node] = 0 # 把它并入树中，就是它到MST的距离为0
        node_count += 1

        for node in graph:
            if dist[node] != 0 and graph[min_node].get(node,INFINITY) < INFINITY: # 与Dijkstra算法类似，当前结点的没有入树的邻接结点
                if graph[min_node][node] < dist[node]: # 如果当前结点的权值＜现在它到树根的距离
                    dist[node] = graph[min_node][node]
                    parent[node] = min_node
    if node_count < len(graph): # 收录的结点＜图中的结点，图不是连通的
            total_wight = None
    return total_wight
```

```
  最小生成树的权重和：16
  MST的集合表示：  {1: 0, 4: 1, 2: 1, 3: 4, 7: 4, 6: 7, 5: 7}
```


​    