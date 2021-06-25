---
title: What is the difference between bottom-up and top-down?
categories:
- DSA
- Algorithm
- Dynamic Programming
tags:
- dynamic programming
- bottom-up
- top-down
date: 2021/6/25
---

# [What is the difference between bottom-up and top-down?](https://stackoverflow.com/questions/6164629/what-is-the-difference-between-bottom-up-and-top-down)

> rev4: A very eloquent comment by user Sammaron has noted that, perhaps, this answer previously confused top-down and bottom-up. While originally this answer (rev3) and other answers said that "bottom-up is memoization" ("assume the subproblems"), it may be the inverse (that is, "top-down" may be "assume the subproblems" and "bottom-up" may be "compose the subproblems"). Previously, I have read on memoization being a different kind of dynamic programming as opposed to a subtype of dynamic programming. I was quoting that viewpoint despite not subscribing to it. I have rewritten this answer to be agnostic of the terminology until proper references can be found in the literature. I have also converted this answer to a community wiki. Please prefer academic sources. List of references: {Web: [1](https://cs.stackexchange.com/questions/2644/top-down-bottom-up-dynamic-programing),[2](https://www.cs.cmu.edu/~avrim/451f09/lectures/lect1001.pdf)} {Literature: [5](https://books.google.com.pk/books?hl=en&lr=&id=aefUBQAAQBAJ&oi=fnd&pg=365)}

## Recap

Dynamic programming is all about ordering your computations in a way that avoids recalculating duplicate work. You have a main problem (the root of your tree of subproblems), and subproblems (subtrees). *The subproblems typically repeat and overlap*.

For example, consider your favorite example of Fibonnaci. This is the full tree of subproblems, if we did a naive recursive call:

```
TOP of the tree
fib(4)
 fib(3)...................... + fib(2)
  fib(2)......... + fib(1)       fib(1)........... + fib(0)
   fib(1) + fib(0)   fib(1)       fib(1)              fib(0)
    fib(1)   fib(0)
BOTTOM of the tree
```

(In some other rare problems, this tree could be infinite in some branches, representing non-termination, and thus the bottom of the tree may be infinitely large. Furthermore, in some problems you might not know what the full tree looks like ahead of time. Thus, you might need a strategy/algorithm to decide which subproblems to reveal.)

------

## Memoization, Tabulation

There are at least two main techniques of dynamic programming which are not mutually exclusive:

- Memoization - This is a laissez-faire approach: You assume that you have already computed all subproblems and that you have no idea what the optimal evaluation order is. Typically, you would perform a recursive call (or some iterative equivalent) from the root, and either hope you will get close to the optimal evaluation order, or obtain a proof that you will help you arrive at the optimal evaluation order. You would ensure that the recursive call never recomputes a subproblem because you *cache* the results, and thus duplicate sub-trees are not recomputed.
    - *example:* If you are calculating the Fibonacci sequence `fib(100)`, you would just call this, and it would call `fib(100)=fib(99)+fib(98)`, which would call `fib(99)=fib(98)+fib(97)`, ...etc..., which would call `fib(2)=fib(1)+fib(0)=1+0=1`. Then it would finally resolve `fib(3)=fib(2)+fib(1)`, but it doesn't need to recalculate `fib(2)`, because we cached it.
    - This starts at the top of the tree and evaluates the subproblems from the leaves/subtrees back up towards the root.
- Tabulation - You can also think of dynamic programming as a "table-filling" algorithm (though usually multidimensional, this 'table' may have non-Euclidean geometry in very rare cases*). This is like memoization but more active, and involves one additional step: You must pick, ahead of time, the exact order in which you will do your computations. This should not imply that the order must be static, but that you have much more flexibility than memoization.
    - *example:* If you are performing fibonacci, you might choose to calculate the numbers in this order: `fib(2)`,`fib(3)`,`fib(4)`... caching every value so you can compute the next ones more easily. You can also think of it as filling up a table (another form of caching).
    - I personally do not hear the word 'tabulation' a lot, but it's a very decent term. Some people consider this "dynamic programming".
    - Before running the algorithm, the programmer considers the whole tree, then writes an algorithm to evaluate the subproblems in a particular order towards the root, generally filling in a table.
    - *footnote: Sometimes the 'table' is not a rectangular table with grid-like connectivity, per se. Rather, it may have a more complicated structure, such as a tree, or a structure specific to the problem domain (e.g. cities within flying distance on a map), or even a trellis diagram, which, while grid-like, does not have a up-down-left-right connectivity structure, etc. For example, user3290797 linked a dynamic programming example of finding the [maximum independent set in a tree](https://people.eecs.berkeley.edu/~vazirani/s99cs170/notes/dynamic2.pdf), which corresponds to filling in the blanks in a tree.

(At it's most general, in a "dynamic programming" paradigm, I would say the programmer considers the whole tree, *then* writes an algorithm that implements a strategy for evaluating subproblems which can optimize whatever properties you want (usually a combination of time-complexity and space-complexity). Your strategy must start somewhere, with some particular subproblem, and perhaps may adapt itself based on the results of those evaluations. In the general sense of "dynamic programming", you might try to cache these subproblems, and more generally, try avoid revisiting subproblems with a subtle distinction perhaps being the case of graphs in various data structures. Very often, these data structures are at their core like arrays or tables. Solutions to subproblems can be thrown away if we don't need them anymore.)

[Previously, this answer made a statement about the top-down vs bottom-up terminology; there are clearly two main approaches called Memoization and Tabulation that may be in bijection with those terms (though not entirely). The general term most people use is still "Dynamic Programming" and some people say "Memoization" to refer to that particular subtype of "Dynamic Programming." This answer declines to say which is top-down and bottom-up until the community can find proper references in academic papers. Ultimately, it is important to understand the distinction rather than the terminology.]

------

## Pros and cons

### Ease of coding

Memoization is very easy to code (you can generally* write a "memoizer" annotation or wrapper function that automatically does it for you), and should be your first line of approach. The downside of tabulation is that you have to come up with an ordering.

*(this is actually only easy if you are writing the function yourself, and/or coding in an impure/non-functional programming language... for example if someone already wrote a precompiled `fib` function, it necessarily makes recursive calls to itself, and you can't magically memoize the function without ensuring those recursive calls call your new memoized function (and not the original unmemoized function))

### Recursiveness

Note that both top-down and bottom-up can be implemented with recursion or iterative table-filling, though it may not be natural.

### Practical concerns

With memoization, if the tree is very deep (e.g. `fib(10^6)`), you will run out of stack space, because each delayed computation must be put on the stack, and you will have 10^6 of them.

### Optimality

Either approach may not be time-optimal if the order you happen (or try to) visit subproblems is not optimal, specifically if there is more than one way to calculate a subproblem (normally caching would resolve this, but it's theoretically possible that caching might not in some exotic cases). Memoization will usually add on your time-complexity to your space-complexity (e.g. with tabulation you have more liberty to throw away calculations, like using tabulation with Fib lets you use O(1) space, but memoization with Fib uses O(N) stack space).

### Advanced optimizations

If you are also doing a extremely complicated problems, you might have no choice but to do tabulation (or at least take a more active role in steering the memoization where you want it to go). Also if you are in a situation where optimization is absolutely critical and you must optimize, tabulation will allow you to do optimizations which memoization would not otherwise let you do in a sane way. In my humble opinion, in normal software engineering, neither of these two cases ever come up, so I would just use memoization ("a function which caches its answers") unless something (such as stack space) makes tabulation necessary... though technically to avoid a stack blowout you can 1) increase the stack size limit in languages which allow it, or 2) eat a constant factor of extra work to virtualize your stack (ick), or 3) program in continuation-passing style, which in effect also virtualizes your stack (not sure the complexity of this, but basically you will effectively take the deferred call chain from the stack of size N and de-facto stick it in N successively nested thunk functions... though in some languages without tail-call optimization you may have to trampoline things to avoid a stack blowout).

------

## More complicated examples

Here we list examples of particular interest, that are not just general DP problems, but interestingly distinguish memoization and tabulation. For example, one formulation might be much easier than the other, or there may be an optimization which basically requires tabulation:

- the algorithm to calculate edit-distance[[4](https://en.wikipedia.org/wiki/Wagner–Fischer_algorithm)], interesting as a non-trivial example of a two-dimensional table-filling algorithm

---

- for python examples, you could google search for `python memoization decorator`; some languages will let you write a macro or code which encapsulates the memoization pattern. The memoization pattern is nothing more than "rather than calling the function, look up the value from a cache (if the value is not there, compute it and add it to the cache first)". 
- I don't see anybody mentioning this but I think another advantage of Top down is that you will only build the look-up table/cache sparsely. (ie you fill in the values where you actually need them). So this might be the pros in addition to easy coding. In other words, top down might save you actual running time since you don't compute everything (you might have tremendously better running time but same asymptotic running time though). Yet it requires additional memory to keep the additional stack frames (again, memory consumption 'may' (only may) double but asymptotically it is the same.