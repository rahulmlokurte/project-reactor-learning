package com.rahullokurte.reactive.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class MonoTest {

  @Test
  void monoSubscriber() {
    String name = "Rahul Lokurte";
    Mono<String> mono = Mono.just(name).log();
    mono.subscribe();
    log.info("---------------");
    StepVerifier.create(mono).expectNext(name).verifyComplete();
  }

  @Test
  void monoSubscriberConsumer() {
    String name = "Rahul Lokurte";
    Mono<String> mono = Mono.just(name).log();
    mono.subscribe(s -> log.info("name: {}", s));
    log.info("---------------");
    StepVerifier.create(mono).expectNext(name).verifyComplete();
  }

  @Test
  void monoSubscriberConsumerError() {
    String name = "Rahul Lokurte";
    Mono<String> mono =
        Mono.just(name)
            .map(
                s -> {
                  throw new RuntimeException("Testing some error");
                });
    mono.subscribe(s -> log.info("name: {}", s), s -> log.error("Something bad happened"));
    mono.subscribe(s -> log.info("name: {}", s), Throwable::printStackTrace);
    log.info("---------------");
    StepVerifier.create(mono).expectError(RuntimeException.class).verify();
  }

  @Test
  void monoSubscriberConsumerComplete() {
    String name = "Rahul Lokurte";
    Mono<String> mono = Mono.just(name).log().map(String::toUpperCase);
    mono.subscribe(
        s -> log.info("Value {}", s), Throwable::printStackTrace, () -> log.info("FINISHED"));
    log.info("-----------------------------------");
    StepVerifier.create(mono).expectNext(name.toUpperCase()).verifyComplete();
  }

  @Test
  void monoSubscriberSubscription() {
    String name = "Rahul Lokurte";
    Mono<String> mono = Mono.just(name).log();
    mono.subscribe(
        s -> log.info("name: {}", s),
        s -> log.info("some error"),
        () -> log.info("FINISHED"),
        subscription -> subscription.request(2));
    log.info("---------------");
    StepVerifier.create(mono).expectNext(name).verifyComplete();
  }

  @Test
  void monoDoOnMethod() {
    String name = "Rahul Lokurte";
    Mono<String> mono =
        Mono.just(name)
            .log()
            .map(String::toUpperCase)
            .doOnSubscribe(subscription -> log.info("Subscribed"))
            .doOnRequest(longConsumer -> log.info("Request received"))
            .doOnNext(s -> log.info("Value {}", s))
            .flatMap(s -> Mono.just(s + "!"))
            .doOnNext(s -> log.info("Value {}", s))
            .doOnSuccess(s -> log.info("doOnSuccess excuted"));
    mono.subscribe(
        s -> log.info("Value {}", s), Throwable::printStackTrace, () -> log.info("FINISHED"));
    log.info("-----------------------------------");
    StepVerifier.create(mono).expectNext(name.toUpperCase() + "!").verifyComplete();
  }

  @Test
  void monoOnError() {
    Mono<Object> mono =
        Mono.error(new IllegalArgumentException("Illegal Argument Exception"))
            .doOnError(e -> log.error("Error Message" + e.getMessage()))
            .doOnNext(s -> log.info("Executing onNext"))
            .log();
    StepVerifier.create(mono).expectError(IllegalArgumentException.class).verify();
  }

  @Test
  void monoOnErrorResume() {
    String name = "Rahul Lokurte";
    Mono<Object> mono =
        Mono.error(new IllegalArgumentException("Illegal Argument Exception"))
            .doOnError(e -> log.error("Error Message" + e.getMessage()))
            .onErrorResume(
                s -> {
                  log.info("Inside on ErrorResume");
                  return Mono.just(name);
                })
            .log();
    StepVerifier.create(mono).expectNext(name).verifyComplete();
  }

  @Test
  void monoOnErrorReturn() {
    String name = "Rahul Lokurte";
    Mono<Object> mono =
        Mono.error(new IllegalArgumentException("Illegal Argument Exception"))
            .onErrorReturn("EMPTY")
            .onErrorResume(
                s -> {
                  log.info("Inside on ErrorResume");
                  return Mono.just(name);
                })
            .doOnError(e -> log.error("Error Message {}", e.getMessage()))
            .log();
    StepVerifier.create(mono).expectNext("EMPTY").verifyComplete();
  }
}
