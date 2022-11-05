package com.application.project.myapiTest;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.application.project.autoRoute.ArkRequest;
import com.application.project.myapi.FlygetUser;
import com.application.project.service.UserServices;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@WebFluxTest(FlygetUser.class)
@ContextConfiguration(classes = {
    FlygetUser.class,
    UserServices.class
  })
public class FlygetUserTest {

    @MockBean
    private UserServices userServices;

    @Autowired
    private FlygetUser flygetUser;


    @Test
    @WithMockUser(username = "lezter",password = "12345")
    public void FlyGetUserTest() throws Exception{
        // RequestBuilder requestBuilder = new 
        MockServerRequest ms =  MockServerRequest.builder().pathVariable("id", "1").body(null);
        ArkRequest request = new ArkRequest(ms);

        Mono<ServerResponse> response = flygetUser.flyget(request);

        StepVerifier.create(response).expectSubscription().assertNext(res->{
            assertNotNull(res);
        }).expectComplete().verify();
    }
    
}
