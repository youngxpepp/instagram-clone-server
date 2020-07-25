package com.youngxpepp.instagramcloneserver.domain.post;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;

	public PostServiceDto.ServiceResponseDto createPost(PostServiceDto.CreateRequestDto createRequestDto) {
		Post post = createRequestDto.toEntity();
		post = postRepository.save(post);
		return PostServiceDto.ServiceResponseDto.of(post);
	}

	public PostServiceDto.ServiceResponseDto modifyPost(PostServiceDto.ModifyRequestDto modifyRequestDto) {
		Long postId = modifyRequestDto.getId();
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		if (!post.getCreatedBy().getId().equals(modifyRequestDto.getModifiedBy().getId())) {
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}
		// TODO
		// 수정 권한 검사

		post.modify(modifyRequestDto);
		post = postRepository.save(post);
		return PostServiceDto.ServiceResponseDto.of(post);
	}

	public PostServiceDto.DeleteResponseDto deletePost(PostServiceDto.DeleteRequestDto deleteRequestDto) {
		Long postId = deleteRequestDto.getId();
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		if (!post.getCreatedBy().getId().equals(deleteRequestDto.getRequestBy().getId())) {
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}
		// TODO
		// 삭제 권한 검사

		postRepository.deleteById(postId);
		return PostServiceDto.DeleteResponseDto.builder()
			.id(postId)
			.build();
	}

	public PostServiceDto.ReadOnePostResponseDto readPost(PostServiceDto.ReadOnePostRequestDto readOnePostRequestDto) {
		Long postId = readOnePostRequestDto.getId();
		// TODO 읽기권한 검사
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
		return PostServiceDto.ReadOnePostResponseDto.of(post);
	}
}
