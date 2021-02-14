package com.youngxpepp.instagramcloneserver.domain.article.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youngxpepp.instagramcloneserver.domain.article.model.MemberLikeArticle;

public interface MemberLikeArticleRepository extends JpaRepository<MemberLikeArticle, Long> {

	boolean existsByMemberIdAndArticleId(Long memberId, Long articleId);

	Optional<MemberLikeArticle> findByMemberIdAndArticleId(Long memberId, Long articleId);
}
