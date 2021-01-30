package com.youngxpepp.instagramcloneserver.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youngxpepp.instagramcloneserver.domain.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
