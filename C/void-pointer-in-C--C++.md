---
title: void pointer in C / C++
categories:
- C
- Pointer
- Void Pointer
tags:
- pointer
date: 2021/6/16
updated: 2021/6/16
---

# void pointer in C / C++

A void pointer is a pointer that has no associated data type with it. A void pointer can hold address of any type and can be typcasted to any type.

```c
int a = 10;
char b = 'x';

void *p = &a; // void pointer holds address of int 'a'
p = &b; // void pointer holds address of char 'b'
```

**Advantages of void pointers:**
**1)** malloc() and calloc() return void * type and this allows these functions to be used to allocate memory of any data type (just because of void *)

```c
int main(void)
{
	// Note that malloc() returns void * which can be
	// typecasted to any type like int *, char *, ..
	int *x = malloc(sizeof(int) * n);
}
```

Note that the above program compiles in C, but doesn’t compile in C++. In C++, we must explicitly typecast return value of malloc to (int *).

**2)** void pointers in C are used to implement generic functions in C. For example [compare function which is used in qsort()](https://www.geeksforgeeks.org/comparator-function-of-qsort-in-c/).

**Some Interesting Facts:**
**1)** void pointers cannot be dereferenced. For example the following program doesn’t compile.

```c
#include<stdio.h>
int main()
{
	int a = 10;
	void *ptr = &a;
	printf("%d", *ptr);
	return 0;
}
```

Output:

```
Compiler Error: 'void*' is not a pointer-to-object type 
```

The following program compiles and runs fine.

```c
#include<stdio.h>
int main()
{
	int a = 10;
	void *ptr = &a;
	printf("%d", *(int *)ptr);
	return 0;
}
```

Output:

```
10
```

**2)** The [C standard](https://www.geeksforgeeks.org/c-programming-language-standard/) doesn’t allow pointer arithmetic with void pointers. However, in GNU C it is allowed by considering the size of void is 1. For example the following program compiles and runs fine in gcc.

```c
#include<stdio.h>
int main()
{
	int a[2] = {1, 2};
	void *ptr = &a;
	ptr = ptr + sizeof(int);
	printf("%d", *(int *)ptr);
	return 0;
}
```

Output:

```
2
```

Note that the above program may not work in other compilers.