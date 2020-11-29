@[toc]
# 题目介绍
The game works as follows: The player starts in the center of a collapsing building, which consists of a quadratic grid of (2n + 1) × (2n + 1) rooms. Each room contains 0-9 units of supplies. With each move the player can go up, down, left or right and collect the supplies in the room entered. The entire row or column of rooms he just left then collapses behind him and can no longer be entered. After 2n moves no further moves can be taken and the game ends. If the player has collected enough supplies to survive till the rescue team gets him after D days, he wins. If not he looses.

游戏的工作原理如下。玩家从一个折叠的建筑物的中心开始，这个建筑物由一个二次元组成 (2n + 1)×(2n + 1)房间的网格。每个房间都包含0-9个单位的物资。随着每一个动作，玩家可以去 上、下、左或右，在进入的房间里收集物资。他刚刚离开的那一排或那一列房间就在他身后折叠，无法再进入。2N次后，不能再进。和游戏结束。如果玩家已经收集到足够的补给品，直到救援队得到他后，D 天，他赢了。如果不是，他就输了。

# 题目分析
- 已知初始值：从初始位置向上、向下、向左、向右一直走，那么走到边界能获得的最大值一定为累加值
（即 走到初始位置的上、下、左、右位置的方式只有可能是一直向上、下、左、右）
- 走到左上方位置的方式有两种：1.先左在上 2.先上再左 
（即 走到任意一个左上方位置，那么能获得的最大值 = max(其下面位置获得的最大值, 其右边位置获得的最大值) + 当前位置的值）；注意左上方的位置的右边和下面一定已经求出来了
- 之后四个方向都求出，一共能够获得的最大值 = 所有border位置中的最大值
```python
from building import *


def max_food(building: Building) -> int:
    """returns the maximum number of food that can be collected from given building"""

    length = 2 * building.size + 1

    dp = [[0 for i in range(length)] for j in range(length)]

    # initial location
    init_row = building.size
    init_col = building.size
    """maximum amount of food available in the up/down/left/right rooms of the initial location is directly cumulative"""

    # up
    for row in range(init_row - 1, -1, -1):
        dp[row][init_col] = dp[row + 1][init_col] + building.rooms[row][init_col].food
    # down
    for row in range(init_row + 1, length):
        dp[row][init_col] = dp[row - 1][init_col] + building.rooms[row][init_col].food
    # left
    for col in range(init_col - 1, -1, -1):
        dp[init_row][col] = dp[init_row][col + 1] + building.rooms[init_row][col].food
    # right
    for col in range(init_col + 1, length):
        dp[init_row][col] = dp[init_row][col - 1] + building.rooms[init_row][col].food

    # up_left
    for row in range(init_row - 1, -1, -1):
        for col in range(init_col - 1, -1, -1):
            dp[row][col] = building.rooms[row][col].food + max(dp[row][col + 1], dp[row + 1][col])  # max(right,down)
    # up_right
    for row in range(init_row - 1, -1, -1):
        for col in range(init_col + 1, length):
            dp[row][col] = building.rooms[row][col].food + max(dp[row][col - 1], dp[row + 1][col])  # max(left,down)
    # down_left
    for row in range(init_row + 1, length):
        for col in range(init_col - 1, -1, -1):
            dp[row][col] = building.rooms[row][col].food + max(dp[row][col + 1], dp[row - 1][col])  # max(right,up)
    # down_right
    for row in range(init_row + 1, length):
        for col in range(init_col + 1, length):
            dp[row][col] = building.rooms[row][col].food + max(dp[row][col - 1], dp[row - 1][col])  # max(left,up)

    return max(dp[0][0], dp[0][length - 1], dp[length - 1][0], dp[length - 1][length - 1])
```

# 题目变式

- 把食物分开成食物和水，所以既要满足食物要求和水的要求，即只要最大值 = min（食物最大值，水最大值） 
 
```python
 def max_supplies(building: Building) -> int:
    """returns the maximum of min(food,water) that can be collected from given building"""
    length = 2 * building.size + 1

    dp = [[[0, 0] for i in range(length)] for j in range(length)]

    init_row = building.size
    init_col = building.size

    # up
    for row in range(init_row - 1, -1, -1):
        dp[row][init_col][0] = dp[row + 1][init_col][0] + building.rooms[row][init_col].food
        dp[row][init_col][1] = dp[row + 1][init_col][1] + building.rooms[row][init_col].water
    # down
    for row in range(init_row + 1, length):
        dp[row][init_col][0] = dp[row - 1][init_col][0] + building.rooms[row][init_col].food
        dp[row][init_col][1] = dp[row - 1][init_col][1] + building.rooms[row][init_col].water
    # left
    for col in range(init_col - 1, -1, -1):
        dp[init_row][col][0] = dp[init_row][col + 1][0] + building.rooms[init_row][col].food
        dp[init_row][col][1] = dp[init_row][col + 1][1] + building.rooms[init_row][col].water
    # right
    for col in range(init_col + 1, length):
        dp[init_row][col][0] = dp[init_row][col - 1][0] + building.rooms[init_row][col].food
        dp[init_row][col][1] = dp[init_row][col - 1][1] + building.rooms[init_row][col].water

    # up_left
    for row in range(init_row - 1, -1, -1):
        for col in range(init_col - 1, -1, -1):
            right = [dp[row][col + 1][0] + building.rooms[row][col].food, dp[row][col + 1][1] + building.rooms[row][col].water]
            down = [dp[row + 1][col][0] + building.rooms[row][col].food, dp[row + 1][col][1] + building.rooms[row][col].water]
            dp[row][col] = right if min(right) > min(down) else down  # max(min(right),min(down))
    # up_right
    for row in range(init_row - 1, -1, -1):
        for col in range(init_col + 1, length):
            left = [dp[row][col - 1][0] + building.rooms[row][col].food, dp[row][col - 1][1] + building.rooms[row][col].water]
            down = [dp[row + 1][col][0] + building.rooms[row][col].food, dp[row + 1][col][1] + building.rooms[row][col].water]
            dp[row][col] = left if min(left) > min(down) else down  # max(min(left),min(down))
    # down_left
    for row in range(init_row + 1, length):
        for col in range(init_col - 1, -1, -1):
            right = [dp[row][col + 1][0] + building.rooms[row][col].food, dp[row][col + 1][1] + building.rooms[row][col].water]
            up = [dp[row - 1][col][0] + building.rooms[row][col].food, dp[row - 1][col][1] + building.rooms[row][col].water]
            dp[row][col] = right if min(right) > min(up) else up  # max(min(right),min(up))
    # down_right
    for row in range(init_row + 1, length):
        for col in range(init_col + 1, length):
            left = [dp[row][col - 1][0] + building.rooms[row][col].food, dp[row][col - 1][1] + building.rooms[row][col].water]
            up = [dp[row][col - 1][0] + building.rooms[row][col].food, dp[row][col - 1][1] + building.rooms[row][col].water]
            dp[row][col] = left if min(left) > min(up) else up  # max(min(left),min(down))

    return max(min(dp[0][0]), min(dp[0][length - 1]), min(dp[length - 1][0]), min(dp[length - 1][length - 1]))
```

- 以下为标准参考代码

```python
def max_supplies(building: Building) -> int:
    """returns the maximum of min(food,water) that can be collected from given building"""
    max_collect = [[None for room in column] for column in building.rooms]
    size = building.size
    max_collect[size][size] = Skyline2D([0])
    for i in range(size):
        # right
        max_collect[size][size + (i + 1)] = max_collect[size][size + i].add(building.rooms[size][size + (i + 1)])
        # left
        max_collect[size][size - (i + 1)] = max_collect[size][size - i].add(building.rooms[size][size - (i + 1)])
        # down
        max_collect[size + (i + 1)][size] = max_collect[size + i][size].add(building.rooms[size + (i + 1)][size])
        # up
        max_collect[size - (i + 1)][size] = max_collect[size - i][size].add(building.rooms[size - (i + 1)][size])
    for r in range(1, size + 1):
        for c in range(1, size + 1):
            # down right
            max_collect[size + r][size + c] = max_collect[size + (r - 1)][size + c].merge(
                max_collect[size + r][size + (c - 1)]).add(building.rooms[size + r][size + c])
            # down left
            max_collect[size + r][size - c] = max_collect[size + (r - 1)][size - c].merge(
                max_collect[size + r][size - (c - 1)]).add(building.rooms[size + r][size - c])
            # up right
            max_collect[size - r][size + c] = max_collect[size - (r - 1)][size + c].merge(
                max_collect[size - r][size + (c - 1)]).add(building.rooms[size - r][size + c])
            # up left
            max_collect[size - r][size - c] = max_collect[size - (r - 1)][size - c].merge(
                max_collect[size - r][size - (c - 1)]).add(building.rooms[size - r][size - c])
    skylines = [max_collect[0][0], max_collect[0][2 * size], max_collect[2 * size][0], max_collect[2 * size][2 * size]]
    return max([s.max_min() for s in skylines])


class Skyline2D:
    """tracks skyline-filtered set of (food,water) pairs - fast for dense skylines"""

    def __init__(self, values: List[int] = []):
        # values for water indexed by food
        # food只影响values中的个数，真实的value是由water决定的
        self.values = values

    def merge(self, other: 'Skyline2D') -> 'Skyline2D':
        """returns merger of given skyline with self"""
        merge_size = min(len(self.values), len(other.values))
        # 找到两个已知方向的格子的max
        values = [max(self.values[i], other.values[i]) for i in range(merge_size)]
        # 添上某个方向上多余的
        values.extend(self.values[merge_size:])
        values.extend(other.values[merge_size:])
        return Skyline2D(values)

    def add(self, supplies: Supplies) -> 'Skyline2D':
        """returns new skyline with all values increased by given value"""
        # 这里values[i]看作拿i个food时和能拿到的water
        values = self.values
        if supplies.water > 0:
            # 当前格子有water，那么就要把目前values中的所有可能+water
            # 目的为了记录到达此处的路径上的max value
            values = [w + supplies.water for w in values]
        # 这里顺序不能交换，每个格子为food/water
        # 所以food是用来记录路径上能获得的food以索引方式
        # max_min中利用索引找到min中的max
        if supplies.food > 0:
            # 新加入的放在前面
            # 刚刚拿到food还没有拿到water
            prepend = [0 for i in range(supplies.food)]
            prepend.extend(values)
            values = prepend
        return Skyline2D(values)

    def max_min(self) -> int:
        """returns maximum supplies"""
        # i:food len(values)就代表了food的max累加值
        # values[i]:water(累加值)
        # 这里也就是food exists时，要把已知的values放在后面
        # 因为一旦把values中累加值放到前面，而0放在后面，那么得到的数会偏小
        return max([min(i, self.values[i]) for i in range(len(self.values))])
```

# SpeedTest
```python
# speed test - use "python optimizer.py" to run
if __name__ == "__main__":
    import timeit
    test_size = 100 # set to 100 to check time for speed race
    t1 = timeit.repeat(stmt="optimizer.max_food(b)", setup="import gc, building, optimizer; b = building.random_building({0}, True); gc.collect()".format(test_size), repeat=3, number=1)
    t2 = timeit.repeat(stmt="optimizer.max_supplies(b)", setup="import gc, building, optimizer; b = building.random_building({0}, False); gc.collect()".format(test_size), repeat=3, number=1)
    # some calculation that takes ~1 sec on my machine
    tref = timeit.repeat(stmt="for i in range(1000000): a=i^2", setup="import gc; gc.collect()", repeat=3, number=19)
    print("max_food(n={0}) = {1} ({3} normalized), max_supplies(n={0}) = {2} ({4} normalized)".format(test_size, min(t1), min(t2), min(t1) / min(tref), min(t2) / min(tref)))

```

# 游戏代码
building.py
```python
from typing import List, TypeVar
import random

SupplyData = TypeVar('SupplyData', List[int], int)

class Supplies:
    """data object for storing supplies"""

    def __init__(self, supp: SupplyData):
        if isinstance(supp, list):
            self.food = supp[0]
            self.water = supp[1]
        else:
            self.food = supp
            self.water = supp

    def __str__(self):
        if self.food == self.water:
            return str(self.food)
        else:
            return "[{0},{1}]".format(self.food, self.water)

class Building:
    """data object for storing a (2n+1) by (2n+1) array of rooms"""

    def __init__(self, rooms: List[List[SupplyData]]):
        """rooms = 2D array of integers or pairs of integers"""
        if len(rooms) % 2 != 1 or len(rooms[0]) != len(rooms):
            raise ValueError("Illegal number of rooms: must be (2n+1) x (2n+1)")
        self.size = len(rooms) // 2
        self.rooms = [[Supplies(s) for s in row] for row in rooms]
        self.reset()

    def __str__(self):
        return "[[" + "],\n [".join( [ ",".join([str(room) for room in row]) for row in self.rooms ] ) + "]]"

    def reset(self) -> None:
        """player starts in the center, without supplies"""
        self.player_row = self.size
        self.player_col = self.size
        self.player_food = 0
        self.player_water = 0

    def is_valid(self, row: int, col: int) -> bool:
        """checks if (row,col) is a valid room location"""
        return 0 <= row <= 2*self.size and 0 <= col <= 2*self.size

    def is_collapsed(self, row: int, col: int) -> bool:
        """checks if the room at position (row,col) is collapsed"""
        def is_after(start: int, x: int, end: int) -> bool:
            return (start < end and x < end) or (start > end and x > end)
        return is_after(self.size, row, self.player_row) or is_after(self.size, col, self.player_col)

    def can_move(self) -> bool:
        """check if the player is able to move in any direction"""
        return not (self.player_row in [0, 2*self.size]) or not (self.player_col in [0, 2*self.size])

    def move_player(self, delta_row: int, delta_col: int) -> bool:
        """move player by (delta_row, delta_col) vector if possible; returns whether successful"""
        new_row = self.player_row + delta_row
        new_col = self.player_col + delta_col
        if not self.is_valid(new_row, new_col) or self.is_collapsed(new_row, new_col):
            return False
        else:
            self.player_row = new_row
            self.player_col = new_col
            self.player_food += self.rooms[self.player_row][self.player_col].food
            self.player_water += self.rooms[self.player_row][self.player_col].water
            return True

def random_building(size: int = 10, equal_supplies: bool = True) -> Building:
    rooms = []
    for r in range(2*size+1):
        row = []
        for c in range(2*size+1):
            if r == size and c == size:
                row.append(0)
            elif equal_supplies:
                row.append(random.randint(0,9))
            else:
                if random.choice([True, False]):
                    row.append([random.randint(0,9), 0])
                else:
                    row.append([0, random.randint(0,9)])
        rooms.append(row)
    return Building(rooms)

```

building_io.py
```python
from typing import List
from building import Building
import pygame

# Define some colors
black    = (   0,   0,   0)
white    = ( 255, 255, 255)
dgray    = (  63,  63,  63)
lgray    = ( 127, 127, 127)
red      = ( 255,   0,   0)
green    = (   0, 255,   0)
blue     = (   0,   0, 255)
brown    = ( 205, 102,  31)

def center_label(screen: pygame.Surface, label: str, x: int, y: int) -> None:
    """places a label on the screen so that its center is at (x,y)"""
    pos_x = x - label.get_width() // 2
    pos_y = y - label.get_height() // 2
    screen.blit(label, (pos_x,pos_y))

class BuildingRenderer:
    """renderer for displaying a building on screen"""

    def __init__(self, building: Building, screen_pos: List[int], room_size: int):
        self.building = building
        self.screen_pos = screen_pos
        self.room_size = room_size

    def display_building(self, screen: pygame.Surface) -> None:
        # Set the screen background
        screen.fill(black)
        # init values
        myfont = pygame.font.SysFont("Comic Sans MS", 6 + self.room_size // 4)
        # draw each room
        dim = 2 * self.building.size + 1
        for row in range(dim):
            for col in range(dim):
                pos_x = self.screen_pos[0] + col * self.room_size
                pos_y = self.screen_pos[1] + row * self.room_size
                pygame.draw.rect(screen, white, pygame.Rect(pos_x, pos_y, self.room_size, self.room_size), 1)
                if self.building.is_collapsed(row, col):
                    pygame.draw.rect(screen, dgray, pygame.Rect(pos_x, pos_y, self.room_size, self.room_size))
                    continue
                elif row == self.building.player_row and col == self.building.player_col:
                    # draw player as @ symbol
                    label = myfont.render("@", 1, red)
                else:
                    # draw food and/or water in room
                    room = self.building.rooms[row][col]
                    if room.food == room.water == 0:
                        continue
                    elif room.food == room.water:
                        label = myfont.render(str(room.food), 1, lgray)
                    elif room.water == 0:
                        label = myfont.render(str(room.food), 1, brown)
                    elif room.food == 0:
                        label = myfont.render(str(room.water), 1, blue)
                    else:
                        label = myfont.render("{0}/{1}".format(room.food, room.water), 1, lgray)
                center_label(screen, label, pos_x + self.room_size // 2, pos_y + self.room_size // 2);

```
collapse.py
```python
import building, building_io, optimizer
import pygame, os, sys, time

# init screen
os.environ['SDL_VIDEO_CENTERED'] = '1'
pygame.init() 
screen_size = [800,600]
screen = pygame.display.set_mode(screen_size)
pygame.display.set_caption("Collapse")

# default config
size = 1
equal_supplies = True

# utility functions
def limit(lower: int, x: int, upper: int) -> int:
    return min(max(lower, x), upper)

def local_delay(t: int) -> None:
    """stops the current execution for t seconds without delaying other threads"""
    start = time.time()
    while time.time() - start < t: pass

# command line overwrite
print("syntax: python collapse.py [size] [easy|hard]")
if len(sys.argv) > 1 and sys.argv[1].isdigit():
    size = limit(1, int(sys.argv[1]), 20)
if len(sys.argv) > 2 and sys.argv[2] == "hard":
    equal_supplies = False

# drawing functions
def show_status() -> None:
    myfont = pygame.font.SysFont("Comic Sans MS", 12)
    def print_at(msg, x, y):
        label = myfont.render(msg, 1, building_io.white)
        screen.blit(label, (x,y))
    if equal_supplies:
        print_at("Supplies collected:  {0} / {1}".format(my_building.player_food, player_needs), screen_size[1] + 15, 20)
    else:
        print_at("Food collected:  {0} / {1}".format(my_building.player_food, player_needs), screen_size[1] + 15, 20)
        print_at("Water collected:  {0} / {1}".format(my_building.player_water, player_needs), screen_size[1] + 15, 40)
    print_at("r: reset", screen_size[1] + 15, 60)
    print_at("n: new", screen_size[1] + 15, 80)

def redraw(flip: bool = True) -> None:
    my_renderer.display_building(screen)
    show_status()
    if flip: pygame.display.flip()

# initialization function
def new_building() -> None:
    global my_building, my_renderer, player_needs
    my_building = building.random_building(size, equal_supplies)
    player_needs = optimizer.max_food(my_building) if equal_supplies else optimizer.max_supplies(my_building)
    # new renderer
    room_size = screen_size[1] // (2*size + 1)
    offset = (screen_size[1] - (2*size+1) * room_size) // 2
    my_renderer = building_io.BuildingRenderer(my_building, (offset,offset), room_size)
    
# initialization
new_building()
redraw(True)

# -------- Main Program Loop -----------
while True:
    event = pygame.event.wait() # get one event, wait until it happens
    if event.type == pygame.QUIT: # user clicked close
        break
    # handle player movement
    if event.type == pygame.KEYDOWN:
        if ((event.key == pygame.K_LEFT     and my_building.move_player( 0,-1)) or
            (event.key == pygame.K_RIGHT    and my_building.move_player( 0, 1)) or
            (event.key == pygame.K_UP       and my_building.move_player(-1, 0)) or
            (event.key == pygame.K_DOWN     and my_building.move_player( 1, 0))):
            # game over?
            if not my_building.can_move():
                redraw(False)
                # show win/loss message
                myfont = pygame.font.SysFont("Comic Sans MS", 16)
                supplies = min(my_building.player_food, my_building.player_water)
                victory = supplies >= player_needs
                msg = "You collected supplies for {0} out of {1} days. You {2}.".format(supplies, player_needs, "survive" if victory else "die")
                if victory:
                    label = myfont.render(msg, 1, building_io.green)
                else:
                    label = myfont.render(msg, 1, building_io.red)
                building_io.center_label(screen, label, screen_size[1]/2, screen_size[1]/2)
                pygame.display.flip()
                # progress to next level
                if victory and (size < 20 or equal_supplies):
                    local_delay(2.0)
                    if size < 20:
                        size += 1
                    else:
                        size = 1
                        equal_supplies = False
                    new_building()
                    redraw()
            else:
                redraw()
        elif event.key == pygame.K_p: # print current game to stdout
            print(my_building)
        elif event.key == pygame.K_n: # new game
            new_building()
            redraw()
        elif event.key == pygame.K_r: # reset current game
            my_building.reset()
            redraw()
     
# clean exit
pygame.quit()

```
