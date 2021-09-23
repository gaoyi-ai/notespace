---
title: Artificial Intelligence Planning with STRIPS, A Gentle Introduction
categories:
- AI
- STRIPS
tags:
- STRIPS
date: 2021/9/22
---



> [www.primaryobjects.com](http://www.primaryobjects.com/2015/11/06/artificial-intelligence-planning-with-strips-a-gentle-introduction/)

> Learn about artificial intelligence planning with STRIPS. This tutorial covers how to create an autom......

[](#Introduction "Introduction")Introduction
--------------------------------------------

Imagine you’re developing a video game where the player has to find magical items, build a weapon, and then attack monsters. This would be relatively straight-forward to create. Just throw some magical items around the map, let the player move around to discover them, and leave it to the player to figure out what combination to put together to build a powerful weapon. Easy.

But, what about the NPC? Once we throw a computer-controlled player into the mix, things get a little complicated. How would you tell the computer which items to collect? Should it just move randomly around, pick up random items that it happens to come across, and build whatever weapon that happens to make? It certainly wouldn’t make the most challenging of NPC characters.

There is a more intelligent approach. Through the use of artificial intelligence planning, you can program the computer to formulate a plan. For “easy” difficulty, the computer could have the goal of building a club. For “hard”, the computer could build a bazooka. But, how do we give the computer this kind of planning intelligence?

In this tutorial, we’ll learn about STRIPS artificial intelligence AI planning. We’ll cover how to create a world domain and various problem sets, to provide the computer with the intelligence it needs to make a plan and execute it, effectively providing a much better gaming experience.

[](#What-is-STRIPS "What is STRIPS?")What is STRIPS?
----------------------------------------------------

The Standford Research Institute Problem Solver ([STRIPS](https://en.wikipedia.org/wiki/STRIPS)) is an automated planning technique that works by executing a domain and problem to find a goal. With STRIPS, you first describe the world. You do this by providing objects, actions, preconditions, and effects. These are all the types of things you can do in the game world.

Once the world is described, you then provide a problem set. A problem consists of an initial state and a goal condition. STRIPS can then search all possible states, starting from the initial one, executing various actions, until it reaches the goal.

A common language for writing STRIPS domain and problem sets is the Planning Domain Definition Language ([PDDL](https://en.wikipedia.org/wiki/Planning_Domain_Definition_Language)). PDDL lets you write most of the code with English words, so that it can be clearly read and (hopefully) well understood. It’s a relatively easy approach to writing simple AI planning problems.

[](#What-can-STRIPS-do "What can STRIPS do?")What can STRIPS do?
----------------------------------------------------------------

A lot of different problems can be solved using STRIPS and PDDL. As long as the world domain and problem can be described with a finite set of actions, preconditions, and effects, you can write a PDDL domain and problem to solve it.

For example, [stacking blocks](https://github.com/primaryobjects/strips#example-output-from-blocks-world-problem-3), [Rubik’s cube](https://github.com/primaryobjects/strips/blob/master/examples/rubikscube/problem1.txt), navigating a robot in [Shakey’s World](https://github.com/primaryobjects/strips/blob/master/examples/shakeysworld/problem1.txt), [Starcraft](https://github.com/primaryobjects/strips#starcraft) build orders, and a lot [more](https://github.com/primaryobjects/strips/tree/master/examples), can be described using STRIPS and PDDL.

[](#Creating-a-Domain "Creating a Domain")Creating a Domain
-----------------------------------------------------------

Let’s start with the example game that we began to describe above. Suppose our world is filled with ogres, trolls, dragons, and magic! Various elements are scattered in caves. Mixing them together creates new magic spells for the player. Let’s see how we can describe this problem in PDDL for an artificial intelligence planning implementation.

We’ll start by defining the domain. All PDDL programs begin with a skeleton layout, as follows:

```
(define (domain magic-world)
   (:requirements :strips :typing)
)
```

That’s it. So far, pretty easy. Let’s add a description of the different kinds of “things” in our world.

```
(define (domain magic-world)
   (:requirements :strips :typing)
   (:types player location monster element chest)
)
```

What we’ve defined in the above PDDL code is five types of things for our domain. We’ll have players and locations, obviously. A player is the user or computer-controlled character. A location is a specific place on the map, such as an area, country, or region. We’ll use this to separate our map into specific regions.

We’ve also defined a “monster” type, which will describe enemies that might be guarding treasure or a region. Also, an “element” type. This will be our ingredient type for making magic spells! Finally, a “chest” type. This is basically a treasure chest that our ingredients will sit inside.

So, the idea is that the player or NPC must visit different areas of the world, find treasure chests, defeat any monsters guarding them, open the treasure chests, collect ingredients, mix them together, build a powerful weapon, then attack the other players. Whew, that sure is a lot! Good thing we have artificial intelligence planning to help us.

[](#Creating-Domain-Actions "Creating Domain Actions")Creating Domain Actions
-----------------------------------------------------------------------------

Let’s define a simple action for our domain. We’ll create a “move” action that allows the player to move from one location to another, as long as they border each other.

```
(define (domain magic-world)
   (:requirements :strips :typing)
   (:types player location monster element chest)

   (:action move
      :parameters (?p - player ?l1 - location ?l2 - location)
      :precondition (and (at ?p ?l1) (border ?l1 ?l2) (not (guarded ?l2)))
      :effect (and (at ?p ?l2) (not (at ?p ?l1)))
   )
)
```

An action is defined by using the :action command. You then specify any parameters used in the action, which in our case, we’ll need to specify a player and two locations (the current location and the new location). We then specify a precondition. This sets the rules for when this action is valid, given the parameters. For example, we’ll only allow moving to a new location if it borders the player’s current location. Otherwise, it’s too far away. We’ll also only allow moving to a location that is not currently guarded by a monster. If it is, the player will have to attack the monster first.

Preconditions are specified by using simple logical phrases. The phrase “and (at ?p ?l1)” simply means that the player must currently be at location 1. The phrase “border ?l1 ?l2” means that the two locations must border each other. Likewise “not (guarded ?l2)” means that the second location has to be free and clear of monsters.

[](#Let’s-Move-Around-in-the-World "Let’s Move Around in the World")Let’s Move Around in the World
--------------------------------------------------------------------------------------------------

Now that we have a simple STRIPS PDDL artificial intelligence planning domain, we can test it out with a simple AI planning problem. First, let’s describe our world with a basic problem and ask the AI to figure out the steps to move the player from a starting location to a goal location.

[](#Creating-a-STRIPS-Problem "Creating a STRIPS Problem")Creating a STRIPS Problem
-----------------------------------------------------------------------------------

```
(define (problem move-to-castle)
   (:domain magic-world)

   (:objects
      npc - player
      town field castle - location
   )

   (:init
      (border town field)
      (border field castle)

      (at npc town)
   )

   (:goal (and (at npc castle)))
)
```

The above STRIPS AI planning problem, uses the domain that we’ve designed above (containing a “move” action command) to define our world. We’re simply included an NPC and three locations. There is a town where the NPC starts. There is also a field and a castle. The goal, defined by the “:goal” directive, is to have the NPC move to the castle. Seems easy enough.

[](#Solution-to-the-Move-to-Castle-Problem "Solution to the Move-to-Castle Problem")Solution to the Move-to-Castle Problem
--------------------------------------------------------------------------------------------------------------------------

If we [run](http://stripsfiddle.herokuapp.com/?d=7cFTqSDwnwjXwDRzj&p=bc7r2Gy7YBmSFsSBn&a=BFS) the artificial intelligence AI planning technique STRIPS on the domain and problem above, we get the following solution:

```
Solution found in 2 steps!
1. move npc town field
2. move npc field castle
```

This is exactly correct - and optimal too! It only takes 2 steps to move from the town to the neighboring field, and finally to the neighboring castle.

Let’s take a look at a slightly more tricky example. We’ll throw a monster onto the field, blocking that path. Instead we’ll provide a different way to the castle and see if the AI planning algorithm can figure it out.

[](#Bypassing-the-Dragon-in-the-Field "Bypassing the Dragon in the Field")Bypassing the Dragon in the Field
-----------------------------------------------------------------------------------------------------------

```
(define (problem sneak-past-dragon-to-castle)
   (:domain magic-world)

   (:objects
      npc - player
      dragon - monster
      town field castle tunnel river - location
   )

   (:init
      (border town field)
      (border town tunnel)
      (border field castle)
      (border tunnel river)
      (border river castle)

      (at npc town)
      (at dragon field)
      (guarded field)
   )

   (:goal (and (at npc castle)))
)
```

Notice that this newly defined STRIPS PDDL problem looks very similar to our first one. The difference here is that we’ve defined a dragon and placed him on the field. Since the field is guarded by a dragon (monster), and our “move” action has a precondition that a location can not be guarded by a monster, we can no longer move directly from the town, to the field, and finally to the castle. Our path to the field is effectively blocked. Especially, since we don’t have an attack action defined! The AI must find another way around.

We’ve actually provided an alternate means to get to the castle, by going through the tunnel in town, across the river, and finally to the castle.

[](#Solution-to-the-Sneak-Past-Dragon-to-Castle-Problem "Solution to the Sneak-Past-Dragon-to-Castle Problem")Solution to the Sneak-Past-Dragon-to-Castle Problem
-----------------------------------------------------------------------------------------------------------------------------------------------------------------

If we [run](http://stripsfiddle.herokuapp.com/?d=7cFTqSDwnwjXwDRzj&p=EQdvuyp3GPkeEzSBv&a=BFS) the problem, the artificial intelligence AI planner finds the following solution.

```
Solution found in 3 steps!

1. move npc town tunnel
2. move npc tunnel river
3. move npc river castle
```

In just 3 steps, the AI can move from the town to the castle, safely bypassing the dragon. What’s even more interesting, is taking a look at the search process that the AI planning algorithm used to find the solution.

```
Using breadth-first-search.

Depth: 0, 2 child states.
Depth: 1, 2 child states.
Depth: 1, 3 child states.
Depth: 2, 3 child states.
Depth: 2, 3 child states.
Depth: 2, 3 child states.
```

As part of the search process, the STRIPS AI planner begins searching at the initial state defined in the problem (player at town). This corresponds to a depth of 0 in the search tree. The AI then proceeds down the child states in the graph of available actions. Somewhere at depth 1, the AI runs into the dragon in the field. At this point, it can not go any further and backtracks to a different branch in the search tree of available actions. It then finds another path at depth 1, using the tunnel instead. Finally, it searches forward at depth 2, finding the river, followed by the castle.

[](#Methods-for-Searching-for-Solutions "Methods for Searching for Solutions")Methods for Searching for Solutions
-----------------------------------------------------------------------------------------------------------------

In the above two examples, we’ve seen how the AI searches through the list of available actions at each state, in order to reach the goal state. But, how exactly does the AI search?

With STRIPS AI planning, a graph can be constructed that contains all available states and the actions that bring you to each state. This is called a planning graph. Here’s an example of what a planning graph might look like (this comes from the birthday dinner [domain](https://github.com/primaryobjects/strips/blob/master/examples/dinner/domain.pddl) and [problem](https://github.com/primaryobjects/strips/blob/master/examples/dinner/problem.pddl)):

![AI Planning Graph for artificial intelligence planning STRIPS](https://raw.githubusercontent.com/primaryobjects/strips/master/examples/dinner/images/birthday-dinner.jpg)

"AI Planning Graph for artificial intelligence planning STRIPS")AI Planning Graph for artificial intelligence planning STRIPS

As with most trees and graphs, we can traverse them using a variety of algorithms. For STRIPS artificial intelligence planners, a common method is to use breadth-first-search, depth-first-search, and the most intelligent approach - A* search.

[](#Breadth-First-Search "Breadth First Search")Breadth First Search
--------------------------------------------------------------------

Breadth-first-search finds the most optimal solution to a STRIPS problem. It searches from the initial state, and evaluates all child states that are available from valid actions at the initial state. It only evaluates at the current depth, completely checking all states before moving to the next depth level. In this manner, if any of the child states results in the goal state, it can stop searching right there and return the solution. This solution will always be the shortest. However, because breadth-first-search scans every single child state at the current depth level, it could take a long time to search, especially if the tree is very wide (with lots of available actions per state).

[](#Depth-First-Search "Depth First Search")Depth First Search
--------------------------------------------------------------

With depth-first-search, the initial state is evaluated and all child states are pushed onto a stack or queue. The AI then moves down the tree to the next child state, then the next child state, all the way down until either a goal is found, or no more child states exist. When it reaches a dead-end, it backtracks until another available child state is found. It repeats this until it gets back to the initial starting state, and then chooses the next child state to head down.

Although depth-first-search might not find the most optimal solution to a STRIPS artificial intelligence planning problem, it can be faster than breadth-first-search in some cases.

[](#A-Search "A* Search")A* Search
----------------------------------

The most intelligent of the searching techniques for solving a STRIPS PDDL artificial intelligence AI planning problem is to use A _search. A_ is a [heuristic](https://en.wikipedia.org/wiki/Heuristic_%28computer_science%29) search. This means it uses a formula or calculation to determine a cost for a particular state. A state that has a cost of 0 is our goal state. A state that has a very large value for cost would be very far from our goal state.

Similar to breadth-first and depth-first search, A _search evaluates all valid actions for a state to determine the available child states. However, here is where it differs. Before selecting the next state, A_ assigns a cost to each one, based upon certain characteristics of the state. It then chooses the next lowest-cost state to move to, with the idea being that the lowest costing states are the ones most likely to result in the goal.

For example, an easy A* search cost heuristic is to use landmark-based heuristics for searching. In our Magic World example, we want to move from the town to the castle. We know that the character must visit the tunnel and the river in order to reach the castle. Therefore, we could assign a cost of 15 to all states by default. If the AI has visited the tunnel state, we reduce the cost by 5, resulting in a cost of 10 for this state. If the AI has visited the river, we again reduce the cost by 5, resulting in a cost of 5 for this state. Finally, when the AI reaches the castle, we reduce the cost to 0.

The code to implement an A* landmark-based heuristic might look something like this:

```
function cost(state) {
    // This is our A* heuristic method to calculate the cost of a state.
    // The heuristic will be how many required locations have been visited. Subtract x from cost for each correct location, with 0 meaning all required locations have been visited and we're done.
    var cost = 15;

    for (var i in state.actions) {
        var action = state.actions[i].action;

        if (action == 'tunnel') {
            cost -= 5;
        }
        else if (action == 'river') {
            cost -= 5;
        }
        else if (action == 'castle') {
            cost -= 5;
        }
    }

    return cost;
}
```

A* search usually provides the fastest way for finding a goal state in a STRIPS planning problem.

[](#Getting-Crazy-with-Magic-World "Getting Crazy with Magic World")Getting Crazy with Magic World
--------------------------------------------------------------------------------------------------

Let’s beef-up our magic world domain, by adding a whole bunch of new actions. We’ll give our characters the ability to move, attack, open treasure chests, collect elements, and build a weapon. This will be more interesting! We’ll change our domain to be defined, as follows:

```
(define (domain magic-world)
   (:requirements :strips :typing)
   (:types player location monster element chest)

   (:action move
      :parameters (?p - player ?l1 - location ?l2 - location)
      :precondition (and (at ?p ?l1) (border ?l1 ?l2) (not (guarded ?l2)))
      :effect (and (at ?p ?l2) (not (at ?p ?l1)))
   )

   (:action attack
      :parameters (?p - player ?m - monster ?l1 - location ?l2 - location)
      :precondition (and (at ?p ?l1) (at ?m ?l2) (border ?l1 ?l2) (guarded ?l2))
      :effect (not (at ?m ?l2) not (guarded ?l2))
   )

   (:action open
      :parameters (?p - player ?c - chest ?l1 - location)
      :precondition (and (at ?p ?l1) (at ?c ?l1) (not (open ?c)))
      :effect (and (open ?c))
   )

   (:action collect-fire
      :parameters (?p - player ?c - chest ?l1 - location ?e - element)
      :precondition (and (at ?p ?l1) (at ?c ?l1) (open ?c) (fire ?e) (in ?e ?c) (not (empty ?c))
      :effect (and (empty ?c) (has-fire ?p))
   )

   (:action collect-earth
      :parameters (?p - player ?c - chest ?l1 - location ?e - element)
      :precondition (and (at ?p ?l1) (at ?c ?l1) (open ?c) (earth ?e) (in ?e ?c) (not (empty ?c))
      :effect (and (empty ?c) (has-earth ?p))
   )

   (:action build-fireball
      :parameters (?p - player)
      :precondition (and (has-fire ?p) (has-earth ?p))
      :effect (and (has-fireball ?p) (not (has-fire ?p) not (has-earth ?p)))
   )
)
```

In the above domain, we’ve defined a bunch of valid actions. A user or computer-controlled player can move from one area to the next, as long as that area is not currently guarded by a monster. If it is, the player will need to attack the monster first. Hence, we’ve defined an “attack” action as well.

We’ve also defined an “open” action to allow opening a treasure chest, and two types of treasures. We have an element of type fire and an element of type earth. Both can be collected for building a weapon. Once the player has both elements, he can build the “fireball” spell.

You can see how other types of spells and treasures can be added, by simply defining additional actions. Now, how about the problem?

[](#Building-a-Fireball-Weapon "Building a Fireball Weapon")Building a Fireball Weapon
--------------------------------------------------------------------------------------

Now, we’ll define an updated problem of figuring out how to build a fireball weapon in our world. The AI will have to figure out a plan for where to move, who to attack, and what to do, in order to build the weapon. We’ll define our problem, as follows:

```
(define (problem fireball)
   (:domain magic-world)

   (:objects
      npc - player
      ogre dragon - monster
      town field river cave - location
      box1 box2 - chest
      reddust browndust - element
   )

   (:init
      (border town field)
      (border field town)
      (border field river)
      (border river field)
      (border river cave)
      (border cave river)

      (at npc town)
      (at ogre river)
      (at dragon cave)
      (guarded river)
      (guarded cave)

      (at box1 river)
      (at box2 cave)

      (fire reddust)
      (in reddust box1)

      (earth browndust)
      (in browndust box2)
   )

   (:goal (and (has-fireball npc)))
)
```

In the above PDDL problem, we’ve added a lot to our world. We have a couple of monsters (an ogre and a dragon). Both monsters are guarding locations on the map. We’ve also added two treasure chests, containing magical elements that can be collected. A character will need both elements in order to build a fireball weapon. Let’s see how the AI STRIPS planner solves this.

[](#Solution-to-the-Fireball-Weapon-Problem "Solution to the Fireball Weapon Problem")Solution to the Fireball Weapon Problem
-----------------------------------------------------------------------------------------------------------------------------

If we [run](http://stripsfiddle.herokuapp.com/?d=7cFTqSDwnwjXwDRzj&p=QuMJhQMBwSTSymFsy&a=BFS) the domain and problem, we get the following optimal solution.

```
Solution found in 10 steps!
1. move npc town field
2. attack npc ogre field river
3. move npc field river
4. attack npc dragon river cave
5. open npc box1 river
6. collect-fire npc box1 river reddust
7. move npc river cave
8. open npc box2 cave
9. collect-earth npc box2 cave browndust
10. build-fireball npc
```

The AI has successfully determined a plan, which involves moving from the town to the field, attacking the ogre at the river, and then moving to the river. It then attacks the dragon in the cave, and then opens the treasure chest in the river (the AI apparently wanted to attack the dragon before opening the treasure chest sitting at its feet - in reality, both actions had an equal depth cost, so the AI simply chose the first one that it found). It then collects the fire element from the treasure chest and moves to the cave. The AI opens the treasure chest in the cave, collects the earth element, and finally builds the fireball weapon.

What would happen if instead of using breadth-first-search, we try [running](http://stripsfiddle.herokuapp.com/?d=7cFTqSDwnwjXwDRzj&p=QuMJhQMBwSTSymFsy&a=DFS) this with depth-first-search? Let’s take a look.

```
Solution found in 15 steps!
1. move npc town field
2. attack npc ogre field river
3. move npc field river
4. attack npc dragon river cave
5. move npc river cave
6. open npc box2 cave
7. move npc cave river
8. open npc box1 river
9. move npc river cave
10. collect-earth npc box2 cave browndust
11. move npc cave river
12. collect-fire npc box1 river reddust
13. move npc river field
14. move npc field town
15. build-fireball npc
```

Depth-first search produces a significantly longer set of steps to achieve the goal. It starts off the same as the optimal solution above, but at step 5, instead of opening the treasure in the river and collecting the fire element, the AI instead chooses to move into the cave and open the treasure there first. Interestingly, after opening the treasure in the cave, it then moves back to the river and opens the box there. This is effectively back-tracking. Once again, it repeats its steps of moving back into the cave to collect the element, and back to the river to collect the second element. Laughably, the AI then walks all the way back to town before building the fireball weapon!

This is a good example of the difference between breadth-first and depth-first search with STRIPS AI planning. The AI was simply following straight down a deep path of actions that lead to the goal state. Many other paths likely exist as well, including of course, the optimal path that was found by breadth-first search.

[](#Integrating-Automated-Planning-into-Games-and-Applications "Integrating Automated Planning into Games and Applications")Integrating Automated Planning into Games and Applications
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

You can see how STRIPS artificial intelligence planning allows the computer to prepare a detailed step plan for achieving a goal. Now, how would you use this with a game?

In the main loop of a game, where the screen is continuously redrawn, there is usually associated logic for moving NPC characters and performing other necessary tasks at each tick. An automated planner can be integrated into this loop to continuously update plans for each NPC character, depending on their goals. Since a player or other NPC character can affect the state of the current world, we would need to update the plan at each tick, so that it reacts to any changes in the current state and updates its plan accordingly.

In this manner, the problem PDDL file could contain a dynamically updated :init section, where the current state of the world is described. The :goal section would remain static, while the :init section changes. At each defined interval, the AI would re-execute the automated planner to produce a new plan, given the state of the world. It would then redirect the NPC character to whichever action is next in the computed plan. The resulting plan may contain less or more action steps to achieve the goal. As the initial state of the problem PDDL changes, so too would the formulated solution plan.

[](#Other-Automated-Planning-Algorithms-For-Speed "Other Automated Planning Algorithms For Speed")Other Automated Planning Algorithms For Speed
-----------------------------------------------------------------------------------------------------------------------------------------------

It’s important to maintain search speed as a top priority. Since the automated planner may well be running at a frequently defined time interval, the faster the search can complete, the higher the application response rate. You’ll likely want to use an optimally programmed A* search heuristic.

For faster automated planner searching, you may even want to upgrade to other types of artificial intelligence planners, including [GraphPlan](https://en.wikipedia.org/wiki/Graphplan) and [hierarchical task network](https://en.wikipedia.org/wiki/Hierarchical_task_network) (HTN) planners.

[](#Trying-an-Automated-Planner-Yourself "Trying an Automated Planner Yourself")Trying an Automated Planner Yourself
--------------------------------------------------------------------------------------------------------------------

You can experiment with different STRIPS PDDL domains and problems with the online application [Strips-Fiddle](https://stripsfiddle.herokuapp.com/). Try any of the example domains or create an account to design your own artificial intelligence planning domains and problems.

For integrating STRIPS-based AI planning into your application or game, you can use the node.js [strips](https://www.npmjs.com/package/strips) library, which supports breadth-first, depth-first, and A* searching. The [homepage](https://github.com/primaryobjects/strips/) for the strips library provides some higher-level overview on the library, including an example of the [Starcraft](https://github.com/primaryobjects/strips/#starcraft) domain.

Domains and problems can be loaded from plain text files into the node.js strips library. Running a problem set can be done with the following code:

```
var strips = require('strips');


strips.load('./domain.txt', './problem.txt', function(domain, problem) {
    
    var solutions = strips.solve(domain, problem);

    
    for (var i in solutions) {
        var solution = solutions[i];

        console.log('- Solution found in ' + solution.steps + ' steps!');
        for (var i = 0; i < solution.path.length; i++) {
            console.log((i + 1) + '. ' + solution.path[i]);
        }        
    }
});
```

By default, depth-first-search is used. You can change this to breadth-first-search by adding a boolean parameter to the solve() method, as follows:

```
var solutions = strips.solve(domain, problem, false);
```

You can also specify a cost heuristic to use A* search, as follows:

```
// Use A* search to run the problem against the domain.
var solutions = strips.solve(domain, problem, cost);

// An example A* landmark-based heuristic method to calculate the cost of a state.
function cost(state) {
    var cost = 10;

    for (var i in state.actions) {
        var action = state.actions[i].action;

        if (action == 'depot') {
            cost -= 5;
        }
        else if (action == 'barracks') {
            cost -= 5;
        }
    }

    return cost;
}
```

For more details, see the Starcraft strips [example](https://github.com/primaryobjects/strips/blob/master/starcraft.js). Give it a try and have fun!

[](#About-the-Author "About the Author")About the Author
--------------------------------------------------------

This article was written by [Kory Becker](http://www.primaryobjects.com/kory-becker/), software developer and architect, skilled in a range of technologies, including web application development, machine learning, artificial intelligence, and data science.