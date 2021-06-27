---
title: what-is-the-difference-between-compiling-and-building
categories:
- Build Tools
tags:
- compile
- build
date: 2021/6/26
---



# [what-is-the-difference-between-compiling-and-building](https://stackoverflow.com/questions/2310261/what-is-the-difference-between-compiling-and-building)

**Compiling** is the act of turning source code into object code.

**Linking** is the act of combining object code with libraries into a raw executable.

**Building** is the sequence composed of compiling and linking, with possibly other tasks such as installer creation.

Many compilers handle the linking step automatically after compiling source code.

---

The "Build" is a process that covers all the steps required to create a "deliverable" of your software. In the Java world, this typically includes:

1. Generating sources (sometimes).
2. Compiling sources.
3. Compiling test sources.
4. Executing tests (unit tests, integration tests, etc).
5. Packaging (into jar, war, ejb-jar, ear).
6. Running health checks (static analyzers like Checkstyle, Findbugs, PMD, test coverage, etc).
7. Generating reports.

So as you can see, compiling is only a (small) part of the build (and the best practice is to fully automate all the steps with tools like Maven or Ant and to run the build continuously which is known as [Continuous Integration](http://martinfowler.com/articles/continuousIntegration.html)).