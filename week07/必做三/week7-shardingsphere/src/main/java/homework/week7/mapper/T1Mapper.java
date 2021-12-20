package homework.week7.mapper;

import java.util.List;

import homework.week7.entity.T1;

public interface T1Mapper {
	int insert(T1 t1);
	
	List<T1> select();
}
