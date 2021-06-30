---
title: Singleton
categories:
- Pattern
- Singleton
tags:
- singleton
date: 2019/8/1 20:00:17
updated: 2020/12/10 12:00:17
---



# Singleton

### 静态内部类实现

静态内部类的优点是：外部类加载时并不需要立即加载内部类，内部类不被加载则不去初始化INSTANCE，故而不占内存。即当SingleTon第一次被加载时，并不需要去加载SingleTonHoler，只有当getInstance()方法第一次被调用时，才会去初始化INSTANCE,第一次调用getInstance()方法会导致虚拟机加载SingleTonHoler类，这种方法不仅能确保线程安全，也能保证单例的唯一性，同时也延迟了单例的实例化。

```java
public class Singleton {
    private Singleton() {
      }

      private static Singleton instatnce;

      public static Singleton getInstance() {
        return SingletonHolder.instatnce;
      }
    
      private static class SingletonHolder {
        private static Singleton instatnce = new Singleton();
      }
	}
```

**那么，静态内部类又是如何实现线程安全的呢？首先，我们先了解下类的加载时机。**

**类加载时机：JAVA虚拟机在有且仅有的5种场景下会对类进行初始化。**

1. 遇到new、getstatic、setstatic或者invokestatic这4个字节码指令时，对应的java代码场景为：new一个关键字或者一个实例化对象时、读取或设置一个静态字段时(final修饰、已在编译期把结果放入常量池的除外)、调用一个类的静态方法时。
2. 使用java.lang.reflect包的方法对类进行反射调用的时候，如果类没进行初始化，需要先调用其初始化方法进行初始化。
3. 当初始化一个类时，如果其父类还未进行初始化，会先触发其父类的初始化。
4. 当虚拟机启动时，用户需要指定一个要执行的主类(包含main()方法的类)，虚拟机会先初始化这个类。
5. 当使用JDK 1.7等动态语言支持时，如果一个java.lang.invoke.MethodHandle实例最后的解析结果REF_getStatic、REF_putStatic、REF_invokeStatic的方法句柄，并且这个方法句柄所对应的类没有进行过初始化，则需要先触发其初始化。

**这5种情况被称为是类的主动引用，注意，这里《虚拟机规范》中使用的限定词是"有且仅有"，那么，除此之外的所有引用类都不会对类进行初始化，称为被动引用。静态内部类就属于被动引用的行列。**

我们再回头看下getInstance()方法，调用的是SingleTonHoler.INSTANCE，取的是SingleTonHoler里的INSTANCE对象，跟上面那个DCL方法不同的是，getInstance()方法并没有多次去new对象，故不管多少个线程去调用getInstance()方法，取的都是同一个INSTANCE对象，而不用去重新创建。当getInstance()方法被调用时，SingleTonHoler才在SingleTon的运行时常量池里，把符号引用替换为直接引用，这时静态对象INSTANCE也真正被创建，然后再被getInstance()方法返回出去，这点同饿汉模式。那么INSTANCE在创建过程中又是如何保证线程安全的呢？在《深入理解JAVA虚拟机》中，有这么一句话:

> 虚拟机会保证一个类的()方法在多线程环境中被正确地加锁、同步，如果多个线程同时去初始化一个类，那么只会有一个线程去执行这个类的()方法，其他线程都需要阻塞等待，直到活动线程执行()方法完毕。如果在一个类的()方法中有耗时很长的操作，就可能造成多个进程阻塞(需要注意的是，其他线程虽然会被阻塞，但如果执行()方法后，其他线程唤醒之后不会再次进入()方法。同一个加载器下，一个类型只会初始化一次。)，在实际应用中，这种阻塞往往是很隐蔽的。

**故而，可以看出INSTANCE在创建过程中是线程安全的，所以说静态内部类形式的单例可保证线程安全，也能保证单例的唯一性，同时也延迟了单例的实例化。**

那么，是不是可以说静态内部类单例就是最完美的单例模式了呢？其实不然，静态内部类也有着一个致命的缺点，就是传参的问题，由于是静态内部类的形式去创建单例的，故外部无法传递参数进去，例如Context这种参数，所以，我们创建单例时，可以在静态内部类与DCL模式里自己斟酌。

## Pro & Cons

### Pros

- You can be sure that a class has only a single instance.
- You gain a global access point to that instance.
- The singleton object is initialized only when it’s requested for the first time.

### Cons

- Since Singleton does not have an abstraction layer, so the expansion has great difficulties.
- Violates the Single Responsibility Principle. The pattern solves
    two problems at the time.
- The Singleton pattern can mask bad design, for instance, when the components of the program know too much about each other.
- The pattern requires special treatment in a multithreaded environment so that multiple threads won’t create a singleton object several times.
- It may be diffi cultt o unit test the client code of the Singleton because many test frameworks rely on inheritance when producing mock objects. Since the constructor of the singleton class is private and overriding static methods is impossible in most languages, you will need to think of a creative way to mock the singleton. Or just don’t write the tests. Or don’t use the Singleton pattern.

## Why Singletons are Evil

>[Why Singletons are Evil - Microsoft](https://docs.microsoft.com/en-us/archive/blogs/scottdensmore/why-singletons-are-evil)

### Singletons frequently are used to provide a global access point for some service.

True, they do this, but at what cost? They provide a well-known point of access to some service in your application so that you don't have to pass around a reference to that service. How is that different from a global variable? (remember, globals are bad, right???) What ends up happening is that the dependencies in your design are hidden inside the code, and not visible by examining the interfaces of your classes and methods. You have to inspect the code to understand exactly what other objects your class uses. This is less clear than it could be. The urge to create something as a global to avoid passing it around is a smell in your design; it is not a feature of globals/singletons. If you examine your design more closely, you can almost always come up with a design that it is better and does not have to pass around tramp data to every object and method.

### Singletons allow you to limit creation of your objects.
This is true, but now you are mixing two different responsibilities into the same class, which is a violation of the [Single Responsibility Principle](http://c2.com/cgi/wiki?SingleResponsibilityPrinciple). A class should not care whether or not it is a singleton. It should be concerned with its business responsibilities only. If you want to limit the ability to instantiate some class, create a factory or builder object that encapsulates creation, and in there, limit creation as you wish. Now the responsibilities of creation are partitioned away from the responsibilities of the business entity.

### Singletons promote tight coupling between classes

One of the underlying properties that makes code testable is that it is loosely coupled to its surroundings. This property allows you to substitute alternate implementations for collaborators during testing to achieve specific testing goals (think mock objects). Singletons tightly couple you to the exact type of the singleton object, removing the opportunity to use polymorphism to substitute an alternative. A better alternative, as discussed in the first point above, is to alter your design to allow you to pass references to objects to your classes and methods, which will reduce the coupling issues described above.

### Singletons carry state with them that last as long as the program lasts
Persistent state is the enemy of unit testing. One of the things that makes unit testing effective is that each test has to be independent of all the others. If this is not true, then the order in which the tests run affects the outcome of the tests. This can lead to cases where tests fail when they shouldn't, and even worse, it can lead to tests that pass just because of the order in which they were run. This can hide bugs and is evil. Avoiding static variables is a good way to prevent state from being carried from test to test. Singletons, by their very nature, depend on an instance that is held in a static variable. This is an invitation for test-dependence. Avoid this by passing references to objects to your classes and methods.