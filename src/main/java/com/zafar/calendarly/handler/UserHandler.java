package com.zafar.calendarly.handler;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

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
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author Zafar Ansari
 */
@Component
public class UserHandler {

  public static final Logger LOG = LogManager.getLogger(UserHandler.class);

  @Autowired
  private UserService calendarService;

  @Autowired
  private SessionService sessionService;

  /**
   * Create a new User if not already existing.
   *
   * @param req the details of the user
   */
  public Mono<ServerResponse> registerUser(ServerRequest req) {
    Mono<RegisterUserRequest> request = req.bodyToMono(RegisterUserRequest.class);
    RegisterUserResponse resp = new RegisterUserResponse(CalendarConstants.ERROR_MESSAGE,
        false);
    return calendarService
        .registerUser(request)
        .map(result -> {
          if (result) {
            resp.setUserRegistered(true);
            resp.setMessage(CalendarConstants.OK_MESSAGE);
          }
          return resp;
        }).onErrorResume(CalendarException.class, error -> {
          resp.setMessage(error.getMessage());
          return Mono.just(resp);
        }).onErrorResume(Exception.class, error -> Mono.just(resp))
        .flatMap(registerUserResponse ->
            ServerResponse.ok().body(fromValue(registerUserResponse)));
  }

  /**
   * Login a user with the given details. If successful return a session-id to be used for
   * authenticating future requests.
   */
  public Mono<ServerResponse> loginUser(ServerRequest req) {
    Mono<UserRequest> request = req.bodyToMono(UserRequest.class);
    LoginUserResponse response = new LoginUserResponse(null, CalendarConstants.FAILED);
    return calendarService
        .isValidUser(request)
        .transform(userId -> sessionService.createSession(userId)
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
        }).flatMap(loginUserResponse ->
            ServerResponse.ok().body(fromValue(loginUserResponse))
        );
  }


}
