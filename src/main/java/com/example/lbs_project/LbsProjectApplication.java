package com.example.lbs_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.io.IOException;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class LbsProjectApplication {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(LbsProjectApplication.class, args);
    }

}
