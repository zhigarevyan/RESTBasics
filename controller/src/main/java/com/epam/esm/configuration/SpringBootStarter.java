package com.epam.esm.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;


@SpringBootApplication
public class SpringBootStarter {
    static ApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(SpringBootStarter.class, args);
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
    }
}
