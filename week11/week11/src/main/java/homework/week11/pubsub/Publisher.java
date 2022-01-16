package homework.week11.pubsub;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;

import homework.week11.pubsub.entity.Order;
import homework.week11.pubsub.json.JsonUtil;

@Configuration
public class Publisher extends Thread {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Bean
	@Scope("prototype")
	public Publisher createPublisher() {
		return new Publisher();
	}
	
	@Override
	public void run() {
		while(true) {
			Order order = new Order();
			order.setUserId(RandomUtils.nextInt(10, 30));
			order.setOrderId(RandomUtils.nextInt(100, 999999));
			redisTemplate.convertAndSend("orderChannel",JsonUtil.orderToJson(order));
			System.out.println(this.getName() + "提交了一个订单 -> " + order + ", "
					+ this.getName() + "可以干其他事情去了");
			try {
				Thread.sleep(RandomUtils.nextInt(5, 10) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
