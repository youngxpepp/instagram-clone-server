package com.youngxpepp.instagramcloneserver.domain.follow.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.youngxpepp.instagramcloneserver.domain.follow.model.QFollow;
import com.youngxpepp.instagramcloneserver.domain.member.model.QMember;

public class FollowCustomRepositoryImpl implements FollowCustomRepository {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	@Override
	public Long getFollowingCountByMemberNickname(String memberNickname) {
		QFollow follow = QFollow.follow;
		QMember member = QMember.member;

		return jpaQueryFactory
			.from(follow)
			.innerJoin(follow.fromMember, member)
			.where(member.nickname.eq(memberNickname))
			.fetchCount();
	}

	@Override
	public Long getFollowerCountByMemberNickname(String memberNickname) {
		QFollow follow = QFollow.follow;
		QMember member = QMember.member;

		return jpaQueryFactory
			.from(follow)
			.innerJoin(follow.toMember, member)
			.where(member.nickname.eq(memberNickname))
			.fetchCount();
	}
}
