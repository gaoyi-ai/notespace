---
title: What is the difference between a library and a framework?
categories:
- SE
tags:
- framework
date: 2021/6/10
---

# What is the difference between a library and a framework?

Libraries are the older concept and these are just collection of utility classes/methods your code calsl upon to get some functionality. Frameworks, on the other hand, contain some functionality or flow and calls your code to extend or make the flow a specific one. The principle of frameworks calling your code is known as "Inversion of Control."

There are several mechanisms you can use to provide Inversion of Control including:

- Subclassing
- Dependency Injection
- Template Methods
- Closures
- etc.

While frameworks are newer than libraries, they are nonetheless not very new themselves; see, for example, the 1988 paper "[Designing Reusable Classes](https://web.archive.org/web/20070504053354/http://www.laputan.org/drc/drc.html)", by Ralph E. Johnson and Brian Foote. (Granted, the examples they give are outdated, but it still provides a very interesting reading.)

Why use a framework instead of a library? For one thing, it is a matter of visousity. When you have a library you need to understand the functionality of each method and it is relativly hard to create complex interactions as you need to invoke many methods to get to the result. Frameworks, on the other hand, contain the basic flow and since you only need to plug in your behavior it is easier to do the right thing. In the GIS example that prompted this discussion, it would be better to have a framework since there a hundreds of interfaces you can use. However, what most users want is an easy way to make a UI entity appear on a map.

The other advantages of frameworks over libraries is flexibility and extensibility. By definition, a framework provides you with the necessary means to extend its behavior. Many times you can even subclass the framework classes and provide completely new functionality.

The disadvantage of frameworks is that temptation to add more and more functionality creates a lot of bloated frameworks which results in immobility and needless complexity.

While frameworks have been over-hyped and "buzzword-ified" (like many other concepts), packing functionality as a framework is still something you should consider when you identify some piece of code as reusable.