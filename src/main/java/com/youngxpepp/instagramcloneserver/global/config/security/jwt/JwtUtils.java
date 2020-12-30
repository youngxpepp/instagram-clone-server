package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import java.util.Date;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.youngxpepp.instagramcloneserver.global.config.property.JwtProperties;
import com.youngxpepp.instagramcloneserver.global.error.exception.NoPrefixJwtException;

@Component
@RequiredArgsConstructor
public class JwtUtils {

	public static final String TOKEN_PREFIX = "Bearer ";

	private final JwtProperties jwtProperties;

	public String generateAccessToken(AccessTokenClaims claims) {
		String accessToken = Jwts.builder()
			.setIssuer(jwtProperties.getIssuer())
			.setSubject(JwtSubject.ACCESS_TOKEN.getValue())
			.setExpiration(this.getAccessTokenExpirationByDate())
			.addClaims(claims.getClaimsByMap())
			.signWith(this.getSecretKey())
			.compact();

		return JwtUtils.TOKEN_PREFIX + accessToken;
	}

	public Jws<Claims> verifyAccessToken(String bearerAccessToken) {
		if (bearerAccessToken.indexOf(JwtUtils.TOKEN_PREFIX) != 0) {
			throw new NoPrefixJwtException(bearerAccessToken);
		}

		String accessToken = bearerAccessToken.substring(JwtUtils.TOKEN_PREFIX.length());

		Jws<Claims> jws = Jwts.parserBuilder()
			.requireIssuer(this.jwtProperties.getIssuer())
			.requireSubject(JwtSubject.ACCESS_TOKEN.getValue())
			.setSigningKey(this.getSecretKey())
			.build()
			.parseClaimsJws(accessToken);

		return jws;
	}

	public boolean isValidAccessToken(String bearerAccessToken) {
		try {
			this.verifyAccessToken(bearerAccessToken);
		} catch (JwtException e) {
			return false;
		}
		return true;
	}

	public Date getAccessTokenExpirationByDate() {
		Date now = new Date();
		long unixTimestamp = now.getTime() + jwtProperties.getAccessTokenExpiration() * 1000L;
		return new Date(unixTimestamp);
	}

	public Date getRefreshTokenExpirationByDate() {
		Date now = new Date();
		long unixTimestamp = now.getTime() + jwtProperties.getRefreshTokenExpiration() * 1000L;
		return new Date(unixTimestamp);
	}

	public SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
	}

	@Getter
	public enum JwtSubject {
		ACCESS_TOKEN("ACCESS_TOKEN"), REFRESH_TOKEN("REFRESH_TOKEN");

		private String value;

		JwtSubject(String value) {
			this.value = value;
		}
	}
}
