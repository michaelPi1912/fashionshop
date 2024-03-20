package com.huuphucdang.fashionshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    @Bean
    public WebMvcConfigurer configurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET","POST","DELETE","PUT","OPTIONS")
                        .allowedHeaders("*")
                        .allowedOrigins("http://localhost:3000/","http://localhost:8080/")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}
