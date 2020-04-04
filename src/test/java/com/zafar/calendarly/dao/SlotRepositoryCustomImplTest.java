package com.zafar.calendarly.dao;

import com.zafar.calendarly.domain.Slot;
import com.zafar.calendarly.domain.User;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author Zafar Ansari
 */
public class SlotRepositoryCustomImplTest {

  @Mock
  private EntityManager em;

  @InjectMocks
  private SlotRepositoryCustomImpl repository = new SlotRepositoryCustomImpl();

  @Before
  public void init() throws IllegalAccessException {
    MockitoAnnotations.initMocks(this);
    FieldUtils.writeField(repository, "lockTimeoutMs", 1000l, true);
  }

  @Test
  public void testBookFreeSlots() {
    Query query = Mockito.mock(Query.class);
    Mockito.when(em.createQuery(Mockito.anyString())).thenReturn(query);
    List<Slot> list = new ArrayList<>();
    Instant instant = Instant.now();
    list.add(new Slot(null, new Timestamp(instant.toEpochMilli()), null));
    Mockito.when(query.getResultList()).thenReturn(list);
    Set<Instant> successfulSet = new HashSet<>();
    Set<Instant> requestedSets = new HashSet<>();
    requestedSets.add(Instant.now().minus(Duration.ofMinutes(60)));
    requestedSets.add(Instant.now().plus(Duration.ofMinutes(60)));
    requestedSets.add(instant);
    repository.bookFreeSlots(successfulSet, new User(), requestedSets, 1);
    Assert.assertTrue(successfulSet.contains(instant));
  }

}
