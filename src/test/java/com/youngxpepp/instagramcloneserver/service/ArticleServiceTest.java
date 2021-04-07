package com.youngxpepp.instagramcloneserver.service;

import static org.assertj.core.api.BDDAssertions.*;

import java.util.Arrays;
import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.ArticleImage;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.domain.MemberRole;
import com.youngxpepp.instagramcloneserver.dto.CreateArticleRequestBody;
import com.youngxpepp.instagramcloneserver.dto.CreateArticleResponseBody;
import com.youngxpepp.instagramcloneserver.dto.GetArticleResponseBody;
import com.youngxpepp.instagramcloneserver.runnable.ArticleCreatedRelay;
import com.youngxpepp.instagramcloneserver.test.IntegrationTest;

public class ArticleServiceTest extends IntegrationTest {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	@Autowired
	private ArticleService articleService;

	@MockBean
	private ArticleCreatedRelay articleCreatedRelay;

	@Test
	@DisplayName("When_createArticle_Then_정상 동작")
	public void createArticleThenSuccess() {
		// given
		List<String> imageUrls = Arrays.asList(
			"https://imgur.com/gallery/3jDDE0O",
			"https://imgur.com/gallery/c6mY4Zo");
		CreateArticleRequestBody requestBody = CreateArticleRequestBody.builder()
			.content("article content")
			.imageUrls(imageUrls)
			.build();

		// when
		CreateArticleResponseBody responseBody = articleService.createArticle(principal.getId(), requestBody);

		// then
		then(responseBody.getId()).isNotNull();
		then(responseBody.getContent()).isEqualTo(requestBody.getContent());
		then(responseBody.getImageUrls()).isEqualTo(imageUrls);
	}

	@Test
	@DisplayName("When_getArticle_Then_정상 동작")
	public void getArticleThenSuccess() {
		// given
		Member createdBy = Member.builder()
			.name("kangyeop.lee")
			.nickname("kangyeop")
			.role(MemberRole.MEMBER)
			.build();
		em.persist(createdBy);
		Article article = Article.builder()
			.content("article content")
			.createdBy(createdBy)
			.build();
		ArticleImage articleImage = ArticleImage.builder()
			.url("https://imgur.com/gallery/3jDDE0O")
			.article(article)
			.build();
		article.getArticleImages().add(articleImage);
		em.persist(article);

		// when
		GetArticleResponseBody responseBody = articleService.getArticle(principal.getId(), article.getId());

		// then
		then(responseBody.getId()).isEqualTo(article.getId());
		then(responseBody.getContent()).isEqualTo(article.getContent());
		then(responseBody.getIsLiked()).isFalse();
		then(responseBody.getImageUrls()).containsOnly(articleImage.getUrl());
		then(responseBody.getCreatedBy().getId()).isEqualTo(createdBy.getId());
		then(responseBody.getCreatedBy().getNickname()).isEqualTo(createdBy.getNickname());
		then(responseBody.getCreatedBy().getProfileImageUrl()).isEqualTo(createdBy.getProfileImageUrl());
	}
}