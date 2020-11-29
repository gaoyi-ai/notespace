
@[toc]

# Red Black Tree
![RBTree](images/Red%20Black%20Tree%20-%20RBTree/2019120113201412.jpg)

1. 节点是RED或BLACK

2. 根节点必须是BLACK

3. **叶子节点（外部节点）都是BLACK**（把度为1或度为0的节点补成度为2的节点）

4. **RED节点的子节点为BLACK**

 - 推论： **RED节点的父结点为BLACK** 
 - 推论：**从根节点到叶子节点的所有路径上不能有2个连续的RED节点**

5. 从任意节点到叶子节点的所有路径都包含相同数目的BLACK节点

# 红黑树 vs 2-3-4树

![B树](images/Red%20Black%20Tree%20-%20RBTree/20191201132147225.jpg)
![2-3-4树](images/Red%20Black%20Tree%20-%20RBTree/20191201132048311.jpg)

 - **红黑树 与 4阶B树(2-3-4树) 等价**（每个节点所存储的节点个数为1-3）
 - 每一个红色节点与其**黑色**父节点合并形成一个超级节点
 - **黑色节点处于中间，红色节点在两侧**

# 添加

**B树中，新元素必定是添加到叶子节点中**
4阶B树所有节点的元素个数 x 都符合 1 ≤  x ≤ 3
◼ 建议新添加的节点默认为 RED，这样能够让红黑树的性质尽快满足（性质 4 不一定，性质 1、2、3都满足，对于性质5因为添加的时红色不会影响到路径上黑色节点的个数，所以5也满足）

◼ 如果添加的是根节点，染成 BLACK 即可 

![在这里插入图片描述](images/Red%20Black%20Tree%20-%20RBTree/20191201132417615.jpg)

1. parent为BLACK：

    - 在对红黑树进行讨论时，始终看作B树

    - 由于添加节点默认为红色，那么直接添加到黑色旁边就行

2. parent为RED：

- 当添加的节点的uncle节点为黑色（外部节点默认为黑色）

对于等价的B树节点，添加的位置只是多了一个节点

LL/RR：把parent染BLACK，grand染RED，对grand进行单旋

![LL/RR](images/Red%20Black%20Tree%20-%20RBTree/20191201133326233.jpg)

LR/RL：把自己染成BLACK，grand染成RED，再进行旋转

![LR/RL](images/Red%20Black%20Tree%20-%20RBTree/20191201133425602.jpg)

- 当添加之后会造成对应B树节点**上溢**，即uncle为红色（因为uncle如果为黑色，会单独成为一个节点）
	

此时把grand向上合并，把grand当作新添加节点（递归）

此时上溢左右两边要独立成为B树节点，17,33只有染成black作为独立的节点

注意：当上溢到根节点，只需要把根节点染成BLACK
	
![上溢](images/Red%20Black%20Tree%20-%20RBTree/20191201133545636.jpg)

# 删除

- B树中，最后真正被删除的元素都在叶子节点中

- 删除RED节点，不会影响红黑树的性质（红色节点只能与黑色父结点合并才构成超级节点）
- 删除BLACK节点
    1. 拥有 2 个 RED 子节点的 BLACK 节点
        ✓ 不可能被直接删除，因为会找它的子节点替代删除
        ✓ 因此不用考虑这种情况（找到前驱或后继覆盖，然后删除前驱或后继，真正删除的仍然是红色节点）

剩下两种情况

1. 拥有 1 个 RED 子节点的 BLACK 节点
2. BLACK 叶子节点

![remove](images/Red%20Black%20Tree%20-%20RBTree/20191201133631655.jpg)

- 删除 拥有 1 个 RED 子节点的 BLACK 节点

    用以替代的子节点是 RED

    ◼ 将替代的子节点染成 BLACK 即可保持红黑树性质

    （对于相应的B树节点，删除的位置只是少了一个节点）

- 删除 BLACK叶子节点 – sibling为BLACK

    ◼BLACK 叶子节点被删除后，会导致B树节点下溢（比如删除88）

    （对应B树当中的删除操作，88位置节点个数＜1，应向兄弟节点借）

1. 如果 sibling 至少有 1 个 RED 子节点（可以借）

    进行旋转操作（parent下来，sibling上去）
    旋转之后的中心节点**继承** parent 的颜色
    旋转之后的左右节点染为 BLACK

2. sibling 没有 1 个 RED 子节点（不能借，父节点下来合并）

    ◼ 将 sibling 染成 RED、parent 染成 BLACK 即可修复红黑树性质

    ◼ 如果 parent 是 BLACK
    会导致 parent 也下溢
    这时只需要把 parent 当做被删除的节点处理即可

    ![下溢](images/Red%20Black%20Tree%20-%20RBTree/20191201133926252.jpg)![下溢修复](images/Red%20Black%20Tree%20-%20RBTree/20191201133943768.jpg)

- 删除 BLACK叶子节点 – sibling为RED

    ◼ 如果 sibling 是 RED

    此时**对应B树的删除情况**，删除88需要向兄弟节点借，但是此时的兄弟节点不是对应B树中的兄弟节点，即想要的兄弟节点应该是76，要向76借，但是此时真正的兄弟节点为55

    所以要对当前情况进行转换，使76成为88的兄弟节点：

    sibling 染成 BLACK，parent 染成 RED，进行旋转
    于是又回到 sibling 是 BLACK 的情况

    ![删除情况三](images/Red%20Black%20Tree%20-%20RBTree/20191201134040663.jpg)

# 平衡情况

红黑树的5条性质，可以保证 红黑树 等价于 4阶B树

◼ 相比AVL树，红黑树的平衡标准比较宽松：没有一条路径会大于其他路径的2倍

红黑树中最短路径为也就是B树的高度，比它长的路径就是多出红色节点，而且最长路径就是一黑一红，即最短路径的2倍

◼ 是一种弱平衡、黑高度平衡
◼ 红黑树的最大高度是 2 ∗ log2(n + 1) ，依然是 O(logn) 级别

# 平均时间复杂度

◼ 搜索：O(logn)
◼ 添加：O(logn)，O(1) 次的旋转操作
◼ 删除：O(logn)，O(1) 次的旋转操作

◼ AVL树
平衡标准比较严格：每个左右子树的高度差不超过1
最大高度是 1.44 ∗ log2 n + 2 − 1.328 （100W个节点，AVL树最大树高28）
搜索、添加、删除都是 O(logn) 复杂度，其中添加仅需 O(1) 次旋转调整、删除最多需要 O(logn) 次旋转调整

◼ 红黑树
平衡标准比较宽松：没有一条路径会大于其他路径的2倍
最大高度是 2 ∗ log2(n + 1) （ 100W个节点，红黑树最大树高40）
搜索、添加、删除都是 O(logn) 复杂度，其中添加、删除都仅需 O(1) 次旋转调整

◼ 搜索的次数远远大于插入和删除，选择AVL树；搜索、插入、删除次数几乎差不多，选择红黑树
◼ 相对于AVL树来说，红黑树牺牲了部分平衡性以换取插入/删除操作时少量的旋转操作，整体来说性能要优于AVL树
◼ 红黑树的平均统计性能优于AVL树，实际应用中更多选择使用红黑树 

# code

```java
public class RBTree<E> extends BBST<E> {
	private static final boolean RED = false;
	private static final boolean BLACK = true;
	
	public RBTree() {
		this(null);
	}
	
	public RBTree(Comparator<E> comparator) {
		super(comparator);
	}
	
	@Override
	// node为新添加的节点
	protected void afterAdd(Node<E> node) {
		Node<E> parent = node.parent;
		
		// 添加的是根节点 或者 上溢到达了根节点
		if (parent == null) {
			black(node);
			return;
		}
		
		// 如果父节点是黑色，直接返回
		if (isBlack(parent)) return;
		
		// 叔父节点
		Node<E> uncle = parent.sibling();
		// 祖父节点
		Node<E> grand = red(parent.parent);
		if (isRed(uncle)) { // 叔父节点是红色【B树节点上溢】
			black(parent);
			black(uncle);
			// 把祖父节点当做是新添加的节点
			afterAdd(grand);
			return;
		}
		
		// 叔父节点不是红色
		if (parent.isLeftChild()) { // L
			if (node.isLeftChild()) { // LL
				black(parent);
			} else { // LR
				black(node);
				rotateLeft(parent);
			}
			rotateRight(grand);
		} else { // R
			if (node.isLeftChild()) { // RL
				black(node);
				rotateRight(parent);
			} else { // RR
				black(parent);
			}
			rotateLeft(grand);
		}
	}
	
	@Override
	protected void afterRemove(Node<E> node) {
		// 如果删除的节点是红色
		// 或者 用以取代删除节点的子节点是红色
         // 对应删除RED节点或删除拥有1个RED子节点的BLACK节点
		if (isRed(node)) {
			black(node);
			return;
		}
		
		Node<E> parent = node.parent;
		// 删除的是根节点
		if (parent == null) return;
		
		// 删除的是黑色叶子节点【下溢】
		// 判断被删除的node是左还是右
         // 递归删除black parent时下溢，由于没有进行remove()
		// parent的left或right没有去除，所以不能使用null判断
		boolean left = parent.left == null || node.isLeftChild();
		Node<E> sibling = left ? parent.right : parent.left;
		if (left) { // 被删除的节点在左边，兄弟节点在右边
			if (isRed(sibling)) { // 兄弟节点是红色
				black(sibling);
				red(parent);
				rotateLeft(parent);
				// 更换兄弟
				sibling = parent.right;
			}
			
			// 兄弟节点必然是黑色
			if (isBlack(sibling.left) && isBlack(sibling.right)) {
				// 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
				boolean parentBlack = isBlack(parent);
				black(parent);
				red(sibling);
				if (parentBlack) {
					afterRemove(parent);
				}
			} else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
				// 兄弟节点的左边是黑色，兄弟要先旋转
				if (isBlack(sibling.right)) {
					rotateRight(sibling);
					sibling = parent.right;
				}
				
				color(sibling, colorOf(parent));
				black(sibling.right);
				black(parent);
				rotateLeft(parent);
			}
		} else { // 被删除的节点在右边，兄弟节点在左边
			if (isRed(sibling)) { // 兄弟节点是红色
				black(sibling);
				red(parent);
				rotateRight(parent);
				// 更换兄弟
				sibling = parent.left;
			}
			
			// 兄弟节点必然是黑色
			if (isBlack(sibling.left) && isBlack(sibling.right)) {
				// 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
				boolean parentBlack = isBlack(parent);
				black(parent);
				red(sibling);
				if (parentBlack) {
					afterRemove(parent);
				}
			} else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
				// 兄弟节点的左边是黑色，兄弟要先旋转
				if (isBlack(sibling.left)) {
					rotateLeft(sibling);
                      // 左旋转之后需要换兄弟，之后把它转上去
					sibling = parent.left;
				}
				
				color(sibling, colorOf(parent));
				black(sibling.left);
				black(parent);
				rotateRight(parent);
			}
		}
	}

	private Node<E> color(Node<E> node, boolean color) {
		if (node == null) return node;
		((RBNode<E>)node).color = color;
		return node;
	}
	
	private Node<E> red(Node<E> node) {
		return color(node, RED);
	}
	
	private Node<E> black(Node<E> node) {
		return color(node, BLACK);
	}
	
	private boolean colorOf(Node<E> node) {
		return node == null ? BLACK : ((RBNode<E>)node).color;
	}
	
	private boolean isBlack(Node<E> node) {
		return colorOf(node) == BLACK;
	}
	
	private boolean isRed(Node<E> node) {
		return colorOf(node) == RED;
	}
	
	@Override
	protected Node<E> createNode(E element, Node<E> parent) {
		return new RBNode<>(element, parent);
	}

	private static class RBNode<E> extends Node<E> {
		boolean color = RED; // 添加节点默认为红色节点
		public RBNode(E element, Node<E> parent) {
			super(element, parent);
		}
		
		@Override
		public String toString() {
			String str = "";
			if (color == RED) {
				str = "R_";
			}
			return str + element.toString();
		}
	}
}
```

# Test
[61, 2, 58, 74, 97, 44, 68, 20, 90, 28, 18, 22, 77, 78, 51]

插入：
![insert](images/Red%20Black%20Tree%20-%20RBTree/20191201140157945.jpg)
删除97：
![remove97](images/Red%20Black%20Tree%20-%20RBTree/20191201140940396.jpg)
删除28：
![remove28](images/Red%20Black%20Tree%20-%20RBTree/20191201141002671.jpg)
删除74：
![remove74](images/Red%20Black%20Tree%20-%20RBTree/20191201141017941.jpg)

> Reference：[geeksforgeeks](https://www.geeksforgeeks.org) / [小码哥MJ](https://space.bilibili.com/325538782/)

