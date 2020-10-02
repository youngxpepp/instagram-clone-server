package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;
import com.youngxpepp.instagramcloneserver.global.config.property.JwtProperties;
import com.youngxpepp.instagramcloneserver.test.MockTest;

public class JwtUtilTest extends MockTest {

	@InjectMocks
	private JwtUtil jwtUtil;

	@Mock
	private JwtProperties jwtProperties;

	@Test
	public void Given_정상적인Claims_When_AccessToken생성_Then_AccessToken검증성공() { // SUPPRESS CHECKSTYLE MethodName
		// given
		given(jwtProperties.getIssuer())
			.willReturn("testIssuer");
		given(jwtProperties.getAccessTokenExpiration())
			.willReturn(1800L);
		given(jwtProperties.getSecret())
			.willReturn("testtesttesttesttesttesttesttest");

		AccessTokenClaims accessTokenClaims = AccessTokenClaims.builder()
			.memberId(0L)
			.roles(Arrays.asList(MemberRole.MEMBER))
			.build();
		String accessToken = jwtUtil.generateAccessToken(accessTokenClaims);

		// when
		Jws<Claims> jws = jwtUtil.verifyAccessToken(accessToken);

		// then
		assertThat(jws.getBody().get("memberId"))
			.isEqualTo(accessTokenClaims.getMemberId().intValue());
		assertThat(jws.getBody().get("roles"))
			.isEqualTo(accessTokenClaims.getRolesByString());
	}

	@Test
	public void Given_기간만료AccessToken_When_검증_Then_기간만료() { // SUPPRESS CHECKSTYLE MethodName
		// given
		given(jwtProperties.getIssuer())
			.willReturn("testIssuer");
		given(jwtProperties.getSecret())
			.willReturn("testtesttesttesttesttesttesttest");

		String bearerAccessToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ0ZXN0SXNzdWVyIiwic3ViIjoiQUNDRVNTX1RPS0VOIiwiZXhwIjowLCJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwicm9sZXMiOlsiUk9MRV9NRU1CRVIiXX0.LySV4gbfTiLilFNg8Z8ACblaQKKOqXs2FkSa9JIF1Ok";

		// when
		// then
		assertThatThrownBy(() -> {
			Jws<Claims> jws = jwtUtil.verifyAccessToken(bearerAccessToken);
		}).isInstanceOf(ExpiredJwtException.class);
	}
}
