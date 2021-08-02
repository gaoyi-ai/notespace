---
title: The Liang-Barsky line clipping algorithm in a nutshell!
categories:
- CG
- Clipping
tags:
- Liang-Barsky
date: 2021/8/1
---

> [Liang-Barsky line clipping](https://www.skytopia.com/project/articles/compsci/clipping.html)

# The Liang-Barsky line clipping algorithm in a nutshell!



![img](https://www.skytopia.com/project/articles/compsci/clip1.png) **Diagram 1**

## GOAL - Clip line to the inside of the lighter blue area

To begin with, we assume that we want to draw the whole line. In reality of course, this would only apply if the line was fully inside the lighter blue area. But bear with me, since the following algorithm will gradually clip it down, edge by edge.
So the beginning of the line **(P0)** will be represented by **0**, and the end of the line **(P1)** by 1. We can store these values in variables, as we will need to adjust them as the algorithm progresses. So:

```
t0 = 0
t1 = 1
```

Now the main idea (and what takes up 99% of the algorithm) is to clip these two values to something a bit more like t0=0.3 and t1=0.8 (that's my rough guess, but you can see that these will approximately represent the points shown by the green squares in the diagram).
We need to find out some values before we proceed. These two (Xdelta and Ydelta) are the most frequently used throughout the whole algorithm, so get them stuck into your brain:

```
Xdelta = P1x-P0x = 280-30 = 250   // Horizontal diff between P0 and P1. 
Ydelta = P1y-P0y = 160-20 = 140   // Vertical diff between P0 and P1.
```

Okay, now we're ready to start. What we'll do is go through each edge (border) in turn (Left, Right, Bottom, Top), and at each stage we will either tweak t0 or t1, or we may find out that the line is outside the border completely, and therefore doesn't need drawing - in which case we will cut the algorithm there and then! (NB., this won't happen with our example as shown by the diagram, since at least some of the line **IS** inside, so we can guarantee that we will complete all four stages of the Left-Right-Down-Up checks). Anyway, we start with a...

![img](https://www.skytopia.com/project/articles/compsci/clip1b.png) **Diagram 1b**

## Left edge check:

More values need calculating for our first check:

```
P = -XDelta           = -(280-30)    = -250
Q = -(leftEdge-P0x)   = -(70-30)     = -40
R = Q/P                              = 0.16
```

After calculating those values, we now see if any of the line is to the right of the LEFT EDGE, and if so then that's where to clip, and if not, then we can quit the whole algorithm, since ALL of the line would be to the left of the left edge (70). Observe:

It isn't in our case, but if P = 0 AND Q < 0, then we already quit. In this case, the line would parallel to the left edge ( P=0 : remember, P represents the Xdelta), and outside the edge (Q<0), so we don't need to worry about anything else, and no line is drawn. 'Thankfully', although Q is less than zero, P is not equal to zero, so we can continue....

If P is less than zero (which it is in our case, since -250 < 0 ), that means, the line from P0 to P1 is travelling in the opposite direction of our edge name (which in our case is 'left', so the line is going rightwards). Anyway, if this is the case (which it is for us), we do this check:



![img](https://www.skytopia.com/project/articles/compsci/clip2.png) **Diagram 2**

Now if R > t1 (is 0.16 > 1 ?), that must mean the line is too short to meet the left edge, so it must be outside. Thus we would scrap everything (don't draw the line!).  Otherwise, if R > t0 (is 0.16 > 0 ?), then set t0 to R !!! (clip the beginning of the line to something higher than zero). So for us, t0 = 0.16 ! Now go to [next edge](https://www.skytopia.com/project/articles/compsci/clipping.html#edges).  Otherwise, skip to [next edge](https://www.skytopia.com/project/articles/compsci/clipping.html#edges).  



If P is more than zero however (which it isn't in this example btw - we've already had the above executed, so the below boxout is only for [future edge checks](https://www.skytopia.com/project/articles/compsci/clipping.html#edges)!), that means the line is travelling in the SAME direction as our edge name (which in our case is 'left', so the line would be going leftwards), we would instead have done another (similar) check:



![img](https://www.skytopia.com/project/articles/compsci/clip1b.png)_**Diagram 1b**_

Just for reference, remember that:

```
P = -XDelta         = -(280-30)    = -250
Q = -(leftEdge-P0x) = -(70-30)     = -40
R = Q/P                            = 0.16
```

Now this time, if R < t0 (is 0.16 < 0 ?), that must mean the (other end of the) line is too short to meet the left edge, so it must be outside. Thus scrap *everything*.

Otherwise if R < t1 (is 0.16 < 1 ?), then set t1 = R ! Now go to [next edge](https://www.skytopia.com/project/articles/compsci/clipping.html#edges).

Otherwise, skip to [next edge](https://www.skytopia.com/project/articles/compsci/clipping.html#edges).





## Do the same for the other edges!

Now you've practically understood the whole algorithm because we can generalize what we've done for the left edge, to the other edges! There are slight differences though. We use slightly different values for P and Q (R remains the same though: Q/P). Also remember to take the new values for t0 and t1 into account (the instructions [above](https://www.skytopia.com/project/articles/compsci/clipping.html#alg) assume we're starting out, and therefore think that t0 still equals 0 and t1 still equals 1, when that'll change as we check each edge in turn).

- ```
    Start with: t0 = 0    t1 = 1
    ```

### ![img](https://www.skytopia.com/project/articles/compsci/clips1.png)Values for Left edge check: (as shown above)

- ```
    P = -XDelta           = -(280-30)    = -250
    Q = -(leftEdge-P0x)   = -(70-30)     = -40
    R = Q/P                              = 0.16
    
    // Results so far using algorithm above:
    t0 = 0.16    t1 = 1
    ```

### ![img](https://www.skytopia.com/project/articles/compsci/clips2.png)Values for Right edge check:

- ```
    P = XDelta            = (280-30)     = 250
    Q = rightEdge-P0x     = (230-30)     = 200
    R = Q/P                              = 0.8
    
    // Results so far using algorithm above:
    t0 = 0.16    t1 = 0.8
    ```

### ![img](https://www.skytopia.com/project/articles/compsci/clips3.png)Values for Bottom edge check:

- ```
    P = -YDelta           = -(160-20)    = -140
    Q = -(bottomEdge-P0y) = -(60-20)     = -40
    R = Q/P                              = 0.2857
    
    // Results so far using algorithm above:
    t0 = 0.2857    t1 = 0.8
    ```

### ![img](https://www.skytopia.com/project/articles/compsci/clips4.png)Values for Top edge check:

- ```
    P = YDelta            = 160-20       = 140
    Q = topEdge-P0y       = 150-20       = 130
    R = Q/P                              = 0.9286
    
    // Results so far using algorithm above:
    t0 = 0.2857    t1 = 0.8
    ```

```
Final values for t0 and t1 !! :
t0 = 0.2857
t1 = 0.8
```

![img](https://www.skytopia.com/project/articles/compsci/clipf.png)_**Diagram 3**_

## Finally, do the following...

```
  newP0x = P0x + t0*Xdelta   = 30 + 0.2857*250   = 101.425
  newP0y = P0y + t0*Ydelta   = 20 + 0.2857*140   = 60

  newP1x = P0x + t1*Xdelta   = 30 + 0.8*250      = 230
  newP1y = P0y + t1*Ydelta   = 20 + 0.8*140      = 132

  drawline newP0x,newP0y  to  newP1x,newP1y
```

We can 'optimize' the above five lines of code by using this instead. It basically avoids any unnecessary calculations if the line was already completely inside the edges to start with:

```
If t0=0				// Stick with original values
	newP0x = P0x;		
	newP0y = P0y;
Else				// Values need clipping
	newP0x = P0x + t0*Xdelta;
	newP0y = P0y + t0*Ydelta;
End if

If t1=1 {				// Stick with original values
	newP1x = P1x;
	newP1y = P1y;
Else					// Values need clipping
	newP1x = x0src + t1*Xdelta;
	newP1y = y0src + t1*Ydelta;
End If

drawline P0x,P0y  to  P1x,P1y
```

## The complete code (!) (in C)

For those too lazy (or too tired, or too short of time :) to understand the above, feel completely free to use the code below:

```c
// Liang-Barsky function by Daniel White @ https://www.skytopia.com/project/articles/compsci/clipping.html
// This function inputs 8 numbers, and outputs 4 new numbers (plus a boolean value to say whether the clipped line is drawn at all).
//
bool LiangBarsky (double edgeLeft, double edgeRight, double edgeBottom, double edgeTop,   // Define the x/y clipping values for the border.
                  double x0src, double y0src, double x1src, double y1src,                 // Define the start and end points of the line.
                  double &x0clip, double &y0clip, double &x1clip, double &y1clip)         // The output values, so declare these outside.
{
    
    double t0 = 0.0;    double t1 = 1.0;
    double xdelta = x1src-x0src;
    double ydelta = y1src-y0src;
    double p,q,r;

    for(int edge=0; edge<4; edge++) {   // Traverse through left, right, bottom, top edges.
        if (edge==0) {  p = -xdelta;    q = -(edgeLeft-x0src);  }
        if (edge==1) {  p = xdelta;     q =  (edgeRight-x0src); }
        if (edge==2) {  p = -ydelta;    q = -(edgeBottom-y0src);}
        if (edge==3) {  p = ydelta;     q =  (edgeTop-y0src);   }   
        r = q/p;
        if(p==0 && q<0) return false;   // Don't draw line at all. (parallel line outside)

        if(p<0) {
            if(r>t1) return false;         // Don't draw line at all.
            else if(r>t0) t0=r;            // Line is clipped!
        } else if(p>0) {
            if(r<t0) return false;      // Don't draw line at all.
            else if(r<t1) t1=r;         // Line is clipped!
        }
    }

    x0clip = x0src + t0*xdelta;
    y0clip = y0src + t0*ydelta;
    x1clip = x0src + t1*xdelta;
    y1clip = y0src + t1*ydelta;

    return true;        // (clipped) line is drawn
}
```

