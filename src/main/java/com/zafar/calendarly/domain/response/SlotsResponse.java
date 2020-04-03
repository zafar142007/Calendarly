package com.zafar.calendarly.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;
import java.util.Map;

/**
 * Slots response
 * @author Zafar Ansari
 */
public class SlotsResponse<T> extends CalendarResponse {

  /**
   * Status of the slots.
   */
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
  private T result;

  public SlotsResponse(String message, T result) {
    super(message);
    this.result = result;
  }

  public SlotsResponse() {
  }

  public T getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }

}
