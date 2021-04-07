package com.youngxpepp.instagramcloneserver.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.youngxpepp.instagramcloneserver.dao.ArticleCreatedRepository;
import com.youngxpepp.instagramcloneserver.dao.FeedRepository;
import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.ArticleCreated;
import com.youngxpepp.instagramcloneserver.domain.Feed;
import com.youngxpepp.instagramcloneserver.domain.Follow;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;
import com.youngxpepp.instagramcloneserver.test.MockTest;

public class FeedServiceImplTest extends MockTest {

	@InjectMocks
	private FeedServiceImpl feedService;

	@Mock
	private ArticleCreatedRepository articleCreatedRepository;

	@Mock
	private FeedRepository feedRepository;

	@Test
	public void createFeedsThenThrowsBusinessException() {
		// given
		long givenId = 1;
		given(articleCreatedRepository.findByIdWithFollowers(givenId)).willThrow(
			new BusinessException(ErrorCode.ENTITY_NOT_FOUND)
		);

		// when
		Throwable throwable = catchThrowable(() -> {
			feedService.createFeeds(givenId);
		});

		// then
		and.then(throwable).isInstanceOf(BusinessException.class);
		and.then(((BusinessException)throwable).getErrorCode()).isEqualTo(ErrorCode.ENTITY_NOT_FOUND);
	}

	@Test
	public void createFeedsThenEarlyReturn() {
		// given
		Member member = Member.builder()
			.build();
		Article article = Article.builder()
			.createdBy(member)
			.build();
		ArticleCreated ac = ArticleCreated.builder()
			.article(article)
			.build();
		given(articleCreatedRepository.findByIdWithFollowers(anyLong()))
			.willReturn(Optional.of(ac));

		// when
		feedService.createFeeds(1);

		// then
		BDDMockito.then(feedRepository).should(times(0)).saveAll(anyIterable());
	}

	@Test
	public void createFeedsThenSuccess() {
		// given
		Member member = Member.builder()
			.build();

		for (int i = 0; i < 3; i++) {
			Member follower = Member.builder()
				.build();
			Follow follow = Follow.builder()
				.followingMember(follower)
				.followedMember(member)
				.build();
			List<Follow> follows = member.getFollowers();
			follows.add(follow);
		}

		Article article = Article.builder()
			.createdBy(member)
			.build();
		ArticleCreated ac = ArticleCreated.builder()
			.article(article)
			.build();
		given(articleCreatedRepository.findByIdWithFollowers(anyLong()))
			.willReturn(Optional.of(ac));

		// when
		feedService.createFeeds(1);

		// then
		BDDMockito.then(feedRepository).should().saveAll(
			argThat(i -> {
				Iterator<Feed> feedIterator = i.iterator();
				int feedIteratorSize = 0;
				while (feedIterator.hasNext()) {
					feedIteratorSize++;
					feedIterator.next();
				}
				return feedIteratorSize == 3;
			})
		);
	}
}
