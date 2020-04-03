package com.zafar.calendarly.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;


/**
 * @author Zafar Ansari
 */
public class AddSlotsRequest extends CalendarRequest {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
  private Instant[] availableSlots;

  public AddSlotsRequest() {
  }

  public Instant[] getAvailableSlots() {
    return availableSlots;
  }

  public void setAvailableSlots(Instant[] availableSlots) {
    this.availableSlots = availableSlots;
  }
}
