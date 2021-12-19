package homework.week7;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import homework.week7.entity.User;
import homework.week7.mapper.UserMapper;
import homework.week7.service.UserServiceImpl;

@SpringBootApplication
@MapperScan
public class App {
	public static void main(String[] args) throws SQLException {
		ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
//		insertVersionOne(context);
//		insertVersionTwo(context);
//		insertVersionThree(context);
//		insertVersionFour(context);
//		insertVersionFive(context);
//		insertVersionSix(context);
//		insertVersionSeven(context);
//		insertVersionEight(context);
//		insertVersionNine(context);
		insertVersionTen(context);
	}
	
	/**
	 * 6秒左右
	 * @param context
	 * @throws SQLException
	 */
	static void insertVersionTen(ConfigurableApplicationContext context) throws SQLException {
		JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
		try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {
			String sql = "load data infile 'C:/Users/hgggr/Desktop/userinfo1.csv'\ninto table user\nCHARACTER SET utf8\n"
					+ "FIELDS TERMINATED BY ','\nOPTIONALLY ENCLOSED BY '\"'\n"
					+ "ESCAPED BY '\\\\'\n"
					+ "(user_id,connect_type,connect_value,address,birthday,update_time,name,gender,create_time)";
			conn.setAutoCommit(false);
			long start = System.currentTimeMillis();
			conn.prepareStatement(sql).execute();
			conn.commit();
			System.out.println("cost -> " + (System.currentTimeMillis() - start) + "ms");
		}
	}
	
	/**
	 * 12秒左右
	 * @param context
	 * @throws SQLException
	 */
	static void insertVersionNine(ConfigurableApplicationContext context) throws SQLException {
		JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
		try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {
			conn.setAutoCommit(false);
			String sql = "insert into user(user_id,name,birthday,gender,connect_type,connect_value) values (?,?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			for (int i = 0; i < 100 * 10000; i++) {
				User user = context.getBean(User.class);
				ps.setLong(1, user.getUserId());
				ps.setString(2, user.getName());
				ps.setDate(3, user.getBirthday());
				ps.setString(4, user.getGender());
				ps.setString(5, user.getConnectType());
				ps.setString(6, user.getConnectValue());
				ps.addBatch();
			}
			
			long start = System.currentTimeMillis();
			ps.executeBatch();
			conn.commit();
			System.out.println("cost -> " + (System.currentTimeMillis() - start) + "ms");
		}
	}
	
	/**
	 * 16秒左右
	 * @param context
	 * @throws SQLException
	 */
	static void insertVersionEight(ConfigurableApplicationContext context) throws SQLException {
		List<Object[]> list = new ArrayList<Object[]>(100 * 10000);
		for (int i = 0; i < 100 * 10000; i++) {
			User user = context.getBean(User.class);
			list.add(new Object[] {user.getUserId(), user.getName(), user.getBirthday(), user.getGender(), user.getConnectType(),user.getConnectValue()});
		}
		
		JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
		
		String sql = "insert into user(user_id,name,birthday,gender,connect_type,connect_value) values (?,?,?,?,?,?)";
		long start = System.currentTimeMillis();
		jdbcTemplate.batchUpdate(sql, list);
		
		System.out.println("cost -> " + (System.currentTimeMillis() - start) + "ms");
	}
	
	@Deprecated
	static void insertVersionSeven(ConfigurableApplicationContext context) {
		int threadCount = 1;
		int totalRecord = 100 * 10000;
		int listSize = totalRecord / threadCount;
		
		CountDownLatch latch = new CountDownLatch(threadCount);
		List<List<User>> list = new ArrayList<List<User>>(20);
		
		for (int i = 0; i < 20; i++) {
			List<User> temp = new ArrayList<User>(5 * 10000);
			for (int x = 0; x < 5 * 10000; x++) {
				temp.add(context.getBean(User.class));
			}
			list.add(temp);
		}
		
		
		WorkThreadSeven thread = new WorkThreadSeven(list, latch, context.getBean(UserServiceImpl.class));
		thread.start();
		
		long start = System.currentTimeMillis();
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("cost -> " + (end - start) + "ms");
	}
	
	/**
	 * 1.在MySQL层面移除了主键的自增
	 * 2.在Java中利用原子类作为主键
	 * 共20秒左右
	 * @param context
	 */
	static void insertVersionSix(ConfigurableApplicationContext context) {
		int threadCount = 20;
		int totalRecord = 100 * 10000;
		int listSize = totalRecord / threadCount;
		
		CountDownLatch latch = new CountDownLatch(threadCount);
		
		List<Thread> threadList = new ArrayList<Thread>(threadCount);
		for (int i = 0; i < threadCount; ++i) {
			List<User> list = new ArrayList<>(listSize);
			for (int x = 0; x < listSize; ++x) {
				list.add(context.getBean(User.class));
			}
			
			WorkThreadFive thread = new WorkThreadFive(list, latch, context.getBean(UserServiceImpl.class));
			threadList.add(thread);
		}
		System.out.println("prepare finish, threads will work soon");
		
		for (Thread thread : threadList) {
			thread.start();
		}
		
		long start = System.currentTimeMillis();
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("cost -> " + (end - start) + "ms");
	}
	
	/**
	 * 25~30秒
	 * @param context
	 */
	static void insertVersionFive(ConfigurableApplicationContext context) {
		int threadCount = 5;
		int totalRecord = 100 * 10000;
		int listSize = totalRecord / threadCount;
		
		CountDownLatch latch = new CountDownLatch(threadCount);
		
		List<Thread> threadList = new ArrayList<Thread>(threadCount);
		for (int i = 0; i < threadCount; ++i) {
			List<User> list = new ArrayList<>(listSize);
			for (int x = 0; x < listSize; ++x) {
				list.add(context.getBean(User.class));
			}
			
			WorkThreadFive thread = new WorkThreadFive(list, latch, context.getBean(UserServiceImpl.class));
			threadList.add(thread);
		}
		System.out.println("prepare finish, threads will work soon");
		
		for (Thread thread : threadList) {
			thread.start();
		}
		
		long start = System.currentTimeMillis();
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("cost -> " + (end - start) + "ms");
	}
	
	static void insertVersionFour(ConfigurableApplicationContext context) {
		List<User> list = new ArrayList<>(100000);
		for (int x = 0; x < 100000; ++x) {
			list.add(context.getBean(User.class));
		}
		System.out.println("prepare finish, threads will work soon");
		
		UserServiceImpl userServiceImpl = context.getBean(UserServiceImpl.class);
		long start = System.currentTimeMillis();
		userServiceImpl.insertBatch(list);
		long end = System.currentTimeMillis();
		System.out.println("cost -> " + (end - start) + "ms");
	}
	
	/**
	 * 改动单线程批量插入，使用mybatis的批量插入模式，插入速率为220条/秒左右
	 * 非常慢
	 * @param context
	 */
	@Deprecated
	static void insertVersionThree(ConfigurableApplicationContext context) {
		List<User> list = new ArrayList<>(100 * 10000);
		for (int x = 0; x < 100 * 10000; ++x) {
			list.add(context.getBean(User.class));
		}
		System.out.println("prepare finish, threads will work soon");
		
		SqlSessionTemplate template = context.getBean(SqlSessionTemplate.class);
		SqlSession sqlSession = template.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		
		long start = System.currentTimeMillis();
		for (User user : list) {
			userMapper.insert(user);
		}
		sqlSession.commit();
		long end = System.currentTimeMillis();
		System.out.println("cost -> " + (end - start) + "ms");
	}
	
	/**
	 * 改用单线程逐条插入, 插入速率：220条/秒
	 * @param context
	 */
	@Deprecated
	static void insertVersionTwo(ConfigurableApplicationContext context) {
		CountDownLatch latch = new CountDownLatch(1);
		UserServiceImpl userServiceImpl = context.getBean(UserServiceImpl.class);
		List<User> list = new ArrayList<>(100 * 10000);
		for (int x = 0; x < 100 * 10000; ++x) {
			list.add(context.getBean(User.class));
		}
		System.out.println("prepare finish, threads will work soon");
		
		new Thread(() -> {
			for (User user : list) {
				userServiceImpl.insert(user);
			}
			latch.countDown();
		}).start();
		
		try {
			long start = System.currentTimeMillis();
			latch.await();
			long end = System.currentTimeMillis();
			System.out.println("cost -> " + (end - start) + "ms");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 方式1：
	 * 每个线程逐条插入, 共耗时1660399ms
	 * @param context
	 */
	@Deprecated
	static void insertVersionOne(ConfigurableApplicationContext context) {
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		CountDownLatch latch = new CountDownLatch(5);
		
		for (int i = 0; i < 5; ++i) {
			List<User> list = new ArrayList<>(20 * 10000);
			for (int x = 0; x < 20 * 10000; ++x) {
				list.add(context.getBean(User.class));
			}
			
			WorkThreadOne thread = new WorkThreadOne(list, latch, context.getBean(UserServiceImpl.class));
			executorService.submit(thread);
		}
		
		System.out.println("prepare finish, threads will work soon");
		long start = System.currentTimeMillis();
		try {
			executorService.shutdown();
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("cost -> " + (end - start) + "ms");
	}
	
	static class WorkThreadSeven extends Thread {
		private List<List<User>> list;
		private CountDownLatch latch;
		private UserServiceImpl userServiceImpl;
		public WorkThreadSeven(List<List<User>> list, CountDownLatch latch, UserServiceImpl userServiceImpl) {
			super();
			this.list = list;
			this.latch = latch;
			this.userServiceImpl = userServiceImpl;
		}
		@Override
		public void run() {
			for (List<User> userList : list) {
				userServiceImpl.insertBatch(userList);
			}
			latch.countDown();
		}
	}
	
	static class WorkThreadOne extends Thread {
		private List<User> list;
		private CountDownLatch latch;
		private UserServiceImpl userServiceImpl;
		public WorkThreadOne(List<User> list, CountDownLatch latch, UserServiceImpl userServiceImpl) {
			super();
			this.list = list;
			this.latch = latch;
			this.userServiceImpl = userServiceImpl;
		}
		@Override
		public void run() {
			for (User user : list) {
				userServiceImpl.insert(user); // 单例的userServiceImpl是否存在并发问题呢？
			}
			latch.countDown();
		}
	}
	
	static class WorkThreadFive extends Thread {
		private List<User> list;
		private CountDownLatch latch;
		private UserServiceImpl userServiceImpl;
		public WorkThreadFive(List<User> list, CountDownLatch latch, UserServiceImpl userServiceImpl) {
			super();
			this.list = list;
			this.latch = latch;
			this.userServiceImpl = userServiceImpl;
		}
		@Override
		public void run() {
			userServiceImpl.insertBatch(list);
			latch.countDown();
		}
	}
}
