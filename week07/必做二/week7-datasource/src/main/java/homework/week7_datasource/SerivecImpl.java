package homework.week7_datasource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import homework.week7_datasource.entity.T1;
import homework.week7_datasource.mapper.master.T1Mapper;
import homework.week7_datasource.mapper.slave.SlaveMapper;

@Component
public class SerivecImpl {
	@Autowired
	private T1Mapper t1Mapper;
	@Autowired
	private SlaveMapper slaveMapper;
	
	public void insert(T1 t) {
		t1Mapper.insert(t);
	}
	
	public void selectAndShow() {
		List<T1> t1 = slaveMapper.select();
		if (t1 == null) {
			System.out.println("result is null");
		} else {
			for (T1 t : t1) {
				System.out.println(t.getId());
			}
		}
	}
}
