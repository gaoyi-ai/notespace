---
title: A Gentle Introduction to Computer Graphics Programming
categories:
- CG
- Rendering
tags:
- Rendering
date: 2021/7/13
---



> [www.scratchapixel.com](https://www.scratchapixel.com/lessons/3d-basic-rendering/get-started) 

A Gentle Introduction to Computer Graphics Programming
------------------------------------------------------

You want to learn computer graphics. First do you know what it is? In the second lesson of this section, you can find a definition of computer graphics, and also learn about how it generally works. Maybe you have heard about terms such as modelling, geometry, animation, 3D, 2D, digital images, 3D viewport, real-time rendering, compositing but you are unsure about what they mean and more importantly, how they relate to each other. The second lesson of this section will answer these questions. From there, you should know little about CG programming, but have a general understanding of CG and the different tools and processes involved in the making of CGI.

What's next? Our world is fundamentally **three-dimensional**. At least as far as we can experience it with our senses. To which some people like to add the dimension of **time**. Time plays an important role in CGI, but we will come back to this later on. Objects from the real world then are three-dimensional. That's a fact we think we can all agree on without having to get into proving it. However, what's interesting, is that **vision**, one of the senses by which this three-dimensional world can be experienced, is principally a two-dimensional process. We could maybe say that the image created in our mind is dimensionless (we don't understand yet very well how images'appear' in our brain), but when we speak of an image, it generally means to us a flat surface, on which the dimensionality of objects has been reduced from three to two dimensions (the surface of the canvas or the surface of the screen). The only reason why this image on the canvas actually looks accurate to our brain, is because objects gets smaller as they get further away from where you stand, an effect called **foreshortening**. If you are not convinced yet, think of an image as nothing more than a mirror reflection. The surface of the mirror is perfectly flat, and yet, we can't make the difference between looking at the image of a scene reflected from a mirror and looking directly at the scene: you don't perceive the reflection, just the object. It's only because we have two eyes that we can actually get a sense of seeing things in 3D, something we call **stereoscopic vision**. Each eye looks at the same scene from a slightly different angle, and the brain can use these two images of the same scene to approximate the distance and the position of objects in 3D space with respect to each other. However stereoscopic vision is quite limited in a way as we can't measure the distance to objects or their size very accurately (which computers can do). Human vision is quite sophisticated and an impressive result of evolution, but it's nonetheless a trick, and it can be fooled easily (many magicians tricks are based on this). To some extent, computer graphics is a mean by which we can create images of artificial worlds and present them to the brain (through the mean of vision), as an experience of reality (something we call photo-realism), exactly like a mirror reflection. This theme is quite common in science fiction, but technology is not far from making this actually possible.

下一步是什么？我们的世界基本上是**三维**。至少我们可以用我们的感官来体验它。有些人喜欢在其中加上**时间**的维度。时间在 CGI 中扮演着重要的角色，但我们稍后会回到这一点。来自现实世界的物体是三维的。这是一个事实，我们认为我们都可以达成一致，而无需进行证明。然而，有趣的是，**视觉**是可以体验这个三维世界的一种感官，主要是一个二维过程。我们也许可以说在我们脑海中创造的图像是无量纲的（我们还不太清楚图像是如何在我们的大脑中“出现”的），但是当我们说到图像时，它通常对我们来说意味着一个平面，在对象的维度已经从三个维度减少到两个维度（画布表面或屏幕表面）。画布上的这个图像实际上在我们的大脑中看起来准确的唯一原因是因为物体离你站的地方越远就越小，这种效果称为**透视**。如果您还不相信，可以将图像视为镜面反射。镜子的表面是完全平坦的，然而，我们无法区分从镜子反射的场景图像和直接看场景：你感知不到反射，只能感知物体。只是因为我们有两只眼睛，我们才能真正感受到 3D 中的事物，我们称之为**立体视觉**。每只眼睛从稍微不同的角度看同一个场景，大脑可以使用同一场景的这两个图像来近似估计 3D 空间中物体相对于彼此的距离和位置。然而，立体视觉在某种程度上是非常有限的，因为我们无法非常准确地测量到物体的距离或它们的大小（计算机可以做到）。人类的视觉非常复杂，是进化的一个令人印象深刻的结果，但它仍然是一个技巧，很容易被愚弄（许多魔术师的技巧都是基于此）。在某种程度上，计算机图形是一种手段，通过它我们可以创建人造世界的图像并将它们呈现给大脑（通过视觉），作为对现实的体验（我们称之为照片写实），就像一面镜子反射。这个主题在科幻小说中很常见，但技术离使这成为可能并不遥远。

> It's actually important to say here that while we may seem more focused on the process of generating these images, a process we call rendering, computer graphics is not only about making images, but also about developing techniques to simulate things such as the motion of fluids, the motion of soft and rigid bodies, finding ways of animating objects and avatars such that their motion and every effects resulting from that motion is accurately simulated (for example when you walk the shape of your muscles changes and the overall outside shape of your body is a result of these muscles deformation ? to create a realistic avatar you need to find ways of simulating these effects. We will also learn about these techniques on Scratchapixel.

What have we learned so far? That the world is three-dimensional, that the way we look at it is two-dimensional, and that if you can replicate the shape and the appearance of objects, the brain can not make the difference between looking at this objects directly, and looking at an image of these objects. Computer graphics is not limited to creating photoreal images but while it's easier to create non photo-realistic images than creating perfectly photo realistic ones, the goal of computer graphics is clearly realism (as much in the way things move than they appear).

All we need to do now, is learn what the rules for making such a photo-real image are, and that's what you will also learn here on Scratchapixel.

Describing Objects Making Up the Virtual World
----------------------------------------------

The difference between the painter who is actually painting a real scene (unless the subject of the painting comes from his/her imagination), and us, trying to create an image with a computer, is that we actually have to first somehow describe the shape (and the appearance) of objects making up the scene we want to render an image of to the computer.

实际绘制真实场景的画家（除非绘画的主题来自他/她的想象）与我们尝试用计算机创建图像之间的区别在于，我们实际上必须先以某种方式描述形状 （和外观）构成我们想要向计算机渲染图像的场景的对象。

![](https://www.scratchapixel.com/images/upload/rendering-3d-scene-overview/coordinate-system.png?)

Figure 1: a 2D Cartesian coordinative systems defined by its two axis (x and y) and the origin. This coordinate system can be used as a reference to define the position or coordinates of points within the plane.

![](https://www.scratchapixel.com/images/upload/rendering-3d-scene-overview/box1.png?)

Figure 2: a box can be described by specifying the coordinates of its eight corners in a Cartesian coordinate system.

One of the simplest and most important concept we learn at school is the idea of space in which points can be defined. The position of a point is generally defined with relation to an **origin**. On a ruler, this is generally the tick marked with the number zero. If we use two rulers, one perpendicular to the other, we can define the position of points in two dimensions. Add a third ruler, perpendicular to the first two, and you can define the position of points in three dimensions. The actual numbers representing the position of the point with respect to one of the tree rulers are called the points **coordinates**. We are all familiar with the concept of coordinates to mark were we are with respect to some reference point or line (for example the Greenwich meridian). We can now define points in three dimensions. Let's imagine that you just bought a computer. This computer probably came in a box, and this box has eight corners (sorry for stating the obvious). One way of describing this box, is to measure the distance of these 8 corners with respect to one of the corners. This corner acts as the origin of our **coordinate system** and obviously the distance of this reference corner with respect to itself will be 0 in all dimensions. However the distance from the reference corner to the other seven corners, will be different than 0. Let's image that our box has the following dimensions:

```
corner 1: (0, 0, 0)  
corner 2: (12, 0, 0)  
corner 3: (12, 8, 0)  
corner 4: (0, 8, 0)  
corner 5: (0, 0, 10)  
corner 6: (12, 0, 10)  
corner 7: (12, 8, 10)  
corner 8: (0, 8, 10)  
```

![](https://www.scratchapixel.com/images/upload/rendering-3d-scene-overview/box2.png?)

Figure 3: a box can be described by specifying the coordinates of its eight corners in a Cartesian coordinate system.

The first number represent the width, the second number the height, and the third number the corner's depth. Corner 1 as you can see, is the origin from which all the over corners have been measured. All you need to do from here, is somehow write a program in which you will define the concept of a three-dimensional point, and use it to store the coordinates of the eight points you just measured. In C/C++, such a program could look like this:

```cpp
typedef float Point[3]; 
int main() 
{ 
    Point corners[8] = { 
        { 0, 0,  0}, 
        {12, 0,  0}, 
        {12, 8,  0}, 
        { 0, 8,  0}, 
        { 0, 0, 10}, 
        {12, 0, 10}, 
        {12, 8, 10}, 
        { 0, 8, 10}, 
    }; 
 
    return 0; 
} 
```

Like in any language, there is always different ways of doing the same thing. This program shows one possible way in C/C++ for defining the concept of point (line 1) and storing the box corners in memory (in this example as an array of eight points)

You have somehow created your first 3D program. It doesn't produce an image yet, but you can already store the description of a 3D object in memory. In CG, the collection of these objects is called a **scene** (a scene also includes the concept of camera and lights but we will talk about this another time). As suggested before, we are lacking two very important things to make the process really complete and interesting. First to actually represent the box in the memory of the computer, ideally, we also need a system that defines how these eight points are connected to each other to make up the faces of the box. In CG, this is called the **topology** of the object (an object is also called a **model**). We will talk about this in the Geometry section and the 3D Basic Render section (in the lesson on rendering triangles and polygonal meshes). Topology refers to how points which we generally call **vertices** are connected to each other to form faces (or flat surfaces). These faces are also called **polygons**. The box would be made of six faces or six polygons and the set of polygons forms what we call a **polygonal mesh** or simply a **mesh**. The second thing we are missing, is a system to create an image of that box. This requires to actually project the corners of the box onto an imaginary canvas, a process we call **perspective projection**.

您以某种方式创建了您的第一个 3D 程序。它尚未生成图像，但您已经可以将 3D 对象的描述存储在内存中。在 CG 中，这些对象的集合称为 **场景**（场景还包括相机和灯光的概念，但我们将在另一个时间讨论）。正如之前所建议的，我们缺少两个非常重要的东西来使这个过程真正完整和有趣。首先要在计算机的内存中实际表示盒子，理想情况下，我们还需要一个系统来定义这八个点如何相互连接以组成盒子的面。在CG中，这称为对象的**拓扑**（对象也称为**模型**）。我们将在几何部分和 3D 基本渲染部分（在渲染三角形和多边形网格的课程中）讨论这一点。拓扑是指我们通常称为**顶点**的点如何相互连接以形成面（或平面）。这些面也称为**多边形**。盒子将由六个面或六个多边形组成，多边形组形成我们所说的**多边形网格**或简单的**网格**。我们缺少的第二件事是创建该框图像的系统。这需要将盒子的角实际投影到想象的画布上，我们称之为**透视投影**的过程。

Creating an Image of this Virtual World
---------------------------------------

![](https://www.scratchapixel.com/images/upload/rendering-3d-scene-overview/frustum4.png?)

Figure 4: if you connect the corners of the canvas to the eye which by default is aligned with our Cartesian coordinate system, and extend the lines further away into the scene, you get some sort of pyramid which we call a viewing frustum. Any object contained within the frustum (or overlapping it) is visible and will show up on the image.

如果您将画布的角连接到默认情况下与我们的笛卡尔坐标系对齐的眼睛，并将线条进一步延伸到场景中，您会得到某种我们称之为视锥体的金字塔。 包含在视锥体内（或与其重叠）的任何对象都是可见的，并将显示在图像上。

The process of projecting 3D point of the surface of the canvas, actually involves a special matrix called the perspective matrix (don't worry if you don't know what a matrix is). Using this matrix to project point is not absolutely necessary but makes things much easier. However, you don't really need mathematics and matrices to figure out how it works. You can see an image, or a canvas as some sort of flat surface placed some distance away from the eye. Trace four lines all starting from the eye to each one of the four corners of the canvas and extend these lines further away into the world (as far as you can see). You get a pyramid which we call a **viewing frustum** (and not frustrum). The viewing frustum defines some sort of volume in 3D space and the canvas itself it just a plane cutting of this volume perpendicular to the eye line of sight. Place your box in front of the canvas. Next, trace a line from each corner of the box to the eye and mark a dot where the line intersects the canvas. Find out on the canvas, the dots corresponding to each of the twelve edges of the box, and trace a line between these dots. What do you see? An image of the box.

在画布表面投影3D点的过程，实际上涉及到一个特殊的矩阵，叫做透视矩阵（如果你不知道矩阵是什么也不要担心）。使用这个矩阵来投影点并不是绝对必要的，但会使事情变得更容易。但是，您实际上并不需要数学和矩阵来弄清楚它是如何工作的。您可以将图像或画布视为某种平面放置在离眼睛一定距离的地方。从眼睛到画布四个角中的每一个都跟踪四条线，并将这些线延伸到世界更远的地方（尽可能远）。你会得到一个金字塔，我们称之为**视锥体**（而不是截头体）。视锥体定义了 3D 空间中的某种体积，而画布本身只是该体积垂直于视线的平面切割。把你的盒子放在画布前面。接下来，从盒子的每个角到眼睛画一条线，并在该线与画布相交的地方标记一个点。在画布上找出对应于盒子十二边中每条边的点，并在这些点之间画一条线。你看到了什么？盒子的图像。

![](https://www.scratchapixel.com/images/upload/rendering-3d-scene-overview/box-setup1.png?)

Figure 5: the box is move in front of our camera setup. The coordinates of the box corners are expressed with respect to this Cartesian coordinate system.

![](https://www.scratchapixel.com/images/upload/rendering-3d-scene-overview/box-setup2.png?)

Figure 6: connecting the box corners to the eye.

![](https://www.scratchapixel.com/images/upload/rendering-3d-scene-overview/box-setup3.png?)

Figure 7: the intersection points between these lines and the canvas are the projection of the box corners onto the canvas. By connecting these points to each other, an wireframe image of the box is created.

The three rulers used to measure the coordinates of the box corner form what we call a coordinate system. It's a system in which points can be measured to. All points' coordinates relate to this coordinate system. Note that a coordinate can either be positive or negative (or zero) depending on whether it's located on the right or the left of the ruler's origin (the value 0). In CG, this coordinate system is often called the **world** coordinate system, and the point (0,0,0), the **origin**.

Let's move the apex of the viewing frustum at the origin and orient the line of sight (the view direction) along the negative z-axis (figure 3). Many graphics applications use this configuration as their default"viewing system". Keep in mind that the top of the pyramid is actually the point from which we will be looking at the scene. Let's also move the canvas one unit away from the origin. Finally, let's move the box some distance away from the origin, so that it is fully contained within the volume of the frustum. Because the box is in a new position (we moved it), the coordinates of its eight corners changed and we need to measure them again. Note that because the box is on left side of the ruler's origin from which we measure the object's depth, all depth coordinates which also called **z-coordinates** will be negative. Four of the corners are also below the reference point used to measure the object's height, and will have a negative height or y-coordinate. Finally, four of the corners will be to the left of the ruler's origin measuring the object's width: their width, or x-coordinates will also be negative. The new coordinates of the box's corners are:

```
corner 1: ( 1, -1, -5) 
corner 2: ( 1, -1, -3) 
corner 3: ( 1,  1, -5) 
corner 4: ( 1,  1, -3) 
corner 5: (-1, -1, -5) 
corner 6: (-1, -1, -3) 
corner 7: (-1,  1, -5) 
corner 8: (-1,  1, -3) 
```

![](https://www.scratchapixel.com/images/upload/rendering-3d-scene-overview/box-setup4.png?)

Figure 8: the coordinates of the point P', the projection of P on the canvas can be computed using simple geometry. The rectangle ABC and AB'C' are said to be similar.

Let's look at our setup from the side and trace a line from one of the corners to the origin (the viewpoint). We can define two triangles: ABC and AB'C'. As you can see these two triangles have the same origin (A). They are also somehow copies of each other, in the sense that the angle defined by the edges AB and AC is the same as the angle defined by the edge AB', AC'. Such triangles are said to be **similar**. Similar triangles have an interesting property: the ratio between their adjacent and opposite sides is the same. In other words:

$${ BC \over AB } = { B'C' \over AB' }.$$

Because the canvas is 1 unit away from the origin, we know that AB' equals 1. We also know the position of B and C which are the z (depth) and y coordinates (height) respectively of the corner. If we substitute these numbers in the above equation, we get:

$${ P.y \over P.z } = { P'.y \over 1}.$$

Where y' is actually the y coordinate of the point where the line going from the corner to the viewpoint intersects the canvas, which is, was we said earlier, the dot from which we can draw an image of the box on the canvas. Thus:

$$P'.y = { P.y \over P.z }.$$

As you can see, the projection of the corner's y-coordinate on the canvas, is nothing more than the corner's y-coordinate divided by its depth (the z-coordinate). This is probably one the simplest and most fundamental relation in computer graphics, known as the z or **perspective divide**. The exact same principle applies to the x coordinate. The projected point x coordinate (x') is the corner's x coordinate divided by its z coordinate.

Note though that because the z-coordinate of P is negative in our example (we will explain why this is always the case in the lesson from the Foundations of 3D Rendering section dedicated to the perspective projection matrix), when the x-coordinate is positive, the projected point's x-coordinate will become negative (similarly, if P.x is negative, P'.x will become positive. The same problem happens with the y-coordinate). As a result the image of the 3D object is mirrored both vertically and horizontally, which is not an effect we want. Thus, to avoid this problem, we will divide the P.x and P.y coordinates with -P.z instead; this will preserve the sign of the x and y coordinates. We finally get:

 

$$\begin{array}{l} P'.x = { P.x \over -P.z }\\ P'.y = { P.y \over -P.z }. \end{array}$$

We now have a method to compute the actual positions of the corners as they appear on the surface of the canvas. These are the two-dimensional coordinates of the points projected on the canvas. Let's update our basic program to compute these coordinates:

```
typedef float Point[3]; 
int main() 
{ 
    Point corners[8] = { 
         { 1, -1, -5}, 
         { 1, -1, -3}, 
         { 1,  1, -5}, 
         { 1,  1, -3}, 
         {-1, -1, -5}, 
         {-1, -1, -3}, 
         {-1,  1, -5}, 
         {-1,  1, -3} 
    }; 
 
    for (int i = 0; i < 8; ++i) { 
        // divide the x and y coordinates by the z coordinate to 
        // project the point on the canvas
        float x_proj = corners[i][0] / -corners[i][2]; 
        float y_proj = corners[i][1] / -corners[i][2]; 
        printf("projected corner: %d x:%f y:%f\n", i, x_proj, y_proj); 
    } 
 
    return 0; 
} 
```

![](https://www.scratchapixel.com/images/upload/rendering-3d-scene-overview/frustum.png?)

Figure 9: in this example, the canvas is 2 units along the x-axis and 2 units along the y-axis. You can change the dimension of the canvas if you wish. By making it bigger or smaller you will see more or less of the scene.

The size of the canvas itself is also arbitrary. It can also be a square or a rectangle. In our example, we made it two units wide in both dimensions, which means that the x and y coordinates of any points lying on the canvas are contained in the range -1 to 1 (figure 9).

Question: what happens if any of the projected point coordinates is not in this range, if for instance x' equals -1.1? The point is simply not visible, it lies outside the boundary of the canvas.

At this point we say that the projected point coordinates are in **screen space** (the space of the screen, where screen and canvas in this context our synonymous). But they are not easy to manipulate, because they can either be negative or positive, and we don't really know what they refer to with respect to for example the dimension of you computer screen (if we want to display these dots on the screen). For this reason, we will first normalize them, which simply means that we convert them from whatever range they are initially in, to the range [0,1]. In our case, because we need to map the coordinates from -1,1 to 0,1 we can simply write:

```
float x_proj_remap = (1 + x_proj) / 2; 
float y_proj_remap = (1 + y_proj) / 2; 
```

Coordinates of the projected points are not in the range 0,1. Such coordinates are said to be defined in NDC space, which stands for Normalized Device Coordinates. This is convenient because regardless of the original size of the canvas (or screen), which can be different depending on the settings you used, we now have all points coordinates defined in a common space. The term **normalize** is very common. It means that you somehow remap values from whatever range they were originally in, to the range [0,1]. Finally, we generally prefer to define point coordinates with regards to the dimensions of the final image, which as you may know or not, is defined in terms of pixels. A digital image is nothing else that a two-dimensional array of pixels (as is you computer screen).

Do you know what is the resolution or dimension of your screen in pixels?

A 512x512 image is a digital image having 512 rows of 512 pixels, of you prefer to see it the other way around, 512 columns of 512 vertically aligned pixels. Since our coordinates are already normalised, all we need to do to express them in terms of pixels, is to multiply these NDC coordinates by the image dimension (512). Here, our canvas being square, we will also use a square image:

```
#include <cstdlib> 
#include <cstdio> 
 
typedef float Point[3]; 
 
int main() 
{ 
    Point corners[8] = { 
         { 1, -1, -5}, 
         { 1, -1, -3}, 
         { 1,  1, -5}, 
         { 1,  1, -3}, 
         {-1, -1, -5}, 
         {-1, -1, -3}, 
         {-1,  1, -5}, 
         {-1,  1, -3} 
    }; 
 
    const unsigned int image_width = 512, image_height = 512; 
 
    for (int i = 0; i < 8; ++i) { 
        // divide the x and y coordinates by the z coordinate to 
        // project the point on the canvas
        float x_proj = corners[i][0] / -corners[i][2]; 
        float y_proj = corners[i][1] / -corners[i][2]; 
        float x_proj_remap = (1 + x_proj) / 2; 
        float y_proj_remap = (1 + y_proj) / 2; 
        float x_proj_pix = x_proj_remap * image_width; 
        float y_proj_pix = y_proj_remap * image_height; 
        printf("corner: %d x:%f y:%f\n", i, x_proj_pix, y_proj_pix); 
    } 
 
    return 0; 
} 
```

The resulting coordinates are said to be in raster space (XX what does raster mean, please explain). Our program is still limited because it doesn't actually create an image of the box, but if you compile it and run it with the following commands (copy/paste the code in a file and save it as box.cpp):

```
c++ box.cpp 
./a.out 
corner: 0 x:307.200012 y:204.800003 
corner: 1 x:341.333344 y:170.666656 
corner: 2 x:307.200012 y:307.200012 
corner: 3 x:341.333344 y:341.333344 
corner: 4 x:204.800003 y:204.800003 
corner: 5 x:170.666656 y:170.666656 
corner: 6 x:204.800003 y:307.200012 
corner: 7 x:170.666656 y:341.333344 
```

You can use a paint program to create an image (set its size to 512x512), and add dots at the pixel coordinates that your computed with the program. Then connect the dots to form the edges of the box, and you will get an actual image of the box (as showed in the [video](https://youtu.be/BiQJxdoL4K8 below). Pixel coordinates are integers, so you will need to round off the numbers given by the program.

What have we learned?
---------------------

*   That we first need to describe three-dimensional objects using things such as vertices and topology (information about how these vertices are connected to each other to form polygons or faces) before we can produce an image of the 3D scene (a scene is a collection of objects).
*   That rendering is the process by which an image of a 3D scene is created. No matter which technique you use to create 3D models (there are quite a few), rendering is a necessary step to 'see' any 3D virtual world.
*   From this simple exercise it should be quite apparent that mathematics (more than programming) are essential in the process of making an image with a computer. Actually the computer is merely a tool used to speed up the computation, but the rules used to create this image are pure mathematics. Geometry plays a particularly important role in this process, particularly to handle objects transformations (scale, rotation, translation) but also provide solutions to problems such as computing angles between lines, or finding out the intersection between a line and other simple shapes (a plane, a sphere, etc.).
*   In conlusion, computer graphics is mostly mathematics applied to a computer program which purpose is to generate an image (photo-real or not) at the quickest possible speed (and the accuracy that computers are capable of).
*   Modeling includes all techniques used to create 3D models. Modeling techniques will be discussed in the Geometry/Modeling section.
*   While static models are fine, it is also possible to animate them over time. Which means that an image of the model at each time step needs to be rendered (you can for instance translate, rotate or scale the box a little between each consecutive image whether by animating the corners' coordinates or applying a transformation matrix to the model). More advanced animation techniques can be used to simulate the deformation of skin by bones and muscles. But all these techniques have in common that geometry (the faces making up the models) are deformed over time. Hence time, as suggested in the introduction is important in CGI as well. Check the Animation section to learn about this topic.
*   One particular field overlaps both animation and modeling. It includes all techniques used to **simulate** the motion of objects in a realistic manner. A very large field of computer graphics is devoted to simulating the motion of fluids (water, fire, smoke), fabric, hair, etc. The laws of physics are applied to 3D models to make them move, deform or break like they would in the real world. Physics simulation are generally very computationally expensive, but they can also run in real time (it all depends on the complexity of the scene you simulate).
*   Rendering is also computationally expensive task. How expensive depends of how much geometry your scene is made up and how photo-real you want the final image to be. In rendering, we differentiate two modes, an **off-line** and a **realtime** rendering mode. Real-time is used (it's actually a requirement) for video games, in which the content of the 3D scenes needs to be rendered at least at 30 frames per second (generally 60 frames a second is considered a standard). Most realtime rendering are performed on the GPU, a processor specially designed to render 3D scenes at the quickest possible speed. Realtime rendering techniques will be discussed in the Realtime section. Offline rendering is commonly used in the production of CGI for films where realtime is not a requirement (images are precomputed and stored before being displayed at 24 or 30 or 60 fps). It may take from a few seconds to many hours before one single image is complete, but it generally handles far more geometry and produce higher quality images than realtime rendering. However, real-time or offline rendering do tend to overlap more and more these days, with video games pushing the amount of geometry they can handle as well as quality, and offline rendering engines trying to take advantage of the latest advancements in the field of CPU technology to greatly improve their performances. Off-line rendering is the main topic of a few sections on Scratchapixel: Foundations of 3D Rendering, Techniques Specific to Ray Tracing, Light Transport Algorithms, Shading and Procedural Texturing. We advice you to read lessons of the first section in chronological order.

Where Should I start?
---------------------

We hope the simple box example got you hooked but the main goal of this introduction is to underline the role that geometry plays in computer graphics. Of course it's not only about geometry, but a lot of the problems can be solved with geometry. Most computer graphics books start with a chapter on geometry, which is always a bit discouraging because it seems like you need to study a lot before you can actually get to making fun stuff. However, we really recommend you to read the lesson on [Geometry](http://localhost/lessons/mathematics-physics-for-computer-graphics/geometry) first before anything else. We will talk and learn about points but also about the concept of vector and normal. We will learn about coordinate systems and more importantly about the concept of matrix. Matrices are used extensively to handle transformations such as rotation, scaling or translation. These concepts are used everywhere throughout all computer graphics literature which is why you need to study them first.

Many CG books do not provide a very good introduction to geometry maybe because the authors assume that readers already know about it or that it's better to read books devoted to this particular topic. Our lesson on geometry is very different. It's very thorough and explain everything in very simple words (including things that only people who worked in production will tell you about). Do start your studies with reading this lesson first.

What Should I Read Next?
------------------------

It's generally easier and more fun to start learning computer graphics programming with rendering. One possible way for you to get through the content of this website, is to start reading the lessons from the [Foundation of 3D Rendering](/localhost/lessons/3d-basic-rendering) section in chronological order. To understand the content of a lesson you may need prior knowledge about some other techniques. At the beginning of each lesson you will find a list of other lessons that contains everything you need to know in order to understand the content of the lesson you are about to start (we call them prerequisites). Use this list to guide you through the content of the website and more importantly get the foundations you need in order to progress in your studies.