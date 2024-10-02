package org.example.sports;

import org.springframework.boot.SpringApplication;

public class TestSportsApplication {

    public static void main(String[] args) {
        SpringApplication.from(SportsApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
