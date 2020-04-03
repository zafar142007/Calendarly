package com.zafar.calendarly.domain.request;

/**
 * @author Zafar Ansari
 */
public class RegisterUserRequest extends UserRequest {

  private String name;

  public RegisterUserRequest() {
  }

  public RegisterUserRequest(String name, String password, String email) {
    super(password, email);
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
