package com.youngxpepp.instagramcloneserver.domain.post;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@GetMapping("/{postId}")
	public PostControllerDto.ReadOnePostResponseDto readPost(@PathVariable Long postId,
		@AuthenticationPrincipal Member principal) {

		PostServiceDto.ReadOnePostRequestDto readOnePostRequestDto = PostServiceDto.ReadOnePostRequestDto.builder()
			.id(postId)
			.requestBy(principal)
			.build();
		PostServiceDto.ReadOnePostResponseDto readOnePostResponseDto = postService.readPost(readOnePostRequestDto);
		return PostControllerDto.ReadOnePostResponseDto.of(readOnePostResponseDto);
	}
}
