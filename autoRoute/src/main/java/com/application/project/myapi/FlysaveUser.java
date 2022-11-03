package com.application.project.myapi;


import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.application.project.model.User;
import com.application.project.service.UserServices;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
@Slf4j
@Validated
@RestController
public class FlysaveUser {
    public UserServices userservice;

    public FlysaveUser(){
        this.userservice= new UserServices();
    }
    
    public Mono<ServerResponse> flypost(ServerRequest request) throws Exception{
        try {
            log.info("Save User API");
            // if(!request.queryParam("id").isPresent()){
            //     return ServerResponse.ok().body(Mono.just("id is Required QueryParameter"),String.class);
            // }
            return request.bodyToMono(User.class).flatMap(req->{
                log.info("This is User Request for saving: {}", req);
                return userservice.saveUser(req);
            });
           
            
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.ok().body(Mono.just("Catch an exception"),String.class);
        }
    }
    
}
