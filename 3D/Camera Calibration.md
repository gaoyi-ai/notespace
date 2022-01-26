---
title: Camera Calibration. Camera Geometry and The Pinhole Model
categories:
- 3D
- Camera Calibration
tags:
- Camera Calibration
date: 2021/12/20
---



# Camera Calibration. Camera Geometry and The Pinhole Model

> [Camera Calibration. Camera Geometry and The Pinhole Model | by Aerin Kim | Towards Data Science](https://towardsdatascience.com/camera-calibration-fda5beb373c3)

## Camera Geometry and The Pinhole Model

Camera calibration or camera resectioning **estimates the parameters of a pinhole camera model** given photograph. Usually, the pinhole camera parameters are represented in a 3 × 4 matrix called the camera matrix. We use these parameters to **estimate the actual size of an object** or **determine the location of the camera in the world**.

# How

Before we talk about camera calibration, first you need to understand how the pinhole camera works.

> Why do I need to know about the pinhole camera?

Because it is the essence of how any camera works. The pinhole camera model explains the relationship between a point in the world and the projection on the image plane (image sensor).

> How do we project the points in the world into a camera sensor?

# The Pinhole **Model**

If we use a wide-open camera sensor, we will end up with blurry images, because the imaging sensor collects light rays from multiple points on the object at the same location on the sensor.

The solution to this problem is to put a barrier in front of the imaging sensor with a tiny hole.

The barrier allows only a limited number of light rays to pass through the hole, and reduces the blurriness of the image.

![img](https://miro.medium.com/max/875/1*z0V2rtG2WkLwKgPqu-QCpQ.jpeg)

https://en.wikipedia.org/wiki/Depth_of_field

**[Example] Real image with different aperture sizes**

![img](https://miro.medium.com/max/875/1*ARDk0TScH0nSnbU-diaWsA.png)

https://en.wikipedia.org/wiki/Depth_of_field#Effect_of_lens_aperture

## **The two most important parameters in a pinhole camera model**

1. **Focal length**: the distance between the pinhole and the image plane

It affects **the size of the projected image**. It affects the camera **focus** when using lenses.

\2. **Camera center:** The coordinates of the center of the **pinhole**.

![img](https://miro.medium.com/max/875/1*x2C8ksonO1JzBPqHfGzopQ.jpeg)

[https://en.wikipedia.org/wiki/Pinhole_camera_model](https://en.wikipedia.org/wiki/Pinhole_camera_model#geometry)

The pinhole camera model is very straightforward. By knowing the focal length and camera’s center, we can mathematically calculate the location where a ray of light that is reflected from an object will strike the image plane.

The focal length and the camera center are the camera **intrinsic parameters, K**. (**K** is an industry norm to express the intrinsic matrix.)

## **Intrinsic parameters**

(Aka, the camera matrix.)

![img](https://miro.medium.com/max/205/1*COt2h9WgoWzpv035yPaUCA.png)

**intrinsic parameters, K**.

(C*x, Cy)* : camera center in pixels.
(*fx*, *fy*) : Focal length in pixels.

*fx* = *F*/*px*
*fy* = *F*/*py*

*F* : Focal length in world units (e.g. millimeters.)

![img](https://miro.medium.com/max/333/1*3fTMdFEQhc53YWWXhQSSKw.png)

Pixel Skew

(P*x*, P*y*) : Size of the pixel in world units.

*s* : Skew coefficient, which is non-zero if the image axes are not perpendicular.
*s* = *fx* tan(*α)*

# Coordinate System Transformation (via Matrix Algebra!)

> Why do we want this?
>
> **In order to project the point in the world frame to the camera image plane!**
>
> To do what?
>
> **(If we are talking about self-driving cars) To localize self-driving cars!**

**Light (reflected from the object)** travels from the world through the camera aperture (pinhole) to the sensor surface. The projection onto the sensor surface through the aperture results in flipped images. To avoid the flipping confusion, we define a **virtual image plane** (yellow plane) in front of the camera center.

![img](https://miro.medium.com/max/875/0*fXQlnjpSwslSWVFC.png)

Diagram from [Simplified Camera Model Projection](https://www.researchgate.net/figure/Pinhole-Camera-Model-ideal-projection-of-a-3D-object-on-a-2D-image_fig1_326518096)

```
# World Coordinate System 
Oworld = [Xw, Yw, Zw]# Camera Coordinate System
Ocamera = [Xc, Yc, Zc]# Pixel Coordinate System
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
1. World coordinates →  Camera coordinatesOcamera = [R|t] * Oworld2. Camera coordinates → Image coordinateOimage = K * Ocamera Remind me what K (camera intrinsic parameter) was?
```

![img](https://miro.medium.com/max/205/1*COt2h9WgoWzpv035yPaUCA.png)

**intrinsic parameters, K: f for focal length, c for camera center, which are camera specific params**

Both steps 1 and 2 are just matrix multiplications. Therefore it can be re-written (combined) as:

```
Oimage = P * Oworld = K[R|t] * OworldLet P = K[R|t]
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

In real life, there will be more complex scenarios, e.g. non-square pixels, camera access skew, distortion, non-unit aspect ratio, etc. However, **they only change the camera matrix K**, and the equations will still be the same.

A few things to note:

a) The **rotation matrix** (**R**) and the **translation vector** (**t**) are called **extrinsic** **parameters** because they are "external" to the camera.

The translation vector ***t\*** can be interpreted as the position of the world origin **in camera coordinates**, and the columns of the rotation matrix ***R\*** represent the directions of the world-axes **in camera coordinates**. This can be a little confusing to interpret because we are used to thinking in terms of the world coordinates.

b) Usually, multiple sensors (e.g. camera, lidar, radar, etc.) are used for perception in self-driving vehicles. Each sensor comes with its own extrinsic parameters that define the transform from the sensor frame to the vehicle frame.

c) Image coordinate (virtual image plane) **[u, v]** starts from the top left corner of the virtual image plane. That’s why we adjust the pixel locations to the image coordinate frame.

# Seeing is believing

Take a look at the function `project_ego_to_image`. It calls two functions in a row, `project_ego_to_cam` first, then`project_cam_to_image` , just as we converted the world coordinate into the image coordinate by breaking it down into 2 steps: **World coordinates** → Camera coordinates, then Camera coordinates → **Image coordinate**.

`cart2hom` converts Cartesian coordinates into Homogeneous coordinates.

<script src="https://gist.github.com/aerinkim/d00e07b8aee06a8d83cba4a6c022527f.js"></script>

The code snippet above is from [argoverse-api](https://github.com/argoai/argoverse-api).

[HomePublicly available datasets for self-driving research rarely include rich map data, even though detailed maps are…www.argoverse.org](https://www.argoverse.org/)

In the jupyter notebook below, you can see the manual calculation (the point cloud projection from world to image plane) matches with the result from argoverse api.

![img](https://miro.medium.com/max/2395/1*qUGn6c46cCVmuy_Ip7983Q.png)

![img](https://miro.medium.com/max/2263/1*OoT_TMggXjlW4nA9vLpLAg.png)

![img](https://miro.medium.com/max/2263/1*okTKRkffKXC7Ct50lphP6A.png)

![img](https://miro.medium.com/max/2263/1*z46iishi6H9-jhXLaO-zeQ.png)

Code: [https://github.com/aerinkim/TowardsDataScience/blob/master/Camera%20Calibration%20Argoverse.ipynb](https://github.com/aerinkim/TowardsDataScience/blob/master/Camera Calibration Argoverse.ipynb)