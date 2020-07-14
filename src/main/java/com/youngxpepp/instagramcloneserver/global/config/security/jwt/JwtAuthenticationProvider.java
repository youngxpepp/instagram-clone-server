package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Component
@AllArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private JwtUtil jwtUtil;
	private MemberRepository memberRepository;

	@Override
	public Authentication authenticate(Authentication authentication)
		throws AuthenticationException, JwtException, BusinessException {
		PreJwtAuthenticationToken preJwtAuthenticationToken = (PreJwtAuthenticationToken)authentication;
		String accessToken = preJwtAuthenticationToken.getAccessToken();

		Jws<Claims> jws = jwtUtil.verifyAccessToken(accessToken);
		String email = jws.getBody().get("email", String.class);
		List<String> roles = jws.getBody().get("roles", List.class);
		List<SimpleGrantedAuthority> authorities = roles.stream()
			.map(role -> new SimpleGrantedAuthority(role))
			.collect(Collectors.toList());

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		return new PostJwtAuthenticationToken(authorities, member);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return PreJwtAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
