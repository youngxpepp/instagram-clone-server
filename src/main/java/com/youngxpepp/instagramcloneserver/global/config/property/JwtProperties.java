package com.youngxpepp.instagramcloneserver.global.config.property;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Validated
@Getter
@Setter
public class JwtProperties {

	@NotNull
	@NotEmpty
	private String issuer;

	@Min(600)
	@Max(3600)
	private long accessTokenExpiration;

	@Max(604800)
	private long refreshTokenExpiration;

	@NotNull
	@NotEmpty
	@Length(min = 32)
	private String secret;
}
