---
title: SOLID Design Principles Explained - The Liskov Substitution Principle with Code Examples
categories:
- SE
- SOLID
- LSP
tags:
- SOLID
- LSP
date: 2021/6/30
---



# SOLID Design Principles Explained: The Liskov Substitution Principle with Code Examples

> [stackify.com](https://stackify.com/solid-design-liskov-substitution-principle/)

> All 5 SOLID design principles are broadly used, If you don't know them by name, you will quickly reco......

The [Open/Closed Principle](https://stackify.com/solid-design-open-closed-principle/), which I explained in a previous article, is one of the key concepts in OOP that enables you to write robust, maintainable and reusable software components. But following the rules of that principle alone is not enough to ensure that you can change one part of your system without breaking other parts. Your classes and interfaces also need to follow the Liskov Substitution Principle to avoid any side-effects.

The Liskov Substitution Principle is the 3rd of [Robert C. Martin](http://blog.cleancoder.com/)‘s famous SOLID design principles:

*   [**S**](https://stackify.com/solid-design-principles/)[ingle Responsibility Principle](https://stackify.com/solid-design-principles/)
*   [**O**](https://stackify.com/solid-design-open-closed-principle/)[pen/Closed Principle](https://stackify.com/solid-design-open-closed-principle/)
*   **L**iskov Substitution Principle
*   [**I**nterface Segregation Principle](https://stackify.com/interface-segregation-principle/)
*   [**D**ependency Inversion](https://stackify.com/dependency-inversion-principle/)

It extends the Open/Closed Principle by focusing on the behavior of a superclass and its subtypes. As I will show you in this article, this is at least as important but harder to validate that the structural requirements of the Open/Closed Principle.

### Definition of the Liskov Substitution Principle

The Liskov Substitution principle was introduced by [Barbara Liskov](https://en.wikipedia.org/wiki/Barbara_Liskov) in her conference keynote “Data abstraction” in 1987. A few years later, she published a paper with Jeanette Wing in which they defined the principle as:

> Let _Φ(x)_ be a property provable about objects _x_ of type _T_. Then _Φ(y)_ should be true for objects _y_ of type _S_ where _S_ is a subtype of _T_.

OK, let’s be honest. Such a scientific definition might be necessary, but it doesn’t help a lot in our daily work as software developers. So, what does it mean for our code?

#### The Liskov Substitution Principle in practical software development

The principle defines that objects of a superclass shall be replaceable with objects of its subclasses without breaking the application. That requires the objects of your subclasses to behave in the same way as the objects of your superclass. You can achieve that by following a few rules, which are pretty similar to the [design by contract](https://en.wikipedia.org/wiki/Design_by_contract) concept defined by Bertrand Meyer.

An overridden method of a subclass needs to accept the same input parameter values as the method of the superclass. That means you can implement less restrictive validation rules, but you are not allowed to enforce stricter ones in your subclass. Otherwise, any code that calls this method on an object of the superclass might cause an [exception](https://stackify.com/specify-handle-exceptions-java/), if it gets called with an object of the subclass.

Similar rules apply to the return value of the method. The return value of a method of the subclass needs to comply with the same rules as the return value of the method of the superclass. You can only decide to apply even stricter rules by returning a specific subclass of the defined return value, or by returning a subset of the valid return values of the superclass.

#### Enforcing the Liskov Substitution Principle

If you decide to apply this principle to your code, the behavior of your classes becomes more important than its structure. Unfortunately, there is no easy way to enforce this principle. The compiler only checks the structural rules defined by the Java language, but it can’t enforce a specific behavior.

You need to implement your own checks to ensure that your code follows the Liskov Substitution Principle. In the best case, you do this via code reviews and test cases. In your test cases, you can execute a specific part of your application with objects of all subclasses to make sure that none of them causes an error or significantly changes its performance. You can try to do similar checks during a code review. But what’s even more important is that you check that you created and executed all the required test cases.

Okay, enough theory. Let’s take a look at an example

### Making coffee with the Liskov Substitution Principle

Most articles about the Liskov Substitution Principle use an example in which they implement a _Rectangle_ and a _Square_ class to show that you break the design principle if your _Square_ class extends the _Rectangle_ class.

But that example is a little bit boring. There are already lots of articles about it, and I have never implemented an application that just requires a set of simple geometric shapes. So, let’s create an example that’s a little bit more fun.

I enjoy drinking a good cup of coffee in the morning, and I want to show you a simple application that uses different kinds of coffee machines to brew a cup of coffee. You might already know very similar examples from my previous articles about the [Single Responsibility Principle](https://stackify.com/solid-design-principles/) or the [Open/Closed Principle](https://stackify.com/solid-design-open-closed-principle/). You can get all source files of this example at [https://github.com/thjanssen/Stackify-SOLID-Liskov](https://github.com/thjanssen/Stackify-SOLID-Liskov).

If you enjoy coffee as much as I do, you most likely used several different coffee machines in the past. There are relatively basic ones that you can use to transform one or two scoops of ground coffee and a cup of water into a nice cup of filter coffee. And there are others that include a grinder to grind your coffee beans and you can use to brew different kinds of coffee, like filter coffee and espresso.

If you decide to implement an application that automatically brews a cup of coffee every morning so that you don’t have to get out of bed before it’s ready, you might decide to model these coffee machines as two classes with the methods _addCoffee_ and _brewCoffee_.

![](https://stackify.com/wp-content/uploads/2018/04/word-image-7.png)

#### A basic coffee machine

The _BasicCoffeeMachine_ can only brew filter coffee. So, the _brewCoffee_ method checks if the provided _CoffeeSelection_ value is equal to _FILTER_COFFEE_ before it calls the private _brewFilterCoffee_ method to create and return a _CoffeeDrink_ object.

```java
public class BasicCoffeeMachine { 

    private Map configMap; 
    private Map groundCoffee; 
    private BrewingUnit brewingUnit;

    public BasicCoffeeMachine(Map coffee) { 
        this.groundCoffee = coffee; 
        this.brewingUnit = new BrewingUnit(); 

        this.configMap = new HashMap(); 
        this.configMap.put(CoffeeSelection.FILTER_COFFEE, 
            new Configuration(30, 480)); 
    } 

    public CoffeeDrink brewCoffee(CoffeeSelection selection) 
        throws CoffeeException {

        switch (selection) { 
            case FILTER_COFFEE: 
                return brewFilterCoffee(); 
            default: 
                throw new CoffeeException(
                    "CoffeeSelection [" + selection + "] not supported!"); 
        } 
    } 

    private CoffeeDrink brewFilterCoffee() { 
        Configuration config = configMap.get(CoffeeSelection.FILTER_COFFEE); 

        // get the coffee 
        GroundCoffee groundCoffee = this.groundCoffee.get(
            CoffeeSelection.FILTER_COFFEE); 

        // brew a filter coffee 
        return this.brewingUnit.brew(CoffeeSelection.FILTER_COFFEE, 
            groundCoffee, config.getQuantityWater()); 
    } 

    public void addCoffee(CoffeeSelection sel, GroundCoffee newCoffee) 
        throws CoffeeException {

        GroundCoffee existingCoffee = this.groundCoffee.get(sel); 
        if (existingCoffee != null) { 
            if (existingCoffee.getName().equals(newCoffee.getName())) { 
                existingCoffee.setQuantity(
                    existingCoffee.getQuantity() + newCoffee.getQuantity()); 
            } else { 
                throw new CoffeeException(
                    "Only one kind of coffee supported for each CoffeeSelection."); 
            } 
        } else { 
            this.groundCoffee.put(sel, newCoffee); 
        } 
    } 
}
```

The _addCoffee_ method expects a _CoffeeSelection_ enum value and a _GroundCoffee_ object. It uses the _CoffeeSelection_ as the key of the internal _groundCoffee_ _Map_.

These are the most important parts of the _BasicCoffeeMachine_ class. Let’s take a look at the _PremiumCoffeeMachine_.

#### A premium coffee machine

The premium coffee machine has an integrated grinder, and the internal implementation of the _brewCoffee_ method is a little more complex. But you don’t see that from the outside. The method signature is identical to the one of the _BasicCoffeeMachine_ class.

```java
public class PremiumCoffeeMachine { 

    private Map<CoffeeSelection, Configuration> configMap; 
    private Map<CoffeeSelection, CoffeeBean> beans; private Grinder grinder; 
    private BrewingUnit brewingUnit; 

    public PremiumCoffeeMachine(Map<CoffeeSelection, CoffeeBean> beans) { 
        this.beans = beans; 
        this.grinder = new Grinder(); 
        this.brewingUnit = new BrewingUnit(); 

        this.configMap = new HashMap<>(); 
        this.configMap.put(CoffeeSelection.FILTER_COFFEE, 
            new Configuration(30, 480)); 
        this.configMap.put(CoffeeSelection.ESPRESSO, 
            new Configuration(8, 28)); 
    } 

    @Override 
    public CoffeeDrink brewCoffee(CoffeeSelection selection) 
        throws CoffeeException { 

        switch(selection) { 
            case ESPRESSO: 
                return brewEspresso(); 
            case FILTER_COFFEE: 
                return brewFilterCoffee(); 
            default: 
                throw new CoffeeException(
                    "CoffeeSelection [" + selection + "] not supported!"); 
        } 
    } 

    private CoffeeDrink brewEspresso() { 
        Configuration config = configMap.get(CoffeeSelection.ESPRESSO); 

        // grind the coffee beans 
        GroundCoffee groundCoffee = this.grinder.grind( 
        this.beans.get(CoffeeSelection.ESPRESSO), 
            config.getQuantityCoffee()); 

        // brew an espresso 
        return this.brewingUnit.brew(CoffeeSelection.ESPRESSO, 
            groundCoffee, config.getQuantityWater()); 
    } 

    private CoffeeDrink brewFilterCoffee() { 
        Configuration config = configMap.get(CoffeeSelection.FILTER_COFFEE); 

        // grind the coffee beans 
        GroundCoffee groundCoffee = this.grinder.grind( 
            this.beans.get(CoffeeSelection.FILTER_COFFEE), 
                config.getQuantityCoffee()); 

        // brew a filter coffee 
        return this.brewingUnit.brew(CoffeeSelection.FILTER_COFFEE, 
            groundCoffee, config.getQuantityWater()); 
    } 

    public void addCoffee(CoffeeSelection sel, CoffeeBean newBeans) 
        throws CoffeeException { 

        CoffeeBean existingBeans = this.beans.get(sel); 
        if (existingBeans != null) { 
            if (existingBeans.getName().equals(newBeans.getName())) { 
                existingBeans.setQuantity(
                    existingBeans.getQuantity() + newBeans.getQuantity()); 
            } else { 
                throw new CoffeeException(
                    "Only one kind of coffee supported for each CoffeeSelection."); 
            } 
        } else { 
            this.beans.put(sel, newBeans); 
        } 
    } 
}
```

But that’s not the case for the _addCoffee_ method. It expects an object of type _CoffeeBean_ instead of an object of type _GroundCoffee_. If you add a shared superclass or an interface that gets implemented by the _BasicCoffeeMachine_ and the _PremiumCoffeeMachine_ class, you will need to decide how to handle this difference.

### **Introducing a shared interface**

You can either create another abstraction, e.g., _Coffee_, as the superclass of _CoffeeBean_ and _GroundCoffee_ and use it as the type of the method parameter. That would unify the structure of both _addCoffee_ methods, but require additional validation in both methods. The _addCoffee_ method of the _BasicCoffeeMachine_ class would need to check that the caller provided an instance of _GroundCoffee_, and the _addCoffee_ implementation of the _PremiumCoffeeMachine_ would require an instance of _CoffeeBean_. This would obviously break the Liskov Substitution Principle because the validation would fail if you provide a _BasicCoffeeMachine_ object instead of a _PremiumCoffeeMachine_ and vice versa.

The better approach is to exclude the _addCoffee_ method from the interface or superclass because you can’t interchangeably implement it. The _brewCoffee_ method, on the other hand, could be part of a shared interface or a superclass, as long as the superclass or interface only guarantees that you can use it to brew filter coffee. The input parameter validation of both implementations accept the _CoffeeSelection_ value _FILTER_COFFEE_. The _addCoffee_ method of the _PremiumCoffeeMachine_ class also accepts the enum value _ESPRESSO_. But as I explained at the beginning of this article, the different subclasses may implement less restrictive validation rules.

![](https://stackify.com/wp-content/uploads/2018/04/word-image-8.png)

### Summary

The Liskov Substitution Principle is the third of Robert C. Martin’s SOLID design principles. It extends the [Open/Closed principle](https://stackify.com/solid-design-open-closed-principle/) and enables you to replace objects of a parent class with objects of a subclass without breaking the application. This requires all subclasses to behave in the same way as the parent class. To achieve that, your subclasses need to follow these rules:

*   Don’t implement any stricter validation rules on input parameters than implemented by the parent class.
*   Apply at the least the same rules to all output parameters as applied by the parent class.
