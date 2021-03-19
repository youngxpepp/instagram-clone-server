package com.youngxpepp.instagramcloneserver.domain;

import lombok.Getter;

@Getter
public enum MemberRole {
	MEMBER("ROLE_MEMBER"),
	ADMIN("ROLE_ADMIN");

	private String value;

	MemberRole(String value) {
		this.value = value;
	}
}
