---
title: Deadlock
categories:
- OS
tags:
- Deadlock
- wiki
date: 2021/3/10 20:00:17
updated: 2021/3/12 12:00:17
---

# Deadlock

>![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/220px-Deadlock_at_a_four-way-stop.gif)
>
>四个进程（蓝线）按照先右后左的策略竞争一个资源（灰圈）。当所有进程同时锁定资源时，就会出现死锁（黑线）。死锁可以通过打破对称性来解决。

>![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/220px-Two_processes,_two_resources.gif)
>
>两个过程以相反的顺序竞争两种资源。
>
>1. 一个进程通过。
>2. 后面的进程必须等待。
>3. 当第一个进程锁定第一个资源的同时，第二个进程锁定第二个资源时，就会发生死锁。
>4. 死锁可以通过取消并重新启动第一个进程来解决。

在并发计算中，死锁是指一个组的每个成员等待包括自己在内的另一个成员采取行动的状态，如发送消息或更常见的释放锁.[1]死锁是多处理系统、并行计算和分布式系统中常见的问题，在这些系统中，软件和硬件锁被用来仲裁共享资源和实现进程同步.[2]。

在操作系统中，当一个进程或线程因为请求的系统资源被另一个等待进程持有而进入等待状态，而该进程又在等待另一个等待进程持有的资源时，就会发生死锁。如果一个进程因为所请求的资源被另一个等待进程使用而无法无限期地改变其状态，则称系统处于死锁状态[3]。

在通信系统中，发生死锁的原因主要是信号丢失或损坏，而不是资源争夺[4]。

Necessary conditions
--------------------

如果且仅当系统中以下所有条件同时存在时，才会出现资源上的死锁情况：[5]。

1. 相互排斥。至少有一种资源必须以不可共享的方式持有。否则，就不会阻止进程在必要时使用该资源。在任何特定的时刻，只有一个进程可以使用该资源。
2. 持有并等待或资源持有：一个进程目前至少持有一个资源，并请求其他进程持有的额外资源。
3. 不抢占：一个资源只能由持有该资源的进程自愿释放。
4. 循环等待：每个进程都必须等待一个正在被另一个进程持有的资源，而这个进程又在等待第一个进程释放资源。一般来说，有一组等待进程，P={P1，P2，...，PN}，这样，P1在等待P2持有的资源，P2在等待P3持有的资源，以此类推，直到PN在等待P1持有的资源为止[3][7] 。

虽然这些条件足以在单实例资源系统上产生死锁，但它们只表明在具有多个资源实例的系统上有可能产生死锁[8]。

Deadlock handling
-----------------

目前大多数操作系统无法防止死锁的发生[9]当死锁发生时，不同的操作系统会以不同的非标准方式来应对。大多数方法都是通过防止四个Coffman条件中的一个条件发生，尤其是第四个条件[10]，主要方法如下。

### Ignoring deadlock

在这种方法中，假设死锁永远不会发生。这也是Ostrich算法的一种应用[10][11]，这种方法最初被MINIX和UNIX使用[7]，当死锁发生的时间间隔很大，而且每次发生的数据损失是可以容忍的时候，就可以使用这种方法。

如果死锁被正式证明永远不会发生，那么忽略死锁就可以安全地进行。一个例子是RTIC框架[12]。

### Detection

在死锁检测下，允许发生死锁。然后对系统状态进行检查，检测是否发生了死锁，随后对其进行纠正。采用了一种跟踪资源分配和进程状态的算法，它回滚并重新启动一个或多个进程，以消除检测到的死锁。由于操作系统的资源调度器知道每个进程已经锁定的资源和/或当前请求的资源，因此检测已经发生的死锁是很容易实现的。

检测到死锁后，可以使用以下方法之一进行纠正：[引文]

1. 进程终止：可中止一个或多个陷入僵局的进程。可以选择中止所有参与死锁的竞争进程。这样可以确保死锁以确定性和速度得到解决。[引用]但代价很高，因为会损失部分计算。或者，人们可以选择一次中止一个进程，直到死锁被解决。这种方法开销很大，因为在每次中止之后，一个算法必须确定系统是否仍然处于死锁状态。[引文] 在选择终止的候选者时，必须考虑几个因素，如进程的优先级和年龄。
2. 资源抢占：分配给各个进程的资源可以连续抢占，并分配给其他进程，直到打破死锁

### Prevention

>![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/380px-Avoiding_deadlock.gif)
>
>(A)两个进程竞争一个资源，遵循先到先得的政策。(B)当两个进程同时锁定资源时，就会出现死锁。(C)死锁可以通过打破锁的对称性来解决。(D)可以通过打破锁机制的对称性来防止死锁的发生。

Main article: [Deadlock prevention algorithms](/wiki/Deadlock_prevention_algorithms "Deadlock prevention algorithms")

Deadlock prevention works by preventing one of the four Coffman conditions from occurring.

*   去除互斥条件意味着没有进程可以独占资源。事实证明，这对于无法假脱机的资源是不可能的。但即使是有spooled的资源，死锁仍然可能发生。避免互斥的算法称为非阻塞同步算法。
*   通过要求进程在启动前（或在开始进行一组特定的操作前）请求它们所需要的所有资源，可以消除保持和等待或资源保持条件。这种预先了解往往难以满足，而且无论如何，都是一种低效率的资源使用。另一种方法是要求进程只有在它没有资源的时候才请求资源；首先，它们必须先释放它们当前持有的所有资源，然后再从头开始请求它们将需要的所有资源。这也往往是不切实际的。之所以如此，是因为资源可能会被分配并长期闲置。另外，一个需要热门资源的进程可能不得不无限期地等待，因为这样的资源可能总是被分配给某个进程，从而导致资源饥饿[14]（这些算法，如序列化令牌，被称为全或无算法）。
*   不抢占条件也可能是难以或无法避免的，因为一个进程必须在一定时间内能够拥有资源，否则处理结果可能会不一致或发生打乱。然而，无法强制执行抢占可能会干扰优先算法。对 "锁定的 "资源的抢占通常意味着回滚，并且是要避免的，因为它的开销非常大。允许抢占的算法包括无锁和无等待算法以及优化并发控制。如果一个进程持有一些资源，并请求另一些不能立即分配给它的资源，那么可以通过释放该进程当前持有的所有资源来消除条件。
*   最后一个条件是循环等待条件。避免循环等待的方法包括在关键部分禁用中断和使用层次结构来确定资源的部分排序。如果不存在明显的层次结构，甚至用资源的内存地址来确定排序，按照枚举的递增顺序请求资源[3]，也可以使用Dijkstra的解决方案。