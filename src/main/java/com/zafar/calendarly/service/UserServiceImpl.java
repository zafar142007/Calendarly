package com.zafar.calendarly.service;

import com.zafar.calendarly.dao.UserRepository;
import com.zafar.calendarly.domain.User;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.util.CalendarConstants;
import com.zafar.calendarly.util.PasswordUtil;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Service to handle operations related to User resource
 * @author Zafar Ansari
 */
@Service
public class UserServiceImpl implements UserService {

  public static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

  @Autowired
  private UserRepository userRepository;

  public boolean registerUser(String userEmail, String password, String userName)
      throws CalendarException {
    try {
      String salt = PasswordUtil.getNewRandomString(CalendarConstants.SALT_LENGTH);
      User user = new User(userEmail, userName,
          PasswordUtil.getHashedSaltedPassword(password, salt).toCharArray(), salt);
      userRepository.save(user);
    } catch (Exception e) {
      throw new CalendarException("User could not be registered, try another input.", e);
    }
    return true;
  }

  public boolean isValidUser(String userEmail, String password) throws CalendarException {
    boolean result = false;
    try {
      List<User> users = userRepository.findByEmail(userEmail);
      if (users != null && users.size() == 1) {
        User usr = users.get(0);
        String hashedPassword = String.valueOf(usr.getHashedPassword());
        result = hashedPassword
            .equals(PasswordUtil.getHashedSaltedPassword(password, usr.getSalt()));
      } else {
        throw new CalendarException("User not found, try another input.");
      }
    } catch (Exception e) {
      throw new CalendarException("Invalid user, try another input.", e);
    }
    return result;
  }

}
