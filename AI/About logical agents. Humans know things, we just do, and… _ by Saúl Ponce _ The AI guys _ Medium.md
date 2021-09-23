---
title: About logical agents
categories:
- AI
- logical agents
tags:
- logical agents
date: 2021/9/22
---



> [medium.com](https://medium.com/the-ai-guys/about-logical-agents-dab799d041f3)

> Humans know things, we just do, and when we have to make a choice we use the stuff we know to imagine......

About logical agents
====================

Humans know things, we just do, and when we have to make a choice we use the stuff we know to imagine what will happen next.

The agents that we’ve discussed so far ([the greedy ones](/the-ai-guys/about-greedy-agents-37d346db34f4)) cannot take decisions based on thing they can know; instead they are limited to the things we have told them.

For example, the Huffman compression algorithm can know how many times a letter has been read from a file, but cannot _infer_ if the next letter would be a _s_ or a _k_.

So what? why would the agent want to know beforehand which letter follows which one, and maybe, it’s alright in this example; but taking the ability to infer out of the humans and putting it into an agent turns it into a _logical agent_ and they are pretty intelligent compared to any greedy one.

But, how to give an agent the gift of taking what it knows, and looking for new information based on that? this is specially useful when the agent cannot (or should not) know everything it needs to make a choice (for example one that plays a video game or poker); this way we can ignore having to teach the agent every possible play, and let it infer its way to victory instead.

The knowledge base
==================

First of all, the agent still has to know something, and that something must be stored somewhere, well, as you might have guessed, that something is the knowledge base (KB for short, just remember it’s not kilobyte).

For something to qualify as an acceptable member of the KB, it must be written or represented in a [formal language](https://en.wikipedia.org/wiki/Formal_language) so the KB cannot have ambiguities.

There are two general approaches to build the KB: the **declarative** and **procedural** approaches. The declarative approach is the simplest one, one ought to give the agent everything it needs to know for it to work correctly, this is pretty boring and exhausting because it takes all the work of figuring out what to do from the agent and gives it to us, the agent’s designers, not fair.

On the other hand, the procedural approach tries to give the agent some information at the beginning (or make it able to percibe the information with its sensors) and then let the agent infer with other mechanisms whatever information it needs next. This makes the agent a little bit more complicated but saves us the boring work of having to tell it everything.

In any case, the KB has some basic operations: TELL, ASK and EXEC.

*   TELL is the way to insert data into the KB.
*   ASK is the way to retrieve information from the KB.
*   EXEC is the way to use the information acquired to execute the next step.

The logical components
======================

For all of this TELL and ASK thing to work, the agent must know logic, most of the time [propositional logic](https://en.wikipedia.org/wiki/Propositional_calculus) suffices.

In an ASK operation, if the agent tries to know if something is true, and there’s nothing in its KB to suppose that that something it’s false, then the agent can conclude that it is true based in its current KB. The fact that something can be concluded as true based on its status in the KB is called _entailment_. Beware that this does not work if you want to show that something must be false.

This is called model-checking, and it can be done in many different ways, but mostly you want model-checking algorithms that are _sound_ and _complete._

By sound we mean that the model-checking algorithm preserves truth, and it can be easily achieved by only allowing it to get information from the KB (it does not take any kind of external suppositions).

And by complete we mean that it can be used to get to all the entailed models of the KB, so no model is unreachable from it.

Inferring knowledge
===================

This whole model-checking is a nice way to show if some assumption or perceptions is true, but very costly and hard to implement, that’s why it’s usually used to evaluate other, more simple, ways to infer knowledge from the KB.

One very simple way of inferring is to construct the truth table of all the KB, if our supposition is always true in rows in which our KB is also true, then the supposition can be inferred from our KB. For example, take the following KB:

![](https://miro.medium.com/max/304/1*bG9DHGBEc1snylaAF_6PLg.png)A very simple knowledge base

Then we can build the truth table for a supposition A v B:

![](https://miro.medium.com/max/1400/1*_hW13M_ICb7p3iWF98pOtQ.png)A not so simple truth table

As seen in the highlighted rows, when our KB is true, our supposition is also true, so the supposition can be inferred from the KB.

There are other methods like modus ponens, modus tollens, resolution and forward and backward chaining.

Modus ponens is very simple, it states that if you have a logical condition and you know that the precondition is true, then the postcondition is also true.

![](https://miro.medium.com/max/524/1*iIGgbEElBnCQkGnUMIKJXQ.png)Modus Ponens

Modus tollens is the same but using the negation of the postcondition, in this case what you get is the negation of the precondition.

![](https://miro.medium.com/max/600/1*bH-3CWvkkaaLNCUM86oB5Q.png)Modus Tollens

Resolution uses something called [Horn clauses](https://en.wikipedia.org/wiki/Horn_clause) in which there are several statements joined with or operators and at most one of them is not negated.

![](https://miro.medium.com/max/772/1*9JV74tkV5S_PFtpxpbdLIw.png)Resolution

Forward chaining uses consecutive modus ponens operations over the KB, resulting in adding most, if not all, of the statements that can be inferred from it.

![](https://miro.medium.com/max/1400/1*EPdp0o5Tjeu7SncdzA6HZw.png)Forward Chaining

Backward chaining is the same, but using modus tollens.

![](https://miro.medium.com/max/1400/1*utxFAAV1Q1VyZJadfwdTRQ.png)Backward Chaining

With all of this, an agent with a solid KB and the ability to get information can start to do ASK operations to its KB, which then will use one or several of the former methods to know if that query can be inferred from what it already knows.

Now our agent knows things as well, like humans.