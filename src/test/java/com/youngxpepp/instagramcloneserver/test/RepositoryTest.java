package com.youngxpepp.instagramcloneserver.test;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.domain.MemberRole;
import com.youngxpepp.instagramcloneserver.global.config.JpaConfig;
import com.youngxpepp.instagramcloneserver.global.config.QuerydslConfig;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Disabled
@Import({QuerydslConfig.class, JpaConfig.class})
public class RepositoryTest {

	@Autowired
	protected JPAQueryFactory jpaQueryFactory;

	protected Member principal;

	@BeforeEach
	public void beforeEach() {
		principal = Member.builder()
			.name("geonhong.lee")
			.nickname("youngxpepp")
			.role(MemberRole.MEMBER)
			.build();
	}
}
