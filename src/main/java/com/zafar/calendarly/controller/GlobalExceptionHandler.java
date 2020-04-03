package com.zafar.calendarly.controller;

import com.zafar.calendarly.domain.response.CalendarResponse;
import com.zafar.calendarly.exception.CalendarException;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * A global exception handler which will catch all uncaught exceptions in controllers.
 *
 * @author Zafar Ansari
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  public static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

  /**
   * All kinds of uncaught exceptions raised by the controller will be processed here. If the exception is
   * {@link CalendarException}, then return corresponding error message and code.
   *
   * @param ex generated exception
   * @return json response
   */
  @ExceptionHandler
  @ResponseBody
  protected CalendarResponse handleAnyException(Exception ex,
      HttpServletResponse httpServletResponse) {
    log.error("Exception detected", ex);
    int httpStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

    CalendarResponse response = null;
    if (ex instanceof CalendarException) {
      response = new CalendarResponse(((CalendarException) ex).getMessage());
    } else {
      response = new CalendarResponse("Some error occurred.");
    }
    httpServletResponse.setStatus(httpStatus);
    log.info("finished exception handling for exception {}", ex.getMessage());
    return response;
  }


}
