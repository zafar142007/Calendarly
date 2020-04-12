//package com.zafar.calendarly.service;
//
//import com.zafar.calendarly.dao.SlotRepository;
//import com.zafar.calendarly.dao.UserRepository;
//import com.zafar.calendarly.domain.Slot;
//import com.zafar.calendarly.domain.User;
//import com.zafar.calendarly.exception.CalendarException;
//import com.zafar.calendarly.service.InMemorySessionProvider.Session;
//import java.sql.Timestamp;
//import java.time.Duration;
//import java.time.Instant;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import org.assertj.core.util.Lists;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.Spy;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//
///**
// * @author Zafar Ansari
// */
//@RunWith(PowerMockRunner.class)
//public class SlotsServiceImplTest {
//
//  @Mock
//  private UserRepository userRepository;
//
//  @Mock
//  private SlotRepository slotRepository;
//
//  @Spy
//  private ValidationService validationService = new ValidationService();
//
//  @InjectMocks
//  private SlotsServiceImpl slotsService = new SlotsServiceImpl();
//
//  @Before
//  public void init() {
//    MockitoAnnotations.initMocks(this);
//  }
//
//  @Test(expected = CalendarException.class)
//  public void testAddSlots_Null() throws CalendarException {
//    slotsService.addSlots(null);
//  }
//
//  @Test
//  public void testAddSlots() throws CalendarException {
//    SessionContainer.getSessionThreadLocal()
//        .set(new Session(Instant.now().plus(Duration.ofMinutes(100)).toEpochMilli(), 1));
//    //only one slot should have been added
//    ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
//    slotsService.addSlots(new Instant[]{Instant.now().plus(Duration.ofMinutes(5)), //future
//        Instant.now().minus(Duration.ofMinutes(65))}); //past slot
//    Mockito.verify(slotRepository, Mockito.times(1)).saveAll(captor.capture());
//    Assert.assertEquals(1, captor.getValue().size());
//  }
//
//  @Test(expected = CalendarException.class)
//  public void testAddSlotsException() throws CalendarException {
//    SessionContainer.getSessionThreadLocal()
//        .set(new Session(Instant.now().plus(Duration.ofMinutes(100)).toEpochMilli(), 1));
//    Mockito.doThrow(new RuntimeException()).when(slotRepository).saveAll(Mockito.anyCollection());
//    //only one slot should have been added
//    slotsService.addSlots(new Instant[]{Instant.now().plus(Duration.ofMinutes(5)), //future
//        Instant.now().minus(Duration.ofMinutes(65))}); //past slot
//  }
//
//
//  @Test
//  public void testGetSlots() throws CalendarException {
//    SessionContainer.getSessionThreadLocal()
//        .set(new Session(Instant.now().plus(Duration.ofMinutes(100)).toEpochMilli(), 1));
//    User user = Mockito.mock(User.class);
//    Mockito.when(userRepository.findByEmail(Mockito.any()))
//        .thenReturn(Arrays.asList(user));
//    Mockito.when(user.getSlotsOwned()).thenReturn(Arrays.asList(
//        new Slot(new User("mock", "mock", "mock".toCharArray(), "salt"),//valid
//            new Timestamp(Instant.now().plus(Duration.ofMinutes(60)).toEpochMilli()), null),
//        new Slot(new User("mock", "mock", "mock".toCharArray(), "salt"), //past slot
//            new Timestamp(Instant.now().minus(Duration.ofMinutes(60)).toEpochMilli()), null),
//        new Slot(new User("mock", "mock", "mock".toCharArray(), "salt"),//used slot
//            new Timestamp(Instant.now().plus(Duration.ofMinutes(160)).toEpochMilli()), new User())
//    ));
//    List<Date> slots = slotsService.getSlots("mock", Instant.now(),
//        Instant.now().plus(Duration.ofMinutes(100)));
//    Assert.assertEquals(1, slots.size());
//  }
//
//  @Test(expected = CalendarException.class)
//  public void testGetSlotsUserNotFound() throws CalendarException {
//    SessionContainer.getSessionThreadLocal()
//        .set(new Session(Instant.now().plus(Duration.ofMinutes(100)).toEpochMilli(), 1));
//    Mockito.when(userRepository.findByEmail(Mockito.any()))
//        .thenReturn(Lists.emptyList());
//    slotsService.getSlots("mock", Instant.now(),
//        Instant.now().plus(Duration.ofMinutes(100)));
//  }
//
//  @Test(expected = CalendarException.class)
//  public void testGetSlotsException() throws CalendarException {
//    SessionContainer.getSessionThreadLocal()
//        .set(new Session(Instant.now().plus(Duration.ofMinutes(100)).toEpochMilli(), 1));
//    Mockito.doThrow(RuntimeException.class).when(userRepository).findByEmail(Mockito.any());
//    slotsService.getSlots("mock", Instant.now(), Instant.now().plus(Duration.ofMinutes(100)));
//  }
//
//
//  @Test(expected = CalendarException.class)
//  public void testBookSlotsUserNotFound() throws CalendarException {
//    SessionContainer.getSessionThreadLocal()
//        .set(new Session(Instant.now().plus(Duration.ofMinutes(100)).toEpochMilli(), 1));
//    Mockito.when(userRepository.findByEmail(Mockito.any()))
//        .thenReturn(Lists.emptyList());
//    slotsService.bookSlots(new Instant[]{}, "mock");
//  }
//
//  @Test(expected = CalendarException.class)
//  public void testBookSlotsException() throws CalendarException {
//    SessionContainer.getSessionThreadLocal()
//        .set(new Session(Instant.now().plus(Duration.ofMinutes(100)).toEpochMilli(), 1));
//    Mockito.doThrow(RuntimeException.class).when(userRepository).findByEmail(Mockito.any());
//    slotsService.bookSlots(new Instant[]{}, "mock");
//  }
//
//  @Test
//  public void testBookSlots() throws CalendarException {
//    SessionContainer.getSessionThreadLocal()
//        .set(new Session(Instant.now().plus(Duration.ofMinutes(100)).toEpochMilli(), 1));
//    User user = Mockito.mock(User.class);
//    Mockito.when(userRepository.findByEmail(Mockito.any()))
//        .thenReturn(Arrays.asList(user));
//    long i = Instant.now().plus(Duration.ofMinutes(60)).toEpochMilli();
//    Mockito.when(user.getSlotsOwned()).thenReturn(Arrays.asList(
//        new Slot(new User("mock", "mock", "mock".toCharArray(), "salt"),//valid
//            new Timestamp(i), null),
//        new Slot(new User("mock", "mock", "mock".toCharArray(), "salt"), //past slot
//            new Timestamp(Instant.now().minus(Duration.ofMinutes(60)).toEpochMilli()), null),
//        new Slot(new User("mock", "mock", "mock".toCharArray(), "salt"),//used slot
//            new Timestamp(Instant.now().plus(Duration.ofMinutes(160)).toEpochMilli()), new User())
//    ));
//    Assert.assertEquals(1,
//        slotsService.bookSlots(new Instant[]{Instant.ofEpochMilli(i)}, "mock").size());
//  }
//
//}
