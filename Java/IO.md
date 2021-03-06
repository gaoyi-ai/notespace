---
title: IO
date: 2020-12-14 18:50:45
categories:
- Java
tags:
- io
updated: 2020/12/14 20:00:13
---

# IO

- IO 不仅仅是对文件的操作，网络编程中，比如 Socket 通信，都是典型的 IO 操作目标。
- 输入流、输出流（InputStream/OutputStream）是用于读取或写入字节的，例如操作图片文件。
- 而 Reader/Writer 则是用于操作字符，增加了字符编解码等功能，适用于类似从文件中读取或者写入文本信息。本质上计算机操作的都是字节，不管是网络通信还是文件读取，Reader/Writer 相当于构建了应用逻辑和原始数据之间的桥梁。
- BufferedOutputStream 等带缓冲区的实现，可以避免频繁的磁盘读写，进而提高 IO 处理效率。这种设计利用了缓冲区，将批量数据进行一次操作，但在使用中千万别忘了 flush。

## java.io

传统的 java.io 包，它基于流模型实现，提供了 IO 功能，比如 File 抽象、输入输出流等。交互方式是同步、阻塞的方式，也就是说，在读取输入流或者写入输出流时，在读、写动作完成之前，线程会一直阻塞在那里，它们之间的调用是可靠的线性顺序。

java.io 包的好处是代码比较简单、直观，缺点则是 IO 效率和扩展性存在局限性，容易成为应用性能的瓶颈。

很多时候，人们也把 java.net 下面提供的部分网络 API，比如 Socket、ServerSocket、HttpURLConnection 也归类到同步阻塞 IO 类库，因为网络通信同样是 IO 行为。

## InputStream / OutputStream

FileInputStream没有任何读入数值类型的方法，DataInputStream也没有任何从文件中获取数据的方法。

Java使用了一种灵巧的机制来分离这两种职责。某些输入流（例如FileInputStream和由URL类的openStream方法返回的输入流）可以从文件和其他更外部的位置上获取字节，而其他的输入流（例如DataInputstream)可以将字节组装到更有用的数据类型中。Java程序员必须对二者进行组合。

例如，为了从文件中读人数字，首先需要创建一个FileInputStream,然后将其传递DataInputStream的构造器：

```java
FileInputStrean fin = new FileInputStream("employee.dat");
DataInputStream din = new DataInputStream(fin);
double x=din.readDouble();
```

## Reader / Writer

在存储文本字符串时，需要考虑字符编码（character encoding)方式。在Java内部使用的UTF-16编码方式中，字符串“1234”编码为00 31 00 32 00 33 00 34十六进制）。

# Serializable

Serializable接口没有任何方法，因此你不需要对这些类做任何改动。

```java
	ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("employee.dat"));
    Employee harry = new Employee("Harry Hacker", 50000, 1989, 10, 1); 
    Manager boss = new Manager("Carl Cracker", 80000, 1987, 12, 15); 
    out.writeObject(harry); 
    out.writeObject(boss);  
```

在幕后，是0bjectoutputstream在浏览对象的所有域，并存储它们的内容。例如，当写出一个Employee对象时，其名字、日期和薪水域都会被写出到输出流中。

但是，有一种重要的情况需要考虑：当一个对象被多个对象共享，作为它们各自状态的一部分时，会发生什么呢？

现在每个Manager对象都包含一个表示秘书的Employee对象的引用，当然，两个经理可以共用一个秘书，正如图2-5和下面的代码所示的那样：

```java
    harry = new Employee("Harry Hacker", . . .); 
    Manager carl = new Manager("Carl Cracker", . . .); 
    carl.setSecretary(harry); 
      Manager tony = new Manager("Tony Tester", . . .); 
    tony.setSecretary(harry);  
```

保存这样的对象网络是一种挑战，在这里我们当然不能去保存和恢复秘书对象的内存地址，因为当对象被重新加载时，它可能占据的是与原来完全不同的内存地址。

与此不同的是，每个对象都是用一个序列号（serial number)保存的，这就是这种机制之所以称为对象序列化的原因。下面是其算法：

- 对你遇到的每一个对象引用都关联一个序列号（如图2-6所示）。

- 对于每个对象，当第一次遇到时，保存其对象数据到输出流中。

- 如果某个对象之前已经被保存过，那么只写出“与之前保存过的序列号为x的对象相同”。

- 在读回对象时，整个过程是反过来的。

    对于对象输入流中的对象，在第一次遇到其序列号时，构建它，并使用流中数据来初始化它，然后记录这个顺序号和新对象之间的关联。

- 当遇到“与之前保存过的序列号为x的对象相同”标记时，获取与这个顺序号相关联的对象引用。

## 修改默认的序列化机制

某些数据域是不可以序列化的，例如，只对本地方法有意义的存储文件句柄或窗口句柄的整数值，这种信息在稍后重新加载对象或将其传送到其他机器上时都是没有用处的。事实上，这种域的值如果不恰当，还会引起本地方法崩溃。

Java拥有一种很简单的机制来防止这种域被序列化，那就是将它们标记成是transient的。如果这些域属于不可序列化的类你也需要将它们标记成transient的。瞬时的域在对象被序列化时总是被跳过的。
序列化机制为单个的类提供了一种方式，去向默认的读写行为添加验证或任何其他想要的行为。可序列化的类可以定义具有下列签名的方法：

```java
    private void readObject(ObjectInputStream in) 
          throws IOException, ClassNotFoundException; 
    private void writeObject(ObjectOutputStream out) 
          throws IOException;  
```

之后，数据域就再也不会被自动序列化，取而代之的是调用这些方法。

下面是一个典型的示例。在java.awt.geom包中有大量的类都是不可序列化的，例如
Point2D.Double。现在假设你想要序列化一个LabeledPoint类，它存储了一个String和一个Point2D.Double。首先，你需要将Point2D.Double标记成transient,以避免抛出NotSerializableException。

```java
public class LabeledPoint implements Serializable{
	private String label;
	private transient Point2D.Double point;
}
```

在writeObject方法中，我们首先通过调用defaultWriteObject方法写出对象描述符和String域label,这是0bjectoutputstream类中的一个特殊的方法，它只能在可序列化类的writeobject方法中被调用。然后，我们使用标准的DataOutput调用写出点的坐标。

```java
    private void writeObject(ObjectOutputStream out) 
          throws IOException 
    { 
          out.defaultWriteObject(); 
          out.writeDouble(point.getX()); 
          out.writeDouble(point.getY()); 
    }  
```

在readObject方法中，我们反过来执行上述过程：

```java
    private void readObject(ObjectInputStream in) 
          throws IOException 
    { 
          in.defaultReadObject(); 
          double x = in.readDouble(); 
          double y = in.readDouble(); 
          point = new Point2D.Double(x, y); 
    }  
```

### 对包含冗余对象信息的序列化

另一个例子是java.util.Date类，它提供了自己的readobject和write0bject方法，这些方法将日期写出为从纪元（UTC时间1970年1月1日0点）开始的毫秒数。Date类有一个复杂的内部表示，为了优化查询，它存储了一个Calendar对象和一个毫秒计数值。Calendar的状态是沉余的，因此并不需要保存。
readobject和writeobject方法**只需要保存和加载它们的数据域**，而**不需要关心超类数据和任何其他类信息**。

除了让序列化机制来保存和恢复对象数据，类还可以定义它自己的机制。为了做到这一点，这个类必须实现Externalizable接口

与前面一节描述的readobject和writeobject不同，这些方法**对包括超类数据在内的整个对象的存储和恢复负全责**。在写出对象时，序列化机制在输出流中仅仅只是记录该对象所属的类。在读人可外部化的类时，对象输入流将用无参构造器创建一个对象，然后调用readExternal方法。下面展示了如何为Employee类实现这些方法：

```java
public void readExternal(ObjectInput s) 
      throws IOException  
          { 
                  name = s.readUTF(); 
                  salary = s.readDouble(); 
                  hireDay = new Date(s.readLong()); 
          }  
public void writeExternal(ObjectOutput s) 
      throws IOException 
          { 
                s.writeUTF(name); 
                s.writeDouble(salary); 
                s.writeLong(hireDay.getTime()); 
          }  
```
### 对单例的序列化

序列化单例和类型安全的枚举

在序列化和反序列化时，如果目标对象是唯一的，那么你必须加倍当心，这通常会在实现**单例**和类型安全的枚举时发生。

如果你使用Java语言的enum结构，那么你就不必担心序列化，它能够正常工作。但是，假设你在维护遗留代码，其中包含下面这样的枚举类型：

```java
    public class Orientation 
    { 
          public static final Orientation HORIZONTAL = new Orientation(1); 
          public static final Orientation VERTICAL = new Orientation(2);  
          private int value;  
          private Orientation(int v) { value = v; } 
      }  
```

当类型安全的枚举实现Serializable接口时，你必须牢记存在着一种重要的变化，此时，默认的序列化机制是不适用的。假设我们写出一个Orientation类型的值，并再次将其读回：

```java
    Orientation original = Orientation.HORIZONTAL; 
    ObjectOutputStream out = . . .; 
    out.write(original); 
    out.close(); 
    ObjectInputStream in = . . .; 
    Orientation saved = (Orientation) in.read();  
// Now the test 
    if (saved == Orientation.HORIZONTAL) . . .  
```

test将失败。事实上，saved的值是Orientation类型的一个全新的对象，它与任何预定义的常量都不等同。即使构造器是私有的，序列化机制也可以创建新的对象！

为了解决这个问题，你需要定义另外一种称为readResolve的特殊序列化方法。如果定义了readResolve方法，在对象被序列化之后就会调用它。它必须返回一个对象，而该对象之后会成为readobject的返回值。在上面的情况中，readResolve方法将检查value域并返回恰当的枚举常量：

```java
    protected Object readResolve() throws ObjectStreamException 
    { 
          if (value == 1) return Orientation.HORIZONTAL; 
          if (value == 2) return Orientation.VERTICAL; 
          return null; // this shouldn't happen 
    }  
```

请记住向遗留代码中所有类型安全的枚举以及向所有支持**单例**设计模式的类中添加readResolve方法。





## java.nio

首先，熟悉一下 NIO 的主要组成部分：

- Buffer，高效的数据容器，除了布尔类型，所有原始数据类型都有相应的 Buffer 实现。

- Channel，类似在 Linux 之类操作系统上看到的文件描述符，是 NIO 中被用来支持批量式 IO 操作的一种抽象。

    File 或者 Socket，通常被认为是比较高层次的抽象，而 Channel 则是更加操作系统底层的一种抽象，这也使得 NIO 得以充分利用现代操作系统底层机制，获得特定场景的性能优化，例如，DMA（Direct Memory Access）等。不同层次的抽象是相互关联的，我们可以通过 Socket 获取 Channel，反之亦然。

- Selector，是 NIO 实现多路复用的基础，它提供了一种高效的机制，可以检测到注册在 Selector 上的多个 Channel 中，是否有 Channel 处于就绪状态，进而实现了单线程对多 Channel 的高效管理。

Selector 同样是基于底层操作系统机制，不同模式、不同版本都存在区别，例如，在最新的代码库里，相关实现如下：Linux 上依赖于 epoll（http://hg.openjdk.java.net/jdk/jdk/file/d8327f838b88/src/java.base/linux/classes/sun/nio/ch/EPollSelectorImpl.java）。

Windows 上  AIO 模式则是依赖于 iocp（http://hg.openjdk.java.net/jdk/jdk/file/d8327f838b88/src/java.base/windows/classes/sun/nio/ch/Iocp.java）。

- Chartset，提供 Unicode 字符串定义，NIO 也提供了相应的编解码器等，例如，通过下面的方式进行字符串到 ByteBuffer 的转换：

```
Charset.defaultCharset().encode("Hello world!"));
```

### NIO 能解决什么问题

设想需要实现一个服务器应用，只简单要求能够同时服务多个客户端请求即可。

使用 java.io 和 java.net 中的同步、阻塞式 API，可以简单实现。

```java
public class DemoServer extends Thread {
    private ServerSocket serverSocket;
    public int getPort() {
        return  serverSocket.getLocalPort();
    }
    public void run() {
        try {
            serverSocket = new ServerSocket(0);
            while (true) {
                Socket socket = serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(socket);
                requestHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ;
            }
        }
    }
    public static void main(String[] args) throws IOException {
        DemoServer server = new DemoServer();
        server.start();
        try (Socket client = new Socket(InetAddress.getLocalHost(), server.getPort())) {
            BufferedReader bufferedReader = new BufferedReader(new                   InputStreamReader(client.getInputStream()));
            bufferedReader.lines().forEach(s -> System.out.println(s));
        }
    }
 }
// 简化实现，不做读取，直接发送字符串
class RequestHandler extends Thread {
    private Socket socket;
    RequestHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream());) {
            out.println("Hello world!");
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 }
```

其实现要点是：

- 服务器端启动 ServerSocket，端口 0 表示自动绑定一个空闲端口。
- 调用 accept 方法，阻塞等待客户端连接。
- 利用 Socket 模拟了一个简单的客户端，只进行连接、读取、打印。
- 当连接建立后，启动一个单独线程负责回复客户端请求。

这样，一个简单的 Socket 服务器就被实现出来了。

这个解决方案在扩展性方面，可能存在什么潜在问题呢？

 Java 语言目前的线程实现是比较重量级的，启动或者销毁一个线程是有明显开销的，每个线程都有单独的线程栈等结构，需要占用非常明显的内存，所以，每一个 Client 启动一个线程似乎都有些浪费。

那么，稍微修正一下这个问题，引入**线程池**机制来避免浪费。

```java
serverSocket = new ServerSocket(0);
executor = Executors.newFixedThreadPool(8);
 while (true) {
    Socket socket = serverSocket.accept();
    RequestHandler requestHandler = new RequestHandler(socket);
    executor.execute(requestHandler);
}
```

这样做似乎好了很多，通过一个固定大小的线程池，来负责管理工作线程，避免频繁创建、销毁线程的开销，这是构建并发服务的典型方式。这种工作方式，可以参考下图来理解。

![socket_threadpool](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/socket_threadpool.png)

如果连接数并不是非常多，只有最多几百个连接的普通应用，这种模式往往可以工作的很好。但是，如果连接数量急剧上升，这种实现方式就无法很好地工作了，因为线程上下文切换开销会在高并发时变得很明显，这是同步阻塞方式的低扩展性劣势。

NIO 引入的多路复用机制，提供了另外一种思路，请参考下面提供的新的版本。

```java
public class NIOServer extends Thread {
    public void run() {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocket = ServerSocketChannel.open();) {// 创建 Selector 和 Channel
            serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
            serverSocket.configureBlocking(false);
            // 注册到 Selector，并说明关注点
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select();// 阻塞等待就绪的 Channel，这是关键点之一
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                   // 生产系统中一般会额外进行就绪状态检查
                    sayHelloWorld((ServerSocketChannel) key.channel());
                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sayHelloWorld(ServerSocketChannel server) throws IOException {
        try (SocketChannel client = server.accept();) {          client.write(Charset.defaultCharset().encode("Hello world!"));
        }
    }
   // 省略了与前面类似的 main
}
```

这个非常精简的样例掀开了 NIO 多路复用的面纱，可以分析下主要步骤和元素：

- 首先，通过 Selector.open() 创建一个 Selector，作为类似调度员的角色。

- 然后，创建一个 ServerSocketChannel，并且向 Selector 注册，通过指定 SelectionKey.OP_ACCEPT，告诉调度员，它关注的是新的连接请求。

    **注意**，为什么要明确配置非阻塞模式呢？这是因为阻塞模式下，注册操作是不允许的，会抛出 IllegalBlockingModeException 异常。

- Selector 阻塞在 select 操作，当有 Channel 发生接入请求，就会被唤醒。

- 在 sayHelloWorld 方法中，通过 SocketChannel 和 Buffer 进行数据操作，在本例中是发送了一段字符串。

可以看到，在前面两个样例中，IO 都是同步阻塞模式，所以需要多线程以实现多任务处理。而 NIO 则是利用了单线程轮询事件的机制，通过高效地定位就绪的 Channel，来决定做什么，仅仅 select 阶段是阻塞的，可以有效避免大量客户端连接时，频繁线程切换带来的问题，应用的扩展能力有了非常大的提高。下面这张图对这种实现思路进行了形象地说明。

![nio](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/nio.png)

在 Java 7 引入的 AIO 中，又增添了一种额外的异步 IO 模式，利用事件和回调，处理 Accept、Read 等操作。 AIO 实现看起来是类似这样子：

```java
AsynchronousServerSocketChannel serverSock =        AsynchronousServerSocketChannel.open().bind(sockAddr);
serverSock.accept(serverSock, new CompletionHandler<>() { // 为异步操作指定 CompletionHandler 回调函数
    @Override
    public void completed(AsynchronousSocketChannel sockChannel, AsynchronousServerSocketChannel serverSock) {
        serverSock.accept(serverSock, this);
        // 另外一个 write（sock，CompletionHandler{}）
        sayHelloWorld(sockChannel, Charset.defaultCharset().encode
                ("Hello World!"));
    }
  // 省略其他路径处理方法...
});
```

- 基本抽象很相似，AsynchronousServerSocketChannel 对应于上面例子中的 ServerSocketChannel；AsynchronousSocketChannel 则对应 SocketChannel。
- 业务逻辑的关键在于，通过指定 CompletionHandler 回调接口，在 accept/read/write 等关键节点，通过事件机制调用，这是非常不同的一种编程思路。

# 文件拷贝

Java 有多种比较典型的文件拷贝实现方式，比如：

利用 java.io 类库，直接为源文件构建一个 FileInputStream 读取，然后再为目标文件构建一个 FileOutputStream，完成写入工作。

```java
public static void copyFileByStream(File source, File dest) throws
        IOException {
    try (InputStream is = new FileInputStream(source);
         OutputStream os = new FileOutputStream(dest);){
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
    }
 }
```

或者，利用 java.nio 类库提供的 transferTo 或 transferFrom 方法实现。

```java
public static void copyFileByChannel(File source, File dest) throws
        IOException {
    try (FileChannel sourceChannel = new FileInputStream(source)
            .getChannel();
         FileChannel targetChannel = new FileOutputStream(dest).getChannel
                 ();){
        for (long count = sourceChannel.size() ;count>0 ;) {
            long transferred = sourceChannel.transferTo(
                    sourceChannel.position(), count, targetChannel);            sourceChannel.position(sourceChannel.position() + transferred);
            count -= transferred;
        }
    }
 }
```

当然，Java 标准类库本身已经提供了几种 Files.copy 的实现。

对于 Copy 的效率，这个其实与操作系统和配置等情况相关，总体上来说，NIO transferTo/From 的方式**可能更快**，因为它更能利用现代操作系统底层机制，避免不必要拷贝和上下文切换。

## 拷贝实现机制分析

先来理解一下，前面实现的不同拷贝方法，本质上有什么明显的区别。

首先，你需要理解用户态空间（User Space）和内核态空间（Kernel Space），这是操作系统层面的基本概念，操作系统内核、硬件驱动等运行在内核态空间，具有相对高的特权；而用户态空间，则是给普通应用和服务使用。你可以参考：https://en.wikipedia.org/wiki/User_space。

当我们使用输入输出流进行读写时，实际上是进行了多次上下文切换，比如应用读取数据时，先在内核态将数据从磁盘读取到内核缓存，再切换到用户态将数据从内核缓存读取到用户缓存。

写入操作也是类似，仅仅是步骤相反，你可以参考下面这张图。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/user_space.png" alt="user_space" style="zoom:50%;" />

所以，这种方式会带来一定的额外开销，可能会降低 IO 效率。

而基于 NIO transferTo 的实现方式，在 Linux 和 Unix 上，则会使用到零拷贝技术，数据传输并不需要用户态参与，省去了上下文切换的开销和不必要的内存拷贝，进而可能提高应用拷贝性能。注意，transferTo 不仅仅是可以用在文件拷贝中，与其类似的，例如读取磁盘文件，然后进行 Socket 发送，同样可以享受这种机制带来的性能和扩展性提高。

transferTo 的传输过程是：

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/transfer.png" alt="transfer" style="zoom:50%;" />

实际上有几个不同的 copy 方法。

```java
public static Path copy(Path source, Path target, CopyOption... options)
    throws IOException
public static long copy(InputStream in, Path target, CopyOption... options)
    throws IOException
public static long copy(Path source, OutputStream out) throws IOException
```

可以看到，copy 不仅仅是支持文件之间操作，没有限定输入输出流一定是针对文件的，这是两个很实用的工具方法。

后面两种 copy 实现，能够在方法实现里直接看到使用的是 InputStream.transferTo()，你可以直接看源码，其内部实现其实是 stream 在用户态的读写；而对于第一种方法的分析过程要相对麻烦一些，可以参考下面片段。只分析同类型文件系统拷贝过程。

```java
public static Path copy(Path source, Path target, CopyOption... options)
    throws IOException
 {
    FileSystemProvider provider = provider(source);
    if (provider(target) == provider) {
        // same provider
        provider.copy(source, target, options);// 这是本文分析的路径
    } else {
        // different providers
        CopyMoveHelper.copyToForeignTarget(source, target, options);
    }
    return target;
}
```

JDK 的源代码中，内部实现和公共 API 定义也不是可以能够简单关联上的，NIO 部分代码甚至是定义为模板而不是 Java 源文件，在 build 过程自动生成源码，下面顺便介绍一下部分 JDK 代码机制和如何绕过隐藏障碍。

- 首先，直接跟踪，发现 FileSystemProvider 只是个抽象类，阅读它的[源码](http://hg.openjdk.java.net/jdk/jdk/file/f84ae8aa5d88/src/java.base/share/classes/java/nio/file/spi/FileSystemProvider.java)能够理解到，原来文件系统实际逻辑存在于 JDK 内部实现里，公共 API 其实是通过 ServiceLoader 机制加载一系列文件系统实现，然后提供服务。
- 可以在 JDK 源码里搜索 FileSystemProvider 和 nio，可以定位到[sun/nio/fs](http://hg.openjdk.java.net/jdk/jdk/file/f84ae8aa5d88/src/java.base/share/classes/sun/nio/fs)， NIO 底层是和操作系统紧密相关的，所以每个平台都有自己的部分特有文件系统逻辑。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/fileSystemProvider.png" alt="fileSystemProvider" style="zoom: 67%;" />

- 省略掉一些细节，最后我们一步步定位到 UnixFileSystemProvider → UnixCopyFile.Transfer，发现这是个本地方法。
- 最后，明确定位到[UnixCopyFile.c](http://hg.openjdk.java.net/jdk/jdk/file/f84ae8aa5d88/src/java.base/unix/native/libnio/fs/UnixCopyFile.c)，其内部实现清楚说明竟然只是简单的用户态空间拷贝

所以，这个最常见的 copy 方法其实不是利用 transferTo，而是本地技术实现的用户态拷贝。

提高类似拷贝等 IO 操作的性能，有一些宽泛的原则：

- 在程序中，使用缓存等机制，合理减少 IO 次数（在网络通信中，如 TCP 传输，window 大小也可以看作是类似思路）。
- 使用 transferTo 等机制，减少上下文切换和额外 IO 操作。
- 尽量减少不必要的转换过程，比如编解码；对象序列化和反序列化，比如操作文本文件或者网络通信，如果不是过程中需要使用文本信息，可以考虑不要将二进制信息转换成字符串，直接传输二进制信息。

## Buffer

 Buffer 是 NIO 操作数据的基本工具，Java 为每种原始数据类型都提供了相应的 Buffer 实现（布尔除外），所以掌握和使用 Buffer 是十分必要的，尤其是涉及 Direct Buffer 等使用，因为其在垃圾收集等方面的特殊性，更要重点掌握。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/buffer.png" alt="buffer" style="zoom: 67%;" />

Buffer 有几个基本属性：

- capcity，它反映这个 Buffer 到底有多大，也就是数组的长度。
- position，要操作的数据起始位置。
- limit，相当于操作的限额。在读取或者写入时，limit 的意义很明显是不一样的。比如，读取操作时，很可能将 limit 设置到所容纳数据的上限；而在写入时，则会设置容量或容量以下的可写限度。
- mark，记录上一次 postion 的位置，默认是 0，算是一个便利性的考虑，往往不是必须的。

前面三个是日常使用最频繁的， Buffer 的基本操作：

- 创建了一个 ByteBuffer，准备放入数据，capcity 当然就是缓冲区大小，而 position 就是 0，limit 默认就是 capcity 的大小。
- 当写入几个字节的数据时，position 就会跟着水涨船高，但是它不可能超过 limit 的大小。
- 如果想把前面写入的数据读出来，需要调用 flip 方法，将 position 设置为 0，limit 设置为以前的 position 那里。
- 如果还想从头再读一遍，可以调用 rewind，让 limit 不变，position 再次设置为 0。