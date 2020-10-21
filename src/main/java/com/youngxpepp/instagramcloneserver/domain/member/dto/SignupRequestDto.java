package com.youngxpepp.instagramcloneserver.domain.member.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SignupRequestDto {

	@NotBlank
	private String nickname;

	@NotBlank
	private String name;

	@NotBlank
	private String password;
}
