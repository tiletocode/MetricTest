package com.example.metrictest.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.metrictest.repository.primary", entityManagerFactoryRef = "primaryEntityManagerFactory", transactionManagerRef = "primaryTransactionManager")
public class PrimaryDatabaseConfig {

    @Primary
    @Bean(name = "primaryDataSource")
    public DataSource primaryDataSource(
            @Value("${spring.datasource.primary.jdbcUrl}") String jdbcUrl,
            @Value("${spring.datasource.primary.username}") String username,
            @Value("${spring.datasource.primary.password}") String password,
            @Value("${spring.datasource.primary.driverClassName}") String driverClassName,
            @Value("${spring.datasource.primary.hikari.minimum-idle:5}") int minimumIdle,
            @Value("${spring.datasource.primary.hikari.maximum-pool-size:20}") int maximumPoolSize,
            @Value("${spring.datasource.primary.hikari.connection-timeout:30000}") long connectionTimeout,
            @Value("${spring.datasource.primary.hikari.idle-timeout:600000}") long idleTimeout,
            @Value("${spring.datasource.primary.hikari.max-lifetime:1800000}") long maxLifetime) {

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
        config.setRegisterMbeans(true);
        config.setPoolName("PrimaryHikariPool");

        return new HikariDataSource(config);
    }

    @Primary
    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            @Qualifier("primaryDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.metrictest.entity.primary");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
