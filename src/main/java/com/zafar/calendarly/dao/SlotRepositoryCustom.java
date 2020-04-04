package com.zafar.calendarly.dao;

import com.zafar.calendarly.domain.User;
import java.time.Instant;
import java.util.Set;

/**
 * @author Zafar Ansari
 */
public interface SlotRepositoryCustom {

  void bookFreeSlots(Set<Instant> successfulSlots, User booker, Set<Instant> requestedSlots,
      int bookeeId);

}
