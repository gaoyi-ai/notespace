---
title: WebGL
categories:
- CG
- WebGL
tags:
- webgl
date: 2021/7/13
---



# WebGL

# `viewport`

`void gl.viewport(x, y, width, height);`

`x` : A [`GLint`](https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/Types) specifying the horizontal coordinate for the lower left corner of the viewport origin. Default value: 0.

`y` : A [`GLint`](https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/Types) specifying the vertical coordinate for the lower left corner of the viewport origin. Default value: 0.

width : A non-negative [`Glsizei`](https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/Types) specifying the width of the viewport. Default value: width of the canvas.

height : A non-negative [`Glsizei`](https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/Types) specifying the height of the viewport. Default value: height of the canvas.

the viewport which is basically just saying in which part of the canvas do we want to draw the things that we specify in gl and `gl.viewport(0, 0, canvas.width, canvas.height);` is basically saying use the entire canvas.

