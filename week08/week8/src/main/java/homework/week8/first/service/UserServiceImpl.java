package homework.week8.first.service;

import org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserServiceImpl {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional
	@ShardingSphereTransactionType(TransactionType.XA)
	public void updateWithXA() {
		String name = "XXXXXAAAAA";
		jdbcTemplate.update("update user set name = '" + name + "' where user_id in (55) ");
		if (name.length() > 3) {
			throw new RuntimeException();
		}
		jdbcTemplate.update("update user set name = '" + name + "' where user_id in (56) ");
	}
	
}
