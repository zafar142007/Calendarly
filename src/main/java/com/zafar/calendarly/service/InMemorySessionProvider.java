package com.zafar.calendarly.service;

import com.zafar.calendarly.util.CalendarConstants;
import com.zafar.calendarly.util.PasswordUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


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

  private final Long expiryInterval = CalendarConstants.SESSION_EXPIRY_INTERVAL_MS;

  @Override
  public String newSession(String email) {
    String session = PasswordUtil.getNewRandomString(CalendarConstants.SESSION_ID_LENGTH);
    long time = System.currentTimeMillis();
    monitor.readLock().lock();
    sessionStore.put(session, new Session(time, email));
    monitor.readLock().unlock();

    return session;
  }

  @Override
  public Session getSession(String sessionId) {
    monitor.readLock().lock();
    Session session = sessionStore.get(sessionId);
    if (session!=null && isExpired(session.getTimestamp())) {
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
    String email;

    public Session(Long timestamp, String email) {
      this.timestamp = timestamp;
      this.email = email;
    }

    public Long getTimestamp() {
      return timestamp;
    }

    public String getEmail() {
      return email;
    }
  }
}
