package com.youngxpepp.instagramcloneserver.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.youngxpepp.instagramcloneserver.domain.Comment;

public interface CustomCommentRepository {

	List<Comment> findAllByArticleId(Long articleId, Pageable pageable);
}
