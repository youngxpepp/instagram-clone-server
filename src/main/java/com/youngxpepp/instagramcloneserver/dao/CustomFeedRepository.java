package com.youngxpepp.instagramcloneserver.dao;

import com.youngxpepp.instagramcloneserver.domain.Feed;

public interface CustomFeedRepository {

	void batchInsert(Iterable<Feed> feeds);

	void bulkInsert(Iterable<Feed> feeds);
}
