package com.youngxpepp.instagramcloneserver.dao;

import static com.youngxpepp.instagramcloneserver.domain.QFeed.*;
import static org.assertj.core.api.BDDAssertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.Feed;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.test.RepositoryTest;

public class FeedRepositoryTest extends RepositoryTest {

	@Autowired
	private FeedRepository feedRepository;

	@Test
	public void bulkInsertThenSuccess() {
		// given
		em.persist(principal);

		Article newArticle = Article.builder()
			.content("this is a content")
			.createdBy(principal)
			.build();
		em.persist(newArticle);

		List<Member> followers = new ArrayList<>();
		List<Feed> newFeeds = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			Member follower = Member.builder()
				.build();
			followers.add(follower);
			em.persist(follower);
			Feed newFeed = Feed.builder()
				.createdAt(newArticle.getCreatedAt())
				.member(follower)
				.article(newArticle)
				.build();
			newFeeds.add(newFeed);
		}

		em.flush();
		em.clear();

		// when
		feedRepository.bulkInsert(newFeeds);

		// then
		List<Feed> selectedFeeds = jpaQueryFactory.selectFrom(feed)
			.where(feed.article.id.eq(newArticle.getId()))
			.fetch();
		then(selectedFeeds).hasSize(5);
	}
}
