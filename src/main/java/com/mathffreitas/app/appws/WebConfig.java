package com.mathffreitas.app.appws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //registry.addMapping("/users/email-verification"); -> specific method
        //registry.addMapping("/**"); -> all methods and all controllers
        //registry.addMapping("/**").allowedMethods("GET", "POST", "PUT").allowedOrigins("*");
        registry.addMapping("/**").allowedMethods("*").allowedOrigins("*");
    }
}
