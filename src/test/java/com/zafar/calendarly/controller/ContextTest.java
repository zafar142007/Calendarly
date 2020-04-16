package com.zafar.calendarly.controller;

import org.apache.logging.log4j.LogManager;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;


/**
 * @author Zafar Ansari
 */
public class ContextTest {

  public static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(ContextTest.class);

  @Test
  public void test() throws Exception {
    Mono<String> mono = Mono.just("hello");

    Mono<String> decorated = addToContext(mono, "key", "value");
    decorated.subscriberContext(context->{
      LOG.info((String)context.get("key"));
      return context;
    });
    Thread.sleep(1000l);
/*
    decorated.flatMap(k -> {
      return Mono.subscriberContext().handle((context, sink) -> {

        LOG.info((String) sink.currentContext().get("key"));
        LOG.info((String) context.get("key"));
        sink.next(context);
      });
    }).block();
    */
  }

  public static <T, V> Mono<T> addToContext(Mono<T> source, String key, V value) {

    return source.subscriberContext(context -> {
      Context context1 = context.put(key, value);
      return context1;
    });
  }
}