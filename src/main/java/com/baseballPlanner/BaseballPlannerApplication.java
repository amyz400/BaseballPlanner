package com.baseballPlanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * Created by aziring on 5/9/17.
 */
@SpringBootApplication
@ComponentScan({"com.baseballPlanner.service", "com.baseballPlanner.web"})
public class BaseballPlannerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) { SpringApplication.run(BaseballPlannerApplication.class, args);  }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BaseballPlannerApplication.class);
    }
}
