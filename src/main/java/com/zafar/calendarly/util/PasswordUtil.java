package com.zafar.calendarly.util;

import com.zafar.calendarly.exception.CalendarException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;


/**
 * Util methods for password and salt manipulation
 *
 * @author Zafar Ansari
 */
@Service
public class PasswordUtil {

  public static final Logger LOG = LogManager.getLogger(PasswordUtil.class);

  @Autowired
  @Qualifier("worker")
  private Scheduler worker;

  /**
   * Get a new random readable string of provided length
   *
   * @return random string
   */
  public Mono<String> getNewRandomString(int length) {
    return Mono.<String>create(sink -> {
      SecureRandom secureRandom = new SecureRandom();
      byte[] bytes = new byte[length];
      secureRandom.nextBytes(bytes);
      sink.success(fromBytes(bytes));
    }).subscribeOn(worker);
  }

  /**
   * Get a salted hash of the provided password using the provided salt
   *
   * @param password password
   * @param salt salt
   * @return salted hash
   */
  public Mono<String> getHashedSaltedPassword(String password, String salt) {

    return Mono.<String>create(sink -> {
      MessageDigest messageDigest = null;
      try {
        messageDigest = MessageDigest.getInstance("SHA-256");
      } catch (Exception e) {
        LOG.error(e);
        sink.error(new CalendarException("Invalid input", e));
      }
      if (password != null && password.isEmpty() && salt != null && salt.isEmpty()) {
        sink.error(new CalendarException("Invalid input"));
      }
      sink.success(toHexString(
          messageDigest.digest(concatSalt(password, salt).getBytes(StandardCharsets.UTF_8))));
    }).subscribeOn(worker);
  }

  private String concatSalt(String password, String salt) {
    return salt.concat(password);
  }

  private String toHexString(byte[] hash) {
    BigInteger number = new BigInteger(1, hash);
    StringBuilder hexString = new StringBuilder(number.toString(16));
    return hexString.toString();
  }

  private String fromBytes(byte[] bytes) {
    StringBuilder s = new StringBuilder();
    for (byte b : bytes) {
      if ((b >= 'a' && b <= 'z') || (b >= 'A' && b <= 'Z') || (b >= '0' && b <= '9')) {
        s.append((char) b);
      }
    }

    return s.toString();
  }
}
