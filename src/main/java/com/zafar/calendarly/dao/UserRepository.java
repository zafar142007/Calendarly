package com.zafar.calendarly.dao;

import com.zafar.calendarly.domain.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


/**
 * Basic Dao for User table supporting CRUD operations. Default implementation provided by Spring.
 *
 * @author Zafar Ansari
 */
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

  @Query("select id, hashed_Password, salt from user where user_email = :email")
  Flux<User> findByEmail(String email);
}
