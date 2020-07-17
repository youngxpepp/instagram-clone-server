package com.youngxpepp.instagramcloneserver.domain.post;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberResponseDto;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;
import com.youngxpepp.instagramcloneserver.test.IntegrationTest;

class PostServiceTest extends IntegrationTest {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PostService postService;

	private Member principal;
	private Member principal2;

	@BeforeEach
	void saveMember() {
		principal = Member.builder()
			.name("principalName")
			.nickname("principalNickname")
			.email("principal@gmail.com")
			.password("123123")
			.role(MemberRole.MEMBER)
			.build();
		principal = memberRepository.save(principal);

		principal2 = Member.builder()
			.name("principalName2")
			.nickname("principalNickname2")
			.email("principal2@gmail.com")
			.password("123123")
			.role(MemberRole.MEMBER)
			.build();
		principal2 = memberRepository.save(principal2);
	}

	private PostServiceDto.CreateServiceRequestDto makePostCreateDto(Member creator, String content) {
		return PostServiceDto.CreateServiceRequestDto.builder()
			.createdBy(creator)
			.content(content)
			.build();
	}

	private PostServiceDto.ModifyServiceRequestDto makePostModifyDto(Member modifier, String content, Long postId) {
		return PostServiceDto.ModifyServiceRequestDto.builder()
			.content(content)
			.modifiedBy(modifier)
			.id(postId)
			.build();
	}

	@Test
	void Given_가입유저_When_게시물생성_Then_게시물정상생성() {
		// given
		PostServiceDto.CreateServiceRequestDto post = makePostCreateDto(principal, "1");

		MemberResponseDto memberResponseDto = MemberResponseDto.of(principal);

		// when
		PostServiceDto.ServiceResponseDto postResponse = postService.createPost(post);

		// then
		assertThat(postResponse.getId()).isEqualTo(1);
		assertThat(postResponse.getContent()).isEqualTo("1");
		assertThat(postResponse.getCreatedBy().getId()).isEqualTo(memberResponseDto.getId());
	}

	@Test
	void Given_글생성유저_When_게시물수정_Then_수정성공() {
		// given
		Member creator = principal;
		PostServiceDto.CreateServiceRequestDto post = makePostCreateDto(creator, "1");
		PostServiceDto.ServiceResponseDto postCreateResponse = postService.createPost(post);

		String modifyContent = "2";
		PostServiceDto.ModifyServiceRequestDto modifyPostRequest =
			makePostModifyDto(creator, modifyContent, postCreateResponse.getId());

		// when
		PostServiceDto.ServiceResponseDto postModifyResponse = postService.modifyPost(modifyPostRequest);

		// then
		assertThat(postModifyResponse.getId()).isEqualTo(postCreateResponse.getId());
		assertThat(postModifyResponse.getContent()).isEqualTo(modifyContent);
		assertThat(postModifyResponse.getModifiedBy().getId()).isEqualTo(creator.getId());
	}

	@Test
	void Given_다른유저_When_게시물수정_Then_수정실패() {
		// given
		Member creator = principal;
		PostServiceDto.CreateServiceRequestDto post = makePostCreateDto(creator, "1");
		PostServiceDto.ServiceResponseDto postCreateResponse = postService.createPost(post);

		String modifyContent = "2";
		Member modifier = principal2;
		PostServiceDto.ModifyServiceRequestDto modifyPostRequest =
			makePostModifyDto(modifier, modifyContent, postCreateResponse.getId());

		// when
		BusinessException businessException = assertThrows(BusinessException.class,
			() -> postService.modifyPost(modifyPostRequest));
		ErrorCode errorCode = businessException.getErrorCode();

		// then
		assertThat(errorCode.getHttpStatus()).isEqualTo(HttpStatus.FORBIDDEN);
		assertThat(errorCode.getCode()).isEqualTo(1005);
	}

	@Test
	void Givne_없는포스트_When_게시물수정_Then_접근실패() {
		// given
		String modifyContent = "2";
		Member modifier = principal;
		Long wrongPostId = 999L;
		PostServiceDto.ModifyServiceRequestDto modifyPostRequest =
			makePostModifyDto(modifier, modifyContent, wrongPostId);

		// when
		BusinessException businessException = assertThrows(BusinessException.class,
			() -> postService.modifyPost(modifyPostRequest));
		ErrorCode errorCode = businessException.getErrorCode();

		// then
		assertThat(errorCode.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(errorCode.getCode()).isEqualTo(1002);
	}
}
