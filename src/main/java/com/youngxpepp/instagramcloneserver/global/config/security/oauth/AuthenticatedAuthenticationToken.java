package com.youngxpepp.instagramcloneserver.global.config.security.oauth;

import java.util.Collection;
import java.util.Collections;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.youngxpepp.instagramcloneserver.domain.Member;

public class AuthenticatedAuthenticationToken extends AbstractAuthenticationToken {

	@Getter
	private Member member;

	public AuthenticatedAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Member member) {
		super(authorities);
		setAuthenticated(true);
		this.member = member;
	}

	public static AuthenticatedAuthenticationToken from(Member member) {
		return new AuthenticatedAuthenticationToken(
			Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getValue())),
			member
		);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return member;
	}
}
