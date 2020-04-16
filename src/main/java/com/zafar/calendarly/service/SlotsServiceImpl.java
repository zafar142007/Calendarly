package com.zafar.calendarly.service;

import com.zafar.calendarly.dao.SlotRepository;
import com.zafar.calendarly.dao.UserRepository;
import com.zafar.calendarly.domain.Slot;
import com.zafar.calendarly.domain.request.BookSlotsRequest;
import com.zafar.calendarly.domain.request.GetSlotRequest;
import com.zafar.calendarly.domain.request.SlotsRequest;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.service.InMemorySessionProvider.Session;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;


/**
 * Service to manipulate slot resource
 *
 * @author Zafar Ansari
 */
@Service
public class SlotsServiceImpl implements SlotsService {

  public static final Logger LOG = LogManager.getLogger(SlotsServiceImpl.class);

  @Autowired
  @Qualifier("worker")
  private Scheduler worker;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SlotRepository slotRepository;

  @Autowired
  private ValidationService validationService;

  @Override
  public Flux<Date> addSlots(Mono<SlotsRequest> slotsRequestMono,
      Mono<Session> sess) {

    return Flux.zip(sess,
        slotsRequestMono, (session, request) -> {
          final Integer userId = session.getUserId();

          Flux<Slot> slots = Flux.create(sink -> {
            for (Instant slot : request.getSlots()) {
              if (validationService.isInFuture(slot)) {
                sink.next(new Slot(userId, LocalDateTime.ofInstant(slot, ZoneId.of("UTC")), null));
              } else {
                LOG.warn("Ignoring slot {} in past", slot);
              }
            }
            sink.complete();
          });

          return slotRepository.saveAllSlots(slots).map(
              slot -> (new Date(
                  slot.getSlotStartTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli())));
        })
        .subscribeOn(worker)
        .flatMap(dateFlux -> dateFlux.map(date -> date));

  }

  @Override
  public Flux<Date> getSlots(Mono<GetSlotRequest> slotsRequestMono,
      Mono<Session> sess) {
    return Flux.zip(sess, slotsRequestMono, (session, request) -> {

      return slotRepository.getAvailableSlots(session.getUserId(),
          LocalDateTime.ofInstant(request.getFromTime().toInstant(),
              ZoneId.of("UTC")),
          LocalDateTime.ofInstant(request.getToTime().toInstant(),
              ZoneId.of("UTC"))).map(slot ->
          new Date(slot.getSlotStartTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli()));

    }).subscribeOn(worker).flatMap(dateFlux -> dateFlux);
  }

  @Override
  public Flux<Date> bookSlots(Mono<BookSlotsRequest> slotsRequestMono,
      Mono<Session> sess) {
    return Flux.zip(sess,
        slotsRequestMono, (session, request) -> {
          Integer userId = session.getUserId();
          return userRepository.findByEmail(request.getEmailAddressBookee())
              .single()
              .onErrorResume(error -> {
                LOG.error(error);
                return Mono.error(new CalendarException("No user found"));
              })
              .flux().flatMap(user -> {
                return slotRepository.bookFreeSlots(userId,
                    Arrays.stream(request.getSlots())
                        .map(slot -> LocalDateTime.ofInstant(slot, ZoneId.of("UTC")))
                        .collect(Collectors.toSet())
                    , user)
                    .onErrorResume(error -> {
                      LOG.error(error);
                      return Flux.empty();
                    });
              });
        })
        .subscribeOn(worker)
        .flatMap(instants -> instants
            .map(instant -> new Date(instant.toEpochMilli())));
  }

}
