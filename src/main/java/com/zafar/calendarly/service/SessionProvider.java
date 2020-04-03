package com.zafar.calendarly.service;

import com.zafar.calendarly.service.InMemorySessionProvider.Session;

/**
 * @author Zafar Ansari
 */
public interface SessionProvider {

  String newSession(String email);

  Session getSession(String session);

}
