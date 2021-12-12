package homework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class JDBCUtil {
	private static Connection connection;

	public static void main(String[] args) {
		try {
			initConnection();
			executeQuery();
			System.out.println("---------");

			insert();
			executeQuery();
			System.out.println("---------");

			update();
			executeQuery();
			System.out.println("---------");

			insertWithTranscationRollBack();
			executeQuery();
			System.out.println("---------");

			batchInsert();
			executeQuery();
			System.out.println("---------");

			insertWithTranscationCommit();
			executeQuery();
			System.out.println("---------");			
			
			delete();
			executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			destory();
		}
	}

	public static void executeQuery() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("select fname, lname from employee order by emp_id");
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(2));
		}

		try {
			if (rs != null) {
				rs.close();
			}
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	public static void insert() throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement("insert into employee(fname, lname, start_date) values (" + "'Sue', " + "'Tan', "
						+ "'2021-12-11'" + ")");
		statement.execute();
		statement.close();
		System.out.println("after insert -> ");
	}

	public static void insertWithTranscationRollBack() throws SQLException {
		if (connection.getAutoCommit()) {
			connection.setAutoCommit(false);
		}

		PreparedStatement statement = connection
				.prepareStatement("insert into employee(fname, lname, start_date) values (" + "'Cloud', " + "'Huang', "
						+ "'2021-12-11'" + ")");
		statement.execute();
		connection.rollback();
		statement.close();
		System.out.println("after insertWithTranscationRollBack -> ");
	}
	
	public static void insertWithTranscationCommit() throws SQLException {
		if (connection.getAutoCommit()) {
			connection.setAutoCommit(false);
		}

		PreparedStatement statement = connection
				.prepareStatement("insert into employee(fname, lname, start_date) values (" + "'Tom', " + "'Huang', "
						+ "'2021-12-11'" + ")");
		statement.execute();
		connection.commit();
		statement.close();
		System.out.println("after insertWithTranscationCommit -> ");
	}

	public static void batchInsert() throws SQLException {
		if (connection.getAutoCommit()) {
			connection.setAutoCommit(false);
		}
		PreparedStatement statement = connection
				.prepareStatement("insert into employee(fname, lname, start_date) values(?, ?, ?)");
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			statement.setString(1, "Cloud" + random.nextInt(10));
			statement.setString(2, "AAA" + random.nextInt(30));
			statement.setString(3, "2021-" + (random.nextInt(10) + 1) + "-" + (random.nextInt(27) + 1));
			statement.addBatch();
		}
		statement.executeBatch();
		statement.close();
		System.out.println("after batchInsert -> ");
	}

	public static void update() throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement("update employee set lname = 'Tang' where fname = 'Sue' and lname = 'Tan'");
		statement.executeUpdate();
		statement.close();
		System.out.println("after udpate -> ");
	}

	public static void delete() throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement("delete from employee where start_date > DATE_FORMAT('2021-01-01', '%Y-%m-%d')");
		statement.execute();
		connection.commit();
		statement.close();
		System.out.println("after delete -> ");
	}

	public static void initConnection() throws Exception {
		if (connection == null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?serverTimezone=UTC", "root",
						null);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static void destory() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
