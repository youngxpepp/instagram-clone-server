package com.youngxpepp.instagramcloneserver.domain.post;

import org.springframework.stereotype.Service;

import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;

	public PostServiceDto.ServiceResponseDto createPost(PostServiceDto.CreateServiceRequestDto createRequestDto) {
		Post post = createRequestDto.toEntity();
		post = postRepository.save(post);
		return PostServiceDto.ServiceResponseDto.of(post);
	}

	public PostServiceDto.ServiceResponseDto modifyPost(
		PostServiceDto.ModifyServiceRequestDto modifyServiceRequestDto) {
		Long postId = modifyServiceRequestDto.getId();
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		if (!post.getCreatedBy().getId().equals(modifyServiceRequestDto.getModifiedBy().getId())) {
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}
		// TODO
		// 수정 권한 검사

		post.modify(modifyServiceRequestDto);
		post = postRepository.save(post);
		return PostServiceDto.ServiceResponseDto.of(post);
	}

}
