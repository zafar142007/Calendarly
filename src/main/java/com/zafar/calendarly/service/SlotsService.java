package com.zafar.calendarly.service;

import com.zafar.calendarly.domain.request.BookSlotsRequest;
import com.zafar.calendarly.domain.request.GetSlotRequest;
import com.zafar.calendarly.domain.request.SlotsRequest;
import com.zafar.calendarly.service.InMemorySessionProvider.Session;
import java.util.Date;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service to manipulate slot resource
 *
 * @author Zafar Ansari
 */
public interface SlotsService {

  /**
   * Add slots as available for the logged-in user
   *
   * @param slots slots available
   */
  Flux<Date> addSlots(Mono<SlotsRequest> slots,
      Mono<Session> sess);

  /**
   * Book slots for the logged-in user in the calendar of the @param emailBookee user if available
   *
   * @return a map indicating which slots were successfully booked, and not booked
   */
  Flux<Date> bookSlots(Mono<BookSlotsRequest> bookSlotsRequestMono,
      Mono<Session> sess);

  /**
   * Get available slots of the given user between the provided times
   *
   * @return list of available slots
   */
  Flux<Date> getSlots(Mono<GetSlotRequest> slots,
      Mono<Session> sess);
}
