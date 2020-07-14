package com.youngxpepp.instagramcloneserver.domain.follow.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FollowRequestDto {

	@JsonProperty("member_nickname")
	@NotNull
	@NotEmpty
	private String memberNickname;
}
