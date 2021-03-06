package com.youngxpepp.instagramcloneserver.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.youngxpepp.instagramcloneserver.domain.MemberRole;

@AllArgsConstructor
@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class MemberDto {

	private Long id;
	private String email;
	private String nickname;
	private String name;
	private String profileImageUrl;
	private String description;
	private MemberRole role;
}
