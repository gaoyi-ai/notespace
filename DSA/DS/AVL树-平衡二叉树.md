---
title: AVL Tree
categories:
- DSA
- DS
tags:
- AVL
date: 2019/8/1 20:00:14
updated: 2020/12/10 12:00:14
---



@[toc]

# AVL树
◼ 平衡因子（Balance Factor）：某结点的左右子树的高度差
◼ AVL树的特点
每个节点的平衡因子只可能是 1、0、-1（绝对值 ≤ 1，如果超过 1，称之为“失衡”）
每个节点的左右子树高度差不超过 1
搜索、添加、删除的时间复杂度是 O(logn)

# 平衡对比
![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200710081102482.png)
# 添加导致的失衡
![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200710081127742.png)
# LL 右旋转
- LL ：使g失衡的是g.left.left

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200710081303736.png)
# RR 左旋转
![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200710081336122.png)
# LR - RR左旋转 LL右旋转
![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200710081425297.png)
# RL - LL右旋转 RR左旋转
![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200710081454618.png)

# 删除导致的失衡
- 删除导致的失衡**只会导致一个节点的失衡**
如果这个节点是失衡的,删掉的是他比较短的一边路径
意味着长的那一边路径是没有发生变化的,意味着高度不变
意味着父结点的平衡因子不会变，继续向上也不会变

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200710082139110.png)
- 对于失衡的父结点，对其进行恢复平衡，
整棵树有没有平衡，还要继续向上，观察有没有节点失衡
有没有失衡取决于本身这颗子树高度有没有发生变化

- 高度是否变化这取决于绿色这个节点是否最存在

- 右旋转之后整体这个子树的高度减少
有可能会导致整体子树高度-1
意味着在向上可能有父节点，
它的左子树的高度就是比右子树高度大1
现在右子树旋转之后，右子树高度-1
意味着这时左右子树高度相差2
那么父节点就会失衡
那么就又会判断父结点为LL、RR等情况进行旋转
继续向上···

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200710082827100.png)
# 总结
■添加
口可能会导致所有祖先节点都失衡
口只要让高度最低的失衡节点恢复平衡,整棵树就恢复平衡【仅需O(1)次调整】
■删除
口可能会导致父节点或祖先节点失衡(只有1个节点会失
口恢复平衡后,可能会导致更高层的祖先节点失衡【最多需要O(logn)次调整】
■平均时间复杂度
口搜索:O(logn)
口添加:O(logn),仅需O(1)次的旋转操作
口删除:O(logn),最多需要O(logn)次的旋转操作

# 统一所有旋转操作
a<b<c<d<e<f<g
不管哪种旋转,最终结果一致
所以只要确定abcdefg
a,g可以不处理,因为位置没有变化

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200710083312205.png)
```java
	private void rotate(
			Node<E> r, // 子树的根节点
			Node<E> b, Node<E> c,
			Node<E> d,
			Node<E> e, Node<E> f) {
		// 让d成为这棵子树的根节点
		d.parent = r.parent;
		if (r.isLeftChild()) {
			r.parent.left = d;
		} else if (r.isRightChild()) {
			r.parent.right = d;
		} else {
			root = d;
		}
		
		//b-c
		b.right = c;
		if (c != null) {
			c.parent = b;
		}
		updateHeight(b);
		
		// e-f
		f.left = e;
		if (e != null) {
			e.parent = f;
		}
		updateHeight(f);
		
		// b-d-f
		d.left = b;
		d.right = f;
		b.parent = d;
		f.parent = d;
		updateHeight(d);
	}



		/**
	 * 恢复平衡
	 * @param grand 高度最低的那个不平衡节点
	 */
	private void rebalance(Node<E> grand) {
		// p为g左右子树高的
		Node<E> parent = ((AVLNode<E>)grand).tallerChild();
		// n为p左右子树高的
		Node<E> node = ((AVLNode<E>)parent).tallerChild();
		if (parent.isLeftChild()) { // L
			if (node.isLeftChild()) { // LL
				rotate(grand, node, node.right, parent, parent.right, grand);
			} else { // LR
				rotate(grand, parent, node.left, node, node.right, grand);
			}
		} else { // R
			if (node.isLeftChild()) { // RL
				rotate(grand, grand, node.left, node, node.right, parent);
			} else { // RR
				rotate(grand, grand, parent.left, parent, node.left, node);
			}
		}
	}
```


# code
这里继承的[BST](https://blog.csdn.net/qq_43658387/article/details/102834119)

```java
public class AVLTree<E> extends BST<E> {
	public AVLTree() {
		this(null);
	}
	
	public AVLTree(Comparator<E> comparator) {
		super(comparator);
	}
	
	@Override
	protected void afterAdd(Node<E> node) {
		// 向上找第一个失衡的节点 (最低的失衡节点)
		while ((node = node.parentStringt) != null) {
			if (isBalanced(node)) {
				// 更新高度
				updateHeight(node);
			} else {
				// 恢复平衡
				rebalance(node);
				// 整棵树恢复平衡
				break;
			}
		}
	}
	
	@Override
	protected void afterRemove(Node<E> node) {
		while ((node = node.parent) != null) {
			if (isBalanced(node)) {
				// 更新高度
				updateHeight(node);
			} else {
				// 恢复平衡
				rebalance(node);
			}
		}
	}
	
	@Override
	protected Node<E> createNode(E element, Node<E> parent) {
		return new AVLNode<>(element, parent);
	}
	
	/**
	 * 恢复平衡
	 * @param grand 高度最低的那个不平衡节点
	 */
	private void rebalance(Node<E> grand) {
		Node<E> parent = ((AVLNode<E>)grand).tallerChild();
		Node<E> node = ((AVLNode<E>)parent).tallerChild();
		if (parent.isLeftChild()) { // L
			if (node.isLeftChild()) { // LL
				rotateRight(grand);
			} else { // LR
				rotateLeft(parent);
				rotateRight(grand);
			}
		} else { // R
			if (node.isLeftChild()) { // RL
				rotateRight(parent);
				rotateLeft(grand);
			} else { // RR
				rotateLeft(grand);
			}
		}
	}

	private void rotateLeft(Node<E> grand) {
		Node<E> parent = grand.right;
		Node<E> child = parent.left;
		grand.right = child;
		parent.left = grand;
		afterRotate(grand, parent, child);
	}
	
	private void rotateRight(Node<E> grand) {
		Node<E> parent = grand.left;
		Node<E> child = parent.right;
		grand.left = child;
		parent.right = grand;
		afterRotate(grand, parent, child);
	}
	
	/*
	 * 更新parent和高度
	 */
	private void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
		// 让parent称为子树的根节点
		parent.parent = grand.parent;
		if (grand.isLeftChild()) {
			grand.parent.left = parent;
		} else if (grand.isRightChild()) {
			grand.parent.right = parent;
		} else { // grand是root节点
			root = parent;
		}
		
		// 更新child的parent
		if (child != null) {
			child.parent = grand;
		}
		
		// 更新grand的parent
		grand.parent = parent;
		
		// 更新高度
		updateHeight(grand);
		updateHeight(parent);
	}
	
	private boolean isBalanced(Node<E> node) {
		return Math.abs(((AVLNode<E>)node).balanceFactor()) <= 1;
	}
	
	private void updateHeight(Node<E> node) {
		((AVLNode<E>)node).updateHeight();
	}
	
	private static class AVLNode<E> extends Node<E> {
		int height = 1;
		
		public AVLNode(E element, Node<E> parent) {
			super(element, parent);
		}
		
		public int balanceFactor() {
			int leftHeight = left == null ? 0 : ((AVLNode<E>)left).height;
			int rightHeight = right == null ? 0 : ((AVLNode<E>)right).height;
			return leftHeight - rightHeight;
		}
		
		public void updateHeight() {
			int leftHeight = left == null ? 0 : ((AVLNode<E>)left).height;
			int rightHeight = right == null ? 0 : ((AVLNode<E>)right).height;
			height = 1 + Math.max(leftHeight, rightHeight);
		}
		
		public Node<E> tallerChild() {
			int leftHeight = left == null ? 0 : ((AVLNode<E>)left).height;
			int rightHeight = right == null ? 0 : ((AVLNode<E>)right).height;
			if (leftHeight > rightHeight) return left;
			if (leftHeight < rightHeight) return right;
			return isLeftChild() ? left : right;
		}
		
		@Override
		public String toString() {
			String parentString = "null";
			if (parent != null) {
				parentString = parent.element.toString();
			}
			return element + "_p(" + parentString + ")_h(" + height + ")";
		}
	}
}
```

> Reference：[小码哥MJ](https://space.bilibili.com/325538782/)

