---
title: WebSocket 是什么原理？为什么可以实现持久连接？
categories:
- Web
- WebSocket
tags:
- websocket
date: 2021/8/11
---



> https://www.zhihu.com/question/20215561

# WebSocket 是什么原理？为什么可以实现持久连接？

**Websocket是什么样的协议，具体有什么优点**

首先，Websocket是一个**持久化**的协议，相对于HTTP这种**非持久**的协议来说。

简单的举个例子吧，用目前应用比较广泛的PHP生命周期来解释。

1) HTTP的生命周期通过Request来界定，也就是一个Request 一个Response，那么在HTTP1.0中，这次HTTP请求就结束了。
2) 在HTTP1.1中进行了改进，使得有一个keep-alive，也就是说，在一个HTTP连接中，可以发送多个Request，接收多个Response。
    但是请记住 Request = Response ， 在HTTP中永远是这样，也就是说一个request只能有一个response。而且这个response也是**被动**的，不能主动发起。

首先Websocket是基于HTTP协议的，或者说**借用**了HTTP的协议来完成一部分握手。

> 但纠正一下：『首先Websocket是基于HTTP协议的，或者说借用了HTTP的协议来完成一部分握手』
>
> 前半句不正确，WebSocket 是基于 TCP 的。RFC6455 一开始就有定义：
>
> 『The protocol consists of an opening handshake followed by basic message framing, layered over TCP.』
>
> TCP 就像是跑在铁路上的火车，而 WebSocket 和 HTTP 像是火车上面的人，只是 WebSocket 和 HTTP 长得有点相似。
>
> 而正因为 WebSocket 和 HTTP 长得比较像，所以要在 WebSocket 的头上面标明：
>
> Upgrade: websocket
> Connection: Upgrade

在握手阶段是一样的，首先我们来看个典型的Websocket握手

```text
GET /chat HTTP/1.1
Host: server.example.com
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Key: x3JJHMbDL1EzLkh9GBhXDw==
Sec-WebSocket-Protocol: chat, superchat
Sec-WebSocket-Version: 13
Origin: http://example.com
```

```text
Upgrade: websocket
Connection: Upgrade
```

```text
Sec-WebSocket-Key: x3JJHMbDL1EzLkh9GBhXDw==
Sec-WebSocket-Protocol: chat, superchat
Sec-WebSocket-Version: 13
```

首先，Sec-WebSocket-Key 是一个Base64 encode的值，这个是浏览器随机生成的。

然后，Sec_WebSocket-Protocol 是一个用户定义的字符串，用来区分同URL下，不同的服务所需要的协议。

最后，Sec-WebSocket-Version 是告诉服务器所使用的Websocket Draft（协议版本），在最初的时候，Websocket协议还在 Draft 阶段，各种奇奇怪怪的协议都有，而且还有很多期奇奇怪怪不同的东西，什么Firefox和Chrome用的不是一个版本之类的，当初Websocket协议太多可是一个大难题。

然后服务器会返回下列东西，表示已经接受到请求， 成功建立Websocket啦！

```text
HTTP/1.1 101 Switching Protocols
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Accept: HSmrc0sMlYUkAGmm5OPpG2HaGWk=
Sec-WebSocket-Protocol: chat
```

这里开始就是HTTP最后负责的区域了，告诉客户，我已经成功切换协议啦~

```text
Upgrade: websocket
Connection: Upgrade
```

依然是固定的，告诉客户端即将升级的是Websocket协议，而不是mozillasocket，lurnarsocket或者shitsocket。
然后，Sec-WebSocket-Accept 这个则是经过服务器确认，并且加密过后的 Sec-WebSocket-Key

后面的，Sec-WebSocket-Protocol 则是表示最终使用的协议。

---

你可以把 WebSocket 看成是 HTTP 协议为了支持长连接所打的一个大补丁，它和 HTTP 有一些共性，是为了解决 HTTP 本身无法解决的某些问题而做出的一个改良设计。在以前 HTTP 协议中所谓的 keep-alive connection 是指在一次 TCP 连接中完成多个 HTTP 请求，但是对每个请求仍然要单独发 header；所谓的 polling 是指从客户端（一般就是浏览器）不断主动的向服务器发 HTTP 请求查询是否有新数据。这两种模式有一个共同的缺点，就是除了真正的数据部分外，服务器和客户端还要大量交换 HTTP header，信息交换效率很低。它们建立的“长连接”都是伪.长连接，只不过好处是不需要对现有的 HTTP server 和浏览器架构做修改就能实现。

WebSocket 解决的第一个问题是，通过第一个 HTTP request 建立了 TCP 连接之后，之后的交换数据都不需要再发 HTTP request了，使得这个长连接变成了一个真.长连接。但是不需要发送 HTTP header就能交换数据显然和原有的 HTTP 协议是有区别的，所以它需要对服务器和客户端都进行升级才能实现。在此基础上 WebSocket 还是一个双通道的连接，在同一个 TCP 连接上既可以发也可以收信息。此外还有 multiplexing 功能，几个不同的 URI 可以复用同一个 WebSocket 连接。这些都是原来的 HTTP 不能做到的。