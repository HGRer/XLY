package homework.week7;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import homework.week7.entity.User;
@Component
public class UserFactory implements FactoryBean<User> {
	
	private static final String[] connectTypeArray = new String[] {"TYPE_EMAIL", "TYPE_PHONE"};
	private static final AtomicLong id = new AtomicLong(1);
	
	@Override
	public User getObject() throws Exception {
		return createUser();
	}

	@Override
	public Class<User> getObjectType() {
		return User.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
	
	private User createUser() {
		User u = new User();
		u.setUserId(id.getAndIncrement());
		u.setName(getName(3));
		u.setGender(gender());
		u.setBirthday(getBirth());
		setConnectTypeAndValue(u);
		return u;
	}
	
	private Date getBirth() {
		Long randomMillis = DateUtils.MILLIS_PER_DAY * RandomUtils.nextInt(365 * 10, 365 * 40);
		Long birthMillis = System.currentTimeMillis() - randomMillis;
		return new Date(birthMillis);
	}
	
	private String gender() {
		return String.valueOf(RandomUtils.nextInt(0, 3));
	}
	
	private void setConnectTypeAndValue(User u) {
		int index = RandomUtils.nextInt(0, 2);
		u.setConnectType(connectTypeArray[index]);
		u.setConnectValue(index == 0 ? u.getName() + "@163.com" : String.valueOf(RandomUtils.nextLong(10000000000l, 20000000000l)));
	}
	
	private String getName(int len) {
        String ret = "";
        for (int i = 0; i < len; i++) {
            String str = null;
            int hightPos, lowPos; // 定义高低位
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39))); // 获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93))); // 获取低位值
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try {
                str = new String(b, "GBK"); // 转成中文
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            ret += str;
        }
        return ret;
    }
}
