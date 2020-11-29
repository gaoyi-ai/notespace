@[toc]
### 实验内容
 1. 设命题变元 P1、P2、P3、…、Pn 是出现在公式 G 中的所有命题变元，指定 P1、 P2、P3、…、Pn的一组真值，则这组真值称为 G 的一个解释或指派，常记为Ｉ
 2. 真值表：公式 G 在其所有可能的解释下所取真值的表

本实验要求从键盘输入一个命题公式列出其真值表。 
### 程序设计
实现真值表难点在于：

 1. 对变元的 T(True) F(False) 的枚举，相当于**未知阶的嵌套循环，对此应用递归解决**。
    对此首先找到递归结束的标志，即把所有变元的TF都枚举出来
 2. 找到命题包含的变元很简单，只需要构造一个集合，或者使用列表
 3. 对于中缀表达式转换后缀表达式，请参考[后缀表达式](https://blog.csdn.net/qq_43658387/article/details/102691904)
### 代码实现
```python
operators = ['!','&', '|', '*', '+'] # 按照优先级从高到低的顺序

def postfix(elements):
    global operators
    stack = list()
    output = list()
    for ele in elements:
        if ele not in operators and ele not in ('(',')'): #
            output.append(ele)
        # '(' 在运算符中优先级最小，其唯一出栈条件时遇到右括号
        elif ele == '(':
            stack.append(ele)
        # 若是 ) ，则出栈直到遇到 ( ，这里注意: ) 遇到的第一个 ( 一定是匹配的一对
        elif ele == ')':
            val = stack.pop()
            while val !='(':
                output.append(val)
                if stack:
                    val = stack.pop()
                else:
                    break
        elif ele in ('&', '|', '*', '+', '!'): # 遇到运算符，比较优先级
            if len(stack) == 0:
                stack.append(ele)
                continue
            # 比较该运算符与栈顶运算符的优先级，遇到比该运算符优先级大于或等于的则将其弹出，最后将该运算符压栈
            while (ele == '!' and stack[-1] =='!') or \
                    (ele == '&' and stack[-1] in ('!', '&')) or \
                    (ele == '|' and stack[-1] in ('!', '&', '|')) or \
                    (ele == '*' and stack[-1] in ('!', '&', '|', '*')) or \
                    (ele == '+' and stack[-1] in ('!', '&', '|', '*', '+')):
                val = stack.pop()
                output.append(val)
                if not stack:
                    break
            stack.append(ele)
    while stack: # 当表达式完全处理完之后，把栈中的运算符一一出栈，FILO，转化成后缀表表达式
        output.append(stack.pop())
    return ''.join(output)

def logicalOp(tmp):
    idx = 0  # 当前命题元素的位置
    while len(tmp) > 1:  # 当最后命题仅为真值后，退出循环
        if tmp[idx] in operators:
            if tmp[idx] == '!':  # 非
                # 这里把命题变元进行转换，根据后缀表达式，一定满足idx的前1位或前2位是真值而不是运算符
                tmp[idx - 1] = 1 if int(tmp[idx - 1]) == 0 else 0
                tmp[idx:idx + 1] = []  # 子命题结果对原命题覆盖
                # 每次从头开始对命题处理
                idx = 0
                continue
            elif tmp[idx] == '&':  # 合取
                tmp[idx] = 1 if int(tmp[idx - 1]) == 1 and int(tmp[idx - 2]) == 1 else 0
                tmp[idx - 2:idx] = []
                idx = 0
                continue
            elif tmp[idx] == '|':  # 析取
                tmp[idx] = 0 if int(tmp[idx - 1]) == 0 and int(tmp[idx - 2]) == 0 else 1
                tmp[idx - 2:idx] = []
                idx = 0
                continue
            elif tmp[idx] == '*':  # 则
                tmp[idx] = 0 if int(tmp[idx - 2]) == 1 and int(tmp[idx - 1]) == 0 else 1
                tmp[idx - 2:idx] = []
                idx = 0
                continue
            elif tmp[idx] == '+':  # 当且仅当
                tmp[idx] = 1 if int(tmp[idx - 2]) == int(tmp[idx - 1]) else 0
                tmp[idx - 2:idx] = []
                idx = 0
                continue
        idx += 1
    print(tmp[0])

def e(idx):
    global expr
    if idx == len(enum): # 递归终止条件为枚举完全部的变元
        print('\t'.join(list(enum.values())),end='\t') # 打印出枚举情况
        tmp = ' '.join(expr) # tmp为对命题处理带入真值的中间量
        for ele in expr:
            if ele in enum:
                tmp = tmp.replace(ele, enum[ele])
        tmp = tmp.split(' ') # 转化成list，由于字符串内部不能修改
        logicalOp(tmp)
        return
    enum[var[idx]] = '0' # 枚举False,最后在转换为int类型，使得可使用字符串替换，把0，1代入
    e(idx+1) # 接着对下一位变元枚举
    enum[var[idx]] = '1' # 枚举True
    e(idx+1)

if __name__=='__main__':
    """
    Attention :
    定义：
    ！：非 (单命题变元)
    &：合取
    |：析取
    *：单条件(则)
    +：双条件(当且仅当)
    """
    print('please input the problem\tExample:  (p*q)&!r  p&q|r*q&!s|r')
    inp = input('>>>')
    expr = postfix(inp)  # expr为生成的后缀表达式
    var = list()  # var为命题变元
    for item in expr:
        # 找出变元且不能重复
        if item not in operators and \
                item not in var and \
                item not in ('(', ')'):
            var.append(item)
    # 对变元枚举TF字典
    enum = {}.fromkeys(var)
    # 打印表头
    print('\t'.join(var),end='\t')
    print(inp)
    # 从第一个变元枚举
    e(0)
```

    Attention :
    定义：
    ！：非 (单命题变元)
    &：合取
    |：析取
    *：单条件(则)
    +：双条件(当且仅当)
    
    please input the problem	Example:  (p*q)&!r  p&q|r*q&!s|r
    >>>p&q|r*q&!s|r
    p	q	r	s	p&q|r*q&!s|r
    0	0	0	0	1
    0	0	0	1	1
    0	0	1	0	1
    0	0	1	1	1
    0	1	0	0	1
    0	1	0	1	1
    0	1	1	0	1
    0	1	1	1	1
    1	0	0	0	1
    1	0	0	1	1
    1	0	1	0	1
    1	0	1	1	1
    1	1	0	0	1
    1	1	0	1	0
    1	1	1	0	1
    1	1	1	1	1

