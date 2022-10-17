/*
 * @Author: Karelian_na
 */
package com.karelian.erl;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;


@SpringBootApplication
@MapperScan({"com.karelian.erl.mapper.**"})
public class ErlApplication {

	private static SqlSessionFactory sqlSessionFactory;
	public static void main(String[] args) {
		SpringApplication.run(ErlApplication.class, args);
	}

	
	public static SqlSession getSqlSession() throws Exception {
		if (null == sqlSessionFactory) {
			DataSource dataSource = new PooledDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/electronicresourcelibrary?serverTimeZone=UTC", "erl_user", "12345678");
			
			MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
			bean.setDataSource(dataSource);
			bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*.xml"));
			sqlSessionFactory = bean.getObject();

		}
		return sqlSessionFactory.openSession();
	}
}
