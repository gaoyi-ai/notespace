---
title: Huffman Tree
categories:
- DSA
- DS
tags:
- huffman tree 
- huffman code
date: 2019/8/1 20:00:14
updated: 2020/12/10 12:00:14
---



@[toc]

# Huffman Tree
最优二叉树 指带权路径长度最短的树

# 特点

 - 不唯一（具有相同带权结点）且 左右子树可以互换
 - 带权值的节点都是叶子节点
 - 只有叶子节点和度为2的节点，没有度为1的节点
 - 若有n个叶子节点，则一共有2n-1个结点
![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200716175015353.png)

# code
```python
class HeapNode:
    def __init__(self, char=None, weight=None, left=None, right=None):
        self.char = char
        self.weight = weight
        self.left = left
        self.right = right

    def __lt__(self, other):
        return self.weight < other.weight

from itertools import groupby
from heapq import *

class HuffmanTree:
    def __init__(self):
        self.codes = {}
        self.reverse_mapping = {}

    def build(self, text):  # 使用heapq构建最小堆，生成Haffman树
        minheap = [HeapNode(char, len(list(times))) for char, times in groupby(sorted(text))] # groupby为分类并统计出现次数
        heapify(minheap) # heapify之后的为列表，按weight排好
        while len(minheap) > 1:
            left = heappop(minheap) # 取出最小的
            right = heappop(minheap) # 取出最小的
            parent = HeapNode(None, left.weight + right.weight) # 将它们并在一起
            parent.left = left
            parent.right = right
            heappush(minheap, parent) # 并在一起的父结点再入堆，重复找两个最小的
        return minheap # 最终列表长度为1，Huffman树的根节点为minheap[0]

    def make_codes(self, code, node):
        if node.char: # 只有叶子节点才有char
            if not code: # 对于树的depth为1
                self.codes[node.char] = "0"
            else:
                self.codes[node.char] = code
                self.reverse_mapping[code] = node.char
        else:
            self.make_codes(code + "0", node.left) # 左子树编码为0
            self.make_codes(code + "1", node.right) # 右子树编码为1

    def encoded_text(self, text):
        encoded_text = ""
        for character in text:
            encoded_text += self.codes[character]
        return encoded_text

    def decode_text(self, encoded_text):
        current_code = ""
        decoded_text = ""
        for bit in encoded_text:
            current_code += bit
            if current_code in self.reverse_mapping:
                character = self.reverse_mapping[current_code]
                decoded_text += character
                current_code = ""
        return decoded_text
    def wpl(self,subtree,depth):
        if subtree.left is None and subtree.right is None: # 只有为叶子结点时，才计算权长
            return depth*subtree.weight
        else:
            return self.wpl(subtree.left,depth+1) + self.wpl(subtree.right,depth+1) # 非叶子节点，递归的计算它的左右子树的权长
```

```python
def test_huffman():
    ht = HuffmanTree()
    text = 'hhhhhhhhhellllllooo'
    httree = ht.build(text) # 根据text中字符出现的频率来构建一颗Huffman树
    ht.make_codes('', httree[0]) # 对叶子结点的字符编码
    print(ht.codes) # 字符对应的编码 {'h': '0', 'e': '100', 'o': '101', 'l': '11'}
    print(ht.reverse_mapping) # 编码对应的字符 {'0': 'h', '100': 'e', '101': 'o', '11': 'l'}
    encode = ht.encoded_text("hello")
    text_ = ht.decode_text(encode)
    print('code:',encode) # code: 01001111101
    print('text:',text_) # text: hello
    print('WPL:',ht.wpl(httree[0],0)) # Huffman树的权长最小 WPL: 33
```

# Huffman Code
由测试结果已知：
- n 个权值构建出来的哈夫曼树拥有 n 个叶子节点
- 每个哈夫曼编码都不是另一个哈夫曼编码的前缀
- 哈夫曼树是带权路径长度最短的树，权值较大的节点离根节点较近
- 带权路径长度：树中所有的叶子节点的权值乘上其到根节点的路径长度。与最终的哈夫曼编码总长度成正比关系。

> Reference：[小码哥MJ](https://space.bilibili.com/325538782/)

