package com.zafar.calendarly.controller;

import com.zafar.calendarly.domain.AddSlotsRequest;
import com.zafar.calendarly.domain.CalendarResponse;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.service.SlotsService;
import com.zafar.calendarly.util.CalendarConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Zafar Ansari
 */
@RestController
@RequestMapping("/calendarly/slot/")
public class SlotsController {

  public static final Logger LOG = LogManager.getLogger(SlotsController.class);

  @Autowired
  private SlotsService slotsService;

  @PostMapping("/add")
  public CalendarResponse addSlots(@RequestBody AddSlotsRequest slot) throws CalendarException {
    CalendarResponse response = null;
    try {
      slotsService.addSlots(slot.getAvailableSlots());
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


}
