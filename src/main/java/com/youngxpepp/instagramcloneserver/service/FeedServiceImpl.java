package com.youngxpepp.instagramcloneserver.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.dao.ArticleCreatedRepository;
import com.youngxpepp.instagramcloneserver.dao.FeedRepository;
import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.ArticleCreated;
import com.youngxpepp.instagramcloneserver.domain.Feed;
import com.youngxpepp.instagramcloneserver.domain.Follow;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

	private final ArticleCreatedRepository articleCreatedRepository;
	private final FeedRepository feedRepository;

	@Override
	@Transactional
	public void createFeeds(long articleCreatedId) {
		ArticleCreated ac = articleCreatedRepository.findByIdWithFollowers(articleCreatedId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
		Article article = ac.getArticle();
		List<Follow> follows = ac.getArticle().getCreatedBy().getFollowers();

		if (follows.isEmpty()) {
			return;
		}

		List<Feed> feeds = new ArrayList<>();
		for (Follow follow : follows) {
			Feed feed = Feed.builder()
				.createdAt(article.getCreatedAt())
				.member(follow.getFollowingMember())
				.article(article)
				.build();
			feeds.add(feed);
		}

		feedRepository.saveAll(feeds);
	}

	@Override
	@Async("articleCreatedExecutor")
	@Transactional
	public Future<?> createFeedsAsync(long articleCreatedId) {
		createFeeds(articleCreatedId);
		return new CompletableFuture<>();
	}
}
