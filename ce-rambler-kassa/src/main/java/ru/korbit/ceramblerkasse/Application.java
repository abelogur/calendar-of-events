package ru.korbit.ceramblerkasse;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.SessionFactory;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import ru.korbit.cecommon.config.Constants;
import ru.korbit.cecommon.services.RamblerKassaService;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * Created by Artur Belogur on 13.10.17.
 */
@SpringBootApplication(scanBasePackages = {"ru.korbit.ceramblerkasse", "ru.korbit.cecommon"},
        exclude = {HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
@Slf4j
public class Application {

    public static void main(final String[] args) {
        if (args.length > 0 && args[0].equals("dev")) {
            Environment.host = Environment.PROXY;
        }

        val app = SpringApplication.run(Application.class, args);
        val redissonClient = app.getBean(RedissonClient.class);
        val ramblerKasseLoader = app.getBean(RamblerKasseLoader.class);
        val service = redissonClient.getRemoteService(Constants.QUEUE_NAME);
        service.register(RamblerKassaService.class, ramblerKasseLoader, 2);
    }

    @Value(value = "classpath:redisson.json")
    private Resource redissonConfig;

    @Value(value = "classpath:hibernate.cfg.xml")
    private Resource hibernateProperties;

    @Bean
    public RedissonClient redissonClient() throws IOException {
        val config = Config.fromJSON(redissonConfig.getInputStream());
        return Redisson.create(config);
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(HikariDataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setConfigLocation(hibernateProperties);
        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }
}