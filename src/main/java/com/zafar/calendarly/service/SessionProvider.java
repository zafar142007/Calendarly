package com.zafar.calendarly.service;

import com.zafar.calendarly.service.InMemorySessionProvider.Session;
import reactor.core.publisher.Mono;

/**
 * A session provider which can generate a login session identifier, and check its validity.
 *
 * @author Zafar Ansari
 */
public interface SessionProvider {

  /**
   * Generate a new session
   *
   * @param id associate the generated Session object with the userId provided
   * @return session id
   */
  Mono<String> newSession(Integer id);

  /**
   * Check if the provided session is valid. If yes, return the Session object else null.
   *
   * @param session session id in question
   * @return Session object
   */
  Session getSession(String session);

}
