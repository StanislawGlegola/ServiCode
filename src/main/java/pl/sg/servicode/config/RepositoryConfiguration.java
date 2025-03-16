package pl.sg.servicode.config;

import liquibase.integration.spring.SpringLiquibase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class RepositoryConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        
        // Sprawdź czy jesteśmy w Railway
        String railwayHost = env.getProperty("MYSQLHOST");
        String railwayPort = env.getProperty("MYSQLPORT");
        String railwayDatabase = env.getProperty("MYSQLDATABASE");
        String railwayUser = env.getProperty("MYSQLUSER");
        String railwayPassword = env.getProperty("MYSQLPASSWORD");
        
        if (railwayHost != null) {
            // Użyj konfiguracji Railway
            String url = String.format("jdbc:mysql://%s:%s/%s?allowPublicKeyRetrieval=true&useSSL=false",
                    railwayHost, railwayPort, railwayDatabase);
            dataSource.setUrl(url);
            dataSource.setUsername(railwayUser);
            dataSource.setPassword(railwayPassword);
        } else {
            // Użyj lokalnej konfiguracji
            String dbHost = env.getProperty("DB_HOST");
            String dbPort = env.getProperty("DB_PORT");
            String dbSid = env.getProperty("DB_SID");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbSid 
                        + "?allowPublicKeyRetrieval=true&useSSL=false";
            dataSource.setUrl(url);
            dataSource.setUsername(env.getProperty("DB_USERNAME"));
            dataSource.setPassword(env.getProperty("DB_PASSWORD"));
        }
        
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // Dodaj konfigurację puli połączeń
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