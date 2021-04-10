package com.youngxpepp.instagramcloneserver.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.Feed;

@Repository
@RequiredArgsConstructor
public class CustomFeedRepositoryImpl implements CustomFeedRepository {

	private static final int BATCH_SIZE = 100;

	private final JdbcOperations jdbcOperations;

	@Override
	@Transactional
	public void batchInsert(Iterable<Feed> feeds) {
		List<Feed> inserted = new ArrayList<>();
		Iterator<Feed> feedIterator = feeds.iterator();
		while (feedIterator.hasNext()) {
			Feed feed = feedIterator.next();
			inserted.add(feed);
			if (inserted.size() % getBatchSize() == 0 || !feedIterator.hasNext()) {
				bulkInsert(inserted);
				inserted.clear();
			}
		}
	}

	@Override
	@Transactional
	public void bulkInsert(Iterable<Feed> feeds) {
		List<Feed> inserted = new ArrayList<>();
		feeds.forEach(inserted::add);

		String sql = "INSERT INTO feed(created_at, member_id, article_id) VALUES(?, ?, ?);";
		jdbcOperations.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				ps.setString(1, inserted.get(index).getCreatedAt().toString());
				ps.setLong(2, inserted.get(index).getMember().getId());
				ps.setLong(3, inserted.get(index).getArticle().getId());
			}

			@Override
			public int getBatchSize() {
				return inserted.size();
			}
		});
	}

	public int getBatchSize() {
		return BATCH_SIZE;
	}
}
