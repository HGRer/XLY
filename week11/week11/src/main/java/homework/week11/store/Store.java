package homework.week11.store;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

@Component
public class Store {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	public boolean buySomething(int num) {
		List<String> list = new ArrayList<String>(1);
		list.add("something");
		if (-1 == redisTemplate.execute(new StoreScript(), list, String.valueOf(num))) {
			return false;
		} else {
			return true;
		}
	}
	
	class StoreScript implements RedisScript<Long> {
		@Override
		public String getSha1() {
			return "d30b6981c095d72e749ceb83bf7c0ef46e7cd73a";
		}

		@Override
		public Class<Long> getResultType() {
			return Long.class;
		}

		@Override
		public String getScriptAsString() {
			// "if tonumber(redis.call('get',KEYS[1])) >= tonumber(ARGV[1]) then return redis.call('decrby',KEYS[1], ARGV[1]) else return -1 end"
			return null;
		}
	}
}
