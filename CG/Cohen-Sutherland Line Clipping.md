---
title: Cohen-Sutherland Line Clipping
categories:
- CG
- Clipping
tags:
- Cohen Sutherland
date: 2021/8/1
---



> [Cohen-Sutherland Line Clipping](https://www.cs.helsinki.fi/group/goa/viewing/leikkaus/lineClip.html)

# **Cohen-Sutherland Line Clipping**

The Cohen-Sutherland line clipping algorithm quickly detects and dispenses with two common and trivial cases. To clip a line, we need to consider only its endpoints. If both endpoints of a line lie inside the window, the entire line lies inside the window. It is [**trivially accepted**](https://www.cs.helsinki.fi/group/goa/viewing/leikkaus/trivial.html) and needs no clipping. On the other hand, if both endpoints of a line lie entirely to one side of the window, the line must lie entirely outside of the window. It is [**trivially rejected**](https://www.cs.helsinki.fi/group/goa/viewing/leikkaus/trivial.html) and needs to be neither clipped nor displayed.

------

## **Inside-Outside Window Codes**

To determine whether endpoints are inside or outside a window, the algorithm sets up a **half-space code** for each endpoint. Each edge of the window defines an infinite line that divides the whole space into two half-spaces, the **inside half-space** and the **outside half-space**, as shown below.

![img](https://www.cs.helsinki.fi/group/goa/viewing/leikkaus/inside1.gif)

As you proceed around the window, extending each edge and defining an inside half-space and an outside half-space, nine regions are created - the eight "outside" regions and the one "inside"region. Each of the nine regions associated with the window is assigned a 4-bit code to identify the region. Each bit in the code is set to either a **1**(true) or a **0**(false). If the region is to the **left** of the window, the **first** bit of the code is set to 1. If the region is to the **top** of the window, the **second** bit of the code is set to 1. If to the **right**, the **third** bit is set, and if to the **bottom**, the **fourth** bit is set. The 4 bits in the code then identify each of the nine regions as shown below.

![img](https://www.cs.helsinki.fi/group/goa/viewing/leikkaus/region1.gif)

For any endpoint **( x , y )** of a line, the code can be determined that identifies which region the endpoint lies. The code's bits are set according to the following conditions:

![img](https://www.cs.helsinki.fi/group/goa/viewing/leikkaus/bit.gif)

The sequence for reading the codes' bits is **LRBT** (Left, Right, Bottom, Top).

Once the codes for each endpoint of a line are determined, the logical **AND** operation of the codes determines if the line is completely outside of the window. If the logical AND of the endpoint codes is **not zero**, the line can be trivally rejected. For example, if an endpoint had a code of 1001 while the other endpoint had a code of 1010, the logical AND would be 1000 which indicates the line segment lies outside of the window. On the other hand, if the endpoints had codes of 1001 and 0110, the logical AND would be 0000, and the line could not be trivally rejected.

The logical **OR** of the endpoint codes determines if the line is completely inside the window. If the logical OR is **zero**, the line can be trivally accepted. For example, if the endpoint codes are 0000 and 0000, the logical OR is 0000 - the line can be trivally accepted. If the endpoint codes are 0000 and 0110, the logical OR is 0110 and the line can not be trivally accepted.

------

# **Algorithm**

The Cohen-Sutherland algorithm uses a divide-and-conquer strategy. The line segment's endpoints are tested to see if the line can be trivally accepted or rejected. If the line cannot be trivally accepted or rejected, an intersection of the line with a window edge is determined and the trivial reject/accept test is repeated. This process is continued until the line is accepted.

To perform the trivial acceptance and rejection tests, we extend the edges of the window to divide the plane of the window into the nine regions. Each end point of the line segment is then assigned the code of the region in which it lies.

1. Given a line segment with endpoint $\mathrm{P}_{1}=\left(\mathrm{x}_{1}, \mathrm{y}\right)$ and $\mathrm{P}_{2}=\left(\mathrm{x}_{2}, \mathrm{y}_{2}\right)$
2. Compute the 4 -bit codes for each endpoint.
If both codes are 0000,(bitwise OR of the codes yields 0000 ) line lies completely inside the window: pass the endpoints to the draw routine.
If both codes have a 1 in the same bit position (bitwise AND of the codes is not 0000), the line lies outside the window. It can be trivially rejected.
3. If a line cannot be trivially accepted or rejected, at least one of the two endpoints must lie outside the window and the line segment crosses a window edge. This line must be clipped at the window edge before being passed to the drawing routine.
4. Examine one of the endpoints, say $\mathrm{P}_{1}=\left(\mathrm{x}_{1}, \mathrm{y}\right)$. Read $\mathrm{P}_{1}$ 's 4 -bit code in order: Left-to-Right, Bottom-to-Top.
5. When a set bit $(1)$ is found, compute the intersection I of the corresponding window edge with the line from $P_{1}$ to $P_{2}$. Replace $P_{\mathbf{l}}$ with I and repeat the algorithm.

### Illustration of Line Clipping

**Before Clipping**

![img](https://www.cs.helsinki.fi/group/goa/viewing/leikkaus/exam1.gif)

1. Consider the line segment AD

    Point **A** has an outcode of **0000** and point **D** has an outcode of **1001**. The logical AND of these outcodes is zero; therefore, the line cannot be trivally rejected. Also, the logical OR of the outcodes is not zero; therefore, the line cannot be trivally accepted. The algorithm then chooses **D** as the outside point (its outcode contains 1's). By our testing order, we first use the top edge to clip **AD** at **B**. The algorithm then recomputes **B**'s outcode as **0000**. With the next iteration of the algorithm, **AB** is tested and is trivially accepted and displayed.

2. Consider the line segment EI

    Point **E** has an outcode of **0100**, while point **I**'s outcode is **1010**. The results of the trivial tests show that the line can neither be trivally rejected or accepted. Point **E** is determined to be an outside point, so the algorithm clips the line against the bottom edge of the window. Now line **EI** has been clipped to be line **FI**. Line **FI** is tested and cannot be trivially accepted or rejected. Point **F** has an outcode of **0000**, so the algorithm chooses point **I** as an outside point since its outcode is**1010**. The line **FI** is clipped against the window's top edge, yielding a new line **FH**. Line **FH** cannot be trivally accepted or rejected. Since **H**'s outcode is **0010**, the next iteration of the algorthm clips against the window's right edge, yielding line **FG**. The next iteration of the algorithm tests **FG**, and it is trivially accepted and display.

**After Clipping**

![img](https://www.cs.helsinki.fi/group/goa/viewing/leikkaus/exam2.gif)

