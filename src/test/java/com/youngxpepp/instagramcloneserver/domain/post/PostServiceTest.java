package com.youngxpepp.instagramcloneserver.domain.post;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberResponseDto;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
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
		memberRepository.save(principal);
	}

	@Test
	void Given_가입유저_When_게시물생성_Then_게시물정상생성() {
		// given
		PostServiceDto.CreateServiceRequestDto post = PostServiceDto.CreateServiceRequestDto.builder()
			.content("1")
			.createdBy(principal)
			.build();

		MemberResponseDto memberResponseDto = MemberResponseDto.of(principal);

		// when
		PostServiceDto.ServiceResponseDto postResponse = postService.createPost(post);

		// then
		assertThat(postResponse.getId()).isEqualTo(1);
		assertThat(postResponse.getContent()).isEqualTo("1");
		assertThat(postResponse.getCreatedBy().getId()).isEqualTo(memberResponseDto.getId());
	}
}
