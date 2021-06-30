---
title: What are metaclasses in Python?
categories:
- Python
tags:
- metaclass
date: 2021/6/30
---



# [What are metaclasses in Python?](https://stackoverflow.com/questions/100003/what-are-metaclasses-in-python)

A metaclass is the class of a class. A class defines how an instance of the class (i.e. an object) behaves while a metaclass defines how a class behaves. A class is an instance of a metaclass.

While in Python you can use arbitrary callables for metaclasses (like [Jerub](https://stackoverflow.com/questions/100003/what-is-a-metaclass-in-python/100037#100037) shows), the better approach is to make it an actual class itself. `type` is the usual metaclass in Python. `type` is itself a class, and it is its own type. You won't be able to recreate something like `type` purely in Python, but Python cheats a little. To create your own metaclass in Python you really just want to subclass `type`.

A metaclass is most commonly used as a class-factory. When you create an object by calling the class, Python creates a new class (when it executes the 'class' statement) by calling the metaclass. Combined with the normal `__init__` and `__new__` methods, metaclasses therefore allow you to do 'extra things' when creating a class, like registering the new class with some registry or replace the class with something else entirely.

When the `class` statement is executed, Python first executes the body of the `class` statement as a normal block of code. The resulting namespace (a dict) holds the attributes of the class-to-be. The metaclass is determined by looking at the baseclasses of the class-to-be (metaclasses are inherited), at the `__metaclass__` attribute of the class-to-be (if any) or the `__metaclass__` global variable. The metaclass is then called with the name, bases and attributes of the class to instantiate it.

However, metaclasses actually define the *type* of a class, not just a factory for it, so you can do much more with them. You can, for instance, define normal methods on the metaclass. These metaclass-methods are like classmethods in that they can be called on the class without an instance, but they are also not like classmethods in that they cannot be called on an instance of the class. `type.__subclasses__()` is an example of a method on the `type` metaclass. You can also define the normal 'magic' methods, like `__add__`, `__iter__` and `__getattr__`, to implement or change how the class behaves.

Here's an aggregated example of the bits and pieces:

```py
def make_hook(f):
    """Decorator to turn 'foo' method into '__foo__'"""
    f.is_hook = 1
    return f

class MyType(type):
    def __new__(mcls, name, bases, attrs):

        if name.startswith('None'):
            return None

        # Go over attributes and see if they should be renamed.
        newattrs = {}
        for attrname, attrvalue in attrs.iteritems():
            if getattr(attrvalue, 'is_hook', 0):
                newattrs['__%s__' % attrname] = attrvalue
            else:
                newattrs[attrname] = attrvalue

        return super(MyType, mcls).__new__(mcls, name, bases, newattrs)

    def __init__(self, name, bases, attrs):
        super(MyType, self).__init__(name, bases, attrs)

        # classregistry.register(self, self.interfaces)
        print "Would register class %s now." % self

    def __add__(self, other):
        class AutoClass(self, other):
            pass
        return AutoClass
        # Alternatively, to autogenerate the classname as well as the class:
        # return type(self.__name__ + other.__name__, (self, other), {})

    def unregister(self):
        # classregistry.unregister(self)
        print "Would unregister class %s now." % self

class MyObject:
    __metaclass__ = MyType


class NoneSample(MyObject):
    pass

# Will print "NoneType None"
print type(NoneSample), repr(NoneSample)

class Example(MyObject):
    def __init__(self, value):
        self.value = value
    @make_hook
    def add(self, other):
        return self.__class__(self.value + other.value)

# Will unregister the class
Example.unregister()

inst = Example(10)
# Will fail with an AttributeError
#inst.unregister()

print inst + inst
class Sibling(MyObject):
    pass

ExampleSibling = Example + Sibling
# ExampleSibling is now a subclass of both Example and Sibling (with no
# content of its own) although it will believe it's called 'AutoClass'
print ExampleSibling
print ExampleSibling.__mro__
```



[Share](https://stackoverflow.com/a/100146)

[Improve this answer](https://stackoverflow.com/posts/100146/edit)

Follow

[edited Mar 4 '19 at 21:34](https://stackoverflow.com/posts/100146/revisions)

[![img](https://lh3.googleusercontent.com/-SHeXKTgoA5E/AAAAAAAAAAI/AAAAAAAADqc/mQZAIP_L20U/photo.jpg?sz=32)](https://stackoverflow.com/users/11150495/cameron-savage)

[Cameron Savage](https://stackoverflow.com/users/11150495/cameron-savage)

**7**22 bronze badges

answered Sep 19 '08 at 7:01

[![img](https://www.gravatar.com/avatar/90285a816eda046ff55a0dcacc0c5ee8?s=32&d=identicon&r=PG)](https://stackoverflow.com/users/17624/thomas-wouters)

[Thomas Wouters](https://stackoverflow.com/users/17624/thomas-wouters)

**119k**2121 gold badges139139 silver badges118118 bronze badges

- 17

    `class A(type):pass<NEWLINE>class B(type,metaclass=A):pass<NEWLINE>b.__class__ = b` – [pppery](https://stackoverflow.com/users/3750257/pppery) [Aug 3 '17 at 14:34](https://stackoverflow.com/questions/100003/what-are-metaclasses-in-python/100037#comment77935564_100146)

- 29

    ppperry he obviously meant you can't recreate type without using type itself as a metaclass. Which is fair enough to say. – [Holle van](https://stackoverflow.com/users/7513138/holle-van) [Sep 18 '18 at 23:24](https://stackoverflow.com/questions/100003/what-are-metaclasses-in-python/100037#comment91736655_100146)

- 4

    Shouldn't unregister() be called by instance of Example class ? – [Ciasto piekarz](https://stackoverflow.com/users/3311276/ciasto-piekarz) [Nov 29 '18 at 0:59](https://stackoverflow.com/questions/100003/what-are-metaclasses-in-python/100037#comment93929134_100146)

- 15

    Note that `__metaclass__` is not supported in Python 3. In Python 3 use `class MyObject(metaclass=MyType)`, see [python.org/dev/peps/pep-3115](https://www.python.org/dev/peps/pep-3115/) and the answer below. – [BlackShift](https://stackoverflow.com/users/2097/blackshift) [May 1 '19 at 8:36](https://stackoverflow.com/questions/100003/what-are-metaclasses-in-python/100037#comment98519860_100146)

- 3

    The documentation describes [how the metaclass is chosen](https://docs.python.org/3/reference/datamodel.html#determining-the-appropriate-metaclass). The metaclass isn't inherited so much as it is derived. If you specify a metaclass, it has to be a subtype of each base class metaclass; otherwise, you'll use the a base class metaclass that is a subtype of each other base class metaclass. Note that it is possible that *no* valid metaclass can be found, and the definition will fail. – [chepner](https://stackoverflow.com/users/1126841/chepner) [Jan 9 '20 at 21:14](https://stackoverflow.com/questions/100003/what-are-metaclasses-in-python/100037#comment105503046_100146)

[Show **5** more comments](https://stackoverflow.com/questions/100003/what-are-metaclasses-in-python/100037#)





7308



+250



# Classes as objects

Before understanding metaclasses, you need to master classes in Python. And Python has a very peculiar idea of what classes are, borrowed from the Smalltalk language.

In most languages, classes are just pieces of code that describe how to produce an object. That's kinda true in Python too:

```py
>>> class ObjectCreator(object):
...       pass
...

>>> my_object = ObjectCreator()
>>> print(my_object)
<__main__.ObjectCreator object at 0x8974f2c>
```

But classes are more than that in Python. Classes are objects too.

Yes, objects.

As soon as you use the keyword `class`, Python executes it and creates an OBJECT. The instruction

```py
>>> class ObjectCreator(object):
...       pass
...
```

creates in memory an object with the name "ObjectCreator".

**This object (the class) is itself capable of creating objects (the instances), and this is why it's a class**.

But still, it's an object, and therefore:

- you can assign it to a variable
- you can copy it
- you can add attributes to it
- you can pass it as a function parameter

e.g.:

```py
>>> print(ObjectCreator) # you can print a class because it's an object
<class '__main__.ObjectCreator'>
>>> def echo(o):
...       print(o)
...
>>> echo(ObjectCreator) # you can pass a class as a parameter
<class '__main__.ObjectCreator'>
>>> print(hasattr(ObjectCreator, 'new_attribute'))
False
>>> ObjectCreator.new_attribute = 'foo' # you can add attributes to a class
>>> print(hasattr(ObjectCreator, 'new_attribute'))
True
>>> print(ObjectCreator.new_attribute)
foo
>>> ObjectCreatorMirror = ObjectCreator # you can assign a class to a variable
>>> print(ObjectCreatorMirror.new_attribute)
foo
>>> print(ObjectCreatorMirror())
<__main__.ObjectCreator object at 0x8997b4c>
```

# Creating classes dynamically

Since classes are objects, you can create them on the fly, like any object.

First, you can create a class in a function using `class`:

```py
>>> def choose_class(name):
...     if name == 'foo':
...         class Foo(object):
...             pass
...         return Foo # return the class, not an instance
...     else:
...         class Bar(object):
...             pass
...         return Bar
...
>>> MyClass = choose_class('foo')
>>> print(MyClass) # the function returns a class, not an instance
<class '__main__.Foo'>
>>> print(MyClass()) # you can create an object from this class
<__main__.Foo object at 0x89c6d4c>
```

But it's not so dynamic, since you still have to write the whole class yourself.

Since classes are objects, they must be generated by something.

When you use the `class` keyword, Python creates this object automatically. But as with most things in Python, it gives you a way to do it manually.

Remember the function `type`? The good old function that lets you know what type an object is:

```py
>>> print(type(1))
<type 'int'>
>>> print(type("1"))
<type 'str'>
>>> print(type(ObjectCreator))
<type 'type'>
>>> print(type(ObjectCreator()))
<class '__main__.ObjectCreator'>
```

Well, [`type`](http://docs.python.org/2/library/functions.html#type) has a completely different ability, it can also create classes on the fly. `type` can take the description of a class as parameters, and return a class.

(I know, it's silly that the same function can have two completely different uses according to the parameters you pass to it. It's an issue due to backward compatibility in Python)

`type` works this way:

```py
type(name, bases, attrs)
```

Where:

- **`name`**: name of the class
- **`bases`**: tuple of the parent class (for inheritance, can be empty)
- **`attrs`**: dictionary containing attributes names and values

e.g.:

```py
>>> class MyShinyClass(object):
...       pass
```

can be created manually this way:

```py
>>> MyShinyClass = type('MyShinyClass', (), {}) # returns a class object
>>> print(MyShinyClass)
<class '__main__.MyShinyClass'>
>>> print(MyShinyClass()) # create an instance with the class
<__main__.MyShinyClass object at 0x8997cec>
```

You'll notice that we use "MyShinyClass" as the name of the class and as the variable to hold the class reference. They can be different, but there is no reason to complicate things.

`type` accepts a dictionary to define the attributes of the class. So:

```py
>>> class Foo(object):
...       bar = True
```

Can be translated to:

```py
>>> Foo = type('Foo', (), {'bar':True})
```

And used as a normal class:

```py
>>> print(Foo)
<class '__main__.Foo'>
>>> print(Foo.bar)
True
>>> f = Foo()
>>> print(f)
<__main__.Foo object at 0x8a9b84c>
>>> print(f.bar)
True
```

And of course, you can inherit from it, so:

```py
>>>   class FooChild(Foo):
...         pass
```

would be:

```py
>>> FooChild = type('FooChild', (Foo,), {})
>>> print(FooChild)
<class '__main__.FooChild'>
>>> print(FooChild.bar) # bar is inherited from Foo
True
```

Eventually, you'll want to add methods to your class. Just define a function with the proper signature and assign it as an attribute.

```py
>>> def echo_bar(self):
...       print(self.bar)
...
>>> FooChild = type('FooChild', (Foo,), {'echo_bar': echo_bar})
>>> hasattr(Foo, 'echo_bar')
False
>>> hasattr(FooChild, 'echo_bar')
True
>>> my_foo = FooChild()
>>> my_foo.echo_bar()
True
```

And you can add even more methods after you dynamically create the class, just like adding methods to a normally created class object.

```py
>>> def echo_bar_more(self):
...       print('yet another method')
...
>>> FooChild.echo_bar_more = echo_bar_more
>>> hasattr(FooChild, 'echo_bar_more')
True
```

You see where we are going: in Python, classes are objects, and you can create a class on the fly, dynamically.

This is what Python does when you use the keyword `class`, and it does so by using a metaclass.

# What are metaclasses (finally)

Metaclasses are the 'stuff' that creates classes.

You define classes in order to create objects, right?

But we learned that Python classes are objects.

Well, metaclasses are what create these objects. They are the classes' classes, you can picture them this way:

```py
MyClass = MetaClass()
my_object = MyClass()
```

You've seen that `type` lets you do something like this:

```py
MyClass = type('MyClass', (), {})
```

It's because the function `type` is in fact a metaclass. `type` is the metaclass Python uses to create all classes behind the scenes.

Now you wonder why the heck is it written in lowercase, and not `Type`?

Well, I guess it's a matter of consistency with `str`, the class that creates strings objects, and `int` the class that creates integer objects. `type` is just the class that creates class objects.

You see that by checking the `__class__` attribute.

Everything, and I mean everything, is an object in Python. That includes ints, strings, functions and classes. All of them are objects. And all of them have been created from a class:

```py
>>> age = 35
>>> age.__class__
<type 'int'>
>>> name = 'bob'
>>> name.__class__
<type 'str'>
>>> def foo(): pass
>>> foo.__class__
<type 'function'>
>>> class Bar(object): pass
>>> b = Bar()
>>> b.__class__
<class '__main__.Bar'>
```

Now, what is the `__class__` of any `__class__` ?

```py
>>> age.__class__.__class__
<type 'type'>
>>> name.__class__.__class__
<type 'type'>
>>> foo.__class__.__class__
<type 'type'>
>>> b.__class__.__class__
<type 'type'>
```

So, a metaclass is just the stuff that creates class objects.

You can call it a 'class factory' if you wish.

`type` is the built-in metaclass Python uses, but of course, you can create your own metaclass.

# The [`__metaclass__`](http://docs.python.org/2/reference/datamodel.html?highlight=__metaclass__#__metaclass__) attribute

In Python 2, you can add a `__metaclass__` attribute when you write a class (see next section for the Python 3 syntax):

```py
class Foo(object):
    __metaclass__ = something...
    [...]
```

If you do so, Python will use the metaclass to create the class `Foo`.

Careful, it's tricky.

You write `class Foo(object)` first, but the class object `Foo` is not created in memory yet.

Python will look for `__metaclass__` in the class definition. If it finds it, it will use it to create the object class `Foo`. If it doesn't, it will use `type` to create the class.

Read that several times.

When you do:

```py
class Foo(Bar):
    pass
```

Python does the following:

Is there a `__metaclass__` attribute in `Foo`?

If yes, create in-memory a class object (I said a class object, stay with me here), with the name `Foo` by using what is in `__metaclass__`.

If Python can't find `__metaclass__`, it will look for a `__metaclass__` at the MODULE level, and try to do the same (but only for classes that don't inherit anything, basically old-style classes).

Then if it can't find any `__metaclass__` at all, it will use the `Bar`'s (the first parent) own metaclass (which might be the default `type`) to create the class object.

Be careful here that the `__metaclass__` attribute will not be inherited, the metaclass of the parent (`Bar.__class__`) will be. If `Bar` used a `__metaclass__` attribute that created `Bar` with `type()` (and not `type.__new__()`), the subclasses will not inherit that behavior.

Now the big question is, what can you put in `__metaclass__`?

The answer is something that can create a class.

And what can create a class? `type`, or anything that subclasses or uses it.

# Metaclasses in Python 3

The syntax to set the metaclass has been changed in Python 3:

```py
class Foo(object, metaclass=something):
    ...
```

i.e. the `__metaclass__` attribute is no longer used, in favor of a keyword argument in the list of base classes.

The behavior of metaclasses however stays [largely the same](https://www.python.org/dev/peps/pep-3115/).

One thing added to metaclasses in Python 3 is that you can also pass attributes as keyword-arguments into a metaclass, like so:

```py
class Foo(object, metaclass=something, kwarg1=value1, kwarg2=value2):
    ...
```

Read the section below for how python handles this.

# Custom metaclasses

The main purpose of a metaclass is to change the class automatically, when it's created.

You usually do this for APIs, where you want to create classes matching the current context.

Imagine a stupid example, where you decide that all classes in your module should have their attributes written in uppercase. There are several ways to do this, but one way is to set `__metaclass__` at the module level.

This way, all classes of this module will be created using this metaclass, and we just have to tell the metaclass to turn all attributes to uppercase.

Luckily, `__metaclass__` can actually be any callable, it doesn't need to be a formal class (I know, something with 'class' in its name doesn't need to be a class, go figure... but it's helpful).

So we will start with a simple example, by using a function.

```py
# the metaclass will automatically get passed the same argument
# that you usually pass to `type`
def upper_attr(future_class_name, future_class_parents, future_class_attrs):
    """
      Return a class object, with the list of its attribute turned
      into uppercase.
    """
    # pick up any attribute that doesn't start with '__' and uppercase it
    uppercase_attrs = {
        attr if attr.startswith("__") else attr.upper(): v
        for attr, v in future_class_attrs.items()
    }

    # let `type` do the class creation
    return type(future_class_name, future_class_parents, uppercase_attrs)

__metaclass__ = upper_attr # this will affect all classes in the module

class Foo(): # global __metaclass__ won't work with "object" though
    # but we can define __metaclass__ here instead to affect only this class
    # and this will work with "object" children
    bar = 'bip'
```

Let's check:

```py
>>> hasattr(Foo, 'bar')
False
>>> hasattr(Foo, 'BAR')
True
>>> Foo.BAR
'bip'
```

Now, let's do exactly the same, but using a real class for a metaclass:

```py
# remember that `type` is actually a class like `str` and `int`
# so you can inherit from it
class UpperAttrMetaclass(type):
    # __new__ is the method called before __init__
    # it's the method that creates the object and returns it
    # while __init__ just initializes the object passed as parameter
    # you rarely use __new__, except when you want to control how the object
    # is created.
    # here the created object is the class, and we want to customize it
    # so we override __new__
    # you can do some stuff in __init__ too if you wish
    # some advanced use involves overriding __call__ as well, but we won't
    # see this
    def __new__(upperattr_metaclass, future_class_name,
                future_class_parents, future_class_attrs):
        uppercase_attrs = {
            attr if attr.startswith("__") else attr.upper(): v
            for attr, v in future_class_attrs.items()
        }
        return type(future_class_name, future_class_parents, uppercase_attrs)
```

Let's rewrite the above, but with shorter and more realistic variable names now that we know what they mean:

```py
class UpperAttrMetaclass(type):
    def __new__(cls, clsname, bases, attrs):
        uppercase_attrs = {
            attr if attr.startswith("__") else attr.upper(): v
            for attr, v in attrs.items()
        }
        return type(clsname, bases, uppercase_attrs)
```

You may have noticed the extra argument `cls`. There is nothing special about it: `__new__` always receives the class it's defined in, as the first parameter. Just like you have `self` for ordinary methods which receive the instance as the first parameter, or the defining class for class methods.

But this is not proper OOP. We are calling `type` directly and we aren't overriding or calling the parent's `__new__`. Let's do that instead:

```py
class UpperAttrMetaclass(type):
    def __new__(cls, clsname, bases, attrs):
        uppercase_attrs = {
            attr if attr.startswith("__") else attr.upper(): v
            for attr, v in attrs.items()
        }
        return type.__new__(cls, clsname, bases, uppercase_attrs)
```

We can make it even cleaner by using `super`, which will ease inheritance (because yes, you can have metaclasses, inheriting from metaclasses, inheriting from type):

```py
class UpperAttrMetaclass(type):
    def __new__(cls, clsname, bases, attrs):
        uppercase_attrs = {
            attr if attr.startswith("__") else attr.upper(): v
            for attr, v in attrs.items()
        }
        return super(UpperAttrMetaclass, cls).__new__(
            cls, clsname, bases, uppercase_attrs)
```

Oh, and in python 3 if you do this call with keyword arguments, like this:

```py
class Foo(object, metaclass=MyMetaclass, kwarg1=value1):
    ...
```

It translates to this in the metaclass to use it:

```py
class MyMetaclass(type):
    def __new__(cls, clsname, bases, dct, kwargs1=default):
        ...
```

That's it. There is really nothing more about metaclasses.

The reason behind the complexity of the code using metaclasses is not because of metaclasses, it's because you usually use metaclasses to do twisted stuff relying on introspection, manipulating inheritance, vars such as `__dict__`, etc.

Indeed, metaclasses are especially useful to do black magic, and therefore complicated stuff. But by themselves, they are simple:

- intercept a class creation
- modify the class
- return the modified class

# Why would you use metaclasses classes instead of functions?

Since `__metaclass__` can accept any callable, why would you use a class since it's obviously more complicated?

There are several reasons to do so:

- The intention is clear. When you read `UpperAttrMetaclass(type)`, you know what's going to follow
- You can use OOP. Metaclass can inherit from metaclass, override parent methods. Metaclasses can even use metaclasses.
- Subclasses of a class will be instances of its metaclass if you specified a metaclass-class, but not with a metaclass-function.
- You can structure your code better. You never use metaclasses for something as trivial as the above example. It's usually for something complicated. Having the ability to make several methods and group them in one class is very useful to make the code easier to read.
- You can hook on `__new__`, `__init__` and `__call__`. Which will allow you to do different stuff, Even if usually you can do it all in `__new__`, some people are just more comfortable using `__init__`.
- These are called metaclasses, damn it! It must mean something!

# Why would you use metaclasses?

Now the big question. Why would you use some obscure error-prone feature?

Well, usually you don't:

> Metaclasses are deeper magic that 99% of users should never worry about it. If you wonder whether you need them, you don't (the people who actually need them to know with certainty that they need them and don't need an explanation about why).

*Python Guru Tim Peters*

The main use case for a metaclass is creating an API. A typical example of this is the Django ORM. It allows you to define something like this:

```py
class Person(models.Model):
    name = models.CharField(max_length=30)
    age = models.IntegerField()
```

But if you do this:

```py
person = Person(name='bob', age='35')
print(person.age)
```

It won't return an `IntegerField` object. It will return an `int`, and can even take it directly from the database.

This is possible because `models.Model` defines `__metaclass__` and it uses some magic that will turn the `Person` you just defined with simple statements into a complex hook to a database field.

Django makes something complex look simple by exposing a simple API and using metaclasses, recreating code from this API to do the real job behind the scenes.

# The last word

First, you know that classes are objects that can create instances.

Well, in fact, classes are themselves instances. Of metaclasses.

```py
>>> class Foo(object): pass
>>> id(Foo)
142630324
```

Everything is an object in Python, and they are all either instance of classes or instances of metaclasses.

Except for `type`.

`type` is actually its own metaclass. This is not something you could reproduce in pure Python, and is done by cheating a little bit at the implementation level.

Secondly, metaclasses are complicated. You may not want to use them for very simple class alterations. You can change classes by using two different techniques:

- [monkey patching](http://en.wikipedia.org/wiki/Monkey_patch)
- class decorators

99% of the time you need class alteration, you are better off using these.

But 98% of the time, you don't need class alteration at all.