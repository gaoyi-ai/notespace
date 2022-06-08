# Behavior Trees for Path Planning (Autonomous Driving)

# Path planning in self-driving cars

Path planning and decision making for autonomous vehicles in urban environments enable self-driving cars to find the safest, most convenient, and most economically beneficial routes from point A to point B. Finding routes is complicated by all of the static and maneuverable obstacles that a vehicle must identify and bypass. Today, the major path planning approaches include the predictive control model, feasible model, and behavior-based model. Let’s first get familiar with some terms to understand how these approaches work.

- A path is a continuous sequence of configurations beginning and ending with boundary configurations. These configurations are also referred to as initial and terminating.
- Path planning involves finding a geometric path from an initial configuration to a given configuration so that each configuration and state on the path is feasible (if time is taken into account).
- A maneuver is a high-level characteristic of a vehicle’s motion, encompassing the position and speed of the vehicle on the road. Examples of maneuvers include going straight, changing lanes, turning, and overtaking.
- Maneuver planning aims at taking the best high-level decision for a vehicle while taking into account the path specified by path planning mechanisms.
- A trajectory is a sequence of states visited by the vehicle, parameterized by time and, most probably, velocity.
- Trajectory planning or trajectory generation is the real-time planning of a vehicle’s move from one feasible state to the next, satisfying the car’s kinematic limits based on its dynamics and as constrained by the navigation mode.

This is the general view of self-driving autonomous system integration :

![img](https://miro.medium.com/max/645/0*f7mv-553dmnBwsIh.png)

The blocks inside the container are the parts of the path planning procedure;

![img](https://miro.medium.com/max/645/0*yhK2IKoTf-9qBMVF.png)

# Trajectory generation :

For each efficient target, we compute the corresponding trajectory. We send commands to the controller as a set of waypoints, i.e., discrete points (supposedly closed to one another) spread across the trajectory, often at a fixed interval equal to the controller’s sampling time.

![img](https://miro.medium.com/max/875/0*C_dwy7oc1KE1x6BM.png)

For my project, the trajectory is generated using cubic spline with four points : *(Note: This explanation is in Frenet coordinates, we use the variables* ***s\*** *and* ***d\*** *to describe a vehicle’s position on the road. The* ***s\*** *coordinate represents distance along the road (also known as longitudinal displacement) and the d coordinate represents a side-to-side position on the road (also known as lateral displacement). And* ***r\*** *is the width of the road (in meters)*

- Current position (s, d)
- Desired lane (s+30, r*lane+(r/2))
- Desired lane (s+60, r*lane+(r/2))
- Desired lane (s+90, r*lane+(r/2))

The controller then has to regenerate trajectory segments between two consecutive waypoints, such that manipulator reaches the next waypoint within the fixed time interval while staying within joint limits, velocity limits, and acceleration limits. However, the controller does not really consider even collision avoidance or anything else

# Prediction:

![img](https://miro.medium.com/max/854/0*bLXswHdzYK-SmSte.png)

We predict situations in over environment in order to get you to the destination safely and efficiently. For this project I had to build collision detection, that predicts a possible collision with two cars.

# Behavior:

![img](https://miro.medium.com/max/875/0*YyFFpgNYGX5pmAlk.png)

Behavior planner takes input :

- map of the world,
- route to the destination
- prediction about what static and dynamic obstacles are likely to do

Output: Suggested maneuver for the vehicle which the trajectory planner is responsible for reaching collision-free, smooth and safe behavior.

# Behavior Tree

A Behavior Tree (BT) is a mathematical model of plan execution used in computer science, robotics, control systems, and video games. They describe switchings between a finite set of tasks in a modular fashion. Their strength comes from their ability to create very complex tasks composed of simple tasks, without worrying how the simple tasks are implemented. BTs present some similarities to hierarchical state machines with the key difference that the main building block of behavior is a task rather than a state. Its ease of human understanding make BTs less error-prone and very popular in the game developer community. BTs have been shown to generalize several other control architectures.

# Pros of using Behavior trees

- Useful when we have so many transitions and states
- Transform hardly-visible state machine into the hierarchical system
- Encapsulate and separate conditional tasks into classes
- Easy automation tests for each task.
- Better when pass/fail of tasks is central
- Reusability
- The appearance of goal-driven behavior
- Multi-step behavior
- Fast
- Recover from errors

# Cons of using Behavior trees

- Clunky for state-based behavior
- Changing behavior based on external changes
- Isn’t really thinking ahead about unique situations
- Only as good as the designer makes it (just follows the recipes)

# Composite Node

A composite node is a node that can have one or more children. They will process one or more of these children in either a first to last sequence or random order depending on the particular composite node in question, and at some stage will consider their processing complete and pass either success or failure to their parent, often determined by the success or failure of the child nodes. During the time they are processing children, they will continue to return Running to the parent.

# Leaf

These are the lowest level node type and are incapable of having any children.

Leaves are however the most powerful of node types, as these will be defined and implemented for your intelligent system to do the actions and behaviors specific or character specific tests or actions required to make your tree actually do useful stuff. A leaf node can be a condition or a Task(Action).

# Condition

A condition can return true for success and false otherwise.

# Task

The task can return true if it is completed, false, otherwise.

# Sequences

The simplest composite node found within behavior trees, their name says it all. A sequence will visit each child in order, starting with the first, and when that succeeds will call the second, and so on down the list of children. If any child fails it will immediately return failure to the parent. If the last child in the sequence succeeds, then the sequence will return success to its parent.

It’s important to make clear that the node types in behavior trees have quite a wide range of applications. The most obvious use of sequences is to define a sequence of tasks that must be completed in entirety, and where the failure of one means further processing of that sequence of tasks becomes redundant.

In the example below is an example of Selector hierarchy, as a part of my behavioral tree used for the path planning project :

![img](https://miro.medium.com/max/716/0*mMqucD-c6O7Af_6g.png)

Execution: The main goal of this selector is to choose left child (detecting whether we have a car very close before us, and adapt the speed accordingly) or right child (drive normally)

This selector will return true if and only if all children return true according to the ordered steps of execution :

1. The car is in second lane (IsCurentLane condition returns true/false)

— (If this block return false, then we stop examining the rest of the blocks in this sequence)

\2. It is safe to switch lane (SafeToSwitchLane condition returns true)

— (if this block return false, then we stop examining the rest of the blocks in this sequence)

\3. Successfully perform the switch task (SwitchLane task is successfully executed, returns true)

\4. Goal achieved

# Selector

Where a sequence is an AND, requiring all children to succeed to return success, a selector will return success if any of its children succeed and not process any further children. It will process the first child, and if it fails will process the second, and if that fails will process the third, until success is reached, at which point it will instantly return success. It will fail if all children fail. This means a selector is analogous with an OR gate, and as a conditional statement can be used to check multiple conditions to see if any one of them is true.

In the example below is an example of Sequence hierarchy, as a part of my behavioral tree used for the path planning project :

![img](https://miro.medium.com/max/875/0*oDYshpa_ctRwcFSw.png)

Execution: The main goal of this selector is to choose left child (detecting whether we have a car very close before us, and adapt the speed accordingly) or right child (drive normally)

This selector will return true only if one of its children returns true, execution is according to the following steps :

**Left Child (Sequence): Returns true if there is car close before us and we are able to adapt our speed**

1. Is there a car close in front of us? (IsCarCloseBeforeUs condition passed)

— (If this block return false, then we stop examining the rest of the blocks in this sequence)

\3. Approximate speed

— (If this block return false, then we stop examining the rest of the blocks in this sequence)

\4. Drive

— (If Left Child return true, then we stop examining the rest of the blocks in this selector

— — — — — — — — — — -

**Right Child (Task)**

1. Drive normally

# Priority Selector

Very simple, It’s the same as a selector but this time they are ordered somehow. If the priority selector is used, child behaviors are ordered in a list and tried one after another.

For this project, I used a priority selector to select and prioritize which of the lanes we should drive/switch. Below there is a picture describing this behavior :

![img](https://miro.medium.com/max/875/0*HA8Vk_ZKHGOO5PlY.png)

# Priority Estimation

For this project I prioritize which of the lanes we should drive or switch based on the following formula :

![img](https://miro.medium.com/max/640/0*rRdUIU0nxD9bw0ZZ.png)

The Bigger the reward is and smaller the penalty, priority for visiting the lane increases.

# Behavior Tree Architecture for Path Planning

Bellow is the complete Path planning behavior tree architecture :

![img](https://miro.medium.com/max/875/0*23lw30WhRGjs1mbM.png)

You can see my implementation on Github :

[kirilcvetkov92/Path-planningContribute to kirilcvetkov92/Path-planning development by creating an account on GitHub.github.com](https://github.com/kirilcvetkov92/Path-planning)