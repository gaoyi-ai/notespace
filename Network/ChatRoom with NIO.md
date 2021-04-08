---
title: ChatRoom with NIO
categories:
- IO
- Socket
tags:
- BIO
- NIO
- AIO
date: 2020/12/27 18:50:45
updated: 2020/12/27 20:00:13
---

# ChatRoom

## BIO 编程模型

acceptor 是阻塞的，那么新来的客户端是怎么处理得的？新建一个 Handler 的线程去处理，这样就不阻塞了当前的客户端了，所以第二个客户端就可以继续与服务器端发送消息。

![image-20201227202446634](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227202446634.png)

- `ChatHandler`是启动的一个新的线程，首先在这个线程里面添加客户端到客户端集合里面，

- 这个时候客户端输入的是一个阻塞的函数，为了同时能够收到消息，新建一个 `UserInputhandler`的线程，专门处理用户输入的线程。如果有输入，就发送给服务器端发送消息

- 用户发送 "quit" 消息的时候，这个时候服务器端就知道某个客户端要退出了，首先要做的事情就是先移除客户端列表，然后客户端关闭 socket

- 最外面的 loop 循环的大框，就是服务器的一个主要的循环，一直在等待 accept 函数的执行，如果有就新建一个 `Handler` 去进行处理

## NIO

### BIO 阻塞的部分

```
ServerSocket.accept()
InputStream.read(),OutputStream.write()
无法在同一个线程里处理多个Stream I/O
```

### NIO处理

```
使用Channel代替Stream
使用Selector监控多条Channel
可以在一个线程里处理多个Channel I/O
```

#### Buffer

Channel 是双向的，即可以读也可以写，那么向 Channel 读或者写都是需要通过 Buffer 来的，即Buffer也是可以读和写的

Buffer内部有三个主要的指针式的结构

- capacity：代表Buffer的容量大小
- position：指示目前所在的位置
- limit：初始的时候位置与capacity相同

###### 向 Buffer 写入数据

当创建一个 Buffer 对象的时候，最初的位置如下图

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227170915312.png)

往Buffer中写数据，空白格子部分假设是写入的数据

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227170935925.png)

然后想要读取刚刚写入的数据的时候需要用到 flip，把写变为读模式，如下图所示

- 调整 position指针位置到开始位置
- 调整limit指针位置到上次写入的结束位置

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227171009605.png)

###### 从 Buffer 读出数据

第一种读取的情况：一口气读完，读到limit指针的位置

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227171148516.png)

假设这个时候想写数据，这个时候又需要翻转了，用 clear()方法，如下所示

仔细一看，发现与刚开始创建的是一样的，其实只是移动了指针，并没有清除 buffer 里面的数据

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227171541767.png)

另外的一种读取的数据的情况：读取部分数据

后面未读的数据保留起来，以后来读，但是这个时候调整为写模式，希望的就是写完数据后，上次没有读完的数据依然能读取出来。compact() 方法就能达到这个目的

这里假设前3条数据是已读的，第4条数据是还没有来得及读完，但是又想在之后继续读取的数据。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227171439826.png)

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227171445104.png)

compact函数会把未读取的数据拷贝到整个Buffer的最开始的位置，也就是说，下次如果再要读数据的话，知道未读的数据肯定是出现在Buffer对象的最开始的位置。

之后会把position指针移动到未读的数据的接下来的位置

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227171456034.png)

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227171259577.png)

接下来，limit指针移动到与capacity同样的位置，之后由读模式翻转回写模式

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227171331051.png)

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227171345741.png)

之后再想写数据，就会从position所指的位置开始写，这样就代表了并不会覆盖掉上一次读取模式时还没有读完的数据。

#### Channel

假设这个时候已经有了 selector 了。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227171811251.png)

下图有一个 channel 变为 connect 的状态，拿到 selectionKey 就可以进一步拿到其对象。 

如下一步操作，第二个 channel 进入了 read 状态，第三个 channel 进入了 write 状态。如下图所示：

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227171821946.png)

 

### NIO编程模型

#### 使用selector来监视各个不同的channel

注册一个Accept事件，让selector监视Accept事件，当有一个客户端发送连接请求，server接受了连接请求的时候，相当于触发了这个Selector上的Accept事件，效果与BIO编程模型上的accept()函数的效果是一样的

### 1.1    NIO编程模型精讲

使用selector来监视各个不同的channel（不同的聊天室的客户）

注册一个Accept事件，让selector监视Accept事件，当有一个客户端发送连接请求，我们的服务接受了连接请求的时候，相当于触发了这个Selector上的Accept事件，效果与BIO编程模型上的accept()函数的效果是一样的

接受了客户端的连接，触发了Accept事件后使用handles来处理建立连接的客户端

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227172122464.png)

之后为新连接的客户端再到selector上注册一个read事件，相当于告诉selector有一个新的客户与我们建立了连接，selector要监视这个客户的socket channel通道上是否触发了read事件，即在客户向服务器发送了数据之后，那么在这个客户的socket channel上就有可供服务器读取的数据了。

接受了客户端的连接，触发了Accept事件后使用handles来处理建立连接的客户端

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227172134537.png)

之后为新连接的客户端再到selector上注册一个read事件，相当于告诉selector有一个新的客户与server建立了连接，selector要监视这个客户的socket channel通道上是否触发了read事件，即在客户向服务器发送了数据之后，那么在这个客户的socket channel上就有可供服务器读取的数据了。

## AIO

### 内核 IO 模型

#### 阻塞 I/O模型

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227172703515.png)

这种模型对应的是BIO模型

用户应用程序首先需要调用内核的函数，这样才可以去询问操作系统，

对于多人聊天室来说，若服务器想要知道客户端有没有向服务器端发送新的信息，就会去询问操作系统有没有收到新的数据，也许这个时候没有收到任何的数据，客户端那边用户还没有输入任何的文本，那怎么办呢？阻塞式的I/O模型，在这种情况下，系统调用就会被阻塞在那里，不会返回，而会一直在那里等待，直到有数据真的从网卡里面收到的数据，然后数据也已经拷贝到了操作系统内核对应的缓冲区，那么这个时候系统调用知道数据来了，接下来数据会被复制到应用程序所对应的缓冲区，然后这个数据就可以被应用程序真正进行接下来的操作，内核的系统调用的也就可以成功的返回。

这就是一个典型的阻塞式I/O模型。在整个等待网络中的数据到达以及把数据经过各种层面的复制，直到把数据成功的复制到应用系统可以直接进行操作的缓冲区，整个过程中系统调用全部都是被阻塞在这里的。在这个等待的过程中，做不了其他的事情，就只能在这里等待，直到数据被准备好。



#### 非阻塞式I/O

不停询问服务器数据有没有准备好，没有准备的好的时候直接返回，即非阻塞式的。这种模型对应的就是NIO模型，但是并不包括selector监听模式，仅仅是NIO中的非阻塞式模式。

 当应用程序进行系统调用，去询问内核说我们等待的数据有没有准备好？假如第一次查询的时候数据还没有传输过来的，还没有准备好，那么既然是非阻塞式的模型，系统调用并不会等待在这里，而是返回。当然这个返回的状态和有数据时的返回的状态是不同的，这时返回的状态会让应用程序知道现在想要的数据还没有准备好，还没有收到，那没关系，那可以过一会儿再去询问一次。如果这个时候还是没有准备好这个数据，又是马上返回了，不会阻塞。所以系统应用即使知道目前没有任何的数据是准备好的，仍然会立刻返回。那么过一会再问，假设这次询问的时候，数据已经成功的被网卡设备收到，并且已经被复制拷贝到内核的缓冲区了。那么接下来就可以像之前的那个I/O模型一样，把这部分数据从内核的缓冲区拷贝到应用程序可以进行操作的缓冲区。然后这次返回这个调用的就是一次成功返回，就是说这一次是真正的收到了想要的数据。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227173148754.png)

 

#### I/O多路复用

对应的就是多人聊天室中使用的selector的NIO的模式。

首先应用程序端发起新的询问是不是有数据可以进行操作了，如果数据没有准备好，并不会使用上边的纯粹的非阻塞式I/O模型里边所使用的那种不停的询问方法，而是说，告诉内核要求其来监听这个I/O通道，直到有了数据准备好在那里可以供应用程序进行操作了，再来通知。这个监听过程，就好像使用的select()函数，这个监听的过程，本身是阻塞式的，直到所监听的这个I/O真的收到了数据，而且这个数据已经在缓冲区已经准备好，可以进行复制。那么这个时候，监听的系统调用的就会返回给应用程序。说所监听的这个I/O有新的状态变化了。并不是把数据复制给应用程序。接下来还要再进行系统调用，把这个已经在内核缓冲区准备好的数据复制到应用程序可以操作的缓冲区。这时才是真正的取得了一直在等待的这个数据。那么这样的一个模型它叫做I/O的多路复用。

 

因为在这个时候，内核可以监听的不只是一个I/O，可以监听多个I/O，那么监听的这些I/O上面有任何一个I/O出现了状态的更新，有了新的动向，那么这个监听都会返回给应用程序说监听的一些I/O里面有一个或者多个出现了这个状态的变化。你要不要检查一下？然后就可以根据返回的可读条件的范围来确定再一次的系统调用，来真正的进行数据复制是针对哪些 I/O进行数据的复制。

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227173554949.png)

#### 异步I/O

首先，应用程序层面发起系统调用，询问内核有没有新的数据准备好，可以让应用程序进行接下的处理。没有准备好的话，就先返回。首先不会阻塞在这个地方。但是之后，虽然从应用程序层面，没有再发起新的系统就要用了。但是这不代表在后台什么事情都没有发生。相反的在这个时候。当关心的数据准备好了，比如数据通过网络发送到这台机器，并且已经拷贝到内核缓冲区之后。那么在后台在其他的进程里边，操作系统是继续的去做着I/O相关的事情的。操作系统不但会注意到等待的数据已经被拷贝到内核的缓冲区，操作系统还会把内核缓冲区的数据复制到系统应用程序所对应的缓冲区。然后当这一切事情都已经做完了，数据完全准备好之后，内核会递交一个信号，会使用这种方法来通知应用程序。 

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227173856233.png)

# WebServer

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201224211748432.png" style="zoom:67%;" />

- Server组件

    Tomcat服务器的最顶层组件
    负责运行Tomcat服务器
    负责加载服务器资源和环境变量

- Service组件

    集合Connector和Engine的抽象组件
    一个Server可以包含多个Service
    ·一个Service可以包含多个Connector和一个Engine
    
- Connector和Processor组件

    Connector提供基于不同特定协议的实现
    Connector接受解析请求，返回响应
    经Processor派遣请求至Engine进行处理

- Engine组件

    容器是Tomcat用来处理请求的组件
    容器内部的组件按照层级排列
    Engine是容器的顶层组件

- Host组件

    Host代表一个虚拟主机
    一个Engine可以支持对多个虚拟主机的请求
    Engine通过解析请求来决定将请求发送个哪一个Host

- Context组件

    Context代表一个Web Application
    Tomcat最复杂的组件之一
    应用资源管理，应用类加载，Servlet管理，安全管理等

- Wrapper组件

    Wrapper是容器的最底层组件
    包裹住Servlet实例
    负责管理Servlet实例的生命周期
