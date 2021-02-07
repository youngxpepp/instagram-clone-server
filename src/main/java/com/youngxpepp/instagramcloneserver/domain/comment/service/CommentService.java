package com.youngxpepp.instagramcloneserver.domain.comment.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateCommentRequestBody;
import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateCommentResponseBody;
import com.youngxpepp.instagramcloneserver.domain.article.model.Article;
import com.youngxpepp.instagramcloneserver.domain.article.repository.ArticleRepository;
import com.youngxpepp.instagramcloneserver.domain.comment.dto.CommentMapper;
import com.youngxpepp.instagramcloneserver.domain.comment.model.Comment;
import com.youngxpepp.instagramcloneserver.domain.comment.repository.CommentRepository;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final MemberRepository memberRepository;
	private final ArticleRepository articleRepository;
	private final CommentRepository commentRepository;
	private final CommentMapper commentMapper;

	@Transactional
	public CreateCommentResponseBody createComment(
		Long memberId,
		Long articleId,
		CreateCommentRequestBody requestBody
	) {
		Article article = articleRepository.findById(articleId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		Member member = memberRepository.getOne(memberId);

		Comment comment = commentMapper.toComment(requestBody, member, article);
		commentRepository.save(comment);

		return commentMapper.toCreateCommentResponseBody(comment);
	}

	@Transactional(readOnly = true)
	public List<Comment> getAllCommentsByArticleId(Long articleId, Pageable pageable) {
		return commentRepository.findAllByArticleId(articleId, pageable);
	}
}
