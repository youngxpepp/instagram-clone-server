package com.youngxpepp.instagramcloneserver.dao;

import static com.youngxpepp.instagramcloneserver.domain.QArticle.*;
import static com.youngxpepp.instagramcloneserver.domain.QMember.*;

import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.QMember;

@RequiredArgsConstructor
public class CustomArticleRepositoryImpl implements CustomArticleRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	@Transactional(readOnly = true)
	public Optional<Article> findByIdWithCreatedBy(long id) {
		Article one = jpaQueryFactory.selectFrom(article)
			.innerJoin(article.createdBy)
			.fetchJoin()
			.where(article.id.eq(id))
			.fetchOne();
		return Optional.ofNullable(one);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Article> findByIdWithFollowers(long id) {
		Article one = jpaQueryFactory.selectFrom(article)
			.innerJoin(article.createdBy, member)
			.fetchJoin()
			.leftJoin(member.followers)
			.fetchJoin()
			.where(article.id.eq(id))
			.fetchOne();
		return Optional.ofNullable(one);
	}
}
