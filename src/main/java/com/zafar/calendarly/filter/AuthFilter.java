package com.zafar.calendarly.filter;

import com.zafar.calendarly.domain.response.CalendarResponse;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.service.SessionService;
import com.zafar.calendarly.util.CalendarConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


/**
 * Filter which checks for the logged-in user by checking the validity of the session by the
 * provided session-id key in the header. This filter is only applicable to certain flows of the
 * application.
 *
 * @author Zafar Ansari
 */
@Component
public class AuthFilter {

  public static final Logger LOG = LogManager.getLogger(AuthFilter.class);

  @Autowired
  private SessionService sessionService;

  public AuthFilter() {
  }

  public AuthFilter(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  public Mono<ServerResponse> filter(ServerRequest serverRequest,
      HandlerFunction<ServerResponse> next) {
    String sessionId = serverRequest.headers()
        .firstHeader(CalendarConstants.SESSION_ID_HEADER_NAME);
    return sessionService.getSession(sessionId)
        .single()
        .onErrorMap(Exception.class,
            error -> new CalendarException(CalendarConstants.UNAUTH_MESSAGE))
        .map(session -> {
          serverRequest.attributes().put(CalendarConstants.SESSION_ID_HEADER_NAME, session);
          return session;
        }).flatMap(session -> next.handle(serverRequest))
        .onErrorResume(CalendarException.class, error -> {
              LOG.error(error);
              return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                  .body(
                      BodyInserters.fromValue(new CalendarResponse(error.getMessage())));
            }
        ).onErrorResume(Exception.class, error -> {
              LOG.error(error);
              return
                  ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                      .body(
                          BodyInserters
                              .fromValue(new CalendarResponse(CalendarConstants.ERROR_MESSAGE)));
            }
        );
  }
}
