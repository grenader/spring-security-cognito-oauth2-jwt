package com.grenader.cognito_oath2_jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private SecurityContextInterceptor securityContextInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityContextInterceptor);
    }
}