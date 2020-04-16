package com.zafar.calendarly.service;

import com.zafar.calendarly.domain.request.RegisterUserRequest;
import com.zafar.calendarly.domain.request.UserRequest;
import reactor.core.publisher.Mono;

/**
 * Service to handle operations related to user resource
 *
 * @author Zafar Ansari
 */
public interface UserService {

  /**
   * Create a new user if not already existing
   *
   * @return true if successful, else false
   */
  Mono<Boolean> registerUser(Mono<RegisterUserRequest> request);

  /**
   * Authenticate the user by checking the details provided.
   *
   * @return id of the user if successful, else null
   */
  Mono<Integer> isValidUser(Mono<UserRequest> request);
}
