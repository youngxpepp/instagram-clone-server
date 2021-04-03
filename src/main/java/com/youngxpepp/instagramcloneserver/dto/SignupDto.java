package com.youngxpepp.instagramcloneserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class SignupDto {

	private final String email;
	private final String nickname;
	private final String name;
	private final String profileImageUrl;
	private final String description;
	private final String oauth2NameAttribute;
	private final String oauth2RegistrationId;
}
