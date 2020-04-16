package com.zafar.calendarly.dao;

import com.zafar.calendarly.domain.Slot;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import reactor.core.publisher.Flux;

/**
 * In order to book a slot, it is required to lock the row we intend to update to allow concurrent
 * bookings. So we need a custom implementation where we could put this in a transaction.
 *
 * @author Zafar Ansari
 */
public interface SlotRepositoryCustom {

  Flux<Instant> bookFreeSlots(Integer bookerId, Set<ZonedDateTime> requestedSlots, int bookeeId);

  Flux<Slot> saveAllSlots(Flux<Slot> slots);


}
