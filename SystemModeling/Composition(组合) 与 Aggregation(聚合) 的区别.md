---
title: Composition & Aggregation
categories:
- System Modeling
- UML
tags:
- Composition
- Aggregation
date: 2021/5/10 18:50:45
updated: 2021/5/10 20:00:13
---

> [blog.csdn.net](https://blog.csdn.net/simonezhlx/article/details/8855297)

1. 若论两种关系表示的强弱程度，Composition 应该更强一些，这也是为什么在图中会以一个实心菱形来代表。反之，聚合使用的是空心菱形。见下图.

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20130426171010963)  

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20130426171100149)  

2. Composition 表示的是'Part-of'的关系， 以图 1 为例，Engine 是 Car 的一部分。脱离 Car 的 Engine 是没有实在意义的；而 Aggregation 表示的是'Has-a'的关系，以图 2 为例，Person 有一个 Address，但是 Addess 的存在是不依赖 Person 的，换句话说，地址本身就有其独立存在的意义，有没有人都是没有关系的。

3. 设计最终还是要落实到代码的，所以我们用代码来再次对比一下二者的区别。此处，我们用 Engine 与 Address 不同的生命周期来理解两种关系的区别。

```
public class Engine
{
 . . . 
}
 
public class Car
{
    Engine e = new Engine();
    .......
}
```

上面的是 Compostion， 下面的是 Aggregation:

```
public class Address
{
 . . .
}
 
public class Person
{
     private Address address;
     public Person(Address address)
     {
         this.address = address;
     }
     . . .
}
```

从两个代码片段中，我们不难看出 Engine 的生命周期是与 Car 一致的，何时 Car 被回收了，那 Engine 也就不存在了。但是反观 Address，它是在 Person 之外创建的，所以即使 Person 被回收了，Address 也不一定马上也会回收。（取决于是否有指向它的其它链接）