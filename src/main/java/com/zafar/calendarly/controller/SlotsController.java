package com.zafar.calendarly.controller;

import com.zafar.calendarly.domain.BookSlotsRequest;
import com.zafar.calendarly.domain.CalendarResponse;
import com.zafar.calendarly.domain.SlotsRequest;
import com.zafar.calendarly.domain.SlotsResponse;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.service.SlotsService;
import com.zafar.calendarly.util.CalendarConstants;
import java.time.Instant;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller to handle operations related to slot resource
 *
 * @author Zafar Ansari
 */
@RestController
@RequestMapping("/calendarly/slot/")
public class SlotsController {

  public static final Logger LOG = LogManager.getLogger(SlotsController.class);

  @Autowired
  private SlotsService slotsService;

  /**
   * Add available slots for the logged-in user.
   *
   * @param slot details of the slots
   */
  @PostMapping("/add")
  public CalendarResponse addSlots(@RequestBody SlotsRequest slot) throws CalendarException {
    CalendarResponse response = null;
    try {
      slotsService.addSlots(slot.getSlots());
      response = new CalendarResponse(CalendarConstants.OK_MESSAGE);
    } catch (CalendarException e) {
      LOG.error("Some error occurred", e);
      response = new CalendarResponse(e.getMessage());
    } catch (Exception e) {
      LOG.error("Some error occurred", e);
      response = new CalendarResponse(CalendarConstants.ERROR_MESSAGE);
    }
    return response;
  }

  /**
   * Book slots for the logged-in user in the calendar of the user given in the request
   *
   * @param slots list of slots requested
   * @return details of which slots were successfully booked and which were not.
   */
  @PostMapping("/book")
  public CalendarResponse bookSlots(@RequestBody BookSlotsRequest slots) throws CalendarException {
    SlotsResponse response = null;
    try {
      Map<Instant, Boolean> result = slotsService
          .bookSlots(slots.getSlots(), slots.getEmailAddressBookee());
      response = new SlotsResponse(CalendarConstants.OK_MESSAGE, result);
    } catch (CalendarException e) {
      LOG.error("Some error occurred", e);
      response = new SlotsResponse(e.getMessage(), null);
    } catch (Exception e) {
      LOG.error("Some error occurred", e);
      response = new SlotsResponse(CalendarConstants.ERROR_MESSAGE, null);
    }
    return response;
  }


}
