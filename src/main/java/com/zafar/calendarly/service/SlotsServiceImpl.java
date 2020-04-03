package com.zafar.calendarly.service;

import com.zafar.calendarly.dao.SlotRepository;
import com.zafar.calendarly.dao.UserRepository;
import com.zafar.calendarly.domain.Slot;
import com.zafar.calendarly.domain.User;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.service.InMemorySessionProvider.Session;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Zafar Ansari
 */
@Service
public class SlotsServiceImpl implements SlotsService {

  public static final Logger LOG = LogManager.getLogger(SlotsServiceImpl.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SlotRepository slotRepository;

  @Override
  public void addSlots(Instant[] slots) throws CalendarException {
    Session session = SessionContainer.getSessionThreadLocal().get();
    try {
      if (session != null) {
        List<User> users = userRepository.findByEmail(session.getEmail());
        if (users != null && users.size() == 1) {
          User usr = users.get(0);
          List<Slot> slotList = new ArrayList<>();
          for (Instant slot : slots) {
            slotList.add(new Slot(usr, new Timestamp(slot.toEpochMilli()), null));
          }
          slotRepository.saveAll(slotList);
        }
      } else {
        throw new CalendarException("Invalid session");
      }
    } catch (Exception e) {
      throw new CalendarException("Could not save slots", e);
    }
  }
}
