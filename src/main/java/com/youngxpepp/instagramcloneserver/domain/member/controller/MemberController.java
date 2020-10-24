package com.youngxpepp.instagramcloneserver.domain.member.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import com.youngxpepp.instagramcloneserver.domain.member.dto.LoginRequestDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.LoginResponseDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberControllerDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberServiceDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.SignupRequestDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.SignupResponseDto;
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

	@PostMapping("/login")
	public LoginResponseDto login(@RequestBody @Valid LoginRequestDto requestDto) {
		String accessToken = this.memberService.login(requestDto.getNickname(), requestDto.getPassword());

		return LoginResponseDto.builder()
			.accessToken(accessToken)
			.build();
	}

	@PostMapping("/signup")
	public SignupResponseDto signup(
		@RequestBody @Valid SignupRequestDto requestDto,
		@AuthenticationPrincipal @ApiIgnore OAuth2User principal) {
		String accessToken = memberService.signup(principal.getName(), requestDto);
		return new SignupResponseDto(accessToken);
	}
}
