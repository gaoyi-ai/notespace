---
title: Camera Calibration in MATLAB
categories:
- 3D
- Camera Calibration
tags:
- Camera Calibration
date: 2021/12/20
---



## What Is Camera Calibration?

*Geometric camera calibration*, also referred to as *camera resectioning*, estimates the parameters of a lens and image sensor of an image or video camera. You can use these parameters to correct for lens distortion, measure the size of an object in world units, or determine the location of the camera in the scene. These tasks are used in applications such as machine vision to detect and measure objects. They are also used in robotics, for navigation systems, and 3-D scene reconstruction.

Examples of what you can do after calibrating your camera:

![img](images/What%20Is%20Camera%20Calibration/calibration_applications.png)



摄像机参数包括内在参数、外在参数和失真系数。为了估计摄像机参数，你需要有三维世界点和它们对应的二维图像点。你可以使用校准图案的多个图像来获得这些对应关系，例如棋盘。利用这些对应关系，你可以求解摄像机的参数。在你校准了一个相机之后，为了评估估计参数的准确性，你可以：

- Plot the relative locations of the camera and the calibration pattern
    绘制相机和校准图案的相对位置图

- Calculate the reprojection errors.
    计算重投影误差

- Calculate the parameter estimation errors.
    计算参数估计误差

    

### Camera Models

The Computer Vision Toolbox™ contains calibration algorithms for the pinhole camera model and the fisheye camera model. You can use the fisheye model with cameras up to a field of view (FOV) of 195 degrees.

![The pinhole model and the fisheye model side-by-side](images/What%20Is%20Camera%20Calibration/pinhole_fisheye.png)

针孔校准算法是基于Jean-Yves Bouguet提出的模型[[3]](https://www.mathworks.com/help/vision/ug/camera-calibration.html#bu1ahkv)。该模型包括，针孔相机模型[[1]](https://www.mathworks.com/help/vision/ug/camera-calibration.html#buvr2qb-1)和镜头失真[[2]](https://www.mathworks.com/help/vision/ug/camera-calibration.html#buvr2qb-2)。针孔相机模型不考虑镜头失真，因为理想的针孔相机没有一个镜头。为了准确地表示一个真实的相机，该算法使用的完整相机模型包括径向和切向的镜头失真。

Because of the extreme distortion a fisheye lens produces, the pinhole model cannot model a fisheye camera. For details on camera calibration using the fisheye model, see [Fisheye Calibration Basics](https://www.mathworks.com/help/vision/ug/fisheye-calibration-basics.html).

### Pinhole Camera Model

A pinhole camera is a simple camera without a lens and with a single small aperture. Light rays pass through the aperture and project an inverted image on the opposite side of the camera. Think of the virtual image plane as being in front of the camera and containing the upright image of the scene.

![img](images/What%20Is%20Camera%20Calibration/camera_calibration_focal_point.png)

The pinhole camera parameters are represented in a 4-by-3 matrix called the *camera matrix*. This matrix maps the 3-D world scene into the image plane. The calibration algorithm calculates the camera matrix using the extrinsic and intrinsic parameters. The extrinsic parameters represent the location of the camera in the 3-D scene. The intrinsic parameters represent the optical center and focal length of the camera.

![img](images/What%20Is%20Camera%20Calibration/calibration_camera_matrix.png)

The world points are transformed to camera coordinates using the extrinsics parameters. The camera coordinates are mapped into the image plane using the intrinsics parameters.

![img](images/What%20Is%20Camera%20Calibration/calibration_cameramodel_coords.png)



### Camera Calibration Parameters

校准算法使用外部和内部参数计算相机矩阵。外部参数表示从 3-D 世界坐标系到 3-D 相机坐标系的刚性变换。内在参数表示从 3-D 相机坐标到 2-D 图像坐标的投影变换。

![img](images/What%20Is%20Camera%20Calibration/calibration_coordinate_blocks.png)

#### Extrinsic Parameters

The extrinsic parameters consist of a rotation, *R*, and a translation, *t*. The origin of the camera’s coordinate system is at its optical center and its *x-* and *y-*axis define the image plane.

![img](images/What%20Is%20Camera%20Calibration/calibration_rt_coordinates.png)

#### Intrinsic Parameters

The intrinsic parameters include the focal length, the optical center, also known as the principal point, and the skew coefficient. The camera intrinsic matrix, $K$, is defined as:
$$
\left[\begin{array}{ccc}
f_{x} & 0 & 0 \\
s & f_{y} & 0 \\
c_{x} & c_{y} & 1
\end{array}\right]
$$
The pixel skew is defined as:

![img](images/What%20Is%20Camera%20Calibration/calibration_skew.png)

$\left[\begin{array}{ll}c_{x} & c_{y}\end{array}\right]$ - Optical center (the principal point), in pixels.
$(f_{x}, f_{y})$ - Focal length in pixels.
$f_{x}=F / p_{x} & \\ f_{y}=F / p_{y} & \\ F-\text { Focal length in world units, typically expressed in } \text { millimeters.}$ 
$\left(p_{x},\right.  \left.p_{y}\right)$ - Size of the pixel in world units.
$s-$ Skew coefficient, which is non-zero if the image axes
are not perpendicular.
$s=f_{x}$ tan $\alpha$

### Distortion in Camera Calibration

摄像机矩阵不考虑镜头失真，因为理想的针孔摄像机没有镜头。为了准确地表示一个真实的相机，相机模型包括径向和切向的镜头失真。

#### Radial Distortion

当光线在靠近透镜边缘的弯曲程度大于在光学中心的弯曲程度时，就会发生径向畸变。镜头越小，畸变越大。

<img src="https://www.mathworks.com/help/vision/ug/calibration_radial_distortion.png" alt="Negative radial distortion &quot;pincushion&quot;, no distortion, and positive radial distortion &quot;barrel&quot; grid." style="zoom:150%;" />

The radial distortion coefficients model this type of distortion. The distorted points are denoted as $\left(x_{\text {distorted }}, y_{\text {distorted }}\right):$
$$
\begin{aligned}
&x_{\text {distorted }}=x\left(1+k_{1}^{\star} r^{2}+k_{2}^{\star} r^{4}+k_{3}{ }^{\star} r^{6}\right) \\
&y_{\text {distorted }}=y\left(1+k_{1}{ }^{\star} r^{2}+k_{2}{ }^{\star} r^{4}+k_{3}{ }^{\star} r^{6}\right)
\end{aligned}
$$
- $x, y$ - Undistorted pixel locations. $x$ and $y$ are in normalized image coordinates. Normalized image coordinates are calculated from pixel coordinates by translating to the optical center and dividing by the focal length in pixels. Thus, $x$ and $y$ are dimensionless.
- $k_{1}, k_{2}$, and $k_{3}-$ Radial distortion coefficients of the lens.
- $r^{2}=x^{2}+y^{2}$

Typically, two coefficients are sufficient for calibration. For severe distortion, such as in wide-angle lenses, you can select three coefficients to include *k*3.

#### Tangential Distortion

当镜头和像平面不平行时，就会发生切向畸变。切向畸变系数模拟这种类型的畸变。

![Comparison of zero tangential distortion and tangential distortion](images/What%20Is%20Camera%20Calibration/calibration_tangential_distortion.png)

The distorted points are denoted as ( $x_{\text {distorted }}$ $y_{\text {distorted }}$ ):
$x_{\text {distorted }}=x+\left[2^{\star} p_{1}^{\star} x^{*} y+p_{2}{ }^{\star}\left(r^{2}+2^{*} x^{2}\right)\right]$ $y_{\text {distorted }}=y+\left[p_{1}^{*}\left(r^{2}+2^{*} y^{2}\right)+2^{*} p_{2}^{*} x^{\star} y\right]$
- $x, y$ - Undistorted pixel locations. $x$ and $y$ are in normalized image coordinates. Normalized image coordinates are calculated from pixel coordinates by translating to the optical center and dividing by the focal length in pixels. Thus, $x$ and $y$ are dimensionless.
- $p_{1}$ and $p_{2}-$ Tangential distortion coefficients of the lens.
- $r^{2}=x^{2}+y^{2}$



## References

[1] Zhang, Z. “A Flexible New Technique for Camera Calibration.” *IEEE Transactions on Pattern Analysis and Machine Intelligence*. Vol. 22, No. 11, 2000, pp. 1330–1334.

[2] Heikkila, J., and O. Silven. “A Four-step Camera Calibration Procedure with Implicit Image Correction.” *IEEE International Conference on Computer Vision and Pattern Recognition*.1997.

[3] Bouguet, J. Y. “Camera Calibration Toolbox for Matlab.” Computational Vision at the California Institute of Technology. [Camera Calibration Toolbox for MATLAB](http://www.vision.caltech.edu/bouguetj/calib_doc/)

[4] Bradski, G., and A. Kaehler. *Learning OpenCV: Computer Vision with the OpenCV Library*. Sebastopol, CA: O'Reilly, 2008.