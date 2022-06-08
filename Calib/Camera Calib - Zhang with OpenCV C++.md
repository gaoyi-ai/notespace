---
title: Camera Calib - Zhang with OpenCV C++
categories:
- 3D
- Camera Calibration
tags:
- Camera Calibration
date: 2022/6/7
---

# Camera Calibration — Theory and Implementation

> [Camera Calibration — Theory and Implementation | by Vasista Ayyagari | Analytics Vidhya | Medium](https://medium.com/analytics-vidhya/camera-calibration-theory-and-implementation-b253dad449fb)

# Why is Camera Calibration so important?

Imagine that you are a part of a cool business or academia project testing out a state-of-the-art Computer Vision algorithm. You will use the images from your camera and the camera specs as input. Now imagine that the camera specs are inaccurate and the images are distorted. You can never completely verify the performance of your algorithm if the input itself is tainted. This is where Camera Calibration can help you improve the performance.

事实证明，立体视觉、三维重建、全景拼接、SLAM、视觉测距等方面的许多计算机视觉算法都假定所提供的图像是不失真的，而其特征是根据投影几何规律来表现的。如果相机没有被正确校准，这些计算机视觉算法的功效将急剧下降。

# What is this post about?

我们将学习为什么以及如何在运行计算机视觉算法之前校准我们的相机。将会有一些数学和代码，但关键的收获应该是关于校准的直觉。

# What are the prerequisites before reading this article?

The idea about mathematical models of physical systems, Linear Algebra, Transformation Matrices, and C++ should be enough.

# Prerequisite concepts

## Homography

![img](https://miro.medium.com/max/828/1*buAxW-6WNdBrXZJYAVM7PA.jpeg)

Figure 1a — View of a wall from an angle | Figure 1b — Transformed view of a wall to look like as if we were standing right in front of it

对于以前没有学过计算机视觉的人（至少对我和我的同学来说），单应性的想法并不是很直观。假设我们从一个角度看到了如图 1a 所示的墙，我们问自己“我想知道当我看到它平行于它时这面墙会是什么样子”。我们可以在不离开我们的地方找到答案。有一个合适的 3x3 矩阵 H，可以帮助我们实现这一点。现在，该图像中的每个像素都有一个 x 和 y 坐标，可以表示为 (x,y)ᵗ。上标 t 表示矩阵/向量的转置。这就是我们所做的。我们首先将 (x,y)ᵗ 转换为齐次坐标，方法是在向量末尾附加 1 以获得 (x,y,1)ᵗ。

现在，为什么我们要在向量上加 1？事实证明，单应变换 H 被定义为一个 3x3 矩阵，所以我们需要一个包含 3 个元素的列向量来对 H 进行适当的数学运算。所以我们推入 1，这似乎让每个人都开心并且保持数学合理。

现在我们有了齐次坐标。我们将 H 与 (x,y,1)ᵗ 相乘得到 (x',y', w)ᵗ。然后我们将 x' 和 y' 除以 w 并将其转换回正常坐标 (x'/w, y'/w)ᵗ。基本上，如果图像中墙壁中窗口的右上角具有像素坐标 (x,y)ᵗ，那么同一点将在转换后的图像中具有坐标 (x'/w, y'/w)ᵗ .现在对图 1a 中的每个像素点执行此操作以获得图 1b。我希望您注意到图 1b 中的一些像素只是黑色，而图 1a 中的一些像素在 1b 中看不到。这是因为当我们将视图从一个地方转移到另一个地方时，我们会在引入场景的新部分时丢失场景的某些部分。

> 我们所做的基本假设是我们已经新建了矩阵 H。一般来说，如果我们有矩阵 H，我们可以将一个视图转换为另一个视图。但是，如果我们有 2 个视图，我们可以计算它们之间的单应性 H。如何找到它们超出了本文的范围。

## Pin Hole Model of the Camera

Now we will create a math model for the camera.

![Figure 2. Representation of the Pinhole model of the camera, source: Stanford AI lab](https://miro.medium.com/max/486/1*ZJbQpCzHFEPfxtbDUDLVaw.png)

Figure 2. Representation of the Pinhole model of the camera, source: Stanford AI lab

The idea is simple. A real-world object exists; for example a candle. We poke a small hole in a box and let the light pass through it. We will notice that the light from the real world object passes through the hole and forms an inverted and laterally inverted image on the screen which is at a distance *F* from the hole. For mathematical simplicity, we assume there is a virtual screen in front of the hole at the same distance *F.* If the image falls on this virtual screen, we would get the same image from the real screen except it is upright.

Now let us define a coordinate system for the camera. We define the origin to be where the pinhole is and call it the camera center *C*. Let us also assume an arbitrary point which belongs to a real-world object at (X, Y, Z)ᵗ with respect to the camera coordinate system . What do you think will be the coordinates of this point if it were projected onto the virtual screen?

![img](https://miro.medium.com/max/875/1*BH2nm9Pe37CFV7qwnrmcJQ.png)

Figure 3 —Camera Coordinate System on the left and the similarity triangle to compute the image coordinates of a real-world object

From Fig. 3, it can be seen that two pairs of similar triangles are formed one for x and y-axis (only the y-axis triangles are seen in Fig. 3 on the right). Hence, if (X, Y, Z)ᵗ are the real-world coordinates then its projected view on the virtual plane will have the coordinates (FX/Z, FY/Z, F)ᵗ. The virtual plane is *F* distance from the origin so z = F while the x and y coordinates are computed using similarity of triangles.

## Using a Lens for our camera

The previous mathematical model represents the ideal camera. In reality, if we use this kind of camera, the image formed will be too dim. If we increase the size of the hole the image will become blurry. A more realistic camera uses a lens in place of a pinhole with the focal length *F* and uses a light-sensitive sensor like a CMOS sensor which converts the light image to a digital signal. This introduces all sorts of problems. First and foremost, due to manufacturing defects, it is possible that the focal length in the x-direction is different from the y-direction. Secondly, the center of the light sensor has to be perfectly aligned with the center of the lens. Finally, we want to convert the (X, Y, Z)ᵗ point in the camera coordinate system to a point in the image coordinate system.

Now let us account for all this. Assume that the focal length along x and y-direction is *F_x* and *F_y.* These focal lengths will be expressed in millimeter, centimeter, or inches. So we can use a factor *s* which represents the number of pixels per unit length. we write *s\*F_x* as *f_x* and *s\*F_y* as *f_y.*
$$
\left[\begin{array}{l}
x \\
y
\end{array}\right]=\left[\begin{array}{l}
f_{x} X / Z \\
f_{y} Y / Z
\end{array}\right]
$$
Equation 1

The center for the image is slightly offset from the origin of the camera coordinate (because the lens and sensor are not perfectly aligned). Moreover, the origin of the digital image in the image coordinate system is typically located on the top-left corner. Hence we need to translate the image using a vector (c_x, c_y)ᵗ. This gives us
$$
\left[\begin{array}{l}
x \\
y
\end{array}\right]=\left[\begin{array}{l}
f_{x} X / Z+c_{x} \\
f_{y} Y / Z+c_{y}
\end{array}\right]
$$
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

<img src="https://miro.medium.com/max/450/1*A_MCu9RBXhRniDmhRvcjKA.png" alt="img" style="zoom:150%;" />

Figure 5- Tangential Distortion

相机中使用的镜头往往会出现两种类型的失真。第一种是径向畸变，如图4中的左图所示。这是由不完美的镜头形状造成的。外边缘通常向外弯曲（尽管它们也可以向内弯曲）。这就形成了一个鱼眼视图。第二种类型的失真是切向失真。这是由于CMOS芯片与镜头不平行造成的。这创造了一个稍微倾斜的视图。

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

事实证明，我们需要失真参数来计算内参，而我们需要内在矩阵来计算失真参数。我们要做的是，我们将所有失真参数设置为 0，然后计算内参。然后使用计算的内参，我们计算失真参数。现在我们多次重复这个过程。

## Calibration Procedure using Zhang’s Technique to find Intrinsic Parameters

Let us look at the general overview of the calibration process proposed by Zhang [Zhang00]. This technique has already been implemented in OpenCV.

> Zhang’s technique will solve for the Intrinsic Matrix K.

我们使用一个校准对象，其中所有 "特征 "的坐标都是已知的。最常见的校准对象是每个方块的已知边长的棋盘。对于这个对象，"特征 "是每个方块的角点，不在边缘上。让我们定义物体坐标系，该坐标系与棋盘相连，棋盘平面为X-Y平面。这样一来，所有的角点都有一个相对于物体坐标系的坐标。由于所有的点都在棋盘上（X-Y平面），Z坐标总是0。角点的坐标可以是(1,1,0)ᵗ, (1,2,0)ᵗ (1,3,0)ᵗ, ..... 等相对于对象框架的坐标。

我们目前有3个坐标系：物体坐标系（附在棋盘上），相机坐标系（附在相机镜头上），以及图像坐标系（这是一个2D坐标系，给出像素的X-Y坐标）。

![image-20220607194858924](images/Camera%20Calibration%20%E2%80%94%20Theory%20and%20Implementation/image-20220607194858924.png)

现在，如果我们从任何角度拍摄这个棋盘的照片，使用我们要校准的相机，识别图像中的角点并将图像中的每个角点与真实对象相关联，我们将能够找到单应性H 从物体坐标系中的棋盘平面到图像坐标系中的投影平面（图像）。牢记这个想法。

现在让我们退后一步。如果我们能以某种方式将方程中的内参矩阵 K 与 H 联系起来，它可以帮助求解 K。我们知道从方程 1 中，我们可以使用内参矩阵 K 将相机坐标转换为图像坐标。让我们假设旋转矩阵 R 和平移向量 t 用于将任何坐标从对象坐标系转换到相机坐标系。因此，我们得到

![img](https://miro.medium.com/max/851/1*x8dw5_UFBq8tDGPaBvbcbQ.png)

Equation 6

From the equations 3 and 6, we get

![img](https://miro.medium.com/max/298/1*RxfkW2pOzQMvfy8VDODB0Q.png)

Equation 7

P_obj 是一个齐次向量，表示为 (X,Y,Z,1)ᵗ。我们也知道 Z 总是等于 0。我们可以通过移除 P_obj 中的 Z 坐标和 R 中的第三列向量来做一个小的调整，以得到轻微的简化。通过这样做，我们得到

![img](https://miro.medium.com/max/429/1*DVUhrr9loZIB9aDB5AsJQg.png)

Equation 8

Hence, we can conclude that

![img](https://miro.medium.com/max/325/1*RAN72p19IKzeofG8LrohWg.png)

Equation 9

现在，对于棋盘的每个视图，我们得到一个新的单应矩阵 H，它增加了 8 个约束。此外，由于新的旋转和平移，每个新视图都会增加 6 个未知数，这些未知数称为外参。每个视图都将具有相同的 4 个内参未知数。因此，我们需要至少两个或更多视图来过度约束方程组并计算内参。

这意味着，如果我们计算每个视图的单应性，我们可以求解内参。下一节将提供求解公式 9 的详细信息。

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

我们知道 r1 和 r2 正交，因此它们的点积将为 0，它们的大小将相等，这意味着
$$
r_{1}^{T} r_{2}=h_{1}\left(K^{-1}\right)^{T} K^{-1} h_{2}=0
$$
and
$$
r_{1}^{T} r_{1}=r_{2}^{T} r_{2} \Longrightarrow h_{1}\left(K^{-1}\right)^{T} K^{-1} h_{1}=h_{2}\left(K^{-1}\right)^{T} K^{-1} h_{2}
$$
Equation 13

我们将引入一个新的矩阵 $\mathrm{B}$ ，它被定义为 $\mathrm{K}$ 的逆矩阵与 $\mathrm{K}$ 的逆矩阵的转置乘积。B 可以简化为封闭形式的解。
$$
B=\left(K^{-1}\right)^{T} K^{-1}=\left[\begin{array}{lll}
B_{11} & B_{12} & B_{13} \\
B_{12} & B_{22} & B_{23} \\
B_{13} & B_{23} & B_{33}
\end{array}\right]=\left[\begin{array}{ccc}
1 / f_{x}^{2} & 0 & -c_{x} / f_{x}^{2} \\
0 & 1 / f_{y}^{2} & -c_{y} / f_{y}^{2} \\
-c_{x} / f_{x}^{2} & -c_{y} / f_{y}^{2} & c_{x} / f_{x}^{2}+c_{y} / f_{y}^{2}+1
\end{array}\right]
$$
Equation 14

> Notice that B is symmetric

Any single term in Equation 13 can be represented as
$$
h_{i}^{T} B h_{j} \text { where } 1 \leq i, j \leq 2
$$
$$
h_{i}^{T} B h_{j}=v_{i j}^{T} b=\left[\begin{array}{llllll}
h_{i 1} h_{j 1} & h_{i 1} h_{j 2}+h_{i 2} h_{j 1} & h_{i 2} h_{j 2} & h_{i 3} h_{j 1}+h_{i 1} h_{j 3} & h_{i 3} h_{j 2}+h_{i 2} h_{j 3} & h_{i 3} h_{j 3}
\end{array}\right]\left[\begin{array}{l}
B_{11} \\
B_{12} \\
B_{22} \\
B_{13} \\
B_{23} \\
B_{33}
\end{array}\right]
$$

Equation 15

我们的工作是找出B。从方程13、14和15中，我们可以将B的元素作为一个线性方程组来解决，其系数矩阵和变量矩阵如下所示（下列矩阵为2x6）
$$
\left[\begin{array}{c}
v_{12} \\
\left(v_{11}-v_{22}\right)^{T}
\end{array}\right] b=0
$$
Equation 16

v 向量的元素是根据棋盘的单个视图的单应矩阵 H 计算的。利用两个平面之间的对应点可以把单应性矩阵 H 求出来。即现在内参A乘以外参RT的这个矩阵已经获得了，就是H现在是已知量，因此等式 16。对于每个新视图，b 向量是恒定的，因为它是相机的内参，但 v 向量随每个视图而变化。这是个好消息。我们可以做的是从相机拍摄多张照片（至少3组），计算每张照片的单应性，计算新的 v 向量，然后简单地将每个视图的所有 v_12 和 (v11- v22)ᵗ 堆叠在一起以获得长列向量 V . 然后我们可以简单地使用奇异值分解来求解向量 b。

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

> 使用Zhang的程序，我们已经计算出了内参矩阵K和外参R和t。这两组参数都是计算失真参数所需要的。

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