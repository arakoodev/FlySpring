package com.application.project.myapi;

import com.flyspring.autoroute.FlyRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlyrandomWithMono {
    public Mono<ServerResponse> flyget(FlyRequest request) {
        return ServerResponse.ok().body(
            Mono.just(Math.random()), 
            Double.class);
      }
}
