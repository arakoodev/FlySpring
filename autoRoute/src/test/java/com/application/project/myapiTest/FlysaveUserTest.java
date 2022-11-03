package com.application.project.myapiTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.application.project.model.User;
import com.application.project.myapi.FlysaveUser;
import com.application.project.service.UserServices;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@WebFluxTest(FlysaveUser.class)
@ContextConfiguration(classes = {
    FlysaveUser.class,
    UserServices.class
  })
public class FlysaveUserTest {

    @MockBean
    private UserServices userServices;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private FlysaveUser flysaveUser;


    @Test
    @WithMockUser(username = "lezter",password = "12345")
    public void FlySaveUserTest() throws Exception{

        User user = new User();
        user.setAge("5");
        user.setGender("Male");
        user.setFirstName("Danilo");
        user.setLastName("Adams");

        MockServerRequest ms =  MockServerRequest.builder().body(Mono.just(user));
        webTestClient.mutate().baseUrl("localhost:8080");
            
        Mono<ServerResponse> response = flysaveUser.flypost(ms);

        StepVerifier.create(response).expectSubscription().assertNext(res->{
            assertNotNull(res);
        }).expectComplete().verify();
        
    }
    
}
