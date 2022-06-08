SMACH是**状态机**的意思，是基于Python实现的一个功能强大且易于扩展的库。 smach本质上并不依赖于ROS，可以用于任意python项目，不过在ROS中元功能包**executive_smach**将smach和ROS很好的集成在了一起，可以**为机器人复杂应用开发提供任务级的状态机框架**，此外元功能包还集成了actionlib和smach_viewer。

> 为避免误导，本文以下提到的SMACH均指ROS中的SMACH功能包。

## **一、关于SMACH**

**1. 什么时候用**

在很多应用场景中，我们需要设计一些复杂的机器人任务，任务中包含多个状态模块，而这些状态模块之间在某些情况下会发生跳转，这就是SMACH可以发挥作用的地方。

- **快速原型设计**：基于Python语法的SMACH可以实现状态机原型的快速开发测试；
- **复杂状态机模型**：SMACH支持设计、维护、调试大型复杂的状态机；
- **可视化**：SMACH提供可视化观测工具smach_viewer ，可以看到完整状态机的状态跳转、数据流等信息

**2. 什么时候不用**

在某些场景下，SMACH也并不适用：

- **非结构化任务**：非结构化任务调度中可能存在未知的状态跳转
- **低层次系统**：SMACH适用于任务机调度，不适合相对简单、不包含任务级调度的系统。
- **拆分模块**：SMACH的使用并不是为了让我们将模块拆分

## **二、安装SMACH**

无论是ROS indigo还是kinetic，都有smach的二进制安装包，可以直接使用如下命令安装：

```text
$ sudo apt-get install ros-kinetic-executive-smach
$ sudo apt-get install ros-kinetic-executive-smach-visualization
```

smach提供了不少官方例程源码，可以直接下载运行，不过其中很多例程没有加入内部观测器，所以古月君对代码进行了一些修改，大家可以下载修改之后的源码：huchunxu/ros_blog_sourcesgithub.com/huchunxu/ros_blog_sources/tree/master/smach_tutorials

## **三、状态机跑起来**

先看一个简单的示例，state_machine_simple_introspection.py：

```text
#!/usr/bin/env python

import rospy
import smach
import smach_ros

# define state Foo
class Foo(smach.State):
  def __init__(self):
  smach.State.__init__(self, outcomes=['outcome1','outcome2'])
  self.counter = 0

  def execute(self, userdata):
  rospy.loginfo('Executing state FOO')
  if self.counter < 3:
  self.counter += 1
  return 'outcome1'
  else:
  return 'outcome2'

# define state Bar
class Bar(smach.State):
  def __init__(self):
  smach.State.__init__(self, outcomes=['outcome2'])

  def execute(self, userdata):
  rospy.loginfo('Executing state BAR')
  return 'outcome2'

# main
def main():
  rospy.init_node('smach_example_state_machine')

  # Create a SMACH state machine
  sm = smach.StateMachine(outcomes=['outcome4', 'outcome5'])

  # Open the container
  with sm:
  # Add states to the container
  smach.StateMachine.add('FOO', Foo(), 
  transitions={'outcome1':'BAR', 
  'outcome2':'outcome4'})
  smach.StateMachine.add('BAR', Bar(), 
  transitions={'outcome2':'FOO'})

  # Create and start the introspection server
  sis = smach_ros.IntrospectionServer('my_smach_introspection_server', sm, '/SM_ROOT')
  sis.start()

  # Execute SMACH plan
  outcome = sm.execute()

  # Wait for ctrl-c to stop the application
  rospy.spin()
  sis.stop()

if __name__ == '__main__':
  
```

使用如下命令运行，看下启动之后的效果：

```text
$ roscore
$ rosrun smach_tutorials state_machine_simple.py
```

![img](https://pic2.zhimg.com/v2-931d3d7e2e6015ec782d70d852ee238d_b.jpg)

在终端中可以看到状态的跳转，但是这样的信息并不是很清晰，我们可以启动一个观测神器来可视化显示状态机：

```text
rosrun smach_viewer smach_viewer.py
```

![img](https://pic2.zhimg.com/v2-afea23b5adc8bb3c435a9f723ddb7595_b.jpg)

## **四、代码分析**

通过上边运行的效果你可能还没看明白，接下来我们就对照代码进行分析。

作为状态机，首先需要有状态，这个例程中有两个状态：**FOO、BAR**，我们来看一下这两个状态在代码中的定义：

```text
# define state Foo
class Foo(smach.State):
  def __init__(self):
    smach.State.__init__(self, outcomes=['outcome1','outcome2'])
    self.counter = 0

  def execute(self, userdata):
    rospy.loginfo('Executing state FOO')
    if self.counter < 3:
      self.counter += 1
      return 'outcome1'
    else:
      return 'outcome2'

# define state Bar
class Bar(smach.State):
  def __init__(self):
    smach.State.__init__(self, outcomes=['outcome2'])

  def execute(self, userdata):
    rospy.loginfo('Executing state BAR')
    return 'outcome2'
```

这两个状态都是通过Python的函数进行定义的，而且结构相似，都包含初始化（__init__）和执行（execute）这两个函数。

**1. 初始化函数**

初始化函数用来初始化该状态类，调用smach中状态的初始化函数，同时需要定义输出状态：outcome1、outcome2。

这里的outcome代表**状态结束时的输出值**，使用字符串表示，由用户定义取值的范围，例如我们可以定义状态执行是否成功：['succeeded', 'failed', 'awesome']。 每个状态的输出值可以有多个，根据不同额输出值有可能跳转到不同的下一个状态。

> 初始化函数中不能阻塞，如果需要实现同步等阻塞功能，可以使用多线程实现。

**2. 执行函数**

执行函数就是每个状态中的具体工作内容了，可以进行阻塞工作，当工作后需要返回定义的输出值，该状态结束。

再来看一下main函数：

```text
# main
def main():
  rospy.init_node('smach_example_state_machine')

  # Create a SMACH state machine
  sm = smach.StateMachine(outcomes=['outcome4', 'outcome5'])

  # Open the container
  with sm:
    # Add states to the container
    smach.StateMachine.add('FOO', Foo(), transitions={'outcome1':'BAR', 'outcome2':'outcome4'})
    smach.StateMachine.add('BAR', Bar(), transitions={'outcome2':'FOO'})

  # Create and start the introspection server
  sis = smach_ros.IntrospectionServer('my_smach_introspection_server', sm, '/SM_ROOT')
  sis.start()

  # Execute SMACH plan
  outcome = sm.execute()

  # Wait for ctrl-c to stop the application
  rospy.spin()
  sis.stop()
```

在main函数中，首先初始化ROS节点，然后**使用StateMachine创建一个状态机**，并且指定状态机执行结束后的最终输出值有两个：outcome4和outcome5。

SMACH状态机是一个**容器**，我们可以使用add方法添加需要的状态到状态机容器当中，同时需要设置状态之间的跳转关系。

```text
smach.StateMachine.add('FOO', Foo(), transitions={'outcome1':'BAR', 'outcome2':'outcome4'})
```

例如这里我们在状态机中添加一个名为“FOO”的状态，该状态的类就是我们之前定义的Foo，transitions代表状态的跳转，如果FOO状态执行输出outcome1时，则跳转到“BAR”状态，如果执行输出outcome2时，则结束这个状态机，并且输出outcome4。

还记得我们上边看到的可视化界面么，为了将状态机可视化显示，我们需要在代码中加入观测器：

```text
  # Create and start the introspection server
  sis = smach_ros.IntrospectionServer('my_smach_introspection_server', sm, '/SM_ROOT')
  sis.start()
```

IntrospectionServer()方法用来创建内部观测器，有三个参数：第一个参数是**观测服务器的名字**，可以根据需要自由给定；第二个参数是**所要观测的状态机**；第三个参数代表**状态机的层级**，因为SMACH状态机支持嵌套，状态内部还可以有自己的状态机。

然后就可以使用execute()方法开始执行状态机了，执行结束后需要讲内部观测器停止。

![img](https://pic1.zhimg.com/v2-8d1d45ad0d139d13a214f2a8f837a2bc_b.jpg)

现在再来回顾整个状态机，从图中我们可以看到：

> \1. 状态机开始工作后首先跳入我们添加的第一个状态“FOO”，然后在该状态中累加counter变量。
> \2. 当counter小于3时，会输出outcome1，状态结束后就跳转到“BAR”状态。
> \3. 在“BAR”状态中什么都没做，输出outcome2回到“FOO”状态。
> \4. 就这样来回几次之后，counter等于3，“FOO”状态的输出值变成outcome2，继而跳转到outcome4，也就代表着有限状态机运行结束。
> \5. outcome5全程并没有涉及到，所以在图上成为了一个孤立的节点。

ROS中的SMACH状态机是不是也并不复杂，将上边的状态机想象成一个简单的机器人应用：机器人去抓取桌子上的杯子，如果抓取到就结束任务，如果抓取不到就继续尝试，尝试3次还没抓到，就放弃抓取，结束任务。

SMACH的功能远远不止如此，比如这是一个复杂的状态机。。。。

![img](https://pic1.zhimg.com/v2-26661d316cc819ab07fbe7193f9dcb9c_b.jpg)

---

**— 一、数据传递 —**



在很多场景下，状态和状态之间有一定耦合，后一个状态的工作需要使用到前一个状态中的数据，这个时候就需要**在状态跳转的同时，将需要的数据传递给下一个状态**。SMACH支持状态之间的数据传递。



先来运行例程看下效果：

```text
roscore
rosrun smach_tutorials user_data.py
rosrun smach_viewer smach_viewer.py
```



启动后可以在终端中看到counter数据在打印输出：

![img](https://pic4.zhimg.com/v2-379adf68e9ced081aa84e2b8bd858987_b.jpg)

再来看一下状态机的结构：

![img](https://pic1.zhimg.com/v2-e347055e1653234dfe70586c6182bd58_b.jpg)

从终端和可视化显示中只能看到有两个状态，可能并不能明确的看到数据到底是如何传递的。我们要从代码进行分析：

```text
#!/usr/bin/env python

import rospy
import smach
import smach_ros

# define state Foo
class Foo(smach.State):
   def __init__(self):
       smach.State.__init__(self,
                            outcomes=['outcome1','outcome2'],
                            input_keys=['foo_counter_in'],
                            output_keys=['foo_counter_out'])

   def execute(self, userdata):
       rospy.loginfo('Executing state FOO')
       if userdata.foo_counter_in < 3:
           userdata.foo_counter_out = userdata.foo_counter_in + 1
           return 'outcome1'
       else:
           return 'outcome2'


# define state Bar
class Bar(smach.State):
   def __init__(self):
       smach.State.__init__(self,
                            outcomes=['outcome1'],
                            input_keys=['bar_counter_in'])

   def execute(self, userdata):
       rospy.loginfo('Executing state BAR')
       rospy.loginfo('Counter = %f'%userdata.bar_counter_in)       
       return 'outcome1'


def main():
   rospy.init_node('smach_example_state_machine')

   # Create a SMACH state machine
   sm = smach.StateMachine(outcomes=['outcome4'])
   sm.userdata.sm_counter = 0

   # Open the container
   with sm:
       # Add states to the container
       smach.StateMachine.add('FOO', Foo(),
                              transitions={'outcome1':'BAR',
                                           'outcome2':'outcome4'},
                              remapping={'foo_counter_in':'sm_counter',
                                         'foo_counter_out':'sm_counter'})
       smach.StateMachine.add('BAR', Bar(),
                              transitions={'outcome1':'FOO'},
                              remapping={'bar_counter_in':'sm_counter'})

   # Create and start the introspection server
   sis = smach_ros.IntrospectionServer('my_smach_introspection_server', sm, '/SM_ROOT')
   sis.start()

   # Execute SMACH plan
   outcome = sm.execute()

   # Wait for ctrl-c to stop the application
   rospy.spin()
   sis.stop()

if __name__ == '__main__':
   main()
```

代码是在上一篇例程的基础上改进的，我们来找不同，看一下修改了哪些地方。

首先看状态的定义：

```text
# define state Foo
class Foo(smach.State):
   def __init__(self):
       smach.State.__init__(self,
                            outcomes=['outcome1','outcome2'],
                            input_keys=['foo_counter_in'],
                            output_keys=['foo_counter_out'])

   def execute(self, userdata):
       rospy.loginfo('Executing state FOO')
       if userdata.foo_counter_in < 3:
           userdata.foo_counter_out = userdata.foo_counter_in + 1
           return 'outcome1'
       else:
           return 'outcome2'
```

可以发现在状态的初始化中多了两个参数：**input_keys**和**output_keys**，这两个参数就是状态的输入输出数据。

在状态的执行函数中，函数的参数也多了一个**userdata**参数，这就是存储状态之间需要传递数据的容器，Foo状态的输入输出数据foo_counter_in和foo_counter_out就存储在userdata中。所以在执行工作时，如果要访问、修改数据，需要使用userdata.foo_counter_out和userdata.foo_counter_in的形式。

```text
# define state Bar
class Bar(smach.State):
   def __init__(self):
       smach.State.__init__(self,
                            outcomes=['outcome1'],
                            input_keys=['bar_counter_in'])

   def execute(self, userdata):
       rospy.loginfo('Executing state BAR')
       rospy.loginfo('Counter = %f'%userdata.bar_counter_in)
       return 'outcome1'
```

在Bar状态中，只需要输入数据bar_counter_in，从上边的可视化图中可以看到，Bar状态由Foo状态转换过来，所以**Bar的输入数据就是Foo的输出数据**。

这里你可能会有一个疑问：Foo的输出是foo_counter_out，Bar的输入是bar_counter_in，驴头不对马嘴呀！不着急，我们继续看main函数：

```text
sm.userdata.sm_counter = 0
```

这里定义了状态之间传递数据的变量sm_counter，怎么和Foo、Bar里的又不一样！接下来就是重点了：

```text
# Open the container
   with sm:
       # Add states to the container
       smach.StateMachine.add('FOO', Foo(),
                              transitions={'outcome1':'BAR',
                                           'outcome2':'outcome4'},
                              remapping={'foo_counter_in':'sm_counter',
                                         'foo_counter_out':'sm_counter'})
       smach.StateMachine.add('BAR', Bar(),
                              transitions={'outcome1':'FOO'},
                              remapping={'bar_counter_in':'sm_counter'})
```

在状态机中添加状态时，多了一个**remapping**参数，如果熟悉ROS，相信你一定想到了ROS中的remapping重映射机制，类似的，这里可以将**参数重映射**，每个状态在设计的时候不需要考虑输入输出的变量具体是什么，只需要留下接口，使用重映射的机制就可以很方便的组合这些状态了。这个和ROS的框架原理很类似，SMACH确实和ROS很配呀！

所以这里我们将sm_counter映射为 foo_counter_in、foo_counter_out、bar_counter_in，也就是给sm_counter取了一堆别名，这样Foo和Bar里边的所有输入输出变量，其实都是sm_counter。在运行终端中可以看到，sm_counter在Foo累加后传递到Bar状态中打印出来了，该参数传递成功。

**二、状态机嵌套**

SMACH中的**状态机是容器**，支持嵌套功能，也就是说在状态机中还可以实现一个内部的状态机。

我们运行以下例程看一下状态机嵌套是什么样的：

```text
roscore
rosrun smach_tutorials state_machine_nesting.py
rosrun smach_viewer smach_viewer.py
```

终端中的显示：

![img](https://pic4.zhimg.com/v2-643b9e50bc29f43b10e09ba20f05bb43_b.jpg)

可视化效果：

![img](https://pic2.zhimg.com/v2-3f59ba83a46812ca66cc70ee500fb861_b.jpg)

在可视化显示中，我们可以看到一个灰色框区域，这个就是状态SUB内部的嵌套状态机。代码实现过程如下：

```text
#!/usr/bin/env python

import rospy
import smach
import smach_ros

# define state Foo
class Foo(smach.State):
   def __init__(self):
       smach.State.__init__(self, outcomes=['outcome1','outcome2'])
       self.counter = 0

   def execute(self, userdata):
       rospy.loginfo('Executing state FOO')
       if self.counter < 3:
           self.counter += 1
           return 'outcome1'
       else:
           return 'outcome2'

# define state Bar
class Bar(smach.State):
   def __init__(self):
       smach.State.__init__(self, outcomes=['outcome1'])

   def execute(self, userdata):
       rospy.loginfo('Executing state BAR')
       return 'outcome1'

# define state Bas
class Bas(smach.State):
   def __init__(self):
       smach.State.__init__(self, outcomes=['outcome3'])

   def execute(self, userdata):
       rospy.loginfo('Executing state BAS')
       return 'outcome3'


def main():
   rospy.init_node('smach_example_state_machine')

   # Create the top level SMACH state machine
   sm_top = smach.StateMachine(outcomes=['outcome5'])

   # Open the container
   with sm_top:

       smach.StateMachine.add('BAS', Bas(),
                              transitions={'outcome3':'SUB'})

       # Create the sub SMACH state machine
       sm_sub = smach.StateMachine(outcomes=['outcome4'])

       # Open the container
       with sm_sub:

           # Add states to the container
           smach.StateMachine.add('FOO', Foo(),
                                  transitions={'outcome1':'BAR',
                                               'outcome2':'outcome4'})
           smach.StateMachine.add('BAR', Bar(),
                                  transitions={'outcome1':'FOO'})

       smach.StateMachine.add('SUB', sm_sub,
                              transitions={'outcome4':'outcome5'})

   # Create and start the introspection server
   sis = smach_ros.IntrospectionServer('my_smach_introspection_server', sm_top, '/SM_ROOT')
   sis.start()

   # Execute SMACH plan
   outcome = sm_top.execute()

   # Wait for ctrl-c to stop the application
   rospy.spin()
   sis.stop()

if __name__ == '__main__':
   main()
```

我们新加入了一个状态Bas，三个状态Foo、Bar、Bas的定义和实现没有什么特别的，重点在main函数中。

```text
# Create the top level SMACH state machine
   sm_top = smach.StateMachine(outcomes=['outcome5'])

   # Open the container
   with sm_top:

       smach.StateMachine.add('BAS', Bas(),
                              transitions={'outcome3':'SUB'})
```

首先定义了一个状态机**sm_top**，我们将这个状态机作为最顶层，并且在这个状态机中加入了一个Bas状态，该状态在初始为outcome3时会跳转到SUB状态。

```text
# Create the sub SMACH state machine
       sm_sub = smach.StateMachine(outcomes=['outcome4'])

# Open the container
       with sm_sub:

           # Add states to the container
           smach.StateMachine.add('FOO', Foo(),
                                  transitions={'outcome1':'BAR',
                                               'outcome2':'outcome4'})
           smach.StateMachine.add('BAR', Bar(),
                                  transitions={'outcome1':'FOO'})
```

接着我们又定义了一个状态机**sm_sub**，并且还在这个状态机中添加了两个状态Foo和Bar，我们将这个状态认为是要嵌套的状态机。

目前这两个状态机还是独立的，我们需要把sm_sub嵌套在sm_top中：

```text
smach.StateMachine.add('SUB', sm_sub,
                              transitions={'outcome4':'outcome5'})
```

类似于添加状态一样，状态机也可以直接使用add方法嵌套添加。

我们来回顾一下两个状态机的输入输出，sub_top中的Bas状态输出是outcome3，会跳到“SUB”状态，也就是sm_sub这个子状态机。sm_sub状态机的输出是outcome4，正好对应到了sm_top的outcome5状态。

不知道你现在是否已经绕糊涂了，回顾一下上边的结构图应该就清晰了。

**三、状态并行**

SMACH还支持多个状态并列运行:

```text
roscore
rosrun smach_tutorials concurrence.py
rosrun smach_viewer smach_viewer.py
```

终端显示：

![img](https://pic1.zhimg.com/v2-92c68609df9ddd797a2cb1eaaa2b8448_b.jpg)

可视化效果：

![img](https://pic3.zhimg.com/v2-20e61a62cd2a44446515bf1594ee9c3e_b.jpg)

从图中可以看到，FOO和BAR两个状态是并列运行的，代码实现如下：

```text
#!/usr/bin/env python

import rospy
import smach
import smach_ros

# define state Foo
class Foo(smach.State):
   def __init__(self):
       smach.State.__init__(self, outcomes=['outcome1','outcome2'])
       self.counter = 0

   def execute(self, userdata):
       rospy.loginfo('Executing state FOO')
       if self.counter < 3:
           self.counter += 1
           return 'outcome1'
       else:
           return 'outcome2'

# define state Bar
class Bar(smach.State):
   def __init__(self):
       smach.State.__init__(self, outcomes=['outcome1'])

   def execute(self, userdata):
       rospy.loginfo('Executing state BAR')
       return 'outcome1'

# define state Bas
class Bas(smach.State):
   def __init__(self):
       smach.State.__init__(self, outcomes=['outcome3'])

   def execute(self, userdata):
       rospy.loginfo('Executing state BAS')
       return 'outcome3'

def main():
   rospy.init_node('smach_example_state_machine')

   # Create the top level SMACH state machine
   sm_top = smach.StateMachine(outcomes=['outcome6'])

   # Open the container
   with sm_top:

       smach.StateMachine.add('BAS', Bas(),
                              transitions={'outcome3':'CON'})

       # Create the sub SMACH state machine
       sm_con = smach.Concurrence(outcomes=['outcome4','outcome5'],
                                  default_outcome='outcome4',
                                  outcome_map={'outcome5':
                                      { 'FOO':'outcome2',
                                        'BAR':'outcome1'}})

       # Open the container
       with sm_con:
           # Add states to the container
           smach.Concurrence.add('FOO', Foo())
           smach.Concurrence.add('BAR', Bar())

       smach.StateMachine.add('CON', sm_con,
                              transitions={'outcome4':'CON',
                                           'outcome5':'outcome6'})

   # Create and start the introspection server
   sis = smach_ros.IntrospectionServer('my_smach_introspection_server', sm_top, '/SM_ROOT')
   sis.start()

   # Execute SMACH plan
   outcome = sm_top.execute()

   # Wait for ctrl-c to stop the application
   rospy.spin()
   sis.stop()

if __name__ == '__main__':
   main()
```

这个例程从之前的嵌套状态机例程发展而来，绝大部分代码是类似的，重点还是在main函数中：

```text
# Create the sub SMACH state machine
       sm_con = smach.Concurrence(outcomes=['outcome4','outcome5'],
                                  default_outcome='outcome4',
                                  outcome_map={'outcome5':
                                      { 'FOO':'outcome2',
                                        'BAR':'outcome1'}})

       # Open the container
       with sm_con:
           # Add states to the container
           smach.Concurrence.add('FOO', Foo())
           smach.Concurrence.add('BAR', Bar())
```

这里我们使用Concurrence创建了一个同步状态机，default_outcome表示该状态机的默认输出是outcome4，也就是依然会循环该状态机，重点是**outcome_map**参数，设置了状态机同步运行的状态跳转，当FOO状态的输出为outcome2、BAR状态的输出为outcome1时，状态机才会输出outcome5，从而跳转到顶层状态机中。

OK，今天我们又学习了SMACH的三种使用方法，总结一下这两篇对SMACH的探索总结：

1. SMACH是一个功能强大的任务级有限状态机
2. SMACH中的状态机是一个容器，支持状态机嵌套
3. SMACH状态机在状态跳转的时候支持数据传递，也支持多个状态同步运行，同时满足条件才能跳转
4. smach_viewer是个好东西！