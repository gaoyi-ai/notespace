@[toc]
# 无权图-BFS

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


# 有权图-Dijkstra算法

![Dijkstra](images/%E5%8D%95%E6%BA%90%E6%9C%80%E7%9F%AD%E8%B7%AF%E5%BE%84-Dijkstra%E7%AE%97%E6%B3%95/20191106152114798.jpg)


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

