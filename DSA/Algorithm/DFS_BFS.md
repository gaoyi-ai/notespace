---
title: DFS & BFS
date: 2020-12-17 16:26:54
categories:
- DSA
- Algorithm
- Graph
tags:
- BFS
- DFS
updated: 2020/12/17 17:00:14
---

# DFS

## stack

```java
    @Override
    public void dfs(V begin, VertexVisitor<V> visitor) {
        if (visitor == null) return;
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return;

        Set<Vertex<V, E>> visitedVertices = new HashSet<>();
        Stack<Vertex<V, E>> stack = new Stack<>();

        // 先访问起点
        stack.push(beginVertex);
        visitedVertices.add(beginVertex);
        if (visitor.visit(begin)) return;

        while (!stack.isEmpty()) {
            Vertex<V, E> vertex = stack.pop();

            for (Edge<V, E> edge : vertex.outEdges) {
                if (visitedVertices.contains(edge.to)) continue;

                // from to 入栈，因为需要回溯，需要记录之前的节点
                stack.push(edge.from);
                stack.push(edge.to);
                visitedVertices.add(edge.to);
                if (visitor.visit(edge.to.value)) return;
                // dfs: 选择访问一个方向，不在访问其他的
                break;
            }
        }
    }
```



## recursion

### Python

```python
DFS_SEARCHED = set()
def dfs(graph,start):
	if start not in DFS_SEARCHED:
		print(start)
		DFS_SEARCHED.add(start)
	for node in graph[start]: # neighbour
		if node not in DFS_SEARCHED:
			dfs(graph,node)       
```

### Java

```java
	public void dfs(V begin) {
		Vertex<V, E> beginVertex = vertices.get(begin);
		if (beginVertex == null) return;
		dfs2(beginVertex, new HashSet<>());
	}
	
	private void dfs(Vertex<V, E> vertex, Set<Vertex<V, E>> visitedVertices) {
		System.out.println(vertex.value);
		visitedVertices.add(vertex);

		for (Edge<V, E> edge : vertex.outEdges) {
			if (visitedVertices.contains(edge.to)) continue;
			dfs2(edge.to, visitedVertices);
		}
	}
```



# BFS

## Java

```java
    @Override
    public void bfs(V begin, VertexVisitor<V> visitor) {
        if (visitor == null) return;
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return;

        Set<Vertex<V, E>> visitedVertices = new HashSet<>();
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        queue.offer(beginVertex);
        // 每次入队代表访问
        // 如果在出队时才加入visitedVertices, 同一层连通的节点会重复
        visitedVertices.add(beginVertex);

        while (!queue.isEmpty()) {
            Vertex<V, E> vertex = queue.poll();
            if (visitor.visit(vertex.value)) return;

            for (Edge<V, E> edge : vertex.outEdges) {
                if (visitedVertices.contains(edge.to)) continue;
                queue.offer(edge.to);
                visitedVertices.add(edge.to);
            }
        }
    }
```

## Python

```python
from collections import deque

def bfs(graph,start):
	search_queue = deque()
	search_queue.append(start)
	searched = set()
	while search_queue:
		curnode =search_queue.popleft()
		if curnode not in searchded:
			searched.add(curnode)
			print(curnode)
			for node in graph[curnode]:
				search_queue.append(node)
```

