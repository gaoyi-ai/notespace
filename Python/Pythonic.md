---
title: Pythonic
categories:
- Python
tags:
- pythonic
date: 2019/8/1 20:00:14
updated: 2021/1/11 12:00:14
---

# zip & map & filter

## zip

zip() 采用惰性计算，返回的是一个对象。如需展示列表，需手动 list() 转换。

```python
>>> list(zip(a,c)) # 元素个数与最短的列表一致
[(1, 4), (2, 5), (3, 6)]
 
>>> a1, a2 = zip(*zip(a,b)) # 与 zip 相反，zip(*) 可理解为解压，返回二维矩阵式
```

```python
def pascal(n): 
    if n == 1:
        return [[1]]
    else:
        result = [[1]]
        x = 1
        while x < n:
            lastRow = result[-1]
            nextRow = [(a+b) for a,b in zip([0] + lastRow, lastRow + [0])]
            result.append(nextRow)
            x += 1
        return result
pascal(4) #  [[1], [1, 1], [1, 2, 1], [1, 3, 3, 1]]
```

当x=1时，zip([0] + lastRow, lastRow + [0]) = 前面[0,1] + 后面[1,0]

当x=2时，zip([0] + lastRow, lastRow + [0]) = 前面[0,1,1] + 后面[1,1,0]

## map

```python
result8 = list(map(lambda x: x + "ing", ["play", "talk", "walk", "teach"]))
print(result8) # ['playing', 'talking', 'walking', 'teaching']
```

## filter

```python
result11 = list(filter(lambda x: len(x) < 5, ["play", "talk", "walk", "teach"]))
print(result11) # ['play', 'talk', 'walk']
```

# flatten

- 第一种方法：从list_of_lists中遍历每一个列表都和`[]`相加，注意列表相加（即拼接）
- 第二种方法：itertools.chain构造链

```python
list_of_lists = [[1,2],[3,4],[5,6]]
print(sum(list_of_lists, [])) # [1, 2, 3, 4, 5, 6]
print(list(chain.from_iterable(list_of_lists))) # [1, 2, 3, 4, 5, 6]
```

# repr & eval & exec

在编写代码时，一般会使 repr() 来生成动态的字符串，再传入到 eval() 或 exec() 函数内，实现动态执行代码的功能。

**注意eval会执行字符串内的语句，所以使用eval一定要安全检查**


```python
s="hello" 
print(repr(s)) # 'hello'
print(str(s)) # hello
a = "[2, 0, 6, 3, 1, 5, 0, 5, 1, 0]"
b = eval(a)
print(b) # ['2', '0', '6', '3', '1', '5', '0', '5', '1', '0']''
print(type(b)) # <class 'list'>
```

# 生成器

```python
g = (x * x for x in range(10))
print(g) # <generator object <genexpr> at 0x00000234C6568840>
next(g) # 0
```

# 字典value找key

If keys, values and items views are iterated over with no intervening modifications to the dictionary, the order of items will directly correspond.

在你迭代的过程中如果没有发生对字典的修改，那么keys() and values() 这两个函数返回的 dict-view对象总是保持对应关系


```python
dicxx = {'a':'001', 'b':'002'}
list(dicxx.keys())[list(dicxx.values()).index("001")]
```

反转字典


```python
old_dict = {'a':'001', 'b':'002','c':'001'}
new_dict = {}
for k,v in old_dict.items():
    if not new_dict.get(v):
        new_dict[v] = [k]
    else:
        new_dict[v].append(k)
print(new_dict['001'])  # ['a', 'c']
```

# super()

继承的功能：父类的代码重用

多态的功能：同一方法对不同类型的对象会有相应的结果

开闭原则：对扩展开放，对修改封闭

super类功能：新式类实现广度优先的不重复的调用父类，解决了钻石继承（多继承）的难题

super实现原理：通过c3算法，生成mro（method resolution order）列表，根据列表中元素顺序查询调用

新式类调用顺序为广度优先，旧式类为深度优先


个人理解：

1.调用了父类的方法，出入的是子类的实例对象

2.新式类子类（A,B），A就在B之前

3.super类似于嵌套的一种设计，当代码执行到super实例化后，先去找同级父类，若没有其余父类，再执行自身父类，再往下走，

　　简洁点的三个原则就是：

子类在父类前，所有类不重复调用，从左到右

```python
class A():
    def go(self): # A
        print ("go A go!")
class B(A):
    def go(self): # A B
        super(B, self).go()
        print ("go B go!")
class C(A):
    def go(self): # A C
        super(C, self).go()
        print ("go C go!")
class D(B,C):
    def go(self): # A C B D
        super(D, self).go()
        print ("go D go!")
```

> [Python面向对象中super用法与MRO机制](https://www.cnblogs.com/chenhuabin/p/10058594.html)

# sort()

由于 **list.sort()** 函数在排序时，使用的是小于号对比，所以自定义的数据类型需要 **override __lt__** 函数才能实现排序。

```python
class Element:  
    def __init__(self,id):  
        self.id=id  
    def __lt__(self, other): # override < 操作符  
        if self.id<other.id:  
            return True  
        return False
```

如果非要将列表里不同类型相比较，可以考虑如下方法:

```python
l = [-1, -3, 1, 0, 3, 'a', 'b', 'c']
l.sort(key = str)
```

```python
a=['delphi', 'Delphi', 'python', 'Python', 'golang', 'Golang', 'c++', 'C++', 'c', 'C']
#按字典序升序排序
a.sort() 
#先按字符串长度升序排序，长度相同按字典序升序排序
a.sort(key=lambda x:(len(x),x)) 
#先按字符串长度升序排序，长度相同按字典序降序排序
a.sort(key=lambda x:(len(x),list(map(lambda c:-ord(c),x)))) 
#先按字符串长度降序排序，长度相同按字典序升序排序
a.sort(key=lambda x:(-len(x),x))
```

再举一例：

```python
a=[[1,3],[3,2],[2,4],[1,2],[1,5],[2,5]]
#先按第一个元素升序排序，第一个元素相同按第二个元素升序排序
a.sort()
#先按第一个元素升序排序，第一个元素相同则保持原来的顺序
a=[[1,3],[3,2],[2,4],[1,2],[1,5],[2,5]]
a.sort(key=lambda x:x[0])
#先按第二个元素升序排序，第二个元素相同则保持原来的顺序
a=[[1,3],[3,2],[2,4],[1,2],[1,5],[2,5]]
a.sort(key=lambda x:x[1])
#先按第二个元素升序排序，第二个元素相同按第一个元素降序排序
a.sort(key=lambda x:(x[1],-x[0]))
```

# decorator

函数对象有一个`__name__`属性，可以拿到函数的名字：

```python
>>> now.__name__
'now'
```

现在，假设我们要增强`now()`函数的功能，比如，在函数调用前后自动打印日志，但又不希望修改`now()`函数的定义，这种在代码运行期间动态增加功能的方式，称之为“装饰器”（Decorator）。

本质上，decorator就是一个返回函数的高阶函数。所以，我们要定义一个能打印日志的decorator，可以定义如下：

```python
def log(func):
    def wrapper(*args, **kw):
        print('call %s():' % func.__name__)
        return func(*args, **kw)
    return wrapper
```

观察上面的`log`，因为它是一个decorator，所以接受一个函数作为参数，并返回一个函数。我们要借助Python的@语法，把decorator置于函数的定义处：

```python
@log
def now():
    print('2015-3-25')
```

调用`now()`函数，不仅会运行`now()`函数本身，还会在运行`now()`函数前打印一行日志：

```python
>>> now()
call now():
2015-3-25
```

把`@log`放到`now()`函数的定义处，相当于执行了语句：

```python
now = log(now)
```

由于`log()`是一个decorator，返回一个函数，所以，原来的`now()`函数仍然存在，只是现在同名的`now`变量指向了新的函数，于是调用`now()`将执行新函数，即在`log()`函数中返回的`wrapper()`函数。

`wrapper()`函数的参数定义是`(*args, **kw)`，因此，`wrapper()`函数可以接受任意参数的调用。在`wrapper()`函数内，首先打印日志，再紧接着调用原始函数。

如果decorator本身需要传入参数，那就需要编写一个返回decorator的高阶函数，写出来会更复杂。比如，要自定义log的文本：

```python
def log(text):
    def decorator(func):
        def wrapper(*args, **kw):
            print('%s %s():' % (text, func.__name__))
            return func(*args, **kw)
        return wrapper
    return decorator
```

这个3层嵌套的decorator用法如下：

```python
@log('execute')
def now():
    print('2015-3-25')
```

执行结果如下：

```python
>>> now()
execute now():
2015-3-25
```

和两层嵌套的decorator相比，3层嵌套的效果是这样的：

```python
>>> now = log('execute')(now)
```

我们来剖析上面的语句，首先执行`log('execute')`，返回的是`decorator`函数，再调用返回的函数，参数是`now`函数，返回值最终是`wrapper`函数。

以上两种decorator的定义都没有问题，但还差最后一步。因为我们讲了函数也是对象，它有`__name__`等属性，但你去看经过decorator装饰之后的函数，它们的`__name__`已经从原来的`'now'`变成了`'wrapper'`：

```python
>>> now.__name__
'wrapper'
```

因为返回的那个`wrapper()`函数名字就是`'wrapper'`，所以，需要把原始函数的`__name__`等属性复制到`wrapper()`函数中，否则，有些依赖函数签名的代码执行就会出错。

不需要编写`wrapper.__name__ = func.__name__`这样的代码，Python内置的`functools.wraps`就是干这个事的，所以，一个完整的decorator的写法如下：

```python
import functools

def log(func):
    @functools.wraps(func)
    def wrapper(*args, **kw):
        print('call %s():' % func.__name__)
        return func(*args, **kw)
    return wrapper
```

或者针对带参数的decorator：

```python
import functools

def log(text):
    def decorator(func):
        @functools.wraps(func)
        def wrapper(*args, **kw):
            print('%s %s():' % (text, func.__name__))
            return func(*args, **kw)
        return wrapper
    return decorator
```

`import functools`是导入`functools`模块。**现在，只需记住在定义`wrapper()`的前面加上`@functools.wraps(func)`即可。**

# @property

在绑定属性时，如果我们直接把属性暴露出去，虽然写起来很简单，但是，没办法检查参数，导致可以把成绩随便改：

```python
s = Student()
s.score = 9999
```

这显然不合逻辑。为了限制score的范围，可以通过一个`set_score()`方法来设置成绩，再通过一个`get_score()`来获取成绩，这样，在`set_score()`方法里，就可以检查参数

装饰器（decorator）可以给函数动态加上功能吗，对于类的方法，装饰器一样起作用。Python内置的`@property`装饰器就是负责把一个方法变成属性调用的：

```python
class Student(object):

    @property
    def score(self):
        return self._score

    @score.setter
    def score(self, value):
        if not isinstance(value, int):
            raise ValueError('score must be an integer!')
        if value < 0 or value > 100:
            raise ValueError('score must between 0 ~ 100!')
        self._score = value
```

`@property`的实现比较复杂，我们先考察如何使用。把一个getter方法变成属性，只需要加上`@property`就可以了，此时，`@property`本身又创建了另一个装饰器`@score.setter`，负责把一个setter方法变成属性赋值，于是，我们就拥有一个可控的属性操作：

```python
>>> s = Student()
>>> s.score = 60 # OK，实际转化为s.set_score(60)
>>> s.score # OK，实际转化为s.get_score()
60
>>> s.score = 9999
Traceback (most recent call last):
  ...
ValueError: score must between 0 ~ 100!
```

注意到这个神奇的`@property`，我们在对实例属性操作的时候，就知道该属性很可能不是直接暴露的，而是通过getter和setter方法来实现的。

还可以定义只读属性，只定义getter方法，不定义setter方法就是一个只读属性：

```python
class Student(object):

    @property
    def birth(self):
        return self._birth

    @birth.setter
    def birth(self, value):
        self._birth = value

    @property
    def age(self):
        return 2015 - self._birth
```

上面的`birth`是可读写属性，而`age`就是一个*只读*属性，因为`age`可以根据`birth`和当前时间计算出来。

# 枚举类

Python提供了`Enum`类来实现这个功能：

```python
from enum import Enum

Month = Enum('Month', ('Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'))
```

这样我们就获得了`Month`类型的枚举类，可以直接使用`Month.Jan`来引用一个常量，或者枚举它的所有成员：

```python
for name, member in Month.__members__.items():
    print(name, '=>', member, ',', member.value)
```

`value`属性则是自动赋给成员的`int`常量，默认从`1`开始计数。

如果需要更精确地控制枚举类型，可以从`Enum`派生出自定义类：

```python
from enum import Enum, unique

@unique
class Weekday(Enum):
    Sun = 0 # Sun的value被设定为0
    Mon = 1
    Tue = 2
    Wed = 3
    Thu = 4
    Fri = 5
    Sat = 6
```

@unique`装饰器可以帮助我们检查保证没有重复值。

既可以用成员名称引用枚举常量，又可以直接根据value的值获得枚举常量。

# 解包

```python
a = {"ross":"123456","xiaoming":"abc123"}
b = {"LiBel":"111111"," zhangsan":"12345678"}
c ={**a,**b} # {"ross":"123456","xiaoming":"abc123","LiBel":"111111"," zhangsan":"12345678"}
```

这里的**这2个星号叫做解包 unpacking

# reduce()

把一个整数列表拼成整数，如下

```python
>>> from functools import reduce
>>> reduce(lambda x, y: x * 10 + y, [1 , 2, 3, 4, 5])
12345
```

```python
scientists =({'name':'Alan Turing', 'age':105},
             {'name':'Dennis Ritchie', 'age':76},
             {'name':'John von Neumann', 'age':114},
             {'name':'Guido van Rossum', 'age':61})
def reducer(accumulator , value):
    sum = accumulator['age'] + value['age']
    return sum
total_age = reduce(reducer, scientists)
print(total_age)
```

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/ad8c014a0188d3d3b41d3d545bfcab4f.png)

```python
scientists =({'name':'Alan Turing', 'age':105, 'gender':'male'},
             {'name':'Dennis Ritchie', 'age':76, 'gender':'male'},
             {'name':'Ada Lovelace', 'age':202, 'gender':'female'},
             {'name':'Frances E. Allen', 'age':84, 'gender':'female'})
def reducer(accumulator , value):
    sum = accumulator + value['age']
    return sum
total_age = reduce(reducer, scientists, 0)
```

reduce 有三个参数， 第三个参数是初始值

修改之后流程如下

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/06ea5e55de79633a6c948e8dec9cbc27.png)

 

这个仍然也可以用 sum 来更简单的完成

```python
sum([x['age'] for x in scientists ])
```

做点更高级的事情，按性别分组

```python
scientists =({'name':'Alan Turing', 'age':105, 'gender':'male'},
             {'name':'Dennis Ritchie', 'age':76, 'gender':'male'},
             {'name':'Ada Lovelace', 'age':202, 'gender':'female'},
             {'name':'Frances E. Allen', 'age':84, 'gender':'female'})
def group_by_gender(accumulator , value):
    accumulator[value['gender']].append(value['name'])
    return accumulator
grouped = reduce(group_by_gender, scientists, {'male':[], 'female':[]})
```

输出

```
{'male': ['Alan Turing', 'Dennis Ritchie'], 'female': ['Ada Lovelace', 'Frances E. Allen']}
```

可以看到，在 reduce 的初始值参数传入了一个dictionary， 但是这样写 key 可能出错，还能再进一步自动化，运行时动态插入key，修改代码如下

```python
grouped = reduce(group_by_gender, scientists, collections.defaultdict(list))
```

这当然也能用 pythonic way 去解决

```python
scientists =({'name':'Alan Turing', 'age':105, 'gender':'male'},
             {'name':'Dennis Ritchie', 'age':76, 'gender':'male'},
             {'name':'Ada Lovelace', 'age':202, 'gender':'female'},
             {'name':'Frances E. Allen', 'age':84, 'gender':'female'})
grouped = {item[0]:list(item[1])
           for item in itertools.groupby(scientists, lambda x: x['gender'])}
```

# remove

当序列在循环中被修改时会有一个微妙的问题（这只可能发生于可变序列例如列表中）。 **会有一个内部计数器被用来跟踪下一个要使用的项，每次迭代都会使计数器递增。** 当计数器值达到序列长度时循环就会终止。 <u>这意味着如果语句体从序列中删除了当前（或之前）的一项，下一项就会被跳过（因为其标号将变成已被处理的当前项的标号）。 类似地，如果语句体在序列当前项的前面插入一个新项，当前项会在循环的下一轮中再次被处理。</u> 这会导致麻烦的程序错误，避免此问题的办法是对整个序列使用切片来创建一个临时**副本**，例如

```python
for x in a[:]:
    if x < 0: a.remove(x)
```

