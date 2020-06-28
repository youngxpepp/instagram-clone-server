package com.youngxpepp.instagramcloneserver.global.config.security;

import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtAuthenticationFailureHandler;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtAuthenticationFilter;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtAuthenticationProvider;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtAuthenticationSuccessHandler;
import com.youngxpepp.instagramcloneserver.global.config.security.matcher.SkipRequestMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

    @Autowired
    private JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        List<String> patterns = new ArrayList<>();
        patterns.add("/api/v1/login");

        RequestMatcher requestMatcher = new SkipRequestMatcher(patterns);

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(requestMatcher, this.jwtAuthenticationSuccessHandler, this.jwtAuthenticationFailureHandler);
        filter.setAuthenticationManager(this.authenticationManagerBean());
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.httpBasic().disable();

        http.headers().frameOptions().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .addFilterBefore(this.jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(this.jwtAuthenticationProvider);
    }
}
