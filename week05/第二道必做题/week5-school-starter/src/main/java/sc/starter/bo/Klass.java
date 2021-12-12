package sc.starter.bo;

import org.springframework.stereotype.Component;

@Component
public class Klass {
	private String name = "Hello World Class";

	@Override
	public String toString() {
		return "Klass [name=" + name + "]";
	}
}
