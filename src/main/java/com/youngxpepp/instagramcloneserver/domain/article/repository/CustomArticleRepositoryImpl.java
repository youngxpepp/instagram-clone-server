package com.youngxpepp.instagramcloneserver.domain.article.repository;

import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import com.youngxpepp.instagramcloneserver.domain.article.model.Article;
import com.youngxpepp.instagramcloneserver.domain.article.model.QArticle;
import com.youngxpepp.instagramcloneserver.domain.member.model.QMember;

@RequiredArgsConstructor
public class CustomArticleRepositoryImpl implements CustomArticleRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<Article> findByIdWithCreatedBy(Long id) {
		Article article = jpaQueryFactory.selectFrom(QArticle.article)
			.innerJoin(QArticle.article.createdBy, QMember.member)
			.fetchJoin()
			.where(QArticle.article.id.eq(id))
			.fetchOne();
		return Optional.ofNullable(article);
	}
}
