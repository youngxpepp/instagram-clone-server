package com.youngxpepp.instagramcloneserver.global.util;

import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import com.youngxpepp.instagramcloneserver.global.config.property.JwtProperties;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.AccessTokenClaims;
import com.youngxpepp.instagramcloneserver.global.error.exception.NoPrefixJwtException;

@Component
public class JwtUtils {

	public static final String TOKEN_PREFIX = "Bearer ";

	private final JwtProperties jwtProperties;
	private final JwtParser accessTokenParser;
	private final ObjectMapper objectMapper;

	private Date getExpirationByDate(long expiration) {
		Date now = new Date();
		long unixTimestamp = now.getTime() + expiration * 1000L;
		return new Date(unixTimestamp);
	}

	private Date getAccessTokenExpirationByDate() {
		return getExpirationByDate(jwtProperties.getAccessTokenExpiration());
	}

	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
	}

	public JwtUtils(JwtProperties jwtProperties, ObjectMapper objectMapper) {
		this.jwtProperties = jwtProperties;
		this.objectMapper = objectMapper;
		this.accessTokenParser = Jwts.parserBuilder()
			.requireIssuer(jwtProperties.getIssuer())
			.requireSubject(JwtSubject.ACCESS_TOKEN.name())
			.setSigningKey(getSecretKey())
			.build();
	}

	public String generateAccessToken(AccessTokenClaims claims) {
		String accessToken = Jwts.builder()
			.setIssuer(jwtProperties.getIssuer())
			.setSubject(JwtSubject.ACCESS_TOKEN.name())
			.setExpiration(getAccessTokenExpirationByDate())
			.addClaims(objectMapper.convertValue(claims, new TypeReference<Map<String, Object>>() {
			}))
			.signWith(getSecretKey())
			.compact();

		return JwtUtils.TOKEN_PREFIX + accessToken;
	}

	public AccessTokenClaims verifyAccessToken(String bearerAccessToken) {
		if (bearerAccessToken.indexOf(JwtUtils.TOKEN_PREFIX) != 0) {
			throw new NoPrefixJwtException(bearerAccessToken);
		}
		String accessToken = bearerAccessToken.substring(JwtUtils.TOKEN_PREFIX.length());
		Jws<Claims> jws = accessTokenParser.parseClaimsJws(accessToken);
		AccessTokenClaims claims = objectMapper.convertValue(jws.getBody(), new TypeReference<AccessTokenClaims>() {
		});
		return claims;
	}

	public enum JwtSubject {
		ACCESS_TOKEN, REFRESH_TOKEN;
	}
}
