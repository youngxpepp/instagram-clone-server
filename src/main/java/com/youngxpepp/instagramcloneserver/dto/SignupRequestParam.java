package com.youngxpepp.instagramcloneserver.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class SignupRequestParam {

	@NotBlank
	private String nickname;

	@NotBlank
	private String name;

	@URL
	private String profileImageUrl;

	@NotBlank
	private String description;

	@URL
	private String redirectUri;
}
