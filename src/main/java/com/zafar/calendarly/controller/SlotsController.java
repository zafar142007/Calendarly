package com.zafar.calendarly.controller;

import com.zafar.calendarly.domain.request.BookSlotsRequest;
import com.zafar.calendarly.domain.request.GetSlotRequest;
import com.zafar.calendarly.domain.request.SlotsRequest;
import com.zafar.calendarly.domain.response.CalendarResponse;
import com.zafar.calendarly.domain.response.SlotsResponse;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.service.SlotsService;
import com.zafar.calendarly.util.CalendarConstants;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    SlotsResponse<Map<Instant, Boolean>> response = null;
    try {
      Map<Instant, Boolean> result = slotsService
          .bookSlots(slots.getSlots(), slots.getEmailAddressBookee());
      response = new SlotsResponse<>(CalendarConstants.OK_MESSAGE, result);
    } catch (CalendarException e) {
      LOG.error("Some error occurred", e);
      response = new SlotsResponse<>(e.getMessage(), null);
    } catch (Exception e) {
      LOG.error("Some error occurred", e);
      response = new SlotsResponse<>(CalendarConstants.ERROR_MESSAGE, null);
    }
    return response;
  }

  @PostMapping("/get")
  public CalendarResponse getSlots(@RequestBody GetSlotRequest slots) throws CalendarException {
    SlotsResponse<List<Date>> response = null;
    List<Date> list = new ArrayList<>();
    try {
      Set<Instant> result = slotsService
          .getSlots(slots.getEmailAddressBookee(), slots.getFromTime().toInstant(),
              slots.getToTime().toInstant());
      result.stream().forEach(res -> list.add(new Date(res.toEpochMilli())));
      response = new SlotsResponse<>(CalendarConstants.OK_MESSAGE, list);
    } catch (CalendarException e) {
      LOG.error("Some error occurred", e);
      response = new SlotsResponse<>(e.getMessage(), null);
    } catch (Exception e) {
      LOG.error("Some error occurred", e);
      response = new SlotsResponse<>(CalendarConstants.ERROR_MESSAGE, null);
    }
    return response;
  }

}
