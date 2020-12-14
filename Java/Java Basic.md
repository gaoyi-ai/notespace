---
title: Java Basic
categories:
- Java
tags:
- Java
date: 2019/8/1 20:00:13
updated: 2020/12/10 12:00:13
---



@[toc]

# 无符号整型

在Java中，并没有无符号整型（Unsigned）的基本数据类型。`byte`、`short`、`int`和`long`都是带符号整型，最高位是符号位。

```java
byte x = -1;
Byte.toUnsignedInt(x); // 255
// 因为byte的-1的二进制表示是11111111，以无符号整型转换后的int就是255
```

# 在一个静态方法内调用一个非静态成员为什么是非法的

由于静态方法可以不通过对象进行调用，因此在静态方法里，不能调用其他非静态变量，也不可以访问非静态变量成员。

# 接口和抽象类的区别是什么

接口的方法默认是 public，所有方法在接口中不能有实现(Java 8 开始接口方法可以有默认实现），抽象类可以有非抽象的方法

接口中的实例变量默认是 final 类型的，而抽象类中则不一定
一个类可以实现多个接口，但最多只能实现一个抽象类
一个类实现接口的话要实现接口的所有方法，而抽象类不一定
接口不能用 new 实例化，但可以声明，但是必须引用一个实现该接口的对象

从设计层面来说，抽象是对类的抽象，是一种模板设计，接口是行为的抽象，是一种行为的规范。

备注:在JDK8中，接口也可以定义静态方法，可以直接用接口名调用。实现类和实现是不可以调用的。如果同时实现两个接口，接口中定义了一样的默认方法，必须重写，不然会报错

# 成员变量与局部变量的区别有那些

- 从语法形式上，看成员变量是属于类的，而局部变量是在方法中定义的变量或是方法的参数；成员变量可以被 public,private,static 等修饰符所修饰，而局部变量不能被访问控制修饰符及 static 所修饰；但是，成员变量和局部变量都能被 final 所修饰；

- 从变量在内存中的存储方式来看:如果成员变量是使用static修饰的，那么这个成员变量是属于类的，如果没有使用使用static修饰，这个成员变量是属于实例的。而对象存在于堆内存，局部变量存在于栈内存

- 从变量在内存中的生存时间上看:成员变量是对象的一部分，它随着对象的创建而存在，而局部变量随着方法的调用而自动消失。

- 成员变量如果没有被赋初值:则会自动以类型的默认值而赋值（一种情况例外被 final 修饰的成员变量也必须显示地赋值）；而局部变量则不会自动赋值。

# 创建一个对象用什么运算符?对象实体与对象引用有何不同?



new运算符，new创建对象实例（对象实例在堆内存中），对象引用指向对象实例（对象引用存放在栈内存中）。一个对象引用可以指向0个或1个对象（一根绳子可以不系气球，也可以系一个气球）;一个对象可以有n个引用指向它（可以用n条绳子系住一个气球）。


    # 静态方法和实例方法有何不同

在外部调用静态方法时，可以使用"类名.方法名"的方式，也可以使用"对象名.方法名"的方式。而实例方法只有后面这种方式。也就是说，调用静态方法可以无需创建对象。

静态方法在访问本类的成员时，只允许访问静态成员（即静态成员变量和静态方法），而不允许访问实例成员变量和实例方法；实例方法则无此限制.

# 在调用子类构造方法之前会先调用父类没有参数的构造方法,其目的是?

帮助子类做初始化工作。

# ==与equals

== : 它的作用是判断两个对象的地址是不是相等。即，判断两个对象是不是同一个对象。(基本数据类型==比较的是值，引用数据类型==比较的是内存地址)

​    equals() : 它的作用也是判断两个对象是否相等。但它一般有两种使用情况：

​    情况1：类没有覆盖 equals() 方法。则通过 equals() 比较该类的两个对象时，等价于通过“==”比较这两个对象。
​    情况2：类覆盖了 equals() 方法。一般，我们都覆盖 equals() 方法来两个对象的内容相等；若它们的内容相等，则返回 true (即，认为这两个对象相等)。
​    举个例子：

```java
public class test1 {
       public static void main(String[] args) {
          String a = new String("ab"); // a 为一个引用
          String b = new String("ab"); // b为另一个引用,对象的内容一样
          String aa = "ab"; // 放在常量池中
          String bb = "ab"; // 从常量池中查找
          System.out.println(aa == bb); // true
          System.out.println(a == b); // false，非同一对象
          System.out.println(a.equals(b)); // true
          System.out.println(42 == 42.0); // true
      }
    }
```

说明：
	String 中的 equals 方法是被重写过的，因为 object 的 equals 方法是比较的对象的内存地址，而 String 的 equals 方法比较的是对象的值。
当创建 String 类型的对象时，虚拟机会在常量池中查找有没有已经存在的值和要创建的值相同的对象，如果有就把它赋给当前引用。如果没有就在常量池中重新创建一个 String 对象。

# hashCode 与 equals

面试官可能会问你：“你重写过 hashcode 和 equals 么，为什么重写equals时必须重写hashCode方法？”

### hashCode（）介绍
​    hashCode() 的作用是获取哈希码，也称为散列码；它实际上是返回一个int整数。这个哈希码的作用是确定该对象在哈希表中的索引位置。hashCode() 定义在JDK的Object.java中，这就意味着Java中的任何类都包含有hashCode() 函数。

​    散列表存储的是键值对(key-value)，它的特点是：能根据“键”快速的检索出对应的“值”。这其中就利用到了散列码！（可以快速找到所需要的对象）

### 为什么要有 hashCode
我们以“HashSet 如何检查重复”为例子来说明为什么要有 hashCode：

​    当你把对象加入 HashSet 时，HashSet 会先计算对象的 hashcode 值来判断对象加入的位置，同时也会与其他已经加入的对象的 hashcode 值作比较，如果没有相符的hashcode，HashSet会假设对象没有重复出现。但是如果发现有相同 hashcode 值的对象，这时会调用 equals（）方法来检查 hashcode 相等的对象是否真的相同。如果两者相同，HashSet 就不会让其加入操作成功。如果不同的话，就会重新散列到其他位置。（摘自《Head first java》第二版）。这样我们就大大减少了 equals 的次数，相应就大大提高了执行速度。

### hashCode（）与equals（）的相关规定

​    如果两个对象相等，则hashcode一定也是相同的

​    两个对象相等,对两个对象分别调用equals方法都返回true

​    两个对象有相同的hashcode值，它们也不一定是相等的

​    因此，equals 方法被覆盖过，则 hashCode 方法也必须被覆盖

​    hashCode() 的默认行为是对堆上的对象产生独特值。如果没有重写 hashCode()，则该 class 的两个对象无论如何都不会相等（即使这两个对象指向相同的数据）

strings should always be compared with equals, never with == !

equals compares the actual characters in strings

# JAVA weak typing

if one operand of + is a string, it will convert the the second one to a string

 i.e., all expressions "1"+"2", 1+"2" and "1"+2 evaluate to "12", while 1+2 evaluates to 3

# Reference (引用)

```java
// 这样创建的数据对象直接交给group类，不会再有其他的引用，这样就是安全的
group.setnames(new String[] {"A","B","C"});
// names 也指向{"A","B","C"}，修改names就会修改group的成员变量
String[] names = {"A","B","C"};
group.setnames(names);
```

# 构造

子类不会继承任何父类的构造方法。子类默认的构造方法是编译器自动生成的，不是继承的。

# 继承转型

note that the compiler only reasons based on the compatibility of declared types, not of actual types

注意，编译器只基于声明类型的兼容性进行推理，而不是实际类型

# super & super()

| SUPER                                                        | SUPER()                                                      |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| The [super keyword in Java](https://www.geeksforgeeks.org/super-keyword/) is a reference variable that is used to refer parent class objects. | The [super() in Java](https://www.geeksforgeeks.org/super-keyword/) is a reference variable that is used to refer parent class constructors. |
| super can be used to call parent class’ variables and methods. | super() can be used to call parent class’ constructors only. |
| The variables and methods to be called through super keyword can be done at any time, | Call to super() must be first statement in Derived(Student) Class constructor. |
| If one does not explicitly invoke a superclass variables or methods, by using super keyword, then nothing happens | If a constructor does not explicitly invoke a superclass constructor by using super(), the Java compiler automatically inserts a call to the no-argument constructor of the superclass. |

# super & this

Extending Methods: The super Reference
● note that super does not reference another object - it still references the callee
● super and this reference the same object
● but with super, the method lookup (dispatch) is different
● this - use the first implementation of a method found when walking up the inheritance hierarchy
● super - use the second implementation of a method found when walking up the inheritance hierarchy

注意，super没有引用另一个对象——它仍然引用被调用对象

● super和this引用的是同一个对象

●但是使用super，方法查找(分派)是不同的

●this—使用在继承层次结构中找到的方法的第一个实现

●super-使用在继承层次结构中找到的方法的第二个实现

# 枚举

使用`enum`定义的枚举类是一种引用类型。

定义的`enum`类型总是继承自`java.lang.Enum`，且无法被继承

只能定义出`enum`的实例，而无法通过`new`操作符创建`enum`的实例

引用类型比较，要使用`equals()`方法，如果使用`==`比较，它比较的是两个引用类型的变量是否是同一个对象。因此，引用类型比较，要始终使用`equals()`方法，但`enum`类型可以例外。

`enum`类型的每个常量在JVM中只有一个唯一实例，所以可以直接用`==`比较

```java
public enum Theme {
    DARK_MODE("深色模式"),LIGHT_MODE("浅色模式");
    private String Chinese;
    // 构造函数只能为私有
    private Theme(String Chinese){
        this.Chinese = Chinese;
    }
}
/*
public enum Color {
	RED,GREEN,BLUE
}
编译器会就会做：
public final class Color extends Enum {
	public static final Color RED = new Color("RED",0);
	public static final Color GREEN = new Color ("GREEN",1);
	public static final Color BLUE = new Color(name:"BLUE", ordinat 2);
	
	private Color(String name, int ordinal) {
		super(name,ordinal);
	};
}
*/
```

# 异常

Exception在runtime中遇到，会throw扔出这个异常，···最终由主函数throw给JVM

捕获异常 try catch finally

抛出异常 throw 

# list中的查找等等为equals

当元素为自定义类，要重写equals

equals要遵守 离散数学的关系

| 自反 | x.equals(x)              | ture        |
| ---- | ------------------------ | :---------- |
| 对称 | x.equals(y)              | y.equals(x) |
| 传递 | x.equals(y), y.equals(z) | x.equals(z) |

当有Student Class 时，stuID, name

方法一，Objects.equals() 相当于内置方法二

Returns `true` if the arguments are equal to each other and `false` otherwise. Consequently, if both arguments are `null`, `true` is returned. Otherwise, if the first argument is not `null`, equality is determined by calling the `equals` method of the first argument with the second argument of this method. Otherwise, `false` is returned.

 方法二，

o1.stuID o2.stuID

| o1.stuID | o2.stuID      |                            |
| -------- | ------------- | -------------------------- |
| null     | null          | true                       |
| 非null   | 非null / null | this.stuID.equals(o.stuID) |
| null     | 非null        | false                      |

# compare 

| p1 < p2 (这里指的是偏序关系)  在当前关系下p1在p2前面 | -1   |
| ---------------------------------------------------- | ---- |
| p1 > p2                                              | 1    |
| p1 == p2                                             | 0    |

# iterator

```java
for ( : ) // for each
```



编译器实际上会 转化 

```java
for(Iterator<> it = *.iterator(); it.hasNext();)
    it.next()
```

一个类要实现可迭代，需要实现Iterable接口中的iterator()，此方法会返回一个iterator

对于一个iterator，要实现hasNext()和next()

```java
class Range implements Iterable<Integer>, Iterator<Integer> {
    private final int start;
    private final int end;
    private final int step;
    private int current; // 记录当前值
    
    public Range(int start, int end, int step) {
    	this.start = start;
    	this.end = end;
    	this.step = step;
         current = start;
    }

    @override
    public Iterator<Integer> iterator() {
        return this; // 自身可迭代，或者自己 创建一个迭代器
    }

    @override
    public boolean hasNext() {
        return current < end
    }

    @override
    public Integer next(){
        int result = current;
        current += step;
        return result;
    }
}

```
