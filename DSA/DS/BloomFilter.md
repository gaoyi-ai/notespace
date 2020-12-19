---
title: BloomFilter
date: 2020-12-19 16:33:27
categories:
- DSA
- DS
updated: 2020/12/19 17:00:14
---

# BloomFilter

◼ 如果要经常判断 1 个元素是否存在
使用哈希表（HashSet、HashMap），将元素作为 key 去查找
✓ 时间复杂度： O(1) ，但是空间利用率不高，需要占用比较多的内存资源

◼ 如果需要编写一个网络爬虫去爬10亿个网站数据，为了避免爬到重复的网站，如何判断某个网站是否爬过？
很显然，HashSet、HashMap 并不是非常好的选择

◼ 是否存在**时间复杂度低**、**占用内存较少**的方案？
布隆过滤器（Bloom Filter）

空间效率高的==概率型==数据结构，可以用来告诉你：一个元素==一定不存在==或者==可能存在==

◼ 优缺点
优点：空间效率和查询时间都远远超过一般的算法
缺点：有一定的误判率、删除困难

◼ 它实质上是一个很长的==二进制向量==和一系列随机映射函数（Hash函数）
◼ 常见应用
网页黑名单系统、垃圾邮件过滤系统、爬虫的网址判重系统、解决缓存穿透问题

## core

◼ 假设布隆过滤器由 20位二进制、 3 个哈希函数组成，每个元素经过哈希函数处理都能生成一个索引位置

添加元素：将每一个哈希函数生成的索引位置都设为 1

查询元素是否存在
✓ 如果有一个哈希函数生成的索引位置不为 1，就代表不存在（100%准确）
✓ 如果每一个哈希函数生成的索引位置都为 1，就代表存在（存在一定的误判率）

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201219182302505.png" alt="image-20201219182302505" style="zoom:67%;" />

◼ 添加、查询的时间复杂度都是： O(k) ， k 是哈希函数的个数。空间复杂度是： O(m) ， m 是二进制位的个数

## 误判率

◼ 误判率 p 受 3 个因素影响：二进制位的个数 m、哈希函数的个数 k、数据规模 n
◼ 已知误判率 p、数据规模 n，求二进制位的个数 m、哈希函数的个数 k

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201219182406486.png" alt="image-20201219182406486" style="zoom:50%;" />

## CODE

```java
public class BloomFilter<T> {
    /**
     * 二进制向量的长度(一共有多少个二进制位)
     */
    private int bitSize;
    /**
     * 二进制向量
     */
    private long[] bits;
    /**
     * 哈希函数的个数
     */
    private int hashSize;

    /**
     * @param n 数据规模
     * @param p 误判率, 取值范围(0, 1)
     */
    public BloomFilter(int n, double p) {
        if (n <= 0 || p <= 0 || p >= 1) {
            throw new IllegalArgumentException("wrong n or p");
        }

        double ln2 = Math.log(2);
        // 求出二进制向量的长度
        bitSize = (int) (-(n * Math.log(p)) / (ln2 * ln2));
        // 求出哈希函数的个数
        hashSize = (int) (bitSize * ln2 / n);
        // bits数组的长度
        // Long.SIZE = 64, long类型是64位
        bits = new long[(bitSize + Long.SIZE - 1) / Long.SIZE];
        // (bitSize + Long.SIZE - 1) / Long.SIZE = 向上取整
        // 分页问题
        // 每一页显示100条数据, pageSize
        // 一共有999999条数据, n
        // 请问有多少页 pageCount = (n + pageSize - 1) / pageSize
    }

    /**
     * 添加元素
     * @return 对二进制位有改变就返回true
     */
    public boolean put(T value) {
        nullCheck(value);

        // 利用value生成2个整数
        int hash1 = value.hashCode();
        int hash2 = hash1 >>> 16;

        boolean result = false;
        for (int i = 1; i <= hashSize; i++) {
            int combinedHash = hash1 + (i * hash2);
            if (combinedHash < 0) { // 保证>=0
                combinedHash = ~combinedHash;
            }
            // 生成一个二进位的索引
            int index = combinedHash % bitSize;
            // 设置index位置的二进位为1
            if (set(index)) result = true;

            //   101010101010010101
            // | 000000000000000100   1 << index
            //   101010111010010101
        }
        return result;
    }

    /**
     * 判断一个元素是否存在
     */
    public boolean contains(T value) {
        nullCheck(value);
        // 利用value生成2个整数
        int hash1 = value.hashCode();
        int hash2 = hash1 >>> 16;

        for (int i = 1; i <= hashSize; i++) {
            int combinedHash = hash1 + (i * hash2);
            if (combinedHash < 0) { // 保证>=0
                combinedHash = ~combinedHash;
            }
            // 生成一个二进位的索引
            int index = combinedHash % bitSize;
            // 查询index位置的二进位是否为0
            if (!get(index)) return false;
        }
        return true;
    }

    /**
     * 设置index位置的二进位为1
     */
    private boolean set(int index) {
        long value = bits[index / Long.SIZE];
        // 二进制位在long内部的索引，（虽然long内部是从右到左，其实只要保证找到唯一位置就ok）
        int bitValue = 1 << (index % Long.SIZE);
        bits[index / Long.SIZE] = value | bitValue;
        return (value & bitValue) == 0;
    }

    /**
     * 查看index位置的二进位的值
     * @return true代表1, false代表0
     */
    private boolean get(int index) {
        long value = bits[index / Long.SIZE];
        return (value & (1 << (index % Long.SIZE))) != 0;
    }

    private void nullCheck(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null.");
        }
    }
}
```

## TEST

```java
public class Main {

    public static void main(String[] args) {
        String[] urls = {};
        BloomFilter<String> bf = new BloomFilter<>(10_0000_0000, 0.01);

        for (String url : urls) { // 只可能少爬，不会重复爬
            // 如果爬过，那么一定返回true，执行continue
            // 如果没有，那么可能返回false
            if (bf.put(url) == false) continue;
            // 爬这个url
            // ......
        }

        /* 相当于
		for (String url : urls) {
			if (bf.contains(url)) continue;
			// 爬这个url
			// ......

			// 放进BloomFilter中
			bf.put(url);
		}*/
    }


    void test() {
//		BloomFilter<Integer> bf = new BloomFilter<>(1_00_0000, 0.01);
//		for (int i = 1; i <= 1_00_0000; i++) {
//			bf.put(i);
//		}
//		
//		int count = 0;
//		for (int i = 1_00_0001; i <= 2_00_0000; i++) {
//			if (bf.contains(i)) { // 误判的
//				count++;
//			}
//		}
//		System.out.println(count);
    }

}
```

