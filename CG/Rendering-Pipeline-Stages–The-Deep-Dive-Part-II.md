---
title: Rendering Pipeline Stages–The Deep Dive Part II
categories:
- CG
- Rendering
tags:
- Rendering
date: 2021/7/13
---

> [3D Pipeline Part II - ExtremeTech](https://www.extremetech.com/computing/49422-3d-pipeline-part-ii)

In our first installment of the 3D Pipeline, we covered the basics of real-time 3D processing and presented a high-level overview of 3D pipeline operations. We then reviewed coordinate systems and spaces, described the application pipeline stage in detail, and covered portions of the geometry stage.

In this much larger second segment, we’ll wrap up the geometry stage, including culling, lighting, clipping, and transforming to screen space. Then we’ll fully explore triangle setup, and investigate rendering functions including shading, texture-mapping/MIP-mapping, fog effects, and alpha blending. For easy reference, the sections we will cover are in bold below.

在这个更大的第二部分中，我们将结束几何阶段，包括剔除、照明、剪辑和转换到屏幕空间。 然后我们将全面探索三角形设置，并研究渲染功能，包括着色、纹理映射/MIP 映射、雾效果和 alpha 混合。 为了便于参考，我们将涵盖的部分在下面以粗体显示。

The Geometry section also includes techniques to reduce the work that must be performed. The first step in reducing the working set of triangles to be processed is to cull (“select from a group”) those that are completely outside of the view volume as we noted previously. This process is called “trivial rejection,” because relative to clipping, this is a fairly simple operation.

A test is performed to determine if the x, y, and z coordinates of a triangle’s three vertices are completely outside of the view volume (frustum). In addition, the triangle’s three vertices have to be completely outside the view volume on the same side of the view frustum, otherwise it could possible for a part of the triangle to pass through the view frustum, even though its three vertices lie completely outside the frustum.

As an example, imagine a triangle that has two vertices on the left side of the frustum near the middle region of that side, and the third vertex is on the right side of the frustum located somewhere near the middle region of that side. All vertices are outside the frustum, but the triangle edges connecting the vertices actually pass through the frustum in a cross-sectional manner. If a triangle passes this test, the triangle is discarded.

If at least one (or two) of a triangle’s vertices has all three of its coordinates (x, y, or z) inside the view volume, that triangle intersects the view volume boundaries somewhere and the portions falling outside the frustum will need to be clipped off later in the pipeline. The remaining portion of the triangle, now forming a non-triangular polygon will need to be subdivided into triangles (called retesselation) within the frustum. These resulting triangles will also need to be clip-tested.

The next operation is called back-face culling (BFC), which as the name suggests, is an operation that discards triangles that have surfaces that are facing away from the view camera. On average, half of a model’s triangles will be facing away from the view camera at any given time, and those triangles that are said to be back-facing, and can be discarded, since you won’t be able to see them. The one exception where you would want to draw back-facing triangles would be if the triangles in front of them are translucent or transparent, but that determination would have to be made by the application, since BFC operations don’t take opacity into account.

Back-face culling is one of those operations that can be done at different points in the pipeline. Some 3D texts, such as Foley-Van Dam, describe BFC being done in view space before lighting operations are done, for the obvious reason that by discarding these triangles, the hardware won’t waste time lighting back-facing triangles that the viewer can’t see. However, other texts, such as Moller-Haines’ *Real-Time Rendering* shows that BFC can be done in either view or screen space. (Moller, T., Haines, E., *Real-Time Rendering* (RTR), (A.K. Peters, Natick, MA, 1999)

Determining whether triangles are back facing depends on the space where the tests are done. In view space, the API looks at each triangle’s normal. This is a vector that is pre-defined when the model is created in a program like 3D Studio Max or Maya, and is perpendicular to the surface of the triangle.

Looking from a straight-line viewing vector projected from the camera (in view space) to the center of a triangle where the normal vector originates, a measure of the angle between the normal vector and the viewing vector can be calculated. The angle is tested to see if it is greater than 90º. If so, the triangle is facing away from the camera, and can be discarded. If it’s less than or equal to 90º, then it’s visible to the view camera and cannot be thrown out. In other words, a check is made to see if the camera is on the same side of the plane of the triangle as the normal vector.

To determine which triangles are back facing in 2D screen space, a similar test can be done, wherein a face normal (a vector that is perpendicular to the surface of the triangle) is calculated for a projected triangle, and the test seeks to determine if this normal points at the camera or away from the camera.

This test is similar to the method used in 3D view space, but there are several differences. Rather than test the angle between the view vector and the triangle’s face normal, this test instead calculates a face normal using a vector operation called a *cross product*, which is a vector multiplication whose result produces a third vector that’s perpendicular to the plane of the original two, in other words, a face normal. In this case, the two vectors being fed into the cross product calculation are two sides of a triangle, and the direction the resulting normal determines whether the triangle is front- or back-facing.

Once it has been determined which way the computed normal is facing, and because the API always uses the same vertex order to calculate the cross product, you can then know whether a triangles vertices are “wound” clockwise or counterclockwise (RTR, p.192). In other words, if you were to walk from vertex 0 to vertex 1 to vertex 2, which direction would you have traveled (Clockwise or Counterclockwise)? But you can’t know which way a triangle’s vertices are wound until you calculate the triangle’s normal using the cross product operation. Take a look at this diagram to get a visual idea of what the cross product operation does.