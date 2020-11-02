package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;

@AllArgsConstructor
@Builder
@Getter
public class AccessTokenClaims {

	private Long memberId;
	private List<MemberRole> roles;

	public List<String> getRolesByString() {
		return this.roles
			.stream()
			.map(role -> role.getValue())
			.collect(Collectors.toList());
	}

	public Map<String, Object> getClaimsByMap() {
		Map<String, Object> claimsByMap = new HashMap<>();
		claimsByMap.put("memberId", this.memberId);
		claimsByMap.put("roles", this.getRolesByString());
		return claimsByMap;
	}
}
