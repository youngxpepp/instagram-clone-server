package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.event.AuthenticationFailureServiceExceptionEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PreJwtAuthenticationToken preJwtAuthenticationToken = (PreJwtAuthenticationToken) authentication;
        String accessToken = preJwtAuthenticationToken.getAccessToken();

        Jws<Claims> jws = jwtUtil.verifyAccessToken(accessToken);
        String email = jws.getBody().get("email", String.class);
        List<String> roles = jws.getBody().get("roles", List.class);
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        return new PostJwtAuthenticationToken(authorities, email);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreJwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
