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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = "homework.week7_datasource.mapper.master", sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSourceConfig {
	@Bean("masterDS")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.primary")
	public DataSource masterDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean("masterSqlSessionFactory")
	@Primary
	public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDS") DataSource ds)
			throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(ds);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
        		.getResource("classpath:/mapper/T1Mapper.xml"));
        return bean.getObject();
	}
	
    @Bean("masterSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate masterSqlSessionTemplate(
            @Qualifier("masterSqlSessionFactory") SqlSessionFactory sessionfactory) {
        return new SqlSessionTemplate(sessionfactory);
    }
}
