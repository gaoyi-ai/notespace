---
title: Importance of function prototype in C
categories:
- C
tags:
- function prototype
date: 2021/6/26
---



# Importance of function prototype in C

Function prototype tells compiler about number of parameters function takes, data-types of parameters and return type of function. By using this information, compiler cross checks function parameters and their data-type with function definition and function call. If we ignore function prototype, program may compile with warning, and may work properly. But some times, it will give strange output and it is very hard to find such programming mistakes. Let us see with examples

```c
#include <errno.h>
#include <stdio.h>

int main(int argc, char *argv[])
{
	FILE *fp;

	fp = fopen(argv[1], "r");
	if (fp == NULL) {
		fprintf(stderr, "%s\n", strerror(errno));
		return errno;
	}

	printf("file exist\n");

	fclose(fp);

	return 0;
}
```

Above program checks existence of file, provided from command line, if given file is exist, then the program prints “file exist”, otherwise it prints appropriate error message. Let us provide a filename, which does not exist in file system, and check the output of program on x86_64 architecture.

```
[narendra@/media/partition/GFG]$ ./file_existence hello.c
Segmentation fault (core dumped)
```

Why this program crashed, instead it should show appropriate error message. This program will work fine on x86 architecture, but will crash on x86_64 architecture. Let us see what was wrong with code. Carefully go through the program, deliberately I haven’t included prototype of “strerror()” function. This function returns “pointer to character”, which will print error message which depends on errno passed to this function. Note that x86 architecture is ILP-32 model, means integer, pointers and long are 32-bit wide, that’s why program will work correctly on this architecture. But x86_64 is LP-64 model, means long and pointers are 64 bit wide. *In C language, when we don’t provide prototype of function, the compiler assumes that function returns an integer*. In our example, we haven’t included “string.h” header file (strerror’s prototype is declared in this file), that’s why compiler assumed that function returns integer. But its return type is pointer to character. In x86_64, pointers are 64-bit wide and integers are 32-bits wide, that’s why while returning from function, the returned address gets truncated (i.e. 32-bit wide address, which is size of integer on x86_64) which is invalid and when we try to dereference this address, the result is segmentation fault.

Now include the “string.h” header file and check the output, the program will work correctly.

```
[narendra@/media/partition/GFG]$ ./file_existence hello.c
No such file or directory
```

Consider one more example.

```c
#include <stdio.h>

int main(void)
{
	int *p = malloc(sizeof(int));

	if (p == NULL) {
		perror("malloc()");
		return -1;
	}

	*p = 10;
	free(p);

	return 0;
}
```

Above code will work fine on IA-32 model, but will fail on IA-64 model. Reason for failure of this code is we haven’t included prototype of malloc() function and returned value is truncated in IA-64 model.