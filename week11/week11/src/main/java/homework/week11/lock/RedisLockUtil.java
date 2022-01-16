package homework.week11.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

@Component
public class RedisLockUtil {
	private final String LOCK_NAME = "dlock";
	private final UnlockScript unlockScript = new UnlockScript();
	volatile private String currentValue = null;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	public synchronized boolean lock(String value) {
		currentValue = value;
		return redisTemplate.opsForValue().setIfAbsent(LOCK_NAME, currentValue, 20, TimeUnit.SECONDS);
	}
	
	public synchronized boolean unlock(String value) {
		if (value == null) {
			throw new RuntimeException("no lock value");
		}
		currentValue = value;
		List<String> list = new ArrayList<String>(1);
		list.add(LOCK_NAME);
		String result = String.valueOf(redisTemplate.execute(unlockScript, list, currentValue));
		if (StringUtils.isNotBlank(result) && "1".equals(result)) {
			return true;
		} 
		return false;
	}
	
	static class UnlockScript implements RedisScript<Long> {
		@Override
		public String getSha1() {
			return "ae3671744a5dbb24ea37ef607b8b10ac7856d43e";
		}

		@Override
		public Class<Long> getResultType() {
			return Long.class;
		}

		@Override
		public String getScriptAsString() {
			return null;
		}
	}
}
