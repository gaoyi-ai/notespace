---
title: Functional Dependency
categories:
- DB
- FD
tags:
- FD
- geeksforgeeks
date: 2021/3/10 20:00:17
updated: 2021/3/12 12:00:17
---



**Functional Dependency**

如果具有相同属性A值的两个元组也具有相同的属性B值，那么关系中的功能依赖A->B就成立。For Example, in relation STUDENT shown in table 1, Functional Dependencies

```
STUD_NO->STUD_NAME, STUD_NO->STUD_PHONE hold
```

but

```
STUD_NAME->STUD_ADDR do not hold
```

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image23.png)

**How to find functional dependencies for a relation?**

Functional Dependencies in a relation are dependent on the domain of the relation. Consider the STUDENT relation given in Table 1.

*   We know that STUD_NO is unique for each student. So STUD_NO->STUD_NAME, STUD_NO->STUD_PHONE, STUD_NO->STUD_STATE, STUD_NO->STUD_COUNTRY and STUD_NO -> STUD_AGE all will be true.
*   Similarly, STUD_STATE->STUD_COUNTRY will be true as if two records have same STUD_STATE, they will have same STUD_COUNTRY as well.
*   For relation STUDENT_COURSE, COURSE_NO->COURSE_NAME will be true as two records with same COURSE_NO will have same COURSE_NAME.

**Functional Dependency Set:** Functional Dependency set or FD set of a relation is the set of all FDs present in the relation. For Example, FD set for relation STUDENT shown in table 1 is:

```
{ STUD_NO->STUD_NAME, STUD_NO->STUD_PHONE, STUD_NO->STUD_STATE, STUD_NO->STUD_COUNTRY, 
  STUD_NO -> STUD_AGE, STUD_STATE->STUD_COUNTRY }
```

**Attribute Closure:** Attribute closure of an attribute set can be defined as set of attributes which can be functionally determined from it.

**How to find attribute closure of an attribute set?**  
To find attribute closure of an attribute set:

*   Add elements of attribute set to the result set.
*   Recursively add elements to the result set which can be functionally determined from the elements of the result set.

Using FD set of table 1, attribute closure can be determined as:

```
(STUD_NO)+ = {STUD_NO, STUD_NAME, STUD_PHONE, STUD_STATE, STUD_COUNTRY, STUD_AGE}
(STUD_STATE)+ = {STUD_STATE, STUD_COUNTRY}
```

**How to find Candidate Keys and Super Keys using Attribute Closure?**

*   If attribute closure of an attribute set contains all attributes of relation, the attribute set will be super key of the relation.
*   If no subset of this attribute set can functionally determine all attributes of the relation, the set will be candidate key as well. For Example, using FD set of table 1,

(STUD_NO, STUD_NAME)+ = {STUD_NO, STUD_NAME, STUD_PHONE, STUD_STATE, STUD_COUNTRY, STUD_AGE}

(STUD_NO)+ = {STUD_NO, STUD_NAME, STUD_PHONE, STUD_STATE, STUD_COUNTRY, STUD_AGE}

(STUD_NO, STUD_NAME) will be super key but not candidate key because its subset (STUD_NO)+ is equal to all attributes of the relation. So, STUD_NO will be a candidate key.

**How to check whether an FD can be derived from a given FD set?**

To check whether an FD A->B can be derived from an FD set F,

1.  Find (A)+ using FD set F.
2.  If B is subset of (A)+, then A->B is true else not true.

**Prime and non-prime attributes**

Attributes which are parts of any candidate key of relation are called as prime attribute, others are non-prime attributes. For Example, STUD_NO in STUDENT relation is prime attribute, others are non-prime attribute.
