package com.zafar.calendarly.controller;

import com.zafar.calendarly.domain.LoginUserResponse;
import com.zafar.calendarly.domain.RegisterUserRequest;
import com.zafar.calendarly.domain.RegisterUserResponse;
import com.zafar.calendarly.domain.UserRequest;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.service.CalendarService;
import com.zafar.calendarly.service.SessionService;
import com.zafar.calendarly.util.CalendarConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller to handle operations related to user resource
 *
 * @author Zafar Ansari
 */
@RestController
@RequestMapping("/calendarly/user/")
public class RegistrationController {

  public static final Logger LOG = LogManager.getLogger(RegistrationController.class);

  @Autowired
  private CalendarService calendarService;

  @Autowired
  private SessionService sessionService;

  /**
   * Create a new User if not already existing.
   *
   * @param request the details of the user
   */
  @PostMapping("/signup")
  public RegisterUserResponse registerUser(@RequestBody RegisterUserRequest request) {
    RegisterUserResponse response = null;
    try {
      boolean result = calendarService
          .registerUser(request.getEmail(), request.getPassword(), request.getName());
      response = new RegisterUserResponse(
          result ? CalendarConstants.OK_MESSAGE : CalendarConstants.ERROR_MESSAGE, result);
    } catch (CalendarException e) {
      LOG.error("Some error occurred", e);
      response = new RegisterUserResponse(e.getMessage(), false);
    } catch (Exception e) {
      LOG.error("Some error occurred", e);
      response = new RegisterUserResponse(CalendarConstants.ERROR_MESSAGE, false);
    }
    return response;
  }

  /**
   * Login a user with the given details. If successful return a session-id to be used for
   * authenticating future requests.
   */
  @PostMapping("/login")
  public LoginUserResponse loginUser(@RequestBody UserRequest request) {
    LoginUserResponse response = null;
    try {
      boolean isValid = calendarService
          .isValidUser(request.getEmail(), request.getPassword());
      if (isValid) {
        String session = sessionService.createSession(request.getEmail());
        response = new LoginUserResponse(
            session, CalendarConstants.OK_MESSAGE);
      } else {
        response = new LoginUserResponse(null, CalendarConstants.FAILED);
      }
    } catch (CalendarException e) {
      LOG.error("Some error occurred", e);
      response = new LoginUserResponse(null, e.getMessage());
    } catch (Exception e) {
      LOG.error("Some error occurred", e);
      response = new LoginUserResponse(null, CalendarConstants.ERROR_MESSAGE);
    }
    return response;
  }

}
