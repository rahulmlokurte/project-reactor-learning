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

        StepVerifier.create(mono)
                .expectNext(name)
                .verifyComplete();
    }
}
