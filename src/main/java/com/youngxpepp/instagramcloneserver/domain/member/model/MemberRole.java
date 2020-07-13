package com.youngxpepp.instagramcloneserver.domain.member.model;

import lombok.Getter;

@Getter
public enum MemberRole {
	MEMBER("ROLE_MEMBER"),
	ADMIN("ROLE_ADMIN");

	private String name;

	MemberRole(String name) {
		this.name = name;
	}
}
