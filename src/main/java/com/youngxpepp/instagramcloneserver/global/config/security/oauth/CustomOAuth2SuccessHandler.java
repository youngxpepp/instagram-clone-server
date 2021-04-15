package com.youngxpepp.instagramcloneserver.global.config.security.oauth;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
import com.youngxpepp.instagramcloneserver.global.config.security.login.MemberDetails;

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
			MemberDetails memberDetails = MemberDetails.from(member);
			Authentication auth = new UsernamePasswordAuthenticationToken(
				memberDetails,
				memberDetails.getPassword(),
				memberDetails.getAuthorities()
			);
			SecurityContextHolder.getContext().setAuthentication(auth);

			finalRedirectUri = getRedirectUrl(successRedirectUri, session.getId());
		} else {
			finalRedirectUri = getRedirectUrl(signupRedirectUri, session.getId());
		}

		redirectStrategy.sendRedirect(request, response, finalRedirectUri);
	}

	private String getRedirectUrl(String url, String sessionId) {
		if (UrlUtils.isAbsoluteUrl(url)) {
			return UriComponentsBuilder.fromUriString(url)
				.queryParam("sessionId", sessionId)
				.build()
				.toUriString();
		}
		return url;
	}
}
