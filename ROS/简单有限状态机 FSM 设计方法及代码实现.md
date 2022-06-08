> [blog.csdn.net](https://blog.csdn.net/qq_35635374/article/details/121626001)

前言
==

本文举例是以 fast_planning 开源工程为例子抽象出来的

有限状态机FSM 的概念
====================================================================================================

![](https://img-blog.csdnimg.cn/53ddf115ba244ce7aa4c62dd3bea8a39.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA55uS5a2Q5ZCbfg==,size_20,color_FFFFFF,t_70,g_se,x_16)  
一个有限状态机通常包含如下几部分：

1.  **输入集合**：通常也叫刺激集合，包含我们考虑到的状态机可能收到的所有输入。通常我们使用符号 Σ 表示这个集合。一个简单的例子，假设我们的无人车上有启动，停止两个按钮（我们以 a，b 代替，不能同时被按下），那么以这两个按钮为输入的 FSM 的输入集合 Σ={a,b}。
2.  **输出集合**：即 FSM 能够作出的响应的集合，这个集合也是有限的，我们通常使用符号 Λ 来表示输出集合，很多情况下 FSM 并不一定有输出，即 Λ 为空集。
3.  **状态集合**：我们通常使用有向图来描述 FSM 内部的状态和转移逻辑，我们使用符号 S 来表示有向图中状态的集合。
4.  **FSM 通常有一个固定的初始状态**（不需要任何输入，状态机默认处于的状态），我们使用符号 s0 表示。
5.  **结束状态集合**，是状态机 S 的子集，也有可能为空集（即整个状态机没有结束状态），通常使用符号 F 表示。
6.  **转移逻辑**：即状态机从一个状态转移到另一个状态的条件（通常是当前状态和输入的共同作用），比如说我们要从上图的 Python 状态转移到 Error 状态，需要的条件是： 1）状态机处于 Python 状态；2）输入不是 “is”。我们通常使用状态转移函数来描述转移逻辑：δ:S×Σ→S 
    接收器（Acceptors）和变换器 (Transducers): 根据是否有输出可以将感知机分为两类：接收器和变换器，其中接收器是指没有输出但是有结束状态，而变换器则有输出集合。

FSM 可进一步区分为确定型（Deterministic）和非确定型（Non-Deterministic）自动机。在确定型自动机中，每个状态对每个可能输入只有精确的一个转移。在非确定型自动机中，给定状态对给定可能输入可以没有或有多于一个转移。

一、步骤 1：思考清楚系统所有的状态和转移条件，画出状态转移图
===============================

示例（fast_planning）
-----------------

画状态转移图出来可以理清楚所有的状态和转移条件，排查合理性，例如不能存在状态进去了就出不来导致系统卡死

画状态转移图最重要的是更清楚的知道状态机有没有和业务匹配，排查合理性

![](https://img-blog.csdnimg.cn/03eecaae6b4c4371a8a1b0bbb97e91ba.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA55uS5a2Q5ZCbfg==,size_20,color_FFFFFF,t_70,g_se,x_16)

二、设计有限状态机 FSM 的枚举状态与变量
======================

这个变量状态的一般定义在. h 对应的 class 里面，状态机实现的逻辑部分写在. cpp 中

```cpp
class KinoReplanFSM {

private:
  /* ---------- FSM flag ---------- */
  enum FSM_EXEC_STATE { INIT, WAIT_TARGET, GEN_NEW_TRAJ, REPLAN_TRAJ, EXEC_TRAJ, REPLAN_NEW };
  FSM_EXEC_STATE exec_state_;

  /*--- fsm functions ---*/
  void execFSMCallback(const ros::TimerEvent& e);
  void changeFSMExecState(FSM_EXEC_STATE new_state, string pos_call);
  void printFSMExecState();
  
  /*--- other functions ---*/
  //状态机某个状态中的实现逻辑函数

  /* ---other flag --- */
  //其他模块的标志位

  /* ---other parameters--- */
  //其他模块的参数
  
  /* ---other data--- */
  //其他模块的数据
  
  /* ---other modular API--- */
  //其他模块的API或者代理
  
public:
  KinoReplanFSM(/* args */) {	//FSM构造函数
  }
  ~KinoReplanFSM() {			//FSM析构函数
  }

  void init(ros::NodeHandle& nh);//FSM初始化函数

}
```

三、设计状态机的初始化函数、构造函数
==================

这一函数一般是先设置了**一系列参数**，实例化**一系列模块算法的类单例**，初始化**一系列系统组件**，等等。具体步骤就在这些组件的调用及**回调函数 / 定时器函数**里实现。

```
void KinoReplanFSM::init(ros::NodeHandle& nh) {
/*  系统其他模块状态的一些flag状态  */

/* initialize main modules */

/*  fsm param  */

/* 话题、服务、定时器的callback */

}

```

四、设计状态转移函数
==========

状态转移函数的作用是负责在符合条件的情况下，进行有限状态即 FSM 的状态转移，每次状态转移的时候最好打印输出一下日志

```
//状态转移函数
void FSM::changeFSMExecState(FSM_EXEC_STATE new_state, string pos_call) {
  string state_str[5] = { "INIT", "WAIT_TARGET", "GEN_NEW_TRAJ", "REPLAN_TRAJ", "EXEC_TRAJ" };
  int    pre_s        = int(exec_state_);
  exec_state_         = new_state;
  cout << "[" + pos_call + "]: from " + state_str[pre_s] + " to " + state_str[int(new_state)] << endl;
}

```

五、设计检查状态机 FSM 正在执行的状态的函数（定时调用，用于 debug）
=======================================

```
void FSM::printFSMExecState() {
  string state_str[5] = { "INIT", "WAIT_TARGET", "GEN_NEW_TRAJ", "REPLAN_TRAJ", "EXEC_TRAJ" };

  cout << "[FSM]: state: " + state_str[int(exec_state_)] << endl;
}

```

六、【重要】设计执行状态机 FSM 的定时器回调函数
==========================================================================================================================

注意：这个函数必须是定时循环调用的，最好单独开一个线程给它，不被打断提高稳定性，不过在定时，或者 main 的死循环里面实现也是可以的

```
void FSM::execFSMCallback(const ros::TimerEvent& e) {
  //（1）设置状态机的运行频率，同时检查系统其他被FSM调用的模块的状态，同时定时打印一次当前执行状态
  static int fsm_num = 0;
  fsm_num++;
  if (fsm_num == 100) {
    printFSMExecState();
    if (!have_odom_) cout << "no odom." << endl;
    if (!trigger_) cout << "wait for goal." << endl;
    fsm_num = 0;
  }


  //（2）根据执行状态变量 exec_state_进入switch循环，进行状态转移
  switch (exec_state_) {

	case INIT: {
	/*（1）运行该状态相关的算法逻辑功能函数*/
	//注意算法逻辑功能函数运行时间不能超过FSM的运行周期，
	//甚至算法逻辑功能函数内不能有死循环
	//不然状态本次状态还没有运行完，就出发下一次运行，系统会有偶然的bug出现

	/*（2）判断有限状态机FSM的状态转移条件，若符合则进行状态转移*/
	if(XXX1){
	 changeFSMExecState(XXX1, "FSM");
	}
	else if(XXX2){
	 changeFSMExecState(XXX2, "FSM");
	}
	else{
	changeFSMExecState(XXX3, "FSM");
	}
	
	/*（3）跳出该状态*/
	 break;
	}


   case WAIT_TARGET: {
   	/*（1）运行该状态相关的算法逻辑功能函数*/
	//注意算法逻辑功能函数运行时间不能超过FSM的运行周期，
	//甚至算法逻辑功能函数内不能有死循环
	//不然状态本次状态还没有运行完，就出发下一次运行，系统会有偶然的bug出现

	/*（2）判断有限状态机FSM的状态转移条件，若符合则进行状态转移*/
	if(XXX1){
	 changeFSMExecState(XXX1, "FSM");
	}
	else if(XXX2){
	 changeFSMExecState(XXX2, "FSM");
	}
	else{
	changeFSMExecState(XXX3, "FSM");
	}
	
	/*（3）跳出该状态*/
	 break;
   }

	case GEN_NEW_TRAJ: {
	/*（1）运行该状态相关的算法逻辑功能函数*/
	//注意算法逻辑功能函数运行时间不能超过FSM的运行周期，
	//甚至算法逻辑功能函数内不能有死循环
	//不然状态本次状态还没有运行完，就出发下一次运行，系统会有偶然的bug出现

	/*（2）判断有限状态机FSM的状态转移条件，若符合则进行状态转移*/
	if(XXX1){
	 changeFSMExecState(XXX1, "FSM");
	}
	else if(XXX2){
	 changeFSMExecState(XXX2, "FSM");
	}
	else{
	changeFSMExecState(XXX3, "FSM");
	}
	
	/*（3）跳出该状态*/
	 break;
	}

	case EXEC_TRAJ: {
	/*（1）运行该状态相关的算法逻辑功能函数*/
	//注意算法逻辑功能函数运行时间不能超过FSM的运行周期，
	//甚至算法逻辑功能函数内不能有死循环
	//不然状态本次状态还没有运行完，就出发下一次运行，系统会有偶然的bug出现

	/*（2）判断有限状态机FSM的状态转移条件，若符合则进行状态转移*/
	if(XXX1){
	 changeFSMExecState(XXX1, "FSM");
	}
	else if(XXX2){
	 changeFSMExecState(XXX2, "FSM");
	}
	else{
	changeFSMExecState(XXX3, "FSM");
	}
	
	/*（3）跳出该状态*/
	 break;
	}


	case REPLAN_TRAJ: {
	/*（1）运行该状态相关的算法逻辑功能函数*/
	//注意算法逻辑功能函数运行时间不能超过FSM的运行周期，
	//甚至算法逻辑功能函数内不能有死循环
	//不然状态本次状态还没有运行完，就出发下一次运行，系统会有偶然的bug出现

	/*（2）判断有限状态机FSM的状态转移条件，若符合则进行状态转移*/
	if(XXX1){
	 changeFSMExecState(XXX1, "FSM");
	}
	else if(XXX2){
	 changeFSMExecState(XXX2, "FSM");
	}
	else{
	changeFSMExecState(XXX3, "FSM");
	}
	
	/*（3）跳出该状态*/
	 break;
	}
	

 }

}
```

七、设计相关算法逻辑功能函数
==============

算法逻辑功能函数一般放置在有限状态机 FSM 的某个状态实现中，一般把算法逻辑功能函数的数据类型设置成布尔 bool 型，算法逻辑功能函数运行正常则返回 true, 算法逻辑功能函数运行不正常或者超时则返回 false

为什么要把算法逻辑功能函数设置成 bool 的数据类型呢？ 

- 第一、不是所有的算法运行都能够输出结果的  
- 第二、通过算法逻辑功能函数返回类型能作为状态转移条件的判断

```
      bool success = callKinodynamicReplan();
      if (success) {
        changeFSMExecState(EXEC_TRAJ, "FSM");
      } else {
        // have_target_ = false;
        // changeFSMExecState(WAIT_TARGET, "FSM");
        changeFSMExecState(GEN_NEW_TRAJ, "FSM");
      }
      break;
```
