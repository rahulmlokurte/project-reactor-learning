package com.rahullokurte.reactive.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FunctionalInterfaceTest {

  @Test
  void whenNamesPresentConsumeAll() {
    Consumer<String> printConsumer = t -> System.out.println(t);
    Stream<String> cities = Stream.of("Bengaluru", "Delhi", "Mumbai", "Chennai");
    cities.forEach(printConsumer);
  }

  @Test
  void whenNamesPresentUseBothConsumer() {
    Stream<String> cities = Stream.of("Bengaluru", "Delhi", "Mumbai", "Chennai");
    Consumer<List<String>> upperCased = list -> list.forEach(String::toUpperCase);
    Consumer<List<String>> print = list -> list.forEach(System.out::println);
    upperCased.andThen(print).accept(cities.collect(Collectors.toList()));
  }

  @Test
  void supplier() {
    Supplier<Double> doubleSupplier1 = () -> Math.random();
    DoubleSupplier doubleSupplier2 = Math::random;
    System.out.println(doubleSupplier1.get());
    System.out.println(doubleSupplier2.getAsDouble());
  }

  @Test
  void supplierWithOptional() {
    Supplier<Double> doubleSupplier = Math::random;
    Optional<Double> optionalDouble = Optional.empty();
    System.out.println(optionalDouble.orElseGet(doubleSupplier));
  }

  @Test
  void testPredicate() {
    List<String> names = Arrays.asList("John", "Smith", "Samueal", "Catley", "Sie");
    Predicate<String> nameStartsWithS = str -> str.startsWith("S");
    names.stream().filter(nameStartsWithS).forEach(System.out::println);
  }

  @Test
  void testPredicateAndComposition() {
    List<String> names = Arrays.asList("John", "Smith", "Samueal", "Catley", "Sie");
    Predicate<String> startPredicate = str -> str.startsWith("S");
    Predicate<String> lengthPredicate = str -> str.length() >= 5;
    names.stream().filter(startPredicate.and(lengthPredicate)).forEach(System.out::println);
  }

  @Test
  void testFunctions() {
    List<String> names = Arrays.asList("Smith", "Gourav", "Heather", "John", "Catania");
    Function<String, Integer> nameMappingFunction = String::length;
    names.stream().map(nameMappingFunction).forEach(System.out::println);
  }
}
