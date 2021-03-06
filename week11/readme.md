此次作业统一入口是/week11/src/main/java/homework/week11/App.java。



作业：在 Java 中实现一个简单的分布式锁

涉及到的包：homework.week11.lock。在App.java入口中的homework1()方法中。

设计思路：

1.因为redis的单线程模型保证对redis的操作不会引起并发安全问题，所以在redis上set一个指定key值（字符串dlock）来表示是否上锁。

2.为了保证锁一定被释放掉（即需要将指定key值移除），上锁/设置指定key的值时需要带有超时时间。

3.持有锁（即设置了指定key的值）的客户端需要释放锁，且在释放的时候需要判断当前锁（key）的值是否和客户端一致。此处释放锁的逻辑由lua脚本实现。

根据以上，模拟两个工人进入工作车间工作的场景，每次只能一个人进入工作车间。

以下是部分行为日志：

```
Tom -> 准备工作...
Sue -> 准备工作...
Sue -> 开始工作，将工作3秒
Tom -> 无法工作，因为当前工作车间有人，将在2秒后再尝试工作
Tom -> 准备工作...
Tom -> 无法工作，因为当前工作车间有人，将在2秒后再尝试工作
Sue -> 完成了工作
Sue -> 离开了工作车间并提交了报告
Tom -> 准备工作...
Tom -> 开始工作，将工作4秒
Sue -> 准备工作...
Sue -> 无法工作，因为当前工作车间有人，将在1秒后再尝试工作
Sue -> 准备工作...
Sue -> 无法工作，因为当前工作车间有人，将在1秒后再尝试工作
Sue -> 准备工作...
Sue -> 无法工作，因为当前工作车间有人，将在1秒后再尝试工作
Sue -> 准备工作...
Sue -> 无法工作，因为当前工作车间有人，将在1秒后再尝试工作
Tom -> 完成了工作
Tom -> 离开了工作车间并提交了报告
Sue -> 准备工作...
Sue -> 开始工作，将工作5秒
Tom -> 准备工作...
Tom -> 无法工作，因为当前工作车间有人，将在3秒后再尝试工作
Tom -> 准备工作...
Tom -> 无法工作，因为当前工作车间有人，将在3秒后再尝试工作
Sue -> 完成了工作
Sue -> 离开了工作车间并提交了报告
Sue -> 准备工作...
Sue -> 开始工作，将工作4秒
Tom -> 准备工作...
Tom -> 无法工作，因为当前工作车间有人，将在1秒后再尝试工作
Tom -> 准备工作...
Tom -> 无法工作，因为当前工作车间有人，将在3秒后再尝试工作
Sue -> 完成了工作
Sue -> 离开了工作车间并提交了报告
Sue -> 准备工作...
Sue -> 开始工作，将工作1秒
Tom -> 准备工作...
Tom -> 无法工作，因为当前工作车间有人，将在2秒后再尝试工作
Sue -> 完成了工作
Sue -> 离开了工作车间并提交了报告
Tom -> 准备工作...
Tom -> 开始工作，将工作4秒
Sue -> 准备工作...
Sue -> 无法工作，因为当前工作车间有人，将在3秒后再尝试工作
Tom -> 完成了工作
```

------

作业：在 Java 中实现一个分布式计数器，模拟减库存

涉及到的包：homework.week11.store。在App.java入口中的homework2()方法中。

设计思路：

1.如何表示库存 -> 在redis中设置一个值something，表示当前库存量

2.如何减库存 -> 利用decrby来模拟客户端减库存

3.增加lua脚本来保证库存能够被正确减去，脚本如下：

```lua
if tonumber(redis.call('get',KEYS[1])) >= tonumber(ARGV[1]) then return redis.call('decrby',KEYS[1], ARGV[1]) else return -1 end
```

根据以上思路，模拟了有三个顾客在购买商品的情况。

部分行为日志如下：

```
0号顾客  -> 计划购买2个商品
1号顾客  -> 计划购买2个商品
2号顾客  -> 计划购买4个商品
1号顾客  -> 购买成功
0号顾客  -> 购买成功
2号顾客  -> 购买成功
2号顾客  -> 计划购买5个商品
2号顾客  -> 购买成功
0号顾客  -> 计划购买7个商品
0号顾客  -> 购买成功
1号顾客  -> 计划购买2个商品
1号顾客  -> 购买成功
0号顾客  -> 计划购买2个商品
0号顾客  -> 购买成功
2号顾客  -> 计划购买4个商品
2号顾客  -> 购买成功
```

------

作业：基于 Redis 的 PubSub 实现订单异步处理

涉及到的包：homework.week11.pubsub.*。在App.java入口中的homework3()方法中。

设计思路：

1.在Spring官网上找到了对redis的发布订阅模式支持的相关类redistemplate和RedisMessageListenerContainer，利用这两个类来实现发布和订阅的行为。

2.根据redis自带的发布订阅模式，生产者负责将order序列化后的字段发布到orderChannel。消费者订阅orderChannel并且处理转换后的order。

部分日志行为如下：

Jason提交了一个订单 -> Order [用户=22, 订单号=507193, flag=0], Jason可以干其他事情去了
Tom提交了一个订单 -> Order [用户=19, 订单号=78739, flag=0], Tom可以干其他事情去了
Alin提交了一个订单 -> Order [用户=27, 订单号=216875, flag=0], Alin可以干其他事情去了
Mike提交了一个订单 -> Order [用户=26, 订单号=604715, flag=0], Mike可以干其他事情去了
Sue提交了一个订单 -> Order [用户=23, 订单号=76057, flag=0], Sue可以干其他事情去了

****************************
后台接收并处理这一个订单 -> Order [用户=22, 订单号=507193, flag=0]
****************************
后台接收并处理这一个订单 -> Order [用户=27, 订单号=216875, flag=0]
****************************
后台接收并处理这一个订单 -> Order [用户=26, 订单号=604715, flag=0]
****************************
后台接收并处理这一个订单 -> Order [用户=23, 订单号=76057, flag=0]
****************************
后台接收并处理这一个订单 -> Order [用户=19, 订单号=78739, flag=0]
****************************
Mike提交了一个订单 -> Order [用户=17, 订单号=476466, flag=0], Mike可以干其他事情去了
****************************
后台接收并处理这一个订单 -> Order [用户=17, 订单号=476466, flag=0]
****************************
Sue提交了一个订单 -> Order [用户=23, 订单号=749886, flag=0], Sue可以干其他事情去了
****************************
后台接收并处理这一个订单 -> Order [用户=23, 订单号=749886, flag=0]
****************************
Jason提交了一个订单 -> Order [用户=19, 订单号=812734, flag=0], Jason可以干其他事情去了
****************************
后台接收并处理这一个订单 -> Order [用户=19, 订单号=812734, flag=0]
****************************
Tom提交了一个订单 -> Order [用户=19, 订单号=388838, flag=0], Tom可以干其他事情去了
****************************
后台接收并处理这一个订单 -> Order [用户=19, 订单号=388838, flag=0]

