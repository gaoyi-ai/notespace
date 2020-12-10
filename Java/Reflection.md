---
title: Reflection
categories:
- Java
tags:
- reflection
date: 2019/8/1 20:00:14
updated: 2020/12/10 12:00:14
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

