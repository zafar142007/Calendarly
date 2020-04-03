package com.zafar.calendarly.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;


/**
 * @author Zafar Ansari
 */
public class SlotsRequest extends CalendarRequest {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
  protected Instant[] slots;

  public SlotsRequest() {
  }

  public Instant[] getSlots() {
    return slots;
  }

  public void setSlots(Instant[] slots) {
    this.slots = slots;
  }
}
