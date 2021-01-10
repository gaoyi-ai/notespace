---
title: Algorithm Visualization
categories:
- DSA
- Algorithm
- Visualization
tags:
- Visualization
date: 2020/12/25 20:00:17
updated: 2020/12/28 12:00:17
---

> Reference: 

# 算法可视化

采用MVC模式

Model 对应的数据由于差异性，需要每次修改

```java
/**
 * Viewer
 */
public class AlgoFrame extends JFrame {
```

```java
/**
 * Controller
 */
public class AlgoVisualizer {
```



## 随机模拟问题

### 一个有意思的分钱问题

房间里有100个人，每人都有100元钱，他们在玩一个游戏。每轮游戏中，每个人都要拿出一元钱随机给另一个人，最后这100个人的财富分布是怎样的？

>[Counterintuitive problem: Everyone in a room keeps giving dollars to random others. You’ll never guess what happens next.](http://www.decisionsciencenews.com/2017/06/19/counterintuitive-problem-everyone-room-keeps-giving-dollars-random-others-youll-never-guess-happens-next/)

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/money.gif" alt="money" style="zoom: 33%;" />

1. 虽然可以缩小这个delay的值，但是缩小这个delay的值其实是有一定的限度的
    如果想快速的模拟很长时间以后，这个结果是怎样的，那么单纯的缩小这个delay 的值是没有用的

- [x] 现在的动画逻辑其实就相当于是每进行绘制一次，然后进行一轮游戏，每一次更新不仅仅更新一轮，而更新k 轮，这样就达到了加快模拟的目的

2. 那么钱最多的人，他的财富值将显示在我们的这个窗口的右侧，此时就能大概齐的看出来这个财富的分布；从大家都是一个水平慢慢的产生了变化，有了非常大的不同，而且这个变化整体呢还似乎不仅仅是线性的这里呢有一个弧度，它确实倾向于是一个幂指数这样的一个分布

    但是这里大家要注意一个问题，虽然这是一个幂指数的分布，不过由于已经排完序，所以其实每一次运行这个程序，这个所谓的财富最多的这个人，他不一定是同一个人，而是具有一定随机性的

- [x] 所以这个实验只能说明这样的模拟这个问题的结果会让财富成一个幂指数这样的形状进行分布，但这不代表每一次值最大的是同样一个人

```java
	// TODO: 编写自己的动画逻辑
    public void run(){

        while(true){

            // 改进2：是否排序
            Arrays.sort(money);
            frame.render(money);
            AlgoVisHelper.pause(DELAY);
            
            // 改进1：每一帧执行的轮数
            for(int k = 0 ; k < 50 ; k ++){
                for(int i = 0 ; i < money.length; i ++){
                    // 改进3：允许money为负值
                    //if(money[i] > 0){
                        int j = (int)(Math.random() * money.length);
                        money[i] -= 1;
                        money[j] += 1;
                    //}
                }
            }
        }
    }
```

事实上，这个模拟将一定呈现出这种不稳定的形式，而不会是呈现出最终大家所有的人的钱都是差不多的情况，那么这具体是什么原因呢，一个相对比较简单的解释是可以把这一百个人，每个人手里有多少的钱，这样的一个事情看作是一个状态，那么大家可以想象一下这个状态的数字是巨大的，有一百个人人在初始的情况下，每人有一百块钱，那么一共就一万块钱，那么状态总数其实就是将这一万块钱分给一百个人，一共有多少个分法，那么这是一个比较经典的数学问题，也可以看作是一个整数划分的问题，事实上这个状态的数量是非常大的，那么经过这种随机模拟之后，最终的结果一定是在这，随机的状态中的一个状态，那么在这里大家要注意，对于这个状态空间来说，所有的人的钱都差不多，这样的状态只是很小的一部分，而大部分状态一定呈现出，有的人的钱非常多，有的人的钱非常少，这样的一个不稳定的状态，只不过具体谁的钱多，谁的钱少是不一样的

也正因为如此，大家可以理解成，最终模拟出来的结果就是在这个状态空间中抽取一个状态，那么事实上得到的是一个不稳定的状态，这个概率是非常高的，那么刚才的这个解释呢不算非常的精确

> 事实上呢这是一个非常经典的物理学的热学研究的场景
> 在这样的一个空间中能量是固定的，那么在能量的传递过程中，最终的结果一定是，使得熵越来越大，那么熵这个概念实际上就是在描述这个空间中的无序程度，也就是这个空间将越来越无序
> 呈现出这样的状态

### 蒙特卡洛算法

通过大量的随机样本。来了解一个系统，进而得到所要计算的值

那么在这里大家要注意蒙特卡罗方法，使用大量的随机样本来去获得我们所要计算的这个值。但是获得的这个值不一定是真值。
而是一个近似值。那么事实上摩托卡罗方法就是利用这样的一个原理，在一些问题中我们可以使用大量的随机样本去模拟。
我们的样本量越大，最终模拟出来的值就相应的会越准确。

假设我们现在并不知道Π的值的话，希望你获得一个Π的近似值的话，那应该怎么做呢？
在这种时候我们就可以使用蒙特卡罗的方法。大家可以看在这个图中其实是一个正方形。在这个正方形中有一个直径和这个正方形的边长一样的圆。那么首先我们来看在这个图形中，圆和这个正方形之间的关系。

- 圆的面积=PI * R * R
- 方形面积=(2 * R) * (2 * R)=4 * R * R
- PI=4 * 圆 / 方

圆的面积，我们怎么获得呢？在这里就使用蒙特卡罗的方法来近似的模拟圆的面积。

在这个正方形中随机地打入一个点，那么这个点可能会落入⚪内，也可能会落入⚪外。那么如果打得点非常多的话，红色点的数量就可以近似的来表示圆的面积。而红色点加绿色点的数量就可以近似的来表示方的面积。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201212191218103.png" alt="image-20201212191218103" style="zoom:50%;" />

### 三门问题

参赛者会看见三扇关闭的门，其中有一扇门的后面呢是一辆汽车。相当于就是一个大奖。现在呢如果参赛者选中了后面有车的那扇门的话，就可以赢得这辆汽车。而另外的两扇门后面呢什么都没有。
但是在这里问题是参赛者不是简单的选一扇门，然后看自己中没中奖，而是有这样的一个环节。当参赛者选定一扇门的时候。在开启这扇门之前，主持人会先开启剩下的两扇门中的一扇门。并且呢这扇门的背后肯定是没有汽车的。比如说你选中了是a 门，于是主持人会开启，比如说b 门，并且告诉你b 门后头没有汽车。此时这个奖品要么在你选的这个a 门的后面，要么在剩下的这。这个c 门的后面现在主持人会问一个问题，给你另外一次机会，问你是否要改变你的选择。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201212200958969.png" alt="image-20201212200958969" style="zoom:67%;" />

```java
public class ThreeGatesExperiment {

    private int N;

    public ThreeGatesExperiment(int N){

        if(N <= 0)
            throw new IllegalArgumentException("N must be larger than 0!");

        this.N = N;
    }

    public void run(boolean changeDoor){

        int wins = 0;
        for(int i = 0 ; i < N ; i ++)
            if(play(changeDoor))
                wins ++;

        System.out.println(changeDoor ? "Change" : "Not Change");
        System.out.println("winning rate: " + (double)wins/N);
    }

    private boolean play(boolean changeDoor){

        // Door 0, 1, 2
        int prizeDoor = (int)(Math.random() * 3);
        int playerChoice = (int)(Math.random() * 3);

        if( playerChoice == prizeDoor)
            return changeDoor ? false : true;
        else
            return changeDoor ? true : false;
    }

    public static void main(String[] args) {

        int N = 10000000;
        ThreeGatesExperiment exp = new ThreeGatesExperiment(N);

        exp.run(true);
        System.out.println();
        exp.run(false);
    }
}
```

## 排序算法可视化

### Selection Sort

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/selection_sort.gif" alt="selection_sort" style="zoom: 50%;" />

这个动画首先每一次在选择排序的过程中，都会去寻找后面未排序部分的一个最小值。而前面的部分则是已排序的部分。已排序的部分，以一种特殊的这种红色标识出来，而未排序的部分呢是这种灰色

第二点在扫描后面的这个未排序的数组的过程中，每次扫描都有一个浅蓝色的扫描的过程。同时在这个扫描的过程中。每一次当前找到的这个最小值，都用一种深蓝色表示出来

核心代码：

```java
    private void run(){

        setData(0, -1, -1);

        for( int i = 0 ; i < data.N() ; i ++ ){
            // 寻找[i, n)区间里的最小值的索引
            int minIndex = i;
            setData(i, -1, minIndex);

            for( int j = i + 1 ; j < data.N() ; j ++ ){
                setData(i, j, minIndex);

                if( data.get(j) < data.get(minIndex) ){
                    minIndex = j;
                    setData(i, j, minIndex);
                }
            }

            data.swap(i , minIndex);
            setData(i+1, -1, -1);
        }

        setData(data.N(),-1,-1);
    }

    // 一旦这个赋值完成之后，相应的也要发生一次绘制
    // 即一旦关注的那个变量发生改变，就进行一次渲染
    private void setData(int orderedIndex, int currentCompareIndex, int currentMinIndex){
        data.orderedIndex = orderedIndex;
        data.currentCompareIndex = currentCompareIndex;
        data.currentMinIndex = currentMinIndex;

        frame.render(data);
        AlgoVisHelper.pause(DELAY);
    }
```

选择排序是交换最少的排序。每一次都从后面被排序的部分选出一个最小的元素。和前面的元素进行交换。那么如果有n 个数据的话，选择排序只需要交换n 次。
即使是对于O(nlogn) 这样级别的排序算法，比如说归并排序、快速排序或者是堆排序，它所需要的交换次数。都不会像选择排序这样稳定在O(n)这个级别。

那么交换最少意味着什么？如果交换这个操作是非常耗时的话，选择排序就成为了最优的选择。

### Merge Sort

#### TopDown

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/merge_sort.gif" alt="merge_sort" style="zoom:50%;" />

```java
    public void run(){

        setData(-1, -1, -1);

        mergeSort(0, data.N()-1);

        setData(0, data.N()-1, data.N()-1);
    }

    private void mergeSort(int l, int r){

        if( l >= r )
            return;

        setData(l, r, -1);

        int mid = (l+r)/2;
        mergeSort(l, mid);
        mergeSort(mid+1, r);
        merge(l, mid, r);
    }
```

#### DownTop

自底向上的归并排序和自顶向下的归并排序的一个主要区别，TopDown可以保证每次划分都是平分的。
但是DownTop是没有这样保证的。尽管如此，这不意味着DownTop的性能比较差。虽然划分的不够平均，但是层数的差距最多只会差一层。而实际上自底向上的规定排序不需要使用递归算法。所以还减少了递归的开销。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/merge_sort_downtop.gif" alt="merge_sort_downtop" style="zoom:50%;" />

```java
    public void run(){

        setData(-1, -1, -1);

        for (int sz = 1; sz < data.N(); sz *= 2)
            for (int i = 0; i < data.N() - sz; i += sz+sz)
                // 对 arr[i...i+sz-1] 和 arr[i+sz...i+2*sz-1] 进行归并
                merge(i, i+sz-1, Math.min(i+sz+sz-1,data.N()-1));

        this.setData(0, data.N()-1, data.N()-1);
    }
```

### QuickSort

**Partition**:

![image-20201213211026771](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201213211026771.png)

`i`遍历每一个元素，橙色的部分其实就是数组arr从i+1到 j 的，这一部分的元素都小于v 的，紫色的部分就是arr从j+1到 i-1 的部分。这一部分的元素都大于v。那么对于每一个索引i 位置的元素，分情况讨论，如果对于 e 来说，它是大于v 的。那么这是非常简单的一种情况，直接让这个元素**融入**紫色的部分。那么所谓的融入紫色的部分，其实就是直接`i++`。但是如果下一个待查找的元素e 如果是小于v 的话，那么会稍微复杂一些，将e 这个元素和紫色部分的第一个元素交换位置。而小于v 的部分扩展了一个蓝色的e。那么相应的这个分隔线的位置也发生了改变。所以 j 要相应的++。最终当遍历完了所有的元素之后。就要把这个红色的标定点放入到合适的位置。只需要将v 这个元素和橙色部分的最后一个元素交换一下位置

`j`所指向的位置是标定点所在的位置

**Pivot**: 每次都会将一个元素放到一个位置。这个位置前面的元素都小于它，后面的元素都大于它，这意味着什么？意味着这个元素就已经在了在排好序之后，他本来应该处在的位置。当排好序一个数组之后，随便抽出一个元素。那么这个元素前面的所有的元素一定小于这个元素，后面的所有元素一定大于这个元素。所以每一个曾经被当过这个标定点，也就是的这样的元素一旦放在了合适的位置，这个位置就不用动了。

![quick_sort](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/quick_sort.gif)

```java
    private int partition(int l, int r){

        int v = data.get(l);
        setData(l, r, -1, l, -1);

        int j = l; // arr[l+1...j] < v ; arr[j+1...i) > v
        for( int i = l + 1 ; i <= r ; i ++ ){
            setData(l, r, -1, l, i);
            if( data.get(i) < v ){
                j ++;
                data.swap(j, i);
                setData(l, r, -1, l, i);
            }
        }

        data.swap(l, j);
        setData(l, r, j, -1, -1);

        return j;
    }
```

#### 数组元素几乎有序的情况

使partition失效，造成pivot处于两端位置，需要通过**Random Pivot**解决

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/quick_sort_random_pivot.gif" alt="quick_sort_random_pivot" style="zoom:50%;" />

```java
        int p = (int)(Math.random()*(r-l+1)) + l;
        setData(l, r, -1, p, -1);
```

#### 数组所有的元素都一致或者几乎一致的情况

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/quick_sort_two_ways-1607867790148.gif" alt="quick_sort_two_ways" style="zoom: 50%;" />

**双路快速排序**

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201213215125965.png" alt="image-20201213215125965" style="zoom:67%;" />

那么对于i 这个索引，从前向后扫描一个元素，而 j 这个索引是从后向前扫描一个元素。其停止条件都是包含等于的。所以对于i 这个索引`e>=v`的时候就停住。对于 j 这个索引`e>=v`的时候就停住。这个数组元素都是相等的元素的话，i 看一个元素，j 也看一个元素就停在了这里。此时这两个元素就发生了一次交换。那么其实这次交换完以后，由于这两个e 他们都等于v 所以其实整个数组的数据没有发生变化。但是 i 索引向后移动，j 索引也向前移动。那么通过这样的机制，i 索引就可以不断的向后移动。而 j 索引可以不断的。向前移动，使得最终橙色部分和紫色部分，其分割线的位置处在整个数组的中央的位置。让整个数组尽量平均的分成两部分，来避免单路的快速排序算法中。出现的面对相同的元素而退化成了O(n^2^) 的问题。

**三路快速排序**

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/quick_sort_three_ways.gif" alt="quick_sort_three_ways" style="zoom:50%;" />

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201213215749360.png" alt="image-20201213215749360" style="zoom:67%;" />

```java
    private void quickSort3Ways(int l, int r){

        if( l > r )
            return;

        if( l == r ) {
            setData(l, r, l, -1, -1, -1);
            return;
        }

        setData(l, r, -1, -1, -1, -1);

        // 随机在arr[l...r]的范围中, 选择一个数值作为标定点pivot
        int p = (int)(Math.random()*(r-l+1)) + l;
        setData(l, r, -1, p, -1, -1);

        data.swap(l, p);
        int v = data.get(l);
        setData(l, r, -1, l, -1, -1);

        int lt = l;     // arr[l+1...lt] < v
        int gt = r + 1; // arr[gt...r] > v
        int i = l+1;    // arr[lt+1...i) == v
        setData(l, r, -1, l, lt, gt);

        while( i < gt ){
            if( data.get(i) < v ){
                data.swap( i, lt+1);
                i ++;
                lt ++;
            }
            else if( data.get(i) > v ){
                data.swap( i, gt-1);
                gt --;
            }
            else // arr[i] == v
                i ++;

            setData(l, r, -1, l, i, gt);
        }

        data.swap( l, lt );
        setData(l, r, lt, -1, -1, -1);

        // skip equals part
        quickSort3Ways(l, lt-1 );
        quickSort3Ways(gt, r);
    }
```

### HeapSort

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/heap_sort.gif" alt="heap_sort" style="zoom:50%;" />

```java
    public void run(){

        setData(data.N());

        // 建堆
        for( int i = (data.N()-1-1)/2 ; i >= 0 ; i -- ){
            shiftDown(data.N(), i);
        }

        // 堆排序
        for( int i = data.N()-1; i > 0 ; i-- ){
            data.swap(0, i);
            shiftDown(i, 0);
            setData(i);
        }

        setData(0);
    }

    /**
    其中n是来标识[0,n)是一个最大堆。由于整个最大堆的长度是在不断变化的，
    所以n来指示最大堆是在哪里结束的。
    */
    private void shiftDown(int n, int k){

        while( 2*k+1 < n ){
            int j = 2*k+1;
            if( j+1 < n && data.get(j+1) > data.get(j) )
                j += 1;

            if( data.get(k) >= data.get(j) )
                break;

            data.swap(k, j);
            setData(data.heapIndex);

            k = j;
        }
    }
```

## 迷宫求解问题

### DFS

回溯算法本身通常也都是使用递归，这种方式来实现的

所谓的回溯算法 ，从某种程度来源于其实就是相对比较高级的穷举的方法，所谓的高级，是因为对于某些问题，可能很难使用循环的方式来穷举所有可能，所以使用这种回溯的方式来穷举这些可能。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/DFS_recursion.gif" alt="DFS_recursion" style="zoom:50%;" />

```java
    public void run(){

        setData(-1, -1, false);

        if(!go(data.getEntranceX(), data.getEntranceY()))
            System.out.println("The maze has NO solution!");

        setData(-1, -1, false);
    }

    // 从(x,y)的位置开始求解迷宫，如果求解成功，返回true；否则返回false
    private boolean go(int x, int y){

        if(!data.inArea(x,y))
            throw new IllegalArgumentException("x,y are out of index in go function!");

        data.visited[x][y] = true;
        setData(x, y, true);

        if(x == data.getExitX() && y == data.getExitY())
            return true;

        for(int i = 0 ; i < 4 ; i ++){
            int newX = x + d[i][0];
            int newY = y + d[i][1];
            if(data.inArea(newX, newY) &&
                    data.getMaze(newX,newY) == MazeData.ROAD &&
                    !data.visited[newX][newY])
                if(go(newX, newY))
                    return true;
        }

        // 回溯
        setData(x, y, false);

        return false;
    }
```



### Non Recursive DFS

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/DFS_non_recursion.gif" alt="DFS_non_recursion" style="zoom:50%;" />

```java
    private void run(){

        setData(-1, -1, false);

        Stack<Position> stack = new Stack<Position>();
        Position entrance = new Position(data.getEntranceX(), data.getEntranceY());
        stack.push(entrance);
        data.visited[entrance.getX()][entrance.getY()] = true;

        boolean isSolved = false;

        while(!stack.empty()){
            Position curPos = stack.pop();
            setData(curPos.getX(), curPos.getY(), true);

            if(curPos.getX() == data.getExitX() && curPos.getY() == data.getExitY()){
                isSolved = true;
                // 当找到出口，向前找到路径
                findPath(curPos);
                break;
            }

            for(int i = 0 ; i < 4  ; i ++){
                int newX = curPos.getX() + d[i][0];
                int newY = curPos.getY() + d[i][1];

                if(data.inArea(newX, newY)
                        && !data.visited[newX][newY]
                        && data.getMaze(newX, newY) == MazeData.ROAD){
                    stack.push(new Position(newX, newY, curPos));
                    data.visited[newX][newY] = true;
                }
            }

        }

        if(!isSolved)
            System.out.println("The maze has no Solution!");

        setData(-1, -1, false);
    }

    private void findPath(Position des){

        Position cur = des;
        while(cur != null){
            data.result[cur.getX()][cur.getY()] = true;
            cur = cur.getPrev();
        }
    }
```



### BFS

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/BFS.gif" alt="BFS" style="zoom:50%;" />

```java
    private void run(){

        setData(-1, -1, false);

        LinkedList<Position> queue = new LinkedList<Position>();
        Position entrance = new Position(data.getEntranceX(), data.getEntranceY());
        queue.addLast(entrance);
        data.visited[entrance.getX()][entrance.getY()] = true;

        boolean isSolved = false;

        while(queue.size() != 0){
            Position curPos = queue.pop();
            setData(curPos.getX(), curPos.getY(), true);

            if(curPos.getX() == data.getExitX() && curPos.getY() == data.getExitY()){
                isSolved = true;
                findPath(curPos);
                break;
            }

            for(int i = 0 ; i < 4  ; i ++){
                int newX = curPos.getX() + d[i][0];
                int newY = curPos.getY() + d[i][1];

                if(data.inArea(newX, newY)
                        && !data.visited[newX][newY]
                        && data.getMaze(newX, newY) == MazeData.ROAD){
                    queue.addLast(new Position(newX, newY, curPos));
                    data.visited[newX][newY] = true;
                }
            }

        }

        if(!isSolved)
            System.out.println("The maze has no Solution!");

        setData(-1, -1, false);
    }
```



### 联系

深度优先遍历和广度优先遍历的关系

- **DFS**

```
stack.add(入口)
while( !stack.empty())
curPos=stack.remove()
if(curPos==出口) break
对和curPos相邻的每一个可能的方向
if(newPos可达)
stack.add(newPos)
```

- **BFS**

```
queue.add(入口)
while( !queue.empty())
curPos=queue.remove()
if(curPos==出口) break
对和curPos相邻的每一个可能的方向
if(newPos可达)
queue.add(newPos)
```

## 迷宫生成问题

迷宫绘制：

1. 只有一个入口，一个出口
2. 只有一个解
3. 路径是连续的
4. 绘制在一个方形画布上
5. 墙和路径都占一个单元格

### what

这个迷宫它的本质其实是一棵树。这个迷宫是从入口开始，一直要到一个出口。这个过程中可能会有一些岔路。

而树恰恰是满足这样的一个特点的。因为对于任意一棵树来说，从一个节点到另外一个节点，只有一个路径。且经过这些路径的过程中，也会遇到相应的每一个节点的其他的子树这样的分支。比如这里的树。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217202025033.png" alt="image-20201217202025033" style="zoom:50%;" />

那么如果把它看作迷宫的话，可以指定这个节点是入口，相应的这个节点是出口。这样的一个迷宫，它的正解就是这样的一个路径。在走这个路径的过程中，比如说走到这个节点，相应的就会有很多的岔路。入口和出口不必须是叶子节点，其实树中任意的节点都有可能。

### how

要生成一个随机的迷宫其实是要生成一棵随机的树，一棵随机的树。这样的一个问题本质都是基于图这样的一个数据结构的。也就是说需要在图中寻找一棵树。

事实上图的遍历结果就是一棵生成树。从图中的一点开始出发，然后不管是使用DFS也好，还是使用BFS也好。当遍历完这个图中的所有的节点之后就产生了一棵生成树。这是因为在这个过程中，每一个节点都只访问了一次，就将这些节点全都连接了起来。而且这个过程不会产生环，所以肯定就产生一棵生成树。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/random_maze-1608208008579.gif" alt="random_maze" style="zoom:50%;" />

蓝色是墙，橙色是路径。

由于墙它所占的这个单元格和路径所占的单元格是一致的。所以路径和墙之间总是间隔着一个单位。而这些路径就好像是把它们之间本来间隔的那个墙给打破了之后产生出来的。

可以把橙色的部分，包括橙色和橙色格子之间连接的这个橙色的线。整体看作是一张图，其中这些橙色的格子是图中的节点，而格子和格子之间这个橙色的线条是图的边。要做的就是遍历图来生成迷宫。

---

图四周都是墙，如果有x 行路径，相应的一定有x+1 行墙。即迷宫的行数和列数都是奇数。

当横纵坐标都是奇数的时候，那么在初始化的时候，就应该将它设置成是路。也就是橙色的格子。而如果横纵坐标中有一个是偶数，那么在初始化的时候，都应该将它设置成墙。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217203600470.png" alt="image-20201217203600470" style="zoom:50%;" />

### 深度及广度优先遍历图

#### BFS

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/bfs_maze.gif" alt="bfs_maze" style="zoom:50%;" />

#### DFS

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/dfs_maze.gif" alt="dfs_maze" style="zoom:50%;" />

### Problem

由于使用的随机队列，每次随机的拿出一个元素作为当前考虑的对象。之后遍历四周的位置再将它们压入随机队列。其实这个过程将会非常像随机化的BFS。也就是每次遍历一个位置，将这个位置相邻的位置都放入这个队列中。然后在这些相邻的位置中随机抽一个再进行这种遍历。和BFS的区别只是在于BFS遍历的顺序是固定的。而现在的遍历的顺序是随机的。

如果随机的抽出的这个元素恰好就是最后一个元素，即恰好就是上一次循环中遍历的这四个元素中，最后压入的这个元素的话，那么在那一刻其实就执行了一个小的深度遍历。

DFS和BFS形成的这个图案的pattern 的不同

可以非常直观的看到迷宫的解，就是从这个正方形的左上角向右走再向下走，最终就来到了出口，之后，所有的岔路都是在向右这个方向走的过程中向下进行的这种平行的岔路。整个广度优先遍历的过程中，没有生成任何的这种弯曲的路径。现在的算法其实就是一个随机的BFS。所以最终得到的结果就是一个整体上是向右和向下的这样的一条路径。

### Solution

比较一下DFS的结果，当使用DFS时，最终的结果虽然也是向下向右，但是会形成S 状的岔路。虽然这个岔路只有一条，但是是这样弯曲的S 状。相比于BFS生成的这些所有的岔路，它都是一条直直的通下底的直线。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201217210846375.png" alt="image-20201217210846375" style="zoom:50%;" />

BFS：生成更多的岔路。但是这些岔路看起来都很规则。
DFS：虽然整体上只有一条岔路，但这个岔路的形状足够奇怪。

如果能将这二者结合起来，可能生成的迷宫将更加的随机。那么这就启发我们，如果在随机队列中添加可以进行DFS的操作是不是能让迷宫生成的结果看起来更加随机。

这里设计另外一个随机队列，依然是有入队和出队两个操作。

入队：随机的放入这个链表的头或者尾，就是入队首或者入队尾。
出队：随机的从队首或者队尾挑选元素。

相当于是结合队列和栈这两种数据结构，那么如果从队首入队，队尾出队，或者从队尾入队，队首出队，其实是一个队列
而如果从对首入队，对首出队或者队尾入队，队尾出队的话，就是一个栈。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/more_random_maze.gif" alt="more_random_maze" style="zoom:50%;" />

## 扫雷

### Knuth洗牌算法

设计一个**公平**的洗牌算法。

公平是指，**对于生成的排列，每一个元素都能等概率地出现在每一个位置。**或者反过来，**每一个位置都能等概率地放置每个元素。**

这个定义和**洗牌结果，可以等概率地给出，一副牌如果有 n 个元素，最终排列的可能性一共有 n! 个。公平的洗牌算法，应该能**等概率地给出这 n! 个结果中的任意一个。**这 n! 个排列中的任意一个，是等价的。**

```
for(int i = n - 1 ; i >= 0 ; i--)
	swap(arr[i], arr[rand()%(i+1)])
```

这个简单的算法，为什么能做到保证：**对于生成的排列，每一个元素都能等概率的出现在每一个位置**了。

模拟一下算法的执行过程，同时，对于每一步，计算一下概率值。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201218213129118.png" alt="image-20201218213129118" style="zoom:50%;" />

那么，根据这个算法，首先会在这五个元素中选一个元素，和最后一个元素 5 交换位置。假设随机出了 2。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201218213233342.png" alt="image-20201218213233342" style="zoom:50%;" />

下面计算 2 出现在最后一个位置的概率是多少？非常简单，因为是从 5 个元素中选的嘛，就是 1/5。实际上，根据这一步，任意一个元素出现在最后一个位置的概率，都是 1/5。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201218213303192.png" alt="image-20201218213303192" style="zoom:50%;" />

根据算法就已经不用管 2 了，而是在前面 4 个元素中，随机一个元素，放在倒数第二的位置。假设随机的是 3。3 和现在倒数第二个位置的元素 4 交换位置。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201218213401406.png" alt="image-20201218213401406" style="zoom:50%;" />

下面的计算非常重要。3 出现在这个位置的概率是多少？计算方式是这样的：

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201218213431081.png" alt="image-20201218213431081" style="zoom:50%;" />

其实很简单，因为 3 逃出了第一轮的筛选，概率是 4/5，但是 3 没有逃过这一轮的选择。在这一轮，一共有4个元素，所以 3 被选中的概率是 1/4。因此，最终，3 出现在这个倒数第二的位置，概率是 4/5 * 1/4 = 1/5。

在整个过程中，每一个元素出现在每一个位置的概率，都是 1/5 ！所以，这个算法是公平的。

当然了，上面只是举例子。这个证明可以很容易地拓展到数组元素个数为 n 的任意数组。整个算法的复杂度是 O(n) 的。

### FloodFill

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201218203301687.png" alt="image-20201218203301687" style="zoom:50%;" />

**Flood fill**, also called **seed fill**, is an [algorithm](https://en.wikipedia.org/wiki/Algorithm) that determines the area [connected](https://en.wikipedia.org/wiki/Glossary_of_graph_theory#Connectivity) to a given node in a multi-dimensional [array](https://en.wikipedia.org/wiki/Array_data_structure). It is used in the "bucket" fill tool of [paint programs](https://en.wikipedia.org/wiki/Paint_program) to fill connected, similarly-colored areas with a different color, and in games such as [Go](https://en.wikipedia.org/wiki/Go_(game)) and [Minesweeper](https://en.wikipedia.org/wiki/Minesweeper_(video_game)) for determining which pieces are cleared.

#### The algorithm

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/Recursive_Flood_Fill_8_(aka).gif" alt="img" style="zoom:67%;" />Recursive flood fill with 8 directions

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/Recursive_Flood_Fill_4_(aka).gif" alt="Recursive_Flood_Fill_4" style="zoom:67%;" />Recursive flood fill with 4 directions

The flood-fill algorithm takes three parameters: a start node, a target color, and a replacement color. The algorithm looks for all nodes in the array that are connected to the start node by a path of the target color and changes them to the replacement color. There are many ways in which the flood-fill algorithm can be structured, but they all make use of a [queue](https://en.wikipedia.org/wiki/Queue_(data_structure)) or [stack](https://en.wikipedia.org/wiki/Stack_(data_structure)) data structure, explicitly or implicitly.

Depending on whether we consider nodes touching at the corners connected or not, we have two variations: eight-way and four-way respectively.

#### Stack-based recursive implementation (four-way)

One implicitly stack-based ([recursive](https://en.wikipedia.org/wiki/Recursion)) flood-fill implementation (for a two-dimensional array) goes as follows:

```
Flood-fill (node, target-color, replacement-color):
 1. If target-color is equal to replacement-color, return.
 2. ElseIf the color of node is not equal to target-color, return.
 3. Else Set the color of node to replacement-color.
 4. Perform Flood-fill (one step to the south of node, target-color, replacement-color).
    Perform Flood-fill (one step to the north of node, target-color, replacement-color).
    Perform Flood-fill (one step to the west of node, target-color, replacement-color).
    Perform Flood-fill (one step to the east of node, target-color, replacement-color).
 5. Return.
```

Though easy to understand, the implementation of the algorithm used above is impractical in languages and environments where stack space is severely constrained

#### Alternative implementations

An explicitly queue-based implementation (sometimes called "Forest Fire algorithm"[[1\]](https://en.wikipedia.org/wiki/Flood_fill#cite_note-1)) is shown in pseudo-code below. It is similar to the simple recursive solution, except that instead of making recursive calls, it pushes the nodes onto a [queue](https://en.wikipedia.org/wiki/Queue_(abstract_data_type)) for consumption:

```
Flood-fill (node, target-color, replacement-color):
  1. If target-color is equal to replacement-color, return.
  2. If color of node is not equal to target-color, return.
  3. Set the color of node to replacement-color.
  4. Set Q to the empty queue.
  5. Add node to the end of Q.
  6. While Q is not empty:
  7.     Set n equal to the first element of Q.
  8.     Remove first element from Q.
  9.     If the color of the node to the west of n is target-color,
             set the color of that node to replacement-color and add that node to the end of Q.
 10.     If the color of the node to the east of n is target-color,
             set the color of that node to replacement-color and add that node to the end of Q.
 11.     If the color of the node to the north of n is target-color,
             set the color of that node to replacement-color and add that node to the end of Q.
 12.     If the color of the node to the south of n is target-color,
             set the color of that node to replacement-color and add that node to the end of Q.
 13. Continue looping until Q is exhausted.
 14. Return.
```

Practical implementations intended for filling rectangular areas can use a loop for the west and east directions as an optimization to avoid the overhead of stack or queue management:

```
Flood-fill (node, target-color, replacement-color):
 1. If target-color is equal to replacement-color, return.
 2. If color of node is not equal to target-color, return.
 3. Set Q to the empty queue.
 4. Add node to Q.
 5. For each element N of Q:
 6.     Set w and e equal to N.
 7.     Move w to the west until the color of the node to the west of w no longer matches target-color.
 8.     Move e to the east until the color of the node to the east of e no longer matches target-color.
 9.     For each node n between w and e:
10.         Set the color of n to replacement-color.
11.         If the color of the node to the north of n is target-color, add that node to Q.
12.         If the color of the node to the south of n is target-color, add that node to Q.
13. Continue looping until Q is exhausted.
14. Return.
```

Adapting the algorithm to use an additional array to store the shape of the region allows generalization to cover "fuzzy" flood filling, where an element can differ by up to a specified threshold from the source symbol. Using this additional array as an [alpha channel](https://en.wikipedia.org/wiki/Alpha_compositing) allows the edges of the filled region to blend somewhat smoothly with the not-filled region.

#### Scanline fill

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/212px-Smiley_fill.gif" alt="img" style="zoom:67%;" />Scanline fill

The algorithm can be sped up by filling lines. Instead of pushing each potential future pixel coordinate on the stack, it inspects the neighbour lines (previous and next) to find adjacent segments that may be filled in a future pass; the coordinates (either the start or the end) of the line segment are pushed on the stack. In most cases this scanline algorithm is at least an order of magnitude faster than the per-pixel one.

**Efficiency**: each pixel is checked once.

#### Large-scale behaviour

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/Wfm_floodfill_animation_queue.gif" alt="img" style="zoom:67%;" />Four-way flood fill using a queue for storage

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/Wfm_floodfill_animation_stack.gif" alt="img" style="zoom:67%;" />Four-way flood fill using a stack for storage

The primary technique used to control a flood fill will either be data-centric or process-centric. A data-centric approach can use either a stack or a queue to keep track of seed pixels that need to be checked. A process-centric algorithm must necessarily use a stack.

A 4-way flood-fill algorithm that uses the adjacency technique and a queue as its seed pixel store yields an expanding lozenge-shaped fill.

**Efficiency**: 4 pixels checked for each pixel filled (8 for an 8-way fill).

A 4-way flood-fill algorithm that use the adjacency technique and a stack as its seed pixel store yields a linear fill with "gaps filled later" behaviour. This approach can be particularly seen in older 8-bit computer games, such as those created with *[Graphic Adventure Creator](https://en.wikipedia.org/wiki/Graphic_Adventure_Creator)*.

**Efficiency**: 4 pixels checked for each pixel filled (8 for an 8-way fill).

#### [leetcode 733.图像渲染](https://leetcode-cn.com/problems/flood-fill/solution/tu-xiang-xuan-ran-by-leetcode-solution/)

从给定的起点开始，进行深度优先搜索。每次搜索到一个方格时，如果其与初始位置的方格颜色相同，就将该方格的颜色更新，以防止重复搜索；如果不相同，则进行回溯。

注意：因为初始位置的颜色会被修改，所以需要保存初始位置的颜色，以便于之后的更新操作。

```python
class Solution:
    def floodFill(self, image: List[List[int]], sr: int, sc: int, newColor: int) -> List[List[int]]:
        n, m = len(image), len(image[0])
        currColor = image[sr][sc]

        def dfs(x: int, y: int):
            if image[x][y] == currColor:
                image[x][y] = newColor
                for mx, my in [(x - 1, y), (x + 1, y), (x, y - 1), (x, y + 1)]:
                    if 0 <= mx < n and 0 <= my < m and image[mx][my] == currColor:
                        dfs(mx, my)

        if currColor != newColor:
            dfs(sr, sc)
        return image
```

### Flood Fill 算法模型详解

>Reference: [https://jishuin.proginn.com/p/763bfbd233b0](https://jishuin.proginn.com/p/763bfbd233b0)

Flood Fill 在图像处理领域大显身手。例如 photoshop 的魔法棒，当点击图像上的一个像素点的时候，魔法棒就把和这个像素点颜色相近的周围像素点全都选取了，这就是 Flood Fill 算法的一个典型应用。

#### 深度优先搜索

可以从开始位置`(sr, sc)`出发，依次向它的四个方向进行搜索，搜索之前要先把当前像素点的颜色改为`newColor`。

```cpp
image[r][c] = newColor;
int vx[] = {0, 0, 1, -1};
int vy[] = {1, -1, 0, 0};
for (int i = 0; i < 4; i++) {
int newr = r + vy[i];
int newc = c + vx[i];
    dfs(image, newr, newc, newColor, color);
}
```

这样一直搜索下去肯定不行， 要注意 DFS 的结束条件：

1. 当位置（行或列）超过数组的边界时，要结束递归。

```cpp
if (r >= image.size() || c >= image[0].size()) {
return;
}
```

1. 如果当前位置的颜色值和开始位置`(sr, sc)`的颜色值不同时，不能修改它的颜色值，要结束递归。

```cpp
if (image[r][c] != color) {
return;
}
```

1. 还有一点要注意的是，当要修改的目标颜色值`newColor`和开始位置的颜色值`image[sr, sc]`相同时，不需要对`image`做任何改变，原`image`就是最终的`image`.

```cpp
int color = image[sr][sc];
if (color == newColor) {
return image;
}
```

#### 广度优先搜索

BFS 就是一层一层的往外边搜索边扩张，使用队列来实现。

一开始先把开始位置`(sr, sc)`加入队列，并且修改它的颜色值：

```cpp
queue<vector<int>> q;
q.push({sr, sc});
image[sr][sc] = newColor;
```

然后队首元素出队列，同时把它上下左右四个方向颜色值为`color`的位置加入到队尾，并修改它们的颜色值为`newColor`。重复操作，直到队列为空。

```cpp
int vx[] = {0, 0, 1, -1};
int vy[] = {1, -1, 0, 0};
while (!q.empty()) {
vector<int> pos = q.front();
    q.pop();
// 标注1
// image[pos[0]][pos[1]] = newColor;
for (int i = 0; i < 4; i++) {
int r = pos[0]+vy[i];
int c = pos[1]+vx[i];
if (r >= image.size() || c >= image[0].size()) {
continue;
        }
if (image[r][c] == color) {
// 标注2
            image[r][c] = newColor;
            q.push({r, c});
        }
    }
}
```

##### 注意

这里特别要提醒的是，**一定要在添加到队尾的同时修改颜色值，不要在出队列时再修改颜色值。** 也就是说修改颜色的代码，要放在`标注2`处，不能放在`标注1`处。

##### 解释

如果等到出队列时再修改颜色值，那对于已经添加到队列中的像素点，虽然他们已经在队列中，但颜色并未及时修改。如果此时出队列的像素点正好位于某个已经在队列中的像素点旁边，那这个已经在队列中的像素点，就会被重复添加到队尾了。

轻则导致耗时增加，严重的话会出现提交超时错误。

#### 并查集

Flood Fill 的定义：漫水填充法是一种用特定的颜色填充**连通区域**，通过设置**可连通像素**的上下限以及**连通方式**来达到不同的填充效果的方法。

定义中多次提到连通，而并查集就是用来解决动态连通性问题的

![image-20201227213944688](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/image-20201227213944688.png)

假设开始位置`(sr, sc)`的颜色为`color`。可以使用并查集把颜色值为`color`并且位置相邻的像素点连通起来，形成一个连通集合。颜色值不是`color`的每个像素点，单独作为一个集合。

例如下面这种情况（圈起来的是开始位置），使用并查集就把它分成了 4 个连通集合。这时只需要把所有和开始位置`(sr, sc)`在同一个集合的像素点的颜色改为`newColor`就行了。

怎么把它们分成若干个集合呢？从`(0, 0)`位置开始依次遍历，这时就不需要同时兼顾上下左右四个方向了，只需要看看它**右边和下面**的像素点颜色是不是一样都为`color`，一样就合并。不一样就不管它，让它自己单独作为一个集合。

<img src="https://gitee.com/gaoyi-ai/image-bed/raw/master/images/unionset_floodfill.gif" alt="unionset_floodfill" style="zoom:67%;" />

提示，这里每个像素点的位置是二维坐标`(row, col)`，为了方便，需要**将它们的位置映射为一维形式**:`row * colNum + col`。`row`表示行坐标，`col`表示列坐标，`colNum`表示数组的列数。

```cpp
for (int i = 0; i < rowNum; i++) {
for (int j = 0; j < colNum; j++) {
if (image[i][j] != color) {
continue;
        }
int right = j+1;
int down = i+1;
if (right < colNum && image[i][right] == color) {
            u.unio(i*colNum+j, i*colNum+right);
        }
if (down < rowNum && image[down][j] == color) {
            u.unio(i*colNum+j, (down)*colNum+j);
        }
    }
}
```

那么接下来只需要把和开始位置`(sr, sc)`在同一个连通集合的像素点颜色值置为`newColor`就行了。

```cpp
for (int i = 0; i < rowNum; i++) {
for (int j = 0; j < colNum; j++) {
if (u.connected(i*colNum+j, sr*colNum+sc)) {
            image[i][j] = newColor;
        }
    }
}
```

#### 总结

漫水填充算法题型有着这样的特征：空间都是按区域划分的，并且每个区域中的元素都是相邻的。

为了扩大它的解题范围，可以再进一步抽象，把一个个区域抽象为一个个集合，集合中的元素都存在着某种逻辑上的连通性。最典型的就是**547. 朋友圈**[2]。

Flood Fill 这类题还有很多，例如：**1020. 飞地的数量**[3]、**1254. 统计封闭岛屿的数目**[4]、**547. 朋友圈**[5] . . .。如果使用DFS或BFS的话，解决它们的步骤无非就是遍历、标记 加 计数。如果抽象为集合的话，就可以使用并查集对它们进行集合划分，最后只需要对目标集合中的元素进行操作就可以了。

#### CODE

#### 深度优先搜素

```cpp
class Solution {
public:
vector<vector<int>> floodFill(vector<vector<int>>& image, int sr, int sc, int newColor) {
int color = image[sr][sc];
if (color == newColor) {
return image;
        }
        dfs(image, sr, sc, newColor, color);
return image;
    }

void dfs(vector<vector<int>>& image, int r, int c, int newColor, int color) {
if (r >= image.size() || c >= image[0].size()) {
return;
        }
if (image[r][c] != color) {
return;
        }
        image[r][c] = newColor;
int vx[] = {0, 0, 1, -1};
int vy[] = {1, -1, 0, 0};
for (int i = 0; i < 4; i++) {
int newr = r + vy[i];
int newc = c + vx[i];
            dfs(image, newr, newc, newColor, color);
        }
    }

};
```

#### 广度优先搜素

```cpp
class Solution {
public:
vector<vector<int>> floodFill(vector<vector<int>>& image, int sr, int sc, int newColor) {
int color = image[sr][sc];
if (color == newColor) {
return image;
        }
queue<vector<int>> q;
        q.push({sr, sc});
        image[sr][sc] = newColor;
int vx[] = {0, 0, 1, -1};
int vy[] = {1, -1, 0, 0};
while (!q.empty()) {
vector<int> pos = q.front();
            q.pop();
for (int i = 0; i < 4; i++) {
int r = pos[0]+vy[i];
int c = pos[1]+vx[i];
if (r >= image.size() || c >= image[0].size()) {
continue;
                }
if (image[r][c] == color) {
                    image[r][c] = newColor;
                    q.push({r, c});
                }
            }
        }
return image;
    }
};
```

#### 并查集

```cpp
class UnionFind {
private:
int* parent;

public:
    UnionFind(){}
    UnionFind(int n) {
        parent = new int[n];
for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
int find(int x) {
while (x != parent[x]) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
return x;
    }
void unio(int x, int y) {
        x = find(x);
        y = find(y);
if (x == y) {
return;
        }
        parent[y] = x;
    }

bool connected(int x, int y) {
return find(x) == find(y);
    }
};

class Solution {
public:
vector<vector<int>> floodFill(vector<vector<int>>& image, int sr, int sc, int newColor) {
int color = image[sr][sc];
if (color == newColor) {
return image;
        }
int rowNum = image.size();
int colNum = image[0].size();
UnionFind u(rowNum * colNum);
for (int i = 0; i < rowNum; i++) {
for (int j = 0; j < colNum; j++) {
if (image[i][j] != color) {
continue;
                }
int right = j+1;
int down = i+1;
if (right < colNum && image[i][right] == color) {
                    u.unio(i*colNum+j, i*colNum+right);
                }
if (down < rowNum && image[down][j] == color) {
                    u.unio(i*colNum+j, (down)*colNum+j);
                }
            }
        }
for (int i = 0; i < rowNum; i++) {
for (int j = 0; j < colNum; j++) {
if (u.connected(i*colNum+j, sr*colNum+sc)) {
                    image[i][j] = newColor;
                }
            }
        }
return image;
    }
};
```

#### 参考资料

[1] leetcode 733 图像渲染: *https://leetcode-cn.com/problems/flood-fill/*

[2] 547. 朋友圈: *https://leetcode-cn.com/problems/friend-circles/*

[3] 1020. 飞地的数量: *https://leetcode-cn.com/problems/number-of-enclaves/*

[4] 1254. 统计封闭岛屿的数目: *https://leetcode-cn.com/problems/number-of-closed-islands/*

[5] 547. 朋友圈: *https://leetcode-cn.com/problems/friend-circles/*