package com.zafar.calendarly.util;

import com.zafar.calendarly.exception.CalendarException;
import java.security.NoSuchAlgorithmException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author Zafar Ansari
 */
public class PasswordUtilTest {

  public static final Logger LOG = LogManager.getLogger(PasswordUtilTest.class);

  @Test
  public void testGetHashedSaltedPassword() throws CalendarException, NoSuchAlgorithmException {
    String salt = PasswordUtil.getNewRandomString(CalendarConstants.SALT_LENGTH);
    String pass1 = PasswordUtil.getHashedSaltedPassword("pass", salt);
    String pass2 = PasswordUtil.getHashedSaltedPassword("pass", salt);
    Assert.assertEquals(pass1, pass2);
  }

}
