package com.application.project.myapi;



import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.application.project.annotation.PathVariableAnnotation;
import com.application.project.autoRoute.ArkRequest;
import com.application.project.autoRoute.ArkRequest;
import com.application.project.service.UserServices;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
@Slf4j
@RestController
public class FlygetUser {
    public UserServices userservice;

    public FlygetUser(){
        this.userservice= new UserServices();
    }
    @PathVariableAnnotation(name = "{id}")
    public Mono<ServerResponse> flyget(ArkRequest request) throws Exception{
        try {

            if(request.getPathVariable("id").isEmpty()){
                return ServerResponse.ok().body(Mono.just("id is Required QueryParameter"),String.class);
            }
            log.info("Request data Id:{}",request.getPathVariable("id"));
            
            String id=request.getPathVariable("id");
           
            return userservice.getUserById(Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.ok().body(Mono.just("Catch an exception"),String.class);
        }
    }
    
}
