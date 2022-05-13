package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class AppApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:aws.yml";

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);

//        new SpringApplicationBuilder(AppApplication.class)
//                .properties(APPLICATION_LOCATIONS)
//                .run(args);
    }

}
