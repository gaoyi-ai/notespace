---
title: Delaunay Triangulation
categories:
- CG
- Triangulation
tags:
- Delaunay Triangulation
date: 2021/7/20
---



> [Nabil MADALI. (2020, September 8). *Delaunay Triangulation - Towards Data Science*. Medium; Towards Data Science. ](https://towardsdatascience.com/delaunay-triangulation-228a86d1ddad)

# Delaunay Triangulation

## How to divide a set of scattered points into uneven triangular grids

When we have a series of point sets, we need to structure the point set data. An important way to structure the point set is to triangulate these points.

![img](https://miro.medium.com/max/514/1*UtTvUlXqbIbWwaSPhz4T3A.png)

A Delaunay triangulation of a random set of 24 points in a plane.

Assume that V is a finite point set on a two-dimensional real number field, edge **e** is a closed line segment composed of points in the point concentration as the end point, and **E** is a set of e. Then a triangulation T=(V,E) of the point set V is a plane graph G, which satisfies the conditions:

- Except for the endpoints, the edges in the plane graph do not contain any points in the point set.
- There are no intersecting edges.
- All faces in the plan view are triangular faces, and the collection of all triangular faces is the convex hull of the scattered point set V.

The most commonly used triangulation in practice is Delaunay triangulation, which is a special kind of triangulation that meet the following properties:

- Delaunay triangulation is unique , in Delaunay triangulation there will be no other points within the circumcircle of any triangle
- A triangle is formed by the three nearest points, and each line segment does not intersect.
- No matter where the area starts from, the final result will be consistent.
- If the diagonals of the convex quadrilateral formed by any two adjacent triangles are interchangeable, then the smallest angle among the six internal angles of the two triangles will not become larger.
- if the smallest angle of each triangle in the triangulation is arranged in ascending order, the arrangement of the Delaunay triangulation will get the largest value.
- Adding, deleting, or moving a vertex will only affect the adjacent triangle.
- The outermost boundary of the triangular mesh forms a convex polygon shell.

Many algorithms for computing Delaunay triangulations rely on fast operations for detecting when a point is within a triangle’s circumcircle and an efficient data structure for storing triangles and edges.

Among these algorithms, the point-by-point insertion algorithm is relatively simple and easy to understand. This article only discusses this algorithm, which is also the most widely used Delaunay algorithm.

The triangulation algorithm may be described in pseudo-code as follows.

```
subroutine triangulate
input : vertex list
output : triangle list
   initialize the triangle list
   determine the supertriangle
   add supertriangle vertices to the end of the vertex list
   add the supertriangle to the triangle list
   for each sample point in the vertex list
      initialize the edge buffer
      for each triangle currently in the triangle list
         calculate the triangle circumcircle center and radius
         if the point lies in the triangle circumcircle then
            add the three triangle edges to the edge buffer
            remove the triangle from the triangle list
         endif
      endfor
      delete all doubly specified edges from the edge buffer
         this leaves the edges of the enclosing polygon only
      add to the triangle list all triangles formed between the point 
         and the edges of the enclosing polygon
   endfor
   remove any triangles from the triangle list that use the supertriangle vertices
   remove the supertriangle vertices from the vertex list
end
 Suppose an edge e in E , if e meets the following conditions, it is called a Delaunay side:
```

Let’s use three points as an example:

![img](https://miro.medium.com/max/265/1*aNMNEt-U4njRmX9y6loT5A.png)

To find a random super triangle that contains all the points in the point set, find the diagonal triangle of the small rectangle. After doubled, the hypotenuse of the enlarged right triangle passes through the point (Xmax, Ymin),but in order to include all the points in the super triangle, the vertices of the triangle are expanded horizontally and high in the lower right corner, and the base of the expanded triangle must be greater than the height to achieve inclusion.

![img](https://miro.medium.com/max/60/0*Veee1RpxTLyQoFa6.png?q=20)

![img](https://miro.medium.com/max/833/0*Veee1RpxTLyQoFa6.png)

The super triangle obtained in this way will not be particularly large, making the calculation complicated, and the process is simple.

Put the super triangle into temp triangles, then traverse the triangle in temp triangle and draw a circumscribed circle.

![img](https://miro.medium.com/max/60/0*fJtynOgFeuJSLq3I.png?q=20)

![img](https://miro.medium.com/max/833/0*fJtynOgFeuJSLq3I.png)

Suppose we start the triangulation with the point on the left, which lie in the circle. So the triangle is not a Delaunay triangle, save its three sides in the edge buffer, and delete the triangle from temp triangle.

Connect this point with each edge in the edge buffer to form three triangles and add them to temp triangles.

![img](https://miro.medium.com/max/60/0*9fmDxzuBNkh2BQE7.png?q=20)

![img](https://miro.medium.com/max/833/0*9fmDxzuBNkh2BQE7.png)

Then repeat the traversal of the temp triangles and draw the circumscribed circle.

![img](https://miro.medium.com/max/60/0*3CnQXqjbrKOKwm3U.png?q=20)

![img](https://miro.medium.com/max/833/0*3CnQXqjbrKOKwm3U.png)

At this time, the second point is used:

1. The point is on the right side of the circle circumscribed by triangle 1, it means that the triangle on the left is a Delaunay triangle.
2. The point is outside the circumcircle of triangle 2, which is an uncertain triangle, so skip it, but it is not deleted in temp triangles
3. The point is inside the circumscribed circle of triangle 3, add the three edges of the triangle to the edge buffer, and combine them to form three triangles and add them to temp triangles.

![img](https://miro.medium.com/max/60/0*Bol379gcQ3N6xYdI.png?q=20)

![img](https://miro.medium.com/max/833/0*Bol379gcQ3N6xYdI.png)

Traverse temp triangles again, the array contains four triangles, one is the triangle with the first point that was skipped in the previous check and the three new triangles generated from the second point:

1. The point is on the right side of the circumscribed circle of triangle 1, the triangle is a Delaunay triangle, which is saved in triangles and deleted in temp triangles.
2. The point is outside the circumcircle of triangle 2, skip
3. The point is inside the circumcircle of triangle 3. Save the three sides in the temp buffer and delete them in temp triangles.
4. The point is inside the circumcircle of triangle 4, save the three sides in temp buffer and delete in temp triangles.

At this time, there are six edges in the temp buffer, two triangles in triangles, and 1 triangle in temp triangles.

Remove the duplication of the six edges in the temp buffer to get five edges. Combine the point and these five edges into five triangles and add them to the temp triangles.

At this time, there are 6 triangles in temp triangles.

![img](https://miro.medium.com/max/60/0*KQ1PJq36ymrVlVnu.png?q=20)

![img](https://miro.medium.com/max/833/0*KQ1PJq36ymrVlVnu.png)

Since the three points have been traversed, the triangle formed by the third point will no longer be circumscribed. At this time, triangles and temp triangles will be merged that contains the determined Delaunay triangle and the remaining triangles.

At this time, all triangles related to the three points of the super triangle in the merged array are removed, the array coordinates are limited, and the final result is obtained:

![img](https://miro.medium.com/max/60/1*0OzNVpTtQB1IWOV3-FoOCw.png?q=20)

![img](https://miro.medium.com/max/431/1*0OzNVpTtQB1IWOV3-FoOCw.png)

The following code provides a simple Delaunay triangulation in python:

```
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.tri
import matplotlib.collectionsnumSeeds = 24
radius = 100
seeds = radius * np.random.random((numSeeds, 2))
print("seeds:\n", seeds)
print("BBox Min:", np.amin(seeds, axis=0),"Bbox Max: ", np.amax(seeds, axis=0))center = np.mean(seeds, axis=0)
print("Center:", center)
center = np.asarray(center)
# Create coordinates for the corners of the framecoords = [center+radius*np.array((-1, -1)),center+radius*np.array((+1, -1)),center+radius*np.array((+1, +1)),center+radius*np.array((-1, +1))]def circumcenter( tri):"""Compute circumcenter and circumradius of a triangle in 2D.Uses an extension of the method described here:http://www.ics.uci.edu/~eppstein/junkyard/circumcenter.html"""pts = np.asarray([coords[v] for v in tri])
   pts2 = np.dot(pts, pts.T)
   A = np.bmat([[2 * pts2, [[1],[1],[1]]],[[[1, 1, 1, 0]]]])
   b = np.hstack((np.sum(pts * pts, axis=1), [1]))
   x = np.linalg.solve(A, b)
   bary_coords = x[:-1]
   center = np.dot(bary_coords, pts)
   # radius = np.linalg.norm(pts[0] - center) # euclidean distance
   radius = np.sum(np.square(pts[0] - center))  # squared distance
   return (center, radius)# Create two dicts to store triangle neighbours and circumcircles.triangles = {}
circles = {}
# Create two CCW triangles for the frameT1 = (0, 1, 3)
T2 = (2, 3, 1)triangles[T1] = [T2, None, None]
triangles[T2] = [T1, None, None]def inCircleFast( tri, p):"""Check if point p is inside of precomputed circumcircle of tri."""
  center, radius = circles[tri]
  return np.sum(np.square(center - p)) <= radius# Compute circumcenters and circumradius for each trianglefor t in triangles:
    circles[t] = circumcenter(t)
def addPoint(p):"""Add a point to the current DT, and refine it using Bowyer-Watson."""p = np.asarray(p)
    idx = len(coords)
    coords.append(p)
    # Search the triangle(s) whose circumcircle contains p
    bad_triangles = []
    for T in triangles:
         # Choose one method: inCircleRobust(T, p) or inCircleFast(T, p)if inCircleFast(T, p):   
            bad_triangles.append(T)
       # Find the CCW boundary (star shape) of the bad triangles,
       # expressed as a list of edges (point pairs) and the opposite
       # triangle to each edge.
       boundary = []
       # Choose a "random" triangle and edge
       T = bad_triangles[0]
       edge = 0
       # get the opposite triangle of this edge
       while True:
          # Check if edge of triangle T is on the boundary...
          # if opposite triangle of this edge is external to the list    
          tri_op = triangles[T][edge]
          if tri_op not in bad_triangles:
            # Insert edge and external triangle into boundary listboundary.append((T[(edge+1) % 3], T[(edge-1) % 3], tri_op)) 
             # Move to next CCW edge in this triangleedge = (edge + 1) % 3
            # Check if boundary is a closed loop
            if boundary[0][0] == boundary[-1][1]:
               break
          else:
           # Move to next CCW edge in opposite triangle
           edge = (triangles[tri_op].index(T) + 1) % 3
           T = tri_op
     # Remove triangles too near of point p of our solution
       for T in bad_triangles:
         del triangles[T]
         del circles[T]
      #Retriangle the hole left by bad_triangles
      new_triangles = []
      for (e0, e1, tri_op) in boundary:
          # Create a new triangle using point p and edge extremes
          T = (idx, e0, e1)
          # Store circumcenter and circumradius of the triangle
         circles[T] = circumcenter(T)
         # Set opposite triangle of the edge as neighbour of T
         triangles[T] = [tri_op, None, None]
         # Try to set T as neighbour of the opposite triangle
         if tri_op:
            # search the neighbour of tri_op that use edge (e1, e0)for i, neigh in enumerate(triangles[tri_op]):if neigh:
                if e1 in neigh and e0 in neigh:
                     # change link to use our new triangletriangles[tri_op][i] = T# Add triangle to a temporal listnew_triangles.append(T)
   # Link the new triangles each another
   N = len(new_triangles)
   for i, T in enumerate(new_triangles):
      triangles[T][1] = new_triangles[(i+1) % N]   # nexttriangles[T][2] = new_triangles[(i-1) % N]   # previous# Insert all seeds one by onefor s in seeds: 
   addPoint(s)# Create a plot with matplotlib.pyplotfig, ax = plt.subplots()
ax.margins(0.1)
ax.set_aspect('equal')
plt.axis([-1, radius+1, -1, radius+1])
# Plot our Delaunay triangulation (plot in blue)cx, cy = zip(*seeds)
dt_tris = [(a-4, b-4, c-4) for (a, b, c) in triangles if a > 3 and b > 3 and c > 3]
ax.triplot(matplotlib.tri.Triangulation(cx, cy, dt_tris), 'bo--')
```

This code has been written to stay simple, easy to read by beginners and with minimal dependencies instead of highly-optimized. There is a section in `addPoint()` method that performs specially bad if you have a big set of input points.

![img](https://miro.medium.com/max/762/1*WhJJ70f0se5yPqdJv2jVMA.gif)

# References

1. Varber, C. B.; Dobkin, D. P.; and Huhdanpaa, H. T. “The Quickhull Algorithm for Convex Hulls.” *ACM Trans. Mathematical Software* **22**, 469–483, 1996.
2. Hinton, P. J. “qh-math: A MathLink Interface To Qhull’s Delaunay Triangulation.”
3. Lee, D. T. and Schachter, B. J. “Two Algorithms for Constructing a Delaunay Triangulation.” *Int. J. Computer Information Sci.* **9**, 219–242, 1980.
4. Okabe, A.; Boots, B.; and Sugihara, K. [*Spatial Tessellations: Concepts and Applications of Voronoi Diagrams.*](https://www.amazon.com/exec/obidos/ASIN/0471986356/ref=nosim/ericstreasuretro) New York: Wiley, 1992.
5. Preparata, F. R. and Shamos, M. I. [*Computational Geometry: An Introduction.*](https://www.amazon.com/exec/obidos/ASIN/0387961313/ref=nosim/ericstreasuretro) New York: Springer-Verlag, 1985.