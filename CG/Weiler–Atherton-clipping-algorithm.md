---
title: Weiler–Atherton clipping algorithm
categories:
- CG
- Clipping
tags:
-  Weiler–Atherton
date: 2021/8/1
---



> [Weiler–Atherton clipping algorithm - Wikipedia](https://en.wikipedia.org/wiki/Weiler–Atherton_clipping_algorithm)

## Weiler–Atherton clipping algorithm

Given polygon A as the clipping region and polygon B as the subject polygon to be clipped, the algorithm consists of the following steps:

1. List the vertices of the clipping-region polygon A and those of the subject polygon B.
2. Label the listed vertices of subject polygon B as either inside or outside of clipping region A.
3. Find all the polygon intersections and insert them into both lists, linking the lists at the intersections.
4. Generate a list of "inbound" intersections – the intersections where the vector from the intersection to the subsequent vertex of subject polygon B begins inside the clipping region.
5. Follow each intersection clockwise around the linked lists until the start position is found.

If there are no intersections then one of three conditions must be true:

1. A is inside B – return A for clipping, B for merging.
2. B is inside A – return B for clipping, A for merging.
3. A and B do not overlap – return None for clipping or A & B for merging.

This algorithm is used for clipping concave polygons. Here V1, V2, V3, V4, V5 are the vertices of the polygon. C4, C2, C3, C4 are the vertices of the clip polygon and I1, I2, I3, I4 are the intersection points of polygon and clip polygon.

![enter image description here](https://i.imgur.com/siMbA1z.jpg)

In this algorithm we take a starting vertex like I1 and traverse the polygon like I1, V3, I2. At occurrence of leaving intersection the algorithm follows the clip polygon vertex list from leaving vertex in downward direction. At occurrence of entering intersection the algorithm follows subject polygon vertex list from the entering intersection vertex. This process is repeated till we get starting vertex. This process has to be repeated for all remaining entering intersections which are not included in the previous traversing of vertex list. Since I3 was not included in first traverse, hence, we start the second traversal from I3. Therefore, first traversal gives polygon as: I1, V3, I2, I1 and second traversal gives polygon as: I3, V5, I4, I3.

## Conclusion

One or more concave polygons may produce more than one intersecting polygon. Convex polygons will only have one intersecting polygon.

The same algorithm can be used for merging two polygons by starting at the outbound intersections rather than the inbound ones. However this can produce counter-clockwise holes.

Some polygon combinations may be difficult to resolve, especially when holes are allowed.

Points very close to the edge of the other polygon may be considered as both in and out until their status can be confirmed after all the intersections have been found and verified; however, this increases the complexity.

Various strategies can be used to improve the speed of this labeling, and to avoid needing to proceed further. Care will be needed where the polygons share an edge.