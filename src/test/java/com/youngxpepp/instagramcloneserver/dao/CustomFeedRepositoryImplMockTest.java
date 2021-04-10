package com.youngxpepp.instagramcloneserver.dao;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.jdbc.core.JdbcOperations;

import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.Feed;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.test.MockTest;

public class CustomFeedRepositoryImplMockTest extends MockTest {

	@Mock
	private JdbcOperations jdbcOperations;

	@InjectMocks
	@Spy
	private CustomFeedRepositoryImpl feedRepository;

	@ParameterizedTest
	@CsvSource({
		"99, 100, 1",
		"100, 100, 1",
		"101, 100, 2",
		"100, 10, 10",
	})
	public void batchInsertThenRightBatchUpdateTimes(int feedSize, int batchSize, int expectedCalledTimes) {
		// given
		List<Feed> feeds = new ArrayList<>();
		Member member = Member.builder().build();
		Article article = Article.builder().build();
		for (int i = 0; i < feedSize; i++) {
			Feed feed = Feed.builder()
				.createdAt(LocalDateTime.now())
				.member(member)
				.article(article)
				.build();
			feeds.add(feed);
		}
		given(feedRepository.getBatchSize()).willReturn(batchSize);

		// when
		feedRepository.batchInsert(feeds);

		// then
		then(feedRepository)
			.should(times(expectedCalledTimes))
			.bulkInsert(anyIterable());
	}
}
