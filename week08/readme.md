**必做一和必做二都写在一个项目中。两道作业题均使用shardingSphere-jdbc**

# 必做一

## 项目计划：

####  数据分片

在shardingSphere官网指导下，带有强关联的表，共同利用一个路由策略，避免计算路由的笛卡尔积，同时在业务上强关联的记录会路由到同一个库，让强关联业务尽量使用本地事务。

#### 库：根据表主键分为两个库，对2取余。

#### 表：

-用户表：USER采取只分库的策略。根据主键对2取余。

-元数据表 单库单表，放在3306上。

-产品表：PRODUCT采取只分库的策略。根据主键对2取余。
-产品价格历史表 采取只分库的策略。根据PRODUCT_ID对2取余

-订单表  ORDER_REQUEST
-订单详情表 ORDER_REQUEST_ITEM
-订单历史表 ORDER_REQUEST_HIST
-订单业务表拆表原则：都使用order_request_id进行拆分。偶数库分为0，2，4，6，8；奇数库分为1，3，5，7，9

![3306库表](https://github.com/HGRer/XLY/tree/master/week08/image/3306_tables.png)

![3307库表](https://github.com/HGRer/XLY/tree/master/week08/image/3307_tables.png)

#### 开发思路：

水平拆分 - 数据结构不变，可拆分为多个数据库/表，每个数据库/表存取的数据集合不同。

按照此思路，在shardingShpere官网上学习到实现数据分片的知识。关键知识是理解数据节点（库+表，逻辑表与真实表的映射关系）、分片策略中的分片键和分片算法。

#### 代码例子

在APP主类中：

-**每次启动程序的时候，会为UserFactory类配置当前USER表中的最大主键（从两个库中查找）。以下是启动程序时日志记录中的Logic SQL和Actual SQL：**

```
2021-12-26 16:35:15.840  INFO 8656 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT  user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time  FROM user  ORDER BY user_id DESC limit 1
2021-12-26 16:35:15.840  INFO 8656 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional[org.apache.shardingsphere.sql.parser.sql.common.segment.dml.pagination.limit.LimitSegment@6f6f65a4], lock=Optional.empty, window=Optional.empty)
2021-12-26 16:35:15.841  INFO 8656 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: SELECT  user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time  FROM USER  ORDER BY user_id DESC limit 1
2021-12-26 16:35:15.841  INFO 8656 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: SELECT  user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time  FROM USER ORDER BY user_id DESC limit 1
```

-**查找与更新的日志**

```
2021-12-26 16:41:04.471  INFO 12672 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM user WHERE user_id=? 
2021-12-26 16:41:04.471  INFO 12672 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2021-12-26 16:41:04.471  INFO 12672 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM USER WHERE user_id=?  ::: [43]
User [userId=43, name=证困蓝, gender=2, connectType=TYPE_EMAIL, connectValue=证困蓝@163.com, address=null, birthday=1998-03-22, createTime=null, updateTime=null]
2021-12-26 16:41:04.488  INFO 12672 --- [           main] ShardingSphere-SQL                       : Logic SQL: UPDATE user  SET name=?,
gender=?,
connect_type=?,
connect_value=?,``birthday=?  WHERE user_id=?
2021-12-26 16:41:04.489  INFO 12672 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLUpdateStatement(orderBy=Optional.empty, limit=Optional.empty)
2021-12-26 16:41:04.489  INFO 12672 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: UPDATE USER  SET name=?,
gender=?,
connect_type=?,
connect_value=?,``birthday=?  WHERE user_id=? ::: [4311, 2, TYPE_EMAIL, 证困蓝@163.com, 1998-03-22, 43]
------- After update -------
2021-12-26 16:41:04.507  INFO 12672 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM user WHERE user_id=? 
2021-12-26 16:41:04.508  INFO 12672 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2021-12-26 16:41:04.508  INFO 12672 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM USER WHERE user_id=?  ::: [43]
User [userId=43, name=4311, gender=2, connectType=TYPE_EMAIL, connectValue=证困蓝@163.com, address=null, birthday=1998-03-22, createTime=null, updateTime=null]
```

-**删除**ID为88的记录

```
2021-12-26 16:45:29.915  INFO 15916 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT  user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time  FROM user ORDER BY user_id DESC limit 1
2021-12-26 16:45:29.915  INFO 15916 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional[org.apache.shardingsphere.sql.parser.sql.common.segment.dml.pagination.limit.LimitSegment@65a4b9d6], lock=Optional.empty, window=Optional.empty)
2021-12-26 16:45:29.916  INFO 15916 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: SELECT  user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time  FROM USER ORDER BY user_id DESC limit 1
2021-12-26 16:45:29.916  INFO 15916 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: SELECT  user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time  FROM USER ORDER BY user_id DESC limit 1
2021-12-26 16:45:30.497  INFO 15916 --- [           main] homework.week8.first.App                 : Started App in 6.224 seconds (JVM running for 6.758)
2021-12-26 16:45:30.612  INFO 15916 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM user WHERE user_id=? 
2021-12-26 16:45:30.612  INFO 15916 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2021-12-26 16:45:30.612  INFO 15916 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM USER WHERE user_id=?  ::: [88]
User [userId=88, name=皖盂翼, gender=2, connectType=TYPE_PHONE, connectValue=10231134557, address=null, birthday=1999-12-27, createTime=null, updateTime=null]
2021-12-26 16:45:30.624  INFO 15916 --- [           main] ShardingSphere-SQL                       : Logic SQL: DELETE FROM user WHERE user_id=?
2021-12-26 16:45:30.624  INFO 15916 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLDeleteStatement(orderBy=Optional.empty, limit=Optional.empty)
2021-12-26 16:45:30.624  INFO 15916 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: DELETE FROM USER WHERE user_id=? ::: [88]
2021-12-26 16:45:30.638  INFO 15916 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM user WHERE user_id=? 
2021-12-26 16:45:30.638  INFO 15916 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2021-12-26 16:45:30.638  INFO 15916 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM USER WHERE user_id=?  ::: [88]
null
```

-**插入的日志**：

```
2021-12-26 16:53:24.024  INFO 12728 --- [           main] homework.week8.first.App                 : Started App in 6.229 seconds (JVM running for 6.77)
current user -> User [userId=103, name=鞠鞭耸, gender=0, connectType=TYPE_EMAIL, connectValue=鞠鞭耸@163.com, address=null, birthday=1982-11-04, createTime=null, updateTime=null]
2021-12-26 16:53:24.152  INFO 12728 --- [           main] ShardingSphere-SQL                       : Logic SQL: INSERT INTO user  ( user_id,
name,gender,connect_type,connect_value,birthday )  VALUES  ( ?,?,?,?,?,? )
2021-12-26 16:53:24.153  INFO 12728 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLInsertStatement(setAssignment=Optional.empty, onDuplicateKeyColumns=Optional.empty)
2021-12-26 16:53:24.153  INFO 12728 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: INSERT INTO USER  ( user_id,
name,gender,connect_type,connect_value,birthday )  VALUES  (?, ?, ?, ?, ?, ?) ::: [103, 鞠鞭耸, 0, TYPE_EMAIL, 鞠鞭耸@163.com, 1982-11-04]
2021-12-26 16:53:24.191  INFO 12728 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM user WHERE user_id=? 
2021-12-26 16:53:24.191  INFO 12728 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2021-12-26 16:53:24.191  INFO 12728 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM USER WHERE user_id=?  ::: [103]
-----after insert -> User [userId=103, name=鞠鞭耸, gender=0, connectType=TYPE_EMAIL, connectValue=鞠鞭耸@163.com, address=null, birthday=1982-11-04, createTime=null, updateTime=null]
```

# 必做二

在homework.week8.first.service.UserServiceImpl类中简单展现跨库更新的操作。要更新的记录id分别为55，56，业务上来这两个记录需要将name字段更新为一模一样的。

在没有配置事务的情况下跨库更新的日志：

```
2021-12-26 17:05:18.338  INFO 15116 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM user WHERE user_id=? 
2021-12-26 17:05:18.338  INFO 15116 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2021-12-26 17:05:18.338  INFO 15116 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM USER WHERE user_id=?  ::: [55]
2021-12-26 17:05:18.342  INFO 15116 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM user WHERE user_id=? 
2021-12-26 17:05:18.342  INFO 15116 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2021-12-26 17:05:18.342  INFO 15116 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM USER WHERE user_id=?  ::: [56]
user55 ->User [userId=55, name=aaaaaaaaaaaaaaa, gender=2, connectType=TYPE_EMAIL, connectValue=筋舌谋@163.com, address=null, birthday=2000-09-16, createTime=null, updateTime=null]
user56 ->User [userId=56, name=aaaaaaaaaaaaaaa, gender=0, connectType=TYPE_PHONE, connectValue=12911070071, address=null, birthday=2011-01-10, createTime=null, updateTime=null]
2021-12-26 17:05:18.367  INFO 15116 --- [           main] ShardingSphere-SQL                       : Logic SQL: update user set name = 'bbbbbbb' where user_id in (55) 
2021-12-26 17:05:18.367  INFO 15116 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLUpdateStatement(orderBy=Optional.empty, limit=Optional.empty)
2021-12-26 17:05:18.367  INFO 15116 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: update USER set name = 'bbbbbbb' where user_id in (55) 
调用updateWithXA()报错了
经过了更新之后
2021-12-26 17:05:18.378  INFO 15116 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM user WHERE user_id=? 
2021-12-26 17:05:18.378  INFO 15116 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2021-12-26 17:05:18.378  INFO 15116 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM USER WHERE user_id=?  ::: [55]
2021-12-26 17:05:18.381  INFO 15116 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM user WHERE user_id=? 
2021-12-26 17:05:18.381  INFO 15116 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2021-12-26 17:05:18.381  INFO 15116 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM USER WHERE user_id=?  ::: [56]
user55 ->User [userId=55, name=bbbbbbb, gender=2, connectType=TYPE_EMAIL, connectValue=筋舌谋@163.com, address=null, birthday=2000-09-16, createTime=null, updateTime=null]
user56 ->User [userId=56, name=aaaaaaaaaaaaaaa, gender=0, connectType=TYPE_PHONE, connectValue=12911070071, address=null, birthday=2011-01-10, createTime=null, updateTime=null]
```

在启动XA事务下的日志：

```
user55 ->User [userId=55, name=bbbbbbb, gender=2, connectType=TYPE_EMAIL, connectValue=筋舌谋@163.com, address=null, birthday=2000-09-16, createTime=null, updateTime=null]
user56 ->User [userId=56, name=bbbbbbb, gender=0, connectType=TYPE_PHONE, connectValue=12911070071, address=null, birthday=2011-01-10, createTime=null, updateTime=null]
2021-12-26 17:10:17.978  INFO 12980 --- [           main] ShardingSphere-SQL                       : Logic SQL: update user set name = 'XXXXXAAAAA' where user_id in (55) 
2021-12-26 17:10:17.978  INFO 12980 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLUpdateStatement(orderBy=Optional.empty, limit=Optional.empty)
2021-12-26 17:10:17.978  INFO 12980 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: update USER set name = 'XXXXXAAAAA' where user_id in (55) 
调用updateWithXA()报错了
经过了更新之后
2021-12-26 17:10:17.999  INFO 12980 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM user WHERE user_id=? 
2021-12-26 17:10:18.000  INFO 12980 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2021-12-26 17:10:18.000  INFO 12980 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds1 ::: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM USER WHERE user_id=?  ::: [55]
2021-12-26 17:10:18.003  INFO 12980 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM user WHERE user_id=? 
2021-12-26 17:10:18.003  INFO 12980 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2021-12-26 17:10:18.003  INFO 12980 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: SELECT user_id,name,gender,connect_type,connect_value,address,birthday,create_time,update_time FROM USER WHERE user_id=?  ::: [56]
user55 ->User [userId=55, name=bbbbbbb, gender=2, connectType=TYPE_EMAIL, connectValue=筋舌谋@163.com, address=null, birthday=2000-09-16, createTime=null, updateTime=null]
user56 ->User [userId=56, name=bbbbbbb, gender=0, connectType=TYPE_PHONE, connectValue=12911070071, address=null, birthday=2011-01-10, createTime=null, updateTime=null]
```

