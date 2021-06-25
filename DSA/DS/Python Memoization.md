---
title: Python Memoization
categories:
- DSA
- DS
- Memoization
tags:
- memoization
date: 2021/6/25
---



# Python Memoization

## An explanation of why you should use memoization

[**Memoization**](https://www.python-course.eu/python3_memoization.php) is a way of caching the results of a function call. If a function is memoized, evaluating it is simply a matter of looking up the result you got the first time the function was called with those parameters. This is recorded in the memoization cache. If the lookup fails, that’s because the function has never been called with those parameters. Only then do you need to run the function itself?

Memoization only makes sense if the function is deterministic, or you can live with the result being out of date. But if the function is expensive, memoization can result in a massive speedup. You’re trading the computational complexity of the function for that of lookup.

So, when I talk about memoization and Python, I am talking about remembering or caching a function’s output based on its inputs. Memoization finds its root word in “memorandum”, which means “to be remembered.”

In this tutorial, you’ll see how and when to wield this simple but powerful concept with Python, so you can use it to optimize your own programs and make them run much faster in some cases.

# Why and When Should You Use Memoization in Your Python Programs?

**The answer is the expensive code:**

When I am analyzing code, I look at it in terms of how long it takes to run and how much memory it uses. If I’m looking at code that takes a long time to run or uses a lot of memory, I call the code *expensive*.

It’s an expensive code because it costs a lot of resources, space, and time, to run. When you run expensive code, it takes resources away from other programs on your machine.

If you want to speed up the parts in your Python application that are expensive, memoization can be a great technique to use. Let’s take a deeper look at memoization before we get our hands dirty and implement it ourselves!

All code examples I use in this tutorial were written in Python 3, but of course the general technique and patterns demonstrated here apply just as well to Python 2.

![img](https://miro.medium.com/max/6336/1*wvBDtWgWdf9Ym0HwTsKDZw.png)

This Fibonacci function will serve as an example of an “expensive” computation. Calculating the n-th Fibonacci number this way has *O(2^n)* time complexity it takes exponential time to complete.

This makes it quite an *expensive* function indeed.

Next, up, I’m going to do some benchmarking in order to get a feel for how computationally expensive this function is. [Python’s built-in **timeit** module ](https://docs.python.org/3/library/timeit.html)lets me measure the execution time in seconds of an arbitrary Python statement.

As you can see, on my machine, it takes about seven seconds to compute the 35th number in the Fibonacci sequence. That’s a pretty slow and expensive operation right there.

Next let’s create and test our memoization decorator out on a recursive Fibonacci sequence function. First, I’ll define a Python decorator that handles memoization to calculates the n-th [Fibonacci number](https://en.wikipedia.org/wiki/Fibonacci_number) and then test it:

![img](https://miro.medium.com/max/3770/1*m_sn00t22ECTX4qQvox-ng.png)

As you can see, the `**cache**` dictionary now also contains cached results for several other inputs to the `**memoize**` function. This allows us to retrieve these results quickly from the cache instead of slowly re-computing them from scratch.

**A quick word of warning on the naive caching implementation in our** `memoize` **decorator:** In this example the cache size is unbounded, which means the cache can grow at will. This is usually not a good idea because it can lead to memory exhaustion bugs in your programs.

With any kind of caching that you use in your programs, it makes sense to put a limit on the amount of data that is kept in the cache at the same time. This is typically achieved either by having a hard limit on the cache size or by defining an expiration policy that evicts old items from the cache at some point.

Please keep in mind that the **memoize** function we wrote earlier is a simplified implementation for demonstration purposes. In the next section in this tutorial you’ll see how to use a “production-ready” implementation of the memoization algorithm in your Python programs.

# Python Memoization with `functools.lru_cache`

Now that you’ve seen how to implement a memoization function yourself, I’ll show you you can achieve the same result using Python’s `**functools.lru_cache**` decorator for added convenience.

One of the things I love the most about Python is that the simplicity and beauty of its syntax go hand in hand with the beauty and simplicity of its philosophy. Python is “batteries included”, which means that Python is bundled with loads of commonly used libraries and modules which are only an `**import**` statement away!

I find `**functools.lru_cache**` to be a great example of this philosophy. The `lru_cache` decorator is Python’s easy to use memoization implementation from the standard library. Once you recognize when to use `**lru_cache**`, you can quickly speed up your application with just a few lines of code.

This time I’ll show you how to add memoization using the`**functools.lru_cache**` decorator:

![img](https://miro.medium.com/max/3770/1*EwEqc34u8aRsAdXQYE7xbw.png)

Note the `**maxsize**` the argument I’m passing to `**lru_cache**` to limit the number of items stored in the cache at the same time.

Once again I’m using the `**timeit**` module to run a simple benchmark so I can get a sense of the performance impact of this optimization.

You may be wondering why we’re getting the result of the first run so much faster this time around. Shouldn’t the cache be “cold” on the first run as well?

The difference is that, in this example, I applied the `**@lru_cach**e` decorator at function definition time. This means that recursive calls to `**fibonacci_lru_cache()**` are also looked up in the cache this time around.

By decorating the `**fibonacci_lru_cache()**` function with the `**@lru_cache**` decorator I basically turned it into a [dynamic programming](https://en.wikipedia.org/wiki/Dynamic_programming) solution, where each subproblem is solved just once by storing the subproblem solutions and looking them up from the cache the next time.

This is just a side-effect in this case — but I’m sure you can begin to see the beauty and the power of using a memoization decorator and how helpful a tool it can be to implement other dynamic programming algorithms as well.

# Why You Should Prefer `functools.lru_cache`

In general, Python’s memoization implementation provided by `**functools.lru_cache**` is much more comprehensive than our Adhoc memoize function, as you can see in the [CPython source code](https://github.com/python/cpython/blob/42336def77f53861284336b3335098a1b9b8cab2/Lib/functools.py#L448).

As you can see in the `**CacheInfo**` output, Python’s `**lru_cache()**` memoized the recursive calls to `**fibonacci()**`. When we look at the cache information for the memoized function, you’ll recognize why it is faster than our version on the first run the cache was hit 34 times.

If you want to learn more about the intricacies of using the `**lru_cache**` decorator I recommend that you consult the [Python standard library documentation](https://docs.python.org/3/library/functools.html#functools.lru_cache).

In summary, you should never need to roll your own memoizing function. Python’s built-in `**lru_cache()**` is readily-available, more comprehensive, and battle-tested.

# Caching Caveats — What Can Be Memoized?

Ideally, you will want to memoize functions that are deterministic.

![img](https://miro.medium.com/max/6336/1*tf1wG_dx7gfrqu_DEameqA.png)

Here `**deterministic_function()**` is a deterministic function because it will always return the same result for the same pair of parameters. For example, if you pass 2 and 3 into the function, it will always return 5.

Compare this behavior with the following *non-deterministic* function:

![img](https://miro.medium.com/max/3770/1*7P5izO2eFbXn_JWdXfU99Q.png)

This function is nondeterministic because its output for a given input will vary depending on the calculation number. If you run this function with even number result and odd number result, the cache will return *first remember condition*.

Generally I find that any function that updates a record or returns information that changes over time is a poor choice to memoize.

# References

1. [**Memoization in Python: How to Cache Function Results — dbader.org**](https://dbader.org/blog/python-memoization)
2. [**Memoization in Python**](https://mike.place/2016/memoization/)