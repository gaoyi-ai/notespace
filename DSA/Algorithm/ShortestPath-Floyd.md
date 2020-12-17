---
title: Floyd
categories:
- DSA
- Algorithm
- Graph
tags:
- shortestPath
date: 2019/8/1 20:00:14
updated: 2020/12/17 12:00:14
---

# Floyd

Floyd 属于多源最短路径算法，能够求出任意2个顶点之间的最短路径，支持负权边
时间复杂度： O(V3) ，效率比执行 V 次 Dijkstra 算法要好（ V 是顶点数量）

## 算法原理

从任意顶点 i 到任意顶点 j 的最短路径不外乎两种可能
① 直接从 i 到 j
② 从 i 经过若干个顶点到 j

假设 dist(i，j) 为顶点 i 到顶点 j 的最短路径的距离
对于每一个顶点 k，检查 dist(i，k) + dist(k，j)＜dist(i，j) 是否成立

✓ 如果成立，证明从 i 到 k 再到 j 的路径比 i 直接到 j 的路径短，设置 dist(i，j) = dist(i，k) + dist(k，j)
✓ 当我们遍历完所有结点 k，dist(i，j) 中记录的便是 i 到 j 的最短路径的距离

![image-20201217152908137](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217152908137.png)

### OOP Solution

```java
    @Override
    public Map<V, Map<V, PathInfo<V, E>>> shortestPath() {
        Map<V, Map<V, PathInfo<V, E>>> paths = new HashMap<>();
        // 初始化
        for (Edge<V, E> edge : edges) {
            Map<V, PathInfo<V, E>> map = paths.get(edge.from.value);
            if (map == null) {
                map = new HashMap<>();
                paths.put(edge.from.value, map);
            }

            PathInfo<V, E> pathInfo = new PathInfo<>(edge.weight);
            pathInfo.edgeInfos.add(edge.info());
            map.put(edge.to.value, pathInfo);
        }

        vertices.forEach((V v2, Vertex<V, E> vertex2) -> {
            vertices.forEach((V v1, Vertex<V, E> vertex1) -> {
                vertices.forEach((V v3, Vertex<V, E> vertex3) -> {
                    if (v1.equals(v2) || v2.equals(v3) || v1.equals(v3)) return;

                    // v1 -> v2
                    PathInfo<V, E> path12 = getPathInfo(v1, v2, paths);
                    if (path12 == null) return;

                    // v2 -> v3
                    PathInfo<V, E> path23 = getPathInfo(v2, v3, paths);
                    if (path23 == null) return;

                    // v1 -> v3
                    PathInfo<V, E> path13 = getPathInfo(v1, v3, paths);

                    E newWeight = weightManager.add(path12.weight, path23.weight);
                    if (path13 != null && weightManager.compare(newWeight, path13.weight) >= 0) return;

                    if (path13 == null) {
                        path13 = new PathInfo<V, E>();
                        paths.get(v1).put(v3, path13);
                    } else {
                        path13.edgeInfos.clear();
                    }

                    path13.weight = newWeight;
                    path13.edgeInfos.addAll(path12.edgeInfos);
                    path13.edgeInfos.addAll(path23.edgeInfos);
                });
            });
        });

        return paths;
    }

    private PathInfo<V, E> getPathInfo(V from, V to, Map<V, Map<V, PathInfo<V, E>>> paths) {
        Map<V, PathInfo<V, E>> map = paths.get(from);
        return map == null ? null : map.get(to);
    }
```

### Matrix Solution

 - dist表示为从i到j的最短路径，但是只经过编号小于等于k的路径
 - 最短路径是一步一步生成的，从0个顶点开始，一步一步增加顶点
 - 初始矩阵对角元全部是0，不邻接为无穷大

当dist前k-1步已经完成，递推地求解dist第k步

- 如果k不在从i到j的最短路径中，即不影响最短路径，dist第k步＝第k-1步

- 如果k在从 i 到 j 的最短路径中，从 i 到 j 一定经过k，则该路径必然由两条路径组成，从 i 到 k 再从k到 j 的路径，而从 i 到k和从k到 j 都不经过k即它们被确定最小

![Floyd](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20191109095539825.jpg)

```python
graph = {
    1:{2:2,4:1},
    2:{5:10,4:3},
    3:{1:4,6:5},
    4:{3:2,6:8,7:4,5:2},
    5:{7:6},
    6:{},
    7:{6:1},
}
INFINITY = float('inf')
graph = [[graph[v1].get(v2,INFINITY) if v1 != v2 else 0 for v2 in graph] for v1 in graph]
```


```python
from copy import deepcopy
def Floyd(graph):
    global dist,path
    dist = deepcopy(graph)
    path = [[-1 for i in range(len(graph))] for j in range(len(graph))]
    for k in range(len(graph)):
        for i in range(len(graph)):
            for j in range(len(graph)):
                if dist[i][k] + dist[k][j] < dist[i][j]:
                    if i!=j: # 环的距离为0
                        dist[i][j] = dist[i][k] + dist[k][j]
                        path[i][j] = k # 如果dist从i到k＋从k到j更小，path就一定经过k
                    if i == j and dist[i][j]< 0:
                        return False
    return True
```
```python
def show_path(path,start,end):
    road = list()
    k = path[start-1][end-1]
    road.append(start)
    while k != -1:
        road.append(k+1)
        k = path[k][end-1]
    road.append(end)
```
```
从4到2的最短路径：4-3-1-2

距离：
    [0, 2, 3, 1, 3, 6, 5]
    [9, 0, 5, 3, 5, 8, 7]
    [4, 6, 0, 5, 7, 5, 9]
    [6, 8, 2, 0, 2, 5, 4]
    [inf, inf, inf, inf, 0, 7, 6]
    [inf, inf, inf, inf, inf, 0, inf]
    [inf, inf, inf, inf, inf, 1, 0]
    路径：(各点＋1为图中的点)
    [-1, -1, 3, -1, 3, 6, 3]
    [3, -1, 3, -1, 3, 6, 3]
    [-1, 0, -1, 0, 3, -1, 3]
    [2, 2, -1, -1, -1, 6, -1]
    [-1, -1, -1, -1, -1, 6, -1]
    [-1, -1, -1, -1, -1, -1, -1]
    [-1, -1, -1, -1, -1, -1, -1]
```

