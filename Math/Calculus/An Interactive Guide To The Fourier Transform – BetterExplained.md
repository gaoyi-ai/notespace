---
title: Fourier Transform
categories:
- Math
- Calculus
tags:
- Fourier Transform
date: 2021/3/30 10:00:00
updated: 2021/3/30 16:00:00
---



> [betterexplained.com](https://betterexplained.com/articles/an-interactive-guide-to-the-fourier-transform/)

The Fourier Transform is one of deepest insights ever made. Unfortunately, the meaning is buried within dense equations:
$$
\begin{array}{l}
X_{k}=\sum_{n=0}^{N-1} x_{n} \cdot e^{-i 2 \pi k n / N} \\
x_{n}=\frac{1}{N} \sum_{k=0}^{N-1} X_{k} \cdot e^{i 2 \pi k n / N}
\end{array}
$$
Yikes. Rather than jumping into the symbols, let's experience the key idea firsthand. Here's a plain-English metaphor:

*   **What does the Fourier Transform do?** Given a smoothie, it finds the recipe.
*   **How?** Run the smoothie through filters to extract each ingredient.
*   **Why?** Recipes are easier to analyze, compare, and modify than the smoothie itself.
*   **How do we get the smoothie back?** Blend the ingredients.

Here's the"math English" version of the above:

* The Fourier Transform takes a time-based pattern, measures every possible cycle, and returns the overall "cycle recipe" (the amplitude, offset, & rotation speed for every cycle that was found).

    傅立叶变换采用基于时间的模式，测量每个可能的周期，并返回总体“周期配方”（找到的每个周期的幅度，偏移和转速）。

Time for the equations? No! Let's get our hands dirty and _experience_ how any pattern can be built with cycles, with live simulations.

If all goes well, we'll have an aha! moment and intuitively realize why the Fourier Transform is possible. We'll save the detailed math analysis for the follow-up.

This isn't a force-march through the equations, it's the casual stroll I wish I had. Onward!

From Smoothie to Recipe
-----------------------

**A math transformation is a change of perspective.** We change our notion of quantity from "single items" (lines in the sand, tally system) to "groups of 10" (decimal) depending on what we're counting. Scoring a game? Tally it up. Multiplying? Decimals, please.

The Fourier Transform changes our perspective from consumer to producer, turning _What do I have?_ into _How was it made?_

In other words: given a smoothie, let's find the recipe.

Why? Well, recipes are great descriptions of drinks. You wouldn't share a drop-by-drop analysis, you'd say "I had an orange/banana smoothie". A recipe is more easily categorized, compared, and modified than the object itself.

So... given a smoothie, how do we find the recipe?

![fourier transform analogy smoothie to recipe](https://betterexplained.com/wp-content/uploads/images/smoothie-to-recipe-20121030-223058.png)

Well, imagine you had a few filters lying around:

*   Pour through the "banana" filter. 1 oz of bananas are extracted.
*   Pour through the "orange" filter. 2 oz of oranges.
*   Pour through the "milk" filter. 3 oz of milk.
*   Pour through the "water" filter. 3 oz of water.

We can reverse-engineer the recipe by filtering each ingredient. The catch?

*   **Filters must be independent**. The banana filter needs to capture bananas, and nothing else. Adding more oranges should never affect the banana reading.
    
*   **Filters must be complete**. We won't get the real recipe if we leave out a filter ("There were mangoes too!"). Our collection of filters must catch every possible ingredient.
    
*   **Ingredients must be combine-able**. Smoothies can be separated and re-combined without issue (A cookie? Not so much. Who wants crumbs?). The ingredients, when separated and combined in any order, must make the same result.
    

See The World As Cycles
-----------------------

The Fourier Transform takes a specific viewpoint: **What if any signal could be filtered into a bunch of circular paths?**

Whoa. This concept is mind-blowing, and poor Joseph Fourier had his idea rejected at first. (_Really Joe, even a staircase pattern can be made from circles?_)

And despite [decades of debate](https://wiki.answers.com/Q/What_is_the_history_of_fourier_series) in the math community, we expect students to internalize the idea without issue. Ugh. Let's walk through the intuition.

The Fourier Transform finds the recipe for a signal, like our smoothie process:

*   Start with a time-based signal
*   Apply filters to measure each possible "circular ingredient"
*   Collect the full recipe, listing the amount of each "circular ingredient"

Stop. Here's where most tutorials excitedly throw engineering applications at your face. Don't get scared; think of the examples as "Wow, we're finally seeing the source code (DNA) behind previously confusing ideas".

*   If earthquake vibrations can be separated into "ingredients" (vibrations of different speeds & amplitudes), buildings can be designed to avoid interacting with the strongest ones.
    
*   如果声波可以分为成分（低音和高音频率），我们可以增强我们关心的部分，并隐藏我们不关心的部分。可以消除随机噪声的裂纹。 Maybe similar"sound recipes" can be compared (music recognition services compare recipes, not the raw audio clips).
    
*   如果计算机数据可以用振荡模式表示，那么最不重要的数据可能会被忽略。 这种“有损压缩”可以大大缩小文件大小（以及为什么JPEG和MP3文件比原始.bmp或.wav文件小得多）。
    
*   如果无线电波是我们的信号，我们可以使用滤波器收听特定的频道。 在冰沙世界中，想象每个人都在关注不同的成分: Adam looks for apples, Bob looks for bananas, and Charlie gets cauliflower (sorry bud).
    

The Fourier Transform is useful in engineering, sure, but it's a metaphor about finding the root causes behind an observed effect.

Think With Circles, Not Just Sinusoids
--------------------------------------

One of my giant confusions was separating the definitions of "sinusoid" and "circle".

*   A "sinusoid" is a specific back-and-forth pattern (a [sine](https://betterexplained.com/articles/intuitive-understanding-of-sine-waves/) or cosine wave), and 99% of the time, it refers to motion in one dimension.
*   A "circle" is a round, 2d pattern you probably know. If you enjoy using 10-dollar words to describe 10-cent ideas, you might call a circular path a "complex sinusoid".

将圆形路径标记为“复杂正弦波”就像将单词描述为“多个字母”一样。 You zoomed into the wrong level of detail. Words are about concepts, not the letters they can be split into!

The Fourier Transform is about circular paths (not 1-d sinusoids) and [Euler's formula](https://betterexplained.com/articles/intuitive-understanding-of-eulers-formula/) is a clever way to generate one:

![](https://betterexplained.com/wp-content/uploads/euler/equal_paths.png)

Must we use imaginary exponents to move in a circle? Nope. But it's convenient and compact. And sure, we can describe our path as coordinated motion in two dimensions (real and imaginary), but don't forget the big picture: we're just moving in a circle.

Following Circular Paths
------------------------

Let's say we're chatting on the phone and, like usual, I want us to draw the same circle simultaneously. (_You promised!_) What should I say?

*   How big is the circle? (Amplitude, i.e. size of radius)
*   How fast do we draw it? (Frequency. 1 circle/second is a frequency of 1 Hertz (Hz) or 2*pi radians/sec)
*   Where do we start? (Phase angle, where 0 degrees is the x-axis)

I could say "2-inch radius, start at 45 degrees, 1 circle per second, go!". After half a second, we should each be pointing to: starting point + amount traveled = 45 + 180 = 225 degrees (on a 2-inch circle).

![](https://betterexplained.com/wp-content/uploads/images/circular-path-parameters-20121201-203317.png)

Every circular path needs a size, speed, and starting angle (amplitude/frequency/phase). We can even combine paths: imagine tiny motorcars, driving in circles at different speeds.

**The combined position of _all the cycles_ is our signal**, just like the combined flavor of _all the ingredients_ is our smoothie.

Here's a simulation of a basic circular path:

(Based on [this animation](http://treeblurb.com/dev_math/sin_canv00.html), here's [the source code](https://gist.github.com/kazad/8bb682da198db597558c). Click the graph to pause/unpause.)

The magnitude of each cycle is listed in order, starting at 0Hz. Cycles `[0 1]` means

*   0 amplitude for the 0Hz cycle (0Hz = a constant cycle, stuck on the x-axis at zero degrees)
*   1 amplitude for the 1Hz cycle (completes 1 cycle per time interval)

Now the tricky part:

*   **The blue graph measures the _real part_ of the cycle**. Another lovely math confusion: the real axis of the circle, which is usually horizontal, has its magnitude shown on the vertical axis. You can mentally rotate the circle 90 degrees if you like.
*   时间点以最快的频率间隔开。 1Hz信号需要2个时间点来开始和停止（单个数据点没有频率）。 时间值[1 -1]显示这些等距间隔处的振幅。

With me? `[0 1]` is a pure 1Hz cycle.

Now let's add a 2Hz cycle to the mix. `[0 1 1]` means "Nothing at 0Hz, 1Hz of amplitude 1, 2Hz of amplitude 1":

Whoa. The little motorcars are getting wild: the green lines are the 1Hz and 2Hz cycles, and the blue line is the combined result. Try toggling the green checkbox to see the final result clearly. The combined "flavor" is a sway that starts at the max and dips low for the rest of the interval.

The yellow dots are when we actually measure the signal. With 3 cycles defined (0Hz, 1Hz, 2Hz), each dot is 1/3 of the way through the signal. In this case, cycles `[0 1 1]` generate the time values `[2 -1 -1]`, which starts at the max (2) and dips low (-1).

Oh! We can't forget phase, the starting angle! Use `magnitude:angle` to set the phase. So `[0 1:45]` is a 1Hz cycle that starts at 45 degrees:

This is a shifted version of `[0 1]`. On the time side we get `[.7 -.7]` instead of `[1 -1]`, because our cycle isn't exactly lined up with our measuring intervals, which are still at the halfway point (this could be desired!).

**傅立叶变换可找到一组循环速度，振幅和相位，以匹配任何时间信号。**

我们的信号成为一个抽象的概念，我们认为它是“时域中的观测”或“频域中的成分”。

Enough talk: try it out! In the simulator, type any time or cycle pattern you'd like to see. If it's time points, you'll get a collection of cycles (that combine into a"wave") that matches your desired points.

[![](https://betterexplained.com/wp-content/uploads/images/time-patterns-20121205-164541.png)](https://betterexplained.com/wp-content/uploads/images/time-patterns-20121205-164541.png)

But… doesn't the combined wave have strange values between the yellow time intervals? Sure. But who's to say whether a signal travels in straight lines, or curves, or zips into other dimensions when we aren't measuring it? It behaves exactly as we need at the equally-spaced moments we asked for.

Making A Spike In Time
----------------------

Can we make a spike in time, like `(4 0 0 0)`, using cycles? I'll use parentheses `()` for a sequence of time points, and brackets `[]` for a sequence of cycles.

Although the spike seems boring to us time-dwellers (_one data point, that's it?_), think about the complexity in the cycle world. Our cycle ingredients must start aligned (at the max value, 4) and then "explode outwards", each cycle with partners that cancel it in the future. Every remaining point is zero, which is a tricky balance with multiple cycles running around (we can't just"turn them off").

Let's walk through each time point:

*   At time 0, the first instant, every cycle ingredient is at its max. Ignoring the other time points, `(4 ? ? ?)` can be made from 4 cycles (0Hz 1Hz 2Hz 3Hz), each with a magnitude of 1 and phase of 0 (i.e., 1 + 1 + 1 + 1 = 4).
    
*   At every future point (t = 1, 2, 3), the sum of all cycles must cancel.
    

Here's the trick: when two cycles are on opposites sides of the circle (North & South, East & West, etc.) their combined position is zero (3 cycles can cancel if they're spread evenly at 0, 120, and 240 degrees).

Imagine a constellation of points moving around the circle. Here's the position of each cycle at every instant:

```
Time 0 1 2 3 
------------
0Hz: 0 0 0 0 
1Hz: 0 1 2 3
2Hz: 0 2 0 2
3Hz: 0 3 2 1
```

Notice how the the 3Hz cycle starts at 0, gets to position 3, then position "6" (with only 4 positions, 6 [modulo](https://betterexplained.com/articles/fun-with-modular-arithmetic/) 4 = 2), then position "9" (9 modulo 4 = 1).

When our cycle is 4 units long, cycle speeds a half-cycle apart (2 units) will either be lined up (difference of 0, 4, 8…) or on opposite sides (difference of 2, 6, 10…).

OK. Let's drill into each time point:

*   Time 0: All cycles at their max (total of 4)
*   Time 1: 1Hz and 3Hz cancel (positions 1 & 3 are opposites), 0Hz and 2Hz cancel as well. The net is 0.
*   Time 2: 0Hz and 2Hz line up at position 0, while 1Hz and 3Hz line up at position 2 (the opposite side). The total is still 0.
*   Time 3: 0Hz and 2Hz cancel. 1Hz and 3Hz cancel.
*   Time 4 (repeat of t=0): All cycles line up.

The trick is having individual speeds cancel (0Hz vs 2Hz, 1Hz vs 3Hz), or having the lined-up pairs cancel (0Hz + 2Hz vs 1Hz + 3Hz).

When every cycle has equal power and 0 phase, we start aligned and cancel afterwards. (I don't have a nice proof yet -- any takers? -- but you can see it yourself. Try `[1 1]`, `[1 1 1]`, `[1 1 1 1]` and notice the signals we generate: `(2 0)`, `(3 0 0)`, `(4 0 0 0)`).

In my head, I label these signals as "time spikes": they have a value for a single instant, and are zero otherwise (the fancy name is a [delta function](https://en.wikipedia.org/wiki/Dirac_delta_function).)

Here's how I visualize the initial alignment, followed by a net cancellation:

![](https://betterexplained.com/wp-content/uploads/images/fourier-interference-20121211-171719.png)

Moving The Time Spike
---------------------

Not everything happens at t=0. Can we change our spike to `(0 4 0 0)`?

It seems the cycle ingredients should be similar to `(4 0 0 0)`, but the cycles must align at t=1 (one second in the future). Here's where phase comes in.

Imagine a race with 4 runners. Normal races have everyone lined up at the starting line, the `(4 0 0 0)` time pattern. Boring.

What if we want everyone to _finish_ at the same time? Easy. Just move people forward or backwards by the appropriate distance. Maybe granny can start 2 feet in front of the finish line, Usain Bolt can start 100m back, and they can cross the tape holding hands.

Phase shifts, the starting angle, are delays in the cycle universe. Here's how we adjust the starting position to delay every cycle 1 second:

*   A 0Hz cycle doesn't move, so it's already aligned
*   A 1Hz cycle goes 1 revolution in the entire 4 seconds, so a 1-second delay is a quarter-turn. Phase shift it 90 degrees backwards (-90) and it gets to phase=0, the max value, at t=1.
*   A 2Hz cycle is twice as fast, so give it twice the angle to cover (-180 or 180 phase shift -- it's across the circle, either way).
*   A 3Hz cycle is 3x as fast, so give it 3x the distance to move (-270 or +90 phase shift)

If time points `(4 0 0 0)` are made from cycles `[1 1 1 1]`, then time points `(0 4 0 0)` are made from `[1 1:-90 1:180 1:90]`. (Note: I'm using"1Hz", but I mean"1 cycle over the entire time period").

Whoa -- we're working out the cycles in our head!

The interference visualization is similar, except the alignment is at t=1.

![](https://betterexplained.com/wp-content/uploads/images/delayed-spike-20121214-162623.png)

Test your intuition: Can you make `(0 0 4 0)`, i.e. a 2-second delay? 0Hz has no phase. 1Hz has 180 degrees, 2Hz has 360 (aka 0), and 3Hz has 540 (aka 180), so it's `[1 1:180 1 1:180]`.

Discovering The Full Transform
------------------------------

The big insight: our signal is just a bunch of time spikes! If we merge the recipes for each time spike, we should get the recipe for the full signal.

The Fourier Transform builds the recipe frequency-by-frequency:

*   Separate the full signal (a b c d) into "time spikes": (a 0 0 0) (0 b 0 0) (0 0 c 0) (0 0 0 d)
*   For any frequency (like 2Hz), the _tentative_ recipe is "a/4 + b/4 + c/4 + d/4" (the amplitude of each spike is split among all frequencies)
*   Wait! We need to offset each spike with a phase delay (the angle for a "1 second delay" depends on the frequency).
*   Actual recipe for a frequency = a/4 (no offset) + b/4 (1 second offset) + c/4 (2 second offset) + d/4 (3 second offset).

We can then loop through every frequency to get the full transform.

Here's the conversion from"math English" to full math:

![](https://betterexplained.com/wp-content/uploads/images/fourier-explained-20121219-224649.png)

A few notes:

*   N = number of time samples we have
*   n = current sample we're considering (0 .. N-1)
*   xn = value of the signal at time n
*   k = current frequency we're considering (0 Hertz up to N-1 Hertz)
*   Xk = amount of frequency k in the signal (amplitude and phase, a complex number)
*   The 1/N factor is usually moved to the _reverse transform_ (going from frequencies back to time). This [is allowed](https://math.stackexchange.com/questions/58163/dft-why-are-the-definitions-for-inverse-and-forward-commonly-switched), though I prefer 1/N in the forward transform since it gives the _actual_ sizes for the time spikes. You can get wild and even use $1/\sqrt{N}$ on both transforms (going forward and back creates the 1/N factor).
*   n/N is the percent of the time we've gone through. 2 * pi * k is our speed in radians / sec. e^-ix is our backwards-moving circular path. The combination is how far we've moved, for this speed and time.
*   The raw equations for the Fourier Transform just say "add the complex numbers". Many programming languages cannot handle complex numbers directly, so you convert everything to rectangular coordinates and add those.

Onward
------

This was my most challenging article yet. The Fourier Transform has several flavors (discrete/continuous/finite/infinite), covers deep math (Dirac delta functions), and it's easy to get lost in details. I was constantly bumping into the edge of my knowledge.

But there's always simple analogies out there -- I refuse to think otherwise. Whether it's a smoothie or Usain Bolt & Granny crossing the finish line, take a simple understanding and refine it. The analogy is flawed, and that's ok: it's a raft to use, and leave behind once we cross the river.

I realized how feeble my own understanding was when I couldn't work out the transform of `(1 0 0 0)` in my head. For me, it was like saying I knew addition but, gee whiz, I'm not sure what"1 + 1 + 1 + 1"would be. Why not? Shouldn't we have an intuition for the simplest of operations?

That discomfort led me around the web to build my intuition. In addition to the references in the article, I'd like to thank:

*   [Scott Young](http://www.scotthyoung.com/blog/), for the initial impetus for this post
*   [Shaheen Gandhi](http://shaheengandhi.com/), Roger Cheng, and [Brit Cruise](http://britcruise.com/) for kicking around ideas & refining the analogy
*   [Steve Lehar](http://cns-alumni.bu.edu/~slehar/fourier/fourier.html) for great examples of the Fourier Transform on images
*   [Charan Langton](http://www.complextoreal.com/chapters/fft1.pdf) for her detailed walkthrough
*   [Julius Smith](https://ccrma.stanford.edu/~jos/log/) for a fantastic walkthrough of the Discrete Fourier Transform (what we covered today)
*   [Bret Victor](http://worrydream.com/#!/LearnableProgramming) for his techniques on visualizing learning

Today's goal was to _experience_ the Fourier Transform. We'll save the advanced analysis for next time.

Happy math.

Appendix: Projecting Onto Cycles
--------------------------------

Stuart Riffle has a [great interpretation](https://web.archive.org/web/20120418231513/http://www.altdevblogaday.com/2011/05/17/understanding-the-fourier-transform/) of the Fourier Transform:

![](https://betterexplained.com/wp-content/uploads/images/DerivedDFT.png)

Imagine spinning your signal in a centrifuge and checking for a bias. I have a correction: we must spin _backwards_ (the exponent in the equation above should be $e^{-i 2 \pi...}$). You already know why: we need a phase _delay_ so spikes appear in the _future_.

Appendix: Another Awesome Visualization
---------------------------------------

[Lucas Vieira](http://toxicdump.org/blog/), author of excellent [Wikipedia animations](https://en.wikipedia.org/wiki/User:Kieff/Gallery), was [inspired](http://blog.matthen.com/post/42112703604/the-smooth-motion-of-rotating-circles-can-be-used) to make this interactive animation:

[Fourier Toy - Click to download, requires flash](https://web.archive.org/web/20170201145911/http://toxicdump.org/stuff/FourierToy.swf)

[![](https://betterexplained.com/wp-content/uploads/2012/12/Fourier_Toy-300x126.png)](http://toxicdump.org/stuff/FourierToy.swf)

([Detailed list](https://www.reddit.com/r/math/comments/17v8cv/visualized_fourier/c89c3pa) of control options)

The Fourier Transform is about cycles added to cycles added to cycles. Try making a "time spike" by setting a amplitude of 1 for every component (press Enter after inputting each number). Fun fact: with enough terms, you can draw any shape, even [Homer Simpson](https://www.youtube.com/watch?v=QVuU2YCwHjw&feature=youtu.be).

Check out [http://www.jezzamon.com/fourier/](http://www.jezzamon.com/fourier/) for a great tool to draw any shape using epicycles.

[![](https://betterexplained.com/wp-content/uploads/2019/01/Fourier-Transforms-2019-01-12-15-11-48.png)](http://www.jezzamon.com/fourier/)

Appendix: Article with R code samples
-------------------------------------

João Neto made a great writeup, with technical (R) code samples here:

[http://www.di.fc.ul.pt/~jpn/r/fourier/fourier.html](http://www.di.fc.ul.pt/~jpn/r/fourier/fourier.html)

Appendix: Using the code
------------------------

All the code and examples are open source (MIT licensed, do what you like).

*   [Interactive example](https://betterexplained.com/examples/fourier/) (view source)
*   [Github gist](https://gist.github.com/129d477ddb1c8025c9ac)
*   [Reddit discussion](https://www.reddit.com/r/programming/comments/156a9i/an_interactive_guide_to_the_fourier_transform/c7k9b1t?context=3) on details of the computation, I'm pb_zeppelin

Other Posts In This Series
--------------------------

1.  [A Visual, Intuitive Guide to Imaginary Numbers](https://betterexplained.com/articles/a-visual-intuitive-guide-to-imaginary-numbers/ "A Visual, Intuitive Guide to Imaginary Numbers")
2.  [Intuitive Arithmetic With Complex Numbers](https://betterexplained.com/articles/intuitive-arithmetic-with-complex-numbers/ "Intuitive Arithmetic With Complex Numbers")
3.  [Understanding Why Complex Multiplication Works](https://betterexplained.com/articles/understanding-why-complex-multiplication-works/ "Understanding Why Complex Multiplication Works")
4.  [Intuitive Guide to Angles, Degrees and Radians](https://betterexplained.com/articles/intuitive-guide-to-angles-degrees-and-radians/ "Intuitive Guide to Angles, Degrees and Radians")
5.  [Intuitive Understanding Of Euler's Formula](https://betterexplained.com/articles/intuitive-understanding-of-eulers-formula/ "Intuitive Understanding Of Euler's Formula")
6.  [An Interactive Guide To The Fourier Transform](https://betterexplained.com/articles/an-interactive-guide-to-the-fourier-transform/ "An Interactive Guide To The Fourier Transform")
7.  [Intuitive Guide to Convolution](https://betterexplained.com/articles/intuitive-convolution/ "Intuitive Guide to Convolution")
8.  [Intuitive Understanding of Sine Waves](https://betterexplained.com/articles/intuitive-understanding-of-sine-waves/ "Intuitive Understanding of Sine Waves")
9.  [An Intuitive Guide to Linear Algebra](https://betterexplained.com/articles/linear-algebra-guide/ "An Intuitive Guide to Linear Algebra")
10.  [A Programmer's Intuition for Matrix Multiplication](https://betterexplained.com/articles/matrix-multiplication/ "A Programmer's Intuition for Matrix Multiplication")
11.  [Imaginary Multiplication vs. Imaginary Exponents](https://betterexplained.com/articles/imaginary-multiplication-exponents/ "Imaginary Multiplication vs. Imaginary Exponents")