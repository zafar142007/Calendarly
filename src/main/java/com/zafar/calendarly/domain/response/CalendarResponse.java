package com.zafar.calendarly.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zafar.calendarly.util.CalendarConstants;
import java.util.Date;


/**
 * Generic response class for all APIs
 *
 * @author Zafar Ansari
 */
public class CalendarResponse {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
  private Date timestamp = new Date();

  private Double version = CalendarConstants.VERSION;

  private String message;

  public CalendarResponse() {
  }

  public CalendarResponse(String message) {
    this.message = message;
  }

  public Date getTimestamp() {
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
