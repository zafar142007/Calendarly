package com.zafar.calendarly.dao;

import com.zafar.calendarly.domain.Slot;
import com.zafar.calendarly.domain.SlotId;
import java.time.ZonedDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


/**
 * Dao of Slot table. Default implementation would be provided by Spring.
 *
 * @author Zafar Ansari
 */
public interface SlotRepository extends ReactiveCrudRepository<Slot, SlotId>, SlotRepositoryCustom {

  @Query("select SLOT_OWNER_ID, SLOT_START from slot where SLOT_BOOKED_BY = null "
      + "and SLOT_START>=sysdate and SLOT_OWNER_ID = :owner and SLOT_START>:start and SLOT_START<:end")
  Flux<Slot> getAvailableSlots(Integer owner, ZonedDateTime start, ZonedDateTime end);

}
