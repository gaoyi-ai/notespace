---
title: Rendering Pipeline Stages–The Deep Dive
categories:
- CG
- Rendering
tags:
- Rendering
date: 2021/7/13
---

> [extremetech-3d-pipeline](https://www.extremetech.com/computing/49076-extremetech-3d-pipeline-tutorial/2)
>
> [3D Pipeline Part II - ExtremeTech](https://www.extremetech.com/computing/49422-3d-pipeline-part-ii)
>
> [3D Pipeline Part III - ExtremeTech](https://www.extremetech.com/computing/49618-3d-pipeline-part-iii)

**1. Application/Scene**

The 3D application itself could be considered the start of the 3D pipeline, though it’s not truly part of the graphics subsystem, but it begins the image generation process that results in the final scene or frame of animation. The application also positions the view camera, which is essentially your “eye” into the 3D world. Objects, both inanimate and animated are first represented in the application using geometric primitives, or basic building blocks. Triangles are the most commonly used primitives. They are simple to utilize because three vertices always describe a plane, whereas polygons with four or more vertices may not reside in the same plane. More sophisticated systems support what are called higher-order surfaces that are different types of curved primitives, which we’ll cover shortly.

3D 应用程序本身可以被认为是 3D 管道的开始，尽管它不是图形子系统的真正组成部分，但它开始了图像生成过程，从而产生最终的场景或动画帧。 该应用程序还会定位视图相机，它本质上是您进入 3D 世界的“眼睛”。 无生命和有动画的对象首先在应用程序中使用几何图元或基本构建块来表示。 三角形是最常用的基元。 它们易于使用，因为三个顶点总是描述一个平面，而具有四个或更多顶点的多边形可能不在同一平面上。 更复杂的系统支持所谓的高阶曲面，它们是不同类型的弯曲基元，我们将在稍后介绍。

3D worlds and the objects in them are created in programs like 3D Studio Max, Maya, AutoDesk 3D Studio, Lightwave, and Softimage to name a few. These programs not only allow 3D artists to build models, but also to animate them. Models are first built using high triangle counts and can then be shaded and textured. Next, depending on the constraints of the rendering engine–off-line or real-time–artists can reduce the triangle counts of these high-detail models to fit within a given performance budget.

3D 世界及其中的对象是在 3D Studio Max、Maya、AutoDesk 3D Studio、Lightwave 和 Softimage 等程序中创建的。 这些程序不仅允许 3D 艺术家构建模型，还可以为它们制作动画。 模型首先使用高三角形数构建，然后可以着色和纹理化。 接下来，根据渲染引擎的限制——离线或实时——艺术家可以减少这些高细节模型的三角形数量，以适应给定的性能预算。

Objects are moved from frame to frame by the application, be it an offline renderer or a game engine. The application traverses the geometry database to gather necessary object information (the geometry database includes all the geometric primitives of the objects), and moves all objects that are going to change in the next frame of animation. Appreciate that in a game engine for instance, the renderer doesn’t have the playground all to itself. The game engine must also tend to AI (artificial intelligence) processing, collision detection and physics, audio, and networking (if the game is being played in multiplayer mode over a network).

对象由应用程序逐帧移动，无论是离线渲染器还是游戏引擎。 应用程序遍历几何数据库以收集必要的对象信息（几何数据库包括对象的所有几何图元），并移动将在下一帧动画中发生变化的所有对象。 欣赏例如在游戏引擎中，渲染器并没有完全属于自己的操场。 游戏引擎还必须倾向于 AI（人工智能）处理、碰撞检测和物理、音频和网络（如果游戏是通过网络以多人游戏模式进行的）。

All models have a default “pose”, and in the case of models of humans, the default pose is called its DaVinci pose, because this pose resembles DaVinci’s famous Vitruvian Man. Once the application has specified the model’s new “pose,” this model is now ready for the next processing step.

所有模型都有一个默认的“姿势”，对于人类模型，默认的姿势被称为它的达芬奇姿势，因为这个姿势类似于达芬奇著名的维特鲁威人。 一旦应用程序指定了模型的新“姿势”，该模型现在就可以进行下一个处理步骤了。

There’s an operation that some applications do at this point, called “occlusion culling”, a visibility test that determines whether an object is partially or completely occluded (covered) by some object in front of it. If it is, the occluded object, or the part of it that is occluded is discarded. The cost savings in terms of calculations that would otherwise need to be performed in the pipeline can be considerable, particularly in a scene with high depth complexity, meaning that objects toward the back of the scene have several “layers” of objects in front of them, occluding them from the view camera.

某些应用程序此时会执行一项操作，称为“遮挡剔除”，这是一种可见性测试，用于确定对象是否被其前面的某个对象部分或完全遮挡（覆盖）。 如果是，则被遮挡的对象或其被遮挡的部分将被丢弃。 否则需要在管道中执行的计算方面的成本节省是相当可观的，特别是在具有高深度复杂性的场景中，这意味着朝向场景后面的对象在它们前面有几个“层”对象 ，从视图相机中遮挡它们。

If these occluded objects can be discarded early, they won’t have to be carried any further into the pipeline, which saves unnecessary lighting, shading and texturing calculations. For example, if you’re in a game where it’s you versus Godzilla, and the big guy is lurking behind a building you’re walking toward, you can’t see him (sneaky devil). The game engine doesn’t have to worry about drawing the Godzilla model, since the building’s model is in front of him, and this can spare the hardware from having to render Godzilla in that frame of animation.

如果可以及早丢弃这些被遮挡的对象，它们就不必进一步进入管道，从而节省了不必要的照明、着色和纹理计算。 例如，如果你在一场你与哥斯拉的比赛中，而大家伙潜伏在你正走向的建筑物后面，你就看不到他（鬼鬼祟祟的恶魔）。 游戏引擎不必担心绘制哥斯拉模型，因为建筑物的模型在他面前，这可以使硬件不必在该动画帧中渲染哥斯拉。

A more important step is a simple visibility check on each object. This can be accomplished by determining if the object is in the view frustum (completely or partially). Some engines also try to determine whether an object in the view frustum is completely occluded by another object. This is typically done using simple concepts like portals or visibility sets, especially for indoor worlds. These are two similar techniques that get implemented in 3D game engines as a way to not have to draw parts of the 3D world that the camera won’t be able to see. [Eberly, p. 413] The original Quake used what were called potentially visible sets (PVS) that divided the world into smaller pieces. Essentially, if the game player was in a particular piece of the world, other areas would not be visible, and the game engine wouldn’t have to process data for those parts of the world.

更重要的一步是对每个对象进行简单的可见性检查。 这可以通过确定对象是否在视锥体中（完全或部分）来实现。 一些引擎还尝试确定视锥体中的对象是否完全被另一个对象遮挡。 这通常使用简单的概念（例如门户或可见性集）来完成，尤其是对于室内世界。 这是在 3D 游戏引擎中实现的两种类似技术，作为一种不必绘制相机无法看到的 3D 世界部分的方法。 [Eberly, p. 413] 最初的 Quake 使用了所谓的潜在可见集 (PVS)，将世界分成了更小的部分。 从本质上讲，如果游戏玩家在世界的某个特定区域，其他区域将不可见，游戏引擎就不必处理世界这些区域的数据。


Another workload-reduction trick that’s a favorite among programmers is the use of bounding boxes. Say for instance you’ve got a 10,000-triangle model of a killer rabbit, and rather than test each of the rabbit model’s triangles, a programmer can encase the model in a bounding box, consisting of 12 triangles (two for each side of the six-sided box). They can then test culling conditions (based on the bounding box vertices instead of the rabbit’s vertices) to see if the killer rabbit will be visible in the scene. Even before you might further reduce the number of vertices by designating those in the killer rabbit model that are shared (vertices of adjacent triangles can be shared, a concept we’ll explore in more detail later), you’ve already reduced your total vertex count from 30,000 (killer rabbit) to 36 (bounding box) for this test. If the test indicates the bounding box is not visible in the scene, the killer rabbit model can be trivially rejected, you’ve just saved yourself a bunch of work.

程序员最喜欢的另一个减少工作量的技巧是使用边界框。例如，假设您有一个杀手兔子的 10,000 个三角形模型，而不是测试兔子模型的每个三角形，程序员可以将模型包裹在一个由 12 个三角形组成的边界框中（每个三角形的每边两个六面盒）。然后他们可以测试剔除条件（基于边界框顶点而不是兔子的顶点）以查看杀手兔子是否在场景中可见。甚至在您可以通过指定共享的杀手兔模型中的顶点来进一步减少顶点数量之前（相邻三角形的顶点可以共享，我们将在后面更详细地探讨这个概念），您已经减少了总顶点数对于此测试，从 30,000（杀手兔）到 36（边界框）计数。如果测试表明边界框在场景中不可见，杀手兔模型可能会被轻易拒绝，您只是为自己节省了大量工作。

Another method for avoiding excessive work is what’s called object Level of Detail, referred to as LOD. This technique is lossy, though given how it’s typically used, the loss of model detail is often imperceptible. Object models are built using several discrete LOD levels. A good example is a jet fighter with a maximum LOD model using 10,000 triangles, and additional lower resolution LOD levels consisting of 5,000, 2,500, 1000 and 500 triangles. The jet’s distance to the view camera will dictate which LOD level gets used. If it’s very near, the highest resolution LOD gets used, but if it’s just barely visible and far from the view camera, the lowest resolution LOD model would be used, and for locations between the two, the other LOD levels would be used.

另一种避免过度工作的方法是所谓的对象细节级别，简称 LOD。 这种技术是有损的，尽管考虑到它的典型使用方式，模型细节的损失通常是察觉不到的。 对象模型是使用多个离散的 LOD 级别构建的。 一个很好的例子是喷气式战斗机，其最大 LOD 模型使用 10,000 个三角形，以及由 5,000、2,500、1000 和 500 个三角形组成的额外低分辨率 LOD 级别。 喷气机到视图相机的距离将决定使用哪个 LOD 级别。 如果它非常接近，则使用最高分辨率的 LOD，但如果它几乎不可见且远离视图相机，则将使用分辨率最低的 LOD 模型，而对于两者之间的位置，将使用其他 LOD 级别。

LOD selection is always done by the application before it passes the object onto the pipeline for further processing. To determine which LOD to use, the application maps a simplified version of the object (often just the center point) to view space to determine the distance to the object. This operation occurs independently of the pipeline. The LOD must be known in order to determine which set of triangles (different LOD levels) to send to the pipeline..

LOD 选择始终由应用程序在将对象传递到管道进行进一步处理之前完成。 为了确定使用哪个 LOD，应用程序将对象的简化版本（通常只是中心点）映射到视图空间，以确定到对象的距离。 此操作独立于管道发生。 必须知道 LOD 才能确定将哪组三角形（不同的 LOD 级别）发送到管道。

**Geometric Parlor Tricks**

Generally speaking, a higher triangle count will produce a more realistic looking model. Information about these triangles–their location in 3D space, color, etc.–is stored in the descriptions of the vertices of each triangle. The aggregation of these vertices in the 3D world is referred to as a scene database, which is the very same animal as the geometry database mentioned above. Curved areas of models, like tires on a car, require many triangles to approximate a smooth curve. The adverse effect of aggressively curtailing the number of vertices/triangles in a circle, for example, via an LOD reduction would be a “bumpy” circle, where you could see the vertices of each component triangle. If many more triangles represented the circle, it would look far smoother at its edge. Optimizations can be made to reduce the actual number of vertices sent down the pipeline without compromising the quality of the model, because connected triangles share vertices. Programmers can use connected triangle patterns called triangle strips and fans to reduce vertex count. For example:

一般来说，更高的三角形数会产生更逼真的模型。关于这些三角形的信息——它们在 3D 空间中的位置、颜色等——存储在每个三角形顶点的描述中。这些顶点在 3D 世界中的聚合被称为场景数据库，它与上面提到的几何数据库是完全相同的动物。模型的弯曲区域，如汽车轮胎，需要许多三角形来近似平滑曲线。积极减少圆中顶点/三角形数量的不利影响（例如，通过 LOD 减少）将是一个“凹凸不平”的圆，您可以在其中看到每个组件三角形的顶点。如果有更多的三角形代表这个圆，它的边缘看起来会平滑得多。可以进行优化以减少沿管道发送的实际顶点数量，而不会影响模型的质量，因为连接的三角形共享顶点。程序员可以使用称为三角形条带和扇形的连接三角形模式来减少顶点数。例如：

In the case of a strip of triangles, the simplest example would be a rectangle described by two right triangles, with a shared hypotenuse. Normally, two such triangles drawn separately would yield six vertices. But, with the two right triangles being connected, they form a simple triangle strip that can be described using four vertices, reducing the average number of vertices per triangle to two, rather than the original three. While this may not seem like much of reduction, the advantage grows as triangle (and resulting vertex) counts scale, and the average number of unique vertices per triangle moves toward one. [RTR, p. 234] Here’s the formula for calculating the average number of vertices, given m triangles:

在三角形条带的情况下，最简单的例子是由两个直角三角形描述的矩形，共享斜边。 通常，单独绘制的两个这样的三角形会产生六个顶点。 但是，通过连接两个直角三角形，它们形成了一个简单的三角形带，可以使用四个顶点来描述，从而将每个三角形的平均顶点数减少到两个，而不是原来的三个。 虽然这看起来不像是减少很多，但优势随着三角形（和生成的顶点）计数的规模而增长，并且每个三角形的平均唯一顶点数向 1 移动。 [RTR, p. 234] 这是计算平均顶点数的公式，给定 m 个三角形：`1 + 2/m`

So, in a strip with 100 triangles, the average number of vertices per triangle would be 1.02, or about 102 vertices total, which is a considerable savings compared to processing the 300 vertices of the individual triangles. In this example, we hit the maximum cost savings obtainable from the use of strips for m number of triangles, which is m+2 vertices [RTR, p. 239]. These savings can really add up when you consider that it takes 32 bytes of data to describe the attributes (such as position, color, alpha, etc.) of a single vertex in Direct3D. Of course, the entire scene won’t consist of strips and fans, but developers do look to use them where they can because of the associated cost savings.

因此，在具有 100 个三角形的条带中，每个三角形的平均顶点数为 1.02，即总共约 102 个顶点，与处理单个三角形的 300 个顶点相比，这是相当可观的节省。 在这个例子中，我们达到了通过对 m 个三角形使用条带获得的最大成本节约，即 m+2 个顶点 [RTR, p. 239]。 当您考虑到在 Direct3D 中描述单个顶点的属性（例如位置、颜色、alpha 等）需要 32 字节的数据时，这些节省真的可以加起来。 当然，整个场景不会由条带和风扇组成，但由于相关的成本节约，开发人员确实希望尽可能使用它们。

In the case of fans, a programmer might describe a semicircle using 20 triangles in a pie-slice arrangement. Normally this would consist of 60 vertices, but by describing this as a fan, the vertex count is reduced to 22. The first triangle would consist of three vertices, but each additional triangle would need only one additional vertex, and the center of the fan has a single vertex shared by all triangles. Again the maximum savings possible using strips/fans is achieved.

在风扇的情况下，程序员可能会使用 20 个三角形呈饼状排列来描述一个半圆。 通常这将由 60 个顶点组成，但通过将其描述为扇形，顶点数减少到 22。第一个三角形将由三个顶点组成，但每个额外的三角形只需要一个额外的顶点，扇形的中心 有一个由所有三角形共享的顶点。 再次实现了使用条带/风扇的最大节约。

Another important advantage of strips and fans, is that they are a “non-lossy” type of data reduction, meaning no information or image quality is thrown away in order to get the data reduction and resulting speedup. Additionally, triangles presented to the hardware in strip or fan order improve vertex cache efficiency, which can boost geometry processing performance. Another tool available to programmers is the indexed triangle list, which can represent a large number of triangles, m, with m/2 vertices, about twice the reduction of using strips or fans. This representational method is preferred by most hardware architectures.

条带和扇形的另一个重要优点是它们是一种“无损”类型的数据缩减，这意味着不会为了数据缩减和由此产生的加速而丢弃任何信息或图像质量。 此外，以条形或扇形顺序呈现给硬件的三角形可提高顶点缓存效率，从而提高几何处理性能。 程序员可用的另一个工具是索引三角形列表，它可以表示大量的三角形，m，具有 m/2 个顶点，大约是使用条带或扇形减少的两倍。 大多数硬件架构都喜欢这种表示方法。

Rather than use numerous triangles to express a curved surface, 3D artists and programmers have another tool at their disposal: higher-order surfaces. These are curved primitives that have more complex mathematical descriptions, but in some cases, this added complexity is still cheaper than describing an object with a multitude of triangles. These primitives have some pretty odd sounding names: parametric polynomials (called SPLINEs), non-uniform rational b-splines (NURBs), Beziers, parametric bicubic surfaces and n-patches. Because 3D hardware best understands triangles, these curved surfaces defined at the application level are tessellated, or converted to triangles by the API runtime, the graphics card driver or the hardware for further handling through the 3D pipeline. Improved performance is possible if the hardware tessellates the surface after it has been sent from the CPU to the 3D card for transform and lighting (T&L) processing, placing less of a load on the AGP port, a potential bottleneck.

3D 艺术家和程序员无需使用大量三角形来表达曲面，而是可以使用另一种工具：高阶曲面。这些曲线图元具有更复杂的数学描述，但在某些情况下，这种增加的复杂性仍然比描述具有多个三角形的对象便宜。这些原语有一些听起来很奇怪的名字：参数多项式（称为 SPLINE）、非均匀有理 b 样条 (NURB)、贝塞尔曲线、参数双三次曲面和 n 面片。因为 3D 硬件最能理解三角形，这些在应用程序级别定义的曲面被细分，或由 API 运行时、显卡驱动程序或硬件转换为三角形，以便通过 3D 管道进一步处理。如果在将表面从 CPU 发送到 3D 卡进行变换和照明 (T&L) 处理后，硬件将表面细分，从而减少 AGP 端口上的负载（潜在的瓶颈），则可以提高性能。

**2. Geometry**

**Make Your Move: The Four Primary Transforms**

Objects get moved from frame to frame to create the illusion of movement, and in a 3D world, objects can be moved or manipulated using four operations broadly referred to as transforms. The transforms are actually performed on object vertices using different types of “transform matrices” via matrix mathematics. All of these transform operations are affine, meaning that they occur in an affine space, which is a mathematical space that includes points and vectors. An affine transformation preserves parallelism of lines, though distance between points can change. (see illustration below, which shows a square being changed into a parallelogram with two sides shorter than the others, and parallelism is preserved, but the angles in the object have changed). These transforms are used when moving objects within a particular coordinate system or space, or when changing between spaces.

对象逐帧移动以创建运动错觉，并且在 3D 世界中，可以使用四种广泛称为变换的操作来移动或操纵对象。 变换实际上是通过矩阵数学使用不同类型的“变换矩阵”在对象顶点上执行的。 所有这些变换操作都是仿射的，这意味着它们发生在仿射空间中，这是一个包含点和向量的数学空间。 仿射变换保留了线的平行度，但点之间的距离可能会发生变化。 （一个正方形变成了两条边比其他边短的平行四边形，并且平行度保持不变，但对象中的角度已更改）。 当在特定坐标系或空间内移动对象时，或在空间之间更改时，将使用这些变换。

**Translation:** the movement of an object along any of the three axes to move that object to another location. Math operation: normally would be addition or subtraction (adding a negative number), but for efficiency purposes, transforms are done such that this operation winds up being matrix multiplication, like rotation and scaling operations.

对象沿三个轴中的任何一个轴移动以将该对象移动到另一个位置。 数学运算：通常是加法或减法（加一个负数），但为了效率的目的，转换是这样完成的，这个运算最终是矩阵乘法，如旋转和缩放操作。

**Rotation:** as the name suggests, an object can be rotated about an arbitrary axis. Math operation: in the simplest case where the object to be rotated is already positioned at the origin of the coordinate system, the multiplication of each coordinate by either the sine or cosine of θ (theta), which is the number of degrees by which the object is being rotated, produces the new post-rotation coordinates for a vertex. If an object’s movement requires it to be rotated around more than one axis (x, y, or z) simultaneously, the ordering of rotation calculations is important, as different ordering can produce different visual results. Rotation around an arbitrary axis requires some extra work. It may first require a transform to move the object to the origin, then some rotations to align the arbitrary axis of rotation with the z-axis. Next the desired rotation is performed, then the alignment rotations must be undone, and the object must be translated back to its original location.

顾名思义，对象可以绕任意轴旋转。 数学运算：在要旋转的对象已经位于坐标系原点的最简单情况下，每个坐标乘以 θ (theta) 的正弦或余弦，即 对象正在旋转，为顶点生成新的旋转后坐标。 如果对象的运动需要它同时围绕多个轴（x、y 或 z）旋转，则旋转计算的顺序很重要，因为不同的顺序会产生不同的视觉效果。 绕任意轴旋转需要一些额外的工作。 它可能首先需要变换以将对象移动到原点，然后进行一些旋转以将任意旋转轴与 z 轴对齐。 接下来执行所需的旋转，然后必须取消对齐旋转，并且必须将对象平移回其原始位置。

**Scaling:** the resizing of a model, used in creating the effect of depth of field during a perspective projection (a kind of transform that we’ll soon cover in more detail ). Math operation: multiply by a scaling factor, say 2 for all three axes, which would double the size of the object. Scaling can be uniform, where all three axes are scaled equally, or each vertex can be scaled by a different amount. Negative scale factors can produce a mirror image reflection of the object.

模型的调整大小，用于在透视投影期间创建景深效果（一种我们将很快详细介绍的变换）。 数学运算：乘以一个比例因子，对所有三个轴都乘以 2，这将使对象的大小加倍。 缩放可以是统一的，其中所有三个轴都按等比例缩放，或者每个顶点可以按不同的量缩放。 负比例因子可以产生对象的镜像反射。

**Skewing:** (also called Shearing) the changing of the shape of a model, by manipulating it along one or more axes. An example might be a rendered scene of a cube of Jell-O on a plate sitting on a table, where the plate is turned up in the air 45° on its side, while the other edge remains on the table, and the cubic piece of Jell-O will now become rhomboid as a result of the pull of gravity. The top part of the model of the Jell-O cube would be skewed toward the ground of the 3D world. Math operation: adding a function of one coordinate to another, for example, add the x value to the y value and leave the x value alone.

**倾斜：**（也称为剪切）通过沿一个或多个轴操作模型来改变模型的形状。 一个例子可能是一个放置在桌子上的盘子上的果冻立方体的渲染场景，盘子在空中翻转 45°，而另一边留在桌子上，立方体 由于重力的作用，果冻现在将变成菱形。 果冻立方体模型的顶部将向 3D 世界的地面倾斜。 数学运算：将一个坐标的函数与另一个坐标相加，例如将x值与y值相加，而将x值单独保留。

As mentioned, matrix math figures large in doing 3D transform calculations. Matrices provide simple representation of transform terms, and 3D matrix math is typically comprised of straightforward multiplies or additions.

Transform processing efficiency comes from the fact that multiple matrix operations can be concatenated together into a single matrix and applied against the vertex as a single matrix operation. This spreads the matrix operation setup costs over the entire scene. One would think that given the three axes used in the various spaces utilizing the Cartestian coordinate system that this matrix should be 3×3. Instead it uses “homogenous coordinates” and is 4×4 in size. Homogenous coordinates permit the certain transforms that would otherwise require addition, such as a simple translation, to be handled as multiply operations. Complex concatenation of multiple different transforms using multiplies is also made possible with homogenous coordinates. The 4th term is actually the scaling factor “w”. It is initially set to 1, and is used to compute a depth value for perspective projection in clip space as previously mentioned, and perspective-correct interpolation of pixel values across the triangle in the Rasterization stage.

变换处理效率来自这样一个事实，即多个矩阵运算可以连接在一起成为单个矩阵，并作为单个矩阵运算应用于顶点。这将矩阵操作设置成本分散到整个场景中。人们会认为，鉴于在使用笛卡尔坐标系的各种空间中使用的三个轴，该矩阵应该是 3×3。相反，它使用“齐次坐标”并且大小为 4×4。齐次坐标允许将需要加法的某些变换（例如简单的平移）作为乘法运算来处理。使用齐次坐标也可以实现使用乘法的多个不同变换的复杂串联。第 4 项实际上是比例因子“w”。它最初设置为 1，用于计算前面提到的剪辑空间中透视投影的深度值，以及在光栅化阶段对整个三角形的像素值进行透视校正插值。

**Space to Space**

Upon entry into the geometry section, we will likely translate objects from model to world space, and from world to view space, or directly to view space from model space in some cases, as we mentioned previously. When converting from one coordinate space to another, many of the basic transforms may be used. Some might be as simple as an inverse translation, or be more complex, such as involving the combination of two translations and a rotation. For example, transforming from world space to view space typically involves a translation and a rotation. After the transform to view space, many interesting things begin to happen.

进入几何部分后，我们可能会将对象从模型转换到世界空间，从世界转换到视图空间，或者在某些情况下直接从模型空间转换到视图空间，如前所述。 当从一个坐标空间转换到另一个坐标空间时，可以使用许多基本变换。 有些可能像逆向平移一样简单，或者更复杂，例如涉及两个平移和旋转的组合。 例如，从世界空间转换到视图空间通常涉及平移和旋转。 转换到视图空间后，许多有趣的事情开始发生。