package com.zafar.calendarly.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;


/**
 * Composite key for Slot
 *
 * @author Zafar Ansari
 */
public class SlotId implements Serializable {

  private Integer slotOwnerId;

  private ZonedDateTime slotStartTimestamp;

  public SlotId() {
  }

  public SlotId(Integer slotOwnerId, ZonedDateTime slotStartTimestamp) {
    this.slotOwnerId = slotOwnerId;
    this.slotStartTimestamp = slotStartTimestamp;
  }


  public SlotId(User slotOwnerId, ZonedDateTime slotStartTimestamp) {
    this.slotOwnerId = slotOwnerId.getId();
    this.slotStartTimestamp = slotStartTimestamp;
  }

  public Integer getSlotOwnerId() {
    return slotOwnerId;
  }

  public void setSlotOwnerId(Integer slotOwnerId) {
    this.slotOwnerId = slotOwnerId;
  }

  public ZonedDateTime getSlotStartTimestamp() {
    return slotStartTimestamp;
  }

  public void setSlotStartTimestamp(ZonedDateTime slotStartTimestamp) {
    this.slotStartTimestamp = slotStartTimestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SlotId slotId = (SlotId) o;

    if (!slotOwnerId.equals(slotId.slotOwnerId)) {
      return false;
    }
    return slotStartTimestamp.equals(slotId.slotStartTimestamp);
  }

  @Override
  public int hashCode() {
    int result = slotOwnerId.hashCode();
    result = 31 * result + slotStartTimestamp.hashCode();
    return result;
  }
}
