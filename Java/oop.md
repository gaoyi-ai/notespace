---
title: Interface_OOP
date: 2020-12-14 19:27:52
categories:
- Java
tags:
- oop
- interface
updated: 2020/12/14 20:00:13
---

# Interface

## Marker Interface

有一类没有任何方法的接口，通常叫作 Marker Interface，顾名思义，它的目的就是为了声明某些东西，比如 Cloneable、Serializable 等。

从表面看，这似乎和 Annotation 异曲同工，也确实如此，它的好处是简单直接。对于 Annotation，因为可以指定参数和值，在表达能力上要更强大一些，所以更多人选择使用 Annotation。

## Functional Interface

Java 8 增加了函数式编程的支持，即 functional interface，简单说就是只有一个抽象方法的接口，通常建议使用 @FunctionalInterface Annotation 来标记。Lambda 表达式本身可以看作是一类 functional interface。 Runnable、Callable 之类，都是 functional interface

# OOP

S.O.L.I.D 原则。

- 单一职责（Single Responsibility），类或者对象最好是只有单一职责，在程序设计中如果发现某个类承担着多种义务，可以考虑进行拆分。
- 开关原则（Open-Close, Open for extension, close for modification），设计要对扩展开放，对修改关闭。换句话说，程序设计应保证平滑的扩展性，尽量避免因为新增同类功能而修改已有实现，这样可以少产出些回归（regression）问题。
- 里氏替换（Liskov Substitution），这是面向对象的基本要素之一，进行继承关系抽象时，凡是可以用父类或者基类的地方，都可以用子类替换。
- 接口分离（Interface Segregation），在进行类和接口设计时，如果在一个接口里定义了太多方法，其子类很可能面临两难，就是只有部分方法对它是有意义的，这就破坏了程序的内聚性。
    对于这种情况，可以通过拆分成功能单一的多个接口，将行为进行解耦。在未来维护中，如果某个接口设计有变，不会对使用其他接口的子类构成影响。
- 依赖反转（Dependency Inversion），实体应该依赖于抽象而不是实现。也就是说高层次模块，不应该依赖于低层次模块，而是应该基于抽象。实践这一原则是保证产品代码之间适当耦合度的法宝。

**OOP 原则在面试题目中的分析**

开关原则（Open-Close）。看看下面这段代码，可以利用面向对象设计原则如何改进？

```java
public class VIPCenter {
  void serviceVIP(T extend User user>) {
     if (user instanceof SlumVIP) {
        // do somthing
      } else if(user instanceof RealVIP) {
        // do somthing
      }
      // ...
  }
```

这段代码的一个问题是，业务逻辑集中在一起，当出现新的用户类型时，就需要直接去修改服务方法代码实现，这可能会意外影响不相关的某个用户类型逻辑。

利用开关原则，可以尝试改造为下面的代码：

```java
public class VIPCenter {
   private Map<User.TYPE, ServiceProvider> providers;
   void serviceVIP(T extend User user） {
      providers.get(user.getType()).service(user);
   }
 }
 interface ServiceProvider{
   void service(T extend User user) ;
 }
 class SlumDogVIPServiceProvider implements ServiceProvider{
   void service(T extend User user){
     // do somthing
   }
 }
 class RealVIPServiceProvider implements ServiceProvider{
   void service(T extend User user) {
     // do something
   }
 } 
```

上面的示例，将不同对象分类的服务方法进行抽象，把业务逻辑的紧耦合关系拆开，实现代码的隔离保证了方便的扩展。