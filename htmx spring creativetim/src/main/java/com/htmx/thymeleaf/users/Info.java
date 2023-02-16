package com.htmx.thymeleaf.users;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "info")
@Getter @Setter
@NoArgsConstructor
public class Info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String date;
    private Integer userCreated;
    private Integer userDeleted;

    public Info (String d, Integer cr, Integer de){
        this.date = d;
        this.userCreated = cr;
        this.userDeleted = de;
    }
}
