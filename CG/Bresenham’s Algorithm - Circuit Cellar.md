---
title: Delaunay Triangulation
categories:
- CG
- Triangulation
tags:
- Delaunay Triangulation
date: 2021/7/20
---



> [circuitcellar.com](https://circuitcellar.com/resources/bresenhams-algorithm/)

> Jack Bresenham is a computer scientist who invented one of the most useful algorithms in computer gra......

Jack Bresenham is a computer scientist who invented one of the most useful algorithms in computer graphics way back in 1962. The Bresenham Line Drawing Algorithm provides a very efficient way to plot a straight line between two points on a bitmap image (such as an LCD screen). The crux of the problem is illustrated in **Figure 1**, where we have to determine which pixels to turn on between the starting pixel (x0, y0) the finishing pixel (x1, y1).

Ideally you want to be able to do this between any arbitrary pixels, but to get a feel for this clever algorithm, let’s start with a limited example where the slope of the line is between zero (horizontal) and one (a 45 degree upward-sloping angle) and starting from the bottom left as in **Figure 1**. In this case it’s easy to plot the first pixel at (x0, y0), but when it comes to the second pixel, we have a decision to make.

![](https://circuitcellar.com/wp-content/uploads/2021/01/0019-Bresenhams_Algorithm_Fig1-Copy.png)FIGURE 1. After setting the first pixel at (x0, y0) we have to decide whether the next pixel to be set should be pixel, pixel B or Pixel C. Bresenham’s algorithm decides this for us based on the cumulative error from the theoretical fractional pixel location.

We know the x![](data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7)-coordinate must be x0 + 1 but the ![](data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7)y-coordinate could be either y0 pixels y0 + 1 (pixels A or B respectively in **Figure 1**). To work out which, the algorithm calculates a running error term that is the difference between the integral pixel ![](data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7)y-coordinate and the ideal y![](data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7)-value given by the slope of the line. If the error is greater than 0.5, the y![](data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7)-value is incremented, otherwise it is not. If the ![](data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7)y-value is incremented, then the error is adjusted to reflect the new baseline. It is easiest to see this in an example as shown in **Code Fragment 1**.

```
CODE FRAGMENT 1

/* Code Fragment 1 - Single octant Bresenham using floating point */
void line(int x0, int y0, int x1, int y1)
{
    int x;
    int y = y0;
    float err = 0.0;
    float derr = ((float)y1 - y0) / ((float)x1 - x0);

    for(x = x0; x <= x1; x++) {
        plot(x, y);
        err = err + derr;
        if(err > 0.5) {
            y = y + 1;
            err = err - 1;
        }
    }
}

```

This is cool but uses floating point arithmetic so it’s not very fast. Bresenham’s genius was to scale everything by 2 × dx to remove the fractional terms. This gives us the integer-only version in **Code Fragment 2**. This will execute much faster.

```
CODE FRAGMENT 2

/* Code Fragment 2 - Single octant Bresenham using only integer operations */
void line(int x0, int y0, int x1, int y1)
{
    int x;
    int y = y0;
    int dx = x1 - x0;
    int dy = y1 - y0;
    int derr = 2 * dy - dx;

    for(x = x0; x <= x1; x++) {
        plot(x, y);
        if(derr > 0) {
            y = y + 1;
           derr = derr - 2 * dx;
        }
        derr = derr + 2 * dy;
    }
}

```

```
CODE FRAGMENT 3

/* Code Fragment 3 - Generalised Bresenham for arbitrary start and finish pixels */
void line(int x0, int y0, int x1, int y1)
{
    int x, y;
    int dx, dy;
    int sx, sy;
    int err, e2;

    dx = x1 >= x0 ? x1 - x0 : x0 - x1;
    dy = y1 >= y0 ? y0 - y1 : y1 - y0;
    sx = x0 < x1 ? 1 : -1;
    sy = y0 < y1 ? 1 : -1;
    err = dx + dy;
    x = x0;
    y = y0;

    while(1){
        plot(x, y);
        if((x == x1) && (y == y1)) break;
        e2 = 2 * err;
        if(e2 >= dy){ // step x
            err += dy;
            x += sx;
        }
        if(e2 <= dx){ // step y
            err += dx;
            y += sy;
        }
    }
}

```

But Bresenham had a bit more to offer. **Code Fragment 3** (above) shows an extension to allow arbitrary start and finish pixels. Focus first on the code in the while loop. It extends the example above to select one of three possible next pixels (A, B and C in **Figure 1**) based on minimising a combined error term. This means it works with slopes over a whole quadrant (for example zero to 90 degrees). By setting up the initial variables based on the relative start and finish points, you effectively select any quadrant – allowing you to draw any arbitrary line. This code is neat and efficient and has been used in a graphics library I wrote many years ago and have been using ever since. 

There are a number of ways these algorithms can be extended – for example, it is not much of a stretch to create a circle-drawing equivalent as shown in **Code Fragment 4**. This works on exactly the same principles and actually draws four quarter circles at the same time. You can’t help admiring Jack Bresenham coming up with such deceptively simple (and fast) code to draw lines circles.

```
CODE FRAGMENT 4

/* Code Fragment 4 - Circle drawing based on Bresenham */
void circle (int x0, int y0, int r)
{
    int x, y;
	int err, temp;

    x = -r;
    y = 0;
    err = 2 - (2 * r);

    do {
        plot(x0 - x, y0 + y);
        plot(x0 - y, y0 - x);
        plot(x0 + x, y0 - y);
        plot(x0 + y, y0 + x);
        temp = err;
        if(temp > x) err += ++x * 2 + 1;
        if(temp <= y) err += ++y * 2 + 1;
    } while (x < 0);


```

**References:**

“Jack Elton Bresenham.” In Wikipedia, September 22, 2019. [https://en.wikipedia.org/w/index.php?title=Jack_Elton_Bresenham&oldid=917239806](https://en.wikipedia.org/w/index.php?title=Jack_Elton_Bresenham&oldid=917239806)

Zingl, Alois. “Rasterizing Curves,” n.d., 98. Accessed December 22, 2020 [http://members.chello.at/~easyfilter/Bresenham.pdf](http://members.chello.at/~easyfilter/Bresenham.pdf)
