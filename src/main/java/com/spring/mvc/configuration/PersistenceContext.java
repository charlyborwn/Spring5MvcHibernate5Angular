package com.spring.mvc.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * This configuration class configures the persistence layer of our example
 * application and enables annotation driven transaction management.
 *
 * This configuration is put to a single class because this way we can write
 * integration tests for our persistence layer by using the configuration used
 * by our example application. In other words, we can ensure that the
 * persistence layer of our application works as expected.
 *
 */
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:/environment.properties")
@ComponentScan({ "com.spring.mvc.model" })
class PersistenceContext {

	@Autowired
	private Environment env;

	private static final String PROPERTY_NAME_DB_DRIVER_CLASS = "jdbc.driverClassName";
	private static final String PROPERTY_NAME_DB_PASSWORD = "jdbc.password";
	private static final String PROPERTY_NAME_DB_URL = "jdbc.url";
	private static final String PROPERTY_NAME_DB_USER = "jdbc.user";
	private static final String PROPERTY_NAME_DB_MINIDLE = "jdbc.pool_conexiones.minIdle";
	private static final String PROPERTY_NAME_DB_MAXTOTAL = "jdbc.pool_conexiones.maxTotal";

	private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
	private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
	private static final String PROPERTY_NAME_HIBERNATE_GENERATE_DDL = "hibernate.generate_ddl";

	/**
	 * Creates and configures the HikariCP datasource bean.
	 * 
	 * @param env
	 *            The runtime environment of our application.
	 * @return
	 */
	@Bean(destroyMethod = "close")
	DataSource dataSource() {
		HikariConfig dataSourceConfig = new HikariConfig();
		dataSourceConfig.setPoolName("MysqlPool");
		dataSourceConfig.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DB_DRIVER_CLASS));
		dataSourceConfig.setJdbcUrl(env.getRequiredProperty(PROPERTY_NAME_DB_URL));
		dataSourceConfig.setUsername(env.getRequiredProperty(PROPERTY_NAME_DB_USER));
		dataSourceConfig.setPassword(env.getRequiredProperty(PROPERTY_NAME_DB_PASSWORD));
		dataSourceConfig.setMinimumIdle(new Integer(env.getRequiredProperty(PROPERTY_NAME_DB_MINIDLE)).intValue());
		dataSourceConfig.setMaximumPoolSize(new Integer(env.getRequiredProperty(PROPERTY_NAME_DB_MAXTOTAL)).intValue());

		return new HikariDataSource(dataSourceConfig);
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(new String[] { "com.spring.mvc.model" });
		sessionFactory.setHibernateProperties(hibernateProperties());

		return sessionFactory;
	}

	/**
	 * Creates the transaction manager bean that integrates the used JPA
	 * provider with the Spring transaction mechanism.
	 * 
	 * @param entityManagerFactory
	 *            The used JPA entity manager factory.
	 * @return
	 */
	@Bean
	@Autowired
	HibernateTransactionManager transactionManager(final SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		return transactionManager;
	}

	private Properties hibernateProperties() {

		final Properties properties = new Properties();

		properties.setProperty(PROPERTY_NAME_HIBERNATE_DIALECT, env.getProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
		properties.setProperty(PROPERTY_NAME_HIBERNATE_GENERATE_DDL,
				env.getProperty(PROPERTY_NAME_HIBERNATE_GENERATE_DDL));
		properties.setProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL, env.getProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));

		return properties;

	}

}