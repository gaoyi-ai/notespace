---
title: Computer Graphic Rendering
categories:
- CG
- Rendering
tags:
- Rendering
date: 2021/7/13
---



> [graphics.fandom.com](https://graphics.fandom.com/wiki/Rendering)

> Rendering is the process of generating an image from a model, by means of a software program. The mod......

**Rendering** is the process of generating an image from a model, by means of a software program. The model is a description of three dimensional objects in a strictly defined language or data structure. It would contain geometry, viewpoint, [texture](https://graphics.fandom.com/wiki/Texture_mapping "Texture mapping") lighting information. The image is a [digital image](https://graphics.fandom.com/wiki/Digital_image "Digital image") or raster graphics image. The term may be by analogy with an "artist's rendering"of a scene.'Rendering' is also used to describe the process of calculating effects in a video editing file to produce final video output.

It is one of the major sub-topics of [3D computer graphics](https://graphics.fandom.com/wiki/3D_computer_graphics "3D computer graphics"), and in practice always connected to the others. In the 'graphics pipeline' it's the last major step, giving the final appearance to the models and animation. With the increasing sophistication of computer graphics since the 1970s onward, it has become a more distinct subject.

It has uses in: computer and video games, simulators, movies or TV special effects, and design visualisation, each employing a different balance of features and techniques. As a product, a wide variety of renderers are available. some are integrated into larger modelling and animation packages, some are stand-alone, some are free open-source projects. On the inside, a renderer is a carefully engineered program, based on a selective mixture of disciplines related to: light physics, visual perception, mathematics, and software development.

In the case of 3D graphics, rendering may be done slowly, as in pre-rendering, or in real time. Pre-rendering is a computationally intensive process that is typically used for movie creation, while real-time rendering is often done for 3D video games which rely on the use of graphics cards with 3D hardware accelerators.

Usage[[edit](https://graphics.fandom.com/wiki/Rendering?veaction=edit&section=1 "Edit section: Usage") | [edit source](https://graphics.fandom.com/wiki/Rendering?section=1&veaction=editsource "Edit section: Usage")]
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

When the pre-image (a wireframe sketch usually) is complete, rendering is used, which adds in bitmap textures or procedural textures, lights, [bump mapping](https://graphics.fandom.com/wiki/Bump_mapping "Bump mapping"), and relative position to other objects. The result is a completed image the consumer or intended viewer sees.

For movie animations, several images (frames) must be rendered, and stitched together in a program capable of making an animation of this sort. Most 3D image editing programs can do this.

Features[[edit](https://graphics.fandom.com/wiki/Rendering?veaction=edit&section=2 "Edit section: Features") | [edit source](https://graphics.fandom.com/wiki/Rendering?section=2&veaction=editsource "Edit section: Features")]
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

A rendered image can be understood in terms of a number of visible features. Rendering research and development has been largely motivated by finding ways to simulate these efficiently. Some relate directly to particular algorithms and techniques, while others are produced together.

*   [**shading**](https://graphics.fandom.com/wiki/Flat_shading "Flat shading") — how the color and brightness of a surface varies with lighting
*   [**texture-mapping**](https://graphics.fandom.com/wiki/Texture_mapping "Texture mapping") — a method of applying detail to surfaces
*   [**bump-mapping**](https://graphics.fandom.com/wiki/Bump_mapping "Bump mapping") — a method of simulating small-scale bumpiness on surfaces
*   **fogging/participating medium** — how light dims when passing through non-clear atmosphere or air
*   **shadows** — the effect of obstructing light
*   **soft shadows** — varying darkness caused by partially obscured light sources
*   **reflection** — mirror-like or highly glossy reflection
*   **transparency** — sharp transmission of light through solid objects
*   **translucency** — highly scattered transmission of light through solid objects
*   **[refraction](https://graphics.fandom.com/wiki/Refraction "Refraction")** — bending of light associated with transparency
*   **indirect illumination** — surfaces illuminated by light reflected off other surfaces, rather than directly from a light source
*   **caustics** (a form of indirect illumination) — reflection of light off a shiny object, or focusing of light through a transparent object, to produce bright highlights on another object
*   **depth of field** — objects appear blurry or out of focus when too far in front of or behind the object in focus
*   **motion blur** — objects appear blurry due to high-speed motion, or the motion of the camera
*   **photorealistic morphing** — photoshopping 3D renderings to appear more life-like
*   **non-photorealistic rendering** — rendering of scenes in an artistic style, intended to look like a painting or drawing

techniques
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Many rendering algorithms have been researched, and software used for rendering may employ a number of different techniques to obtain a final image.

Tracing every ray of light in a scene would be impractical and would take gigantic amounts of time. Even tracing a portion large enough to produce an image takes an inordinate amount of time if the sampling is not intelligently restricted.

Therefore, four loose families of more-efficient light transport modelling techniques have emerged: **rasterisation**, including **scanline rendering**, considers the objects in the scene and projects them to form an image, with no facility for generating a point-of-view perspective effect
**光栅化**，包括**扫描线渲染**，考虑场景中的对象并将它们投影以形成图像，无法生成视点透视效果; **ray casting** considers the scene as observed from a specific point-of-view, calculating the observed image based only on geometry and very basic optical laws of reflection intensity, and perhaps using Monte Carlo techniques to reduce artifacts; **[radiosity](https://graphics.fandom.com/wiki/Radiosity "Radiosity")** uses finite element mathematics to simulate diffuse spreading of light from surfaces; and **[ray tracing](https://graphics.fandom.com/wiki/Ray_tracing "Ray tracing")** is similar to ray casting, but employs more advanced optical simulation, and usually uses Monte Carlo techniques, to obtain more realistic results, at a speed which is often orders of magnitude slower.

Most advanced software combines two or more of the techniques to obtain good-enough results at reasonable cost.

### Scanline rendering and rasterisation

A high-level representation of an image necessarily contains elements in a different domain from pixels. These elements are referred to as primitives. In a schematic drawing, for instance, line segments and curves might be primitives. In a graphical user interface, windows and buttons might be the primitives. In 3D rendering, triangles and polygons in space might be primitives.

图像的高级表示必然包含与像素不同域中的元素。 这些元素被称为基元。 例如，在示意图中，线段和曲线可能是图元。 在图形用户界面中，窗口和按钮可能是原语。 在 3D 渲染中，空间中的三角形和多边形可能是图元。

If a pixel-by-pixel approach to rendering is impractical or too slow for some task, then a primitive-by-primitive approach to rendering may prove useful. Here, one loops through each of the primitives, determines which pixels in the image it affects, and modifies those pixels accordingly. This is called **rasterization**, and is the rendering method used by all current graphics cards.

如果逐像素渲染方法对某些任务不切实际或太慢，那么逐个原始渲染方法可能会证明是有用的。 在这里，一个循环遍历每个基元，确定它影响图像中的哪些像素，并相应地修改这些像素。 这称为**光栅化**，是当前所有显卡使用的渲染方法。

Rasterization is frequently faster tharge areas of the image may be empty of primitives; rasterization will ignore these areas, but pixel-by-pixel rendering must pass through them. Second, rasterization can improve cache coherency and reduce redundant work by taking advantage of the fact that the pixels occupied by a single primitive tend to be contiguous in the image. For these reasons, rasterization is usually the approach of choice when interactive rendering is required; however, the pixel-by-pixel approach can often produce higher-quality images and is more versatile because it does not depend on as many assumptions about the image as rasterization.

光栅化通常更快，图像的大区域可能没有图元； 光栅化会忽略这些区域，但逐像素渲染必须通过它们。 其次，光栅化可以利用单个图元占据的像素在图像中往往是连续的这一事实来提高缓存一致性并减少冗余工作。 由于这些原因，当需要交互式渲染时，光栅化通常是首选方法； 然而，逐像素方法通常可以产生更高质量的图像并且更加通用，因为它不像光栅化那样依赖关于图像的那么多假设。

Rasterization exists in two main forms, not only when an entire face (primitive) is rendered but when the vertices of a face are all rendered and then the pixels on the face which lie between the vertices rendered using simple blending of each vertex colour to the next, this version of rasterization has overtaken the old method as it allows the graphics to flow without complicated textures (a rasterized image when used face by face tends to have a very block like effect if not covered in complex textures, the faces aren't smooth because there is no gradual smoothness from one pixel to the next,) this meens that you can utilise the graphics card's more taxing shading functions and still achieve better performance because you have freed up space o the card because complex textures aren't necessary. sometimes people will use one rasterization method on some faces and the other method on others based on the angle at which that face meets other joined faces, this can increase speed and not take away too much from the images overall effect.

### Ray casting

Ray casting is primarily used for real time simulations, such as those used in 3D computer games and cartoon animations, where detail is not important, or where it is more efficient to manually fake the details in order to obtain better performance in the computational stage. This is usually the case when a large number of frames need to be animated. The results have a characteristic 'flat' appearance when no additional tricks are used, as if objects in the scene were all painted with matt finish, or had been lightly sanded.

The geometry which has been modelled is parsed pixel by pixel, line by line, from the point of view outward, as if casting rays out from the point of view. Where an object is intersected, the colour value at the point may be evaluated using several methods. In the simplest, the colour value of the object at the point of intersection becomes the value of that pixel. The colour may be determined from a [texture-map](https://graphics.fandom.com/wiki/Texture_mapping "Texture mapping"). A more sophisticated method is to modify the colour value by an illumination factor, but without calculating the relationship to a simulated light source. To reduce artifacts, a number of rays in slightly different directions may be averaged.

Rough simulations of optical properties may be additionally employed: commonly, making a very simple calculation of the ray from the object to the point of view. Another calculation is made of the angle of incidence of light rays from the light source(s). And from these and the specified intensities of the light sources, the value of the pixel is calculated.

Or illumination plotted from a radiosity algorithm could be employed. Or a combination of these.

### Radiosity

Radiosity is a method which attempts to simulate the way in which reflected light, instead of just reflecting to another surface, also illuminates the area around it. This produces more realistic shading and seems to better capture the 'ambience' of an indoor scene, a classic example used is of the way that shadows 'hug' the corners of rooms.

辐射是一种方法，它试图模拟反射光的方式，而不是仅仅反射到另一个表面，还照亮它周围的区域。 这会产生更逼真的阴影，似乎能更好地捕捉室内场景的“氛围”，使用的一个经典示例是阴影“拥抱”房间角落的方式。

The optical basis of the simulation is that some diffused light from a given point on a given surface is reflected in a large spectrum of directions and illuminates the area around it.

The simulation technique may vary in complexity. Many renderings have a very rough estimate of radiosity, simply illuminating an entire scene very slightly with a factor known as ambience. However, when advanced radiosity estimation is coupled with a high quality ray tracing algorithim, images may exhibit convincing realism, particularly for indoor scenes.

In advanced radiosity simulation, recursive, finite-element algorithms 'bounce' light back and forth between surfaces in the model, until some recursion limit is reached. The colouring of one surface in this way influences the colouring of a neighbouring surface, and vice versa. The resulting values of illumination throughout the model (sometimes including for empty spaces) are stored and used as additional inputs when performing calculations in a ray-casting or ray-tracing model.

Due to the iterative/recursive nature of the technique, complex objects are particularly slow to emulate. Advanced radiosity calculations may be reserved for calulating the ambience of the room, from the light reflecting off walls, floor and celiing, without examining the contribution that complex objects make to the radiosity -- or complex objects may be replaced in the radiosity calculation with simpler objects of similar size and texture.

If there is little rearrangement of radiosity objects in the scene, the same radiosity data may be reused for a number of frames, making radiosity an effective way to improve on the flatness of ray casting, without seriously impacting the overall rendering time-per-frame.

Because of this, radiosity has become the leading real-time rendering method, and has been used to beginning-to-end create a large number of well-known recent feature-length animated 3D-cartoon films.

### Ray tracing

Ray tracing is an extension of the same technique developed in scanline rendering and ray casting. Like those, it handles complicated objects well, and the objects may be described mathematically. Unlike scanline and casting, ray tracing is almost always a Monte Carlo technique, that is one based on averaging a number of randomly generated samples from a model.

In this case, the samples are imaginary rays of light intersecting the viewpoint from the objects in the scene. It is primarily beneficial where complex and accurate rendering of shadows, refraction or reflection are issues.

In a final, production quality rendering of a ray traced work, multiple rays are generally shot for each pixel, and traced not just to the first object of intersection, but rather, through a number of sequential 'bounces', using the known laws of optics such as "angle of incidence equals angle of reflection" and more advanced laws that deal with refraction and surface roughness.

Once the ray either encounters a light source, or more probably once a set limiting number of bounces has been evaluated, then the surface illumination at that final point is evaluated using techniques described above, and the changes along the way through the various bounces evaluated to estimate a value observed at the point of view. This is all repeated for each sample, for each pixel.

In some cases, at each point of intersection, multiple rays may be spawned.

As a brute-force method, raytracing has been too slow to consider for realtime, and until recently too slow even to consider for short films of any degree of quality, although it has been used for special effects sequences, and in advertising, where a short portion of high quality (perhaps even photorealistic) footage is required.

However efforts at optimising to reduce the number of calculations needed in portions of a work where detail is not high or does not depend on raytracing features, has lead to a realistic possiblity of wider use of ray tracing. There is now some hardware accelerated ray tracing equipment, at least in prototype phase, and some game demos which show use of realtime software or hardware ray tracing.

Optimisation
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

### Optimisations used by an artist when a scene is being developed

Due to the large number of calulations, a work in progress is usually only rendered in detail appropriate to the portion of the work being developed at a given time, so in the initial stages of modelling, wireframe and ray casting may be used, even where the target output is ray tracing with radiosity. It is also common to render only parts of the scene at high detail, and to remove objects that are not important to what is currently being developed.

### Common optimisations for real time rendering[[edit](https://graphics.fandom.com/wiki/Rendering?veaction=edit&section=10 "Edit section: Common optimisations for real time rendering") | [edit source](https://graphics.fandom.com/wiki/Rendering?section=10&veaction=editsource "Edit section: Common optimisations for real time rendering")]

For real-time, it is appropriate to simplify one or more common approximations, and tune to the exact parameters of the scenery in question, which is also tuned to the agreed parameters to get the most 'bang for buck'.

There are some lesser known approaches to rendering, as spherical harmonics. These techniques are lesser known often due to slow speed, lack of practical use or simply because they are in early stages of development, maybe some will offer a new solution.

Sampling and filtering[[edit](https://graphics.fandom.com/wiki/Rendering?veaction=edit&section=11 "Edit section: Sampling and filtering") | [edit source](https://graphics.fandom.com/wiki/Rendering?section=11&veaction=editsource "Edit section: Sampling and filtering")]
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

One problem that any rendering system must deal with, no matter which approach it takes, is the **sampling problem**. Essentially, the rendering process tries to depict a continuous function from image space to colors by using a finite number of pixels. As a consequence of the Nyquist theorem, the scanning frequency must be twice the dot rate, which is proportional to [image resolution](https://graphics.fandom.com/wiki/Image_resolution "Image resolution"). In simpler terms, this expresses the idea that an image cannot display details smaller than one pixel.

If a naive rendering algorithm is used, high frequencies in the image function will cause ugly [aliasing](https://graphics.fandom.com/wiki/Aliasing "Aliasing") to be present in the final image. Aliasing typically manifests itself or jagged edges on objects where the pixel grid is visible. In order to remove aliasing, all rendering algorithms (if they are to produce good-looking images) must filter the image function to remove high frequencies, a process called antialiasing.

See also[[edit](https://graphics.fandom.com/wiki/Rendering?veaction=edit&section=12 "Edit section: See also") | [edit source](https://graphics.fandom.com/wiki/Rendering?section=12&veaction=editsource "Edit section: See also")]
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

*   the painter's algorithm
*   Scanline algorithms like [Reyes](https://graphics.fandom.com/wiki/Reyes_rendering "Reyes rendering")
*   Z-buffer algorithms
*   Global illumination
*   [Radiosity](https://graphics.fandom.com/wiki/Radiosity "Radiosity")
*   [Ray tracing](https://graphics.fandom.com/wiki/Ray_tracing "Ray tracing")
*   Volume rendering

Rendering for movies often takes place on a network of tightly connected computers known as a render farm.

The current state of the art in 3-D image description for movie creation is the RenderMan scene description language designed at Pixar. (compare with simpler 3D fileformats such as VRML or APIs such as [OpenGL](https://graphics.fandom.com/wiki/OpenGL "OpenGL") and DirectX tailored for 3D hardware accelerators).

Movie type rendering software includes:

*   RenderMan compliant renderers
*   Mental Ray
*   Brazil
*   Blender (may also be used for modeling)

Academic core[[edit](https://graphics.fandom.com/wiki/Rendering?veaction=edit&section=13 "Edit section: Academic core") | [edit source](https://graphics.fandom.com/wiki/Rendering?section=13&veaction=editsource "Edit section: Academic core")]
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

The implementation of a realistic renderer always has some basic element of physical simulation or emulation — some computation which resembles or abstracts a real physical process.

The term "_physically-based_" indicates the use of physical models and approximations that are more general and widely accepted outside rendering. A particular set of related techniques have gradually become established in the rendering community.

The basic concepts are moderately straightforward, but intractable to calculate; and a single elegant algorithm or approach has been elusive for more general purpose renderers. In order to meet demands of robustness, accuracy, and practicality, an implementation will be a complex combination of different techniques.

Rendering research is concerned with both the adaptation of scientific models and their efficient application.

### The rendering equation[[edit](https://graphics.fandom.com/wiki/Rendering?veaction=edit&section=14 "Edit section: The rendering equation") | [edit source](https://graphics.fandom.com/wiki/Rendering?section=14&veaction=editsource "Edit section: The rendering equation")]

This is the key academic/theoretical concept in rendering. It serves as the most abstract formal expression of the non-perceptual aspect of rendering. All more complete algorithms can be seen as solutions to particular formulations of this equation.

![](https://wikimedia.org/api/rest_v1/media/math/render/png/2e9b47791ca5246c6d87db2d5ec4e023c5c71bf2)

Meaning: at a particular position and direction, the outgoing light (Lo) is the sum of the emitted light (Le) and the reflected light. The reflected light being the sum of the incoming light (Li) from all directions, multiplied by the surface reflection and incoming angle. By connecting outward light to inward light, via an interaction point, this equation stands for the whole 'light transport' — all the movement of light — in a scene.

### The Bidirectional Reflectance Distribution Function[[edit](https://graphics.fandom.com/wiki/Rendering?veaction=edit&section=15 "Edit section: The Bidirectional Reflectance Distribution Function") | [edit source](https://graphics.fandom.com/wiki/Rendering?section=15&veaction=editsource "Edit section: The Bidirectional Reflectance Distribution Function")]

The **Bidirectional Reflectance Distribution Function** (BRDF) expresses a simple model of light interaction with a surface as follows:

![](https://wikimedia.org/api/rest_v1/media/math/render/png/7fe0667913736d25488de5a050c4ddbec48eb56a)

Light interaction is often approximated by the even simpler models: diffuse reflection and specular reflection, although both can be BRDFs.

### Geometric optics[[edit](https://graphics.fandom.com/wiki/Rendering?veaction=edit&section=16 "Edit section: Geometric optics") | [edit source](https://graphics.fandom.com/wiki/Rendering?section=16&veaction=editsource "Edit section: Geometric optics")]

Rendering is practically exclusively concerned with the particle aspect of light physics — known as geometric optics. Treating light, at its basic level, as particles bouncing around is a simplification, but appropriate: the wave aspects of light are negligible in most scenes, and are significantly more difficult to simulate. Notable wave aspect phenomena include diffraction — as seen in the colours of CDs and DVDs — and polarisation — as seen in LCDs. Both types of effect, if needed, are made by appearance-oriented adjustment of the reflection model.

### Visual perception[[edit](https://graphics.fandom.com/wiki/Rendering?veaction=edit&section=17 "Edit section: Visual perception") | [edit source](https://graphics.fandom.com/wiki/Rendering?section=17&veaction=editsource "Edit section: Visual perception")]

Though it receives less attention, an understanding of human visual perception is valuable to rendering. This is mainly because image displays and human perception have restricted ranges. A renderer can simulate an almost infinite range of light brightness and color, but current displays — movie screen, computer monitor, etc. — cannot handle so much, and something must be discarded or compressed. Human perception also has limits, and so doesn't need to be given large-range images to create realism. This can help solve the problem of fitting images into displays, and, furthermore, suggest what short-cuts could be used in the rendering simulation, since certain subtleties won't be noticeable. This related subject is tone mapping.

Mathematics used in rendering includes: linear algebra, calculus, numerical mathematics, signal processing, monte carlo.

Chronology of important published ideas[[edit](https://graphics.fandom.com/wiki/Rendering?veaction=edit&section=18 "Edit section: Chronology of important published ideas") | [edit source](https://graphics.fandom.com/wiki/Rendering?section=18&veaction=editsource "Edit section: Chronology of important published ideas")]
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

*   1970 **Scan-line algorithm** (Bouknight, W. J. (1970). A procedure for generation of three-dimensional half-tone computer graphics presentations. _Communications of the ACM_)
*   1971 **Gouraud shading** (Gouraud, H. (1971). Computer display of curved surfaces. _IEEE Transactions on Computers_ **20** (6), 623–629.)
*   1974 **Texture mapping** (Catmull, E. (1974). A subdivision algorithm for computer display of curved surfaces. _PhD thesis_, University of Utah.)
*   1974 **Z-buffer** (Catmull, E. (1974). A subdivision algorithm for computer display of curved surfaces. _PhD thesis_)
*   1975 **Phong shading** (Phong, B-T. (1975). Illumination for computer generated pictures. _Communications of the ACM_ **18** (6), 311–316.)
*   1976 **Environment mapping** (Blinn, J.F., Newell, M.E. (1976). Texture and reflection in computer generated images. _Communications of the ACM_ **19**, 542–546.)
*   1977 **Shadow volumes** (Crow, F.C. (1977). Shadow algorithms for computer graphics. _Computer Graphics (Proceedings of SIGGRAPH 1977)_ **11** (2), 242–248.)
*   1978 **Shadow buffer** (Williams, L. (1978). Casting curved shadows on curved surfaces. _Computer Graphics (Proceedings of SIGGRAPH 1978)_ **12** (3), 270–274.)
*   1978 **Bump mapping** (Blinn, J.F. (1978). Simulation of wrinkled surfaces. _Computer Graphics (Proceedings of SIGGRAPH 1978)_ **12** (3), 286–292.)
*   1980 **BSP trees** (Fuchs, H. Kedem, Z.M. Naylor, B.F. (1980). On visible surface generation by a priori tree structures. _Computer Graphics (Proceedings of SIGGRAPH 1980)_ **14** (3), 124–133.)
*   1980 **Ray tracing** (Whitted, T. (1980). An improved illumination model for shaded display. _Communications of the ACM_ **23** (6), 343–349.)
*   1981 **Cook shader** (Cook, R.L. Torrance, K.E. (1981). A reflectance model for computer graphics. _Computer Graphics (Proceedings of SIGGRAPH 1981)_ **15** (3), 307–316.)
*   1983 **Mipmaps** (Williams, L. (1983). Pyramidal parametrics. _Computer Graphics (Proceedings of SIGGRAPH 1983)_ **17** (3), 1–11.)
*   1984 **Octree ray tracing** (Glassner, A.S. (1984). Space subdivision for fast ray tracing. _IEEE Computer Graphics & Applications_ **4** (10), 15–22.)
*   1984 **Alpha compositing** (Porter, T. Duff, T. (1984). Compositing digital images. _Computer Graphics (Proceedings of SIGGRAPH 1984)_ **18** (3), 253–259.)
*   1984 **Distributed ray tracing** (Cook, R.L. Porter, T. Carpenter, L. (1984). Distributed ray tracing. _Computer Graphics (Proceedings of SIGGRAPH 1984)_ **18** (3), 137–145.)
*   1984 **Radiosity** (Goral, C. Torrance, K.E. Greenberg, D.P. Battaile, B. (1984). Modelling the interaction of light between diffuse surfaces. _Computer Graphics (Proceedings of SIGGRAPH 1984)_ **18** (3), 213–222.)
*   1985 **Hemi-cube radiosity** (Cohen, M.F. Greenberg, D.P. (1985). The hemi-cube: a radiosity solution for complex environments. _Computer Graphics (Proceedings of SIGGRAPH 1985)_ **19** (3), 31–40.)
*   1986 **Light source tracing** (Arvo, J. (1986). Backward ray tracing. _SIGGRAPH 1986 Developments in Ray Tracing course notes_)
*   1986 **Rendering equation** (Kajiya, J.T. (1986). The rendering equation. _Computer Graphics (Proceedings of SIGGRAPH 1986)_ **20** (4), 143–150.)
*   1987 **[Reyes](https://graphics.fandom.com/wiki/Reyes_rendering "Reyes rendering") algorithm** (Cook, R.L. Carpenter, L. Catmull, E. (1987). The reyes image rendering architecture. _Computer Graphics (Proceedings of SIGGRAPH 1987)_ **21** (4), 95–102.)
*   1991 **Hierarchical radiosity** (Hanrahan, P. Salzman, D. Aupperle, L. (1991). A rapid hierarchical radiosity algorithm. _Computer Graphics (Proceedings of SIGGRAPH 1991)_ **25** (4), 197–206.)
*   1993 **Tone mapping** (Tumblin, J. Rushmeier, H.E. (1993). Tone reproduction for realistic computer generated images. _IEEE Computer Graphics & Applications_ **13** (6), 42–48.)
*   1993 **Subsurface scattering** (Hanrahan, P. Krueger, W. (1993). Reflection from layered surfaces due to subsurface scattering. _Computer Graphics (Proceedings of SIGGRAPH 1993)_ **27** (), 165–174.)
*   1995 **Photon mapping** (Jensen, H.J. Christensen, N.J. (1995). Photon maps in bidirectional monte carlo ray tracing of complex objects. _Computers & Graphics_ **19** (2), 215–224.)

See also[[edit](https://graphics.fandom.com/wiki/Rendering?veaction=edit&section=19 "Edit section: See also") | [edit source](https://graphics.fandom.com/wiki/Rendering?section=19&veaction=editsource "Edit section: See also")]
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

*   Pre-rendered
*   [Graphics pipeline](https://graphics.fandom.com/wiki/Graphics_pipeline "Graphics pipeline")
*   Virtual model

Books and summaries[[edit](https://graphics.fandom.com/wiki/Rendering?veaction=edit&section=20 "Edit section: Books and summaries") | [edit source](https://graphics.fandom.com/wiki/Rendering?section=20&veaction=editsource "Edit section: Books and summaries")]
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

*   Foley; Van Dam; Feiner; Hughes (1990). _Computer Graphics: Principles And Practice_. Addison Wesley. [ISBN 0201121107](https://graphics.fandom.com/wiki/Special:BookSources/0201121107).
*   Glassner (1995). _Principles Of Digital Image Synthesis_. Morgan Kaufmann. [ISBN 1558602763](https://graphics.fandom.com/wiki/Special:BookSources/1558602763).
*   Pharr; Humphreys (2004). _Physically Based Rendering_. Morgan Kaufmann. [ISBN 012553180X](https://graphics.fandom.com/wiki/Special:BookSources/012553180X).
*   Dutre; Bala; Bekaert (2002). _Advanced Global Illumination_. AK Peters. [ISBN 1568811772](https://graphics.fandom.com/wiki/Special:BookSources/1568811772).
*   Jensen (2001). _Realistic Image Synthesis Using Photon Mapping_. AK Peters. [ISBN 1568811470](https://graphics.fandom.com/wiki/Special:BookSources/1568811470).
*   Shirley; Morley (2003). _Realistic Ray Tracing_ (2nd ed.). AK Peters. [ISBN 1568811985](https://graphics.fandom.com/wiki/Special:BookSources/1568811985).
*   Glassner (1989). _An Introduction To Ray Tracing_. Academic Press. [ISBN 0122861604](https://graphics.fandom.com/wiki/Special:BookSources/0122861604).
*   Cohen; Wallace (1993). _Radiosity and Realistic Image Synthesis_. AP Professional. [ISBN 0121782700](https://graphics.fandom.com/wiki/Special:BookSources/0121782700).
*   Akenine-Moller; Haines (2002). _Real-time Rendering_ (2nd ed.). AK Peters. [ISBN 1568811829](https://graphics.fandom.com/wiki/Special:BookSources/1568811829).
*   Gooch; Gooch (2001). _Non-Photorealistic Rendering_. AKPeters. [ISBN 1568811330](https://graphics.fandom.com/wiki/Special:BookSources/1568811330).
*   Strothotte; Schlechtweg (2002). _Non-Photorealistic Computer Graphics_. Morgan Kaufmann. [ISBN 1558607870](https://graphics.fandom.com/wiki/Special:BookSources/1558607870).
*   Blinn (1996). _Jim Blinns Corner - A Trip Down The Graphics Pipeline_. Morgan Kaufmann. [ISBN 1558603875](https://graphics.fandom.com/wiki/Special:BookSources/1558603875).
*   [Description of the 'Radiance' system](http://radsite.lbl.gov/radiance/papers/sg94.1/)

External links[[edit](https://graphics.fandom.com/wiki/Rendering?veaction=edit&section=21 "Edit section: External links") | [edit source](https://graphics.fandom.com/wiki/Rendering?section=21&veaction=editsource "Edit section: External links")]
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

*   [SIGGRAPH](http://www.siggraph.org/) The ACMs special interest group in graphics — the largest academic and professional association and conference.
*   [Ray Tracing News](http://www.raytracingnews.org/) A newsletter on ray tracing technical matters.
*   [Real-Time Rendering resources](http://www.realtimerendering.com/) A list of links to resources, associated with the _Real-Time Rendering_ book.
*   [http://www.graphicspapers.com/](http://www.graphicspapers.com/) Database of graphics papers citations.
*   [http://www.cs.brown.edu/~tor/](http://www.cs.brown.edu/~tor/) List of links to (recent) siggraph papers (and some others) on the web.
*   [http://www.pointzero.nl/renderers/](http://www.pointzero.nl/renderers/) List of links to all kinds of renderers.
*   [http://www.renderman.org/](http://www.renderman.org/)
*   ['Radiance' renderer.](http://radsite.lbl.gov/radiance/) A highly accurate ray-tracing software system.
*   [Pixie](http://pixie.sourceforge.net/) An efficient and free RenderMan compatible OpenSource renderer.
*   ['Aqsis' renderer](http://www.aqsis.org/) A free RenderMan compatible OpenSource REYES renderer.
*   [http://www.povray.org/](http://www.povray.org/) A free ray tracer.
*   [http://www.daylongraphics.com/products/leveller/render/techniques.htm](http://www.daylongraphics.com/products/leveller/render/techniques.htm)