package com.youngxpepp.instagramcloneserver.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class CreateCommentResponseBody {

	private final Long id;
	private final String content;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
	private final LocalDateTime createdAt;

	private final CreatedByDto createdBy;
}
