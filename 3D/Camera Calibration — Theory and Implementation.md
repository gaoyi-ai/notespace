---
title: Camera Calibration — Theory and Implementation
categories:
- 3D
- Camera Calibration
tags:
- Camera Calibration
date: 2021/12/20
---



# Camera Calibration — Theory and Implementation

> https://medium.com/analytics-vidhya/camera-calibration-theory-and-implementation-b253dad449fb

# Why is Camera Calibration so important?

Imagine that you are a part of a cool business or academia project testing out a state-of-the-art Computer Vision algorithm. You will use the images from your camera and the camera specs as input. Now imagine that the camera specs are inaccurate and the images are distorted. You can never completely verify the performance of your algorithm if the input itself is tainted. This is where Camera Calibration can help you improve the performance.

It turns out that many computer vision algorithms in stereo vision, 3D reconstruction, panorama stitching, SLAM, visual odometry, etc assume that the images that are provided are undistorted while its features behave according to the laws of projective geometry. The efficacy of these computer vision algorithms will drop drastically if the camera is not calibrated properly.

# What is this post about?

We will learn about why and how to calibrate our cameras prior to running Computer Vision algorithms. There is going to be some math and code but the key takeaway should be the intuition about calibration.

# What are the prerequisites before reading this article?

The idea about mathematical models of physical systems, Linear Algebra, Transformation Matrices, and C++ should be enough.

# Prerequisite concepts

## Homography

![img](https://miro.medium.com/max/828/1*buAxW-6WNdBrXZJYAVM7PA.jpeg)

Figure 1a — View of a wall from an angle | Figure 1b — Transformed view of a wall to look like as if we were standing right in front of it

The idea of homography is not very intuitive for people who have not studied computer vision before (at least for me and my classmates). Suppose we saw a wall like in Fig. 1a from an angle and we ask ourselves “I wonder how this wall would look like of I saw it while facing it parallel to it”. We can find out without moving from our place. There is an appropriate 3x3 matrix *H*, that can help us achieve this. Now, each pixel in this image has an x and y coordinate which can be represented as (x,y)ᵗ. The superscript t is to denote the transpose of a matrix/vector. Here is what we do. We first convert (x,y)ᵗ to a homogenous coordinate by appending a 1 to the end of the vector to get (x,y,1)ᵗ.

Now, why do we add a 1 to the vector? It turns out that the homography transformation *H* is defined as a 3x3 matrix, so we need a column vector with 3 elements to do the appropriate math with H. So we push a 1 and it seems to make everyone happy and keeps the math sensible.

Now that we have our homogenous coordinate. We multiply *H* with (x,y,1)ᵗ to get (x’,y’, w)ᵗ. Then we divide x’ and y’ with w and convert it back to normal coordinate (x’/w, y’/w)ᵗ. Basically, if the top right corner of a window in the wall in the image has the pixel coordinates (x,y)ᵗ then the same point will have the coordinates (x’/w, y’/w)ᵗ in the transformed image. Now do this for every pixel point in Fig. 1a to get the Fig 1b. I hope you noticed some pixels in Fig. 1b is just black while some pixels in Fig. 1a are not seen in 1b. This is because as we shift our view from one place to another, we lose some part of the scene while introducing new parts of the scene.

> The fundamental assumption we made was we already new the matrix H. In general, if we have the matrix H, we can transform one view into another view. However, if we have 2 views, we can compute the homography H between them. How to find them is out of scope of this article.

## Pin Hole Model of the Camera

Now we will create a math model for the camera.

![img](https://miro.medium.com/max/486/1*ZJbQpCzHFEPfxtbDUDLVaw.png)

Figure 2. Representation of the Pinhole model of the camera, source: Stanford AI lab

The idea is simple. A real-world object exists; for example a candle. We poke a small hole in a box and let the light pass through it. We will notice that the light from the real world object passes through the hole and forms an inverted and laterally inverted image on the screen which is at a distance *F* from the hole. For mathematical simplicity, we assume there is a virtual screen in front of the hole at the same distance *F.* If the image falls on this virtual screen, we would get the same image from the real screen except it is upright.

Now let us define a coordinate system for the camera. We define the origin to be where the pinhole is and call it the camera center *C*. Let us also assume an arbitrary point which belongs to a real-world object at (X, Y, Z)ᵗ with respect to the camera coordinate system . What do you think will be the coordinates of this point if it were projected onto the virtual screen?

![img](https://miro.medium.com/max/875/1*BH2nm9Pe37CFV7qwnrmcJQ.png)

Figure 3 —Camera Coordinate System on the left and the similarity triangle to compute the image coordinates of a real-world object

From Fig. 3, it can be seen that two pairs of similar triangles are formed one for x and y-axis (only the y-axis triangles are seen in Fig. 3 on the right). Hence, if (X, Y, Z)ᵗ are the real-world coordinates then its projected view on the virtual plane will have the coordinates (FX/Z, FY/Z, F)ᵗ. The virtual plane is *F* distance from the origin so z = F while the x and y coordinates are computed using similarity of triangles.

## Using a Lens for our camera

The previous mathematical model represents the ideal camera. In reality, if we use this kind of camera, the image formed will be too dim. If we increase the size of the hole the image will become blurry. A more realistic camera uses a lens in place of a pinhole with the focal length *F* and uses a light-sensitive sensor like a CMOS sensor which converts the light image to a digital signal. This introduces all sorts of problems. First and foremost, due to manufacturing defects, it is possible that the focal length in the x-direction is different from the y-direction. Secondly, the center of the light sensor has to be perfectly aligned with the center of the lens. Finally, we want to convert the (X, Y, Z)ᵗ point in the camera coordinate system to a point in the image coordinate system.

Now let us account for all this. Assume that the focal length along x and y-direction is *F_x* and *F_y.* These focal lengths will be expressed in millimeter, centimeter, or inches. So we can use a factor *s* which represents the number of pixels per unit length. we write *s\*F_x* as *f_x* and *s\*F_y* as *f_y.*

![img](https://miro.medium.com/max/264/1*aLnOp8kiBsa-perPVHTqPA.png)

Equation 1

The center for the image is slightly offset from the origin of the camera coordinate (because the lens and sensor are not perfectly aligned). Moreover, the origin of the digital image in the image coordinate system is typically located on the top-left corner. Hence we need to translate the image using a vector (c_x, c_y)ᵗ. This gives us

![img](https://miro.medium.com/max/346/1*tDvStytEIfx66h4Uaikdpg.png)

Equation 2

The above equation is a more accurate model of the camera. If we convert all coordinates to homogenous coordinates, we can write the above equation using matrix multiplication.
$$
\left[\begin{array}{c}
x \\
y \\
w
\end{array}\right]=\left[\begin{array}{ccc}
f_{x} & 0 & c_{x} \\
0 & f_{y} & c_{y} \\
0 & 0 & 1
\end{array}\right]\left[\begin{array}{l}
X \\
Y \\
Z
\end{array}\right] \text { or } p=K P
$$
Equation 3

The matrix *K* is called the intrinsic matrix while f_x, f_y, c_x, c_y are intrinsic parameters.

> Finding this Intrinsic parameters is the first purpose of Camera Calibration.

## Image Distortion due to Imperfect Lenses

![img](https://miro.medium.com/max/875/1*qsqYBjrktUfq4wltuzCKow.png)

Figure 4-Radial Distortion

![img](https://miro.medium.com/max/361/1*A_MCu9RBXhRniDmhRvcjKA.png)

Figure 5- Tangential Distortion

Lenses used in cameras tend to show two types of distortion. The first is a radial distortion as seen in the left image in Fig. 4. This is caused by the imperfect lens shape. The outer edges generally curve outward (although they can curve inward as well). This creates a fish-eye view. The second type of distortion is tangential distortion. This is caused because the CMOS chip is not parallel to the lens. This creates a slightly skewed view.

The radial distortion can be modeled using three parameters as follows
$$
\left[\begin{array}{l}
x_{d} \\
y_{d}
\end{array}\right]=\left(1+k_{1} r^{2}+k_{2} r^{4}+k_{3} r^{6}\right)\left[\begin{array}{l}
x \\
y
\end{array}\right]
$$
Equation 4

The tangential distortion can be modeled using two parameters as follows
$$
\left[\begin{array}{l}
x_{d} \\
y_{d}
\end{array}\right]=\left[\begin{array}{l}
x \\
y
\end{array}\right]+\left[\begin{array}{l}
2 p_{1} x y+p_{2}\left(r^{2}+2 x^{2}\right) \\
2 p_{2} x y+p_{1}\left(r^{2}+2 y^{2}\right)
\end{array}\right]
$$
Equation 5

As we can see, there are a total of five distortion parameters k₁, k₂, k₃, p₁and p₂.

> Finding the distortion parameters is the final purpose of Camera Calibration.

For every pixel value, we calculate the corrected coordinates and undistort the radial and tangential distortions.

# What is Camera Calibration and why do we do it?

Imagine the speedometer in your car shows 3 km/h ( or 1.86 miles/h) even though your car is not moving. You will quickly realize that that whenever you are moving, the actual velocity is 3 km/h less than what is shown in your speedometer. Essentially, you are correcting the faulty reading from the sensor to get the real value. In the same way, the raw image obtained from the camera is not the real view from the ideal camera. We need to transform this raw image into something that would look like if we were to use the ideal camera. The parameters needed to transform the raw image to the ideal image the intrinsic parameters in the intrinsic matrix *K* and the distortion parameters.

> The process of computing the intrinsic parameters in the intrinsic matrix *K* and the distortion parameters is known as Camera Calibration

After finding the values of these parameters, we undistort the image to obtain the rectified image.

# How to calibrate cameras?

It turns out that we need the distortion parameters to compute the intrinsic parameters, while we need the intrinsic matrix to compute the distortion parameters. What we will do is, we will set all distortion parameters to 0, and then compute the intrinsic parameters. Then using the computed intrinsic parameters, we compute the distortion parameters. Now we repeat this process multiple times.

## Calibration Procedure using Zhang’s Technique to find Intrinsic Parameters

Let us look at the general overview of the calibration process proposed by Zhang [Zhang00]. This technique has already been implemented in OpenCV.

> Zhang’s technique will solve for the Intrinsic Matrix K.

We use a calibration object where all the coordinates of the “features” are known. The most common calibration object is the chessboard of known side lengths of each square. For this object, the “features ”are the corner points of each square not lying on the edge. Let us define the object coordinate system which is attached to the chessboard with the chessboard plane as the x-y plane. In this way, all the corner points will have a coordinate with respect to the object coordinate frame. Since all points are lying on the chessboard (the x-y plane), the z-coordinate is always 0. The corner points can have the coordinates as (1,1,0)ᵗ, (1,2,0)ᵗ (1,3,0)ᵗ, ….. etc with respect to the object frame.

We currently have 3 coordinate systems: the object coordinate system (attached to the chessboard), the camera coordinate system( attached to the lens of the camera), and the image coordinate system (which is a 2D coordinate system that gives x-y coordinates of pixels)

Now, if we take a picture of this chessboard from any view, with the camera we want to calibrate, identify the corner points in the image and correlate each corner points from the image to the real object, we will be able to find the homography *H* from the chessboard plane in the object coordinate system to the projection plane ( the image ) in the image coordinate system. Bear this idea in mind.

Now let us take a step back. If we can somehow relate the intrinsic matrix K to *H* in an equation, it can help solve for *K.* We know that form Equation 1, we can use the Intrinsic Matrix *K* to transform from the camera coordinates to image coordinates. Let us assume that rotation matrix *R* and the translational vector *t* are used to transform any coordinate from the object coordinate system to the camera coordinate system. Hence, we get

![img](https://miro.medium.com/max/851/1*x8dw5_UFBq8tDGPaBvbcbQ.png)

Equation 6

From the equations 3 and 6, we get

![img](https://miro.medium.com/max/298/1*RxfkW2pOzQMvfy8VDODB0Q.png)

Equation 7

P_obj is a homogenous vector expressed as (X,Y,Z,1)ᵗ. We also know that Z is always equal to zero. We can do a minor adjustment by removing the Z coordinate from P_obj and the third column vector in *R* to get a slight simplification. By doing this we get

![img](https://miro.medium.com/max/429/1*DVUhrr9loZIB9aDB5AsJQg.png)

Equation 8

Hence, we can conclude that

![img](https://miro.medium.com/max/325/1*RAN72p19IKzeofG8LrohWg.png)

Equation 9

Now, for each view of the chessboard, we get a new homography matrix *H* which adds 8 constraints. Moreover, every new view adds 6 unknowns due to a new rotation and translation which are called extrinsic parameters. Every view will have the same 4 unknowns from the intrinsic parameters. Hence, we need at least two or more views to over constrain the system of equations and compute the intrinsic parameters.

This means, that, if we calculate the homography for each view, we can solve for the intrinsic parameters. The details to solve Equation 9 will be provided in the following section.

## The Math for Solving equation 9

You can also refer to the contents of this entire section from the original paper [here](https://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=888718).

First of all, let us resolve the 3x3 homography matrix *H* as a vector of column vectors

![img](https://miro.medium.com/max/294/1*kyAZ7JTyiCJESsdAtapzTA.png)

Equation 10

Hence, Equation 9 in full form would look as follows.

![img](https://miro.medium.com/max/510/1*E8aHx6Ih1Ke1ASn79MnbJQ.png)

Equation 11

Solving for the above equation we get

![img](https://miro.medium.com/max/314/1*6HZvjjZcScSk2otkOTJHRQ.png)

Equation 12

We know that the r₁ and r₂ orthonormal, hence their dot product will be 0 and their magnitudes will be equal which means

![img](https://miro.medium.com/max/875/1*xd4xLcjKe0wLmMT0FSSQLQ.png)

Equation 13

We will introduce a new matrix *B* which is defined as the product of transpose of the inverse of *K* with the inverse of *K. B* can be simplified to a closed-form solution.

![img](https://miro.medium.com/max/875/1*tAYYUvueAWptsFJlh_l87g.png)

Equation 14

> Notice that B is symmetric

Any single term in Equation 13 can be represented as
$$
h_{i}^{T} B h_{j} \text { where } 1 \leq i, j \leq 2
$$
![img](https://miro.medium.com/max/875/1*PZgo9dnKrTieEC152t0tDw.png)

Equation 15

Our job is to find out B. From equation 13, 14 and 15 we can solve the elements of *B* as a system of linear equation with the coefficient matrix and variable matrix as follows
$$
\left[\begin{array}{c}
v_{12} \\
\left(v_{11}-v_{22}\right)^{T}
\end{array}\right] b=0
$$
Equation 16

Now comes my absolute favorite part. I am mind blown about how elegantly this algorithm has been formulated. The elements of *v* vector are computed from the homography matrix *H* for a single view of the chessboard. Hence Equation 16. For every new view, the *b* vector is constant as it is a function of the camera, but the *v* vector changes with each view. This is good news. What we can do is take multiple pictures from the camera, compute the homography for each, compute the new *v* vector and simply stack all v_12 and (v11- v22)ᵗ of each view on top of each other to get a long column vector V. Then we can simply solve for vector *b* using Singular Value Decomposition.

We have officially solved for B. The rest is now a straightforward closed-form solution.

![img](https://miro.medium.com/max/559/1*xBPmg-2yI322XaOlpLSJPw.png)

Equation 17

There is one last thing we need to compute are the extrinsic parameters *R* and *t*. Their formulas are below
$$
\begin{aligned}
&r_{1}=\lambda K^{-1} h_{1} \\
&r_{2}=\lambda K^{-1} h_{2} \\
&r_{3}=r_{1} \times r_{2} \\
&t=\lambda K^{-1} h_{3}
\end{aligned}
$$
Equation 18

Finally!! We finished the calibration! Wrong. We still need to find out the distortion coefficient parameters. But do not worry, the bulk of the setup is already done in the previous sections.

> Using Zhang’s procedure, we have computed the intrinsic matrix K and the extrinsic parameters R and t. Both sets of parameters are required again to compute distortion parameters.

## Calibration Procedure to find Distortion Parameters

In the previous sections, we captured images of the chessboard from different viewpoints, extracted and correlated its corners in the image to the real world corners and computed the intrinsic and extrinsic parameters. The coordinates of the corner points in the image are distorted. Let us denote these points as (xd, yd)ᵗ. Let us denote the coordinates where the corners should lie in the image from an ideal camera as (xc,yc)ᵗ.

We will reuse the images we had taken in the previous calibration step. Using Equation 7, we will compute (xc,yc)ᵗ. Using the models for radial and tangential distortions we get

![img](https://miro.medium.com/max/875/1*-XNBsccZguzGJRepE3FcsQ.png)

Equation 19

These pairs of equations are obtained for one corner of one view of the chessboard. We can stack more rows of equations from other corners and views. These equations are linear with respect to the distortion parameters. Hence, a simple least-squares solution can be obtained for the distortion parameters. In this process, we finish calibrating for the distortion parameters as well.

# The Code using OpenCV

<script src="https://gist.github.com/clueless-bachu/a74020a811caf0ab84e55cf6bb11418f.js"></script>

We will start by discussing the main functions that do the magic. The findChessboardCorners function (Lines 153 to 158) finds the corners of the chessboard for each image. The cornerSubPix function (Lines 163 to 167) refines the accuracy of the found corners to a sub-pixel level. Finally, the calibrateCamera function (Lines 201 to 210) does the camera calibration.

The program starts with a bunch of command-line arguments. These include the number of columns, rows, the length of each side in the chessboard, the path to images, and the verbosity. If all these arguments are not given in the command-line then an error is thrown (Lines 103 to 106). If help about its usage is required, we can pass the -h argument and the show_usage function (Lines 18 to 29) will be called. It is generally a good practice to pass arguments from the command-line. Finally, the arguments are parsed with the help of parse_args function (Lines 31 to 76) and each argument is assigned to the appropriate variable.

We read all the image paths (Lines 139 and 140) using the dir_files function (Lines 78 to 98). It takes in the path and outputs the absolute path of all images in the directory of the given path.

Next, we initialize all the required variables (Lines 127 to 136). We define the pattern size with the number of corner points in each row and column. The object_pts and image_pts variables will be used to store each corner point of each view in the object and image coordinate system respectively. We also define the output variables cameraMatrix and distortion.

After that, we load the color images from the stored image paths (Lines 139 and 140).

Now the main part. We loop over each image. For each image, we check if the image is valid (Lines 146 to 150). If it is not valid, then we skip the image. We find the corners of the chessboard and store it in the variable “corners”. We convert the image to grayscale (Line 162) because the cornerSubPix gave an error for color images. We refine the accuracy of the found corner points (Line 163 to 169). Then, if we pass the verbose argument as 1, we draw the corner points onto the image and display it (Line 172 to 179).

Now, the most important part (in my opinion) is to appropriately store the found corners onto the object_pts and image_pts data structure. These variables are a matrix where each row represents the corners of a single image while each element of each row represents a single corner as a vector of 3 elements ((X, Y, Z) for object_pts) or vector of 2 elements ((x,y) for image_pts). First, we create individual vector points and push them to a row vector. After that, we push the entire row to the main data structure (Lines 186 to 194). Notice, that while creating a vector for the object_pts, we multiply it by the scale. This is because we create coordinates in the form (1,1), (2,1)…etc, and scale it up according to the length of the side of the chessboard. This is now ready to be directly sent to the cameraCalibrate function.

We pass all the important arguments cameraCalibrate function and find out all the required parameters.

## Compiling, Running, and Examining the Output

I compiled the code using the following command

```
>> g++ calib.cpp -o calib `pkg-config --cflags --libs opencv`
```

We have to be careful with specifying the appropriate arguments during compilation. When I ran this without the pkg-config arguments I was getting a linking error.

> Linking errors were caused because only the function declarations were included in the header files; not the function itself. So the computer was confused about what the function does.

We run the code using the following command. Notice the arguments specified

```
>> ./calib -v 1 -p /home/vasista/Pictures/Webcam/ -r 6 -c 9 -s 1
```

The path command is unique to my computer, please specify the path to the directory where your images are stored.

Finally, this was the output that I received

```
Path: /home/vasista/Pictures/Webcam/
Cols: 9
Rows: 6
Scale: 1.000000 
Intrinsic Matrix:
[68.99938659967282, 0, 319.9999986330897;
 0, 28.14721786194214, 179.9999892462267;
 0, 0, 1]
Distortion Coefficients:
[-0.003394632213282647, -3.786919409222491e-07, -0.006844931007237185, -0.004091696754772819, 2.555933548677116e-09]
```

## Comments

We change different variables in these functions to improve the speed and accuracy of the calibration. Notice the distortion coefficients being pretty small. So when we undistort the image, there will not be any noticeable difference when we see it. Nevertheless, I would recommend running your algorithms with and without undistorting the image and check for accuracy. If the difference is negligible, then we can skip the undistorting of the image to get minor speedups.

# Conclusion

Thank you for reading this article. Let me know how you feel about the explanation in the responses below.

I would highly appreciate if you can suggest any good topics for future articles. They could be fun or application-oriented topics in Reinforcement Learning, Computer Vision, Robotics, etc.