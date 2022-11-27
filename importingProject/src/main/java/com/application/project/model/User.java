package com.application.project.model;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String age;
    private String gender;
    
}
