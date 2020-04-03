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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Service to manipulate slot resource
 *
 * @author Zafar Ansari
 */
@Service
public class SlotsServiceImpl implements SlotsService {

  public static final Logger LOG = LogManager.getLogger(SlotsServiceImpl.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SlotRepository slotRepository;

  @Autowired
  private ValidationService validationService;

  @Override
  public void addSlots(final Instant[] slots) throws CalendarException {
    Session session = SessionContainer.getSessionThreadLocal().get();
    try {
      if (session != null) {
        List<User> users = userRepository.findByEmail(session.getEmail());
        if (users != null && users.size() == 1) {
          User usr = users.get(0);
          List<Slot> slotList = new ArrayList<>();
          for (Instant slot : slots) {
            if (validationService.isInFuture(slot)) {
              slotList.add(new Slot(usr, new Timestamp(slot.toEpochMilli()), null));
            } else {
              LOG.warn("Ignoring slot {} in past", slot);
            }
          }
          slotRepository.saveAll(slotList);
        }
      } else {
        LOG.error("session invalid");
        throw new CalendarException("Invalid session");
      }
    } catch (Exception e) {
      LOG.error("Could not save slots", e);
      throw new CalendarException("Could not save slots", e);
    }
  }

  private void getAvailableSlots(List<Date> result, Instant from, Instant to,
      List<Slot> availableSlots) {
    for (Slot slot : availableSlots) {
      Instant slotInstant = slot.getSlotStartTimestamp().toInstant();
      if (slot.getSlotBooker() == null && slotInstant.isAfter(from) && slotInstant.isBefore(to)) {
        result.add(new Date(slotInstant.toEpochMilli()));
      }
    }
  }

  @Override
  public List<Date> getSlots(String email, Instant from, Instant to)
      throws CalendarException {
    Session session = SessionContainer.getSessionThreadLocal().get();
    List<Date> successfulSlots = new ArrayList<>();
    try {
      if (session != null) {
        List<User> userList = userRepository.findByEmail(email);
        if (userList != null && userList.size() > 0) {
          User user = userList.get(0);
          List<Slot> availableSlots = user.getSlotsOwned();
          getAvailableSlots(successfulSlots, from, to, availableSlots);
        } else {
          LOG.error("Bookee user {} not found", email);
          throw new CalendarException("No user found to get slots");
        }
      } else {
        LOG.error("session invalid");
        throw new CalendarException("Invalid session");
      }
    } catch (CalendarException e) {
      LOG.error("some error occurred", e);
      throw e;
    } catch (Exception e) {
      LOG.error("Could not get slots", e);
      throw new CalendarException("Could not get slots", e);
    }
    return successfulSlots;
  }


  @Override
  public Map<Instant, Boolean> bookSlots(final Instant[] slots, String emailBookee)
      throws CalendarException {
    Session session = SessionContainer.getSessionThreadLocal().get();
    Set<Instant> successfulSlots = new HashSet<>();
    try {
      if (session != null) {
        List<User> users = userRepository.findByEmail(session.getEmail());
        if (users != null && users.size() == 1) {
          User booker = users.get(0);
          List<User> bookeeList = userRepository.findByEmail(emailBookee);
          if (bookeeList != null && bookeeList.size() > 0) {
            User bookee = bookeeList.get(0);
            Set<Instant> requestedSlots = new HashSet<>(Arrays.asList(slots));
            List<Slot> availableSlots = bookee.getSlotsOwned();
            book(successfulSlots, booker, requestedSlots, availableSlots);
          } else {
            LOG.error("Bookee user {} not found", emailBookee);
            throw new CalendarException("No user found to book");
          }
        }
      } else {
        LOG.error("session invalid");
        throw new CalendarException("Invalid session");
      }
    } catch (CalendarException e) {
      LOG.error("some error occurred", e);
      throw e;
    } catch (Exception e) {
      LOG.error("Could not book slots", e);
      throw new CalendarException("Could not book slots", e);
    }
    return prepareResult(slots, successfulSlots);
  }

  private Map<Instant, Boolean> prepareResult(Instant[] slots, Set<Instant> successfulSlots) {
    Map<Instant, Boolean> result = new HashMap<>();
    for (Instant ins : slots) {
      if (successfulSlots.contains(ins)) {
        result.put(ins, true);
      } else {
        result.put(ins, false);
      }
    }
    return result;
  }

  private void book(Set<Instant> successfulSlots, User booker, Set<Instant> requestedSlots,
      List<Slot> availableSlots) {
    for (Slot availableSlot : availableSlots) {
      if (availableSlot.getSlotBooker() == null && requestedSlots
          .contains(availableSlot.getSlotStartTimestamp().toInstant())) {
        availableSlot.setSlotBooker(booker);
        try {
          slotRepository.save(availableSlot);
          successfulSlots.add(availableSlot.getSlotStartTimestamp().toInstant());
        } catch (Exception e) {
          LOG.error("Error while booking slot {}", availableSlot);
        }
      }
    }
  }

}
