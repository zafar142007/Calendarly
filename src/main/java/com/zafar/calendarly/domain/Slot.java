package com.zafar.calendarly.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 * Domain class for Slot
 *
 * @author Zafar Ansari
 */
@Entity
public class Slot implements Serializable {

  private final static long serialVersionUID = 0l;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SLOT_OWNER_ID", nullable = false)
  private User slotOwner;

  @Id
  @Column(name = "SLOT_START", nullable = false)
  private Timestamp slotStartTimestamp;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SLOT_BOOKED_BY", nullable = true)
  private User slotBooker;


}
