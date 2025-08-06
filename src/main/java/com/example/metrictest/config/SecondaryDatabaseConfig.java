package com.example.metrictest.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.metrictest.repository.secondary", entityManagerFactoryRef = "secondaryEntityManagerFactory", transactionManagerRef = "secondaryTransactionManager")
public class SecondaryDatabaseConfig {

    @Bean(name = "secondaryDataSource")
    public DataSource secondaryDataSource(
            @Value("${spring.datasource.secondary.jdbcUrl}") String jdbcUrl,
            @Value("${spring.datasource.secondary.username}") String username,
            @Value("${spring.datasource.secondary.password}") String password,
            @Value("${spring.datasource.secondary.driverClassName}") String driverClassName,
            @Value("${spring.datasource.secondary.hikari.minimum-idle:5}") int minimumIdle,
            @Value("${spring.datasource.secondary.hikari.maximum-pool-size:20}") int maximumPoolSize,
            @Value("${spring.datasource.secondary.hikari.connection-timeout:30000}") long connectionTimeout,
            @Value("${spring.datasource.secondary.hikari.idle-timeout:600000}") long idleTimeout,
            @Value("${spring.datasource.secondary.hikari.max-lifetime:1800000}") long maxLifetime) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);

        // application.properties에서 읽어온 HikariCP 설정
        config.setMinimumIdle(minimumIdle);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setRegisterMbeans(false);
        config.setPoolName("SecondaryHikariPool");

        return new HikariDataSource(config);
    }

    @Bean(name = "secondaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
            @Qualifier("secondaryDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.metrictest.entity.secondary");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.physical_naming_strategy",
                "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
