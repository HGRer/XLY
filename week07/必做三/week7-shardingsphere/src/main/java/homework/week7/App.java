package homework.week7;

import java.util.Random;

import org.apache.commons.lang3.RandomUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import homework.week7.entity.T1;
import homework.week7.service.ServiceImpl;

@SpringBootApplication
@MapperScan
public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
    	ServiceImpl serviceImpl = context.getBean(ServiceImpl.class);
    	
    	serviceImpl.selectAndShow();
    	
    	T1 t = new T1();
    	t.setId(RandomUtils.nextInt(1234, 565437));
    	System.out.println("current id -> " + t.getId());
    	serviceImpl.insert(t);
    	
    	System.out.println("--------------");
//    	Thread.sleep(500);
    	serviceImpl.selectAndShow(); // 读写分离，但是仍然存在写后，从库读取不到的情况
    }
}
