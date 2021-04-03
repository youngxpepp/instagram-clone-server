package com.youngxpepp.instagramcloneserver.controller;

import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import com.youngxpepp.instagramcloneserver.dto.GetMemberResponseBody;
import com.youngxpepp.instagramcloneserver.dto.SignupDto;
import com.youngxpepp.instagramcloneserver.dto.SignupRequestBody;
import com.youngxpepp.instagramcloneserver.mapper.MemberMapper;
import com.youngxpepp.instagramcloneserver.service.MemberService;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Validated
public class MemberController {

	private final MemberService memberService;
	private final MemberMapper memberMapper;

	@GetMapping("/{memberId}")
	public GetMemberResponseBody getMember(@PathVariable("memberId") @NotNull Long memberId) {
		return memberService.getMember(memberId);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(
		@RequestBody @Validated SignupRequestBody body,
		@NotNull @ApiIgnore OAuth2AuthenticationToken authentication
	) {
		SignupDto signupDto = memberMapper.toSignupDto(
			body,
			authentication.getPrincipal().getAttribute("email"),
			authentication.getPrincipal().getName(),
			authentication.getAuthorizedClientRegistrationId()
		);
		memberService.signup(signupDto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
