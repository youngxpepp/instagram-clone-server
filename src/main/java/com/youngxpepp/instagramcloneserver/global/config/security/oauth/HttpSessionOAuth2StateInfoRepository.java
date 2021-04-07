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
public class HttpSessionOAuth2StateInfoRepository
	implements OAuth2AdditionalStateRepository<OAuth2StateInfo> {

	private static final String OAUTH2_STATE_INFO_MAP_ATTR_NAME = HttpSessionOAuth2StateInfoRepository.class.getName();
	private static final String SUCCESS_REDIRECT_URI_PARAM_NAME = "successRedirectUri";
	private static final String SIGNUP_REDIRECT_URI_PARAM_NAME = "signupRedirectUri";

	@Override
	public OAuth2StateInfo loadOAuth2State(HttpServletRequest request) {
		String state = getStateParameter(request);
		if (state == null) {
			return null;
		}
		Map<String, OAuth2StateInfo> map = getOAuth2StateInfoMap(request);
		return map.get(state);
	}

	@Override
	public void saveOAuth2State(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request) {
		String state = authorizationRequest.getState();
		String successRedirectUri = request.getParameter(SUCCESS_REDIRECT_URI_PARAM_NAME);
		String signupRedirectUri = request.getParameter(SIGNUP_REDIRECT_URI_PARAM_NAME);

		Assert.hasText(state, "state cannot be empty");
		Assert.hasText(successRedirectUri, "successRedirectUri cannot be empty");
		Assert.hasText(signupRedirectUri, "signupRedirectUri cannot be empty");

		OAuth2StateInfo oAuth2StateInfo = OAuth2StateInfo.builder()
			.successRedirectUri(successRedirectUri)
			.signupRedirectUri(signupRedirectUri)
			.build();

		Map<String, OAuth2StateInfo> map = getOAuth2StateInfoMap(request);
		map.put(state, oAuth2StateInfo);

		request.getSession().setAttribute(OAUTH2_STATE_INFO_MAP_ATTR_NAME, map);
	}

	@Override
	public OAuth2StateInfo removeOAuth2State(HttpServletRequest request) {
		String state = getStateParameter(request);
		if (state == null) {
			return null;
		}

		Map<String, OAuth2StateInfo> map = getOAuth2StateInfoMap(request);
		OAuth2StateInfo oAuth2StateInfo = map.get(state);
		map.remove(state);

		if (map.isEmpty()) {
			request.getSession(false).removeAttribute(OAUTH2_STATE_INFO_MAP_ATTR_NAME);
		} else {
			request.getSession(false).setAttribute(OAUTH2_STATE_INFO_MAP_ATTR_NAME, map);
		}

		return oAuth2StateInfo;
	}

	@SuppressWarnings("unchecked")
	private Map<String, OAuth2StateInfo> getOAuth2StateInfoMap(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Map<String, OAuth2StateInfo> oAuth2StateInfoMap = session == null ? null :
			(Map<String, OAuth2StateInfo>)session.getAttribute(OAUTH2_STATE_INFO_MAP_ATTR_NAME);
		if (oAuth2StateInfoMap == null) {
			return new HashMap<>();
		}
		return oAuth2StateInfoMap;
	}

	private String getStateParameter(HttpServletRequest request) {
		return request.getParameter(OAuth2ParameterNames.STATE);
	}
}
