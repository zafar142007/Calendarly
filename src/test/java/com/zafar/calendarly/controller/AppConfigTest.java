package com.zafar.calendarly.controller;

import com.zafar.calendarly.config.AppConfig;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author Zafar Ansari
 */
public class AppConfigTest {

  @Test
  public void test() {
    Assert.assertTrue(
        new AppConfig().loggingFilter().getUrlPatterns().contains("/calendarly/slot/*"));
  }
}
