---
title: Lazy Initialization
categories:
- Pattern
- Singleton
tags:
- lazy initialize
- thread safe
date: 2021/1/9 20:00:17
updated: 2021/1/10 12:00:17
---



# Best Practices for Lazy Initialization

**Q**: What's most important to understand about best practices for lazy initialization?

**A**: The single most important piece of advice is "Don't do it unless you need to." The great majority of your initialization code should look like this:

```java
// Normal initialization, not lazy!
private final FieldType field = computeFieldValue();
```

If you need lazy initialization for correctness -- but not for performance -- just use a synchronized accessor. It's simple and clearly correct.

If you need better performance, your best choice depends on whether you're initializing a static field or an instance field. If it's a static field, use the lazy initialization holder class idiom:

```java
// Lazy initialization holder class idiom for static fields
private static class FieldHolder {
     static final FieldType field = computeFieldValue();
}
static FieldType getField() { return FieldHolder.field; }
```

This idiom is almost magical. There's synchronization going on, but it's invisible. The Java Runtime Environment does it for you, behind the scenes. And many VMs actually patch the code to eliminate the synchronization once it's no longer necessary, so this idiom is extremely fast.

If you need high-performance lazy initializing of an instance field, use the double-check idiom with a volatile field. This idiom wasn't guaranteed to work until release 5.0, when the platform got a new memory model. The idiom is very fast but also complicated and delicate, so don't be tempted to modify it in any way. Just copy and paste -- normally not a good idea, but appropriate here:

```java
// Double-check idiom for lazy initialization of instance fields.
private volatile FieldType field;
FieldType getField() {
    FieldType result = field;
    if (result == null) { // First check (no locking)
        synchronized(this) {
            result = field;
            if (result == null) // Second check (with locking)
                field = result = computeFieldValue();
        }
    }
     return result;
}
```