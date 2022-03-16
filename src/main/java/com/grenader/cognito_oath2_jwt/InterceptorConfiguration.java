package com.grenader.cognito_oath2_jwt;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    private final SecurityContextInterceptor securityContextInterceptor;

    public InterceptorConfiguration(SecurityContextInterceptor securityContextInterceptor) {
        this.securityContextInterceptor = securityContextInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityContextInterceptor);
    }
}