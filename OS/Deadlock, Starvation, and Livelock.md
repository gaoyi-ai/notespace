---
title: Deadlock, Starvation, and Livelock
categories:
- OS
tags:
- Deadlock
- Starvation
- Livelock
date: 2021/3/10 20:00:17
updated: 2021/3/12 12:00:17
---

# Deadlock Starvation and Livelock

Prerequisite – [Deadlock and Starvation](https://www.geeksforgeeks.org/deadlock-starvation-java/)  
**Livelock** occurs when two or more processes continually repeat the same interaction in response to changes in the other processes without doing any useful work. These processes are not in the waiting state, and they are running concurrently. This is different from a deadlock because in a deadlock all processes are in the waiting state.

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/aaa-1.png)

**Example:**  
Imagine a pair of processes using two resources, as shown:

```cpp
void process_A(void) 
{ 
	enter_reg(& resource_1); 
	enter_reg(& resource_2); 
	use_both_resources(); 
	leave_reg(& resource_2); 
	leave_reg(& resource_1); 
} 
void process_B(void) 
{ 
	enter_reg(& resource_1); 
	enter_reg(& resource_2); 
	use_both_resources(); 
	leave_reg(& resource_2); 
	leave_reg(& resource_1); 
} 
```

两个进程中的每一个都需要这两个资源，它们使用轮询基元enter_reg尝试获取它们所需的锁。如果尝试失败，该进程就会再次尝试。

如果进程A先运行并获取资源1，然后进程B再运行并获取资源2，无论哪个进程接下来运行，都不会再有任何进展，但两个进程都不会阻塞。实际上发生的情况是，它一次又一次地用掉了它的CPU quantum ，却没有任何进展，但也没有任何形式的阻塞。因此，这种情况不是死锁（因为没有进程被阻塞），但我们有一些功能上相当于死锁的东西。LIVELOCK.

**What leads to Livelocks?**  
livelocks的发生可能以最令人惊讶的方式发生。在某些系统中，允许的进程总数，是由进程表的条目数决定的。因此，进程表槽可以被称为有限资源。如果因为表满而导致fork失败，那么对于做fork的程序来说，随机等待一段时间再尝试是一个合理的做法。

考虑一个有100个进程槽的UNIX系统。10个程序正在运行，每个程序要创建12个（子）进程。在每个进程创建了9个进程后，10个原始进程和90个新进程已经用完了表。10个原始进程中的每一个现在都坐在一个无尽的循环中分叉和失败--这恰恰是死锁的情况。这种情况发生的概率非常小，但有可能发生。

**Difference between Deadlock, Starvation, and Livelock:**  
Livelock类似于死锁，只是参与livelock的进程的状态彼此不断变化，没有一个进程在进步。活锁是资源饥饿的一种特殊情况；一般的定义只是说某一特定进程没有进展。

**Livelock:**

```java
var l1 = .... // lock object like semaphore or mutex etc 
var l2 = .... // lock object like semaphore or mutex etc 
	
		// Thread1 
		Thread.Start( ()=> { 
	while (true) { 
		if (!l1.Lock(1000)) { 
			continue; 
		} 
		if (!l2.Lock(1000)) { 
			continue; 
		} 
		/// do some work 
	}); 

		// Thread2 
		Thread.Start( ()=> { 	
		while (true) { 		
			if (!l2.Lock(1000)) { 
				continue; 
			} 		
			if (!l1.Lock(1000)) { 
				continue; 
			} 
			// do some work 
		}); 
```

**Deadlock:**

```java
var p = new object(); 
lock(p) 
{ 
	lock(p) 
	{ 
		// deadlock. Since p is previously locked 
		// we will never reach here... 
	} 
```

Deadlock是指一组行动中的每个成员都在等待其他成员释放锁的状态。而Livelock则与死锁几乎相似，只是Livelock中涉及的进程的状态不断地相互变化，没有一个进程在进步。因此，Livelock是资源饥饿的一种特殊情况，正如一般定义中所说的那样，进程没有进展。

**Starvation:** 
饥饿是一个与Livelock和Deadlock都密切相关的问题。在一个动态系统中，对资源的请求不断发生。因此，需要一些政策来决定谁在什么时候获得资源。这个过程是合理的，但可能会导致一些进程永远得不到服务，即使它们没有被死锁。

```java
Queue q = ..... 

		while (q.Count & gt; 0) 
{ 
	var c = q.Dequeue(); 
	......... 

		// Some method in different thread accidentally 
		// puts c back in queue twice within same time frame 
		q.Enqueue(c); 
	q.Enqueue(c); 

	// leading to growth of queue twice then it 
	// can consume, thus starving of computing 
} 
```

当 "贪婪 "的线程使共享资源长时间不可用时，就会发生饥饿现象。例如，假设一个对象提供了一个经常需要很长时间才能返回的同步方法。如果一个线程频繁调用这个方法，其他同样需要频繁同步访问同一个对象的线程往往会被阻塞。

---

Another example on Livelock:

```java
public class Livelock {
    static class Spoon {
        private Diner owner;
        public Spoon(Diner d) { owner = d; }
        public Diner getOwner() { return owner; }
        public synchronized void setOwner(Diner d) { owner = d; }
        public synchronized void use() { 
            System.out.printf("%s has eaten!", owner.name); 
        }
    }

    static class Diner {
        private String name;
        private boolean isHungry;

        public Diner(String n) { name = n; isHungry = true; }       
        public String getName() { return name; }
        public boolean isHungry() { return isHungry; }

        public void eatWith(Spoon spoon, Diner spouse) {
            while (isHungry) {
                // Don't have the spoon, so wait patiently for spouse.
                if (spoon.owner != this) {
                    try { Thread.sleep(1); } 
                    catch(InterruptedException e) { continue; }
                    continue;
                }                       

                // If spouse is hungry, insist upon passing the spoon.
                if (spouse.isHungry()) {                    
                    System.out.printf(
                        "%s: You eat first my darling %s!%n", 
                        name, spouse.getName());
                    spoon.setOwner(spouse);
                    continue;
                }

                // Spouse wasn't hungry, so finally eat
                spoon.use();
                isHungry = false;               
                System.out.printf(
                    "%s: I am stuffed, my darling %s!%n", 
                    name, spouse.getName());                
                spoon.setOwner(spouse);
            }
        }
    }

    public static void main(String[] args) {
        final Diner husband = new Diner("Bob");
        final Diner wife = new Diner("Alice");

        final Spoon s = new Spoon(husband);

        new Thread(new Runnable() { 
            public void run() { husband.eatWith(s, wife); }   
        }).start();

        new Thread(new Runnable() { 
            public void run() { wife.eatWith(s, husband); } 
        }).start();
    }
}
```

---

# 什么是进程的饥饿和饿死？

在一个动态系统中，资源请求与释放是经常性发生的进程行为．对于每类系统资源，操作系统需要确定一个分配策略，当多个进程同时申请某类资源时，由分配策略确定资源分配给进程的次序。 资源分配策略可能是公平的(fair)，能保证请求者在有限的时间内获得所需资源；资源分配策略也可能是不公平的(unfair)，即不能保证等待时间上界的存在。 在后一种情况下，即使系统没有发生死锁，某些进程也可能会长时间等待．当等待时间给进程推进和响应带来明显影响时，称发生了进程饥饿(starvation)，当饥饿到一定程度的进程所赋予的任务即使完成也不再具有实际意义时称该进程被饿死(starve to death)。

考虑一台打印机分配的例子，当有多个进程需要打印文件时，系统按照短文件优先的策略排序，该策略具有平均等待时间短的优点，似乎非常合理，但当短文件打印任务源源不断时，长文件的打印任务将被无限期地推迟，导致饥饿以至饿死。

当等待时间给进程的推进和相应带来明显的影响时，就称发生了进程饥饿。当饥饿到一定程度的进程所赋予的任务即使完成也不再具有实际意义时，称该进程被饿死。

当一组进程到达时，CPU根据算法进行进程调度。有的进程因此而需要等待，而不能及时得到资源，这就叫饥饿。进程得到资源时，再完成已经不再具有意义，这就叫做饿死。

Starvation is simply when a process or service is not being serve, even when there is no deadlock on the system.

This is an example I just made up just for clarification purposes.

Imagine an algorithm that control computers access to a WAN or something like that. This algorithm could have a policy that says "Provide priority access to those computers that will use less bandwidth.", that will seem like a proper policy, but then what happens when a single computer wants to access the network for an ftp upload that will send several GB somewhere. With this policy alone, that computer will starve since the algorithm will never select that computer, since there will be always other computers requesting smaller bandwidth usage.

That is called starvation.


 