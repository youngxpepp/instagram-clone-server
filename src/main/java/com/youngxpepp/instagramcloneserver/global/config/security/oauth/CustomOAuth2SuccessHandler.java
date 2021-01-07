package com.youngxpepp.instagramcloneserver.global.config.security.oauth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.AccessTokenClaims;
import com.youngxpepp.instagramcloneserver.global.util.JwtUtils;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final OAuth2FinalRedirectUriRepository finalRedirectUriRepository;
	private final MemberRepository memberRepository;
	private final JwtUtils jwtUtils;
	private final SecurityContextRepository repo = new HttpSessionSecurityContextRepository();

	@Override
	@Transactional
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		String finalRedirectUriString = finalRedirectUriRepository.removeFinalRedirectUri(request);
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(finalRedirectUriString);
		OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
		String email = oAuth2AuthenticationToken.getPrincipal().getAttribute("email");

		Member member = memberRepository.findByEmail(email).orElse(null);

		if (member == null) {
			repo.saveContext(SecurityContextHolder.getContext(), request, response);
		} else {
			List<MemberRole> roles = new ArrayList<>();
			Optional.ofNullable(member.getRole()).ifPresent(roles::add);
			AccessTokenClaims claims = AccessTokenClaims.ofMemberRoleList(member.getId(), roles);
			String accessToken = jwtUtils.generateAccessToken(claims);
			uriBuilder.queryParam("accessToken", accessToken);
		}

		response.sendRedirect(uriBuilder.build().toUriString());
	}
}
