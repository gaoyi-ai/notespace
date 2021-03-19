---
title: Specialization,Generalization,Aggregation
categories:
- DB
tags:
- Database Design
date: 2021/3/18 20:00:17
updated: 2021/3/18 12:00:17
---

Specialization, Generalization and Aggregation are essential techniques or processes in ER modeling. To reuse entities, special relationships of super class and subclass are defined and  abstraction is implemented among entities.

Specialization
--------------

It’s a Top Down approach to design ER model where subclasses of entity types are defined that inherits attributes of another entity type. The entity type which passes on attributes to other entities is called the super class of the specialization. The set of subclasses that form a specialization is defined on the basis of some distinguishing characteristic of the entities in the super class. In other words, Specialization is the process of classifying class of objects into more specialized subclasses. It is a conceptual refinement. 

Example: 
Let us understand specialization with the help of an ER-diagram. 
![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/Generalization.png)

In the above example **Person** is an entity type with three attributes Name, Age, Phone. A person in an educational institute can be **Employee** or **Student**. Employee Entity has an additional attribute Employee ID.  Student entity has two additional attributes Student ID and Course. Employee entity Type is further specialized in two entities Faculty and Administrative. Faculty entity type has attribute Department and Administrative entity has attribute Designation.

In Specialization and Generalization hierarchy, Person is the Super class and Employee and Student are the sub classes of Person. Faculty and Administrative are Sub classes of Employee entity type. Sub Classes (entities) inherit the attributes of Super Class and add some attributes of their own.

While converting an ER diagram into tables we write respective schemas. But schemas against all entities are not formed.  Schemas are formed against only leaf entities. We do not write schema for Person entity because Person **_is an_** employee or a Student. Further employee **_is a_** Faculty or Administrative. So, we make schemas of only Student, Faculty and Administrative entity types.

**Schemas:**

Student (Name, Age, Phone, Student ID)

Faculty (Name, Age, Phone, Employee ID, Department)

Administrative (Name, Age, Phone, Employee ID, Designation)

Here, Student schema inherits attributes of Person. Faculty and Administrative entities inherit the attributes of Person and Employee.

Generalization
--------------

Generalization is the reverse of specialization. In this process differences among multiple entity types are suppressed. Their common features are identified and then generalized into single super class of which the original entity types are special subclasses.

Generalization is a conceptual synthesis. Bottom up approach is used in this process since we start with special entity types. Their common attributes are used to define a higher level entity type. This process is repeated till we get one super entity type at the top level

Example:

We start with two entity types

Employee (Emp_id, Name, Address, Contact no)

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/Specialization-Employee.png)

Customer (Cust id, Name, Address, Contact no).

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/Specialization-Customer.png)

These two are special entities types. We can merge these two entities into one entity type named Person. We have made these two special entities general. In this process common attributes are included in the general entity.

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/Specialization-Customer.png)

Aggregation
-----------

Aggregation is an abstraction through which we can represent relationships as a higher level entity. It is an abstraction concept for building composite objects from their component objects. There is a limitation in ER modeling that we don’t have a way to represent relationship among relationships. To overcome this limitation aggregation is the solution.

Example:

Suppose in an organization the Manager entity manages set of employee in a department working on specific projects.

Employee, Department and Projects are entity types having a ternary relationship called **Works On**. Manager manages employee, manager also manages department and manager also manages Projects. It is difficult to represent this complicated relationship representation in ER diagram. It will be quite complicated to involve entity types into a relationship when they already are participating in a relationship.

Aggregation is used to solve such complication representations. In aggregation we represent same Employee, Department and Project entities and their relationship as a higher level entity by aggregating. Here we have another entity Manager who creates a relationship with this higher level entity.

![](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/Aggregation.png)