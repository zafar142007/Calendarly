package com.zafar.calendarly.util;

/**
 * @author Zafar Ansari
 */
public interface CalendarConstants {

  String ERROR_MESSAGE = "Some error occurred";
  String OK_MESSAGE = "Success";

  Integer SALT_LENGTH = 64;

  Double VERSION = 1.0;
  Long SESSION_EXPIRY_INTERVAL_MS = 300000l;
  int SESSION_ID_LENGTH = 16;
  String FAILED = "Failure";
  String SESSION_ID_HEADER_NAME = "session-id";
}
