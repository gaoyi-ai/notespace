---
title: gray-scale image
categories:
- Image Processing
tags:
- gray-scale
date: 2021/5/17 18:50:45
updated: 2021/5/17 20:00:13
---

In this post, we will learn how to create gray-scale images

A **gray-scale** (or gray level) **image** is simply one in which the only colors are shades of **gray**. The reason for differentiating such **images** from any other sort of color **image** is that less information needs to be provided for each pixel.

In a gray-scale image, each pixel has a value between 0 and 255, where zero corresponds to “black” and 255 corresponds to “white”. The values in between 0 and 255 are varying shades of gray, where values closer to 0 are darker and values closer to 255 are lighter.

![gray-scale_example](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/gray-scale_example.png)

Code

```python
import cv2
image = cv2.imread('bgrflag.jpg')
print(image.shape) # (400, 600, 3)
cv2.imshow("Image", image)
cv2.waitKey()
# option 1 - creating gray scale by converting color image
gray_image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
print(gray_image.shape) # (400, 600)
cv2.imshow("Grayscale Image", gray_image)
cv2.waitKey()
# option - reading an image in gray scale format
grayimage = cv2.imread('bgrflag.jpg', cv2.IMREAD_GRAYSCALE)
cv2.imshow("GrayImage", grayimage)
print(grayimage.shape) # (400, 600)
cv2.waitKey()
cv2.destroyAllWindows()
```
Gray-scale images are very common, in part because much of today’s display and image capture hardware can only support 8-bit images.

In addition, gray-scale images are entirely sufficient for many tasks and so there is no need to use more complicated and harder-to-process color images.

---

> [Grayscale Definition (techterms.com)](https://techterms.com/definition/grayscale)

Grayscale is a range of [monochromatic](https://techterms.com/definition/monochrome) shades from black to white. Therefore, a grayscale image contains only shades of gray and no color.

While [digital](https://techterms.com/definition/digital) images can be saved as grayscale (or black and white) images, even color images contain grayscale information. This is because each [pixel](https://techterms.com/definition/pixel) has a luminance value, regardless of its color. Luminance can also be described as brightness or intensity, which can be measured on a scale from black (zero intensity) to white (full intensity). Most image [file formats](https://techterms.com/definition/file_format) support a minimum of 8-bit grayscale, which provides 2^8 or 256 levels of luminance per pixel. Some formats support 16-bit grayscale, which provides 2^16 or 65,536 levels of luminance.

Many image editing programs allow you to convert a color image to black and white, or grayscale. This process removes all color information, leaving only the luminance of each pixel. Since digital images are displayed using a combination of red, green, and blue ([RGB](https://techterms.com/definition/rgb)) colors, each pixel has three separate luminance values. Therefore, these three values must be combined into a single value when removing color from an image. There are several ways to do this. One option is to average all luminance values for each pixel. Another method involves keeping only the luminance values from the red, green, or blue channel. Some programs provide other custom grayscale conversion [algorithms](https://techterms.com/definition/algorithm) that allow you to generate a black and white image with the appearance you prefer.

---

In [digital photography](/wiki/Digital_photography "Grayscale (disambiguation)"), [computer-generated imagery](/wiki/Computer-generated_imagery "Computer-generated imagery"), and [colourimetry](/wiki/Colorimetry "Colorimetry"), a **greyscale** or [image](/wiki/Image "Image") is one in which the value of each [pixel](/wiki/Pixel "Pixel") is a single [sample](/wiki/Sample_(signal) "Sample (signal)") representing only an _amount_ of [light](/wiki/Light "Light"); that is, it carries only [intensity](/wiki/Luminous_intensity "Luminous intensity") information. Greyscale images, a kind of [black-and-white](/wiki/Black-and-white) or grey [monochrome](/wiki/Monochrome "Monochrome"), are composed exclusively of [shades of grey](/wiki/Shades_of_gray). The [contrast](/wiki/Contrast_(vision) "Contrast (vision)") ranges from [black](/wiki/Black "Black") at the weakest intensity to [white](/wiki/White "White") at the strongest.[[1]](#cite_note-1)

在数字摄影、计算机生成图像和比色学中，灰度或图像是指每个像素的值是仅代表光量的单个样本;也就是说，它只携带强度信息。灰度图像是一种黑白或灰色单色图像，完全由灰度阴影组成。对比范围从最弱的黑色到最强的白色

Greyscale images are distinct from one-bit bi-tonal black-and-white images, which, in the context of computer imaging, are images with only two [colours](/wiki/Color "Color"): black and white (also called _bilevel_ or _[binary images](/wiki/Binary_image "Binary image")_). Greyscale images have many shades of grey in between.

灰度图像不同于1位双色调黑白图像，后者在计算机成像中是只有两种颜色的图像:黑色和白色(也称为双层或二值图像)。灰度图像中间有许多灰度。

Greyscale images can be the result of measuring the intensity of light at each pixel according to a particular weighted combination of frequencies (or wavelengths), and in such cases they are [monochromatic](/wiki/Monochromatic_light "Monochromatic light") proper when only a single [frequency](/wiki/Frequency "Frequency") (in practice, a narrow band of frequencies) is captured. The frequencies can in principle be from anywhere in the [electromagnetic spectrum](/wiki/Electromagnetic_spectrum "Lab color space") (e.g. [infrared](/wiki/Infrared "Infrared"), [visible light](/wiki/Visible_spectrum "Visible spectrum"), [ultraviolet](/wiki/Ultraviolet "Ultraviolet"), etc.).

灰度图像可以是根据特定的加权频率(或波长)组合测量每个像素的光强度的结果，在这种情况下，当只捕获单一频率(实际上是窄带频率)时，它们是单色的。频率原则上可以来自电磁光谱的任何地方(例如，红外线、可见光、紫外线等)。

A [colourimetric](/wiki/Colorimetry "Black-and-white") (or more specifically [photometric](/wiki/Photometry_(optics) "Photometry (optics)")) greyscale image is an image that has a defined greyscale [colourspace](/wiki/Colorspace "Colorspace"), which maps the stored numeric sample values to the achromatic channel of a standard colourspace, which itself is based on measured properties of [human vision](/wiki/Visual_perception).

色度(或更具体地说，光度)灰度图像是一种具有定义的灰度颜色空间的图像，它将存储的数值样本值映射到标准颜色空间的消色差通道，而标准颜色空间本身是基于测量的人类视觉属性。

If the original colour image has no defined colourspace, or if the greyscale image is not intended to have the same human-perceived achromatic intensity as the colour image, then there is no unique [mapping](/wiki/Map_(mathematics) "Map (mathematics)") from such a colour image to a greyscale image.

如果原始彩色图像没有定义的颜色空间，或者如果灰度图像不打算具有与彩色图像相同的人类感知的消色差强度，那么就不存在从这种彩色图像到灰度图像的唯一映射。

Numerical representations
-------------------------

The intensity of a pixel is expressed within a given range between a minimum and a maximum, inclusive. This range is represented in an abstract way as a range from 0 (or 0%) (total absence, black) and 1 (or 100%) (total presence, white), with any fractional values in between. This notation is used in academic papers, but this does not define what "black" or "white" is in terms of [colourimetry](/wiki/Colorimetry "Colorimetry"). Sometimes the scale is reversed, as in [printing](/wiki/Printing "Printing") where the numeric intensity denotes how much ink is employed in [halftoning](/wiki/Halftoning "Halftoning"), with 0% representing the paper white (no ink) and 100% being a solid black (full ink).

In computing, although the greyscale can be computed through [rational numbers](/wiki/Rational_numbers "Rational numbers"), image pixels are usually [quantized](/wiki/Quantization_(signal_processing) "Quantization (signal processing)") to store them as unsigned integers, to reduce the required storage and computation. Some early greyscale monitors can only display up to sixteen different shades, which would be stored in [binary](/wiki/Binary_code "Binary code") form using 4 [bits](/wiki/Bit "Bit"). But today greyscale images (such as photographs) intended for visual display (both on screen and printed) are commonly stored with 8 bits per sampled pixel. This pixel [depth](/wiki/Color_depth "Color depth") allows 256 different intensities (i.e., shades of grey) to be recorded, and also simplifies computation as each pixel sample can be accessed individually as one full [byte](/wiki/Byte "Byte"). However, if these intensities were spaced equally in proportion to the amount of physical light they represent at that pixel (called a linear encoding or scale), the differences between adjacent dark shades could be quite noticeable as banding [artifacts](/wiki/Compression_artifact "Compression artifact"), while many of the lighter shades would be "wasted" by encoding a lot of perceptually-indistinguishable increments. Therefore, the shades are instead typically spread out evenly on a [gamma-compressed nonlinear scale](/wiki/Gamma_correction "Gamma correction"), which better approximates uniform perceptual increments for both dark and light shades, usually making these 256 shades enough (just barely) to avoid noticeable increments.

Converting colour to greyscale
------------------------------

Conversion of an arbitrary colour image to greyscale is not unique in general; different weighting of the colour channels effectively represent the effect of shooting black-and-white film with different-coloured [photographic filters](/wiki/Photographic_filter "Photographic filter") on the cameras.

Greyscale as single channels of multichannel colour images
----------------------------------------------------------

Colour images are often built of several stacked [colour channels](/wiki/Channel_(digital_image) "Channel (digital image)"), each of them representing value levels of the given channel. For example, [RGB](/wiki/RGB "RGB") images are composed of three independent channels for red, green and blue [primary colour](/wiki/Primary_color "Primary color") components; [CMYK](/wiki/CMYK "CMYK") images have four channels for cyan, magenta, yellow and black [ink plates](/wiki/Color_printing "Color printing"), etc.

Here is an example of colour channel splitting of a full RGB colour image. The column at left shows the isolated colour channels in natural colours, while at right there are their greyscale equivalences:

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/400px-Beyoglu_4671_tricolor.png)

The reverse is also possible: to build a full colour image from their separate greyscale channels. By mangling channels, using offsets, rotating and other manipulations, artistic effects can be achieved instead of accurately reproducing the original image.