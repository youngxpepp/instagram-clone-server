package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.youngxpepp.instagramcloneserver.domain.Member;

public class PostJwtAuthenticationToken extends AbstractAuthenticationToken {

	private Member principal;

	public PostJwtAuthenticationToken(List<SimpleGrantedAuthority> authorities, Member member) {
		super(authorities);
		super.setAuthenticated(true);
		this.principal = member;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}
}
