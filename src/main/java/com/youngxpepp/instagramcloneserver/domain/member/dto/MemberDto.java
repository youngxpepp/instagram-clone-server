package com.youngxpepp.instagramcloneserver.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;

@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class MemberDto {

	private Long id;
	private String nickname;
	private String name;

	public static MemberDto ofMember(Member member) {
		return MemberDto.builder()
			.id(member.getId())
			.nickname(member.getNickname())
			.name(member.getName())
			.build();
	}
}
