package com.youngxpepp.instagramcloneserver.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;

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
