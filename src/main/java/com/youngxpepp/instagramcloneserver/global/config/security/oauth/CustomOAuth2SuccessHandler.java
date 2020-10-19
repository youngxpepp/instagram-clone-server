package com.youngxpepp.instagramcloneserver.global.config.security.oauth;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> customOAuth2AuthorizationRequestRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		if (CustomOAuth2AuthorizationRequestRepository.class.isAssignableFrom(
			customOAuth2AuthorizationRequestRepository.getClass())) {

			CustomOAuth2AuthorizationRequestRepository repository =
				(CustomOAuth2AuthorizationRequestRepository)customOAuth2AuthorizationRequestRepository;
			String finalRedirectUri = repository.removeFinalRedirectUri(request);
			response.sendRedirect(finalRedirectUri);
		}
	}
}
