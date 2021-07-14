---
title: Computer Graphic Rendering - gentle explain
categories:
- CG
- Rendering
tags:
- Rendering
date: 2021/7/13
---



# Computer Graphic Rendering - gentle explain

Walk into a darkened room and turn on the lights. The gleam of furniture, the color and texture of carpets and curtains are instantly visible. A simple everyday act-but the science behind it is a complex and increasingly important part of materials design and manufacturing. Light from a light source hits a surface and part of it is scattered and transmitted to your eyes. The visual perception of an object- what we call its appearance arises from the interaction of incident light with the object's surface geometry or texture. Optical properties of the material (such as index of refraction or polarization) also play a very important role. Will there come a day when given the microstructural and optical properties of a material a computer program can create the image of a chair made with that material? Making that day a reality will depend on the outcome of research done today.

走进一个黑暗的房间，打开灯。 家具的光泽、地毯和窗帘的颜色和质地都一目了然。 一个简单的日常行为——但其背后的科学是材料设计和制造中一个复杂且日益重要的部分。 来自光源的光照射到表面，其中一部分被散射并传输到您的眼睛。 物体的视觉感知——我们称之为外观的东西来自入射光与物体表面几何形状或纹理的相互作用。 材料的光学特性（例如折射率或偏振）也起着非常重要的作用。 考虑到一种材料的微观结构和光学特性，计算机程序会创建出用这种材料制成的椅子的图像吗？ 使这一天成为现实将取决于今天所做的研究结果。

For some years computer programs have produced images of scenes based on a simulation of scattering and reflection of light off one or more surfaces. In response to increasing demand for the use of rendering in design and manufacturing, the models used in these programs have undergone intense research in the computer graphics community. In particular, more physically realistic models are sought (i.e. models that more accurately depict the physics of light interaction).

多年来，计算机程序已经基于对光从一个或多个表面的散射和反射的模拟来生成场景图像。 为了响应在设计和制造中使用渲染的日益增长的需求，这些程序中使用的模型已经在计算机图形界进行了深入的研究。 特别是，寻求更真实的物理模型（即更准确地描述光相互作用物理的模型）。

---

> [ExtremeTech 3D Pipeline Tutorial](https://www.extremetech.com/computing/49076-extremetech-3d-pipeline-tutorial/2)

Because of the sequential nature of 3D graphics rendering, and because there are so many calculations to be done and volumes of data to be handled, the entire process is broken down into component steps, sometimes called stages. These stages are serialized into the aforementioned 3D graphics pipeline.

由于 3D 图形渲染的顺序性质，并且由于需要进行大量计算和处理大量数据，整个过程被分解为多个组件步骤，有时称为阶段。 这些阶段被序列化到前面提到的 3D 图形管道中。

The huge amount of work involved in creating a scene has led 3D rendering system designers (both hardware and software) to look for all possible ways to avoid doing unnecessary work. One designer quipped, “3D graphics is the art of cheating without getting caught.” Translated, this means that one of the art-forms in 3D graphics is to elegantly reduce visual detail in a scene so as to gain better performance, but do it in such a way that the viewer doesn’t notice the loss of quality. Processor and memory bandwidth are precious commodities, so anything designers can do to conserve them benefits performance greatly. One quick example of this is culling, which tells the renderer, “If the view camera (the viewer’s eye) can’t see it, don’t bother processing it and only worry about what the view camera can see.”

创建场景所涉及的大量工作促使 3D 渲染系统设计人员（硬件和软件）寻找所有可能的方法来避免做不必要的工作。 一位设计师打趣道：“3D 图形是一种作弊而不会被抓住的艺术。” 换而言之，这意味着 3D 图形中的一种艺术形式是优雅地减少场景中的视觉细节以获得更好的性能，但要以观看者不会注意到质量损失的方式进行。 处理器和内存带宽是宝贵的商品，因此设计人员可以采取任何措施来保护它们，从而极大地提高性能。 一个简单的例子是剔除，它告诉渲染器，“如果视图相机（观察者的眼睛）看不到它，不要费心处理它，只关心视图相机能看到什么。”

With the number of steps involved and their complexity, the ordering of these stages of the pipeline can vary between implementations. While we’ll soon inspect the operations within these stages in much more detail, broadly described, the general ordering of a 3D pipeline breaks down into four sections: Application/Scene, Geometry, Triangle Setup, and Rasterization/Rendering. While the following outline of these sections may look daunting, by the time you’re done reading this story, you’ll be among an elite few who really understand how 3D graphics works, and we think you’ll want to get even deeper!

由于涉及的步骤数量及其复杂性，管道的这些阶段的顺序可能因实现而异。 虽然我们很快会更详细地检查这些阶段中的操作，并进行广泛描述，但 3D 管道的一般顺序分为四个部分：应用程序/场景、几何、三角形设置和光栅化/渲染。 虽然这些部分的以下大纲可能看起来令人生畏，但当您读完这个故事时，您将成为真正了解 3D 图形工作原理的少数精英之一，我们认为您会想要更深入！

**3D Pipeline – High-Level Overview**  

1. Application/Scene  
    - Scene/Geometry database traversal
    - Movement of objects, and aiming and movement of view camera
    - Animated movement of object models
    - Description of the contents of the 3D world
    - Object Visibility Check including possible Occlusion Culling
    - Select Level of Detail (LOD)
2. Geometry  
	- Transforms (rotation, translation, scaling) 
	- Transform from Model Space to World Space (Direct3D) 
	- Transform from World Space to View Space 
	- View Projection 
	- Trivial Accept/Reject Culling 
	- Back-Face Culling (can also be done later in Screen Space)  Lighting 
	- Perspective Divide – Transform to Clip Space 
	- Clipping 
	- Transform to Screen Space 
3. Triangle Setup 
	- Back-face Culling (or can be done in view space before lighting)
	- Slope/Delta Calculations
	- Scan-Line Conversion
4. Rendering / Rasterization
	- Shading
	- Texturing
	- Fog
	- Alpha Translucency Tests
	- Depth Buffering
	- Antialiasing (optional)
	- Display

**Where the Work Gets Done**

While numerous high-level aspects of the 3D world are managed by the application software at the application stage of the pipeline (which some argue isn’t technically part of the 3D pipeline), the last three major stages of the pipeline are often managed by an Application Programming Interface (API), such as SGI’s OpenGL, Microsoft’s Direct3D, or Pixar’s Renderman. And graphics drivers and hardware are called by the APIs to performing many of the graphics operations in hardware.

虽然 3D 世界的许多高级方面在管道的应用阶段由应用软件管理（有些人认为这在技术上不是 3D 管道的一部分），但管道的最后三个主要阶段通常由 应用程序编程接口 (API)，例如 SGI 的 OpenGL、Microsoft 的 Direct3D 或 Pixar 的 Renderman。 图形驱动程序和硬件由 API 调用以在硬件中执行许多图形操作。

Graphics APIs actually abstract the application from the hardware, and vice versa, providing the application with true device independence. Thus, such APIs are often called Hardware Abstraction Layers (HALs). The goal of this design is pretty straightforward– application makers can write their program once to an API, and it will (should) run on any hardware whose drivers support that API. Conversely, hardware makers write their drivers up to the API, and in this way applications written to this API will (should) run on their hardware. The parenthetical “should’s” are added because there are sometimes compatibility issues as a result of incorrect usages of the API (called violations), that might cause application dependence on particular hardware features, or incorrect implementation of API features in a hardware driver, resulting in incorrect or unexpected results.

图形 API 实际上从硬件中抽象了应用程序，反之亦然，为应用程序提供了真正的设备独立性。 因此，此类 API 通常称为硬件抽象层 (HAL)。 这种设计的目标非常简单——应用程序制造商可以将他们的程序编写到 API 中，并且它将（应该）运行在驱动程序支持该 API 的任何硬件上。 相反，硬件制造商将他们的驱动程序编写到 API，这样编写到这个 API 的应用程序将（应该）在他们的硬件上运行。 添加括号中的“应该”是因为有时由于 API 的不正确使用（称为违规）会导致兼容性问题，这可能会导致应用程序依赖特定的硬件功能，或者在硬件驱动程序中不正确地实现 API 功能，从而导致 不正确或意外的结果。

Before we dive into pipeline details, we need to first understand the high level view of how 3D objects and 3D worlds are defined and how objects are defined, placed, located, and manipulated in larger three-dimensional spaces, or even within their own boundaries. In a 3D rendering system, multiple Cartesian coordinate systems (x- (left/right), y- (up/down) and z-axis (near/far)) are used at different stages of the pipeline. While used for different though related purposes, each coordinate system provides a precise mathematical method to locate and represent objects in the space. And not surprisingly, each of these coordinate systems is referred to as a “space.”

在我们深入研究管道细节之前，我们需要首先了解 3D 对象和 3D 世界如何定义的高级视图，以及如何在更大的 3D 空间中甚至在它们自己的边界内定义、放置、定位和操作对象 . 在 3D 渲染系统中，管道的不同阶段使用多个笛卡尔坐标系（x-（左/右）、y-（上/下）和 z 轴（近/远））。 虽然用于不同但相关的目的，但每个坐标系都提供了一种精确的数学方法来定位和表示空间中的对象。 毫不奇怪，这些坐标系中的每一个都被称为“空间”。

Objects in the 3D scene and the scene itself are sequentially converted, or transformed, through five spaces when proceeding through the 3D pipeline. A brief overview of these spaces follows:

**Model Space:** where each model is in its own coordinate system, whose origin is some point on the model, such as the right foot of a soccer player model. Also, the model will typically have a control point or “handle”. To move the model, the 3D renderer only has to move the control point, because model space coordinates of the object remain constant relative to its control point. Additionally, by using that same “handle”, the object can be rotated.

**World Space:** where models are placed in the actual 3D world, in a unified world coordinate system. It turns out that many 3D programs skip past world space and instead go directly to clip or view space. The OpenGL API doesn’t really have a world space.

**View Space (also called Camera Space):** in this space, the view camera is positioned by the application (through the graphics API) at some point in the 3D world coordinate system, if it is being used. The world space coordinate system is then transformed (using matrix math that we’ll explore later), such that the camera (your eye point) is now at the origin of the coordinate system, looking straight down the z-axis into the scene. If world space is bypassed, then the scene is transformed directly into view space, with the camera similarly placed at the origin and looking straight down the z-axis. Whether z values are increasing or decreasing as you move forward away from the camera into the scene is up to the programmer, but for now assume that z values are increasing as you look into the scene down the z-axis. Note that culling, back-face culling, and lighting operations can be done in view space.

在这个空间中，如果正在使用，应用程序（通过图形 API）将视图相机定位在 3D 世界坐标系中的某个点。 然后转换世界空间坐标系（使用我们稍后将探讨的矩阵数学），这样相机（您的眼点）现在位于坐标系的原点，沿 z 轴直视场景。 如果绕过世界空间，则场景将直接转换为视图空间，相机类似地放置在原点并沿 z 轴直视。 当您离开相机进入场景时，z 值是增加还是减少取决于程序员，但现在假设当您沿 z 轴观察场景时，z 值会增加。 请注意，剔除、背面剔除和照明操作可以在视图空间中完成。

The view volume is actually created by a projection, which as the name suggests, “projects the scene” in front of the camera. In this sense, it’s a kind of role reversal in that the camera now becomes a projector, and the scene’s view volume is defined in relation to the camera. Think of the camera as a kind of holographic projector, but instead of projecting a 3D image into air, it instead projects the 3D scene “into” your monitor. The shape of this view volume is either rectangular (called a parallel projection), or pyramidal (called a perspective projection), and this latter volume is called a view frustum (also commonly called frustrum, though frustum is the more current designation).

视图体积实际上是由投影创建的，顾名思义，它在摄像机前“投影场景”。 从这个意义上说，这是一种角色转换，因为相机现在变成了投影仪，场景的视图体积是相对于相机定义的。 将相机视为一种全息投影仪，但它不是将 3D 图像投射到空气中，而是将 3D 场景“投射到”您的显示器中。 此视图体积的形状是矩形（称为平行投影）或金字塔形（称为透视投影），后一种体积称为视锥体（通常也称为截锥体，尽管截锥体是更流行的名称）。

The view volume defines what the camera will see, but just as importantly, it defines what the camera *won’t* see, and in so doing, many objects models and parts of the world can be discarded, sparing both 3D chip cycles and memory bandwidth.

视图体积定义了相机将看到的内容，但同样重要的是，它定义了相机*不会*看到的内容，因此，可以丢弃许多对象模型和世界的一部分，从而节省 3D 芯片周期和 内存带宽。

The frustum actually looks like an pyramid with its top cut off. The top of the inverted pyramid projection is closest to the camera’s viewpoint and radiates outward. The top of the frustum is called the near (or front) clipping plane and the back is called the far (or back) clipping plane. The entire rendered 3D scene must fit between the near and far clipping planes, and also be bounded by the sides and top of the frustum. If triangles of the model (or parts of the world space) falls outside the frustum, they won’t be processed. Similarly, if a triangle is partly inside and partly outside the frustrum the external portion will be clipped off at the frustum boundary, and thus the term clipping. Though the view space frustum has clipping planes, clipping is actually performed when the frustum is transformed to clip space.

截锥体实际上看起来像一个顶部被切掉的金字塔。 倒金字塔投影的顶部最靠近相机的视点并向外辐射。 截锥体的顶部称为近（或前）剪裁平面，背面称为远（或后）剪裁平面。 整个渲染的 3D 场景必须适合近剪裁平面和远剪裁平面之间，并且还以截锥体的侧面和顶部为界。 如果模型的三角形（或世界空间的一部分）落在平截头体之外，它们将不会被处理。 类似地，如果三角形部分在截锥体内部，部分在截锥体外部，则外部部分将在截锥体边界处被剪掉，因此术语剪裁。 尽管视图空间视锥体具有剪裁平面，但剪裁实际上是在视锥体转换为剪裁空间时执行的。

**Clip Space:** Similar to View Space, but the frustum is now “squished” into a unit cube, with the x and y coordinates normalized to a range between -1 and 1,and z is between 0 and 1, which simplifies clipping calculations. The “perspective divide” performs the normalization feat, by dividing all x, y, and z vertex coordinates by a special “w” value, which is a scaling factor that we’ll soon discuss in more detail. The perspective divide makes nearer objects larger, and farther objects smaller as you would expect when viewing a scene in reality.

与 View Space 类似，但现在平截头体被“挤压”成一个单位立方体，x 和 y 坐标标准化为 -1 和 1 之间的范围，z 介于 0 和 1 之间，这简化了裁剪计算。 “透视除法”通过将所有 x、y 和 z 顶点坐标除以一个特殊的“w”值来执行归一化，这是一个我们将很快详细讨论的比例因子。 正如您在查看现实场景时所期望的那样，透视鸿沟使较近的物体变大，而较远的物体变小。

**Screen Space:** where the 3D image is converted into x and y 2D screen coordinates for 2D display. Note that z and w coordinates are still retained by the graphics systems for depth/Z-buffering (see Z-buffering section below) and back-face culling before the final render. Note that the conversion of the scene to pixels, called rasterization, has not yet occurred.

其中 3D 图像被转换为 x 和 y 2D 屏幕坐标以进行 2D 显示。 请注意，图形系统仍保留 z 和 w 坐标，用于深度/Z 缓冲（请参阅下面的 Z 缓冲部分）和最终渲染之前的背面剔除。 请注意，场景到像素的转换（称为光栅化）尚未发生。

Because so many of the conversions involved in transforming through these different spaces essentially are changing the frame of reference, it’s easy to get confused. Part of what makes the 3D pipeline confusing is that there isn’t one “definitive” way to perform all of these operations, since researchers and programmers have discovered different tricks and optimizations that work for them, and because there are often multiple viable ways to solve a given 3D/mathematical problem. But, in general, the space conversion process follows the order we just described.

因为通过这些不同空间进行转换所涉及的许多转换本质上是在改变参考系，所以很容易混淆。 使 3D 管道令人困惑的部分原因是，没有一种“确定的”方式来执行所有这些操作，因为研究人员和程序员已经发现了对他们有用的不同技巧和优化，并且因为通常有多种可行的方法来执行所有这些操作。 解决给定的 3D/数学问题。 但是，一般情况下，空间转换过程遵循我们刚刚描述的顺序。

To get an idea about how these different spaces interact, consider this example:


Take several pieces of Lego, and snap them together to make some object. Think of the individual pieces of Lego as the object’s edges, with vertices existing where the Legos interconnect (while Lego construction does not form triangles, the most popular primitive in 3D modeling, but rather quadrilaterals, our example will still work). Placing the object in front of you, the origin of the model space coordinates could be the lower left near corner of the object, and all other model coordinates would be measured from there. The origin can actually be any part of the model, but the lower left near corner is often used. As you move this object around a room (the 3D world space or view space, depending on the 3D system), the Lego pieces’ positions relative to one another remain constant (model space), although their coordinates change in relation to the room (world or view spaces).

拿几块乐高积木，把它们拼在一起做成一些物体。 将乐高的各个部分视为对象的边缘，顶点存在于乐高互连的地方（虽然乐高构造不形成三角形，这是 3D 建模中最流行的基元，而是四边形，我们的示例仍然有效）。 将物体放在您的面前，模型空间坐标的原点可以是物体的左下角附近，所有其他模型坐标将从那里测量。 原点实际上可以是模型的任何部分，但经常使用左下角附近。 当您在房间（3D 世界空间或视图空间，取决于 3D 系统）周围移动此对象时，乐高积木相对于彼此的位置保持不变（模型空间），尽管它们的坐标相对于房间发生变化（ 世界或视图空间）。

In some sense, 3D chips have become physical incarnations of the pipeline, where data flows “downstream” from stage to stage. It is useful to note that most operations in the application/scene stage and the early geometry stage of the pipeline are done per vertex, whereas culling and clipping is done per triangle, and rendering operations are done per pixel. Computations in various stages of the pipeline can be overlapped, for improved performance. For example, because vertices and pixels are mutually independent of one another in both Direct3D and OpenGL, one triangle can be in the geometry stage while another is in the Rasterization stage. Furthermore, computations on two or more vertices in the Geometry stage and two or more pixels (from the same triangle) in the Rasterzation phase can be performed at the same time.

从某种意义上说，3D 芯片已经成为管道的物理化身，数据从一个阶段“下游”流向另一个阶段。 需要注意的是，应用程序/场景阶段和管道的早期几何阶段的大多数操作都是按顶点完成的，而剔除和裁剪是按三角形完成的，渲染操作是按像素完成的。 流水线各个阶段的计算可以重叠，以提高性能。 例如，因为在 Direct3D 和 OpenGL 中顶点和像素相互独立，所以一个三角形可以处于几何阶段，而另一个处于光栅化阶段。 此外，几何阶段的两个或多个顶点和光栅化阶段的两个或多个像素（来自同一三角形）的计算可以同时执行。


Another advantage of pipelining is that because no data is passed from one vertex to another in the geometry stage or from one pixel to another in the rendering stage, chipmakers have been able to implement multiple pixel pipes and gain considerable performance boosts using parallel processing of these independent entities. It’s also useful to note that the use of pipelining for real-time rendering, though it has many advantages, is not without downsides. For instance, once a triangle is sent down the pipeline, the programmer has pretty much waved goodbye to it. To get status or color/alpha information about that vertex once it’s in the pipe is very expensive in terms of performance, and can cause pipeline stalls, a definite no-no.

流水线的另一个优点是，由于在几何阶段没有数据从一个顶点传递到另一个顶点，或者在渲染阶段从一个像素传递到另一个像素，芯片制造商已经能够实现多个像素管道并使用这些并行处理获得可观的性能提升 独立实体。 值得注意的是，使用流水线进行实时渲染虽然有很多优点，但也并非没有缺点。 例如，一旦一个三角形被发送到管道中，程序员几乎已经向它挥手告别了。 就性能而言，获取该顶点的状态或颜色/alpha 信息在性能方面非常昂贵，并且可能导致管道停顿，这是一个明确的禁忌。