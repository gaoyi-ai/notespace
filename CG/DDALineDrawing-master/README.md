[![typescript](https://camo.githubusercontent.com/56e4a1d9c38168bd7b1520246d6ee084ab9abbbb/68747470733a2f2f62616467656e2e6e65742f62616467652f69636f6e2f547970655363726970743f69636f6e3d74797065736372697074266c6162656c266c6162656c436f6c6f723d626c756526636f6c6f723d353535353535)](https://www.typescriptlang.org/)

# DDALineDrawing

This Node.js library (support TypeScript) will facilitate the calculation of DDA algorithm for a given 2 end points.

## Installation	
     npm install dda-line-algorithm

## Usage

**DDALine** method reqire *4 integers* (startX, startY, endX, endY) as parameters and algorithm will generate the line coordinates between the starting and ending points and returns *array of objects*

To get the coordinates of line from (1,4) to (5,9) use below code
``` js
let dda = require('dda-line-algorithm') OR import dda from 'dda-line-algorithm'
let pointsList = dda.DDALine (1,4,5,9);
```

returns
``` js
[ { x: 1, y: 4 },
  { x: 2, y: 5 },
  { x: 3, y: 6 },
  { x: 3, y: 7 },
  { x: 4, y: 8 },
  { x: 5, y: 9 } ]
```
 
## Access points

you can access points using 
``` js
for(const point of pointsList) {
  x = point.x
  y = point.y
  // ...
}
```
