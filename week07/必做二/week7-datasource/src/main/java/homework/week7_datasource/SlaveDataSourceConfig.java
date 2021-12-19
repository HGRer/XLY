package homework.week7_datasource;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = "homework.week7_datasource.mapper.slave", sqlSessionFactoryRef = "slaveSqlSessionFactory")
public class SlaveDataSourceConfig {
	@Bean("slaveDS")
	@ConfigurationProperties(prefix = "spring.datasource.datasource2")
	public DataSource slaveDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean("slaveSqlSessionFactory")
	public SqlSessionFactory slaveSqlSessionFactory(@Qualifier("slaveDS") DataSource ds) 
			throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(ds);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
        		.getResource("classpath:/mapper/SlaveMapper.xml"));
        return bean.getObject();
	}
	
    @Bean("slaveSqlSessionTemplate")
    public SqlSessionTemplate slaveSqlSessionTemplate(
            @Qualifier("slaveSqlSessionFactory") SqlSessionFactory sessionfactory) {
        return new SqlSessionTemplate(sessionfactory);
    }
}
