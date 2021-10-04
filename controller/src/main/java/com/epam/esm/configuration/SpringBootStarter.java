package com.epam.esm.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class SpringBootStarter extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStarter.class, args);
    }
}
