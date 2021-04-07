package com.youngxpepp.instagramcloneserver.global.config.security.oauth;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2StateInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String successRedirectUri;
	private final String signupRedirectUri;

	@Builder
	public OAuth2StateInfo(String successRedirectUri, String signupRedirectUri) {
		this.successRedirectUri = successRedirectUri;
		this.signupRedirectUri = signupRedirectUri;
	}
}
