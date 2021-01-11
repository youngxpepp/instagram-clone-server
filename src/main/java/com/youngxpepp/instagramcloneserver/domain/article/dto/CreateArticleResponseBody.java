package com.youngxpepp.instagramcloneserver.domain.article.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class CreateArticleResponseBody {

	private final Long id;
	private final String content;
	private final List<String> imageUrls;
}
