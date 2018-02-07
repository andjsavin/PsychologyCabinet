package com.poradnia.configuration;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

	@Autowired
	private DataSource dataSource;

	@Value("${hibernate.dialect:org.hibernate.dialect.PostgreSQL94Dialect}")
	private String HIBERNATE_DIALECT;

	
	//@Value("${hibernate.hbm2ddl.auto:create}")
	@Value("${hibernate.hbm2ddl.auto:update}")
	private String HBM2DDL_AUTO;

	@Value("${hibernate.show_sql:true}")
	private String SHOW_SQL;

	@Value("${hibernate.format_sql:true}")
	private String FORMAT_SQL;

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactoryBean.setPackagesToScan("com.poradnia.model");

		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty(Environment.DIALECT, HIBERNATE_DIALECT);

		hibernateProperties.setProperty(Environment.HBM2DDL_AUTO, HBM2DDL_AUTO);

		hibernateProperties.setProperty(Environment.SHOW_SQL, SHOW_SQL);
		hibernateProperties.setProperty(Environment.FORMAT_SQL, FORMAT_SQL);
		entityManagerFactoryBean.setJpaProperties(hibernateProperties);

		return entityManagerFactoryBean;
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}
	
	

}
