---
title: Sequence
date: 2020-12-20 22:33:27
categories:
- DSA
- DS
tags:
- KMP
updated: 2020/12/20 23:00:14
---

# Sequence

串是开发中非常熟悉的字符串，是由若干个字符组成的有限序列
String text = "Thank";

◼ 字符串 thank 的前缀（prefix）、真前缀（proper prefix）、后缀（suffix）、真后缀（proper suffix）

前缀 t, th, tha, than, thank
真前缀 t, th, tha, than
后缀 thank, hank, ank, nk, k
真后缀 hank, ank, nk, k

`tlen` 代表文本串 text 的长度，`plen`代表模式串 pattern 的长度

## 蛮力（Brute Force）
◼ 以字符为单位，从左到右移动模式串，直到匹配成功
<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220205209675.png" alt="image-20201220205209675" style="zoom:67%;" />
◼ 蛮力算法有 2 种常见实现思路

### 蛮力1 – 执行过程

◼ pi 的取值范围 [0, plen)
◼ ti 的取值范围 [0, tlen)

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220205648385.png" alt="image-20201220205648385" style="zoom:67%;" />

匹配成功 

口`pi++` 
口`ti++`

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220205702957.png" alt="image-20201220205702957" style="zoom:67%;" />

匹配失败 

口`ti –= pi – 1`即ti - 前面匹配成功的字符，再向后移一位
口`pi = 0`

`pi == plen`代表匹配成功

```java
    public static int indexOf(String text, String pattern) {
        if (text == null || pattern == null) return -1;
        int tlen = text.length();
        int plen = pattern.length();
        if (tlen == 0 || plen == 0 || tlen < plen) return -1;

        int pi = 0, ti = 0;
        while (pi < plen && ti < tlen) {
            if (text.charAt(ti) == pattern.charAt(pi)) {
                ti++;
                pi++;
            } else {
                ti -= pi - 1;
                pi = 0;
            }
        }
        return pi == plen ? ti - pi : -1;
    }
```

### 蛮力1 – 优化
◼ 此前实现的蛮力算法，在恰当的时候可以提前退出，减少比较次数

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220210201612.png" alt="image-20201220210201612" style="zoom:67%;" />

◼ 因此，ti 的退出条件可以从 `ti < tlen` 改为
`ti – pi <= tlen – plen`
ti – pi 是指每一轮比较中 text 首个比较字符的位置

```java
    public static int indexOf(String text, String pattern) {
        if (text == null || pattern == null) return -1;
        int tlen = text.length();
        int plen = pattern.length();
        if (tlen == 0 || plen == 0 || tlen < plen) return -1;

        int pi = 0, ti = 0;
        int tmax = ti;
        while (pi < plen && ti - pi < tmax) {
            if (text.charAt(ti) == pattern.charAt(pi)) {
                ti++;
                pi++;
            } else {
                ti -= pi - 1;
                pi = 0;
            }
        }
        return pi == plen ? ti - pi : -1;
    }
```

### 蛮力2 – 执行过程

◼ pi 的取值范围 [0, plen)
◼ ti 的取值范围 [0, tlen – plen]

ti的含义：每一轮比较中，文本串首个比较字符的位置

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220210344596.png" alt="image-20201220210344596" style="zoom:67%;" />

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220210423139.png" alt="image-20201220210423139" style="zoom:67%;" />

匹配失败 
口 `pi = 0`
口 `ti++`

`pi == plen`代表匹配成功

```java
    public static int indexOf(String text, String pattern) {
        if (text == null || pattern == null) return -1;
        int tlen = text.length();
        int plen = pattern.length();
        if (tlen == 0 || plen == 0 || tlen < plen) return -1;

        int tmax = tlen - plen;
        for (int ti = 0; ti <= tmax; ti++) {
            int pi = 0;
            for (; pi < plen; pi++) {
                if (text.charAt(ti + pi) != pattern.charAt(pi)) break;
                if (pi == plen) return ti;
            }
        }
        return -1;
    }
```

### 蛮力 – 性能分析
◼ n 是文本串长度，m 是模式串长度

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220210700757.png" alt="image-20201220210700757" style="zoom:67%;" />

### 蛮力 – 性能分析
◼ 最好情况
只需一轮比较就完全匹配成功，比较 m 次（ m 是模式串的长度）
时间复杂度为 O(m)
![image-20201220210740321](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220210740321.png)

◼ 最坏情况（字符集越大，出现概率越低）
执行了 n – m + 1 轮比较（ n 是文本串的长度）
每轮都比较至模式串的末字符后失败（ m – 1 次成功，1 次失败）
时间复杂度为 O(m ∗ (n − m + 1)) ，由于一般 m 远小于 n，所以为 O(mn)
![image-20201220210752382](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220210752382.png)

## KMP

### 蛮力 vs KMP

◼ 对比蛮力算法，KMP的精妙之处：充分利用了此前比较过的内容，可以很聪明地跳过一些不必要的比较位置

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220210904234.png" alt="image-20201220210904234" style="zoom:67%;" />

### KMP – next表的使用
◼ KMP 会预先根据模式串的内容生成一张 next 表（一般是个数组）

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220211115368.png" alt="image-20201220211115368" style="zoom:80%;" />

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220211153584.png" alt="image-20201220211153584" style="zoom:80%;" />

### KMP – 核心原理

![image-20201220211238174](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220211238174.png)

◼ 当 d、e 失配时，如果希望 pattern 能够一次性向右移动一大段距离，然后直接比较 d、c 字符
前提条件是 A 必须等于 B

◼ 所以 KMP 必须在失配字符 e 左边的子串中找出符合条件的 A、B，从而得知向右移动的距离

◼ 向右移动的距离：e左边子串的长度 – A的长度，等价于：e的索引 – c的索引

◼ 且 `c的索引 == next[e的索引]`，所以向右移动的距离：`e的索引 – next[e的索引]`

◼ 总结

- 如果在 pi 位置失配，向右移动的距离是 `pi – next[pi]`，即失配之后，查找左边子串中的前缀与后缀完全相同的；若有，则滑动至前缀与后缀对齐，所以 `next[pi]` 越小，移动距离越大

- `next[pi]` 是 pi 左边子串的真前缀后缀的最大公共子串长度

### KMP – 真前缀后缀的最大公共子串长度
<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220211612937.png" alt="image-20201220211612937" style="zoom:80%;" />

### KMP – 得到next表
![image-20201220211652666](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220211652666.png)

◼ 将最大公共子串长度都向后移动 1 位，首字符设置为 负1，就得到了 next 表

![image-20201220211705217](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220211705217.png)

### KMP – 负1的精妙之处
<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220211813601.png" alt="image-20201220211813601" style="zoom:80%;" />

`pi = next[pi]` → -1
`pi++` → 0
`ti++` → 3

◼ 相当于在负1位置有个假想的通配字符（哨兵）
匹配成功后`ti++`、`pi++`

```java
    public static int indexOf(String text, String pattern) {
        if (text == null || pattern == null) return -1;
        int plen = pattern.length();
        int tlen = text.length();
        if (tlen == 0 || plen == 0 || tlen < plen) return -1;

        int[] next = next(pattern);
        int pi = 0, ti = 0;
        int tmax = tlen - plen;
        while (pi < plen && ti - pi <= tmax) {
            if (pi < 0 || text.charAt(ti) == pattern.charAt(pi)) {
                ti++;
                pi++;
            } else {
                pi = next[pi];
            }
        }
        return pi == plen ? ti - pi : -1;
    }
```

### KMP – 为什么是“最大“公共子串长度？
◼ 假设文本串是AAAAABCDEF，模式串是AAAAB

![image-20201220212049641](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220212049641.png)

◼ 应该将1、2、3中的哪个值赋值给 pi 是正确的？

![image-20201220212114929](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220212114929.png)

◼ 将 3 赋值给 pi
向右移动了 1 个字符单位，最后成功匹配

◼ 将 1 赋值给 pi
向右移动了 3 个字符单位，错过了成功匹配的机会

◼ 公共子串长度越小，向右移动的距离越大，越不安全
◼ 公共子串长度越大，向右移动的距离越小，越安全

### KMP – next表的构造思路
![image-20201220212221699](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220212221699.png)

◼ 已知 `next[i] == n`，即i 左侧的真前缀后缀的最大公共子串为n
① 如果 `pattern.charAt(i) == pattern.charAt(n)`
那么 `next[i + 1] == n + 1`

② 如果` pattern.charAt(i) != pattern.charAt(n)`
已知 `next[n] == k`
如果 `pattern.charAt(i) == pattern.charAt(k)`
✓ 那么 `next[i + 1] == k + 1`
如果`pattern.charAt(i) != pattern.charAt(k)`
✓ 将 k 代入 n ，重复执行 ②

```java
    public static int[] next(String pattern) {
        int len = pattern.length();
        int[] next = new int[len];
        int i = 0;
        int n = next[i] = -1;
        int imax = len - 1;
        while (i < imax) {
            if (n < 0 || pattern.charAt(i) == pattern.charAt(n)) {
                next[++i] = ++n;
            } else {
                n = next[n];
            }
        }
        return next;
    }
```

### KMP – next表的不足之处
◼ 假设文本串是 AAABAAAAB ，模式串是 AAAAB

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220212414348.png" alt="image-20201220212414348" style="zoom:80%;" />

◼ 在这种情况下，KMP显得比较笨拙

### KMP – next表的优化思路

◼ 已知：`next[i] == n`，`next[n] == k`

![image-20201220212538401](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220212538401.png)

◼ 如果 `pattern[i] != d`，就让模式串滑动到 next[i]（也就是n）位置跟 d 进行比较
◼ 如果 `pattern[n] != d`，就让模式串滑动到 next[n]（也就是k）位置跟 d 进行比较
◼ 如果 `pattern[i] == pattern[n]`，那么当 i 位置失配时，模式串最终必然会滑到 k 位置跟 d 进行比较
所以 `next[i]` 直接存储 `next[n]`（也就是k）即可

```java
    public static int[] next(String pattern) {
        int len = pattern.length();
        int[] next = new int[len];
        int i = 0;
        int n = next[i] = -1;
        int imax = len - 1;
        while (i < imax) {
            if (n < 0 || pattern.charAt(i) == pattern.charAt(n)) {
                i++;
                n++;
                if (pattern.charAt(i) == pattern.charAt(n)) {
                    next[i] = next[n];
                } else {
                    next[i] = n;
                }
            } else {
                n = next[n];
            }
        }
        return next;
    }
```

### KMP – next表的优化效果

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220212631550.png" alt="image-20201220212631550" style="zoom:80%;" />

### KMP – 性能分析

◼ KMP 主逻辑
最好时间复杂度： O(m) 
最坏时间复杂度： O(n) ，不超过 O(2n)

◼ next 表的构造过程跟 KMP 主体逻辑类似
时间复杂度： O(m)

◼ KMP 整体
最好时间复杂度： O(m) 
最坏时间复杂度： O(n + m) 
空间复杂度： O(m)

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220212703118.png" alt="image-20201220212703118" style="zoom:67%;" />

### 蛮力 vs KMP
◼ 蛮力算法为何低效？

◼ 当字符失配时

蛮力算法
✓ ti 回溯到左边位置
✓ pi 回溯到 0

KMP 算法
✓ ti 不必回溯
✓ pi 不一定要回溯到 0

## Boyer-Moore算法

口最好时间复杂度：O(n/m) ,最坏时间复杂度：O(n+m)
口该算法从模式串的尾部开始匹配（自后向前）

BM算法的移动字符数是通过2条规则计算出的最大值
口坏字符规则（Bad Character,简称BC)
口好后缀规则（Good Suffix,简称GS)

### BC

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220203458717.png" alt="image-20201220203458717" style="zoom:67%;" />

当Pattern中的字符E和Text中的S失配时，称S为“坏字符”
口如果Pattern的未匹配子串中不存在坏字符，直接将Pattern移动到坏字符的下一位
口否则，让Pattern的未匹配子串中最靠右的坏字符与Text中的坏字符对齐

### GS

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220203841330.png" alt="image-20201220203841330" style="zoom:67%;" />

"MPLE”是一个成功匹配的后缀，“E"、"LE"、"PLE"、"MPLE"都是"好后缀"
口如果Pattern中找不到与好后缀对齐的子串，直接将Pattern移动到好后缀的下一位
口否则，从Pattern中找出子串与Text中的好后缀对齐

### BM 最好最坏情况

#### 最好情况

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220204914194.png" alt="image-20201220204914194" style="zoom:67%;" />

时间复杂度：O(n/m)

#### 最坏情况

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220204752991.png" alt="image-20201220204752991" style="zoom:67%;" />

时间复杂度：O(n+m)
口其中的O(m)是构造BC、GS表

## Rabin-Karp算法

或Karp-Rabin算法, 简称RK算法，是一种基于hash的字符串匹配算法

大致原理
口将Pattern的hash值与Text中每个子串的hash值进行比较
口某一子串的hash值可以根据上一子串的hash值在0(1)时间内计算出来

## Sunday算法

口从前向后匹配
当匹配失败时，关注的是Text中参与匹配的子串的下一位字符A
如果A没有在Pattern中出现，则直接跳过，即移动位数=Pattern长度+1
否则，让Pattern中最靠右的A与Text中的A对齐

![image-20201220204333686](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201220204333686.png)