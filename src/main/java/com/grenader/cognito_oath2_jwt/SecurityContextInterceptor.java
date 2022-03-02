package com.grenader.cognito_oath2_jwt;


import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Service
public class SecurityContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        String idToken = request.getHeader("idToken");

        SecurityContext sec = SecurityContextHolder.getContext();
        JwtAuthenticationToken auth = (JwtAuthenticationToken) sec.getAuthentication();

        try {
            String currentIssuerUri = (String) auth.getTokenAttributes().get("iss");
            JwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(currentIssuerUri);

            Jwt idJWT = jwtDecoder.decode(idToken);
            Map<String, Object> claims = idJWT.getClaims();
            auth.setDetails(claims);
        } catch (JwtException e) {
            e.printStackTrace();
        }
        return true;
    }
}
