package com.htmx.thymeleaf.users;


import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(unique = true)
    private String email;

    private boolean enabled = true;

    public User (String name, String email){
        this.name = name;
        this.email = email;
    }
}
