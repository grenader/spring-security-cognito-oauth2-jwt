package com.grenader.cognito_oath2_jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MainController {

    @Autowired
    private ApplicationContext context;

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
    public String user(@RequestHeader("idToken") String idToken) {

        JwtDecoder jwtDecoder = context.getBean(JwtDecoder.class);

        // Sample:
        // "eyJraWQiOiI4NVczejBUMGRlaE5LM0J6cWhFdDR6VTNnMHdkd3lvZjhNeHVwM29qRHZNPSIsImFsZyI6IlJTMjU2In0.eyJhdF9oYXNoIjoibTdTWEJ3WWNkNFkwU09DYUFlVGlQdyIsInN1YiI6Ijk5YjI5NjhlLTA2MjctNDg5Mi1hMjliLWU1YzNlZDJjOWFmOCIsImNvZ25pdG86Z3JvdXBzIjpbIkFDQURFTVlfU1RVREVOVCIsIkxFVkVMX1VTRVIiXSwiZW1haWxfdmVyaWZpZWQiOnRydWUsImlzcyI6Imh0dHBzOlwvXC9jb2duaXRvLWlkcC5jYS1jZW50cmFsLTEuYW1hem9uYXdzLmNvbVwvY2EtY2VudHJhbC0xX2t0M3h0QVFZaCIsInBob25lX251bWJlcl92ZXJpZmllZCI6dHJ1ZSwiY29nbml0bzp1c2VybmFtZSI6InRlc3QxIiwib3JpZ2luX2p0aSI6IjU4ZWEzMzc1LTZkZDQtNGFlNC1iNmMwLTNmNTNiNWI5MzQ2NCIsImF1ZCI6IjVkZGJnZThnYWo1bHYxbmFzdDVybjVyZ2ltIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2NDU1ODE5MDYsInBob25lX251bWJlciI6IisxNjQ3NzAxMzcyNyIsImV4cCI6MTY0NTU4NTUwNiwiaWF0IjoxNjQ1NTgxOTA2LCJqdGkiOiI0MWNlZjUwNy0xNmVhLTQwZmUtOGZmYS1hMzk2YTY5ZjVjODEiLCJlbWFpbCI6InllbGxvdy5zaHJpbXBzQGdtYWlsLmNvbSJ9.DeO5NRYM4JeOngTBncscu-WzI4mmUqJH1vmSJWDHv66Gr-iM0N1GIZdN2er394LHPNMlksCRZnBvmFXTNRHl3rsujWSDgVdDgkzBsODFhUeiba0IxFAQftQWwi4S2SVQz2E8AXZD42CsqofU-uU6w05At36uL0-lBAPlGfPsyCIIdAYJQ8nYg6Lh2syySPZ17AujDKGXeE15wkAjBjQzZbfTTfm2M7iIz_HOG44hbEjtFGT8mirIIR2uPrgDzYW7ZmN2B9BaPbIvT3YQxWefODTbkqVwRhaIx43w4DiAnjuoQ6VUH8vMqWYZGYj2371FBCROTMe8ihyOeb6qTyeHVw"
        Jwt idJWT = jwtDecoder.decode(idToken);

        Map<String, Object> IdClaims = idJWT.getClaims();
        System.out.println("IdToken Claims = " + IdClaims);

        Object email = IdClaims.get("email");
        System.out.println("email = " + email);

        Object phoneNumber = IdClaims.get("phone_number");
        System.out.println("phoneNumber = " + phoneNumber);

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
        }

        return "I am an authenticated user!";
    }

}
