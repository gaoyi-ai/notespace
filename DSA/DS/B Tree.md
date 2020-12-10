---
title: B Tree
categories:
- DSA
- DS
tags:
- B
date: 2019/8/1 20:00:14
updated: 2020/12/10 12:00:14
---



@[toc]
**本文采用主动插入算法**

# B 树
- B树是一种自平衡搜索树。
- 在大多数其他的自平衡搜索树(如AVL和红黑树)中，都假定所有内容都在主内存中。
- 要理解B树的用法，我们必须考虑无法装入主内存的大量数据。当键数高时，数据以块的形式从磁盘读取。与主存访问时间相比，磁盘访问时间非常高。
- 使用B树的主要思想是减少磁盘访问的次数。大多数的树操作(搜索，插入，删除，最大，最小，..等)需要O(h)磁盘访问，其中h是树的高度。
- B树是一棵肥美的树。通过在b树节点中放置最大可能的键来保持B树的高度较低。通常，B树节点大小与磁盘块大小保持相等。
- 由于B树的h值较低，因此与AVL树、红黑树等平衡二叉搜索树相比，大部分操作的总磁盘访问量大大减少。
# 性质
 - 所有的叶子都在同一水平面上（**平衡，每个节点的所有子树高度一致**）
 - 1. B树由最小度t定义。t的值取决于磁盘块大小
除根节点外，每个节点都必须包含至少MinDeg-1个键。根目录可以包含至少一个键
所有节点(包括根节点)最多可以包含2*MinDeg-1个键
 - 2. B树还可以由阶来定义。 
**m阶B树的性质（m≥2）
假设一个节点存储的元素个数为 x
根节点：1 ≤ x ≤ m − 1
非根节点：┌ m/2 ┐ − 1 ≤ x ≤ m − 1
如果有子节点，子节点个数 y = x + 1
✓ 根节点：2 ≤ y ≤ m 
✓ 非根节点：┌ m/2 ┐ ≤ y ≤ m 
➢ 比如 m = 3，2 ≤ y ≤ 3，因此可以称为（2, 3）树、2-3树
➢ 比如 m = 4，2 ≤ y ≤ 4，因此可以称为（2, 4）树、2-3-4树**
➢ 比如 m = 5，3 ≤ y ≤ 5，因此可以称为（3, 5）树
➢ 比如 m = 6，3 ≤ y ≤ 6，因此可以称为（3, 6）树
➢ 比如 m = 7，4 ≤ y ≤ 7，因此可以称为（4, 7）树
◼ **思考：如果 m = 2，那B树是一颗BST**
 - 一个节点的所有键按递增顺序排序。两个键k1和k2之间的子键包含k1和k2范围内的所有键。与二叉搜索树不同的是，B树从根开始生长和收缩。二叉搜索树向下生长，也向下收缩
 - 与其他平衡二叉搜索树一样，搜索、插入和删除的时间复杂度为O(logn)

# B 树 vs BST
![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/2020071118212642.png)
# 搜索
1. 先在节点内部从小到大开始搜索元素
2. 如果命中，搜索结束
3. 如果未命中，再去对应的子节点中搜索元素，重复步骤 1

# 添加
- **新添加的元素必定时添加到叶子节点** 
// 即添加到最后一行，从上向下不断的比较，所以一定插入到叶子节点
- **当插入新节点时节点个数超过限制，则会导致上溢overflow**
	- 1. // 上溢节点keys的个数一定为最大限制数（2* MinDeg-1），则把中间位置mid的元素向上与父结点合并
// 把[0,mid-1],[mid+1,2* MinDeg-1]分裂成两个子节点
// 并且这两个子节点的keys个数都不会少于MinDeg
// 注意，父节点可能仍然会上溢，则采用递归方法处理
	- 2. 添加 – 上溢的解决(假设5阶)
◼ 上溢节点的元素个数必然等于 m
◼ 假设上溢节点最中间元素的位置为 k
将 k 位置的元素向上与父节点合并
将 [0, k-1] 和 [k + 1, m - 1] 位置的元素分裂成 2 个子节点
✓ 这 2 个子节点的元素个数，必然都不会低于最低限制（┌ m/2 ┐ − 1）
◼ 一次分裂完毕后，有可能导致父节点上溢，依然按照上述方法解决
最极端的情况，有可能一直分裂到根节点
- **当分裂到根节点时，整个B树就会长高（B树长高的唯一途径就是根节点overflow）**

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200711182850371.png)![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200711182759277.png)![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200711182818570.png)

让要插入的键为k，就像BST一样，从根节点开始，向下遍历，直到到达叶节点。一旦我们到达一个叶节点，我们就在那个叶节点中插入密钥。与BST不同，我们在一个节点可以包含的键数上有一个预定义的范围。所以在向节点插入键之前，我们要确保节点有额外的空间。
为确保节点在插入密钥之前有可用空间，使用splitChild()，该操作用于分割节点的一个子节点。子节点y (x)被分成两个节点y和z。注意，splitChild操作将一个键向上移动，这是b树生长的原因，而bst则相反。如前所述，要插入新键，需要从根向下到叶。在向下遍历节点之前，我们首先检查节点是否已满。如果节点已满，我们将其拆分以创建空间。

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200711183159976.png)

**注意：**
算法遵循Cormen的书。它实际上是一种主动插入算法，在向下访问节点之前，如果节点已满，我们就对其进行分割。分割的优点是，我们从不遍历一个节点两次。如果我们在向下访问一个节点并仅在插入一个新键(响应式)时才对其进行拆分，那么我们可能会再次从叶子节点遍历到根节点。当从根到叶的路径上的所有节点都满时，就会发生这种情况。当我们到达叶节点时，我们把它分开，然后向上移动一个键。向上移动一个键会导致父节点分裂(因为父节点已经满了)。这种级联效应在这种主动插入算法中从未发生。这种主动插入有一个缺点，我们可能会做不必要的分割。
# 删除
从B树中删除比插入更复杂，因为我们可以从任何节点(而不仅仅是叶子节点)中删除键，而且当我们从内部节点中删除键时，我们将不得不重新排列节点的子节点。与插入一样，我们必须确保删除操作不会违反B树性质。正如我们必须确保节点不会因为插入而变得太大一样，我们也必须确保节点在删除期间不会变得太小(除了允许根的键数小于最小的MinDeg-1)。

- 假如需要删除的元素在非叶子节点中
	1. 先找到前驱或后继元素，覆盖所需删除元素的值
	2. 再把前驱或后继元素删除

	◼ 非叶子节点的前驱或后继元素，必定在叶子节点中
	所以这里的删除前驱或后继元素 ，就是最开始提到的情况：删除的元素在叶子节点中
	**真正的删除元素都是发生在叶子节点**

## 下溢 underflow
- 叶子节点被删掉一个元素后，元素个数可能会低于最低限制（ ≥ ┌ m/2 ┐ − 1 ）
这种现象称为：下溢（underflow）
- 1. ◼ 下溢节点的元素数量必然等于 ┌ m/2 ┐ − 2
◼ 如果下溢节点临近的兄弟节点，有至少 ┌ m/2 ┐ 个元素，可以向其借一个元素
将父节点的元素 b 插入到下溢节点的 0 位置（最小位置）
用兄弟节点的元素 a（最大的元素）替代父节点的元素 b
这种操作其实就是：旋转
- 2. **当删去节点会导致节点keys个数少于MinDeg，则会导致下溢**
// 注意，下溢的节点keys个数一定为MinDeg-1
// 如果兄弟节点有至少MinDeg+1个keys，则可以向其借
// 需要把父结点的一个元b插入到下溢节点的首位置
// 再不兄弟节点的最大元素a插入到父结点代替b
// 即对b右旋

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200711183640826.png)
- // 注意，当兄弟节点不够借时
// 将父结点的元素b下移和该节点和兄弟节点合并（节点keys个数不会超过限制）
// 注意，父结点也有可能下溢，递归解决

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200711183803403.png)
◼ 如果下溢节点临近的兄弟节点，只有 ┌ m/2 ┐ − 1 个元素
 将父节点的元素 b 挪下来跟左右子节点进行合并
 合并后的节点元素个数等于┌ m/2 ┐ + ┌ m/2 ┐ − 2，不超过 m − 1
 这个操作可能会导致父节点下溢，依然按照上述方法解决，下溢现象可能会一直往上传播

- **当下溢到根节点时，整个B树就会变矮（B树变矮的唯一途径就是根节点underflow）**

# 4阶B树
如果先学习4阶B树（2-3-4树），将能更好地学习理解红黑树
◼ 4阶B树的性质
所有节点能存储的元素个数 x ：1 ≤ x ≤ 3
所有非叶子节点的子节点个数 y ：2 ≤ y ≤ 4 

观察
从 1 添加到 22；从 1 删除到 22

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/2020071118505157.png)
![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200711185655797.gif)

# code
```java
class BTreeNode{

    int[] keys; // 节点的keys
    int MinDeg; // B树节点的最小度数
    BTreeNode[] children; // 子节点
    int num; // 节点的keys数
    boolean isLeaf; // 当为叶子节点时为真

    // 构造函数
    public BTreeNode(int deg,boolean isLeaf){

        this.MinDeg = deg;
        this.isLeaf = isLeaf;
        this.keys = new int[2*this.MinDeg-1]; // 节点最多有2*MinDeg-1个keys
        this.children = new BTreeNode[2*this.MinDeg];
        this.num = 0;
    }

    // 找到第一个等于或大于key的位置索引
    public int findKey(int key){

        int idx = 0;
        // 退出循环的条件有: 1.idx == num, 即全部扫描一遍
        // 2.idx < num, 即找到key或大于key
        while (idx < num && keys[idx] < key)
            ++idx;
        return idx;
    }


    public void remove(int key){

        int idx = findKey(key);
        if (idx < num && keys[idx] == key){ // 找到key
            if (isLeaf) // key在叶子节点中
                removeFromLeaf(idx);
            else // key不在叶子节点中
                removeFromNonLeaf(idx);
        }
        else{
            if (isLeaf){ // 如果该节点为叶子节点，那么则该节点不在B树中
                System.out.printf("The key %d is does not exist in the tree\n",key);
                return;
            }

            // 否则,要删除的键存在于以该节点为根的子树中

            // 该标志指示键是否存在于以该节点的最后一个子节点为根的子树中
            // 当idx等于num时，即比较了整个节点，flag为true
            boolean flag = idx == num; 
            
            if (children[idx].num < MinDeg) // 当该节点的孩子节点没有满时，先填满
                fill(idx);


            //如果最后一个子节点已经合并，那么它必须已经与前一个子节点合并，因此我们在第（idx-1）个子节点上递归。
            // 否则，我们递归到第（idx）个子节点，该子节点现在至少有最小度数的keys
            if (flag && idx > num)
                children[idx-1].remove(key);
            else
                children[idx].remove(key);
        }
    }

    public void removeFromLeaf(int idx){

        // 从idx后移
        for (int i = idx +1;i < num;++i)
            keys[i-1] = keys[i];
        num --;
    }

    public void removeFromNonLeaf(int idx){

        int key = keys[idx];

        // 如果key (children[idx]) 之前的子树至少有t个键
        // 那么在以children[idx]为根的子树中找到key的前驱'pred'
        // 用'pred'替换key, 递归删除children[idx]中的pred
        if (children[idx].num >= MinDeg){
            int pred = getPred(idx);
            keys[idx] = pred;
            children[idx].remove(pred);
        }
        // 如果children[idx]有比MinDeg少的keys,就检查children[idx+1]
        // 如果children[idx+1]有至少MinDeg个键,在根为children[idx+1]的子树中
        // 找到key的后继'succ'递归删除children[idx+1]中的succ
        else if (children[idx+1].num >= MinDeg){
            int succ = getSucc(idx);
            keys[idx] = succ;
            children[idx+1].remove(succ);
        }
        else{
            // 如果children[idx]和children[idx+1]的键数都小于MinDeg
            // 则将key和children[idx+1]合并为children[idx]
            // 现在children[idx]包含2t-1键
            // 释放children[idx+1],递归删除children[idx]中的key
            merge(idx);
            children[idx].remove(key);
        }
    }

    public int getPred(int idx){ // 前驱节点为从左子树一直找最右的节点

        // 一直移动到最右边的节点,直到到达叶子节点
        BTreeNode cur = children[idx];
        while (!cur.isLeaf)
            cur = cur.children[cur.num];
        return cur.keys[cur.num-1];
    }

    public int getSucc(int idx){ // 后继节点为从右子树一直向左找

        // 继续从children[idx+1]开始移动最左边的节点,直到到达叶子节点
        BTreeNode cur = children[idx+1];
        while (!cur.isLeaf)
            cur = cur.children[0];
        return cur.keys[0];
    }

    // 填满children[idx]其有少于MinDeg个keys
    public void fill(int idx){

        // 如果前一个子节点有多个MinDeg-1个keys,就从其中借用
        if (idx != 0 && children[idx-1].num >= MinDeg)
            borrowFromPrev(idx);
        // 后一个子节点有多个MinDeg-1个keys,就从其中借用
        else if (idx != num && children[idx+1].num >= MinDeg)
            borrowFromNext(idx);
        else{
            // 合并children[idx]和它的兄弟
            // 如果children[idx]是最后一个子节点
            // 那么将它与前一个子节点合并否则就将它与它的下一个兄弟合并
            if (idx != num)
                merge(idx);
            else
                merge(idx-1);
        }
    }

    // 从children[idx-1]中借一个key,然后插入到children[idx]
    public void borrowFromPrev(int idx){

        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx-1];

        // 来自children[idx-1]的最后一个key上溢到父节点
        // 来自父节点的key[idx-1]下溢作为children[idx]中的第一个键插入
        // 因此，sibling减少一个，children增加一个
        for (int i = child.num-1; i >= 0; --i) // children[idx]前移
            child.keys[i+1] = child.keys[i];

        if (!child.isLeaf){ // 当children[idx]不是叶子节点时，将其孩子节点后移
            for (int i = child.num; i >= 0; --i)
                child.children[i+1] = child.children[i];
        }

        // 将子节点的第一个键设置为当前节点的keys[idx-1]
        child.keys[0] = keys[idx-1];
        if (!child.isLeaf) // 将sibling的最后一个子节点作为children[idx]的第一个子节点
            child.children[0] = sibling.children[sibling.num];

        // 把sibling的最后一个键上移到当前节点的最后一个
        keys[idx-1] = sibling.keys[sibling.num-1];
        child.num += 1;
        sibling.num -= 1;
    }

    // 与borrowFromPrev对称
    public void borrowFromNext(int idx){

        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx+1];

        child.keys[child.num] = keys[idx];

        if (!child.isLeaf)
            child.children[child.num+1] = sibling.children[0];

        keys[idx] = sibling.keys[0];

        for (int i = 1; i < sibling.num; ++i)
            sibling.keys[i-1] = sibling.keys[i];

        if (!sibling.isLeaf){
            for (int i= 1; i <= sibling.num;++i)
                sibling.children[i-1] = sibling.children[i];
        }
        child.num += 1;
        sibling.num -= 1;
    }

    // 把childre[idx+1]合并到childre[idx]
    public void merge(int idx){

        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx+1];

        // 将当前节点的最后一个键插入到子节点的MinDeg-1位置上
        child.keys[MinDeg-1] = keys[idx];

        // keys: children[idx+1]复制到children[idx]
        for (int i =0 ; i< sibling.num; ++i)
            child.keys[i+MinDeg] = sibling.keys[i];

        // children: children[idx+1]复制到children[idx]
        if (!child.isLeaf){
            for (int i = 0;i <= sibling.num; ++i)
                child.children[i+MinDeg] = sibling.children[i];
        }

        // 将keys前移, 不上由于移动keys[idx]到children[idx]造成的gap
        for (int i = idx+1; i<num; ++i)
            keys[i-1] = keys[i];
        // 将对应的子节点也前移
        for (int i = idx+2;i<=num;++i)
            children[i-1] = children[i];

        child.num += sibling.num + 1;
        num--;
    }


    public void insertNotFull(int key){

        int i = num -1; // 初始化i为最右值的索引

        if (isLeaf){ // 当为叶子节点时
            // 找到新的key应该插入的位置
            while (i >= 0 && keys[i] > key){
                keys[i+1] = keys[i]; // keys后移
                i--;
            }
            keys[i+1] = key;
            num = num +1;
        }
        else{
            // 找到应该插入的子节点位置
            while (i >= 0 && keys[i] > key)
                i--;
            if (children[i+1].num == 2*MinDeg - 1){ // 当子节点已经满时
                splitChild(i+1,children[i+1]);
                // 分裂后，子节点中间的key上移, 该子节点分裂为两个
                if (keys[i+1] < key)
                    i++;
            }
            children[i+1].insertNotFull(key);
        }
    }


    public void splitChild(int i ,BTreeNode y){

        // 首先创建一个容纳y的MinDeg-1的keys的节点
        BTreeNode z = new BTreeNode(y.MinDeg,y.isLeaf);
        z.num = MinDeg - 1;

        // 把y的属性都传递给z
        for (int j = 0; j < MinDeg-1; j++)
            z.keys[j] = y.keys[j+MinDeg];
        if (!y.isLeaf){
            for (int j = 0; j < MinDeg; j++)
                z.children[j] = y.children[j+MinDeg];
        }
        y.num = MinDeg-1;

        // 把新的子节点插入到子节点中
        for (int j = num; j >= i+1; j--)
            children[j+1] = children[j];
        children[i+1] = z;

        // 将y中的一个key会移到此节点中
        for (int j = num-1;j >= i;j--)
            keys[j+1] = keys[j];
        keys[i] = y.keys[MinDeg-1];

        num = num + 1;
    }


    public void traverse(){
        int i;
        for (i = 0; i< num; i++){
            if (!isLeaf)
                children[i].traverse();
            System.out.printf(" %d",keys[i]);
        }

        if (!isLeaf){
            children[i].traverse();
        }
    }


    public BTreeNode search(int key){
        int i = 0;
        while (i < num && key > keys[i])
            i++;

        if (keys[i] == key)
            return this;
        if (isLeaf)
            return null;
        return children[i].search(key);
    }
}


class BTree{
    BTreeNode root;
    int MinDeg;

    // 构造函数
    public BTree(int deg){
        this.root = null;
        this.MinDeg = deg;
    }

    public void traverse(){
        if (root != null){
            root.traverse();
        }
    }

    // 查找key的函数
    public BTreeNode search(int key){
        return root == null ? null : root.search(key);
    }

    public void insert(int key){

        if (root == null){

            root = new BTreeNode(MinDeg,true);
            root.keys[0] = key;
            root.num = 1;
        }
        else {
            // 当根节点已经满时, 此时树就要长高
            if (root.num == 2*MinDeg-1){
                BTreeNode s = new BTreeNode(MinDeg,false);
                // 旧的根节点成为新的根节点的子节点
                s.children[0] = root;
                // 分开旧的根节点, 并且将一个key给新节点
                s.splitChild(0,root);
                // 新的根节点有2个子节点, 要把旧的根节点移到那边
                int i = 0;
                if (s.keys[0]< key)
                    i++;
                s.children[i].insertNotFull(key);

                root = s;
            }
            else
                root.insertNotFull(key);
        }
    }

    public void remove(int key){
        if (root == null){
            System.out.println("The tree is empty");
            return;
        }

        root.remove(key);

        if (root.num == 0){ // 如果根节点有0个键
            // 如果它有一个子节点，则将它的第一个子节点作为新根节点，
            // 否则将根节点设置为null
            if (root.isLeaf)
                root = null;
            else
                root = root.children[0];
        }
    }
}
```

# 测试
```java
public class Main {

    public static void main(String[] args) {

        BTree t = new BTree(2); // A B-Tree with minium degree 2
        t.insert(1);
        t.insert(3);
        t.insert(7);
        t.insert(10);
        t.insert(11);
        t.insert(13);
        t.insert(14);
        t.insert(15);
        t.insert(18);
        t.insert(16);
        t.insert(19);
        t.insert(24);
        t.insert(25);
        t.insert(26);
        t.insert(21);
        t.insert(4);
        t.insert(5);
        t.insert(20);
        t.insert(22);
        t.insert(2);
        t.insert(17);
        t.insert(12);
        t.insert(6);

        System.out.println("Traversal of tree constructed is");
        t.traverse();
        System.out.println();

        t.remove(6);
        System.out.println("Traversal of tree after removing 6");
        t.traverse();
        System.out.println();

        t.remove(13);
        System.out.println("Traversal of tree after removing 13");
        t.traverse();
        System.out.println();

        t.remove(7);
        System.out.println("Traversal of tree after removing 7");
        t.traverse();
        System.out.println();

        t.remove(4);
        System.out.println("Traversal of tree after removing 4");
        t.traverse();
        System.out.println();

        t.remove(2);
        System.out.println("Traversal of tree after removing 2");
        t.traverse();
        System.out.println();

        t.remove(16);
        System.out.println("Traversal of tree after removing 16");
        t.traverse();
        System.out.println();
    }
}
```

# 被动插入算法

> Source：[geeksforgeeks](https://www.geeksforgeeks.org)

找到它所属的叶节点，并将其插入其中。我们通过在适当的子节点上调用insert算法来递归地插入key。这个过程的结果是向下到key所属的叶节点，将key插入，然后一直返回到根节点。有时一个节点是满的，即它包含2*t-1项，其中t是最小度数。在这种情况下，必须拆分节点。在这种情况下，一个键成为父节点，并创建一个新节点。我们首先插入新密钥，使总密钥为2*t。将前t项保留在原始节点中，将最后的(t-1)项转移到新节点，并将(t+1)th节点设置为这些节点的父节点。如果被分割的节点是非子节点，那么我们也必须分割子指针。一个有2*t键的节点有2*t + 1个子指针。第一个(t+1)指针保存在原始节点中，其余的t指针指向新节点。

code:

```cpp
class BTreeNode { 

	// Vector of keys 
	vector<int> keys; 

	// Minimum degree 
	int t; 

	// Vector of child pointers 
	vector<BTreeNode*> C; 

	// Is true when node is leaf, else false 
	bool leaf; 

public: 
	// Constructor 
	BTreeNode(int t, bool leaf); 

	// Traversing the node and print its content 
	// with tab number of tabs before 
	void traverse(int tab); 

	// Insert key into given node. If child is split, we 
	// have to insert *val entry into keys vector and 
	// newEntry pointer into C vector of this node 
	void insert(int key, int* val, 
				BTreeNode*& newEntry); 

	// Split this node and store the new parent value in 
	// *val and new node pointer in newEntry 
	void split(int* val, BTreeNode*& newEntry); 

	// Returns true if node is full 
	bool isFull(); 

	// Makes new root, setting current root as its child 
	BTreeNode* makeNewRoot(int val, BTreeNode* newEntry); 
}; 

bool BTreeNode::isFull() 
{ 
	// returns true if node is full 
	return (this->keys.size() == 2 * t - 1); 
} 

BTreeNode::BTreeNode(int t, bool leaf) 
{ 
	// Constructor to set value of t and leaf 
	this->t = t; 
	this->leaf = leaf; 
} 

// Function to print the nodes of B-Tree 
void BTreeNode::traverse(int tab) 
{ 
	int i; 
	string s; 

	// Print 'tab' number of tabs 
	for (int j = 0; j < tab; j++) { 
		s += '\t'; 
	} 
	for (i = 0; i < keys.size(); i++) { 

		// If this is not leaf, then before printing key[i] 
		// traverse the subtree rooted with child C[i] 
		if (leaf == false) 
			C[i]->traverse(tab + 1); 
		cout << s << keys[i] << endl; 
	} 

	// Print the subtree rooted with last child 
	if (leaf == false) { 
		C[i]->traverse(tab + 1); 
	} 
} 

// Function to split the current node and store the new 
// parent value is *val and new child pointer in &newEntry 
// called only for splitting non-leaf node 
void BTreeNode::split(int* val, BTreeNode*& newEntry) 
{ 

	// Create new non leaf node 
	newEntry = new BTreeNode(t, false); 

	//(t+1)th becomes parent 
	*val = this->keys[t]; 

	// Last (t-1) entries will go to new node 
	for (int i = t + 1; i < 2 * t; i++) { 
		newEntry->keys.push_back(this->keys[i]); 
	} 

	// This node stores first t entries 
	this->keys.resize(t); 

	// Last t entries will go to new node 
	for (int i = t + 1; i <= 2 * t; i++) { 
		newEntry->C.push_back(this->C[i]); 
	} 

	// This node stores first (t+1) entries 
	this->C.resize(t + 1); 
} 

// Function to insert a new key in given node. 
// If child of this node is split, we have to insert *val 
// into keys vector and newEntry pointer into C vector 
void BTreeNode::insert(int new_key, int* val, 
					BTreeNode*& newEntry) 
{ 

	// Non leaf node 
	if (leaf == false) { 
		int i = 0; 

		// Find first key greater than new_key 
		while (i < keys.size() && new_key > keys[i]) 
			i++; 

		// We have to insert new_key into left child of 
		// Node with index i 
		C[i]->insert(new_key, val, newEntry); 

		// No split was done 
		if (newEntry == NULL) 
			return; 
		if (keys.size() < 2 * t - 1) { 

			// This node can accomodate a new key 
			// and child pointer entry 
			// Insert *val into key vector 
			keys.insert(keys.begin() + i, *val); 

			// Insert newEntry into C vector 
			C.insert(C.begin() + i + 1, newEntry); 

			// As this node was not split, set newEntry 
			// to NULL 
			newEntry = NULL; 
		} 
		else { 

			// Insert *val and newentry 
			keys.insert(keys.begin() + i, *val); 
			C.insert(C.begin() + i + 1, newEntry); 

			// Current node has 2*t keys, so split it 
			split(val, newEntry); 
		} 
	} 
	else { 

		// Insert new_key in this node 
		vector<int>::iterator it; 

		// Find correct position 
		it = lower_bound(keys.begin(), keys.end(), 
						new_key); 

		// Insert in correct position 
		keys.insert(it, new_key); 

		// If node is full 
		if (keys.size() == 2 * t) { 

			// Create new node 
			newEntry = new BTreeNode(t, true); 

			// Set (t+1)th key as parent 
			*val = this->keys[t]; 

			// Insert last (t-1) keys into new node 
			for (int i = t + 1; i < 2 * t; i++) { 
				newEntry->keys.push_back(this->keys[i]); 
			} 

			// This node stores first t keys 
			this->keys.resize(t); 
		} 
	} 
} 

// Function to create a new root 
// setting current node as its child 
BTreeNode* BTreeNode::makeNewRoot(int val, BTreeNode* newEntry) 
{ 
	// Create new root 
	BTreeNode* root = new BTreeNode(t, false); 

	// Stores keys value 
	root->keys.push_back(val); 

	// Push child pointers 
	root->C.push_back(this); 
	root->C.push_back(newEntry); 
	return root; 
} 

class BTree { 

	// Root of B-Tree 
	BTreeNode* root; 

	// Minimum degree 
	int t; 

public: 
	// Constructor 
	BTree(int t); 

	// Insert key 
	void insert(int key); 

	// Display the tree 
	void display(); 
}; 

// Function to create a new BTree with 
// minimum degree t 
BTree::BTree(int t) 
{ 
	root = new BTreeNode(t, true); 
} 

// Function to insert a node in the B-Tree 
void BTree::insert(int key) 
{ 
	BTreeNode* newEntry = NULL; 
	int val = 0; 

	// Insert in B-Tree 
	root->insert(key, &val, newEntry); 

	// If newEntry is not Null then root needs to be 
	// split. Create new root 
	if (newEntry != NULL) { 
		root = root->makeNewRoot(val, newEntry); 
	} 
} 

// Prints BTree 
void BTree::display() 
{ 
	root->traverse(0); 
} 

// Driver code 
int main() 
{ 

	// Create B-Tree 
	BTree* tree = new BTree(3); 
	cout << "After inserting 1 and 2" << endl; 
	tree->insert(1); 
	tree->insert(2); 
	tree->display(); 

	cout << "After inserting 5 and 6" << endl; 
	tree->insert(5); 
	tree->insert(6); 
	tree->display(); 

	cout << "After inserting 3 and 4" << endl; 
	tree->insert(3); 
	tree->insert(4); 
	tree->display(); 

	return 0; 
} 
```


> Reference：[geeksforgeeks](https://www.geeksforgeeks.org) / [小码哥MJ](https://space.bilibili.com/325538782/)
