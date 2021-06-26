---
title: Auto-Correct - A Real-World Application
categories:
- DSA
- Algorithm
- Dynamic Programming
tags:
- dynamic programming
date: 2021/6/25
---



# Auto-Correct: A Real-World Application

## Introduction

Autocorrect is a ubiquitous software feature, whether it be on the web, cellphones, or desktop. So it might come as a surprise that this feature uses a DP algorithm, called the Levenshtein distance.

The Levenshtein distance is defined as the number of single-character edits (insertion, removal, or deletion) that are needed to change one string into another. For example, the Levenshtein distance between “sick” and “sock” is 1.

At a very high-level, autocorrect works by comparing your inputted string to strings in a dictionary, and finding the smallest Levenshtein distance to figure out which word in the dictionary you were most likely to have misspelled. Of course, other tools like context awareness are used, but this is the barebones feature.



## Levenshtein Distance

Say we are given two strings, A and B, with lengths i and j, respectively. The Levenshtein distance algorithm is recursively defined as follows:

$$lev_{A,B}(i,j) = \begin{cases} \max(i,j) & \text{if} \min(i,j)=0,\\ \min \begin{cases} lev_{A,B}(i-1,j)+1 \\ lev_{A,B}(i,j-1)+1 \\ lev_{A,B}(i-1,j-1)+1_{(A_i\neq B_j)} \end{cases} & \text{otherwise} \end{cases} $$

In English:

1. We look at the strings starting from their ends (indexing into them with i and j), and work our way backwards.
2. The base case occurs when we reach the beginning of either string (let’s call it “bottoming out”). Say that we want to turn string A to B, but bottom out on A first. To complete the transformation, we need to prepend the remaining length of B to A. The number of edits in the base case, then, is the difference in length of the two strings.
3. Otherwise, we try to either insert, remove, or delete a character. Whichever yields the minimum Levenshtein distance at that iteration, we take. Since the subproblems are recursively defined, we are guaranteed that picking the minimum Levenshtein distance in this step will help us get the minimum Levenshtein distance overall.

It’s apparent that we are going to analyze the cost of every insertion, removal, or deletion. In other words, we are going to have to solve every subproblem in the space. This sounds familiar—tabulation! Even though the algorithm was recursively defined, since we need the values of all subproblems anyway, we should implement the algorithm using tabulation. Here is what the subproblem space looks like, once the algorithm runs:

## Javascript Implementation

```js
// Authored by Jonah Schwartz, Awjin Ahn, 2015
function levenshtein (s_str, t_str)
{
    var s = s_str.split("");
    var t = t_str.split("");

    var s_len = s.length + 1;
    var t_len = t.length + 1;

    // Initialize table to store Levenshtein Distances
    var d = new Array(s_len);
    for (var i = 0; i < s_len; i++) {
        d[i] = new Array(t_len);
        for (var j = 0; j < t_len; j++) {
            d[i][j] = 0;
        }
    }
			
    for (var i = 1; i < s_len; i++) {
        d[i][0] = i;
    }
    for (var j = 1; j < t_len; j++) {
        d[0][j] = j;
    }

    for (var j = 1; j < t_len; j++) {
        for (var i = 1; i < s_len; i++) {
            if (s[i-1] === t[j-1]) {  // Base case
                d[i][j] = d[i-1][j-1];
            } else {  // Otherwise take whichever edit give min distance
                d[i][j] = Math.min(d[i-1][j] + 1,
                                   d[i][j-1] + 1, 
                                   d[i-1][j-1] + 1);
            }
        }
    }

    return d[s_len-1][t_len-1];
}
```

