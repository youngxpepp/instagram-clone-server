package com.youngxpepp.instagramcloneserver.global.config.security.oauth;

import java.io.IOException;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.youngxpepp.instagramcloneserver.dao.MemberRepository;
import com.youngxpepp.instagramcloneserver.domain.Member;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final OAuth2AdditionalStateRepository<OAuth2StateInfo> oAuth2StateInfoRepository;
	private final MemberRepository memberRepository;
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		HttpSession session = request.getSession(false);

		OAuth2StateInfo oAuth2StateInfo = oAuth2StateInfoRepository.removeOAuth2State(request);
		String successRedirectUri = oAuth2StateInfo.getSuccessRedirectUri();
		String signupRedirectUri = oAuth2StateInfo.getSignupRedirectUri();
		String finalRedirectUri = null;

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

			if (UrlUtils.isAbsoluteUrl(successRedirectUri)) {
				finalRedirectUri = UriComponentsBuilder.fromUriString(successRedirectUri)
					.queryParam("sessionId", session.getId())
					.build()
					.toUriString();
			} else {
				finalRedirectUri = successRedirectUri;
			}
		} else {
			if (UrlUtils.isAbsoluteUrl(signupRedirectUri)) {
				finalRedirectUri = UriComponentsBuilder.fromUriString(signupRedirectUri)
					.queryParam("sessionId", session.getId())
					.build()
					.toUriString();
			} else {
				finalRedirectUri = signupRedirectUri;
			}
		}

		redirectStrategy.sendRedirect(request, response, finalRedirectUri);
	}
}
