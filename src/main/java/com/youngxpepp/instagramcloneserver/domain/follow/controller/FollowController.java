package com.youngxpepp.instagramcloneserver.domain.follow.controller;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import com.youngxpepp.instagramcloneserver.domain.follow.dto.FollowRequestDto;
import com.youngxpepp.instagramcloneserver.domain.follow.dto.UnfollowRequestDto;
import com.youngxpepp.instagramcloneserver.domain.follow.service.FollowService;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.PostJwtAuthenticationToken;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@RestController
@RequestMapping("/api/v1/follows")
@AllArgsConstructor
@Slf4j
@Validated
public class FollowController {

	private FollowService followService;
	private Validator validator;

	@PostMapping
	public void follow(
		@RequestBody @Valid FollowRequestDto dto,
		@AuthenticationPrincipal @ApiIgnore Member principal
	)
		throws BusinessException {

		followService.follow(principal, dto);
	}

	@DeleteMapping("/{member_nickname}")
	public void unfollow(
		@PathVariable("member_nickname") String memberNickname,
		@AuthenticationPrincipal @ApiIgnore Member principal
	) {

		UnfollowRequestDto dto = UnfollowRequestDto.builder()
			.memberNickname(memberNickname)
			.build();
		followService.unfollow(principal, dto);
	}
}
