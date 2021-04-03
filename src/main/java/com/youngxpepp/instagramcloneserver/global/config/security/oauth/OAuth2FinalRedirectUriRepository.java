package com.youngxpepp.instagramcloneserver.global.config.security.oauth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public interface OAuth2FinalRedirectUriRepository {

	void saveFinalRedirectUri(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request);

	String removeFinalRedirectUri(HttpServletRequest request);
}
