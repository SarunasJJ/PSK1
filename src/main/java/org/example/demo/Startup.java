package org.example.demo;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;

@Singleton
@jakarta.ejb.Startup
public class Startup {
    @PostConstruct
    public void init() {
        System.out.println("Application started");
    }
}
