---
title: Graph
date: 2020-12-17 16:33:27
categories:
- DSA
- DS
- Graph
tags:
- OOP
- graph
updated: 2020/12/17 17:00:14
---

# Graph

## Interface

```java
package graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Graph<V, E> {
	protected WeightManager<E> weightManager;
	
	public Graph() {}
	
	public Graph(WeightManager<E> weightManager) {
		this.weightManager = weightManager;
	}
	
	public abstract int edgesSize();
	public abstract int verticesSize();
	
	public abstract void addVertex(V v);
	public abstract void addEdge(V from, V to);
	public abstract void addEdge(V from, V to, E weight);
	
	public abstract void removeVertex(V v);
	public abstract void removeEdge(V from, V to);
	
	public abstract void bfs(V begin, VertexVisitor<V> visitor);
	public abstract void dfs(V begin, VertexVisitor<V> visitor);
	
	public abstract Set<EdgeInfo<V, E>> mst();
	
	public abstract List<V> topologicalSort();
	
	public abstract Map<V, PathInfo<V, E>> shortestPath(V begin);
	
	public abstract Map<V, Map<V, PathInfo<V, E>>> shortestPath();
	
	public interface WeightManager<E> {
		int compare(E w1, E w2);
		E add(E w1, E w2);
		E zero();
	}
	
	public interface VertexVisitor<V> {
		boolean visit(V v);
	}
	
	public static class PathInfo<V, E> {
		protected E weight;
		protected List<EdgeInfo<V, E>> edgeInfos = new LinkedList<>();
		public PathInfo() {}
		public PathInfo(E weight) {
			this.weight = weight;
		}
		public E getWeight() {
			return weight;
		}
		public void setWeight(E weight) {
			this.weight = weight;
		}
		public List<EdgeInfo<V, E>> getEdgeInfos() {
			return edgeInfos;
		}
		public void setEdgeInfos(List<EdgeInfo<V, E>> edgeInfos) {
			this.edgeInfos = edgeInfos;
		}
		@Override
		public String toString() {
			return "PathInfo [weight=" + weight + ", edgeInfos=" + edgeInfos + "]";
		}
	}
	
	public static class EdgeInfo<V, E> {
		private V from;
		private V to;
		private E weight;
		public EdgeInfo(V from, V to, E weight) {
			this.from = from;
			this.to = to;
			this.weight = weight;
		}
		public V getFrom() {
			return from;
		}
		public void setFrom(V from) {
			this.from = from;
		}
		public V getTo() {
			return to;
		}
		public void setTo(V to) {
			this.to = to;
		}
		public E getWeight() {
			return weight;
		}
		public void setWeight(E weight) {
			this.weight = weight;
		}
		@Override
		public String toString() {
			return "EdgeInfo [from=" + from + ", to=" + to + ", weight=" + weight + "]";
		}
	}
}
```

## ListGraph

```java
package graph;

import ds.MinHeap;
import ds.UnionFind;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;



public class ListGraph<V, E> extends Graph<V, E> {
    public ListGraph() {
    }

    public ListGraph(WeightManager<E> weightManager) {
        super(weightManager);
    }

    private static class Vertex<V, E> {
        V value;
        Set<Edge<V, E>> inEdges = new HashSet<>();
        Set<Edge<V, E>> outEdges = new HashSet<>();

        Vertex(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) { // 顶点相同=顶点上的数据相同
            return Objects.equals(value, ((Vertex<V, E>) obj).value);
        }

        @Override
        public int hashCode() { // 允许顶点上的数据为null
            return value == null ? 0 : value.hashCode();
        }

        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

    private static class Edge<V, E> {
        Vertex<V, E> from;
        Vertex<V, E> to;
        E weight;

        Edge(Vertex<V, E> from, Vertex<V, E> to) {
            this.from = from;
            this.to = to;
        }

        EdgeInfo<V, E> info() {
            return new EdgeInfo<>(from.value, to.value, weight);
        }

        @Override
        public boolean equals(Object obj) {
            Edge<V, E> edge = (Edge<V, E>) obj; // 边相同=两个顶点相同
            return Objects.equals(from, edge.from) && Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            return from.hashCode() * 31 + to.hashCode();
        }

        @Override
        public String toString() {
            return "Edge [from=" + from + ", to=" + to + ", weight=" + weight + "]";
        }
    }

    private Map<V, Vertex<V, E>> vertices = new HashMap<>();
    private Set<Edge<V, E>> edges = new HashSet<>();
    private Comparator<Edge<V, E>> edgeComparator = (Edge<V, E> e1, Edge<V, E> e2) -> {
        return weightManager.compare(e1.weight, e2.weight);
    };

    public void print() {
        System.out.println("[顶点]-------------------");
        vertices.forEach((V v, Vertex<V, E> vertex) -> {
            System.out.println(v);
            System.out.println("out-----------");
            System.out.println(vertex.outEdges);
            System.out.println("in-----------");
            System.out.println(vertex.inEdges);
        });

        System.out.println("[边]-------------------");
        edges.forEach((Edge<V, E> edge) -> {
            System.out.println(edge);
        });
    }

    @Override
    public int edgesSize() {
        return edges.size();
    }

    @Override
    public int verticesSize() {
        return vertices.size();
    }

    @Override
    public void addVertex(V v) {
        if (vertices.containsKey(v)) return;
        vertices.put(v, new Vertex<>(v));
    }

    @Override
    public void addEdge(V from, V to) {
        addEdge(from, to, null);
    }

    @Override
    public void addEdge(V from, V to, E weight) {
        Vertex<V, E> fromVertex = vertices.get(from);
        if (fromVertex == null) {
            fromVertex = new Vertex<>(from);
            vertices.put(from, fromVertex);
        }

        Vertex<V, E> toVertex = vertices.get(to);
        if (toVertex == null) {
            toVertex = new Vertex<>(to);
            vertices.put(to, toVertex);
        }

        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
        edge.weight = weight;
        // 综合 更新和添加
        // hashSet remove add 比较的是 equals
        // 即在更新时remove的是旧值，add的是新值
        if (fromVertex.outEdges.remove(edge)) {
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }
        fromVertex.outEdges.add(edge);
        toVertex.inEdges.add(edge);
        edges.add(edge);
    }

    @Override
    public void removeEdge(V from, V to) {
        Vertex<V, E> fromVertex = vertices.get(from);
        if (fromVertex == null) return;

        Vertex<V, E> toVertex = vertices.get(to);
        if (toVertex == null) return;

        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
        if (fromVertex.outEdges.remove(edge)) {
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }
    }

    @Override
    public void removeVertex(V v) {
        Vertex<V, E> vertex = vertices.remove(v);
        if (vertex == null) return;

        for (Iterator<Edge<V, E>> iterator = vertex.outEdges.iterator(); iterator.hasNext(); ) {
            Edge<V, E> edge = iterator.next();
            edge.to.inEdges.remove(edge);
            // 将当前遍历到的元素edge从集合vertex.outEdges中删掉
            iterator.remove();
            edges.remove(edge);
        }

        for (Iterator<Edge<V, E>> iterator = vertex.inEdges.iterator(); iterator.hasNext(); ) {
            Edge<V, E> edge = iterator.next();
            edge.from.outEdges.remove(edge);
            // 将当前遍历到的元素edge从集合vertex.inEdges中删掉
            iterator.remove();
            edges.remove(edge);
        }
    }
```

## WeightManager

```java
    static WeightManager<Double> weightManager = new WeightManager<Double>() {
        public int compare(Double w1, Double w2) {
            return w1.compareTo(w2);
        }

        public Double add(Double w1, Double w2) {
            return w1 + w2;
        }

        @Override
        public Double zero() {
            return 0.0;
        }
    };
```

