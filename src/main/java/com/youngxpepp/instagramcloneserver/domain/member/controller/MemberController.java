package com.youngxpepp.instagramcloneserver.domain.member.controller;

import java.net.URISyntaxException;
import java.util.Arrays;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import com.youngxpepp.instagramcloneserver.domain.member.dto.GetMemberResponseBody;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberMapper;
import com.youngxpepp.instagramcloneserver.domain.member.dto.SignupRequestParam;
import com.youngxpepp.instagramcloneserver.domain.member.service.MemberService;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.AccessTokenClaims;
import com.youngxpepp.instagramcloneserver.global.util.JwtUtils;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Validated
public class MemberController {

	private final MemberService memberService;
	private final MemberMapper memberMapper;
	private final JwtUtils jwtUtils;

	@GetMapping("/{memberId}")
	public GetMemberResponseBody getMember(@PathVariable("memberId") @NotNull Long memberId) {
		return memberService.getMember(memberId);
	}

	@GetMapping("/signup")
	public ResponseEntity<?> signup(
		@Valid SignupRequestParam requestParam,
		@AuthenticationPrincipal @ApiIgnore OAuth2User principal) throws URISyntaxException {
		MemberDto memberDto = memberMapper.toMemberDto(requestParam, principal.getAttribute("email"));
		MemberDto responseDto = memberService.signup(memberDto);

		AccessTokenClaims accessTokenClaims = AccessTokenClaims.builder()
			.memberId(responseDto.getId())
			.roles(Arrays.asList(responseDto.getRole().getValue()))
			.build();
		String accessToken = jwtUtils.generateAccessToken(accessTokenClaims);

		UriComponentsBuilder redirectUriBuilder = UriComponentsBuilder.fromUriString(requestParam.getRedirectUri());
		redirectUriBuilder.queryParam("accessToken", accessToken);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(redirectUriBuilder.build().toUri());

		return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
	}
}
