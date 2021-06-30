---
title: The Liskov Substitution Principle Made Simple
categories:
- SE
- SOLID
- LSP
tags:
- SOLID
- LSP
date: 2021/6/30
---



> [levelup.gitconnected.com](https://levelup.gitconnected.com/the-liskov-substitution-principle-made-simple-5e69165e7ab5)

> Or when to extend a base class and when to compose objects.

Or when to extend a base class and when to compose objects.
-----------------------------------------------------------

![](https://miro.medium.com/max/6720/1*2X9Af2QGC0LaXt206Z0exg.png)

©Mihai Sandu

And the number one spot for the most overlooked SOLID principle goes to… Liskov substitution. No surprises here.

I’m not going to bore you with the scientific [definition](https://en.wikipedia.org/wiki/Liskov_substitution_principle). In short, the principle states that all object types should be substitutable for their subtypes without changing the correct behavior.

The key to understanding the principle stays in “correct behavior”. Let’s look at the principle with an example.

As part of the software engineering team at AutoPilotCars AI startup, we have to come up with a solution to control normal cars (like a Toyota Camry).

After much thought, the team has decided to build two modules:

*   a module that transforms the car into a “drivable” object. This module supports four simple commands: go forward or backward, turn left or right.
*   an AutoPilot module that calculates a route and drives the car with the help of the four commands exposed by the drivable module.

Here is a diagram:

![img](https://miro.medium.com/max/2247/1*Y9nlNMTtxSaXmB9Sjv33eA.png)

We have to build two modules: one to control the car and one to drive the car. ©Mihai Sandu

And some basic C# code:

```c#
public class Car //system that controls car driving operations 
{
    public void GoForward()
    {
        Console.WriteLine("Car going forward.");
    }

    public void TurnLeft()
    {
        Console.WriteLine("Car turns left.");
    }

    public void TurnRight()
    {
        Console.WriteLine("Car turns right.");
    }

    public void GoBackward()
    {
        Console.WriteLine("Car backing up.");
    }
}

public class AutoPilot //the autopilot software class
{
    private Car vehicle;

    public AutoPilot(Car vehicle)
    {
        this.vehicle = vehicle;
    }

    public void Navigate(string destination)
    {
        //imagine here it is a complex algorithm that navigates the car from point A to point B.
        vehicle.GoForward(); //simulate all driving operations. 
        vehicle.TurnLeft();
        vehicle.GoBackward();
        vehicle.TurnRight();
    }
}
```

Expanding into the truck market
-------------------------------

Since our two modules were incredibly successful, the company decides to extend into a new market, trucks. We observe that trucks support the same kind of commands as cars do, so we want to reuse the AutoPilot module and reimplement the Drivable module.

We will extract the common abstractions into a separate interface and let the AutoPilot module control the drivable object. Here is the solution:

```
public interface IDrivable // extracted interface from Car class
    {
        void GoForward();
        void TurnLeft();
        void TurnRight();
        void GoBackward();
    }

    public class Car : IDrivable
    {
       ...
    }

    public class Truck : IDrivable
    {
        public void GoForward()
        {
            Console.WriteLine("Truck going forward.");
        }

        public void TurnLeft()
        {
            Console.WriteLine("Truck turns left.");
        }

        public void TurnRight()
        {
            Console.WriteLine("Truck turns right.");
        }

        public void GoBackward()
        {
            Console.WriteLine("Truck backing up.");
        }
    }
```

Based on the Car class, we have extracted the “IDrivable” interface. Now, the AutoPilot class depends on the IDrivable interface. All good up to here.

Things are going wonderfully so our company wants to expand into trains.

![img](https://miro.medium.com/max/2073/1*I1ssa2070cIhtJ2Uhv39nQ.png)

Trains can’t turn left or right. ©Mihai Sandu

This is our first speedbump. Trains can’t turn left or right since they are on tracks. So we have no choice but to overlook implementing the TurnLeft() and TurnRight() methods. That means throwing NotImplementedException or doing nothing (since in this case, the method returns void).

Imagine that there is an AI algorithm behind the AutoPilot module. After countless hours of training, it finally mastered the four directions. The module doesn’t know if it controls a car, a truck, or a train, it just knows it will receive an “IDrivable” object.

For cars and trucks it works flawlessly, but for trains? We have two possible outcomes:

*   if the TurnLeft() and TurnRight() methods throw an exception, it will unexpectedly stop the program at runtime. No good
*   if the methods do nothing, the algorithm could learn to either ignore them (which is not good because on cars and trucks we don’t want them to be ignored) or could remain stuck on an infinite loop trying to take a turn. No good again

We broke the Liskov substitution principle by inheriting the IDrivable interface. Our AutoPilot class works only if we get a Car or a Truck, not a Train.

Solution
--------

We could omit to inherit the IDrivable interface for the Train class. This will have the logic separated and so all code looks ok. But doing this will also mean we have to build another AutoPilot module just for trains. Expensive.

We could update the AutoPilot module and verify if the received object is a Train. And update the code accordingly. But this is the first step before falling down the rabbit hole. And if leads to another if and so on until we reach a point where we wonder wtf happened.

Maybe there is a better way. We will talk about this in the Interface Segregation Principle article.

Example of LSP violation in the .NET framework
----------------------------------------------

Sometimes is hard to not violate the principle. Take Microsoft for example. They arguably have some of the best developers in the world working on the C# language. But even they couldn’t foresee how the .NET framework will evolve.

Today, in .NET you can call the Add() method arrays. It will not work, it will throw a NotSupportException. But why can we do it? Because the Array class implements the IList interface which defines the method.

This problem appeared with .NET 2.0 (when generics were introduced) and since Microsoft didn’t want to break the backward compatibility they made this compromise

Avoid extracting interfaces based on a class. In 90% of cases, extracted interfaces lead to breaking the principle. When you look for common abstractions do the following:

*   apply the ROT rule (rule of three). We are eager to extract code as soon as we copy-paste it once. Two cases represent too little information to be able to extract a good generalization. Wait at least three times, even more, if you don’t feel confident.
*   not every **“is a”** relationship should lead to inheritance. For example, a Square is a Rectangle, but we wouldn’t want to have one inherit the other. Rectangles can set Width and Length, but such methods are bad for a Square. Sometimes is best to keep things separated.
*   look at your class API from the client’s point of view before writing interfaces or establishing class hierarchies.

But, sometimes you have no choice but to break the LSP. It happens to the best of us. Just like Microsoft didn’t want to want to break old contracts and keep or keep the framework stop, you could face a situation where the only way out is by breaking it. Do the best you can to let your clients know and move on.

# Takeaways

- Generally, LSP is violated when we try to remove features. NotImplementedException (or similar) is the biggest violation sign.
- Avoid extracting interfaces from a class. Look for common abstractions. Apply the ROT *(rule of three -> minimum three copy-paste before extracting abstractions)*
- Favor multiple smaller interfaces in face of bigger ones; it is less likely to violate the principle (will talk more in Interface Segregation Principle)
- breaking the principle almost always leads to hard-to-find bugs.

**SOLID principles made simple series:**

- [The Single Responsibility Principle](https://levelup.gitconnected.com/the-single-responsibility-principle-made-simple-4e1597a44d7d) (SRP)
- [The Open-Closed Principle](https://levelup.gitconnected.com/the-open-closed-principle-made-simple-cc3d0ed70553) (OCP)
- The Liskov Substitution Principle (LSP)
- [The Interface Segregation Principle ](https://levelup.gitconnected.com/interface-segregation-principle-made-simple-990da495441c)(ISP)
- [The Dependency Inversion Principle](https://levelup.gitconnected.com/the-dependency-inversion-principle-made-simple-70108b88dc76) (DIP)

# Further reading

[SOLID design: The Liskov Substitution Principle (LSP) - NDependThe Liskov substitution principle is the L in the well known SOLID acronym. The original principle definition is…blog.ndepend.com](https://blog.ndepend.com/solid-design-the-liskov-substitution-principle/)

[SOLID Design Principles Explained: The Liskov Substitution Principle with Code ExamplesAll 5 SOLID design principles are broadly used, If you don't know them by name, you will quickly recognize that they…stackify.com](https://stackify.com/solid-design-liskov-substitution-principle/)

[Liskov Substitution Principle in Java | BaeldungThe SOLID design principles were introduced by Robert C. Martin in his 2000 paper, Design Principles and Design…www.baeldung.com](https://www.baeldung.com/java-liskov-substitution-principle)