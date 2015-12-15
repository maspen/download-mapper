package com.intelligrated.download;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableAutoConfiguration
public class Config {
	@Autowired
	ApplicationContext context;
	
	@Bean
	public DataSource dataSource() {
		//BasicDataSource ds = new BasicDataSource(); //new DriverManagerDataSource();
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		ds.setUrl("jdbc:sqlserver://localhost:1433/master");
		ds.setUsername("matt.aspen");
		ds.setPassword("Welcome2");
		
		return ds;
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
		factory.setDataSource(dataSource());
		factory.setMappingResources("eporder.hbm.xml");
		
		//factory.setConfigLocation(context.getResource("classpath:hibernate-cfg.xml"));
		factory.setHibernateProperties(hibernateProperties());
		return factory;
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityMangerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource());
		factory.setPersistenceUnitName("jpaData");
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		factory.setJpaProperties(hibernateProperties());
		return factory;
	}
	
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);

		return txManager;
	}
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	Properties hibernateProperties() {
	      return new Properties() {
	         {
	        	setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
	     		setProperty("hibernate.current_session_context_class", "thread");
	     		setProperty("hibernate.show_sql", "true");
	         }
	      };
	   }
}
