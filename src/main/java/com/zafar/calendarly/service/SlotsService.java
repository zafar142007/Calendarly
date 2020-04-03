package com.zafar.calendarly.service;

import com.zafar.calendarly.exception.CalendarException;
import java.time.Instant;

/**
 * @author Zafar Ansari
 */
public interface SlotsService {

  void addSlots(Instant[] slots) throws CalendarException;

}
