package com.youngxpepp.instagramcloneserver.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youngxpepp.instagramcloneserver.domain.MemberLikeComment;

public interface MemberLikeCommentRepository extends JpaRepository<MemberLikeComment, Long> {

	Optional<MemberLikeComment> findByMemberIdAndCommentId(Long memberId, Long commentId);
}
