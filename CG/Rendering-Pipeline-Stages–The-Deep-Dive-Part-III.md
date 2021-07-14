---
title: Rendering Pipeline Stages–The Deep Dive Part III
categories:
- CG
- Rendering
tags:
- Rendering
date: 2021/7/13
---

> [3D Pipeline Part III - ExtremeTech](https://www.extremetech.com/computing/49618-3d-pipeline-part-iii)

This is the third and final installment in our detailed tutorial on the 3D pipeline. In this, the final installment of our three-part pipeline odyssey, we’ll get deeper into rendering functions such as shadowing, antialiasing, Z-buffering, and finally, output to the display.

In Part 1, we covered the basics of real-time 3D processing and presented a high-level overview of 3D pipeline operations. We then reviewed coordinate systems and spaces, described the application pipeline stage in detail, and covered portions of the geometry stage. Part II wrapped up the geometry stage (including culling, lighting, clipping, and transforming to screen space), and explored triangle setup, shading, texture-mapping/MIP-Mapping, fog effects, and alpha blending.