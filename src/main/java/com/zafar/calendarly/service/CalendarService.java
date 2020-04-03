package com.zafar.calendarly.service;

import com.zafar.calendarly.exception.CalendarException;

/**
 * @author Zafar Ansari
 */
public interface CalendarService {

  boolean registerUser(String userEmail, String password, String userName)
      throws CalendarException;

  boolean isValidUser(String userEmail, String password) throws CalendarException;
}
