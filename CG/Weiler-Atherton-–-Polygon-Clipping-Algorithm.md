---
title: Weiler Atherton – Polygon Clipping Algorithm
categories:
- CG
- Clipping
tags:
-  Weiler–Atherton
date: 2021/8/1
---



# Weiler Atherton – Polygon Clipping Algorithm

> [Weiler Atherton - Polygon Clipping Algorithm - GeeksforGeeks](https://www.geeksforgeeks.org/weiler-atherton-polygon-clipping-algorithm/)

### **Background:**

Weiler Atherton Polygon Clipping Algorithm is an algorithm made to allow clipping of even concave algorithms to be possible. Unlike Sutherland – Hodgman polygon clipping algorithm, this algorithm is able to clip concave polygons without leaving any residue behind.

### **Algorithm:**

```
 1. First make a list of all intersection points namely i1, i2, i3, ...
 2. Classify those intersection points as entering or exiting.
 3. Now, make two lists, one for the clipping polygon, and the other 
    for the clipped polygon.
 4. Fill both the lists up in such a way that the intersection points 
    lie between the correct vertices of each of the polygon. That is 
    the clipping polygon list is filled up with all the vertices of 
    the clipping polygon along with the intersecting points lying 
    between the corresponding vertices.
 5. Now, start at the 'to be clipped' polygon's list.
 6. Choose the first intersection point which has been labelled as 
    an entering point. Follow the points in the list (looping back to 
    the top of the list, in case the list ends) and keep on pushing 
    them into a vector or something similar of the sorts. Keep on following
    the list until an exiting intersection point is found.
 7. Now switch the list to the 'polygon that is clipping' list, and find
    the exiting the intersection that was previously encountered. Now keep
    on following the points in this list (similar to how we followed the
    previous list) until the entering intersection point is found (the 
    one that was found in the previous 'to be clipped' polygon's list).
 8. This vector now formed by pushing all the encountered points in the 
    two lists, is now the clipped polygon (one of the many clipped 
    polygons if any of the clipping polygons is concave).
 9. Repeat this clipping procedure (i.e. from step 5) until all the 
    entering intersection points have been visited once.
```

### **Explanation:**

**1. Finding all the intersection points and grouping them**
Here, let there be a polygon ABCD and another polygon VWXYZ. Let ABCD be the clipping polygon and let VWXYZ be the clipped polygon.
![img](https://media.geeksforgeeks.org/wp-content/uploads/20190527235144/WeilerAtherton-1.png)
So, we can find the intersection points using any method. For example, we can find the intersecting points separately and then find for each intersecting point find if it is entering or leaving, or, we can use Cyrus Beck and find all the intersecting points and also get if a point is entering or exiting. Refer [Cyrus Beck](https://www.geeksforgeeks.org/cyrus-beck/) for more information on this algorithm.
![img](https://media.geeksforgeeks.org/wp-content/uploads/20190527235719/WeilerAtherton-2.png)

**2. Making and filling of two lists**
Now, we make two lists. One for the clipping polygon and one for the clipped polygon.
Now this is how we fill it:
![img](https://media.geeksforgeeks.org/wp-content/uploads/20190528003804/WeilerAtherton-3.png)

**3. Running of the algorithm**
We start at the clipped polygon’s list, i.e. VWXYZ.
Now, we find the first intersecting point that is entering. Hence we choose i1.
From here we begin the making of the list of vertices (or vector) to make a clipped sub-polygon.
![img](https://media.geeksforgeeks.org/wp-content/uploads/20190528005047/WeilerAtherton-4.png)
According to the given example, **i1 Y i2** is a clipped sub-polygon.
Similarly, we get:
![img](https://media.geeksforgeeks.org/wp-content/uploads/20190528010134/WeilerAtherton-5.png)
**i0 V i3** as another sub-polygon also.

Hence, we were able to get two sub-polygons as a result of this polygon clipping, which involved a concave polygon, which resulted in:
![img](https://media.geeksforgeeks.org/wp-content/uploads/20190528010750/WeilerAtherton-6.png)
Similarly, this clipping works for convex polygons.





### **Limitations:**

This polygon clipping algorithm does not work for self – intersecting polygons, although some methods have been proposed to be able to solve this issue also, and have successfully worked.

### **Example:**

Let V1V2V3V4V5V6 be the clipping window and P1 P2 P3 P4 P5 P6 be the polygon.
Now, here is how the algorithm will operate.
Two lists are generated, one for the clipping window, and the other for the polygon. We will start from the polygon’s list. (Note that the polygon’s list only contains the polygon’s vertices and the intersections and similarly for the clipping window’s list.)
![img](https://media.geeksforgeeks.org/wp-content/uploads/20190616203542/WeilerExample1-1-1024x639.png)

So, according to the algorithm, the first polygon to be formed will be *i*2 *i*3 *i*8 *i*1

![img](https://media.geeksforgeeks.org/wp-content/uploads/20190616203800/WeilerExample1-2.png)

Then the next subpolygon, *i*4 *i*5 *i*6 *i*7.

The output will look like:
![img](https://media.geeksforgeeks.org/wp-content/uploads/20190616203935/WeilerExample1-3.png)

### **Citation:**

K. Weiler and P. Atherton. 1988. Hidden surface removal using polygon area sorting. In Tutorial: computer graphics; image synthesis, Kenneth I. Joy, Charles W. Grant, Nelson L. Max, and Lansing Hatfield (Eds.). Computer Science Press, Inc., New York, NY, USA 209-217.

**Source:** https://www.cs.drexel.edu/~david/Classes/CS430/HWs/p214-weiler.pdf