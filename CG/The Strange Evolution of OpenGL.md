---
title: The Strange Evolution of OpenGL
categories:
- CG
- OpenGL
tags:
- OpenGL
date: 2021/7/13
---



# [The Strange Evolution of OpenGL](https://www.shamusyoung.com/twentysidedtale/?p=26340)

In the old days, you could sit down and write a dozen or so lines of code that would put some polygons up on the screen.

The old way:

1. Set up your OpenGL context so you can draw stuff.
2. Position the camera.
3. Specify a few vertices to make a triangle.
4. If you’re feeling creative, you could maybe color them or put a texture map in there, but whatever.

The new way:

1. Set up your OpenGL context so you can draw stuff.
2. You’ll need a vertex shader. That’s another whole program witten in GLSL, which looks a bit like C++ but is actually its own language.
3. You’ll also need a fragment shader. Yes, another program.
4. You’ll need to compile both shaders. That is, your program runs some code to turn some other code into a program. It’s very meta.
5. You’ll need to build an interface so your game can talk to the shaders you just wrote.
6. You’ll need to gather up your vertices and pack them into a vertex buffer, along with any data they might need.
7. Explain the format of your vertex data to OpenGL and store that data on the GPU.
8. Position the camera.
9. Draw those triangles you put together a couple of steps ago.
10. If you’re feeling creative, you can re-write your shaders and their interface to support some color or texture. Hope you planned ahead!

Our videogames are based on triangles. **Everything** is triangles. Even cube-based Minecraft is made by creating rectangles from pairs of triangles. Even text and icons on-screen are made by putting pictures of words and symbols onto triangle pairs.

我们的电子游戏基于三角形。 **一切**都是三角形。 甚至基于立方体的 Minecraft 也是通过从成对的三角形创建矩形来制作的。 甚至屏幕上的文字和图标也是通过将文字和符号的图片放在三角形对上来制作的。

I suppose there are a few other ways our graphics technology might have developed if history had played out just a little differently. We might have wound up with [voxels](http://www.shamusyoung.com/twentysidedtale/?p=890), for example. But triangles was always a likely path for us to take.

我想如果历史的发展稍有不同，我们的图形技术可能会以其他一些方式发展。 例如，我们可能已经结束了体素。 但三角形始终是我们可能采用的路径。

Why triangles and not rectangles? Because triangles are mathematically more fundamental than rectangles. You can make rectangles from triangles, but you can’t make triangles from rectangles. Computers hate ambiguity, and there’s a certain ambiguity to rendering with rectangles. Like your geometry teacher was busy telling you while you were drawing Power Rangers in your notebook, “3 points form a plane.” More informally, a 3-legged stool is inherently stable but a 4-legged stool might wobble. That wobble introduces a certain ambiguity. If you try to draw a rectangle and all 4 points aren’t on the same plane, that wobble needs to be resolved one way or another before it can begin drawing. And it turns out that the solution to that problem involves breaking the rectangle… into triangles.

为什么是三角形而不是矩形？ 因为三角形在数学上比矩形更基本。 你可以用三角形制作矩形，但不能用矩形制作三角形。 计算机讨厌歧义，用矩形渲染有一定的歧义。 就像你在笔记本上画 Power Rangers 时，几何老师忙着告诉你，“3 个点组成一个平面。” 更通俗地说，3 条腿的凳子本质上是稳定的，但 4 条腿的凳子可能会摇晃。 这种不稳定引入了某种模糊性。 如果您尝试绘制一个矩形并且所有 4 个点不在同一平面上，则需要以某种方式解决该不稳定，然后才能开始绘制。 事实证明，该问题的解决方案涉及将矩形……分成三角形。

The point is: It’s triangles all the way down.

3D rendering today consists of taking a bunch of 3D data and mushing it down to the 2D plane of your screen. From the Minecraft image above, you can see a cube is made from rectangles and rectangles are made from triangles. So while your brain uses its magical perspective detection to see the 3D world, to the computer it’s just a big pile of triangles sitting next to each other, with no more meaning than this lone triangle:

今天的 3D 渲染包括获取一堆 3D 数据并将其压缩到屏幕的 2D 平面。 从上面的 Minecraft 图像中，您可以看到立方体由矩形构成，而矩形由三角形构成。 因此，当您的大脑使用其神奇的透视检测来查看 3D 世界时，对于计算机而言，它只是一大堆彼此相邻的三角形，没有比这个孤立的三角形更多的意义：

So you give OpenGL 3 points. Assuming those points wind up on the screen then we get a triangle. Once the triangle is calculated, the graphics hardware fills in the space with pixels. This is called [rasterization](https://en.wikipedia.org/wiki/Rasterisation).

所以你给OpenGL 3分。 假设这些点在屏幕上结束 然后我们得到一个三角形。 一旦计算出三角形，图形硬件就会用像素填充空间。 这称为 [光栅化](https://en.wikipedia.org/wiki/Rasterisation)。

We don’t generally want to fill those pixels in with a solid color. I mean, you *can*, but you wind up with something like this:

| ![I don’t want to come off like some kind of graphics snob, but this probably isn’t good enough to ship.](http://www.shamusyoung.com/twentysidedtale/images/bughunt10.jpg) |
| ------------------------------------------------------------ |
| I don’t want to come off like some kind of graphics snob, but this probably isn’t good enough to ship. |

So while you’re defining the positions of your vertices, you can also give each one a color.

| ![Here is how to meet the public’s insatiable demand for red/green/blue triangles. You’re welcome.](http://www.shamusyoung.com/twentysidedtale/images/opengl_triangles3.jpg) |
| ------------------------------------------------------------ |
| Here is how to meet the public’s insatiable demand for red/green/blue triangles. You’re welcome. |

I imagine most people understand what a texture map is, even if they don’t get how it works. But for the sake of completeness: A texture map is when you take an image and use it to color your triangles like so:

| ![I wish I’d drawn this diagram a bit differently. Imagine the Mona Lisa not once, but as an infinite plane of that face repeating over and over like endless tiling wallpaper. Now picture putting the A, B, and C points anywhere you like on that plane. This will, of course, form a triangle. The image within that triangle will be mapped to the shape of the 3D triangle we’re drawing on screen. (Even if they’re wildly different proportions.)](http://www.shamusyoung.com/twentysidedtale/images/opengl_triangles4.jpg) |
| ------------------------------------------------------------ |
| I wish I’d drawn this diagram a bit differently. Imagine the Mona Lisa not once, but as an infinite plane of that face repeating over and over like endless tiling wallpaper. Now picture putting the A, B, and C points anywhere you like on that plane. This will, of course, form a triangle. The image within that triangle will be mapped to the shape of the 3D triangle we’re drawing on screen. (Even if they’re wildly different proportions.) |

### 1. Rasterization

As far as I can tell, this was the “killer app” of graphics cards. Your game shoves the texture maps over to the graphics card. Then it tells OpenGL how big the canvas (the screen) is. Then it sends a bunch of 2D triangles along the lines of, “This triangle occupies this part of the screen and uses such-and-such part of this texture.” The graphics card would do all the work of coloring those triangles in with pixels.

据我所知，这是显卡的“杀手级应用”。 您的游戏将纹理贴图推送到显卡上。 然后它告诉 OpenGL 画布（屏幕）有多大。 然后它沿着“这个三角形占据了屏幕的这一部分并使用了这个纹理的某某部分”的线条发送了一堆 2D 三角形。 图形卡将完成用像素为这些三角形着色的所有工作。

graphics cards. They just needed to fill in triangles with pixels. They didn’t need to do three completely different things at once, or handle complex branching code. Since their output was in colored pixels and not hard data, a certain degree of slop was allowed. The math could make certain approximations and shortcuts because if the output was 0.01% off, nobody would be able to tell. Even if you had superhuman eyes that could spot the subtle color differences, you’re using a monitor that can’t *display* differences that slight.

显卡。 他们只需要用像素填充三角形。 他们不需要同时做三件完全不同的事情，也不需要处理复杂的分支代码。 由于它们的输出是彩色像素而不是硬数据，因此允许一定程度的倾斜。 数学可以做出某些近似值和捷径，因为如果输出是 0.01% 的折扣，没有人会知道。 即使您拥有可以发现细微色差的超人眼睛，您使用的显示器也无法*显示*细微的差异。

All of this meant that graphics processors could be much simpler. It’s a bit like comparing a classic fast-food place to the work of a single highly trained chef. The chef can make you almost anything you can ask for, while the fast food place can only make hamburgers. But the fast food place is optimized for it and can crank out 24 hamburgers a minute. Simplicity is speed.

所有这些都意味着图形处理器可以简单得多。 这有点像将一个经典的快餐店比作一位训练有素的厨师的工作。 厨师几乎可以为您制作任何您可以要求的东西，而快餐店只能制作汉堡包。 但是快餐店针对它进行了优化，每分钟可以制作 24 个汉堡包。 简单就是速度。

Simpler cores didn’t just make them faster, it also made them smaller. So instead of one giant core they could have a whole bunch of them. And those cores would do nothing but take in 2D triangles and spit out pixels.

更简单的内核不仅使它们更快，而且使它们更小。 因此，他们可以拥有一大堆，而不是一个巨大的核心。 这些核心只会接收 2D 三角形并吐出像素。

### 2. Transform and Lighting

So it’s sometime in the late 90’s and we’ve got these graphics cards that take 2D triangles and fill them in with pixels. That’s cool, I guess. It was certainly a massive boost in terms of our rendering capabilities. But it was clear they could be doing a lot more.

There’s a step I’ve been sort of glossing over here. That’s the bit of math you have to do to figure out if a particular triangle will end up on-screen, and if so, where. “Hm. Given that the camera is in position C and looking in direction D, and this particular triangle is so many units away, then the vertices of this triangle will end up on *this* part of the screen.” Like coloring in triangles with pixels, this is yet another brute-force, bulk, doing-math-on-three-numbers kind of job. Which means it’s a good thing to offload onto the graphics card.

这里有一个步骤我一直在掩饰。 这就是你必须做的一些数学运算，以确定一个特定的三角形是否会出现在屏幕上，如果是，那么在哪里。 “嗯。 考虑到相机在位置 C 并朝 D 方向看，并且这个特定的三角形距离很多单位，那么这个三角形的顶点将最终出现在屏幕的*这个*部分上。” 就像用像素为三角形着色一样，这是另一种蛮力的、批量的、对三个数字进行数学运算的工作。 这意味着卸载到显卡上是一件好事。

This process of translating vertices from game-space to screen-space is called “Transform and lighting”.

这种将顶点从游戏空间转换到屏幕空间的过程称为“变换和照明”。

### 3. Vertex Buffers

Remember that a graphics card is, in a lot of ways, a separate computer. It’s got its own memory and its own processors. So when you want to tell the graphics card to render a triangle, you need to send it all of the information about that triangle. There is a bit of a choke point between the devices, meaning it takes much longer to send a triangle to the graphics card that it does to (say) move the triangle from one part of memory to another.

请记住，显卡在很多方面都是一台独立的计算机。它有自己的内存和自己的处理器。因此，当您想告诉显卡渲染一个三角形时，您需要向它发送有关该三角形的所有信息。设备之间有一点阻塞点，这意味着将三角形发送到图形卡需要更长的时间（例如）将三角形从内存的一个部分移动到另一个部分。

So vertex buffers give us a way to shove all the data from the PC and store it on the GPU. So instead of sending 1,000 polygons, I just need to tell the card, “Remember where I gave you 1,000 triangles? Draw those again, but with the camera in this new location.”

因此，顶点缓冲区为我们提供了一种从 PC 中推送所有数据并将其存储在 GPU 上的方法。因此，与其发送 1,000 个多边形，我只需要告诉卡片，“还记得我在哪里给你 1,000 个三角形吗？再画一遍，但把相机放在这个新位置。”

### 4. Shaders

Like I said a few paragraphs ago: If we wanted games to continue improving visually, it was pretty clear that mindlessly cranking up the polygon counts wasn’t the way to go. To take the next step, we needed to change **how** we drew those polygons. And for that we needed shaders.

就像我在几段前所说的那样：如果我们希望游戏在视觉上继续改进，很明显，盲目地增加多边形数量是不可行的。下一步，我们需要改变绘制这些多边形的方式。为此，我们需要着色器。

So now developers need to make two shaders: A vertex shader to do the Transform & Lighting, and a fragment shader to do the rasterization.

所以现在开发人员需要制作两个着色器：一个顶点着色器来做变换和光照，一个片段着色器来做光栅化。

![Not pictured: A complete lack of documentation, ambiguous standards, sloppy implementation, and standards-breaking features from the big GPU companies.](https://www.shamusyoung.com/twentysidedtale/images/octant11_1.png)

Shaders made a lot of things possible or practical: Light bloom, anti-aliasing, various lighting tricks, normal maps. This was a massive turning point in game development. 

着色器使很多事情成为可能或实用：光晕、抗锯齿、各种照明技巧、法线贴图。这是游戏开发的一个重大转折点。