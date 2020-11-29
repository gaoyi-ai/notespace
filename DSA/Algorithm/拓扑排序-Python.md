
### 拓扑排序- TopSort
**拓扑序：
如果图中从V到W有一条有向路径，则V一定排在W之前**

**AOV(Activity On Vertex)
如果有合理的拓扑序，则一定是有向无环图(Directed Acyclic Graph, DAG）**
![DAG](images/%E6%8B%93%E6%89%91%E6%8E%92%E5%BA%8F-Python/20191108141234483.jpg)
![TopSort](images/%E6%8B%93%E6%89%91%E6%8E%92%E5%BA%8F-Python/20191108141115515.jpg)

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

