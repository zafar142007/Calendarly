package com.zafar.calendarly.service;

import com.zafar.calendarly.exception.CalendarException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service to manipulate slot resource
 *
 * @author Zafar Ansari
 */
public interface SlotsService {

  /**
   * Add slots as available for the logged-in user
   *
   * @param slots slots available
   */
  void addSlots(Instant[] slots) throws CalendarException;

  /**
   * Book slots for the logged-in user in the calendar of the @param emailBookee user if available
   *
   * @param slots slots requested
   * @param emailBookee email of the user whose slots are requested
   * @return a map indicating which slots were successfully booked, and not booked
   */
  Map<Instant, Boolean> bookSlots(Instant[] slots, String emailBookee) throws CalendarException;

  /**
   * Get available slots of the given user between the provided times
   *
   * @return list of available slots
   */
  List<Date> getSlots(String email, Instant from, Instant to)
      throws CalendarException;
}
