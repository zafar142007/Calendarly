package com.zafar.calendarly.util;

import com.zafar.calendarly.exception.CalendarException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Util methods for password and salt manipulation
 *
 * @author Zafar Ansari
 */
public class PasswordUtil {

  public static final Logger LOG = LogManager.getLogger(PasswordUtil.class);

  public static String getNewRandomString(int length) {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[length];
    random.nextBytes(bytes);
    return fromBytes(bytes);
  }

  public static String getHashedSaltedPassword(String password, String salt)
      throws NoSuchAlgorithmException, CalendarException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    if (password != null && password.isEmpty() && salt != null && salt.isEmpty()) {
      throw new CalendarException("Invalid input");
    }
    return toHexString(md.digest(concatSalt(password, salt).getBytes(StandardCharsets.UTF_8)));
  }

  private static String concatSalt(String password, String salt) {
    return salt.concat(password);
  }

  private static String toHexString(byte[] hash) {
    BigInteger number = new BigInteger(1, hash);
    StringBuilder hexString = new StringBuilder(number.toString(16));
    return hexString.toString();
  }

  private static String fromBytes(byte[] bytes) {
    StringBuilder s = new StringBuilder();
    for (byte b : bytes) {
      if ((b >= 'a' && b <= 'z') || (b >= 'A' && b <= 'Z') || (b >= '0' && b <= '9')) {
        s.append((char)b);
      }
    }

    return s.toString();
  }
}
