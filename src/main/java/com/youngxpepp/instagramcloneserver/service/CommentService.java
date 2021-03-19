package com.youngxpepp.instagramcloneserver.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.dto.CreateCommentRequestBody;
import com.youngxpepp.instagramcloneserver.dto.CreateCommentResponseBody;
import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.dao.ArticleRepository;
import com.youngxpepp.instagramcloneserver.mapper.CommentMapper;
import com.youngxpepp.instagramcloneserver.domain.Comment;
import com.youngxpepp.instagramcloneserver.domain.MemberLikeComment;
import com.youngxpepp.instagramcloneserver.dao.CommentRepository;
import com.youngxpepp.instagramcloneserver.dao.MemberLikeCommentRepository;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.dao.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final MemberRepository memberRepository;
	private final ArticleRepository articleRepository;
	private final CommentRepository commentRepository;
	private final MemberLikeCommentRepository memberLikeCommentRepository;
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

	@Transactional
	public void likeComment(Long memberId, Long commentId) {
		memberLikeCommentRepository.findByMemberIdAndCommentId(memberId, commentId)
			.ifPresent(t -> {
				throw new BusinessException(ErrorCode.ENTITY_ALREADY_EXIST);
			});
		MemberLikeComment memberLikeComment = MemberLikeComment.builder()
			.member(memberRepository.getOne(memberId))
			.comment(commentRepository.getOne(commentId))
			.build();
		memberLikeCommentRepository.save(memberLikeComment);
	}

	@Transactional
	public void unlikeComment(Long memberId, Long commentId) {
		MemberLikeComment memberLikeComment =
			memberLikeCommentRepository.findByMemberIdAndCommentId(memberId, commentId)
				.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
		memberLikeCommentRepository.delete(memberLikeComment);
	}
}
