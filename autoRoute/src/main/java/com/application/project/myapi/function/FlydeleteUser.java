package com.application.project.myapi.function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.application.project.annotation.PathVariableAnnotation;
import com.application.project.autoRoute.ArkRequest;
import com.application.project.entity.PersonEntity;
import com.application.project.model.User;
import com.application.project.repository.PersonService;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class FlydeleteUser {
    @Autowired
    public PersonService service;

    @PathVariableAnnotation(name="{id}")
    public Mono<ServerResponse> flypost(ArkRequest request) throws Exception{
        try {
            log.info("Update User API");
            // if(!request.queryParam("id").isPresent()){
            //     return ServerResponse.ok().body(Mono.just("id is Required QueryParameter"),String.class);
            // }
            String id =request.getPathVariable("id");
            return ServerResponse.ok().body(service.deletePerson(Integer.parseInt(id)),JsonNode.class);
           
            
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.ok().body(Mono.just("Catch an exception"),String.class);
        }
    }
}
