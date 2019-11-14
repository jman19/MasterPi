package com.masterPi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.Duration;

@SpringBootApplication
@EnableSwagger2
public class Application {

    @Autowired
    private Environment env;

    @Bean
    @Qualifier("slavePiRestTemplate")
    public RestTemplate slavePiRestTemplate(RestTemplateBuilder builder){
        return builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .rootUri(env.getProperty("slavePiUri")).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
