package com.application.project.myapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.application.project.services.SimpleService;
import com.flyspring.autoroute.FlyRequest;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class FlyrandomInt {

    @Autowired
    SimpleService simpleService;

    public Mono<ServerResponse> flyget(FlyRequest request) throws Exception{
        try {
            log.info("get randomInt API");
            Integer randomInt = simpleService.getRandomInt();
            log.info("Random int: " + randomInt);
            return ServerResponse.ok().body(Mono.just(randomInt),Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.ok().body(Mono.just(e.getMessage()),String.class);
        }
    }
}
