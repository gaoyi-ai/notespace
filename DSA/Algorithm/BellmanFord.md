---
title: Bellman Ford
date: 2020-12-17 16:19:32
categories:
- DSA
- Algorithm
- Graph
tags:
- BFS
- shortestPath
updated: 2020/12/17 17:00:14
---

# Bellman Ford

◼ Bellman-Ford 也属于单源最短路径算法，支持负权边，还能检测出是否有负权环
算法原理：对所有的边进行 V – 1 次松弛，即更新源点到每个顶点的最短路径，操作（ V 是节点数量），得到所有可能的最短路径
时间复杂度： O(EV)，E 是边数量，V 是节点数量

◼ 下图的最好情况是恰好从左到右的顺序对边进行松弛操作
对所有边仅需进行 1 次松弛操作就能计算出A到达其他所有顶点的最短路径

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217162133498.png" alt="image-20201217162133498" style="zoom:80%;" />

◼ 最坏情况是恰好每次都从右到左的顺序对边进行松弛操作，即即每一轮松弛仅确定一个顶点的最短路径
对所有边需进行 V – 1 次松弛操作才能计算出A到达其他所有顶点的最短路径

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217162233535.png" alt="image-20201217162233535" style="zoom:80%;" />

### OOP Solution

```java
    private Map<V, PathInfo<V, E>> bellmanFord(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return null;

        Map<V, PathInfo<V, E>> selectedPaths = new HashMap<>();
        // 源点处理
        selectedPaths.put(begin, new PathInfo<>(weightManager.zero()));

        int count = vertices.size() - 1;
        for (int i = 0; i < count; i++) { // v - 1 次
            for (Edge<V, E> edge : edges) {
                PathInfo<V, E> fromPath = selectedPaths.get(edge.from.value);
                if (fromPath == null) continue;
                relax(edge, fromPath, selectedPaths);
            }
        }

        for (Edge<V, E> edge : edges) {
            PathInfo<V, E> fromPath = selectedPaths.get(edge.from.value);
            if (fromPath == null) continue;
            if (relax(edge, fromPath, selectedPaths)) {
                System.out.println("有负权环");
                return null;
            }
        }

        selectedPaths.remove(begin);
        return selectedPaths;
    }


    /**
     * 松弛
     * @param edge     需要进行松弛的边
     * @param fromPath edge的from的最短路径信息
     * @param paths    存放着其他点（对于dijkstra来说，就是还没有离开桌面的点）的最短路径信息
     */
    private boolean relax(Edge<V, E> edge, PathInfo<V, E> fromPath, Map<V, PathInfo<V, E>> paths) {
        // 新的可选择的最短路径：beginVertex到edge.from的最短路径 + edge.weight
        E newWeight = weightManager.add(fromPath.weight, edge.weight);
        // 以前的最短路径：beginVertex到edge.to的最短路径
        PathInfo<V, E> oldPath = paths.get(edge.to.value);
        if (oldPath != null && weightManager.compare(newWeight, oldPath.weight) >= 0) return false;

        if (oldPath == null) {
            oldPath = new PathInfo<>();
            paths.put(edge.to.value, oldPath);
        } else {
            oldPath.edgeInfos.clear();
        }

        oldPath.weight = newWeight;
        oldPath.edgeInfos.addAll(fromPath.edgeInfos);
        oldPath.edgeInfos.add(edge.info());

        return true;
    }
```

