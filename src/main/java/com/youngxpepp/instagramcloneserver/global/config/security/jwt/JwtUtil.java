package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import com.youngxpepp.instagramcloneserver.global.config.property.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Autowired
    JwtProperties jwtProperties;

    public static final String TOKEN_PREFIX = "Bearer ";

    public String generateAccessToken(AccessTokenClaims claims) {
        String accessToken = Jwts.builder()
                .setIssuer(jwtProperties.getIssuer())
                .setSubject(JwtSubject.ACCESS_TOKEN.getValue())
                .setExpiration(this.getAccessTokenExpirationByDate())

                .addClaims(claims.getClaimsByMap())

                .signWith(this.getSecretKey())
                .compact();

        return JwtUtil.TOKEN_PREFIX + accessToken;
    }

    public Jws<Claims> verifyAccessToken(String bearerAccessToken) {
        if(bearerAccessToken.indexOf(JwtUtil.TOKEN_PREFIX) != 0) {
            throw new JwtException("");
        }

        String accessToken = bearerAccessToken.substring(JwtUtil.TOKEN_PREFIX.length());

        Jws<Claims> jws = Jwts.parserBuilder()
                .requireIssuer(jwtProperties.getIssuer())
                .requireSubject(JwtSubject.ACCESS_TOKEN.getValue())
                .setSigningKey(this.getSecretKey())
                .build()
                .parseClaimsJws(accessToken);

        return jws;
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
