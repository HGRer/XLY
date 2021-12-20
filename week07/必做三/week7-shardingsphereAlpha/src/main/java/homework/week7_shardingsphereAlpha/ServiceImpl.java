package homework.week7_shardingsphereAlpha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import homework.week7_shardingsphereAlpha.entity.T1;
import homework.week7_shardingsphereAlpha.mapper.T1Mapper;

@Component
public class ServiceImpl {
	@Autowired
	private T1Mapper mapper;
	
	public void insert(T1 t) {
		mapper.insert(t);
	}
	
	public void selectAndShow() {
		mapper.select().stream().forEach((t) -> System.out.println(t.getId()));
	}
}
