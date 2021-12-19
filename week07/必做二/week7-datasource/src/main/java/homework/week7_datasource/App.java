package homework.week7_datasource;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import homework.week7_datasource.entity.T1;

@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
    	ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
    	
    	SerivecImpl serviceImpl = context.getBean(SerivecImpl.class);
    	
    	serviceImpl.selectAndShow();
    	
    	T1 t1 = new T1();
    	t1.setId(RandomUtils.nextInt(100, 90000));
    	System.out.println("current id -> " + t1.getId() + " , insert into db");
    	serviceImpl.insert(t1);
    	
    	System.out.println("----After inserting-----");
    	try {
    		// 偶尔会读写不同步
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	serviceImpl.selectAndShow();
    }
}
