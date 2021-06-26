---
title: Dynamic Programming - Memoization vs Tabulation
categories:
- DSA
- Algorithm
- Dynamic Programming
tags:
- dynamic programming
- memoization 
- tabulation
date: 2021/6/25
---



# Dynamic Programming: Memoization vs Tabulation

## Building better applications with dynamic programming

At the beginning of your programming journey, all you hoped was that your programs run without any bugs. But when you grow as a developer, you would start thinking about other aspects of your program. Core aspects such as code readability, memory usage, and performance are equally important to experienced programmers as they build up their application.

If you want to start building better applications, you need to start learning advanced concepts. One such concept is known as Dynamic Programming. Although I referred to dynamic programming as an advanced concept, it is quite easy to understand and with enough practice, you can easily become an expert in it. Dynamic programming is an approach to solving algorithmic problems, in order to receive a solution that is more efficient than a naïve solution.

You can read more about dynamic programming in my article over here.

[JavaScript Dynamic Programming with Ugly NumbersUnderstand the concepts of dynamic programmingblog.bitsrc.io](https://blog.bitsrc.io/javascript-dynamic-programming-with-ugly-numbers-63d5658113e4)

There are two major approaches to solving a problem with dynamic programming.

- Top-down approach with Memoization
- Bottom-up approach with Tabulation

# Memoization — Recap

Under this approach, we try to solve a problem by recursively breaking it into smaller problems. These smaller problems are then solved one after the other. Due to these sub-problems overlapping, we store the results of each of these sub-problems in a cache or temporary memory store and use these values when needed later. This will reduce the time spent computing the result, as we would retrieve the value stored in the cache from a previous computation of the same sub-problem.

This technique of storing results of the computed sub-problems is called Memoization. You can read more about Memoization in my article over here.

[A Beginner’s Guide to Memoization with JavaScriptStart implementing efficient functions with memoizationblog.bitsrc.io](https://blog.bitsrc.io/a-beginners-guide-to-memoization-with-javascript-59d9c818f4c8)

This approach is called the top-down approach because we start solving the main problem initially. In other words, we start with the given n and recursively compute until we reach the base problem. And once the base problem is solved, we return the values to each parent-problem, ultimately receiving the final value for the main problem.

# Tabulation — Recap

Tabulation, on the other hand, avoids recursion. This approach is somewhat of a total upside of the top-down approach. Rather than starting from the main problem, we start solving the sub-problems and move our way up. The tabulation approach is done by filling up a table of n-dimension, hence the name **tabulation**. The results stored in the table are then used to compute the output of the original problem.

# Memoization vs Tabulation

Although both memoization and tabulation work by accessing stored results, they slightly differ due to the way these values are calculated and stored. In other words, both these techniques sacrifice space complexity to get better time complexity.

Under memoization, the order in which we solve the subproblems are not important as it is a top-down approach. All the results of the subproblems will be used to calculate the result of the main problem at the end. But with tabulation, this will not be the case. Since tabulation is a bottom-up approach, we should be careful in selecting the order in which the sub-problems are solved.

Furthermore, we will have to solve each and every subproblem with tabulation as we do not know whether this subproblem will contribute to the result of the main problem. But with memoization, we are able to identify the subproblems that will contribute to the result of the main problem thanks to the top-down approach. If a subproblem does not contribute to the result of the main problem, we can simply skip its calculation.

I have decided to skip the more technical and theoretical part of dynamic programming in this article as it is out of scope. But if you would like to learn dynamic programming in detail, here is a [resource](https://www2.cs.duke.edu/courses/spring16/compsci330/Notes/dynamic.pdf) that I personally found very useful.

# Under the microscope

## Semantic Complexity

It is easier to translate the naive solution to the problem into a memoized solution compared to a tabulated solution. The conversion process is straightforward with memoization as you have to translate the algorithm into code.

But with tabulation, you will have to consider the order of subproblems being solved. Therefore more effort is needed when it comes to tabulation.

## Memory/Storage

In terms of the memory occupied, tabulation gives us the ability to further enhance our application by using lesser memory. If the order of the subproblems is carefully considered, the results of certain subproblems will not be needed to be used again. This allows us to reduce the size of the memory required by our algorithm. As [Awjin](https://awjin.me/) says, a perfect simplified example for this would be the tabulation of Fibonacci numbers. Since our result is only dependent on the last two results, we can simply discard the previously calculated values. This will give us a sliding window that enables us to only store two results at a given time.

With memoization, this would not be the case. Since we cannot determine the order of the subproblems being computed as it is based on recursion, we will not be able to further modify our algorithm. This leaves us with no option, but to cache every result computed for future reference by another subproblem.

## Runtime Overhead

When it comes to runtime overhead, tabulation does a better job than memoization, especially with complex problems. A complex problem being solved with memoization will have several recursion cycles. And it is important that the entire recursion tree is kept in memory. This leads to a possible stack overflow error if the recursion is too deep.

According to Awjin, a way to speed up memoization is to parallelize the recursive calls, while maintaining a global storage for memoized answers to subproblems. Since the ordering of subproblems in memoization does not matter (unlike tabulation), this is viable.

As tabulation does not use recursion, you will not be facing any issues associated with the function call stack overflowing.

# Which one should you choose?

Like most problems, there is no one solution for dynamic programming problems. Although theoretically, it is possible to implement dynamic programming with memoization and tabulation, the optimal approach would depend on the nature of the problem.

Tabulation would be a better approach if it is known that all subproblems must be computed to find the final solution. It can also be helpful when you know the ordering of the subproblems as it will help you further enhance your solution.

Memoization would be favored if the problem is not complicated and there is no need to compute all of the subproblems to find the final answer. Furthermore, memoization is helpful if there is no visible logic in the ordering of the subproblems.