![Prim](images/%E6%9C%80%E5%B0%8F%E7%94%9F%E6%88%90%E6%A0%91-Prim%E7%AE%97%E6%B3%95/20191108124929234.jpg)
### 最小生成树(Minimum Spanning Tree)
**特点：
首先是一棵树：
没有回路
V个顶点一定有V-1条边**

**其次是生成树：
包含所有顶点
并且只能从现有边生成树的边
向生成树中任意加一条边都一定会构成回路
边的权重和最小**

### 贪心算法
**每一步都要最好的
什么是好？
即权重最小的边**

**但是有约束：
只能用图中的边
只能正好用V-1条边
不能构成回路**

Prim算法代码实现：
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

  最小生成树的权重和：16
  MST的集合表示：  {1: 0, 4: 1, 2: 1, 3: 4, 7: 4, 6: 7, 5: 7}
    
