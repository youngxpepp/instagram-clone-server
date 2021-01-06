package com.youngxpepp.instagramcloneserver.domain.follow.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class FollowDto {

	private final Long id;
	private final MemberDto followingMember;
	private final MemberDto followedMember;

	@RequiredArgsConstructor
	@Getter
	public static class MemberDto {
		private final Long id;
		private final String nickname;
		private final String name;
	}
}
