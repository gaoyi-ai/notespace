---
title: Cut
categories:
- DSA
- Algorithm
- Graph
tags:
- dfs
- cut
date: 2019/12/29 20:00:14
updated: 2020/12/30 12:00:14
---



# Cut

对于无向图，如果删除了了一个顶点， ( 顶点邻边也删除 )，整个图联通分量数量变化，则这个顶点称为割点  (Cut Points)，桥也叫割边  (Cut Edges)

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201229204459918.png" alt="image-20201229204459918" style="zoom:67%;" />

## 算法

对于边  v-w，满足  low[w] > ord[v]，则  v-w  是桥

如果点  v  有一个孩子节点  w，满足  low[w] >= ord[v]，则  v  是割点，特殊情况：根节点

对于根节点，如果有一个以上的孩子（DFS 遍历树节点对应的子节点），则根节点是割点

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201229204855431.png" alt="image-20201229204855431" style="zoom:80%;" />

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201229204917267.png" alt="image-20201229204917267" style="zoom:80%;" />

## Solution

```java
public class FindCutPoints {

    private Graph G;
    private boolean[] visited;

    private int[] ord;
    private int[] low;
    private int cnt;

    private HashSet<Integer> res;

    public FindCutPoints(Graph G){

        this.G = G;
        visited = new boolean[G.V()];

        res = new HashSet<>();
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

        int child = 0;

        for(int w: G.adj(v))
            if(!visited[w]){
                dfs(w, v);
                low[v] = Math.min(low[v], low[w]);

                if(v != parent && low[w] >= ord[v])
                    res.add(v);

                child ++;
                if(v == parent && child > 1)
                    res.add(v);
            }
            else if(w != parent)
                low[v] = Math.min(low[v], low[w]);
    }
}
```

