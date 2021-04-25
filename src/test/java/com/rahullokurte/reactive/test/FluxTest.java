package com.rahullokurte.reactive.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class FluxTest {

  @Test
  void generateFlux() {
    Flux<String> flux =
        Flux.generate(
            AtomicInteger::new,
            (state, sink) -> {
              long i = state.getAndIncrement();
              sink.next("3 x " + i + " = " + 3 * i);
              if (i == 10) sink.complete();
              return state;
            },
            (state) -> System.out.println("Last value of state: " + state));

    flux.subscribe(System.out::println);
  }
}
