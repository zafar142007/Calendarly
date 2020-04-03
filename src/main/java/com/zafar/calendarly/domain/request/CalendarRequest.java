package com.zafar.calendarly.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * Generic request for all APIs
 * @author Zafar Ansari
 */
public class CalendarRequest {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
  protected Date timestamp;

  protected double version;

  public Date getTimestamp() {
    return timestamp;
  }

  public double getVersion() {
    return version;
  }

  public void setVersion(double version) {
    this.version = version;
  }

  public CalendarRequest() {
  }
}
