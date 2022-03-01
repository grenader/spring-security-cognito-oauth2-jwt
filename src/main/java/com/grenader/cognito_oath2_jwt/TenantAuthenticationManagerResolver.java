package com.grenader.cognito_oath2_jwt;

import com.nimbusds.jwt.JWTParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class TenantAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {
//    private final BearerTokenResolver resolver = new DefaultBearerTokenResolver();
    private final Map<String, String> tenants = new HashMap<>();

//    private final Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();

    @Autowired
    private JwtDecoder jwtDecoder;

    public TenantAuthenticationManagerResolver() {
        this.tenants.put("one", "https://cognito-idp.ca-central-1.amazonaws.com/ca-central-1_kt3xtAQYh");
        this.tenants.put("two", "https://cognito-idp.ca-central-1.amazonaws.com/ca-central-1_kt3xtAQYh");
    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest request) {
        return Optional.ofNullable(this.tenants.get("one"))
                .map(JwtDecoders::fromIssuerLocation)
                .map( t -> new JwtAuthenticationProvider(this.jwtDecoder))
                .orElseThrow(() -> new IllegalArgumentException("unknown tenant"))::authenticate;
    }


}
