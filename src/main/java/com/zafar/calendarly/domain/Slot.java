package com.zafar.calendarly.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;


/**
 * Domain class for Slot
 *
 * @author Zafar Ansari
 */
public class Slot implements Serializable {

  private final static long serialVersionUID = 1l;

  @Id
  @Column("SLOT_OWNER_ID")
  private Integer slotOwnerId;

  @Column("SLOT_START")
  private Timestamp slotStartTimestamp;

  @Column("SLOT_BOOKED_BY")
  private Integer slotBookerId;


  public Slot(Integer slotOwner, Timestamp slotStartTimestamp,
      Integer slotBooker) {
    slotOwnerId = slotOwner;
    slotBookerId = slotBooker;
    this.slotStartTimestamp = slotStartTimestamp;
  }

  public Slot() {
  }


  public Timestamp getSlotStartTimestamp() {
    return slotStartTimestamp;
  }

  public void setSlotStartTimestamp(Timestamp slotStartTimestamp) {
    this.slotStartTimestamp = slotStartTimestamp;
  }

  public Integer getSlotOwnerId() {
    return slotOwnerId;
  }

  public void setSlotOwnerId(Integer slotOwnerId) {
    this.slotOwnerId = slotOwnerId;
  }

  public Integer getSlotBookerId() {
    return slotBookerId;
  }

  public void setSlotBookerId(Integer slotBookerId) {
    this.slotBookerId = slotBookerId;
  }
}
