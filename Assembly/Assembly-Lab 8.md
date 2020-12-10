---
title: Assembly-Lab 8
categories:
- Assembly
tags:
- jmp
- Program execution process
date: 2019/8/1 20:00:00
updated: 2020/12/10 12:00:00
---



@[toc]

# 实验8

分析下面程序，这个程序可以正确返回吗？

```assembly
assume cs:codesg
codesg segment

	mov ax,4c00h
	int 21h

start:	mov ax,0
    s:	nop
    	nop
    	
    mov	di,offset s
    mov	si,offset s2
    mov	ax,cs:[si]
    mov	cs:[di],ax
    
    s0:	jmp short s
    
    s1:	mov ax,0
    	int 21h
    	mov ax,0
    s2:	jmp short s1
	    nop

codesg ends
end start
```

# 实验分析

主要考察对于`jmp`指令的原理的理解

```assembly
    mov	di,offset s
    mov	si,offset s2
    mov	ax,cs:[si]
    mov	cs:[di],ax
```

这4条指令的作用就是将s2标号地址的指令赋值到s标号地址

1. 直接从s0标号地址指令开始分析

    此时程序会跳转到s标号位置，而s标号地址我们已经知道现在存储的时s2标号地址指令

2. 这里就考察对于`jmp 转移位移`的指令的原理的理解

    先贴出其对应的机器指令
    ![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200714123600363.png)

    1. 首先先回顾CPU执行指令的过程，比如下面中的指令`EB03`

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200714123723199.png)
![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200714123743385.png)

   2. `jmp 转移位移`指令的转移位移的计算方法

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200714123815152.png)

3. 清楚了上面的知识，再来观察ip

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200714124031660.png)

4. 程序经过编译之后

    - s0标号地址的jmp指令计算的转移位移为F0（补码）=-16

    - s2标号地址的jmp指令计算的转移位移是程序还没有运行前的，所以转移位移为F6（补码）=-10

    **所以当执行完`mov cs:[di],ax`时，ip=16h**

    **之后读取`jmp short s`后，ip+=2，等于18h也就是24**

    **之后执行指令ip+=-16，等于8**

    **之后再读取s标号地址指令，注意此时已经为`EB F0`（也就是`jmp short s1`编译的机器指令），ip+=2，等于0ah也就是10**

    **之后再执行指令ip+=-10，等于0**

5. 程序执行之后变为
![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200714124040312.png)

# 汇编编译器(masm.exe)对jmp的相关处理

1.向前转移

```assembly
s:	.
	.
	jmp s ( jmp short s、 jmp near ptr s、 jmp far ptr s)
```

编译器中有一个地址计数器(AC,编译器在编译程序过程中,每读到一个字节AC就加1。当编译器遇到一些伪操作的时候,也会根据具体情况使AC增加,如db、dw等。
在向前转移时,编译器可以在读到标号s后记下AC的值as,在读到jmp….s后记下AC的值aj。编译器就可以用as-aj算出位移量disp。

2.向后转移

```assembly
jmp s( jmp short s、 jmp near ptr s、 jmp far ptr s)
	.
	.
s:	
```

在这种情况下,编译器先读到jmp….s指令。由于它还没有读到标号s,所以编译器此时还不能确定标号s处的AC值。也就是说,编译器不能确定位移量disp的大小。

此时,编译器将jmp….s指令都当作 jmp short s来读取,记下jmp….指令的位置和AC的值a,并作如下处理。

- 对于 jmp short s,编译器生成EB和I个nop指令(相当于预留1个字节的空间,存放8位disp);
- 对于jmp s和 jmp near ptr s,编译器生成EB和两个nop指令(相当于预留两个字节的空间,存放16位disp)
- 对于 jmp far ptr s,编译器生成EB和4个nop指令(相当于预留4个字节的空间,存放段地址和偏移地址)

作完以上处理后,编译器继续工作,当向后读到标号s时,记下AC的值as,并计算出转移的位移量:disp=as-aj
