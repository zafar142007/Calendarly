package com.zafar.calendarly.service;

import com.zafar.calendarly.exception.CalendarException;
import java.time.Instant;
import java.util.Map;

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
}
