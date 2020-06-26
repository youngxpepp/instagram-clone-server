package com.youngxpepp.instagramcloneserver.global.config.security.controller;

import com.youngxpepp.instagramcloneserver.domain.member.Member;
import com.youngxpepp.instagramcloneserver.domain.member.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.config.security.dto.LoginRequestDto;
import com.youngxpepp.instagramcloneserver.global.config.security.dto.LoginResponseDto;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.AccessTokenClaims;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtUtil;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class SecurityController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/api/v1/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new BusinessException(ErrorCode.AUTHENTICATION_FAILED));

        if(passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            AccessTokenClaims claims = AccessTokenClaims.builder()
                    .email(member.getEmail())
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