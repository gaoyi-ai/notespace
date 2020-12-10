---
title: DNA
categories:
- DSA
- Algorithm
tags:
- dp
date: 2019/8/1 20:00:14
updated: 2020/12/10 12:00:14
---

@[toc]

# 题目描述
DNA can be modeled as a string consisting of letters A,T,G and C, denoting the four diﬀerent nitrogen bases. To measure genetic similarity, we want to identify non-overlapping substrings that match given non-empty sequences, each of which carries a value, such that the total value of these sequences is maximized.

Example: Given the DNA string AGGCTAC and the (sequence,value) pairs (AGG, 2), (TAC, 2) and (GCTA, 3), we could match AGGCTAC for a total value of 2+2=4, or AGGCTAC for a total of 3. The following function for computing the maximal total value is provided:

# 递归

```python
def recursive(dna, sequences):
    max_total = 0 if len(dna) == 0 else recursive(dna[1:], sequences)
    if len(dna) == 0: return 0 # 如果dna已经为空，那么和最大为0
    for (seq, value) in sequences:
        if dna[:len(seq)] == seq: # dna前len个与seq相等，再去dna第len后面找
            total = value + recursive(dna[len(seq):], sequences)
            max_total = max(total, max_total)
    return max_total

# test with:
print(recursive("AGGCTAC", [("AGG", 2), ("TAC", 0), ("GCTA", 3)])) # 3
print(recursive("AGGCTAC", [("AGG", 2), ("TAC", 2), ("GCTA", 3)])) # 4

```

# 动态规划

```python
def recursive(dna: str, sequences:list):

    n = len(dna)
    sequences.sort(key = lambda x : len(x[0]))
    # max_totals of end with i
    dp = [0 for i in range(n)]
    for i in range(n):
        # end with i dna[i] 
        former = dna[:i+1]
        for (s, v) in sequences:
            if s not in former: continue
            
            # when s is not end with dna[i] then it should not be added twice
            greater = v + dp[i-len(s) -1] if former.endswith(s) else v

            # compare v + max_total of i- len(s) with max_total of end with i-1 
            # then update dp[i] to maintain max 
            dp[i] = max(greater, dp[i-1], dp[i])
    print(dp)
    return dp[n-1]


# test with:
print(recursive("AGGCTAC", [("AGG", 2), ("TAC", 0), ("GCTA", 3)])) # 3
print(recursive("AGGCTAC", [("AGG", 2), ("TAC", 2), ("GCTA", 3)])) # 4

```
