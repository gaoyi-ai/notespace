---
title: What Is Camera Calibration?
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

![img](https://www.mathworks.com/help/vision/ug/calibration_applications.png)



Camera parameters include intrinsics, extrinsics, and distortion coefficients. To estimate the camera parameters, you need to have 3-D world points and their corresponding 2-D image points. You can get these correspondences using multiple images of a calibration pattern, such as a checkerboard. Using the correspondences, you can solve for the camera parameters. After you calibrate a camera, to evaluate the accuracy of the estimated parameters, you can:

- Plot the relative locations of the camera and the calibration pattern
- Calculate the reprojection errors.
- Calculate the parameter estimation errors.

### Camera Models

The Computer Vision Toolbox™ contains calibration algorithms for the pinhole camera model and the fisheye camera model. You can use the fisheye model with cameras up to a field of view (FOV) of 195 degrees.

![The pinhole model and the fisheye model side-by-side](https://www.mathworks.com/help/vision/ug/pinhole_fisheye.png)

The pinhole calibration algorithm is based on the model proposed by Jean-Yves Bouguet [[3\]](https://www.mathworks.com/help/vision/ug/camera-calibration.html#bu1ahkv). The model includes, the pinhole camera model [[1\]](https://www.mathworks.com/help/vision/ug/camera-calibration.html#buvr2qb-1) and lens distortion [[2\]](https://www.mathworks.com/help/vision/ug/camera-calibration.html#buvr2qb-2).The pinhole camera model does not account for lens distortion because an ideal pinhole camera does not have a lens. To accurately represent a real camera, the full camera model used by the algorithm includes the radial and tangential lens distortion.

Because of the extreme distortion a fisheye lens produces, the pinhole model cannot model a fisheye camera. For details on camera calibration using the fisheye model, see [Fisheye Calibration Basics](https://www.mathworks.com/help/vision/ug/fisheye-calibration-basics.html).

### Pinhole Camera Model

A pinhole camera is a simple camera without a lens and with a single small aperture. Light rays pass through the aperture and project an inverted image on the opposite side of the camera. Think of the virtual image plane as being in front of the camera and containing the upright image of the scene.

![img](https://www.mathworks.com/help/vision/ug/camera_calibration_focal_point.png)



The pinhole camera parameters are represented in a 4-by-3 matrix called the *camera matrix*. This matrix maps the 3-D world scene into the image plane. The calibration algorithm calculates the camera matrix using the extrinsic and intrinsic parameters. The extrinsic parameters represent the location of the camera in the 3-D scene. The intrinsic parameters represent the optical center and focal length of the camera.

![img](https://www.mathworks.com/help/vision/ug/calibration_camera_matrix.png)

The world points are transformed to camera coordinates using the extrinsics parameters. The camera coordinates are mapped into the image plane using the intrinsics parameters.

![img](https://www.mathworks.com/help/vision/ug/calibration_cameramodel_coords.png)



### Camera Calibration Parameters

The calibration algorithm calculates the camera matrix using the extrinsic and intrinsic parameters. The extrinsic parameters represent a rigid transformation from 3-D world coordinate system to the 3-D camera’s coordinate system. The intrinsic parameters represent a projective transformation from the 3-D camera’s coordinates into the 2-D image coordinates.



![img](https://www.mathworks.com/help/vision/ug/calibration_coordinate_blocks.png)



#### Extrinsic Parameters

The extrinsic parameters consist of a rotation, *R*, and a translation, *t*. The origin of the camera’s coordinate system is at its optical center and its *x-* and *y-*axis define the image plane.

![img](https://www.mathworks.com/help/vision/ug/calibration_rt_coordinates.png)

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

![img](https://www.mathworks.com/help/vision/ug/calibration_skew.png)

$\left[\begin{array}{ll}c_{x} & c_{y}\end{array}\right]$ - Optical center (the principal point), in pixels.
$(f_{x}, f_{y})$ - Focal length in pixels.
$f_{x}=F / p_{x} & \\ f_{y}=F / p_{y} & \\ F-\text { Focal length in world units, typically expressed in } \text { millimeters.}$ 
$\left(p_{x},\right.  \left.p_{y}\right)$ - Size of the pixel in world units.
$s-$ Skew coefficient, which is non-zero if the image axes
are not perpendicular.
$s=f_{x}$ tan $\alpha$

### Distortion in Camera Calibration

The camera matrix does not account for lens distortion because an ideal pinhole camera does not have a lens. To accurately represent a real camera, the camera model includes the radial and tangential lens distortion.

#### Radial Distortion

Radial distortion occurs when light rays bend more near the edges of a lens than they do at its optical center. The smaller the lens, the greater the distortion.



![Negative radial distortion "pincushion", no distortion, and positive radial distortion "barrel" grid.](https://www.mathworks.com/help/vision/ug/calibration_radial_distortion.png)

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

Tangential distortion occurs when the lens and the image plane are not parallel. The tangential distortion coefficients model this type of distortion.

![Comparison of zero tangential distortion and tangential distortion.](https://www.mathworks.com/help/vision/ug/calibration_tangentialdistortion.png)

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