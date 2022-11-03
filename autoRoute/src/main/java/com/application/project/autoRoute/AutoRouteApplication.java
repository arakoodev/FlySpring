package com.application.project.autoRoute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")
@SpringBootApplication
public class AutoRouteApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoRouteApplication.class, args);
	}

}
