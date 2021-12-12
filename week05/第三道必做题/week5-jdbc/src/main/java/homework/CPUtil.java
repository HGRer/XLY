package homework;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class CPUtil {
	private static HikariDataSource dataSource;
	
	public static void main(String[] args) throws SQLException {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://localhost:3306/bank?serverTimezone=UTC");
		config.setUsername("root");
		config.setMaximumPoolSize(20);
		config.setAutoCommit(false);
		dataSource = new HikariDataSource(config);
		//
		ExecutorService es = Executors.newFixedThreadPool(4);
		es.submit(() -> CPUtil.insert());
		es.submit(() -> CPUtil.update());
		es.submit(() -> CPUtil.batchInsert());
		es.submit(() -> CPUtil.delete());
		es.shutdown();
		System.out.println("main thread end");
	}
	
	public static void executeQuery() {
		ResultSet rs = null;
		try (Connection conn = dataSource.getConnection()) {
			rs = conn.prepareStatement("select fname, lname from employee order by emp_id").executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString(1) + " " + rs.getString(2));
			}
			System.out.println("-------------------");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void insert(){
		while(true) {
			try {
				Thread.sleep(1000 * 2);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try (Connection conn = dataSource.getConnection()) {
				conn.prepareStatement("insert into employee(fname, lname, start_date) values (" + "'Sue', " + "'Tan', "
								+ "'2021-12-11'" + ")").execute();
				conn.commit();
			}  catch(SQLException e) {
				e.printStackTrace();
			}
			System.out.println("after insert ------> ");
			executeQuery();
		}
	}
	
	public static void batchInsert() {
		while(true) {
			try {
				Thread.sleep(1000 * 4);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try (Connection conn = dataSource.getConnection()) {
				PreparedStatement statement = conn
						.prepareStatement("insert into employee(fname, lname, start_date) values(?, ?, ?)");
				Random random = new Random();
				for (int i = 0; i < 5; i++) {
					statement.setString(1, "Cloud" + random.nextInt(10));
					statement.setString(2, "AAA" + random.nextInt(30));
					statement.setString(3, "2021-" + (random.nextInt(10) + 1) + "-" + (random.nextInt(27) + 1));
					statement.addBatch();
				}
				statement.executeBatch();
				conn.commit();
			} catch(SQLException e) {
				e.printStackTrace();
			}
			System.out.println("after batchInsert ------> ");
			executeQuery();
		}
	}
	
	public static void update() {
		while(true) {
			try {
				Thread.sleep(1000 * 2);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try (Connection conn = dataSource.getConnection()) {
				conn.prepareStatement("update employee set lname = 'Tang' where fname = 'Sue' and lname = 'Tan'").execute();
				conn.commit();
			} catch(SQLException e) {
				e.printStackTrace();
			}
			System.out.println("after update ------> ");
			executeQuery();
		}
	}
	
	public static void delete() {
		while(true) {
			try {
				Thread.sleep(1000 * 3);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try (Connection conn = dataSource.getConnection()) {
				System.out.println("Before delete, this connection autoCommit -> " + conn.getAutoCommit());

				conn.prepareStatement("delete from employee where start_date > DATE_FORMAT('2021-01-01', '%Y-%m-%d')").execute();
				conn.commit();
			} catch(SQLException e) {
				e.printStackTrace();
			}
			System.out.println("after delete ------> ");
			executeQuery();
		}
	}
}
