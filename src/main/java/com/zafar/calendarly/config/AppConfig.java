package com.zafar.calendarly.config;

import com.zafar.calendarly.service.SessionService;
import io.r2dbc.h2.CloseableConnectionFactory;
import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.connectionfactory.init.CompositeDatabasePopulator;
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;


/**
 * Spring bean declarations
 *
 * @author Zafar Ansari
 */
@EnableScheduling
@EnableR2dbcRepositories
@Configuration
public class AppConfig {

  public static final Logger LOG = LogManager.getLogger(AppConfig.class);

  @Autowired
  private SessionService sessionService;

  /**
   * Authentication is only required for only certain flows. For those flows check for session.
   */
/*  @Bean
  public FilterRegistrationBean<AuthFilter> loggingFilter() {
    FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new AuthFilter(sessionService));
    registrationBean.addUrlPatterns("/calendarly/slot/*");
    return registrationBean;
  }
*/
//  @Bean("r2dbcDatabaseClient")
//  public DatabaseClient getDatabaseClient() {
//    H2ConnectionConfiguration config  = H2ConnectionConfiguration.builder().
//    CloseableConnectionFactory connectionFactory = H2ConnectionFactory.inMemory("testdb");
//    return null;
//  }

  @Bean
  public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
    initializer.setConnectionFactory(connectionFactory);

    CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
    populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
    initializer.setDatabasePopulator(populator);

    return initializer;
  }

  @Bean("worker")
  public Scheduler workerPool(@Value("${worker.pool.size}") int workerPoolSize) {
    return Schedulers.fromExecutorService(Executors.newFixedThreadPool(workerPoolSize,
        new BasicThreadFactory.Builder()
            .namingPattern("worker-pool-%d")
            .priority(Thread.NORM_PRIORITY)
            .build()));
  }


}
