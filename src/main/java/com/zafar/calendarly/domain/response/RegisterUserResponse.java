package com.zafar.calendarly.domain.response;

/**
 * @author Zafar Ansari
 */
public class RegisterUserResponse extends CalendarResponse {

  private boolean isUserRegistered = false;

  public RegisterUserResponse(String message, boolean isUserRegistered) {
    super(message);
    this.isUserRegistered = isUserRegistered;
  }

  public boolean isUserRegistered() {
    return isUserRegistered;
  }

  public void setUserRegistered(boolean userRegistered) {
    isUserRegistered = userRegistered;
  }
}
