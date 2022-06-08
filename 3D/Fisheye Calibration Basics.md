---

---



## Fisheye Calibration Basics

> [Fisheye Calibration Basics - MATLAB & Simulink (mathworks.com)](https://www.mathworks.com/help/vision/ug/fisheye-calibration-basics.html)

Camera calibration is the process of computing the extrinsic and intrinsic parameters of a camera. Once you calibrate a camera, you can use the image information to recover 3-D information from 2-D images. You can also undistort images taken with a fisheye camera. The Computer Vision Toolbox™ contains calibration algorithms for the pinhole camera model and the fisheye camera model. You can use the fisheye model with cameras up to a field of view (FOV) of 195 degrees.

Fisheye cameras are used in odometry and to solve the simultaneous localization and mapping (SLAM) problems visually. Other applications include, surveillance systems, GoPro, virtual reality (VR) to capture 360 degree field of view (fov), and stitching algorithms. These cameras use a complex series of lenses to enlarge the camera's field of view, enabling it to capture wide panoramic or hemispherical images. However, the lenses achieve this extremely wide angle view by distorting the lines of perspective in the images



![img](https://www.mathworks.com/help/vision/ug/beforeafter.png)



Because of the extreme distortion a fisheye lens produces, the pinhole model cannot model a fisheye camera.

![img](https://www.mathworks.com/help/vision/ug/pinhole_fisheye.png)

### Fisheye Camera Model

The Computer Vision Toolbox calibration algorithm uses the fisheye camera model proposed by Scaramuzza [[1\]](https://www.mathworks.com/help/vision/ug/fisheye-calibration-basics.html#mw_f299f68d-403b-45e0-9de5-55203a09460d). The model uses an omnidirectional camera model. The process treats the imaging system as a compact system. In order to relate a 3-D world point on to a 2-D image, you must obtain the camera extrinsic and intrinsic parameters. World points are transformed to camera coordinates using the extrinsics parameters. The camera coordinates are mapped into the image plane using the intrinsics parameters.

![img](https://www.mathworks.com/help/vision/ug/calibration_world_to_image.png)

#### Extrinsic Parameters

The extrinsic parameters consist of a rotation, *R*, and a translation, *t*. The origin of the camera's coordinate system is at its optical center and its *x*- and *y*-axis define the image plane.

![img](https://www.mathworks.com/help/vision/ug/calibration_extrinsics_rt.png)

The transformation from world points to camera points is:

![img](https://www.mathworks.com/help/vision/ug/calibration_extrinsics.png)

#### Intrinsic Parameters

For the fisheye camera model, the intrinsic parameters include the polynomial mapping coefficients of the projection function. The alignment coefficients are related to sensor alignment and the transformation from the sensor plane to a pixel location in the camera image plane.

The following equation maps an image point into its corresponding 3-D vector.
$$
\left(\begin{array}{l}
X_{c} \\
Y_{c} \\
Z_{c}
\end{array}\right)=\lambda\left(\begin{array}{c}
u \\
v \\
a_{0}+a_{2} \rho^{2}+a_{3} \rho^{3}+a_{4} \rho^{4}
\end{array}\right)
$$
- $(u, v)$ are the ideal image projections of the real-world points.
- $\lambda$ represents a scalar factor.
- $a_{0}, a_{2}, a_{3}, a_{4}$ are polynomial coefficents described by the Scaramuzza model, where $a_{1}=0$.
- $\rho$ is a function of $(u, v)$ and depends only on the distance of a point from the image center: $\rho=\sqrt{u^{2}+v^{2}}$

The intrinsic parameters also account for stretching and distortion. The stretch matrix compensates for the sensor-to-lens misalignment, and the distortion vector adjusts the (0,0) location of the image plane.

![img](https://www.mathworks.com/help/vision/ug/stretchshift.png)

The following equation relates the real distorted coordinates (*u''*,*v''*) to the ideal distorted coordinates (*u*,*v*).

![img](https://www.mathworks.com/help/vision/ug/stretchdistortionlabeled.png)