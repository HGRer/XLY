package homework.week7_shardingsphereAlpha.mapper;

import java.util.List;

import homework.week7_shardingsphereAlpha.entity.T1;

public interface T1Mapper {
	void insert(T1 t);
	
	List<T1> select();
}
