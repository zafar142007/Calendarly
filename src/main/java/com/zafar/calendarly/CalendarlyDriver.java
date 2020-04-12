package com.zafar.calendarly;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Driver class to start the calendarly app
 *
 * @author Zafar Ansari
 */
@SpringBootApplication
public class CalendarlyDriver {

  public static final Logger LOG = LogManager.getLogger(CalendarlyDriver.class);

  public static void main(String[] args) {
    SpringApplication.run(CalendarlyDriver.class, args);
  }


}
