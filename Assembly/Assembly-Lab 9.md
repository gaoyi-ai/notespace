---
title: Assembly-Lab 9
categories:
- Assembly
tags:
- 寻址方式
date: 2019/8/1 20:00:01
updated: 2020/12/10 12:00:01
---

@[toc]

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200714165105311.png)
![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/2020071416512730.png)

# 实验分析
- 3行16列的字符串处理，一定会用到嵌套循环，所以要定义stack保存cx

- 设置指针指向行数列数

    - 题目要求每行颜色不同，那么对于字符颜色的处理要放在row循环中

    - 题目要求从屏幕中间输出，那么就要开始行数及列数

        需显示在屏幕中间，由材料可得每1行有80个字符占160个字节，而显示的字符串字节+属性字节，共32个字节，所以，要达到水平居中的效果开始值应该为（160 - 32）/ 2 =64
        一共显示3行，所以行数开始值为（25-3）/ 2 =11

- 对于寻址方式的理解
	这道题也可以用bx指向行si指向列di
	![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200714165546682.png)

# code

```
assume cs:code,ds:data,ss:stack

data segment
	str	db 'welcome to masm!'
	c db 2,24h,71h
data ends

stack segment
	db 16 dup (0)
stack ends

code segment
start:	mov ax,data
		mov ds,ax

		mov ax,stack
		mov ss,ax
		mov sp,16

		mov ax,0b86eh
		mov es,ax
		sub si,si	;指向颜色同时指向行数

		mov cx,3
row:	sub di,di	;指向字符位置
		sub bx,bx	;指向字符位置
		mov ah,c[si]	;读颜色

		push cx
		mov cx,16
col:	mov al,str[di]	;读字符，字符串内容的偏移地址，每次增加1
		mov es:40h[bx+di],ax	;写字符，缓冲区的偏移地址，每次增加2
		inc di
		inc bx
		loop col

		pop cx
		mov ax,es 	;修改段地址
		add ax,0ah
		mov es,ax
		inc si
		loop row

		mov ax,4c00h
		int 21h

code ends
end start
```
