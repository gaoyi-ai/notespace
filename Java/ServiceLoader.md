---
title: ServiceLoader
categories:
- Java
tags:
- serviceLoader
date: 2019/8/1 20:00:15
updated: 2020/12/10 12:00:15
---



# ServiceLoader

A *service* is a well-known set of interfaces and (usually abstract) classes. A *service provider* is a specific implementation of a service. The classes in a provider typically implement the interfaces and subclass the classes defined in the service itself. Service providers can be installed in an implementation of the Java platform in the form of extensions, that is, jar files placed into any of the usual extension directories. Providers can also be made available by adding them to the application's class path or by some other platform-specific means.

The *provider class* is typically not the entire provider itself but rather a proxy which contains enough information to decide whether the provider is able to satisfy a particular request together with code that can create the actual provider on demand. The details of provider classes tend to be highly service-specific; no single class or interface could possibly unify them

Providers are located and instantiated lazily, that is, on demand. A service loader maintains a cache of the providers that have been loaded so far. Each invocation of the iterator method returns an iterator that first yields all of the elements of the cache, in instantiation order, and then lazily locates and instantiates any remaining providers, adding each one to the cache in turn. The cache can be cleared via the reloadmethod.

服务是一组众所周知的接口和（通常是抽象的）类。服务提供者是一个服务的具体实现。提供者中的类通常实现接口，并对服务本身中定义的类进行子类化。服务提供者可以以扩展的形式安装在Java平台的实现中，也就是将jar文件放入任何一个常用的扩展目录中。提供者也可以通过将其添加到应用程序的类路径中或通过其他一些特定于平台的方式来提供。

提供者类通常不是整个提供者本身，而是一个代理，它包含足够的信息来决定提供者是否能够满足特定的请求，同时还包含可以根据需求创建实际提供者的代码。提供者类的细节往往是高度服务特定的；没有一个类或接口可能将它们统一起来。

提供者被懒惰地定位和实例化，也就是按需定位。服务加载器维护着迄今为止已经加载的提供者的缓存。每次调用迭代器方法都会返回一个迭代器，它首先按照实例化顺序产生缓存中的所有元素，然后懒惰地定位和实例化任何剩余的提供者，依次将每个提供者添加到缓存中。缓存可以通过reloadmethod清除。

 

**Example** Suppose we have a service type `com.example.CodecSet` which is intended to represent sets of encoder/decoder pairs for some protocol. In this case it is an abstract class with two abstract methods:

```java
 public abstract Encoder getEncoder(String encodingName);
 public abstract Decoder getDecoder(String encodingName);
```

Each method returns an appropriate object or null if the provider does not support the given encoding. Typical providers support more than one encoding.

If `com.example.impl.StandardCodecs` is an implementation of the `CodecSet` service then its jar file also contains a file named

`META-INF/services/com.example.CodecSet`

This file contains the single line:

 `com.example.impl.StandardCodecs   # Standard codecs`

The `CodecSet` class creates and saves a single service instance at initialization:

```java
private static ServiceLoader<CodecSet> codecSetLoader = ServiceLoader.load(CodecSet.class);
```

To locate an encoder for a given encoding name it defines a static factory method which iterates through the known and available providers, returning only when it has located a suitable encoder or has run out of providers.

```java
public static Encoder getEncoder(String encodingName) {
	for (CodecSet cp : codecSetLoader) {
        Encoder enc = cp.getEncoder(encodingName);
        if (enc != null)
            return enc;
    }
    return null;
}
```

---

# ServiceLoader的使用  mysql-connecter-java

## ServiceLoader的使用

这里先列举一个经典的例子，[MySQL](https://cloud.tencent.com/product/cdb?from=10680)的Java驱动就是通过ServiceLoader加载的，先引入mysql-connector-java的依赖：

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.47</version>
</dependency>
```

查看这个依赖的源码包下的META-INF目录，可见：

我们接着查看java.lang.DriverManager，静态代码块里面有：

```java
static {
    loadInitialDrivers();
    println("JDBC DriverManager initialized");
}
```

其中，可以查看`loadInitialDrivers()`有如下的代码片段：

java.lang.DriverManager是启动类加载器加载的基础类，但是它可以加载rt.jar包之外的类，上篇文章提到，这里打破了双亲委派模型，原因是：ServiceLoader中使用了线程上下文类加载器去加载类。这里JDBC加载的过程就是典型的SPI的使用，总结规律如下：

- 1、需要定义一个接口。
- 2、接口提供商需要实现第1步中的接口。
- 3、接口提供商在META-INF/services目录下建立一个文本文件，文件名是第1步中定义的接口的全限定类名，文本内容是接口的实现类的全限定类名，每个不同的实现占独立的一行。
- 4、使用ServiceLoader加载接口类，获取接口的实现的实例迭代器。

举个简单的实例，先定义一个接口和两个实现：

```java
public interface Say {
void say();
}
public class SayBye implements Say {
@Override
	public void say() {
		System.out.println("Bye!");
	}
}
public class SayHello implements Say {
@Override
	public void say() {
		System.out.println("Hello!");
	}
}
```

接着在项目的META-INF/services中添加文件如下：

最后通过main函数验证：

基于SPI或者说ServiceLoader加载接口实现这种方式也可以广泛使用在相对基础的组件中，因为这是一个成熟的规范。

## ServiceLoader源码分析

上面通过一个经典例子和一个实例介绍了ServiceLoader的使用方式，接着我们深入分析ServiceLoader的源码。我们先看ServiceLoader的类签名和属性定义：

```java
 public final class ServiceLoader<S> implements Iterable<S>{
     //需要加载的资源的路径的目录，固定是ClassPath下的META-INF/services/
     private static final String PREFIX = "META-INF/services/";
     // ServiceLoader需要正在需要加载的类或者接口
     // The class or interface representing the service being loaded
     private final Class<S> service;
     // ServiceLoader进行类加载的时候使用的类加载器引用
     // The class loader used to locate, load, and instantiate providers
     private final ClassLoader loader;
     // 权限控制上下文
     // The access control context taken when the ServiceLoader is created
     private final AccessControlContext acc;
     //基于实例的顺序缓存类的实现实例，其中Key为实现类的全限定类名
     // Cached providers, in instantiation order
     private LinkedHashMap<String,S> providers = new LinkedHashMap<>();
     // 当前的"懒查找"迭代器，这个是ServiceLoader的核心
     // The current lazy-lookup iterator
     private LazyIterator lookupIterator;
     //暂时忽略其他代码...
 }
```

ServiceLoader实现了Iterable接口，这一点提示了等下我们在分析它源码的时候，需要重点分析`iterator()`方法的实现。ServiceLoader依赖于类加载器实例进行类加载，它的核心属性LazyIterator是就是用来实现`iterator()`方法的，下文再重点分析。接着，我们分析ServiceLoader的构造函数：

```java
public void reload() {
    //清空缓存
    providers.clear();
    //构造LazyIterator实例
    lookupIterator = new LazyIterator(service, loader);
}
private ServiceLoader(Class<S> svc, ClassLoader cl) {
    service = Objects.requireNonNull(svc, "Service interface cannot be null");
    loader = (cl == null) ? ClassLoader.getSystemClassLoader() : cl;
    acc = (System.getSecurityManager() != null) ? AccessController.getContext() : null;
    reload();
}
```

ServiceLoader只有一个私有的构造函数，也就是它不能通过构造函数实例化，但是要实例化ServiceLoader必须依赖于它的静态方法调用私有构造去完成实例化操作，而实例化过程主要做了几步：

- 判断传入的接口或者类的Class实例不能为null，否则会抛出异常。
- 如果传入的ClassLoader实例为null，则使用应用类加载器(Application ClassLoader)。
- 实例化访问控制上下文。
- 调用实例方法`reload()`，清空目标加载类的实现类实例的缓存并且构造LazyIterator实例。

注意一点是实例方法`reload()`的修饰符是public，也就是可以主动调用去清空目标加载类的实现类实例的缓存和重新构造LazyIterator实例。接着看ServiceLoader提供的静态方法：

```java
public static <S> ServiceLoader<S> load(Class<S> service, ClassLoader loader){
    return new ServiceLoader<>(service, loader);
}
public static <S> ServiceLoader<S> load(Class<S> service) {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    return ServiceLoader.load(service, cl);
}
public static <S> ServiceLoader<S> loadInstalled(Class<S> service) {
    ClassLoader cl = ClassLoader.getSystemClassLoader();
    ClassLoader prev = null;
    while (cl != null) {
        prev = cl;
        cl = cl.getParent();
    }
    return ServiceLoader.load(service, prev);
}
```

上面的三个公共静态方法都是用于构造ServiceLoader实例，其中`load(Class<S> service, ClassLoader loader)`就是典型的静态工厂方法，直接调用ServiceLoader的私有构造器进行实例化，除了需要指定加载类的目标类型，还需要传入类加载器的实例。`load(Class<S> service)`实际上也是委托到`load(Class<S> service, ClassLoader loader)`，不过它使用的类加载器指定为线程上下文类加载器，一般情况下，线程上下文类加载器获取到的就是应用类加载器(系统类加载器)。`loadInstalled(Class<S> service)`方法又看出了"双亲委派模型"的影子，它指定类加载器为最顶层的启动类加载器，最后也是委托到`load(Class<S> service, ClassLoader loader)`。接着我们需要重点分析`ServiceLoader#iterator()`：

```java
public Iterator<S> iterator() {
//Iterator的匿名实现
    return new Iterator<S>() {
        
    //目标类实现类实例缓存的Map的Entry的迭代器实例
    Iterator<Map.Entry<String,S>> knownProviders = providers.entrySet().iterator();
        
        //先从缓存中判断是否有下一个实例，否则通过懒加载迭代器LazyIterator去判断是否存在下一个实例
        public boolean hasNext() {
            if (knownProviders.hasNext())
                return true;
            return lookupIterator.hasNext();
        }
//如果缓存中判断是否有下一个实例，如果有则从缓存中的值直接返回
        //否则通过懒加载迭代器LazyIterator获取下一个实例
        public S next() {
            if (knownProviders.hasNext())
                return knownProviders.next().getValue();
            return lookupIterator.next();
        }
//不支持移除操作，直接抛异常
        public void remove() {
            throw new UnsupportedOperationException();
        }
    };
}
```

`iterator()`内部仅仅是Iterator接口的匿名实现，`hasNext()`和`next()`方法都是优先判断缓存中是否已经存在实现类的实例，如果存在则直接从缓存中返回，否则调用懒加载迭代器LazyIterator的实例去获取，而LazyIterator本身也是一个Iterator接口的实现，它是ServiceLoader的一个私有内部类，源码如下：

```java
 private class LazyIterator implements Iterator<S>{

         Class<S> service;
         ClassLoader loader;
         //加载的资源的URL集合
         Enumeration<URL> configs = null;
         //所有需要加载的实现类的全限定类名的集合
         Iterator<String> pending = null;
         //下一个需要加载的实现类的全限定类名
         String nextName = null;

         private LazyIterator(Class<S> service, ClassLoader loader) {
             this.service = service;
             this.loader = loader;
         }

         private boolean hasNextService() {
             //如果下一个需要加载的实现类的全限定类名不为null，则说明资源中存在内容
             if (nextName != null) {
                 return true;
             }
             //如果加载的资源的URL集合为null则尝试进行加载
             if (configs == null) {
                 try {
                     //资源的名称，META-INF/services + '需要加载的类的全限定类名'
                     //这样得到的刚好是需要加载的文件的资源名称
                     String fullName = PREFIX + service.getName();
                     //这里其实ClassLoader实例应该不会为null
                     if (loader == null)
                         configs = ClassLoader.getSystemResources(fullName);
                     else
                         //从ClassPath加载资源
                         configs = loader.getResources(fullName);
                 } catch (IOException x) {
                     fail(service, "Error locating configuration files", x);
                 }
             }
             //从资源中解析出需要加载的所有实现类的全限定类名
             while ((pending == null) || !pending.hasNext()) {
                 if (!configs.hasMoreElements()) {
                     return false;
                 }
                 pending = parse(service, configs.nextElement());
             }
             //获取下一个需要加载的实现类的全限定类名
             nextName = pending.next();
             return true;
         }
     
         private S nextService() {
             if (!hasNextService())
                 throw new NoSuchElementException();
             String cn = nextName;
             nextName = null;
             Class<?> c = null;
             try {
                 //反射构造Class<S>实例
                 c = Class.forName(cn, false, loader);
             } catch (ClassNotFoundException x) {
                 fail(service,
                      "Provider " + cn + " not found");
             }
             //这里会做一次类型判断，也就是实现类必须是当前加载的类或者接口的派生类，否则抛出异常终止
             if (!service.isAssignableFrom(c)) {
                 fail(service,
                      "Provider " + cn  + " not a subtype");
             }
             try {
                 //通过Class#newInstance()进行实例化，并且强制转化为对应的类型的实例
                 S p = service.cast(c.newInstance());
                 //添加缓存，Key为实现类的全限定类名，Value为实现类的实例
                 providers.put(cn, p);
                 return p;
             } catch (Throwable x) {
                 fail(service,
                      "Provider " + cn + " could not be instantiated",
                      x);
             }
             throw new Error();          // This cannot happen
         }

         public boolean hasNext() {
             if (acc == null) {
                 return hasNextService();
             } else {
                 PrivilegedAction<Boolean> action = new PrivilegedAction<Boolean>() {
                     public Boolean run() { return hasNextService(); }
                 };
                 return AccessController.doPrivileged(action, acc);
             }
         }
     
         public S next() {
             if (acc == null) {
                 return nextService();
             } else {
                 PrivilegedAction<S> action = new PrivilegedAction<S>() {
                     public S run() { return nextService(); }
                 };
                 return AccessController.doPrivileged(action, acc);
             }
         }
         public void remove() {
             throw new UnsupportedOperationException();
         }
     }
```

`LazyIterator`也是Iterator接口的实现，它的Lazy特性表明它总是在ServiceLoader的Iterator接口匿名实现`iterator()`执行`hasNext()`判断是否有下一个实现或者`next()`获取下一个实现类的实例的时候才会"懒判断"或者"懒加载"下一个实现类的实例。最后是加载资源文件后对资源文件的解析过程的源码：

```java
 private Iterator<String> parse(Class<?> service, URL u) throws ServiceConfigurationError{
         InputStream in = null;
         BufferedReader r = null;
         //存放文件中所有的实现类的全类名，每一行是一个元素
         ArrayList<String> names = new ArrayList<>();
         try {
             in = u.openStream();
             r = new BufferedReader(new InputStreamReader(in, "utf-8"));
             int lc = 1;
             while ((lc = parseLine(service, u, r, lc, names)) >= 0);
         } catch (IOException x) {
             fail(service, "Error reading configuration file", x);
         } finally {
             try {
                 if (r != null) r.close();
                 if (in != null) in.close();
             } catch (IOException y) {
                 fail(service, "Error closing configuration file", y);
             }
         }
         //返回的是ArrayList的迭代器实例
         return names.iterator();
 }

 //解析资源文件中每一行的内容
 private int parseLine(Class<?> service, URL u, BufferedReader r, int lc,
                       List<String> names)throws IOException, ServiceConfigurationError{
         // 下一行没有内容，返回-1，便于上层可以跳出循环                 
         String ln = r.readLine();
         if (ln == null) {
             return -1;
         }
         //如果存在'#'字符，截取第一个'#'字符串之前的内容，'#'字符之后的属于注释内容
         int ci = ln.indexOf('#');
         if (ci >= 0) ln = ln.substring(0, ci);
         ln = ln.trim();
         int n = ln.length();
         if (n != 0) {
             //不能存在空格字符' '和特殊字符'\t'
             if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0))
                 fail(service, u, lc, "Illegal configuration-file syntax");
             int cp = ln.codePointAt(0);
             //判断第一个char是否一个合法的Java起始标识符
             if (!Character.isJavaIdentifierStart(cp))
                 fail(service, u, lc, "Illegal provider-class name: " + ln);
             //判断所有其他字符串是否属于合法的Java标识符
             for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
                 cp = ln.codePointAt(i);
                 if (!Character.isJavaIdentifierPart(cp) && (cp != '.'))
                     fail(service, u, lc, "Illegal provider-class name: " + ln);
             }
             //如果缓存中不存在加载出来的全类名或者已经加载的列表中不存在加载出来的全类名则添加进去加载的全类名列表中
             if (!providers.containsKey(ln) && !names.contains(ln))
                 names.add(ln);
         }
         return lc + 1;
     }
```

整个资源文件的解析过程并不复杂，主要包括文件内容的字符合法性判断和缓存避免重复加载的判断。

**小结**

SPI被广泛使用在第三方插件式类库的加载，最常见的如JDBC、JNDI、JCE(Java加密模块扩展)等类库。理解ServiceLoader的工作原理有助于编写扩展性良好的可插拔的类库。

来自 <https://cloud.tencent.com/developer/article/1650078> 

