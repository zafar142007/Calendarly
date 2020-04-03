package com.zafar.calendarly.service;

import com.zafar.calendarly.exception.CalendarException;

/**
 * Service to handle operations related to user resource
 *
 * @author Zafar Ansari
 */
public interface UserService {

  /**
   * Create a new user if not already existing
   *
   * @return if successful
   */
  boolean registerUser(String userEmail, String password, String userName)
      throws CalendarException;

  /**
   * Authenticate the user by checking the details provided.
   */
  boolean isValidUser(String userEmail, String password) throws CalendarException;
}
