package com.zafar.calendarly.service;

import com.zafar.calendarly.dao.UserRepository;
import com.zafar.calendarly.domain.User;
import com.zafar.calendarly.domain.request.RegisterUserRequest;
import com.zafar.calendarly.domain.request.UserRequest;
import com.zafar.calendarly.exception.CalendarException;
import com.zafar.calendarly.util.CalendarConstants;
import com.zafar.calendarly.util.PasswordUtil;
import java.util.AbstractMap.SimpleEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


/**
 * Service to handle operations related to User resource
 *
 * @author Zafar Ansari
 */
@Service
public class UserServiceImpl implements UserService {

  public static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordUtil passwordUtil;

  public Mono<Boolean> registerUser(Mono<RegisterUserRequest> request) {

    return passwordUtil.getNewRandomString(CalendarConstants.SALT_LENGTH)
        .zipWith(request)
        .flatMap(tuple ->
            passwordUtil
                .getHashedSaltedPassword(tuple.getT2().getPassword(), tuple.getT1())
                .zipWith(Mono.just(tuple))
        )
        .flatMap(tuple -> userRepository
            .save(new User(tuple.getT2().getT2().getEmail(), tuple.getT2().getT2().getName(),
                tuple.getT1(), tuple.getT2().getT1())))
        .map(user -> user.getId() != null)
        .onErrorMap(error -> new CalendarException("User exists, try another input.", error));
  }

  public Mono<Integer> isValidUser(Mono<UserRequest> request) {
    return request
        .flatMap(userRequest -> userRepository.findByEmail(userRequest.getEmail())
            .single()
            .onErrorResume(error -> Mono.error(new CalendarException("User does not exist.")))
            .map(user -> new SimpleEntry<>(user, userRequest))
            .flatMap(entry -> {
              String storedPassword = String.valueOf(entry.getKey().getHashedPassword());
              return passwordUtil
                  .getHashedSaltedPassword(entry.getValue().getPassword(), entry.getKey().getSalt())
                  .handle((hashedSaltedPassword, sink) -> {
                    if (!hashedSaltedPassword.equals(storedPassword)) {
                      sink.error(new CalendarException("Password does not match."));
                    } else {
                      sink.next(entry.getKey().getId());
                    }
                  });
            })
        );
  }

}
