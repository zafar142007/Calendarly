package com.zafar.calendarly.service;

import com.zafar.calendarly.dao.UserRepository;
import com.zafar.calendarly.domain.User;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.util.CalendarConstants;
import com.zafar.calendarly.util.PasswordUtil;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 * @author Zafar Ansari
 */
@RunWith(PowerMockRunner.class)
public class CalendarServiceImplTest {

  public static final Logger LOG = LogManager.getLogger(CalendarServiceImplTest.class);

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CalendarService service = new CalendarServiceImpl();

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testIsValidUser() throws Exception {
    String salt = PasswordUtil.getNewRandomString(CalendarConstants.SALT_LENGTH);
    Mockito.when(userRepository.findByEmail(Mockito.any()))
        .thenReturn(Arrays.asList(new User("mock", "mockName",
            PasswordUtil.getHashedSaltedPassword("mock", salt).toCharArray(), salt)));
    Assert.assertTrue(service.isValidUser("mock", "mock"));
  }

  @Test(expected = CalendarException.class)
  public void testInvalidUser() throws CalendarException {
    Mockito.when(userRepository.findByEmail(Mockito.any()))
        .thenReturn(Lists.emptyList());
    service.isValidUser("mock", "mock");
  }

  @Test(expected = CalendarException.class)
  public void testRegisterInvalidUser() throws Exception {
    Mockito.doThrow(RuntimeException.class).when(userRepository).save(Mockito.any(User.class));
    service.registerUser("mock", "mock", "mock");
  }

  @Test
  public void testRegisterUser() throws Exception {
    Assert.assertTrue(service.registerUser("mock", "mock", "mock"));
  }


}
