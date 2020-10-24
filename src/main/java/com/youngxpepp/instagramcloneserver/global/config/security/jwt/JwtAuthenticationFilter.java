package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.youngxpepp.instagramcloneserver.global.error.exception.NoAuthorizationException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired
	private HandlerExceptionResolver handlerExceptionResolver;

	public JwtAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		throws AuthenticationException, IOException, ServletException, JwtException {
		SecurityContextHolder.clearContext();
		String accessToken = request.getHeader("Authorization");
		if (accessToken == null) {
			throw new NoAuthorizationException(null);
		}
		PreJwtAuthenticationToken preJwtAuthenticationToken = new PreJwtAuthenticationToken(accessToken);
		return getAuthenticationManager().authenticate(preJwtAuthenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authResult);
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		handlerExceptionResolver.resolveException(request, response, null, failed);
	}
}
