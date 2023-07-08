package com.example.lbs_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class LbsProjectApplication extends SpringBootServletInitializer{

    public static void main(String[] args)  {
        SpringApplication.run(LbsProjectApplication.class, args);

    }

}

