package com.rahullokurte.reactive.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
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

        StepVerifier.create(mono)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    void monoSubscriberConsumer() {
        String name = "Rahul Lokurte";
        Mono<String> mono = Mono.just(name).log();
        mono.subscribe(s -> log.info("name: {}",s));
        log.info("---------------");

        StepVerifier.create(mono)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    void monoSubscriberConsumerError() {
        String name = "Rahul Lokurte";
        Mono<String> mono = Mono.just(name)
                .map(s -> {throw new RuntimeException("Testing some error");});
        mono.subscribe(s -> log.info("name: {}",s), s-> log.error("Something bad happened" ));
        mono.subscribe(s -> log.info("name: {}",s), Throwable::printStackTrace);
        log.info("---------------");

        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();
    }
}
