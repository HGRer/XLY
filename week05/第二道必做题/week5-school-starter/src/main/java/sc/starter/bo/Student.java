package sc.starter.bo;

import org.springframework.stereotype.Component;

@Component
public class Student {
	private String name = "Default Student";
	private int id = 0;
	@Override
	public String toString() {
		return "Student [name=" + name + ", id=" + id + "]";
	}
}
