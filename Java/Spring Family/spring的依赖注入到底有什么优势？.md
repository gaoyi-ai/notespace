---
title: spring的依赖注入到底有什么优势？
categories:
- Java
- Spring
- DI
tags:
- DI
date: 2021/6/27
---



# spring的依赖注入到底有什么优势？

## 依赖注入用来做对象的管理

为啥要管理呢？面向对象本质上是要多个不同职责相互对立的Object互相通过发消息来进行系统设计。但是这个思想落地时，因为性能的原因，Object之间并不像网络那样真的发消息，而是互相调用对方的方法（C++，Java，C#等都是这么做的）。如果想调用某个Object的方法，就得拿到那个Object的引用。

那么怎么拿到Object的引用呢？题目中说new一个不就可以了嘛。但现实当中比这个更复杂。比如一个系统需要一个“paymentService”的对象做支付，也许整个系统只需要一个paymentService的实例，所以必须得判断那个要用的Object有没有，没有才创建。但有时，对于一些Object可能每次都需要一个新的实例。两种策略之间，也许是某种“池化Object“的特定的逻辑，比如连接池只能有100个Connection Object。每个对象要初始化，获取一些资源，也可能某些时候释放一些资源。这些都是对Object生命周期管理的逻辑。如果没有注入机制，那么这些逻辑会散播到整个系统每一个角落，带来巨大的维护问题。

一个更好的办法是，每个Object可以定义自己的初始化静态工厂方法完成初始化，都封装在里面，然后别的地方都调用这个方法。这个模式比上一种好很多，但是依然需要手工写这些调用。这时，开发者通过写代码告诉编译器“我要XXXObject”。这个模式被提取出来就称作“静态依赖注入”。

## 静态注入

静态注入的本质是弄一个全局的大KV，key是那个Object的类，value就是new出来的所有Object。这时你编写代码的逻辑就是：

1. 创建这个大KV
2. 手工new或者通过静态方法创建第一个没有任何依赖的Object，并记录到大KV里
3. 手工new或者通过静态方法创建其他Object，如果创建时需要依赖已经创建好的Object，就从KV里取出来直接用
4. 所有的Object都创建完了，服务就可以启动了

看上去挺顺当，但这么做有个问题是**不够方便。**要手工编写一大堆创建，set引用的代码；此外得自己管理创建Object的顺序，如果系统比较简单还可以；如果一个系统有上千上万个Object，就要写吐血。

要是可以自动产生这些代码就好了。解决这个问题的主流方案有两类，第一类就是“编译期直接产生代码“的方案，代表就是 [Dagger](https://google.github.io/dagger/)。编写程序时，开发人员标记哪些地方需要 @Inject，哪些需要 @Provides。再利用maven之类的工具build时，上述的一系列代码会被自动产生出来。这样做的好处是，因为代码都是编译出来的，安全性比较高，执行效率也很好。但坏处就是**约束特别多**。毕竟编译器能干的事情只能基于代码本身，表达力也相对受限。可以参考[Dagger vs. Guice. If you are familiar with dependency… | by Diwaker Gupta | Floating Sun](https://dig.floatingsun.net/dagger-vs-guice-8c9fbae4712e)

## 动态注入

另一类被称为运行时注入，又叫做动态注入。代表是Spring和Guice。以Spring为例，你只需要定义两类信息：

- 你要如何初始化Object（如何创建/单例、多例还是要弄个什么Factory/有没有自定义初始化代码等）
- 你的Object依赖什么Object（可以通过class或者name来描述）

其余的Spring都帮你直接搞定。使用Spring的程序一启动就开始根据当前的配置信息，classpath等各种静态 + 运行时信息帮你搞定一切。

这么方便，还要啥自行车？

## Spring干了啥

在Spring的体系中，这种被初始化好的，可以被用的Object被称为“Bean”，以避免和Java自己术语的object混淆，也利于顺便从EJB用户那里刷存在感（都是“Bean”嘛）。Bean的配置和注入都用xml来定义（称为application context）。用Spring写的程序启动时会优先启动Spring的一个加载器来读取xml文件，并且按照xml里的描述来自动创建、初始化和管理Bean（Spring后续版本也支持用annotation或者Java代码描述注入关系了，但是道理是一样的）。Spring加载器会自动识别依赖关系，按照树形结构依次创建那些Bean。万一发生了依赖循环，Spring能自己检测出来并报错。这就把程序员关于系统里Object怎么初始化，管理和相互引用的工作量降到最低。

通过动态注入，Spring可以搞一些比较炫的功能，比如绕开访问权限，直接注入private成员，方便编码和测试；动态产生代码以实现AOP；还有最玄幻的AutoConfiguration。Spring能根据当前能加载的Bean有哪些来决定如何初始化一些业务功能。比如启动时，如果classpath里有HSQLDB，而开发者并没有手工配置数据源的类型，那么Spring就会帮你创建一个HSQLDB的内存数据源，来应对那些需要注入DataSource的地方。Spring会用类似的办法帮你决定controller的返回是变成json还是xml，mvc里到底用哪套模版引擎，监控数据到底输出哪些metrics等等。

> 我个人是不太喜欢这种玄幻的功能，一旦结果不符合预期就很难搞明白到底怎么回事。

动态编码在表达能力上肯定是比静态更灵活和方便，有利于提高开发效率。但动态注入也有不小的代价，就是编译器不能帮助检查错误了，程序员要面对大量的运行时错误。此外，报错信息也会比较难看。最后，加载Object的速度也比静态编译好的代码要慢。

## 说说依赖注入本身

本质上“依赖注入”是对Java静态import的一个补充。在Java中，由于语言的限制，**import只能引入无法直接使用的Type，却不能引入已经初始化好的Object**。但是系统开发中无论如何都要解决Object的管理和组织的问题，所以必然需要某种注入框架来完成这个工作。不是Spring也是Summer或者Autumn之类的出现。如果换一个动态语言，比如javascript，import和export的本来就可以是任何对象/函数，所以天然就能“注入”了。另外，FP类语言可以直接覆盖函数的定义，实现接近注入的效果，比如这里提到的。["Dependency Injection" in Golang](https://www.openmymind.net/Dependency-Injection-In-Go/)

## Beyond注入

抛开注入这个事情本身， Spring实际上提供的是全家桶式的开发支持。这才是他被大量使用的更主要的原因。Spring的注入能力只是其庞大身躯中比较核心的内容，但是相比于其他功能来说非常小。 Spring的作者最早想做的事情是“J2EE Development without EJB“。Spring在Java Web服务这个领域的地位几乎无可撼动。

参见我的这个回答：

[关于J2EE和Spring目前到底是怎样的关系，以及未来这两者的发展是怎样的，是否存在竞争市场的情况？](https://www.zhihu.com/question/268742981)

而Dagger主要的应用场景是Android的开发，原因是Android的JRE的反射特别慢，所以不得不利用静态注入的方式来提速。