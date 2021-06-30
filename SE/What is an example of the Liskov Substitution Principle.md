---
title: What is an example of the Liskov Substitution Principle?
categories:
- SE
- SOLID
- LSP
tags:
- SOLID
- LSP
date: 2021/6/30
---



# [What is an example of the Liskov Substitution Principle?](https://stackoverflow.com/questions/56860/what-is-an-example-of-the-liskov-substitution-principle)

A great example illustrating LSP (given by Uncle Bob in a podcast I heard recently) was how sometimes something that sounds right in natural language doesn't quite work in code.

In mathematics, a `Square` is a `Rectangle`. Indeed it is a specialization of a rectangle. The "is a" makes you want to model this with inheritance. However if in code you made `Square` derive from `Rectangle`, then a `Square` should be usable anywhere you expect a `Rectangle`. This makes for some strange behavior.

Imagine you had `SetWidth` and `SetHeight` methods on your `Rectangle` base class; this seems perfectly logical. However if your `Rectangle` reference pointed to a `Square`, then `SetWidth` and `SetHeight` doesn't make sense because setting one would change the other to match it. In this case `Square` fails the Liskov Substitution Test with `Rectangle` and the abstraction of having `Square` inherit from `Rectangle` is a bad one.

Well, a square clearly IS a type of rectangle in the real world. Whether we can model this in our code depends on the spec. What the LSP indicates is that subtype behavior should match base type behavior as defined in the base type specification. If the rectangle base type spec says that height and width can be set independently, then LSP says that square cannot be a subtype of rectangle. If the rectangle spec says that a rectangle is immutable, then a square can be a subtype of rectangle. It's all about subtypes maintaining the behavior specified for the base type. 

Q:

What if it's an immutable Rectangle such that instead of SetWidth and SetHeight, we have the methods GetWidth and GetHeight instead?

A:

there is no issue if it's immutable. The real issue here is that we are not modeling rectangles, but rather "reshapable rectangles," i.e., rectangles whose width or height can be modified after creation (and we still consider it to be the same object). If we look at the rectangle class in this way, it is clear that a square is not a "reshapable rectangle", because a square cannot be reshaped and still be a square (in general). Mathematically, we don't see the problem because mutability doesn't even make sense in a mathematical context. 

---

[![enter image description here](https://i.stack.imgur.com/ilxzO.jpg)](https://i.stack.imgur.com/ilxzO.jpg)

Moral of the story: model your classes based on behaviours not on properties; model your data based on properties and not on behaviours. If it behaves like a duck, it's certainly a bird. 

