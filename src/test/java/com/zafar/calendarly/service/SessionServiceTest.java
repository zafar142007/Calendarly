package com.zafar.calendarly.service;

import com.zafar.calendarly.service.InMemorySessionProvider.Session;
import java.time.Instant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;


/**
 * @author Zafar Ansari
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionServiceTest {

  @Mock
  private SessionProvider sessionProvider;

  @InjectMocks
  private SessionService sessionService = new SessionService();

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    Mockito.when(sessionProvider.getSession(Mockito.any()))
        .thenReturn(new Session(Instant.now().toEpochMilli(), 1));
    Mockito.when(sessionProvider.newSession(Mockito.any())).thenReturn("mock");
  }

  @Test
  public void testGet() {
    Assert.assertEquals(1, (int) (sessionService.getSession("mock").getUserId()));
  }

  @Test
  public void testNewSession() {
    Assert.assertEquals("mock", sessionService.createSession(1));
  }

}
