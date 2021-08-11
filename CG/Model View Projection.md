---
title: Model View Projection - World View Projection
categories:
- CG
- Perspective Projection
tags:
- Perspective Projection
date: 2021/8/9
---



> [jsantell.com](https://jsantell.com/model-view-projection/#homogeneous-coordinates)

> Jordan Santell, focusing on open web engineering, immersive web, WebXR, WebGL, JavaScript, open sourc......April 14, 2019

In 3D engines, scenes are typically described as objects in three-dimensional space, with each object comprised of many three-dimensional vertices. Ultimately, these objects are rendered and displayed on a flat screen. Rendering a scene is always relative to the camera, and as such, the scene's vertices must also be defined relative to the camera's view.

<video src="https://jsantell.com/model-view-projection/mvp.webm" control></video>

A scene being visualized in world space, camera space, and then normalized device coordinates, representing the stages of transformation in the _Model View Projection_ pipeline.

When drawing a mesh in an OpenGL pipeline, a vertex shader will process every vertex, expecting the vertex's position to be defined in _clip space_. _Model View Projection_ is a common series of matrix transformations that can be applied to a vertex defined in _model space_, transforming it into _clip space_, which can then be rasterized.

v′=P⋅V⋅M⋅v

A vertex position is transformed by a _model_ matrix, then a _view_ matrix, followed by a _projection_ matrix, hence the name _Model View Projection_, or _MVP_.

[](#model-space)Model Space
---------------------------

Models, geometry and meshes are some series of vertices defined in _model space_. For example, a cube geometry could be defined as 8 vertices: (1,1,1), (−1,−1,−1), (1,1,−1), and so on. This would result in a 2x2x2 cube, centered at (0,0,0).

![](https://jsantell.com/model-view-projection/model-space.png)A geometry's vertices defined in model space.

Often geometry is reused multiple times in the same render, at different locations or different sizes. Pushing unique vertices for each model instance is costly and unnecessary. A single set of geometry vertices can be shared across multiple instances, with each instance applying its own unique set of transformations, represented by a _model matrix_. The model matrix transforms vertices from _model space_ to _world space_. A 2x2x2 cube centered at (0,0,0) can be resized, twisted and placed anywhere when combined with a model matrix.

![](https://jsantell.com/model-view-projection/world-space.png)Many cubes in world space.

A model matrix M is composed from an object's _translation_ transform T, _rotation_ transform R, and _scale_ transform S. Multiplying a vertex position v by this model matrix transforms the vector into _world space_.

$$
\begin{aligned}
M &=T \cdot R \cdot S \\
v_{\text {world }} &=M \cdot v_{\text {model }}
\end{aligned}
$$

[](#view)View
-------------

_World space_ is the shared global 3D Cartesian coordinate system. Renderable objects, lights, and cameras all exist within this space, defined by their model matrix, all relative to the same (0,0,0) point.

As all renders are from some camera's perspective, all vertices must be defined relatively to the camera.

_Camera space_ is the coordinate system defined as the camera at (0,0,0), facing down its -Z axis. The camera also has a model matrix defining its position in world space. The inverse of the camera's model matrix is the _view matrix_, and it transforms vertices from _world space_ to _camera space_, or _view space_.

![](https://jsantell.com/model-view-projection/camera-space.png)A scene in camera space, where everything is relative to the camera, the origin.

Sometimes the view matrix and model matrix are premultiplied and stored as a _model-view matrix_. While each object has its own model matrix, the view matrix is shared by all objects in the scene, as they are all rendered from the same camera. Given a camera's model matrix C, any vector v can be transformed from model space, to world space, to camera space.

$$
\begin{aligned}
V &=C^{-1} \\
v_{\text {camera }} &=V \cdot M \cdot v_{\text {model }}
\end{aligned}
$$
In an OpenGL system where the camera faces down -Z, any vertex that will be rendered must be in front of the camera, and in camera space, will have a negative Z value.

[](#projection)Projection
-------------------------

Once vertices are in _camera space_, they can finally be transformed into _clip space_ by applying a projection transformation. The projection matrix encodes how much of the scene is captured in a render by defining the extents of the camera's view. The two most common types of projection are _perspective_ and _orthographic_.

Perspective projection results in the natural effect of things appearing smaller the further away they are from the viewer. Orthographic projections do not have this feature, which can be useful for technical schematics or architectural blueprints for example. Much like how different lenses in a traditional camera can drastically change the field of view or distortion, the projection matrix transforms the scene in a similar way.

After applying a projection matrix, the scene's vertices are now in _clip space_. Note that the 3D vertices are represented by 4D vectors of [homogeneous coordinates](https://jsantell.com/matrix-transformations#homogeneous-coordinates), with w=1.

vclip​=P⋅V⋅M⋅v

In camera space, after the model-view transformations, w is still unchanged and equal to 1. However, perspective projection is a large reason the 4th coordinate is needed, and may no longer equal 1 after applying projection.

The vertex shader in OpenGL expects `vec4 gl_Position` to be set to clip space coordinates. Once the vertex shader finishes and the clip space position is known, the pipeline automatically performs [perspective division](https://www.learnopengles.com/tag/perspective-divide/), dividing the [x,y,z] components by the w value turning the 4D vector back into a 3D vector, resulting in the vertex finally being in _normalized device coordinates_.

$$
\left[\begin{array}{l}
x_{n d c} \\
y_{n d c} \\
z_{n d c}
\end{array}\right]=\left[\begin{array}{l}
x_{c l i p} / w_{c l i p} \\
y_{c l i p} / w_{c l i p} \\
z_{c l i p} / w_{c l i p}
\end{array}\right]
$$
![](https://jsantell.com/model-view-projection/ndc.png)Visualization of objects in normalized device coordinates. Note that the Z axis has flipped, where the camera is now facing down the +Z axis.

At this point, the pipeline discards any vertices outside of a 2x2x2 cube with extents at (−1,−1,−1) and (1,1,1). The entire visible scene, defined by the projection matrix, is now collapsed into a cube, with frustum extents defining how much was squashed into that cube, with the near plane mapped to z=−1 and the far plane mapped to z=1.

The model, view, and projection matrices transform vertices that start in _model space_, and then _world space_, _camera space_, and then _clip space_. The vertices are then transformed into _normalized device coordinates_ via implicit perspective division. Finally, during rasterization, a viewport transform is applied to interpolated vertex positions, resulting in a _window space_ position: an X and Y position of a texel in two dimensions, translating some point in 3D space relative to some viewer, into a specific pixel on a screen.

$$
\begin{aligned}
v_{\text {world }} &=M \cdot v_{\text {model }} \\
v_{\text {camera }} &=V \cdot M \cdot v_{\text {model }} \\
v_{\text {clip }} &=P \cdot V \cdot M \cdot v_{\text {model }}
\end{aligned}
$$

[](#resources-%26-references)Resources & References
---------------------------------------------------

*   [Learn OpenGL: Coordinate Systems](https://learnopengl.com/Getting-started/Coordinate-Systems)
*   [Song Ho Ahn: OpenGL Projection Matrix](http://www.songho.ca/opengl/gl_projectionmatrix.html)
*   [Oleksandr Kaleniuk: Programmer's Guide to Homogeneous Coordinates](https://hackernoon.com/programmers-guide-to-homogeneous-coordinates-73cbfd2bcc65)
*   [Learn OpenGL ES: perspective divide](http://www.learnopengles.com/tag/perspective-divide/)
*   [MDN: Model view projection](https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/WebGL_model_view_projection)