**集合使用双亲表示法，给一个结点，找到它的根节点，即它所在的集合**

**集合常用方法：并、查找**

**路径压缩：一次查找就把处在一链上的结点的父结点，直接指向集合的根节点**
**按秩归并：比较两个集合的规模，或者树的高度，
永远让小树并到大树上，维持树的高度**
![并查集](images/%E5%B9%B6%E6%9F%A5%E9%9B%86-Python/20191103203205654.jpg)

```python
class SetADT:
    def __init__(self):
        self.set = list()  # 默认集合初始元素为-1 
    def find(self, target):
        if self.set[target] < 0:  # 找到集合的根
            return target
        else:
            # 先找到根，把根变成 x 的父结点，再返回根
            self.set[target] = self.find(self.set[target]) # 路径压缩
            return self.set[target]
    def union(self, par1=None, par2=None):
        if self.set[par1] < self.set[par2]: # 按秩归并
            self.set[par1] += self.set[par2]
            self.set[par2] = par1
        else:
            self.set[par2] += self.set[par1]
            self.set[par1] = par2
```

```python
def test_set():
    set_1 = SetADT()
    set_1.set = [6, 6, -4, 4, 2, 2, -3]  # 根来表示集合的规模
    print(set_1.find(3))
    set_1.union(2,6)
    print(set_1.set[6])
```

