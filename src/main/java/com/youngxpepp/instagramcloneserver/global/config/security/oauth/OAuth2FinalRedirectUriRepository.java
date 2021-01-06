package com.youngxpepp.instagramcloneserver.global.config.security.oauth;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class OAuth2FinalRedirectUriRepository {

	private static final String FINAL_REDIRECT_URI_ATTR_NAME =
		OAuth2FinalRedirectUriRepository.class.getName() + ".FINAL_REDIRECT_URI";

	private static final String FINAL_REDIRECT_URI_PARAM_NAME = "finalRedirectUri";

	public void saveFinalRedirectUri(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request) {
		String state = authorizationRequest.getState();
		String finalRedirectUri = request.getParameter(FINAL_REDIRECT_URI_PARAM_NAME);
		Assert.hasText(finalRedirectUri, "query parameter final_redirect_uri cannot be empty");
		Map<String, String> finalRedirectUriMap = this.getFinalRedirectUriMap(request);
		finalRedirectUriMap.put(state, finalRedirectUri);
		request.getSession().setAttribute(FINAL_REDIRECT_URI_ATTR_NAME, finalRedirectUriMap);
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

	@SuppressWarnings("unchecked")
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
