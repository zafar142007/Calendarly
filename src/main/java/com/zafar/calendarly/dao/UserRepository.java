package com.zafar.calendarly.dao;

import com.zafar.calendarly.domain.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;


/**
 * Basic Dao supporting CRUD operations. Default implementation provided by Spring
 * @author Zafar Ansari
 */
public interface UserRepository extends CrudRepository<User, Long> {
  List<User> findByEmail(String email);
}
