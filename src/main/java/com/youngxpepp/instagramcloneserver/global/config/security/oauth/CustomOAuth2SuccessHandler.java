package com.youngxpepp.instagramcloneserver.global.config.security.oauth;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final OAuth2FinalRedirectUriRepository finalRedirectUriRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		String finalRedirectUri = finalRedirectUriRepository.removeFinalRedirectUri(request);
		response.sendRedirect(finalRedirectUri);
	}
}
