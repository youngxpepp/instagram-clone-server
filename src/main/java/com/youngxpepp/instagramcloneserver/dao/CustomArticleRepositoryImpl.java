package com.youngxpepp.instagramcloneserver.dao;

import static com.youngxpepp.instagramcloneserver.domain.QArticle.*;

import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import com.youngxpepp.instagramcloneserver.domain.Article;

@RequiredArgsConstructor
public class CustomArticleRepositoryImpl implements CustomArticleRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<Article> findByIdWithCreatedBy(Long id) {
		Article one = jpaQueryFactory.selectFrom(article)
			.innerJoin(article.createdBy)
			.fetchJoin()
			.where(article.id.eq(id))
			.fetchOne();
		return Optional.ofNullable(one);
	}
}
