package com.youngxpepp.instagramcloneserver.controller;

import java.util.List;
import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import com.youngxpepp.instagramcloneserver.dto.CreateArticleRequestBody;
import com.youngxpepp.instagramcloneserver.dto.CreateArticleResponseBody;
import com.youngxpepp.instagramcloneserver.dto.CreateCommentRequestBody;
import com.youngxpepp.instagramcloneserver.dto.CreateCommentResponseBody;
import com.youngxpepp.instagramcloneserver.dto.GetArticleResponseBody;
import com.youngxpepp.instagramcloneserver.dto.GetCommentsResponseBody;
import com.youngxpepp.instagramcloneserver.service.ArticleService;
import com.youngxpepp.instagramcloneserver.dto.CommentDto;
import com.youngxpepp.instagramcloneserver.mapper.CommentMapper;
import com.youngxpepp.instagramcloneserver.domain.Comment;
import com.youngxpepp.instagramcloneserver.service.CommentService;
import com.youngxpepp.instagramcloneserver.domain.Member;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
@Validated
public class ArticleController {

	private final ArticleService articleService;
	private final CommentService commentService;
	private final CommentMapper commentMapper;

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

	@GetMapping("/{articleId}/comments")
	public GetCommentsResponseBody getComments(
		@PathVariable("articleId") Long articleId,
		Pageable pageable,
		@AuthenticationPrincipal @ApiIgnore Member principal
	) {
		List<Comment> comments = commentService.getAllCommentsByArticleId(articleId, pageable);
		List<CommentDto> commentDtos = commentMapper.toCommentDtoList(comments);
		return new GetCommentsResponseBody(commentDtos);
	}

	@PostMapping("/{articleId}/likes")
	public ResponseEntity<?> likeArticle(
		@PathVariable("articleId") Long articleId,
		@AuthenticationPrincipal @ApiIgnore Member principal
	) {
		articleService.likeArticle(principal.getId(), articleId);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@DeleteMapping("/{articleId}/likes")
	public ResponseEntity<?> unlikeArticle(
		@PathVariable("articleId") Long articleId,
		@AuthenticationPrincipal @ApiIgnore Member principal
	) {
		articleService.unlikeArticle(principal.getId(), articleId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
