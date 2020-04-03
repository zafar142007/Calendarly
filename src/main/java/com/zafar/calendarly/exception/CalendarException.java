package com.zafar.calendarly.exception;

/**
 * Exception for calendarly application
 * @author Zafar Ansari
 */
public class CalendarException extends Exception {

  public CalendarException(String message, Throwable cause) {
    super(message, cause);
  }

  public CalendarException(String s) {
    super(s);
  }
}
