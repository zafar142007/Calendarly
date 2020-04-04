package com.zafar.calendarly.controller;

import com.zafar.calendarly.domain.response.CalendarResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Meta Controller for operations related to the app itself
 * @author Zafar Ansari
 */
@RestController
@RequestMapping("/calendarly")
public class MetaController {

  @GetMapping("/health")
  public CalendarResponse health() {
    return new CalendarResponse("UP");
  }

}
