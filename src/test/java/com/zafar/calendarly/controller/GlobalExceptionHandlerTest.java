package com.zafar.calendarly.controller;

import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.util.CalendarConstants;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 * @author Zafar Ansari
 */
@RunWith(PowerMockRunner.class)
public class GlobalExceptionHandlerTest {

  @Test
  public void test() {
    GlobalExceptionHandler handler = new GlobalExceptionHandler();
    Exception ex = new RuntimeException();
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    Assert.assertTrue(handler.handleAnyException(ex, response).getMessage()
        .equals(CalendarConstants.ERROR_MESSAGE));
  }

  @Test
  public void testException() {
    GlobalExceptionHandler handler = new GlobalExceptionHandler();
    Exception ex = new CalendarException("mock");
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    Assert.assertTrue(handler.handleAnyException(ex, response).getMessage()
        .equals("mock"));
  }

}
