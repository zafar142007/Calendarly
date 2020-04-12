//package com.zafar.calendarly.filter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zafar.calendarly.domain.response.CalendarResponse;
//import com.zafar.calendarly.service.SessionContainer;
//import com.zafar.calendarly.service.SessionService;
//import com.zafar.calendarly.util.CalendarConstants;
//import java.io.IOException;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.core.annotation.Order;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zafar.calendarly.domain.response.CalendarResponse;
//import com.zafar.calendarly.service.InMemorySessionProvider.Session;
//import com.zafar.calendarly.service.SessionContainer;
//import com.zafar.calendarly.service.SessionService;
//import com.zafar.calendarly.util.CalendarConstants;
//import java.io.IOException;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.core.annotation.Order;
//
//
///**
// * Filter which checks for the logged-in user by checking the validity of the session by the
// * provided session-id key in the header. This filter is only applicable to certain flows of the
// * application.
// *
// * @author Zafar Ansari
// */
//@Order(2)
//public class AuthFilter implements Filter {
//
//  public static final Logger LOG = LogManager.getLogger(AuthFilter.class);
//  private SessionService sessionService;
//  private ObjectMapper mapper = new ObjectMapper();
//
//  public AuthFilter() {
//  }
//
//  public AuthFilter(SessionService sessionService) {
//    this.sessionService = sessionService;
//  }
//
//  /**
//   * Filter by checking validity of the session. If valid, process the request, else send 401.
//   */
//  @Override
//  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//      throws IOException, ServletException {
//    HttpServletRequest req = (HttpServletRequest) request;
//    HttpServletResponse res = (HttpServletResponse) response;
//    LOG.info(
//        "Logging Request  {} : {}", req.getMethod(),
//        req.getRequestURI());
//    String sessionId = req.getHeader(CalendarConstants.SESSION_ID_HEADER_NAME);
//    Session session = null;
//    if (sessionId != null && (session = sessionService.getSession(sessionId)) != null) {
//      LOG.info("Session {} is valid", sessionId);
//      SessionContainer.getSessionThreadLocal().set(session);
//      chain.doFilter(request, response);
//    } else {
//      LOG.info("Session {} is invalid", sessionId);
//      SessionContainer.getSessionThreadLocal().set(null);
//      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//      res.setContentType("application/json");
//      res.getWriter()
//          .write(mapper.writeValueAsString(new CalendarResponse(CalendarConstants.UNAUTH_MESSAGE)));
//      res.getWriter().flush();
//    }
//  }
//}
