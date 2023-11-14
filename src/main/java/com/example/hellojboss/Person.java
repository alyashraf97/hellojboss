package com.example.hellojboss;
import jakarta.persistence.*;

@Entity
public class Person {
    @Id
    private Long id;
    private String name;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
