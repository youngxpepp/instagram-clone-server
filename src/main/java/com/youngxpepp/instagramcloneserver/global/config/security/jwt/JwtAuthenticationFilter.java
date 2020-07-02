package com.youngxpepp.instagramcloneserver.global.config.security.jwt;

import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;
import com.youngxpepp.instagramcloneserver.global.error.exception.NoAuthorizationException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;
import sun.util.resources.th.CalendarData_th;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        try {
            super.doFilter(req, res, chain);
            return;
        } catch (JwtException | AuthenticationException | BusinessException e) {
            SecurityContextHolder.clearContext();
            handlerExceptionResolver.resolveException(request, response, null, e);

            return;
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException, JwtException {
        String accessToken = request.getHeader("Authorization");

        if(accessToken == null) {
            throw new NoAuthorizationException("No authorization in header");
        }

        PreJwtAuthenticationToken preJwtAuthenticationToken = new PreJwtAuthenticationToken(accessToken);

        return getAuthenticationManager().authenticate(preJwtAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);

        chain.doFilter(request, response);
    }
}
