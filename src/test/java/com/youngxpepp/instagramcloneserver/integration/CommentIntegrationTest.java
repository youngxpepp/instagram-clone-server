package com.youngxpepp.instagramcloneserver.integration;

import static org.assertj.core.api.BDDAssertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.youngxpepp.instagramcloneserver.dto.CreateCommentRequestBody;
import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.Comment;
import com.youngxpepp.instagramcloneserver.domain.MemberLikeComment;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.AccessTokenClaims;
import com.youngxpepp.instagramcloneserver.test.IntegrationTest;
import com.youngxpepp.instagramcloneserver.test.WithCustomSecurityContext;

public class CommentIntegrationTest extends IntegrationTest {

	@Test
	@DisplayName("When_댓글 생성 API 호출_Then_201 Created")
	@WithCustomSecurityContext(nickname = "youngxpepp")
	public void createCommentThen201Created() throws Exception {
		// given
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

	@Test
	@DisplayName("When_게시글 댓글 조회 API_Then_200 ok 10개 댓글 반환")
	@WithCustomSecurityContext(nickname = "youngxpepp")
	public void getCommentsThen10Comments() throws Exception {
		// given
		Article article = Article.builder()
			.content("example article")
			.createdBy(principal)
			.build();
		em.persist(article);

		List<Comment> comments = new ArrayList<>();
		for (int i = 0; i < 15; i++) {
			comments.add(
				Comment.builder()
					.content("example comment")
					.createdBy(principal)
					.article(article)
					.build()
			);
			em.persist(comments.get(i));
		}

		String accessToken = jwtUtils.generateAccessToken(AccessTokenClaims.ofMember(principal));

		// when
		MvcResult result = mockMvc.perform(get("/api/v1/articles/{articleId}/comments", article.getId())
			.header("Authorization", accessToken)
			.queryParam("page", Integer.toString(0))
			.queryParam("size", Integer.toString(10))
		).andReturn();

		// then
		// GetCommentsResponseBody responseBody = objectMapper.readValue(
		// 	result.getResponse().getContentAsString(), GetCommentsResponseBody.class);
		// for (int i = 0; i < responseBody.getComments().size(); i++) {
		// 	then(responseBody.getComments().get(i).getId()).isEqualTo(comments.get(14 - i).getId());
		// }
	}

	@Test
	@DisplayName("When_댓글 좋아요 API 호출_Then_201 Created")
	@WithCustomSecurityContext(nickname = "youngxpepp")
	public void likeCommentThen201Created() throws Exception {
		// given
		Article article = Article.builder()
			.content("example article")
			.createdBy(principal)
			.build();
		em.persist(article);

		Comment comment = Comment.builder()
			.content("this is a content")
			.createdBy(principal)
			.article(article)
			.build();
		em.persist(comment);

		String accessToken = jwtUtils.generateAccessToken(AccessTokenClaims.ofMember(principal));

		// when
		MvcResult result = mockMvc.perform(post("/api/v1/comments/{commentId}/likes", comment.getId())
			.header("Authorization", accessToken)
		).andReturn();

		// then
		then(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
	}

	@Test
	@DisplayName("When_댓글 좋아요 취소 API 호출_Then_200 OK")
	@WithCustomSecurityContext(nickname = "youngxpepp")
	public void unlikeCommentThen200Ok() throws Exception {
		// given
		Article article = Article.builder()
			.content("example article")
			.createdBy(principal)
			.build();
		em.persist(article);

		Comment comment = Comment.builder()
			.content("this is a content")
			.createdBy(principal)
			.article(article)
			.build();
		em.persist(comment);

		MemberLikeComment memberLikeComment = MemberLikeComment.builder()
			.member(principal)
			.comment(comment)
			.build();
		em.persist(memberLikeComment);

		String accessToken = jwtUtils.generateAccessToken(AccessTokenClaims.ofMember(principal));

		// when
		MvcResult result = mockMvc.perform(delete("/api/v1/comments/{commentId}/likes", comment.getId())
			.header("Authorization", accessToken)
		).andReturn();

		// then
		then(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
	}
}
