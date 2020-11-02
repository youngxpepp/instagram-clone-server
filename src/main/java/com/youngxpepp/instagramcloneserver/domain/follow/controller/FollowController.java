package com.youngxpepp.instagramcloneserver.domain.follow.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import com.youngxpepp.instagramcloneserver.domain.follow.dto.FollowDto;
import com.youngxpepp.instagramcloneserver.domain.follow.dto.FollowRequestDto;
import com.youngxpepp.instagramcloneserver.domain.follow.service.FollowService;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;

@RestController
@RequestMapping("/api/v1/follows")
@AllArgsConstructor
@Slf4j
@Validated
public class FollowController {

	private FollowService followService;

	@PostMapping
	public ResponseEntity<FollowDto> follow(
		@RequestBody @Valid FollowRequestDto requestDto,
		@AuthenticationPrincipal @ApiIgnore Member principal) {
		return new ResponseEntity<FollowDto>(followService.follow(principal.getId(), requestDto.getMemberId()),
			HttpStatus.CREATED);
	}

	@DeleteMapping("/{followId}")
	public ResponseEntity unfollow(
		@PathVariable("followId") @NotNull Long followId,
		@AuthenticationPrincipal @ApiIgnore Member principal
	) {
		followService.unfollow(principal.getId(), followId);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
