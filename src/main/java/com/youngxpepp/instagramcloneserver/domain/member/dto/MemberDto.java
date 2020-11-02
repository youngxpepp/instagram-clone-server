package com.youngxpepp.instagramcloneserver.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;

@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class MemberDto {

	private Long id;
	private String nickname;
	private String name;
	private MemberRole role;

	public static MemberDto ofMember(Member member) {
		return MemberDto.builder()
			.id(member.getId())
			.nickname(member.getNickname())
			.name(member.getName())
			.role(member.getRole())
			.build();
	}
}
