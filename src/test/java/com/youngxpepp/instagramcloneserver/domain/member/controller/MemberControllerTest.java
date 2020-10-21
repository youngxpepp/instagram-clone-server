package com.youngxpepp.instagramcloneserver.domain.member.controller;

import static org.assertj.core.api.BDDAssertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.youngxpepp.instagramcloneserver.domain.follow.model.Follow;
import com.youngxpepp.instagramcloneserver.domain.follow.repository.FollowRepository;
import com.youngxpepp.instagramcloneserver.domain.member.dto.LoginRequestDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.LoginResponseDto;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtUtils;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.ErrorResponse;
import com.youngxpepp.instagramcloneserver.test.IntegrationTest;

public class MemberControllerTest extends IntegrationTest {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private FollowRepository followRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtils jwtUtils;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	public void Given_MemberABC서로팔로우_When_MemberA조회_Then_MemberA() throws Exception {

		// given
		Member memberA = Member.builder()
			.name("a")
			.nickname("a")
			.password("a")
			.role(MemberRole.MEMBER)
			.build();
		Member memberB = Member.builder()
			.name("b")
			.nickname("b")
			.password("b")
			.role(MemberRole.MEMBER)
			.build();
		Member memberC = Member.builder()
			.name("c")
			.nickname("c")
			.password("c")
			.role(MemberRole.MEMBER)
			.build();
		memberRepository.saveAll(Arrays.asList(memberA, memberB, memberC));

		List<Follow> follows = new ArrayList<>();
		follows.add(Follow.builder()
			.fromMember(memberA)
			.toMember(memberB)
			.build());
		follows.add(Follow.builder()
			.fromMember(memberA)
			.toMember(memberC)
			.build());
		follows.add(Follow.builder()
			.fromMember(memberB)
			.toMember(memberA)
			.build());
		follows.add(Follow.builder()
			.fromMember(memberC)
			.toMember(memberA)
			.build());
		followRepository.saveAll(follows);

		// when
		ResultActions resultActions =
			mockMvc.perform(get("/api/v1/members/{nickname}", memberA.getNickname()));

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("member_nickname").value(memberA.getNickname()))
			.andExpect(jsonPath("member_name").value(memberA.getName()))
			.andExpect(jsonPath("follower_count").value(2L))
			.andExpect(jsonPath("following_count").value(2L));
	}

	@Test
	public void Given_정상회원_When_로그인_Then_토큰발급() throws Exception {
		// given
		String password = "password";
		Member principal = Member.builder()
			.name("name")
			.nickname("nickname")
			.password(this.passwordEncoder.encode(password))
			.role(MemberRole.MEMBER)
			.build();
		this.memberRepository.save(principal);

		// when
		MvcResult mvcResult = this.requestLogin(principal.getNickname(), password);
		LoginResponseDto loginResponseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
			LoginResponseDto.class);
		String accessToken = loginResponseDto.getAccessToken();

		// then
		then(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
		then(jwtUtils.isValidAccessToken(accessToken)).isTrue();
	}

	@Test
	public void Given_정상회원_When_틀린비밀번호_Then_400BadRequest() throws Exception {
		// given
		String password = "password";
		Member principal = Member.builder()
			.name("name")
			.nickname("nickname")
			.password(this.passwordEncoder.encode(password))
			.role(MemberRole.MEMBER)
			.build();
		this.memberRepository.save(principal);

		// when
		MvcResult mvcResult = this.requestLogin(principal.getNickname(), "wrong password");
		ErrorResponse errorResponse = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
			ErrorResponse.class);

		// then
		then(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		then(errorResponse.getError().getCode()).isEqualTo(ErrorCode.WRONG_PASSWORD.getCode());
	}

	@Test
	public void Given__When_없는회원_Then_404NotFound() throws Exception {
		// given

		// when
		MvcResult mvcResult = this.requestLogin("nickname", "password");
		ErrorResponse errorResponse = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
			ErrorResponse.class);

		// then
		then(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		then(errorResponse.getError().getCode()).isEqualTo(ErrorCode.ENTITY_NOT_FOUND.getCode());
	}

	private MvcResult requestLogin(String nickname, String password) throws Exception {
		LoginRequestDto loginRequestDto = LoginRequestDto.builder()
			.nickname(nickname)
			.password(password)
			.build();

		return this.mockMvc.perform(post("/api/v1/members/login")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(this.objectMapper.writeValueAsString(loginRequestDto)))
			.andReturn();
	}
}
