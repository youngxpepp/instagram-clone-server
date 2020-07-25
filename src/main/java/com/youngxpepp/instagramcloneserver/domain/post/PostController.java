package com.youngxpepp.instagramcloneserver.domain.post;

import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public ResponseEntity<PostControllerDto.ReadOnePostResponseDto> readPost(@PathVariable Long postId,
		@AuthenticationPrincipal Member principal) {

		PostServiceDto.ReadOnePostRequestDto readOnePostRequestDto = PostServiceDto.ReadOnePostRequestDto.builder()
			.id(postId)
			.requestBy(principal)
			.build();
		PostServiceDto.ReadOnePostResponseDto readOnePostResponseDto = postService.readPost(readOnePostRequestDto);
		return ResponseEntity.ok(PostControllerDto.ReadOnePostResponseDto.of(readOnePostResponseDto));
	}

	@PostMapping
	public ResponseEntity<PostControllerDto.CreateResponseDto> createPost(
		@RequestBody PostControllerDto.CreateRequestDto createRequestDto,
		@AuthenticationPrincipal Member principal) {
		PostServiceDto.CreateRequestDto createServiceRequestDto = createRequestDto.toServiceDto(principal);

		PostServiceDto.CreateResponseDto postDto = postService.createPost(createServiceRequestDto);

		return ResponseEntity.created(null).body(PostControllerDto.CreateResponseDto.of(postDto));
	}

	@PatchMapping("/{postId}")
	public ResponseEntity<PostControllerDto.ModifyResponseDto> updatePost(
		@PathVariable @NotNull Long postId,
		@RequestBody PostControllerDto.ModifyRequestDto updateRequestDto,
		@AuthenticationPrincipal Member principal) {
		
		PostServiceDto.ModifyRequestDto modifyRequestDto = updateRequestDto.toServiceDto(postId, principal);

		PostServiceDto.ModifyResponseDto serviceResponseDto = postService.modifyPost(modifyRequestDto);

		return ResponseEntity.ok(PostControllerDto.ModifyResponseDto.of(serviceResponseDto));
	}
}
