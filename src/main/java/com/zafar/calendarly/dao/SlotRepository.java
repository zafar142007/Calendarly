package com.zafar.calendarly.dao;

import com.zafar.calendarly.domain.Slot;
import com.zafar.calendarly.domain.SlotId;
import org.springframework.data.repository.CrudRepository;


/**
 * @author Zafar Ansari
 */
public interface SlotRepository extends CrudRepository<Slot, SlotId> {


}
