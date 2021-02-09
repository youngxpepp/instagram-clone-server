package com.youngxpepp.instagramcloneserver.domain.comment.repository;

import java.util.ArrayList;
import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import com.youngxpepp.instagramcloneserver.domain.comment.model.Comment;
import com.youngxpepp.instagramcloneserver.domain.comment.model.QComment;
import com.youngxpepp.instagramcloneserver.domain.member.model.QMember;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Comment> findAllByArticleId(Long articleId, Pageable pageable) {
		if (pageable == null) {
			return jpaQueryFactory.selectFrom(QComment.comment)
				.innerJoin(QComment.comment.createdBy)
				.fetchJoin()
				.leftJoin(QComment.comment.nestedComments)
				.fetchJoin()
				.where(QComment.comment.article.id.eq(articleId))
				.orderBy(QComment.comment.id.asc())
				.fetch();
		}

		List<Long> commentIds = jpaQueryFactory.select(QComment.comment.id)
			.from(QComment.comment)
			.where(QComment.comment.article.id.eq(articleId))
			.orderBy(QComment.comment.id.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		if (commentIds.isEmpty()) {
			return new ArrayList<>();
		}

		return jpaQueryFactory.selectFrom(QComment.comment)
			.innerJoin(QComment.comment.createdBy)
			.fetchJoin()
			.leftJoin(QComment.comment.nestedComments)
			.fetchJoin()
			.where(QComment.comment.id.in(commentIds))
			.orderBy(QComment.comment.id.asc())
			.fetch();
	}
}
