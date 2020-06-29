package com.youngxpepp.instagramcloneserver.global.config.security;

import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtAuthenticationFilter;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        List<String> patterns = new ArrayList<>();

//        Jwt 인증 필터가 적용될 경로
        patterns.add("/test/path");

        List<RequestMatcher> requestMatchers = patterns
                .stream()
                .map(pattern -> new AntPathRequestMatcher(pattern))
                .collect(Collectors.toList());

        RequestMatcher requestMatcher = new OrRequestMatcher(requestMatchers);

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(requestMatcher);
        filter.setAuthenticationManager(this.authenticationManagerBean());
        return filter;
    }

//    필터가 두 번 등록되는 것을 방지
    @Bean
    FilterRegistrationBean filterRegistrationBean(JwtAuthenticationFilter jwtAuthenticationFilter) {
        FilterRegistrationBean<JwtAuthenticationFilter> filterRegistrationBean = new FilterRegistrationBean<>(jwtAuthenticationFilter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
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
