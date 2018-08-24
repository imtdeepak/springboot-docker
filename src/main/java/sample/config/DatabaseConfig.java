package sample.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * Created by deepak on 8/20/18.
 */

@Configuration
public class DatabaseConfig {

    private @Value("${DATABASE_HOST}") String databaseHost;

    private @Value("${DATABASE_PORT}") String databasePort;

    private @Value("${DATABASE_USER}") String databaseUser;

    private @Value("${DATABASE_PASSWORD:}") String databasePassword;

    private @Value("${DATABASE_NAME}") String databaseName;


    @Bean
    DataSource dataSource() {

        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        props.setProperty("dataSource.serverName", databaseHost);
        props.setProperty("dataSource.portNumber", databasePort);
        props.setProperty("dataSource.user", databaseUser);
        props.setProperty("dataSource.password", databasePassword);
        props.setProperty("dataSource.databaseName", databaseName);
        props.put("dataSource.logWriter", new PrintWriter(System.out));

        HikariConfig config = new HikariConfig(props);

        HikariDataSource hikariDataSource = new HikariDataSource(config);

        return hikariDataSource;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment env) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceUnitName("persistenceUnit");
        return entityManagerFactoryBean;
    }

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
