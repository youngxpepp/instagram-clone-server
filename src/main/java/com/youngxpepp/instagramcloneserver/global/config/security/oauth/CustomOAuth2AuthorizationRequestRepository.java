package com.youngxpepp.instagramcloneserver.global.config.security.oauth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2AuthorizationRequestRepository
	implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> delegate
		= new HttpSessionOAuth2AuthorizationRequestRepository();
	private final OAuth2AdditionalStateRepository<OAuth2StateInfo> oAuth2StateInfoRepository;

	public CustomOAuth2AuthorizationRequestRepository(
		OAuth2AdditionalStateRepository<OAuth2StateInfo> oAuth2StateInfoRepository
	) {
		this.oAuth2StateInfoRepository = oAuth2StateInfoRepository;
	}

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		return delegate.loadAuthorizationRequest(request);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
		HttpServletResponse response) {
		delegate.saveAuthorizationRequest(authorizationRequest, request, response);
		oAuth2StateInfoRepository.saveOAuth2State(authorizationRequest, request);
	}

	@Override
	@Deprecated
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		return null;
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
		HttpServletResponse response) {
		return delegate.removeAuthorizationRequest(request, response);
	}
}
