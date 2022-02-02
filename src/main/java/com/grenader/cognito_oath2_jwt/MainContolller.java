package com.grenader.cognito_oath2_jwt;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainContolller {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Java. No authentication needed!";
    }

    @GetMapping("/visitor")
    @PreAuthorize("hasAuthority('user')")
    public String visitor() {
        return "I am authenticated visitor!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('LEVEL_ADMIN')")
    public String admin() {
        return "I am an authenticated admin!";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('LEVEL_USER') OR hasAuthority('LEVEL_ADMIN')")
    public String user() {
        return "I am an authenticated user!";
    }
}
