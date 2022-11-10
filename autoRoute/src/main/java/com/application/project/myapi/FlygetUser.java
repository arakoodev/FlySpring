package com.application.project.myapi;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.application.project.annotation.PathVariableAnnotation;
import com.application.project.autoRoute.ArkRequest;
import com.application.project.entity.PersonEntity;
import com.application.project.repository.PersonRepo;
import com.application.project.repository.PersonService;
import com.application.project.autoRoute.ArkRequest;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
@Slf4j
@RestController
public class FlygetUser {
    
    @Autowired
    public PersonService service;

    // public FlygetUser(){
    //     this.service= SpringContext.getBean(PersonService.class);
    // }
    @PathVariableAnnotation(name = "{id}")
    public Mono<ServerResponse> flyget(ArkRequest request) throws Exception{
        try {

            if(request.getPathVariable("id").isEmpty()){
                return ServerResponse.ok().body(Mono.just("id is Required QueryParameter"),String.class);
            }
            log.info("Request data Id:{}",request.getPathVariable("id"));
            
            String id=request.getPathVariable("id");
            Mono<JsonNode> person = this.service.getPersonByid(Integer.parseInt(id));

            return ServerResponse.ok().body(person,JsonNode.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.ok().body(Mono.just("Catch an exception"),String.class);
        }
    }
    
}
