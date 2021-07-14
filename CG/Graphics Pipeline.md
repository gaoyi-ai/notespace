---
title: Graphics Pipeline
categories:
- CG
- Rendering
tags:
- Rendering
date: 2021/7/13
---



> [web.archive.org](https://web.archive.org/web/20180707221446/http://nehe.gamedev.net/tutorial/prep_the_graphics_pipeline/59002)

> OpenGL Tutorials, Demos, Games and More...

In this preparation tutorial I will give an brief overview of the graphics pipeline. A basic knowledge of the steps that a graphics card performs while rendering is essential for using and understanding modern OpenGL.

OpenGL is of course, a graphics rendering API. It provides functions for you as a programmer to generate primitives (triangles, points etc.) by specifying the vertices that make them. These vertices are then manipulated by the graphics card, and then the final shapes are rasterized with the eventual result being a chunk of pixel data in a buffer. This buffer, called the framebuffer, is what you see displayed on the screen.

There are a few steps though between supplying the raw vertex data, and the framebuffer being displayed and this is called the rendering pipeline. Below is a simplified basic description of the stages of the pipeline, some have been omitted for the sake of clarity and will be covered in later lessons.

### Step 1. - Per-vertex Operations

In this stage the vertices that are sent to OpenGL are normally transformed through the model-view-projection matrix into screen coordinates. In modern GL, this stage is overriden by the vertex shader. This is where per-vertex lighting is normally calculated. Of course, as you have control of this stage via GLSL, it's up to you what you do with it!

### Step 2 - Clipping and culling

Primitives are clipped to the screen and faces that are marked for culling (e.g. backface culling) are culled before rasterization.

### Step 3 - Rasterization

The vertices are formed into primitives and rasterized (filled in).

### Step 4 - Per-fragment Operations

This stage can also be overridden by GLSL, this time using a fragment-shader. This is where you calculate the final colour of on-screen pixels, the colour is normally calculated using interpolated values passed from the vertex shader to sample from a texture. But again, you have full control over this.

### Step 5 - Framebuffer

This is where the pixels end up in a big chunk of memory called the framebuffer. The default framebuffer is displayed on screen when the buffers are swapped (there are at least two destination buffers that are rapidly switched to create a smooth animation). Interestingly, OpenGL allows for multiple framebuffer objects (FBOs) which allow rendering offscreen. These FBOs can then be applied to primitives as textures.

That's it! A brief description of the main steps involved in getting your triangles on screen!