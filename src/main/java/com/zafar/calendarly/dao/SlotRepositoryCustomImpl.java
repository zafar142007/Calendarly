package com.zafar.calendarly.dao;

import com.zafar.calendarly.domain.Slot;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.util.CalendarConstants;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.mapping.SettableValue;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
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

  @Transactional
  @Override
  public Flux<Instant> bookFreeSlots(Integer bookerId, Set<ZonedDateTime> requestedSlots,
      int bookeeId) {
    FluxProcessor bookedSlots = DirectProcessor.create().serialize();

    databaseClient.execute(
        "select from Slot where SLOT_OWNER_ID = :owner "
            + "and SLOT_BOOKED_BY = null and SLOT_START>=sysdate and"
            + " SLOT_START in (:requestedSlots)")
        .bind("owner", bookeeId)
        .bind("requestedSlots", requestedSlots)
        .as(Slot.class)
        .fetch()
        .all().subscribeOn(worker).handle((slot, sink) -> {
      slot.setSlotBookerId(bookerId);
      databaseClient.update()
          .table(Slot.class)
          .using(slot)
          .fetch().rowsUpdated().handle((rows, s) -> {
        if (rows != 1) {
          LOG.error("error while booking a slot");
          s.error(new CalendarException("Could not update the slot"));
        } else {
          bookedSlots.sink().next(slot.getSlotStartTimestamp().toInstant());
        }
      }).doOnError(error -> {
        LOG.error("error while booking a particular slot", error);
        sink.error(error);
      });
    });
    return bookedSlots;
  }

  @Override
  public Flux<Slot> saveAllSlots(Flux<Slot> slots) {
    return
        slots.flatMap(slot -> {
          return databaseClient
              .execute(
                  "merge into SLOT (SLOT_OWNER_ID, SLOT_START, SLOT_BOOKED_BY) "
                      + "KEY(SLOT_OWNER_ID, SLOT_START) values (:owner, :slot, :booker)")
              .bind("owner", slot.getSlotOwnerId())
              .bind("slot", slot.getSlotStartTimestamp())
              .bind("booker", SettableValue.fromOrEmpty(null, Integer.class))
              .fetch()
              .all()
              .subscribeOn(worker)
              .onErrorResume(error -> {
                LOG.error(error);
                return Flux.empty();
              })
              .handle((sl, sin) -> {
                LOG.info("done");
              });
        });

  }


}
