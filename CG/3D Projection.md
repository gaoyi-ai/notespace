---
title: 3D Projection
categories:
- CG
- Perspective Projection
tags:
- Perspective Projection
date: 2021/8/9
---



> [jsantell.com](https://jsantell.com/3d-projection/)

> Jordan Santell, focusing on open web engineering, immersive web, WebXR, WebGL, JavaScript, open sourc......April 29, 2019

In 3D graphics, objects are rendered from some viewer's position and displayed on a flat screen, like a phone or laptop. _Projection_ describes the transformation of a three-dimensional point into a two-dimensional point. This transformation can be represented by a _projection matrix_, which may encode both perspective, like a camera's focal length, as well as the transformation to normalized device coordinates (NDC).

Projection matrices are one of the more confusing parts of the GL pipeline, are notoriously difficult to debug, and can be parameterized in several different ways. The following fundamentals and equations attempt to clarify the process and provide reference for common projection tasks and conversions.

<figure><div class="vsc-controller" data-vscid="8d9kmw354"></div><video style="max-height:300px" muted="" autoplay="" loop="" data-vscid="8d9kmw354"><source src="https://jsantell.com/3d-projection/dollyzoom.webm" type="video/webm; codecs=vp9"><source src="https://jsantell.com/3d-projection/dollyzoom.mp4" type="video/mp4"></video><figcaption>The camera moves as the field of view changes to ensure that the subject is always consistently framed, a technique known as the <a href="https://en.wikipedia.org/wiki/Dolly_zoom">dolly zoom</a>.<br><a href="https://sketchfab.com/models/bbc1b4aa9e4c4e0ba6201e6129d05d23">Boy Finnigon </a>by <a href="https://dadracodesign.de/">Fion Noir</a> (<a href="https://creativecommons.org/licenses/by/4.0/">CC-BY-4.0</a>), <a href="https://poly.google.com/view/64iwya-59ew">Forest Block </a>by <a href="https://www.artstation.com/jarlanperez">Jarlan Perez</a> (<a href="https://creativecommons.org/licenses/by/3.0/">CC-BY-3.0</a>)</figcaption></figure>

[](#projection-transformation)Projection Transformation
-------------------------------------------------------

The two most common types of projection are [orthographic](https://en.wikipedia.org/wiki/Orthographic_projection) and [perspective](https://en.wikipedia.org/wiki/Perspective_(graphical)) projection. [Axonometric (isometric) projections](https://commons.wikimedia.org/wiki/File:Graphical_projection_comparison.png) are common in games as well.

Orthographic projections do not visualize depth, and are often used for schematics, architectural drawings, and 3D software when lining up vertices. As there is no applied perspective, lines can be absolutely measured and compared.

Perspective projection, however, accounts for depth in a way that simulates how humans perceive the world. Objects that are further away appear smaller, resulting in roughly a single [vanishing point](https://en.wikipedia.org/wiki/Vanishing_point) in the center of our vision.

![](https://jsantell.com/3d-projection/grafitti-sf.jpg)

Parallel lines of a road appear to eventually converge due to perspective.

[Haight Street, San Francisco](https://aforeignperspective.wordpress.com/2012/03/09/60/#jp-carousel-61), [Jens-Oliver Pukke](https://aforeignperspective.wordpress.com/)

Whatever type of projection is used, the end result is a [4D homogeneous coordinate](https://jsantell.com/model-view-projection#homogeneous-coordinates) in _clip space_; in the OpenGL pipeline, this value is then divided by w, becoming a 3D vector in _normalized device coordinates_, and any vertex outside of the −1 to 1 range gets clipped.

<figure><div class="vsc-controller" data-vscid="hittnvm4u"></div><video style="height:250px" muted="" autoplay="" loop="" data-vscid="hittnvm4u"><source src="https://jsantell.com/3d-projection/world-to-ndc.webm" type="video/webm; codecs=vp9"><source src="https://jsantell.com/3d-projection/world-to-ndc.mp4" type="video/mp4"></video><figcaption>The projection transformation maps the viewing volume into an NDC cube in OpenGL.</figcaption></figure>

[](#viewing-frustum)Viewing Frustum
-----------------------------------

A camera abstraction in a 3D engine has an area of space that is visible, described as a viewing volume in a cuboid shape for orthographic projections, or a [frustum](https://en.wikipedia.org/wiki/Viewing_frustum) for perspective projections. The human visual system, although a [series of lies and magic](https://twitter.com/foone/status/1014267515696922624), has a viewing volume that includes 180° horizontally and 90° vertically, and extends essentially an infinite amount. After all, we can see [V762 Cas in Cassiopeia, 16,308 light-years away](http://astrobob.areavoices.com/2010/10/02/how-to-see-the-farthest-thing-you-can-see/)! Cameras in 3D engines are much more constrained.

A camera's frustum can be thought of as 6 planes, and any objects between those planes are visible and within the camera's field of view. Frustums are generally defined in terms of the _near_ and _far_ planes' distance from the camera on the `Z` axis, and how far the frustum extends on the near plane to the left, right, top and bottom from the `Z` axis. The near plane is the 2D plane that the rendered image will be projected upon.

Frustum visualization, using extents parameterization.

[](#perspective-projection)Perspective projection
-------------------------------------------------

With the six extent values (near, far, left, right, top, bottom), a perspective projection matrix can be created:

$$
\left[\begin{array}{cccc}
\frac{2 n}{r-l} & 0 & \frac{r+l}{r-l} & 0 \\
0 & \frac{2 n}{t-b} & \frac{t+b}{t-b} & 0 \\
0 & 0 & \frac{f+n}{n-f} & \frac{2 f n}{n-f} \\
0 & 0 & -1 & 0
\end{array}\right]
$$
Most 3D engines or libraries will have a function that creates a perspective matrix from these values, like [glFrustum](https://docs.microsoft.com/en-us/windows/desktop/OpenGL/glfrustum) or [three.js](https://threejs.org/)'s [Matrix4#makePerspective](https://threejs.org/docs/index.html#api/en/math/Matrix4.makePerspective),

These values are in world units; the _near_ and _far_ values are absolute distances from the camera's forward axis, and the extents are the relative position between the camera's focal point on the camera's forward axis on the near plane, and the extent.

The following figures illustrate the context of the extent values, and how they can be used with trigonometry to measure any length or angle.

<figure><img alt="Diagram of a symmetric camera's frustum on the YZ plane in camera space" style="width:auto;max-height:300px" src="https://jsantell.com/3d-projection/frustum-yz.svg" loading="lazy"><figcaption><strong>Figure 1</strong> A view of a symmetric camera's frustum on the <span class="var">YZ</span> plane in camera space. Notice how the top (<span class="var">t</span>), bottom (<span class="var">b</span>) and near plane distance (<span class="var">n</span>) all determine the vertical field of view (<span class="var">θ</span>). The Y extents on the far plane are calculated with the ratio <span class="var">f/n</span>, as the angles are identical for both the origin/near triangle, as well as the origin/far triangle.</figcaption></figure>

![](https://jsantell.com/3d-projection/frustum-xz.svg)**Figure 2** A view of a symmetric camera's frustum on the XZ plane in camera space. Almost identical to _Figure 1_, except using the left (l) and right (r) frustum extents.

[](#projection-symmetry)Projection Symmetry
-------------------------------------------

Note that the simulation and images so far have been _symmetric_ projections. The symmetric frustums' extents are symmetrical both vertically and horizontally around the Z axis at the near plane, such that r=−l and t=−b. Symmetric projections are common in 3D renderings, although asymmetric projections can be used in [stereoscopic VR rendering](https://developer.oculus.com/documentation/unity/latest/concepts/unity-asymmetric-fov-faq/), augmented reality platforms, or immersive installations.

A simplified form of the perspective projection matrix can be used for symmetric projections, where r=−l and t=−b:

$$
\left[\begin{array}{cccc}
\frac{n}{r} & 0 & 0 & 0 \\
0 & \frac{n}{t} & 0 & 0 \\
0 & 0 & \frac{f+n}{n-f} & \frac{2 f n}{n-f} \\
0 & 0 & -1 & 0
\end{array}\right]
$$

[](#parameterization)Parameterization
-------------------------------------

Defining a perspective projection in terms of its frustum extents is just one option. Projections can be defined via aspect ratio, field of view, focal length, or other parameters, depending on background or purpose.

### [](#field-of-view)Field of view

Perhaps more commonly, perspective cameras are defined by a _vertical field of view_ and the projection screen's _aspect ratio_, as well as the _near_ and _far_ plane values. This parameterization is (subjectively) more human-understandable: aspect ratio usually must be configurable to work across different screen resolutions, and the field of view is more intuitive than frustum extents.

<figure><iframe title="Frustum visualization" src="https://jsantell.dev/frustum-viewer" height="300px" width="100%" loading="lazy"></iframe><figcaption>Frustum visualization, using aspect ratio/FOV parameterization.</figcaption></figure>



Referencing **Figure 1** above and using some trigonometry, the vertical field of view and aspect ratio can be converted to frustum extents, or used directly in the creation of the matrix. This assumes a symmetric projection.

```
  let top = near * Math.tan(fov / 2);
  let bottom = -top;
  let right = aspect * top;
  let left = -right;


```

$$
\begin{gathered}
e=\frac{1}{\tan (F O V / 2)} \\
{\left[\begin{array}{cccc}
\frac{e}{\text { aspect }} & 0 & 0 & 0 \\
0 & e & 0 & 0 \\
0 & 0 & \frac{f+n}{n-f} & \frac{2 f n}{n-f} \\
0 & 0 & -1 & 0
\end{array}\right]}
\end{gathered}
$$

e above can be thought of as the _focal length_. While rendering doesn't quite have the same idea as a focal length, [Eric Lengyel shared some matrix tricks at GDC 2007](http://www.terathon.com/gdc07_lengyel.pdf) to simulate the parameterization. [Paul Bourke's brief note,"Field of view and focal length"](http://paulbourke.net/miscellaneous/lens/) sketches out the relationship between the two as well.

### [](#camera-intrinsics)Camera intrinsics

If working with [OpenCV](https://opencv.org/) or augmented reality platforms ([ARCore](https://developers.google.com/ar/reference/java/arcore/reference/com/google/ar/core/CameraIntrinsics), [ARKit](https://developer.apple.com/documentation/arkit/arcamera/2875730-intrinsics)), controlling projections via camera intrinsics may be necessary.

Where fx​ and fy​ are the horizontal and vertical focal lengths in pixels, an often unused s for skew, and cx​ and cy​ representing the _principal point_, or the horizontal and vertical offset from the _bottom-left_ in pixels, which for symmetric projections results in cx​=width/2 and cy​=height/2.
$$
\left[\begin{array}{ccc}
f_{x} & s & c_{x} \\
0 & f_{y} & c_{y} \\
0 & 0 & 1
\end{array}\right]
$$
Koshy George shared a specialized form of representing camera intrinsics in OpenGL, for symmetric projections that have adjustable near/far planes:
$$
\left[\begin{array}{cccc}
\frac{f_{x}}{c_{x}} & 0 & 0 & 0 \\
0 & \frac{f_{y}}{c_{y}} & 0 & 0 \\
0 & 0 & \frac{f+n}{n-f} & \frac{2 f n}{n-f} \\
0 & 0 & -1 & 0
\end{array}\right]
$$
George's solution derives from [Kyle Simek's excellent and detailed series](http://ksimek.github.io/2013/06/03/calibrated_cameras_in_opengl/) on camera calibration and OpenGL, where more background and a generalized form is described.

[](#framing)Framing
-------------------

Sometimes it's desirable to change the position of the camera such that some object is framed relatively to the viewport. Unlike the very specific dolly zoom example above, the field of view is most likely a fixed size.

<video src="https://jsantell.com/3d-projection/framing.webm" control></video>

Illustration of how the position of a camera can change to accomodate framing any distance from the camera with any field of view. [Boy Finnigon](https://sketchfab.com/models/bbc1b4aa9e4c4e0ba6201e6129d05d23) by [Fion Noir](https://dadracodesign.de/) ([CC-BY-4.0](https://creativecommons.org/licenses/by/4.0/))

For example, a lot of thought went into creating the framing rules used in [model-viewer](https://github.com/GoogleWebComponents/model-viewer). We wanted an arbitrarily-sized model to look great inside of an arbitrarily sized viewport. To ensure good "framing", the model is placed inside of a "room" representing the camera frustum that maximizes the model's size given the current aspect ratio. The camera's near plane "frames" the room's forward plane.

Given a static vertical field of view, and the height of the frame in world units, the corresponding camera's position can be calculated via [similar triangles](https://en.wikipedia.org/wiki/Similarity_(geometry)#Similar_triangles), using values from **Figure 1** above. Using half of the height and half of the field of view (in radians), the distance can be derived the same way as the near plane (n=t/tan(fov/2)).

```
const d = (height / 2) / Math.tan(fov / 2)


```

Similarly, this can be done with horizontal field of view and extents, or revised to find [the size of a frustum at a given distance from the camera](https://docs.unity3d.com/Manual/FrustumSizeAtDistance.html).

[](#orthographic-projection)Orthographic projection
---------------------------------------------------

Orthographic projections lack perspective and are a bit more straight forward than perspective projections.

<figure><iframe title="Frustum visualization" src="https://jsantell.dev/frustum-viewer?extents&amp;orthographic" style="height:300px;" loading="lazy"></iframe><figcaption>Frustum visualization of orthographic projection.</figcaption></figure>

The orthographic projection matrix can be constructed from its extent values like perspective projection:
$$
\left[\begin{array}{cccc}
\frac{2}{r-l} & 0 & 0 & -\frac{r+l}{r-l} \\
0 & \frac{2}{t-b} & 0 & -\frac{t+b}{t-b} \\
0 & 0 & \frac{-2}{f-n} & -\frac{f+n}{f-n} \\
0 & 0 & 0 & 1
\end{array}\right]
$$
A simplified form can be used for symmetric projections, where
$r=-l$ and $t=-b .$
$$
\left[\begin{array}{cccc}
\frac{1}{r} & 0 & 0 & 0 \\
0 & \frac{1}{t} & 0 & 0 \\
0 & 0 & \frac{-2}{f-n} & -\frac{f+n}{f-n} \\
0 & 0 & 0 & 1
\end{array}\right]
$$

[](#resources-%26-references)Resources & References
---------------------------------------------------

*   [Kyle Simek: Calibrated cameras in OpenGL](http://ksimek.github.io/2013/06/03/calibrated_cameras_in_opengl/)
*   [Koshy George: Calculating OpenGL perspective matrix from OpenCV intrinsic matrix](http://kgeorge.github.io/2014/03/08/calculating-opengl-perspective-matrix-from-opencv-intrinsic-matrix)
*   [Song Ho Ahn: OpenGL Projection Matrix](http://www.songho.ca/opengl/gl_projectionmatrix.html)
*   [Foone Turing on the human visual system](https://twitter.com/foone/status/1014267515696922624)
*   [Decoding A Projection Matrix](http://xdpixel.com/decoding-a-projection-matrix/)
*   [Decompose the OpenGL Projection Matrix](https://lektiondestages.art.blog/2013/11/18/decompose-the-opengl-projection-matrix/)