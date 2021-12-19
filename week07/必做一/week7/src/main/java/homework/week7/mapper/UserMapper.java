package homework.week7.mapper;

import java.util.List;

import homework.week7.entity.User;

public interface UserMapper {
	int insert(User user);
	
	int insertBatch(List<User> list);
}
