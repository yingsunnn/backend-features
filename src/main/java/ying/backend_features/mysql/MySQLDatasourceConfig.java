package ying.backend_features.mysql;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by ying on 2017-04-17.
 */
@Configuration
public class MySQLDatasourceConfig {

    private static final String CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    @Value("${mysql.connection.url}")
    private String mySQLURL;
    @Value("${mysql.connection.user_name}")
    private String mySQLUsername;
    @Value("${mysql.connection.password}")
    private String mySQLPassword;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(CLASS_NAME);
        dataSource.setUsername(mySQLUsername);
        dataSource.setPassword(mySQLPassword);
        dataSource.setUrl(mySQLURL);
        return dataSource;
    }

    @Bean
    @Autowired
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        jdbcTemplate.afterPropertiesSet();
        return jdbcTemplate;
    }

    @Bean
    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    @Autowired
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
