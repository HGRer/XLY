package sc.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="sc.starter")
public class SchoolProperties {
	public String schoolName = "This is Spring Boot Starter Default School";
}