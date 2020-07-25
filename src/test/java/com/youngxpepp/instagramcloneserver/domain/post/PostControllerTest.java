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
	private String accessToken2;
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

		Member principal2 = Member.builder()
			.name("principalName2")
			.nickname("principalNickname2")
			.email("principal2@gmail.com")
			.password("123123")
			.role(MemberRole.MEMBER)
			.build();
		memberRepository.save(principal2);

		AccessTokenClaims accessTokenClaims2 = AccessTokenClaims.builder()
			.email(principal2.getEmail())
			.roles(ImmutableList.of(principal2.getRole()))
			.build();
		accessToken2 = jwtUtil.generateAccessToken(accessTokenClaims2);

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

	private ResultActions requestModify(Long id, String token, PostControllerDto.ModifyRequestDto requestDto) throws
		Exception {
		StringBuilder url = new StringBuilder("/api/v1/post/");
		if (id != null) {
			url.append(id);
		}

		if (principal == null) {
			return mockMvc.perform(patch(url.toString())
				.content(objectMapper.writeValueAsString(requestDto))
				.contentType(MediaType.APPLICATION_JSON));
		}

		return mockMvc.perform(patch(url.toString())
			.header("Authorization", token)
			.content(objectMapper.writeValueAsString(requestDto))
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

	// @Test
	// void Given_없는유저_When_게시물읽기_Then_읽기성공() throws Exception {
	// 	// given
	// 	Long id = post.getId();
	//
	// 	// when
	// 	ResultActions perform = requestRead(id, null);
	//
	// 	// then
	// 	perform
	// 		.andExpect(status().isOk())
	// 		.andExpect(jsonPath("$.id").value(id));
	// }

	@Test
	void Given_정상유저_and_컨텐츠_When_게시물생성_Then_게시물생성성공() throws Exception {
		// given
		String content = "test";
		PostControllerDto.CreateRequestDto createRequestDto = PostControllerDto.CreateRequestDto.builder()
			.content(content)
			.build();

		// when
		ResultActions resultActions = mockMvc.perform(post("/api/v1/post")
			.header("Authorization", accessToken)
			.content(objectMapper.writeValueAsString(createRequestDto))
			.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.content").value(content))
			.andExpect(jsonPath("$.created_by.id").value(principal.getId()));
	}

	@Test
	void Given_정상유저_and_빈컨텐츠_When_게시물생성_Then_게시물생성성공() throws Exception {
		// given
		PostControllerDto.CreateRequestDto createRequestDto = PostControllerDto.CreateRequestDto.builder()
			.build();

		// when
		ResultActions resultActions = mockMvc.perform(post("/api/v1/post")
			.header("Authorization", accessToken)
			.content(objectMapper.writeValueAsString(createRequestDto))
			.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.created_by.id").value(principal.getId()));
	}

	@Test
	void Given_요청자없음_When_게시물생성_Then_권한없음() throws Exception {
		// given
		PostControllerDto.CreateRequestDto createRequestDto = PostControllerDto.CreateRequestDto.builder()
			.build();

		// when
		ResultActions resultActions = mockMvc.perform(post("/api/v1/post")
			.content(objectMapper.writeValueAsString(createRequestDto))
			.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.error.code").value(2003));
	}

	@Test
	void Given_정상유저_and_request_없음_When_게시물생성_Then_게시물생성성공() throws Exception {
		// given

		// when
		ResultActions resultActions = mockMvc.perform(post("/api/v1/post")
			.header("Authorization", accessToken)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpect(status().isBadRequest());
	}

	@Test
	void Given_게시물생성유저_When_게시물수정_Then_게시물수정성공() throws Exception {
		// given
		String modifiedString = "test2";
		PostControllerDto.ModifyRequestDto requestDto = PostControllerDto.ModifyRequestDto.builder()
			.content(modifiedString)
			.build();

		// when
		ResultActions resultActions = requestModify(post.getId(), accessToken, requestDto);

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(post.getId()))
			.andExpect(jsonPath("$.content").value(modifiedString))
			.andExpect(jsonPath("$.modified_by.id").value(principal.getId()));
	}

	@Test
	void Given_다른유저_When_게시물수정_Then_게시물수정실패() throws Exception {
		// given
		String modifiedString = "test2";
		PostControllerDto.ModifyRequestDto requestDto = PostControllerDto.ModifyRequestDto.builder()
			.content(modifiedString)
			.build();

		// when
		ResultActions resultActions = requestModify(post.getId(), accessToken2, requestDto);

		// then
		resultActions
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$.error.code").value(1005));
	}

	@Test
	void Given_없는게시물_When_게시물수정_Then_게시물수정실패() throws Exception {
		// given
		String modifiedString = "test2";
		PostControllerDto.ModifyRequestDto requestDto = PostControllerDto.ModifyRequestDto.builder()
			.content(modifiedString)
			.build();

		// when
		ResultActions resultActions = requestModify(999L, accessToken, requestDto);

		// then
		resultActions
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.error.code").value(1002));
	}
}
