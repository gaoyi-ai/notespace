---
title: OSPF路由规划拓扑
categories:
- Network
tags:
- OSPF
- RIP
date: 2019/8/1 20:00:16
updated: 2020/12/10 12:00:16
---



@[toc]

# OSPF路由规划拓扑

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200521204216315.png)
# OSPF与RIP协议对比
显然，根据RIP协议，10.0.0.1 向 40.0.0.1 发送ICMP请求会

选择 10.0.0.0 -> 192.168.0.0 -> 40.0.0.0 路径（依据跳步数最少）

RIP协议依据距离矢量算法只是记录到达某一网络的距离（这里都是最短距离）

当到达某网络跳步相同时，会进行负载均衡


---

然而，根据OSPF协议，依据链路状态选择会决策代价（cost）

Cisco路由器判断代价为 100M带宽/链路带宽（小于1则取1）

- 选择 10.0.0.0 -> 20.0.0.0 -> 30.0.0.0 -> 40.0.0.0 的代价为 1+1+1
- 而选择10.0.0.0 -> 192.168.0.0 -> 40.0.0.0 的代价为 1+ 明显大于1的链路(>>1)


观察OSPF生成的链路数据库，前去 192.168.0.0 有两段cost相同链路，下一跳地址分别 20.0.0.1 和 30.0.0.1 ，所以路由器会进行负载均衡


# 通配符掩码

通配符掩码是一个由比特组成的掩码，表示IP地址的哪些部分可供检查。在Cisco IOS中，它们被用在几个地方，例如：
- 用于指示某些路由协议（如OSPF）的网络或子网的大小。
- 用于指示访问控制列表（ACL）中应该允许或拒绝哪些IP地址。

在简单的层面上，通配符掩码可以被认为是一个倒置的子网掩码。
通配符掩码是一种匹配规则，通配符掩码的规则是:
- 0表示等效位必须匹配
- 1表示等效位无所谓

OSPF宣告网段的命令是：Network + IP + wildcard bits， 通过 IP 和 wildcard bits 筛选出一组IP地址，从而定位出需要开启OSPF的接口范围(谁拥有其中一个IP地址谁就开启OSPF)。

例如： `network` `10.1.1.0` `0.0.0.255` `area 0`，这个network命令实际上宣告了10.1.1.0-10.1.1.255 这256个地址
但通常要求宣告的准确性，即如果OSPF互联地址是10.1.1.1/30，则需要宣告`network` `10.1.1.0` `0.0.0.3``area 0`，这个3换算二进制是 00000011，其中1表示任意数字，所以限定了10.1.1.0——10.1.1.3这4个IP地址。

> [Wildcard mask](https://en.wikipedia.org/wiki/Wildcard_mask)

