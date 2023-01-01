package com.application.project.services;

import org.springframework.stereotype.Service;

@Service
public class SimpleService {
    

    public Integer getRandomInt(){
        return (int)(Math.random() * Integer.MAX_VALUE);
    }
}
