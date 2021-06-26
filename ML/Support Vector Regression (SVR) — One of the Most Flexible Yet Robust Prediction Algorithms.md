---
title: Support Vector Regression
categories:
- ML
- SVR
tags:
- svr
date: 2021/6/25
---



> [towardsdatascience.com](https://towardsdatascience.com/support-vector-regression-svr-one-of-the-most-flexible-yet-robust-prediction-algorithms-4d25fbdaca60)

Machine Learning
----------------

A visual explanation of SVR with Python implementation examples
---------------------------------------------------------------

![img](https://miro.medium.com/max/1600/1*hULW-5rjkYKmd2R40-YYUA.png)

Machine Learning is making huge leaps forward, with an increasing number of algorithms enabling us to solve complex real-world problems.

This story is part of a deep dive series explaining the mechanics of Machine Learning algorithms. In addition to giving you an understanding of how ML algorithms work, it also provides you with Python examples to build your own ML models.

*   The category of algorithms that SVR belongs to
*   An intuitive explanation of how SVR works
*   A few examples of how to build SVR models in Python

While you may not be familiar with SVR, chances are you have previously heard about Support Vector Machines (SVM). SVMs are most frequently used for solving **classification** problems, which fall under the supervised machine learning category.

With small adaptations, however, SVMs can also be used for other types of problems such as:

*   **Clustering** (unsupervised learning) through the use of Support Vector Clustering algorithm (SVC)
*   **Regression** (supervised learning) through the use of Support Vector Regression algorithm (SVR)

These use cases utilize the same idea behind support vectors, but each has a slightly different implementation. This enables us to use these algorithms across different categories of machine learning.

<iframe src="https://cdn.embedly.com/widgets/media.html?src=https%3A%2F%2Fplotly.com%2F%7ESolClover%2F40.embed%3Fautosize%3Dtrue&amp;display_name=Plotly&amp;url=http%3A%2F%2Fchart-studio.plotly.com%2F%7ESolClover%2F40%2F&amp;image=http%3A%2F%2Fchart-studio.plotly.com%2Fstatic%2Fwebapp%2Fimages%2Fplotly-logo.8d56a320dbb8.png&amp;key=a19fcc184b9711e1b4764040d3dc5c07&amp;type=text%2Fhtml&amp;schema=plotly" allowfullscreen="" title="sunburst made by Solclover | plotly" class="et fh fd ka v" scrolling="auto" width="600" height="400" frameborder="0"></iframe>

> Side note, while I have put Neural Networks in a category of their own within the supervised learning branch, they can be used to solve a wide range of problems including classification and regression. The above chart is **interactive** so try clicking on different categories 👆.

In general, you can use SVR to solve the same problems you would use linear regression for. Unlike linear regression, though, SVR also allows you to model non-linear relationships between variables and provides the flexibility to adjust the model's robustness by tuning hyperparameters.

Before we look at the regression side, let us familiarize ourselves with SVM usage for classification. This will aid our understanding of how the algorithm has been adapted for regression.

Support Vector Machines (SVM)
-----------------------------

Let’s assume we have a set of points that belong to two separate classes. We want to separate those two classes in a way that allows us to correctly assign any future new points to one class or the other.

SVM algorithm achieves that by finding a hyperplane that separates the two classes with the highest possible margin. The points that end up on the margins are known as support vectors (see illustration below).

Sometimes, however, it may not be impossible to separate the two classes correctly, or you may have some outliers which fall inside the margin. Any such **misclassified** points or points **inside the margin** would be penalized. This is where the “slack” value comes in, denoted by a greek letter ξ (xi, pronounced “ksi”). You may ignore it for now, but it will become more relevant when we look at support vector regression.

![img](https://miro.medium.com/max/1255/1*pD-snE2N9QXv0M26jx63Hg.png)

Separating the two classes of points with the SVM algorithm. Image by [author.](https://solclover.medium.com/)

In the graph above, we have a class of blue points and a class of green points. We try a few different hyperplanes to separate the points with the following results:

*   H1 was not able to correctly separate the classes. Hence, it is not a viable solution.
*   H2 separated the classes correctly, but the margin between the hyperplane and the nearest point is quite small. Hence, there is a high chance of incorrectly classifying any future new points. E.g., the new grey point (x1=3, x2=3.6) would be assigned to the green class by the algorithm when it is obvious that it should belong to the blue class instead.
*   H3 separates the two classes with the highest possible margin, making the model a lot more robust. This ensures that we are more likely to assign future new points to the right class correctly. _Note, we did have one outlier that has fallen inside the margin._

Support Vector Regression (SVR)
-------------------------------

In general, SVR is quite similar to SVM, but there are some notable differences:

*   SVR has an additional tunable parameter **ε (epsilon)**. The value of epsilon determines the width of the tube around the estimated function (hyperplane). Points that fall inside this tube are considered as correct predictions and are not penalized by the algorithm.
*   The support vectors are the points that fall outside the tube rather than just the ones at the margin, as seen in the SVM classification example.
*   Finally, “slack” (ξ ) measures the distance to points outside the tube, and you can control how much you care about it by tuning a regularization parameter C (more about it in the Python section below).

![img](https://miro.medium.com/max/1300/1*_IWq-IN-R7sFy0Snv2iJdg.png)

Support Vector Regression — hyperplane line together with boundary lines defined by +-epsilon. Image by [author](https://solclover.medium.com/).

A simple way to think about SVR is to imagine a tube with an estimated function (hyperplane) in the middle and boundaries on either side defined by ε. The algorithm's goal is to minimize the error by identifying a function that puts more of the original points inside the tube while at the same time reducing the “slack.”

While the above explanations focus on linear examples, SVM and SVR algorithms can also handle non-linear situations through a kernel trick. A kernel is a function (you can choose between a few different ones) that takes the original non-linear problem and transforms it into a linear one, which is then handled by the algorithm in a higher-dimensional space.

While I will not go into the details about kernel functions, you will see how to pick between different ones in the Python examples section below. However, if you are interested in how kernel functions work, you can refer to my SVM and RBF kernel story here:

[svm-classifier-and-rbf-kernel-how-to-make-better-models-in-python](https://towardsdatascience.com/svm-classifier-and-rbf-kernel-how-to-make-better-models-in-python-73bb4914af5b)

# How to build SVR models in Python

Now that we have some background about SVRs, it is time to build a couple of Python prediction models. Similar to other regression algorithms analyzed in my previous stories, we will utilize the following data and Python packages:

*   [House price data from Kaggle](https://www.kaggle.com/quantbruce/real-estate-price-prediction?select=Real+estate.csv)
*   Scikit-learn library to build SVR and linear regression models
*   Plotly library for visualizations
*   Pandas and Numpy

Setup
-----

First, we import the required libraries.\

```python
import pandas as pd # for data manipulation
import numpy as np # for data manipulation
from sklearn.linear_model import LinearRegression # for building a linear regression model
from sklearn.svm import SVR # for building support vector regression model
import plotly.graph_objects as go # for data visualization
import plotly.express as px # for data visualization
```

Next, we download and ingest the data that we will use to build our SVR and linear regression models. 
_(source:_ [_https://www.kaggle.com/quantbruce/real-estate-price-prediction?select=Real+estate.csv_](https://www.kaggle.com/quantbruce/real-estate-price-prediction?select=Real+estate.csv)_)_

```python
# Read in data
df = pd.read_csv('Real estate.csv', encoding='utf-8')
# Print Dataframe
df
```

![img](https://miro.medium.com/max/2500/1*Jru5s-K-zFf9z5EWlQXYIA.png)

House price data from [Kaggle](https://www.kaggle.com/quantbruce/real-estate-price-prediction?select=Real+estate.csv). Image by [author](https://solclover.medium.com/).

SVR vs. simple linear regression — 1 independent variable
---------------------------------------------------------

We will take ‘X3 distance to the nearest MRT station’ as our input (independent) variable and ‘Y house price of unit area’ as our output (dependent) variable and create a scatterplot to visualize the data.

```python
# Create a scatter plot
fig = px.scatter(df, x=df['X3 distance to the nearest MRT station'], y=df['Y house price of unit area'], 
                 opacity=0.8, color_discrete_sequence=['black'])

# Change chart background color
fig.update_layout(dict(plot_bgcolor = 'white'))

# Update axes lines
fig.update_xaxes(showgrid=True, gridwidth=1, gridcolor='lightgrey', 
                 zeroline=True, zerolinewidth=1, zerolinecolor='lightgrey', 
                 showline=True, linewidth=1, linecolor='black')

fig.update_yaxes(showgrid=True, gridwidth=1, gridcolor='lightgrey', 
                 zeroline=True, zerolinewidth=1, zerolinecolor='lightgrey', 
                 showline=True, linewidth=1, linecolor='black')

# Set figure title
fig.update_layout(title=dict(text="House Price Based on Distance from the Nearest MRT", 
                             font=dict(color='black')))

# Update marker size
fig.update_traces(marker=dict(size=3))

fig.show()
```

![img](https://miro.medium.com/max/2303/1*g4_xsJ2aXpcItD1rhaUqLw.png)

Scatterplot of X and Y. Image by [author](https://solclover.medium.com/).

As we can see from the graph above, there is a clear relationship between the two variables, with house price (per unit area) decreasing as the distance from the nearest MRT station increases.

Let us now fit the two models. Note, we use the following hyperparameter values for the SVR model: `epsilon = 10, C = 1.` As explained before, epsilon defines the width of the tube around the hyperplane. Meanwhile, regularization parameter C allows us to assign the weight to “slack,” telling the algorithm how much we care about the error.

Also, we use the default ‘rbf’ (radial-basis function) kernel. Other choices available in sklearn’s SVR package are: ‘linear’, ‘poly’, ‘sigmoid’, and ‘precomputed’.

```python
# ------- Select variables -------
# Note, we need X to be a 2D array, hence reshape
X=df['X3 distance to the nearest MRT station'].values.reshape(-1,1)
y=df['Y house price of unit area'].values

# ------- Linear regression -------
model1 = LinearRegression()
lr = model1.fit(X, y)

# ------- Support Vector regression -------
model2 = SVR(kernel='rbf', C=1, epsilon=10) # set kernel and hyperparameters
svr = model2.fit(X, y)

# ------- Predict a range of values based on the models for visualization -------
# Create 100 evenly spaced points from smallest X to largest X
x_range = np.linspace(X.min(), X.max(), 100)

# Predict y values for our set of X values
y_lr = model1.predict(x_range.reshape(-1, 1)) # Linear regression
y_svr = model2.predict(x_range.reshape(-1, 1)) # SVR
```

With fitting done, let’s visualize the two models.

```python
# Create a scatter plot
fig = px.scatter(df, x=df['X3 distance to the nearest MRT station'], y=df['Y house price of unit area'], 
                 opacity=0.8, color_discrete_sequence=['black'])

# Add a best-fit line
fig.add_traces(go.Scatter(x=x_range, y=y_lr, name='Linear Regression', line=dict(color='limegreen')))
fig.add_traces(go.Scatter(x=x_range, y=y_svr, name='Support Vector Regression', line=dict(color='red')))
fig.add_traces(go.Scatter(x=x_range, y=y_svr+10, name='+epsilon', line=dict(color='red', dash='dot')))
fig.add_traces(go.Scatter(x=x_range, y=y_svr-10, name='-epsilon', line=dict(color='red', dash='dot')))

# Change chart background color
fig.update_layout(dict(plot_bgcolor = 'white'))

# Update axes lines
fig.update_xaxes(showgrid=True, gridwidth=1, gridcolor='lightgrey', 
                 zeroline=True, zerolinewidth=1, zerolinecolor='lightgrey', 
                 showline=True, linewidth=1, linecolor='black')

fig.update_yaxes(showgrid=True, gridwidth=1, gridcolor='lightgrey', 
                 zeroline=True, zerolinewidth=1, zerolinecolor='lightgrey', 
                 showline=True, linewidth=1, linecolor='black')

# Set figure title
fig.update_layout(title=dict(text="House Price Based on Distance from the Nearest MRT with Model Predictions (epsilon=10, C=1)", 
                             font=dict(color='black')))
# Update marker size
fig.update_traces(marker=dict(size=3))

fig.show()
```

![img](https://miro.medium.com/max/2433/1*-4yqLW560uchZ70juqpmOA.png)

Linear regression and SVR model comparison (C=1). Image by [author](https://solclover.medium.com/).

Visually we can see how support vector regression is much more flexible compared to linear regression. SVR can capture the slope change as the increase in the distance from the nearest MRT has a diminishing effect on a house's price.

Meanwhile, simple linear regression has only one slope parameter, meaning that it maintains the curve's steepness throughout, overestimating the relationship at higher distance values.

Let us now adjust the hyperparameter C, increasing it to 1000, and see how that affects the SVR model. Note, the Python code we use is identical to the one above apart from `C=1000` instead of `C=1`. Here are the results visualized:

![img](https://miro.medium.com/max/2413/1*xS524nCrLXcRdii_bTcQhg.png)

Linear regression and SVR model comparison (C=1000). Image by [author](https://solclover.medium.com/).

You will note how increasing C to 1000 created a much more “wavy” best-fit line. By increasing C's value, we told the algorithm that we care a lot more about minimizing the “slack.” The algorithm responded to this by capturing more points inside the epsilon-tube.

While increasing C allows us to fit the data better, it also makes our model less robust, risking overfitting. Hence, it is best to be cautious when tuning hyperparameters and split the data into training and testing datasets so you can evaluate your model with unseen data.

SVR vs. multiple linear regression — 2 independent variables
------------------------------------------------------------

Let’s now take a look at another example using multiple independent variables.

We start by creating a 3D scatterplot that contains the same data as before plus an additional independent variable — ‘X2 house age’.

```python
# Create a 3D scatter plot
fig = px.scatter_3d(df, x=df['X3 distance to the nearest MRT station'], y=df['X2 house age'], z=df['Y house price of unit area'], 
                 opacity=0.8, color_discrete_sequence=['black'])

# Set figure title
fig.update_layout(title_text="Scatter 3D Plot",
                  scene = dict(xaxis=dict(backgroundcolor='white',
                                          color='black',
                                          gridcolor='lightgrey'),
                               yaxis=dict(backgroundcolor='white',
                                          color='black',
                                          gridcolor='lightgrey'
                                          ),
                               zaxis=dict(backgroundcolor='white',
                                          color='black', 
                                          gridcolor='lightgrey')))

# Update marker size
fig.update_traces(marker=dict(size=3))

fig.show()
```

![img](https://miro.medium.com/max/2718/1*CYz5D9PMz87u1-Nss_12BQ.png)

Observations visualized with Plotly 3D scatterplot. Image by [author](https://solclover.medium.com/).

We can see from the graph that the house price tends to increase as the house age decreases. While the relationship between X2 and Y is not as strong as the one between X3 and Y, it will still improve our model.

Let us now fit the two models and create a prediction plane. This time we use the following hyperparameters for the SVR model: `epsilon = 1, C = 100.` Note that we do not go through the process of hyperparameter tuning in these examples. This means that the above parameters are by no means ideal for this model. You should train and test multiple versions of the model to identify more optimal hyperparameter values.

```python

# ----------- Select variables -----------
X=df[['X3 distance to the nearest MRT station','X2 house age']]
y=df['Y house price of unit area'].values

# ----------- Model fitting -----------
# Define models and set hyperparameter values
model1 = LinearRegression()
model2 = SVR(kernel='rbf', C=100, epsilon=1)

# Fit the two models 
lr = model1.fit(X, y)
svr = model2.fit(X, y)

# ----------- For creating a prediciton plane to be used in the visualization -----------
# Set Increments between points in a meshgrid
mesh_size = 1

# Identify min and max values for input variables
x_min, x_max = X['X3 distance to the nearest MRT station'].min(), X['X3 distance to the nearest MRT station'].max()
y_min, y_max = X['X2 house age'].min(), X['X2 house age'].max()

# Return evenly spaced values based on a range between min and max
xrange = np.arange(x_min, x_max, mesh_size)
yrange = np.arange(y_min, y_max, mesh_size)

# Create a meshgrid
xx, yy = np.meshgrid(xrange, yrange)

# ----------- Create a prediciton plane  -----------
# Use models to create a prediciton plane --- Linear Regression
pred_LR = model1.predict(np.c_[xx.ravel(), yy.ravel()])
pred_LR = pred_LR.reshape(xx.shape)

# Use models to create a prediciton plane --- SVR
pred_svr = model2.predict(np.c_[xx.ravel(), yy.ravel()])
pred_svr = pred_svr.reshape(xx.shape)

# Note, .ravel() flattens the array to a 1D array,
# then np.c_ takes elements from flattened xx and yy arrays and puts them together,
# this creates the right shape required for model input

# prediction array that is created by the model output is a 1D array,
# Hence, we need to reshape it to be the same shape as xx or yy to be able to display it on a graph
```

As we now have all the data ready, let us use Plotly again to create two 3D scatterplots. The first one contains a prediction plane from the linear regression model, and the second one shows the same for SVR.

```python

# Create a 3D scatter plot with predictions
fig = px.scatter_3d(df, x=df['X3 distance to the nearest MRT station'], y=df['X2 house age'], z=df['Y house price of unit area'], 
                 opacity=0.8, color_discrete_sequence=['black'])

# Set figure title and colors
fig.update_layout(title_text="Scatter 3D Plot with Linear Regression Prediction Surface",
                  scene = dict(xaxis=dict(backgroundcolor='white',
                                          color='black',
                                          gridcolor='lightgrey'),
                               yaxis=dict(backgroundcolor='white',
                                          color='black',
                                          gridcolor='lightgrey'
                                          ),
                               zaxis=dict(backgroundcolor='white',
                                          color='black', 
                                          gridcolor='lightgrey')))
# Update marker size
fig.update_traces(marker=dict(size=3))

# Add prediction plane
fig.add_traces(go.Surface(x=xrange, y=yrange, z=pred_LR, name='LR'))

fig.show()
```

![img](https://miro.medium.com/max/3138/1*foIQeiwKKBipYEg0-nFIEw.png)

Multiple Linear Regression with 2 independent variables. Image by [author](https://solclover.medium.com/).

```python

# Create a 3D scatter plot with predictions
fig = px.scatter_3d(df, x=df['X3 distance to the nearest MRT station'], y=df['X2 house age'], z=df['Y house price of unit area'], 
                 opacity=0.8, color_discrete_sequence=['black'])

# Set figure title and colors
fig.update_layout(title_text="Scatter 3D Plot with SVR Prediction Surface",
                  scene = dict(xaxis=dict(backgroundcolor='white',
                                          color='black',
                                          gridcolor='lightgrey'),
                               yaxis=dict(backgroundcolor='white',
                                          color='black',
                                          gridcolor='lightgrey'
                                          ),
                               zaxis=dict(backgroundcolor='white',
                                          color='black', 
                                          gridcolor='lightgrey')))
# Update marker size
fig.update_traces(marker=dict(size=3))

# Add prediction plane
fig.add_traces(go.Surface(x=xrange, y=yrange, z=pred_svr, name='SVR',
                          colorscale=px.colors.sequential.Plotly3))

fig.show()
```

_To mix it up a little, we used a different color scale for this chart called “Plotly3.” If you like the color used in the featured image, it is called “_Sunsetdark.”

![img](https://miro.medium.com/max/3273/1*HO_okTJXlRkCyXQkoTzyvw.png)

Support Vector Regression (SVR) with 2 independent variables. Image by [author](https://solclover.medium.com/).

It is easy to see the difference between the two models, and it is not just the color scheme. Multiple linear regression creates a prediction plane that looks like a flat sheet of paper. Meanwhile, SVR is more like a soft fabric that you can bend and fold in whatever way you need to better fit your data. This gives you a lot more flexibility and enables you to get a more accurate model.

Support Vector Regression — Interactive 3D Graph. Image by [author](https://solclover.medium.com/).

<iframe src="https://cdn.embedly.com/widgets/media.html?src=https%3A%2F%2Fdatapane.com%2Fu%2Fsolclover%2Freports%2Fsupport-vector-regression-svr-3d-plot%2Fembed%2F&amp;display_name=Datapane&amp;url=https%3A%2F%2Fdatapane.com%2Fu%2Fsolclover%2Freports%2Fsupport-vector-regression-svr-3d-plot%2F&amp;image=https%3A%2F%2Fstorage.googleapis.com%2Fdatapane-files-prod%2Fpublic%2F96ee466b-562f-47b5-bae0-4c8437d433bf.png&amp;key=a19fcc184b9711e1b4764040d3dc5c07&amp;type=text%2Fhtml&amp;schema=datapane" allowfullscreen="" title="Support Vector Regression (SVR) 3D Plot" class="et fh fd ka v" scrolling="auto" width="800" height="625" frameborder="0"></iframe>

I have also embedded an interactive graph for you to explore.

Note, the graph may look a bit small depending on the device you are using.

Unfortunately, it is hard to optimize it to make it look good on both laptop and mobile devices simultaneously. Hence, the chosen size is a compromise between the two.

# Conclusion

Support vector regression algorithm is a huge improvement over simple linear regression. It allows you to build non-linear models and gives you control over the flexibility vs. robustness of your models.

As long as you are willing to spend a little bit of time tuning hyperparameters and evaluating model performance on the test samples, you will be able to get some excellent results.

I hope you found this useful! Feel free to reach out if you have any feedback or questions. If you would like to learn about alternative regression methods, I have included links to my MARS and LOWESS stories below.

[MARS: Multivariate Adaptive Regression Splines — How to Improve on Linear Regression?A visual explanation of the MARS algorithm with Python examples and comparison to linear regressiontowardsdatascience.com](https://towardsdatascience.com/mars-multivariate-adaptive-regression-splines-how-to-improve-on-linear-regression-e1e7a63c5eae)

[LOWESS Regression in Python: How to Discover Clear Patterns in Your Data?A detailed guide to using Locally Weighted Scatterplot Smoothing (LOWESS) algorithm in Pythontowardsdatascience.com](https://towardsdatascience.com/lowess-regression-in-python-how-to-discover-clear-patterns-in-your-data-f26e523d7a35)

