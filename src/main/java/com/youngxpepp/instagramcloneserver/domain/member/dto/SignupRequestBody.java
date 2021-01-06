package com.youngxpepp.instagramcloneserver.domain.member.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SignupRequestBody {

	@NotBlank
	private String nickname;

	@NotBlank
	private String name;

	@NotBlank
	private String profileImageUrl;

	@NotBlank
	private String description;
}
