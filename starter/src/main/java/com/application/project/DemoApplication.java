/**
 * A class that runs a Spring Boot application.
 */

package com.application.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    /**
     * The main method that launches the application.
     *
     * @param args The command-line arguments (optional).
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
