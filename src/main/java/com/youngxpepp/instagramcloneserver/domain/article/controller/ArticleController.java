package com.youngxpepp.instagramcloneserver.domain.article.controller;

import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateArticleRequestBody;
import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateArticleResponseBody;
import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateCommentRequestBody;
import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateCommentResponseBody;
import com.youngxpepp.instagramcloneserver.domain.article.dto.GetArticleResponseBody;
import com.youngxpepp.instagramcloneserver.domain.article.service.ArticleService;
import com.youngxpepp.instagramcloneserver.domain.comment.service.CommentService;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
@Validated
public class ArticleController {

	private final ArticleService articleService;
	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<CreateArticleResponseBody> createArticle(
		@AuthenticationPrincipal @ApiIgnore Member principal,
		@RequestBody CreateArticleRequestBody requestBody) {
		CreateArticleResponseBody responseBody = articleService.createArticle(principal.getId(), requestBody);
		return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
	}

	@GetMapping("/{articleId}")
	public ResponseEntity<GetArticleResponseBody> getArticle(
		@PathVariable("articleId") @NotNull Long articleId,
		@AuthenticationPrincipal @ApiIgnore Member principal
	) {
		GetArticleResponseBody responseBody = articleService.getArticle(principal.getId(), articleId);
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

	@PostMapping("/{articleId}/comments")
	public ResponseEntity<CreateCommentResponseBody> createComment(
		@PathVariable("articleId") @NotNull Long articleId,
		@RequestBody CreateCommentRequestBody requestBody,
		@AuthenticationPrincipal @ApiIgnore Member principal
	) {
		CreateCommentResponseBody responseBody =
			commentService.createComment(principal.getId(), articleId, requestBody);
		return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
	}
}
