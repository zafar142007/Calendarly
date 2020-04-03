package com.zafar.calendarly.domain.response;

/**
 * @author Zafar Ansari
 */
public class LoginUserResponse extends CalendarResponse {

  private String sessionId;

  public LoginUserResponse(String sessionId, String message) {
    super(message);
    this.sessionId = sessionId;

  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
}
