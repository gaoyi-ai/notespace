![warshall算法](images/warshall%E7%AE%97%E6%B3%95/20191026215014783.png)

**Python代码实现**
```python
import numpy as np
def warshall(array):
   t = np.array(array)
   for col in range(t.shape[1]): # 每列
      for row in range(t.shape[0]): # 列的每行
         if t[row][col] == 1: # 对此列的每一行检查，如果为1
            for k in range(t.shape[1]): 
            # 这一行＝这一行 | 行标号为列数的一行
               t[row][k] = t[row][k] | t[col][k] 
   return t


source = [[0,1,0,0],[0,0,1,0],[0,0,0,1],[0,0,0,0]]
result = warshall(source)
"""
R:
[[0 1 0 0]
 [0 0 1 0]
 [0 0 0 1]
 [0 0 0 0]]
t(R):
[[0 1 1 1]
 [0 0 1 1]
 [0 0 0 1]
 [0 0 0 0]
"""
```

