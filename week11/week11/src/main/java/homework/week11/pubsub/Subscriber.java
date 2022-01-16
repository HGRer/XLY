package homework.week11.pubsub;

import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import homework.week11.pubsub.entity.Order;
import homework.week11.pubsub.json.JsonUtil;

@Configuration
public class Subscriber {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory factory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(factory);
		container.setTaskExecutor(Executors.newFixedThreadPool(3));
		container.addMessageListener((Message message, byte[] pattern) -> {
			Order order = JsonUtil.jsonToOrder(new String(message.getBody()));
			System.out.println("****************************");
			System.out.println("后台接收并处理这一个订单 -> " + order);
			System.out.println("****************************");
		}, new ChannelTopic("orderChannel"));
		
		return container;
	}
}
