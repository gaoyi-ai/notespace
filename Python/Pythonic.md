---
title: Pythonic
categories:
- Python
tags:
- pythonic
date: 2019/8/1
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

# generator

```python
g = (x * x for x in range(10))
print(g) # <generator object <genexpr> at 0x00000234C6568840>
next(g) # 0
```

# dict -  map value to key

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

# Return self

Returning self from a method simply means that your method returns a reference to the instance object on which it was called. This can sometimes be seen in use with object oriented APIs that are designed as a fluent interface that encourages method cascading.
从方法返回self只是意味着您的方法返回对调用该方法的实例对象的引用。 有时可以将其与面向对象的API一起使用，这些对象被设计为鼓励方法级联的流畅接口。

# Static variables and methods in Python

> [Static variables and methods in Python (radek.io)](https://radek.io/2011/07/21/static-variables-and-methods-in-python/)

How to declare a data member or a method [static](https://en.wikipedia.org/wiki/Method_(computer_programming)#Static_methods) in Python? Static means, that the member is on a class level rather on the instance level. Static variables exist only on class level and aren't instantiated. If you change a static variable in one instance of the class, the change will affect its value in all other instances.

Static methods don't refer to any instance of the class and can be called outside of it. They also cannot access any non-static data members of the class for obvious reasons. Let's have a look how to get some static from Python.

## Variables

All variables defined on the class level in Python are considered static. See this example:

```python
class Example:
    staticVariable = 5 # Access through class

print Example.staticVariable # prints 5

# Access through an instance
instance = Example()
print instance.staticVariable # still 5

# Change within an instance
instance.staticVariable = 6
print instance.staticVariable # 6
print Example.staticVariable # 5

# Change through the class
class Example.staticVariable = 7
print instance.staticVariable # still 6
print Example.staticVariable # now 7
```

Seems pretty straight-forward. The only confusion might arise from the fact that you can have two different variables in your class with the same name (one static and one ordinary). I wouldn't recommend relying on that behaviour in your code.

## Methods

With static methods, it gets a little more complicated. In Python, there are two ways of defining static methods within a class.

### @staticmethod

A method decorated with this decorator only shares the namespace with the class. Note that no arguments are mandatory in the method definition. A static method defined this way can access the class' static variables. See the following example:

```python
class Example:
    name = "Example"

    @staticmethod
    def static():
        print "%s static() called" % Example.name

class Offspring1(Example):
    name = "Offspring1"

class Offspring2(Example):
    name = "Offspring2"

    @staticmethod
    def static():
        print "%s static() called" % Offspring2.name

Example.static() # prints Example
Offspring1.static() # prints Example
Offspring2.static() # prints Offspring2
```

### @classmethod

The difference between class method and static method in Python is, that class method recieves one mandatory argument - a class it was called from. Let's take a look:

```python
class Example:
    name = "Example"

    @classmethod
    def static(cls):
        print "%s static() called" % cls.name

class Offspring1(Example):
    name = "Offspring1"
    pass

class Offspring2(Example):
    name = "Offspring2"

    @classmethod
    def static(cls):
        print "%s static() called" % cls.name

Example.static()    # prints Example
Offspring1.static() # prints Offspring1
Offspring2.static() # prints Offspring2
```

Which one should you use? The first option allows you to only access the static variables in the same class. With the second approach, you'll be able to modify class variables of the subclasses without the neccessity of redefining the method when using inheritance.

Personally, I prefer the first variant because I think it's a little cleaner, but the second variant might come useful in certain situations as well.

## Sources

- [Static vs. Class method @ RAPD](https://rapd.wordpress.com/2008/07/02/python-staticmethod-vs-classmethod/)
- [Static variable @ stack overflow](https://stackoverflow.com/questions/68645/static-class-variables-in-python)
