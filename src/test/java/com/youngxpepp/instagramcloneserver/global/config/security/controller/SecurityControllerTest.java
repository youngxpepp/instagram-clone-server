package com.youngxpepp.instagramcloneserver.global.config.security.controller;

import com.youngxpepp.instagramcloneserver.domain.member.Member;
import com.youngxpepp.instagramcloneserver.domain.member.MemberRepository;
import com.youngxpepp.instagramcloneserver.domain.member.MemberRole;
import com.youngxpepp.instagramcloneserver.global.config.security.dto.LoginRequestDto;
import com.youngxpepp.instagramcloneserver.global.config.security.dto.LoginResponseDto;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtUtil;
import com.youngxpepp.instagramcloneserver.test.IntegrationTest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class SecurityControllerTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void Given_계정생성_When_로그인시도_Then_AccessToken검증() throws Exception {
//        given
        Member member = Member.builder()
                .nickname("testNickname")
                .name("testName")
                .email("test@gmail.com")
                .password(passwordEncoder.encode("123123"))
                .role(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);
        LoginRequestDto requestDto = LoginRequestDto.builder()
                .email(member.getEmail())
                .password("123123")
                .build();

//        when
        MvcResult result = requestLogin(requestDto);
        String content = result.getResponse().getContentAsString();
        LoginResponseDto responseDto = objectMapper.readValue(content, LoginResponseDto.class);
        Jws<Claims> jws = jwtUtil.verifyAccessToken(responseDto.getAccessToken());

//        then
        assertThat(jws.getBody().get("email"))
                .isEqualTo(member.getEmail());
        assertThat(jws.getBody().get("roles"))
                .isEqualTo(Arrays.asList(member.getRole().getName()));

    }

    private MvcResult requestLogin(LoginRequestDto dto) throws Exception {
        return mockMvc.perform(post("/api/v1/login")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
    }
}