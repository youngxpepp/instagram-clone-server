package com.youngxpepp.instagramcloneserver.global.config.security.controller;

import java.util.Arrays;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.config.security.dto.LoginRequestDto;
import com.youngxpepp.instagramcloneserver.global.config.security.dto.LoginResponseDto;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.AccessTokenClaims;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtUtil;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@RestController
@RequiredArgsConstructor
public class SecurityController {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@PostMapping("/api/v1/login")
	public LoginResponseDto login(@Valid @RequestBody LoginRequestDto dto) {
		Member member = memberRepository.findByNickname(dto.getNickname())
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		if (passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
			AccessTokenClaims claims = AccessTokenClaims.builder()
				.memberId(member.getId())
				.roles(Arrays.asList(member.getRole()))
				.build();
			String accessToken = jwtUtil.generateAccessToken(claims);
			return LoginResponseDto.builder()
				.accessToken(accessToken)
				.build();
		}

		throw new BusinessException(ErrorCode.AUTHENTICATION_FAILED);
	}
}
