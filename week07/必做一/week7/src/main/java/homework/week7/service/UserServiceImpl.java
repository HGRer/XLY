package homework.week7.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import homework.week7.entity.User;
import homework.week7.mapper.UserMapper;

@Component
public class UserServiceImpl {
	@Autowired
	private UserMapper userMapper;
	
	public void insert(User user) {
		userMapper.insert(user);
	}
	
	public void insertBatch(List<User> list) {
		userMapper.insertBatch(list);
	}
}
