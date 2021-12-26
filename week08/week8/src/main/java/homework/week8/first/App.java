package homework.week8.first;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import homework.week8.first.entity.User;
import homework.week8.first.entity.factory.UserFactory;
import homework.week8.first.mapper.UserMapper;
import homework.week8.first.service.UserServiceImpl;

/**
 * @author hgggr
 *
 */
@SpringBootApplication
@MapperScan
public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
    	UserMapper userMapper = context.getBean(UserMapper.class);
    	UserFactory uf = context.getBean(UserFactory.class);
    	// 插入
    	User u = uf.getObject();
    	System.out.println("current user -> " + u);
    	userMapper.insert(u);
    	System.out.println("-----after insert -> " + userMapper.selectById(u.getUserId()));
    	
    	// 查找与更新
    	User user43 = userMapper.selectById(Long.valueOf("43"));
    	System.out.println(user43);
    	user43.setName("4311");
    	userMapper.updateById(user43);
    	System.out.println("------- After update -------");
    	user43 = userMapper.selectById(Long.valueOf("43"));
    	System.out.println(user43);
    	
    	// 删除
    	User user88 = userMapper.selectById(Long.valueOf("88"));
    	System.out.println(user88);
    	userMapper.deleteById(user88);
    	user88 = userMapper.selectById(Long.valueOf("88"));
    	System.out.println(user88);
    	
    	User user55 = userMapper.selectById(Long.valueOf(55));
    	User user56 = userMapper.selectById(Long.valueOf(56));
    	System.out.println("user55 ->" + user55);
    	System.out.println("user56 ->" + user56);
    	UserServiceImpl ds = context.getBean(UserServiceImpl.class);
    	try {
    		ds.updateWithXA();
    	} catch(Exception e) {
    		System.out.println("调用updateWithXA()报错了");
    	}
    	System.out.println("经过了更新之后");
    	user55 = userMapper.selectById(Long.valueOf(55));
    	user56 = userMapper.selectById(Long.valueOf(56));
    	System.out.println("user55 ->" + user55);
    	System.out.println("user56 ->" + user56);
    }
}