package com.youngxpepp.instagramcloneserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youngxpepp.instagramcloneserver.domain.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long>, CustomFeedRepository {
}
