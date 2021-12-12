package sc.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sc.starter.bo.School;
import sc.starter.bo.Student;

@RestController
@Configuration
@ComponentScan("sc.starter")
@EnableConfigurationProperties(SchoolProperties.class)
public class SchoolAutoConfiguration {
	@Autowired
	private School school;
	@Autowired
	private Student student;
	
	@RequestMapping("/schoolInfo")
	public String schoolInfo() {
		return school.toString();
	}
	
	@RequestMapping("/studentInfo")
	public String studentInfo() {
		return student.toString();
	}
}
