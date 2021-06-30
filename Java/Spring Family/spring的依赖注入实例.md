---
title: spring的依赖注入实例
categories:
- Java
- Spring
- DI
tags:
- DI
date: 2021/6/27
---



理解依赖注入的优势前, 先要理解是什么是单元测试(unit test)。

如果你只是写一个几十行不到的小脚本，你可能不需要什么单元测试，只要写完之后运行一下，看看是否跑通，没跑通看看出了什么错，然后根据错误提示找到bug修复，直到跑通为止。

但是如果你写的程序是几万行呢？如果是几十个人一起合作写一个项目呢？你难道要每次都把一个几万行的程序完整运行一遍，看看是否跑通？

显然不行，那样的话就算程序出了问题，你也没办法定位。

那么我们就把这几万行代码分成好多个模块，每个模块尽量小，比如就是一个小函数，小类，然后对这每个模块，进行单元测试。

上代码看看，看这块代码：

```java
public class VotingBooth {

    VoteRecorder voteRecorder = new VoteRecorder();
    
    public void vote(Candidate candidate) {
        voteRecorder.record(candidate);
    }
    
    class VoteRecorder {
        Map hVotes = new HashMap();
        
        public void record(Candidate candidate) {
            int count = 0;
            
            if (!hVotes.containsKey(candidate)){
                hVotes.put(candidate, count);
            } else {
                count = hVotes.get(candidate);
            }
            
            count++; 1
            
            hVotes.put(candidate, count);  2
            
        }
    }

}
```

代码很简单，就是一个投票纪录器而已，但是我们发现VoteRecorder类在VotingBooth类里面，换句话说就是这个VotingBooth依赖于VoteRecorder，那假如我想给VoteBooth做一个单元测试，那么无可避免的也同时对VoteRecorder做了测试，我们希望把这个测试尽可能地分开，先单独对VoteRecorder做测试，确保它没有问题之后，再对VoteBooth做测试。

所以我们希望尽可能降低代码之间的耦合度，什么是耦合度呢？

**两个东西如果耦合在一起，就不太好将它们分开了，当改变其中一个的时候，也可能影响另一个的运行状态，让别的部分无法工作，你如果想复用代码中的某一部分，也难以将这一部分代码提取出来，给代码的复用性，代码的测试，代码的改进，修改增添了不必要的困难。**

那我们将上面的代码做一个改进，降低其耦合度，说白了就是把两个类分开呗：

```java
public class VoteRecorder{
    Map hVotes = new HashMap();
    public void record(Candidate candidate) {
        int count = 0;
        if (!hVotes.containsKey(candidate)){
            hVotes.put(candidate, count);
        } else {
            count = hVotes.get(candidate);
        }
        count++;
        hVotes.put(candidate, count);
    }
}
public class VotingBooth {
    VoteRecorder recorder = null;
    public void setVoteRecorder(VoteRecorder recorder) {
        this.recorder = recorder;
    }
    public void vote(Candidate candidate) {
        recorder.record(candidate);
    }
}
                  
```

那么这样，VoteRecorder和VotingBooth是不是就分开了？

注意，分开了不代表就不发生联系了，分开只是指代码层面上的分开，它们之间仍然在交流，只不过是通过“注入”的方式，什么意思呢，就是我们看VotingBooth的setVoteRecorder（）方法中也是需要一个VoteRecorder的实例的。

**“注入”（Injection）的意思是什么？说白了就是从外界传入的意思，而不是里面本来就有。**

你需要这个东西，我就给你，你不需要，那就先不给你。

就像你要做一个手工品，你需要剪刀的时候，我给你剪刀，你需要胶水的时候，我给你胶水，这是从根据需求从外界传入，也就是“注入”，而不是一开始就把所有可能需要的东西都准备好。

你再看上面的实例代码，是不是能理解 **VoteRecorder注入到了VoteBooth里面** 的含义了？

但我们发现将代码整成这样之后，如果我要使用这些代码，就需要做一些“多余”的步骤，那就是：手动创建实例。

如果是原来的代码，未修改前的，如果你要调用VotingBooth的vote（）方法来记录投票数据，你只需要创建一个VotingBooth的实例，然后直接调用该方法即可：

```java
VotingBooth votingBooth = new VotingBooth();
votingBooth.vote(...);
```

修改后的代码呢？我们需要这样：

```java
VotingRecorder votingRecorder = new VotingRecorder();

VotingBooth votingBooth = new VotingBooth(votingRecorder);
votingBooth.vote(...);
```

也就是我们为了增强代码的可测试性，复用性，降低耦合性，将代码改为“依赖注入”这种形式后，再代码的调用上，增添了新的负担，也就是实例的创造（Instantiation）。

感觉好麻烦诶。

没关系，刚刚增添的这些负担，我们自然不想让它成为程序员的多余工作, 于是我们让Spring来帮忙!

所以Spring框架的一个重要功能就是, **它自动帮你进行依赖注入，你没有必要自己来做这个事情。**

那如果现在有了Spring的帮助了，你要调用这些类，只需怎么做呢？

只需要这样：

```text
public main(VotingBooth votingBooth) {
  votingBooth.vote(...);
}
```

没了。就这？就这。

因为这个“votingBooth”的实例的创造，注入，都是spring帮你进行的，也就是Ioc容器，你只需要在函数的需要的参数里声明“我需要一个votingBooth”，Spring框架会帮你创造一个实例，再传入这个函数，减少了程序员不必要的劳动。