package com.zafar.calendarly.service;

import com.zafar.calendarly.service.InMemorySessionProvider.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


/**
 * A service that can handle operations related a user's session
 *
 * @author Zafar Ansari
 */
@Service
public class SessionService {

  @Autowired
  private SessionProvider sessionProvider;

  public Mono<String> createSession(Mono<Integer> id) {
    return sessionProvider.newSession(id);
  }

  public Mono<Session> getSession(String sessionId) {
    return sessionProvider.getSession(sessionId);
  }

}
