package com.youngxpepp.instagramcloneserver.integration;

import static org.assertj.core.api.BDDAssertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import com.youngxpepp.instagramcloneserver.domain.article.model.Article;
import com.youngxpepp.instagramcloneserver.domain.article.model.MemberLikeArticle;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.AccessTokenClaims;
import com.youngxpepp.instagramcloneserver.test.IntegrationTest;

public class ArticleIntegrationTest extends IntegrationTest {

	@Test
	@DisplayName("When_게시물 좋아요 API 호출_Then_201 Created")
	public void likeArticle_0() throws Exception {
		// given
		em.persist(principal);

		Article article = Article.builder()
			.content("this is a content")
			.createdBy(principal)
			.build();
		em.persist(article);

		String accessToken = jwtUtils.generateAccessToken(AccessTokenClaims.ofMember(principal));

		// when
		MvcResult mvcResult = mockMvc.perform(post("/api/v1/articles/{articleId}/likes", article.getId())
			.header("Authorization", accessToken)
		).andReturn();

		// then
		then(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
	}

	@Test
	@DisplayName("When_게시물 좋아요 취소 API 호출_Then_200 OK")
	public void unlikeArticle_0() throws Exception {
		// given
		em.persist(principal);

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
