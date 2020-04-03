package com.zafar.calendarly.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;


/**
 * Domain class for Slot
 *
 * @author Zafar Ansari
 */
@Entity
@IdClass(SlotId.class)
public class Slot implements Serializable {

  private final static long serialVersionUID = 1l;

  @Id
  @JoinColumn(name = "SLOT_OWNER_ID", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private User slotOwner;

  @Id
  @Column(name = "SLOT_START", nullable = false)
  private Timestamp slotStartTimestamp;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SLOT_BOOKED_BY", nullable = true)
  private User slotBooker;

  public Slot(User slotOwner, Timestamp slotStartTimestamp,
      User slotBooker) {
    this.slotOwner = slotOwner;
    this.slotStartTimestamp = slotStartTimestamp;
    this.slotBooker = slotBooker;
  }

  public Slot() {
  }

  public User getSlotOwner() {
    return slotOwner;
  }

  public void setSlotOwner(User slotOwner) {
    this.slotOwner = slotOwner;
  }

  public Timestamp getSlotStartTimestamp() {
    return slotStartTimestamp;
  }

  public void setSlotStartTimestamp(Timestamp slotStartTimestamp) {
    this.slotStartTimestamp = slotStartTimestamp;
  }

  public User getSlotBooker() {
    return slotBooker;
  }

  public void setSlotBooker(User slotBooker) {
    this.slotBooker = slotBooker;
  }
}
