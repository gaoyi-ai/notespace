# What is Reactive Programming?

I enjoyed reading this question on Stack Overflow: [What is (functional) reactive programming](http://stackoverflow.com/questions/1028250)? I thought I'd take a stab at an explanation of what reactive programming means to me. Let me take you to the future.

The year is 2051, and our team of code anthropologists discovered some ancient script in [the long-abandoned ruins of a decaying open source project repository](http://sourceforge.net/). With some deciphering, we discover the the script is written in a primitive dialect of P#, the dominant language of the year 2051. It reads:

```
var a = 10;
var b = a + 1;
a = 11;
b = a + 1;
```

Through careful analysis of the script, we eventually decipher this to be an ancient parable. It tells the story of two brothers. **a** is the first born, proud and independent. **b** is the younger of the two, but it is dependent; it needs **a**. But the relationship is short lived. Once the second line is executed, and **b** grows up, the relationship between the adventurers ends - **b** is no longer dependent on **a**. When **a** changes, **b** does not. **a** must attempt to re-establish the once-lost relationship, but even that does not last.

We can relate to this as if it were the code version of [Cats in the Cradle](http://www.birdsnest.com/catcrad.htm). First there is **a**, and **b** who needs **a**. But by the third line, **b** has grown up, and has no time for **a**. It is only when **a** calls that the two are momentarily re-connected, but even that is only fleeting.

Thanks to these cave paintings, we can draw the conclusion that the primitive compilers weren't smart enough to figure out that the destinies of these two proud brothers were intertwined. It seems that ancient programmers had to continually re-establish the relationship, or risk data being out of sync. As modern day code anthropologists, it's hard to imagine how it might have felt to write code like this.

In the year 2051, reactive programming is the norm. Language creators discovered the **destiny operator** decades ago, and the old ways were quickly forgotten. For example, in P#, we can write:

```
var a = 10;
var b <= a + 1;
a = 20;
Assert.AreEqual(21, b);
```

As you can see, the statement establishes **b** and **a** as having intertwined destinies, which are unbroken and forever. They are **bound**. The relationship between them isn't implicit, an idea that only exists in the mind of the programmers; it's explicit, a part of the language, and it exists for all time.

Although the **destiny operator** is wide spread, the way it works is a closely guarded secret. Some say that when the compiler encounters code that changes **a**, it inserts the corresponding change for **b**, such that they are always in sync. Others say that **a**, instead of being a lowly 4-byte integer, is ascended into a higher plane of existence. It becomes an **observable**, an object whose changes reverberate throughout the software at runtime, with the aid of event handlers created by the compiler. Old wives tales even tell of a great timer that constantly ticks, re-aligning all the variables after every change.

Nevertheless, through understanding the parable of the dependent brothers and getting a glimpse into the ways of ancient programmers, we can be even more thankful for the **destiny operator** and reactive programming. Instead of slaving over repetitive code and dealing with bugs, modern programmers can express relationships that last forever.