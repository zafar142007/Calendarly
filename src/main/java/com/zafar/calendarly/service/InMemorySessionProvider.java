package com.zafar.calendarly.service;

import com.zafar.calendarly.util.CalendarConstants;
import com.zafar.calendarly.util.PasswordUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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
  public Mono<String> newSession(Integer id) {
    Mono<String> session = passwordUtil.getNewRandomString(CalendarConstants.SESSION_ID_LENGTH);
    long time = System.currentTimeMillis();
    session.subscribeOn(worker).handle((sessionId, sink) -> {
      monitor.readLock().lock();
      sessionStore.put(sessionId, new Session(time, id));
      monitor.readLock().unlock();
    });
    return session;
  }

  @Override
  public Session getSession(String sessionId) {
    monitor.readLock().lock();
    Session session = sessionStore.get(sessionId);
    if (session != null && isExpired(session.getTimestamp())) {
      sessionStore.remove(sessionId);
      session = null;
    }
    monitor.readLock().unlock();
    return session;
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
