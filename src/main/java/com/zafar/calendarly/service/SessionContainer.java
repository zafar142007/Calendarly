package com.zafar.calendarly.service;

import com.zafar.calendarly.service.InMemorySessionProvider.Session;


/**
 * @author Zafar Ansari
 */
public class SessionContainer {

  private static ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();

  public static ThreadLocal<Session> getSessionThreadLocal() {
    return sessionThreadLocal;
  }
}
