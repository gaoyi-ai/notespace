---
title: OpenGL Projection Matrix
categories:
- CG
- Perspective Projection
tags:
- Perspective Projection
date: 2021/8/9
---



> [www.songho.ca](https://www.songho.ca/opengl/gl_projectionmatrix.html)

> how to construct OpenGL projection matrix

**Related Topics:** [OpenGL Transformation](https://www.songho.ca/opengl/gl_transform.html), [OpenGL Matrix](https://www.songho.ca/opengl/gl_matrix.html)

*   [Overview](#overview)
*   [Perspective Projection](#perspective)
*   [Orthographic Projection](#ortho)

**Updates:** The MathML version is available [here.](https://www.songho.ca/opengl/gl_projectionmatrix_mathml.html)

### Overview

A computer monitor is a 2D surface. A 3D scene rendered by OpenGL must be projected onto the computer screen as a 2D image. GL_PROJECTION [matrix](https://www.songho.ca/opengl/gl_matrix.html) is used for this projection [transformation](https://www.songho.ca/opengl/gl_transform.html). First, it transforms all vertex data from the eye coordinates to the clip coordinates. Then, these clip coordinates are also transformed to the normalized device coordinates (NDC) by dividing with _w_ component of the clip coordinates.

![](https://www.songho.ca/opengl/files/gl_frustumclip.png)  
A triangle clipped by frustum

Therefore, we have to keep in mind that both clipping (frustum culling) and NDC transformations are integrated into **GL_PROJECTION** [matrix](https://www.songho.ca/opengl/gl_matrix.html). The following sections describe how to build the projection matrix from 6 parameters; _left_, _right_, _bottom_, _top_, _near_ and _far_ boundary values.

Note that the frustum culling (clipping) is performed in the clip coordinates, just before dividing by wc. The clip coordinates, xc, yc and zc are tested by comparing with wc. If any clip coordinate is less than -wc, or greater than wc, then the vertex will be discarded.  
![](https://www.songho.ca/opengl/files/gl_projectionmatrix_eq27.png)

Then, OpenGL will reconstruct the edges of the polygon where clipping occurs.

### Perspective Projection

![](https://www.songho.ca/opengl/files/gl_projectionmatrix01.png)  
Perspective Frustum and Normalized Device Coordinates (NDC)

In perspective projection, a 3D point in a truncated pyramid frustum (eye coordinates) is mapped to a cube (NDC); the range of x-coordinate from [l, r] to [-1, 1], the y-coordinate from [b, t] to [-1, 1] and the z-coordinate from [-n, -f] to [-1, 1].

Note that the eye coordinates are defined in the right-handed coordinate system, but NDC uses the left-handed coordinate system. That is, the camera at the origin is looking along -Z axis in eye space, but it is looking along +Z axis in NDC. Since **glFrustum()** accepts only positive values of _near_ and _far_ distances, we need to negate them during the construction of GL_PROJECTION matrix.

In OpenGL, a 3D point in eye space is projected onto the _near_ plane (projection plane). The following diagrams show how a point (xe, ye, ze) in eye space is projected to (xp, yp, zp) on the _near_ plane.

<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix03.png" style="zoom:50%;" />  
Top View of Frustum

<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix04.png" style="zoom:50%;" />  
Side View of Frustum

From the top view of the frustum, the x-coordinate of eye space, xe is mapped to xp, which is calculated by using the ratio of similar triangles;  
<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq01.png" style="zoom:50%;" />

From the side view of the frustum, yp is also calculated in a similar way;  
<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq02.png" style="zoom:50%;" />

Note that both xp and yp depend on ze; they are inversely propotional to -ze. In other words, they are both divided by -ze. It is a very first clue to construct GL_PROJECTION matrix. After the eye coordinates are transformed by multiplying GL_PROJECTION matrix, the clip coordinates are still a [homogeneous coordinates](https://www.songho.ca/math/homogeneous/homogeneous.html). It finally becomes the normalized device coordinates (NDC) by divided by the w-component of the clip coordinates. (_See more details on [OpenGL Transformation](https://www.songho.ca/opengl/gl_transform.html)._)  
<img src="https://www.songho.ca/opengl/files/gl_transform08.png" style="zoom:50%;" /> ,    <img src="https://www.songho.ca/opengl/files/gl_transform12.png" style="zoom:50%;" />

Therefore, we can set the w-component of the clip coordinates as -ze. And, the 4th of GL_PROJECTION matrix becomes (0, 0, -1, 0).  
<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq03.png" style="zoom:50%;" />

Next, we map xp and yp to xn and yn of NDC with linear relationship; [l, r] ⇒ [-1, 1] and [b, t] ⇒ [-1, 1].

<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix05.png" style="zoom:50%;" />  
Mapping from xp to xn

<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq04.png" style="zoom:50%;" />

<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix06.png" style="zoom:50%;" />  
Mapping from yp to yn

<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq05.png" style="zoom:50%;" />

Then, we substitute xp and yp into the above equations.

<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq06.png" style="zoom:50%;" />

<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq07.png" style="zoom:50%;" />

Note that we make both terms of each equation divisible by -ze for perspective division (xc/wc, yc/wc). And we set wc to -ze earlier, and the terms inside parentheses become xc and yc of the clip coordiantes.

From these equations, we can find the 1st and 2nd rows of GL_PROJECTION matrix.  
<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq08.png" style="zoom:50%;" />

Now, we only have the 3rd row of GL_PROJECTION matrix to solve. Finding zn is a little different from others because ze in eye space is always projected to -n on the near plane. But we need unique z value for the clipping and depth test. Plus, we should be able to unproject (inverse transform) it. Since we know z does not depend on x or y value, we borrow w-component to find the relationship between zn and ze. Therefore, we can specify the 3rd row of GL_PROJECTION matrix like this.  
<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq10.png" style="zoom:50%;" />

In eye space, we equals to 1. Therefore, the equation becomes;  
<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq11.png" style="zoom:50%;" />

To find the coefficients, _A_ and _B_, we use the (ze, zn) relation; (-n, -1) and (-f, 1), and put them into the above equation.  
<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq12.png" style="zoom:50%;" />

To solve the equations for _A_ and _B_, rewrite eq.(1) for B;  
<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq13.png" style="zoom:50%;" />

Substitute eq.(1') to _B_ in eq.(2), then solve for A;  
<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq14.png" style="zoom:50%;" />

Put _A_ into eq.(1) to find _B_;  
<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq15.png" style="zoom:50%;" />

We found _A_ and _B_. Therefore, the relation between ze and zn becomes;  
<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq17.png" style="zoom:50%;" />

Finally, we found all entries of GL_PROJECTION matrix. The complete projection [matrix](https://www.songho.ca/opengl/gl_matrix.html) is;  
<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq16.png" style="zoom:50%;" />  
OpenGL Perspective Projection Matrix

This projection [matrix](https://www.songho.ca/opengl/gl_matrix.html) is for a general frustum. If the viewing volume is symmetric, which is ![](https://www.songho.ca/opengl/files/gl_projectionmatrix_eq18.png) and ![](https://www.songho.ca/opengl/files/gl_projectionmatrix_eq19.png), then it can be simplified as;  
<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq20.png" style="zoom:50%;" />

Before we move on, please take a look at the relation between ze and zn, eq.(3) once again. You notice it is a rational function and is non-linear relationship between ze and zn. It means there is very high precision at the _near_ plane, but very little precision at the _far_ plane. If the range [-n, -f] is getting larger, it causes a depth precision problem (z-fighting); a small change of ze around the _far_ plane does not affect on zn value. The distance between _n_ and _f_ should be short as possible to minimize the depth buffer precision problem.

![](https://www.songho.ca/opengl/files/gl_projectionmatrix07.png)  
Comparison of Depth Buffer Precisions

### Orthographic Projection

![](https://www.songho.ca/opengl/files/gl_projectionmatrix02.png)  
Orthographic Volume and Normalized Device Coordinates (NDC)

Constructing GL_PROJECTION [matrix](https://www.songho.ca/opengl/gl_matrix.html) for orthographic projection is much simpler than perspective mode.

All xe, ye and ze components in eye space are linearly mapped to NDC. We just need to scale a rectangular volume to a cube, then move it to the origin. Let's find out the elements of GL_PROJECTION using linear relationship.

![](https://www.songho.ca/opengl/files/gl_projectionmatrix08.png)  
Mapping from xe to xn

<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq21.png" style="zoom:50%;" />

![](https://www.songho.ca/opengl/files/gl_projectionmatrix09.png)  
Mapping from ye to yn

<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq22.png" style="zoom:50%;" />

![](https://www.songho.ca/opengl/files/gl_projectionmatrix10.png)  
Mapping from ze to zn

<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq23.png" style="zoom:50%;" />

Since w-component is not necessary for orthographic projection, the 4th row of GL_PROJECTION matrix remains as (0, 0, 0, 1). Therefore, the complete GL_PROJECTION [matrix](https://www.songho.ca/opengl/gl_matrix.html) for orthographic projection is;  
<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq24.png" style="zoom:50%;" />  
OpenGL Orthographic Projection Matrix

It can be further simplified if the viewing volume is symmetrical, ![](https://www.songho.ca/opengl/files/gl_projectionmatrix_eq18.png) and ![](https://www.songho.ca/opengl/files/gl_projectionmatrix_eq19.png).  
<img src="https://www.songho.ca/opengl/files/gl_projectionmatrix_eq25.png" style="zoom:50%;" />