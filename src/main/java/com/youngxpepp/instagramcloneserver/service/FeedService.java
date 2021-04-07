package com.youngxpepp.instagramcloneserver.service;

import java.util.concurrent.Future;

public interface FeedService {

	void createFeeds(long articleCreatedId);

	Future<?> createFeedsAsync(long articleCreatedId);
}
