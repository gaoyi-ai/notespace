---
title: Void Pointer
categories:
- C
- Pointer
- Void Pointer
tags:
- pointer
date: 2021/6/25
---



> [mp.weixin.qq.com](https://mp.weixin.qq.com/s/k6pdjGkhZ9nLwuYPtBqQww)

# 1. 不能动的 “地址” 之 void 指针

## 1.1 void 指针初探

`void *`表示一个 “不知道类型” 的指针，也就不知道从这个指针地址开始多少字节为一个数据。和用 int 表示指针异曲同工，只是更明确是“指针”。

因此`void *`只能表示一个地址，不能用来`&`取值，也不能`++`和`--`移动指针，因此不知道多少字节是一个数据单位。

```
    int nums[] = {3,5,6,7,9};
    void* ptr1 = nums;
    //int i = *ptr1; // 对于void指针没法直接取值
    int* ptr2 = (int*)nums;
    printf("%d,%d\n",ptr1,ptr2);
    int i = *ptr2;
    printf("%d\n",i);

```

 从输出结果可以看出，无论是无类型的 void 指针还是 int 类型指针，指向的地址都是一样的：   

![](https://mmbiz.qpic.cn/mmbiz_png/PPUawreTASf264aXDJbpHVoT6qnue0FhmIYLUPcR2bx1icvBLbu8Z3M7vNPORe6vtxVd1Tfw6Nq72wc7q2K5upg/640?wx_fmt=png)

> PS：void * 就是一个不能动的 “地址”，在进行 &、移动指针之前必须转型为类型指针。  

## 1.2 void 指针的用途


![](https://mmbiz.qpic.cn/mmbiz_png/PPUawreTASf264aXDJbpHVoT6qnue0FhdkcXib9LHNicGXHhVIViae2Y5ZXuibQDhrM8O5KuLNCOrb5NIYaC2TjA8w/640?wx_fmt=png)

这里我们看一下我们之前了解的`memset`函数，其第一个参数就是一个`void`指针，它可以帮我们屏蔽各种不同类型指针的差异。

如下面代码所示，我们既可以传入一个`int`类型数组的指针，也可以传入一个`char`类型数组的指针：  

```c
    int nums[20];
    memset(nums,0,sizeof(nums));
    char chs[2];
    memset(chs,0,sizeof(chs));
```

 那么，我们也可以试着自己动手模拟一下这个`memset`函数，暂且命名为`mymemset`吧：

```c
void mymemset(void *data,int num,int byteSize)
{
    // char就是一个字节，而计算机中是以字节为单位存储的
    char *ptr = (char*)data;
    int i;
    for(i=0;i<byteSize;i++)
    {
        *ptr=num;
        ptr++;
    }
}

int main(int argc, char *argv[])
{
    int nums[20];
    mymemset(nums,0,sizeof(nums));
    int i,len=sizeof(nums)/sizeof(int);
    for(i=0;i<len;i++)
    {
        printf("%d ",nums[i]);
    }
    printf("\n");

    return 0;
}
```

在这个`mymemset`函数中，我们利用`void`指针接收不同类型的指针，利用 char 类型（一个字节）逐个字节读取内存中的每一个字节，最后依次填充指定的数字。

由于`char`类型是一个具体类型，所以可以使用`++`或者`--`进行指针的移动。

对于结构体类型，也可以使用我们的`mymemset`函数：

```c
typedef struct _Person
{
    char *name;
    int age;
} Person;

Person p1;
mymemset(&p1,0,sizeof(Person));
printf("p1.Age:%d\n",p1.age);
```

最终的运行结果如下图所示：  

![](https://mmbiz.qpic.cn/mmbiz_png/PPUawreTASf264aXDJbpHVoT6qnue0FhWmJQJwIOeLmrNYsq4JhwXqCt6VwxvuKRsYxnkQdap7KcOd3moY8GVA/640?wx_fmt=png)

> void * 的用途：在只知道内存，但是不知道是什么类型的时候。

## 2. 函数指针

### 2.1 指向函数的指针

我们可以在 C 中轻松地定义一个函数指针：  

```c
typedef void (*intFunc)(int i);
```

这里我们定义了一个无返回值的，只有一个`int`类型参数的函数指针`intFunc`。

我们可以在`main`函数中使用这个函数指针来指向一个具体的函数（这个具体的函数定义需要和函数指针的定义一致）：

```c
void test1(int age){

    printf("test1:%d\n",age);
}

int main(void){
    
    // 声明一个intFunc类型的函数指针
    intFunc f1 = test1;
    // 执行f1函数指针所指向的代码区
    f1(8);
    return 0;
}
```

最终运行结果如下图所示，执行函数指针 f1 即执行了其所指向的具体的函数：

![](https://mmbiz.qpic.cn/mmbiz_png/PPUawreTASf264aXDJbpHVoT6qnue0FhdRpMicOphicDNc3yGs4oGI66gj4GuEYteLCvibyzOkx3kroPYWOASXflg/640?wx_fmt=png)

### 2.2 函数指针的基本使用

这里我们通过一个小案例来对函数指针做一个基本的使用介绍。相信大部分的`C#`或`Java`程序员都很熟悉`foreach`，那么我们就来模拟 foreach 对 int 数组中的值进行不同的处理。具体体现为 for 循环的代码是复用的，但是怎么处理这些数据不确定，因此把处理数据的逻辑由函数指针指定。  

```c
void foreachNums(int *nums,int len,intFunc func)
{
    int i;
    for(i=0;i<len;i++)
    {
        int num = nums[i];
        func(num);
    }
}

void printNum(int num)
{
    printf("value=%d\n",num);
}
```

在`foreachNums`函数中，我们定义了一个`intFunc`函数指针，`printNum`函数是满足`intFunc`定义的一个具体的函数。

下面我们在`main`函数中将`printNum`函数作为函数指针传递给`foreachNums`函数。

```c
    int nums[] = { 1,5,666,23423,223 };
    foreachNums(nums,sizeof(nums)/sizeof(int),printNum);
```

最终运行的结果如下图所示：

![](https://mmbiz.qpic.cn/mmbiz_png/PPUawreTASf264aXDJbpHVoT6qnue0Fh2wdeWzRjkuVkC6sSib9TPibYs9m5hCibYn0wF4WhKHa0GHQFGppBsm7aw/640?wx_fmt=png)

通过函数指针，我们可以屏蔽各种具体处理方法的不同，也就是将不确定的因素都依赖于抽象，这也是面向抽象或面向接口编程的精髓。

## 3. 函数指针应用案例

### 3.1 计算任意类型的最大值

（1）定义函数指针及 getMax 主体：

```c
typedef int (*compareFunc)(void *data1,void *data2);
// getMax 函数参数说明：
// data 待比较数据数组的首地址,uniteSize单元字节个数
// length：数据的长度。{1,3,5,6}：length=4
// 比较data1和data2指向的数据做比较，
// 如果data1>data2，则返回正数
void *getMax(void *data,int unitSize,int length,compareFunc func)
{
    int i;
    char *ptr = (char*)data;
    char *max = ptr;
    
    for(i=1;i<length;i++)
    {
        char *item = ptr+i*unitSize;
        //到底取几个字节进行比较是func内部的事情
        if(func(item,max)>0)
        {
            max = item;
        }
    }

    return max;
}
```

这里可以看到，在`getMax`中到底取几个字节去比较都是由`compareFunc`所指向的函数去做，`getMax`根本不用关心。

（2）定义符合函数指针定义的不同类型的函数：

```c
int intDataCompare(void *data1,void *data2)
{
    int *ptr1 = (int*)data1;
    int *ptr2 = (int*)data2;

    int i1=*ptr1;
    int i2=*ptr2;

    return i1-i2;
}

typedef struct _Dog
{
    char *name;
    int age;
} Dog;

int dogDataCompare(void *data1,void *data2)
{
    Dog *dog1 = (Dog*)data1;
    Dog *dog2 = (Dog*)data2;

    return (dog1->age)-(dog2->age);
}
```

（3）在 main 函数中针对 int 类型和结构体类型进行调用：

```c
int main(int argc, char *argv[])
{
    // test1:int类型求最大值
    int nums[] = { 3,5,8,7,6 };
    int *pMax = (int *)getMax(nums,sizeof(int),sizeof(nums)/sizeof(int),
        intDataCompare);
    int max = *pMax;
    printf("%d\n",max);
    // test2:结构体类型求最大值
    Dog dogs[] ={{"沙皮",3},{"腊肠",10},{"哈士奇",5},
        {"京巴",8},{"大狗",2}};
    Dog *pDog = (Dog *)getMax(dogs,sizeof(Dog),
        sizeof(dogs)/sizeof(Dog),dogDataCompare);
    printf("%s=%d",pDog->name,pDog->age);

    return 0;
}
```

最终运行结果如下图所示：

![](https://mmbiz.qpic.cn/mmbiz_png/PPUawreTASf264aXDJbpHVoT6qnue0FhMV3f4pA4C1Ej2h6hVFTQwHmSqNXuGSOXZVeVibOoFWCXzwxGAQSIGSw/640?wx_fmt=png)

## 3.2 C 中自带的 qsort 函数—自定义排序

`qsort`包含在`<stdlib.h>`头文件中，此函数根据你给的比较条件进行快速排序，通过指针移动实现排序。排序之后的结果仍然放在原数组中。

使用`qsort`函数必须自己写一个比较函数。我们可以看看`qsort`函数的原型：

```c
 void qsort ( void * base, size_t num, size_t size, int ( * comparator ) ( const void *, const void * ) );
```

```c
    int nums[] = { 3,5,8,7,6 };
    qsort(nums,sizeof(nums)/sizeof(int),sizeof(int),intDataCompare);
    int i;
    for(i=0;i<sizeof(nums)/sizeof(int);i++)
    {
        printf("%d ",nums[i]);
    }
    printf("\n");

    Dog dogs[] ={{"沙皮",3},{"腊肠",10},{"哈士奇",5},
        {"京巴",8},{"大狗",2}};
    qsort(dogs,sizeof(dogs)/sizeof(Dog),sizeof(Dog),dogDataCompare);
    for(i=0;i<sizeof(dogs)/sizeof(Dog);i++)
    {
        printf("%s %d ",dogs[i].name,dogs[i].age);
    }
```

那么，快速排序后是否有结果呢？答案是肯定的，我们可以传入各种比较方法，可以升序排序也可以降序排序。

![](https://mmbiz.qpic.cn/mmbiz_png/PPUawreTASf264aXDJbpHVoT6qnue0FhuNyUMH2XibPYcOkmMJX4PFPDde5GtqibGlJuFCCkyGOSHMWHVxib6nXGw/640?wx_fmt=png)