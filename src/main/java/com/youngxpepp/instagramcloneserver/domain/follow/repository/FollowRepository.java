package com.youngxpepp.instagramcloneserver.domain.follow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youngxpepp.instagramcloneserver.domain.follow.model.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {

	Optional<Follow> findByFollowingMemberIdAndFollowedMemberId(Long followingMemberId, Long followedMemberId);

	Long countByFollowingMemberId(Long followingMemberId);

	Long countByFollowedMemberId(Long followedMemberId);
}
