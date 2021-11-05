package by.shnitko.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {
    @Bean @Profile("!junit")
    public DataSource dataSource(AppProperties appProperties) {
        var databaseProps = appProperties.getDatabase();

        return DataSourceBuilder.create()
                .url(databaseProps.jdbcUrl())
                .driverClassName(databaseProps.getDriverClassName())
                .username(databaseProps.getUsername())
                .password(databaseProps.getPassword())
                .build();
    }

}
