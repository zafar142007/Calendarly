package com.zafar.calendarly.controller;

import com.zafar.calendarly.domain.request.RegisterUserRequest;
import com.zafar.calendarly.domain.request.UserRequest;
import com.zafar.calendarly.domain.response.LoginUserResponse;
import com.zafar.calendarly.domain.response.RegisterUserResponse;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.service.SessionService;
import com.zafar.calendarly.service.UserService;
import com.zafar.calendarly.util.CalendarConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


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
  private UserService calendarService;

  @Autowired
  private SessionService sessionService;

  /**
   * Create a new User if not already existing.
   *
   * @param request the details of the user
   */
  @PostMapping("/signup")
  public Mono<RegisterUserResponse> registerUser(@RequestBody RegisterUserRequest request) {
    RegisterUserResponse resp = new RegisterUserResponse(CalendarConstants.ERROR_MESSAGE,
        false);
    return calendarService
        .registerUser(request.getEmail(), request.getPassword(), request.getName())
        .map(result -> {
          if (result) {
            resp.setUserRegistered(true);
            resp.setMessage(CalendarConstants.OK_MESSAGE);
          }
          return resp;
        }).onErrorResume(CalendarException.class, error -> {
          resp.setMessage(error.getMessage());
          return Mono.just(resp);
        }).onErrorResume(Exception.class, error -> Mono.just(resp));
  }

  /**
   * Login a user with the given details. If successful return a session-id to be used for
   * authenticating future requests.
   */
  @PostMapping("/login")
  public Mono<LoginUserResponse> loginUser(@RequestBody UserRequest request) {
    LoginUserResponse response = new LoginUserResponse(null, CalendarConstants.FAILED);
    return calendarService
        .isValidUser(request.getEmail(), request.getPassword())
        .flatMap(userId -> sessionService.createSession(userId)
            .map(id -> {
              response.setSessionId(id);
              response.setMessage(CalendarConstants.OK_MESSAGE);
              return response;
            }))
        .onErrorResume(CalendarException.class, error -> {
          response.setMessage(error.getMessage());
          return Mono.just(response);
        })
        .onErrorResume(Exception.class, error -> {
          LOG.error(error);
          return Mono.just(response);
        });
  }

}
