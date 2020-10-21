package com.youngxpepp.instagramcloneserver.global.config.security.oauth;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.youngxpepp.instagramcloneserver.domain.member.model.Google;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.repository.GoogleRepository;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.AccessTokenClaims;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtUtils;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final OAuth2FinalRedirectUriRepository finalRedirectUriRepository;
	private final GoogleRepository googleRepository;
	private final JwtUtils jwtUtils;
	private final SecurityContextRepository repo = new HttpSessionSecurityContextRepository();

	@Override
	@Transactional
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		String finalRedirectUriString = finalRedirectUriRepository.removeFinalRedirectUri(request);
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(finalRedirectUriString);
		OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
		String key = oAuth2AuthenticationToken.getPrincipal().getName();

		Google google = googleRepository.findByKey(key).orElse(null);

		if (google == null) {
			repo.saveContext(SecurityContextHolder.getContext(), request, response);
		} else {
			Member member = google.getMember();
			AccessTokenClaims claims = AccessTokenClaims.builder()
				.memberId(member.getId())
				.roles(Arrays.asList(member.getRole()))
				.build();
			String accessToken = "Bearer " + jwtUtils.generateAccessToken(claims);
			uriBuilder.queryParam("access_token", accessToken);
		}

		response.sendRedirect(uriBuilder.build().toUriString());
	}
}
