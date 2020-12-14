```
title: Java Immutable
categories:
- Java
tags:
- final
- immutable
date: 2020-12-14 12:08:09
updated: 2020/12/14 12:10:00
```

# Java Immutable

注意，final 不是 immutable！

推荐使用 final 关键字来明确表示代码的语义、逻辑意图，这已经被证明在很多场景下是非常好的实践，比如：

- 可以将方法或者类声明为 final，这样就可以明确告知别人，这些行为是不许修改的。

Java 核心类库的定义或源码，  java.lang 包下面的很多类，相当一部分都被声明成为 final class？在第三方类库的一些基础类中同样如此，这可以有效避免 API 使用者更改基础功能，某种程度上，这是保证平台安全的必要手段。

- 使用 final 修饰参数或者变量，也可以清楚地避免意外赋值导致的编程错误，甚至，有人明确推荐将所有方法参数、本地变量、成员变量声明成 final。
- final 变量产生了某种程度的不可变（immutable）的效果，所以，可以用于保护只读数据，尤其是在并发编程中，因为明确地不能再赋值 final 变量，有利于减少额外的同步开销，也可以省去一些防御性拷贝的必要。

final 在实践中的益处，需要注意的是，**final 并不等同于 immutable**，比如下面这段代码：

```java
 final List<String> strList = new ArrayList<>();
 strList.add("Hello");
 strList.add("world");  

 List<String> unmodifiableStrList = List.of("hello", "world");
 unmodifiableStrList.add("again"); // Exception
```

final 只能约束 strList 这个引用不可以被赋值，但是 strList 对象行为不被 final 影响，添加元素等操作是完全正常的。如果我们真的希望对象本身是不可变的，那么需要相应的类支持不可变的行为。在上面这个例子中，[List.of 方法](http://openjdk.java.net/jeps/269)创建的本身就是不可变 List，最后那句 add 是会在运行时抛出异常的。

Immutable 在很多场景是非常棒的选择，某种意义上说，Java 语言目前并没有原生的不可变支持，如果要实现 immutable 的类，我们需要做到：

- 将 class 自身声明为 final，这样别人就不能扩展来绕过限制了。
- 将所有成员变量定义为 private 和 final，并且不要实现 setter 方法。
- 通常构造对象时，成员变量使用深度拷贝来初始化，而不是直接赋值，这是一种防御措施，因为你无法确定输入对象不被其他人修改。
- 如果确实需要实现 getter 方法，或者其他可能会返回内部状态的方法，使用 copy-on-write 原则，创建私有的 copy。

这些原则是不是在并发编程实践中经常被提到？的确如此。