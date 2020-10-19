package com.youngxpepp.instagramcloneserver.global.config.security.oauth;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CustomOAuth2AuthorizationRequestRepository
	implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	private static final String DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME =
		CustomOAuth2AuthorizationRequestRepository.class.getName() + ".AUTHORIZATION_REQUEST";

	private static final String FINAL_REDIRECT_URI_ATTR_NAME =
		CustomOAuth2AuthorizationRequestRepository.class.getName() + ".FINAL_REDIRECT_URI";

	private static final String FINAL_REDIRECT_URI_PARAM_NAME = "final_redirect_uri";

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		Assert.notNull(request, "request cannot be null");
		String stateParameter = this.getStateParameter(request);
		if (stateParameter == null) {
			return null;
		}
		Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
		return authorizationRequests.get(stateParameter);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
		HttpServletResponse response) {
		Assert.notNull(request, "request cannot be null");
		Assert.notNull(response, "response cannot be null");
		if (authorizationRequest == null) {
			this.removeAuthorizationRequest(request, response);
			return;
		}
		String state = authorizationRequest.getState();
		Assert.hasText(state, "authorizationRequest.state cannot be empty");

		this.saveFinalRedirectUri(authorizationRequest, request);

		Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
		authorizationRequests.put(state, authorizationRequest);
		request.getSession().setAttribute(DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME, authorizationRequests);
	}

	@Override
	@Deprecated
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		return null;
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
		HttpServletResponse response) {
		Assert.notNull(request, "request cannot be null");
		Assert.notNull(response, "response cannot be null");
		String stateParameter = this.getStateParameter(request);
		if (stateParameter == null) {
			return null;
		}
		Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
		OAuth2AuthorizationRequest originalRequest = authorizationRequests.remove(stateParameter);
		if (!authorizationRequests.isEmpty()) {
			request.getSession().setAttribute(DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME, authorizationRequests);
		} else {
			request.getSession().removeAttribute(DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME);
		}
		return originalRequest;
	}

	public String removeFinalRedirectUri(HttpServletRequest request) {
		String stateParameter = this.getStateParameter(request);
		if (stateParameter == null) {
			return null;
		}
		Map<String, String> finalRedirectUriMap = this.getFinalRedirectUriMap(request);
		String originalFinalRedirectUri = finalRedirectUriMap.remove(stateParameter);
		if (finalRedirectUriMap.isEmpty()) {
			request.getSession().removeAttribute(FINAL_REDIRECT_URI_ATTR_NAME);
		} else {
			request.getSession().setAttribute(FINAL_REDIRECT_URI_ATTR_NAME, finalRedirectUriMap);
		}
		return originalFinalRedirectUri;
	}

	private String getStateParameter(HttpServletRequest request) {
		return request.getParameter(OAuth2ParameterNames.STATE);
	}

	private Map<String, OAuth2AuthorizationRequest> getAuthorizationRequests(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Map<String, OAuth2AuthorizationRequest> authorizationRequests = session == null ? null :
			(Map<String, OAuth2AuthorizationRequest>)session.getAttribute(DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME);
		if (authorizationRequests == null) {
			return new HashMap<>();
		}
		return authorizationRequests;
	}

	private void saveFinalRedirectUri(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request) {
		String state = authorizationRequest.getState();
		String finalRedirectUri = request.getParameter(FINAL_REDIRECT_URI_PARAM_NAME);
		Assert.hasText(finalRedirectUri, "query parameter final_redirect_uri cannot be empty");
		Map<String, String> finalRedirectUriMap = this.getFinalRedirectUriMap(request);
		finalRedirectUriMap.put(state, finalRedirectUri);
		request.getSession().setAttribute(FINAL_REDIRECT_URI_ATTR_NAME, finalRedirectUriMap);
	}

	private Map<String, String> getFinalRedirectUriMap(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Map<String, String> finalRedirectUriMap = session == null ? null :
			(Map<String, String>)session.getAttribute(FINAL_REDIRECT_URI_ATTR_NAME);
		if (finalRedirectUriMap == null) {
			return new HashMap<>();
		}

		return finalRedirectUriMap;
	}
}
