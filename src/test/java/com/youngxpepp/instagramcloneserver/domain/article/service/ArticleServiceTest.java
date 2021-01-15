package com.youngxpepp.instagramcloneserver.domain.article.service;

import static org.assertj.core.api.BDDAssertions.*;

import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateArticleRequestBody;
import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateArticleResponseBody;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;
import com.youngxpepp.instagramcloneserver.test.IntegrationTest;

public class ArticleServiceTest extends IntegrationTest {

	@Autowired
	private ArticleService articleService;

	@PersistenceContext
	private EntityManager em;

	private Member principal;

	@BeforeEach
	public void beforeEach() {
		principal = Member.builder()
			.name("geonhong lee")
			.nickname("youngxpepp")
			.role(MemberRole.MEMBER)
			.build();
	}

	@Test
	@DisplayName("When_createArticle_Then_전달한 인자와 일치")
	public void testCreateArticle_0() {
		// given
		em.persist(principal);

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
}
