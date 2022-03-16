package com.grenader.cognito_oath2_jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import java.util.*;

@Component
@ConfigurationProperties(prefix = "config")
class JWTIssuersProps {
    private List<String> issuers;

    public List<String> getIssuers() {
        return issuers;
    }

    public void setIssuers(List<String> issuers) {
        this.issuers = issuers;
    }
}

@Configuration
public class JWTCustomSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JWTIssuersProps props;

    Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();

    JwtIssuerAuthenticationManagerResolver authenticationManagerResolver =
            new JwtIssuerAuthenticationManagerResolver(authenticationManagers::get);

    public JWTCustomSecurityConfiguration(JWTIssuersProps props) {
        this.props = props;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        List<String> propsIssuers = props.getIssuers();
        propsIssuers.forEach(issuer -> addManager(authenticationManagers, issuer));

        http.
                // CORS configuration
                cors().configurationSource(request -> {
                    var cors = new CorsConfiguration();
                    cors.setAllowedOrigins(List.of("http://localhost:3000"));
                    cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    cors.setAllowedHeaders(List.of("*"));
                    return cors;
                })

                .and()
                .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .and()
                .oauth2ResourceServer(oauth2ResourceServer -> {
                    oauth2ResourceServer.authenticationManagerResolver(this.authenticationManagerResolver);
                });
    }

    public void addManager(Map<String, AuthenticationManager> authenticationManagers, String issuer) {
        JwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer);
        System.out.println("issuer = " + issuer);

        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new MyJwtAuthenticationConverter());

        JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
        authenticationProvider.setJwtAuthenticationConverter(jwtAuthenticationConverter);
        authenticationManagers.put(issuer, authenticationProvider::authenticate);
    }

    static class MyJwtAuthenticationConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        public Collection<GrantedAuthority> convert(final Jwt jwt) {
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
}
