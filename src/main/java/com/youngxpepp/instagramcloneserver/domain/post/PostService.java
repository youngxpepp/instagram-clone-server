package com.youngxpepp.instagramcloneserver.domain.post;

import org.springframework.stereotype.Service;

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
}
