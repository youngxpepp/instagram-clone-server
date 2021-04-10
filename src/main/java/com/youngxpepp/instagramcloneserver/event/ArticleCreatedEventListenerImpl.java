package com.youngxpepp.instagramcloneserver.event;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.youngxpepp.instagramcloneserver.dao.ArticleRepository;
import com.youngxpepp.instagramcloneserver.dao.FeedRepository;
import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.Feed;
import com.youngxpepp.instagramcloneserver.domain.Follow;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Component
@RequiredArgsConstructor
public class ArticleCreatedEventListenerImpl implements ArticleCreatedEventListener {

	private final ArticleRepository articleRepository;
	private final FeedRepository feedRepository;

	@Async("articleCreatedExecutor")
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = {ArticleCreatedEvent.class})
	@Transactional
	public void onArticleCreatedEvent(ArticleCreatedEvent event) {
		Article article = articleRepository.findByIdWithFollowers(event.getArticleId())
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
		List<Follow> follows = article.getCreatedBy().getFollowers();

		if (follows.isEmpty()) {
			return;
		}

		List<Feed> feeds = new ArrayList<>();
		for (Follow follow : follows) {
			Feed feed = Feed.builder()
				.createdAt(article.getCreatedAt())
				.article(article)
				.member(follow.getFollowingMember())
				.build();
			feeds.add(feed);
		}
		feedRepository.batchInsert(feeds);
	}
}
