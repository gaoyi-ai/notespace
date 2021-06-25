---
title: Trigger Case
categories:
- DB 
tags:
- Trigger
date: 2021/6/25
---



# trigger case

```sql
CREATE TABLE car_rentals(
	plate varchar(10) NOT NULL,
	start_date date NOT NULL, 
	end_date date NOT NULL, 
	license_nr varchar(10) NOT NULL, 
	CONSTRAINT unq_car_rentals_start UNIQUE (plate, start_date),
	CONSTRAINT unq_car_rentals_end UNIQUE (plate, end_date)
);
```

While the unique constraints deﬁned here are sensible, they are not suﬃcient to express the constraint that a car (identiﬁed by its plate) cannot be rented more than once on any particular day.

## 问题

Write a stored procedure (function in postgres) which takes as input a plate, start date and end date and throws an error if table car rental contains any row with the same plate where the rental period is diﬀerent from but overlaps with the given period.

## 分析

这个需求就是判断是否有冲突，所以并不需要进行 INSERT 或者 UPDATE 等操作，仅仅是如果找到了有冲突的租赁记录就抛个异常消息而已，可以通过 IF EXIST SELECT × FROM ... WHERE ...这种结构来实现，但是特别注意这个提示：where the rental period is different from but overlaps with the given period.，即排除掉租期起止完全相同的记录之后存在满足冲突的记录，这个要求其实是和 下一问 联系在一起的，具体解释见 下一问。

Note: By checking only for diﬀerent rental periods we don’t need to worry about the newly inserted or updated tuple using the procedure in a trigger (see next question). Duplicate periods are already prevented by the uniqueness constraints speciﬁed.

Answer: A possible implementation in Postgres is below:

```sql
CREATE FUNCTION check_conflict_rentals(
	IN i_plate varchar(10), IN i_start_date date, IN i_end_date date)
	RETURNS void AS $$
BEGIN
	IF EXISTS (SELECT 1 FROM car_rentals
		WHERE plate = i_plate
			AND start_date <= i_end_date
			AND end_date >= i_start_date
			AND (start_date <> i_start_date
				OR end_date <> i_end_date)
    ) THEN
		RAISE unique_violation USING HINT = ’duplicate rental’;
	END IF;
END;
$$ LANGUAGE plpgsql;
```

## 问题

Create triggers which invoke the stored procedure created to enforce the “no duplicate rentals” constraint. 

## 分析

分别为 INSERT 和 UPDATE 行为创建触发器调用该触发器函数，其中 INSERT 行为时机应该设置为 BEFORE ，这个好理解，在插入之前判定下有没有冲突的记录，但 UPDATE 行为则需要设置为 AFTER ，这是因为：

如果是 UPDATE 之前从所有记录集合里找有没有冲突的记录，必然也包括要更改的这一条记录，而如果你更改的这条记录和要更改成的日期恰好冲突，那这条记录就更改成功不了了，实际情景可能是，因为某些原因，我要把我的租约延后一天，但是因为和我原来的租约冲突，所以更改不了，这很荒谬，所以应该是在更改完了之后，我看更改后的记录和其他记录冲突不冲突才对，但是更改之后，由于日期已经更新过了，所以记录集里这条记录和传递进来的 `i_plate` / `i_start_date` / `i_end_date` 完全相同，即100%有冲突，怎么办呢，在检查冲突时，把这条记录排除掉即可，即从排除掉这条记录后的其它记录集合里去找是否有冲突的，没有冲突说明这个 UPDATE 是可行的。这就是为什么 上一问 里在编写条件时，需要 `AND` 上一个 `NOT (plate = i_plate AND start_date = i_start_date AND end_date = i_end_date )` 。

验证的话，用后边那堆 DML 自行尝试做到和给的结果一致就说明应该对了，其中比较有疑问的是第二个 UPDATE 语句，一般说来，只对符合特定条件的才 UPDATE ，所以一般是带有 WHERE 条件的，但强行都 UPDATE 也不是不可以，由于本例中前边只 INSERT 了一个记录，所以这个不带 WHERE 的记录也会更新成功，这里边的一个误区就是，不要认为我更新的这个时间和数据库里那个记录时间是冲突的，应该是抛异常才对，错了，那条记录本来就是你要更新的，应该是和其他记录比较看看有没有冲突才对，而不是和你要更新的这条记录比较，所以前边 第一问 里那个条件就尤为重要了。

再补充一点，之所以在 第二问 的触发器函数中封装 第一问 写的存储过程而不是单独写语句实现相同的功能，我想主要还是代码复用，也就是说，很多地方其实都会用到这个冲突校验功能的，编写成存储过程使用起来更方便些。

上面这个 Note 的解读如下：

Note: By checking only for different rental periods we don’t need to worry about the newly inserted or updated tuple using the procedure in a trigger (see next question). Duplicate periods are already prevented by the uniqueness constraints specified.

刚才提到，`pd_check_conflict_rental` 中为了避免 UPDATE 时无法更改记录，改为了 AFTER 后触发，又引出了 100% 会冲突问题，所以利用 `NOT (plate = i_plate AND start_date = i_start_date AND end_date = i_end_date )` 这个 WHERE 条件限定只在与其不同的其它记录中校验是否冲突，从而解决了这个问题，但这其实也会引入新问题，例如：我想插入一个新租赁记录，恰巧，已经有人用同样的时间对同一车辆进行了租赁登记，但由于该存储过程检查时会忽略这个记录（因为 `plate` / `start_date` / `end_date` 都一样），其它记录又确实没有冲突，所以并没有抛出错误，难道这个新记录就给写进去了吗？显然不能，否则车行就要打架了，因为同一辆车同样的时间被租赁了两次甚至多次，谁阻止了呢，就是 `CREATE TABLE` 时的

`CONSTRAINT unq_car_rentals_start UNIQUE (plate, start_date),`
`CONSTRAINT unq_car_rentals_end UNIQUE (plate, end_date)`

这两个约束是不允许同一车辆同一时间被租赁多次的，所以才有 By checking only for different rental periods we don’t need to worry about the newly inserted or updated tuple 。

另：下面代码中的 `tf_conflict_unique_rentals()` 改为 `tf_conflict_rentals()` 可读性比较好些。

Create a separate trigger function that meets these requirements and invokes the function deﬁned earlier. Note: You can test that your triggers work by inserting and updating tuples in car rentals:

• INSERT INTO car rentals VALUES (’2-F4ST’, ’2015-02-02’, ’2015-02-11’, ’DI123’) 
 →should succeed
• UPDATE car rentals SET start date = ’2015-02-01’, end date = ’2015-02-10’ 
 →should succeed
• INSERT INTO car rentals VALUES (’SP33DY’, ’2015-01-20’, ’2015-02-05’, ’DI234’) 
 →should succeed
• UPDATE car rentals SET plate = ’2-F4ST’ WHERE plate = ’SP33DY’ 
 →should fail
• INSERT INTO car rentals VALUES (’2-F4ST’, ’2015-02-10’, ’2015-02-15’, ’DI234’) 
 →should fail
• INSERT INTO car rentals VALUES (’2-F4ST’, ’2015-01-20’, ’2015-02-15’, ’DI234’) 
 →should fail
• INSERT INTO car rentals VALUES (’2-F4ST’, ’2015-02-02’, ’2015-02-09’, ’DI234’) 
 →should fail
• INSERT INTO car rentals VALUES (’2-F4ST’, ’2015-03-01’, ’2015-03-10’, ’DI234’) 
 →should succeed

Answer:

```sql
CREATE FUNCTION tf_check_rentals() RETURNS trigger AS $$
BEGIN
	PERFORM check_conflict_rentals(NEW.plate, NEW.start_date, NEW.end_date);
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trig_rentals_insert
BEFORE INSERT ON car_rentals FOR EACH ROW
EXECUTE PROCEDURE tf_check_rentals();

CREATE TRIGGER trig_rentals_update
AFTER UPDATE ON car_rentals FOR EACH ROW
EXECUTE PROCEDURE tf_check_rentals();
```

