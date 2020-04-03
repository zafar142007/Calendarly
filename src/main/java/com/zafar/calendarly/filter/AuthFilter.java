package com.zafar.calendarly.filter;

import com.zafar.calendarly.service.InMemorySessionProvider.Session;
import com.zafar.calendarly.service.SessionContainer;
import com.zafar.calendarly.service.SessionService;
import com.zafar.calendarly.util.CalendarConstants;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;


/**
 * @author Zafar Ansari
 */
@Order(2)
public class AuthFilter implements Filter {

  public static final Logger LOG = LogManager.getLogger(AuthFilter.class);
  private SessionService sessionService;

  public AuthFilter(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    LOG.info(
        "Logging Request  {} : {}", req.getMethod(),
        req.getRequestURI());
    String sessionId = req.getHeader(CalendarConstants.SESSION_ID_HEADER_NAME);
    Session session = null;
    if ((session = sessionService.getSession(sessionId)) != null) {
      LOG.info("Session {} is valid", sessionId);
      SessionContainer.getSessionThreadLocal().set(session);
      chain.doFilter(request, response);
    } else {
      LOG.info("Session {} is invalid", sessionId);
      SessionContainer.getSessionThreadLocal().set(null);
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }
}
