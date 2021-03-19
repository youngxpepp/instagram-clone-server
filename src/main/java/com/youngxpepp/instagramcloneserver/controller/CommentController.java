package com.youngxpepp.instagramcloneserver.controller;

import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import com.youngxpepp.instagramcloneserver.service.CommentService;
import com.youngxpepp.instagramcloneserver.domain.Member;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Validated
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/{commentId}/likes")
	public ResponseEntity<?> likeComment(
		@PathVariable("commentId") @NotNull Long commentId,
		@AuthenticationPrincipal @ApiIgnore Member principal
	) {
		commentService.likeComment(principal.getId(), commentId);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@DeleteMapping("/{commentId}/likes")
	public ResponseEntity<?> unlikeComment(
		@PathVariable("commentId") @NotNull Long commentId,
		@AuthenticationPrincipal @ApiIgnore Member principal
	) {
		commentService.unlikeComment(principal.getId(), commentId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
