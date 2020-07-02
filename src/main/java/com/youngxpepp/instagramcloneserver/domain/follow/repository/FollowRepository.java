package com.youngxpepp.instagramcloneserver.domain.follow.repository;

import com.youngxpepp.instagramcloneserver.domain.follow.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);
}
