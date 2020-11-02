package com.youngxpepp.instagramcloneserver.domain.follow.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.youngxpepp.instagramcloneserver.domain.follow.dto.FollowRequestDto;
import com.youngxpepp.instagramcloneserver.domain.follow.model.Follow;
import com.youngxpepp.instagramcloneserver.domain.follow.repository.FollowRepository;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.AccessTokenClaims;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtUtils;
import com.youngxpepp.instagramcloneserver.test.IntegrationTest;

public class FollowControllerTest extends IntegrationTest {

	private Member principal;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private FollowRepository followRepository;

	@Autowired
	private JwtUtils jwtUtils;

	@BeforeEach
	public void setUp() {
		principal = Member.builder()
			.name("principalName")
			.nickname("principalNickname")
			.password("123123")
			.role(MemberRole.MEMBER)
			.build();
		memberRepository.save(principal);
	}

	@AfterEach
	public void tearDown() {
		// memberRepository.deleteAll();
	}

	@Test
	public void Given_팔로우할상대방생성_When_follow호출_Then_200ok() throws Exception {
		// given
		Member opponent = Member.builder()
			.name("opponentName")
			.nickname("opponentNickname")
			.password("123123")
			.role(MemberRole.MEMBER)
			.build();
		memberRepository.save(opponent);

		AccessTokenClaims accessTokenClaims = AccessTokenClaims.builder()
			.memberId(principal.getId())
			.roles(Arrays.asList(principal.getRole()))
			.build();
		String accessToken = jwtUtils.generateAccessToken(accessTokenClaims);

		// when
		ResultActions resultActions = requestFollow(accessToken, new FollowRequestDto(opponent.getId()));

		// then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("id").isNumber())
			.andExpect(jsonPath("followingMember.id").value(principal.getId()))
			.andExpect(jsonPath("followingMember.nickname").value(principal.getNickname()))
			.andExpect(jsonPath("followingMember.name").value(principal.getName()))
			.andExpect(jsonPath("followingMember.role").value(principal.getRole().name()))
			.andExpect(jsonPath("followedMember.id").value(opponent.getId()))
			.andExpect(jsonPath("followedMember.nickname").value(opponent.getNickname()))
			.andExpect(jsonPath("followedMember.name").value(opponent.getName()))
			.andExpect(jsonPath("followedMember.role").value(opponent.getRole().name()));
	}

	@Test
	public void Given_서로팔로우_When_언팔로우_Then_200ok() throws Exception { // SUPPRESS CHECKSTYLE MethodName
		// given
		Member opponent = Member.builder()
			.name("opponentName")
			.nickname("opponentNickname")
			.password("123123")
			.build();
		memberRepository.save(opponent);

		Follow follow = Follow.builder()
			.followingMember(principal)
			.followedMember(opponent)
			.build();
		followRepository.save(follow);

		AccessTokenClaims accessTokenClaims = AccessTokenClaims.builder()
			.memberId(principal.getId())
			.roles(Arrays.asList(principal.getRole()))
			.build();
		String accessToken = jwtUtils.generateAccessToken(accessTokenClaims);

		// when
		ResultActions resultActions = requestUnfollow(accessToken, follow.getId());

		// then
		resultActions.andExpect(status().isNoContent());
	}

	private ResultActions requestFollow(String accessToken, FollowRequestDto dto) throws Exception {
		return mockMvc.perform(post("/api/v1/follows")
			.header("Authorization", accessToken)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto)));
	}

	private ResultActions requestUnfollow(String accessToken, Long followId) throws Exception {
		return mockMvc.perform(delete("/api/v1/follows/{followId}", followId)
			.header("Authorization", accessToken));
	}
}
