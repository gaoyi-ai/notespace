---
title: Dynamic programming vs memoization vs tabulation
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



> [programming.guide](https://programming.guide/dynamic-programming-vs-memoization-vs-tabulation.html)

# Dynamic programming vs memoization vs tabulation

Dynamic programming is a technique for solving problems recursively. It can be implemented by **memoization** or **tabulation**.

Dynamic programming
-------------------

Dynamic programming, DP for short, can be used when the computations of subproblems overlap.

If you’re computing for instance `fib(3)` (the third [Fibonacci number](https://en.wikipedia.org/wiki/Fibonacci_number)), a naive implementation would compute `fib(1)` twice:

<svg style="max-width: 200px;" viewBox="40 20 200.0 250" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
        <text text-anchor="middle" x="150" y="50">
          fib(3)
        </text>
        <use href="#r-arrow-head-black" transform="translate(110 115) rotate(120.964) translate(-10 0)"></use>
        <line stroke="black" x1="143" x2="112.57247877713765" y1="60" y2="110.71253537143728"></line>
        <text text-anchor="middle" x="105" y="140">
          fib(2)
        </text>
        <use href="#r-arrow-head-black" transform="translate(190 115) rotate(59.036) translate(-10 0)"></use>
        <line stroke="black" x1="157" x2="187.42752122286237" y1="60" y2="110.71253537143727"></line>
        <text fill="red" style="font-weight: bold" text-anchor="middle" x="195" y="140">
          fib(1)
        </text>
        <use href="#r-arrow-head-black" transform="translate(70 215) rotate(112.62) translate(-10 0)"></use>
        <line stroke="black" x1="95" x2="71.92307692307693" y1="155" y2="210.3846153846154"></line>
        <text text-anchor="middle" x="70" y="235">
          fib(0)
        </text>
        <use href="#r-arrow-head-black" transform="translate(134 215) rotate(67.38) translate(-10 0)"></use>
        <line stroke="black" x1="109" x2="132.07692307692307" y1="155" y2="210.3846153846154"></line>
        <text fill="red" style="font-weight: bold" text-anchor="middle" x="140" y="235">
          fib(1)
        </text></svg>

With a more clever DP implementation, the tree could be collapsed into a graph (a DAG):

<svg style="max-width: 200px;" viewBox="40 20 200.0 250" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
        <text text-anchor="middle" x="150" y="50">
          fib(3)
        </text>
        <use href="#r-arrow-head-black" transform="translate(110 115) rotate(120.964) translate(-10 0)"></use>
        <line stroke="black" x1="143" x2="112.57247877713765" y1="60" y2="110.71253537143728"></line>
        <text text-anchor="middle" x="105" y="140">
          fib(2)
        </text>
        <use href="#r-arrow-head-black" transform="translate(180 190) rotate(79.967) translate(-10 0)"></use>
        <line stroke="black" x1="157" x2="179.12891285363054" y1="60" y2="185.07646395530307"></line>
        <text fill="red" style="font-weight: bold" text-anchor="middle" x="180" y="210">
          fib(1)
        </text>
        <use href="#r-arrow-head-black" transform="translate(70 215) rotate(112.62) translate(-10 0)"></use>
        <line stroke="black" x1="95" x2="71.92307692307693" y1="155" y2="210.3846153846154"></line>
        <text text-anchor="middle" x="70" y="235">
          fib(0)
        </text>
        <use href="#r-arrow-head-black" transform="translate(153 193) rotate(40.815) translate(-10 0)"></use>
        <line stroke="black" x1="109" x2="149.2158849611542" y1="155" y2="189.73190064826954"></line></svg>

It doesn’t look very impressive in this example, but it’s in fact enough to bring down the complexity from $O({2^n}) \ to \ O(n)$. Here’s a better illustration that compares the full call tree of `fib(7)` (left) to the corresponding collapsed DAG:

<svg style="max-width: 1520px;" viewBox="-1440 -10 1520.0 540" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
        <line stroke="black" x1="-1375.0" x2="-1400.0" y1="410" y2="490"></line>
        <line stroke="black" x1="-1375.0" x2="-1350.0" y1="410" y2="490"></line>
        <line stroke="black" x1="-1337.5" x2="-1375.0" y1="330" y2="410"></line>
        <line stroke="black" x1="-1337.5" x2="-1300.0" y1="330" y2="410"></line>
        <line stroke="black" x1="-1200.0" x2="-1225.0" y1="330" y2="410"></line>
        <line stroke="black" x1="-1200.0" x2="-1175.0" y1="330" y2="410"></line>
        <line stroke="black" x1="-1268.75" x2="-1337.5" y1="250" y2="330"></line>
        <line stroke="black" x1="-1268.75" x2="-1200.0" y1="250" y2="330"></line>
        <line stroke="black" x1="-1100.0" x2="-1125.0" y1="330" y2="410"></line>
        <line stroke="black" x1="-1100.0" x2="-1075.0" y1="330" y2="410"></line>
        <line stroke="black" x1="-1062.5" x2="-1100.0" y1="250" y2="330"></line>
        <line stroke="black" x1="-1062.5" x2="-1025.0" y1="250" y2="330"></line>
        <line stroke="black" x1="-1165.625" x2="-1268.75" y1="170" y2="250"></line>
        <line stroke="black" x1="-1165.625" x2="-1062.5" y1="170" y2="250"></line>
        <line stroke="black" x1="-925.0" x2="-950.0" y1="330" y2="410"></line>
        <line stroke="black" x1="-925.0" x2="-900.0" y1="330" y2="410"></line>
        <line stroke="black" x1="-887.5" x2="-925.0" y1="250" y2="330"></line>
        <line stroke="black" x1="-887.5" x2="-850.0" y1="250" y2="330"></line>
        <line stroke="black" x1="-750.0" x2="-775.0" y1="250" y2="330"></line>
        <line stroke="black" x1="-750.0" x2="-725.0" y1="250" y2="330"></line>
        <line stroke="black" x1="-818.75" x2="-887.5" y1="170" y2="250"></line>
        <line stroke="black" x1="-818.75" x2="-750.0" y1="170" y2="250"></line>
        <line stroke="black" x1="-992.1875" x2="-1165.625" y1="90" y2="170"></line>
        <line stroke="black" x1="-992.1875" x2="-818.75" y1="90" y2="170"></line>
        <line stroke="black" x1="-650.0" x2="-675.0" y1="330" y2="410"></line>
        <line stroke="black" x1="-650.0" x2="-625.0" y1="330" y2="410"></line>
        <line stroke="black" x1="-612.5" x2="-650.0" y1="250" y2="330"></line>
        <line stroke="black" x1="-612.5" x2="-575.0" y1="250" y2="330"></line>
        <line stroke="black" x1="-475.0" x2="-500.0" y1="250" y2="330"></line>
        <line stroke="black" x1="-475.0" x2="-450.0" y1="250" y2="330"></line>
        <line stroke="black" x1="-543.75" x2="-612.5" y1="170" y2="250"></line>
        <line stroke="black" x1="-543.75" x2="-475.0" y1="170" y2="250"></line>
        <line stroke="black" x1="-375.0" x2="-400.0" y1="250" y2="330"></line>
        <line stroke="black" x1="-375.0" x2="-350.0" y1="250" y2="330"></line>
        <line stroke="black" x1="-337.5" x2="-375.0" y1="170" y2="250"></line>
        <line stroke="black" x1="-337.5" x2="-300.0" y1="170" y2="250"></line>
        <line stroke="black" x1="-440.625" x2="-543.75" y1="90" y2="170"></line>
        <line stroke="black" x1="-440.625" x2="-337.5" y1="90" y2="170"></line>
        <line stroke="black" x1="-716.40625" x2="-992.1875" y1="10" y2="90"></line>
        <line stroke="black" x1="-716.40625" x2="-440.625" y1="10" y2="90"></line>-716.40625
        <rect fill="white" height="27" width="60" x="-1430.0" y="476"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-1400.0" y="490">
          fib(1)
        </text>
        <rect fill="white" height="27" width="60" x="-1380.0" y="476"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-1350.0" y="490">
          fib(0)
        </text>
        <rect fill="white" height="27" width="60" x="-1405.0" y="396"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-1375.0" y="410">
          fib(2)
        </text>
        <rect fill="white" height="27" width="60" x="-1330.0" y="396"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-1300.0" y="410">
          fib(1)
        </text>
        <rect fill="white" height="27" width="60" x="-1367.5" y="316"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-1337.5" y="330">
          fib(3)
        </text>
        <rect fill="white" height="27" width="60" x="-1255.0" y="396"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-1225.0" y="410">
          fib(1)
        </text>
        <rect fill="white" height="27" width="60" x="-1205.0" y="396"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-1175.0" y="410">
          fib(0)
        </text>
        <rect fill="white" height="27" width="60" x="-1230.0" y="316"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-1200.0" y="330">
          fib(2)
        </text>
        <rect fill="white" height="27" width="60" x="-1298.75" y="236"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-1268.75" y="250">
          fib(4)
        </text>
        <rect fill="white" height="27" width="60" x="-1155.0" y="396"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-1125.0" y="410">
          fib(1)
        </text>
        <rect fill="white" height="27" width="60" x="-1105.0" y="396"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-1075.0" y="410">
          fib(0)
        </text>
        <rect fill="white" height="27" width="60" x="-1130.0" y="316"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-1100.0" y="330">
          fib(2)
        </text>
        <rect fill="white" height="27" width="60" x="-1055.0" y="316"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-1025.0" y="330">
          fib(1)
        </text>
        <rect fill="white" height="27" width="60" x="-1092.5" y="236"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-1062.5" y="250">
          fib(3)
        </text>
        <rect fill="white" height="27" width="60" x="-1195.625" y="156"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-1165.625" y="170">
          fib(5)
        </text>
        <rect fill="white" height="27" width="60" x="-980.0" y="396"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-950.0" y="410">
          fib(1)
        </text>
        <rect fill="white" height="27" width="60" x="-930.0" y="396"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-900.0" y="410">
          fib(0)
        </text>
        <rect fill="white" height="27" width="60" x="-955.0" y="316"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-925.0" y="330">
          fib(2)
        </text>
        <rect fill="white" height="27" width="60" x="-880.0" y="316"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-850.0" y="330">
          fib(1)
        </text>
        <rect fill="white" height="27" width="60" x="-917.5" y="236"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-887.5" y="250">
          fib(3)
        </text>
        <rect fill="white" height="27" width="60" x="-805.0" y="316"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-775.0" y="330">
          fib(1)
        </text>
        <rect fill="white" height="27" width="60" x="-755.0" y="316"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-725.0" y="330">
          fib(0)
        </text>
        <rect fill="white" height="27" width="60" x="-780.0" y="236"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-750.0" y="250">
          fib(2)
        </text>
        <rect fill="white" height="27" width="60" x="-848.75" y="156"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-818.75" y="170">
          fib(4)
        </text>
        <rect fill="white" height="27" width="60" x="-1022.1875" y="76"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-992.1875" y="90">
          fib(6)
        </text>
        <rect fill="white" height="27" width="60" x="-705.0" y="396"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-675.0" y="410">
          fib(1)
        </text>
        <rect fill="white" height="27" width="60" x="-655.0" y="396"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-625.0" y="410">
          fib(0)
        </text>
        <rect fill="white" height="27" width="60" x="-680.0" y="316"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-650.0" y="330">
          fib(2)
        </text>
        <rect fill="white" height="27" width="60" x="-605.0" y="316"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-575.0" y="330">
          fib(1)
        </text>
        <rect fill="white" height="27" width="60" x="-642.5" y="236"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-612.5" y="250">
          fib(3)
        </text>
        <rect fill="white" height="27" width="60" x="-530.0" y="316"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-500.0" y="330">
          fib(1)
        </text>
        <rect fill="white" height="27" width="60" x="-480.0" y="316"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-450.0" y="330">
          fib(0)
        </text>
        <rect fill="white" height="27" width="60" x="-505.0" y="236"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-475.0" y="250">
          fib(2)
        </text>
        <rect fill="white" height="27" width="60" x="-573.75" y="156"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-543.75" y="170">
          fib(4)
        </text>
        <rect fill="white" height="27" width="60" x="-430.0" y="316"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-400.0" y="330">
          fib(1)
        </text>
        <rect fill="white" height="27" width="60" x="-380.0" y="316"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-350.0" y="330">
          fib(0)
        </text>
        <rect fill="white" height="27" width="60" x="-405.0" y="236"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-375.0" y="250">
          fib(2)
        </text>
        <rect fill="white" height="27" width="60" x="-330.0" y="236"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-300.0" y="250">
          fib(1)
        </text>
        <rect fill="white" height="27" width="60" x="-367.5" y="156"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-337.5" y="170">
          fib(3)
        </text>
        <rect fill="white" height="27" width="60" x="-470.625" y="76"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-440.625" y="90">
          fib(5)
        </text>
        <rect fill="white" height="27" width="60" x="-746.40625" y="-4"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-716.40625" y="10">
          fib(7)
        </text>-716.40625
        <line stroke="black" x1="40" x2="-24.0" y1="0" y2="30"></line>
        <line stroke="black" x1="40" x2="40" y1="0" y2="82"></line>
        <line stroke="black" x1="-40" x2="24.0" y1="50" y2="80"></line>
        <line stroke="black" x1="-40" x2="-40" y1="50" y2="132"></line>
        <line stroke="black" x1="40" x2="-24.0" y1="100" y2="130"></line>
        <line stroke="black" x1="40" x2="40" y1="100" y2="182"></line>
        <line stroke="black" x1="-40" x2="24.0" y1="150" y2="180"></line>
        <line stroke="black" x1="-40" x2="-40" y1="150" y2="232"></line>
        <line stroke="black" x1="40" x2="-24.0" y1="200" y2="230"></line>
        <line stroke="black" x1="40" x2="40" y1="200" y2="282"></line>
        <line stroke="black" x1="-40" x2="24.0" y1="250" y2="280"></line>
        <line stroke="black" x1="-40" x2="-40" y1="250" y2="332"></line>
        <line stroke="black" x1="40" x2="-24.0" y1="300" y2="330"></line>
        <line stroke="black" x1="40" x2="40" y1="300" y2="382"></line>
        <line stroke="black" x1="-40" x2="24.0" y1="350" y2="380"></line>
        <rect fill="white" height="27" width="60" x="10" y="-14"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="40" y="0">
          fib(8)
        </text>
        <rect fill="white" height="27" width="60" x="-70" y="36"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-40" y="50">
          fib(7)
        </text>
        <rect fill="white" height="27" width="60" x="10" y="86"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="40" y="100">
          fib(6)
        </text>
        <rect fill="white" height="27" width="60" x="-70" y="136"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-40" y="150">
          fib(5)
        </text>
        <rect fill="white" height="27" width="60" x="10" y="186"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="40" y="200">
          fib(4)
        </text>
        <rect fill="white" height="27" width="60" x="-70" y="236"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-40" y="250">
          fib(3)
        </text>
        <rect fill="white" height="27" width="60" x="10" y="286"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="40" y="300">
          fib(2)
        </text>
        <rect fill="white" height="27" width="60" x="-70" y="336"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="-40" y="350">
          fib(1)
        </text>
        <rect fill="white" height="27" width="60" x="10" y="386"></rect>
        <text dominant-baseline="middle" text-anchor="middle" x="40" y="400">
          fib(0)
        </text></svg>

This improvement in complexity is achieved regardles of which DP technique (memoization or tabulation) is used.

Memoization
-----------

Memoization refers to the technique of caching and reusing previously computed results. Here’s a comparison of a `square` function and the memoized version:

```
square(x) {              square_mem(x) {
  return x * x             if (mem[x] is not set)
}                            mem[x] = x * x
                           return mem[x]
                         }
```

A memoized `fib` function would thus look like this:

```
fib_mem(n) {
    if (mem[n] is not set)
        if (n < 2) result = n
        else result = fib_mem(n-2) + fib_mem(n-1)
        mem[n] = result
    return mem[n]
}
```

As you can see `fib_mem(k)` will only be computed **at most once** for each k. (Second time around it will return the memoized value.)

This is enough to cause the tree to collapse into a graph as shown in the figures above. For `fib_mem(4)` the calls would be made in the following order:

```
fib_mem(4)
    fib_mem(3)
        fib_mem(2)
            fib_mem(1)
            fib_mem(0)
        fib_mem(1)       
    fib_mem(2)           
```

This approach is **top-down** since the original problem, `fib_mem(4)`, is at the top in the above computation.

Tabulation
----------

Tabulation is similar in the sense that it builds up a cache, but the approach is different. A tabulation algorithm focuses on filling the entries of the cache, until the target value has been reached.

While DP problems, such as the fibonacci computation, are recursive in nature, a tabulation implementation is always iterative.

The tabulation version of `fib` looks like this:

```
fib_tab(n) {
    mem[0] = 0
    mem[1] = 1
    for i = 2...n
        mem[i] = mem[i-2] + mem[i-1]
    return mem[n]
}
```

The computation of `fib_tab(4)` can be described as follows:

```
mem[0] = 0
mem[1] = 1
mem[2] = mem[0] + mem[1]
mem[3] = mem[1] + mem[2]
mem[4] = mem[2] + mem[3]
```

As opposed to the memoization technique, this computation is **bottom-up** since original problem, `fib_tab(4)`, is at the bottom of the computation.

**Complexity Bonus:** The complexity of recursive algorithms can be hard to analyze. With a tabulation based implentation however, you get the complexity analysis for free! Tabulation based solutions always boils down to filling in values in a vector (or matrix) using for loops, and each value is typically computed in constant time.

Should I use tabulation or memoization?
---------------------------------------

If the original problem requires all subproblems to be solved, tabulation usually outperformes memoization by a constant factor. This is because tabulation has no overhead for recursion and can use a preallocated array rather than, say, a hash map.

If only some of the subproblems need to be solved for the original problem to be solved, then memoization is preferrable since the subproblems are solved lazily, i.e. precisely the computations that are needed are carried out.