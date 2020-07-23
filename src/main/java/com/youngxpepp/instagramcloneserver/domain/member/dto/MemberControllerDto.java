package com.youngxpepp.instagramcloneserver.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberControllerDto {

	@AllArgsConstructor
	@Builder
	@Getter
	public static class GetMemberResponseDto {

		@JsonProperty("member_nickname")
		private String memberNickname;

		@JsonProperty("member_name")
		private String memberName;

		@JsonProperty("member_email")
		private String memberEmail;

		@JsonProperty("follower_count")
		private Long followerCount;

		@JsonProperty("following_count")
		private Long followingCount;
	}
}
