package com.youngxpepp.instagramcloneserver.domain.member.controller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberControllerDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberServiceDto;
import com.youngxpepp.instagramcloneserver.domain.member.service.MemberService;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Validated
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/{nickname}")
	public MemberControllerDto.GetMemberResponseDto getMember(
		@PathVariable("nickname") @NotBlank String memberNickname) {

		MemberServiceDto.GetMemberResponseDto serviceResponse = memberService.getMember(memberNickname);

		return MemberControllerDto.GetMemberResponseDto.builder()
			.memberNickname(serviceResponse.getMemberNickname())
			.memberName(serviceResponse.getMemberName())
			.followerCount(serviceResponse.getFollowerCount())
			.followingCount(serviceResponse.getFollowingCount())
			.build();
	}
}
