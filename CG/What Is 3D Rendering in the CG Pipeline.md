---
title: What Is 3D Rendering in the CG Pipeline
categories:
- CG
- Rendering
tags:
- Rendering
date: 2021/7/13
---



> [www.lifewire.com](https://www.lifewire.com/what-is-rendering-1954)

> Rendering refers to the calculations performed by a 3D software package’s render engine to translate ......

The rendering process plays a crucial role in the computer graphics development cycle. Rendering is the most technically complex aspect of [3D production](https://www.lifewire.com/what-is-3d-1951), but it can actually be understood quite easily in the context of an analogy: Much like a film photographer must develop and print his photos before they can be displayed, computer graphics professionals are burdened a similar necessity.

When an artist works on a [3D scene](https://www.lifewire.com/what-is-3d-modeling-2164), the models he manipulates are actually a mathematical representation of points and surfaces (more specifically, vertices and polygons) in three-dimensional space.

The term rendering refers to the calculations performed by a [3D software package’s](https://www.lifewire.com/free-3d-software-list-2005) render engine to translate the scene from a mathematical approximation to a finalized 3D image. During the process, the entire scene’s spatial, textural, and lighting information are combined to determine the color value of each pixel in the flattened image.

术语渲染是指由 [3D 软件包](https://www.lifewire.com/free-3d-software-list-2005) 渲染引擎执行的计算，用于将场景从数学近似转换为最终的 3D 图像。 在此过程中，将整个场景的空间、纹理和光照信息结合起来，以确定扁平化图像中每个像素的颜色值。

Two Types of Rendering
----------------------

There are two major types of rendering, their chief difference being the speed at which images are computed and finalized.

1.  **Real-Time Rendering**: Real-time rendering is used most prominently in gaming and interactive graphics, where images must be computed from 3D information at an incredibly rapid pace. Because it is impossible to predict exactly how a player will interact with the game environment, images must be rendered in “real-time” as the action unfolds.
2.  **Speed Matters**: In order for the motion to appear fluid, a minimum of 18 to 20 frames per second must be rendered to the screen. Anything less than this and action will appear choppy.
3.  **The methods**: Real-time rendering is drastically improved by [dedicated graphics hardware](https://www.lifewire.com/graphics-cards-3d-graphics-834089), and by pre-compiling as much information as possible. A great deal of a game environment’s lighting information is pre-computed and “baked” directly into the environment’s texture files to improve render speed.
4.  **Offline or Pre-Rendering**: Offline rendering is used in situations where speed is less of an issue, with calculations typically performed using multi-core CPUs rather than dedicated graphics hardware. Offline rendering is seen most frequently in animation and effects work where visual complexity and photorealism are held to a much higher standard. Since there is no unpredictability as to what will appear in each frame, large studios have been known to dedicate up to 90 hours of render time to individual frames.
5.  **Photorealism**: Because offline rendering occurs within an open-ended time-frame, higher levels of photorealism can be achieved than with real-time rendering. Characters, environments, and their associated textures and lights are typically allowed higher polygon counts, and [4k (or higher) resolution](https://www.lifewire.com/4k-resolution-overview-and-perspective-1846842) texture files.

Rendering Techniques
--------------------

There are three major computational techniques used for most rendering. Each has its own set of advantages and disadvantages, making all three viable options in certain situations.

*   **Scanline (or rasterization)**: Scanline rendering is used when speed is a necessity, which makes it the technique of choice for real-time rendering and interactive graphics. **Instead of rendering an image pixel-by-pixel, scanline renderers compute on a polygon by polygon basis.** Scanline techniques used in conjunction with precomputed (baked) lighting can achieve speeds of 60 frames per second or better on a high-end graphics card.
*   **Raytracing**: In raytracing, for every pixel in the scene, one or more rays of light are traced from the camera to the nearest 3D object. The light ray is then passed through a set number of "bounces," which can include reflection or refraction depending on the materials in the 3D scene. The color of each pixel is computed algorithmically based on the light ray's interaction with objects in its traced path. Raytracing is capable of greater photorealism than scanline but is exponentially slower.
*   **Radiosity**: Unlike raytracing, radiosity is calculated independent of the camera, and is surface oriented rather than pixel-by-pixel. The primary function of radiosity is to more accurately simulate surface color by accounting for indirect illumination (bounced diffuse light). Radiosity is typically characterized by soft graduated shadows and color bleeding, where light from brightly colored objects "bleeds" onto nearby surfaces.
    **辐射度**：与光线追踪不同，辐射度是独立于相机计算的，并且是面向表面的，而不是逐像素的。 光能传递的主要功能是通过考虑间接照明（反射漫射光）来更准确地模拟表面颜色。 光能传递的典型特征是柔和的渐变阴影和颜色渗色，其中来自鲜艳物体的光“渗色”到附近的表面上。

In practice, radiosity and raytracing are often used in conjunction with one another, using the advantages of each system to achieve impressive levels of photorealism.
