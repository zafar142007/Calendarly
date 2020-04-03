package com.zafar.calendarly.service;

import com.zafar.calendarly.service.InMemorySessionProvider.Session;

/**
 * A session provider which can generate a login session identifier, and check its validity.
 *
 * @author Zafar Ansari
 */
public interface SessionProvider {

  /**
   * Generate a new session
   *
   * @param email associate the generated Session object with the email provided
   * @return session id
   */
  String newSession(String email);

  /**
   * Check if the provided session is valid. If yes, return the Session object else null.
   *
   * @param session session id in question
   * @return Session object
   */
  Session getSession(String session);

}
