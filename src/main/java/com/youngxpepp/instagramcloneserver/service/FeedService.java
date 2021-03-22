package com.youngxpepp.instagramcloneserver.service;

import java.util.concurrent.Future;

public interface FeedService {

	void createFeed(long articleCreatedId);

	Future<?> createFeedAsync(long articleCreatedId);
}
