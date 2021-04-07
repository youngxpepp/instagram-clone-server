package com.youngxpepp.instagramcloneserver.global.config.security.oauth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public interface OAuth2AdditionalStateRepository<T> {

	T loadOAuth2State(HttpServletRequest request);

	void saveOAuth2State(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request);

	T removeOAuth2State(HttpServletRequest request);
}
