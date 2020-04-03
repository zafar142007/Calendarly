package com.zafar.calendarly.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;
import java.util.Map;

/**
 * Slots response
 * @author Zafar Ansari
 */
public class SlotsResponse extends CalendarResponse {

  /**
   * Status of the slots.
   */
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
  private Map<Instant, Boolean> result;

  public SlotsResponse(String message, Map<Instant, Boolean> result) {
    super(message);
    this.result = result;
  }

  public SlotsResponse() {
  }

  public Map<Instant, Boolean> getResult() {
    return result;
  }

  public void setResult(Map<Instant, Boolean> result) {
    this.result = result;
  }

}
