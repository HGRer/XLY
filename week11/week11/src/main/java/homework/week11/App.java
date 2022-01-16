package homework.week11;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import homework.week11.lock.RedisLockUtil;
import homework.week11.pubsub.Publisher;
import homework.week11.store.Store;

@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
    	ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
    	
//    	homework1(context);
//    	homework2(context);
    	homework3(context);

    }
    
    static void homework3(ConfigurableApplicationContext context) {
    	Publisher p1 = (Publisher) context.getBean("createPublisher");
    	Publisher p2 = (Publisher) context.getBean("createPublisher");
    	Publisher p3 = (Publisher) context.getBean("createPublisher");
    	Publisher p4 = (Publisher) context.getBean("createPublisher");
    	Publisher p5 = (Publisher) context.getBean("createPublisher");
    	p1.setName("Tom");
    	p2.setName("Sue");
    	p3.setName("Mike");
    	p4.setName("Jason");
    	p5.setName("Alin");
    	
    	p1.start();
    	p2.start();
    	p3.start();
    	p4.start();
    	p5.start();
    }
    
    static void homework2(ConfigurableApplicationContext context) {
    	Store store = context.getBean(Store.class);
    	
    	for (int i = 0; i < 3; i++) {
    		Thread thread = new Thread(() -> {
    			while(true) {
        			try {
    					Thread.sleep(RandomUtils.nextLong(100, 1000));
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
        			
        			int num = RandomUtils.nextInt(1, 10);
        			System.out.println(Thread.currentThread().getName() 
        					+ "  -> 计划购买" + num + "个商品");
    				if (store.buySomething(num)) {
    	    			System.out.println(Thread.currentThread().getName() 
    	    					+ "  -> 购买成功");
    				} else {
    	    			System.out.println(Thread.currentThread().getName() 
    	    					+ "  -> 购买" + num + "个商品失败，库存不足！！！");
    				}
    				
        			try {
    					Thread.sleep(RandomUtils.nextLong(2000, 5000));
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    			}
    		});
    		//
    		thread.setName(i + "号顾客");
    		thread.start();
    	}
    }
    
    static void homework1(ConfigurableApplicationContext context) {
    	RedisLockUtil redisLockUtil = context.getBean(RedisLockUtil.class);
    	WorkThread tom = new WorkThread("Tom", redisLockUtil);
    	WorkThread sue = new WorkThread("Sue", redisLockUtil);
    	
    	ExecutorService es = Executors.newFixedThreadPool(2);
    	es.execute(tom);
    	es.execute(sue);
    }
    
    private static class WorkThread extends Thread {
    	private String name;
    	private RedisLockUtil redisLockUtil;
    	
    	public WorkThread(String name, RedisLockUtil redisLockUtil) {
    		this.name = name;
    		this.redisLockUtil = redisLockUtil;
    	}
    	
    	@Override
		public void run() {
			while(true) {
				work();
			}
		}

		public void work() {
    		System.out.println(name + " -> 准备工作...");
    		String lockValue = String.valueOf(System.currentTimeMillis()
    				+ RandomUtils.nextLong(123456l, 128734987l));
    		if (redisLockUtil.lock(lockValue)) {
    			int second = RandomUtils.nextInt(1, 6);
    			System.out.println(name + " -)> 开始工作，将工作" + second + "秒");
        		try {
    				Thread.sleep(second * 1000);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
        		
        		System.out.println(name + " -> 完成了工作");
        		if (redisLockUtil.unlock(lockValue)) {
        			System.out.println(name + " -> 离开了工作车间并提交了报告");
        		} else {
        			System.out.println(name + " -> 离开了工作车间单无提交报告，因为超过指定时间了....");
        		}
        		
        		try {
        			// 休息
    				Thread.sleep(RandomUtils.nextInt(1000, 3000));
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		} else {
    			int sleepSecond = RandomUtils.nextInt(1, 4);
    			System.out.println(name + " -> 无法工作，因为当前工作车间有人，将在" 
    					+ sleepSecond + "秒后再尝试工作");
    			
        		try {
    				Thread.sleep(sleepSecond * 1000);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    }
}
