---
title: Proxy
categories:
- Pattern
- Proxy
tags:
- dynamic proxy
- cglib
date: 2021/1/10 20:00:17
updated: 2021/1/11 12:00:17
---



# 实现

在前文动态代理实现jdk动态代理中我们有这样一个类的调用关系图：

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20190822150752270.png)

其中需要引用到jdk的Proxy和InvocationHandler这两个类，这里我们自己简单实现MyProxy.java和MyInvocationHandler.java

由于需要动态生成代理类，那么就需要生成，编译，加载到jvm，因此我们实现了一个简单的类加载器MyClassLoader.java

此时类调用关系图略微变化：

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20190822155245897.png)

不多说，上代码，大多地方都有比较详细的注释 

 **MyInvocationHandler.java** 

```java
/** 用于自定义代理逻辑处理
 */
public interface MyInvocationHandler {
	/** invoke
	 * @param proxy 指被代理的对象
	 * @param method 要调用的方法
	 * @param args 方法调用时所需要的参数
	 * @return Object
	 * @throws Throwable 异常
	 */
	Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
```

**MyProxy.java** 这个类就是JDK动态代理的关键，其中进行了代理类的动态生成：

1. 生成源代码
2. 将生成的源代码输出到磁盘，保存为.java文件
3. 编译源代码，并且生成.class文件
4. 将class文件中的内容，动态加载到JVM中来
5. 返回被代理后的代理对象

```java
/** 生成代理对象的代码
 */
class MyProxy {
 
	private static final String ln = "\r\n";
 
    /** 通过此类为一个或多个接口动态的生成实现类
     * @param classLoader 类加载器
     * @param interfaces 得到全部的接口
     * @param h 得到InvocationHandler接口的子类实例
     * @return Object
     */
	static Object newProxyInstance(MyClassLoader classLoader, Class<?>[] interfaces, MyInvocationHandler h){
		try{
			//1、生成源代码
			String proxySrc = generateSrc(interfaces[0]);
			//2、将生成的源代码输出到磁盘，保存为.java文件
			String filePath = MyProxy.class.getResource("").getPath();
			File f = new File(filePath + "$Proxy0.java");
			FileWriter fw = new FileWriter(f);
			fw.write(proxySrc);
			fw.flush();
			fw.close();
			//3、编译源代码，并且生成.class文件
			JavaCompiler  compiler = ToolProvider.getSystemJavaCompiler();
			StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
			Iterable iterable = manager.getJavaFileObjects(f);
			CompilationTask task = compiler.getTask(null, manager, null, null, null, iterable);
			task.call();
			manager.close();
			//4.将class文件中的内容，动态加载到JVM中来
			//5.返回被代理后的代理对象
			Class proxyClass = classLoader.findClass("$Proxy0");
			Constructor c = proxyClass.getConstructor(MyInvocationHandler.class);
			//这里先不删除生成的$Proxy0.java文件,实际上要删除的
			f.delete();
			return c.newInstance(h);
 
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
 
    /** 生成代理对象$Proxy0的源代码
     * @param interfaces 抽象对象
     * @return String
     */
	private static String generateSrc(Class<?> interfaces){
		StringBuilder src = new StringBuilder();
        src.append("package com.wang.proxy.custom.jdk.proxy;" + ln);
        //引入反射相关的包
        src.append("import java.lang.reflect.Method;" + ln);
        //动态代理类实现被代理接口，在此为Person类
        src.append("public class $Proxy0 implements " + interfaces.getName() + "{" + ln);
        src.append("MyInvocationHandler h;" + ln);
        src.append("public $Proxy0(MyInvocationHandler h) {" + ln);
        src.append("this.h = h;" + ln);
        src.append("}" + ln);
        //通过反射获取代理接口的所有方法并激活
        for (Method m : interfaces.getMethods()) {
            src.append("public " + m.getReturnType().getName() + " " + m.getName() + "(){" + ln);
 
            src.append("try{" + ln);
            src.append("Method m = " + interfaces.getName() + ".class.getMethod(\"" +m.getName()+"\",new Class[]{});" + ln);
            src.append("this.h.invoke(this,m,null);" + ln);
            src.append("}catch(Throwable e){e.printStackTrace();}" + ln);
            src.append("}" + ln);
        }
        src.append("}");
		return src.toString();
	}
}
```

**MyClassLoader.java**

```java
/**将class重新动态load到JVM
 */
public class MyClassLoader extends ClassLoader{
 
	private File baseDir;
	
	MyClassLoader(){
		String basePath = MyClassLoader.class.getResource("").getPath();
		this.baseDir = new File(basePath);
	}
	
	@Override
	protected Class<?> findClass(String name) {
		String className = MyClassLoader.class.getPackage().getName() + "." + name;
		if(baseDir != null){
			File classFile = new File(baseDir,name.replaceAll("\\.", "/") + ".class");
			if(classFile.exists()){
				FileInputStream in = null;
				ByteArrayOutputStream out = null;
				try{
					in = new FileInputStream(classFile);
					out = new ByteArrayOutputStream();
					byte [] buff = new byte[1024];
					int len;
					while ((len = in.read(buff)) != -1) {
						out.write(buff, 0, len);
					}
					return defineClass(className, out.toByteArray(), 0,out.size());
				}catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(null != in){
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(null != out){
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					//先不删除，可以看到class文件内容
					//classFile.delete();
				}
			}
		}
		return null;
	}
}
```

生成的动态代理类，同样是Person的实现类，同样也实现了findLove方法。

```java
public class $Proxy0 implements Person {
    MyInvocationHandler h;
 
    public $Proxy0(MyInvocationHandler var1) {
        this.h = var1;
    }
 
    public void findLove() {
        try {
            Method var1 = Person.class.getMethod("findLove");
            this.h.invoke(this, var1, (Object[])null);
        } catch (Throwable var2) {
            var2.printStackTrace();
        }
 
    }
}
```

现在可以得知MyProxy.newProxyInstance返回的是动态生成的代理类\$Proxy0的对象，也可以称作是Person接口的一个实现类的对象。当调用person.findLove()时，其实是调用$Proxy0.findLove(),然后对照刚刚的类调用关系图，即可调用到被代理对象Girl实例的findLove方法，从而实现了动态代理。

# 静态代理

假设现在项目经理有一个需求：在项目现有所有类的方法前后打印日志。

你如何在**不修改已有代码的前提下**，完成这个需求？

我首先想到的是静态代理。具体做法是：

1.为现有的每一个类都编写一个**对应的**代理类，并且让它实现和目标类相同的接口（假设都有）

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-001c5db900d8785d47c1a5a0c6f32762_720w.jpg)

2.在创建代理对象时，通过构造器塞入一个目标对象，然后在代理对象的方法内部调用目标对象同名方法，并在调用前后打印日志。也就是说，**代理对象 = 增强代码 + 目标对象（原对象）**。有了代理对象后，就不用原对象了

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-e302487f952bdf8e284afc0d8d6a770b_720w.jpg)

**静态代理的缺陷**

程序员要手动为每一个目标类编写对应的代理类。如果当前系统已经有成百上千个类，工作量太大了。所以，现在我们的努力方向是：如何少写或者不写代理类，却能完成代理功能？

**复习对象的创建**

很多初学Java的朋友眼中创建对象的过程

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-9cd31ab516bd967e1b8e68736931f8ba_720w.jpg)

实际上可以换个角度，也说得通

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-eddc430b991c58039dfc79dd6f3139cc_720w.jpg)

所谓的Class对象，是Class类的实例，而Class类是描述所有类的，比如Person类，Student类

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-c9bf695b1b9d2a0ae01cf92501492159_720w.jpg)

可以看出，要创建一个实例，最关键的就是**得到对应的Class对象。**只不过对于初学者来说，new这个关键字配合构造方法，实在太好用了，底层隐藏了太多细节，一句 Person p = new Person();直接把对象返回给你了。我自己刚开始学Java时，也没意识到Class对象的存在。

分析到这里，貌似有了思路：

**能否不写代理类，而直接得到代理Class对象，然后根据它创建代理实例（反射）。**

Class对象包含了一个类的所有信息，比如构造器、方法、字段等。如果我们不写代理类，这些信息从哪获取呢？苦思冥想，突然灵光一现：代理类和目标类理应实现同一组接口。**之所以实现相同接口，是为了尽可能保证代理对象的内部结构和目标对象一致，这样我们对代理对象的操作最终都可以转移到目标对象身上，代理对象只需专注于增强代码的编写。**还是上面这幅图：

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-e302487f952bdf8e284afc0d8d6a770b_720w.jpg)

所以，可以这样说：接口拥有代理对象和目标对象共同的类信息。所以，我们可以从接口那得到理应由代理类提供的信息。但是别忘了，接口是无法创建对象的，怎么办？

# 动态代理

JDK提供了java.lang.reflect.InvocationHandler接口和 java.lang.reflect.Proxy类，这两个类相互配合，入口是Proxy，所以我们先聊它。

Proxy有个静态方法：getProxyClass(ClassLoader, interfaces)，只要你给它传入类加载器和一组接口，它就给你返回代理Class对象。

所以，一旦我们明确接口，完全可以通过接口的Class对象，创建一个代理Class，通过代理Class即可创建代理对象。

大体思路

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-d187a82b1eb9c088fe60327828ee63aa_720w.jpg)

静态代理

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-28223a1c03c1800052a5dfe4e6cb8c53_720w.jpg)

动态代理

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-ba3d9206f341be466f18afbdd938a3b3_720w.jpg)

所以，按我理解，Proxy.getProxyClass()这个方法的本质就是：**以Class造Class。**

有了Class对象，就很好办了，具体看代码：

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-6b091b6d41bae1f88ba74a510acb24b1_720w.jpg)

根据代理Class的构造器创建对象时，需要传入InvocationHandler。每次调用代理对象的方法，最终都会调用InvocationHandler的invoke()方法：

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-92610586e77cf71ba5ce89087de52ff1_720w.jpg)

怎么做到的呢？

上面不是说了吗，根据代理Class的构造器创建对象时，需要传入InvocationHandler。**通过构造器传入一个引用，那么必然有个成员变量去接收。**没错，代理对象的内部确实有个成员变量invocationHandler，而且代理对象的每个方法内部都会调用handler.invoke()！InvocationHandler对象成了代理对象和目标对象的桥梁，不像静态代理这么直接。

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-b5fc8b279a6152889afdfedbb0f611cc_720w.jpg)

大家仔细看上图右侧的动态代理，我在invocationHandler的invoke()方法中并没有写目标对象。因为一开始invocationHandler的invoke()里确实没有目标对象，需要我们手动new。

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-88147b81ee9342015374159b2671320b_720w.jpg)

但这种写法不够优雅，属于硬编码。我这次代理A对象，下次想代理B对象还要进来改invoke()方法，太差劲了。改进一下，让调用者把目标对象作为参数传进来：

```java
public class ProxyTest {
	public static void main(String[] args) throws Throwable {
		CalculatorImpl target = new CalculatorImpl();
                //传入目标对象
                //目的：1.根据它实现的接口生成代理对象 2.代理对象调用目标对象方法
		Calculator calculatorProxy = (Calculator) getProxy(target);
		calculatorProxy.add(1, 2);
		calculatorProxy.subtract(2, 1);
	}

	private static Object getProxy(final Object target) throws Exception {
		//参数1：随便找个类加载器给它， 参数2：目标对象实现的接口，让代理对象实现相同接口
		Class proxyClazz = Proxy.getProxyClass(target.getClass().getClassLoader(), target.getClass().getInterfaces());
		Constructor constructor = proxyClazz.getConstructor(InvocationHandler.class);
		Object proxy = constructor.newInstance(new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println(method.getName() + "方法开始执行...");
				Object result = method.invoke(target, args);
				System.out.println(result);
				System.out.println(method.getName() + "方法执行结束...");
				return result;
			}
		});
		return proxy;
	}
}
```

这样就非常灵活，非常优雅了。无论现在系统有多少类，只要你把实例传进来，getProxy()都能给你返回对应的代理对象。就这样，我们完美地跳过了代理类，直接创建了代理对象！

不过实际编程中，一般不用getProxyClass()，而是使用Proxy类的另一个静态方法：Proxy.newProxyInstance()，直接返回代理实例，连中间得到代理Class对象的过程都帮你隐藏：

```java
public class ProxyTest {
	public static void main(String[] args) throws Throwable {
		CalculatorImpl target = new CalculatorImpl();
		Calculator calculatorProxy = (Calculator) getProxy(target);
		calculatorProxy.add(1, 2);
		calculatorProxy.subtract(2, 1);
	}

	private static Object getProxy(final Object target) throws Exception {
		Object proxy = Proxy.newProxyInstance(
				target.getClass().getClassLoader(),/*类加载器*/
				target.getClass().getInterfaces(),/*让代理对象和目标对象实现相同接口*/
				new InvocationHandler(){/*代理对象的方法最终都会被JVM导向它的invoke方法*/
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						System.out.println(method.getName() + "方法开始执行...");
						Object result = method.invoke(target, args);
						System.out.println(result);
						System.out.println(method.getName() + "方法执行结束...");
						return result;
					}
				}
		);
		return proxy;
	}
}
```

现在，我想题主应该能看懂动态代理了。

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-6aacbe1e9df4fe982a68fe142401952e_720w.jpg)

最后讨论一下代理对象是什么类型。

首先，请区分两个概念：代理Class对象和代理对象。

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-bb82bd129d63f77265f51b2209159269_720w.jpg)

单从名字看，代理Class和Calculator的接口确实相去甚远，但是我们却能将代理对象赋值给接口类型：

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-e869e67fc4fbc708b793ff6ea6e2c012_720w.jpg)

千万别觉得名字奇怪，就怀疑它不能用接口接收，只要实现该接口就是该类型。

> 代理对象的本质就是：和目标对象实现相同接口的实例。代理Class可以叫任何名字，whatever，只要它实现某个接口，就能成为该接口类型。

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-91d716b1a95099ad364233de91fca7a3_720w.jpg)

我写了一个MyProxy类，那么它的Class名字必然叫MyProxy。**但这和能否赋值给接口没有任何关系。**由于它实现了Serializable和Collection，所以myProxy（代理实例）**同时**是这两个接口的类型。

动态代理生成的代理对象，最终都可以用接口接收，和目标对象一起形成了多态，可以随意切换展示不同的功能。但是切换的同时，只能使用该接口定义的方法。

------

## 关于类加载器

初学者可能对诸如“字节码文件”、Class对象比较陌生。所以这里花一点点篇幅介绍一下类加载器的部分原理。如果我们要定义类加载器，需要继承ClassLoader类，并覆盖findClass()方法：

```java
@Override
public Class<?> findClass(String name) throws ClassNotFoundException {
	try {
		/*自己另外写一个getClassData()
                  通过IO流从指定位置读取xxx.class文件得到字节数组*/
		byte[] datas = getClassData(name);
		if(datas == null) {
			throw new ClassNotFoundException("类没有找到：" + name);
		}
		//调用类加载器本身的defineClass()方法，由字节码得到Class对象
		return this.defineClass(name, datas, 0, datas.length);
	} catch (IOException e) {
		e.printStackTrace();
		throw new ClassNotFoundException("类找不到：" + name);
	}
}
```

所以，这就是类加载之所以能把xxx.class文件加载进内存，并创建对应Class对象的深层原因。

# JDK源码分析

从`Proxy#newProxyInstance`入口进行源码分析：

```java
public static Object newProxyInstance(ClassLoader loader,
                                      Class<?>[] interfaces,
                                      InvocationHandler h)
    throws IllegalArgumentException
{
    Objects.requireNonNull(h);

    final Class<?>[] intfs = interfaces.clone();
    final SecurityManager sm = System.getSecurityManager();
    if (sm != null) {
        checkProxyAccess(Reflection.getCallerClass(), loader, intfs);
    }

    // 查找或生成指定的代理类
    Class<?> cl = getProxyClass0(loader, intfs);

    try {
        if (sm != null) {
            checkNewProxyPermission(Reflection.getCallerClass(), cl);
        }

        // 获取代理的构造器
        final Constructor<?> cons = cl.getConstructor(constructorParams);
        final InvocationHandler ih = h;
        // 处理代理类修饰符，使得能被访问
        if (!Modifier.isPublic(cl.getModifiers())) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                public Void run() {
                    cons.setAccessible(true);
                    return null;
                }
            });
        }
        // 创建代理类实例化
        return cons.newInstance(new Object[]{h});
    } catch (IllegalAccessException|InstantiationException e) {
        throw new InternalError(e.toString(), e);
    } catch (InvocationTargetException e) {
        Throwable t = e.getCause();
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        } else {
            throw new InternalError(t.toString(), t);
        }
    } catch (NoSuchMethodException e) {
        throw new InternalError(e.toString(), e);
    }
}
```

newProxyInstance 方法里面获取到代理类，如果类的作用不能访问，使其能被访问到，最后实例化代理类。这段代码中最为核心的是获取代理类的`getProxyClass0`方法。

```java
private static final WeakCache<ClassLoader, Class<?>[], Class<?>> proxyClassCache = new WeakCache<>(new KeyFactory(), new ProxyClassFactory());

private static Class<?> getProxyClass0(ClassLoader loader,
                                       Class<?>... interfaces) {
    // 实现类的接口不能超过 65535 个
    if (interfaces.length > 65535) {
        throw new IllegalArgumentException("interface limit exceeded");
    }

    // 获取代理类
    return proxyClassCache.get(loader, interfaces);
}
```

如果 proxyClassCache 缓存中存在指定的代理类，则从缓存直接获取；如果不存在，则通过 ProxyClassFactory 创建代理类。 至于为什么接口最大为 65535，这个是由字节码文件结构和 Java 虚拟机规定的，具体可以通过研究字节码文件了解。

进入到`proxyClassCache#get`，获取代理类:

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-a0b17abb142503472fb652f737389105_720w.jpg)

继续进入`Factory#get`查看，

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-0429079177d709afa4b62280d2fac2c7_720w.jpg)

最后到`ProxyClassFactory#apply`，这里实现了代理类的创建。

```java
private static final class ProxyClassFactory implements BiFunction<ClassLoader, Class<?>[], Class<?>>{
    // 所有代理类名称都已此前缀命名
    private static final String proxyClassNamePrefix = "$Proxy";

    // 代理类名的编号
    private static final AtomicLong nextUniqueNumber = new AtomicLong();

    @Override
    public Class<?> apply(ClassLoader loader, Class<?>[] interfaces) {

        Map<Class<?>, Boolean> interfaceSet = new IdentityHashMap<>(interfaces.length);
        for (Class<?> intf : interfaces) {

            // 校验代理和目标对象是否实现同一接口
            Class<?> interfaceClass = null;
            try {
                interfaceClass = Class.forName(intf.getName(), false, loader);
            } catch (ClassNotFoundException e) {
            }
            if (interfaceClass != intf) {
                throw new IllegalArgumentException(
                    intf + " is not visible from class loader");
            }

            // 校验 interfaceClass 是否为接口
            if (!interfaceClass.isInterface()) {
                throw new IllegalArgumentException(
                    interfaceClass.getName() + " is not an interface");
            }

            // 判断当前 interfaceClass 是否被重复
            if (interfaceSet.put(interfaceClass, Boolean.TRUE) != null) {
                throw new IllegalArgumentException(
                    "repeated interface: " + interfaceClass.getName());
            }
        }

        // 代理类的包名
        String proxyPkg = null;     
        int accessFlags = Modifier.PUBLIC | Modifier.FINAL;

        // 记录非 public 修饰符代理接口的包，使生成的代理类与它在同一个包下
        for (Class<?> intf : interfaces) {
            int flags = intf.getModifiers();
            if (!Modifier.isPublic(flags)) {
                accessFlags = Modifier.FINAL;
                // 获取接口类名
                String name = intf.getName();
                // 去掉接口的名称，获取所在包的包名
                int n = name.lastIndexOf('.');
                String pkg = ((n == -1) ? "" : name.substring(0, n + 1));
                if (proxyPkg == null) {
                    proxyPkg = pkg;
                } else if (!pkg.equals(proxyPkg)) {
                    throw new IllegalArgumentException(
                        "non-public interfaces from different packages");
                }
            }
        }

        if (proxyPkg == null) {
            // 如果接口类是 public 修饰，则用 com.sun.proxy 包名
            proxyPkg = ReflectUtil.PROXY_PACKAGE + ".";
        }

        // 创建代理类名称
        long num = nextUniqueNumber.getAndIncrement();
        String proxyName = proxyPkg + proxyClassNamePrefix + num;

        // 生成代理类字节码文件
        byte[] proxyClassFile = ProxyGenerator.generateProxyClass(
            proxyName, interfaces, accessFlags);
        try {
            // 加载字节码，生成指定代理对象
            return defineClass0(loader, proxyName,
                                proxyClassFile, 0, proxyClassFile.length);
        } catch (ClassFormatError e) {
            throw new IllegalArgumentException(e.toString());
        }
    }
}
```

以上就是创建字节码流程，通过检查接口的属性，决定代理类字节码文件生成的包名及名称规则，然后加载字节码获取代理实例。操作生成字节码文件在`ProxyGenerator#generateProxyClass`中生成具体的字节码文件，字节码操作这里不做详细讲解。 生成的字节码文件，我们可以通过保存本地进行反编译查看类信息，保存生成的字节码文件可以通过两种方式：设置jvm参数或将生成 byte[] 写入文件。

![img](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/v2-afe6bc4c623cf54fdf95af54559e5b9b_720w.jpg)

上图的`ProxyGenerator#generateProxyClass`方法可知，是通过 saveGeneratedFiles 属性值控制，该属性的值来源：

```java
private static final boolean saveGeneratedFiles = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.misc.ProxyGenerator.saveGeneratedFiles"))).booleanValue();
```

所以通过设置将生成的代理类字节码保存到本地。

`-Dsun.misc.ProxyGenerator.saveGeneratedFiles=true`