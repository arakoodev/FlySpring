package com.application.project.myapi.secured.role.editor;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.flyspring.autoroute.FlyRequest;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class FlyeditAccess {
    
        public Mono<ServerResponse> flyget(FlyRequest request) throws Exception{
            try {
                log.info("get editAccess API");
                return ServerResponse.ok().body(Mono.just("You can edit."),String.class);
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.ok().body(Mono.just(e.getMessage()),String.class);
            }
        }
    }