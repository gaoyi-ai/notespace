@[toc]
### 欧拉图
**欧拉图：所有节点度数均为偶数**
**欧拉路：仅有2个奇度数节点**
**欧拉图(eular graph)：经过每个边仅一次走完图**
**桥：删去此边，图就不连通**

![eular](images/Fleury%E7%AE%97%E6%B3%95-%E6%AC%A7%E6%8B%89%E5%9B%BE/20191122183826793.jpg)

```python
def isEulerGraph(graph):
    degree = { i+1:graph[i].count(1) for i in range(len(graph))}
    for deg in degree.values():
        if deg % 2 == 1: # 当度数有为奇数的节点，就不是欧拉图
            return False
    return True
```


```python
def isBridge(v1,v2): # 判断从v1从v2是否还有其他路径
    visited = [-1 for i in range(length) for j in range(length)] # 初始化访问过节点为-1
    ind = pos = 0
    visited[ind] = v1 # 收录进起点
    while visited[ind] !=-1: # 当起点为有效节点时，循环
        for v in range(length):
            if graph[visited[ind]][v] == 1 and v == v2: # 从图中的起点开始，如果存在其他边能连接到终点，则不是桥
                return False
            if graph[visited[ind]][v] == 1 and v not in visited: # 起点到v有边，并且v还没被访问过
                pos += 1
                visited[pos] = v # 收录进来
        ind +=1 # ind作为新起点
    return True # 如果没有找到，那么一定为桥
```
### Fleury算法
Fleury算法：
  (1)任取v0∈V(G)，令P0=v0
  (2)设pi = v0e1v1e2...eivi已经走遍，按下面方法从E(G)-{e1,e2,e3,...,ei}中选取e(i+1)
  (3)当(2)不能在进行时，算法停止
e(i+1)满足:
1、e(i+1)与vi相关联；
2、除非没有别的边可以走，否则e(i+1)不应该为Gi = G-{e1,e2,e3,...,ei}中的桥

```python
def Fleury(graph):
    path = list() # 欧拉路径
    global length
    length = len(graph)
    cur = 0 # 设置起点为0
    path.append(cur)
    bridge = True # 判断是否为桥的标记
    visited = [0 for i in range(length) for j in range(length)] # 收录访问节点
    edges = len( [ graph[i][j] for i in range(length) for j in range(length) if graph[i][j] ==1] ) // 2 # 计算边数
    for e in range(edges):
        for adj in range(length): # 枚举图中的所有节点找到邻接点
            if graph[visited[cur]][adj] == 1:
                graph[visited[cur]][adj] = graph[adj][visited[cur]] = 0 # 删去边
                if isBridge(visited[cur],adj): # 判断当前节点和邻接点之间是否为桥
                    bn = adj # 保存当前邻接点作为桥的一个端点
                    graph[visited[cur]][adj] = graph[adj][visited[cur]] = 1 # 补上这条边，实际上是等把所有的邻接点遍历完之后，只要能找到还有不构成桥的边，就不过桥
                else: # 找到不构成桥的邻接点，添加到访问过的结点中
                    cur += 1
                    visited[cur] = adj # 收录当前结点
                    bridge = False
                    path.append(adj)
                    break
        if bridge: # 最后如果只能过桥，就把桥删去
            graph[visited[cur]][bn] = graph[bn][visited[cur]] = 0
            cur += 1
            visited[cur] = bn
            path.append(bn)
        bridge = True
```


```python
if __name__ == '__main__':
    graph = [
          [0, 1, 0, 0, 0, 0, 1]
        , [1, 0, 1, 1, 0, 0, 1]
        , [0, 1, 0, 1, 0, 0, 0]
        , [0, 1, 1, 0, 1, 0, 1]
        , [0, 0, 0, 1, 0, 1, 0]
        , [0, 0, 0, 0, 1, 0, 1]
        , [1, 1, 0, 1, 0, 1, 0]]
    if isEulerGraph(graph):
        Fleury(graph)
```
   Path:
    [0, 1, 2, 3, 1, 6, 3, 4, 5, 6, 0]
    
