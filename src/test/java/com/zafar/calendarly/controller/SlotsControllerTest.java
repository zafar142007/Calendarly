package com.zafar.calendarly.controller;

import com.zafar.calendarly.domain.request.BookSlotsRequest;
import com.zafar.calendarly.domain.request.GetSlotRequest;
import com.zafar.calendarly.domain.request.SlotsRequest;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.service.SlotsService;
import com.zafar.calendarly.util.CalendarConstants;
import java.util.Date;
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
public class SlotsControllerTest {

  @Mock
  private SlotsService slotsService;

  @InjectMocks
  private SlotsController controller;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testAddSlots() throws CalendarException {
    Mockito.doThrow(new RuntimeException()).when(slotsService).addSlots(Mockito.any());
    Assert.assertTrue(
        controller.addSlots(new SlotsRequest()).getMessage()
            .equals(CalendarConstants.ERROR_MESSAGE));
  }

  @Test
  public void testAddSlotsException() throws CalendarException {
    Assert.assertTrue(
        controller.addSlots(new SlotsRequest()).getMessage()
            .equals(CalendarConstants.OK_MESSAGE));
  }

  @Test
  public void testBookSlots() throws CalendarException {
    Mockito.doThrow(new RuntimeException()).when(slotsService)
        .bookSlots(Mockito.any(), Mockito.anyString());
    Assert.assertTrue(
        controller.bookSlots(new BookSlotsRequest("mock")).getMessage()
            .equals(CalendarConstants.ERROR_MESSAGE));
  }

  @Test
  public void testBookSlotsException() throws CalendarException {
    Assert.assertTrue(
        controller.bookSlots(new BookSlotsRequest()).getMessage()
            .equals(CalendarConstants.OK_MESSAGE));
  }

  @Test
  public void testGetSlots() throws CalendarException {
    Mockito.doThrow(new RuntimeException()).when(slotsService)
        .getSlots(Mockito.any(), Mockito.any(), Mockito.any());
    Assert.assertTrue(
        controller.getSlots(new GetSlotRequest()).getMessage()
            .equals(CalendarConstants.ERROR_MESSAGE));
  }

  @Test
  public void testGetSlotsException() throws CalendarException {
    Assert.assertTrue(
        controller.getSlots(new GetSlotRequest(new Date(), new Date(), "mock")).getMessage()
            .equals(CalendarConstants.OK_MESSAGE));
  }

}
