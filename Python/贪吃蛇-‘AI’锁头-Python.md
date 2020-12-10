---
title: 贪吃蛇
categories:
- Python
tags:
- Pygame
date: 2019/8/1 20:00:14
updated: 2020/12/10 12:00:14
---



### - **主要任务**

 - **实现游戏结束判定，包括：**
    **玩家碰到边界、玩家撞到障碍物、玩家撞到AI、AI撞到玩家、玩家撞到自己**
 - **食物设置、障碍物设置**
 - **分数设置**
 ### - **游戏特点**
 - **AI能实现锁头追踪**


 具体代码如下：

```python
import pygame
import random

# --- Globals ---
# Colors
BLACK = (0, 0, 0)
WHITE = (255, 255, 255)
RED = (255, 0, 0)
GREEN = (0, 255, 0)
PINK = (255, 200, 203)
BLUE = (0, 0, 255)
# Screen size
height = 600
width = 600
# 每个蛇身的空隙
segment_margin = 3
# 蛇身的宽高
segment_width = min(height, width) / 40 - segment_margin
segment_height = min(height, width) / 40 - segment_margin
```

```python
class Obstacle:
    """
    障碍物可能会重叠
    玩家和AI都不会在一开始就撞上障碍物
    以下为自定义地图，而不是随机生成的
    """
    def __init__(self):
        self.obstacles = []
        self.spriteslist = pygame.sprite.Group()
        for loc in [4, 20, 35]:
            for i in range(random.randrange(1, 4)):
                x = (segment_width + segment_margin) * loc - (segment_width + segment_margin) * i
                for j in range(random.randrange(1, 4)):
                    y = (segment_height + segment_margin) * loc - (segment_height + segment_margin) * j
                    obstacle_item = ObstacleItem(x, y)
                    self.obstacles.append(obstacle_item)
                    self.spriteslist.add(obstacle_item)
        for loc in [4, 35]:
            for i in range(random.randrange(1, 4)):
                x = (segment_width + segment_margin) * loc - (segment_width + segment_margin) * i
                for j in range(random.randrange(1, 4)):
                    y = (segment_height + segment_margin) * (40 - loc) - (segment_height + segment_margin) * j
                    obstacle_item = ObstacleItem(x, y)
                    self.obstacles.append(obstacle_item)
                    self.spriteslist.add(obstacle_item)
```


```python
class Food:
    def __init__(self):
        self.items = []
        self.spriteslist = pygame.sprite.Group()
        x, y = (segment_width + segment_margin) * 32, (segment_width + segment_margin) * 32
        food_item = FoodItem(x, y)
        self.items.append(food_item)
        self.spriteslist.add(food_item)
        
    def replenish(self):
        old_segment = self.items.pop()
        self.spriteslist.remove(old_segment)
        x = (segment_width + segment_margin) * random.randrange(1, 39)
        y = (segment_height + segment_margin) * random.randrange(1, 39)
        food_item = FoodItem(x, y)
        self.items.append(food_item)
        self.spriteslist.add(food_item)
```


```python
class Snake:
    # Constructor
    def __init__(self,length):
        self.segments = []
        self.spriteslist = pygame.sprite.Group()
        for i in range(length):
            x = (segment_width + segment_margin) * 30 - (segment_width + segment_margin) * i
            y = (segment_height + segment_margin) * 2
            segment = Segment(x, y)
            self.segments.append(segment)
            self.spriteslist.add(segment)
        # 设定初始速度
        self.x_change = segment_width + segment_margin
        self.y_change = 0
        
    def move(self):
        #  决定身体向哪个方向变化，x_change和y_change通过正负来表示上下左右
        x = self.segments[0].rect.x + self.x_change
        y = self.segments[0].rect.y + self.y_change
        if 0 <= x <= width - segment_width and 0 <= y <= height - segment_height:
            # 向蛇头的方向，即前进方向，添加身体，蛇尾减去身体
            segment = Segment(x, y)
            self.segments.insert(0, segment)
            self.spriteslist.add(segment)
            # Get rid of last segment of the snake
            old_segment = self.segments.pop()
            self.spriteslist.remove(old_segment)

    def grow(self):
        # 因为蛇一直在走，每次走一格
        # 所以当它吃到食物时，可以直接加上它的尾巴，因为他的尾巴已经走到尾巴前面一格了
        self.segments.append(self.segments[-1])
        self.spriteslist.add(self.segments[-1])
        
    def ai_move(self):
        x = self.segments[0].rect.x
        y = self.segments[0].rect.y
        # 当靠近左边界时
        if x - (segment_width + segment_margin) <= 0:
            # 既靠近左边界又靠近右边界，即‘左上角’
            if y - (segment_height + segment_margin) <= 0:
                (self.x_change, self.y_change) = (-self.y_change, -self.x_change)
            #‘左下角’
            elif y + (segment_height + segment_margin) >= height:
                (self.x_change, self.y_change) = (self.y_change, self.x_change)
        # 靠近右边界
        elif x + (segment_width + segment_margin) >= width:
            # ‘右上角’
            if y - (segment_height + segment_margin) <= 0:
                (self.x_change, self.y_change) = (self.y_change, self.x_change)
            # ‘右下角’
            elif y + (segment_height + segment_margin) >= height:
                (self.x_change, self.y_change) = (-self.y_change, -self.x_change)
        # 如果只是单纯的要抵达某一边界，交换x、y，这样就实现转弯
        if y - (segment_height + segment_margin) <= 0 or y + (segment_height + segment_margin) >= height or \
                x + (segment_width + segment_margin) >= width or x - (segment_width + segment_margin) <= 0:
            (self.x_change, self.y_change) = (-self.y_change, -self.x_change)
        self.move()

    # 锁头攻击
    def chase(self, snake):
        x = snake.segments[0].rect.x
        y = snake.segments[0].rect.y
        # 玩家蛇 x 坐标离AI很远
        if (x - self.segments[0].rect.x) ** 2 >= (y - self.segments[0].rect.y) ** 2: # 比较x、y 的距离差
            # 玩家在AI的右边，而AI没有向右走
            if x > self.segments[0].rect.x and self.x_change <= 0:
                self.x_change = (segment_width + segment_margin)
                self.y_change = 0
            else:
                self.x_change = -(segment_width + segment_margin)
                self.y_change = 0
        # 玩家蛇 y 坐标离AI很远
        else:
            # 玩家在AI的下边，而AI没有向下走
            if y > self.segments[0].rect.y and self.y_change >= 0:
                self.y_change = (segment_height + segment_margin)
                self.x_change = 0
            else:
                self.y_change = -(segment_height + segment_margin)
                self.x_change = 0
        self.move()
```


```python
class Segment(pygame.sprite.Sprite):
    # Constructor
    def __init__(self, x, y):
        # Call the parent's constructor
        super().__init__()
        # Set height, width
        self.image = pygame.Surface([segment_width, segment_height])
        self.image.fill(WHITE)
        # Set top-left corner of the bounding rectangle to be the passed-in location.
        self.rect = self.image.get_rect()
        self.rect.x = x
        self.rect.y = y

class FoodItem(Segment):
    def __init__(self, x, y):
        super().__init__(x, y)
        self.type = random.randrange(0, 3)
        if self.type == 0:
            self.points = 1
            self.image.fill(GREEN)
        elif self.type == 1:
            self.points = 2
            self.image.fill(PINK)
        elif self.type == 2:
            self.points = 3
            self.image.fill(BLUE)

class ObstacleItem(Segment):
    def __init__(self, x, y):
        super().__init__(x, y)
        self.image.fill(RED)
```


```python
# Call this function so the Pygame library can initialize itself
pygame.init()
# Create a 600x700 sized screen
screen = pygame.display.set_mode([width, height + 100])
# Set the title of the window
pygame.display.set_caption('Snake Game')
# Create an initial snake
my_snake = Snake(3)
food = Food()
obstacle = Obstacle()
ai_snake = Snake(9)
# 初始时 AI远离玩家
for segments in ai_snake.segments:
    segments.rect.y = (segment_height + segment_margin) * 35
    
clock = pygame.time.Clock()
done = False
game_ended = False

"""本地保存每次游戏的分数，找到最高分"""
score = 0
top_score = 0
with open('history.txt', 'r') as f:
    scores = f.readlines()
score_list = []
for score_his in scores:
    score_his = score_his.strip()
    score_list.append(int(score_his))
if score_list:
    top_score = max(score_list)
```
```python
count = 0
while not done:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            done = True
        if event.type == pygame.KEYDOWN:
            if event.key == pygame.K_LEFT:
                if my_snake.x_change != (segment_width + segment_margin):  # 玩家不能直接往回走
                    my_snake.x_change = (segment_width + segment_margin) * -1
                    my_snake.y_change = 0
            if event.key == pygame.K_RIGHT:
                if my_snake.x_change != (segment_width + segment_margin) * -1:
                    my_snake.x_change = (segment_width + segment_margin)
                    my_snake.y_change = 0
            if event.key == pygame.K_UP:
                if my_snake.y_change != (segment_height + segment_margin):
                    my_snake.x_change = 0
                    my_snake.y_change = (segment_height + segment_margin) * -1
            if event.key == pygame.K_DOWN:
                if my_snake.y_change != (segment_height + segment_margin) * -1:
                    my_snake.x_change = 0
                    my_snake.y_change = (segment_height + segment_margin)

    """游戏结束的判定：玩家碰到边界、玩家撞到障碍物、玩家撞到AI、AI撞到玩家、玩家撞到自己"""
    if my_snake.segments[0].rect.x + segment_width + segment_margin >= width or my_snake.segments[0].rect.x <= 0 or \
            my_snake.segments[0].rect.y + segment_height + segment_margin >= height or my_snake.segments[
        0].rect.y <= 0 or \
            pygame.sprite.spritecollide(my_snake.segments[0], obstacle.spriteslist, False) or \
            pygame.sprite.spritecollide(my_snake.segments[0], my_snake.segments[1:], False) or \
            pygame.sprite.spritecollide(ai_snake.segments[0], my_snake.spriteslist, False):
        # 游戏结束
        game_ended = True
        screen.fill(BLACK)
        font = pygame.font.SysFont('comicsansms', 48)
        text = font.render('Top Score : %d' % top_score, True, WHITE)
        textrect = text.get_rect(center=(300, 470))
        screen.blit(text, textrect)
        text = font.render('Game Over.', True, RED)
        textrect = text.get_rect(center=(300, 530))
        screen.blit(text, textrect)
        text_score = font.render('Score : ' + str(score), True, WHITE)
        textrect = text_score.get_rect(center=(300, 580))
        screen.blit(text_score, textrect)

    if not game_ended:
        # move snake one step
        my_snake.move()
        ai_snake.ai_move()
        # 循环16次，锁头
        if count % 16 == 0:
            ai_snake.chase(my_snake)
        count +=1
        # 游戏界面
        screen.fill(BLACK)
        # 实时更新当前分数
        font = pygame.font.SysFont('comicsansms', 30)
        text = font.render('Score = ' + str(score), True, WHITE)
        textrect = text.get_rect(center=(100, 600))
        screen.blit(text, textrect)
        # 显示food的分数
        font = pygame.font.SysFont('comicsansms', 20)
        text = font.render('■ -- 1', True, GREEN)
        textrect = text.get_rect(center=(250, 600))
        screen.blit(text, textrect)
        text = font.render('■ -- 2', True, PINK)
        textrect = text.get_rect(center=(250, 630))
        screen.blit(text, textrect)
        text = font.render('■ -- 3', True, BLUE)
        textrect = text.get_rect(center=(250, 660))
        screen.blit(text, textrect)
        
        # 更新屏幕
        obstacle.spriteslist.draw(screen)
        my_snake.spriteslist.draw(screen)
        ai_snake.spriteslist.draw(screen)
        food.spriteslist.draw(screen)
        
        if pygame.sprite.spritecollide(my_snake.segments[0], food.spriteslist, False):
            my_snake.grow()
            score += food.items[0].points
            food.replenish()
            while pygame.sprite.spritecollide(food.items[0], obstacle.spriteslist, False) or \
                    pygame.sprite.spritecollide(food.items[0], my_snake.spriteslist, False):
                # 确保食物不会一开始，或者随机生成是就落到障碍物和玩家蛇身上
                # 如果有，就更新
                food.replenish()
    # Flip screen
    pygame.display.flip()
    # Pause
    clock.tick(5)

pygame.quit()
```

```python
with open('history.txt', 'a+') as f:
    f.write(str(score) + '\n')
```

游戏截图如下：
**初始页面**![初始界面](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20191102203219575.jpg)

**碰到边界**![碰到边界](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20191102203419540.jpg)

**碰到AI**
![碰到AI](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20191102203532194.jpg)
**AI碰到玩家**
![AI碰到玩家](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20191102203656621.jpg)
**碰到自己**
![碰到自己](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20191102203723500.jpg)

