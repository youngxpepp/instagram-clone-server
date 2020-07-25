package com.youngxpepp.instagramcloneserver.domain.post;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

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
	private PostRepository postRepository;

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

	private PostServiceDto.CreateRequestDto makePostCreateDto(Member creator, String content) {
		return PostServiceDto.CreateRequestDto.builder()
			.createdBy(creator)
			.content(content)
			.build();
	}

	private PostServiceDto.ModifyRequestDto makePostModifyDto(Member modifier, String content, Long postId) {
		return PostServiceDto.ModifyRequestDto.builder()
			.content(content)
			.modifiedBy(modifier)
			.id(postId)
			.build();
	}

	private void testPostContent(Long postId, String content) {
		Optional<Post> byId = postRepository.findById(postId);
		Post post = byId.orElse(null);
		assertThat(post).isNotNull();
		assertThat(post.getContent()).isEqualTo(content);
	}

	@Test
	void Given_가입유저_When_게시물생성_Then_게시물정상생성() {
		// given
		PostServiceDto.CreateRequestDto post = makePostCreateDto(principal, "1");

		MemberResponseDto memberResponseDto = MemberResponseDto.of(principal);

		// when
		PostServiceDto.ServiceResponseDto postResponse = postService.createPost(post);

		// then
		Optional<Post> byId = postRepository.findById(postResponse.getId());
		assertThat(byId).isPresent();

		assertThat(postResponse.getContent()).isEqualTo("1");
		assertThat(postResponse.getCreatedBy().getId())
			.isEqualTo(memberResponseDto.getId());
	}

	@Test
	void Given_글생성유저_When_게시물수정_Then_수정성공() {
		// given
		Member creator = principal;
		PostServiceDto.CreateRequestDto post = makePostCreateDto(creator, "1");
		PostServiceDto.ServiceResponseDto postCreateResponse = postService.createPost(post);

		String modifyContent = "2";
		PostServiceDto.ModifyRequestDto modifyPostRequest =
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
		String originalContent = "1";
		PostServiceDto.CreateRequestDto postDto = makePostCreateDto(creator, originalContent);
		PostServiceDto.ServiceResponseDto postCreateResponse = postService.createPost(postDto);

		String modifyContent = "2";
		Member modifier = principal2;
		PostServiceDto.ModifyRequestDto modifyPostRequest =
			makePostModifyDto(modifier, modifyContent, postCreateResponse.getId());

		// when
		BusinessException businessException = assertThrows(BusinessException.class,
			() -> postService.modifyPost(modifyPostRequest));
		ErrorCode errorCode = businessException.getErrorCode();

		// then
		assertThat(errorCode.getHttpStatus()).isEqualTo(HttpStatus.FORBIDDEN);
		assertThat(errorCode.getCode()).isEqualTo(1005);

		testPostContent(postCreateResponse.getId(), originalContent);
	}

	@Test
	void Given_없는포스트_When_게시물수정_Then_접근실패() {
		// given
		String modifyContent = "2";
		Member modifier = principal;
		Long wrongPostId = 999L;
		PostServiceDto.ModifyRequestDto modifyPostRequest =
			makePostModifyDto(modifier, modifyContent, wrongPostId);

		// when
		BusinessException businessException = assertThrows(BusinessException.class,
			() -> postService.modifyPost(modifyPostRequest));
		ErrorCode errorCode = businessException.getErrorCode();

		// then
		assertThat(errorCode.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(errorCode.getCode()).isEqualTo(1002);
	}

	@Test
	void Given_글생성유저_When_게시물삭제_Then_삭제성공() {
		// given
		Post post = Post.builder()
			.content("1")
			.createdBy(principal)
			.build();
		post = postRepository.save(post);

		PostServiceDto.DeleteRequestDto deleteRequestDto = PostServiceDto.DeleteRequestDto.builder()
			.id(post.getId())
			.requestBy(principal)
			.build();

		// when
		PostServiceDto.DeleteResponseDto deleteResponseDto = postService.deletePost(deleteRequestDto);

		// then
		Optional<Post> postOptional = postRepository.findById(deleteResponseDto.getId());
		assertThat(postOptional).isNotPresent();
	}

	@Test
	void Given_다른유저_When_게시물삭제_Then_삭제실패() {
		// given
		Post post = Post.builder()
			.content("1")
			.createdBy(principal)
			.build();
		post = postRepository.save(post);

		PostServiceDto.DeleteRequestDto deleteRequestDto = PostServiceDto.DeleteRequestDto.builder()
			.id(post.getId())
			.requestBy(principal2)
			.build();

		// when
		BusinessException businessException = assertThrows(BusinessException.class,
			() -> postService.deletePost(deleteRequestDto));
		ErrorCode errorCode = businessException.getErrorCode();

		// then
		assertThat(errorCode.getCode()).isEqualTo(1005);
		assertThat(errorCode.getHttpStatus()).isEqualTo(HttpStatus.FORBIDDEN);

		Optional<Post> byId = postRepository.findById(post.getId());
		assertThat(byId).isPresent();
	}

	@Test
	void Given_없는게시물_When_게시물삭제_Then_삭제실패() {
		// given
		Long postId = 999L;
		PostServiceDto.DeleteRequestDto deleteRequestDto = PostServiceDto.DeleteRequestDto.builder()
			.id(999L)
			.requestBy(principal)
			.build();

		// when
		BusinessException businessException = assertThrows(BusinessException.class,
			() -> postService.deletePost(deleteRequestDto));
		ErrorCode errorCode = businessException.getErrorCode();

		// then
		assertThat(errorCode.getCode()).isEqualTo(1002);
		assertThat(errorCode.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);

		Optional<Post> byId = postRepository.findById(999L);
		assertThat(byId).isNotPresent();
	}

	@Test
	void Given_정상게시물_When_게시물읽기_then_읽기성공() {
		// given
		PostServiceDto.CreateRequestDto postCreateDto = makePostCreateDto(principal, "1");

		PostServiceDto.ServiceResponseDto post = postService.createPost(postCreateDto);

		PostServiceDto.ReadOnePostRequestDto readOnePostRequestDto =
			PostServiceDto.ReadOnePostRequestDto.builder()
				.id(post.getId())
				.build();

		// when
		PostServiceDto.ReadOnePostResponseDto readOnePostResponseDto = postService.readPost(readOnePostRequestDto);

		// then
		assertThat(readOnePostResponseDto.getId()).isEqualTo(post.getId());
		assertThat(readOnePostResponseDto.getContent()).isEqualTo(post.getContent());
		assertThat(readOnePostResponseDto.getCreatedBy().getId()).isEqualTo(post.getCreatedBy().getId());
		assertThat(readOnePostResponseDto.getCreatedAt()).isEqualTo(post.getCreatedAt());
	}

	@Test
	void Given_없는게시물_When_게시물읽기_Then_읽기실패() {
		// given
		Long postId = 999L;
		PostServiceDto.ReadOnePostRequestDto postRequestDto = PostServiceDto.ReadOnePostRequestDto.builder()
			.id(postId)
			.build();

		// when
		BusinessException businessException = assertThrows(BusinessException.class,
			() -> postService.readPost(postRequestDto));
		ErrorCode errorCode = businessException.getErrorCode();

		// then
		assertThat(errorCode.getCode()).isEqualTo(1002);
		assertThat(errorCode.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);

		Optional<Post> byId = postRepository.findById(999L);
		assertThat(byId).isNotPresent();
	}
}
