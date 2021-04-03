package com.youngxpepp.instagramcloneserver.dao;

import static com.youngxpepp.instagramcloneserver.domain.QMemberOAuth2Info.*;

import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.domain.MemberOAuth2Info;
import com.youngxpepp.instagramcloneserver.domain.QMemberOAuth2Info;

@Repository
@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	@Transactional(readOnly = true)
	public Optional<Member> findByOAuth2NameAttributeAndOAuth2RegistrationId(
		String oauth2NameAttribute,
		String oauth2RegistrationId
	) {
		MemberOAuth2Info oAuth2Info = jpaQueryFactory.selectFrom(memberOAuth2Info)
			.innerJoin(memberOAuth2Info.member)
			.fetchJoin()
			.where(memberOAuth2Info.name.eq(oauth2NameAttribute))
			.where(memberOAuth2Info.registrationId.eq(oauth2RegistrationId))
			.fetchOne();

		if (oAuth2Info == null) {
			return Optional.empty();
		}

		return Optional.ofNullable(oAuth2Info.getMember());
	}
}
