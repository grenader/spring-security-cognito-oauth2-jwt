package com.grenader.cognito_oath2_jwt;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EnableWebSecurity
public class JWTCustomSecurityConfiguration extends WebSecurityConfigurerAdapter {

    static class MyJwtAuthenticationConverter extends JwtAuthenticationConverter {
        @Override
        protected Collection<GrantedAuthority> extractAuthorities(final Jwt jwt) {
            List<String> groups = jwt.getClaim("cognito:groups");

            System.out.println("User groups list.size =" + groups.size());
            System.out.println("User groups:");
            groups.forEach(System.out::println);

            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String role : groups) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            return authorities;
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .configurationSource(request -> {
                    var cors = new CorsConfiguration();
                    cors.setAllowedOrigins(List.of("http://localhost:3000"));
                    cors.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS"));
                    cors.setAllowedHeaders(List.of("*"));
                    return cors;
                })
                .and()
                .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .and()
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
                {
                    jwt.jwtAuthenticationConverter(new MyJwtAuthenticationConverter());
                }));
    }

}

