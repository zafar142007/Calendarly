package com.zafar.calendarly.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;

import com.zafar.calendarly.filter.AuthFilter;
import com.zafar.calendarly.handler.SlotHandler;
import com.zafar.calendarly.handler.UserHandler;
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
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
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

  @Bean
  public RouterFunction<ServerResponse> registrationRoutes(@Autowired UserHandler userHandler) {
    return RouterFunctions.route().nest(path("/calendarly/user"), builder -> {
      builder.POST("/signup", userHandler::registerUser);
      builder.POST("/login", userHandler::loginUser);
    }).build();
  }

  @Bean
  public RouterFunction<ServerResponse> slotRoutes(@Autowired SlotHandler slotHandler,
      @Autowired AuthFilter authFilter) {
    return RouterFunctions.route().nest(path("/calendarly/slot"), builder -> {
      builder.POST("/add", slotHandler::addSlots);
      builder.POST("/book", slotHandler::bookSlots);
      builder.POST("/get", slotHandler::getSlots);
    })
        .filter(authFilter::filter)
        .build();
  }

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
