package com.zafar.calendarly.controller;

import com.zafar.calendarly.domain.request.RegisterUserRequest;
import com.zafar.calendarly.domain.request.UserRequest;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.service.SessionService;
import com.zafar.calendarly.service.UserService;
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
public class RegistrationControllerTest {

  @Mock
  private UserService calendarService;

  @Mock
  private SessionService sessionService;

  @InjectMocks
  private RegistrationController controller = new RegistrationController();

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testRegisterUser() throws CalendarException {
    Mockito.when(calendarService.registerUser(Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(true);
    Assert.assertTrue(controller.registerUser(new RegisterUserRequest()).isUserRegistered());
  }

  @Test
  public void testRegisterUserException() throws CalendarException {
    Mockito.doThrow(new RuntimeException()).when(calendarService)
        .registerUser(Mockito.any(), Mockito.any(), Mockito.any());
    Assert.assertFalse(controller.registerUser(new RegisterUserRequest()).isUserRegistered());
  }
  @Test
  public void testRegisterUserException1() throws CalendarException {
    Mockito.doThrow(new CalendarException("mock")).when(calendarService)
        .registerUser(Mockito.any(), Mockito.any(), Mockito.any());
    Assert.assertFalse(controller.registerUser(new RegisterUserRequest()).isUserRegistered());
  }

  @Test
  public void testLoginUserException() throws CalendarException {
    Mockito.doThrow(new RuntimeException()).when(calendarService)
        .isValidUser(Mockito.any(), Mockito.any());
    Assert.assertNull(controller.loginUser(new UserRequest()).getSessionId());
  }

  @Test
  public void testLoginUserException1() throws CalendarException {
    Mockito.doThrow(new CalendarException("mock")).when(calendarService)
        .isValidUser(Mockito.any(), Mockito.any());
    Assert.assertNull(controller.loginUser(new UserRequest()).getSessionId());
  }

  @Test
  public void testLoginUserValid() throws CalendarException {
    Mockito.when(calendarService
        .isValidUser(Mockito.any(), Mockito.any())).thenReturn(true);
    Mockito.when(sessionService
        .createSession(Mockito.any())).thenReturn("mock");

    Assert.assertEquals("mock", controller.loginUser(new UserRequest()).getSessionId());
  }

  @Test
  public void testLoginUserInValid() throws CalendarException {
    Mockito.when(calendarService
        .isValidUser(Mockito.any(), Mockito.any())).thenReturn(false);

    Assert.assertNull(controller.loginUser(new UserRequest()).getSessionId());
  }

}
