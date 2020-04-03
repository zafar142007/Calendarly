package com.zafar.calendarly.domain;

import com.zafar.calendarly.util.CalendarConstants;
import java.time.Instant;


/**
 * @author Zafar Ansari
 */
public class CalendarResponse {

  private Instant timestamp = Instant.now();

  private Double version = CalendarConstants.VERSION;

  private String message;

  public CalendarResponse() {
  }

  public CalendarResponse(String message) {
    this.message = message;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public Double getVersion() {
    return version;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
