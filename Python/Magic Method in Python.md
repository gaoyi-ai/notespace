---
title: Magic Method in Python
categories:
- Python
- Magic Method
tags:
- pythonic
date: 2021/2/21 20:00:14
updated: 2021/2/22 12:00:14
---

# Python中的魔术方法详解

## 构造和初始化

每个 Pythoner 都知道一个最基本的魔术方法， `__init__`。通过此方法我们可以定义一个对象的初始操作。然而，当调用 x = SomeClass() 的时候， `__init__`并不是第一个被调用的方法。实际上，还有一个叫做`__new__`的方法，两个共同构成了 “构造函数”。

`__new__`是用来创建类并返回这个类的实例, 而`__init__`只是将传入的参数来初始化该实例。

>- `object.``__new__`(*cls*[, *...*])
>
>    调用以创建一个 *cls* 类的新实例。[`__new__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__new__) 是一个静态方法 (因为是特例所以你不需要显式地声明)，它会将所请求实例所属的类作为第一个参数。其余的参数会被传递给对象构造器表达式 (对类的调用)。[`__new__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__new__) 的返回值应为新对象实例 (通常是 *cls* 的实例)。典型的实现会附带适宜的参数使用 `super().__new__(cls[, ...])`，通过超类的 [`__new__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__new__) 方法来创建一个类的新实例，然后根据需要修改新创建的实例再将其返回。如果 [`__new__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__new__) 在构造对象期间被发起调用并且它返回了一个实例或 *cls* 的子类，则新实例的 [`__init__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__init__) 方法将以 `__init__(self[, ...])` 的形式被发起调用，其中 *self* 为新实例而其余的参数与被传给对象构造器的参数相同。如果 [`__new__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__new__) 未返回一个 *cls* 的实例，则新实例的 [`__init__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__init__) 方法就不会被执行。[`__new__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__new__) 的目的主要是允许不可变类型的子类 (例如 int, str 或 tuple) 定制实例创建过程。它也常会在自定义元类中被重载以便定制类创建过程。
>
>- `object.``__init__`(*self*[, *...*])
>
>    在实例 (通过 [`__new__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__new__)) 被创建之后，返回调用者之前调用。其参数与传递给类构造器表达式的参数相同。一个基类如果有 [`__init__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__init__) 方法，则其所派生的类如果也有 [`__init__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__init__) 方法，就必须显式地调用它以确保实例基类部分的正确初始化；例如: `super().__init__([args...])`.因为对象是由 [`__new__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__new__) 和 [`__init__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__init__) 协作构造完成的 (由 [`__new__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__new__) 创建，并由 [`__init__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__init__) 定制)，所以 [`__init__()`](https://docs.python.org/zh-cn/3/reference/datamodel.html?highlight=__new__#object.__init__) 返回的值只能是 `None`，否则会在运行时引发 [`TypeError`](https://docs.python.org/zh-cn/3/library/exceptions.html#TypeError)。

在对象生命周期调用结束时，`__del__`方法会被调用，可以将`__del__`理解为 “构析函数”。下面通过代码的看一看这三个方法:

```python
class FileObject: 
    '''给文件对象进行包装从而确认在删除时文件流关闭'''
    
    def __init__(self, filepath='~', filename='sample.txt'): 
        #读写模式打开一个文件 
        self.file = open(join(filepath, filename), 'r+') 
    def __del__(self): 
        self.file.close() 
        del self.file
```

```python
class Singleobject:
	def __new__(cls):
		if not hasattr(cls,'instance'): #先判断是否已有实例，保证单例
			cls.instance = super(Singleobject,cls).__new__(cls)
		return cls.instance

obj1 = Singleobject()
obj2 = Singleobject()
assert obj1 is obj2 #True
```

## 控制属性访问

许多从其他语言转到 Python 的人会抱怨它缺乏类的真正封装。(没有办法定义私有变量，然后定义公共的 getter 和 setter)。Python 其实可以通过魔术方法来完成封装。我们来看一下:

```python
__getattr__(self, name):
```

定义当用户试图获取一个不存在的属性时的行为。这适用于对普通拼写错误的获取和重定向，对获取一些不建议的属性时候给出警告 (如果你愿意你也可以计算并且给出一个值) 或者处理一个 AttributeError 。只有当调用不存在的属性的时候会被返回。

```python
__setattr__(self, name, value):
```

与`__getattr__(self, name) `不同，`__setattr__`是一个封装的解决方案。无论属性是否存在，它都允许你定义对对属性的赋值行为，以为这你可以对属性的值进行个性定制。实现`__setattr__`时要避免 "无限递归" 的错误。

```python
__delattr__:
```

与`__setattr__`相同，但是功能是删除一个属性而不是设置他们。实现时也要防止无限递归现象发生。

```python
__getattribute__(self, name):
```

`__getattribute__`定义了你的属性被访问时的行为，相比较，__getattr__只有该属性不存在时才会起作用。因此，在支持__getattribute__的 Python 版本, 调用__getattr__前必定会调用 __getattribute__。__getattribute__同样要避免 "无限递归" 的错误。需要提醒的是，最好不要尝试去实现__getattribute__, 因为很少见到这种做法，而且很容易出 bug。

在进行属性访问控制定义的时候很可能会很容易引起 “无限递归”。如下面代码:

```python
#  错误用法 
def __setattr__(self, name, value): 
    self.name = value 
    # 每当属性被赋值的时候(如self.name = value)， ``__setattr__()`` 会被调用，这样就造成了递归调用。 
    # 这意味这会调用 ``self.__setattr__('name', value)`` ，每次方法会调用自己。这样会造成程序崩溃。 

#  正确用法 
def __setattr__(self, name, value): 
    self.__dict__[name] = value  # 给类中的属性名分配值 
    # 定制特有属性
```

Python 的魔术方法很强大，但是用时却需要慎之又慎，了解正确的使用方法非常重要。

## 创建自定义容器

有很多方法可以让你的 Python 类行为向内置容器类型一样，比如我们常用的 list、dict、tuple、string 等等。Python 的容器类型分为可变类型 (如 list、dict) 和不可变类型（如 string、tuple），可变容器和不可变容器的区别在于，不可变容器一旦赋值后，不可对其中的某个元素进行修改。

在讲创建自定义容器之前，应该先了解下协议。这里的协议跟其他语言中所谓的 "接口" 概念很像，它给你很多你必须定义的方法。然而在 Python 中的协议是很不正式的，不需要明确声明实现。事实上，他们更像一种指南。

### 自定义容器的 magic method

下面细致了解下定义容器可能用到的魔术方法。首先，实现不可变容器的话，你只能定义`__len__`和 `__getitem__ `。可变容器协议则需要所有不可变容器的所有，另外还需要 `__setitem__`和 `__delitem__ `。如果你希望你的对象是可迭代的话，你需要定义`__iter__`会返回一个迭代器。迭代器必须遵循迭代器协议，需要有`__iter_`(返回它本身) 和 `next`。

```python
__len__(self):
```

返回容器的长度。对于可变和不可变容器的协议，这都是其中的一部分。

```python
__getitem__(self, key):
```

定义当某一项被访问时，使用 self[key] 所产生的行为。这也是不可变容器和可变容器协议的一部分。如果键的类型错误将产生 TypeError；如果 key 没有合适的值则产生 KeyError。

```python
__setitem__(self, key, value):
```

当你执行 self[key] = value 时，调用的是该方法。

```python
__delitem__(self, key):
```

定义当一个项目被删除时的行为 (比如 del self[key])。这只是可变容器协议中的一部分。当使用一个无效的键时应该抛出适当的异常。

```python
__iter__(self):
```

返回一个容器迭代器，很多情况下会返回迭代器，尤其是当内置的 iter() 方法被调用的时候，以及当使用 for x in container: 方式循环的时候。迭代器是它们本身的对象，它们必须定义返回 self 的__iter__方法。

```python
__reversed__(self):
```

实现当 reversed() 被调用时的行为。应该返回序列反转后的版本。仅当序列可以是有序的时候实现它，例如对于列表或者元组。

```python
__contains__(self, item):
```

定义了调用 in 和 not in 来测试成员是否存在的时候所产生的行为。你可能会问为什么这个不是序列协议的一部分？因为当`__contains__`没有被定义的时候，如果没有定义，那么 Python 会迭代容器中的元素来一个一个比较，从而决定返回 True 或者 False。

```python
__missing__(self, key):
```

dict 字典类型会有该方法，它定义了 key 如果在容器中找不到时触发的行为。比如 `d = {'a': 1}`, 当你执行 `d[notexist]` 时，`d.__missing__['notexist']` 就会被调用。

### 实例

下面是书中的例子，用魔术方法来实现 Haskell 语言中的一个数据结构。

```python
class FunctionalList: 
    ''' 实现了内置类型list的功能,并丰富了一些其他方法: head, tail, init, last, drop, take'''
    def __init__(self, values=None): 
        if values is None: 
            self.values = [] 
        else: 
            self.values = values 
    def __len__(self): 
        return len(self.values) 
    def __getitem__(self, key): 
        return self.values[key] 
    def __setitem__(self, key, value): 
        self.values[key] = value 
    def __delitem__(self, key): 
        del self.values[key] 
    def __iter__(self): 
        return iter(self.values) 
    def __reversed__(self): 
        return FunctionalList(reversed(self.values)) 
    def append(self, value): 
        self.values.append(value) 
    def head(self): 
        # 获取第一个元素 
        return self.values[0] 
    def tail(self): 
        # 获取第一个元素之后的所有元素 
        return self.values[1:] 
    def init(self): 
        # 获取最后一个元素之前的所有元素 
        return self.values[:-1] 
    def last(self): 
        # 获取最后一个元素 
        return self.values[-1] 
    def drop(self, n): 
        # 获取所有元素，除了前N个 
        return self.values[n:] 
    def take(self, n): 
        # 获取前N个元素 
        return self.values[:n]
```

其实在 collections 模块中已经有了很多类似的实现，比如 Counter、OrderedDict 等等。

## 反射

你也可以控制怎么使用内置在函数`isinstance()`和 `issubclass()`方法反射定义魔术方法. 这个魔术方法是:

```python
__instancecheck__(self, instance):
```

检查一个实例是不是你定义的类的实例

```python
__subclasscheck__(self, subclass):
```

检查一个类是不是你定义的类的子类

这些魔术方法的用例看起来很小, 并且确实非常实用. 它们反应了关于面向对象程序上一些重要的东西在 Python 上, 并且总的来说 Python: 总是一个简单的方法去找某些事情, 即使是没有必要的. 这些魔法方法可能看起来不是很有用, 但是一旦你需要它们，你会感到庆幸它们的存在。

## 可调用的对象

你也许已经知道，在 Python 中，方法是最高级的对象。这意味着他们也可以被传递到方法中，就像其他对象一样。这是一个非常惊人的特性。

在 Python 中，一个特殊的魔术方法可以让类的实例的行为表现的像函数一样，你可以调用它们，将一个函数当做一个参数传到另外一个函数中等等。这是一个非常强大的特性，其让 Python 编程更加舒适甜美。

```python
__call__(self, [args...]):
```

允许一个类的实例像函数一样被调用。实质上说，这意味着 x() 与 `x.__call__()` 是相同的。注意 `__call__ `的参数可变。这意味着你可以定义 `__call__` 为其他你想要的函数，无论有多少个参数。

`__call__` 在那些类的实例经常改变状态的时候会非常有效。调用这个实例是一种改变这个对象状态的直接和优雅的做法。用一个实例来表达最好不过了:

```python
class Entity: 
    """ 
    调用实体来改变实体的位置 
    """
    def __init__(self, size, x, y): 
        self.x, self.y = x, y 
        self.size = size 
    def __call__(self, x, y): 
        """ 
        改变实体的位置 
        """
        self.x, self.y = x, y
```

## 上下文管理

在 with 声明的代码段中，我们可以做一些对象的开始操作和退出操作, 还能对异常进行处理。这需要实现两个魔术方法: `__enter__` 和 `__exit__`。

```python
__enter__(self):
```

定义了当使用 with 语句的时候，会话管理器在块被初始创建时要产生的行为。请注意，`__enter__`的返回值与 with 语句的目标或者 as 后的名字绑定。

```python
__exit__(self, exception_type, exception_value, traceback):
```

定义了当一个代码块被执行或者终止后，会话管理器应该做什么。它可以被用来处理异常、执行清理工作或做一些代码块执行完毕之后的日常工作。如果代码块执行成功，exception_type，exception_value，和 traceback 将会为 None。否则，你可以选择处理这个异常或者是直接交给用户处理。如果你想处理这个异常的话，请确保`__exit__`在所有语句结束之后返回 True。如果你想让异常被会话管理器处理的话，那么就让其产生该异常。

## 创建对象描述器

描述器是通过获取、设置以及删除的时候被访问的类。当然也可以改变其它的对象。描述器并不是独立的。相反，它意味着被一个所有者类持有。当创建面向对象的数据库或者类，里面含有相互依赖的属性时，描述器将会非常有用。一种典型的使用方法是用不同的单位表示同一个数值，或者表示某个数据的附加属性。

为了成为一个描述器，一个类必须至少有`__get__`，`__set__`，`__delete__`方法被实现：

```python
__get__(self, instance, owner):
```

定义了当描述器的值被取得的时候的行为。instance 是拥有该描述器对象的一个实例。owner 是拥有者本身

```python
__set__(self, instance, value):
```

定义了当描述器的值被改变的时候的行为。instance 是拥有该描述器类的一个实例。value 是要设置的值。

```python
__delete__(self, instance):
```

定义了当描述器的值被删除的时候的行为。instance 是拥有该描述器对象的一个实例。

下面是一个描述器的实例：单位转换。

```python
class Meter: 
    """ 
    对于单位"米"的描述器 
    """
    def __init__(self, value=0.0): 
        self.value = float(value) 
    def __get__(self, instance, owner): 
        return self.value 
    def __set__(self, instance, value): 
        self.value = float(value)

class Foot: 
    """ 
    对于单位"英尺"的描述器 
    """
    def __get__(self, instance, owner): 
        return instance.meter * 3.2808
    def __set__(self, instance, value): 
        instance.meter = float(value) / 3.2808
  
class Distance(object): 
    """ 
    用米和英寸来表示两个描述器之间的距离 
    """
    meter = Meter(10) 
    foot = Foot()

#d = Distance() 
#d.foot #32.808
#d.meter #10.0
```

## 复制

有时候，尤其是当你在处理可变对象时，你可能想要复制一个对象，然后对其做出一些改变而不希望影响原来的对象。这就是 Python 的 copy 所发挥作用的地方。

```python
__copy__(self):
```

定义了当对你的类的实例调用 copy.copy() 时所产生的行为。copy.copy() 返回了你的对象的一个浅拷贝——这意味着，当实例本身是一个新实例时，它的所有数据都被引用了——例如，当一个对象本身被复制了，它的数据仍然是被引用的（因此，对于浅拷贝中数据的更改仍然可能导致数据在原始对象的中的改变）。

```python
__deepcopy__(self, memodict={}):
```

定义了当对你的类的实例调用 copy.deepcopy() 时所产生的行为。copy.deepcopy() 返回了你的对象的一个深拷贝——对象和其数据都被拷贝了。memodict 是对之前被拷贝的对象的一个缓存——这优化了拷贝过程并且阻止了对递归数据结构拷贝时的无限递归。当你想要进行对一个单独的属性进行深拷贝时，调用 copy.deepcopy()，并以 memodict 为第一个参数。

> [Python中的魔术方法详解](https://www.cnblogs.com/pyxiaomangshe/p/7927540.html)