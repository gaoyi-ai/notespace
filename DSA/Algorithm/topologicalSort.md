---
title: Topological Sort
categories:
- DSA
- Algorithm
- Sort
tags:
- graph
- AOV
date: 2019/8/1 20:00:14
updated: 2020/12/17 12:00:14
---

# AOV

◼ 一项大的工程常被分为多个小的子工程
✓ 子工程之间可能存在一定的先后顺序，即某些子工程必须在其他的一些子工程完成后才能开始

◼ 在现代化管理中，人们常用有向图来描述和分析一项工程的计划和实施过程，子工程被称为活动（Activity）
✓ 以顶点表示活动、有向边表示活动之间的先后关系，这样的图简称为 AOV 网

◼ 标准的AOV网必须是一个有向无环图（Directed Acyclic Graph，简称 DAG）

![image-20201217155042009](images/%E6%8B%93%E6%89%91%E6%8E%92%E5%BA%8F-Python/image-20201217155042009.png)

◼ B依赖于A
◼ C依赖于B
◼ D依赖于B
◼ E依赖于B、C、D
◼ F依赖于E

### Topological Sort

◼ 什么是拓扑排序？
将 AOV 网中所有活动排成一个序列，使得每个活动的前驱活动都排在该活动的前面
比如上图的拓扑排序结果是：A、B、C、D、E、F 或者 A、B、D、C、E、F （结果并不一定是唯一的）

◼ 前驱活动：有向边起点的活动称为终点的前驱活动
只有当一个活动的前驱全部都完成后，这个活动才能进行

◼ 后继活动：有向边终点的活动称为起点的后继活动



![DAG](images/%E6%8B%93%E6%89%91%E6%8E%92%E5%BA%8F-Python/20191108141234483.jpg)

◼ 可以使用卡恩算法（Kahn于1962年提出）完成拓扑排序

假设 L 是存放拓扑排序结果的列表
① 把所有入度为 0 的顶点放入 L 中，然后把这些顶点从图中去掉
② 重复操作 ①，直到找不到入度为 0 的顶点
如果此时 L 中的元素个数和顶点总数相同，说明拓扑排序完成
如果此时 L 中的元素个数少于顶点总数，说明原图中存在环，无法进行拓扑排序

![TopSort](images/%E6%8B%93%E6%89%91%E6%8E%92%E5%BA%8F-Python/20191108141115515.jpg)

### OOP Solution

```java
    @Override
    public List<V> topologicalSort() {
        List<V> list = new ArrayList<>();
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        // 维持节点与其入度的映射, 拿到入度为0的, 并且不需要将初始图中入度为0的节点加入
        Map<Vertex<V, E>, Integer> ins = new HashMap<>();
        // 初始化（将度为0的节点都放入队列）
        vertices.forEach((V v, Vertex<V, E> vertex) -> {
            int in = vertex.inEdges.size();
            if (in == 0) {
                queue.offer(vertex);
            } else {
                ins.put(vertex, in);
            }
        });

        while (!queue.isEmpty()) {
            Vertex<V, E> vertex = queue.poll();
            // 放入返回结果中
            list.add(vertex.value);

            for (Edge<V, E> edge : vertex.outEdges) {
                int toIn = ins.get(edge.to) - 1;
                if (toIn == 0) {
                    queue.offer(edge.to);
                } else {
                    ins.put(edge.to, toIn);
                }
            }
        }

        return list;
    }
```



### 链表实现

```python
graph = {
    1:[3],
    2:[3,13],
    3:[7],
    4:[5],
    5:[6],
    6:[15],
    7:[10,11,12],
    8:[9],
    9:[10,12],
    10:[14],
    11:[],
    12:[],
    13:[],
    14:[],
    15:[],
}
def flatten(a):
    if not isinstance(a, (list, )):
        return [a]
    else:
        b = []
        for item in a:
            b += flatten(item)
    return b
from collections import Counter
from collections import deque
def TopSort(graph):
    # 遍历图，得到InDegree
    cnt = Counter() # 使用Python内置的计数器
    for val in flatten(list(graph.values())): 
    # 拿到所有的邻接点因为是每一个结点的邻接点是List存储，所以要展平
        cnt[val] += 1
    InDegree = {node: cnt[node] for node in graph}
    # 将所有入度为0的顶点入列
    queue = deque()
    zero_indeg = [node for node in graph if InDegree[node] == 0]
    queue.extend(zero_indeg)
    # 拓扑排序
    TopOrder = list()
    while len(queue):
        node = queue.popleft()
        TopOrder.append(node)
        for adj in graph[node]: # 从图中拿走这一点，就是把它的邻接点的入度-1
            InDegree[adj] -= 1
            if InDegree[adj] == 0: # 上一步操作之后，还要对图中的结点进行入度判断
                queue.append(adj)
    if len(TopOrder) != len(graph): # 最后结果不包含全部的点，则图不连通
        return False
    else:
        return True
```

    [1, 2, 4, 8, 3, 13, 5, 9, 7, 6, 10, 11, 12, 15, 14]
    True

