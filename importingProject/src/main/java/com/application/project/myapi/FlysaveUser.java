package com.application.project.myapi;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.application.project.entity.PersonEntity;
import com.application.project.repository.PersonService;
import com.fasterxml.jackson.databind.JsonNode;
import com.flyspring.autoroute.ArkRequest;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
@Slf4j
@Validated
@RestController
public class FlysaveUser {
    @Autowired
    public PersonService service;
    
    public Mono<ServerResponse> flypost(ArkRequest request) throws Exception{
        try {
            log.info("Save User API");
             return request.bodyToMono(PersonEntity.class).flatMap(req->{
                log.info("This is User Request for saving: {}", req);
                service.savePerson(req);
                return ServerResponse.ok().body(service.savePerson(req),JsonNode.class);
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.ok().body(Mono.just("Catch an exception"),String.class);
        }
    }
    
}
