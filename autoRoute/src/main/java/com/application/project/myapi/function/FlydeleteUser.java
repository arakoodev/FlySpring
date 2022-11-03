package com.application.project.myapi.function;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.application.project.model.User;
import com.application.project.service.UserServices;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class FlydeleteUser {
    public UserServices userservice;

    public FlydeleteUser(){
        this.userservice= new UserServices();
    }
    
    public Mono<ServerResponse> flypost(ServerRequest request) throws Exception{
        try {
            log.info("Update User API");
            // if(!request.queryParam("id").isPresent()){
            //     return ServerResponse.ok().body(Mono.just("id is Required QueryParameter"),String.class);
            // }
            return request.bodyToMono(User.class).flatMap(req->{
                log.info("This is User Request for Updating: {}", req);
                return userservice.deleteUser(req);
            });
           
            
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.ok().body(Mono.just("Catch an exception"),String.class);
        }
    }
}
