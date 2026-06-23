package com.bank.loans.infrastructure.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.*;


@Configuration
public class CorsConfig {


    @Bean
    WebMvcConfigurer corsConfigurer(){


        return new WebMvcConfigurer() {


            @Override
            public void addCorsMappings(
                    CorsRegistry registry
            ){


                registry
                    .addMapping("/**")

                    .allowedOrigins(
                        "http://localhost:5173"
                    )

                    .allowedMethods(
                        "*"
                    )

                    .allowedHeaders(
                        "*"
                    );


            }


        };


    }


}