package com.zafar.calendarly.dao;

import com.zafar.calendarly.domain.Slot;
import com.zafar.calendarly.domain.User;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.util.CalendarConstants;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;


/**
 * In order to book a slot, it is required to lock the row we intend to update to allow concurrent
 * bookings. So we need a custom implementation where we could put this in a transaction.
 *
 * @author Zafar Ansarius
 */
public class SlotRepositoryCustomImpl implements SlotRepositoryCustom {

  public static final Logger LOG = LogManager.getLogger(SlotRepositoryCustomImpl.class);

  @Value(CalendarConstants.DB_ROW_LOCK_TIMEOUT_MS)
  private long lockTimeoutMs;

  @Autowired
  @Qualifier("worker")
  private Scheduler worker;

  @Autowired
  private DatabaseClient databaseClient;

  @Override
  public Flux<Instant> bookFreeSlots(Integer bookerId, Set<LocalDateTime> requestedSlots,
      User bookee) {
    return
        databaseClient
            .select()
            .from(Slot.class)
            .matching(Criteria.where("SLOT_OWNER_ID").is(bookee.getId())
                .and("SLOT_BOOKED_BY").isNull()
                .and("SLOT_START").greaterThanOrEquals(LocalDateTime.ofInstant(Instant.now(),
                    ZoneId.of("UTC")))
                .and("SLOT_START").in(requestedSlots))
            .as(Slot.class)
            .all()
            .subscribeOn(worker)
            .flatMap((slot) -> {
              slot.setSlotBookerId(bookerId);
              return databaseClient.update()
                  .table("slot")
                  .using(Update.update("SLOT_BOOKED_BY", bookerId))
                  .matching(Criteria.where("SLOT_OWNER_ID").is(slot.getSlotOwnerId())
                      .and("SLOT_START").is(slot.getSlotStartTimestamp()))
                  .fetch().rowsUpdated()
                  .flatMap((rows) -> {
                    if (rows != 1) {
                      LOG.error("error while booking a slot");
                      return Mono.error(new CalendarException(CalendarConstants.FAILED));
                    } else {
                      return Mono.just(slot.getSlotStartTimestamp().toInstant(ZoneOffset.UTC));
                    }
                  })
                  .onErrorResume(Exception.class, error -> {
                    LOG.error(error);
                    return Mono.empty();
                  });
            });
  }

  @Override
  public Flux<Slot> saveAllSlots(Flux<Slot> slots) {
    return
        slots.flatMap(slot -> {
          return databaseClient
              .insert()
              .into(Slot.class)
              .using(slot)
              .fetch()
              .rowsUpdated()
              .subscribeOn(worker)
              .filter(i -> i == 1)
              .onErrorResume(error -> {
                LOG.error(error);
                return Mono.empty();
              })
              .flatMap((rowsUpdated) -> {
                LOG.info("done for {} row", rowsUpdated);
                return Mono.just(slot);
              });
        });

  }

  @Override
  public Flux<Slot> getAvailableSlots(Integer owner, LocalDateTime start, LocalDateTime end) {
    return databaseClient
        .select()
        .from(Slot.class)
        .matching(Criteria.where("SLOT_OWNER_ID").is(owner)
            .and("SLOT_BOOKED_BY").isNull()
            .and("SLOT_START").greaterThanOrEquals(LocalDateTime.ofInstant(Instant.now(),
                ZoneId.of("UTC")))
            .and("SLOT_START").greaterThanOrEquals(start)
            .and("SLOT_START").lessThanOrEquals(end))
        .as(Slot.class)
        .all()
        .subscribeOn(worker);
  }

}
