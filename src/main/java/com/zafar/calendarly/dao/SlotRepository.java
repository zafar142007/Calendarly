package com.zafar.calendarly.dao;

import com.zafar.calendarly.domain.Slot;
import com.zafar.calendarly.domain.SlotId;
import org.springframework.data.repository.CrudRepository;


/**
 * Dao of Slot table. Default implementation would be provided by Spring.
 * @author Zafar Ansari
 */
public interface SlotRepository extends CrudRepository<Slot, SlotId> {

}
