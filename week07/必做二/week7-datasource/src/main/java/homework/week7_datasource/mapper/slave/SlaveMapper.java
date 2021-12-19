package homework.week7_datasource.mapper.slave;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import homework.week7_datasource.entity.T1;

@Mapper
public interface SlaveMapper {
	List<T1> select();
}
