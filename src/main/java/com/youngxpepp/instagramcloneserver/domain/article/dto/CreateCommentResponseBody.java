package com.youngxpepp.instagramcloneserver.domain.article.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class CreateCommentResponseBody {

	private final Long id;
	private final String content;
	private final LocalDateTime createdAt;
	private final CreatedBy createdBy;

	@RequiredArgsConstructor
	@Builder
	@Getter
	public static class CreatedBy {

		private final Long id;
		private final String nickname;
		private final String profileImageUrl;
	}
}
