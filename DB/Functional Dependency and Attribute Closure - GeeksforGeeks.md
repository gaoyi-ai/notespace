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

**GATE Question: Consider the relation scheme R = {E, F, G, H, I, J, K, L, M, M} and the set of functional dependencies {{E, F} -> {G}, {F} -> {I, J}, {E, H} -> {K, L}, K -> {M}, L -> {N} on R. What is the key for R? (GATE-CS-2014)**  
A. {E, F}  
B. {E, F, H}  
C. {E, F, H, K, L}  
D. {E}

**Answer:** Finding attribute closure of all given options, we get:  
{E,F}+ = {EFGIJ}  
{E,F,H}+ = {EFHGIJKLMN}  
{E,F,H,K,L}+ = {{EFHGIJKLMN}  
{E}+ = {E}  
{EFH}+ and {EFHKL}+ results in set of all attributes, but EFH is minimal. So it will be candidate key. So correct option is (B).

**How to check whether an FD can be derived from a given FD set?**

To check whether an FD A->B can be derived from an FD set F,

1.  Find (A)+ using FD set F.
2.  If B is subset of (A)+, then A->B is true else not true.

**GATE Question: In a schema with attributes A, B, C, D and E following set of functional dependencies are given**  
**{A -> B, A -> C, CD -> E, B -> D, E -> A}**  
**Which of the following functional dependencies is NOT implied by the above set? (GATE IT 2005)**  
A. CD -> AC  
B. BD -> CD  
C. BC -> CD  
D. AC -> BC

**Answer:** Using FD set given in question,  
(CD)+ = {CDEAB} which means CD -> AC also holds true.  
(BD)+ = {BD} which means BD -> CD can’t hold true. So this FD is no implied in FD set. So (B) is the required option.  
Others can be checked in the same way.

**Prime and non-prime attributes**

Attributes which are parts of any candidate key of relation are called as prime attribute, others are non-prime attributes. For Example, STUD_NO in STUDENT relation is prime attribute, others are non-prime attribute.

**GATE Question:  Consider a relation scheme R = (A, B, C, D, E, H) on which the following functional dependencies hold: {A–>B, BC–> D, E–>C, D–>A}. What are the candidate keys of R? [GATE 2005]**  
(a) AE, BE  
(b) AE, BE, DE  
(c) AEH, BEH, BCH  
(d) AEH, BEH, DEH

**Answer:** (AE)+ = {ABECD} which is not set of all attributes. So AE is not a candidate key. Hence option A and B are wrong.  
(AEH)+ = {ABCDEH}  
(BEH)+ = {BEHCDA}  
(BCH)+ = {BCHDA} which is not set of all attributes. So BCH is not a candidate key. Hence option C is wrong.  
So correct answer is D.