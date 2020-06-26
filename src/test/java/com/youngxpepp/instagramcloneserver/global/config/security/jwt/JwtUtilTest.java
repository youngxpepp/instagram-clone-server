package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import com.youngxpepp.instagramcloneserver.domain.member.MemberRole;
import com.youngxpepp.instagramcloneserver.test.MockTest;
import com.youngxpepp.instagramcloneserver.global.config.property.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

public class JwtUtilTest extends MockTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @Mock
    private JwtProperties jwtProperties;

    @Test
    public void Given_정상적인Claims_When_AccessToken생성_Then_AccessToken검증성공() {
//        given
        given(jwtProperties.getIssuer())
                .willReturn("testIssuer");
        given(jwtProperties.getAccessTokenExpiration())
                .willReturn(1800L);
        given(jwtProperties.getSecret())
                .willReturn("testtesttesttesttesttesttesttest");
        AccessTokenClaims accessTokenClaims = AccessTokenClaims.builder()
                .email("test@gmail.com")
                .roles(Arrays.asList(MemberRole.MEMBER))
                .build();
        String accessToken = jwtUtil.generateAccessToken(accessTokenClaims);
        String bearerAccessToken = JwtUtil.TOKEN_PREFIX + accessToken;

//        when
        Jws<Claims> jws = jwtUtil.verifyAccessToken(bearerAccessToken);

//        then
        assertThat(jws.getBody().get("email"))
                .isEqualTo(accessTokenClaims.getEmail());
        assertThat(jws.getBody().get("roles"))
                .isEqualTo(accessTokenClaims.getRolesByString());
    }

    @Test
    public void Given_기간만료AccessToken_When_검증_Then_기간만료() {
//                given
        given(jwtProperties.getIssuer())
                .willReturn("testIssuer");
        given(jwtProperties.getSecret())
                .willReturn("testtesttesttesttesttesttesttest");

        String bearerAccessToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ0ZXN0SXNzdWVyIiwic3ViIjoiQUNDRVNTX1RPS0VOIiwiZXhwIjowLCJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwicm9sZXMiOlsiUk9MRV9NRU1CRVIiXX0.LySV4gbfTiLilFNg8Z8ACblaQKKOqXs2FkSa9JIF1Ok";

//        when
//        then
        assertThatThrownBy(() -> {
            Jws<Claims> jws = jwtUtil.verifyAccessToken(bearerAccessToken);
        }).isInstanceOf(ExpiredJwtException.class);
    }
}
