package com.zafar.calendarly.domain;

/**
 * @author Zafar Ansari
 */
public class BookSlotsRequest extends SlotsRequest {

  private String emailAddressBookee;

  public BookSlotsRequest(String emailAddressBookee) {
    this.emailAddressBookee = emailAddressBookee;
  }

  public BookSlotsRequest() {
  }

  public String getEmailAddressBookee() {
    return emailAddressBookee;
  }

  public void setEmailAddressBookee(String emailAddressBookee) {
    this.emailAddressBookee = emailAddressBookee;
  }
}
