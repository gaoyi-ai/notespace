---
title: Camera Calibration impl by Python
categories:
- 3D
- Camera Calibration
tags:
- Camera Calibration
date: 2022/6/7
---



# Camera Calibration

## Camera Geometry and The Pinhole Model

> [Camera Calibration. Camera Geometry and The Pinhole Model | by Aerin Kim | Towards Data Science](https://towardsdatascience.com/camera-calibration-fda5beb373c3)

Camera calibration or camera resectioning **estimates the parameters of a pinhole camera model** given photograph. Usually, the pinhole camera parameters are represented in a 3 × 4 matrix called the camera matrix. We use these parameters to **estimate the actual size of an object** or **determine the location of the camera in the world**.

## How

Before we talk about camera calibration, first you need to understand how the pinhole camera works.

> Why do I need to know about the pinhole camera?

Because it is the essence of how any camera works. The pinhole camera model explains the relationship between a point in the world and the projection on the image plane (image sensor).

# The Pinhole Model

> How do we project the points in the world into a camera sensor?

如果我们使用广角相机的传感器，我们最终会得到模糊的图像，因为成像传感器在传感器的同一位置收集来自物体上多个点的光线。

解决这个问题的办法是在成像传感器前面放一个带有小孔的屏障。

屏障只允许有限的光线通过孔，并减少图像的模糊性。

![img](https://miro.medium.com/max/875/1*z0V2rtG2WkLwKgPqu-QCpQ.jpeg)

https://en.wikipedia.org/wiki/Depth_of_field

[Example] 不同光圈大小的真实图像

![img](https://miro.medium.com/max/875/1*ARDk0TScH0nSnbU-diaWsA.png)

https://en.wikipedia.org/wiki/Depth_of_field#Effect_of_lens_aperture

**The two most important parameters in a pinhole camera model**

1. **Focal length**: the distance between the pinhole and the image plane
    It affects **the size of the projected image**. It affects the camera **focus** when using lenses.

2. **Camera center:** The coordinates of the center of the **pinhole**.

![img](https://miro.medium.com/max/875/1*x2C8ksonO1JzBPqHfGzopQ.jpeg)

[https://en.wikipedia.org/wiki/Pinhole_camera_model](https://en.wikipedia.org/wiki/Pinhole_camera_model#geometry)

针孔相机模型非常简单。通过了解焦距和相机的中心，我们可以用数学方法计算出从物体反射的光线将射入图像平面的位置。

The focal length and the camera center are the camera **intrinsic parameters, K**. (**K** is an industry norm to express the intrinsic matrix.)

# Coordinate System Transformation (via Matrix Algebra!)

![image-20220607185834978](images/Camera%20Calibration/image-20220607185834978.png)

> Why do we want this?
>
> **In order to project the point in the world frame to the camera image plane!**

**光（从物体上反射出来的）**从世界上通过相机孔径（针孔）到达传感器表面。通过光圈投射到传感器表面的结果是翻转的图像。为了避免翻转的混乱，我们在相机中心的前面定义了一个的**虚拟图像平面**（黄色平面）。

![img](images/Camera%20Calibration/0fXQlnjpSwslSWVFC.png)

Diagram from [Simplified Camera Model Projection](https://www.researchgate.net/figure/Pinhole-Camera-Model-ideal-projection-of-a-3D-object-on-a-2D-image_fig1_326518096)

```
# World Coordinate System 
Oworld = [Xw, Yw, Zw]
# Camera Coordinate System
Ocamera = [Xc, Yc, Zc]
# Pixel Coordinate System
Oimage = [u,v]
```

We define a 3 by 3 **rotation matrix** (**R**) and a 3 by 1 **translation vector** (**t**) in order to model ANY transformation between a world coordinate system and another.

Now we can frame the projection problem (World Coordinates → Image Coordinates) as

1. **World coordinates** → Camera coordinates
2. Camera coordinates → **Image coordinate**

```
Oworld [Xw,Yw,Zw] → Oimage [u,v]
```

## How? by using Linear Algebra!

```
1. World coordinates →  Camera coordinates
Ocamera = [R|t] * Oworld
2. Camera coordinates → Image coordinate
Oimage = K * Ocamera 
Remind me what K (camera intrinsic parameter) was?
```

![img](https://miro.medium.com/max/205/1*COt2h9WgoWzpv035yPaUCA.png)

**intrinsic parameters, K: f for focal length, c for camera center, which are camera specific params**

Both steps 1 and 2 are just matrix multiplications. Therefore it can be re-written (combined) as:

```
Oimage = P * Oworld = K[R|t] * Oworld
Let P = K[R|t]
P as Projection
```

Wait, **K** is (3,3) matrix. **[R|t]** is (3,4). (| means you are concatenating matrix **R** with vector **t**.) **Oworld [Xw,Yw,Zw]** is (3,1).

Then you can’t multiply **K[R|t] (3,4)** with **Oworld [Xw,Yw,Zw] (3,1)**!

😎 We can resolve this by adding one at the end the Oworld vector **[Xw,Yw,Zw,1], called homogeneous coordinate (or projective coordinate)**.

If you want to further transform image coordinates to pixel coordinates: Divide x and y by z to get homogeneous coordinates in the image plane.

```
[x, y, z] -> [u, v, 1] = 1/z * [x, y, z]
```

This is it. This is the core. **This simple projection principle will be used in every 3d visual perception algorithm, from object detection to 3d scene reconstruction.**

在现实生活中，会有更复杂的情况，例如，非正方形的像素、相机接入的倾斜、失真、非单位长宽比等。然而，**他们只改变了摄像机矩阵K**，方程仍然是一样的。

A few things to note:

...

c) 图像坐标（虚拟图像平面）[u, v]从虚拟图像平面的左上角开始。这就是为什么我们要将像素位置调整到图像坐标帧。

# Seeing is believing

Take a look at the function `project_ego_to_image`. It calls two functions in a row, `project_ego_to_cam` first, then`project_cam_to_image` , just as we converted the world coordinate into the image coordinate by breaking it down into 2 steps: **World coordinates** → Camera coordinates, then Camera coordinates → **Image coordinate**.

`cart2hom` converts Cartesian coordinates into Homogeneous coordinates.

<script src="https://gist.github.com/aerinkim/d00e07b8aee06a8d83cba4a6c022527f.js"></script>
