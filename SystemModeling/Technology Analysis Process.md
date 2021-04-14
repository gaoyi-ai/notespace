---
title: Technology Analysis Process
categories:
- System Modeling
- Analysis
tags:
- TA
date: 2021/4/13 18:50:45
updated: 2021/4/13 20:00:13
---



> [www.digiteum.com](https://www.digiteum.com/technology-analysis-process/)

_The role of a tech lead or technical expert and solution architect in the software development process._

We always talk about the importance of [Business Analysis (BA) in software development](https://www.digiteum.com/business-analysis-techniques-it/). It’s hard to overestimate the role of a business analyst who helps formulate and translate stakeholders’ requirements, align expectations, and as a result, launch a valuable software product.

However, there’s another major analysis in software development that helps create successful products and deliver a project effectively. This analysis is called technical analysis or technical investigation and is usually fulfilled by a tech lead, solution architect or other tech experts in the team.

As we know, business analysts work on functional requirements, whereas the responsibility of solution architects is to define an integration strategy, find the right platforms and frameworks for the future system and document non-functional requirements.

在软件开发过程中，技术主管或技术专家和解决方案架构师的角色

我们总是谈论[软件开发中的业务分析(BA)](https://www.digiteum.com/business-analysis-techniques-it/)的重要性。很难高估业务分析师的作用，他们帮助制定和翻译涉众的需求，调整预期，并最终推出有价值的软件产品。

然而，在软件开发中还有另一个主要的分析可以帮助创建成功的产品并有效地交付项目。这种分析被称为技术分析或技术调查，通常由团队中的技术负责人、解决方案架构师或其他技术专家来完成。

正如我们所知，业务分析师处理功能需求，而解决方案架构师的职责是定义集成策略，为未来的系统找到合适的平台和框架，并记录非功能需求。

Here are the key steps to make during technology analysis:

Analyze the client’s technology infrastructure
----------------------------------------------

Almost every client has at least some technology infrastructure to start with. By infrastructure, we usually imply hosting, email services, SSL certificates, database servers and any other technology component on the client’s end that will interact with the future system.

In order to understand how to work with the given infrastructure, a tech lead performs the technical investigation and tries to understand how to further use this infrastructure in designing and building a new system or upgrading an existing software.

Usually, if the existing infrastructure is well-built and compatible with a new product, it can be completely re-used. Sometimes, the legacy systems are simply too old and won’t go along with a new product. Then, such infrastructures require a complete upgrade. If this is the case, it’s good to learn about it at the very beginning of the project to [implement relevant prioritization of project requirements](https://www.digiteum.com/product-requirements/).

几乎每个客户都至少有一些技术基础设施可以开始。关于基础设施，我们通常指托管、电子邮件服务、SSL证书、数据库服务器和客户端上任何其他将与未来系统交互的技术组件。

为了理解如何使用给定的基础设施，技术主管进行技术调查，并试图理解如何在设计和构建新系统或升级现有软件时进一步使用该基础设施。

通常，如果现有的基础架构构建良好并与新产品兼容，则可以完全重用它。有时，遗留系统太老了，无法与新产品相适应。然后，这些基础设施需要全面升级。如果是这样的话，最好在项目的一开始就了解它[实现项目需求的相关优先级](https://www.digiteum.com/product-requirements/)。

At this stage of technology analysis, tech leads or solution architects also try to find out how to integrate the client’s services into a new system.

For example, if a new app is supposed to use some old backend system, a technical analyst should define the capabilities and limitations for such integration. It is worth discussing the integration opportunities with a backend development team in the first place. In some cases, when direct integration is impossible, the scope may include the development of a private REST API to connect the backend with the app.

在技术分析的这一阶段，技术主管或解决方案架构师也试图找出如何将客户的服务集成到一个新系统中。

例如，如果一个新的应用程序应该使用一些旧的后端系统，技术分析师应该定义这种集成的能力和限制。首先有必要讨论与后端开发团队的集成机会。在某些情况下，当不可能直接集成时，范围可能包括开发私有REST API来连接后端和应用程序。

Analyze the solutions that will work as the basis for a new system and its components
-------------------------------------------------------------------------------------

There is no point in reinventing a wheel. Custom software should benefit from a wide range of platforms, frameworks and services available in the market.

For example, we are building an app or a website that should display certain content managed by an admin. This app or website may be stuffed with custom unique features which require serious design and development efforts. However, adding the component responsible for content management is a no-brainer. Today, it can be built on top of a time-proven CMS platform such as a WordPress, SiteScore, etc.

There are plenty of examples when platform integration actually speeds up the development. Different CRM systems, e-commerce platforms, mail services are among the most common out-of-the-box solutions used and customized for these purposes. The main task of a technology expert at this stage is to [find and compare potential third-party software solutions](https://www.digiteum.com/how-to-compare-software-solutions-frameworks-libraries-and-other-components/), validate them against given business requirements and choose the right ones for the project.

重新发明轮子是没有意义的。定制软件应该从市场上广泛的平台、框架和服务中受益。

例如，我们正在构建一个应用程序或一个网站，应该显示由管理员管理的某些内容。这个应用程序或网站可能塞满了自定义独特的功能，这需要认真的设计和开发努力。然而，添加负责内容管理的组件是很容易的。现在，它可以建立在一个经过时间验证的CMS平台上，比如WordPress、SiteScore等。

平台集成实际上加速了开发的例子有很多。不同的CRM系统、电子商务平台、邮件服务是用于这些目的的最常见的开箱即用的解决方案。技术专家在这个阶段的主要任务是[发现和比较潜在的第三方软件解决方案](https://www.digiteum.com/how-to-compare-software-solutions-frameworks-libraries-and-other-components/),验证它们对给定的业务需求,选择合适的项目。

Analyze third-party APIs required for the development
-----------------------------------------------------

Nowadays, almost any new system requires certain integrations. It can be a payment gateway, social networks or domain specific services and platforms usually integrated via API.

Sometimes, the list of API is provided by the client. Often, it’s the responsibility of a tech lead to [find an appropriate library or framework](https://www.digiteum.com/how-to-compare-software-solutions-frameworks-libraries-and-other-components/) that can be useful in the course of the project development.

For this purpose, it’s useful to dig through the Internet, choose the most popular libraries or other solutions in a specific business domain, study the approaches competitors of other companies use to solve similar problems and learn from the experience in the company.

When defining the list of libraries, APIs or frameworks that could be used in the project, tech experts may need to [compare several software solutions](https://www.digiteum.com/how-to-compare-software-solutions-frameworks-libraries-and-other-components/).

如今，几乎所有的新系统都需要某些集成。它可以是支付网关、社交网络或特定领域的服务和平台，通常通过API集成。

有时，API列表是由客户机提供的。通常，技术主管的职责是[找到一个合适的库或框架](https://www.digiteum.com/how-to-compare-software-solutions-frameworks-libraries-and-other-components/)，它在项目开发过程中非常有用。

为此，在互联网上进行挖掘，选择特定业务领域中最流行的库或其他解决方案，研究其他公司的竞争对手解决类似问题的方法，并学习该公司的经验是很有用的。

在定义项目中可能使用的库、api或框架列表时，技术专家可能需要[比较几种软件解决方案](https://www.digiteum.com/how-to-compare-software-solutions-frameworks-libraries-and-other-components/)。

Once the final list of integrations is defined, tech specialists perform the analysis that usually consists of the following steps:

*   Reviewing relevant documentation,
*   Matching endpoints/features and product requirements,
*   Analyzing how these solutions could be used in the future (important for the features, which are out of the project’s scope now but can be included later)
*   Validating API, framework, platform or other solutions’ limitations against the given requirements,
*   Validating API, framework, platform or other solutions’ performance capabilities against the given requirements/established project goals,
*   Analyzing how to integrate API, framework, platform or other solutions in the existing infrastructure.

Document non-functional requirements
------------------------------------

As a rule, business analysts discuss business goals and requirements with a client. Technology analysts, in turn, collect and document non-functional requirements.

Non-functional requirements are abundant. The most common and important ones are the following:

### Security

Security requirements include:

*   protection against unauthorized access to the system and its data,
*   authorization and authentication across different user roles,
*   data privacy,
*   prevention and protection against cyber attacks and malware.

### Reliability

This requirement determines the ability of a system to perform under certain conditions in a certain environment. It includes:

*   consistent system performance without failure for a period of time,
*   the impact of bugs, hardware malfunction, other problems with system components,
*   the quality of task implementation.

### Performance

Performance measures the quality of system interaction with users. It includes:

*   user experience quality,
*   system safety against overloads,
*   system responsiveness to different user interactions.

### Scalability

These requirements imply system growth capacity. It includes:

*   the ability to provide more users with the service of the same quality,
*   processing more data or transactions,
*   the capacity for memory, space, speed growth.

These are the important non-functional requirements for almost every software development project. If these requirements are not taken into consideration at the beginning of a project, it entails serious risks which may end up with additional costs and time loss.

The results of technical investigation
--------------------------------------

When tech leads or solution architects are done with all the steps of technical investigation and analysis, they record the results and create the following artifacts:

*   **Technology stack description.** This description includes the programming languages, databases, servers, hosting and third-party libraries recommended to use on the project.
*   **The architectural diagram of a product.** Usually, the diagram shows different project components with their roles and functionality and the relations between different components of the future system.
*   **Non-functional requirements specification.** This is a separate part of the functional specification which describes non-functional requirements – security, reliability, scalability, performance, etc.

I believe a balanced combination of business and technology analyses leads to the excellence of a software development process and a final product. In my experience, the more thoughts you put into the future system at the beginning of the project, the easier and faster the development will go and the better results you get in the end.