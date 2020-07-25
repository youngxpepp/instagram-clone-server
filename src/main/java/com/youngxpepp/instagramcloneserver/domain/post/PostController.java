package com.youngxpepp.instagramcloneserver.domain.post;

import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

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

	@PostMapping
	public ResponseEntity<PostControllerDto.CreateResponseDto> createPost(
		@RequestBody @NotNull PostControllerDto.CreateRequestDto createRequestDto,
		Errors errors,
		@AuthenticationPrincipal Member principal) {
		if (errors.hasErrors()) {
			System.out.println("error");
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
		}
		PostServiceDto.CreateRequestDto createServiceRequestDto = createRequestDto.toServiceDto(principal);

		PostServiceDto.ServiceResponseDto postDto = postService.createPost(createServiceRequestDto);

		return ResponseEntity.created(null).body(PostControllerDto.CreateResponseDto.of(postDto));
	}

}
