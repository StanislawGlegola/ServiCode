package pl.sg.servicode.config;

import liquibase.integration.spring.SpringLiquibase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

@Configuration
public class RepositoryConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RepositoryConfiguration.class);

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        
        // Debugowanie wszystkich zmiennych środowiskowych
        log.info("Checking all possible database configuration variables:");
        log.info("MYSQL_URL: {}", env.getProperty("MYSQL_URL"));
        log.info("MYSQLHOST: {}", env.getProperty("MYSQLHOST"));
        log.info("MYSQL_HOST: {}", env.getProperty("MYSQL_HOST"));
        log.info("MYSQLPORT: {}", env.getProperty("MYSQLPORT"));
        log.info("MYSQL_PORT: {}", env.getProperty("MYSQL_PORT"));
        log.info("MYSQLDATABASE: {}", env.getProperty("MYSQLDATABASE"));
        log.info("MYSQL_DATABASE: {}", env.getProperty("MYSQL_DATABASE"));
        
        // Sprawdź najpierw MYSQL_URL
        String railwayUrl = env.getProperty("MYSQL_URL");
        if (railwayUrl != null && !railwayUrl.isEmpty()) {
            log.info("Using Railway MYSQL_URL: {}", railwayUrl);
            dataSource.setUrl(railwayUrl);
        } else {
            // Sprawdź alternatywne zmienne Railway
            String railwayHost = env.getProperty("MYSQLHOST", env.getProperty("MYSQL_HOST"));
            String railwayPort = env.getProperty("MYSQLPORT", env.getProperty("MYSQL_PORT"));
            String railwayDatabase = env.getProperty("MYSQLDATABASE", env.getProperty("MYSQL_DATABASE"));
            String railwayUser = env.getProperty("MYSQLUSER", env.getProperty("MYSQL_USER"));
            String railwayPassword = env.getProperty("MYSQLPASSWORD", env.getProperty("MYSQL_PASSWORD"));
            
            if (railwayHost != null) {
                String url = String.format("jdbc:mysql://%s:%s/%s?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                        railwayHost, railwayPort, railwayDatabase);
                log.info("Using Railway individual variables. URL: {}", url);
                dataSource.setUrl(url);
                dataSource.setUsername(railwayUser);
                dataSource.setPassword(railwayPassword);
            } else {
                // Lokalna konfiguracja jako ostateczność
                String dbHost = env.getProperty("DB_HOST", "localhost");
                String dbPort = env.getProperty("DB_PORT", "3306");
                String dbSid = env.getProperty("DB_SID", "servicode");
                String url = String.format("jdbc:mysql://%s:%s/%s?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                        dbHost, dbPort, dbSid);
                log.info("Using local configuration. URL: {}", url);
                dataSource.setUrl(url);
                dataSource.setUsername(env.getProperty("DB_USERNAME", "root"));
                dataSource.setPassword(env.getProperty("DB_PASSWORD", ""));
            }
        }
        
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setInitialSize(1);
        dataSource.setMaxTotal(3);
        dataSource.setMaxWaitMillis(10000);
        dataSource.setTestOnBorrow(true);
        dataSource.setValidationQuery("SELECT 1");
        
        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource());
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.yml");
        return liquibase;
    }
}