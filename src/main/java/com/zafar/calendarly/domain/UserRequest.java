package com.zafar.calendarly.domain;

/**
 * @author Zafar Ansari
 */
public class UserRequest extends CalendarRequest {

  protected String email;
  protected String password;

  public UserRequest() {
  }

  public UserRequest(String password, String email) {
    this.password = password;
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
