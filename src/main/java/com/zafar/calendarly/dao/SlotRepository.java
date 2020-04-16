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
}
