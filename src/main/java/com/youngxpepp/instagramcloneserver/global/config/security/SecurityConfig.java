package com.youngxpepp.instagramcloneserver.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final HandlerExceptionResolver handlerExceptionResolver;
	private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> customOAuth2AuthorizationRequestRepository;
	private final AuthenticationSuccessHandler customOAuth2SuccessHandler;

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
			.antMatchers("/api/v1/articles/**").hasRole("MEMBER")
			.antMatchers("/api/v1/comments/**").hasRole("MEMBER");

		http.oauth2Login()
			.successHandler(customOAuth2SuccessHandler)
			.authorizationEndpoint()
			.authorizationRequestRepository(customOAuth2AuthorizationRequestRepository);
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
