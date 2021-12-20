package homework.week7.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import homework.week7.entity.T1;
import homework.week7.mapper.T1Mapper;

@Component
public class ServiceImpl {
	@Autowired
	private T1Mapper mapper;
	
	public void insert(T1 t) {
		mapper.insert(t);
	}
	
	public void selectAndShow() {
		List<T1> list = mapper.select();
		list.stream().forEach((t) -> System.out.println(t.getId()));
	}
}
