package com.youngxpepp.instagramcloneserver.domain.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateArticleRequestBody;
import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateArticleResponseBody;
import com.youngxpepp.instagramcloneserver.domain.article.service.ArticleService;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

	private final ArticleService articleService;

	@PostMapping
	public ResponseEntity<CreateArticleResponseBody> createArticle(
		@AuthenticationPrincipal @ApiIgnore Member principal,
		@RequestBody CreateArticleRequestBody requestBody) {
		CreateArticleResponseBody responseBody = articleService.createArticle(principal.getId(), requestBody);
		return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
	}
}
