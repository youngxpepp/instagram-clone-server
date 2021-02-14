package com.youngxpepp.instagramcloneserver.domain.comment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youngxpepp.instagramcloneserver.domain.comment.model.MemberLikeComment;

public interface MemberLikeCommentRepository extends JpaRepository<MemberLikeComment, Long> {

	Optional<MemberLikeComment> findByMemberIdAndCommentId(Long memberId, Long commentId);
}
