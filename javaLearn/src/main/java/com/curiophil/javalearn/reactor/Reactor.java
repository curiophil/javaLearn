package com.curiophil.javalearn.reactor;

import akka.stream.javadsl.Sink;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class Reactor {


    public static void main(String[] args) {
        Mono<Object> empty = Mono.empty();
        empty.doOnTerminate(() -> System.out.println(true))
                .subscribe(System.out::println);

//        AtomicInteger i = new AtomicInteger(5);
//        Mono<Integer> just = Mono.create(Sink -> {
//            Sink.success(i.getAndIncrement());
//        });
//        System.out.println(i.getAndIncrement());
//        just.subscribe(System.out::println);

//        int j = 5;
//        Mono<Integer> just1 = Mono.just(j++);
//        System.out.println(j++);
//        just1.subscribe(System.out::println);

//        Flux.merge(Flux.interval(Duration.ofMillis(0), Duration.ofMillis(50)).take(5),
//                        Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5))
//                .toStream()
//                .forEach(System.out::println);

//        Flux.just(1, 2).concatWith(Mono.error(new RuntimeException("xxx"))).concatWith(Flux.just(3, 4))
//                .retry(3)
//                .subscribe(System.out::println, e -> System.out.println(e.getMessage()));

//        Flux.create(sink -> {
//                    sink.next(Thread.currentThread().getName());
//                    sink.complete();
//                }).publishOn(Schedulers.single())
//                .map(x ->  String.format("[%s] %s", Thread.currentThread().getName(), x))
//                .publishOn(Schedulers.elastic())
//                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
//                .subscribeOn(Schedulers.parallel())
//                .toStream()
//                .forEach(System.out::println);

//        Flux.just(1, 0).map(x -> 1/x).checkpoint("test").subscribe(System.out::println);

        Object block = Mono.empty().block();
        System.out.println(block);

        AtomicInteger integer = new AtomicInteger(5);
        Mono<Integer> defer = Mono.defer(() -> Mono.just(integer.getAndIncrement()));

        System.out.println(integer.getAndIncrement());
        defer.subscribe(System.out::println);
    }
}
