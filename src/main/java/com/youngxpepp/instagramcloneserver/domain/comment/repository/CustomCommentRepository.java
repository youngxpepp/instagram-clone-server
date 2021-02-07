package com.youngxpepp.instagramcloneserver.domain.comment.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.youngxpepp.instagramcloneserver.domain.comment.model.Comment;

public interface CustomCommentRepository {

	List<Comment> findAllByArticleId(Long articleId, Pageable pageable);
}
