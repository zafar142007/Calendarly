package com.zafar.calendarly.controller;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author Zafar Ansari
 */
public class MetaControllerTest {

  @Test
  public void testHealth() {
    Assert.assertEquals("OK", new MetaController().health().getMessage());
  }
}
