package com.youngxpepp.instagramcloneserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youngxpepp.instagramcloneserver.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {
}
