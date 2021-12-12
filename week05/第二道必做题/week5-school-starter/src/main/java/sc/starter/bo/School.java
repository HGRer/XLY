package sc.starter.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sc.starter.SchoolProperties;

@Component
public class School {
	@Autowired
	private SchoolProperties schoolProperties;

	@Override
	public String toString() {
		return "School [name=" + schoolProperties.schoolName + "]";
	}
	
}
