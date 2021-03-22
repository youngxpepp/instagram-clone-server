package com.youngxpepp.instagramcloneserver.test;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.AfterEach;
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
@Import({QuerydslConfig.class, JpaConfig.class, JpaTestSupport.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Disabled
public class RepositoryTest {

	@Autowired
	protected JPAQueryFactory jpaQueryFactory;

	@Autowired
	protected JpaTestSupport jpaTestSupport;

	protected Member principal;

	@BeforeEach
	public void beforeEach() {
		principal = Member.builder()
			.name("geonhong.lee")
			.nickname("youngxpepp")
			.role(MemberRole.MEMBER)
			.build();
	}

	@AfterEach
	public void afterEach() {
		jpaTestSupport.deleteAllInAllTables();
	}
}
