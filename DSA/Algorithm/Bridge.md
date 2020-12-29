---
title: Bridge
categories:
- DSA
- Algorithm
- Graph
tags:
- dfs
- bridge
date: 2019/12/29 20:00:14
updated: 2020/12/30 12:00:14
---



# Bridge

对于无向图，如果删除了一条边，整个图联通分量数量变化，则这条边称为桥  (Bridge)

![image-20201229175832942](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201229175832942.png)

似乎桥和环有关系？

和判断一张图是否有环不同
判断一张图是否有环，是整张图的属性

桥，是一条边的属性，所以需要遍历每一条边

![image-20201229180052324](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201229180052324.png)

如何判断  0-1  是不不是桥？
看通过 1 ，能否从另外一条路路回到 0

如何判断  1-3  是不不是桥？
看通过 3 ，能否从另外一条路路回到 1
看通过 3 ，能否从另外一条路路回到 0

![image-20201229180119245](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201229180119245.png)

如何判断  v-w  是不不是桥？
看通过  w ，能否从另外一条路路回到 v，或者  v  之前的顶点
对于每一个顶点，记录  DFS  的顺序
`ord[v]`  表示顶点  v  在  DFS  的访问顺序

![image-20201229180303726](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201229180303726.png)

对于每一个顶点，记录能到达的最小 ord
low[v]  表示  DFS  过程中，顶点  v  能到达的最小  ord  值

如何判断  v-w  是不是桥？
看通过  w ，能否从另外一条路回到 v，或者  v  之前的顶点

## 算法思路

对于每一条边v-w，通过w能够到达和v的order值相同的，即回到v
或者到达比v的order值还小的点，也就是v的祖先节点。即v-w不是桥。

![bridge](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/bridge.gif)



## DFS 遍历树

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201229180812142.png" alt="image-20201229180812142" style="zoom:67%;" />

![image-20201229180942076](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201229180942076.png)

## BFS 遍历树

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201229180812142.png" alt="image-20201229180812142" style="zoom:67%;" />

![image-20201229180942076](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201229180942076.png)

## Solution

```java
public class FindBridges {

    private Graph G;
    private boolean[] visited;

    private int ord[];
    private int low[];
    private int cnt;

    private ArrayList<Edge> res;

    public FindBridges(Graph G){

        this.G = G;
        visited = new boolean[G.V()];

        res = new ArrayList<>();
        ord = new int[G.V()];
        low = new int[G.V()];
        cnt = 0;

        for(int v = 0; v < G.V(); v ++)
            if(!visited[v])
                dfs(v, v);
    }

    private void dfs(int v, int parent){

        visited[v] = true;
        ord[v] = cnt;
        low[v] = ord[v];
        cnt ++;

        for(int w: G.adj(v))
            if(!visited[w]){
                dfs(w, v);
                low[v] = Math.min(low[v], low[w]);
                if(low[w] > ord[v])
                    res.add(new Edge(v, w));
            }
            else if(w != parent)
                low[v] = Math.min(low[v], low[w]);
    }
}
```

