package com.zafar.calendarly;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/**
 * Driver class to start the calendarly app
 * @author Zafar Ansari
 */
@SpringBootApplication
public class CalendarlyDriver extends SpringBootServletInitializer {

  public static final Logger LOG = LogManager.getLogger(CalendarlyDriver.class);

  public static void main(String[] args) {
    SpringApplication.run(CalendarlyDriver.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(CalendarlyDriver.class);
  }
}
