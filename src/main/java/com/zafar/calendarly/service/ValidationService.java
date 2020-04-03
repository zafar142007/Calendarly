package com.zafar.calendarly.service;

import java.time.Instant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;


/**
 * Validate certain requests
 * @author Zafar Ansari
 */
@Service
public class ValidationService {

  public static final Logger LOG = LogManager.getLogger(ValidationService.class);

  /**
   * Check if the provided instant in the future
   * @param instant provided instant
   * @return boolean indicating future status
   */
  public boolean isInFuture(Instant instant) {
    return Instant.now().isBefore(instant);
  }

}
