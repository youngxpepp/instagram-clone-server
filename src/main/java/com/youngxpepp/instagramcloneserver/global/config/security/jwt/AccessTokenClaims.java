package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.domain.MemberRole;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public final class AccessTokenClaims {

	@JsonProperty
	private Long memberId;

	@JsonProperty
	private List<String> roles;

	@Builder
	private AccessTokenClaims(Long memberId, List<String> roles) {
		this.memberId = memberId;
		this.roles = roles;
	}

	public static AccessTokenClaims ofMember(Member member) {
		return AccessTokenClaims.builder()
			.memberId(member.getId())
			.roles(Arrays.asList(member.getRole().getValue()))
			.build();
	}

	public static AccessTokenClaims ofMemberRoleList(Long memberId, List<MemberRole> memberRoles) {
		List<String> roles = memberRoles.stream().map(MemberRole::getValue).collect(Collectors.toList());
		return new AccessTokenClaims(memberId, roles);
	}
}
