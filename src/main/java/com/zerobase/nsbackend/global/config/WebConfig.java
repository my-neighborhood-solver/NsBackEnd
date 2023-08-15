package com.zerobase.nsbackend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:5173","https://accounts.kakao.com"
                ,"https://kauth.kakao.com","http://localhost:5500")
            .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
            .exposedHeaders(HttpHeaders.LOCATION)
            .maxAge(3000);
    }
}
