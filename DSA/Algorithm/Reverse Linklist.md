---
title: 反转链表
categories:
- DSA
- Algorithm
- LeetCode
date: 2019/8/1 20:00:14
updated: 2020/12/10 12:00:14
---



## 反转链表

 1. **反转一定个数节点**

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20191020194153196.png)
**tmp用来记住old的next结点**

```python
# class Node(object):
#     """docstring for Node"""
#     def __init__(self, value=None, next=None):
#         self.value , self.next = value, next
# class LinkedList(object):
#     """docstring for linkedList"""
#     def __init__(self,maxsize=None):
#         self.maxsize = maxsize
#         self.root = Node()
#         self.length = 0
#         self.tailnode = None
#     def __len__(self):
#         return self.length
#     def append(self,value):
#         if self.maxsize is not None and len(self) > self.maxsize:
#             raise Exception('Full')
#         node = Node(value)
#         tailnode = self.tailnode
#         if tailnode is None:
#             self.root.next = node
#         else:
#             tailnode.next = node
#         self.tailnode = node
#         self.length +=1

def reverse_linklist(ll,k):
    count = 1
    new = ll.next
    old = new.next
    while count<k: # 如果想要逆转全部的 list，判断就为 old is not None
        tmp = old.next
        old.next = new
        new = old
        old = tmp
        count +=1
    ll.next.next = old 
    # ll.next还是传来的根结点的下一个，ll.next.next把指 0的next指向 old
    return new # new此时就是头结点

# def test_reverse_linklist():
#     ll = LinkedList()
#     for i in range(1,10):
#         ll.append(i)
#     rev_ll = reverse_linklist(ll.root,5)
#     assert rev_ll.value == 5
#     while rev_ll:
#         print(rev_ll.value)
#         rev_ll =rev_ll.next
```

 2. **对完全反转和二次反转的一些理解**

```python
def reverse_linklist(ll):
    count = 1
    new = ll.next
    old = new.next
    while old: # 如果想要逆转全部的list，判断就为old is not None
        tmp = old.next
        old.next = new
        new = old
        old = tmp
        count +=1
    ll.next.next = old # ll.next还是传来的根结点的下一个，ll.next.next把指0的next指向old
    return new # new此时就是头结点
def test_reverse_linklist():	
    ll = LinkedList()
    for i in range(5):
        ll.append(i)
    rev_ll = reverse_linklist(ll.root) # rel_ll 没有根节点，也没有尾节点
    root= Node(next=rev_ll) # 为rel_ll 添加根节点
    res_ll2 = reverse_linklist(root)
```

ll链表
![ll链表](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20191026144758340.jpg)
rev_ll链表
注意到此时ll链表的root和tailnode交换，即头尾互换
rev_ll没有根结点
![rev_ll链表](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20191026144548354.jpg)
rev_ll2链表
rev_ll2是由rev_ll加上根节点反转的
注意此时的ll链表再次反转

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20191026144910537.jpg)
