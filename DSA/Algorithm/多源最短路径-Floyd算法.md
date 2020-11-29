
### Floyd算法-Python实现

 - **dist表示为从i到j的最短路径，但是只经过编号小于等于k的路径**
 - **最短路径是一步一步生成的**
 - **从0个顶点开始，一步一步增加顶点**
 - **初始矩阵对角元全部是0，不邻接为无穷大**

**当dist前k-1步已经完成，递推地求解dist第k步**

**如果k不在从i到j的最短路径中，即不影响最短路径，dist第k步＝第k-1步**

**如果k在从i到j的最短路径中，从i到j一定经过k，则该路径必然由两条路径组成，
从i到k再从k到j的路径，而从i到k和从k到j都不经过k即它们被确定最小**

![Floyd](images/%E5%A4%9A%E6%BA%90%E6%9C%80%E7%9F%AD%E8%B7%AF%E5%BE%84-Floyd%E7%AE%97%E6%B3%95/20191109095539825.jpg)

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
    

