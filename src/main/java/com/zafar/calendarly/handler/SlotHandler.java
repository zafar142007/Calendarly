package com.zafar.calendarly.handler;

import com.zafar.calendarly.domain.request.BookSlotsRequest;
import com.zafar.calendarly.domain.request.GetSlotRequest;
import com.zafar.calendarly.domain.request.SlotsRequest;
import com.zafar.calendarly.domain.response.CalendarResponse;
import com.zafar.calendarly.domain.response.SlotsResponse;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.service.InMemorySessionProvider.Session;
import com.zafar.calendarly.service.SlotsService;
import com.zafar.calendarly.util.CalendarConstants;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * @author Zafar Ansari
 */
@Component
public class SlotHandler {

  public static final Logger LOG = LogManager.getLogger(SlotHandler.class);

  @Autowired
  private SlotsService slotsService;

  @Value("${timeout.ms}")
  private long timeoutMs;


  public Mono<ServerResponse> getSlots(ServerRequest serverRequest) {
    return process(slotsService
        .getSlots(serverRequest.bodyToMono(GetSlotRequest.class), getSession(serverRequest)));
  }

  public Mono<ServerResponse> bookSlots(ServerRequest serverRequest) {
    return process(slotsService
        .bookSlots(serverRequest.bodyToMono(BookSlotsRequest.class), getSession(serverRequest)));
  }

  public Mono<ServerResponse> addSlots(ServerRequest serverRequest) {
    return process(slotsService
        .addSlots(serverRequest.bodyToMono(SlotsRequest.class), getSession(serverRequest)));
  }

  private Mono<Session> getSession(ServerRequest serverRequest) {
    return Mono.just((Session) serverRequest.attributes()
        .get(CalendarConstants.SESSION_ID_HEADER_NAME));
  }

  private Mono<ServerResponse> process(Flux<Date> targetSlots) {
    SlotsResponse<List<Date>> response = new SlotsResponse<>();

    return targetSlots.collectList().map(list -> {
      response.setResult(list);
      response.setMessage(CalendarConstants.OK_MESSAGE);
      return response;
    }).flatMap(resp -> {
      return ServerResponse.ok().body(BodyInserters.fromValue(resp));
    })
        .onErrorResume(CalendarException.class, error -> {
          LOG.error(error);
          return ServerResponse.ok().body(
              BodyInserters.fromValue(new CalendarResponse(error.getMessage())));
        })
        .onErrorResume(Exception.class, error -> {
          LOG.error(error);
          return ServerResponse.ok().body(
              BodyInserters.fromValue(new CalendarResponse(CalendarConstants.FAILED)));
        }).timeout(Duration.ofMillis(timeoutMs),
            ServerResponse.status(HttpStatus.GATEWAY_TIMEOUT).body(
                BodyInserters.fromValue(new CalendarResponse(CalendarConstants.TIMEOUT_MESSAGE))));

  }

}
