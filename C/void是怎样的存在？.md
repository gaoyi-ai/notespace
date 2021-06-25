---
title: void pointer 是怎样的存在？
categories:
- C
- Pointer
- Void Pointer
tags:
- pointer
date: 2021/6/16
updated: 2021/6/16
---

# `void *`

A pointer to `void` is a "generic" pointer type. A `void *` can be converted to any other pointer type without an explicit cast. You cannot dereference a `void *` or do pointer arithmetic with it; you must convert it to a pointer to a complete data type first.

`void *` is often used in places where you need to be able to work with different pointer types in the same code. One commonly cited example is the library function `qsort`:

```c
void qsort(void *base, size_t nmemb, size_t size, 
           int (*compar)(const void *, const void *));
```

`base` is the address of an array, `nmemb` is the number of elements in the array, `size` is the size of each element, and `compar` is a pointer to a function that compares two elements of the array. It gets called like so:

```c
int iArr[10];
double dArr[30];
long lArr[50];
...
qsort(iArr, sizeof iArr/sizeof iArr[0], sizeof iArr[0], compareInt);
qsort(dArr, sizeof dArr/sizeof dArr[0], sizeof dArr[0], compareDouble);
qsort(lArr, sizeof lArr/sizeof lArr[0], sizeof lArr[0], compareLong);
```

The array expressions `iArr`, `dArr`, and `lArr` are implicitly converted from array types to pointer types in the function call, and each is implicitly converted from "pointer to `int`/`double`/`long`" to "pointer to `void`".

The comparison functions would look something like:

```c
int compareInt(const void *lhs, const void *rhs)
{
  const int *x = lhs;  // convert void * to int * by assignment
  const int *y = rhs;

  if (*x > *y) return 1;
  if (*x == *y) return 0;
  return -1;
}
```

By accepting `void *`, `qsort` can work with arrays of any type.

The disadvantage of using `void *` is that you throw type safety out the window and into oncoming traffic. There's nothing to protect you from using the wrong comparison routine:

```c
qsort(dArr, sizeof dArr/sizeof dArr[0], sizeof dArr[0], compareInt);
```

`compareInt` is expecting its arguments to be pointing to `int`s, but is actually working with `double`s. There's no way to catch this problem at compile time; you'll just wind up with a missorted array.

---

指针的类型不过是解释数据的方式不同，这样的道理也可用于很多场合的强制类型转换，例如将`int`类型指针转换为`char`型指针，并不会改变内存的实际内容，只是修改了解释方式而已。而`void *`是一种无类型指针，任何类型指针都可以转为`void*`，它无条件接受各种类型。而既然是无类型指针，那么就不要尝试做下面的事情：

- 解引用
- 算术运算

由于不知道其解引用操作的内存大小，以及算术运算操作的大小，因此它的结果是未知的。

```c
#include <stdio.h>
int main(void)
{
    int a = 10;
    int *b = &a;
    void *c = b;
    *c;
    return 0;
}
```

编译警告如下：

```text
warning: dereferencing ‘void *’ pointer
```

## 如何使用

既然如此，那么`void*`有什么用呢？实际上我们在很多接口中都会发现它们的参数类型都是`void*`,例如:

```c
ssize_t read(int fd, void *buf, size_t count);
void *memcpy(void *dest, const void *src, size_t n);
```

为何要如此设计？因为对于这种通用型接口，你不知道用户的数据类型是什么，但是你必须能够处理用户的各种类型数据，因而会使用`void*`。`void*`能包容地接受各种类型的指针。也就是说，如果你期望接口能够接受任何类型的参数，你可以使用`void*`类型。但是在具体使用的时候，你必须转换为具体的指针类型。例如，你传入接口的是`int*`，那么你在使用的时候就应该按照`int*`使用。

## 注意

使用`void*`需要特别注意的是，你必须清楚原始传入的是什么类型，然后转换成对应类型。例如，你准备使用库函数qsort进行排序：

```c
void qsort(void *base,size_t nmemb,size_t size , int(*compar)(const void *,const void *));
```

它的第三个参数就是比较函数，它接受的参数都是`const void*`，如果你的比较对象是一个结构体类型，那么你自己在实现compar函数的时候，也必须是转换为该结构体类型使用。举个例子，你要实现学生信息按照成绩比较：

```c
typedef struct student_tag
{
    char name[STU_NAME_LEN];  //学生姓名
    unsigned int id;          //学生学号
    int score;                //学生成绩
}student_t;
int studentCompare(const void *stu1,const void *stu2)
{
　　/*强转成需要比较的数据结构*/
    student_t *value1 = (student_t*)stu1;
    student_t *value2 = (student_t*)stu2;
    return value1->score-value2->score;
}
```

在将其传入`studentCompare`函数后，必须转换为其对应的类型进行处理。