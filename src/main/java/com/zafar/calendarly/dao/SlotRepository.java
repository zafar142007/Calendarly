package com.zafar.calendarly.dao;

import com.zafar.calendarly.domain.Slot;
import com.zafar.calendarly.domain.SlotId;
import com.zafar.calendarly.domain.User;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;


/**
 * Dao of Slot table. Default implementation would be provided by Spring.
 *
 * @author Zafar Ansari
 */
public interface SlotRepository extends CrudRepository<Slot, SlotId>, SlotRepositoryCustom {

}
