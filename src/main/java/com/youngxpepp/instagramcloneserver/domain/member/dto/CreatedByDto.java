package com.youngxpepp.instagramcloneserver.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class CreatedByDto {

	private final Long id;
	private final String nickname;
	private final String profileImageUrl;
}
