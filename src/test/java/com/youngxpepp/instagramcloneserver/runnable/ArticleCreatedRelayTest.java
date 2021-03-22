package com.youngxpepp.instagramcloneserver.runnable;

import static org.assertj.core.api.BDDAssertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.youngxpepp.instagramcloneserver.dao.ArticleCreatedRepository;
import com.youngxpepp.instagramcloneserver.service.FeedService;
import com.youngxpepp.instagramcloneserver.test.MockTest;

public class ArticleCreatedRelayTest extends MockTest {

	@InjectMocks
	private ArticleCreatedRelay relay;

	@Mock
	private ArticleCreatedRepository articleCreatedRepository;

	@Mock
	private FeedService feedService;

	@Test
	@Timeout(1000)
	public void runThenKeepWaitingForNotPaused() throws InterruptedException {
		// given
		relay.toggleStopped(true);

		// when
		relay.run();
	}
}
