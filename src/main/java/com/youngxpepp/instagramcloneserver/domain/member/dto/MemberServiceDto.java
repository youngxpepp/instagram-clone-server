package com.youngxpepp.instagramcloneserver.domain.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberServiceDto {

	@AllArgsConstructor
	@Builder
	@Getter
	public static class GetMemberResponseDto {

		private String memberNickname;
		private String memberName;
		private String memberEmail;
		private Long followerCount;
		private Long followingCount;
	}
}
