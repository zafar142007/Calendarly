package com.zafar.calendarly.dao;

import com.zafar.calendarly.domain.User;
import java.time.Instant;
import java.util.Set;

/**
 * In order to book a slot, it is required to lock the row we intend to update to allow concurrent
 * bookings. So we need a custom implementation where we could put this in a transaction.
 *
 * @author Zafar Ansari
 */
public interface SlotRepositoryCustom {

  void bookFreeSlots(Set<Instant> successfulSlots, User booker, Set<Instant> requestedSlots,
      int bookeeId);

}
