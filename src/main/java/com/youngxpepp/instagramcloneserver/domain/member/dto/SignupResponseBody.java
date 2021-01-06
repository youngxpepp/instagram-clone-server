package com.youngxpepp.instagramcloneserver.domain.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SignupResponseBody {

	private final Long id;
	private final String email;
	private final String nickname;
	private final String name;
	private final String profileImageUrl;
}
