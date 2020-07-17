package com.youngxpepp.instagramcloneserver.domain.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import com.google.common.collect.ImmutableList;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.AccessTokenClaims;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtUtil;
import com.youngxpepp.instagramcloneserver.test.IntegrationTest;

class PostControllerTest extends IntegrationTest {

	private Member principal;
	private String accessToken;
	Post post;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private PostRepository postRepository;

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
			.roles(ImmutableList.of(principal.getRole()))
			.build();
		accessToken = jwtUtil.generateAccessToken(accessTokenClaims);

		Post test = Post.builder()
			.createdBy(principal)
			.content("test")
			.build();
		post = postRepository.save(test);
	}

	private ResultActions requestRead(Long id, Member principal) throws Exception {
		StringBuilder url = new StringBuilder("/api/v1/post/");
		if (id != null) {
			url.append(id);
		}

		if (principal == null) {
			return mockMvc.perform(get(url.toString())
				.contentType(MediaType.APPLICATION_JSON));
		}
		AccessTokenClaims accessTokenClaims = AccessTokenClaims.builder()
			.email(principal.getEmail())
			.roles(ImmutableList.of(principal.getRole()))
			.build();
		String token = jwtUtil.generateAccessToken(accessTokenClaims);

		return mockMvc.perform(get(url.toString())
			.header("Authorization", token)
			.contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void Given_있는포스트_When_게시물읽기_Then_읽기성공() throws Exception {
		// given
		Long id = post.getId();

		// when
		ResultActions perform = requestRead(id, principal);

		// then
		perform
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(id));
	}

	@Test
	void Given_없는게시물_When_게시물읽기_Then_읽기실패() throws Exception {
		// given
		Long id = 99999L;

		// when
		ResultActions perform = requestRead(id, principal);

		perform
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.error.code").value(1002));
	}

	@Test
	void Given_없는유저_When_게시물읽기_Then_읽기성공() throws Exception {
		// given
		Long id = post.getId();

		// when
		ResultActions perform = requestRead(id, null);

		// then
		perform
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(id));
	}
}