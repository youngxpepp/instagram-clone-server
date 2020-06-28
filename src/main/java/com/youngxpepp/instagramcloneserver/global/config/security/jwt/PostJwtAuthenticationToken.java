package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class PostJwtAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private String email;

    public PostJwtAuthenticationToken(List<SimpleGrantedAuthority> authorities, String email) {
        super(authorities);
        this.email = email;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
