---
title: Shader types
categories:
- CG
- OpenGL
- Shader
tags:
- OpenGL
- Shader
date: 2021/7/13
---



# [Shader types](https://developer.mozilla.org/en-US/docs/Games/Techniques/3D_on_the_web/GLSL_Shaders#shader_types)

A shader is essentially a function required to draw something on the screen. Shaders run on a [GPU](https://en.wikipedia.org/wiki/GPU) (graphics processing unit), which is optimized for such operations. Using a GPU to deal with shaders offloads some of the number crunching from the CPU. This allows the CPU to focus its processing power on other tasks, like executing code.

### [Vertex shaders](https://developer.mozilla.org/en-US/docs/Games/Techniques/3D_on_the_web/GLSL_Shaders#vertex_shader)

Vertex shaders manipulate coordinates in a 3D space and are called once per vertex. The purpose of the vertex shader is to set up the `gl_Position` variable — this is a special, global, and built-in GLSL variable. `gl_Position` is used to store the position of the current vertex.

The `void main()` function is a standard way of defining the `gl_Position` variable. Everything inside `void main()` will be executed by the vertex shader. A vertex shader yields a variable containing how to project a vertex's position in 3D space onto a 2D screen.

### [Fragment shaders](https://developer.mozilla.org/en-US/docs/Games/Techniques/3D_on_the_web/GLSL_Shaders#fragment_shader)

Fragment (or texture) shaders define RGBA (red, green, blue, alpha) colors for each pixel being processed — a single fragment shader is called once per pixel. The purpose of the fragment shader is to set up the `gl_FragColor` variable. `gl_FragColor` is a built-in GLSL variable like `gl_Position`.

The calculations result in a variable containing the information about the RGBA color.