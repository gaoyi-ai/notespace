---
title: STRIPS and the joy of planning
categories:
- AI
- STRIPS
tags:
- STRIPS
date: 2021/9/22
---



> [medium.com](https://medium.com/the-ai-guys/strips-and-the-joy-of-planning-f85d522ae36c)

> Until now, most of the agents that we’ve described have been pretty straightforward: there’s an objec......

STRIPS and the joy of planning
==============================

In which they geek about AI that can beat you in Starcraft
----------------------------------------------------------

Until now, most of the agents that we’ve described have been pretty straightforward: there’s an objective, and the agent must follow a series of steps to reach it.

But what if we only know what kind of _actions_ our agent can do in order to reach the goal? there are no definite set of steps to follow, only those actions, the initial state and our objective.

This is when planning enters the scene, because that’s exactly what planning is: setting a series of actions to get from where we are now to where we want to be. We don’t meddle with the fine details yet, we are just getting the big picture of the problem.

![](https://miro.medium.com/max/1180/1*9s1eVLrUTD-2_4BcM5Eu2A.png)It can even be used to play an [automated game of Age of Empires](https://steamcommunity.com/sharedfiles/filedetails/?id=137724532)!

But planning is costly, very costly, it requires us to think of a problem and invest time in something that it’s not solving the problem directly. So why would we want to plan? The answer is simple: because tackling some problems directly, when they are too large or difficult, or involve too much other factors, is a far worse idea than to spend some resources beforehand to get a solution faster, even if that solution is not always the best one.

There are two strategies for planning:

*   Planning for specific domains, which is what we’re going to discuss today
*   Domain-independent planning, which is mainly logic-based, as we shown in our [last post](/the-ai-guys/about-logical-agents-dab799d041f3).

Since we have an **initial state**, and a set of **actions** each containing it’s **prerequisites** and it’s **effects** on the current state, we may be tempted to use some search algorithm to go through the tree that appears by applying every action possible until we reach our goal state; and thats a pretty fair way to solve planned problems, but this brings trouble: some branches of the action tree might cycle out after some iterations, which means we’re never gonna get to the goal state (the plan is not complete), and the **branching factor** (the number of children of the node) of each node in the tree might be so big that using any search algorithm to iterate it over and over again is no longer useful.

This is why most implementations of plans for AI nowadays use some form of logic-based approach, but using a special planning algorithm.

**STRIPS** (which stands for **Stanford Research Institute Problem Solver**) is a very well known panning language and is easy to learn because it uses **First-Order Logic** (FOL) notation to state states and actions.

In STRIPS, every state is a conjunction of FOL statements take for example, the well known **Monkey and Banana** problem: there are three rooms one next another, we’ll call them A, B and C (how original), our monkey is in room A, and hanging at the top in room B there are some bananas that our monkey wants to have (and consequently eat). But in order to get the bananas the monkey must first climb onto a box that is located in room C.

![](https://miro.medium.com/max/1400/1*X2NXWR-oFGBAVAx8V2j0aA.png)Apparently, there was a real-life research of the problem…

So our initial state in STRIPS would be:

`down(monkey) ∧ down(box) ∧ up(banana) ∧ at(monkey, A) ∧ at(box, C) ∧ at(banana, B)`

And our goal state would be something like this:

`take(monkey, banana)`

Is important to note that in STRIPS, states cannot have negative statements (those that start with a `¬` symbol).

![](https://miro.medium.com/max/840/0*fm4Ua14t3UbF6nkg.)

STRIPS takes care of expanding the nodes and doing the search to reach the best solution with the given context of the problem.

It uses three search strategies: depth-first, breadth-first, and [A*](https://en.wikipedia.org/wiki/A*_search_algorithm) (A-Star).

For the case of the monkey and bananas problem, the program can find a solution in four steps:

1.  The monkey moves from C to A.
2.  The monkey pushes the box from A to B.
3.  The monkey climbs the box on B.
4.  Lastly, the monkey takes the bananas on B.

And it sounds kind of logical, right? That would be the intuitive way in which any of us would do it.