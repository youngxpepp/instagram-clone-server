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
import com.youngxpepp.instagramcloneserver.dao.MemberRepository;
import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.ArticleCreated;
import com.youngxpepp.instagramcloneserver.domain.Feed;
import com.youngxpepp.instagramcloneserver.domain.Follow;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

	private final ArticleCreatedRepository articleCreatedRepository;
	private final MemberRepository memberRepository;
	private final FeedRepository feedRepository;

	@Override
	@Transactional
	public void createFeed(long articleCreatedId) {
		ArticleCreated ac = articleCreatedRepository.findByIdWithFollowers(articleCreatedId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
		Article article = ac.getArticle();
		List<Follow> follows = ac.getArticle().getCreatedBy().getFollowers();

		if (follows.isEmpty()) {
			return;
		}

		List<Feed> feeds = new ArrayList<>();
		for (Follow follow : follows) {
			Member follower = memberRepository.getOne(follow.getFollowingMember().getId());
			Feed feed = Feed.builder()
				.createdAt(article.getCreatedAt())
				.member(follower)
				.article(article)
				.build();
			feeds.add(feed);
		}

		feedRepository.saveAll(feeds);
	}

	@Override
	@Async("articleCreatedExecutor")
	@Transactional
	public Future<?> createFeedAsync(long articleCreatedId) {
		createFeed(articleCreatedId);
		return new CompletableFuture<>();
	}
}
