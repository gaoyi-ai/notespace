---
title: Dining Philosophers Problem -Wiki
categories:
- OS
tags:
- deadlock
- starvation
date: 2021/7/13
---



# Dining philosophers problem

## Problem statement

Five silent [philosophers](https://en.wikipedia.org/wiki/Philosopher) sit at a round table with bowls of [spaghetti](https://en.wikipedia.org/wiki/Spaghetti). Forks are placed between each pair of adjacent philosophers.

Each philosopher must alternately think and eat. However, a philosopher can only eat spaghetti when they have both left and right forks. Each fork can be held by only one philosopher and so a philosopher can use the fork only if it is not being used by another philosopher. After an individual philosopher finishes eating, they need to put down both forks so that the forks become available to others. A philosopher can only take the fork on their right or the one on their left as they become available and they cannot start eating before getting both forks.

Eating is not limited by the remaining amounts of spaghetti or stomach space; an infinite supply and an infinite demand are assumed.

The problem is how to design a discipline of behavior (a [concurrent](https://en.wikipedia.org/wiki/Concurrency_(computer_science)) [algorithm](https://en.wikipedia.org/wiki/Algorithm)) such that no philosopher will starve; *i.e.*, each can forever continue to alternate between eating and thinking, assuming that no philosopher can know when others may want to eat or think.

### Problems

The problem was designed to illustrate the challenges of avoiding [deadlock](https://en.wikipedia.org/wiki/Deadlock), a system state in which no progress is possible. To see that a proper solution to this problem is not obvious, consider a proposal in which each philosopher is instructed to behave as follows:

- think until the left fork is available; when it is, pick it up;
- think until the right fork is available; when it is, pick it up;
- when both forks are held, eat for a fixed amount of time;
- then, put the right fork down;
- then, put the left fork down;
- repeat from the beginning.

This attempted solution fails because it allows the system to reach a deadlock state, in which no progress is possible. This is a state in which each philosopher has picked up the fork to the left, and is waiting for the fork to the right to become available. With the given instructions, this state can be reached, and when it is reached, each philosopher will eternally wait for another (the one to the right) to release a fork.[[4\]](https://en.wikipedia.org/wiki/Dining_philosophers_problem#cite_note-4)

[Resource starvation](https://en.wikipedia.org/wiki/Resource_starvation) might also occur independently of deadlock if a particular philosopher is unable to acquire both forks because of a timing problem. For example, there might be a rule that the philosophers put down a fork after waiting ten minutes for the other fork to become available and wait a further ten minutes before making their next attempt. This scheme eliminates the possibility of deadlock (the system can always advance to a different state) but still suffers from the problem of [livelock](https://en.wikipedia.org/wiki/Deadlock#Livelock). If all five philosophers appear in the dining room at exactly the same time and each picks up the left fork at the same time the philosophers will wait ten minutes until they all put their forks down and then wait a further ten minutes before they all pick them up again.

[Mutual exclusion](https://en.wikipedia.org/wiki/Mutual_exclusion) is the basic idea of the problem; the dining philosophers create a generic and abstract scenario useful for explaining issues of this type. The failures these philosophers may experience are analogous to the difficulties that arise in real computer programming when multiple programs need exclusive access to shared resources. These issues are studied in [concurrent programming](https://en.wikipedia.org/wiki/Concurrent_programming). The original problems of Dijkstra were related to external devices like tape drives. However, the difficulties exemplified by the dining philosophers problem arise far more often when multiple processes access sets of data that are being updated. Complex systems such as [operating system](https://en.wikipedia.org/wiki/Operating_system) [kernels](https://en.wikipedia.org/wiki/Kernel_(operating_system)) use thousands of [locks](https://en.wikipedia.org/wiki/Lock_(computer_science)) and [synchronizations](https://en.wikipedia.org/wiki/Synchronization_(computer_science)) that require strict adherence to methods and protocols if such problems as deadlock, starvation, and data corruption are to be avoided.

## Solutions

### Resource hierarchy solution

While the resource hierarchy solution avoids deadlocks, it is not always practical, especially when the list of required resources is not completely known in advance. For example, if a unit of work holds resources 3 and 5 and then determines it needs resource 2, it must release 5, then 3 before acquiring 2, and then it must re-acquire 3 and 5 in that order. Computer programs that access large numbers of database records would not run efficiently if they were required to release all higher-numbered records before accessing a new record, making the method impractical for that purpose.[[2\]](https://en.wikipedia.org/wiki/Dining_philosophers_problem#cite_note-formalization-2)

- An even philosopher should pick the right chopstick and then the left chopstick while an odd philosopher should pick the left chopstick and then the right chopstick.

The resource hierarchy solution is not *fair*. If philosopher 1 is slow to take a fork, and if philosopher 2 is quick to think and pick its forks back up, then philosopher 1 will never get to pick up both forks. A fair solution must guarantee that each philosopher will eventually eat, no matter how slowly that philosopher moves relative to the others.

### Arbitrator solution

Another approach is to guarantee that a philosopher can only pick up both forks or none by introducing an arbitrator, e.g., a waiter. In order to pick up the forks, a philosopher must ask permission of the waiter. The waiter gives permission to only one philosopher at a time until the philosopher has picked up both of their forks. Putting down a fork is always allowed. The waiter can be implemented as a [mutex](https://en.wikipedia.org/wiki/Mutex). In addition to introducing a new central entity (the waiter), this approach can result in reduced parallelism: if a philosopher is eating and one of his neighbors is requesting the forks, all other philosophers must wait until this request has been fulfilled even if forks for them are still available.

- A philosopher should only be allowed to pick their chopstick if both are available at the same time.

### Limiting the number of diners in the table

- There should be at most four philosophers on the table.

A solution presented by [William Stallings](https://en.wikipedia.org/wiki/William_Stallings)[[5\]](https://en.wikipedia.org/wiki/Dining_philosophers_problem#cite_note-5) is to allow a maximum of *n-1* philosophers to sit down at any time. The last philosopher would have to wait (for example, using a semaphore) for someone to finish dining before they "sit down" and request access to any fork. This guarantees at least one philosopher may always acquire both forks, allowing the system to make progress.

### Chandy/Misra solution

In 1984, [K. Mani Chandy](https://en.wikipedia.org/wiki/K._Mani_Chandy) and [J. Misra](https://en.wikipedia.org/wiki/Jayadev_Misra)[[6\]](https://en.wikipedia.org/wiki/Dining_philosophers_problem#cite_note-6) proposed a different solution to the dining philosophers problem to allow for arbitrary agents (numbered *P*1, ..., *P**n*) to contend for an arbitrary number of resources, unlike Dijkstra's solution. It is also completely distributed and requires no central authority after initialization. However, it violates the requirement that "the philosophers do not speak to each other" (due to the request messages).

1. For every pair of philosophers contending for a resource, create a fork and give it to the philosopher with the lower ID (*n* for agent *P**n*). Each fork can either be *dirty* or *clean.* Initially, all forks are dirty.
2. When a philosopher wants to use a set of resources (*i.e.*, eat), said philosopher must obtain the forks from their contending neighbors. For all such forks the philosopher does not have, they send a request message.
3. When a philosopher with a fork receives a request message, they keep the fork if it is clean, but give it up when it is dirty. If the philosopher sends the fork over, they clean the fork before doing so.
4. After a philosopher is done eating, all their forks become dirty. If another philosopher had previously requested one of the forks, the philosopher that has just finished eating cleans the fork and sends it.

This solution also allows for a large degree of concurrency, and will solve an arbitrarily large problem.

It also solves the starvation problem. The clean/dirty labels act as a way of giving preference to the most "starved" processes, and a disadvantage to processes that have just "eaten". One could compare their solution to one where philosophers are not allowed to eat twice in a row without letting others use the forks in between. Chandy and Misra's solution is more flexible than that, but has an element tending in that direction.

In their analysis, they derive a system of preference levels from the distribution of the forks and their clean/dirty states. They show that this system may describe a [directed acyclic graph](https://en.wikipedia.org/wiki/Directed_acyclic_graph), and if so, the operations in their protocol cannot turn that graph into a cyclic one. This guarantees that deadlock cannot occur. However, if the system is initialized to a perfectly symmetric state, like all philosophers holding their left side forks, then the graph is cyclic at the outset, and their solution cannot prevent a deadlock. Initializing the system so that philosophers with lower IDs have dirty forks ensures the graph is initially acyclic.

## See also

- [Cigarette smokers problem](https://en.wikipedia.org/wiki/Cigarette_smokers_problem)
- [Producers-consumers problem](https://en.wikipedia.org/wiki/Producers-consumers_problem)
- [Readers-writers problem](https://en.wikipedia.org/wiki/Readers-writers_problem)
- [Sleeping barber problem](https://en.wikipedia.org/wiki/Sleeping_barber_problem)