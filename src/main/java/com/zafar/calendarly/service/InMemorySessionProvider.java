package com.zafar.calendarly.service;

import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.util.CalendarConstants;
import com.zafar.calendarly.util.PasswordUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;


/**
 * In-memory implementation of session provider
 *
 * @author Zafar Ansari
 */
@Component
public class InMemorySessionProvider implements SessionProvider {

  public static final Logger LOG = LogManager.getLogger(InMemorySessionProvider.class);

  private Map<String, Session> sessionStore = new ConcurrentHashMap<>();
  private final ReentrantReadWriteLock monitor = new ReentrantReadWriteLock();

  @Value(CalendarConstants.SESSION_EXPIRY_INTERVAL_MS)
  private Long expiryInterval;

  @Autowired
  private PasswordUtil passwordUtil;

  @Autowired
  @Qualifier("worker")
  private Scheduler worker;

  @Override
  public Mono<String> newSession(Mono<Integer> id) {
    Mono<String> session = passwordUtil.getNewRandomString(CalendarConstants.SESSION_ID_LENGTH);
    final long time = System.currentTimeMillis();
    return Mono.zip(session, id, (sessionId, userId) -> {
      monitor.readLock().lock();
      sessionStore.put(sessionId, new Session(time, userId));
      monitor.readLock().unlock();
      return sessionId;
    });
  }

  @Override
  public Mono<Session> getSession(String sessionId) {

    return Mono.just(sessionId).flatMap(id -> {
      if (StringUtils.isEmpty(id)) {
        return Mono.error(new CalendarException("session id not provided"));
      } else {
        Mono<Session> result = null;
        monitor.readLock().lock();
        Session session = sessionStore.get(sessionId);
        if (session != null && isExpired(session.getTimestamp()) || session == null) {
          sessionStore.remove(sessionId);
          result = Mono.empty();
        } else if (session != null) {
          result = Mono.just(session);
        }
        monitor.readLock().unlock();
        return result;
      }
    }).subscribeOn(worker);

  }

  private boolean isExpired(long time) {
    long currentTime = System.currentTimeMillis();
    return time + expiryInterval <= currentTime;
  }

  @Scheduled(fixedDelay = 24 * 60 * 60l)
  public void cleanup() {
    Map<String, Session> copy = new ConcurrentHashMap<>();
    monitor.writeLock().lock();
    sessionStore.entrySet().stream().forEach(entry -> {
      if (!isExpired(entry.getValue().getTimestamp())) {
        copy.put(entry.getKey(), entry.getValue());
      }
    });
    sessionStore = copy;
    monitor.writeLock().unlock();
  }

  public static class Session {

    Long timestamp;
    Integer userId;

    public Session(Long timestamp, Integer userId) {
      this.timestamp = timestamp;
      this.userId = userId;
    }

    public Long getTimestamp() {
      return timestamp;
    }

    public Integer getUserId() {
      return userId;
    }
  }
}
