package com.grenader.cognito_oath2_jwt;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MainController {

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

        String email = "";
        String phoneNumber = "";

        try {
            // Adding more security reading:
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth instanceof JwtAuthenticationToken) {
                JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;

                Object principal = jwtAuth.getPrincipal();
                System.out.println("principal = " + principal);

                String authName = jwtAuth.getName();
                System.out.println("authName = " + authName);

                Object username = jwtAuth.getTokenAttributes().get("username");
                System.out.println("username = " + username);

                Object details = jwtAuth.getDetails();
                System.out.println("details = " + details);

                if (details instanceof Map) {
                    Map<String, String> IdClaims = (Map<String, String>) details;

                    email = IdClaims.get("email");
                    System.out.println("email = " + email);

                    phoneNumber = IdClaims.get("phone_number");
                    System.out.println("phoneNumber = " + phoneNumber);
                }

            }
        } catch (JwtException e) {
            e.printStackTrace();
        }

        return "I am an authenticated user! Phone number: "+phoneNumber+", email:"+email;
    }

}
