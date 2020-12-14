---
title: Reflection
categories:
- Java
tags:
- reflection
date: 2019/8/1 20:00:14
updated: 2020/12/14 17:00:14
---

# The Reflection API

## Uses of Reflection

Reflection is commonly used by programs which require the ability to examine or modify the runtime behavior of applications running in the Java virtual machine.

- Extensibility Features

    An application may make use of external, user-defined classes by creating instances of extensibility objects using their fully-qualified names.

- Class Browsers and Visual Development Environments

    A class browser needs to be able to enumerate the members of classes. Visual development environments can benefit from making use of type information available in reflection to aid the developer in writing correct code.

- Debuggers and Test Tools

    Debuggers need to be able to examine private members on classes. Test harnesses can make use of reflection to systematically call a discoverable set APIs defined on a class, to insure a high level of code coverage in a test suite.

## Drawbacks of Reflection

Reflection is powerful, but should not be used indiscriminately. If it is possible to perform an operation without using reflection, then it is preferable to avoid using it. The following concerns should be kept in mind when accessing code via reflection.

- Performance Overhead

    Because reflection involves types that are dynamically resolved, certain Java virtual machine optimizations can not be performed. Consequently, reflective operations have slower performance than their non-reflective counterparts, and should be avoided in sections of code which are called frequently in performance-sensitive applications.

- Security Restrictions

    Reflection requires a runtime permission which may not be present when running under a security manager. This is in an important consideration for code which has to run in a restricted security context, such as in an Applet.

- Exposure of Internals

    Since reflection allows code to perform operations that would be illegal in non-reflective code, such as accessing `private` fields and methods, the use of reflection can result in unexpected side-effects, which may render code dysfunctional and may destroy portability. Reflective code breaks abstractions and therefore may change behavior with upgrades of the platform.

---

The name reflection is used to describe code which is able to inspect other code in the same system (or itself).

For example, say you have an object of an unknown type in Java, and you would like to call a 'doSomething' method on it if one exists. Java's static typing system isn't really designed to support this unless the object conforms to a known interface, but using reflection, your code can look at the object and find out if it has a method called 'doSomething' and then call it if you want to.

So, to give you a code example of this in Java (imagine the object in question is foo) :

```java
Method method = foo.getClass().getMethod("doSomething", null);
method.invoke(foo, null);
```

One very common use case in Java is the usage with annotations. JUnit 4, for example, will use reflection to look through your classes for methods tagged with the @Test annotation, and will then call them when running the unit test.

---

- Reflection is much slower than just calling methods by their name, because it has to inspect the metadata in the bytecode instead of just using precompiled addresses and constants.
- Reflection is also more powerful: you can retrieve the definition of a `protected` or `final` member, *remove the protection* and manipulate it as if it had been declared mutable! Obviously this subverts many of the guarantees the language normally makes for your programs and can be very, very dangerous.

And this pretty much explains when to use it. Ordinarily, don't. If you want to call a method, just call it. If you want to mutate a member, just declare it mutable instead of going behind the compile's back.

One useful real-world use of reflection is when writing a framework that has to interoperate with user-defined classes, where the framework author doesn't know what the members (or even the classes) will be. Reflection allows them to deal with any class without knowing it in advance. For instance, I don't think it would be possible to write a complex aspect-oriented library without reflection.

As another example, JUnit used to use a trivial bit of reflection: it enumerates all methods in your class, assumes that all those called `testXXX` are test methods, and executes only those. But this can now be done better with annotations instead, and in fact JUnit 4 has largely moved to annotations instead.

---

# 动态代理

首先，它是一个**代理机制**。如果熟悉设计模式中的代理模式，代理可以看作是对调用目标的一个包装，这样对目标代码的调用不是直接发生的，而是通过代理完成。其实很多动态代理场景，可以看作是装饰器（Decorator）模式的应用

通过代理可以让调用者与实现者之间**解耦**。比如进行 RPC 调用，框架内部的寻址、序列化、反序列化等，对于调用者往往是没有太大意义的，通过代理，可以提供更加友善的界面。

 JDK 动态代理的一个简单例子。下面只是加了一句 print，在生产系统中，可以轻松扩展类似逻辑进行诊断、限流等。

```java
public class MyDynamicProxy {
    public static  void main (String[] args) {
        HelloImpl hello = new HelloImpl();
        MyInvocationHandler handler = new MyInvocationHandler(hello);
        // 构造代码实例
        Hello proxyHello = (Hello) Proxy.newProxyInstance(HelloImpl.class.getClassLoader(), HelloImpl.class.getInterfaces(), handler);
        // 调用代理方法
        proxyHello.sayHello();
    }
}
interface Hello {
    void sayHello();
}
class HelloImpl implements  Hello {
    @Override
    public void sayHello() {
        System.out.println("Hello World");
    }
}
 class MyInvocationHandler implements InvocationHandler {
    private Object target;
    public MyInvocationHandler(Object target) {
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        System.out.println("Invoking sayHello");
        Object result = method.invoke(target, args);
        return result;
    }
}
 
```

上面的 JDK Proxy 例子，非常简单地实现了动态代理的构建和代理操作。首先，实现对应的 InvocationHandler；然后，以接口 Hello 为纽带，为被调用目标构建代理对象，进而应用程序就可以使用代理对象间接运行调用目标的逻辑，代理为应用插入额外逻辑（这里是 println）提供了便利的入口。

从 API 设计和实现的角度，这种实现仍然有局限性，因为它是以接口为中心的，相当于添加了一种对于被调用者没有太大意义的限制。实例化的是 Proxy 对象，而不是真正的被调用类型，这在实践中还是可能带来各种不便和能力退化。

如果被调用者没有实现接口，而还是希望利用动态代理机制，那么可以考虑其他方式。Spring AOP 支持两种模式的动态代理，JDK Proxy 或者 cglib，如果我们选择 cglib 方式，你会发现对接口的依赖被克服了。

cglib 动态代理采取的是创建目标类的子类的方式，因为是子类化，可以达到近似使用被调用者本身的效果。在 Spring 编程中，框架通常会处理这种情况，当然也可以[显式指定](http://cliffmeyers.com/blog/2006/12/29/spring-aop-cglib-or-jdk-dynamic-proxies.html)。

简单对比下两种方式各自优势。

JDK Proxy 的优势：

- 最小化依赖关系，减少依赖意味着简化开发和维护，JDK 本身的支持，可能比 cglib 更加可靠。
- 平滑进行 JDK 版本升级，而字节码类库通常需要进行更新以保证在新版 Java 上能够使用。
- 代码实现简单。

基于类似 cglib 框架的优势：

- 有的时候调用目标可能不便实现额外接口，从某种角度看，限定调用者实现接口是有些侵入性的实践，类似 cglib 动态代理就没有这种限制。
- 只操作所关心的类，而不必为其他相关类增加工作量。
- 高性能。

