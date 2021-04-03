package com.youngxpepp.instagramcloneserver.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.youngxpepp.instagramcloneserver.dao.ArticleCreatedRepository;
import com.youngxpepp.instagramcloneserver.runnable.ArticleCreatedRelay;
import com.youngxpepp.instagramcloneserver.runnable.ArticleCreatedRelayFactoryBean;
import com.youngxpepp.instagramcloneserver.service.FeedService;

@Configuration
@RequiredArgsConstructor
public class ArticleCreatedRelayConfig {

	private final ArticleCreatedRepository articleCreatedRepository;
	private final FeedService feedService;
	private final Environment environment;

	@Bean(name = "articleCreatedRelay")
	public FactoryBean<ArticleCreatedRelay> articleCreatedRelayFactory() {
		return new ArticleCreatedRelayFactoryBean(
			articleCreatedRepository,
			feedService,
			environment
		);
	}
}
