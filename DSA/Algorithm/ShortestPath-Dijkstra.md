---
title: Dijkstra
categories:
- DSA
- Algorithm
- Graph
tags:
- BFS
- shortestPath
date: 2019/8/1 20:00:14
updated: 2020/12/17 12:00:14
---



@[toc]

# BFS

```python
graph = {
    1:[2,4],
    2:[4,5],
    3:[1,6],
    4:[3,5,6,7],
    5:[7],
    6:[],
    7:[6],
}

from collections import deque

dist = {}
path = {}
def bfs(graph,start): # 无权最短路径问题就是bfs算法的改进
    search = deque()
    search.append(start)
    path[start] = 0 # 源点的没有上一个结点，要特殊化
    dist[start] = 0
    while search:
        cur =search.popleft()
        for node in graph[cur]:
            if dist.get(node) is None: # 对于当前结点的邻接点，如果没有被访问过
                dist[node] = dist[cur] + 1 # 当前路径长度要＋1
                path[node] = cur # 此邻接点的上一步为当前结点
                search.append(node)

bfs(graph,3) # 源点为3
print('Path:')
print(path)
print('Distance:')
print(dist)
```

    Path:
    {3: 0, 1: 3, 6: 3, 2: 1, 4: 1, 5: 2, 7: 4}
    Distance:
    {3: 0, 1: 1, 6: 1, 2: 2, 4: 2, 5: 3, 7: 3}

# Shortest Path

最短路径是指两顶点之间权值之和最小的路径（有向图、无向图均适用，不能有**负权环**）

无权图相当于是全部边权值为1的有权图

◼ 有负权边，但没有负权环时，存在最短路径

![image-20201217160559421](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217160559421.png)

◼ A到E的最短路径是：A → B → E

◼ 有负权环时，不存在最短路径

![image-20201217160709118](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217160709118.png)

◼ 通过负权环， A到E的路径可以无限短
A → E → D → F → E → D → F → E → D → F → E → D → F → E → ......

# Dijkstra

◼ Dijkstra 属于单源最短路径算法，用于计算一个顶点到其他所有顶点的最短路径
使用前提：不能有负权边
时间复杂度：可优化至 O ElogV ，E 是边数量，V 是节点数量

## Dijkstra – 等价思考
◼ Dijkstra 的原理其实跟生活中的一些自然现象完全一样
把每1个顶点想象成是1块小石头
每1条边想象成是1条绳子，每一条绳子都连接着2块小石头，边的权值就是绳子的长度
将小石头和绳子平放在一张桌子上（下图是一张俯视图，图中黄颜色的是桌子）

![image-20201217160930636](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217160930636.png)

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217160956332.png)



◼ 接下来想象一下，手拽着小石头A，慢慢地向上提起来，远离桌面
B、D、C、E会依次离开桌面
**最后绷直的绳子就是A到其他小石头的最短路径**

◼ 有一个很关键的信息
后离开桌面的小石头
✓ 都是被先离开桌面的小石头拉起来的

## 执行过程

◼ 绿色
已经“离开桌面”
已经确定了最终的最短路径

◼ 红色：更新了最短路径信息

![image-20201217161231739](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217161231739.png)

![image-20201217161301609](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217161301609.png)

![image-20201217161344270](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217161344270.png)

![image-20201217161404631](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217161404631.png)

◼ 松弛操作（Relaxation）：更新2个顶点之间的最短路径
这里一般是指：更新源点到另一个点的最短路径
松弛操作的意义：尝试找出更短的最短路径
◼ 确定A到D的最短路径后，对DC、DE边进行松弛操作，更新了A到C、A到E的最短路径

（想象两点之间通过弹簧连接.并且保持绷直,如果两点之间距离缩短,那么弹簧就会变松弛）

![Dijkstra](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20191106152114798.jpg)

### OOP Solution

```java

    private Map<V, PathInfo<V, E>> dijkstra(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return null;

        Map<V, PathInfo<V, E>> selectedPaths = new HashMap<>();
        Map<Vertex<V, E>, PathInfo<V, E>> paths = new HashMap<>();
        // 初始化paths
        paths.put(beginVertex, new PathInfo<>(weightManager.zero()));
//		for (Edge<V, E> edge : beginVertex.outEdges) {
//			PathInfo<V, E> path = new PathInfo<>();
//			path.weight = edge.weight;
//			path.edgeInfos.add(edge.info());
//			paths.put(edge.to, path);
//		}

        while (!paths.isEmpty()) {
            Entry<Vertex<V, E>, PathInfo<V, E>> minEntry = getMinPath(paths);
            // minVertex离开桌面
            Vertex<V, E> minVertex = minEntry.getKey();
            PathInfo<V, E> minPath = minEntry.getValue();
            selectedPaths.put(minVertex.value, minPath);
            paths.remove(minVertex);
            // 对它的minVertex的outEdges进行松弛操作
            for (Edge<V, E> edge : minVertex.outEdges) {
                // 如果edge.to已经离开桌面，就没必要进行松弛操作
                if (selectedPaths.containsKey(edge.to.value)) continue;
                relaxForDijkstra(edge, minPath, paths);
            }
        }
        // 去除起点
        selectedPaths.remove(begin);
        return selectedPaths;
    }

    /**
     * 松弛
     *
     * @param edge     需要进行松弛的边
     * @param fromPath edge的from的最短路径信息
     * @param paths    存放着其他点（对于dijkstra来说，就是还没有离开桌面的点）的最短路径信息
     */
    private void relaxForDijkstra(Edge<V, E> edge, PathInfo<V, E> fromPath, Map<Vertex<V, E>, PathInfo<V, E>> paths) {
        // 新的可选择的最短路径：beginVertex到edge.from的最短路径 + edge.weight
        E newWeight = weightManager.add(fromPath.weight, edge.weight);
        // 以前的最短路径：beginVertex到edge.to的最短路径
        PathInfo<V, E> oldPath = paths.get(edge.to);
        if (oldPath != null && weightManager.compare(newWeight, oldPath.weight) >= 0) return;
        // 以前就不存在路径
        if (oldPath == null) {
            oldPath = new PathInfo<>();
            paths.put(edge.to, oldPath);
        } else { // 清除旧路径
            oldPath.edgeInfos.clear();
        }

        oldPath.weight = newWeight;
        oldPath.edgeInfos.addAll(fromPath.edgeInfos);
        oldPath.edgeInfos.add(edge.info());
    }

    /**
     * 从paths中挑一个最小的路径出来
     */
    private Entry<Vertex<V, E>, PathInfo<V, E>> getMinPath(Map<Vertex<V, E>, PathInfo<V, E>> paths) {
        Iterator<Entry<Vertex<V, E>, PathInfo<V, E>>> it = paths.entrySet().iterator();
        Entry<Vertex<V, E>, PathInfo<V, E>> minEntry = it.next();
        while (it.hasNext()) {
            Entry<Vertex<V, E>, PathInfo<V, E>> entry = it.next();
            if (weightManager.compare(entry.getValue().weight, minEntry.getValue().weight) < 0) {
                minEntry = entry;
            }
        }
        return minEntry;
    }
```



### 链表实现


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

def shortest_path(graph,dist,visited): # 返回未被收录顶点中dist最小者
    MinDist = float('inf')
    for node in graph: # 这里为全部遍历一遍，还可以采用最小堆
        if node not in visited and dist[node] < MinDist:
            # 若V未被收录，且dist[V]更小
            MinDist = dist.get(node)
            MinNode = node
    if MinDist < float('inf'):
        return MinNode
    else:
        return None

def Dijkstra(graph,start):
    # 初始化
    global visited,path,dist
    visited = set()
    path = {}
    dist = {}
    for node in graph:
        dist[node] = graph[start].get(node, float('inf')) 
        # 如果图中的点和当前结点不是邻接点，那么从当前结点到图中的点的dist定义为无穷大
        # 否则dist为权值
        if dist[node] < float('inf'): # 如果邻接，那么定义好路径
            path[node] = start
    # 访问源点
    visited.add(start)
    dist[start] = 0
    path[start] = -1 # 源点path特殊化
    while True:
        min_node = shortest_path(graph,dist,visited)
        if min_node is None:
            break
# 这里用下面三行实现shortest_path的功能，不过fromkeys每次都会新建一个dist
#       if len(graph) == len(visited): # 如果都被访问过，结束
#           break
#       min_node = min(dist.fromkeys(filter(lambda node:node not in visited,graph)),key=dist.get)
        visited.add(min_node) # 找到当前结点的邻接未访问的权值最小的点，访问它(使它成为当前结点)
        for node in graph: # 此循环找到
            if node not in visited and node in graph[min_node]: # 表示此节点node是当前结点的邻接点
                if graph[min_node][node] < 0: # 不能处理负值圈
                    return False
                if dist[min_node] + graph[min_node].get(node,float('inf')) < dist[node]: 
                    # 如果当前结点的dist加上当前结点到它的权值＜它的dist，更新dist，path
                    dist[node] = dist[min_node] + graph[min_node].get(node,float('inf'))
                    path[node] = min_node
    return True

print(Dijkstra(graph,1))
print('path',path)
print('dist',dist)
```

    True
    path {2: 1, 4: 1, 1: -1, 3: 4, 5: 4, 6: 7, 7: 4}
    dist {1: 0, 2: 2, 3: 3, 4: 1, 5: 3, 6: 6, 7: 5}

