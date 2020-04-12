//package com.zafar.calendarly.filter;
//
//import com.zafar.calendarly.service.InMemorySessionProvider.Session;
//import com.zafar.calendarly.service.SessionService;
//import java.io.IOException;
//import java.io.PrintWriter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.powermock.modules.junit4.PowerMockRunner;
//
///**
// * @author Zafar Ansari
// */
//@RunWith(PowerMockRunner.class)
//public class AuthFilterTest {
//
//  @Mock
//  private SessionService service;
//
//  @InjectMocks
//  private AuthFilter authFilter = new AuthFilter();
//
//  @Before
//  public void init() {
//    MockitoAnnotations.initMocks(this);
//  }
//
//  @Test
//  public void test() throws IOException, ServletException {
//    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
//    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
//    Mockito.when(request.getHeader(Mockito.anyString())).thenReturn("mock");
//    Mockito.when(service.getSession(Mockito.anyString())).thenReturn(new Session(0l, 1));
//    authFilter.doFilter(request, response, Mockito.mock(FilterChain.class));
//  }
//
//  @Test
//  public void testNoSession() throws IOException, ServletException {
//    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
//    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
//    Mockito.when(request.getHeader(Mockito.anyString())).thenReturn("mock");
//    Mockito.when(response.getWriter()).thenReturn(Mockito.mock(PrintWriter.class));
//    Mockito.when(service.getSession(Mockito.anyString())).thenReturn(null);
//    authFilter.doFilter(request, response, Mockito.mock(FilterChain.class));
//    Mockito.verify(response).setStatus(401);
//  }
//
//}
