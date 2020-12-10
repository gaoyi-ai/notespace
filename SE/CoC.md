---
title: Convention Over Conguration
categories:
- SE
tags:
- CoC
date: 2019/8/1 20:00:14
updated: 2020/12/10 12:00:14
---



# Convention Over Conguration

Convention is at the heart of Maven. Convention over configuration is a popular aphorism these days, and Maven fully embraces this concept. Convention over configuration is at the central philosophy of frameworks such as Ruby on Rails, and more recently, the EJB3 specification. In its most basic sense it means that, while configuration is certainly necessary, the majority of users will never utilize such edge-cases those complex configurations provide. Although a powerful framework certainly needs to have the power to configure when necessary, it is certainly reasonable to create defaults to allow the 95% of similar use-cases to work without defining anything at all... the system can assume these defaults. In other words, the system has its own convention. Because of this, the monstrous configurations required of build tools like Ant (where a majority of Ant scripts are cut-and-pasted from existing projects) are non-existent for those projects that follow Maven's conventions.

Another driving force behind the popularity of convention over configuration is the speed at which new users may pick up a new technology, or the speed by which a seasoned user may begin using the tool without concerning him/herself with details that need not come up until later in the development process. The computer world is finally beginning to embrace the idea that ease of use and reduced configurations do not have to interfere with the power of advanced configurability. Convention and configuration reside together within the Maven world, each providing their own unique perspective of a power tool.

惯例是Maven的核心。惯例大于配置是最近很流行的一句格言，而Maven完全接受了这个概念。惯例大于配置是Ruby on Rails等框架以及最近的EJB3规范的核心理念。在最基本的意义上，它意味着，虽然配置肯定是必要的，但大多数用户永远不会利用那些复杂配置提供的这种边缘情况。虽然一个强大的框架当然需要在必要的时候拥有配置的能力，但是创建默认值来让95%的类似用例在完全不需要定义任何东西的情况下就可以工作，这当然是合理的......系统可以承担这些默认值。换句话说，系统有自己的约定。正因为如此，对于那些遵循Maven惯例的项目来说，像Ant这样的构建工具所需要的庞大配置（其中大部分Ant脚本都是从现有项目中剪切和粘贴过来的）是不存在的。

惯例比配置更受欢迎的另一个驱动力是新用户学习新技术的速度，或者是老用户开始使用工具的速度，而不需要关心那些在开发过程中不需要出现的细节。计算机世界终于开始接受这样的观点，即易用性和减少配置不一定要影响先进的配置能力。约定和配置在Maven世界中共存，各自提供了自己独特的视角的有力工具。

---

> [Convention Over Configuration (markheath.net)](https://markheath.net/post/convention-over-configuration)

**idea is that wherever possible we attempt to remove the need to explicitly configure things, and instead rely on sensible (but overridable) defaults.**

There are a lot of advantages to this convention over configuration approach. First of all, **it makes it very easy to add new components**, simply by copying examples already present in the code. If I need a new orders controller, I can easily understand who to do it by looking at the other controllers. This can make it very easy for people new to the project to extend it.

This is also an excellent example of the “[open closed principle](https://markheath.net/post/essential-developer-principles-4-open)” in action. A convention over configuration approach means that **you can add a new feature without having to change any existing code at all**. You simply add a new class that implements a certain interface or is named in a particular way. This has the side benefit of [eliminating merge conflicts](https://markheath.net/post/solid-code-is-mergeable-code) in classes or files that contain a lot of configuration, which usually experience a lot of “[code churn](https://markheath.net/post/how-to-calculate-code-churn-using-tfs)”.

Convention over configuration is also used commonly with setting up message or command handlers. Simply implement the `IHandle<T>` interface, and some reflection code behind the scenes will discover your handler and wire it up appropriately. Again this makes a developer’s job very easy – need to add a new message or command handler? Just follow the pattern of the other ones.

## drawbacks?

One criticism is that **this kind of approach can seem like “magic”** – making it hard for new starters on a project to understand how it works. Often the IDE support to “find all references” will return nothing when these conventions are being used because reflection is typically used at run-time to discover the methods to be called. It can leave developers wondering “how on earth does this even work”?

Generally speaking, **the more ubiquitous a convention is, the easier it will be for developers to learn and understand.** The conventions in a framework like ASP.NET make sense because they are used over and over again – meaning the time invested in learning them is well spent. But beware of creating lots of conventions that only get used in one or two places. This introduces unnecessary additional learning for developers with minimal benefit.

A particularly painful point can be the mechanism by which you override the “sensible defaults” of the convention. How is a developer supposed to know how to do that? In ASP.NET there are attributes that can be used to override the route used by an action on a controller, which is fine because ASP.NET is well documented, but if it’s a bespoke convention you’ve invented for your project, you’ll need to **make sure all the information about how your convention works is readily available**.

Another disadvantage is that **conventions can sometimes produce unexpected results**. A good example is the fact that because ASP.NET uses reflection to find all classes that inherit from a base Controller class, if someone happens to reference an assembly from another project that also contains some controllers, you might find you’re exposing new endpoints that you weren’t intending to. This happened once on a project I worked on and opened up a serious security hole. This is the reason why **some developers prefer the competing “best practice” of “[explicit is better than implicit](https://www.python.org/dev/peps/pep-0020/)”**. So whenever you use conventions, try to build in protections against these types of unintended consequences.

So, should you adopt “convention over configuration” for your own frameworks? Well **it comes back to the question of what problem you are trying to avoid**. If it’s about eliminating repetitive and redundant configuration code, then it only makes sense to introduce if the convention is going to be applied many times. If its just once or twice, it may not be worth it.

As I said in my “[best practices](https://markheath.net/post/best-practices-do-they-exist)” post, there isn’t one clear right way to approach every problem in software development. **Conventions remove some problems, but introduce others**. So you need to understand the trade-offs in order to make a decision about what makes sense for you. Used judiciously, conventions can help developers fall into the “[pit of success](https://blog.codinghorror.com/falling-into-the-pit-of-success/)” – it should be easier to get it right than to get it wrong.

