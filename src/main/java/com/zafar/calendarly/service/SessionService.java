package com.zafar.calendarly.service;

import com.zafar.calendarly.service.InMemorySessionProvider.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * A service that can handle operations related a user's session
 * @author Zafar Ansari
 */
@Service
public class SessionService {

  public static final Logger LOG = LogManager.getLogger(SessionService.class);

  @Autowired
  private SessionProvider sessionProvider;

  public String createSession(String email) {
    return sessionProvider.newSession(email);
  }

  public Session getSession(String sessionId) {
    return sessionProvider.getSession(sessionId);
  }

}
