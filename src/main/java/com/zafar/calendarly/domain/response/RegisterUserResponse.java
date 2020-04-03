package com.zafar.calendarly.domain.response;

/**
 * @author Zafar Ansari
 */
public class RegisterUserResponse extends CalendarResponse {

  boolean isUserRegistered = false;

  public RegisterUserResponse(String message, boolean isUserRegistered) {
    super(message);
    this.isUserRegistered = isUserRegistered;
  }
}
