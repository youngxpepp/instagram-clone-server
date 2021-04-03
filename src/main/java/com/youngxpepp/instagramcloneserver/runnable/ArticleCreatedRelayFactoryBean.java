package com.youngxpepp.instagramcloneserver.runnable;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import com.youngxpepp.instagramcloneserver.dao.ArticleCreatedRepository;
import com.youngxpepp.instagramcloneserver.service.FeedService;

@RequiredArgsConstructor
public class ArticleCreatedRelayFactoryBean implements FactoryBean<ArticleCreatedRelay> {

	private static String[] profiles = new String[]{
		"dev",
		"local",
		"prod"
	};

	private final ArticleCreatedRepository articleCreatedRepository;
	private final FeedService feedService;
	private final Environment environment;

	@Override
	public ArticleCreatedRelay getObject() throws Exception {
		ArticleCreatedRelay relay = new ArticleCreatedRelay(
			articleCreatedRepository,
			feedService
		);

		if (!environment.acceptsProfiles(Profiles.of(profiles))) {
			relay.togglePaused(true);
		}

		return relay;
	}

	@Override
	public Class<?> getObjectType() {
		return ArticleCreatedRelay.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
