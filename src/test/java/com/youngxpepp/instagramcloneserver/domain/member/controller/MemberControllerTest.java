package com.youngxpepp.instagramcloneserver.domain.member.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import com.youngxpepp.instagramcloneserver.domain.follow.model.Follow;
import com.youngxpepp.instagramcloneserver.domain.follow.repository.FollowRepository;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.test.IntegrationTest;

class MemberControllerTest extends IntegrationTest {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private FollowRepository followRepository;

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
			.email("a@gmail.com")
			.name("a")
			.nickname("a")
			.password("a")
			.role(MemberRole.MEMBER)
			.build();
		Member memberB = Member.builder()
			.email("b@gmail.com")
			.name("b")
			.nickname("b")
			.password("b")
			.role(MemberRole.MEMBER)
			.build();
		Member memberC = Member.builder()
			.email("c@gmail.com")
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
			mockMvc.perform(get("/api/v1/members/{memberNickname}", memberA.getNickname()));

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("member_nickname").value(memberA.getNickname()))
			.andExpect(jsonPath("member_name").value(memberA.getName()))
			.andExpect(jsonPath("member_email").value(memberA.getEmail()))
			.andExpect(jsonPath("follower_count").value(2))
			.andExpect(jsonPath("following_count").value(2));
	}
}
