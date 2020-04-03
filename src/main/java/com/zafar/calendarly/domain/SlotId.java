package com.zafar.calendarly.domain;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * Composite key for Slot
 * @author Zafar Ansari
 */
public class SlotId implements Serializable {

  private Integer slotOwner;

  private Timestamp slotStartTimestamp;

  public SlotId() {
  }

  public SlotId(Integer slotOwner, Timestamp slotStartTimestamp) {
    this.slotOwner = slotOwner;
    this.slotStartTimestamp = slotStartTimestamp;
  }


  public SlotId(User slotOwner, Timestamp slotStartTimestamp) {
    this.slotOwner = slotOwner.getId();
    this.slotStartTimestamp = slotStartTimestamp;
  }

  public Integer getSlotOwner() {
    return slotOwner;
  }

  public void setSlotOwner(Integer slotOwner) {
    this.slotOwner = slotOwner;
  }

  public Timestamp getSlotStartTimestamp() {
    return slotStartTimestamp;
  }

  public void setSlotStartTimestamp(Timestamp slotStartTimestamp) {
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

    if (!slotOwner.equals(slotId.slotOwner)) {
      return false;
    }
    return slotStartTimestamp.equals(slotId.slotStartTimestamp);
  }

  @Override
  public int hashCode() {
    int result = slotOwner.hashCode();
    result = 31 * result + slotStartTimestamp.hashCode();
    return result;
  }
}
