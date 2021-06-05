---
title: A Gentle Introduction to Joint, Marginal, and Conditional Probability
categories:
- Math
- Statistic
tags:
- Marginal Distribution
- Joint Distribution
- Conditional Distribution
date: 2021/6/4 10:00:00
updated: 2021/6/4 16:00:00
---

> [machinelearningmastery.com](https://machinelearningmastery.com/joint-marginal-and-conditional-probability-for-machine-learning/)

# A Gentle Introduction to Joint, Marginal, and Conditional Probability

Probability quantifies the uncertainty of the outcomes of a random variable.

It is relatively easy to understand and compute the probability for a single variable. Nevertheless, in machine learning, we often have many random variables that interact in often complex and unknown ways.

There are specific techniques that can be used to quantify the probability for multiple random variables, such as the joint, marginal, and conditional probability. These techniques provide the basis for a probabilistic understanding of fitting a predictive model to data.

In this post, you will discover a gentle introduction to joint, marginal, and conditional probability for multiple random variables.

After reading this post, you will know:

*   Joint probability is the probability of two events occurring simultaneously.
*   Marginal probability is the probability of an event irrespective of the outcome of another variable.
*   Conditional probability is the probability of one event occurring in the presence of a second event.

Overview
--------

This tutorial is divided into three parts; they are:

1.  Probability of One Random Variable
2.  Probability of Multiple Random Variables
3.  Probability of Independence and Exclusivity

Probability of One Random Variable
----------------------------------

Probability quantifies the likelihood of an event.

Specifically, it quantifies how likely a specific outcome is for a random variable, such as the flip of a coin, the roll of a dice, or drawing a playing card from a deck.

> Probability gives a measure of how likely it is for something to happen.

— Page 57, [Probability: For the Enthusiastic Beginner](https://amzn.to/2jULJsu), 2016.

For a random variable _x_, _P(x)_ is a function that assigns a probability to all values of _x_.

*   Probability Density of x = P(x)

The probability of a specific event _A_ for a random variable x is denoted as _P(x=A)_, or simply as _P(A)._

*   Probability of Event A = P(A)

Probability is calculated as the number of desired outcomes divided by the total possible outcomes, in the case where all outcomes are equally likely.

*   Probability = (number of desired outcomes) / (total number of possible outcomes)

This is intuitive if we think about a discrete random variable such as the roll of a die. For example, the probability of a die rolling a 5 is calculated as one outcome of rolling a 5 (1) divided by the total number of discrete outcomes (6) or 1/6 or about 0.1666 or about 16.666%.

The sum of the probabilities of all outcomes must equal one. If not, we do not have valid probabilities.

*   Sum of the Probabilities for All Outcomes = 1.0.

The probability of an impossible outcome is zero. For example, it is impossible to roll a 7 with a standard six-sided die.

*   Probability of Impossible Outcome = 0.0

The probability of a certain outcome is one. For example, it is certain that a value between 1 and 6 will occur when rolling a six-sided die.

*   Probability of Certain Outcome = 1.0

The probability of an event not occurring, called the complement.

This can be calculated by one minus the probability of the event, or _1 – P(A)_. For example, the probability of not rolling a 5 would be 1 – P(5) or 1 – 0.166 or about 0.833 or about 83.333%.

*   Probability of Not Event A = 1 – P(A)

Now that we are familiar with the probability of one random variable, let’s consider probability for multiple random variables.

### Want to Learn Probability for Machine Learning

Take my free 7-day email crash course now (with sample code).

Click to sign-up and also get a free PDF Ebook version of the course.

[Download Your FREE Mini-Course](https://machinelearningmastery.lpages.co/leadbox/16cf92561172a2%3A164f8be4f346dc/4623731828588544/)

Probability of Multiple Random Variables
----------------------------------------

In machine learning, we are likely to work with many random variables.

For example, given a table of data, such as in excel, each row represents a separate observation or event, and each column represents a separate random variable.

Variables may be either discrete, meaning that they take on a finite set of values, or continuous, meaning they take on a real or numerical value.

As such, we are interested in the probability across two or more random variables.

This is complicated as there are many ways that random variables can interact, which, in turn, impacts their probabilities.

This can be simplified by reducing the discussion to just two random variables (_X, Y_), although the principles generalize to multiple variables.

And further, to discuss the probability of just two events, one for each variable (_X=A, Y=B_), although we could just as easily be discussing groups of events for each variable.

Therefore, we will introduce the probability of multiple random variables as the probability of event _A_ and event _B_, which in shorthand is _X=A_ and _Y=B_.

We assume that the two variables are related or dependent in some way.

As such, there are three main types of probability we might want to consider; they are:

*   **Joint Probability**: Probability of events _A_ and _B_.
*   **Marginal Probability**: Probability of event X=_A_ given variable _Y_.
*   **Conditional Probability**: Probability of event _A_ given event _B_.

These types of probability form the basis of much of predictive modeling with problems such as classification and regression. For example:

*   The probability of a row of data is the joint probability across each input variable.
*   The probability of a specific value of one input variable is the marginal probability across the values of the other input variables.
*   The predictive model itself is an estimate of the conditional probability of an output given an input example.

Joint, marginal, and conditional probability are foundational in machine learning.

Let’s take a closer look at each in turn.

### Joint Probability of Two Variables

We may be interested in the probability of two simultaneous events, e.g. the outcomes of two different random variables.

The probability of two (or more) events is called the [joint probability](https://en.wikipedia.org/wiki/Joint_probability_distribution). The joint probability of two or more random variables is referred to as the joint probability distribution.

For example, the joint probability of event _A_ and event _B_ is written formally as:

*   P(A and B)

The “_and_” or conjunction is denoted using the upside down capital “_U_” operator “_^_” or sometimes a comma “,”.

*   P(A ^ B)
*   P(A, B)

The joint probability for events _A_ and _B_ is calculated as the probability of event _A_ given event _B_ multiplied by the probability of event _B_.

This can be stated formally as follows:

*   P(A and B) = P(A given B) * P(B)

The calculation of the joint probability is sometimes called the fundamental rule of probability or the “_product rule_” of probability or the [“chain rule” of probability](https://en.wikipedia.org/wiki/Chain_rule_(probability)).

Here, _P(A given B)_ is the probability of event A given that event B has occurred, called the conditional probability, described below.

The joint probability is symmetrical, meaning that _P(A and B)_ is the same as _P(B and A)_. The calculation using the conditional probability is also symmetrical, for example:

*   P(A and B) = P(A given B) * P(B) = P(B given A) * P(A)

### Marginal Probability

We may be interested in the probability of an event for one random variable, irrespective of the outcome of another random variable.

For example, the probability of _X=A_ for all outcomes of _Y_.

The probability of one event in the presence of all (or a subset of) outcomes of the other random variable is called the [marginal probability](https://en.wikipedia.org/wiki/Marginal_distribution) or the marginal distribution. The marginal probability of one random variable in the presence of additional random variables is referred to as the marginal probability distribution.

It is called the marginal probability because if all outcomes and probabilities for the two variables were laid out together in a table (_X_ as columns, _Y_ as rows), then the marginal probability of one variable (_X_) would be the sum of probabilities for the other variable (Y rows) on the margin of the table.

There is no special notation for the marginal probability; it is just the sum or union over all the probabilities of all events for the second variable for a given fixed event for the first variable.

*   P(X=A) = sum P(X=A, Y=yi) for all y

This is another important foundational rule in probability, referred to as the “_sum rule_.”

The marginal probability is different from the conditional probability (described next) because it considers the union of all events for the second variable rather than the probability of a single event.

### Conditional Probability

We may be interested in the probability of an event given the occurrence of another event.

The probability of one event given the occurrence of another event is called the [conditional probability](https://en.wikipedia.org/wiki/Conditional_probability). The conditional probability of one to one or more random variables is referred to as the conditional probability distribution.

For example, the conditional probability of event _A_ given event _B_ is written formally as:

*   P(A given B)

The “_given_” is denoted using the pipe “|” operator; for example:

*   P(A | B)

The conditional probability for events _A_ given event _B_ is calculated as follows:

*   P(A given B) = P(A and B) / P(B)

This calculation assumes that the probability of event _B_ is not zero, e.g. is not impossible.

The notion of event _A_ given event _B_ does not mean that event _B_ has occurred (e.g. is certain); instead, it is the probability of event _A_ occurring after or in the presence of event _B_ for a given trial.

Probability of Independence and Exclusivity
-------------------------------------------

When considering multiple random variables, it is possible that they do not interact.

We may know or assume that two variables are not dependent upon each other instead are independent.

Alternately, the variables may interact but their events may not occur simultaneously, referred to as exclusivity.

We will take a closer look at the probability of multiple random variables under these circumstances in this section.

### Independence

If one variable is not dependent on a second variable, this is called [independence](https://en.wikipedia.org/wiki/Independence_(probability_theory)) or statistical independence.

This has an impact on calculating the probabilities of the two variables.

For example, we may be interested in the joint probability of independent events _A_ and _B_, which is the same as the probability of _A_ and the probability of _B._

Probabilities are combined using multiplication, therefore the joint probability of independent events is calculated as the probability of event _A_ multiplied by the probability of event _B_.

This can be stated formally as follows:

*   **Joint Probability**: P(A and B) = P(A) * P(B)

As we might intuit, the marginal probability for an event for an independent random variable is simply the probability of the event.

It is the idea of probability of a single random variable that are familiar with:

*   **Marginal Probability**: P(A)

We refer to the marginal probability of an independent probability as simply the probability.

Similarly, the conditional probability of _A_ given _B_ when the variables are independent is simply the probability of _A_ as the probability of _B_ has no effect. For example:

*   **Conditional Probability**: P(A given B) = P(A)

We may be familiar with the notion of statistical independence from sampling. This assumes that one sample is unaffected by prior samples and does not affect future samples.

Many machine learning algorithms assume that samples from a domain are independent to each other and come from the same probability distribution, referred to as [independent and identically distributed](https://en.wikipedia.org/wiki/Independent_and_identically_distributed_random_variables), or i.i.d. for short.

### Exclusivity

If the occurrence of one event excludes the occurrence of other events, then the events are said to be [mutually exclusive](https://en.wikipedia.org/wiki/Mutual_exclusivity).

The probability of the events are said to be disjoint, meaning that they cannot interact, are strictly independent.

If the probability of event _A_ is mutually exclusive with event _B_, then the joint probability of event _A_ and event _B_ is zero.

*   P(A and B) = 0.0

Instead, the probability of an outcome can be described as event _A_ or event _B_, stated formally as follows:

*   P(A or B) = P(A) + P(B)

The “or” is also called a union and is denoted as a capital “_U_” letter; for example:

*   P(A or B) = P(A U B)

If the events are not mutually exclusive, we may be interested in the outcome of either event.

The probability of non-mutually exclusive events is calculated as the probability of event _A_ and the probability of event _B_ minus the probability of both events occurring simultaneously.

This can be stated formally as follows:

*   P(A or B) = P(A) + P(B) – P(A and B)

Further Reading
---------------

This section provides more resources on the topic if you are looking to go deeper.

### Books

*   [Probability: For the Enthusiastic Beginner](https://amzn.to/2jULJsu), 2016.
*   [Pattern Recognition and Machine Learning](https://amzn.to/2JwHE7I), 2006.
*   [Machine Learning: A Probabilistic Perspective](https://amzn.to/2xKSTCP), 2012.

### Articles

*   [Probability, Wikipedia](https://en.wikipedia.org/wiki/Probability).
*   [Notation in probability and statistics, Wikipedia](https://en.wikipedia.org/wiki/Notation_in_probability_and_statistics).
*   [Independence (probability theory), Wikipedia](https://en.wikipedia.org/wiki/Independence_(probability_theory)).
*   [Independent and identically distributed random variables, Wikipedia](https://en.wikipedia.org/wiki/Independent_and_identically_distributed_random_variables).
*   [Mutual exclusivity, Wikipedia](https://en.wikipedia.org/wiki/Mutual_exclusivity).
*   [Marginal distribution, Wikipedia](https://en.wikipedia.org/wiki/Marginal_distribution).
*   [Joint probability distribution, Wikipedia](https://en.wikipedia.org/wiki/Joint_probability_distribution).
*   [Conditional probability, Wikipedia](https://en.wikipedia.org/wiki/Conditional_probability).
