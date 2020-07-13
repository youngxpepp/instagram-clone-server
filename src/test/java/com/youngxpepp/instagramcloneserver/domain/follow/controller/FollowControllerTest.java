package com.youngxpepp.instagramcloneserver.domain.follow.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.youngxpepp.instagramcloneserver.domain.follow.dto.FollowRequestDto;
import com.youngxpepp.instagramcloneserver.domain.follow.dto.UnfollowRequestDto;
import com.youngxpepp.instagramcloneserver.domain.follow.model.Follow;
import com.youngxpepp.instagramcloneserver.domain.follow.repository.FollowRepository;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.AccessTokenClaims;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtUtil;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;
import com.youngxpepp.instagramcloneserver.test.IntegrationTest;

public class FollowControllerTest extends IntegrationTest {

	private Member principal;
	private String accessToken;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private FollowRepository followRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@BeforeEach
	public void setUp() {
		principal = Member.builder()
			.name("principalName")
			.nickname("principalNickname")
			.email("principal@gmail.com")
			.password("123123")
			.role(MemberRole.MEMBER)
			.build();
		memberRepository.save(principal);

		AccessTokenClaims accessTokenClaims = AccessTokenClaims.builder()
			.email(principal.getEmail())
			.roles(Arrays.asList(principal.getRole()))
			.build();
		accessToken = jwtUtil.generateAccessToken(accessTokenClaims);
	}

	@AfterEach
	public void tearDown() {
		//        memberRepository.deleteAll();
	}

	@Test
	// CHECKSTYLE:OFF
	public void Given_팔로우할상대방생성_When_follow호출_Then_200ok_Follow생성확인() throws Exception {
		// CHECKSTYLE:ON
		//        given
		Member opponent = Member.builder()
			.name("opponentName")
			.nickname("opponentNickname")
			.email("opponent@gmail.com")
			.password("123123")
			.role(MemberRole.MEMBER)
			.build();
		memberRepository.save(opponent);

		//        when
		FollowRequestDto dto = FollowRequestDto.builder()
			.memberNickname(opponent.getNickname())
			.build();
		ResultActions resultActions = requestFollow(dto);

		Follow follow = followRepository.findByFromMemberIdAndToMemberId(principal.getId(), opponent.getId())
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		//        then
		resultActions
			.andExpect(status().isOk());
		assertThat(follow.getFromMember().getId())
			.isEqualTo(principal.getId());
		assertThat(follow.getToMember().getId())
			.isEqualTo(opponent.getId());
	}

	@Test
	public void Given_서로팔로우_When_언팔로우_Then_200ok_팔로삭제확인() throws Exception { // SUPPRESS CHECKSTYLE MethodName
		//        given
		Member opponent = Member.builder()
			.name("opponentName")
			.nickname("opponentNickname")
			.email("opponent@gmail.com")
			.password("123123")
			.build();
		memberRepository.save(opponent);

		Follow follow = Follow.builder()
			.fromMember(principal)
			.toMember(opponent)
			.build();
		followRepository.save(follow);

		//        when
		UnfollowRequestDto dto = UnfollowRequestDto.builder()
			.memberNickname(opponent.getNickname())
			.build();
		ResultActions resultActions = requestUnfollow(dto);
		Optional<Follow> resultFollow = followRepository.findByFromMemberIdAndToMemberId(principal.getId(),
			opponent.getId());

		//        then
		resultActions.andExpect(status().isOk());
		assertThat(resultFollow).isEqualTo(Optional.empty());
	}

	private ResultActions requestFollow(FollowRequestDto dto) throws Exception {
		return mockMvc.perform(post("/api/v1/follows")
			.header("Authorization", accessToken)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto)));
	}

	private ResultActions requestUnfollow(UnfollowRequestDto dto) throws Exception {
		return mockMvc.perform(delete("/api/v1/follows")
			.header("Authorization", accessToken)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto)));
	}
}
