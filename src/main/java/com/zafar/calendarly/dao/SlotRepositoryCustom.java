package com.zafar.calendarly.dao;

import com.zafar.calendarly.domain.Slot;
import com.zafar.calendarly.domain.User;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * In order to book a slot, it is required to lock the row we intend to update to allow concurrent
 * bookings. So we need a custom implementation where we could put this in a transaction.
 *
 * @author Zafar Ansari
 */
public interface SlotRepositoryCustom {

  Flux<Instant> bookFreeSlots(Integer bookerId, Set<LocalDateTime> requestedSlots, User bookee);

  Flux<Slot> saveAllSlots(Flux<Slot> slots);

  Flux<Slot> getAvailableSlots(Integer owner, LocalDateTime start, LocalDateTime end);

}
