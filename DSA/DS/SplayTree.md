---
title: Splay Tree
categories:
- DSA
- DS
tags:
- Splay Tree
date: 2019/8/1 20:00:14
updated: 2020/12/10 12:00:14
---



﻿@[toc]

> Reference：https://www.geeksforgeeks.org/splay-tree-set-2-insert-delete/

### 伸展树(Splay Tree)的特点
二叉搜索树(BST)操作最坏情况下的时间复杂度为O(n)。最坏的情况发生在树倾斜的时候。通过AVL和红黑树，我们可以得到最坏情况下的时间复杂度O(logn)。
在实际应用中，我们能比AVL或红黑树做得更好吗?
与AVL和红黑树一样，Splay树也是自平衡的BST。**splay树的主要思想是将最近访问过的项放到树根位置，这使得最近搜索的项在O(1)时间内可以再次访问**。其思想是使用引用的位置(在典型的应用程序中，**80%的访问是20%的项**)。设想这样一种情况:我们有数百万或数十亿个密钥，但只有少数几个密钥被频繁访问，这在许多实际应用中很有可能。所有splay树操作平均运行时间为O(log n)，其中n是树中的条目数。在最坏的情况下，任何一个操作都要花费(n)的时间。
### 搜索(Search)
搜索或splay操作不仅将搜索到的键移动到树根位置，而且还平衡了BST。

### 插入(insert)
下面是在splay树中插入键k的不同情况：
 1. Root是None：简单地分配一个新节点并将其作为根节点返回。
 2. 将给定的键k展开(splay)，如果k已经存在，那么它将成为新的根。如果不存在，则最后访问的叶子节点将成为新的根。
 3. 如果新根的键与k相同，则不要执行任何操作，因为k已经存在。
 4. 否则为新节点分配内存，并将root的key与k进行比较。
 5. 如果k小于root的key，则将root作为新节点的右子节点，将root的左子节点复制为新节点的左子节点，将root的左子节点复制为None
 6. 如果k大于root的key，则将root作为新节点的左子节点，将root的右子节点复制为新节点的右子节点，将root的右子节点复制为None
 7. 将新节点作为树的新根返回。

### 归纳
 1. Splay树具有良好的局部性。经常访问的项很容易找到
 2. 所有的splay树操作平均花费O(logn)时间。可以严格地显示，在任何操作序列(假设我们从一个空树开始)上，每个操作的平均运行时间为O(log n)
 3. 与AVL和红黑树相比，Splay树更简单，因为每个树节点都不需要额外的字段
 4. 与AVL树不同，splay树可以通过只读操作(如搜索)进行更改

### 代码实现


```python
class Node(object):
    def __init__(self,key):
        self.key = key
        self.left = None
        self.right = None
        
class SplayTree(object):
    def __init__(self):
        self.root = None
        self.size = 0
    def RightRotate(self,x): # x为根节点
        y = x.left
        x.left = y.right
        y.right = x
        return y
    def LeftRotate(self,x): # x为根节点
        y = x.right
        x.right = y.left
        y.left = x
        return y
    def splay(self,root,key):
        """
        如果键存在于树中，则将键置于树根位置
        如果键不存在，那么将把最后一个被访问的项放在树根
        """
        if root is None or root.key == key:
            # 没有根或者树根为key则对根进行splay
            return root
        if root.key > key: # key在左子树中
            if root.left is None: # 左子树为空，则返回唯一访问的根节点
                return root
            if root.left.key > key: # Zig-Zig (Left Left)
                # 首先递归地把key移至左子树的左子树的根
                root.left.left = self.splay(root.left.left,key)
                # 第一次对根右旋，第二次是在执行else之后才可能进行右旋
                root = self.RightRotate(root)
            elif root.left.key < key: # Zig-Zag (Left Right)
                root.left.right = self.splay(root.left.right,key)
                root.left = self.LeftRotate(root.left)
            # LR情况：对调整之后的根在进行右旋
            return root if root.left is None else self.RightRotate(root)
        else: # key在右子树中
            if root.right is None: # 右子树为空，则返回唯一访问的根节点
                return root
            if root.right.key > key: # Zig-Zag (Right Left)
                # 首先递归地把key移至右子树的左子树的根
                root.right.left = self.splay(root.right.left,key)
                if root.right.left:
                    root.right = self.RightRotate(root.right)
            elif root.right.key < key: # Zag-Zag (Right Right)
                    root.right.right = self.splay(root.right.right,key)
                    root = self.LeftRotate(root)
            # RL情况：对调整之后的根在进行左旋
            return root if root.right is None else self.LeftRotate(root)
    def search(self,key):
        self.root = self.splay(self.root,key)
    def insert(self,key):
        self.size += 1
        return self._insert(self.root,key)
    def _insert(self,root,key):
        if root is None: # 数空情况
            return Node(key)
        # 像访问要访问的节点置于根节点
        root = self.splay(root,key)
        if root.key == key: # 如果根节点==key，那么直接返回根节点
            return root
        new = Node(key)
        if root.key > key: 
            # 如果根节点的key＞key，那么就把根节点作为其右子树，根节点的左子树作为new的左子树
            new.right = root
            new.left = root.left
            root.left = None
        else:
            new.left = root
            new.right = root.right
            root.right = None
        return new # 返回新的根节点
    def preorder(self, subtree):
        if subtree is not None:
            print(subtree.key, end=' ')
            self.preorder(subtree.left)
            self.preorder(subtree.right)
            
if __name__=='__main__':
    sp = SplayTree()
    sp.root = sp.insert(100)
    sp.root =sp.insert(50)
    sp.root =sp.insert(200)
    sp.root =sp.insert(40)
    sp.root =sp.insert(30)
    sp.root =sp.insert(20)
    sp.root =sp.insert(25)
    print("前序遍历:")
    sp.preorder(sp.root) # 25 20 30 40 50 100 200 
    sp.search(200)
    print("\n当前根节点:",sp.root.key) # 200
    print("前序遍历:")
    sp.preorder(sp.root) # 200 30 25 20 50 40 100 
```

