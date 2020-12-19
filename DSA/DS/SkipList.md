---
title: SkipList
date: 2020-12-19 21:33:27
categories:
- DSA
- DS
updated: 2020/12/19 22:00:14
---

# SkipList

◼ 一个有序链表搜索、添加、删除的平均时间复杂度是多少？
 O(n)

◼ 能否利用二分搜索优化有序链表，将搜索、添加、删除的平均时间复杂度降低至 O(logn) ？
链表没有像数组那样的高效**随机访问**（ O(1) 时间复杂度），所以不能像有序数组那样直接进行二分搜索优化

◼ 跳表，在有序链表的基础上增加了“跳跃”的功能
由William Pugh于1990年发布，设计的初衷是为了取代平衡树（比如红黑树）

◼ Redis中 的 SortedSet、LevelDB 中的 MemTable 都用到了跳表
Redis、LevelDB 都是著名的 Key-Value 数据库

◼ 对比平衡树
跳表的实现和维护会更加简单
跳表的搜索、删除、添加的平均时间复杂度是 O(logn)

![image-20201219221417363](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201219221417363.png)

## 搜索

1. 从顶层链表的首元素开始，从左往右搜索，直至找到一个大于或等于目标的元素，或者到达当前层链表的尾部
2. 如果该元素等于目标元素，则表明该元素已被找到 
3. 如果该元素大于目标元素或已到达链表的尾部，则退回到当前层的前一个元素，然后转入下一层进行搜索

## 添加、删除

![image-20201219221615017](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201219221615017.png)

### 添加

◼ 添加的细节
随机决定新添加元素的层数

```java
	private int randomLevel() {
		int level = 1;
		while (Math.random() < P && level < MAX_LEVEL) {
			level++;
		}
		return level;
	}
```

◼ 前驱都是会向下走的节点
会向下走是因为**向右走发现>=插入key，所以才会退回去 向下走**，同时也证明 **前驱和后继中间没有其他节点**

◼ 向下走发现为null 即找到插入位置

```java
	public V put(K key, V value) {
		keyCheck(key);
		
		Node<K, V> node = first;
		Node<K, V>[] prevs = new Node[level];
		for (int i = level - 1; i >= 0; i--) {
			int cmp = -1;
			while (node.nexts[i] != null 
					&& (cmp = compare(key, node.nexts[i].key)) > 0) {
				node = node.nexts[i];
			}
			if (cmp == 0) { // 节点是存在的
				V oldV = node.nexts[i].value;
				node.nexts[i].value = value;
				return oldV;
			}
			prevs[i] = node;
		}
		
		// 新节点的层数
		int newLevel = randomLevel();
		// 添加新节点
		Node<K, V> newNode = new Node<>(key, value, newLevel);
		// 设置前驱和后继
		for (int i = 0; i < newLevel; i++) {
			if (i >= level) {
				// 新插入节点level>当前level，前驱应为first
				first.nexts[i] = newNode;
			} else {
				// 后继
				newNode.nexts[i] = prevs[i].nexts[i];
				// 前驱
				prevs[i].nexts[i] = newNode;
			}
		}
		
		// 节点数量增加
		size++;
		
		// 计算跳表的最终层数
		level = Math.max(level, newLevel);
		
		return null;
	}
```



### 删除

◼ 删除的细节
删除一个元素后，整个跳表的层数可能会降低

```java
	public V remove(K key) {
		keyCheck(key);
		
		Node<K, V> node = first;
		Node<K, V>[] prevs = new Node[level];
		boolean exist = false;
		for (int i = level - 1; i >= 0; i--) {
			int cmp = -1;
			while (node.nexts[i] != null 
					&& (cmp = compare(key, node.nexts[i].key)) > 0) {
				node = node.nexts[i];
			}
			prevs[i] = node;
			if (cmp == 0) exist = true;
		}
		if (!exist) return null;
		
		// 需要被删除的节点
		Node<K, V> removedNode = node.nexts[0];
		
		// 数量减少
		size--;
		
		// 设置后继
		for (int i = 0; i < removedNode.nexts.length; i++) {
			prevs[i].nexts[i] = removedNode.nexts[i];
		}
		
		// 更新跳表的层数
		int newLevel = level;
		// 检查当前层有没有节点
		while (--newLevel >= 0 && first.nexts[newLevel] == null) {
			level = newLevel;
		}
		
		return removedNode.value;
	}
```

## 跳表的层数


◼ 跳表是按层构造的，底层是一个普通的有序链表，高层相当于是低层的“快速通道”

在第 i 层中的元素按某个固定的概率 p（通常为 ½ 或 ¼ ）出现在第 i + 1层中，产生越高的层数，概率越低
✓ 元素层数恰好等于 1 的概率为 1 – p
✓ 元素层数大于等于 2 的概率为 p，而元素层数恰好等于 2 的概率为 p * (1 – p)
✓ 元素层数大于等于 3 的概率为 p^2，而元素层数恰好等于 3 的概率为 p^2 * (1 – p)
✓ 元素层数大于等于 4 的概率为 p^3，而元素层数恰好等于 4 的概率为 p^3 * (1 – p)
✓ ......
✓ 一个元素的平均层数是 1 / (1 – p)

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201219222246431.png" alt="image-20201219222246431" style="zoom:67%;" />

◼ 当 p = ½ 时，每个元素所包含的平均指针数量是 2
◼ 当 p = ¼ 时，每个元素所包含的平均指针数量是 1.33

## 跳表的复杂度分析

◼ 每一层的元素数量
第 1 层链表固定有 n 个元素
第 2 层链表平均有 n * p 个元素
第 3 层链表平均有 n * p^2 个元素
第 k 层链表平均有 n * p^k 个元素
…
◼ 另外
最高层的层数是 log1/p n ，平均有个 1/p 元素
在搜索时，每一层链表的预期查找步数最多是 1/p，所以总的查找步数是 –( logp n /p )，时间复杂度是 O(logn)

## CODE

```java
package com.mj;

import java.util.Comparator;

public class SkipList<K, V> {
	private static final int MAX_LEVEL = 32;
	private static final double P = 0.25;
	private int size;
	private Comparator<K> comparator;
	/**
	 * 有效层数
	 */
	private int level;
	/**
	 * 不存放任何K-V
	 */
	private Node<K, V> first;
	
	public SkipList(Comparator<K> comparator) {
		this.comparator = comparator;
		first = new Node<>(null, null, MAX_LEVEL);
	}
	
	public SkipList() {
		this(null);
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public V get(K key) {
		keyCheck(key);	
		Node<K, V> node = first;
		for (int i = level - 1; i >= 0; i--) {
			int cmp = -1;
			// 如果右边的元素小于node，就一直向右找，直至找到一个大于或等于目标的元素
			while (node.nexts[i] != null 
					&& (cmp = compare(key, node.nexts[i].key)) > 0) {
				node = node.nexts[i];
			}
			// node.nexts[i].key >= key
			if (cmp == 0) return node.nexts[i].value;
		}
		return null;
	}
	
	public V put(K key, V value) {
		keyCheck(key);
		
		Node<K, V> node = first;
		Node<K, V>[] prevs = new Node[level];
		for (int i = level - 1; i >= 0; i--) {
			int cmp = -1;
			while (node.nexts[i] != null 
					&& (cmp = compare(key, node.nexts[i].key)) > 0) {
				node = node.nexts[i];
			}
			if (cmp == 0) { // 节点是存在的
				V oldV = node.nexts[i].value;
				node.nexts[i].value = value;
				return oldV;
			}
			prevs[i] = node;
		}
		
		// 新节点的层数
		int newLevel = randomLevel();
		// 添加新节点
		Node<K, V> newNode = new Node<>(key, value, newLevel);
		// 设置前驱和后继
		for (int i = 0; i < newLevel; i++) {
			if (i >= level) {
				// 新插入节点level>当前level，前驱应为first
				first.nexts[i] = newNode;
			} else {
				// 后继
				newNode.nexts[i] = prevs[i].nexts[i];
				// 前驱
				prevs[i].nexts[i] = newNode;
			}
		}
		
		// 节点数量增加
		size++;
		
		// 计算跳表的最终层数
		level = Math.max(level, newLevel);
		
		return null;
	}
	
	public V remove(K key) {
		keyCheck(key);
		
		Node<K, V> node = first;
		Node<K, V>[] prevs = new Node[level];
		boolean exist = false;
		for (int i = level - 1; i >= 0; i--) {
			int cmp = -1;
			while (node.nexts[i] != null 
					&& (cmp = compare(key, node.nexts[i].key)) > 0) {
				node = node.nexts[i];
			}
			prevs[i] = node;
			if (cmp == 0) exist = true;
		}
		if (!exist) return null;
		
		// 需要被删除的节点
		Node<K, V> removedNode = node.nexts[0];
		
		// 数量减少
		size--;
		
		// 设置后继
		for (int i = 0; i < removedNode.nexts.length; i++) {
			prevs[i].nexts[i] = removedNode.nexts[i];
		}
		
		// 更新跳表的层数
		int newLevel = level;
		// 检查当前层有没有节点
		while (--newLevel >= 0 && first.nexts[newLevel] == null) {
			level = newLevel;
		}
		
		return removedNode.value;
	}
	
	private int randomLevel() {
		int level = 1;
		while (Math.random() < P && level < MAX_LEVEL) {
			level++;
		}
		return level;
	}
	
	private void keyCheck(K key) {
		if (key == null) {
			throw new IllegalArgumentException("key must not be null.");
		}
	}
	
	private int compare(K k1, K k2) {
		return comparator != null 
				? comparator.compare(k1, k2)
				: ((Comparable<K>)k1).compareTo(k2);
	}
	
	private static class Node<K, V> {
		K key;
		V value;
		Node<K, V>[] nexts;
		public Node(K key, V value, int level) {
			this.key = key;
			this.value = value;
			nexts = new Node[level];
		}
		@Override
		public String toString() {
			return key + ":" + value + "_" + nexts.length;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("一共" + level + "层").append("\n");
		for (int i = level - 1; i >= 0; i--) {
			Node<K, V> node = first;
			while (node.nexts[i] != null) {
				sb.append(node.nexts[i]);
				sb.append(" ");
				node = node.nexts[i];
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
```

