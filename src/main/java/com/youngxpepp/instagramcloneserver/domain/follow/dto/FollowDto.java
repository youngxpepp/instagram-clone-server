package com.youngxpepp.instagramcloneserver.domain.follow.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;

import com.youngxpepp.instagramcloneserver.domain.follow.model.Follow;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberDto;

@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class FollowDto {

	private Long id;
	private MemberDto followingMember;
	private MemberDto followedMember;

	public static FollowDto ofFollow(Follow follow) {
		return FollowDto.builder()
			.id(follow.getId())
			.followingMember(MemberDto.ofMember(follow.getFollowingMember()))
			.followedMember(MemberDto.ofMember(follow.getFollowedMember()))
			.build();
	}
}
