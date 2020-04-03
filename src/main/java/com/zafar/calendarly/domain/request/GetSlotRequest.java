package com.zafar.calendarly.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * @author Zafar Ansari
 */
public class GetSlotRequest extends BookSlotsRequest {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
  protected Date fromTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
  protected Date toTime;

  public GetSlotRequest(Date fromTime, Date toTime, String email) {
    super(email);
    this.fromTime = fromTime;
    this.toTime = toTime;
  }

  public GetSlotRequest() {
  }

  public Date getFromTime() {
    return fromTime;
  }

  public void setFromTime(Date fromTime) {
    this.fromTime = fromTime;
  }

  public Date getToTime() {
    return toTime;
  }

  public void setToTime(Date toTime) {
    this.toTime = toTime;
  }
}
