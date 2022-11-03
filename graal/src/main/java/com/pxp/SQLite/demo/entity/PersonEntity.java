package com.pxp.SQLite.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PersonEntity {
    @Id
    private int id;

    private String name;

    private String email;

    public PersonEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
