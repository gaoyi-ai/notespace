---
title: image threshold - ostu - opencv
categories:
- Image Processing
tags:
- ostu
date: 2021/5/17 18:50:45
updated: 2021/5/17 20:00:13
---



> [learnopencv.com](https://learnopencv.com/otsu-thresholding-with-opencv/)

In this post, we will examine Otsu’s method for **automatic image thresholding**.

What is Image Thresholding?
---------------------------

![](https://learnopencv.com/wp-content/uploads/2015/02/opencv-threshold-tutorial-1024x341.jpg)

[Image thresholding](https://learnopencv.com/opencv-threshold-python-cpp/ "OpenCV Threshold ( Python , C++ )") is used to binarize the image based on pixel intensities. The input to such thresholding algorithm is usually a grayscale image and a threshold. The output is a binary image.

If the intensity of a pixel in the input image is greater than a threshold, the corresponding output pixel is marked as white (foreground), and if the input pixel intensity intensity is less than or equal to the threshold, the output pixel location is marked black (background).

Image thresholding is used in many applications as a pre-processing step. For example, you may use it in medical image processing to reveal tumor in a mammogram or localize a natural disaster in satellite images.

A problem with simple thresholding is that you have to manually specify the threshold value. We can manually check how good a threshold is by trying different values but it is tedious and it may break down in the real world.

So, we need a way to automatically determine the threshold. The Otsu’s technique named after its creator Nobuyuki Otsu is a good example of auto thresholding.

Before we jump into the details of the technique let’s understand how image thresholding relates to image segmentation.

Image Tresholding vs. Image Segmentation
----------------------------------------

[Image segmentation](https://learnopencv.com/image-segmentation/ "Image Segmentation") refers to the class of algorithms that partition the image into different segments or groups of pixels.

In that sense, image thresholding is the simplest kind of image segmentation because it partitions the image into two groups of pixels — white for foreground, and black for background.

The figure below shows different types of segmentation algorithms:

![](https://learnopencv.com/wp-content/uploads/2020/06/segmentation_methods-1.png)Pic. 1: Highlevel classification of image segmentation approaches

I've partnered with OpenCV.org to bring you official courses in **Computer Vision**, **Machine Learning**, and **AI**! Sign up now and take your skills to the next level!

You can see image thresholding (shown using a red bounding box) is a type of image segmentation.

Image thresholding be future sub-divied into the **local** and **global** image tresholding algorithms.

In global thresholding, a single threshold is used globally, for the whole image.

In local thresholding, some characteristics of some local image areas (e.g. the local contrast) may be used to choose a different threshold for different parts of the image.

Otsu’s method is a global image thresholding algorithm.

Otsu’s Thresholding Concept
---------------------------

Automatic global thresholding algorithms usually have following steps.

1.  Process the input image
2.  Obtain image histogram (distribution of pixels)
3.  Compute the threshold value ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-58f18d11e5ffdd11dd9095c427922c8b_l3.png)
4.  Replace image pixels into white in those regions, where saturation is greater than ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-58f18d11e5ffdd11dd9095c427922c8b_l3.png) and into the black in the opposite cases.

Usually, different algorithms differ in step 3.

Let’s understand the idea behind Otsu’s approach. The method processes image histogram, segmenting the objects by minimization of the variance on each of the classes. Usually, this technique produces the appropriate results for bimodal images. The histogram of such image contains two clearly expressed peaks, which represent different ranges of intensity values.

The core idea is separating the image histogram into two clusters with a threshold defined as a result of minimization the weighted variance of these classes denoted by ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-35c534b18ad6b15481dff67ace77e491_l3.png).

The whole computation equation can be described as: ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-964b715a54ea69e289e94d7d8257cb3c_l3.png), where ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-c50faa13ce0a4e984cfe33f723c31818_l3.png) are the probabilities of the two classes divided by a threshold ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-6b97bb0f65c75b6cc0fba1868749478d_l3.png), which value is within the range from 0 to 255 inclusively.

As it was shown in the [Otsu’s paper](https://www.semanticscholar.org/paper/A-Threshold-Selection-Method-from-Gray-Level-Otsu/1d4816c612e38dac86f2149af667a5581686cdef) there are actually two options to find the threshold. The first is to minimize the within-class variance defined above ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-35c534b18ad6b15481dff67ace77e491_l3.png), the second is to maximize the between-class variance using the expression below:  
![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-5dc504f8972333b4448fbdf1247a7f50_l3.png), where ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-6576b2f1aac7a51c593ecf86b70cd09f_l3.png) is a mean of class ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-8511b1f6cf9db17d46ddabb67bac99f5_l3.png).  

It should be noted that the image can presented as intensity function ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-c02544e5ef1c7210ee2311498096bd12_l3.png), which values are gray-level. The quantity of the pixels with a specified gray-level ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-8511b1f6cf9db17d46ddabb67bac99f5_l3.png) denotes by ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-8511b1f6cf9db17d46ddabb67bac99f5_l3.png). The general number of pixels in the image is ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-a63eb5ff0272d3119fa684be6e7acce8_l3.png).  
Thus, the probability of gray-level ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-8511b1f6cf9db17d46ddabb67bac99f5_l3.png) occurrence is:  
![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-949a4c38ed92cfcbd4bb5c834b37e0a9_l3.png).

The pixel intensity values for the ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-782c65cbd411fb8862688afc92bc1eea_l3.png) are in ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-9f2b38c3733b510951df3c7085d6f7d0_l3.png) and for ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-e7a76f05c39a95697f83e79bbd4b7f39_l3.png) are in ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-af5b206e0c8c65a73ec70255bddbc180_l3.png), where ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-ca2b1f375fecf1f1d2741f9a14018727_l3.png) is the maximum pixel value (255).

The next phase is to obtain the means for ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-08d1f29fa9c0981e916619b6c6bc7eee_l3.png), which are denoted by ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-8baeaaeef43c1ea5236aeae84565371e_l3.png) appropriately:  
![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-4c5717e9496c09d32b0641e45454136b_l3.png),

![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-aa45d3de447d7f6b40a02498a5e1e765_l3.png)

Now let’s remember the above equation of the within-classes weighted variance. We will find the rest of its components (![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-f8534afb97f596575497ba27607ccb7f_l3.png)) mixing all the obtained above ingredients:  
![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-c3a8cf57ef3acb6023ea5b8952f67698_l3.png),

![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-30f2e763ba799268a4dedb43cbfae13a_l3.png).

It should be noted that if the threshold was chosen incorrectly the variance of some class would be large. To get the total variance we simply need to summarize the within class and between-class variances: ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-ccbcddff746fa3db93bf9812a7950348_l3.png), where ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-272af786ffbd0274efb607e6acf895b1_l3.png). The total variance of the image (![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-8f448e52175fab72ef748e7bbc42c61d_l3.png)) does not depend on the threshold.

Thus, the general algorithm’s pipeline for the between-class variance maximization option can be represented in the following way:

1.  calculate the histogram and intensity level probabilities
2.  initialize ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-ebd6d2b5bba76b600eb808211f604a03_l3.png)
3.  iterate over possible thresholds: ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-99a5a623726b2f8a6ff65800699d122d_l3.png)
4.  the final threshold is the maximum ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-d22ef6a5938915fd71554b9ceefb2fda_l3.png) value

Otsu’s Binarization Application
-------------------------------

You could ask what is the real case where Otsu’s approach could be applied? Despite the fact that the method was announced in 1979, it still forms the basis of some complex solutions. Let’s take as an example an urgent task of robotic mapping, concluding in accurate spatial representation of any environment covered by a robot. This information is key for a properly robot autonomous functioning. The non-trivial case is underwater surface mapping described in the article [“An improved Otsu threshold segmentation method for underwater simultaneous localization and mapping-based navigation”](https://www.semanticscholar.org/paper/An-Improved-Otsu-Threshold-Segmentation-Method-for-Yuan-Mart%C3%ADnez/25d839ea8101ffcb06259a9882848f0f8983b06f).

The authors provide improved Otsu’s method as one of the approaches for estimation of the underwater landmark localization. Advantages of such an approach are precise real-time segmentation of underwater features and proven performance in comparison with threshold segmentation methods. Let’s view its idea more precisely using the provided in the article side-scan sonar (SSS) shipwreck image example. Modern SSS systems can cover large areas of the sea bottom performing two-dimensional realistic images. Thus, their background contains the regions of sludge and aquatic animals in form of spots usually <= 30 pixels (this further will be used as a parameter denoted by ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-8b87ab7d2390a7fc32d54e2f303a33af_l3.png)).

![](https://learnopencv.com/wp-content/uploads/2020/06/sss_image.png)Pic. 2: SSS sea bottom image

They distort correct image processing due to the similarity of their gray level to certain zones of foreground objects. Classical Otsu’s technique results in the segmented image with these artifacts as we can see below:

![](https://learnopencv.com/wp-content/uploads/2020/06/otsu_sss.png)Pic. 3: SSS sea bottom image

The method based on Otsu’s binarization was developed to deal with this spot challenge constraining the search range of the appropriate segmentation threshold for foreground object division. The improved Otsu’s method pipeline is the following:

1.  Obtain Otsu’s threshold ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-58f18d11e5ffdd11dd9095c427922c8b_l3.png)
2.  Apply [Canny edge detection](https://en.wikipedia.org/wiki/Canny_edge_detector) to compute ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-8b87ab7d2390a7fc32d54e2f303a33af_l3.png)
3.  if ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-74c8b1aec33fcfb554e48feb5cadb923_l3.png) compute final ![](https://learnopencv.com/wp-content/ql-cache/quicklatex.com-58f18d11e5ffdd11dd9095c427922c8b_l3.png) value.

The result is clear wrecked ship separation from the background:

![](https://learnopencv.com/wp-content/uploads/2020/06/improved_otsu.png)Pic. 4: Improved Otsu’s result

Method Implementation
---------------------

Let’s implement Otsu’s method on our own. It will look similar to [`threshold_otsu`](https://github.com/scikit-image/scikit-image/blob/master/skimage/filters/thresholding.py#L237) solution from the scikit-learn library, so feel free to use it as a reference. The function is built around maximization of the between-class variance (as we remember there is also minimization option) as OpenCV [`getThreshVal_Otsu`](https://github.com/opencv/opencv/blob/master/modules/imgproc/src/thresh.cpp#L1129).

The below image is used as input:

![](https://learnopencv.com/wp-content/uploads/2020/06/boat_initial-1024x685.jpg)Pic. 5: Input image

Now let’s go through the following necessary points in order to achieve the result.

1. Read Image.  
First, we need to read image in a grayscale mode and its possible improvement with a Gaussian blur in order to reduce the noise:

**Download Code** To easily follow along this tutorial, please download code by clicking on the button below. It's FREE!

C++
---

```
// Include Libraries
#include <iostream>
#include <opencv2/opencv.hpp>
#include <opencv2/imgproc.hpp>

using namespace std;
using namespace cv;

int main(){

	// read the image in BGR format
	Mat testImage = imread("boat.jpg", 0);


```

Python
------

```
# Read the image in a grayscale mode
image = cv2.imread(img_title, 0)

# Apply GaussianBlur to reduce image noise if it is required
if is_reduce_noise:
   image = cv2.GaussianBlur(image, (5, 5), 0)


```

In our case the image is quite qualitative, hence we set `is_reduce_noise` flag to `False`.

2. Calculate the Otsu’s threshold.  
The below code block represents the main algorithm computation part concluding in the threshold obtaining. The precise explanations of the lines can be found in the comments:

C++
---

```
int bins_num = 256;

// Get the histogram
long double histogram[256];

// initialize all intensity values to 0
for(int i = 0; i < 256; i++)
    histogram[i] = 0;

// calculate the no of pixels for each intensity values
for(int y = 0; y < testImage.rows; y++)
    for(int x = 0; x < testImage.cols; x++)
        histogram[(int)testImage.at<uchar>(y,x)]++;

// Calculate the bin_edges
long double bin_edges[256];
bin_edges[0] = 0.0;
long double increment = 0.99609375;
for(int i = 1; i < 256; i++)
    bin_edges[i] = bin_edges[i-1] + increment;

// Calculate bin_mids
long double bin_mids[256];
for(int i = 0; i < 256; i++)
	bin_mids[i] = (bin_edges[i] + bin_edges[i+1])/2;

// Iterate over all thresholds (indices) and get the probabilities weight1, weight2
long double weight1[256];
weight1[0] = histogram[0];
for(int i = 1; i < 256; i++)
	weight1[i] = histogram[i] + weight1[i-1];

int total_sum=0;
for(int i = 0; i < 256; i++)
    total_sum = total_sum + histogram[i];
long double weight2[256];
weight2[0] = total_sum;
for(int i = 1; i < 256; i++)
	weight2[i] = weight2[i-1] - histogram[i - 1];

// Calculate the class means: mean1 and mean2
long double histogram_bin_mids[256];
for(int i = 0; i < 256; i++)
	histogram_bin_mids[i] = histogram[i] * bin_mids[i];

long double cumsum_mean1[256];
cumsum_mean1[0] = histogram_bin_mids[0];
for(int i = 1; i < 256; i++)
	cumsum_mean1[i] = cumsum_mean1[i-1] + histogram_bin_mids[i];

long double cumsum_mean2[256];
cumsum_mean2[0] = histogram_bin_mids[255];
for(int i = 1, j=254; i < 256 && j>=0; i++, j--)
	cumsum_mean2[i] = cumsum_mean2[i-1] + histogram_bin_mids[j];

long double mean1[256];
for(int i = 0; i < 256; i++)
	mean1[i] = cumsum_mean1[i] / weight1[i];

long double mean2[256];
for(int i = 0, j = 255; i < 256 && j >= 0; i++, j--)
	mean2[j] = cumsum_mean2[i] / weight2[j];

// Calculate Inter_class_variance
long double Inter_class_variance[255];
long double dnum = 10000000000;
for(int i = 0; i < 255; i++)
	Inter_class_variance[i] = ((weight1[i] * weight2[i] * (mean1[i] - mean2[i+1])) / dnum) * (mean1[i] - mean2[i+1]);	

// Maximize interclass variance
long double maxi = 0;
int getmax = 0;
for(int i = 0;i < 255; i++){
	if(maxi < Inter_class_variance[i]){
		maxi = Inter_class_variance[i];
		getmax = i;
	}
}

cout << "Otsu's algorithm implementation thresholding result: " << bin_mids[getmax];


```

Python
------

```
# Set total number of bins in the histogram
bins_num = 256

# Get the image histogram
hist, bin_edges = np.histogram(image, bins=bins_num)

# Get normalized histogram if it is required
if is_normalized:
    hist = np.divide(hist.ravel(), hist.max())

# Calculate centers of bins
bin_mids = (bin_edges[:-1] + bin_edges[1:]) / 2.

# Iterate over all thresholds (indices) and get the probabilities w1(t), w2(t)
weight1 = np.cumsum(hist)
weight2 = np.cumsum(hist[::-1])[::-1]

# Get the class means mu0(t)
mean1 = np.cumsum(hist * bin_mids) / weight1
# Get the class means mu1(t)
mean2 = (np.cumsum((hist * bin_mids)[::-1]) / weight2[::-1])[::-1]

inter_class_variance = weight1[:-1] * weight2[1:] * (mean1[:-1] - mean2[1:]) ** 2

# Maximize the inter_class_variance function val
index_of_max_val = np.argmax(inter_class_variance)

threshold = bin_mids[:-1][index_of_max_val]
print("Otsu's algorithm implementation thresholding result: ", threshold)


```

3. Results.  
The execution result is:

`Otsu's algorithm implementation thresholding result: 131.982421875`

Now we better understand the algorithm’s essence after its whole pipeline implementation. Let’s explore how we can obtain the same result using the already implemented [`threshold`](https://docs.opencv.org/master/d7/d1b/group__imgproc__misc.html#gae8a4a146d1ca78c626a53577199e9c57) method from the OpenCV library.

1. Read Image.  
The first step is the same – image loading in a grayscale mode with a possible noise reduction. Let’s visualize the results of the preprocessed image and its histogram:

![](https://learnopencv.com/wp-content/uploads/2020/06/processed_img-1-1024x685.jpg)Pic. 6: Preprocessed image

In the below image histogram we can see clearly expressed mono peak and its near region and slightly expressed peak at the beginning of the scale:

![](https://learnopencv.com/wp-content/uploads/2020/06/image_hist.png)Pic. 7. Image histogram

2. Calculate the Otsu’s threshold.  
To apply Otsu’s technique we simply need to use OpenCV `threshold` function with set [`THRESH_OTSU`](https://docs.opencv.org/master/d7/d1b/group__imgproc__misc.html#ggaa9e58d2860d4afa658ef70a9b1115576a95251923e8e22f368ffa86ba8bce87ff) flag:

C++
---

```
Mat testImage = imread("boat.jpg", 0);

Mat dst;
double thresh = 0;
double maxValue = 255;

long double thres = cv::threshold(testImage,dst, thresh, maxValue, THRESH_OTSU);

cout << "Otsu Threshold : " << thres <<endl;


```

Python
------

```
# Applying Otsu's method setting the flag value into cv.THRESH_OTSU.
# Use a bimodal image as an input.
# Optimal threshold value is determined automatically.
otsu_threshold, image_result = cv2.threshold(
    image, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU,
)
print("Obtained threshold: ", otsu_threshold)


```

3. Results.  
The threshold value is near the obtained above in a handmade case (131.98):  

```
Obtained threshold:  132.0

```

Now let’s view the final binarized image after Otsu’s method application:

![](https://learnopencv.com/wp-content/uploads/2020/06/otsu_result-1024x685.png) Pic. 8: Otsu’s method result

We can clearly observe that the background and the main objects in the picture were separated. Let’s draw a histogram for the obtained binarized image:

![](https://learnopencv.com/wp-content/uploads/2020/06/image_hist_res.png)Pic. 9: Otsu’s method result

As we can see, image pixels are now separated into 2 clusters with intensities values 0 and 255.