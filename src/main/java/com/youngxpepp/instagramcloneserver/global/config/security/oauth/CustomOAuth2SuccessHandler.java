package com.youngxpepp.instagramcloneserver.global.config.security.oauth;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;

import com.youngxpepp.instagramcloneserver.dao.MemberRepository;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.global.util.JwtUtils;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private static final String DEFAULT_FINAL_REDIRECT_URI = "/";
	private static final String SIGNUP_REDIRECT_URI = "/signup";

	private final OAuth2FinalRedirectUriRepository finalRedirectUriRepository;
	private final MemberRepository memberRepository;
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		String finalRedirectUri = finalRedirectUriRepository.removeFinalRedirectUri(request);

		if (finalRedirectUri == null) {
			finalRedirectUri = DEFAULT_FINAL_REDIRECT_URI;
		}

		if (!UrlUtils.isValidRedirectUrl(finalRedirectUri)) {
			finalRedirectUri = DEFAULT_FINAL_REDIRECT_URI;
		}

		OAuth2AuthenticationToken oAuth2Authentication = (OAuth2AuthenticationToken)authentication;
		String registrationId = oAuth2Authentication.getAuthorizedClientRegistrationId();
		String nameAttribute = oAuth2Authentication.getName();

		Member member = memberRepository
			.findByOAuth2NameAttributeAndOAuth2RegistrationId(nameAttribute, registrationId)
			.orElse(null);

		if (member != null) {
			Authentication auth = new AuthenticatedAuthenticationToken(
				Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getValue())),
				member
			);
			SecurityContextHolder.getContext().setAuthentication(auth);
			redirectStrategy.sendRedirect(request, response, finalRedirectUri);
		} else {
			redirectStrategy.sendRedirect(request, response, SIGNUP_REDIRECT_URI);
		}
	}
}
