package com.youngxpepp.instagramcloneserver.dao;

import static com.youngxpepp.instagramcloneserver.domain.QArticle.*;
import static com.youngxpepp.instagramcloneserver.domain.QArticleCreated.*;
import static com.youngxpepp.instagramcloneserver.domain.QMember.*;

import java.util.List;
import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.ArticleCreated;

@RequiredArgsConstructor
public class CustomArticleCreatedRepositoryImpl implements CustomArticleCreatedRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	@Transactional
	public long deleteAllInIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return 0;
		}

		return jpaQueryFactory.delete(articleCreated)
			.where(articleCreated.id.in(ids))
			.execute();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ArticleCreated> findByIdWithFollowers(long id) {
		ArticleCreated one = jpaQueryFactory.selectFrom(articleCreated)
			.innerJoin(articleCreated.article, article)
			.fetchJoin()
			.innerJoin(article.createdBy, member)
			.fetchJoin()
			.leftJoin(member.followers)
			.fetchJoin()
			.where(articleCreated.id.eq(id))
			.fetchOne();

		return Optional.ofNullable(one);
	}
}
