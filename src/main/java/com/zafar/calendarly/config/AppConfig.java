package com.zafar.calendarly.config;

import com.zafar.calendarly.filter.AuthFilter;
import com.zafar.calendarly.service.SessionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Spring bean declarations
 *
 * @author Zafar Ansari
 */
@EnableScheduling
@Configuration
public class AppConfig {

  public static final Logger LOG = LogManager.getLogger(AppConfig.class);

  @Autowired
  private SessionService sessionService;

  /**
   * Authentication is only required for only certain flows. For those flows check for session.
   */
  @Bean
  public FilterRegistrationBean<AuthFilter> loggingFilter() {
    FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new AuthFilter(sessionService));
    registrationBean.addUrlPatterns("/calendarly/slot/*");
    return registrationBean;
  }

}
