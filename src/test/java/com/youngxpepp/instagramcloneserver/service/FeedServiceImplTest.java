package com.youngxpepp.instagramcloneserver.service;

import static com.youngxpepp.instagramcloneserver.domain.QFeed.*;
import static org.assertj.core.api.BDDAssertions.*;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.ArticleCreated;
import com.youngxpepp.instagramcloneserver.domain.ArticleImage;
import com.youngxpepp.instagramcloneserver.domain.Feed;
import com.youngxpepp.instagramcloneserver.domain.Follow;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.domain.MemberRole;
import com.youngxpepp.instagramcloneserver.test.IntegrationTest;

public class FeedServiceImplTest extends IntegrationTest {

	@Autowired
	private FeedService feedService;

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void createFeedThenSuccess() {
		// given
		jpaTestSupport.save(principal);

		Member follower = Member.builder()
			.name("kangyeop")
			.nickname("lky")
			.role(MemberRole.MEMBER)
			.build();
		jpaTestSupport.save(follower);

		Follow follow = Follow.builder()
			.followingMember(follower)
			.followedMember(principal)
			.build();
		jpaTestSupport.save(follow);

		ArticleImage newArticleImage = ArticleImage.builder()
			.url("https://cdn.pixabay.com/photo/2020/12/13/16/21/stork-5828727_1280.jpg")
			.build();
		Article newArticle = Article.builder()
			.content("content")
			.createdBy(principal)
			.articleImages(Collections.singletonList(newArticleImage))
			.build();
		jpaTestSupport.save(newArticle);

		ArticleCreated newArticleCreated = ArticleCreated.builder()
			.article(newArticle)
			.build();
		jpaTestSupport.save(newArticleCreated);

		// when
		feedService.createFeed(newArticleCreated.getId());

		// then
		Feed one = jpaQueryFactory.selectFrom(feed)
			.where(feed.member.id.eq(follower.getId()))
			.where(feed.article.id.eq(newArticle.getId()))
			.fetchOne();
		then(one).isNotNull();
	}
}
