---
title: image threshold - ostu
categories:
- Image Processing
tags:
- ostu
date: 2021/5/17 18:50:45
updated: 2021/5/17 20:00:13
---



> [www.labbookpages.co.uk](http://www.labbookpages.co.uk/software/imgProc/otsuThreshold.html)

![](http://www.labbookpages.co.uk/common/menuImages/histogram.png)

Converting a greyscale image to monochrome is a common image processing task. Otsu's method, named after its inventor Nobuyuki Otsu, is one of many binarization algorithms. This page describes how the algorithm works and provides a Java implementation, which can be easily ported to other languages. If you are in a hurry, jump to the [code](#java).

Many thanks to Eric Moyer who spotted a potential overflow when two integers are multiplied in the variance calculation. The example code has been updated with the integers cast to floats during the calculation.

* * *

Otsu Thresholding Explained
---------------------------

Otsu's thresholding method involves iterating through all the possible threshold values and calculating a measure of spread for the pixel levels each side of the threshold, i.e. the pixels that either fall in foreground or background. The aim is to find the threshold value where the sum of foreground and background spreads is at its minimum.

The algorithm will be demonstrated using the simple 6x6 image shown below. The histogram for the image is shown next to it. To simplify the explanation, only 6 greyscale levels are used.

![](http://www.labbookpages.co.uk/software/imgProc/files/otsuOrig.png)

A 6-level greyscale image and its histogram

The calculations for finding the foreground and background variances (the measure of spread) for a single threshold are now shown. In this case the threshold value is 3.

| 

![](http://www.labbookpages.co.uk/software/imgProc/files/otsuBG.png)

 | 

![](http://www.labbookpages.co.uk/software/imgProc/files/otsuEqns/bgExample.png)

 |
| 

![](http://www.labbookpages.co.uk/software/imgProc/files/otsuFG.png)

 | 

![](http://www.labbookpages.co.uk/software/imgProc/files/otsuEqns/fgExample.png)

 |

The next step is to calculate the 'Within-Class Variance'. This is simply the sum of the two variances multiplied by their associated weights.

![](http://www.labbookpages.co.uk/software/imgProc/files/otsuEqns/sumExample.png)

This final value is the 'sum of weighted variances' for the threshold value 3. This same calculation needs to be performed for all the possible threshold values 0 to 5. The table below shows the results for these calculations. The highlighted column shows the values for the threshold calculated above.

<table summary="Otsu Threshold Levels"><tbody><tr><td><strong>Threshold</strong></td><td><code>T=0</code></td><td><code>T=1</code></td><td><code>T=2</code></td><td><code>T=3</code></td><td><code>T=4</code></td><td><code>T=5</code></td></tr><tr><td></td><td><p><img class="" src="http://www.labbookpages.co.uk/software/imgProc/files/otsuT0.png"></p></td><td><p><img class="" src="http://www.labbookpages.co.uk/software/imgProc/files/otsuT1.png"></p></td><td><p><img class="" src="http://www.labbookpages.co.uk/software/imgProc/files/otsuT2.png"></p></td><td><p><img class="" src="http://www.labbookpages.co.uk/software/imgProc/files/otsuT3.png"></p></td><td><p><img class="" src="http://www.labbookpages.co.uk/software/imgProc/files/otsuT4.png"></p></td><td><p><img class="" src="http://www.labbookpages.co.uk/software/imgProc/files/otsuT5.png"></p></td></tr><tr><td><strong>Weight, Background</strong></td><td><code>W<sub>b</sub> = 0</code></td><td><code>W<sub>b</sub> = 0.222</code></td><td><code>W<sub>b</sub> = 0.4167</code></td><td><code>W<sub>b</sub> = 0.4722</code></td><td><code>W<sub>b</sub> = 0.6389</code></td><td><code>W<sub>b</sub> = 0.8889</code></td></tr><tr><td><strong>Mean, Background</strong></td><td><code>M<sub>b</sub> = 0</code></td><td><code>M<sub>b</sub> = 0</code></td><td><code>M<sub>b</sub> = 0.4667</code></td><td><code>M<sub>b</sub> = 0.6471</code></td><td><code>M<sub>b</sub> = 1.2609</code></td><td><code>M<sub>b</sub> = 2.0313</code></td></tr><tr><td><strong>Variance, Background</strong></td><td><code>σ<sup>2</sup><sub>b</sub> = 0</code></td><td><code>σ<sup>2</sup><sub>b</sub> = 0</code></td><td><code>σ<sup>2</sup><sub>b</sub> = 0.2489</code></td><td><code>σ<sup>2</sup><sub>b</sub> = 0.4637</code></td><td><code>σ<sup>2</sup><sub>b</sub> = 1.4102</code></td><td><code>σ<sup>2</sup><sub>b</sub> = 2.5303</code></td></tr><tr><td colspan="7"></td></tr><tr><td><strong>Weight, Foreground</strong></td><td><code>W<sub>f</sub> = 1</code></td><td><code>W<sub>f</sub> = 0.7778</code></td><td><code>W<sub>f</sub> = 0.5833</code></td><td><code>W<sub>f</sub> = 0.5278</code></td><td><code>W<sub>f</sub> = 0.3611</code></td><td><code>W<sub>f</sub> = 0.1111</code></td></tr><tr><td><strong>Mean, Foreground</strong></td><td><code>M<sub>f</sub> = 2.3611</code></td><td><code>M<sub>f</sub> = 3.0357</code></td><td><code>M<sub>f</sub> = 3.7143</code></td><td><code>M<sub>f</sub> = 3.8947</code></td><td><code>M<sub>f</sub> = 4.3077</code></td><td><code>M<sub>f</sub> = 5.000</code></td></tr><tr><td><strong>Variance, Foreground</strong></td><td><code>σ<sup>2</sup><sub>f</sub> = 3.1196</code></td><td><code>σ<sup>2</sup><sub>f</sub> = 1.9639</code></td><td><code>σ<sup>2</sup><sub>f</sub> = 0.7755</code></td><td><code>σ<sup>2</sup><sub>f</sub> = 0.5152</code></td><td><code>σ<sup>2</sup><sub>f</sub> = 0.2130</code></td><td><code>σ<sup>2</sup><sub>f</sub> = 0</code></td></tr><tr><td colspan="7"></td></tr><tr><td><strong>Within Class Variance</strong></td><td><code>σ<sup>2</sup><sub>W</sub> = 3.1196</code></td><td><code>σ<sup>2</sup><sub>W</sub> = 1.5268</code></td><td><code>σ<sup>2</sup><sub>W</sub> = 0.5561</code></td><td><code><strong>σ<sup>2</sup><sub>W</sub> = 0.4909</strong></code></td><td><code>σ<sup>2</sup><sub>W</sub> = 0.9779</code></td><td><code>σ<sup>2</sup><sub>W</sub> = 2.2491</code></td></tr></tbody></table>

It can be seen that for the threshold equal to 3, as well as being used for the example, also has the lowest sum of weighted variances. Therefore, this is the final selected threshold. All pixels with a level less than 3 are background, all those with a level equal to or greater than 3 are foreground. As the images in the table show, this threshold works well.

![](http://www.labbookpages.co.uk/software/imgProc/files/otsuResult.png)

Result of Otsu's Method

This approach for calculating Otsu's threshold is useful for explaining the theory, but it is computationally intensive, especially if you have a full 8-bit greyscale. The next section shows a faster method of performing the calculations which is much more appropriate for implementations.

* * *

A Faster Approach
-----------------

By a bit of manipulation, you can calculate what is called the _between class_ variance, which is far quicker to calculate. Luckily, the threshold with the maximum _between class_ variance also has the minimum _within class_ variance. So it can also be used for finding the best threshold and therefore due to being simpler is a much better approach to use.

![](http://www.labbookpages.co.uk/software/imgProc/files/otsuEqns/simplification.png)

The table below shows the different variances for each threshold value.

| **Threshold**              | `T=0`          | `T=1`          | `T=2`          | `T=3`              | `T=4`          | `T=5`          |
| -------------------------- | -------------- | -------------- | -------------- | ------------------ | -------------- | -------------- |
|                            |                |                |                |                    |                |                |
| **Within Class Variance**  | `σ2W = 3.1196` | `σ2W = 1.5268` | `σ2W = 0.5561` | `**σ2W = 0.4909**` | `σ2W = 0.9779` | `σ2W = 2.2491` |
| **Between Class Variance** | `σ2B = 0`      | `σ2B = 1.5928` | `σ2B = 2.5635` | `**σ2B = 2.6287**` | `σ2B = 2.1417` | `σ2B = 0.8705` |

* * *

Java Implementation
-------------------

A simple demo program that uses the Otsu threshold is linked to below.

[demo download](http://www.labbookpages.co.uk/software/imgProc/files/otsuDemo.tar.gz)

The important part of the algorithm is shown here. The input is an array of bytes, `srcData` that stores the greyscale image.

File Excerpt: `OtsuThresholder.java`

```java
int ptr = 0;
while (ptr < srcData.length) {
   int h = 0xFF & srcData[ptr];
   histData[h] ++;
   ptr ++;
}


int total = srcData.length;

float sum = 0;
for (int t=0 ; t<256 ; t++) sum += t * histData[t];

float sumB = 0;
int wB = 0;
int wF = 0;

float varMax = 0;
threshold = 0;

for (int t=0 ; t<256 ; t++) {
   wB += histData[t];               
   if (wB == 0) continue;

   wF = total - wB;                 
   if (wF == 0) break;

   sumB += (float) (t * histData[t]);

   float mB = sumB / wB;            
   float mF = (sum - sumB) / wF;    

   
   float varBetween = (float)wB * (float)wF * (mB - mF) * (mB - mF);

   
   if (varBetween > varMax) {
      varMax = varBetween;
      threshold = t;
   }
}
```

* * *

Examples
--------

Here are a number of examples of the Otsu Method in use. It works well with images that have a bi-modal histogram (those with two distinct regions).

<table summary="Otsu Threshold Examples"><tbody><tr><th>Greyscale Image</th><th>Binary Image</th><th>Histogram</th></tr><tr><td><p><img class="" src="http://www.labbookpages.co.uk/software/imgProc/files/otsuExamples/harewood.jpg"></p></td><td><p><img class="" src="http://www.labbookpages.co.uk/software/imgProc/files/otsuExamples/harewood_BW.png"></p></td><td><p><img class="" src="http://www.labbookpages.co.uk/software/imgProc/files/otsuExamples/harewood_hist.png"></p></td></tr><tr><td><p><img class="" src="http://www.labbookpages.co.uk/software/imgProc/files/otsuExamples/nutsBolts.jpg"></p></td><td><p><img class="" src="http://www.labbookpages.co.uk/software/imgProc/files/otsuExamples/nutsBolts_BW.png"></p></td><td><p><img class="" src="http://www.labbookpages.co.uk/software/imgProc/files/otsuExamples/nutsBolts_hist.png"></p></td></tr><tr><td><p><img class="" src="http://www.labbookpages.co.uk/software/imgProc/files/otsuExamples/snow.jpg"></p></td><td><p><img class="" src="http://www.labbookpages.co.uk/software/imgProc/files/otsuExamples/snow_BW.png"></p></td><td><p><img class="" src="http://www.labbookpages.co.uk/software/imgProc/files/otsuExamples/snow_hist.png"></p></td></tr></tbody></table>

