---
title:  Why Continuous Integration Doesn’t Work
categories:
- SE
- Continuous Integration
tags:
- CI
date: 2021/6/30
---

> [Why Continuous Integration Doesn't Work (devops.com)](https://devops.com/continuous-integration-doesnt-work/)

![img](https://devops.com/wp-content/uploads/2014/09/continuous_header.png)

# Why Continuous Integration Doesn’t Work

Continuous integration is easy. Download Jenkins, install, create a job, click the button, and get a nice email saying that your build is broken (I assume your build is automated). Then, fix broken tests (I assume you have tests), and get a much better looking email saying that your build is clean. Then, tweet about it, claiming that your team is using continuous integration.

I’ve seen it multiple times in multiple projects. The start is always bright and smooth. Problems start later when the build gets broken again and we simply don’t have time to fix it. We’re simply busy doing something else and a few broken unit tests shouldn’t be a distraction for us. After all, we all know that unit testing is not for a team working with deadlines, right?

Wrong. Continuous integration can and must work. The latest build should always be clean. Always.

# What is Continuous Integration?

Nowadays, software development is done in teams. We develop in [feature branches](http://martinfowler.com/bliki/FeatureBranch.html) and isolate changes while they are in development. Then, we merge branches into `master` (Git usage is assumed). After every merge, we test the entire product, executing all available unit and integration tests. This is what is called [continuous integration](https://en.wikipedia.org/wiki/Continuous_integration), abbreviated as CI.

Sometimes, some tests fail. When this happens, we say that our “build is broken”. Such a failure is a positive side effect of quality control because it raises a red flag immediately after an error gets into `master`. It is a well-known practice, when fixing that error becomes a top priority for its author and the entire team. The error should be fixed right after a red flag is raised by the continuous integration server.

**# Why Continuous Integration Doesn’t Work?**

CI is great, but the bigger the team (and the code base), the more often builds get broken. And, the longer it takes to fix them. I’ve seen many examples where a hard working team starts to ignore red flags, raised by Jenkins, after a few weeks or trying to keep up.

The team simply becomes incapable of fixing all errors in time. Mostly because the business has other priorities. Product owners do not understand the importance of a “clean build” and technical leaders can’t buy time for fixing unit tests. Moreover, the code that broke them was already in `master` and, in most cases, has been already deployed to production and delivered to end-users. What’s the urgency of fixing some tests if business value was already delivered?

In the end, most development teams don’t take continuous integration alerts seriously. Jenkins or Travis are just fancy tools for them that play no role in the entire development and delivery pipeline. No matter what continuous integration server says, we still deliver new features to our end-users. We’ll fix our build later. And it’s only logical.

**# What Is a Solution?**

The solution I propose is simple — prohibit anyone from merging anything into `master` and create scripts that anyone can call. The script will merge, test, and commit. The script will not make any exceptions. If any branch is breaking at even one unit test, the entire branch will be rejected.

In other words, we should raise that red flag **before** the code gets into `master`. We should put the blame for broken tests on the shoulders of its author.

Say, I’m developing a feature in my own branch. I finished the development and broke a few tests, accidentally. It happens, we all make mistakes. I can’t merge my changes into `master`. Git simply rejects my `push`, because I don’t have the appropriate permissions. All I can do is call a magic script, asking it to merge my branch. The script will try to merge, but before pushing into `master`, it will run all tests. And if any of them break, my branch will be rejected. My changes won’t be merged. Now it’s my responsibility — to fix them and call the script again.

In the beginning, this approach slows down the development, because everybody has to start writing cleaner code. At the end, though, this method pays off big time.

In my projects I automate this merging operation using www.rultor.com, a free hosted DevOps service. Its usage is explained in this article: [Rultor.com, a Merging Bot](http://www.yegor256.com/2014/07/24/rultor-automated-merging.html). I would be glad to answer your questions, just shoot me an email.