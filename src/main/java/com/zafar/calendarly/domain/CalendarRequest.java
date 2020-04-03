package com.zafar.calendarly.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zafar.calendarly.service.InMemorySessionProvider.Session;
import java.time.Instant;
import java.util.Date;

/**
 * @author Zafar Ansari
 */
public class CalendarRequest {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
  protected Date timestamp;

  protected double version;

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
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
