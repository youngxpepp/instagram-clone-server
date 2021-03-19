package com.youngxpepp.instagramcloneserver.global.util;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.youngxpepp.instagramcloneserver.domain.MemberRole;
import com.youngxpepp.instagramcloneserver.global.config.JsonConfig;
import com.youngxpepp.instagramcloneserver.global.config.property.JwtProperties;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.AccessTokenClaims;
import com.youngxpepp.instagramcloneserver.test.MockTest;

public class JwtUtilsTest extends MockTest {

	private final ObjectMapper objectMapper = new JsonConfig().objectMapper();

	@Mock
	private JwtProperties jwtProperties;

	@Test
	@DisplayName("When_generateAccessToken_Then_Access Token의 prefix가 Bearer이어야 함")
	public void generateAccessTokenThenPrefixBearer() {
		// given
		given(jwtProperties.getIssuer()).willReturn("instagram-clone");
		given(jwtProperties.getSecret()).willReturn("123123123123123123123fhaskjhfadshfas");
		given(jwtProperties.getAccessTokenExpiration()).willReturn(1800L);
		JwtUtils jwtUtils = new JwtUtils(jwtProperties, objectMapper);

		Long memberId = 1L;
		List<MemberRole> memberRoles = Arrays.asList(MemberRole.MEMBER);
		AccessTokenClaims accessTokenClaims = AccessTokenClaims.ofMemberRoleList(memberId, memberRoles);

		// when
		String accessToken = jwtUtils.generateAccessToken(accessTokenClaims);

		// then
		then(accessToken).startsWith("Bearer ");
	}

	@Test
	@DisplayName("When_generateAccessToken_Then_Claims가 일치해야 함")
	public void generateAccessTokenThenEqualsClaims() {
		// given
		given(jwtProperties.getIssuer()).willReturn("instagram-clone");
		given(jwtProperties.getSecret()).willReturn("123123123123123123123fhaskjhfadshfas");
		given(jwtProperties.getAccessTokenExpiration()).willReturn(1800L);
		JwtUtils jwtUtils = new JwtUtils(jwtProperties, objectMapper);

		Long memberId = 1L;
		List<MemberRole> memberRoles = Arrays.asList(MemberRole.MEMBER);
		AccessTokenClaims accessTokenClaims = AccessTokenClaims.ofMemberRoleList(memberId, memberRoles);

		// when
		String accessToken = jwtUtils.generateAccessToken(accessTokenClaims);

		// then
		AccessTokenClaims verifiedAccessTokenClaims = jwtUtils.verifyAccessToken(accessToken);
		then(verifiedAccessTokenClaims.getMemberId()).isEqualTo(memberId);
		then(verifiedAccessTokenClaims.getRoles()).isEqualTo(accessTokenClaims.getRoles());
	}

	@Test
	@DisplayName("Given_잘못된 issuer_When_verifyAccessToken_Then_throw IncorrectClaimException")
	public void verifyAccessTokenThenThrowsInCorrectClaimException() {
		// given
		given(jwtProperties.getIssuer()).willReturn("instagram-clone", "wrong issuer");
		given(jwtProperties.getSecret()).willReturn("123123123123123123123fhaskjhfadshfas");
		given(jwtProperties.getAccessTokenExpiration()).willReturn(1800L);
		JwtUtils jwtUtils = new JwtUtils(jwtProperties, objectMapper);

		Long memberId = 1L;
		List<MemberRole> memberRoles = Arrays.asList(MemberRole.MEMBER);
		AccessTokenClaims accessTokenClaims = AccessTokenClaims.ofMemberRoleList(memberId, memberRoles);
		String accessToken = jwtUtils.generateAccessToken(accessTokenClaims);

		// when
		Throwable throwable = catchThrowable(() -> {
			jwtUtils.verifyAccessToken(accessToken);
		});

		// then
		then(throwable).isInstanceOf(IncorrectClaimException.class);
	}

	@Test
	@DisplayName("Given_유효기간이 만료된 Access Token_When_verifyAccessToken_Then_throw ExpiredJwtException")
	public void verifyAccessTokenThenThrowsExpiredJwtException() {
		// given
		given(jwtProperties.getIssuer()).willReturn("instagram-clone");
		given(jwtProperties.getSecret()).willReturn("123123123123123123123fhaskjhfadshfas");
		given(jwtProperties.getAccessTokenExpiration()).willReturn(-1800L);
		JwtUtils jwtUtils = new JwtUtils(jwtProperties, objectMapper);

		Long memberId = 1L;
		List<MemberRole> memberRoles = Arrays.asList(MemberRole.MEMBER);
		AccessTokenClaims accessTokenClaims = AccessTokenClaims.ofMemberRoleList(memberId, memberRoles);
		String accessToken = jwtUtils.generateAccessToken(accessTokenClaims);

		// when
		Throwable throwable = catchThrowable(() -> {
			jwtUtils.verifyAccessToken(accessToken);
		});

		// then
		then(throwable).isInstanceOf(ExpiredJwtException.class);
	}
}
