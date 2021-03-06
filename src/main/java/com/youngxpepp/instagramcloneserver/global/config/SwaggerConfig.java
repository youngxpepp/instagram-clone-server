package com.youngxpepp.instagramcloneserver.global.config;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

	@Bean
	public Boolean swaggerEnabled(Environment environment) {
		return environment.acceptsProfiles(Profiles.of("dev", "local"));
	}

	@Bean
	public Docket getDocket(@Autowired Boolean swaggerEnabled) {
		RequestParameter authorization = new RequestParameterBuilder()
			.in(ParameterType.HEADER)
			.name("Authorization")
			.required(false)
			.build();

		return new Docket(DocumentationType.SWAGGER_2)
			.enable(swaggerEnabled)
			.globalRequestParameters(Arrays.asList(authorization))
			.select()
			.apis(RequestHandlerSelectors.any())
			.paths(PathSelectors.ant("/api/v1/**"))
			.build();
	}
}
