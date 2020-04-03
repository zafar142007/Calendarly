package com.zafar.calendarly.util;

/**
 * Constants of the application
 *
 * @author Zafar Ansari
 */
public interface CalendarConstants {

  String ERROR_MESSAGE = "Some error occurred";
  String UNAUTH_MESSAGE = "Unauthorized";
  String OK_MESSAGE = "Success";

  Integer SALT_LENGTH = 64;
  Double VERSION = 1.0;
  String SESSION_EXPIRY_INTERVAL_MS = "${application.session.expiry.interval.ms}";
  int SESSION_ID_LENGTH = 64;
  String FAILED = "Failure";
  String SESSION_ID_HEADER_NAME = "session-id";
}
