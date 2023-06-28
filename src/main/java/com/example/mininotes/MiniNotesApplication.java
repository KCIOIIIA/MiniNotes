package com.example.mininotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MiniNotesApplication {

    public static void main(String[] args) {
        System.setProperty("server.port", "8081");
        SpringApplication.run(MiniNotesApplication.class, args);
    }

}
