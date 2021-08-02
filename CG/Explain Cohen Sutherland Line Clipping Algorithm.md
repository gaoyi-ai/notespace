---
title: Explain Cohen Sutherland Line Clipping
categories:
- CG
- Clipping
tags:
- Cohen Sutherland
date: 2021/8/1
---



> [programmerbay.com](https://programmerbay.com/cohen-sutherland-line-clipping-algorithm/)

Line clipping is a clipping concept in which lines that lies outside the clipping window is removed from the clip region. As a result, only lines which is inside the view plane are visible. Cohen Sutherland Algorithm is one of the popular line clipping algorithm used for the purpose.

> **Must Read** : [What is Clipping in Computer Graphics](https://programmerbay.com/what-is-clipping-in-computer-graphics/)

### What is Cohen Sutherland Line Clipping?

Cohen Sutherland uses region code to clip a portion of the line which is not present in the visible region. It divides a region into 9 columns based on (X_MAX,Y_MAX) and (X_MIN,Y_MIN).

The central part is viewing region or window, all the lines which lie within this region are completely visible. A region code is always assigned to endpoints of the given line.

To check whether the line is visible or not.

![](https://www.programmerbay.com/wp-content/uploads/2018/11/Region-Code.jpg)

![](https://www.programmerbay.com/wp-content/uploads/2018/11/region-code-structure.jpg)

A line can be drawn:

a) Inside the Window, if that is the case, then no clipping is required

b) Completely outside the Window, if that is the case, then no clipping is required because entire line isn’t in the window.

c) Partially inside or outside the window, if that is the case, then we need to find the intersection point and clipping would take place.

Algorithm of Cohen Sutherland Line Clipping
---------------------------------------------------

1) First, define a window or View plane. Get coordinates from the user of a line.

2) Initialize the region code for initial and end coordinates of a line to 0000.  
3) Check whether the line lies within, partially or outside the window.

*     Now, Assign the region code for both the initial and end coordinates.
*    After Assigning, If both the endpoints give 0000, then the line is completely within the window.
*    Else perform AND operation, if the result is not 0000, then the line is not inside the window and that line would not be considered for clipping.
*   Else the line is partially inside the window.

4) After confirming the line is partially inside the window, the next step is to find the intersection point at the window boundary. By using the following formula:

```
If the line passes through the top, 
x=x+(W_ymax-y)/slope ; 
y=W_ymax; 
If the line passes through the bottom, 
x=x+(W_ymin-y)/slope ; 
y=W_ymin; 
if the line passes through the left region, 
y=y+(W_xmin-x)*slope, 
x1=W_xmin; 
if the line passes through the right region, 
y1=y1+(W_xmax-x1)*slope , 
x1=W_xmax
```

5) Now, overwrite the endpoint with a new one and update it.  
6) Repeat 4th step till your line doesn’t get clipped completely.

### C++ Code

```c
typedef int OutCode;

const int INSIDE = 0; // 0000
const int LEFT = 1;   // 0001
const int RIGHT = 2;  // 0010
const int BOTTOM = 4; // 0100
const int TOP = 8;    // 1000

// Compute the bit code for a point (x, y) using the clip
// bounded diagonally by (xmin, ymin), and (xmax, ymax)

// ASSUME THAT xmax, xmin, ymax and ymin are global constants.

OutCode ComputeOutCode(double x, double y)
{
	OutCode code;

	code = INSIDE;          // initialised as being inside of [[clip window]]

	if (x < xmin)           // to the left of clip window
		code |= LEFT;
	else if (x > xmax)      // to the right of clip window
		code |= RIGHT;
	if (y < ymin)           // below the clip window
		code |= BOTTOM;
	else if (y > ymax)      // above the clip window
		code |= TOP;

	return code;
}

// Cohen–Sutherland clipping algorithm clips a line from
// P0 = (x0, y0) to P1 = (x1, y1) against a rectangle with 
// diagonal from (xmin, ymin) to (xmax, ymax).
void CohenSutherlandLineClipAndDraw(double x0, double y0, double x1, double y1)
{
	// compute outcodes for P0, P1, and whatever point lies outside the clip rectangle
	OutCode outcode0 = ComputeOutCode(x0, y0);
	OutCode outcode1 = ComputeOutCode(x1, y1);
	bool accept = false;

	while (true) {
		if (!(outcode0 | outcode1)) {
			// bitwise OR is 0: both points inside window; trivially accept and exit loop
			accept = true;
			break;
		} else if (outcode0 & outcode1) {
			// bitwise AND is not 0: both points share an outside zone (LEFT, RIGHT, TOP,
			// or BOTTOM), so both must be outside window; exit loop (accept is false)
			break;
		} else {
			// failed both tests, so calculate the line segment to clip
			// from an outside point to an intersection with clip edge
			double x, y;

			// At least one endpoint is outside the clip rectangle; pick it.
			OutCode outcodeOut = outcode1 > outcode0 ? outcode1 : outcode0;

			// Now find the intersection point;
			// use formulas:
			//   slope = (y1 - y0) / (x1 - x0)
			//   x = x0 + (1 / slope) * (ym - y0), where ym is ymin or ymax
			//   y = y0 + slope * (xm - x0), where xm is xmin or xmax
			// No need to worry about divide-by-zero because, in each case, the
			// outcode bit being tested guarantees the denominator is non-zero
			if (outcodeOut & TOP) {           // point is above the clip window
				x = x0 + (x1 - x0) * (ymax - y0) / (y1 - y0);
				y = ymax;
			} else if (outcodeOut & BOTTOM) { // point is below the clip window
				x = x0 + (x1 - x0) * (ymin - y0) / (y1 - y0);
				y = ymin;
			} else if (outcodeOut & RIGHT) {  // point is to the right of clip window
				y = y0 + (y1 - y0) * (xmax - x0) / (x1 - x0);
				x = xmax;
			} else if (outcodeOut & LEFT) {   // point is to the left of clip window
				y = y0 + (y1 - y0) * (xmin - x0) / (x1 - x0);
				x = xmin;
			}

			// Now we move outside point to intersection point to clip
			// and get ready for next pass.
			if (outcodeOut == outcode0) {
				x0 = x;
				y0 = y;
				outcode0 = ComputeOutCode(x0, y0);
			} else {
				x1 = x;
				y1 = y;
				outcode1 = ComputeOutCode(x1, y1);
			}
		}
	}
}
```

### Python Code

```python
# Python program to implement Cohen Sutherland algorithm
# for line clipping.

# Defining region codes
INSIDE = 0 # 0000
LEFT = 1 # 0001
RIGHT = 2 # 0010
BOTTOM = 4 # 0100
TOP = 8	 # 1000

# Defining x_max, y_max and x_min, y_min for rectangle
# Since diagonal points are enough to define a rectangle
x_max = 10.0
y_max = 8.0
x_min = 4.0
y_min = 4.0


# Function to compute region code for a point(x, y)
def computeCode(x, y):
	code = INSIDE
	if x < x_min:	 # to the left of rectangle
		code |= LEFT
	elif x > x_max: # to the right of rectangle
		code |= RIGHT
	if y < y_min:	 # below the rectangle
		code |= BOTTOM
	elif y > y_max: # above the rectangle
		code |= TOP

	return code


# Implementing Cohen-Sutherland algorithm
# Clipping a line from P1 = (x1, y1) to P2 = (x2, y2)
def cohenSutherlandClip(x1, y1, x2, y2):

	# Compute region codes for P1, P2
	code1 = computeCode(x1, y1)
	code2 = computeCode(x2, y2)
	accept = False

	while True:

		# If both endpoints lie within rectangle
		if code1 == 0 and code2 == 0:
			accept = True
			break

		# If both endpoints are outside rectangle
		elif (code1 & code2) != 0:
			break

		# Some segment lies within the rectangle
		else:

			# Line Needs clipping
			# At least one of the points is outside,
			# select it
			x = 1.0
			y = 1.0
			if code1 != 0:
				code_out = code1
			else:
				code_out = code2

			# Find intersection point
			# using formulas y = y1 + slope * (x - x1),
			# x = x1 + (1 / slope) * (y - y1)
			if code_out & TOP:
			
				# point is above the clip rectangle
				x = x1 + (x2 - x1) * (y_max - y1) / (y2 - y1)
				y = y_max

			elif code_out & BOTTOM:
				
				# point is below the clip rectangle
				x = x1 + (x2 - x1) * (y_min - y1) / (y2 - y1)
				y = y_min

			elif code_out & RIGHT:
				
				# point is to the right of the clip rectangle
				y = y1 + (y2 - y1) * (x_max - x1) / (x2 - x1)
				x = x_max

			elif code_out & LEFT:
				
				# point is to the left of the clip rectangle
				y = y1 + (y2 - y1) * (x_min - x1) / (x2 - x1)
				x = x_min

			# Now intersection point x, y is found
			# We replace point outside clipping rectangle
			# by intersection point
			if code_out == code1:
				x1 = x
				y1 = y
				code1 = computeCode(x1, y1)

			else:
				x2 = x
				y2 = y
				code2 = computeCode(x2, y2)

	if accept:
		print ("Line accepted from %.2f, %.2f to %.2f, %.2f" % (x1, y1, x2, y2))

		# Here the user can add code to display the rectangle
		# along with the accepted (portion of) lines

	else:
		print("Line rejected")

# Driver Script
# First Line segment
# P11 = (5, 5), P12 = (7, 7)
cohenSutherlandClip(5, 5, 7, 7)

# Second Line segment
# P21 = (7, 9), P22 = (11, 4)
cohenSutherlandClip(7, 9, 11, 4)

# Third Line segment
# P31 = (1, 5), P32 = (4, 1)
cohenSutherlandClip(1, 5, 4, 1)
```

