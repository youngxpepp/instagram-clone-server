package com.youngxpepp.instagramcloneserver.domain.article.dto;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GetArticleResponseBody {

	private final Long id;
	private final String content;
	private final Boolean isLiked;
	private final List<String> imageUrls;
	private final CreatedByDto createdBy;

	@RequiredArgsConstructor
	@Getter
	public static class CreatedByDto {

		private final Long id;
		private final String nickname;
		private final String profileImageUrl;
	}
}
