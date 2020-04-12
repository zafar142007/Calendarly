package com.zafar.calendarly.service;

import com.zafar.calendarly.dao.UserRepository;
import com.zafar.calendarly.domain.User;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.util.CalendarConstants;
import com.zafar.calendarly.util.PasswordUtil;
import java.util.AbstractMap.SimpleEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


/**
 * Service to handle operations related to User resource
 *
 * @author Zafar Ansari
 */
@Service
public class UserServiceImpl implements UserService {

  public static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordUtil passwordUtil;

  public Mono<Boolean> registerUser(String userEmail, String password, String userName) {
    return passwordUtil.getNewRandomString(CalendarConstants.SALT_LENGTH)
        .flatMap(salt -> passwordUtil.getHashedSaltedPassword(password, salt)
            .map((pass -> new SimpleEntry<>(pass, salt))))
        .flatMap(tuple -> userRepository
            .save(new User(userEmail, userName, tuple.getKey(), tuple.getValue())))
        .map(user -> user.getId() != null)
        .onErrorMap(error -> new CalendarException("User exists, try another input.", error));
  }

  public Mono<Integer> isValidUser(String userEmail, String password) {
    return userRepository.findByEmail(userEmail)
        .single()
        .flatMap(user -> {
          String storedPassword = String.valueOf(user.getHashedPassword());
          return passwordUtil.getHashedSaltedPassword(password, user.getSalt())
              .flatMap((hashedSaltedPassword) -> {
                if (!hashedSaltedPassword.equals(storedPassword)) {
                  return Mono.error(new CalendarException("Password does not match."));
                } else {
                  return Mono.just(user.getId());
                }
              });
        });
  }

}
