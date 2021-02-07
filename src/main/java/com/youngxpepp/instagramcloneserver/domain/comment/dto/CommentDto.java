package com.youngxpepp.instagramcloneserver.domain.comment.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.youngxpepp.instagramcloneserver.domain.member.dto.CreatedByDto;

@AllArgsConstructor
@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class CommentDto {

	private Long id;

	private String content;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
	private LocalDateTime createdAt;

	private CreatedByDto createdBy;

	private List<CommentDto> nestedComments;
}
