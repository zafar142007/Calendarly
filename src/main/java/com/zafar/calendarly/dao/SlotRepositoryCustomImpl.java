package com.zafar.calendarly.dao;

import com.zafar.calendarly.domain.Slot;
import com.zafar.calendarly.domain.User;
import com.zafar.calendarly.util.CalendarConstants;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.PessimisticLockScope;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;


/**
 * In order to book a slot, it is required to lock the row we intend to update to allow concurrent
 * bookings. So we need a custom implementation where we could put this in a transaction.
 *
 * @author Zafar Ansarius
 */
public class SlotRepositoryCustomImpl implements SlotRepositoryCustom {

  public static final Logger LOG = LogManager.getLogger(SlotRepositoryCustomImpl.class);

  @PersistenceContext
  private EntityManager em;

  @Value(CalendarConstants.DB_ROW_LOCK_TIMEOUT_MS)
  private long lockTimeoutMs;

  public SimpleEntry<Timestamp, Timestamp> pickMaxMin(Set<Instant> requestedSlots) {
    Instant max = null, min = null;
    for (Instant instant : requestedSlots) {
      if (max == null && min == null) {
        max = instant;
        min = instant;
      } else {
        if (instant.isBefore(min)) {
          min = instant;
        }
        if (instant.isAfter(max)) {
          max = instant;
        }
      }
    }
    return new SimpleEntry<>(new Timestamp(max.toEpochMilli()), new Timestamp(min.toEpochMilli()));
  }

  @Transactional
  @Override
  public void bookFreeSlots(Set<Instant> successfulSlots, User booker, Set<Instant> requestedSlots,
      int bookeeId) {
    Map<String, Object> properties = new HashMap<>();
    properties.put("javax.persistence.lock.scope", PessimisticLockScope.EXTENDED);
    properties.put("javax.persistence.lock.timeout", lockTimeoutMs);
    SimpleEntry<Timestamp, Timestamp> maxMin = pickMaxMin(requestedSlots);
    Query query = em.createQuery(
        "select s from Slot s where s.slotOwner.id = :owner and s.slotBooker = null and s.slotStartTimestamp>=sysdate and  s.slotStartTimestamp >= :startDate and s.slotStartTimestamp <=:endDate");

    query.setParameter("owner", bookeeId);
    query.setParameter("startDate", maxMin.getValue());
    query.setParameter("endDate", maxMin.getKey());
    List<Slot> slots = query.getResultList();
    for (Slot availableSlot : slots) {
      try {
        em.lock(availableSlot, LockModeType.PESSIMISTIC_WRITE, properties);
      } catch (Exception ex) {
        LOG.error("error while booking a particular slot", ex);
        continue;
      }
      if (availableSlot.getSlotBooker() == null && requestedSlots
          .contains(availableSlot.getSlotStartTimestamp().toInstant())) {
        //it is still free, so book it
        availableSlot.setSlotBooker(booker);
        try {
          em.persist(availableSlot);
          successfulSlots.add(availableSlot.getSlotStartTimestamp().toInstant());
        } catch (Exception e) {
          LOG.error("Unable to save slot", e);
        }
      }
      try {
        em.lock(availableSlot, LockModeType.NONE);
      } catch (Exception e) {
        LOG.error("error while unlocking", e);
      }
    }
  }


}
