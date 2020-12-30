package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final JwtUtils jwtUtils;
	private final MemberRepository memberRepository;

	@Override
	public Authentication authenticate(Authentication authentication) {
		PreJwtAuthenticationToken preJwtAuthenticationToken = (PreJwtAuthenticationToken)authentication;
		String accessToken = preJwtAuthenticationToken.getAccessToken();

		Jws<Claims> jws = jwtUtils.verifyAccessToken(accessToken);
		Long id = jws.getBody().get("memberId", Long.class);
		List<String> roles = jws.getBody().get("roles", List.class);
		List<SimpleGrantedAuthority> authorities = roles.stream()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		return new PostJwtAuthenticationToken(authorities, member);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return PreJwtAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
