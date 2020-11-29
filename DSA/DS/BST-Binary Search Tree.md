@[toc]

# Binary Search Tree (BST)

- 任意一个节点的值都大于其左子树所有节点的值
- 任意一个节点的值都小于其右子树所有节点的值
- 它的左右子树也是一棵二叉搜索树
- 二叉搜索树可以大大提高搜索数据的效率
- 二叉搜索树存储的元素必须具备**可比较性**

# add
◼ 添加步骤
找到父节点 parent
创建新节点 node
parent.left = node 或者 parent.right = node
◼ 遇到值相等的元素该如何处理？
建议覆盖旧的值

# remove
## 直接删除
node == node.parent.left
✓ node.parent.left = null

node == node.parent.right
✓ node.parent.right = null

node.parent == null
✓ root = null

## 删除节点 – 度为1的节点
◼ 用子节点替代原节点的位置
child 是 node.left 或者 child 是 node.right
用 child 替代 node 的位置
✓ 如果 node 是左子节点
➢ child.parent = node.parent
➢ node.parent.left = child
✓ 如果 node 是右子节点
➢ child.parent = node.parent
➢ node.parent.right = child
✓ 如果 node 是根节点
➢ root = child
➢ child.parent = null

## 删除节点 – 度为2的节点
◼ 先用前驱或者后继节点的值覆盖原节点的值
◼ 然后删除相应的前驱或者后继节点
◼ 如果一个节点的度为 2，那么
它的前驱、后继节点的度只可能是 1 和 0

# code
```java
public class BinarySearchTree<E>{
	private int size;
	private Node<E> root;
	private Comparator<E> comparator;
	
	public BinarySearchTree() {
		this(null);
	}
	
	public BinarySearchTree(Comparator<E> comparator) {
		this.comparator = comparator;
	}
	
	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void clear() {
		root = null;
		size = 0;
	}

	public void add(E element) {
		elementNotNullCheck(element);
		
		// 添加第一个节点
		if (root == null) {
			root = new Node<>(element, null);
			size++;
			return;
		}
		
		// 添加的不是第一个节点
		// 找到父节点
		Node<E> parent = root;
		Node<E> node = root;
		int cmp = 0;
		do {
			cmp = compare(element, node.element);
			parent = node;
			if (cmp > 0) {
				node = node.right;
			} else if (cmp < 0) {
				node = node.left;
			} else { // 相等
				node.element = element;
				return;
			}
		} while (node != null);

		// 看看插入到父节点的哪个位置
		Node<E> newNode = new Node<>(element, parent);
		if (cmp > 0) {
			parent.right = newNode;
		} else {
			parent.left = newNode;
		}
		size++;
	}

	public void remove(E element) {
		remove(node(element));
	}

	public boolean contains(E element) {
		return node(element) != null;
	}
	
	private void remove(Node<E> node) {
		if (node == null) return;
		
		size--;
		
		if (node.hasTwoChildren()) { // 度为2的节点
			// 找到后继节点
			Node<E> s = successor(node);
			// 用后继节点的值覆盖度为2的节点的值
			node.element = s.element;
			// 删除后继节点
			node = s;
		}
		
		// 删除node节点（node的度必然是1或者0）
		Node<E> replacement = node.left != null ? node.left : node.right;
		
		if (replacement != null) { // node是度为1的节点
			// 更改parent
			replacement.parent = node.parent;
			// 更改parent的left、right的指向
			if (node.parent == null) { // node是度为1的节点并且是根节点
				root = replacement;
			} else if (node == node.parent.left) {
				node.parent.left = replacement;
			} else { // node == node.parent.right
				node.parent.right = replacement;
			}
		} else if (node.parent == null) { // node是叶子节点并且是根节点
			root = null;
		} else { // node是叶子节点，但不是根节点
			if (node == node.parent.left) {
				node.parent.left = null;
			} else { // node == node.parent.right
				node.parent.right = null;
			}
		}
	}
	
	/*
	 * 根据元素内容获取节点
	 */
	private Node<E> node(E element) {
		Node<E> node = root;
		while (node != null) {
			int cmp = compare(element, node.element);
			if (cmp == 0) return node;
			if (cmp > 0) {
				node = node.right;
			} else { // cmp < 0
				node = node.left;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toString(root, sb, "");
		return sb.toString();
	}
	
	/*
	 * 树状打印
	 */
	private void toString(Node<E> node, StringBuilder sb, String prefix) {
		if (node == null) return;

		sb.append(prefix).append(node.element).append("\n");
		toString(node.left, sb, prefix + "L---");
		toString(node.right, sb, prefix + "R---");
	}
	
	/**
	 * @return 返回值等于0，代表e1和e2相等；返回值大于0，代表e1大于e2；返回值小于于0，代表e1小于e2
	 */
	private int compare(E e1, E e2) {
		if (comparator != null) { // 当外部有比较器时，使用外部比较器
			return comparator.compare(e1, e2);
		}
		return ((Comparable<E>)e1).compareTo(e2);
	}
	
	private void elementNotNullCheck(E element) {
		if (element == null) {
			throw new IllegalArgumentException("element must not be null");
		}
	}
	
	private Node<E> predecessor(Node<E> node) {
		if (node == null) return null;
		
		// 前驱节点在左子树当中（left.right.right.right....）
		Node<E> p = node.left;
		if (p != null) {
			while (p.right != null) {
				p = p.right;
			}
			return p;
		}
		
		// 从父节点、祖父节点中寻找前驱节点
		while (node.parent != null && node == node.parent.left) {
			node = node.parent;
		}

		// node.parent == null
		// node == node.parent.right
		return node.parent;
	}
	
	private Node<E> successor(Node<E> node) {
		if (node == null) return null;
		
		// 前驱节点在左子树当中（right.left.left.left....）
		Node<E> p = node.right;
		if (p != null) {
			while (p.left != null) {
				p = p.left;
			}
			return p;
		}
		
		// 从父节点、祖父节点中寻找前驱节点
		while (node.parent != null && node == node.parent.right) {
			node = node.parent;
		}

		return node.parent;
	}
	
	public static abstract class Visitor<E> {
		boolean stop;
		/**
		 * 制定访问每个节点的规则
		 * @return 如果返回true，就代表停止遍历
		 */
		public abstract boolean visit(E element);
	}

	private static class Node<E> {
		E element;
		Node<E> left;
		Node<E> right;
		Node<E> parent;
		public Node(E element, Node<E> parent) {
			this.element = element;
			this.parent = parent;
		}
		
		public boolean isLeaf() {
			return left == null && right == null;
		}
		
		public boolean hasTwoChildren() {
			return left != null && right != null;
		}
	}
}
```

> Reference：[小码哥MJ](https://space.bilibili.com/325538782/)

