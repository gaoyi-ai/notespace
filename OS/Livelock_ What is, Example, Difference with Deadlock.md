---
title: Livelock
categories:
- OS
tags:
- Livelock
date: 2021/3/10 20:00:17
updated: 2021/3/12 12:00:17
---



What is Livelock?
-----------------

A **Livelock** is a situation where a request for an exclusive lock is denied repeatedly, as many overlapping shared locks keep on interfering each other. The processes keep on changing their status, which further prevents them from completing the task. This further prevents them from completing the task.

In this operating system tutorial, you will learn:

*   [What is Livelock?](#1)
*   [Examples of Livelock](#2)
*   [What Leads to Livelock?](#3)
*   [What is Deadlock?](#4)
*   [Example of Deadlock](#5)
*   [What is Starvation?](#6)
*   [Difference Between Deadlock, Starvation, and Livelock](#7)

Examples of Livelock
--------------------

Example 1:

An easiest example of Livelock would be two people who meet face-to-face in a corridor, and both of them move aside to let the other pass. They end up moving from side to side without making any progress as they move the same way at the time. Here, they never cross each other.

Example 2:

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/122319_0813_LivelockWha1.png" style="zoom:67%;" />

You can see in the above image, each of the two given processes needs two resources, and they use the primitive polling enter registry to try to acquire the locks necessary for them. If the attempt fails, the method works again.

1.  Process A hold Y resource
2.  Process B holds resource X
3.  Process A require X resource
4.  Process B require Y resource

Assuming, process A runs first and acquires data resource X and then process B runs and acquires resource Y, no matter which process runs first, none of them further progress.

However, neither of the two processes are blocked. They use up CPU resources repeatedly without any progress being made but also stop any processing block.

Therefore, this situation is not that of a deadlock because there is not a single process that is blocked, but we face the situation something equivalent to deadlock, which is LIVELOCK.

What Leads to Livelock?
-----------------------

Livelock occurs when the total number of allowed processes in a specific system should be defined by the total number of entries in the process table. Therefore, process table slots should be referred to as Finite Resources.

What is Deadlock?
-----------------

A deadlock is a situation that occurs in OS when any process enters a waiting state because another waiting process is holding the demanded resource. Deadlock is a common problem in multi-processing where several processes share a specific type of mutually exclusive resource known as a soft lock or software.

Example of Deadlock
-------------------

*   A real-world example would be traffic, which is going only in one direction.
*   Here, a bridge is considered a resource.
*   So, when Deadlock happens, it can be easily resolved if one car backs up (Preempt resources and rollback).
*   Several cars may have to be backed up if a deadlock situation occurs.
*   So starvation is possible.

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/122319_0813_LivelockWha2.png) Example of deadlock

What is Starvation?
-------------------

Starvation is a situation where all the low priority processes got blocked, and the high priority processes proceed. In any system, requests for high/low priority resources keep on happening dynamically. Thereby, some policy is require to decide who gets support when.

Using some algorithms, some processes may not get the desired serviced even though they are not deadlocked. Starvation occurs when some threads make shared resources unavailable for a long period of time.

### Example of Starvation:

For example, an object offers a synchronized method which likely to take a long time to return. If one thread uses this method frequently, other threads that also need frequent synchronized access to the same object will often be blocked.

Difference Between Deadlock, Starvation, and Livelock
-----------------------------------------------------

*   A deadlock is a situation that occurs in OS when any process enters in a waiting state because the demanded resource is being held by another waiting process.
*   A livelock, on the other hand, is almost similar to a deadlock, except that the states of the processes which are involved in a livelock always keep on changing to one another, none progressing.
*   So, Livelock is a unique case of resource starvation.

### Summary:

*   Definition: A Livelock is a situation where a request for an exclusive lock is denied repeatedly, as many overlapping shared locks keep on interfering each other.
*   Livelock occurs when the total number of allowed processes in a specific system should be defined by the total number of entries in the process table
*   A deadlock is a situation that occurs in OS when any process enters a waiting state because another waiting process is holding the demanded resource.
*   A real-world example would be traffic, which is going only in one direction.
*   An example of Livelock would be two people who meet face-to-face in a corridor, and both of them move aside to let the other pass.
*   Starvation is a situation where all the low priority processes got blocked, and the high priority processes proceed.