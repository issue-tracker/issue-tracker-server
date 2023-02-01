package com.ahoo.issuetrackerserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000"
                , "https://issue-tracker-web.monster"
                , "https://front.issue-tracker.link")
            .allowedMethods("PATCH", "POST", "GET", "PUT", "OPTIONS", "DELETE", "HEAD")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
