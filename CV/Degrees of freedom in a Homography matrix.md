---
title: Degrees of freedom in a Homography matrix
categories:
- CV
- Homography
tags:
- Homography
date: 2021/12/20
---



# [Degrees of freedom in a Homography matrix](https://math.stackexchange.com/questions/508668/degrees-of-freedom-in-a-homography-matrix)

> [projective geometry - 8 degrees of freedom in homography - Mathematics Stack Exchange](https://math.stackexchange.com/questions/3398164/8-degrees-of-freedom-in-homography)

When we use homogeneous coordinates, if a point P in a plane is represented by a vector $x = [x_1, x_2, x_3]^T$, then any non-zero scalar multiple of x also represents the same point P.

If H represents a homography, and $Hx = y$, then $(cH)x = cy$. And $cy$ represents the same point as y. This shows that $cH$ represents the same homography as H. (Assuming $c \neq 0$.)

Affine transformation is
$$
\begin{bmatrix} a_{11} & a_{12} & a_{13} \\ a_{21} & a_{22} & a_{23} \\ 0 & 0 & a_{33} \end{bmatrix}
$$
why is $\bold{\operatorname{dof}(A) 6}$ ?

One way to look at it is that, when we use homogeneous coordinates, an affine transformation is represented by a matrix of the form
$$
\left(\begin{array}{l}
x^{\prime} \\
y^{\prime} \\
1
\end{array}\right)=A x=\left(\begin{array}{ccc}
a_{1} & a_{2} & a_{3} \\
a_{4} & a_{5} & a_{6} \\
0 & 0 & 1
\end{array}\right)\left(\begin{array}{l}
x \\
y \\
1
\end{array}\right)
$$
where $a_{33} \neq 0$. This matrix has 7 non-zeros entries, but any nonzero scalar multiple of it represents the same affine transformation. So there are 6 degrees of freedom for the affine transformation.

---

For short, a point $P$ on the affine line $L$ is given by its coordinate, " $x$ ", say. But we consider also the infinite point, so we have to consider more "complicated point( representation)s". Instead of $x$ we write $[x: 1]$, in words, $x$ divided by one. And the point at infinity is $[1: 0]$. We can easily "see" these points if we also know the second dimension as follows. Instead of $[x: 1]$ we draw in the plane the point $(x, 1)$. This is all. In this plane there are also many other points, but from the point of view of the "camera placed in $(0,0)$ ", we can not distinguish two points on the same line, more exactly, on the same ray. For instance, the points $(2,1)$, and $(4,2)$, and $(6,3)$, and ... map in the projective line to the same point $[2: 1]=[4: 2]=[6: 3]=\ldots$ and we will always want this last view.

Why do we like this representation, and also the representation $[1: 0]$ for the "point at infinity"? Because we can take also an other way to view things. Imagine a radar, a sonar, a camera placed in the middle $O$ of a $2 \mathrm{D}$ beach, and the sea starts in some 20 meters, say, in front of us, in a point $A=[0: 1]=0$. The camera should not be able to recognize the depth. Then a full turn of the camera is something that can be easily imagined, and in this full turn, we cover the line of the breakers, of the last bastion of sand, from $A$ to the right. At some point, after some seconds, we pass through the point of view "opposite" to $A$, it has no coordinate $x$. And in the next ms we are coming from the left to $A$.

Now imagine there are some 7 surfers playing in the breakers. At coordinates $1,3,7,8,9,21,100$. An other camera sees the first three surfers in $-3,5,1$. Where should we place the other surfers?
This is a similar question to the one in the OP. We need a matrix transformation,
$$
\begin{aligned}
\left[\begin{array}{l}
x \\
1
\end{array}\right] & \rightarrow\left[\begin{array}{l}
x^{\prime} \\
1
\end{array}\right]:=\left[\begin{array}{ll}
a & b \\
c & d
\end{array}\right]\left[\begin{array}{l}
x \\
1
\end{array}\right], \quad \text { so } \\
x & \rightarrow x^{\prime}=\frac{a x+b}{c x+d}
\end{aligned}
$$
Notice the above simpler form for the transformation.
It is clear that the simpler form for the homographic transformation $x \rightarrow x^{\prime}=\frac{a x+b}{c x+d}$ is homogenous in $(a, b, c, d)$. Multiplying them in the same time by something $\neq 0$ would lead to a transformation, which is the same one.

Now we are searching for a specific homography. There are too many (redundant) variables in
$$
\left[\begin{array}{ll}
a & b \\
c & d
\end{array}\right]
$$
We are free to make one choice, one norming. Let us say, i would like to norm $d=1$. This means, we replace the above by
$$
\frac{1}{d}\left[\begin{array}{ll}
a & b \\
c & d
\end{array}\right]=\left[\begin{array}{cc}
a / d & b / d \\
c / d & d / d
\end{array}\right]=\left[\begin{array}{cc}
a / d & b / d \\
c / d & 1
\end{array}\right]
$$
(The one right lower corner entry is now one.)

Now we have two cameras. One point gives a boring situation, but if we take some seven points, say, and try to get the right camera transform, things are slightly more complicated. Let us take the (random) points mentioned above. We try to norm $d=1$, thus obtaining a system in the other three variables $a, b, c$. Conditions:
- $1 \rightarrow(a \cdot 1+b) /(c \cdot 1+1)=-3$,
- $3 \rightarrow(a \cdot 3+b) /(c \cdot 3+1)=5$,
- $7 \rightarrow(a \cdot 7+b) /(c \cdot 7+1)=1$.

So the solution is:
$$
T=\left[\begin{array}{ll}
a & b \\
c & 1
\end{array}\right]=\left[\begin{array}{cc}
-1 / 11 & -17 / 11 \\
-5 / 11 & 1
\end{array}\right]
$$
Humanly we would multiply with $-11$, obtaining an other matrix giving the same homographic transformation:
$$
U=\left[\begin{array}{cc}
1 & 17 \\
5 & -11
\end{array}\right]
$$
Passing from $U$ to $T$ is this step of norming. (By chance, we have now a normed entry in the $a-$ place.)
We can (if we can) also try to norm the entry denoted above all the time by $b$, the matrix $V$ obtained implements the same homographic transformation:
$$
V=\frac{1}{17} U=\left[\begin{array}{cc}
1 / 17 & 1 \\
5 / 17 & -11 / 17
\end{array}\right]
$$