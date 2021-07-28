
exports.DDALine = function(startX, startY, endX, endY) {
    let outputArray = [];
    if(startX > endX) {
        newStartX = startX;
        newStartY = startY;

        startX = endX;
        endX = newStartX;

        startY = endY;
        endY = newStartY;
    }

    // calculate the slope of the line
    const slope = calculateSlope(startX, startY, endX, endY);
    
    if(slope === 0) {
        outputArray = calculateHorizontalVerticleLine(startX, endX, startY, true);
    }
    else if (slope === Infinity) {
        outputArray = calculateHorizontalVerticleLine(startY, endY, startX, false);
    }
    else if(slope === 1) {
        outputArray = calculateDiagonalLine(startX, startY, endX);
    }
    else if(slope < 1) {
        outputArray = calculateCase1(startX, startY, endX, endY, slope)
    }
    else if(slope > 1) {
        outputArray = calculateCase2(startX, startY, endX, endY, slope)
    }
    return outputArray;
}

const calculateSlope = function(startX, startY, endX, endY) {
    return (endY - startY)/(endX - startX)
}

const calculateHorizontalVerticleLine = function(startVal, endVal, startConstVal, isHorizontal) {
    let value = startVal;
    let arr = [];
    do {
        if(isHorizontal) {
            arr.push({x: value, y: startConstVal});
        } else {
            arr.push({x: startConstVal, y: value});
        }
        value++;
        }
    while (value <= endVal)
    return arr;
}

const calculateDiagonalLine = function(startX, startY, endX) {
    let arr = [];
    do {
        arr.push({x: startX, y: startY});
        startX++;
        startY++;
        }
    while (startX <= endX )
    return arr; 
}

const calculateCase1 = function(startX, startY, endX, endY, slope) {
    let arr = [];
    do {
        arr.push({x: startX, y: Math.round(startY) === -0 ? 0 :  Math.round(startY)});
        startX++;
        startY = startY + slope;
        }
    while (startX <= endX)
    return arr; 
}

const calculateCase2 = function(startX, startY, endX, endY, slope) {
    let arr = [];
    do {
        arr.push({x: Math.round(startX) === -0 ? 0 :  Math.round(startX), y: startY});
        startY++;
        startX = startX + (1/slope);
        }
    while (startY <= endY)
    return arr; 
}