package com.zafar.calendarly.config;

import com.zafar.calendarly.filter.AuthFilter;
import com.zafar.calendarly.service.SessionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author Zafar Ansari
 */
@Configuration
public class AppConfig {

  public static final Logger LOG = LogManager.getLogger(AppConfig.class);

  @Autowired
  private SessionService sessionService;

  @Bean
  public FilterRegistrationBean<AuthFilter> loggingFilter(){
    FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new AuthFilter(sessionService));
    registrationBean.addUrlPatterns("/calendarly/slot/add");
    return registrationBean;
  }

}
