package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class PostJwtAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private Member member;

    public PostJwtAuthenticationToken(List<SimpleGrantedAuthority> authorities, Member member) {
        super(authorities);
        this.member = member;
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
