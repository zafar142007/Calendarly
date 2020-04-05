package com.zafar.calendarly.service;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Zafar Ansari
 */
@RunWith(PowerMockRunner.class)
public class InMemorySessionProviderTest {

  @InjectMocks
  private InMemorySessionProvider provider = new InMemorySessionProvider();

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testNewSession() throws IllegalAccessException {
    FieldUtils.writeField(provider, "expiryInterval", 30000l, true);
    String id = provider.newSession(1);
    Assert.assertEquals(1, (int)provider.getSession(id).getUserId());
  }

  @Test
  public void testValidSession() throws IllegalAccessException {
    FieldUtils.writeField(provider, "expiryInterval", -30000l, true);
    String id = provider.newSession(1);
    Assert.assertNull(provider.getSession(id));
  }

  @Test
  public void testCleanup() throws IllegalAccessException {
    FieldUtils.writeField(provider, "expiryInterval", 0l, true);
    provider.newSession(1);
    provider.newSession(2);
    provider.cleanup();
  }


}


