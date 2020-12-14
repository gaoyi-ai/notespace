---
title: Pattern
date: 2020-12-14 21:06:38
categories:
- SE
- Pattern
tags:
- pattern
updated: 2020/12/14 22:00:13
---

# Pattern

## Builder

创建型模式尤其是工厂模式，在代码中随处可见，比如，JDK 最新版本中 HTTP/2 Client API，下面这个创建 HttpRequest 的过程，就是典型的构建器模式（Builder），通常会被实现成[fluent 风格](https://en.wikipedia.org/wiki/Fluent_interface)的 API，也有人叫它方法链。

```java
HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                     .header(headerAlice, valueAlice)
                     .headers(headerBob, value1Bob,
                      headerCarl, valueCarl,
                      headerBob, value2Bob)
                     .GET()
                     .build();
```

使用构建器模式，可以比较优雅地解决构建复杂对象的麻烦，这里的“复杂”是指类似需要输入的参数组合较多，如果用构造函数，往往需要为每一种可能的输入参数组合实现相应的构造函数，一系列复杂的构造函数会让代码阅读性和可维护性变得很差。

上面的分析也进一步反映了创建型模式的初衷，即，将对象创建过程单独抽象出来，从结构上把对象使用逻辑和创建逻辑相互独立，隐藏对象实例的细节，进而为使用者实现了更加规范、统一的逻辑。

## Decorator

InputStream 是一个抽象类，标准类库中提供了 FileInputStream、ByteArrayInputStream 等各种不同的子类，分别从不同角度对 InputStream 进行了功能扩展，这是典型的装饰器模式应用案例。

识别装饰器模式，可以通过**识别类设计特征**来进行判断，也就是其类构造函数以**相同的**抽象类或者接口为输入参数。

因为装饰器模式本质上是包装同类型实例，对目标对象的调用，往往会通过包装类覆盖过的方法，迂回调用被包装的实例，这就可以很自然地实现增加额外逻辑的目的，也就是所谓的“装饰”。

例如，BufferedInputStream 经过包装，为输入流过程增加缓存，类似这种装饰器还可以多次嵌套，不断地增加不同层次的功能。