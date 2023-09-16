package com.carsonlius.controller;

import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/9/12 17:14
 * @company
 * @description
 */
public class TestController {
    public static void main(String[] args) {
//        Flux<Integer> integerFlux = Flux.just(1,2,3,4);
//        integerFlux.subscribe(o->{
//            System.out.println("打印: " + o);
//        });
//
//
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("hello", "world"));
        stringFlux.doFinally(o->{
            System.out.println("this is doFinally method and param is " + o);
        });

        List<String> emptyList = new ArrayList<>();
        stringFlux.subscribe(System.out::println);
        stringFlux.subscribe(o->{
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("第二个订阅:" + o);
            emptyList.add(o);
        });

        if (!emptyList.isEmpty()){
            System.out.println("emptyList不为空" + emptyList.toString());
        }







    }
}
