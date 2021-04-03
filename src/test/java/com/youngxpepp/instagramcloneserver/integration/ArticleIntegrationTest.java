package com.youngxpepp.instagramcloneserver.integration;

import static org.assertj.core.api.BDDAssertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.MemberLikeArticle;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.AccessTokenClaims;
import com.youngxpepp.instagramcloneserver.test.IntegrationTest;
import com.youngxpepp.instagramcloneserver.test.WithCustomSecurityContext;

public class ArticleIntegrationTest extends IntegrationTest {

	@Test
	@DisplayName("When_게시물 좋아요 API 호출_Then_201 Created")
	@WithCustomSecurityContext(nickname = "youngxpepp")
	public void likeArticleThen201Created() throws Exception {
		Article article = Article.builder()
			.content("this is a content")
			.createdBy(principal)
			.build();
		em.persist(article);

		// when
		MvcResult mvcResult = mockMvc.perform(post("/api/v1/articles/{articleId}/likes", article.getId())
		).andReturn();

		// then
		then(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
	}

	@Test
	@DisplayName("When_게시물 좋아요 취소 API 호출_Then_200 OK")
	@WithCustomSecurityContext(nickname = "youngxpepp")
	public void unlikeArticleThen200Ok() throws Exception {
		// given
		Article article = Article.builder()
			.content("this is a content")
			.createdBy(principal)
			.build();
		em.persist(article);

		MemberLikeArticle memberLikeArticle = MemberLikeArticle.builder()
			.member(principal)
			.article(article)
			.build();
		em.persist(memberLikeArticle);

		String accessToken = jwtUtils.generateAccessToken(AccessTokenClaims.ofMember(principal));

		// when
		MvcResult mvcResult = mockMvc.perform(delete("/api/v1/articles/{articleId}/likes", article.getId())
			.header("Authorization", accessToken)
		).andReturn();

		// then
		then(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
	}
}
