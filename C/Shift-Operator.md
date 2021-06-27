---
title: Shift Operator
categories:
- C
- Coding Standard
tags:
- shift operator
date: 2021/6/26
---

> [INT34-C. Do not shift an expression by a negative number of bits or by greater than or equal to the number of bits that exist in the operand](https://wiki.sei.cmu.edu/confluence/display/c/INT34-C.+Do+not+shift+an+expression+by+a+negative+number+of+bits+or+by+greater+than+or+equal+to+the+number+of+bits+that+exist+in+the+operand)

# Shift Operator

Do not shift an expression by a negative number of bits or by a number greater than or equal to the of the promoted left operand. The precision of an integer type is the number of bits it uses to represent values, excluding any sign and padding bits.

For unsigned integer types, the width and the precision are the same; whereas for signed integer types, the width is one greater than the precision. 

This rule uses precision instead of width because, in almost every case, an attempt to shift by a number of bits greater than or equal to the precision of the operand indicates a bug (logic error). A logic error is different from overflow, in which there is simply a representational deficiency. In general, shifts should be performed only on unsigned operands.