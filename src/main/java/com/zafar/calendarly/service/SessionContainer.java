package com.zafar.calendarly.service;

import com.zafar.calendarly.service.InMemorySessionProvider.Session;


/**
 * A session container which uses a thread local variable to store the current session of the
 * logged-in user
 *
 * @author Zafar Ansari
 */
public class SessionContainer {

  private static ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();

  public static ThreadLocal<Session> getSessionThreadLocal() {
    return sessionThreadLocal;
  }
}
