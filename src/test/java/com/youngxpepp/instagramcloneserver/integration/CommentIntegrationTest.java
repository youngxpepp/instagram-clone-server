package com.youngxpepp.instagramcloneserver.integration;

import static org.assertj.core.api.BDDAssertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateCommentRequestBody;
import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateCommentResponseBody;
import com.youngxpepp.instagramcloneserver.domain.article.model.Article;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.AccessTokenClaims;
import com.youngxpepp.instagramcloneserver.test.IntegrationTest;

public class CommentIntegrationTest extends IntegrationTest {

	@Test
	@DisplayName("When_댓글 생성 API 호출_Then_201 Created")
	public void testCreateComment_0() throws Exception {
		// given
		em.persist(principal);

		Article article = Article.builder()
			.content("this is an article")
			.createdBy(principal)
			.build();
		em.persist(article);

		CreateCommentRequestBody requestBody = CreateCommentRequestBody.builder()
			.content("this is a comment")
			.build();

		String accessToken = jwtUtils.generateAccessToken(AccessTokenClaims.ofMember(principal));

		// when
		MvcResult result = mockMvc.perform(
			post("/api/v1/articles/{articleId}/comments", article.getId())
				.header("authorization", accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody))
		).andReturn();

		// then
		then(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
	}
}
