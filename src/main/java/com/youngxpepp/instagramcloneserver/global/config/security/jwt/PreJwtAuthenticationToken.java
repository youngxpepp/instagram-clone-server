package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class PreJwtAuthenticationToken extends AbstractAuthenticationToken {

	@Getter
	private String accessToken;

	public PreJwtAuthenticationToken(String accessToken) {
		super(null);
		this.accessToken = accessToken;
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
