package com.youngxpepp.instagramcloneserver.domain.follow.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FollowRequestBody {

	@NotNull
	private Long memberId;
}
