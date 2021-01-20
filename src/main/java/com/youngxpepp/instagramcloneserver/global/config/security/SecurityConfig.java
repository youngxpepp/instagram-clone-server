package com.youngxpepp.instagramcloneserver.global.config.security;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtAuthenticationFilter;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtAuthenticationProvider jwtAuthenticationProvider;
	private final HandlerExceptionResolver handlerExceptionResolver;
	private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> customOAuth2AuthorizationRequestRepository;
	private final AuthenticationSuccessHandler customOAuth2SuccessHandler;

	@Bean
	JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		// Jwt 인증 필터가 적용될 경로
		List<String> allowedPatterns = new ArrayList<>();
		allowedPatterns.add("/api/v1/follows/**");
		allowedPatterns.add("/api/v1/post/**");
		allowedPatterns.add("/api/v1/articles/**");
		allowedPatterns.add("/api/v1/members/**");
		RequestMatcher allowed = new OrRequestMatcher(allowedPatterns
			.stream()
			.map(AntPathRequestMatcher::new)
			.collect(Collectors.toList()));

		// Jwt 인증 필터가 적용되지 않는 경로
		List<String> deniedPatterns = new ArrayList<>();
		deniedPatterns.add("/api/v1/members/signup");
		RequestMatcher denied = new OrRequestMatcher(deniedPatterns
			.stream()
			.map(AntPathRequestMatcher::new)
			.collect(Collectors.toList()));
		denied = new NegatedRequestMatcher(denied);

		RequestMatcher requestMatcher = new AndRequestMatcher(allowed, denied);

		JwtAuthenticationFilter filter = new JwtAuthenticationFilter(requestMatcher);
		filter.setAuthenticationManager(this.authenticationManagerBean());
		return filter;
	}

	// 필터가 두 번 등록되는 것을 방지
	@Bean
	FilterRegistrationBean filterRegistrationBean(JwtAuthenticationFilter jwtAuthenticationFilter) {
		FilterRegistrationBean<JwtAuthenticationFilter> filterRegistrationBean = new FilterRegistrationBean<>(
			jwtAuthenticationFilter);
		filterRegistrationBean.setEnabled(false);
		return filterRegistrationBean;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.httpBasic().disable();

		http.formLogin().disable();

		http.headers().frameOptions().disable();

		// http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.cors(Customizer.withDefaults());

		AuthenticationEntryPoint authenticationEntryPoint = (request, response, authException) -> {
			this.handlerExceptionResolver.resolveException(request, response, null, authException);
		};

		http
			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint);

		http
			.authorizeRequests()
			.antMatchers("/api/v1/follows/**").hasRole("MEMBER")
			.antMatchers("/api/v1/post/**").hasRole("MEMBER")
			.antMatchers("/api/v1/members/signup").authenticated()
			.antMatchers("/api/v1/members/**").hasRole("MEMBER")
			.antMatchers("/api/v1/articles/**").hasRole("MEMBER");

		http
			.addFilterBefore(this.jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		http.oauth2Login()
			.successHandler(customOAuth2SuccessHandler)
			.authorizationEndpoint()
			.authorizationRequestRepository(customOAuth2AuthorizationRequestRepository);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(this.jwtAuthenticationProvider);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("http://localhost:3000");
		configuration.addAllowedOrigin("https://instagram-clone-client.herokuapp.com");
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}
