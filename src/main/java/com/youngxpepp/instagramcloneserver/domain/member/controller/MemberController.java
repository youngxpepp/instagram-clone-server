package com.youngxpepp.instagramcloneserver.domain.member.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.youngxpepp.instagramcloneserver.domain.member.dto.GetMemberResponseDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberMapper;
import com.youngxpepp.instagramcloneserver.domain.member.dto.SignupRequestBody;
import com.youngxpepp.instagramcloneserver.domain.member.dto.SignupResponseBody;
import com.youngxpepp.instagramcloneserver.domain.member.service.MemberService;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Validated
public class MemberController {

	private final MemberService memberService;
	private final MemberMapper memberMapper;

	@GetMapping("/{memberId}")
	public GetMemberResponseDto getMember(@PathVariable("memberId") @NotNull Long memberId) {
		return memberService.getMember(memberId);
	}

	@PostMapping("/signup")
	public ResponseEntity<SignupResponseBody> signup(@RequestBody @Valid SignupRequestBody signupRequestBody,
		@AuthenticationPrincipal @ApiIgnore OAuth2User principal) {
		MemberDto memberDto = memberMapper.toMemberDto(signupRequestBody, principal.getAttribute("email"));
		SignupResponseBody signupResponseBody = memberService.signup(memberDto);
		return new ResponseEntity<SignupResponseBody>(signupResponseBody, HttpStatus.CREATED);
	}
}
