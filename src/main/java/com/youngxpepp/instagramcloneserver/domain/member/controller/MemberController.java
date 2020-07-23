package com.youngxpepp.instagramcloneserver.domain.member.controller;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Validated
public class MemberController {

	private MemberService memberService;

	@GetMapping("/{memberNickname}")
	public MemberControllerDto.GetMemberResponseDto getMember(
		@PathVariable("memberNickname") @NotEmpty @NotNull String memberNickname) {

		MemberServiceDto.GetMemberResponseDto serviceResponse = memberService.getMember(memberNickname);

		return MemberControllerDto.GetMemberResponseDto.builder()
			.memberNickname(serviceResponse.getMemberNickname())
			.memberName(serviceResponse.getMemberName())
			.memberEmail(serviceResponse.getMemberEmail())
			.followerCount(serviceResponse.getFollowerCount())
			.followingCount(serviceResponse.getFollowingCount())
			.build();
	}
}
